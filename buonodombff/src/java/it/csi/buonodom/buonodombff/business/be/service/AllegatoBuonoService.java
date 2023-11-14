/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.business.be.service.util.FilesEncrypt;
import it.csi.buonodom.buonodombff.dto.ModelAllegato;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelGetAllegato;
import it.csi.buonodom.buonodombff.dto.custom.ResponseGetAllegato;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.integration.dao.custom.AllegatoBuonoDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodombff.integration.service.LogAuditService;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class AllegatoBuonoService extends BaseService {

	@Autowired
	LogAuditService logaudit;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	private FilesEncrypt filesEncrypt;

	@Autowired
	AllegatoBuonoDao allegatoBuonoDao;

	@Autowired
	CodParametroDao parametroDao;

	public Response getAllegatoBuono(Integer idAllegato, String xForwardedFor, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[2];
		try {
			// Validazione della richiesta
			List<ErroreDettaglioExt> listError = validateGeneric.validateAllegato(xForwardedFor,
					shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						/*
						 * Error 400, la richiesta fatta dal client è errata.
						 */
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			ResponseGetAllegato allegatoContratto = new ResponseGetAllegato();
			// Ottengo l'allegato
			allegatoContratto = getAllegatoBuonoFileSystem(idAllegato);
			if (allegatoContratto == null) { // Allegato non trovato
				param[0] = String.valueOf(idAllegato);
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR11.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"allegato inesistente");
			}

			return Response.status(200).entity(allegatoContratto.getAllegato())
					.header("Content-Disposition", "attachment; filename=\"" + allegatoContratto.getFileName() + "\"")
					.build();

		} catch (DatabaseException e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			e.printStackTrace();
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore generico ", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	private ResponseGetAllegato getAllegatoBuonoFileSystem(Integer idAllegato) throws DatabaseException, IOException {
		/*
		 * Ottengo il record dell'allegato in bdom_t_buono_allegato
		 */
		ModelGetAllegato allegato = allegatoBuonoDao.selectAllegatoBuono(idAllegato);
		if (allegato == null) {
			return null;
		}
		ResponseGetAllegato getAllegato = new ResponseGetAllegato();
		String os = System.getProperty("os.name");
		boolean locale = false;
		File file = null;
		if (os.toLowerCase().contains("win")) {
			locale = true;
		}
		if (!locale) // Linux
			file = new File(allegato.getFilePath() + "/" + allegato.getFileName());
		else // Windows
			file = new File(allegato.getFilePath() + "\\" + allegato.getFileName());

		byte[] bytes = Files.readAllBytes(file.toPath());

		byte[] allegatoDecifrato = filesEncrypt.creaFileDeCifratoByte(Cipher.DECRYPT_MODE, bytes);
		getAllegato.setAllegato(allegatoDecifrato);
		getAllegato.setFileName(allegato.getFileName());
		logInfo("getAllegatoBuonoFileSystem: ", "file decifrato e recuperato, nome_file: " + allegato.getFileName());
		return getAllegato;
	}

	public Response addAllegatoContratto(String tipoAllegato, String numeroDomanda, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale, String xFilenameOriginale,
			String contentType, byte[] allegato, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		try {
			// Validazione della richiesta
			List<ErroreDettaglioExt> listError = validateGeneric.validatePostAllegati(xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, allegato,
					xFilenameOriginale);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			String richiedenteCf = allegatoBuonoDao.selectCfRichiedenteNumeroDomanda(numeroDomanda);
			ModelAllegato archiviadomanda = new ModelAllegato();
			if (richiedenteCf != null) {
				/*
				 * Controllo per error 403, l'utente non ha i permessi necessari per
				 * visualizzare queste risorse.
				 */
				if (!richiedenteCf.equals(shibIdentitaCodiceFiscale)) {
					param[0] = tipoAllegato;
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR12.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
							"Il codice fiscale del richiedente della domanda non corrisponde");
				}
				// Archivio l'allegato
				archiviadomanda = archiviaAllegato(allegato, richiedenteCf, tipoAllegato, numeroDomanda, contentType,
						xFilenameOriginale.replaceAll("\\s+", ""), shibIdentitaCodiceFiscale);
			} else {
				param[0] = numeroDomanda;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"buono inesistente");
			}

			if (archiviadomanda != null) {
				return Response.status(200).entity(archiviadomanda).build();
			} else {
				param[0] = tipoAllegato;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR16.getCode(), param));
				logError("allegato non salvato nel filesystem per errori ", tipoAllegato);
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
						"allegato non salvato");
			}

		} catch (DatabaseException e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore riguardante database:", e);
			logError(metodo, "Chiave duplicata, nomeFile o idFile gia' esistenti");
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore generico ", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	/*
	 * Da sviluppare in base a dove si vuole archiviare l'allegato
	 */
	private ModelAllegato archiviaAllegato(byte[] allegato, String cfRichiedente, String tipoAllegato,
			String numeroDomanda, String contentType, String XFilenameOriginale, String shibIdentitaCodiceFiscale)
			throws DatabaseException, IOException, DuplicateKeyException, ResponseErrorException {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		ErrorBuilder error = null;
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[2];

		String path = parametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO);
		ModelAllegato allegatoResponse = new ModelAllegato();
		Long allegatoId = null;
		// LOCALE (assicurarsi di avere la cartella condivisa tmp il locale)
		String os = System.getProperty("os.name");
		boolean locale = false;
		if (os.toLowerCase().contains("win")) {
			path = "c:\\temp\\";
			locale = true;
		} else
			path += "/";
		/*
		 * Genero/navigo il PATH per l'inserimento dell'allegato
		 * {sportello}/{lettera}/{numero_domanda}/{buono_cod}/{
		 * tipologia_allegato_idallegato}
		 */
		// {sportello}
		String sportello = allegatoBuonoDao.selectSportelloCodFromNumeroDomanda(numeroDomanda);
		path += sportello;
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// /{lettera}
		char primaLetteraRaw = cfRichiedente.charAt(0); // Prima lettere del cfRichiedente
		String primaLettera = Character.toString(primaLetteraRaw).toUpperCase();
		if (!locale) { // Linux
			path += "/" + primaLettera;
		} else { // Windows
			path += "\\" + primaLettera;
		}
		dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// /{numero_domanda}
		if (!locale)
			path += "/" + numeroDomanda.toUpperCase();
		else
			path += "\\" + numeroDomanda.toUpperCase();
		dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// /{buono_cod}
		String buonoCod = allegatoBuonoDao.selectBuonoCodFromNumeroDomanda(numeroDomanda);
		if (buonoCod == null) {
			return null; // Buono non in stato attivo da fare
		}
		if (!locale)
			path += "/" + buonoCod.toUpperCase();
		else
			path += "\\" + buonoCod.toUpperCase();
		dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// /{tipologia_allegato
		if (!locale)
			path += "/" + tipoAllegato;
		else
			path += "\\" + tipoAllegato;

		String fileName = tipoAllegato + "_" + XFilenameOriginale;

		/*
		 * Controllo tipoAllegato passato da FE
		 */
		if (allegatoBuonoDao.checkEsistenzaTipoAllegato(tipoAllegato)) {
			allegatoId = allegatoBuonoDao.insertAllegato(fileName, contentType, path, tipoAllegato,
					shibIdentitaCodiceFiscale, shibIdentitaCodiceFiscale);
			path += "_" + allegatoId;
			allegatoBuonoDao.updateAllegato(fileName, contentType, path, tipoAllegato, shibIdentitaCodiceFiscale,
					allegatoId);
			logInfo("allegato non esiste " + tipoAllegato, path + "/" + XFilenameOriginale);
			logInfo("inserisco allegato su db " + tipoAllegato, path + "/" + XFilenameOriginale);
		} else {
			logError(metodo, "Errore riguardante database:", "allegatoTipo inesistente");
			param[0] = ": errore tipoAllegato";
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			throw new ResponseErrorException(
					ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
					"L'allegatoTipo passato non e' corretto");
		}

		// {tipologia_allegato_idallegato}
		dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// Genero il file da scrivere su file system
		File file = null;
		if (!locale) {
			file = new File(path + "/" + fileName);
		} else {
			file = new File(path + "\\" + fileName);
		}

		// cripto il file
		OutputStream out = new FileOutputStream(file);
		logInfo("cifro allegato ", tipoAllegato);
		byte[] alelgatoCifrato = filesEncrypt.creaFileCifratoByte(Cipher.ENCRYPT_MODE, allegato);
		if (alelgatoCifrato != null) {
			out.write(alelgatoCifrato, 0, alelgatoCifrato.length);
			out.close();
			logInfo("scrivo su su file system allegato cifrato ", tipoAllegato);
			allegatoResponse.setTipo(tipoAllegato);
			allegatoResponse.setFilename(fileName);
			allegatoResponse.setId(allegatoId);
			return allegatoResponse;
		} else {
			logInfo("errore in cifratura allegato non scritto su db ", tipoAllegato);
			ModelAllegato modelAllegatoReturn = null;
			return modelAllegatoReturn;
		}

	}

}

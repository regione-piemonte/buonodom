/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.business.be.service.util.FilesEncrypt;
import it.csi.buonodom.buonodombff.dto.ModelAllegato;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelGetAllegato;
import it.csi.buonodom.buonodombff.dto.custom.ResponseGetAllegato;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.integration.dao.custom.AllegatoContrattoDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodombff.integration.service.LogAuditService;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class AllegatoContrattoService extends BaseService {

	@Autowired
	LogAuditService logaudit;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	private FilesEncrypt filesEncrypt;

	@Autowired
	AllegatoContrattoDao allegatoContrattoDao;

	@Autowired
	CodParametroDao parametroDao;

	public Response getAllegatoContratto(String tipoAllegato, Integer idContratto, String xForwardedFor,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
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

			String richiedenteCf = allegatoContrattoDao.selectCfRichiedenteFromContratto(idContratto);
			ResponseGetAllegato allegatoContratto = new ResponseGetAllegato();
			if (richiedenteCf != null) {
				/*
				 * Controllo per error 403, l'utente non ha i permessi necessari per
				 * visualizzare queste risorse.
				 */
				if (!richiedenteCf.equals(shibIdentitaCodiceFiscale)) {
					param[0] = idContratto.toString();
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR12.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
							"Il codice fiscale del richiedente della domanda non corrisponde");
				}
				// Ottengo l'allegato
				allegatoContratto = getAllegatoFileSystem(idContratto, tipoAllegato);
				if (allegatoContratto == null) { // Allegato non trovato
					param[0] = tipoAllegato;
					param[1] = idContratto.toString();
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR11.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
							"allegato inesistente");
				}
			} else {
				param[0] = idContratto.toString();
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"buono inesistente");
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

	private ResponseGetAllegato getAllegatoFileSystem(Integer idContratto, String tipoAllegato)
			throws DatabaseException, IOException {
		/*
		 * Ottengo il record dell'allegato in bdom_t_buono_allegato
		 */
		ModelGetAllegato allegato = allegatoContrattoDao.selectAllegatoContratto(tipoAllegato, idContratto);
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

		return getAllegato;
	}

	public Response addAllegatoContratto(String tipoAllegato, Integer idContratto, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale, String xFilenameOriginale,
			String contentType, byte[] allegato, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErroreDettaglioExt errore = new ErroreDettaglioExt();
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

			String richiedenteCf = allegatoContrattoDao.selectCfRichiedenteFromContratto(idContratto);
			ModelAllegato archiviadomanda = new ModelAllegato();
			if (richiedenteCf != null) {
				/*
				 * Controllo per error 403, l'utente non ha i permessi necessari per
				 * visualizzare queste risorse.
				 */
				if (!richiedenteCf.equals(shibIdentitaCodiceFiscale)) {
					param[0] = idContratto.toString();
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR12.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
							"Il codice fiscale del richiedente della domanda non corrisponde");
				}
				// Archivio l'allegato
//				archiviadomanda = archiviaAllegato(allegato, shibIdentitaCodiceFiscale, richiesta.getDomandaDetCod(),
//						contentType, tipoAllegato, richiesta, xFilenameOriginale.replaceAll("\\s+", ""),
//						numeroRichiesta);
			} else {
				param[0] = idContratto.toString();
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"buono inesistente");
			}

			if (archiviadomanda != null) {
				return Response.status(200).entity(archiviadomanda).build();
			} else {
				param[0] = idContratto.toString();
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
//	private ModelAllegato archiviaAllegato(byte[] domanda, String cfRichiedente, String detCod, String contentType,
//			String tipoAllegato, ModelRichiestaExt richiesta, String XFilenameOriginale, String numeroRichiesta)
//			throws DatabaseException, IOException {
//		String path = parametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
//				Constants.PARAMETRO_GENERICO);
////		ModelSportelloExt sportello = new ModelSportelloExt();
//		ModelAllegato allegato = new ModelAllegato();
//		// LOCALE (assicurarsi di avere la cartella condivisa tmp il locale)
//		String os = System.getProperty("os.name");
//		boolean locale = false;
//		if (os.toLowerCase().contains("win")) {
//			path = "c:\\temp\\";
//			locale = true;
//		} else
//			path += "/";
//		try {
////		 sportello=sportelliDao.selectSportelliCod(richiesta.getSportelloId());
//			path += sportello.getSportelloCod();
//			File dir = new File(path);
//			if (!dir.exists()) {
//				dir.mkdirs();
//			}
//			if (!locale) {	//Linux
//				path += "/" + richiesta.getRichiedente().getCf().substring(0, 1).toUpperCase();
//			} else {	//Windows
//				path += "\\" + richiesta.getRichiedente().getCf().substring(0, 1).toUpperCase();
//			}
//			dir = new File(path);
//			if (!dir.exists()) {
//				dir.mkdirs();
//			}
//
//			if (!locale)
//				path += "/" + numeroRichiesta.toUpperCase();
//			else
//				path += "\\" + numeroRichiesta.toUpperCase();
//
//			dir = new File(path + "/");
//			if (!dir.exists()) {
//				dir.mkdirs();
//				logInfo("creo cartella", dir.getPath());
//			}
//
//			if (!locale)
//				path += "/" + detCod.toUpperCase();
//			else
//				path += "\\" + detCod.toUpperCase();
//			dir = new File(path);
//			if (!dir.exists()) {
//				dir.mkdirs();
//			}
//			File file = null;
//			String fileName = null;
//			if (!locale) {
//				fileName = tipoAllegato + "_" + XFilenameOriginale;
//				file = new File(path + "/" + fileName);
//			} else {
//				fileName = tipoAllegato + "_" + XFilenameOriginale;
//				file = new File(path + "\\" + fileName);
//			}
//
//			if (allegatoDao.selectEsisteAllegato(tipoAllegato, richiesta.getSportelloId(), richiesta.getDomandaDetId(),
//					detCod)) {
//				ModelGetAllegato a = allegatoDao.selectAllegato(detCod, tipoAllegato, richiesta.getDomandaDetId());
//				logInfo("allegato gia esistente " + tipoAllegato, a.getFilePath() + "/" + a.getFileName());
//				File f = null;
//				if (!locale)
//					f = new File(a.getFilePath() + "/" + a.getFileName());
//				else
//					f = new File(a.getFilePath() + "\\" + a.getFileName());
//				f.delete();
//				logInfo("cancello allegato su file system ", a.getFilePath() + "/" + a.getFileName());
//				allegatoDao.updateAllegato(detCod, cfRichiedente, fileName, contentType, path, tipoAllegato);
//				logInfo("aggiorno allegato su db " + tipoAllegato, a.getFilePath() + "/" + a.getFileName());
//			} else {
//				logInfo("allegato non esiste " + tipoAllegato, path + "/" + fileName);
//				allegatoDao.insertAllegato(fileName, contentType, path, richiesta.getSportelloId(),
//						richiesta.getDomandaDetId(), detCod, tipoAllegato, cfRichiedente, cfRichiedente);
//				logInfo("inserisco allegato su db " + tipoAllegato, path + "/" + fileName);
//			}
//			// cripto il file
//			OutputStream out = new FileOutputStream(file);
//			logInfo("cifro allegato ", tipoAllegato);
//			byte[] domandaCifrata = filesEncrypt.creaFileCifratoByte(Cipher.ENCRYPT_MODE, domanda);
//			if (domandaCifrata != null) {
//				out.write(domandaCifrata, 0, domandaCifrata.length);
//				out.close();
//				logInfo("scrivo su su file system allegato cifrato ", tipoAllegato);
//				allegato.setTipo(tipoAllegato);
//				allegato.setFilename(fileName);
//				return allegato;
//			} else {
//				logInfo("errore in cifratura allegato non scritto su db ", tipoAllegato);
//				return null;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logError("Archiviazione allegati", "Errore archivia allegati", e);
//			return null;
//		}
//	}
}

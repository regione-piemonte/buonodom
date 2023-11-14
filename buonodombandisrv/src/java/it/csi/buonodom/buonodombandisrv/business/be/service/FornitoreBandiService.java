/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.business.be.service;

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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonodom.buonodombandisrv.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombandisrv.dto.DatiBuono;
import it.csi.buonodom.buonodombandisrv.dto.ModelAllegatoContratto;
import it.csi.buonodom.buonodombandisrv.dto.ModelBandiMessage;
import it.csi.buonodom.buonodombandisrv.dto.ModelRichiesta;
import it.csi.buonodom.buonodombandisrv.dto.ModelSetFornitore;
import it.csi.buonodom.buonodombandisrv.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombandisrv.exception.DatabaseException;
import it.csi.buonodom.buonodombandisrv.exception.ResponseErrorException;
import it.csi.buonodom.buonodombandisrv.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodombandisrv.integration.dao.custom.LogBandiDao;
import it.csi.buonodom.buonodombandisrv.integration.dao.custom.RendicontazioneDao;
import it.csi.buonodom.buonodombandisrv.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodombandisrv.integration.dao.custom.SportelliDao;
import it.csi.buonodom.buonodombandisrv.integration.service.util.FilesEncrypt;
import it.csi.buonodom.buonodombandisrv.util.Constants;
import it.csi.buonodom.buonodombandisrv.util.Converter;
import it.csi.buonodom.buonodombandisrv.util.ErrorBuilder;
import it.csi.buonodom.buonodombandisrv.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombandisrv.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombandisrv.util.rest.ResponseRest;
import it.csi.buonodom.buonodombandisrv.util.validator.impl.ValidateGenericImpl;

@Service
public class FornitoreBandiService extends BaseService {

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	CodParametroDao parametroDao;

	@Autowired
	SportelliDao sportelliDao;

	@Autowired
	LogBandiDao logBandiDao;

	@Autowired
	private FilesEncrypt filesEncrypt;

	@Autowired
	ServizioRestService restbase;

	@Autowired
	RendicontazioneDao rendicontazioneDao;

	public Response setFornitoreBandi(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ModelRichiesta richiesta = new ModelRichiesta();
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErroreDettaglioExt errore = new ErroreDettaglioExt();
		String[] param = new String[1];
		try {
			List<ErroreDettaglioExt> listError = validateGeneric.validateDomanda(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, numeroRichiesta, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			ResponseRest responserest = new ResponseRest();
			List<ModelBandiMessage> body = new ArrayList<ModelBandiMessage>();
			ObjectMapper mapper = new ObjectMapper();
			if (richiesta != null) {
				// verifico se il buono attivo allora faccio ma se creato o altro no
				DatiBuono buonoId = logBandiDao.selectBuono(richiesta.getSportelloId(), richiesta.getDomandaId(),
						Constants.ATTIVO);
				if (buonoId != null) {
					// genero il fornitore da comunicare
					List<String> fornitoreJson = generaJson(numeroRichiesta);
					if (fornitoreJson == null || fornitoreJson.size() == 0) {
						param[0] = "Errore nella denerazione del JSON del fornitore ";
						logError(metodo, "Errore Crea JSON del fornitore ", param[0]);
						listerrorservice
								.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
								"errore in crea JSON del fornitore");
					}
					// chiama il servizio post fornitore di bandi per quanti sono i fornitori nella
					// lista
					boolean erroreservizio = false;
					for (String json : fornitoreJson) {
						responserest = new ResponseRest();
						responserest = restbase.postSetFornitore(json, shibIdentitaCodiceFiscale, xRequestId,
								xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);
						if (responserest.getStatusCode() != 200 && responserest.getStatusCode() != 201) {
							param[0] = responserest.getJson();
							logError(metodo, "Errore set fornitore ", param[0]);
							logBandiDao.insertLogBandi(richiesta.getNumero(), buonoId.getBuonoId(), null, json, "KO",
									String.valueOf(responserest.getStatusCode()), "Errore set fornitore " + param[0],
									Constants.SET_FORNITORE);
							listerrorservice
									.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
							erroreservizio = true;
						} else {
							mapper = new ObjectMapper();
							ModelBandiMessage bodysin = new ModelBandiMessage();
							try {
								bodysin = mapper.readValue(responserest.getJson(), ModelBandiMessage.class);
							} catch (IOException e) {
								param[0] = responserest.getJson();
								logError(metodo, "Errore set fornitore mapper ", param[0]);
								logBandiDao.insertLogBandi(richiesta.getNumero(), buonoId.getBuonoId(), null, json,
										"KO", String.valueOf(responserest.getStatusCode()),
										"Errore set fornitore mapper " + param[0], Constants.SET_FORNITORE);
								e.printStackTrace();
							}
							// scrivo nella tabella di log di bandi
							if (bodysin.getEsitoServizio().equalsIgnoreCase(Constants.BANDI_OK)) {
								logBandiDao.insertLogBandi(richiesta.getNumero(), buonoId.getBuonoId(),
										bodysin.getUuid(), json, bodysin.getEsitoServizio(), null, null,
										Constants.SET_FORNITORE);
							} else {
								logBandiDao.insertLogBandi(richiesta.getNumero(), buonoId.getBuonoId(), null, json,
										bodysin.getEsitoServizio(), bodysin.getCodiceErrore(), bodysin.getMessaggio(),
										Constants.SET_FORNITORE);
							}
							body.add(bodysin);
						}
					}
					if (erroreservizio) {
						param[0] = responserest.getJson();
						logError(metodo, "Errore set fornitore almeno su una chiamata post ", param[0]);
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
								"Errore set fornitore almeno su una chiamata post");
					}
				} else {
					// errore non esiste il buonoid
					param[0] = "Errore non trovato o buono non in stato attivo";
					logError(metodo, "Errore Crea xml bandi ", param[0]);
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
							"Errore non trovato o buono  non in stato creato");
				}
			} else {
				param[0] = "Errore set fornitore Bandi domanda non trovata";
				logError(metodo, param[0]);
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"errore set fornitore bandi domanda non trovata");
			}

			return Response.ok().entity(body).build();
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

	public List<String> generaJson(String numeroDomanda) throws DatabaseException, Exception {

		List<String> fornitori = rendicontazioneDao.selectDocumentoSpesaFornitori(numeroDomanda, Constants.DA_INVIARE);
		ModelSetFornitore fornitore = new ModelSetFornitore();
		List<String> jsonFornitori = new ArrayList<String>();
		if (fornitori != null && fornitori.size() > 0) {
			for (String forn : fornitori) {
				// fornitore contratto nella lista
				String[] fornContr = forn.split(";");
				fornitore = rendicontazioneDao.selectSetFornitore(Converter.getInt(fornContr[0]),
						Converter.getInt(fornContr[1]));
				if (fornitore != null) {
					if (fornitore.getFiles() != null && fornitore.getFiles().size() > 0) {
						fornitore.setFiles(generaFile(fornitore.getFiles()));
					}
					fornitore.setNumeroDomanda(numeroDomanda);
					ObjectMapper mapper = new ObjectMapper();
					mapper.setSerializationInclusion(Include.NON_EMPTY);
					jsonFornitori.add(mapper.writeValueAsString(fornitore));
				}
			}
		}

		return jsonFornitori;
	}

	private List<ModelAllegatoContratto> generaFile(List<ModelAllegatoContratto> allegati) throws IOException {

		File file = null;
		for (int i = 0; i < allegati.size(); i++) {
			file = new File(allegati.get(i).getPath());
			byte[] bytes = Files.readAllBytes(file.toPath());
			byte[] allegatoDecifrato = filesEncrypt.creaFileDeCifratoByte(Cipher.DECRYPT_MODE, bytes);
			allegati.get(i).setFile(allegatoDecifrato);
			allegati.get(i).setPath(null);
		}
		return allegati;
	}

}

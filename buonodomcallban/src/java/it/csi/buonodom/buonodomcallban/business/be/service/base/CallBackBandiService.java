/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.business.be.service.base;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodomcallban.dto.DatiBuono;
import it.csi.buonodom.buonodomcallban.dto.DatiDichiarazione;
import it.csi.buonodom.buonodomcallban.dto.DatiDichiarazioneDocSpesa;
import it.csi.buonodom.buonodomcallban.dto.ModelDocumentoRicevuto;
import it.csi.buonodom.buonodomcallban.dto.PayloadEsitoAcquisizione;
import it.csi.buonodom.buonodomcallban.dto.PayloadEsitoAcquisizioneSpesa;
import it.csi.buonodom.buonodomcallban.dto.PayloadRevDomanda;
import it.csi.buonodom.buonodomcallban.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodomcallban.exception.DatabaseException;
import it.csi.buonodom.buonodomcallban.exception.ErrorBuilder;
import it.csi.buonodom.buonodomcallban.exception.ResponseErrorException;
import it.csi.buonodom.buonodomcallban.integration.dao.custom.LogBandiDao;
import it.csi.buonodom.buonodomcallban.util.Constants;
import it.csi.buonodom.buonodomcallban.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodomcallban.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodomcallban.util.rest.validator.ValidateGenericImpl;

@Service
public class CallBackBandiService extends BaseService {

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	LogBandiDao logBandiDao;

	public Response acquisizionePost(String xRequestId, String numeroDomanda,
			PayloadEsitoAcquisizione payloadEsitoAcquisizione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		String[] param = new String[1];
		ErrorBuilder error = null;
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		try {
			List<ErroreDettaglioExt> listError = validateGeneric.validateAcquisizione(xRequestId,
					payloadEsitoAcquisizione, securityContext, httpHeaders, httpRequest);
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			// recupero id bandi se esiste buono creato
			DatiBuono buonoId = logBandiDao.selectBuono(numeroDomanda);
			if (buonoId != null) {
				if (payloadEsitoAcquisizione.getEsitoAcquisizione().getEsito().equalsIgnoreCase(Constants.OK)) {
					logBandiDao.insertLogBandi(numeroDomanda, buonoId.getBuonoId(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getUuid(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getEsito(), null, null,
							"CALLBACK_INVIO_DOMANDA_POST", null, null);
					// devo aggiornare la tabella dei buoni con uuid
					logBandiDao.updateBuonoConUUID(numeroDomanda, buonoId.getBuonoId(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getUuid());
					// chiudo il record creato del buono e apro il record ATTIVO
					logBandiDao.chiudoStatoBuono(buonoId.getBuonoId());
					logBandiDao.aproRecordBuonoStatoAttivo(buonoId.getBuonoId(), buonoId.getIban(),
							buonoId.getIbanIntestatario());
				} else {
					logBandiDao.insertLogBandi(numeroDomanda, buonoId.getBuonoId(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getUuid(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getEsito(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getErrore().getCodice(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getErrore().getMessaggio(),
							"CALLBACK_INVIO_DOMANDA_POST", null, null);
				}
			} else {
				// errore
				logError(metodo, "Non trovato il buono della domanda ", null);
				param[0] = "Non trovato il buono della domanda";
				DatiBuono buonoIdEsistente = logBandiDao.selectBuono(numeroDomanda);
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				if (buonoIdEsistente != null)
					logBandiDao.insertLogBandi(numeroDomanda, buonoIdEsistente.getBuonoId(), null, "KO", "999",
							"ERRORE INVIO CALLBACK", "CALLBACK_INVIO_DOMANDA_POST", null,
							"Non trovato il buono della domanda " + payloadEsitoAcquisizione.toString());
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
						"errore buono non trovato");
			}
			return Response.status(201).entity("OK").build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			e.printStackTrace();
			logError(metodo, "Errore generico ", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;

	}

	public Response acquisizionePut(String xRequestId, String numeroDomanda,
			PayloadEsitoAcquisizione payloadEsitoAcquisizione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		String[] param = new String[1];
		ErrorBuilder error = null;
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		try {
			List<ErroreDettaglioExt> listError = validateGeneric.validateAcquisizione(xRequestId,
					payloadEsitoAcquisizione, securityContext, httpHeaders, httpRequest);
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			// recupero id bandi se esiste buono creato
			DatiBuono buonoId = logBandiDao.selectBuono(numeroDomanda);
			if (buonoId != null) {
				if (payloadEsitoAcquisizione.getEsitoAcquisizione().getEsito().equalsIgnoreCase(Constants.OK)) {
					logBandiDao.insertLogBandi(numeroDomanda, buonoId.getBuonoId(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getUuid(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getEsito(), null, null,
							"CALLBACK_INVIO_DOMANDA_PUT", null, null);
					// devo aggiornare la tabella dei buoni con uuid
					logBandiDao.updateBuonoConUUID(numeroDomanda, buonoId.getBuonoId(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getUuid());
					// chiudo il record creato del buono e apro il record ATTIVO
					logBandiDao.chiudoStatoBuono(buonoId.getBuonoId());
					logBandiDao.aproRecordBuonoStatoAttivo(buonoId.getBuonoId(), buonoId.getIban(),
							buonoId.getIbanIntestatario());
				} else {
					logBandiDao.insertLogBandi(numeroDomanda, buonoId.getBuonoId(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getUuid(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getEsito(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getErrore().getCodice(),
							payloadEsitoAcquisizione.getEsitoAcquisizione().getErrore().getMessaggio(),
							"CALLBACK_INVIO_DOMANDA_PUT", null, null);
				}
			} else {
				// errore
				logError(metodo, "Non trovato il buono della domanda ", null);
				param[0] = "Non trovato il buono della domanda";
				// prendi il buono id in qualunque stato sia
				DatiBuono buonoIdEsistente = logBandiDao.selectBuono(numeroDomanda);
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				if (buonoIdEsistente != null)
					logBandiDao.insertLogBandi(numeroDomanda, buonoIdEsistente.getBuonoId(), null, "KO", "999",
							"ERRORE INVIO CALLBACK", "CALLBACK_INVIO_DOMANDA_PUT", null,
							"Non trovato il buono della domanda " + payloadEsitoAcquisizione.toString());
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
						"errore buono non trovato");
			}
			return Response.status(201).entity("OK").build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			e.printStackTrace();
			logError(metodo, "Errore generico ", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;

	}

	public Response revocaPut(String xRequestId, String numeroDomanda, PayloadRevDomanda payloadRevDomanda,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		String[] param = new String[1];
		ErrorBuilder error = null;
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
//		try {
//			List<ErroreDettaglioExt> listError = validateGeneric.validateAcquisizione(xRequestId,payloadEsitoAcquisizione,securityContext, httpHeaders,
//					httpRequest);	
//			if (!listError.isEmpty()) {
//				throw new ResponseErrorException(
//				ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
//				"errore in validate");
//				}
//			//recupero id bandi
//			BigDecimal buonoId = logBandiDao.selectBuono(payloadEsitoAcquisizione.getEsitoAcquisizione().getUuid());
//			if (buonoId!=null) {
//			if (payloadEsitoAcquisizione.getEsitoAcquisizione().getEsito().equalsIgnoreCase(Constants.OK)){
//				logBandiDao.insertLogBandi(numeroDomanda, buonoId, payloadEsitoAcquisizione.getEsitoAcquisizione().getUuid(),
//						payloadEsitoAcquisizione.getEsitoAcquisizione().getEsito(),null,null);
//				//devo aggiornare la tabella dei buoni con uuid
//				logBandiDao.updateBuonoConUUID(numeroDomanda,buonoId);
//			}
//			else {
//				logBandiDao.insertLogBandi(numeroDomanda, buonoId, null,
//						payloadEsitoAcquisizione.getEsitoAcquisizione().getEsito(),payloadEsitoAcquisizione.getEsitoAcquisizione().getErrore().getCodice(),
//						payloadEsitoAcquisizione.getEsitoAcquisizione().getErrore().getMessaggio());
//			}
//			}
//				else {
//					//errore
//					logError(metodo, "Non trovato il buono della domanda ", null);
//					param[0] = "Non trovato il buono della domanda";
//					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(),param));
//					error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
//				}
//			return Response.status(201).entity("OK").build();
//		}
//		catch (DatabaseException e) {
//			e.printStackTrace();
//			logError(metodo, "Errore riguardante database:", e);
//			param[0] = e.getMessage();
//			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(),param));
//			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
//		} catch (ResponseErrorException e) {
//			logError(metodo, "Errore generico response:", e);
//			error = e.getResponseError();
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			logError(metodo, "Errore generico ", e);
//			param[0] = e.getMessage();
//			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(),param));
//			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
//		}
//		Response esito = error.generateResponseError();
//		return esito;
//		
		return null;
	}

	public Response acquisizioneDichiarazionePost(String xRequestId, String idDichiarazionespesaBuonodom,
			PayloadEsitoAcquisizioneSpesa payloadEsitoAcquisizioneSpesa, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		String[] param = new String[1];
		ErrorBuilder error = null;
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		try {
			List<ErroreDettaglioExt> listError = validateGeneric.validateRendicontazioneAcquisizione(xRequestId,
					payloadEsitoAcquisizioneSpesa, securityContext, httpHeaders, httpRequest);
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			// verifico se esiste dichiarazione spesa
			DatiDichiarazione rendicontazione = new DatiDichiarazione();
			rendicontazione = logBandiDao.selectDatiRendicontazioneDocSpesa(idDichiarazionespesaBuonodom,
					Constants.CARICATA, Constants.DA_INVIARE);
			if (rendicontazione != null) {
				if (payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getEsito().equalsIgnoreCase(Constants.OK)) {
					List<ModelDocumentoRicevuto> listainiziale = new ArrayList<ModelDocumentoRicevuto>();
					listainiziale.addAll(payloadEsitoAcquisizioneSpesa.getDocumenti());
					// verifico che i documenti di spesa siano identici nei due array
					if (confrontaArray(rendicontazione.getDocSpesa(), payloadEsitoAcquisizioneSpesa.getDocumenti())) {
						// esito positivo mi aspetto di avere i dati da inserire per i vari documenti di
						// spesa
						logBandiDao.insertLogBandi(rendicontazione.getDomandaNumero(), rendicontazione.getBuonoId(),
								payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getUuid(),
								payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getEsito(), null, null,
								"CALLBACK_INVIO_RENDICONTAZIONE_POST", idDichiarazionespesaBuonodom, null);
						// devo aggiornare la tabella della dichiarazione spesa con uuid
						logBandiDao.updateDichiarazioneSpesaConUUID(idDichiarazionespesaBuonodom,
								payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getUuid(),
								payloadEsitoAcquisizioneSpesa.getIdDichiarazioneSpesaBandi());
						// chiudo il record creato della dichiarazione da caricata ad inviata
						logBandiDao.chiudoStatoDichiarazioneCaricata(rendicontazione.getDicSpesaId());
						logBandiDao.aproRecordDichiarazioneInviata(rendicontazione.getDicSpesaId());
						// della spesa da da_inviare a da_validare
						for (DatiDichiarazioneDocSpesa docSpesa : rendicontazione.getDocSpesa()) {
							List<ModelDocumentoRicevuto> singolo = listainiziale.stream()
									.filter(p -> p.getIdDocumentoBuonodom().equalsIgnoreCase(docSpesa.getDocSpesaCod()))
									.collect(Collectors.toList());
							logBandiDao.updateDocumentoSpesa(docSpesa.getDocSpesaId(),
									singolo.get(0).getIdDocumentoBandi());
							logBandiDao.chiudoStatoDocSpesaDaInviare(docSpesa.getDocSpesaId());
							logBandiDao.aproRecordDocSpesaDaValidare(docSpesa.getDocSpesaId());
						}
					} else {
						logError(metodo, "I documenti di spesa della callback non sono gli stessi della dichiarazione "
								+ idDichiarazionespesaBuonodom, null);
						param[0] = "I documenti di spesa della callback non sono gli stessi della dichiarazione "
								+ idDichiarazionespesaBuonodom;
						listerrorservice
								.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
						logBandiDao.insertLogBandi(rendicontazione.getDomandaNumero(), rendicontazione.getBuonoId(),
								null, "KO", "999", "ERRORE INVIO CALLBACK", "CALLBACK_INVIO_RENDICONTAZIONE_POST",
								idDichiarazionespesaBuonodom,
								"I documenti di spesa della callback non sono gli stessi della dichiarazione "
										+ payloadEsitoAcquisizioneSpesa.toString());
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
								"errore i documenti di spesa della callback non sono gli stessi della dichiarazione "
										+ idDichiarazionespesaBuonodom);
					}
				} else {
					// esito negativo non faccio nulla e scrivo solo nella logbandi
					logBandiDao.insertLogBandi(rendicontazione.getDomandaNumero(), rendicontazione.getBuonoId(),
							payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getUuid(),
							payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getEsito(),
							payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getErrore().getCodice(),
							payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getErrore().getMessaggio(),
							"CALLBACK_INVIO_RENDICONTAZIONE_POST", idDichiarazionespesaBuonodom, null);
				}
			} else {
				// prendo il numero domanda e idbuono
				rendicontazione = logBandiDao.selectBuonoDomanda(idDichiarazionespesaBuonodom);
				// errore
				logError(metodo,
						"Non trovato alcun documento di spesa per la dichiarazione o gli stati non sono coerenti "
								+ idDichiarazionespesaBuonodom,
						null);
				param[0] = "Non trovato alcun documento di spesa per la dichiarazione  o gli stati non sono coerenti "
						+ idDichiarazionespesaBuonodom;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				if (rendicontazione != null && rendicontazione.getDomandaNumero() != null
						&& rendicontazione.getBuonoId() != null)
					logBandiDao.insertLogBandi(rendicontazione.getDomandaNumero(), rendicontazione.getBuonoId(), null,
							"KO", "999", "ERRORE INVIO CALLBACK", "CALLBACK_INVIO_RENDICONTAZIONE_POST",
							idDichiarazionespesaBuonodom,
							"Non trovato alcun documento di spesa per la dichiarazione o gli stati non sono coerenti "
									+ payloadEsitoAcquisizioneSpesa.toString());
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
						"errore dichiarazione di spesa errata o gli stati non sono coerenti "
								+ idDichiarazionespesaBuonodom);
			}
			return Response.status(201).entity("OK").build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			e.printStackTrace();
			logError(metodo, "Errore generico ", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;

	}

	public Response acquisizioneDichiarazionePut(String xRequestId, String idDichiarazionespesaBuonodom,
			PayloadEsitoAcquisizioneSpesa payloadEsitoAcquisizioneSpesa, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		String[] param = new String[1];
		ErrorBuilder error = null;
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		try {
			List<ErroreDettaglioExt> listError = validateGeneric.validateRendicontazioneAcquisizione(xRequestId,
					payloadEsitoAcquisizioneSpesa, securityContext, httpHeaders, httpRequest);
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			// verifico se esiste dichiarazione spesa
			DatiDichiarazione rendicontazione = new DatiDichiarazione();
			rendicontazione = logBandiDao.selectDatiRendicontazioneDocSpesa(idDichiarazionespesaBuonodom,
					Constants.CARICATA, Constants.DA_INVIARE);
			if (rendicontazione != null) {
				if (payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getEsito().equalsIgnoreCase(Constants.OK)) {
					List<ModelDocumentoRicevuto> listainiziale = new ArrayList<ModelDocumentoRicevuto>();
					listainiziale.addAll(payloadEsitoAcquisizioneSpesa.getDocumenti());
					// verifico che i documenti di spesa siano identici nei due array
					if (confrontaArray(rendicontazione.getDocSpesa(), payloadEsitoAcquisizioneSpesa.getDocumenti())) {
						// esito positivo mi aspetto di avere i dati da inserire per i vari documenti di
						// spesa
						logBandiDao.insertLogBandi(rendicontazione.getDomandaNumero(), rendicontazione.getBuonoId(),
								payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getUuid(),
								payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getEsito(), null, null,
								"CALLBACK_INVIO_RENDICONTAZIONE_PUTT", idDichiarazionespesaBuonodom, null);
						// devo aggiornare la tabella della dichiarazione spesa con uuid
						logBandiDao.updateDichiarazioneSpesaConUUID(idDichiarazionespesaBuonodom,
								payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getUuid(),
								payloadEsitoAcquisizioneSpesa.getIdDichiarazioneSpesaBandi());
						// chiudo il record creato della dichiarazione da caricata ad inviata
						logBandiDao.chiudoStatoDichiarazioneCaricata(rendicontazione.getDicSpesaId());
						logBandiDao.aproRecordDichiarazioneInviata(rendicontazione.getDicSpesaId());
						// della spesa da da_inviare a da_validare
						for (DatiDichiarazioneDocSpesa docSpesa : rendicontazione.getDocSpesa()) {
							List<ModelDocumentoRicevuto> singolo = listainiziale.stream()
									.filter(p -> p.getIdDocumentoBuonodom().equalsIgnoreCase(docSpesa.getDocSpesaCod()))
									.collect(Collectors.toList());
							logBandiDao.updateDocumentoSpesa(docSpesa.getDocSpesaId(),
									singolo.get(0).getIdDocumentoBandi());
							logBandiDao.chiudoStatoDocSpesaDaInviare(docSpesa.getDocSpesaId());
							logBandiDao.aproRecordDocSpesaDaValidare(docSpesa.getDocSpesaId());
						}
					} else {
						logError(metodo, "I documenti di spesa della callback non sono gli stessi della dichiarazione "
								+ idDichiarazionespesaBuonodom, null);
						param[0] = "I documenti di spesa della callback non sono gli stessi della dichiarazione "
								+ idDichiarazionespesaBuonodom;
						listerrorservice
								.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
						logBandiDao.insertLogBandi(rendicontazione.getDomandaNumero(), rendicontazione.getBuonoId(),
								null, "KO", "999", "ERRORE INVIO CALLBACK", "CALLBACK_INVIO_RENDICONTAZIONE_PUT",
								idDichiarazionespesaBuonodom,
								"I documenti di spesa della callback non sono gli stessi della dichiarazione "
										+ payloadEsitoAcquisizioneSpesa.toString());
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
								"errore i documenti di spesa della callback non sono gli stessi della dichiarazione "
										+ idDichiarazionespesaBuonodom);
					}
				} else {
					// esito negativo non faccio nulla e scrivo solo nella logbandi
					logBandiDao.insertLogBandi(rendicontazione.getDomandaNumero(), rendicontazione.getBuonoId(),
							payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getUuid(),
							payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getEsito(),
							payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getErrore().getCodice(),
							payloadEsitoAcquisizioneSpesa.getEsitoAcquisizione().getErrore().getMessaggio(),
							"CALLBACK_INVIO_RENDICONTAZIONE_PUT", idDichiarazionespesaBuonodom, null);
				}
			} else {
				// prendo il numero domanda e idbuono
				rendicontazione = logBandiDao.selectBuonoDomanda(idDichiarazionespesaBuonodom);
				// errore
				logError(metodo,
						"Non trovato alcun documento di spesa per la dichiarazione o gli stati non sono coerenti "
								+ idDichiarazionespesaBuonodom,
						null);
				param[0] = "Non trovato alcun documento di spesa per la dichiarazione  o gli stati non sono coerenti "
						+ idDichiarazionespesaBuonodom;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				if (rendicontazione != null && rendicontazione.getDomandaNumero() != null
						&& rendicontazione.getBuonoId() != null)
					logBandiDao.insertLogBandi(rendicontazione.getDomandaNumero(), rendicontazione.getBuonoId(), null,
							"KO", "999", "ERRORE INVIO CALLBACK", "CALLBACK_INVIO_RENDICONTAZIONE_PUT",
							idDichiarazionespesaBuonodom,
							"Non trovato alcun documento di spesa per la dichiarazione o gli stati non sono coerenti "
									+ payloadEsitoAcquisizioneSpesa.toString());
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
						"errore dichiarazione di spesa errata o gli stati non sono coerenti "
								+ idDichiarazionespesaBuonodom);
			}
			return Response.status(201).entity("OK").build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			e.printStackTrace();
			logError(metodo, "Errore generico ", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;

	}

	public static boolean confrontaArray(List<DatiDichiarazioneDocSpesa> dichiarazionedocSpesa,
			List<ModelDocumentoRicevuto> docRicevuto) {
		if (dichiarazionedocSpesa.size() != docRicevuto.size())
			return false;
		for (DatiDichiarazioneDocSpesa docSpesa : dichiarazionedocSpesa) {
			for (ModelDocumentoRicevuto docBandi : docRicevuto)
				if (docSpesa.getDocSpesaCod().equalsIgnoreCase(docBandi.getIdDocumentoBuonodom())) {
					docRicevuto.remove(docBandi);
					break;
				}
		}
		if (docRicevuto.size() == 0)
			return true;
		else
			return false;
	}
}

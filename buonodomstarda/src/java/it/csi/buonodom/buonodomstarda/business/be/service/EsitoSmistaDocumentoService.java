/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomstarda.business.be.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodomstarda.business.be.service.base.BaseService;
import it.csi.buonodom.buonodomstarda.dto.EsitoSmistaDocumentoRequest;
import it.csi.buonodom.buonodomstarda.dto.EsitoStep;
import it.csi.buonodom.buonodomstarda.dto.InformazioneType;
import it.csi.buonodom.buonodomstarda.dto.ResultType;
import it.csi.buonodom.buonodomstarda.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodomstarda.dto.custom.StardasLog;
import it.csi.buonodom.buonodomstarda.exception.DatabaseException;
import it.csi.buonodom.buonodomstarda.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodomstarda.integration.dao.custom.StardasLogDao;
import it.csi.buonodom.buonodomstarda.util.Constants;
import it.csi.buonodom.buonodomstarda.util.Util;
import it.csi.buonodom.buonodomstarda.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodomstarda.util.validator.impl.ValidateGenericImpl;

@Service
public class EsitoSmistaDocumentoService extends BaseService {

	@Autowired
	StardasLogDao logStardas;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	RichiesteDao richiesteDao;

	/*
	 * Nel payload in allegato ci sono i 2 possibili esiti step con due nomi diversi
	 * --> i 2 possibili nomi step sono: - ARCHIVIAZIONE_ACTA - PROTOCOLLAZIONE_ACTA
	 * sono stringhe costanti quindi dovremo testare se sono o meno presenti e
	 * verificarne il codice esito sulla struttura esitoStep corrispondente. La
	 * struttura esitoTrattamento ha come codice esito il risultato dei due codici
	 * esito degli step, vince sempre l'esito negativo (ovvero se è negativo uno
	 * dei due step lo sarà pure l'esito trattamento). La chiamata alla callback
	 * potrà essere effettuata sia con un esitoStep solo o anche con entrambi; nel
	 * caso in cui ci sarà un solo esitoStep stardass avrà eseguito solo il
	 * trattamento dell'archiviazione su acta e non la protocollazione, quindi in
	 * questo caso richiamerà la callback una seconda volta per lo stesso documento
	 * usando in questo caso il servizio di put. Nel nostro caso, in cui abbiamo un
	 * documento principale e altri allegati, potremmo avere uno scenario simile:
	 * 
	 * 1 chiamata callback in post per la domanda (con esitoStep archiviazione_acta)
	 * 2 chiamata callback in post per allegato della domanda (con esitoStep
	 * archiviazione_acta) 3 chiamata callback in post per allegato della domanda
	 * (con esitoStep archiviazione_acta) 4 chiamata callback in put per la domanda
	 * (con esitoStep protocollazione_acta)
	 * 
	 * Potrebbe capitare anche di ricevere una callback che con un esito step non
	 * positivo e in quel caso stardass eseguirà un'altra callback (anche in questo
	 * caso viene chiamata una put).
	 * 
	 * I dati del protocollo sono valorizzati nella sezione del payload
	 * informazioniAggiuntive e hanno il nome (come da allegato di esempio):
	 * Numero_reg_protocollo Tipo_reg_protocollo Data_reg_protocollo
	 * 
	 * ma sono valorizzati solo nel caso in cui ci sia l'esitoStep
	 * PROTOCOLLAZIONE_ACTA.
	 * 
	 * Il campo messageId è valorizzato con l'uuid che ci viene fornito alla
	 * chiamata che eseguiamo noi alla smistadocumento. Il campo idDocumentoFruitore
	 * avrà invece il campo idDocumentoFruitore che abbiamo passato noi alla
	 * request.
	 * 
	 */
	public Response executePut(EsitoSmistaDocumentoRequest body, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		// prendo esito dal body per poter mettere numero protocollo ed esito poi creo
		// tabella per log
		ResultType esitocallback = new ResultType();
		EsitoStep esitostep = new EsitoStep();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		String[] param = new String[1];
		try {
			List<ErroreDettaglioExt> listError = validateGeneric.validateSmista(body, securityContext, httpHeaders,
					httpRequest);
			if (!listError.isEmpty()) {
				param[0] = metodo + " " + listError.toString();
				esitocallback.setCodice(Constants.KO_APPLICATIVI);
				esitocallback.setMessaggio(
						validateGeneric.getValueGenericErrorStardas(CodeErrorEnum.ERR17.getCode(), param));
				// esitostep.setNome("esito");
				esitostep.setEsito(esitocallback);
				return Response.ok().entity(esitostep).build();
			}
			// verifica se otteniamo protocollo ed archiviazione
			StardasLog log = new StardasLog();
			boolean esitook = false;
			boolean protocolloEsiste = false;
			String protocollo = null;
			String idDocumentoFruitore = null;
			String dataprotocollo = null;
			String tipoprotocollo = null;
			String uuid = null;
			String esitokocallback = null;
			if (body.getEsitoSmistaDocumento() != null) {
				if (body.getEsitoSmistaDocumento().getEsito() != null) {
					log.setMessageUUID(body.getEsitoSmistaDocumento().getEsito().getMessageUUID());
					uuid = body.getEsitoSmistaDocumento().getEsito().getMessageUUID();
					log.setIdDocumentoFruitore(body.getEsitoSmistaDocumento().getEsito().getIdDocumentoFruitore());
					idDocumentoFruitore = body.getEsitoSmistaDocumento().getEsito().getIdDocumentoFruitore();
					log.setTipoTrattamento(body.getEsitoSmistaDocumento().getEsito().getTipoTrattamento());
					if (body.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento() != null) {
						log.setEsitoTrattamento(
								body.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getCodice() + " " + body
										.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getMessaggio());
						// verifico se codice 000 ossia positivo
						if (body.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getCodice() != null) {
							if (body.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getCodice()
									.equalsIgnoreCase(Constants.OK)
									|| body.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getCodice()
											.equalsIgnoreCase(Constants.OK_PARZIALE)) {
								esitook = true;
							} else {
								esitokocallback = body.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento()
										.getMessaggio();
							}
						}
					}
					if (body.getEsitoSmistaDocumento().getEsito().getEsitiStep() != null) {
						if (body.getEsitoSmistaDocumento().getEsito().getEsitiStep().getEsitoStep() != null) {
							List<EsitoStep> esitiStep = new ArrayList<EsitoStep>();
							for (EsitoStep esito : body.getEsitoSmistaDocumento().getEsito().getEsitiStep()
									.getEsitoStep()) {
								// esiste il protocollo con esito positvo
								if (esito.getNome().equalsIgnoreCase(Constants.PROTOCOLLAZIONE_ACTA)) {
									if (esito.getEsito().getCodice().equalsIgnoreCase(Constants.OK)
											|| esito.getEsito().getCodice().equalsIgnoreCase(Constants.OK_PARZIALE)) {
										protocolloEsiste = true;
									}
								}
								esitiStep.add(esito);
							}
							log.setEsitiStep(esitiStep.toString());
						}
					}
					// if (protocolloEsiste) {
					if (body.getEsitoSmistaDocumento().getEsito().getInformazioniAggiuntive() != null) {
						if (body.getEsitoSmistaDocumento().getEsito().getInformazioniAggiuntive()
								.getInformazione() != null) {
							List<InformazioneType> infoType = new ArrayList<InformazioneType>();
							for (InformazioneType info : body.getEsitoSmistaDocumento().getEsito()
									.getInformazioniAggiuntive().getInformazione()) {
								if (info.getNome().toUpperCase().equalsIgnoreCase(Constants.NUMERO_REG_PROTOCOLLO)) {
									// prelevo i dati del protocollo
									protocollo = info.getValore();
								}
								if (info.getNome().toUpperCase().equalsIgnoreCase(Constants.DATA_REG_PROTOCOLLO)) {
									// prelevo i dati del protocollo
									dataprotocollo = info.getValore();
								}
								if (info.getNome().toUpperCase().equalsIgnoreCase(Constants.TIPO_REG_PROTOCOLLO)) {
									// prelevo i dati del protocollo
									tipoprotocollo = info.getValore();
								}
								infoType.add(info);
							}
							log.setInformazioniAggiuntive(infoType.toString());
						}
					}
					// }
					// inserisci i valori
					logStardas.insertLogStardas(log);
				}
			}

			if (!richiesteDao.selectIdDocumentoFruitore(idDocumentoFruitore)) {
				param[0] = metodo + " " + "iddocumentofruitore non trovato";
				esitocallback.setCodice(Constants.KO_APPLICATIVI);
				esitocallback.setMessaggio(
						validateGeneric.getValueGenericErrorStardas(CodeErrorEnum.ERR17.getCode(), param));
				// esitostep.setNome("esito");
				esitostep.setEsito(esitocallback);
				return Response.ok().entity(esitostep).build();
			}
//		if (!esitook) {
//			esitocallback.setCodice(Constants.OK);
//			esitocallback.setMessaggio(validateGeneric.getValueGenericSuccess(Constants.OK_DESCRIZIONE, null));
//			esitostep.setEsito(esitocallback);
//			return Response.ok().entity(esitostep).build();
//		}
//		else {
			// se esiste il protocollo devo cambiare stato alla domanda ed inserirne il
			// numero
			if (protocolloEsiste) {
				if (tipoprotocollo.equalsIgnoreCase("PARTENZA")) {
					if (idDocumentoFruitore.contains("LETTERA_DINIEGO"))
						richiesteDao.updateProtocollo(protocollo, Util.convertStringToTimestampItalian(dataprotocollo),
								tipoprotocollo, idDocumentoFruitore, "LETTERA_DINIEGO");
					else if (idDocumentoFruitore.contains("LETTERA_AMMISSIONE_FINANZIAMENTO"))
						richiesteDao.updateProtocollo(protocollo, Util.convertStringToTimestampItalian(dataprotocollo),
								tipoprotocollo, idDocumentoFruitore, "LETTERA_AMMISSIONE_FINANZIAMENTO");
					else if (idDocumentoFruitore.contains("LETTERA_AMMISSIONE_NON_FINANZIAMENTO"))
						richiesteDao.updateProtocollo(protocollo, Util.convertStringToTimestampItalian(dataprotocollo),
								tipoprotocollo, idDocumentoFruitore, "LETTERA_AMMISSIONE_NON_FINANZIAMENTO");
				} else
					richiesteDao.updateProtocollo(protocollo, Util.convertStringToTimestampItalian(dataprotocollo),
							tipoprotocollo, idDocumentoFruitore, "DOMANDA");
			}
			esitocallback.setCodice(Constants.OK);
			esitocallback.setMessaggio(validateGeneric.getValueGenericSuccess(Constants.OK_DESCRIZIONE, null));
			// esitostep.setNome("esito");
			esitostep.setEsito(esitocallback);
			return Response.ok().entity(esitostep).build();
//		}
		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			if (e.getMessage() != null)
				param[0] = metodo + " Errore riguardante database";
			else
				param[0] = metodo + " " + "null pointer exception";
			esitocallback.setCodice(Constants.KO_APPLICATIVI);
			esitocallback
					.setMessaggio(validateGeneric.getValueGenericErrorStardas(CodeErrorEnum.ERR17.getCode(), param));
			// esitostep.setNome("esito");
			esitostep.setEsito(esitocallback);
			return Response.ok().entity(esitostep).build();
		} catch (Exception e) {
			e.printStackTrace();
			logError(metodo, "Errore generico:", e);
			if (e.getMessage() != null)
				param[0] = metodo + " " + e.getMessage();
			else
				param[0] = metodo + " " + "null pointer exception";
			esitocallback.setCodice(Constants.KO_APPLICATIVI);
			esitocallback
					.setMessaggio(validateGeneric.getValueGenericErrorStardas(CodeErrorEnum.ERR16.getCode(), param));
			// esitostep.setNome("esito");
			esitostep.setEsito(esitocallback);
			return Response.ok().entity(esitostep).build();
		}

	}

	public Response executePost(EsitoSmistaDocumentoRequest body, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		// prendo esito dal body per poter mettere numero protocollo ed esito poi creo
		// tabella per log
		ResultType esitocallback = new ResultType();
		EsitoStep esitostep = new EsitoStep();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		String[] param = new String[1];
		try {
			List<ErroreDettaglioExt> listError = validateGeneric.validateSmista(body, securityContext, httpHeaders,
					httpRequest);
			if (!listError.isEmpty()) {
				param[0] = metodo + " " + listError.toString();
				esitocallback.setCodice(Constants.KO_APPLICATIVI);
				esitocallback.setMessaggio(
						validateGeneric.getValueGenericErrorStardas(CodeErrorEnum.ERR17.getCode(), param));
				// esitostep.setNome("esito");
				esitostep.setEsito(esitocallback);
				return Response.ok().entity(esitostep).build();
			}
			// verifica se otteniamo protocollo ed archiviazione
			StardasLog log = new StardasLog();
			boolean esitook = false;
			boolean protocolloEsiste = false;
			String protocollo = null;
			String idDocumentoFruitore = null;
			String dataprotocollo = null;
			String tipoprotocollo = null;
			String uuid = null;
			String esitokocallback = null;
			if (body.getEsitoSmistaDocumento() != null) {
				if (body.getEsitoSmistaDocumento().getEsito() != null) {
					log.setMessageUUID(body.getEsitoSmistaDocumento().getEsito().getMessageUUID());
					uuid = body.getEsitoSmistaDocumento().getEsito().getMessageUUID();
					log.setIdDocumentoFruitore(body.getEsitoSmistaDocumento().getEsito().getIdDocumentoFruitore());
					idDocumentoFruitore = body.getEsitoSmistaDocumento().getEsito().getIdDocumentoFruitore();
					log.setTipoTrattamento(body.getEsitoSmistaDocumento().getEsito().getTipoTrattamento());
					if (body.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento() != null) {
						log.setEsitoTrattamento(
								body.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getCodice() + " " + body
										.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getMessaggio());
						// verifico se codice 000 ossia positivo
						if (body.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getCodice() != null) {
							if (body.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getCodice()
									.equalsIgnoreCase(Constants.OK)
									|| body.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento().getCodice()
											.equalsIgnoreCase(Constants.OK_PARZIALE)) {
								esitook = true;
							} else {
								esitokocallback = body.getEsitoSmistaDocumento().getEsito().getEsitoTrattamento()
										.getMessaggio();
							}
						}
					}
					if (body.getEsitoSmistaDocumento().getEsito().getEsitiStep() != null) {
						if (body.getEsitoSmistaDocumento().getEsito().getEsitiStep().getEsitoStep() != null) {
							List<EsitoStep> esitiStep = new ArrayList<EsitoStep>();
							for (EsitoStep esito : body.getEsitoSmistaDocumento().getEsito().getEsitiStep()
									.getEsitoStep()) {
								// esiste il protocollo con esito positvo
								if (esito.getNome().equalsIgnoreCase(Constants.PROTOCOLLAZIONE_ACTA)) {
									protocolloEsiste = true;
								}
								esitiStep.add(esito);
							}
							log.setEsitiStep(esitiStep.toString());
						}
					}
					// if (protocolloEsiste) {
					if (body.getEsitoSmistaDocumento().getEsito().getInformazioniAggiuntive() != null) {
						if (body.getEsitoSmistaDocumento().getEsito().getInformazioniAggiuntive()
								.getInformazione() != null) {
							List<InformazioneType> infoType = new ArrayList<InformazioneType>();
							for (InformazioneType info : body.getEsitoSmistaDocumento().getEsito()
									.getInformazioniAggiuntive().getInformazione()) {
								if (info.getNome().toUpperCase().equalsIgnoreCase(Constants.NUMERO_REG_PROTOCOLLO)) {
									// prelevo i dati del protocollo
									protocollo = info.getValore();
								}
								if (info.getNome().toUpperCase().equalsIgnoreCase(Constants.DATA_REG_PROTOCOLLO)) {
									// prelevo i dati del protocollo
									dataprotocollo = info.getValore();
								}
								if (info.getNome().toUpperCase().equalsIgnoreCase(Constants.TIPO_REG_PROTOCOLLO)) {
									// prelevo i dati del protocollo
									tipoprotocollo = info.getValore();
								}
								infoType.add(info);
							}
							log.setInformazioniAggiuntive(infoType.toString());
						}
					}
					// }
					// inserisci i valori
					logStardas.insertLogStardas(log);
				}
			}

			if (!richiesteDao.selectIdDocumentoFruitore(idDocumentoFruitore)) {
				param[0] = metodo + " " + "iddocumentofruitore non trovato";
				esitocallback.setCodice(Constants.KO_APPLICATIVI);
				esitocallback.setMessaggio(
						validateGeneric.getValueGenericErrorStardas(CodeErrorEnum.ERR17.getCode(), param));
				// esitostep.setNome("esito");
				esitostep.setEsito(esitocallback);
				return Response.ok().entity(esitostep).build();
			}
//				if (!esitook) {
//					param[0] = metodo + " " +esitokocallback;
//					esitocallback.setCodice(Constants.KO);
//					esitocallback.setMessaggio(validateGeneric.getValueGenericErrorStardas(CodeErrorEnum.ERR16.getCode(), param));
//				//	esitostep.setNome("esito");
//					esitostep.setEsito(esitocallback);
//					return Response.ok().entity(esitostep).build();
//				}
//				else {
			// se esiste il protocollo devo cambiare stato alla domanda ed inserirne il
			// numero
			if (protocolloEsiste) {
				if (tipoprotocollo.equalsIgnoreCase("PARTENZA")) {
					if (idDocumentoFruitore.contains("LETTERA_DINIEGO"))
						richiesteDao.updateProtocollo(protocollo, Util.convertStringToTimestampItalian(dataprotocollo),
								tipoprotocollo, idDocumentoFruitore, "LETTERA_DINIEGO");
					else if (idDocumentoFruitore.contains("LETTERA_AMMISSIONE_FINANZIAMENTO"))
						richiesteDao.updateProtocollo(protocollo, Util.convertStringToTimestampItalian(dataprotocollo),
								tipoprotocollo, idDocumentoFruitore, "LETTERA_AMMISSIONE_FINANZIAMENTO");
					else if (idDocumentoFruitore.contains("LETTERA_AMMISSIONE_NON_FINANZIAMENTO"))
						richiesteDao.updateProtocollo(protocollo, Util.convertStringToTimestampItalian(dataprotocollo),
								tipoprotocollo, idDocumentoFruitore, "LETTERA_AMMISSIONE_NON_FINANZIAMENTO");
				} else
					richiesteDao.updateProtocollo(protocollo, Util.convertStringToTimestampItalian(dataprotocollo),
							tipoprotocollo, idDocumentoFruitore, "DOMANDA");
			}
			esitocallback.setCodice(Constants.OK);
			esitocallback.setMessaggio(validateGeneric.getValueGenericSuccess(Constants.OK_DESCRIZIONE, null));
			// esitostep.setNome("esito");
			esitostep.setEsito(esitocallback);
			return Response.ok().entity(esitostep).build();
//				}
		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			if (e.getMessage() != null)
				param[0] = metodo + " Errore riguardante database";
			else
				param[0] = metodo + " " + "null pointer exception";
			esitocallback.setCodice(Constants.KO_APPLICATIVI);
			esitocallback
					.setMessaggio(validateGeneric.getValueGenericErrorStardas(CodeErrorEnum.ERR17.getCode(), param));
			// esitostep.setNome("esito");
			esitostep.setEsito(esitocallback);
			return Response.ok().entity(esitostep).build();
		} catch (Exception e) {
			e.printStackTrace();
			logError(metodo, "Errore generico:", e);
			if (e.getMessage() != null)
				param[0] = metodo + " " + e.getMessage();
			else
				param[0] = metodo + " " + "null pointer exception";
			esitocallback.setCodice(Constants.KO_APPLICATIVI);
			esitocallback
					.setMessaggio(validateGeneric.getValueGenericErrorStardas(CodeErrorEnum.ERR16.getCode(), param));
			// esitostep.setNome("esito");
			esitostep.setEsito(esitocallback);
			return Response.ok().entity(esitostep).build();
		}

	}
}

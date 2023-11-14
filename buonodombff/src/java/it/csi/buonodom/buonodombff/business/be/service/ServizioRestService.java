/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.integration.rest.RestBaseService;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.rest.ResponseRest;

@Service
public class ServizioRestService extends BaseService {
	@Value("${buonodomsrvurl}")
	private String buonodomsrvurl;

	@Autowired
	RestBaseService restbase;

	@Autowired
	BatchService batchService;

	public ResponseRest getCreaDomanda(String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			String numerodomanda) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		Long risultOk = null;
		Long risultKo = null;
		String batchCod = null;
		String motivoCod = null;
		boolean eisteinvianotifica = false;
		if (httpRequest.getMethod().equalsIgnoreCase("PUT") && httpRequest.getRequestURI().contains("richieste")) {
			batchCod = "PUT_RICHIESTA";
			motivoCod = "AGGIORNA_DOMANDA";
		} else if (httpRequest.getMethod().equalsIgnoreCase("POST")
				&& httpRequest.getRequestURI().contains("richieste")) {
			batchCod = "POST_RICHIESTA";
			motivoCod = "CREA_DOMANDA";
		} else if (httpRequest.getMethod().equalsIgnoreCase("POST")
				&& httpRequest.getRequestURI().contains("cronologia")) {
			batchCod = "POST_CRONOLOGIA";
			// prendi il body e cattura lo stato
			String stato = batchService.selectStatoDomanda(numerodomanda);
			if (stato != null)
				motivoCod = "CAMBIO_STATO_DOMANDA_" + stato;
			else
				motivoCod = "CAMBIO_STATO_DOMANDA";
			if (stato.equalsIgnoreCase(Constants.INVIATA) || stato.equalsIgnoreCase(Constants.RETTIFICATA)
					|| stato.equalsIgnoreCase(Constants.CONTRODEDOTTA) || stato.equalsIgnoreCase(Constants.RINUNCIATA)
					|| stato.equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
				eisteinvianotifica = true;
			}
		}

		String utente = null;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}
		int esitopos = 0;
		try {
			// chiamo servizio se ok allora inserisco in t_batch_esecuzione e step se ko in
			// s_batch_esecuzione
			String os = System.getProperty("os.name");
			// verifico se ho un errore o esito positivo per scrivere i log
			if (batchCod.equalsIgnoreCase("POST_CRONOLOGIA")) {
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGet(httpHeaders,
							"http://localhost:8080/buonodomsrv/api/v1/crea_domanda/" + numerodomanda, true, null, true);
				} else {
					response = restbase.eseguiGet(httpHeaders, buonodomsrvurl + "/crea_domanda/" + numerodomanda, true,
							null, true);
				}
				if (eisteinvianotifica) {
					// ci sono tre step crea domanda invio stardas invio notifica
					risultOk = batchService.inserisciBatchOK(numerodomanda, batchCod, motivoCod, utente, "STATO_OK");
					risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente, "STATO_KO");
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					if (!eisteinvianotifica) {
						risultOk = batchService.inserisciBatchOK(numerodomanda, batchCod, motivoCod, utente,
								"STATO_OK");
					}
					// esito positivo
					if (risultOk > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepOK("CREA_DOMANDA", "Domanda " + numerodomanda + " " + motivoCod,
								risultOk, utente);
					}
				} else {
					if (!eisteinvianotifica) {
						risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente,
								"STATO_KO");
					}
					if (risultKo != null && risultKo > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepKO("CREA_DOMANDA",
								"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), risultKo,
								utente);
					}
				}
			} else {
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGet(httpHeaders,
							"http://localhost:8080/buonodomsrv/api/v1/crea_domanda/" + numerodomanda, true, null, true);
				} else {
					response = restbase.eseguiGet(httpHeaders, buonodomsrvurl + "/crea_domanda/" + numerodomanda, true,
							null, true);
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// esito positivo
					risultOk = batchService.inserisciBatchOK(numerodomanda, batchCod, motivoCod, utente, "STATO_OK");
					if (risultOk != null && risultOk > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepOK("CREA_DOMANDA", "Domanda " + numerodomanda + " " + motivoCod,
								risultOk, utente);
					}
				} else {
					// esito negativo
					risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente, "STATO_KO");
					if (risultKo != null && risultKo > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepKO("CREA_DOMANDA",
								"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), risultKo,
								utente);
					}
				}
			}
			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			log.error(metodo + " - errore db");
			e.printStackTrace();
		} catch (Exception e) {
			if (batchCod.equalsIgnoreCase("POST_CRONOLOGIA")) {
				risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente, "STATO_KO");
				if (risultKo != null && risultKo > 0) {
					// inserisco nella det
					batchService.inserisciBatchStepKO("CREA_DOMANDA",
							"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), risultKo, utente);
				}
			} else {
				risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente, "STATO_KO");
				if (risultKo != null && risultKo > 0) {
					// inserisco nella det
					batchService.inserisciBatchStepKO("CREA_DOMANDA",
							"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), risultKo, utente);
				}
			}
			log.error(metodo + " - errore " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getInviaNotifica(String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			String numerodomanda, String tipoNotifica) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		String utente = null;
		int esitopos = 0;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}
		Long numseqbatchKo = batchService.selectEventoKo(numerodomanda, "STATO_KO", "POST_CRONOLOGIA");
		Long numseqbatchOk = batchService.selectEventoOk(numerodomanda, "STATO_OK", "POST_CRONOLOGIA");
		try {
			String os = System.getProperty("os.name");
			esitopos = batchService.selectEsitoPositivo(numerodomanda, "INVIO_NOTIFICA_" + tipoNotifica);
			if (esitopos == 0) {
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGet(httpHeaders,
							"http://localhost:8080/buonodomsrv/api/v1/notifiche/" + numerodomanda + "/" + tipoNotifica,
							true, null, true);
				} else {
					response = restbase.eseguiGet(httpHeaders,
							buonodomsrvurl + "/notifiche/" + numerodomanda + "/" + tipoNotifica, true, null, true);
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchService.inserisciBatchStepOK("INVIO_NOTIFICA_" + tipoNotifica,
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoNotifica, numseqbatchOk,
							utente);
				} else {
					// inserisco nella det
					batchService
							.inserisciBatchStepKO(
									"INVIO_NOTIFICA_" + tipoNotifica, "Domanda " + numerodomanda
											+ " CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(),
									numseqbatchKo, utente);
				}
				// verifico come chiudere il batch se tutti gli step hanno esito ok o ko
				batchService.deleteEsitoVuotoOk(numseqbatchOk);
				batchService.deleteEsitoVuotoKo(numseqbatchKo);
				batchService.DeleteRecordDaCancellareDuplicati();
			}
			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			log.error(metodo + " - errore db");
			e.printStackTrace();
		} catch (Exception e) {
			if (esitopos == 0) {
				batchService.inserisciBatchStepKO("INVIO_NOTIFICA_" + tipoNotifica,
						"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(),
						numseqbatchKo, utente);
				batchService.deleteEsitoVuotoOk(numseqbatchOk);
				batchService.deleteEsitoVuotoKo(numseqbatchKo);
				batchService.DeleteRecordDaCancellareDuplicati();
			}
			log.error(metodo + " - errore " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getContatti(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			String os = System.getProperty("os.name");
			if (os.toLowerCase().contains("win")) {
				response = restbase.eseguiGet(httpHeaders,
						"http://localhost:8080/buonodomsrv/api/v1/notifiche/contatti", true, null, true);
			} else {
				response = restbase.eseguiGet(httpHeaders, buonodomsrvurl + "/notifiche/contatti", true, null, true);
			}
			log.info(metodo + " - Fine ");
		} catch (Exception e) {
			log.error(metodo + " - errore " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getPreferenze(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			String os = System.getProperty("os.name");
			if (os.toLowerCase().contains("win")) {
				response = restbase.eseguiGet(httpHeaders,
						"http://localhost:8080/buonodomsrv/api/v1/notifiche/preferenze", true, null, true);
			} else {
				response = restbase.eseguiGet(httpHeaders, buonodomsrvurl + "/notifiche/preferenze", true, null, true);
			}
			log.info(metodo + " - Fine ");
		} catch (Exception e) {
			log.error(metodo + " - errore " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getStartDas(String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			String numerodomanda) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		String utente = null;
		int esitopos = 0;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}
		Long numseqbatchKo = batchService.selectEventoKo(numerodomanda, "STATO_KO", "POST_CRONOLOGIA");
		Long numseqbatchOk = batchService.selectEventoOk(numerodomanda, "STATO_OK", "POST_CRONOLOGIA");
		try {
			String os = System.getProperty("os.name");
			esitopos = batchService.selectEsitoPositivo(numerodomanda, "STARDAS_PROTOCOLLO_ARRIVO");
			if (esitopos == 0) {
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGet(httpHeaders,
							"http://localhost:8080/buonodomsrv/api/v1/smistadocumento/" + numerodomanda, true, null,
							true);
				} else {
					response = restbase.eseguiGet(httpHeaders, buonodomsrvurl + "/smistadocumento/" + numerodomanda,
							true, null, true);
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchService.inserisciBatchStepOK("STARDAS_PROTOCOLLO_ARRIVO",
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA", numseqbatchOk, utente);
				} else {
					// inserisco nella det
					batchService.inserisciBatchStepKO("STARDAS_PROTOCOLLO_ARRIVO",
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA" + " " + response.getJson(),
							numseqbatchKo, utente);
				}
			}
			log.info(metodo + " - Fine ");
		} catch (DatabaseException e) {
			log.error(metodo + " - errore db");
			e.printStackTrace();
		} catch (Exception e) {
			if (esitopos == 0) {
				batchService.inserisciBatchStepKO("STARDAS_PROTOCOLLO_ARRIVO",
						"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA" + " " + response.getJson(), numseqbatchKo,
						utente);
			}
			log.error(metodo + " - errore " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
}

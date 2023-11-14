/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.business.be.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombo.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombo.integration.rest.RestBaseService;
import it.csi.buonodom.buonodombo.util.rest.ResponseRest;

@Service
public class ServizioRestService extends BaseService {
	@Value("${buonodomsrvurl}")
	private String buonodomsrvurl;

	@Autowired
	RestBaseService restbase;

	@Autowired
	BatchService batchService;

	// solo domanda per richiedi_rettifica diniego inpagamento
	// preavvisopernonammissibilita

	public ResponseRest getCreaDomanda(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String numerodomanda, String cf) {
		ResponseRest response = new ResponseRest();
		Long risultOk = null;
		Long risultKo = null;
		String batchCod = null;
		String motivoCod = null;
		int esitopos = 0;
		batchCod = "CAMBIO_STATO_OPERATORE";
		// prendi il body e cattura lo stato
		String stato = batchService.selectStatoDomanda(numerodomanda);
		if (stato != null)
			motivoCod = "CAMBIO_STATO_DOMANDA_" + stato;
		else
			motivoCod = "CAMBIO_STATO_DOMANDA";
		String utente = null;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}
		try {
			String os = System.getProperty("os.name");
			esitopos = batchService.selectEsitoPositivo(numerodomanda, motivoCod);
			if (os.toLowerCase().contains("win")) {
				response = restbase.eseguiGet(httpRequest, httpHeaders,
						"http://localhost:8080/buonodomsrv/api/v1/crea_domanda/" + numerodomanda, true, null, true, cf);
			} else {
				response = restbase.eseguiGet(httpRequest, httpHeaders,
						buonodomsrvurl + "/crea_domanda/" + numerodomanda, true, null, true, cf);
			}
			// verifico se ho un errore o esito positivo per scrivere i log
			if (httpRequest.getRequestURI().contains("richiedi_rettifica")
					|| httpRequest.getRequestURI().contains("diniego")
					|| httpRequest.getRequestURI().contains("inpagamento")
					|| httpRequest.getRequestURI().contains("preavvisopernonammissibilita")) {
				// ci sono altri step oltre a crea domanda
				risultOk = batchService.inserisciBatchOK(numerodomanda, batchCod, motivoCod, utente, "STATO_OK");
				risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente, "STATO_KO");
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// esito positivo
					if (risultOk > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepOK("CREA_DOMANDA", "Domanda " + numerodomanda + " " + motivoCod,
								risultOk, utente);
					}
				} else {
					if (risultKo > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepKO("CREA_DOMANDA",
								"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), risultKo,
								utente);
					}
				}
			} else {
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// esito positivo
					risultOk = batchService.inserisciBatchOK(numerodomanda, batchCod, motivoCod, utente, "STATO_OK");
					if (risultOk > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepOK("CREA_DOMANDA", "Domanda " + numerodomanda + " " + motivoCod,
								risultOk, utente);
					}
				} else {
					// esito negativo
					risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente, "STATO_KO");
					if (risultKo > 0) {
						// inserisco nella det
						batchService.inserisciBatchStepKO("CREA_DOMANDA",
								"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), risultKo,
								utente);
					}
				}
			}
		} catch (Exception e) {
			risultKo = batchService.inserisciBatchKO(numerodomanda, batchCod, motivoCod, utente, "STATO_KO");
			if (risultKo > 0) {
				// inserisco nella det
				batchService.inserisciBatchStepKO("CREA_DOMANDA",
						"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), risultKo, utente);
			}
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getInviaNotifica(HttpHeaders httpHeaders, HttpServletRequest httpRequest, String numerodomanda,
			String tipoNotifica, String cf) {
		ResponseRest response = new ResponseRest();
		String utente = null;
		int esitopos = 0;
		long numseqbatchKo = 0;
		long numseqbatchOk = 0;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}
		try {
			String os = System.getProperty("os.name");
			esitopos = batchService.selectEsitoPositivo(numerodomanda, "INVIO_NOTIFICA_" + tipoNotifica);
			if (esitopos == 0) {
				numseqbatchKo = batchService.selectEventoKo(numerodomanda, "STATO_KO");
				numseqbatchOk = batchService.selectEventoOk(numerodomanda, "STATO_OK");
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGet(httpRequest, httpHeaders,
							"http://localhost:8080/buonodomsrv/api/v1/notifiche/" + numerodomanda + "/" + tipoNotifica,
							true, null, true, cf);
				} else {
					response = restbase.eseguiGet(httpRequest, httpHeaders,
							buonodomsrvurl + "/notifiche/" + numerodomanda + "/" + tipoNotifica, true, null, true, cf);
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
		} catch (Exception e) {
			if (esitopos == 0) {
				batchService.inserisciBatchStepKO("INVIO_NOTIFICA_" + tipoNotifica,
						"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(),
						numseqbatchKo, utente);
				batchService.deleteEsitoVuotoOk(numseqbatchOk);
				batchService.deleteEsitoVuotoKo(numseqbatchKo);
				batchService.DeleteRecordDaCancellareDuplicati();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getStarDas(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String numerodomanda, String cf) {
		ResponseRest response = new ResponseRest();
		try {
			String os = System.getProperty("os.name");
			if (os.toLowerCase().contains("win")) {
				response = restbase.eseguiGet(httpRequest, httpHeaders,
						"http://localhost:8080/buonodomsrv/api/v1/smistadocumento/" + numerodomanda, true, null, true,
						cf);
			} else {
				response = restbase.eseguiGet(httpRequest, httpHeaders,
						buonodomsrvurl + "/smistadocumento/" + numerodomanda, true, null, true, cf);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getStarDasPartenza(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String numerodomanda, String cf, String tipoLettera) {
		ResponseRest response = new ResponseRest();
		int esitopos = 0;
		long numseqbatchKo = 0;
		long numseqbatchOk = 0;
		String utente = null;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}
		try {
			esitopos = batchService.selectEsitoPositivo(numerodomanda, "STARDAS_PARTENZA_" + tipoLettera);
			if (esitopos == 0) {
				numseqbatchKo = batchService.selectEventoKo(numerodomanda, "STATO_KO");
				numseqbatchOk = batchService.selectEventoOk(numerodomanda, "STATO_OK");
				String os = System.getProperty("os.name");
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGet(httpRequest, httpHeaders,
							"http://localhost:8080/buonodomsrv/api/v1/smistadocumentopartenza/" + numerodomanda + "/"
									+ tipoLettera,
							true, null, true, cf);
				} else {
					response = restbase.eseguiGet(httpRequest, httpHeaders,
							buonodomsrvurl + "/smistadocumentopartenza/" + numerodomanda + "/" + tipoLettera, true,
							null, true, cf);
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchService.inserisciBatchStepOK("STARDAS_PARTENZA_" + tipoLettera,
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoLettera, numseqbatchOk, utente);
				} else {
					// inserisco nella det
					batchService.inserisciBatchStepKO("STARDAS_PARTENZA_" + tipoLettera, "Domanda " + numerodomanda
							+ " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(), numseqbatchKo, utente);
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				batchService.inserisciBatchStepKO("STARDAS_PARTENZA_" + tipoLettera,
						"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(),
						numseqbatchKo, utente);
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getCreaLettera(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String numerodomanda, String cf, String tipoLettera) {
		ResponseRest response = new ResponseRest();
		int esitopos = 0;
		long numseqbatchKo = 0;
		long numseqbatchOk = 0;
		String utente = null;
		if (httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale") != null
				&& httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Identita-CodiceFiscale").get(0);
		} else if (httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale") != null
				&& httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").size() > 0) {
			utente = httpHeaders.getRequestHeader("Shib-Iride-IdentitaDigitale").get(0);
		}
		try {
			esitopos = batchService.selectEsitoPositivo(numerodomanda, "CREA_LETTERA_" + tipoLettera);
			if (esitopos == 0) {
				numseqbatchKo = batchService.selectEventoKo(numerodomanda, "STATO_KO");
				numseqbatchOk = batchService.selectEventoOk(numerodomanda, "STATO_OK");
				String os = System.getProperty("os.name");
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGet(httpRequest, httpHeaders,
							"http://localhost:8080/buonodomsrv/api/v1/crea_lettera/" + numerodomanda + "/"
									+ tipoLettera,
							true, null, true, cf);
				} else {
					response = restbase.eseguiGet(httpRequest, httpHeaders,
							buonodomsrvurl + "/crea_lettera/" + numerodomanda + "/" + tipoLettera, true, null, true,
							cf);
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchService.inserisciBatchStepOK("CREA_LETTERA_" + tipoLettera,
							"CAMBIO_STATO_DOMANDA_" + tipoLettera, numseqbatchOk, utente);
				} else {
					// inserisco nella det
					batchService.inserisciBatchStepKO("CREA_LETTERA_" + tipoLettera, "Domanda " + numerodomanda
							+ " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(), numseqbatchKo, utente);
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				batchService.inserisciBatchStepKO("CREA_LETTERA_" + tipoLettera,
						"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(),
						numseqbatchKo, utente);
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

}

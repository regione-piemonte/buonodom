/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombanbatch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.codehaus.jackson.map.ObjectMapper;

import it.csi.buonodom.buonodombanbatch.dao.BuonodombanBatchDAO;
import it.csi.buonodom.buonodombanbatch.dao.BuonodombanBatchLogDAO;
import it.csi.buonodom.buonodombanbatch.dao.DBConnectionManager;
import it.csi.buonodom.buonodombanbatch.dto.Errore;
import it.csi.buonodom.buonodombanbatch.dto.ModelBandiMessage;
import it.csi.buonodom.buonodombanbatch.dto.RichiestaDto;
import it.csi.buonodom.buonodombanbatch.exception.ConnectionException;
import it.csi.buonodom.buonodombanbatch.exception.ConnectionSoapException;
import it.csi.buonodom.buonodombanbatch.integration.rest.RestBaseService;
import it.csi.buonodom.buonodombanbatch.logger.BatchLoggerFactory;
import it.csi.buonodom.buonodombanbatch.util.Checker;
import it.csi.buonodom.buonodombanbatch.util.Constants;
import it.csi.buonodom.buonodombanbatch.util.rest.ResponseRest;

public class Main {
	private Connection conn;
	private BuonodombanBatchDAO dao = null;
	private RestBaseService restbase = null;
	private BuonodombanBatchLogDAO batchDao = null;
	private int systemReturn = 0;

	public static void main(String[] args) {
		new Main().run();
	}

	/**
	 * Crea la connessione a db
	 * 
	 * @throws ConnectionException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private void init() throws ConnectionSoapException {
		try {
			BatchLoggerFactory.getLogger(this).info("--- CONNESSIONE AL DATABASE IN CORSO ---");
			this.conn = DBConnectionManager.getConnection();
			this.conn.setAutoCommit(false);
			dao = new BuonodombanBatchDAO(this.conn);
			batchDao = new BuonodombanBatchLogDAO(this.conn);
			restbase = RestBaseService.getInstance(dao);
			BatchLoggerFactory.getLogger(this).info("--- CONNESSIONE EFFETTUATA ---");

		} catch (SQLException e) {
			throw new ConnectionSoapException("Database connection fail: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new ConnectionSoapException("Database connection fail: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void run() {

		try {
			BatchLoggerFactory.getLogger(this).info("--- AVVIO BATCH ---");
			// Stabilisco la connessione con il Database
			init();
			BatchLoggerFactory.getLogger(this).info("--- PRENDO ELENCO CF CON RICHIESTE IN PAGAMENTO ---");
			List<RichiestaDto> richiesta = new ArrayList<RichiestaDto>();
			ResponseRest response = new ResponseRest();
			dao.insertAudit("Inizio BanBatch", "Conteggio pratiche di pagamento per avvisi", 0);
			Integer avvisipagamento = 0;
			Boolean graduatoriaPubblicata = false;
			String codGraduatoria = null;
			// verifico se inviare a bandi le domande
			boolean eseguoinviobandidomande = dao
					.getParametro(Constants.INVIO_DOMANDE_BANDI, Constants.TIPO_PARAMETRO_GIORNI).toLowerCase()
					.equalsIgnoreCase("true") ? true : false;
			boolean notificaammessafinanziata = dao
					.getParametro(Constants.NOTIFICA_AMMESSA_FINANZIATA, Constants.TIPO_PARAMETRO_GIORNI).toLowerCase()
					.equalsIgnoreCase("true") ? true : false;

			richiesta = dao.getRichieste();
			for (RichiestaDto rich : richiesta) {
				try {
					graduatoriaPubblicata = dao.checkStatoGraduatoria(rich.getSportelloId());
					codGraduatoria = dao.getCodGraduatoria(rich.getSportelloId());
					if (graduatoriaPubblicata) {
						// domanda in pagamento che proviene da ammessa per cui esiste almeno uno step
						// andato in ko
						BatchLoggerFactory.getLogger(this).info(
								"---Ban batch creazione pdf domanda da " + Constants.AMMESSA + " a " + rich.getStato());
						response = new ResponseRest();
						response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "PUBBLICAZIONE",
								"CAMBIO_STATO_DOMANDA_BANDI", codGraduatoria);
						BatchLoggerFactory.getLogger(this).info("---Ban batch creazione pdf lettera finanziata");
						response = new ResponseRest();
						response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(), "PUBBLICAZIONE",
								"CAMBIO_STATO_DOMANDA_BANDI", Constants.LETTERA_AMMISSIONE_FINANZIAMENTO,
								codGraduatoria);
						BatchLoggerFactory.getLogger(this)
								.info("---Ban batch invio stardas pertenza pdf lettera finanziata");
						if (notificaammessafinanziata) {
							response = new ResponseRest();
							response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									"PUBBLICAZIONE", "CAMBIO_STATO_DOMANDA_BANDI",
									Constants.LETTERA_AMMISSIONE_FINANZIAMENTO, codGraduatoria);
						}
						BatchLoggerFactory.getLogger(this).info("---Ban batch chiamata bandi");
						// creo le tabelle del buono
						creaBuonoRendicontazione(rich, "PUBBLICAZIONE", "CAMBIO_STATO_DOMANDA_BANDI");
						if (eseguoinviobandidomande) {
							// chiama bandi
							response = new ResponseRest();
							response = invioBandi(rich.getDomandaNumero(), rich.getRichiedenteCf(), "PUBBLICAZIONE",
									"CAMBIO_STATO_DOMANDA_BANDI", codGraduatoria);
						}
						BatchLoggerFactory.getLogger(this).info("---Ban batch creo record buono");
						if (notificaammessafinanziata) {
							String statoultimo = dao.selectStatoPrecedente(rich.getDomandaNumero());
							if (statoultimo.contains(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
								BatchLoggerFactory.getLogger(this).info(
										"---Ban batch invio notifica finanziata proveniente da perfezionata in pagamento");
								response = new ResponseRest();
								response = getInviaNotifica(rich.getDomandaNumero(),
										Constants.BO_PERFEZIONATA_IN_PAGAMENTO, rich.getRichiedenteCf(), codGraduatoria,
										"PUBBLICAZIONE", "CAMBIO_STATO_DOMANDA_BANDI");
							} else {
								BatchLoggerFactory.getLogger(this).info("---Ban batch invio notifica finanziata");
								response = new ResponseRest();
								response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_AMMESSA_FINANZIATA,
										rich.getRichiedenteCf(), codGraduatoria, "PUBBLICAZIONE",
										"CAMBIO_STATO_DOMANDA_BANDI");
							}
						}
						avvisipagamento = avvisipagamento + 1;
						dao.insertAudit("Domanda numero " + rich.getDomandaNumero() + " richiedente "
								+ rich.getRichiedenteCf() + " destinatario " + rich.getCf(),
								"Domanda finanziata in pagamento ", 200);
					}
				} catch (SQLException e) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException invio bandi: ", e);
					systemReturn = 5;
					e.printStackTrace();
					continue;
				}
			}
			dao.insertAudit("Fine primo step BanBatch",
					"Numero notifiche per stato " + Constants.IN_PAGAMENTO + " " + avvisipagamento.toString(), 200);
			boolean notificaammessanonfinanziata = dao
					.getParametro(Constants.NOTIFICA_AMMESSA_NON_FINANZIATA, Constants.TIPO_PARAMETRO_GIORNI)
					.toLowerCase().equalsIgnoreCase("true") ? true : false;
			richiesta = dao.getRichiesteNonFinanziate();
			avvisipagamento = 0;
			BatchLoggerFactory.getLogger(this).info("---Prende ultima graduatoria pubblicata---");
			String ultimaGraduatoria = dao.getCodGraduatoriaUltima();
			if (ultimaGraduatoria != null) {
				for (RichiestaDto rich : richiesta) {
					try {
						// domanda ammessa e ammmessa con riserva non finanziata in ultimo sportello
						BatchLoggerFactory.getLogger(this).info("---Ban batch creazione pdf lettera non finanziata");
						response = new ResponseRest();
						response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(), "NON_FINANZIAMENTO",
								"DOMANDA_NON_FINANZIATA", Constants.LETTERA_AMMISSIONE_NON_FINANZIAMENTO,
								ultimaGraduatoria);
						BatchLoggerFactory.getLogger(this)
								.info("---Ban batch invio stardas pertenza pdf lettera non finanziata");
						if (notificaammessanonfinanziata) {
							response = new ResponseRest();
							response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									"NON_FINANZIAMENTO", "DOMANDA_NON_FINANZIATA",
									Constants.LETTERA_AMMISSIONE_NON_FINANZIAMENTO, ultimaGraduatoria);
							BatchLoggerFactory.getLogger(this).info("---Ban batch invio notifica non finanziata");
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_AMMESSA_NON_FINANZIATA,
									rich.getRichiedenteCf(), ultimaGraduatoria, "NON_FINANZIAMENTO",
									"DOMANDA_NON_FINANZIATA");
						}
						avvisipagamento = avvisipagamento + 1;
						dao.insertAudit("Domanda numero " + rich.getDomandaNumero() + " richiedente "
								+ rich.getRichiedenteCf() + " destinatario " + rich.getCf(), "Domanda non finanziata",
								200);
					} catch (SQLException e) {
						systemReturn = 4;
						BatchLoggerFactory.getLogger(this).error("ERRORE SQLException non finanziata: ", e);
						e.printStackTrace();
						continue;
					}
				}
			}
			dao.insertAudit("Fine BanBatch",
					"Numero notifiche per domande non finanziate " + avvisipagamento.toString(), 200);
			boolean inviorendicontazionebandi = dao
					.getParametro(Constants.INVIO_RENDICONTAZIONE_DOMANDE_BANDI, Constants.TIPO_PARAMETRO_GIORNI)
					.toLowerCase().equalsIgnoreCase("true") ? true : false;
			// prendo domande per cui inviare rendicontazione a bandi
			avvisipagamento = 0;
			if (inviorendicontazionebandi) {
				richiesta = dao.getDomandeInvioRendicontazioneBandi(Constants.CARICATA);
				for (RichiestaDto rich : richiesta) {
					try {
						// chiama bandi fornitore

						String esito = setFornitore(rich.getDomandaNumero(), rich.getRichiedenteCf(),
								"RENDICONTAZIONE_BUONI", "SET_FORNITORE");
						// chiama bandi invio rendicontazione
						if (esito == "OK") {
							response = new ResponseRest();
							response = invioRendicontazioneBandi(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									"RENDICONTAZIONE_BUONI", "INVIO_RENDICONTAZIONE_BANDI");
							dao.insertAudit(
									"Domanda numero " + rich.getDomandaNumero() + " richiedente "
											+ rich.getRichiedenteCf() + " destinatario " + rich.getCf(),
									"Domanda invio rendicontazione a bandi", 200);
						} else {
							dao.insertAudit(
									"Domanda numero " + rich.getDomandaNumero() + " richiedente "
											+ rich.getRichiedenteCf() + " destinatario " + rich.getCf(),
									"Domanda invio fornitore a bandi", 500);
						}

						avvisipagamento = avvisipagamento + 1;
					} catch (SQLException e) {
						systemReturn = 2;
						BatchLoggerFactory.getLogger(this).error("ERRORE SQLException invio rendicontazione: ", e);
						e.printStackTrace();
						continue;
					}
				}
			}
			dao.insertAudit("Fine BanBatch",
					"Numero notifiche per invio rendicontazione bandi " + avvisipagamento.toString(), 200);
			// check and count errors from t batch tables
			int errorCount = batchDao.countBatchExecutionErrors();
			if (errorCount == -1) {
				BatchLoggerFactory.getLogger(this).error(
						"--- ERRORE countBatchExecutionErrors: non è possibile contare gli errori sul database");
			} else {
				systemReturn = errorCount > 0 ? 6 : 0;
			}
		} catch (SQLException e) {
			systemReturn = 3;
			BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e);
			e.printStackTrace();
		} catch (ConnectionSoapException e1) {
			systemReturn = 1;
			BatchLoggerFactory.getLogger(this).error("ERRORE ConnectionSoapException: ", e1);
			e1.printStackTrace();
		} finally {
			BatchLoggerFactory.getLogger(this).info("--- CANCELLO RECORD DUPLICATI NELLE TABELLE DI LOG ---");
			eliminaRecordVuoti();
			BatchLoggerFactory.getLogger(this).info("--- END BATCH ---");
			System.exit(systemReturn);
			dao.closeAll();
		}

	}

	public ResponseRest getInviaNotifica(String numerodomanda, String tipoNotifica, String cf, String codGraduatoria,
			String batchCod, String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONODOMBANBATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda,
					"INVIO_NOTIFICA_" + tipoNotifica + "_" + codGraduatoria);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				String os = System.getProperty("os.name");

				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGet(cf,
							"http://localhost:8080/buonodomsrv/api/v1/notifiche/" + numerodomanda + "/" + tipoNotifica,
							true, null, true);
				} else {
					response = restbase.eseguiGet(cf,
							restbase.getBuonodomsrvurl() + "/notifiche/" + numerodomanda + "/" + tipoNotifica, true,
							null, true);
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchDao.inserisciBatchStepOK("INVIO_NOTIFICA_" + tipoNotifica + "_" + codGraduatoria,
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoNotifica, numseqbatchOk,
							utente);
				} else {
					// inserisco nella det
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO(
								"INVIO_NOTIFICA_" + tipoNotifica + "_" + codGraduatoria, "Domanda " + numerodomanda
										+ " CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO(
								"INVIO_NOTIFICA_" + tipoNotifica + "_" + codGraduatoria, "Domanda " + numerodomanda
										+ " CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
					e1.printStackTrace();
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getCreaDomanda(String numerodomanda, String cf, String batchCod, String motivoCod,
			String codGraduatoria) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONODOMBANBATCH";
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		int esitopos = 0;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, motivoCod + "_" + codGraduatoria);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				String os = System.getProperty("os.name");
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGet(cf,
							"http://localhost:8080/buonodomsrv/api/v1/crea_domanda/" + numerodomanda, true, null, true);
				} else {
					response = restbase.eseguiGet(cf, restbase.getBuonodomsrvurl() + "/crea_domanda/" + numerodomanda,
							true, null, true);
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// esito positivo
					// inserisco nella det
					batchDao.inserisciBatchStepOK(motivoCod + "_" + codGraduatoria,
							"Domanda " + numerodomanda + " " + motivoCod, numseqbatchOk, utente);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(motivoCod + "_" + codGraduatoria,
								"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				}

			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(motivoCod + "_" + codGraduatoria,
								"Domanda " + numerodomanda + " " + motivoCod + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
					e1.printStackTrace();
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getStartDasPartenza(String numerodomanda, String cf, String batchCod, String motivoCod,
			String tipoLettera, String codGraduatoria) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONODOMBANBATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda,
					"STARDAS_PARTENZA_" + tipoLettera + "_" + codGraduatoria);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				String os = System.getProperty("os.name");
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGet(cf,
							"http://localhost:8080/buonodomsrv/api/v1/smistadocumentopartenza/" + numerodomanda + "/"
									+ tipoLettera,
							true, null, true);
				} else {
					response = restbase.eseguiGet(cf, restbase.getBuonodomsrvurl() + "/smistadocumentopartenza/"
							+ numerodomanda + "/" + tipoLettera, true, null, true);
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchDao.inserisciBatchStepOK("STARDAS_PARTENZA_" + tipoLettera + "_" + codGraduatoria,
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoLettera, numseqbatchOk, utente);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(
								"STARDAS_PARTENZA_" + tipoLettera + "_" + codGraduatoria, "Domanda " + numerodomanda
										+ " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO(
								"STARDAS_PARTENZA_" + tipoLettera + "_" + codGraduatoria, "Domanda " + numerodomanda
										+ " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
					e1.printStackTrace();
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getCreaLettera(String numerodomanda, String cf, String batchCod, String motivoCod,
			String tipoLettera, String codGraduatoria) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONODOMBANBATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda,
					"CREA_LETTERA_" + tipoLettera + "_" + codGraduatoria);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				String os = System.getProperty("os.name");
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGet(cf, "http://localhost:8080/buonodomsrv/api/v1/crea_lettera/"
							+ numerodomanda + "/" + tipoLettera, true, null, true);
				} else {
					response = restbase.eseguiGet(cf,
							restbase.getBuonodomsrvurl() + "/crea_lettera/" + numerodomanda + "/" + tipoLettera, true,
							null, true);
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					batchDao.inserisciBatchStepOK("CREA_LETTERA_" + tipoLettera + "_" + codGraduatoria,
							"Domanda " + numerodomanda + " CAMBIO_STATO_DOMANDA_" + tipoLettera, numseqbatchOk, utente);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(
								"CREA_LETTERA_" + tipoLettera + "_" + codGraduatoria, "Domanda " + numerodomanda
										+ " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO(
								"CREA_LETTERA_" + tipoLettera + "_" + codGraduatoria, "Domanda " + numerodomanda
										+ " CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
					e1.printStackTrace();
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
			e.printStackTrace();
		}

		return response;
	}

	public ResponseRest invioBandi(String numerodomanda, String cf, String batchCod, String motivoCod,
			String codGraduatoria) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONODOMBANBATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda,
					"INVIO_BANDI_" + numerodomanda + "_" + codGraduatoria);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				String os = System.getProperty("os.name");
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGetBandi(cf,
							"http://localhost:8080/buonodombandisrv/api/v1/acquisizione_domande_bandi/" + numerodomanda,
							true, null, true);
				} else {
					response = restbase.eseguiGetBandi(cf,
							restbase.getBuonodombandisrvurl() + "/acquisizione_domande_bandi/" + numerodomanda, true,
							null, true);
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					ModelBandiMessage body = new ModelBandiMessage();
					if (response.getJson() != null) {
						ObjectMapper mapper = new ObjectMapper();
						if (response.getJson().contains("status")) {
							Errore bodyErrore = new Errore();
							bodyErrore = mapper.readValue(response.getJson(), Errore.class);
							if (numseqbatchKo != null && numseqbatchKo > 0) {
								// inserisco nella det
								batchDao.inserisciBatchStepKO("INVIO_BANDI_" + numerodomanda + "_" + codGraduatoria,
										"Domanda " + numerodomanda + " INVIO_BANDI" + " " + response.getJson(),
										numseqbatchKo, utente);
							}
						} else {
							body = mapper.readValue(response.getJson(), ModelBandiMessage.class);
							batchDao.inserisciBatchStepOK("INVIO_BANDI_" + numerodomanda + "_" + codGraduatoria,
									"Domanda " + numerodomanda + " INVIO_BANDI_MESSAGEUUID_" + body.getUuid(),
									numseqbatchOk, utente);
						}
					}
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO("INVIO_BANDI_" + numerodomanda + "_" + codGraduatoria,
								"Domanda " + numerodomanda + " INVIO_BANDI" + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("INVIO_BANDI_" + numerodomanda + "_" + codGraduatoria,
								"Domanda " + numerodomanda + " INVIO_BANDI" + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
					e1.printStackTrace();
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
			e.printStackTrace();
		}

		return response;
	}

	private void creaBuonoRendicontazione(RichiestaDto richiesta, String batchCod, String motivoCod) {
		// verifico se il buono creato se si non faccio nulla
		String utente = "BUONODOMBANBATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		long buonoId = 0;
		BatchLoggerFactory.getLogger(this)
				.info("--- INIZIO CREAZIONE TABELLE BUONI DOMANDA --- " + richiesta.getDomandaNumero());
		BatchLoggerFactory.getLogger(this).info("--- SE ESISTE BUONO NON FARE NULLA ---");
		try {
			esitopos = batchDao.selectEsitoPositivo(richiesta.getDomandaNumero(),
					"CREA_BUONO_" + richiesta.getDomandaNumero());
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(richiesta.getDomandaNumero(), "STATO_KO", batchCod, motivoCod,
						utente);
				numseqbatchOk = batchDao.selectEventoOk(richiesta.getDomandaNumero(), "STATO_OK", batchCod, motivoCod,
						utente);
				buonoId = dao.selectEsisteBuonoRendicontazione(richiesta.getDomandaId(), richiesta.getSportelloId());
				if (!Checker.isValorizzato(buonoId)) {
					BatchLoggerFactory.getLogger(this).info("--- BUONO NON ESISTE CREA --- " + buonoId);
					buonoId = dao.insertBuono(richiesta.getDomandaDetId(), richiesta.getSportelloId(),
							richiesta.getDomandaId(), richiesta.getIban(), richiesta.getIbanIntestatario());
				}
				BatchLoggerFactory.getLogger(this)
						.info("--- FINE CREAZIONE TABELLE BUONI DOMANDA --- " + richiesta.getDomandaNumero());

				batchDao.inserisciBatchStepOK("CREA_BUONO_" + richiesta.getDomandaNumero(),
						"Domanda " + richiesta.getDomandaNumero() + " CREA_BUONO_DOMANDA", numseqbatchOk, utente);
			}
		} catch (SQLException e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO(
								"CREA_BUONO_" + richiesta.getDomandaNumero(), "Domanda " + richiesta.getDomandaNumero()
										+ " CREA_BUONO_DOMANDA" + " " + ExceptionUtils.getStackTrace(e),
								numseqbatchKo, utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
					e1.printStackTrace();
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e);
			e.printStackTrace();
		}
	}

	public void eliminaRecordVuoti() {
		try {
			batchDao.DeleteRecordSenzaStepOk();
			batchDao.DeleteRecordSenzaStepKo();
			batchDao.DeleteRecordDaCancellareDuplicati();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// batchcod=RENDICONTAZIONE_BUONI e motivocod=INVIO_RENDICONTAZIONE_BANDI
	public ResponseRest invioRendicontazioneBandi(String numerodomanda, String cf, String batchCod, String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONODOMBANBATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, "INVIO_RENDICONTAZIONE_BANDI_" + numerodomanda);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				String os = System.getProperty("os.name");
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGetBandi(cf,
							"http://localhost:8080/buonodombandisrv/api/v1/invio_rendicontazione_bandi/"
									+ numerodomanda,
							true, null, true);
				} else {
					response = restbase.eseguiGetBandi(cf,
							restbase.getBuonodombandisrvurl() + "/invio_rendicontazione_bandi/" + numerodomanda, true,
							null, true);
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					// inserisco nella det
					if (response.getJson() == null) {
						if (numseqbatchKo != null && numseqbatchKo > 0) {
							// inserisco nella det
							batchDao.inserisciBatchStepKO(
									"INVIO_RENDICONTAZIONE_BANDI_" + numerodomanda, "Domanda " + numerodomanda
											+ " INVIO_RENDICONTAZIONE_BANDI_" + " " + response.getJson(),
									numseqbatchKo, utente);
						}
						return response;
					}
					ObjectMapper mapper = new ObjectMapper();
					ModelBandiMessage body = mapper.readValue(response.getJson(), ModelBandiMessage.class);
					batchDao.inserisciBatchStepOK("INVIO_RENDICONTAZIONE_BANDI_" + numerodomanda,
							"Domanda " + numerodomanda + " INVIO_RENDICONTAZIONE_BANDI_MESSAGEUUID_" + body.getUuid(),
							numseqbatchOk, utente);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO("INVIO_RENDICONTAZIONE_BANDI_" + numerodomanda,
								"Domanda " + numerodomanda + " INVIO_RENDICONTAZIONE_BANDI_" + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("INVIO_RENDICONTAZIONE_BANDI_" + numerodomanda,
								"Domanda " + numerodomanda + " INVIO_RENDICONTAZIONE_BANDI_" + " " + response.getJson(),
								numseqbatchKo, utente);
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
					e1.printStackTrace();
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
			e.printStackTrace();
		}

		return response;
	}

	// batchcod=RENDICONTAZIONE_BUONI e motivocod=SET_FORNITORE
	public String setFornitore(String numerodomanda, String cf, String batchCod, String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONODOMBANBATCH";
		int esitopos = 0;
		Long numseqbatchOk = null;
		Long numseqbatchKo = null;
		String esitoresponse = "OK";
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, "SET_FORNITORE_" + numerodomanda);
			if (esitopos == 0) {
				numseqbatchKo = batchDao.selectEventoKo(numerodomanda, "STATO_KO", batchCod, motivoCod, utente);
				numseqbatchOk = batchDao.selectEventoOk(numerodomanda, "STATO_OK", batchCod, motivoCod, utente);
				String os = System.getProperty("os.name");
				if (os.toLowerCase().contains("win")) {
					response = restbase.eseguiGetBandi(cf,
							"http://localhost:8080/buonodombandisrv/api/v1/fornitore/" + numerodomanda, true, null,
							true);
				} else {
					response = restbase.eseguiGetBandi(cf,
							restbase.getBuonodombandisrvurl() + "/fornitore/" + numerodomanda, true, null, true);
				}
				if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
					ObjectMapper mapper = new ObjectMapper();
					if (response.getJson() == null) {
						if (numseqbatchKo != null && numseqbatchKo > 0) {
							// inserisco nella det
							batchDao.inserisciBatchStepKO("SET_FORNITORE_" + numerodomanda,
									"Domanda " + numerodomanda + " SET_FORNITORE_" + " " + response.getJson(),
									numseqbatchKo, utente);
						}
						return "KO";
					}
					List<ModelBandiMessage> body = Arrays
							.asList(mapper.readValue(response.getJson(), ModelBandiMessage[].class));
					for (ModelBandiMessage singBody : body) {
						if (singBody.getEsitoServizio().equalsIgnoreCase("KO")) {
							esitoresponse = "KO";
							break;
						}
					}
					if (esitoresponse.equalsIgnoreCase("OK")) {
						batchDao.inserisciBatchStepOK("SET_FORNITORE_" + numerodomanda,
								"Domanda " + numerodomanda + " SET_FORNITORE_" + " " + response.getJson(),
								numseqbatchOk, utente);
					} else if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO("SET_FORNITORE_" + numerodomanda,
								"Domanda " + numerodomanda + " SET_FORNITORE_" + " " + response.getJson(),
								numseqbatchKo, utente);
						esitoresponse = "KO";
					}
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO("SET_FORNITORE_" + numerodomanda,
								"Domanda " + numerodomanda + " SET_FORNITORE_" + " " + response.getJson(),
								numseqbatchKo, utente);
						esitoresponse = "KO";
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("SET_FORNITORE_" + numerodomanda,
								"Domanda " + numerodomanda + " SET_FORNITORE_" + " " + response.getJson(),
								numseqbatchKo, utente);
						esitoresponse = "KO";
					}
				} catch (SQLException e1) {
					BatchLoggerFactory.getLogger(this).error("ERRORE SQLException: ", e1);
					e1.printStackTrace();
				}
			}
			BatchLoggerFactory.getLogger(this).error("ERRORE Exception: ", e);
			e.printStackTrace();
		}
		return esitoresponse;
	}

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombatch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.WebServiceException;

import org.apache.commons.lang3.exception.ExceptionUtils;

import it.csi.buonodom.buonodombatch.dao.BuonodomBatchDAO;
import it.csi.buonodom.buonodombatch.dao.BuonodomBatchLogDAO;
import it.csi.buonodom.buonodombatch.dao.DBConnectionManager;
import it.csi.buonodom.buonodombatch.dto.RichiestaDto;
import it.csi.buonodom.buonodombatch.exception.ConnectionException;
import it.csi.buonodom.buonodombatch.exception.ConnectionSoapException;
import it.csi.buonodom.buonodombatch.external.anagraficaservice.InterrogaMefEsenredd1Soap;
import it.csi.buonodom.buonodombatch.external.anagraficaservice.InterrogaMefEsenreddRes;
import it.csi.buonodom.buonodombatch.external.anagraficaservice.ServiziEsterniClient;
import it.csi.buonodom.buonodombatch.integration.rest.RestBaseService;
import it.csi.buonodom.buonodombatch.logger.BatchLoggerFactory;
import it.csi.buonodom.buonodombatch.util.Checker;
import it.csi.buonodom.buonodombatch.util.Constants;
import it.csi.buonodom.buonodombatch.util.Converter;
import it.csi.buonodom.buonodombatch.util.rest.ResponseRest;

public class Main {
	private Connection conn;
	private BuonodomBatchDAO dao = null;
	private ServiziEsterniClient serviziEsterni = null;
	private RestBaseService restbase = null;
	private BuonodomBatchLogDAO batchDao = null;
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
			dao = new BuonodomBatchDAO(this.conn);
			batchDao = new BuonodomBatchLogDAO(this.conn);
			serviziEsterni = ServiziEsterniClient.getInstance(dao);
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
			BatchLoggerFactory.getLogger(this).info("--- PRENDO ELENCO CF CON RICHIESTE APERTE ---");
			List<RichiestaDto> richiesta = new ArrayList<RichiestaDto>();
			ResponseRest response = new ResponseRest();
			Integer contadeceduti = 0;
			Integer contacambioresidenza = 0;
			boolean eseguoresidenza = dao.getParametro(Constants.BATCH_CONTROLLO_DECESSO_RESIDENZA, Constants.GENERICO)
					.toLowerCase().equalsIgnoreCase("true") ? true : false;
			// prendo il parametro della pausa per interrogamef
			Integer pausa = Converter.getInt(dao.getParametro(Constants.PAUSA, Constants.TIPO_PARAMETRO_GIORNI));
			if (eseguoresidenza) {
				richiesta = dao.getRichieste();
				InterrogaMefEsenredd1Soap port = serviziEsterni.creaServizio();
				dao.insertAudit("Inizio Batch", "Conteggio pratiche revocate decesso o cambio residenza", 0);
				for (RichiestaDto rich : richiesta) {
					try {
						// verifico se lo stato della domanda proviene da un pagamento emesso
						// metto una pausa prima di chiamare interrogamef
						Thread.sleep(pausa);
						InterrogaMefEsenreddRes dati = serviziEsterni.chiamaGetInterrogaMef(rich.getCf(), port);
						if (dati.getEsitomef().equalsIgnoreCase("OK")) {
							if (dati.getBody() != null) {
								if (Checker.isValorizzato(dati.getBody().getDatadecesso())) {
									BatchLoggerFactory.getLogger(this).error("--- Cf " + rich.getCf()
											+ " deceduto in data " + dati.getBody().getDatadecesso());
									// revoco la richiesta per decesso
									// se la domanda ammessa o ammessa riserva e la graduatoria provvisoria devo
									// aggiornarla
									if (rich.getStato().equalsIgnoreCase(Constants.AMMESSA)
											|| rich.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA)) {
										long idGraduatoria = dao
												.selectGraduatoriaIdByDomandaDettaglioId(rich.getDomandaDetId());
										if (idGraduatoria != 0) {
											if (dao.checkStatoGraduatoria(idGraduatoria, Constants.PROVVISORIA)) {
												dao.updateRGraduatoriaStato(idGraduatoria, Constants.DA_AGGIORNARE,
														"BUONODOMBATCH");
											}
										}
									}
									dao.executeCambioStatoDomanda(rich.getDomandaDetId(), Constants.DINIEGO,
											"decesso del destinatario", rich.getDomandaNumero());
									response = new ResponseRest();
									response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(),
											"DECESSO", "DECESSO");
									response = new ResponseRest();
									response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(),
											Constants.DINIEGO_SCADENZA_REQUISTI_DECESSO, "DECESSO", "DECESSO");
									response = new ResponseRest();
									response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
											Constants.LETTERA_DINIEGO, "DECESSO", "DECESSO");
									response = new ResponseRest();
									response = getInviaNotifica(rich.getDomandaNumero(),
											Constants.BO_DINIEGO_SCADENZA_REQUISTI_DECESSO, rich.getRichiedenteCf(),
											"DECESSO", "DECESSO");
									contadeceduti = contadeceduti + 1;
									dao.insertAudit(
											rich.getCf() + " deceduto in data " + dati.getBody().getDatadecesso(),
											dati.toString(), 200);
								}
								// commento parte residenza
								else {
									BatchLoggerFactory.getLogger(this)
											.error("--- Cf " + rich.getCf() + " non deceduto controllo residenza ");
									if (dati.getBody().getProvinciaResidenza() != null) {
										if (!dao.IsPiemontese(dati.getBody().getProvinciaResidenza())) {
											BatchLoggerFactory.getLogger(this)
													.error("--- Cf " + rich.getCf() + " cambio residenza");
											// revoco la richiesta per decesso
											// se la domanda ammessa o ammessa riserva e la graduatoria provvisoria devo
											// aggiornarla
											if (rich.getStato().equalsIgnoreCase(Constants.AMMESSA)
													|| rich.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA)) {
												long idGraduatoria = dao.selectGraduatoriaIdByDomandaDettaglioId(
														rich.getDomandaDetId());
												if (idGraduatoria != 0) {
													if (dao.checkStatoGraduatoria(idGraduatoria,
															Constants.PROVVISORIA)) {
														dao.updateRGraduatoriaStato(idGraduatoria,
																Constants.DA_AGGIORNARE, "BUONODOMBATCH");
													}
												}
											}
											dao.executeCambioStatoDomanda(rich.getDomandaDetId(), Constants.DINIEGO,
													"cambio di residenza fuori Piemonte del destinatario",
													rich.getDomandaNumero());
											response = new ResponseRest();
											response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(),
													"CAMBIO_RESIDENZA", "CAMBIO_RESIDENZA");
											response = new ResponseRest();
											response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(),
													Constants.DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA,
													"CAMBIO_RESIDENZA", "CAMBIO_RESIDENZA");
											response = new ResponseRest();
											response = getStartDasPartenza(rich.getDomandaNumero(),
													rich.getRichiedenteCf(), Constants.LETTERA_DINIEGO,
													"CAMBIO_RESIDENZA", "CAMBIO_RESIDENZA");
											response = new ResponseRest();
											response = getInviaNotifica(rich.getDomandaNumero(),
													Constants.BO_DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA,
													rich.getRichiedenteCf(), "CAMBIO_RESIDENZA", "CAMBIO_RESIDENZA");
											contacambioresidenza = contacambioresidenza + 1;
											dao.insertAudit(
													rich.getCf() + " cambio residenza in "
															+ dati.getBody().getComuneResidenza() + " ("
															+ dati.getBody().getProvinciaResidenza() + ")",
													dati.toString(), 200);
										} else {
											// residenza non cambiata
											BatchLoggerFactory.getLogger(this)
													.error("--- Cf " + rich.getCf() + " residenza non cambiata "
															+ dati.getBody().getProvinciaResidenza());
										}
									}
								}
							}
						}
					} catch (Exception e) {
						BatchLoggerFactory.getLogger(this)
								.error("Errore nella gestione della domanda deceduto o residente - "
										+ rich.getDomandaNumero());
						systemReturn = 4;
						continue;
					}
				}
				dao.insertAudit("Fine Batch", "Numero di deceduti " + contadeceduti.toString(), 200);
				dao.insertAudit("Fine Batch", "Numero di cambi residenza " + contacambioresidenza.toString(), 200);
			}
			// per la rettifica selezionare ultimo stato se rettifica conta i giorni se in
			// rettifica prendi lo stato da rettificare con data inzio validita max
			dao.insertAudit("Inizio Batch", "Conteggio pratiche per avvisi", 0);
//			//ciclo per le pratiche su cui mandare notifiche
			Integer avvisirettifica = 0;
			Integer avvisicontrodedotta = 0;
			Integer avvisiammessariserva = 0;
			Integer avvisiammessa = 0;
			Integer numerogiornirettifica = Converter
					.getInt(dao.getParametro(Constants.DA_RETTIFICARE_GIORNI, Constants.TIPO_PARAMETRO_GIORNI));
			Integer numerogiornicontrodedotta = Converter
					.getInt(dao.getParametro(Constants.CONTRODEDOTTA_GIORNI, Constants.TIPO_PARAMETRO_GIORNI));
			Integer avvisiammessaapreavviso = 0;
			Integer avvisipreavvisodiniego = 0;
			Integer numerogiorniammessaapreavviso = Converter.getInt(dao
					.getParametro(Constants.AMMESSA_RISERVA_PREAVVISO_DINIEGO_GIORNI, Constants.TIPO_PARAMETRO_GIORNI));
			Integer numerogiornipreavvisodiniego = Converter.getInt(
					dao.getParametro(Constants.PREAVVISO_DINIEGO_A_DINIEGO_GIORNI, Constants.TIPO_PARAMETRO_GIORNI));
			boolean notificaammessariservapagamento = dao
					.getParametro(Constants.NOTIFICA_AMMESSA_RISERVA_IN_PAGAMENTO, Constants.TIPO_PARAMETRO_GIORNI)
					.toLowerCase().equalsIgnoreCase("true") ? true : false;
			Long idsportellochiuso = dao.getUltimoSportelloChiuso();
			richiesta = dao.getRichieste();
			for (RichiestaDto rich : richiesta) {
				try {
					if ((rich.getStato().equalsIgnoreCase(Constants.DA_RETTIFICARE)
							&& rich.getGiorni() > numerogiornirettifica)
							|| (rich.getStato().equalsIgnoreCase(Constants.IN_RETTIFICA)
									&& dao.getGiorniStatoPrecedente(rich.getDomandaNumero(),
											Constants.DA_RETTIFICARE) > numerogiornirettifica)) {
						dao.executeCambioStatoDomanda(rich.getDomandaDetId(), Constants.NON_AMMISSIBILE,
								"Non ammessa per scadenza dei " + numerogiornirettifica + " giorni di rettifica",
								rich.getDomandaNumero());
						avvisirettifica = avvisirettifica + 1;
						response = new ResponseRest();
						response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.DA_RETTIFICARE);
						dao.insertAudit(
								"Domanda numero " + rich.getDomandaNumero() + " richiedente " + rich.getRichiedenteCf()
										+ " destinatario " + rich.getCf(),
								"Non ammessa per scadenza dei " + numerogiornirettifica + " giorni di rettifica", 200);
					} else if ((rich.getStato().equalsIgnoreCase(Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA)
							&& rich.getGiorni() > numerogiornicontrodedotta)) {
						dao.executeCambioStatoDomanda(
								rich.getDomandaDetId(), Constants.NON_AMMISSIBILE, "Non ammessa per scadenza dei "
										+ numerogiornicontrodedotta + " giorni consentiti per la controdeduzione",
								rich.getDomandaNumero());
						avvisicontrodedotta = avvisicontrodedotta + 1;
						response = new ResponseRest();
						response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA);
						dao.insertAudit(
								"Domanda numero " + rich.getDomandaNumero() + " richiedente " + rich.getRichiedenteCf()
										+ " destinatario " + rich.getCf(),
								"Non ammessa per scadenza dei " + numerogiornicontrodedotta
										+ " giorni consentiti per la controdeduzione",
								200);
					} else if ((rich.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA_IN_PAGAMENTO)
							&& rich.getGiorni() > numerogiorniammessaapreavviso)) {
						dao.executeCambioStatoDomanda(rich.getDomandaDetId(), Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO,
								"Preavviso di diniego per scadenza dei " + avvisiammessaapreavviso
										+ " giorni consentiti per il perfezionamento",
								rich.getDomandaNumero());
						avvisiammessaapreavviso = avvisiammessaapreavviso + 1;
						response = new ResponseRest();
						response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.AMMESSA_RISERVA_IN_PAGAMENTO);
						response = new ResponseRest();
						response = getInviaNotifica(rich.getDomandaNumero(),
								Constants.BO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO, rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.AMMESSA_RISERVA_IN_PAGAMENTO);
						dao.insertAudit(
								"Domanda numero " + rich.getDomandaNumero() + " richiedente " + rich.getRichiedenteCf()
										+ " destinatario " + rich.getCf(),
								"Preavviso di diniego per scadenza dei " + avvisiammessaapreavviso
										+ " giorni consentiti per il perfezionamento",
								200);
					}
					// verifica caso ammessa con riserva in pagamento che proviene dalla
					// pubblicazione per partenza di notifica 30 giorni
					// passo che va fatto solo se non esiste esito positivo
					else if (rich.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA_IN_PAGAMENTO)) {
						response = new ResponseRest();
						response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.AMMESSA_RISERVA);
						response = new ResponseRest();
						response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(),
								Constants.LETTERA_AMMISSIONE_FINANZIAMENTO, "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.AMMESSA_RISERVA);
						if (notificaammessariservapagamento) {
							response = new ResponseRest();
							response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_AMMISSIONE_FINANZIAMENTO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.AMMESSA_RISERVA);
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_AMMESSA_RISERVA_CONTRATTO,
									rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.AMMESSA_RISERVA);
						}
						dao.insertAudit(
								"Domanda numero " + rich.getDomandaNumero() + " richiedente " + rich.getRichiedenteCf()
										+ " destinatario " + rich.getCf(),
								"Ammessa con riserva in pagamento 30 giorni consentiti per il perfezionamento", 200);
					} else if ((rich.getStato().equalsIgnoreCase(Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO)
							&& rich.getGiorni() > numerogiornipreavvisodiniego)) {
						dao.executeCambioStatoDomanda(
								rich.getDomandaDetId(), Constants.DINIEGO, "Diniego per scadenza dei "
										+ numerogiornipreavvisodiniego + " giorni consentiti per il perfezionamento",
								rich.getDomandaNumero());
						avvisipreavvisodiniego = avvisipreavvisodiniego + 1;
						response = new ResponseRest();
						response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO);
						response = new ResponseRest();
						response = getCreaLettera(rich.getDomandaNumero(), rich.getRichiedenteCf(),
								Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO);
						response = new ResponseRest();
						response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
								Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO);
						response = new ResponseRest();
						response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_DINIEGO,
								rich.getRichiedenteCf(), "CAMBIO_STATO",
								"CAMBIO_STATO_DOMANDA_" + Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO);
						dao.insertAudit(
								"Domanda numero " + rich.getDomandaNumero() + " richiedente " + rich.getRichiedenteCf()
										+ " destinatario " + rich.getCf(),
								"Diniego per scadenza dei " + numerogiornipreavvisodiniego
										+ " giorni consentiti per il perfezionamento",
								200);
					}
					// caso della graduatoria cambio stato da ammissibile ad ammessa e ammessa con
					// riserva
					else if (rich.getStato().equalsIgnoreCase(Constants.AMMISSIBILE)
							&& rich.getSportelloId().equals(idsportellochiuso)) {
						Boolean incompatibilita = dao.getIncompatibilita(rich.getDomandaDetId());
						// AMMESSA CON RISERVA
						if (incompatibilita) {
							dao.executeCambioStatoDomanda(rich.getDomandaDetId(), Constants.AMMESSA_RISERVA,
									"Batch passaggio ad Ammessa con riserva in graduatoria", rich.getDomandaNumero());
							avvisiammessariserva = avvisiammessariserva + 1;
							response = new ResponseRest();
							response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.AMMISSIBILE);
							dao.insertAudit(
									"Domanda numero " + rich.getDomandaNumero() + " richiedente "
											+ rich.getRichiedenteCf() + " destinatario " + rich.getCf(),
									"Passaggio ad ammessa con riserva in graduatoria", 200);
						}
						// AMMESSA
						else {
							dao.executeCambioStatoDomanda(rich.getDomandaDetId(), Constants.AMMESSA,
									"Batch passaggio ad Ammessa in graduatoria", rich.getDomandaNumero());
							avvisiammessa = avvisiammessa + 1;
							response = new ResponseRest();
							response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.AMMISSIBILE);
							dao.insertAudit(
									"Domanda numero " + rich.getDomandaNumero() + " richiedente "
											+ rich.getRichiedenteCf() + " destinatario " + rich.getCf(),
									"Passaggio ad ammessa in graduatoria", 200);
						}

						long idGraduatoria = dao.selectGraduatoriaIdByDomandaDettaglioId(rich.getDomandaDetId());
						if (idGraduatoria != 0) {
							if (dao.checkStatoGraduatoria(idGraduatoria, Constants.PROVVISORIA)) {
								dao.updateRGraduatoriaStato(idGraduatoria, Constants.DA_AGGIORNARE, "BUONODOMBATCH");
							}
						}
					}
				} catch (Exception e) {
					BatchLoggerFactory.getLogger(this)
							.error("Errore nella gestione della domanda cambi stato - " + rich.getDomandaNumero());
					systemReturn = 6;
					continue;
				}
			}
			Integer avvisidinieghi = 0;
			boolean notificadiniego = dao.getParametro(Constants.NOTIFICA_DINIEGO, Constants.TIPO_PARAMETRO_GIORNI)
					.toLowerCase().equalsIgnoreCase("true") ? true : false;
			// determina effettuata
			if (notificadiniego) {
				richiesta = dao.getRichiesteDiniego();
				for (RichiestaDto rich : richiesta) {
					try {
						boolean procedinotifica = false;
						long idGraduatoria = dao.selectGraduatoriaIdByDomandaDettaglioId(rich.getDomandaDetId());
						if (idGraduatoria != 0) {
							if (dao.checkStatoGraduatoria(idGraduatoria, Constants.PUBBLICATA)) {
								procedinotifica = true;
							}
						}
						String statoultimo = dao.selectStatoPrecedente(rich.getDomandaNumero());
						if (statoultimo.contains(Constants.AMMESSA) && procedinotifica) {
							response = new ResponseRest();
							response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							// invio a stardas in partenza
							response = new ResponseRest();
							response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_DINIEGO,
									rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							avvisidinieghi = avvisidinieghi + 1;
						} else if (statoultimo.contains(Constants.CONTRODEDOTTA) && procedinotifica) { // Stato
																										// CONTRODEDOTTA?
							response = new ResponseRest();
							response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							// invio a stardas in partenza
							response = new ResponseRest();
							response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_DINIEGO,
									rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							avvisidinieghi = avvisidinieghi + 1;
						} else if (statoultimo.contains(Constants.NON_AMMISSIBILE) && procedinotifica) { // Stato
																											// NON_AMMISSIBILE?
							response = new ResponseRest();
							response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							// invio a stardas in partenza
							response = new ResponseRest();
							response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_DINIEGO,
									rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							avvisidinieghi = avvisidinieghi + 1;
						} else if (statoultimo.contains(Constants.AMMESSA_RISERVA) && procedinotifica) { // Stato
							response = new ResponseRest();
							response = getCreaDomanda(rich.getDomandaNumero(), rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);// AMMESSA_CON_RISERVA?
							// invio a stardas in partenza
							response = new ResponseRest();
							response = getStartDasPartenza(rich.getDomandaNumero(), rich.getRichiedenteCf(),
									Constants.LETTERA_DINIEGO, "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							response = new ResponseRest();
							response = getInviaNotifica(rich.getDomandaNumero(), Constants.BO_DINIEGO,
									rich.getRichiedenteCf(), "CAMBIO_STATO",
									"CAMBIO_STATO_DOMANDA_" + Constants.DINIEGO);
							avvisidinieghi = avvisidinieghi + 1;
						}
					} catch (Exception e) {
						BatchLoggerFactory.getLogger(this)
								.error("Errore nella gestione della domanda diniego - " + rich.getDomandaNumero());
						systemReturn = 7;
						continue;
					}
				}
			}
			dao.insertAudit("Fine Batch",
					"Numero notifiche per stato " + Constants.DA_RETTIFICARE + " " + avvisirettifica.toString(), 200);
			dao.insertAudit("Fine Batch", "Numero notifiche per stato "
					+ Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA + " " + avvisicontrodedotta.toString(), 200);
			dao.insertAudit("Fine Batch", "Numero notifiche per stato " + Constants.AMMESSA_RISERVA_IN_PAGAMENTO + " "
					+ avvisiammessaapreavviso.toString(), 200);
			dao.insertAudit("Fine Batch", "Numero notifiche per stato " + Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO + " "
					+ avvisipreavvisodiniego.toString(), 200);
			dao.insertAudit("Fine Batch",
					"Numero notifiche per stato " + Constants.AMMESSA_RISERVA + " " + avvisiammessariserva.toString(),
					200);
			dao.insertAudit("Fine Batch",
					"Numero notifiche per stato " + Constants.AMMESSA + " " + avvisiammessa.toString(), 200);
			dao.insertAudit("Fine Batch", "Numero notifiche per stato " + Constants.DINIEGO
					+ " invio notifiche post determina " + avvisidinieghi.toString(), 200);
			// check and count errors from t batch tables
			int errorCount = batchDao.countBatchExecutionErrors();
			if (errorCount == -1) {
				BatchLoggerFactory.getLogger(this).error(
						"--- ERRORE countBatchExecutionErrors: non è possibile contare gli errori sul database");
			} else {
				systemReturn = errorCount > 0 ? 8 : 0;
			}
		} catch (Exception e) {
			systemReturn = 5;
			BatchLoggerFactory.getLogger(this).error("--- ERRORE GENERICO: --- ", e);
			e.printStackTrace();
			String msg = null;
			try {
				if (e instanceof WebServiceException) {
					systemReturn = 3;
					BatchLoggerFactory.getLogger(this).error("--- ERRORE WEBSERVICE: --- ", e);
					msg = ((WebServiceException) e).getMessage();
				} else if (e instanceof SQLException) {
					systemReturn = 2;
					msg = ((SQLException) e).getMessage();
					BatchLoggerFactory.getLogger(this).error("--- ERRORE QUERY1: --- ", e);
				}
				// log error
				dao.insertAudit("Errore generico durante la chiamata a InterrogaMef", ExceptionUtils.getStackTrace(e),
						500);

			} catch (SQLException e2) {
				systemReturn = 1;
				BatchLoggerFactory.getLogger(this).error("--- ERRORE QUERY: --- ", e2);
				e2.printStackTrace();
			}
		} finally {
			BatchLoggerFactory.getLogger(this).info("--- CANCELLO RECORD DUPLICATI NELLE TABELLE DI LOG ---");
			eliminaRecordVuoti();
			BatchLoggerFactory.getLogger(this).info("--- END BATCH ---");
			System.exit(systemReturn);
			dao.closeAll();
		}

	}

	public ResponseRest getInviaNotifica(String numerodomanda, String tipoNotifica, String cf, String batchCod,
			String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONODOM_BATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, "INVIO_NOTIFICA_" + tipoNotifica);
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
					batchDao.inserisciBatchStepOK("INVIO_NOTIFICA_" + tipoNotifica,
							"CAMBIO_STATO_DOMANDA_" + tipoNotifica, numseqbatchOk, utente);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO("INVIO_NOTIFICA_" + tipoNotifica,
								"CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("INVIO_NOTIFICA_" + tipoNotifica,
								"CAMBIO_STATO_DOMANDA_" + tipoNotifica + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getCreaDomanda(String numerodomanda, String cf, String batchCod, String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONODOM_BATCH";
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		int esitopos = 0;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, motivoCod);
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
					// inserisco nella det
					batchDao.inserisciBatchStepOK(motivoCod, motivoCod, numseqbatchOk, utente);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(motivoCod, motivoCod + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO(motivoCod, motivoCod + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getStartDasPartenza(String numerodomanda, String cf, String tipoLettera, String batchCod,
			String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONODOM_BATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, "STARDAS_PARTENZA_" + tipoLettera);
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
					batchDao.inserisciBatchStepOK("STARDAS_PARTENZA_" + tipoLettera,
							"CAMBIO_STATO_DOMANDA_" + tipoLettera, numseqbatchOk, utente);
				} else {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						// inserisco nella det
						batchDao.inserisciBatchStepKO("STARDAS_PARTENZA_" + tipoLettera,
								"CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("STARDAS_PARTENZA_" + tipoLettera,
								"CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest getCreaLettera(String numerodomanda, String cf, String tipoLettera, String batchCod,
			String motivoCod) {
		ResponseRest response = new ResponseRest();
		String utente = "BUONODOM_BATCH";
		int esitopos = 0;
		Long numseqbatchKo = null;
		Long numseqbatchOk = null;
		try {
			esitopos = batchDao.selectEsitoPositivo(numerodomanda, "CREA_LETTERA_" + tipoLettera);
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
					batchDao.inserisciBatchStepOK("CREA_LETTERA_" + tipoLettera, "CAMBIO_STATO_DOMANDA_" + tipoLettera,
							numseqbatchOk, utente);
				} else {
					// inserisco nella det
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("CREA_LETTERA_" + tipoLettera,
								"CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				}
			}
		} catch (Exception e) {
			if (esitopos == 0) {
				try {
					if (numseqbatchKo != null && numseqbatchKo > 0) {
						batchDao.inserisciBatchStepKO("CREA_LETTERA_" + tipoLettera,
								"CAMBIO_STATO_DOMANDA_" + tipoLettera + " " + response.getJson(), numseqbatchKo,
								utente);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}

		return response;
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

}

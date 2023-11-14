/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.business.be.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.buonodom.buonodomsrv.business.be.service.base.BaseService;
import it.csi.buonodom.buonodomsrv.dto.ModelIncompatibilitaRichiesta;
import it.csi.buonodom.buonodomsrv.dto.ModelRichiesta;
import it.csi.buonodom.buonodomsrv.dto.ModelSportello;
import it.csi.buonodom.buonodomsrv.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodomsrv.exception.DatabaseException;
import it.csi.buonodom.buonodomsrv.exception.ResponseErrorException;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.SportelliDao;
import it.csi.buonodom.buonodomsrv.integration.notificatore.NotificatoreService;
import it.csi.buonodom.buonodomsrv.integration.notificatore.enumerator.NotificatoreEventCode;
import it.csi.buonodom.buonodomsrv.integration.service.util.FilesEncrypt;
import it.csi.buonodom.buonodomsrv.util.Constants;
import it.csi.buonodom.buonodomsrv.util.ErrorBuilder;
import it.csi.buonodom.buonodomsrv.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodomsrv.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodomsrv.util.validator.impl.ValidateGenericImpl;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

@Service
public class CreaLetteraService extends BaseService {

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	CodParametroDao parametroDao;

	@Autowired
	SportelliDao sportelliDao;

	@Autowired
	private FilesEncrypt filesEncrypt;

	@Autowired
	private NotificatoreService notificatore;

	public Response creaLettera(String numeroRichiesta, String tipoLettera, String xRequestId, String xForwardedFor,
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
			List<ErroreDettaglioExt> listError = validateGeneric.validateLettera(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, numeroRichiesta, tipoLettera, securityContext, httpHeaders,
					httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			if (!richiesta.getStato().equalsIgnoreCase(Constants.DINIEGO)
					&& !richiesta.getStato().equalsIgnoreCase(Constants.IN_PAGAMENTO)
					&& !richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA)
					&& !richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA)
					&& !richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA_IN_PAGAMENTO)) {
				param[0] = "Domanda in stato non coerente";
				logError(metodo, "Errore Crea Lettera ", param[0]);
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
						"errore in crea lettera");
			}
			byte[] lettera = null;
			Map<String, Object> parameters = new HashMap<String, Object>();
			logInfo("creo pdf parametri", "pdf lettera prendendo notifica");
			boolean trovato = false;
			if (richiesta != null) {
				String statoultimo = richiesteDao.selectStatoPrecedente(richiesta.getNumero());
				if (richiesta.getStato().equalsIgnoreCase(Constants.DINIEGO)
						&& tipoLettera.equalsIgnoreCase(Constants.LETTERA_DINIEGO)) {
					parameters.put("TIPO_LETTERA", Constants.LETTERA_DINIEGO.replace("_", " "));
					String statopernultimo = richiesteDao.selectStatoPenultimo(richiesta.getNumero());
					String datacontrodedotta = null;
					datacontrodedotta = richiesteDao.selectDataControdedotta(richiesta.getNumero());
					boolean preavvisodiniego = false;
					boolean rettifica = false;
					boolean controdedotta = false;
					boolean preavvisodiniegoinpagamento = false;
					boolean perfezionatainpagamento = false;
					if (statopernultimo != null && statoultimo != null) {
						if (statopernultimo.contains(Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA)
								&& statoultimo.contains(Constants.NON_AMMISSIBILE)) {
							preavvisodiniego = true;
						} else if (statopernultimo.contains(Constants.DA_RETTIFICARE)
								|| statopernultimo.contains(Constants.IN_RETTIFICA)
										&& statoultimo.contains(Constants.NON_AMMISSIBILE)) {
							rettifica = true;
						} else if (statoultimo.contains(Constants.CONTRODEDOTTA)) {
							controdedotta = true;
						} else if (statoultimo.contains(Constants.NON_AMMISSIBILE)
								&& statopernultimo.contains(Constants.CONTRODEDOTTA)) {
							controdedotta = true;
						} else if (statoultimo.contains(Constants.AMMESSA)
								|| statoultimo.contains(Constants.AMMESSA_RISERVA)) {
							rettifica = true;
						} else if (statoultimo.contains(Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO)) {
							preavvisodiniegoinpagamento = true;
						} else if (statoultimo.contains(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
							perfezionatainpagamento = true;
						}
					}
					// messagio 3.4.3 si arriva da preavviso di diniego per non ammissibilita non
					// invia le controdeduzioni dopo averle richieste
					// prendo il penultimo stato e vedo se preavviso di diniego per non
					// ammissibilita
					if (preavvisodiniego) {
						trovato = true;
						parameters.put("TESTO_EMAIL", buildBody(
								parametroDao.selectValoreParametroFromCod(
										NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_DOPO_PREAVVISO.getCode(),
										Constants.NOTIFICATORE),
								richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
								richiesta.getNumero(), richiesta.getNote(), datacontrodedotta));
					}
					// messagio 3.4.5 si arriva da in rettifica rettificata non richieste
					// controdeduzioni
					// prendo il penultimo stato e vedo se da rettificare e rettifica
					else if (rettifica) {
						trovato = true;
						parameters.put("TESTO_EMAIL", buildBody(
								parametroDao.selectValoreParametroFromCod(
										NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SENZA_PREAVVISO.getCode(),
										Constants.NOTIFICATORE),
								richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
								richiesta.getNumero(), richiesta.getNote(), datacontrodedotta));
					}
					// messagio 3.4.4 si arriva da controdedotta
					// prendo il penultimo stato e vedo se da controdedotta
					else if (controdedotta) {
						trovato = true;
						parameters.put("TESTO_EMAIL", buildBody(
								parametroDao.selectValoreParametroFromCod(
										NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_CON_CONTRODEDUZIONI.getCode(),
										Constants.NOTIFICATORE),
								richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
								richiesta.getNumero(), richiesta.getNote(), datacontrodedotta));
					} else if (preavvisodiniegoinpagamento) {
						trovato = true;
						parameters.put("TESTO_EMAIL", buildBody(parametroDao.selectValoreParametroFromCod(
								NotificatoreEventCode.MSG_BODY_MAIL_DINIEGO_DOPO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO
										.getCode(),
								Constants.NOTIFICATORE), richiesta.getRichiedente().getNome(),
								richiesta.getRichiedente().getCognome(), richiesta.getNumero(), richiesta.getNote(),
								datacontrodedotta));
					} else if (perfezionatainpagamento) {
						trovato = true;
						parameters.put("TESTO_EMAIL", buildBody(
								parametroDao.selectValoreParametroFromCod(
										NotificatoreEventCode.MSG_BODY_MAIL_DINIEGO_DOPO_PERFEZIONATA_IN_PAGAMENTO
												.getCode(),
										Constants.NOTIFICATORE),
								richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
								richiesta.getNumero(), richiesta.getNote(), datacontrodedotta));
					}

				} else if (richiesta.getStato().equalsIgnoreCase(Constants.DINIEGO)
						&& tipoLettera.equalsIgnoreCase(Constants.DINIEGO_SCADENZA_REQUISTI_DECESSO)) {
					parameters.put("TIPO_LETTERA", Constants.LETTERA_DINIEGO.replace("_", " "));
					trovato = true;
					parameters.put("TESTO_EMAIL", buildBody(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SCADENZA_REQUISTI_DECESSO.getCode(),
									Constants.NOTIFICATORE),
							richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
							richiesta.getNumero(), richiesta.getNote(), null));
				} else if (richiesta.getStato().equalsIgnoreCase(Constants.DINIEGO)
						&& tipoLettera.equalsIgnoreCase(Constants.DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA)) {
					parameters.put("TIPO_LETTERA", Constants.LETTERA_DINIEGO.replace("_", " "));
					trovato = true;
					parameters.put("TESTO_EMAIL", buildBody(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA
											.getCode(),
									Constants.NOTIFICATORE),
							richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
							richiesta.getNumero(), richiesta.getNote(), null));
				} else if (richiesta.getStato().equalsIgnoreCase(Constants.DINIEGO)
						&& tipoLettera.equalsIgnoreCase(Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO)) {
					parameters.put("TIPO_LETTERA", Constants.LETTERA_DINIEGO.replace("_", " "));
					trovato = true;
					parameters.put("TESTO_EMAIL", buildBody(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SCADENZA_REQUISTI.getCode(),
									Constants.NOTIFICATORE),
							richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
							richiesta.getNumero(), richiesta.getNote(), null));
				} else if (richiesta.getStato().equalsIgnoreCase(Constants.IN_PAGAMENTO)
						&& tipoLettera.equalsIgnoreCase(Constants.LETTERA_AMMISSIONE_FINANZIAMENTO)) {
					parameters.put("TIPO_LETTERA", Constants.LETTERA_AMMISSIONE_FINANZIAMENTO.replace("_", " "));
					trovato = true;
					if (statoultimo.contains(Constants.PERFEZIONATA_IN_PAGAMENTO))
						parameters.put("TESTO_EMAIL", buildBody(
								parametroDao.selectValoreParametroFromCod(
										NotificatoreEventCode.MSG_BODY_MAIL_BO_PERFEZIONATA_IN_PAGAMENTO.getCode(),
										Constants.NOTIFICATORE),
								richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
								richiesta.getNumero(), richiesta.getNote(), null));
					else
						parameters.put("TESTO_EMAIL",
								buildBody(
										parametroDao.selectValoreParametroFromCod(
												NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_FINANZIATA.getCode(),
												Constants.NOTIFICATORE),
										richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
										richiesta.getNumero(), richiesta.getNote(), null));
				} else if (richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA_IN_PAGAMENTO)
						&& tipoLettera.equalsIgnoreCase(Constants.LETTERA_AMMISSIONE_FINANZIAMENTO)) {
					parameters.put("TIPO_LETTERA", Constants.LETTERA_AMMISSIONE_FINANZIAMENTO.replace("_", " "));
					trovato = true;
					ModelIncompatibilitaRichiesta incompatibilita = richiesteDao
							.selectIncompatibilitaRichiesta(richiesta.getNumero());
					if (incompatibilita != null) {
						if (incompatibilita.isIncompatibilitaPerContratto()
								&& !incompatibilita.isNessunaIncompatibilita()) {
							parameters.put("TESTO_EMAIL", buildBody(parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_BODY_MAIL_BO_AMMESSA_RISERVA_INCOMPATIBILITA_CONTRATTO
											.getCode(),
									Constants.NOTIFICATORE), richiesta.getRichiedente().getNome(),
									richiesta.getRichiedente().getCognome(), richiesta.getNumero(), richiesta.getNote(),
									null));
						} else if (incompatibilita.isIncompatibilitaPerContratto()) {
							parameters.put("TESTO_EMAIL",
									buildBody(parametroDao.selectValoreParametroFromCod(
											NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_RISERVA_CONTRATTO.getCode(),
											Constants.NOTIFICATORE), richiesta.getRichiedente().getNome(),
											richiesta.getRichiedente().getCognome(), richiesta.getNumero(),
											richiesta.getNote(), null));
						} else if (!incompatibilita.isNessunaIncompatibilita()) {
							parameters.put("TESTO_EMAIL", buildBody(
									parametroDao.selectValoreParametroFromCod(
											NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_RISERVA_INCOMPATIBILITA
													.getCode(),
											Constants.NOTIFICATORE),
									richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
									richiesta.getNumero(), richiesta.getNote(), null));
						}
					}
				} else if ((richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA)
						|| richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA))
						&& tipoLettera.equalsIgnoreCase(Constants.LETTERA_AMMISSIONE_NON_FINANZIAMENTO)) {
					parameters.put("TIPO_LETTERA", Constants.LETTERA_AMMISSIONE_NON_FINANZIAMENTO.replace("_", " "));
					trovato = true;
					parameters.put("TESTO_EMAIL", buildBody(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_NON_FINANZIATA.getCode(),
									Constants.NOTIFICATORE),
							richiesta.getRichiedente().getNome(), richiesta.getRichiedente().getCognome(),
							richiesta.getNumero(), richiesta.getNote(), null));
				}
				if (trovato) {
					lettera = getPdfLettera(parameters);
					logInfo("creo pdf fine parametri", "pdf lettera");
					if (lettera.length < 1000) // NO DATA
						lettera = null;
					if (lettera != null)
						archivialettera(lettera, richiesta.getNumero(), richiesta.getRichiedente().getCf(),
								richiesta.getDomandaDetCod(), richiesta.getSportelloId(), richiesta.getDomandaDetId(),
								tipoLettera);
					else {
						param[0] = "Errore nella denerazione del PDF della lettera";
						logError(metodo, "Errore Crea Lettera ", param[0]);
						listerrorservice
								.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
								"errore in crea lettera");
					}
				}
			}
			return Response.ok().entity(richiesta).build();
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

	@Transactional(readOnly = true)
	public byte[] getPdfLettera(Map<String, Object> parameters) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream input = classloader.getResourceAsStream("/report/lettera.jasper");
		JasperReport jasperReport = null;
		try {
			JRPdfExporter exporter = new JRPdfExporter();

			jasperReport = (JasperReport) JRLoader.loadObject(input);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			logInfo("pdf", "esporter input");
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			logInfo("fine creo pdf", "pdf lettera");
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				logError("IOException pdf", "Errore crea lettera", e);
				return null;
			}

			SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
			reportConfig.setSizePageToContent(true);
			reportConfig.setForceLineBreakPolicy(false);
			SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();

			exportConfig.setMetadataAuthor("SISTEMA PIEMONTE");
			exportConfig.setAllowedPermissionsHint("PRINTING");

			exporter.setConfiguration(reportConfig);
			exporter.setConfiguration(exportConfig);
			exporter.exportReport();

			return outputStream.toByteArray();
		} catch (JRException e) {
			logError("JRException pdf", "Errore jasper", e);
			e.printStackTrace();
			return null;
		}

	}

	public void archivialettera(byte[] lettera, String numerodomanda, String richiedente, String detCod,
			BigDecimal sportelloid, BigDecimal domandadetid, String tipoLettera) throws DatabaseException, IOException {
		String path = parametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO);
		ModelSportello sportello = new ModelSportello();
		try {
			String nomefile = null;
			if (tipoLettera.contains(Constants.DINIEGO))
				nomefile = Constants.LETTERA_DINIEGO;
			else if (tipoLettera.contains(Constants.AMMISSIONE_FINANZIAMENTO))
				nomefile = Constants.LETTERA_AMMISSIONE_FINANZIAMENTO;
			else if (tipoLettera.contains(Constants.AMMISSIONE_NON_FINANZIAMENTO))
				nomefile = Constants.LETTERA_AMMISSIONE_NON_FINANZIAMENTO;
			// LOCALE (assicurarsi di avere la cartella condivisa tmp il locale)
			String os = System.getProperty("os.name");
			boolean locale = false;
			if (os.toLowerCase().contains("win")) {
				path = "c:\\temp\\";
				locale = true;
			} else
				path += "/";
			sportello = sportelliDao.selectSportelli(sportelloid);
			path += sportello.getSportelloCod();
			File dir = new File(path + "/");
			if (!dir.exists()) {
				dir.mkdirs();
				logInfo("creo cartella", dir.getPath());
			} else
				logInfo("cartella esiste ", dir.getPath());
			if (!locale)
				path += "/" + richiedente.substring(0, 1).toUpperCase();
			else
				path += "\\" + richiedente.substring(0, 1).toUpperCase();
			dir = new File(path + "/");
			if (!dir.exists()) {
				dir.mkdirs();
				logInfo("creo cartella", dir.getPath());
			} else
				logInfo("cartella esiste ", dir.getPath());
			// aggiunta cartella numero domanda
			if (!locale)
				path += "/" + numerodomanda.toUpperCase();
			else
				path += "\\" + numerodomanda.toUpperCase();
			dir = new File(path + "/");
			if (!dir.exists()) {
				dir.mkdirs();
				logInfo("creo cartella", dir.getPath());
			} else
				logInfo("cartella esiste ", dir.getPath());
			if (!locale)
				path += "/" + detCod.toUpperCase();
			else
				path += "\\" + detCod.toUpperCase();
			dir = new File(path + "/");
			if (!dir.exists()) {
				dir.mkdirs();
				logInfo("creo cartella", dir.getPath());
			} else
				logInfo("cartella esiste ", dir.getPath());

			File file = null;
			if (!locale)
				file = new File(path + "/" + nomefile + ".pdf");
			else
				file = new File(path + "\\" + nomefile + ".pdf");
			logInfo("scrivo allegato su db", "pdf lettera " + path + " /" + nomefile + ".pdf");
			// faccio update o insert su tabella allegato
			if (richiesteDao.selectEsisteAllegato(detCod, nomefile))
				richiesteDao.updateAllegato(detCod, richiedente, nomefile + ".pdf", "application/pdf", path,
						"nomefile");
			else
				richiesteDao.insertAllegato(nomefile + ".pdf", "application/pdf", path, sportelloid, domandadetid,
						detCod, nomefile, richiedente, richiedente);

			logInfo("creo pdf archivio ", "pdf lettera" + path + " /" + nomefile + ".pdf");
			OutputStream out = new FileOutputStream(file);
			// cripto il file
			byte[] domandaCifrata = filesEncrypt.creaFileCifratoByte(Cipher.ENCRYPT_MODE, lettera);
			out.write(domandaCifrata, 0, domandaCifrata.length);
//		out.write(lettera, 0, lettera.length);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			logError("Archiviazione PDF lettera", "Errore archivia lettera", e);
		}
	}

	private String buildBody(String msg, String nome, String cognome, String numero, String motivo,
			String datacontrodedotta) {
		String result = msg;
		result = result.replace("{0}", nome + " " + cognome);
		result = result.replace("{1}", numero);
		if (StringUtils.isNotBlank(motivo)) {
			result = result.replace("{3}", motivo);
		} else
			result = result.replace("{3}", "");
		if (StringUtils.isNotBlank(datacontrodedotta)) {
			result = result.replace("{4}", datacontrodedotta);
		} else
			result = result.replace("{4}", "");
		return result;
	}
}

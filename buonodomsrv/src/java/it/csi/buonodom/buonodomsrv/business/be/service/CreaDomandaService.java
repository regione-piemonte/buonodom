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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.buonodom.buonodomsrv.business.be.service.base.BaseService;
import it.csi.buonodom.buonodomsrv.dto.Contact;
import it.csi.buonodom.buonodomsrv.dto.ModelGetAllegatoExt;
import it.csi.buonodom.buonodomsrv.dto.ModelRichiesta;
import it.csi.buonodom.buonodomsrv.dto.ModelSportello;
import it.csi.buonodom.buonodomsrv.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodomsrv.exception.DatabaseException;
import it.csi.buonodom.buonodomsrv.exception.ResponseErrorException;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.SportelliDao;
import it.csi.buonodom.buonodomsrv.integration.notificatore.NotificatoreService;
import it.csi.buonodom.buonodomsrv.integration.service.util.FilesEncrypt;
import it.csi.buonodom.buonodomsrv.util.Constants;
import it.csi.buonodom.buonodomsrv.util.Converter;
import it.csi.buonodom.buonodomsrv.util.ErrorBuilder;
import it.csi.buonodom.buonodomsrv.util.Util;
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
public class CreaDomandaService extends BaseService {

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

	public Response creaDomanda(String numeroRichiesta, String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
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
			byte[] domanda = null;
			if (richiesta != null) {
				domanda = creadomanda(richiesta, shibIdentitaCodiceFiscale);
				if (domanda != null)
					archiviadomanda(domanda, richiesta.getNumero(), richiesta.getRichiedente().getCf(),
							richiesta.getDomandaDetCod(), richiesta.getSportelloId(), richiesta.getDomandaDetId());
				else {
					param[0] = "Errore nella denerazione del PDF Domanda";
					logError(metodo, "Errore Crea Domanda ", param[0]);
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
							"errore in crea domanda");
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
	public byte[] getPdfDomanda(Map<String, Object> parameters) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		// InputStream input = classloader.getResourceAsStream("/report/domanda.jrxml");
		InputStream input = classloader.getResourceAsStream("/report/domanda.jasper");
		JasperReport jasperReport = null;
		try {
//			logInfo("creo pdf", "pdf domanda");
//			jasperReport = JasperCompileManager.compileReport(input);
//			logInfo("fine creo pdf", "pdf domanda compilata");
//			JasperPrint jasperPrint = null;
//			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
//			logInfo("pdf", "fill report");
			JRPdfExporter exporter = new JRPdfExporter();

			jasperReport = (JasperReport) JRLoader.loadObject(input);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			logInfo("pdf", "esporter input");
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			logInfo("fine creo pdf", "pdf domanda");
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				logError("IOException pdf", "Errore crea domanda", e);
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

	public byte[] creadomanda(ModelRichiesta domanda, String iride) throws DatabaseException {

		Map<String, Object> parameters = new HashMap<String, Object>();
		logInfo("creo pdf parametri", "pdf domanda parametri");
		try {
			if (Util.isValorizzato(domanda.getNumero())) {
				parameters.put("NUMERO_DOMANDA", domanda.getNumero().toUpperCase());
			}
			if (Util.isValorizzato(domanda.getDomandaStatoDesc())) {
				parameters.put("STATO", domanda.getDomandaStatoDesc().toUpperCase());
			} else
				parameters.put("STATO", "");
			if (domanda.getRichiedente() != null) {
				String parametrocontatti = parametroDao.selectValoreParametroFromCod(Constants.CHIAMA_CONTATTI,
						Constants.PARAMETRO_GENERICO);
				Contact contatto = new Contact();
				boolean verificasechiamarecontatti = true;
				if (parametrocontatti != null)
					verificasechiamarecontatti = parametrocontatti.equalsIgnoreCase("TRUE") ? true : false;
				if (verificasechiamarecontatti) {
					contatto = notificatore.notificaContact(domanda.getRichiedente().getCf(), iride);
				} else {
					contatto.setEmail("provamail@testcarico.it");
					contatto.setSms("cellcarico");
					contatto.setPhone("telcarico");
				}
				parameters.put("RICHIEDENTE_DENOMINAZIONE", domanda.getRichiedente().getCognome().toUpperCase() + " "
						+ domanda.getRichiedente().getNome().toUpperCase());
				if (Util.isValorizzato(domanda.getRichiedente().getComuneNascita()))
					parameters.put("RICHIEDENTE_NATOA", domanda.getRichiedente().getComuneNascita().toUpperCase() + " ("
							+ domanda.getRichiedente().getProvinciaNascita().toUpperCase() + ")");
				else
					parameters.put("RICHIEDENTE_NATOA", domanda.getRichiedente().getStatoNascita().toUpperCase());
				parameters.put("RICHIEDENTE_NATOIL", Converter.getDataISO(domanda.getRichiedente().getDataNascita()));
				parameters.put("RICHIEDENTE_RESIDENTEA", domanda.getRichiedente().getComuneResidenza().toUpperCase()
						+ " (" + domanda.getRichiedente().getProvinciaResidenza().toUpperCase() + ")");
				parameters.put("RICHIEDENTE_RESIDENTEIND",
						domanda.getRichiedente().getIndirizzoResidenza().toUpperCase());
				parameters.put("RICHIEDENTE_CF", domanda.getRichiedente().getCf().toUpperCase());
				if (domanda.getDomicilioDestinatario() != null) {
					if (domanda.getDomicilioDestinatario().getComune() != null) {
						parameters.put("DESTINATARIO_DOMICILIO",
								domanda.getDomicilioDestinatario().getComune().toUpperCase());
					} else
						parameters.put("DESTINATARIO_DOMICILIO", "");
					if (domanda.getDomicilioDestinatario().getIndirizzo() != null) {
						parameters.put("DESTINATARIO_DOMICILIOIND",
								domanda.getDomicilioDestinatario().getIndirizzo().toUpperCase());
					} else
						parameters.put("DESTINATARIO_DOMICILIOIND", "");
				}
				if (Util.isValorizzato(domanda.getAslDestinatario()))
					parameters.put("ASL", domanda.getAslDestinatario());
				else
					parameters.put("ASL", "");
				if (domanda.isLavoroDestinatario() != null) {
					if (domanda.isLavoroDestinatario()) {
						parameters.put("LAVORO_DESTINATARIO", true);
					} else
						parameters.put("LAVORO_DESTINATARIO", false);
				} else
					parameters.put("LAVORO_DESTINATARIO", null);
				if (contatto != null) {
					if (contatto.getSms() != null)
						parameters.put("RICHIEDENTE_TEL", contatto.getSms());
					else if (contatto.getPhone() != null)
						parameters.put("RICHIEDENTE_TEL", contatto.getPhone());
					else
						parameters.put("RICHIEDENTE_TEL", "");
					if (contatto.getEmail() != null)
						parameters.put("RICHIEDENTE_MAIL", contatto.getEmail());
					else
						parameters.put("RICHIEDENTE_MAIL", "");
				}
				if (domanda.getRichiedente().getCf().equalsIgnoreCase(domanda.getDestinatario().getCf())) {
					parameters.put("SESTESSO", true);
					parameters.put("DESTINATARIO_DENOMINAZIONE", "");
					parameters.put("DESTINATARIO_NATOA", "");
					parameters.put("DESTINATARIO_NATOIL", "");
					parameters.put("DESTINATARIO_RESIDENTEA", "");
					parameters.put("DESTINATARIO_RESIDENTEIND", "");
					parameters.put("DESTINATARIO_CF", "");
				} else {
					if (domanda.getDestinatario() != null) {
						// per altra persona
						parameters.put("SESTESSO", false);
						parameters.put("DESTINATARIO_DENOMINAZIONE",
								domanda.getDestinatario().getCognome().toUpperCase() + " "
										+ domanda.getDestinatario().getNome().toUpperCase());
						if (Util.isValorizzato(domanda.getDestinatario().getComuneNascita()))
							parameters.put("DESTINATARIO_NATOA",
									domanda.getDestinatario().getComuneNascita().toUpperCase() + " ("
											+ domanda.getDestinatario().getProvinciaNascita().toUpperCase() + ")");
						else
							parameters.put("DESTINATARIO_NATOA",
									domanda.getDestinatario().getStatoNascita().toUpperCase());
						parameters.put("DESTINATARIO_NATOIL",
								Converter.getDataISO(domanda.getDestinatario().getDataNascita()));
						parameters.put("DESTINATARIO_RESIDENTEA",
								domanda.getDestinatario().getComuneResidenza().toUpperCase() + " ("
										+ domanda.getDestinatario().getProvinciaResidenza().toUpperCase() + ")");
						parameters.put("DESTINATARIO_RESIDENTEIND",
								domanda.getDestinatario().getIndirizzoResidenza().toUpperCase());
						parameters.put("DESTINATARIO_CF", domanda.getDestinatario().getCf().toUpperCase());
					}
				}
			}
			if (domanda.getStudioDestinatario() != null) {
				if (domanda.getStudioDestinatario().equalsIgnoreCase(Constants.DIPLOMA_PRIMO_GRADO)) {
					parameters.put("STUDIO_PRIMO", true);
					parameters.put("STUDIO_SECONDO", false);
					parameters.put("STUDIO_TERZO", false);
					parameters.put("NESSUN_TITOLO_STUDIO", false);
				} else if (domanda.getStudioDestinatario().equalsIgnoreCase(Constants.DIPLOMA_SECONDO_GRADO)) {
					parameters.put("STUDIO_PRIMO", false);
					parameters.put("STUDIO_SECONDO", true);
					parameters.put("STUDIO_TERZO", false);
					parameters.put("NESSUN_TITOLO_STUDIO", false);
				} else if (domanda.getStudioDestinatario().equalsIgnoreCase(Constants.DIPLOMA_TERZIARIA)) {
					parameters.put("STUDIO_PRIMO", false);
					parameters.put("STUDIO_SECONDO", false);
					parameters.put("STUDIO_TERZO", true);
					parameters.put("NESSUN_TITOLO_STUDIO", false);
				} else if (domanda.getStudioDestinatario().equalsIgnoreCase(Constants.NESSUN_TITOLO_STUDIO)) {
					parameters.put("STUDIO_PRIMO", false);
					parameters.put("STUDIO_SECONDO", false);
					parameters.put("STUDIO_TERZO", false);
					parameters.put("NESSUN_TITOLO_STUDIO", true);
				} else {
					parameters.put("STUDIO_PRIMO", false);
					parameters.put("STUDIO_SECONDO", false);
					parameters.put("STUDIO_TERZO", false);
					parameters.put("NESSUN_TITOLO_STUDIO", false);
				}
			}
			if (domanda.getDelega() != null) {
				if (domanda.getDelega().equalsIgnoreCase(Constants.POTESTA_GENITORIALE)) {
					parameters.put("RAPPORTO_POTESTA", true);
					parameters.put("RAPPORTO_CONIUGE", false);
					parameters.put("RAPPORTO_PARENTE", false);
					parameters.put("RAPPORTO_TUTELA", false);
					parameters.put("RAPPORTO_CURATELA", false);
					parameters.put("RAPPORTO_SOSTEGNO", false);
					parameters.put("RAPPORTO_NUCLEO", false);
					parameters.put("RAPPORTO_ALTRO", false);
					parameters.put("RAPPORTO_PROCURA", false);
				} else if (domanda.getDelega().equalsIgnoreCase(Constants.NUCLEO_FAMILIARE)) {
					parameters.put("RAPPORTO_POTESTA", false);
					parameters.put("RAPPORTO_CONIUGE", false);
					parameters.put("RAPPORTO_PARENTE", false);
					parameters.put("RAPPORTO_TUTELA", false);
					parameters.put("RAPPORTO_CURATELA", false);
					parameters.put("RAPPORTO_SOSTEGNO", false);
					parameters.put("RAPPORTO_NUCLEO", true);
					parameters.put("RAPPORTO_ALTRO", false);
					parameters.put("RAPPORTO_PROCURA", false);
				} else if (domanda.getDelega().equalsIgnoreCase(Constants.CONIUGE)) {
					parameters.put("RAPPORTO_POTESTA", false);
					parameters.put("RAPPORTO_CONIUGE", true);
					parameters.put("RAPPORTO_PARENTE", false);
					parameters.put("RAPPORTO_TUTELA", false);
					parameters.put("RAPPORTO_CURATELA", false);
					parameters.put("RAPPORTO_SOSTEGNO", false);
					parameters.put("RAPPORTO_NUCLEO", false);
					parameters.put("RAPPORTO_ALTRO", false);
					parameters.put("RAPPORTO_PROCURA", false);
				} else if (domanda.getDelega().equalsIgnoreCase(Constants.PARENTE_PRIMO_GRADO)) {
					parameters.put("RAPPORTO_POTESTA", false);
					parameters.put("RAPPORTO_CONIUGE", false);
					parameters.put("RAPPORTO_PARENTE", true);
					parameters.put("RAPPORTO_TUTELA", false);
					parameters.put("RAPPORTO_CURATELA", false);
					parameters.put("RAPPORTO_SOSTEGNO", false);
					parameters.put("RAPPORTO_NUCLEO", false);
					parameters.put("RAPPORTO_ALTRO", false);
					parameters.put("RAPPORTO_PROCURA", false);
				} else if (domanda.getDelega().equalsIgnoreCase(Constants.TUTELA)) {
					parameters.put("RAPPORTO_POTESTA", false);
					parameters.put("RAPPORTO_CONIUGE", false);
					parameters.put("RAPPORTO_PARENTE", false);
					parameters.put("RAPPORTO_TUTELA", true);
					parameters.put("RAPPORTO_CURATELA", false);
					parameters.put("RAPPORTO_SOSTEGNO", false);
					parameters.put("RAPPORTO_NUCLEO", false);
					parameters.put("RAPPORTO_ALTRO", false);
					parameters.put("RAPPORTO_PROCURA", false);
				} else if (domanda.getDelega().equalsIgnoreCase(Constants.CURATELA)) {
					parameters.put("RAPPORTO_POTESTA", false);
					parameters.put("RAPPORTO_CONIUGE", false);
					parameters.put("RAPPORTO_PARENTE", false);
					parameters.put("RAPPORTO_TUTELA", false);
					parameters.put("RAPPORTO_CURATELA", true);
					parameters.put("RAPPORTO_SOSTEGNO", false);
					parameters.put("RAPPORTO_NUCLEO", false);
					parameters.put("RAPPORTO_ALTRO", false);
					parameters.put("RAPPORTO_PROCURA", false);
				} else if (domanda.getDelega().equalsIgnoreCase(Constants.AMMINISTRAZIONE_SOSTEGNO)) {
					parameters.put("RAPPORTO_POTESTA", false);
					parameters.put("RAPPORTO_CONIUGE", false);
					parameters.put("RAPPORTO_PARENTE", false);
					parameters.put("RAPPORTO_TUTELA", false);
					parameters.put("RAPPORTO_CURATELA", false);
					parameters.put("RAPPORTO_SOSTEGNO", true);
					parameters.put("RAPPORTO_NUCLEO", false);
					parameters.put("RAPPORTO_ALTRO", false);
					parameters.put("RAPPORTO_PROCURA", false);
				} else if (domanda.getDelega().equalsIgnoreCase(Constants.ALTRO)) {
					parameters.put("RAPPORTO_POTESTA", false);
					parameters.put("RAPPORTO_CONIUGE", false);
					parameters.put("RAPPORTO_PARENTE", false);
					parameters.put("RAPPORTO_TUTELA", false);
					parameters.put("RAPPORTO_CURATELA", false);
					parameters.put("RAPPORTO_SOSTEGNO", false);
					parameters.put("RAPPORTO_NUCLEO", false);
					parameters.put("RAPPORTO_ALTRO", true);
					parameters.put("RAPPORTO_PROCURA", false);
				} else if (domanda.getDelega().equalsIgnoreCase(Constants.PROCURA_SPECIALE)) {
					parameters.put("RAPPORTO_POTESTA", false);
					parameters.put("RAPPORTO_CONIUGE", false);
					parameters.put("RAPPORTO_PARENTE", false);
					parameters.put("RAPPORTO_TUTELA", false);
					parameters.put("RAPPORTO_CURATELA", false);
					parameters.put("RAPPORTO_SOSTEGNO", false);
					parameters.put("RAPPORTO_NUCLEO", false);
					parameters.put("RAPPORTO_ALTRO", false);
					parameters.put("RAPPORTO_PROCURA", true);
				} else {
					parameters.put("RAPPORTO_POTESTA", false);
					parameters.put("RAPPORTO_CONIUGE", false);
					parameters.put("RAPPORTO_PARENTE", false);
					parameters.put("RAPPORTO_TUTELA", false);
					parameters.put("RAPPORTO_CURATELA", false);
					parameters.put("RAPPORTO_SOSTEGNO", false);
					parameters.put("RAPPORTO_NUCLEO", false);
					parameters.put("RAPPORTO_ALTRO", false);
					parameters.put("RAPPORTO_PROCURA", false);
				}
			}
			if (domanda.getValutazioneMultidimensionale() != null) {
				if (domanda.getValutazioneMultidimensionale().equalsIgnoreCase(Constants.PERSONA_PIU_65_ANNI)) {
					parameters.put("ULTRA65", true);
				} else if (domanda.getValutazioneMultidimensionale().equalsIgnoreCase(Constants.PERSONA_DISABILE)) {
					parameters.put("ULTRA65", false);
				} else {
					parameters.put("ULTRA65", null);
				}
			} else
				parameters.put("ULTRA65", null);
			if (domanda.isNessunaIncompatibilita() != null) {
				if (domanda.isNessunaIncompatibilita()) {
					parameters.put("NESSUNA_INCOMPATIBILITA", true);
				} else
					parameters.put("NESSUNA_INCOMPATIBILITA", false);
			} else
				parameters.put("NESSUNA_INCOMPATIBILITA", null);
			parameters.put("PUNTEGGIO", domanda.getPunteggioBisognoSociale());

			if (domanda.getContratto() != null) {
				if (domanda.getContratto().getDataInizio() != null)
					parameters.put("DATA_INI_CONTRATTO", Converter.getDataISO(domanda.getContratto().getDataInizio()));
				else
					parameters.put("DATA_INI_CONTRATTO", "");
				if (domanda.getContratto().getDataInizio() != null)
					parameters.put("DATA_FINE_CONTRATTO", Converter.getDataISO(domanda.getContratto().getDataFine()));
				else
					parameters.put("DATA_FINE_CONTRATTO", "");

				String tipoAssistente = domanda.getContratto().getTipoSupportoFamiliare() != null
						? domanda.getContratto().getTipoSupportoFamiliare()
						: Constants.ASSISTENTE_FAMILIARE;
				if (domanda.getContratto().getTipo() != null) {
					if (domanda.getContratto().getTipo().equalsIgnoreCase(Constants.ASSISTENTE_FAMILIARE)) {
						parameters.put("CONTRATTO_TIPO_FAMILIARE", true);
						parameters.put("CONTRATTO_TIPO_COOPERATIVA_ASSISTENTE", false);
						parameters.put("CONTRATTO_TIPO_COOPERATIVA_EDUCATORE", false);
						parameters.put("CONTRATTO_TIPO_NESSUNO", false);
						parameters.put("CONTRATTO_PIVA_ASSISTENTE", false);
						parameters.put("CONTRATTO_PIVA_EDUCATORE", false);
					} else if (domanda.getContratto().getTipo().equalsIgnoreCase(Constants.COOPERATIVA)
							&& tipoAssistente.equalsIgnoreCase(Constants.ASSISTENTE_FAMILIARE)) {
						parameters.put("CONTRATTO_TIPO_FAMILIARE", false);
						parameters.put("CONTRATTO_TIPO_COOPERATIVA_ASSISTENTE", true);
						parameters.put("CONTRATTO_TIPO_COOPERATIVA_EDUCATORE", false);
						parameters.put("CONTRATTO_TIPO_NESSUNO", false);
						parameters.put("CONTRATTO_PIVA_ASSISTENTE", false);
						parameters.put("CONTRATTO_PIVA_EDUCATORE", false);
					} else if (domanda.getContratto().getTipo().equalsIgnoreCase(Constants.PARTITA_IVA)
							&& tipoAssistente.equalsIgnoreCase(Constants.ASSISTENTE_FAMILIARE)) {
						parameters.put("CONTRATTO_TIPO_FAMILIARE", false);
						parameters.put("CONTRATTO_TIPO_COOPERATIVA_ASSISTENTE", false);
						parameters.put("CONTRATTO_TIPO_COOPERATIVA_EDUCATORE", false);
						parameters.put("CONTRATTO_TIPO_NESSUNO", false);
						parameters.put("CONTRATTO_PIVA_ASSISTENTE", true);
						parameters.put("CONTRATTO_PIVA_EDUCATORE", false);
					} else if (domanda.getContratto().getTipo().equalsIgnoreCase(Constants.COOPERATIVA)
							&& tipoAssistente.equalsIgnoreCase(Constants.EDUCATORE_PROFESSIONALE)) {
						parameters.put("CONTRATTO_TIPO_FAMILIARE", false);
						parameters.put("CONTRATTO_TIPO_COOPERATIVA_ASSISTENTE", false);
						parameters.put("CONTRATTO_TIPO_COOPERATIVA_EDUCATORE", true);
						parameters.put("CONTRATTO_TIPO_NESSUNO", false);
						parameters.put("CONTRATTO_PIVA_ASSISTENTE", false);
						parameters.put("CONTRATTO_PIVA_EDUCATORE", false);
					} else if (domanda.getContratto().getTipo().equalsIgnoreCase(Constants.PARTITA_IVA)
							&& tipoAssistente.equalsIgnoreCase(Constants.EDUCATORE_PROFESSIONALE)) {
						parameters.put("CONTRATTO_TIPO_FAMILIARE", false);
						parameters.put("CONTRATTO_TIPO_COOPERATIVA_ASSISTENTE", false);
						parameters.put("CONTRATTO_TIPO_COOPERATIVA_EDUCATORE", false);
						parameters.put("CONTRATTO_TIPO_NESSUNO", false);
						parameters.put("CONTRATTO_PIVA_ASSISTENTE", false);
						parameters.put("CONTRATTO_PIVA_EDUCATORE", true);
					} else {
						parameters.put("CONTRATTO_TIPO_FAMILIARE", false);
						parameters.put("CONTRATTO_TIPO_COOPERATIVA_ASSISTENTE", false);
						parameters.put("CONTRATTO_TIPO_COOPERATIVA_EDUCATORE", false);
						parameters.put("CONTRATTO_TIPO_NESSUNO", true);
						parameters.put("CONTRATTO_PIVA_ASSISTENTE", false);
						parameters.put("CONTRATTO_PIVA_EDUCATORE", false);
					}
				} else {
					parameters.put("CONTRATTO_TIPO_FAMILIARE", false);
					parameters.put("CONTRATTO_TIPO_COOPERATIVA_ASSISTENTE", false);
					parameters.put("CONTRATTO_TIPO_COOPERATIVA_EDUCATORE", false);
					parameters.put("CONTRATTO_TIPO_NESSUNO", false);
					parameters.put("CONTRATTO_PIVA_ASSISTENTE", false);
					parameters.put("CONTRATTO_PIVA_EDUCATORE", false);
				}
				if (domanda.getContratto().getIntestatario() != null) {
					if (domanda.getContratto().getIntestatario().getCf() != null) {
						if (domanda.getContratto().getIntestatario().getCf()
								.equalsIgnoreCase(domanda.getDestinatario().getCf())) {
							// datore uguale destinatario
							parameters.put("DATORE_DESTINATARIO", true);
							parameters.put("DATORE_RICHIEDENTE", false);
							parameters.put("DATORE_ALTRO", false);
						} else if (domanda.getContratto().getIntestatario().getCf()
								.equalsIgnoreCase(domanda.getRichiedente().getCf())) {
							// datore uguale rihiedente
							parameters.put("DATORE_DESTINATARIO", false);
							parameters.put("DATORE_RICHIEDENTE", true);
							parameters.put("DATORE_ALTRO", false);
						} else {
							parameters.put("DATORE_DESTINATARIO", false);
							parameters.put("DATORE_RICHIEDENTE", false);
							parameters.put("DATORE_ALTRO", true);
						}
					} else {
						parameters.put("DATORE_DESTINATARIO", false);
						parameters.put("DATORE_RICHIEDENTE", false);
						parameters.put("DATORE_ALTRO", false);
					}
					if (Util.isValorizzato(domanda.getContratto().getIntestatario().getCognome())
							&& Util.isValorizzato(domanda.getContratto().getIntestatario().getNome()))
						parameters.put("DATORE_DENOMINAZIONE",
								domanda.getContratto().getIntestatario().getCognome().toUpperCase() + " "
										+ domanda.getContratto().getIntestatario().getNome().toUpperCase());
					else
						parameters.put("DATORE_DENOMINAZIONE", "");
					if (Util.isValorizzato(domanda.getContratto().getIntestatario().getComuneNascita()))
						parameters.put("DATORE_NATOA",
								domanda.getContratto().getIntestatario().getComuneNascita().toUpperCase());
					else
						parameters.put("DATORE_NATOA", "");
					parameters.put("DATORE_NATOIL",
							Converter.getDataISO(domanda.getContratto().getIntestatario().getDataNascita()));
					if (Util.isValorizzato(domanda.getContratto().getIntestatario().getCf()))
						parameters.put("DATORE_CF", domanda.getContratto().getIntestatario().getCf().toUpperCase());
					else
						parameters.put("DATORE_CF", "");
				} else {
					parameters.put("DATORE_DESTINATARIO", false);
					parameters.put("DATORE_RICHIEDENTE", false);
					parameters.put("DATORE_ALTRO", false);
					parameters.put("DATORE_CF", "");
					parameters.put("DATORE_NATOA", "");
					parameters.put("DATORE_DENOMINAZIONE", "");
					parameters.put("DATORE_NATOIL", "");
				}
				if (domanda.getContratto().getAgenzia() != null) {
					if (Util.isValorizzato(domanda.getContratto().getAgenzia().getCf()))
						parameters.put("COOPERATIVA_CF", domanda.getContratto().getAgenzia().getCf().toUpperCase());
					else
						parameters.put("COOPERATIVA_CF", "");
				}
				if (domanda.getContratto().getRelazioneDestinatario() != null)
					parameters.put("RELAZIONE_DESTINATARIO",
							domanda.getContratto().getRelazioneDestinatario().toUpperCase());
				else
					parameters.put("RELAZIONE_DESTINATARIO", "");
				if (domanda.getContratto().getAssistenteFamiliare() != null) {
					if (Util.isValorizzato(domanda.getContratto().getAssistenteFamiliare().getCognome())
							&& Util.isValorizzato(domanda.getContratto().getAssistenteFamiliare().getNome()))
						parameters.put("ASSISTENTE_DENOMINAZIONE",
								domanda.getContratto().getAssistenteFamiliare().getCognome().toUpperCase() + " "
										+ domanda.getContratto().getAssistenteFamiliare().getNome().toUpperCase());
					else
						parameters.put("ASSISTENTE_DENOMINAZIONE", "");
					if (Util.isValorizzato(domanda.getContratto().getAssistenteFamiliare().getComuneNascita()))
						parameters.put("ASSISTENTE_NATOA",
								domanda.getContratto().getAssistenteFamiliare().getComuneNascita().toUpperCase());
					else
						parameters.put("ASSISTENTE_NATOA", "");
					parameters.put("ASSISTENTE_NATOIL",
							Converter.getDataISO(domanda.getContratto().getAssistenteFamiliare().getDataNascita()));
					if (Util.isValorizzato(domanda.getContratto().getAssistenteFamiliare().getCf()))
						parameters.put("ASSISTENTE_CF",
								domanda.getContratto().getAssistenteFamiliare().getCf().toUpperCase());
					else
						parameters.put("ASSISTENTE_CF", "");
					if (Util.isValorizzato(domanda.getContratto().getPivaAssitenteFamiliare()))
						parameters.put("ASSISTENTE_PIVA",
								domanda.getContratto().getPivaAssitenteFamiliare().toUpperCase());
					else
						parameters.put("ASSISTENTE_PIVA", "");
				} else {
					parameters.put("ASSISTENTE_DENOMINAZIONE", "");
					parameters.put("ASSISTENTE_NATOA", "");
					parameters.put("ASSISTENTE_NATOIL", "");
					parameters.put("ASSISTENTE_CF", "");
					parameters.put("PIVA_CF", "");
				}
			} else {
				parameters.put("CONTRATTO_TIPO_FAMILIARE", false);
				parameters.put("CONTRATTO_TIPO_COOPERATIVA_ASSISTENTE", false);
				parameters.put("CONTRATTO_TIPO_COOPERATIVA_EDUCATORE", false);
				parameters.put("CONTRATTO_TIPO_NESSUNO", false);
				parameters.put("CONTRATTO_PIVA_ASSISTENTE", false);
				parameters.put("CONTRATTO_PIVA_EDUCATORE", false);
				parameters.put("DATORE_DESTINATARIO", false);
				parameters.put("DATORE_RICHIEDENTE", false);
				parameters.put("DATORE_ALTRO", false);
				parameters.put("RELAZIONE_DESTINATARIO", "");
			}

			if (domanda.getAccredito() != null) {
				if (domanda.getAccredito().getIban() != null)
					parameters.put("IBAN", domanda.getAccredito().getIban().trim().toUpperCase());
				else
					parameters.put("IBAN", "");
				if (domanda.getAccredito().getIntestatario() != null)
					parameters.put("IBAN_INTESTATARIO", domanda.getAccredito().getIntestatario().toUpperCase());
				else
					parameters.put("IBAN_INTESTATARIO", "");
			} else {
				parameters.put("IBAN", "");
				parameters.put("IBAN_INTESTATARIO", "");
			}

			String dgr = parametroDao.selectValoreParametroFromCod(Constants.DGR, Constants.PARAMETRO_GENERICO);
			if (dgr != null)
				parameters.put("DGR", dgr.toUpperCase());
			else
				parameters.put("DGR", "");

			// prendo gli allegati
			List<ModelGetAllegatoExt> allegati = new ArrayList<ModelGetAllegatoExt>();
			allegati = richiesteDao.selectAllegati(domanda.getDomandaDetId());
			String elenco_allegati = "";
			String elenco_allegati_controdeduzione = "";
			for (ModelGetAllegatoExt allegato : allegati) {
				if (!allegato.getCodTipoAllegato().equalsIgnoreCase(Constants.DOMANDA)
						&& !allegato.getCodTipoAllegato().equalsIgnoreCase(Constants.CONTRODEDUZIONE)) {
					elenco_allegati = elenco_allegati + allegato.getDescTipoAllegato();
					elenco_allegati = elenco_allegati + " : " + allegato.getFileName() + System.lineSeparator();
				} else if (allegato.getCodTipoAllegato().equalsIgnoreCase(Constants.CONTRODEDUZIONE)) {
					elenco_allegati_controdeduzione = elenco_allegati_controdeduzione + allegato.getDescTipoAllegato();
					elenco_allegati_controdeduzione = elenco_allegati_controdeduzione + " : " + allegato.getFileName()
							+ System.lineSeparator();
				}
			}
			if (Util.isValorizzato(elenco_allegati))
				parameters.put("ELENCO_ALLEGATI", elenco_allegati);
			else
				parameters.put("ELENCO_ALLEGATI", "");

			if (Util.isValorizzato(elenco_allegati_controdeduzione) || Util.isValorizzato(domanda.getNoteRichiedente()))
				parameters.put("CONTRODEDUZIONE", true);
			else
				parameters.put("CONTRODEDUZIONE", false);

			if (Util.isValorizzato(elenco_allegati_controdeduzione))
				parameters.put("ELENCO_ALLEGATI_CONTRODEDUZIONE", elenco_allegati_controdeduzione);
			else
				parameters.put("ELENCO_ALLEGATI_CONTRODEDUZIONE", "");

			if (Util.isValorizzato(domanda.getNoteRichiedente()))
				parameters.put("NOTA_CONTRODEDUZIONE", domanda.getNoteRichiedente());
			else
				parameters.put("NOTA_CONTRODEDUZIONE", "");

			byte[] byteArray = getPdfDomanda(parameters);
			if (byteArray.length < 1000) // NO DATA
				return null;
			logInfo("creo pdf fine parametri", "pdf domanda");
			return byteArray;
		} catch (Exception e) {
			e.printStackTrace();
			logError("Creazione PDF Domanda", "Errore crea domanda", e);
		}
		return null;
	}

	public void archiviadomanda(byte[] domanda, String numerodomanda, String richiedente, String detCod,
			BigDecimal sportelloid, BigDecimal domandadetid) throws DatabaseException, IOException {
		String path = parametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO);
		ModelSportello sportello = new ModelSportello();
		try {
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
				file = new File(path + "/DOMANDA.pdf");
			else
				file = new File(path + "\\DOMANDA.pdf");
			logInfo("scrivo allegato su db", "pdf domanda " + path + " /DOMANDA.pdf");
			// faccio update o insert su tabella allegato
			if (richiesteDao.selectEsisteAllegato(detCod, "DOMANDA"))
				richiesteDao.updateAllegato(detCod, richiedente, "DOMANDA.pdf", "application/pdf", path, "DOMANDA");
			else
				richiesteDao.insertAllegato("DOMANDA.pdf", "application/pdf", path, sportelloid, domandadetid, detCod,
						"DOMANDA", richiedente, richiedente);

			logInfo("creo pdf archivio ", "pdf domanda" + path + " /DOMANDA.pdf");
			OutputStream out = new FileOutputStream(file);
			// cripto il file
			byte[] domandaCifrata = filesEncrypt.creaFileCifratoByte(Cipher.ENCRYPT_MODE, domanda);
			out.write(domandaCifrata, 0, domandaCifrata.length);
			// out.write(domanda, 0, domanda.length);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			logError("Archiviazione PDF Domanda", "Errore archivia domanda", e);
		}
	}
}

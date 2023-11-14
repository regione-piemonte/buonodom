/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.business.be.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonodom.buonodombandisrv.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombandisrv.dto.Contact;
import it.csi.buonodom.buonodombandisrv.dto.DatiBuono;
import it.csi.buonodom.buonodombandisrv.dto.ModelAnagraficaBeneficiario;
import it.csi.buonodom.buonodombandisrv.dto.ModelAnagraficaPersonaFisica;
import it.csi.buonodom.buonodombandisrv.dto.ModelBandiMessage;
import it.csi.buonodom.buonodombandisrv.dto.ModelDocumentoAllegato;
import it.csi.buonodom.buonodombandisrv.dto.ModelDocumentoSpesa;
import it.csi.buonodom.buonodombandisrv.dto.ModelFornitore;
import it.csi.buonodom.buonodombandisrv.dto.ModelInvioBandi;
import it.csi.buonodom.buonodombandisrv.dto.ModelInvioDichiarazioneSpesa;
import it.csi.buonodom.buonodombandisrv.dto.ModelInvioDocumentoSpesa;
import it.csi.buonodom.buonodombandisrv.dto.ModelInvioFornitore;
import it.csi.buonodom.buonodombandisrv.dto.ModelMese;
import it.csi.buonodom.buonodombandisrv.dto.ModelProgettoAgevolazione;
import it.csi.buonodom.buonodombandisrv.dto.ModelProgettoBandi;
import it.csi.buonodom.buonodombandisrv.dto.ModelRichiesta;
import it.csi.buonodom.buonodombandisrv.dto.ModelSetFornitore;
import it.csi.buonodom.buonodombandisrv.dto.ModelSpeseProgetto;
import it.csi.buonodom.buonodombandisrv.dto.ModelSportello;
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
import it.csi.buonodom.buonodombandisrv.util.Util;
import it.csi.buonodom.buonodombandisrv.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombandisrv.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombandisrv.util.rest.ResponseRest;
import it.csi.buonodom.buonodombandisrv.util.validator.impl.ValidateGenericImpl;
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
public class ServiziVersoBandiService extends BaseService {

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

	private String stringXml = null;
	private String nomeFileZip = null;
	private ZipEntry ezip = null;
	private ZipOutputStream outZip = null;
	private File fileZip = null;

	public Response acquisizioneDomandaBandi(String numeroRichiesta, String xRequestId, String xForwardedFor,
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
			ModelBandiMessage body = new ModelBandiMessage();
			if (richiesta != null) {
				// verifico se il buono creato allora faccio ma se attivo o altro no
				DatiBuono buonoId = logBandiDao.selectBuono(richiesta.getSportelloId(), richiesta.getDomandaId(),
						Constants.CREATO);
				if (buonoId != null) {
					// prendo i contatti
					responserest = restbase.getContatti(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
							xCodiceServizio, securityContext, httpHeaders, httpRequest);
					Contact contatto = new Contact();
					ObjectMapper mapper = new ObjectMapper();
					logInfo("creo contatti", responserest.toString());
					if (responserest != null && responserest.getStatusCode() == 200) {
						contatto = mapper.readValue(responserest.getJson(), Contact.class);
					}
					File bandi = generaXml(richiesta, contatto, buonoId.getIban(), buonoId.getIbanIntestatario());

					FileInputStream fl = new FileInputStream(bandi);

					byte[] xml = new byte[(int) bandi.length()];

					fl.read(xml);
					fl.close();

					String BANDI_XML = bandi.getName() + "_" + richiesta.getDomandaId() + "_1_"
							+ Converter.getDataOra(new Timestamp(System.currentTimeMillis())) + ".xml";
					if (xml.length < 1000) // NO DATA
						xml = null;
					if (xml != null) {
						archiviaxml(xml, richiesta.getNumero(), richiesta.getRichiedente().getCf(),
								richiesta.getDomandaDetCod(), richiesta.getSportelloId(), richiesta.getDomandaId(),
								BANDI_XML, 0);
						responserest = new ResponseRest();
						responserest = restbase.postAcquisizioneDomande(richiesta.getNumero(),
								shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, securityContext,
								httpHeaders, httpRequest, "file", BANDI_XML, bandi);
						if (responserest.getStatusCode() != 200 && responserest.getStatusCode() != 201) {
							param[0] = responserest.getJson();
							logError(metodo, "Errore post acquisizione ", param[0]);
							logBandiDao.insertLogBandi(richiesta.getNumero(), buonoId.getBuonoId(), null, stringXml,
									"KO", String.valueOf(responserest.getStatusCode()),
									"Errore post acquisizione " + param[0], Constants.INVIO_DOMANDA);
							listerrorservice
									.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
							throw new ResponseErrorException(
									ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
									"Errore set fornitore");
						}
						mapper = new ObjectMapper();
						try {
							body = mapper.readValue(responserest.getJson(), ModelBandiMessage.class);
						} catch (IOException e) {
							param[0] = responserest.getJson();
							logError(metodo, "Errore post acquisizione mapper ", param[0]);
							logBandiDao.insertLogBandi(richiesta.getNumero(), buonoId.getBuonoId(), null, stringXml,
									"KO", String.valueOf(responserest.getStatusCode()),
									"Errore post acquisizione mapper " + param[0], Constants.INVIO_DOMANDA);
							e.printStackTrace();
						}
						// scrivo nella tabella di log di bandi
						if (body.getEsitoServizio().equalsIgnoreCase(Constants.BANDI_OK)) {
							logBandiDao.insertLogBandi(richiesta.getNumero(), buonoId.getBuonoId(), body.getUuid(),
									stringXml, body.getEsitoServizio(), null, null, Constants.INVIO_DOMANDA);
						} else {
							logBandiDao.insertLogBandi(richiesta.getNumero(), buonoId.getBuonoId(), null, stringXml,
									body.getEsitoServizio(), body.getCodiceErrore(), body.getMessaggio(),
									Constants.INVIO_DOMANDA);
						}
					} else {
						param[0] = "Errore nella denerazione del xml per bandi";
						logError(metodo, "Errore Crea xml bandi ", param[0]);
						listerrorservice
								.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
								"errore in xml bandi");
					}
				} else {
					// errore non esiste il buonoid
					param[0] = "Errore non trovato o buono non in stato creato";
					logError(metodo, "Errore Crea xml bandi ", param[0]);
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
							"Errore non trovato o buono  non in stato creato");
				}
			} else {
				param[0] = "Errore nell'invio a Bandi domanda non trovata";
				logError(metodo, param[0]);
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"errore in invio a bandi domanda non trovata");
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

	private File generaXml(ModelRichiesta richiesta, Contact contatto, String iban, String IbanIntestatario)
			throws DatabaseException {

		ModelInvioBandi bandiXml = new ModelInvioBandi();
		ModelProgettoAgevolazione progettoDiAgevolazione = new ModelProgettoAgevolazione();
		ModelAnagraficaBeneficiario anagraficaBeneficiario = new ModelAnagraficaBeneficiario();
		ModelAnagraficaPersonaFisica anagraficaPersonaFisica = new ModelAnagraficaPersonaFisica();
		ModelSpeseProgetto speseProgetto = new ModelSpeseProgetto();
		ModelProgettoBandi progetto = new ModelProgettoBandi();
		List<ModelProgettoBandi> progetti = new ArrayList<ModelProgettoBandi>();
		ModelFornitore fornitore = new ModelFornitore();
		bandiXml.setVersion("1.0.0");
		bandiXml.setXmlns("http://www.csi.it/interscambio/finaziamenti");
		try {

			Map<String, String> parametri = parametroDao.selectParametriPerTipo(Constants.BANDI);
			String importo = richiesteDao.selectImporto(richiesta.getNumero());
			progetto.setNormaIncentivazione(parametri.get(Constants.NORMA_INCENTIVAZIONE));
			// aspettare risposta manca domanda id
			progetto.setIdProgetto(richiesta.getNumero());
			progetto.setCodiceAsse(parametri.get(Constants.CODICE_ASSE));
			progetto.setDescrizioneAsse(parametri.get(Constants.DESCRIZIONE_ASSE));
			progetto.setCodiceMisura(parametri.get(Constants.CODICE_MISURA));
			progetto.setDescrizioneMisura(parametri.get(Constants.DESCRIZIONE_MISURA));
			progetto.setDescrizioneBando(parametri.get(Constants.DESCRIZIONE_BANDO));
			progetto.setDestinatarioDescrizione(parametri.get(Constants.DESTINATARIO_DESCRIZIONE));
			progetto.setDestinatarioDirezione(parametri.get(Constants.DESTINATARIO_DIREZIONE));
			progetto.setCodiceIntervento(parametri.get(Constants.CODICE_INTERVENTO));
			progetto.setDescrizioneIntervento(parametri.get(Constants.DESCRIZIONE_INTERVENTO));
			progettoDiAgevolazione.setDataPresentazione(richiesta.getDataDomanda());
			if (Util.isValorizzato(richiesta.getOraDomanda()) && richiesta.getOraDomanda().length() >= 8) {
				progettoDiAgevolazione.setOraPresentazione(richiesta.getOraDomanda().substring(0, 8));
			} else
				progettoDiAgevolazione.setOraPresentazione(richiesta.getOraDomanda());
			progettoDiAgevolazione.setCodSportello(richiesteDao.selectSportello(richiesta.getSportelloId()));
			if (richiesta.getAreaId() != null)
				progettoDiAgevolazione.setCodAreaTerritoriale(richiesteDao.selectArea(richiesta.getAreaId()));
			else
				progettoDiAgevolazione.setCodAreaTerritoriale("N.D.");
			progettoDiAgevolazione.setPartitaivaUnitalocale("");
			progettoDiAgevolazione.setSedeNonAttivataInPiemonte("");
			progettoDiAgevolazione.setCodiceAteco("");
			progettoDiAgevolazione.setDescrizioneAttivitaUnitalocale("");
			progettoDiAgevolazione.setCodiceSettoreUnitalocale("");
			progettoDiAgevolazione.setDescrizioneSettoreUnitalocale("");
			progettoDiAgevolazione.setStatoUnitalocale(Constants.STATO);
			progettoDiAgevolazione.setCodStatoUnitalocale(Constants.COD_STATO);
			progettoDiAgevolazione.setCapUnitalocale("");
			// preso istat da descrizione comune
			progettoDiAgevolazione.setCodiceComuneUnitalocale(
					richiesteDao.selectIstatComune(richiesta.getDestinatario().getComuneResidenza()));
			progettoDiAgevolazione.setComuneUnitalocale(richiesta.getDestinatario().getComuneResidenza());
			progettoDiAgevolazione.setProvinciaUnitalocale(richiesta.getDestinatario().getProvinciaResidenza());
			progettoDiAgevolazione.setModalitaRegistrazione(parametri.get(Constants.MODALITA_REGISTRAZIONE));
			progettoDiAgevolazione.setIndirizzoUnitalocale("");
			progettoDiAgevolazione.setTitoloProgetto("");
			progettoDiAgevolazione.setDescrizioneProgetto("");
			progettoDiAgevolazione.setAbstractProgetto("");
			progettoDiAgevolazione.setContributoRichiesto(importo);
			progettoDiAgevolazione.setFinanziamentoRichiesto("");
			progettoDiAgevolazione.setSpesaAmmessa("");
			progettoDiAgevolazione.setFinanziamentoBancario("");
			progetto.setProgettoDiAgevolazione(progettoDiAgevolazione);
			anagraficaBeneficiario.setCodTipoUtente(parametri.get(Constants.COD_TIPO_UTENTE));
			anagraficaBeneficiario.setTipoUtente(parametri.get(Constants.TIPO_UTENTE));
			anagraficaBeneficiario.setTipologiaEnte(parametri.get(Constants.TIPOLOGIA_ENTE));
			anagraficaBeneficiario.setCodiceDipartimento("");
			anagraficaBeneficiario.setFormaGiuridicaImpresa("");
			anagraficaBeneficiario.setCodiceFiscaleImpresa(richiesta.getDestinatario().getCf());
			anagraficaBeneficiario.setRagioneSocialeImpresa(
					richiesta.getDestinatario().getNome() + " " + richiesta.getDestinatario().getCognome());
			anagraficaBeneficiario.setPartitaivaSedelegale("");
			anagraficaBeneficiario.setCodiceAteco("");
			anagraficaBeneficiario.setStatoSedelegale(Constants.STATO);
			anagraficaBeneficiario.setCodStatoSedelegale(Constants.COD_STATO);
			anagraficaBeneficiario.setTipologiaRegistroIscrizione("");
			anagraficaBeneficiario.setIscrizioneIncorso("");
			// String iban = richiesta.getAccredito().getIban().trim();
			String rPad = "";
			if (Util.isValorizzato(iban)) {
				if (iban.length() > 27)
					iban = iban.substring(0, 27);
				if (iban.length() < 27) {
					for (int i = 0; i < 27 - iban.length(); i++) {
						rPad = rPad + "X";
					}
					iban = iban + rPad;
				}
			} else {
				iban = "XXXXXXXXXXXXXXXXXXXXXXXXXXX";
			}
			// prendo i dati specifici dei singoli campi
			anagraficaBeneficiario.setCin(iban.substring(4, 5));
			anagraficaBeneficiario.setAbi(iban.substring(5, 10));
			anagraficaBeneficiario.setCab(iban.substring(10, 15));
			anagraficaBeneficiario.setIban(iban);
			anagraficaBeneficiario.setBic("");
			anagraficaBeneficiario.setnContocorrente(iban.substring(15, 27));
			anagraficaBeneficiario.setRuoloBeneficiario(parametri.get(Constants.RUOLO_BENEFICIARIO));
			anagraficaBeneficiario.setClassificazioneEnte(parametri.get(Constants.CLASSIFICAZIONE_ENTE));
			anagraficaBeneficiario.setCognomePersonaFisica(richiesta.getDestinatario().getCognome());
			anagraficaBeneficiario.setNomePersonaFisica(richiesta.getDestinatario().getNome());
			anagraficaBeneficiario.setStatoNascitaPersonaFisica(richiesta.getDestinatario().getStatoNascita());
			anagraficaBeneficiario.setProvinciaNascitaPersonaFisica("");
			anagraficaBeneficiario.setProvinciaNascitaDescrizionePersonaFisica("");
			String comunenascita = richiesteDao.selectIstatComune(richiesta.getDestinatario().getComuneNascita());
			if (comunenascita != null)
				anagraficaBeneficiario.setComuneNascitaPersonaFisica(comunenascita);
			else
				anagraficaBeneficiario.setComuneNascitaPersonaFisica("");
			anagraficaBeneficiario.setStatoEsteroNascitaPersonaFisica("");
			anagraficaBeneficiario
					.setDataNascitaPersonaFisica(Converter.getDataISO(richiesta.getDestinatario().getDataNascita()));
			anagraficaBeneficiario.setCodiceComuneSedelegale(
					richiesteDao.selectIstatComune(richiesta.getDestinatario().getComuneResidenza()));
			anagraficaBeneficiario.setComuneSedelegale(richiesta.getDestinatario().getComuneResidenza());
			anagraficaBeneficiario.setIndirizzoSedelegale(richiesta.getDestinatario().getIndirizzoResidenza());
			anagraficaBeneficiario.setCapSedelegale("");
			if (contatto != null) {
				anagraficaBeneficiario.setTelefonoSedelegale(contatto.getPhone());
				anagraficaBeneficiario.setEmailSedelegale(contatto.getEmail());
			} else {
				anagraficaBeneficiario.setTelefonoSedelegale("");
				anagraficaBeneficiario.setEmailSedelegale("");
			}
			anagraficaBeneficiario.setPecSedelegale("");
			progetto.setAnagraficaBeneficiario(anagraficaBeneficiario);
			if (!richiesta.getDestinatario().getCf().equalsIgnoreCase(richiesta.getRichiedente().getCf())) {
				anagraficaPersonaFisica.setCodiceFiscaleImpresa(richiesta.getRichiedente().getCf());
				anagraficaPersonaFisica.setTipoPersonaFisica(parametri.get(Constants.TIPO_PERSONAFISICA));
				anagraficaPersonaFisica.setCodiceFiscaleSoggetto(richiesta.getRichiedente().getCf());
				anagraficaPersonaFisica.setCognome(richiesta.getRichiedente().getCognome());
				anagraficaPersonaFisica.setNome(richiesta.getRichiedente().getNome());
				anagraficaPersonaFisica
						.setDataNascita(Converter.getDataISO(richiesta.getRichiedente().getDataNascita()));
				anagraficaPersonaFisica.setStatoNascita(richiesta.getRichiedente().getStatoNascita());
				String comunenascitarichiedente = richiesteDao
						.selectIstatComune(richiesta.getRichiedente().getComuneNascita());
				if (comunenascitarichiedente != null)
					anagraficaPersonaFisica.setCodiceComuneNascita(comunenascitarichiedente);
				else
					anagraficaPersonaFisica.setCodiceComuneNascita("");
				anagraficaPersonaFisica.setComuneNascita(richiesta.getRichiedente().getComuneNascita());
				anagraficaPersonaFisica.setStatoIndirizzo("");
				anagraficaPersonaFisica.setCodStatoIndirizzo("");
				String comuneresidenzarichiedente = richiesteDao
						.selectIstatComune(richiesta.getRichiedente().getComuneResidenza());
				if (comuneresidenzarichiedente != null)
					anagraficaPersonaFisica.setCodiceComuneIndirizzo(comuneresidenzarichiedente);
				else
					anagraficaPersonaFisica.setCodiceComuneIndirizzo("");
				anagraficaPersonaFisica.setComuneIndirizzo(richiesta.getRichiedente().getComuneResidenza());
				anagraficaPersonaFisica.setCapIndirizzo("");
				anagraficaPersonaFisica.setIndirizzo(richiesta.getRichiedente().getIndirizzoResidenza());
				progetto.setAnagraficaPersonaFisica(anagraficaPersonaFisica);
			}
			speseProgetto.setCodiceTipologiaSpesa(parametri.get(Constants.CODICE_TIPOLOGIA_SPESA));
			speseProgetto.setDescrTipologiaSpesa(parametri.get(Constants.DESCR_TIPOLOGIA_SPESA));
			speseProgetto.setImportoSpesaRichiestaFinanziata(importo);
			speseProgetto.setImportoSpesaRichiestaNonFinanziata("");
			speseProgetto.setCodDettIntervento("");
			speseProgetto.setFlagIvaCosto(parametri.get(Constants.FLAG_IVA_COSTO));
			progetto.setSpeseProgetto(speseProgetto);
			if (richiesta.getContratto() != null) {
				if (richiesta.getContratto().getTipo() != null
						&& richiesta.getContratto().getTipo().equals(Constants.COOPERATIVA)) {
					fornitore.setCodiceFiscaleFornitore(richiesta.getContratto().getAgenzia().getCf());
					fornitore.setCognomeFornitore("");
					fornitore.setNomeFornitore("");
					fornitore.setRagioneSociale("N.D.");
					fornitore.setPartitaIvaFornitore("");
				}
				if (richiesta.getContratto().getTipo() != null
						&& richiesta.getContratto().getTipo().equals(Constants.PARTITA_IVA)) {
					fornitore.setCodiceFiscaleFornitore(richiesta.getContratto().getAssistenteFamiliare().getCf());
					if (richiesta.getContratto().getAssistenteFamiliare().getCognome() != null)
						fornitore.setCognomeFornitore(richiesta.getContratto().getAssistenteFamiliare().getCognome());
					else
						fornitore.setCognomeFornitore("");
					if (richiesta.getContratto().getAssistenteFamiliare().getNome() != null)
						fornitore.setNomeFornitore(richiesta.getContratto().getAssistenteFamiliare().getNome());
					else
						fornitore.setNomeFornitore("");
					fornitore.setRagioneSociale("N.D.");
					// verifica bandi che dice che il campo deve essere vuoto se maggiore 16
					if (richiesta.getContratto().getPivaAssitenteFamiliare() != null
							&& richiesta.getContratto().getPivaAssitenteFamiliare().trim().length() == 11)
						fornitore.setPartitaIvaFornitore(richiesta.getContratto().getPivaAssitenteFamiliare());
					else
						fornitore.setPartitaIvaFornitore("");
				}
				if (richiesta.getContratto().getTipo() != null
						&& richiesta.getContratto().getTipo().equals(Constants.ASSISTENTE_FAMILIARE)) {
					fornitore.setCodiceFiscaleFornitore(richiesta.getContratto().getAssistenteFamiliare().getCf());
					if (richiesta.getContratto().getAssistenteFamiliare().getCognome() != null)
						fornitore.setCognomeFornitore(richiesta.getContratto().getAssistenteFamiliare().getCognome());
					else
						fornitore.setCognomeFornitore("");
					if (richiesta.getContratto().getAssistenteFamiliare().getNome() != null)
						fornitore.setNomeFornitore(richiesta.getContratto().getAssistenteFamiliare().getNome());
					else
						fornitore.setNomeFornitore("");
					fornitore.setRagioneSociale("");
					fornitore.setPartitaIvaFornitore("");

				}
				if (richiesta.getContratto().getDataInizio() != null)
					fornitore.setDataInizioContratto(Converter.getData(richiesta.getContratto().getDataInizio()));
				else
					fornitore.setDataInizioContratto("");
				if (richiesta.getContratto().getDataFine() != null)
					fornitore.setDataFineContratto(Converter.getData(richiesta.getContratto().getDataFine()));
				else
					fornitore.setDataFineContratto("");
			} else {
				fornitore.setCodiceFiscaleFornitore("");
				fornitore.setCognomeFornitore("");
				fornitore.setNomeFornitore("");
				fornitore.setDataInizioContratto("");
				fornitore.setDataFineContratto("");
				fornitore.setRagioneSociale("");
				fornitore.setPartitaIvaFornitore("");
			}
			fornitore.setQualificaFornitore(parametri.get(Constants.QUALIFICAFORNITORE));
			fornitore.setFormaGiuridica("");
			progetto.setFornitore(fornitore);
			progetti.add(progetto);
			bandiXml.setProgetti(progetti);
			JAXBContext jCont = JAXBContext.newInstance(ModelInvioBandi.class);
			Marshaller marshal = jCont.createMarshaller();
			marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshal.marshal(bandiXml, System.out);
			StringWriter sw = new StringWriter();
			marshal.marshal(bandiXml, sw);
			stringXml = sw.toString();
			String BANDI_XML = parametri.get(Constants.NOME_FILE);
			File pdfFile = new File(BANDI_XML);
			marshal.marshal(bandiXml, pdfFile);

			return pdfFile;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String[] archiviaDichiarazione(byte[] lettera, String numerodomanda, String richiedente, String detCod,
			BigDecimal sportelloid, BigDecimal domandaId, String nomefile, int dicSpesaId)
			throws DatabaseException, IOException {
		String path = parametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO);
		ModelSportello sportello = new ModelSportello();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		String[] datiritorno = new String[2];
		int allegatoId = 0;
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

			String buonoCod = rendicontazioneDao.selectBuonoCodFromNumeroDomanda(numerodomanda);

			if (!locale)
				path += "/" + buonoCod.toUpperCase();
			else
				path += "\\" + buonoCod.toUpperCase();
			dir = new File(path + "/");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// tutto andato bene genero l'allegato nella dichiarazione spesa
			allegatoId = rendicontazioneDao.insertAllegato(nomefile, "application/pdf", path, "DICHIARAZIONE_SPESA",
					richiedente, richiedente);
			rendicontazioneDao.updateAllegato(allegatoId + "_" + nomefile, path + "_" + allegatoId, richiedente,
					allegatoId);
			if (!locale)
				path += "/DICHIARAZIONE_SPESA_" + allegatoId;
			else
				path += "\\DICHIARAZIONE_SPESA_" + allegatoId;

			dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = null;
			if (!locale)
				file = new File(path + "/" + allegatoId + "_" + nomefile);
			else
				file = new File(path + "\\" + allegatoId + "_" + nomefile);
			logInfo("creo bdf DICHIARAZIONE_SPESA bandi ",
					"pdf DICHIARAZIONE_SPESA bandi" + path + " /" + allegatoId + "_" + nomefile);
			OutputStream out = new FileOutputStream(file);
			// cripto il file
			byte[] domandaCifrata = filesEncrypt.creaFileCifratoByte(Cipher.ENCRYPT_MODE, lettera);
			out.write(domandaCifrata, 0, domandaCifrata.length);

			out.close();

			List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
			String[] param = new String[2];
			if (rendicontazioneDao.checkEsistenzaTipoAllegato("DICHIARAZIONE_SPESA")) {
				Map<String, String> parametri = parametroDao.selectParametriPerTipo(Constants.BANDI);
				// aggiungo il file allo zip
				nomeFileZip = parametri.get(Constants.NOME_FILE_RENDICONTAZIONE_ZIP) + "_" + domandaId + "_1_"
						+ Converter.getDataOra(new Timestamp(System.currentTimeMillis())) + ".zip";
				if (!locale)
					fileZip = new File(path + "/" + nomeFileZip);
				else
					fileZip = new File(path + "\\" + nomeFileZip);
				rendicontazioneDao.insertAllegatoDichiarazioneSpesa(dicSpesaId, allegatoId, richiedente);
				logInfo("allegato non esiste " + "DICHIARAZIONE_SPESA", path + "/" + allegatoId + "_" + nomefile);
				logInfo("inserisco allegato su db " + "DICHIARAZIONE_SPESA", path + "/" + allegatoId + "_" + nomefile);
				outZip = new ZipOutputStream(new FileOutputStream(fileZip));
				ezip = new ZipEntry(file.getName());
				outZip.putNextEntry(ezip);
				outZip.write(lettera, 0, lettera.length);
				outZip.closeEntry();

			} else {
				if (outZip != null) {
					outZip.finish();
					outZip.close();
					outZip = null;
				}
				if (fileZip != null) {
					if (fileZip.length() < 1000) {
						fileZip.delete();
						fileZip = null;
					}
				}
				logError(metodo, "Errore riguardante database:", "allegatoTipo inesistente");
				param[0] = ": errore tipoAllegato";
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
						"L'allegatoTipo passato non e' corretto");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (outZip != null) {
				outZip.finish();
				outZip.close();
				outZip = null;
			}
			if (fileZip != null) {
				if (fileZip.length() < 1000) {
					fileZip.delete();
					fileZip = null;
				}
			}
			logError("Archiviazione pdl DICHIARAZIONE_SPESA bandi", "Errore archivia DICHIARAZIONE_SPESA bandi", e);
		}
		datiritorno[0] = path;
		datiritorno[1] = allegatoId + "_" + nomefile;
		return datiritorno;
	}

	public String archiviaxml(byte[] lettera, String numerodomanda, String richiedente, String detCod,
			BigDecimal sportelloid, BigDecimal domandaId, String nomefile, int dicSpesaId)
			throws DatabaseException, IOException {
		String path = parametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO);
		ModelSportello sportello = new ModelSportello();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
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

			String buonoCod = rendicontazioneDao.selectBuonoCodFromNumeroDomanda(numerodomanda);

			if (!locale)
				path += "/" + buonoCod.toUpperCase();
			else
				path += "\\" + buonoCod.toUpperCase();
			dir = new File(path + "/");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (!locale)
				path += "/XML";
			else
				path += "\\XML";

			dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = null;
			if (!locale)
				file = new File(path + "/" + nomefile);
			else
				file = new File(path + "\\" + nomefile);

			logInfo("creo xml bandi ", "xml bandi " + path + " /" + nomefile);
			OutputStream out = new FileOutputStream(file);
			// cripto il file
			byte[] domandaCifrata = filesEncrypt.creaFileCifratoByte(Cipher.ENCRYPT_MODE, lettera);
			out.write(domandaCifrata, 0, domandaCifrata.length);

			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			logError("Archiviazione xml bandi", "Errore archivia xml bandi", e);
		}
		return path;
	}

	public Response invioRendicontazioneBandi(String numeroRichiesta, String xRequestId, String xForwardedFor,
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
			// verifico se esiste dichiarazione di spesa da inviare
			List<Integer> esisteinviata = rendicontazioneDao.esisteDichiarazioneSpesa(numeroRichiesta,
					Constants.CARICATA);
			if (esisteinviata.size() == 0) {
				param[0] = "Dichiarazione di spesa già inviata o inesistente per domanda " + numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
						"Dichiarazione di spesa già inviata o inesistente per domanda");
			}
			byte[] template = null;
			ResponseRest responserest = new ResponseRest();
			ModelBandiMessage body = new ModelBandiMessage();
			List<String> mesiSabbatici = rendicontazioneDao.selectMesiSabbatici(richiesta.getNumero());
			List<ModelDocumentoSpesa> spesa = rendicontazioneDao.selectDocumentoSpesa(richiesta.getNumero(),
					Constants.DA_INVIARE, mesiSabbatici);
			if (richiesta != null) {
				String[] datiritorno = new String[2];
				// recupero id bandi buono creato
				DatiBuono buonoId = logBandiDao.selectBuono(richiesta.getSportelloId(), richiesta.getDomandaId(),
						Constants.ATTIVO);
				if (buonoId != null) {
					template = creaTemplate(richiesta, spesa);
					if (template != null) {
						datiritorno = archiviaDichiarazione(template, richiesta.getNumero(),
								richiesta.getRichiedente().getCf(), richiesta.getDomandaDetCod(),
								richiesta.getSportelloId(), richiesta.getDomandaId(), "DICHIARAZIONE_SPESA.PDF",
								esisteinviata.get(0));
						// genera json
						String json = generaJson(datiritorno[1], richiesta.getNumero(), spesa);
						if (json != null) {
							// procedi con il resto
							responserest = new ResponseRest();
							// genera lo zip
							generaZip(spesa, richiesta.getRichiedente().getCf(), "DICHIARAZIONE_SPESA.PDF",
									esisteinviata.get(0), datiritorno[0], richiesta.getNumero());

							// mettere la chiamata di invio a bandi
							responserest = restbase.postInviaRendicontazioneDomande(richiesta.getNumero(),
									shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio,
									securityContext, httpHeaders, httpRequest, "file", "dati", json, nomeFileZip,
									fileZip);
							if (responserest.getStatusCode() != 200 && responserest.getStatusCode() != 201) {
								param[0] = responserest.getJson();
								logError(metodo, "Errore invio rendicontazione ", param[0]);
								logBandiDao.insertLogBandi(richiesta.getNumero(), buonoId.getBuonoId(), null, json,
										"KO", String.valueOf(responserest.getStatusCode()),
										"Errore invio rendicontazione " + param[0], Constants.INVIO_RENDICONTAZIONE);
								listerrorservice.add(
										validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
								throw new ResponseErrorException(ErrorBuilder.generateErrorBuilderError(
										StatusEnum.SERVER_ERROR, listerrorservice), "Errore set fornitore");
							}
							ObjectMapper mapper = new ObjectMapper();
							try {
								body = mapper.readValue(responserest.getJson(), ModelBandiMessage.class);
							} catch (IOException e) {
								param[0] = responserest.getJson();
								logError(metodo, "Errore invio rendicontazione mapper ", param[0]);
								logBandiDao.insertLogBandi(richiesta.getNumero(), buonoId.getBuonoId(), null, json,
										"KO", String.valueOf(responserest.getStatusCode()),
										"Errore invio rendicontazione mapper " + param[0],
										Constants.INVIO_RENDICONTAZIONE);
								e.printStackTrace();
							}
							// scrivo nella tabella di log di bandi

							if (body.getEsitoServizio().equalsIgnoreCase(Constants.BANDI_OK)) {
								logBandiDao.insertLogBandi(richiesta.getNumero(), buonoId.getBuonoId(), body.getUuid(),
										json, body.getEsitoServizio(), null, null, Constants.INVIO_RENDICONTAZIONE);
							} else {
								logBandiDao.insertLogBandi(richiesta.getNumero(), buonoId.getBuonoId(), null, json,
										body.getEsitoServizio(), body.getCodiceErrore(), body.getMessaggio(),
										Constants.INVIO_RENDICONTAZIONE);
							}
						} else {
							param[0] = "Errore nella denerazione del JSON della DICHIARAZIONE_SPESA";
							logError(metodo, "Errore Crea JSON della DICHIARAZIONE_SPESA ", param[0]);
							listerrorservice
									.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
							throw new ResponseErrorException(
									ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
									"errore in crea JSON della DICHIARAZIONE_SPESA");
						}
					} else {
						param[0] = "Errore nella denerazione del PDF del DICHIARAZIONE_SPESA";
						logError(metodo, "Errore Crea DICHIARAZIONE_SPESA ", param[0]);
						listerrorservice
								.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
								"errore in crea DICHIARAZIONE_SPESA");
					}
				} else {
					// errore non esiste il buonoid
					param[0] = "Errore non trovato buono o non in stato attivo";
					logError(metodo, "Errore Crea DICHIARAZIONE_SPESA bandi ", param[0]);
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
							"Errore non trovato buono o non in stato attivo");
				}
			} else {
				param[0] = "Errore nell'invio a Bandi del DICHIARAZIONE_SPESA domanda non trovata";
				logError(metodo, param[0]);
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
						"errore in invio a bandi del DICHIARAZIONE_SPESA domanda non trovata");
			}

			return Response.ok().entity(body).build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			if (outZip != null) {
				try {
					outZip.finish();
					outZip.close();
					outZip = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (fileZip != null) {
				if (fileZip.length() < 1000) {
					fileZip.delete();
					fileZip = null;
				}
			}
			param[0] = e.getMessage();
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			if (outZip != null) {
				try {
					outZip.finish();
					outZip.close();
					outZip = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (fileZip != null) {
				if (fileZip.length() < 1000) {
					fileZip.delete();
					fileZip = null;
				}
			}
			e.printStackTrace();
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			if (outZip != null) {
				try {
					outZip.finish();
					outZip.close();
					outZip = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (fileZip != null) {
				if (fileZip.length() < 1000) {
					fileZip.delete();
					fileZip = null;
				}
			}
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore generico ", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	public byte[] creaTemplate(ModelRichiesta domanda, List<ModelDocumentoSpesa> spesa) throws DatabaseException {

		Map<String, Object> parameters = new HashMap<String, Object>();
		logInfo("creo pdf parametri", "pdf DICHIARAZIONE_SPESA parametri");
		try {
			if (domanda.getRichiedente() != null) {
				parameters.put("RICHIEDENTE", domanda.getRichiedente().getCognome().toUpperCase() + " "
						+ domanda.getRichiedente().getNome().toUpperCase());
			}
			if (domanda.getRichiedente().getCf().equalsIgnoreCase(domanda.getDestinatario().getCf())) {
				parameters.put("SESTESSO", true);
				parameters.put("DESTINATARIO", "");
			} else {
				if (domanda.getDestinatario() != null) {
					// per altra persona
					parameters.put("SESTESSO", false);
					parameters.put("DESTINATARIO", domanda.getDestinatario().getCognome().toUpperCase() + " "
							+ domanda.getDestinatario().getNome().toUpperCase());
				}
			}
			// mensilita
			String importoMensile = rendicontazioneDao.selectImportoMensile(domanda.getDomandaId());
			parameters.put("IMPORTOMENSILE", importoMensile);
			parameters.put("MESI", "");
			parameters.put("MENSILITA", "");
			Set<String> mesitotali = new HashSet<String>();

			if (spesa != null && spesa.size() > 0) {
				for (ModelDocumentoSpesa docspesa : spesa) {
					mesitotali.addAll(docspesa.getMesi());
				}
			}
			String mesi = "";
			String mensilita = "";
			for (String mesisin : mesitotali) {
				mesi = mesi + mesisin.substring(5) + "/" + mesisin.substring(0, 4) + " ";
				mensilita = mensilita + Util.nomeMese(Converter.getInt(mesisin.substring(5))) + " "
						+ mesisin.substring(0, 4) + " ";
			}
			parameters.put("MESI", mesi);
			parameters.put("MENSILITA", mensilita);
			byte[] byteArray = getPdfTemplate(parameters);
			if (byteArray.length < 1000) // NO DATA
				return null;
			logInfo("creo pdf fine parametri", "pdf DICHIARAZIONE_SPESA");
			return byteArray;
		} catch (Exception e) {
			e.printStackTrace();
			logError("Creazione PDF DICHIARAZIONE_SPESA", "Errore crea DICHIARAZIONE_SPESA", e);
		}
		return null;
	}

	@Transactional(readOnly = true)
	public byte[] getPdfTemplate(Map<String, Object> parameters) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream input = classloader.getResourceAsStream("/report/TemplateRendicontazione.jasper");
		JasperReport jasperReport = null;
		try {

			JRPdfExporter exporter = new JRPdfExporter();

			jasperReport = (JasperReport) JRLoader.loadObject(input);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			logInfo("pdf", "esporter input");
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			logInfo("fine creo pdf", "pdf DICHIARAZIONE_SPESA");
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				logError("IOException pdf", "Errore crea DICHIARAZIONE_SPESA", e);
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

	public String generaJson(String nomefile, String numeroDomanda, List<ModelDocumentoSpesa> spesa)
			throws DatabaseException, Exception {
		String json = null;
		ModelInvioDichiarazioneSpesa dichiarazione = new ModelInvioDichiarazioneSpesa();
		dichiarazione.setNumeroDomanda(numeroDomanda);
		dichiarazione.setCodiceBando("DOM");
		dichiarazione.setDataDichiarazioneSpesa(Converter.getData(new Timestamp(System.currentTimeMillis())));
		dichiarazione.setNomeFile(nomefile);
		// prendo i mesi sabbatici

		List<ModelMese> mesi = new ArrayList<ModelMese>();
		List<String> mesiSabbatici = rendicontazioneDao.selectMesiSabbatici(numeroDomanda);
		for (String mesespesa : mesiSabbatici) {
			ModelMese mese = new ModelMese();
			mese.setValore(mesespesa.substring(5) + mesespesa.substring(0, 4));
			mese.setSabbatico(true);
			mesi.add(mese);
		}
		// prendo fornitore e contratto
		List<ModelInvioFornitore> fornitori = new ArrayList<ModelInvioFornitore>();
		List<String> fornContratto = rendicontazioneDao.selectDocumentoSpesaFornitori(numeroDomanda,
				Constants.DA_INVIARE);
		ModelInvioFornitore fornitore = new ModelInvioFornitore();
		if (fornContratto != null && fornContratto.size() > 0) {
			for (String forn : fornContratto) {
				String[] fornContr = forn.split(";");
				fornitore = rendicontazioneDao.selectFornitore(Converter.getInt(fornContr[0]),
						Converter.getInt(fornContr[1]));
				fornitori.add(fornitore);
			}
		}
		List<ModelInvioDocumentoSpesa> documentiSpesa = new ArrayList<ModelInvioDocumentoSpesa>();
		if (spesa != null && spesa.size() > 0) {
			for (ModelDocumentoSpesa docspesa : spesa) {
				boolean esiste = false;
				String descFornitore = null;
				for (String mesespesa : docspesa.getMesi()) {
					boolean trovato = false;
					for (ModelMese ms : mesi) {
						if (ms.getValore().equalsIgnoreCase(mesespesa.substring(5) + mesespesa.substring(0, 4))) {
							trovato = true;
							break;
						}
					}
					if (!trovato) {
						ModelMese mese = new ModelMese();
						mese.setValore(mesespesa.substring(5) + mesespesa.substring(0, 4));
						mese.setSabbatico(false);
						mesi.add(mese);
					}
				}
				descFornitore = docspesa.getFornitoreCf();
				ModelInvioDocumentoSpesa inviodocSpesa = rendicontazioneDao.selectDettaglioSpesa(docspesa.getId(),
						descFornitore);
				documentiSpesa.add(inviodocSpesa);
				dichiarazione.setDicSpesaCod(docspesa.getDicSpesaCod());
			}
		}

		dichiarazione.setMese(mesi);
		dichiarazione.setFornitore(fornitori);
		dichiarazione.setDocumentoDiSpesa(documentiSpesa);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		json = mapper.writeValueAsString(dichiarazione);

		return json;
	}

	private void generaZip(List<ModelDocumentoSpesa> spesa, String richiedente, String nomefile, int dicSpesaId,
			String path, String numeroDomanda) throws DatabaseException, IOException {

		List<ModelDocumentoAllegato> allegati = new ArrayList<ModelDocumentoAllegato>();
		for (ModelDocumentoSpesa docsp : spesa) {
			allegati.addAll(rendicontazioneDao.selectAllegatiSpesaId(docsp.getId()));
		}
		List<String> fornitori = rendicontazioneDao.selectDocumentoSpesaFornitori(numeroDomanda, Constants.DA_INVIARE);
		ModelSetFornitore fornitore = new ModelSetFornitore();
		if (fornitori != null && fornitori.size() > 0) {
			for (String forn : fornitori) {
				// prendo il contratto per fornitore
				// nella lista fornitore contratto
				String[] fornContr = forn.split(";");
				allegati.addAll(rendicontazioneDao.selectAllegatiContrattoId(Converter.getInt(fornContr[0]),
						Converter.getInt(fornContr[1])));
			}
		}
		if (allegati != null && allegati.size() > 0) {
			String os = System.getProperty("os.name");
			boolean locale = false;
			File file = null;
			if (os.toLowerCase().contains("win")) {
				locale = true;
			}

			// ciclo su allegato e metto nello zip
			for (int i = 0; i < allegati.size(); i++) {
				if (!locale) // Linux
					file = new File(allegati.get(i).getPath() + "/" + allegati.get(i).getNomefile());
				else // Windows
					file = new File(allegati.get(i).getPath() + "\\" + allegati.get(i).getNomefile());

				byte[] bytes = Files.readAllBytes(file.toPath());

				byte[] allegatoDecifrato = filesEncrypt.creaFileDeCifratoByte(Cipher.DECRYPT_MODE, bytes);
				ezip = new ZipEntry(allegati.get(i).getAllegatoId().toString() + "_" + file.getName());
				outZip.putNextEntry(ezip);
				outZip.write(allegatoDecifrato, 0, allegatoDecifrato.length);
				outZip.closeEntry();

			}
			if (outZip != null) {
				outZip.finish();
				outZip.close();
				outZip = null;
			}
			if (fileZip != null) {
				if (fileZip.length() < 1000) {
					fileZip.delete();
					fileZip = null;
				}
			}
		}
	}

}

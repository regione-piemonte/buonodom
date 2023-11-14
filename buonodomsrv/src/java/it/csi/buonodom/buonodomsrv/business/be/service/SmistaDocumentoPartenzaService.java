/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.business.be.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodomsrv.business.be.service.base.BaseService;
import it.csi.buonodom.buonodomsrv.dto.ModelGetAllegatoExt;
import it.csi.buonodom.buonodomsrv.dto.ModelRichiesta;
import it.csi.buonodom.buonodomsrv.dto.ModelSportello;
import it.csi.buonodom.buonodomsrv.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodomsrv.exception.DatabaseException;
import it.csi.buonodom.buonodomsrv.external.startdas.services.stardascommontypes.ConfigurazioneChiamanteType;
import it.csi.buonodom.buonodomsrv.external.startdas.services.stardascommontypes.DatiSmistaDocumentoType;
import it.csi.buonodom.buonodomsrv.external.startdas.services.stardascommontypes.DocumentoElettronicoType;
import it.csi.buonodom.buonodomsrv.external.startdas.services.stardascommontypes.EmbeddedBinaryType;
import it.csi.buonodom.buonodomsrv.external.startdas.services.stardascommontypes.MetadatiType;
import it.csi.buonodom.buonodomsrv.external.startdas.services.stardascommontypes.MetadatoType;
import it.csi.buonodom.buonodomsrv.external.startdas.services.stardascommontypes.ResultType;
import it.csi.buonodom.buonodomsrv.external.startdas.stardasservice.SmistaDocumentoRequestType;
import it.csi.buonodom.buonodomsrv.external.startdas.stardasservice.SmistaDocumentoResponseType;
import it.csi.buonodom.buonodomsrv.external.startdas.stardasservice.StardasServiceProxy;
import it.csi.buonodom.buonodomsrv.external.startdas.stardasservice.StardasServiceProxyPortType;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.SportelliDao;
import it.csi.buonodom.buonodomsrv.integration.rest.ApiManagerServiceClient;
import it.csi.buonodom.buonodomsrv.integration.service.util.FilesEncrypt;
import it.csi.buonodom.buonodomsrv.util.Constants;
import it.csi.buonodom.buonodomsrv.util.Converter;
import it.csi.buonodom.buonodomsrv.util.ErrorBuilder;
import it.csi.buonodom.buonodomsrv.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodomsrv.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodomsrv.util.validator.impl.ValidateGenericImpl;

@Service
public class SmistaDocumentoPartenzaService extends BaseService {

	@Value("${stardasUrl}")
	private String stardasUrl;
	@Autowired
	ApiManagerServiceClient tokenApiManager;
	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	CodParametroDao codParametro;

	@Autowired
	private FilesEncrypt filesEncrypt;

	@Autowired
	private SportelliDao sportelliDao;

	public Response execute(String numerorichiesta, String tipolettera, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		ModelRichiesta richiesta = new ModelRichiesta();
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErroreDettaglioExt errore = new ErroreDettaglioExt();
		String[] param = new String[1];
		SmistaDocumentoResponseType result = new SmistaDocumentoResponseType();
		try {
			ModelGetAllegatoExt lettera = new ModelGetAllegatoExt();
			// prendo la richiesta
			richiesta = richiesteDao.selectNumeroRichiesta(numerorichiesta);
			if (richiesta != null) {
				// prendo allegati solo della tipologia DINIEGO AMMESSA FINANZIATA E AMMESSA NON
				// FINANZIATA
				lettera = richiesteDao.selectAllegatiProtPartenza(richiesta.getDomandaDetId(), tipolettera);
				String parametrostardas = codParametro.selectValoreParametroFromCod(Constants.CHIAMA_STARDAS,
						Constants.PARAMETRO_GENERICO);
				boolean verificasechiamareStardas = true;
				if (parametrostardas != null)
					verificasechiamareStardas = parametrostardas.equalsIgnoreCase("TRUE") ? true : false;
				if (lettera != null) {
					lettera.setFilebyte(getAllegato(lettera.getFilePath(), lettera.getFileName()));
					if (lettera.getFilebyte().length > 0) {
						StardasServiceProxy stardas = new StardasServiceProxy();
						StardasServiceProxyPortType port = stardas.getStardasServiceProxyHttpEndpoint();
						setWSSecurity((BindingProvider) port);
						if (verificasechiamareStardas) {
							// documento prinicipale
							result = sendStardas(lettera, richiesta, 0, port);
						} else {
							ResultType risultato = new ResultType();
							result.setMessageUUID("urn:uuidstartdasdomandatestdicarico");
							risultato.setCodice(Constants.STARDAS_OK);
							risultato.setMessaggio("EsitoOK");
							result.setResult(risultato);
						}
						String messageuuid = null;
						if (!result.getResult().getCodice().equalsIgnoreCase(Constants.STARDAS_OK)
								&& !result.getResult().getCodice().equalsIgnoreCase(Constants.STARDAS_OK_PARZIALE)) {
							param[0] = result.getResult().getMessaggio();
							logError(metodo, "Errore riguardante Stardas Invio Lettera:", param[0]);
							listerrorservice
									.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
							error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
							Response esito = error.generateResponseError();
							return esito;
						}
						messageuuid = result.getMessageUUID();
						// se esito ok per allegati allora aggiorno uuid nella tabella
						richiesteDao.updateMessageUUID(richiesta.getDomandaDetCod(), messageuuid);
						return Response.ok().entity("OKSTARDAS").build();
					} else {
						param[0] = "Lettera domanda vuota " + numerorichiesta;
						logError(metodo, "Lettera domanda vuota:", param[0]);
						listerrorservice
								.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
						error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
						Response esito = error.generateResponseError();
						return esito;
					}
				} else {
					param[0] = "Lettera domanda non trovata " + numerorichiesta;
					logError(metodo, "Lettera domanda non trovata:", param[0]);
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
					error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
					Response esito = error.generateResponseError();
					return esito;
				}
			} else {
				param[0] = "Domanda non trovata " + numerorichiesta;
				logError(metodo, "Domanda non trovata:", param[0]);
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
				Response esito = error.generateResponseError();
				return esito;
			}

		} catch (WebServiceException ws) {
			ws.printStackTrace();
			if (ws.getMessage() != null)
				param[0] = metodo + " " + ws.getMessage();
			else
				param[0] = metodo + " " + "null pointer exception";
			logError(metodo, "Errore riguardante ws :", ws);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (DatabaseException e) {
			e.printStackTrace();
			if (e.getMessage() != null)
				param[0] = metodo + " " + e.getMessage();
			else
				param[0] = metodo + " " + "null pointer exception";
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null)
				param[0] = metodo + " " + e.getMessage();
			else
				param[0] = metodo + " " + "null pointer exception";
			logError(metodo, "Errore generico ", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	/*
	 * • CODICE_SERIE_DOSSIER • DESCRIZIONE_SERIE_DOSSIER (Coincide con
	 * PAROLA_CHIAVE_SERIE_DOSSIER che carica in automatico da
	 * DESCRIZIONE_SERIE_DOSSIER) • NUMERO_DOSSIER • DESCRIZIONE_DOSSIER
	 * (Coincide con PAROLA_CHIAVE_DOSSIER che carica in automatico da
	 * DESCRIZIONE_DOSSIER) • NUMERO_FASCICOLO • OGGETTO FASCICOLO ( Coincide
	 * con PAROLA_CHIAVE_FASCICOLO che carica in automatico da OGGETTO _FASCICOLO)
	 * • OGGETTO_DOCUMENTO • PAROLA_CHIAVE_DOCUMENTO • AUTORE_FISICO_DOCUMENTO
	 * • DATA_CRONICA_DOCUMENTO • MITTENTE_PF_COGNOME • MITTENTE_PF_NOME •
	 * MITTENTE_PF_CODICE_FISCALE Allegati (BUONODOM_ALL): • OGGETTO_DOCUMENTO •
	 * PAROLA_CHIAVE_DOCUMENTO • DATA_CRONICA_DOCUMENTO
	 */
	private SmistaDocumentoResponseType sendStardas(ModelGetAllegatoExt allegato, ModelRichiesta richiesta,
			int numallegati, StardasServiceProxyPortType port) {
		SmistaDocumentoResponseType smistaResponse = new SmistaDocumentoResponseType();
		SmistaDocumentoRequestType smistaRequest = new SmistaDocumentoRequestType();
		ConfigurazioneChiamanteType chiamante = new ConfigurazioneChiamanteType();
		DatiSmistaDocumentoType smistadocumento = new DatiSmistaDocumentoType();
		try {
			Map<String, String> parametri = codParametro.selectParametriPerTipo(Constants.STARDAS);
			chiamante.setCodiceApplicazione(parametri.get(Constants.CODICEAPPLICAZIONE));
			chiamante.setCodiceFiscaleEnte(parametri.get(Constants.CODICEFISCALEENTE));
			chiamante.setCodiceFruitore(parametri.get(Constants.CODICEFRUITORE));
			chiamante.setCodiceTipoDocumento(parametri.get(Constants.CODICETIPODOCUMENTOPAR));
			smistaRequest.setConfigurazioneChiamante(chiamante);

			// smistadocumento
			// verificare se mettere cf generico o quello richiedente
			smistadocumento.setIdDocumentoFruitore(richiesta.getDomandaDetCod() + "_" + allegato.getCodTipoAllegato());
			smistadocumento.setResponsabileTrattamento(parametri.get(Constants.CFTRATTAMENTO));

			DocumentoElettronicoType docelettronico = new DocumentoElettronicoType();
			EmbeddedBinaryType binarytype = new EmbeddedBinaryType();
			// invio allegato
			docelettronico.setNomeFile(allegato.getFileName());
			docelettronico.setMimeType(allegato.getFileType());
			// aggiunto
			docelettronico.setDocumentoFirmato(false);
			binarytype.setContent(allegato.getFilebyte());
			docelettronico.setContenutoBinario(binarytype);
			smistadocumento.setDocumentoElettronico(docelettronico);

			MetadatiType metadati = new MetadatiType();
			List<MetadatoType> metadato = new ArrayList<MetadatoType>();
			MetadatoType metadatosin = new MetadatoType();
			ModelSportello sportelli = sportelliDao.selectSportelli(richiesta.getSportelloId());
			// SERIE DOSSIER
			metadatosin.setNome(Constants.CODICE_SERIE_DOSSIER);
			metadatosin.setValore(parametri.get(Constants.CODICEFRUITORE) + " " + sportelli.getSportelloAnno());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DESCRIZIONE_SERIE_DOSSIER);
			metadatosin.setValore(richiesta.getContributoTipoDesc() + " " + sportelli.getSportelloAnno());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.PAROLA_CHIAVE_SERIE_DOSSIER);
			metadatosin.setValore(richiesta.getContributoTipoDesc() + " " + sportelli.getSportelloAnno());
			metadato.add(metadatosin);
			// DOSSIER
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.CODICE_DOSSIER);
			metadatosin.setValore(sportelli.getSportelloCod());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DESCRIZIONE_DOSSIER);
			metadatosin.setValore("Sportello " + richiesta.getContributoTipoDesc() + " " + sportelli.getSportelloCod());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.PAROLA_CHIAVE_DOSSIER);
			metadatosin.setValore("Sportello " + richiesta.getContributoTipoDesc() + " " + sportelli.getSportelloCod());
			metadato.add(metadatosin);
			// FASCICOLO
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.NUMERO_FASCICOLO);
			metadatosin.setValore(richiesta.getNumero());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.OGGETTO_FASCICOLO);
			if (richiesta.getDestinatario() != null) {
				if (richiesta.getDestinatario().getCf() != null) {
					// mattiamo anche la domanda errore
					metadatosin.setValore(richiesta.getNumero() + " Richiedente "
							+ richiesta.getRichiedente().getCognome() + " " + richiesta.getRichiedente().getNome() + " "
							+ richiesta.getRichiedente().getCf() + " - Destinatario "
							+ richiesta.getDestinatario().getCognome() + " " + richiesta.getDestinatario().getNome()
							+ " " + richiesta.getDestinatario().getCf());
				}
			} else {
				metadatosin.setValore("Richiedente " + richiesta.getRichiedente().getCognome() + " "
						+ richiesta.getRichiedente().getNome() + " " + richiesta.getRichiedente().getCf());
			}
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.PAROLA_CHIAVE_FASCICOLO);
			if (richiesta.getDestinatario() != null) {
				if (richiesta.getDestinatario().getCf() != null) {
					// mattiamo anche la domanda errore
					metadatosin.setValore(richiesta.getNumero() + " Richiedente "
							+ richiesta.getRichiedente().getCognome() + " " + richiesta.getRichiedente().getNome() + " "
							+ richiesta.getRichiedente().getCf() + " - Destinatario "
							+ richiesta.getDestinatario().getCognome() + " " + richiesta.getDestinatario().getNome()
							+ " " + richiesta.getDestinatario().getCf());
				}
			} else {
				metadatosin.setValore("Richiedente " + richiesta.getRichiedente().getCognome() + " "
						+ richiesta.getRichiedente().getNome() + " " + richiesta.getRichiedente().getCf());
			}
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			// DOCUMENTO
			metadatosin.setNome(Constants.OGGETTO_DOCUMENTO);
			metadatosin.setValore(richiesta.getContributoTipoDesc() + " " + allegato.getCodTipoAllegato() + " di "
					+ richiesta.getRichiedente().getCognome() + " " + richiesta.getRichiedente().getNome());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.PAROLE_CHIAVE_DOCUMENTO);
			metadatosin.setValore(richiesta.getDomandaDetCod() + "_" + richiesta.getContributoTipoDesc() + " "
					+ allegato.getCodTipoAllegato() + " di " + richiesta.getRichiedente().getCognome() + " "
					+ richiesta.getRichiedente().getNome());
			metadato.add(metadatosin);
			// nuovo metadato
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DESTINATARIO_GIURIDICO_DOCUMENTO);
			metadatosin.setValore(richiesta.getRichiedente().getCognome() + " " + richiesta.getRichiedente().getNome());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DATA_CRONICA_DOCUMENTO);
			metadatosin.setValore(Converter.getDataISO(new Date()));
			metadato.add(metadatosin);
			// RGZ in Partenza
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DESTINATARIO_PF_COGNOME);
			metadatosin.setValore(richiesta.getRichiedente().getCognome());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DESTINATARIO_PF_NOME);
			metadatosin.setValore(richiesta.getRichiedente().getNome());
			metadato.add(metadatosin);
			metadatosin = new MetadatoType();
			metadatosin.setNome(Constants.DESTINATARIO_PF_CODICE_FISCALE);
			metadatosin.setValore(richiesta.getRichiedente().getCf());
			metadato.add(metadatosin);
			metadati.setMetadato(metadato);
			smistadocumento.setMetadati(metadati);
			// verificare se bisogna mettere il numero degli allegati
			smistadocumento.setNumAllegati(0);

			smistaRequest.setDatiSmistaDocumento(smistadocumento);

			// chiamo stardas
			System.out.println("--- Request Stardas ---");
			JAXBContext jaxRes = JAXBContext.newInstance(SmistaDocumentoRequestType.class);
			Marshaller jaxMres = jaxRes.createMarshaller();
			jaxMres.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxMres.marshal(smistaRequest, System.out);
			System.out.println("---------------------------\n");

			// chiamo stardas
			smistaResponse = port.smistaDocumento(smistaRequest);

			System.out.println("--- Response Stardas ---");
			jaxRes = JAXBContext.newInstance(SmistaDocumentoResponseType.class);
			jaxMres = jaxRes.createMarshaller();
			jaxMres.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxMres.marshal(smistaResponse, System.out);
			System.out.println("---------------------------\n");
			// if
			// (smistaResponse.getResult().getCodice().equalsIgnoreCase(Constants.STARDAS_OK)
			// ||
			// smistaResponse.getResult().getCodice().equalsIgnoreCase(Constants.STARDAS_OK_PARZIALE))
			// {
			// esito positivo prendo l'uuid per il prossimo invio
			return smistaResponse;
			// }
		} catch (WebServiceException ws) {
			logError("SmistaDocumentoStardas", "Errore ws ", ws);
			ws.printStackTrace();
		} catch (DatabaseException e) {
			logError("SmistaDocumentoStardas", "Errore db ", e);
			e.printStackTrace();
		} catch (Exception e) {
			logError("SmistaDocumentoStardas", "Errore generico ", e);
			e.printStackTrace();
		}
		return null;
	}

	private void setWSSecurity(BindingProvider prov) {
		Map<String, List<String>> requestHeaders = new HashMap<>();
		prov.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, stardasUrl);
		String token;
		try {
			token = tokenApiManager.refreshToken();
			if (token != null) {
				requestHeaders.put("Authorization", Collections.singletonList(token));
				prov.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private byte[] getAllegato(String filePath, String fileName) {

		String os = System.getProperty("os.name");
		boolean locale = false;
		File file = null;
		byte[] allegatoDecifrato = null;
		try {
			if (os.toLowerCase().contains("win")) {
				locale = true;
			}
			if (!locale)
				file = new File(filePath + "/" + fileName);
			else
				file = new File(filePath + "\\" + fileName);

			byte[] bytes;

			bytes = Files.readAllBytes(file.toPath());

			allegatoDecifrato = filesEncrypt.creaFileDeCifratoToByte(Cipher.DECRYPT_MODE, bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logError("Decripta docuemnto", "Errore generico ", e);
			e.printStackTrace();
		}

		return allegatoDecifrato;
	}
}
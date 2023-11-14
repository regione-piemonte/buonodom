/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.dto.ModelDecodifica;
import it.csi.buonodom.buonodombff.dto.ModelDocumentoSpesa;
import it.csi.buonodom.buonodombff.dto.ModelDocumentoSpesaDettaglio;
import it.csi.buonodom.buonodombff.dto.ModelFornitore;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelGetAllegato;
import it.csi.buonodom.buonodombff.dto.custom.ModelRichiestaExt;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.integration.dao.custom.AllegatoBuonoDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.FornitoriDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.GetListeDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.RendicontazioneDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class DocumentoSpesaService extends BaseService {

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	FornitoriDao fornitoriDao;

	@Autowired
	RendicontazioneDao rendicontazioneDao;

	@Autowired
	AllegatoBuonoDao allegatoBuonoDao;

	@Autowired
	GetListeDao getListeDao;

	public Response addRendicontazione(String numeroRichiesta, ModelDocumentoSpesa documentoSpesa, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		List<ErroreDettaglioExt> listError = new ArrayList<ErroreDettaglioExt>();
		try {

			// VALIDAZIONE 400
			listError = validateGeneric.validateAddRendicontazione(numeroRichiesta, documentoSpesa, xRequestId,
					xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			// verifico se lo stato del buono attivo se no non faccio proseguire da fare
//			String buonoCod = allegatoBuonoDao.selectBuonoCodFromNumeroDomanda(numeroRichiesta);
//			if (buonoCod==null) {
//				param[0] = "Buono non in stato attivo per domanda " + numeroRichiesta;
//                listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
//                throw new ResponseErrorException(
//                        ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
//                        "Buono non in stato attivo per domanda");
//			}
			// prendo i mesi sabbatici se il periodo riguarda un mese sabbatico deve essere
			// prima rimosso
			List<String> mesiSabbatici = rendicontazioneDao.selectMesiSabbatici(numeroRichiesta);
			for (String docSpesa : documentoSpesa.getMesi()) {
				if (mesiSabbatici.contains(docSpesa)) {
					param[0] = "Documentazione di spesa non permessa per il mese " + docSpesa;
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
							"Documentazione di spesa non permessa per il mese " + docSpesa);
				}
			}
			List<Integer> esisteinviata = rendicontazioneDao.esisteDichiarazioneSpesa(numeroRichiesta,
					Constants.INVIATA);
			if (esisteinviata.size() > 0) {
				param[0] = "Dichiarazione di spesa già inviata per domanda " + numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
						"Dichiarazione di spesa già inviata per domanda");
			}
//			boolean esiste = rendicontazioneDao.esisteDocumentoSpesa(numeroRichiesta, documentoSpesa.getTipologia(), documentoSpesa.getMesi());
//			if (esiste) {
//				param[0] = "Documento spesa presente " + documentoSpesa.getTipologia();
//				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
//				throw new ResponseErrorException(
//						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
//						"Documento spesa presente");
//			}
			ModelRichiestaExt richiesta = richiesteDao.selectNumeroRichiestaExt(numeroRichiesta);
			if (richiesta != null) {
				listError = validateGeneric.checkCodFiscaleAndShibIden(listError, richiesta.getRichiedente().getCf(),
						shibIdentitaCodiceFiscale);
				if (!listError.isEmpty()) {
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
							"errore in validate");
				}
				// tipologia dettaglio spesa
				boolean trovatovalore = false;
				List<ModelDecodifica> lista = getListeDao.selectTipiDocumentoSpesa();
				for (ModelDecodifica tipo : lista) {
					if (tipo.getCodice().equalsIgnoreCase(documentoSpesa.getTipologia())) {
						trovatovalore = true;
						break;
					}
				}
				if (!trovatovalore) {
					param[0] = "Tipologia documento spesa " + documentoSpesa.getTipologia();
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR23.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
							"tipologia documento spesa inesistente");
				}
				boolean fornitoreesiste = false;
				// controllo presenza fornitore
				List<ModelFornitore> fornitore = fornitoriDao.selectFornitori(numeroRichiesta);
				if (fornitore != null && fornitore.size() > 0) {
					for (ModelFornitore forn : fornitore) {
						if (forn.getId().equals(documentoSpesa.getIdFornitore())) {
							fornitoreesiste = true;
							break;
						}
					}
				} else {
					param[0] = "Fornitore " + documentoSpesa.getIdFornitore().toString();
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR23.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
							"fornitore inesistente");
				}
				if (!fornitoreesiste) {
					param[0] = "Fornitore " + documentoSpesa.getIdFornitore().toString();
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR23.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
							"fornitore inesistente");
				}
				// controllo presenza allegati
				BigDecimal importototale = BigDecimal.ZERO;
				BigDecimal importoqueitanzato = BigDecimal.ZERO;
				trovatovalore = false;
				boolean allegatotrovato = true;
				if (documentoSpesa.getDettagli().size() != 2) {
					param[0] = "Numero allegati documento spesa dettaglio errati";
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
							"numero allegati documento spesa dettaglio errati");
				}
				lista = getListeDao.selectTipiDocumentoSpesaDettaglio();
				for (ModelDocumentoSpesaDettaglio dettaglio : documentoSpesa.getDettagli()) {
					trovatovalore = false;
					for (ModelDecodifica tipo : lista) {
						if (tipo.getCodice().equalsIgnoreCase(dettaglio.getTipo())) {
							trovatovalore = true;
							ModelDecodifica tipodaremove = new ModelDecodifica();
							tipodaremove.setCodice(tipo.getCodice());
							tipodaremove.setEtichetta(tipo.getEtichetta());
							lista.remove(tipodaremove);
							break;
						}
					}
					if (!trovatovalore) {
						param[0] = "Tipologia documento spesa dettaglio " + dettaglio.getTipo();
						listerrorservice
								.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR23.getCode(), param));
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
								"tipologia documento spesa dettaglio inesistente");
					}
					if (dettaglio.getTipo().equalsIgnoreCase(Constants.GIUSTIFICATIVO))
						importoqueitanzato = dettaglio.getImporto();
					if (dettaglio.getTipo().equalsIgnoreCase(Constants.QUIETANZA))
						importototale = dettaglio.getImporto();
					ModelGetAllegato allegato = allegatoBuonoDao.selectAllegatoBuonoPerTipo(dettaglio.getIdAllegato(),
							dettaglio.getTipo());
					if (allegato != null) {
						allegatotrovato = true;
						// verificare che esiste allegato su file system
						validateGeneric.checkAllegatoBuonoFileSystem(listError, allegato.getFilePath(),
								allegato.getFileName());
						if (!listError.isEmpty()) {
							throw new ResponseErrorException(
									ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listError),
									"errore in allega documenti");
						}
					} else {
						allegatotrovato = false;
					}
				}
				if (!allegatotrovato) {
					param[0] = "Allegato ";
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR23.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
							"allegato inesistente");
				}
				// INSERISCO DOCUMENTO DI SPESA E DETTAGLI
				List<Integer> dicSpesaId = new ArrayList<Integer>();
				int docSpesaId = 0;
				// devo verificare se esiste una dichiarazione su quel buono
				dicSpesaId = rendicontazioneDao.insertDichiarazioneSpesa(numeroRichiesta,
						richiesta.getRichiedente().getCf(), richiesta.getRichiedente().getCf());
				if (dicSpesaId.size() == 1) {
					docSpesaId = rendicontazioneDao.insertDocumentoSpesa(documentoSpesa.getMesi(),
							documentoSpesa.getTipologia(), dicSpesaId.get(0), documentoSpesa.getIdFornitore(),
							richiesta.getRichiedente().getCf(), richiesta.getRichiedente().getCf(),
							documentoSpesa.getNumero(), importototale, importoqueitanzato, richiesta.getDomandaId(),
							documentoSpesa.getNote(), documentoSpesa.getStato());
				} else {
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listError),
							"errore in insert");
				}
				if (docSpesaId > 0) {
					for (ModelDocumentoSpesaDettaglio dettaglio : documentoSpesa.getDettagli()) {
						rendicontazioneDao.insertDocumentoSpesaDettaglio(dettaglio.getTipo(), docSpesaId,
								richiesta.getRichiedente().getCf(), richiesta.getRichiedente().getCf(),
								dettaglio.getImporto(), dettaglio.getData(), dettaglio.getIdAllegato());
					}
				} else {
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listError),
							"errore in insert");
				}
				documentoSpesa.setId(docSpesaId);
				return Response.status(200).entity(documentoSpesa).build();
			} else {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");
			}

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

	public Response getRendicontazione(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		try {
			// validate
			List<ErroreDettaglioExt> listError = validateGeneric.validateListaDecodifica(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			ModelRichiestaExt richiesta = richiesteDao.selectNumeroRichiestaExt(numeroRichiesta);
			if (richiesta != null) {
				listError = validateGeneric.checkCodFiscaleAndShibIden(listError, richiesta.getRichiedente().getCf(),
						shibIdentitaCodiceFiscale);
				if (!listError.isEmpty()) {
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
							"errore in validate");
				}
			} else {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");
			}
			// chiamo la get per avere i dati del documento di spesa
			List<String> mesiSabbatici = rendicontazioneDao.selectMesiSabbatici(numeroRichiesta);
			List<ModelDocumentoSpesa> spesa = rendicontazioneDao.selectDocumentoSpesa(numeroRichiesta, mesiSabbatici);

			return Response.status(200).entity(spesa).build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
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
}

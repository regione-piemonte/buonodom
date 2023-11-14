/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.dto.ModelContratto;
import it.csi.buonodom.buonodombff.dto.ModelContrattoAllegati;
import it.csi.buonodom.buonodombff.dto.ModelContrattoFornitore;
import it.csi.buonodom.buonodombff.dto.ModelRichiesta;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.integration.dao.custom.AllegatoBuonoDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.ContrattiDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class ContrattiService extends BaseService {

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	AllegatoBuonoDao allegatoBuonoDao;

	@Autowired
	ContrattiDao contrattiDao;

	@Autowired
	RichiesteDao richiesteDao;

	public Response addContratto(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContrattoAllegati contrattoAllegati,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		List<ErroreDettaglioExt> listError = new ArrayList<ErroreDettaglioExt>();
		try {
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			listError = validateGeneric.checkCodFiscaleAndShibIden(listError, richiesta.getRichiedente().getCf(),
					shibIdentitaCodiceFiscale);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"Il codice fiscale del richiedente della domanda non corrisponde");
			}
			// VALIDAZIONE 400
			Long rapporto_tipo_id = richiesteDao
					.selectIdRapporto(contrattoAllegati.getContratto().getRelazioneDestinatario());
			Long contratto_tipo_id = richiesteDao.selectIdContratto(contrattoAllegati.getContratto().getTipo());
			Long fornitore_tipo_id = contrattiDao
					.selectIdFornitoreTipo(contrattoAllegati.getContratto().getTipoSupportoFamiliare());
			String cfFornitore = null;
			int fornitoreId = 0;
			if (contrattoAllegati.getContratto().getTipo().equals(Constants.COOPERATIVA))
				cfFornitore = contrattoAllegati.getContratto().getAgenzia().getCf();
			else
				cfFornitore = contrattoAllegati.getContratto().getAssistenteFamiliare().getCf();
			fornitoreId = contrattiDao.checkEsistenzaAssistenteFamigliare(cfFornitore, numeroRichiesta);
			listError = validateGeneric.validateAddContratto(numeroRichiesta, rapporto_tipo_id, contratto_tipo_id,
					fornitore_tipo_id, xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
					contrattoAllegati, richiesta.getDestinatario().getCf(), fornitoreId);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			// VALIDAZIONE 404
			Long idDettaglio = richiesteDao.selectIdDettaglio(numeroRichiesta);

			if (idDettaglio == null) {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");
			}

			if (contrattoAllegati.getAllegati() != null) {
				if (!contrattiDao.checkEsistenzaAllegati(contrattoAllegati.getAllegati())) {
					param[0] = "allegati";
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
							"Allegati inesistenti");
				}
			}

			listError = validateGeneric.checkAllegatoContrattoFileSystem(contrattoAllegati);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			if (fornitoreId == 0) {
				fornitoreId = contrattiDao.insertAssistenteFamigliare(contrattoAllegati.getContratto(), numeroRichiesta,
						shibIdentitaCodiceFiscale);
			} else {
				contrattiDao.updateAssistenteFamigliare(contrattoAllegati.getContratto(), numeroRichiesta,
						shibIdentitaCodiceFiscale, cfFornitore);
			}

			// INSERT CONTRATTO
			ModelContratto anteprimaContratto = contrattiDao.insertContratto(contrattoAllegati.getContratto(),
					numeroRichiesta, shibIdentitaCodiceFiscale, fornitoreId);

			// INSERT ALLEGATI
			contrattiDao.insertAllegatiContratto(anteprimaContratto.getId(), contrattoAllegati.getAllegati(),
					shibIdentitaCodiceFiscale);

			contrattoAllegati.getContratto().setId(anteprimaContratto.getId());
			return Response.status(200).entity(contrattoAllegati).build();

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

	public Response getContratti(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		List<ErroreDettaglioExt> listError = new ArrayList<ErroreDettaglioExt>();
		try {

			// VALIDAZIONE 400
			listError = validateGeneric.validateGetContratto(numeroRichiesta, xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			// VALIDAZIONE 404
			Long idDettaglio = richiesteDao.selectIdDettaglio(numeroRichiesta);

			if (idDettaglio == null) {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");
			}

			// VALIDAZIONE 403
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			listError = validateGeneric.checkCodFiscaleAndShibIden(listError, richiesta.getRichiedente().getCf(),
					shibIdentitaCodiceFiscale);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"Il codice fiscale del richiedente della domanda non corrisponde");
			}

			// SELECT CONTRATTI
			List<ModelContrattoAllegati> contrattiAllegati = contrattiDao
					.selectContrattiByNumeroRichiesta(numeroRichiesta);

			return Response.status(200).entity(contrattiAllegati).build();

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

	public Response putContratto(String numeroRichiesta, Integer idContratto, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContratto contratto,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		List<ErroreDettaglioExt> listError = new ArrayList<ErroreDettaglioExt>();
		try {

			// VALIDAZIONE 400
			listError = validateGeneric.validatePutContratto(numeroRichiesta, idContratto, xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale, contratto);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			// VALIDAZIONE 404
			Long idDettaglio = richiesteDao.selectIdDettaglio(numeroRichiesta);
			if (idDettaglio == null) {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");
			}
			ModelContratto contrattoDb = contrattiDao.selectContrattoById(idContratto, numeroRichiesta);
			if (contrattoDb == null) {
				param[0] = idContratto.toString();
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR05.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"contratto non esistente");
			}
			if (contrattoDb.getDataFine() != null) {
				// se il contratto chiuso nessuna modifica
				// posso anticipare la data di chiusura
				if (contratto.getDataFine().compareTo(contrattoDb.getDataFine()) > 0) {
					param[0] = "Impossibile posticipare la data di chiusura del contratto " + idContratto.toString();
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
							"Impossibile posticipare la data di chiusura del contratto");
				}
			}
			// se la data fine minore uguale a data inizio
			if (contratto.getDataFine().compareTo(contrattoDb.getDataInizio()) < 0) {
				param[0] = "Data fine Contratto minore o uguale data inizio " + idContratto.toString();
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
						"Data fine Contratto minore o uguale data inizio");
			}
			// VALIDAZIONE 403
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			listError = validateGeneric.checkCodFiscaleAndShibIden(listError, richiesta.getRichiedente().getCf(),
					shibIdentitaCodiceFiscale);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"Il codice fiscale del richiedente della domanda non corrisponde");
			}

			// CHIUDI CONTRATTO
			contrattiDao.chiudiContratto(idContratto, shibIdentitaCodiceFiscale, contratto.getDataFine());
			contrattoDb.setDataFine(contratto.getDataFine());
			return Response.status(200).entity(contrattoDb).build();

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

	public Response delContratto(String numeroRichiesta, Integer idContratto, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		List<ErroreDettaglioExt> listError = new ArrayList<ErroreDettaglioExt>();
		try {

			// VALIDAZIONE 400
			listError = validateGeneric.validateDelContratto(numeroRichiesta, idContratto, xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			// VALIDAZIONE 403
			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			listError = validateGeneric.checkCodFiscaleAndShibIden(listError, richiesta.getRichiedente().getCf(),
					shibIdentitaCodiceFiscale);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"Il codice fiscale del richiedente della domanda non corrisponde");
			}

			// VALIDAZIONE 404
			Long idDettaglio = richiesteDao.selectIdDettaglio(numeroRichiesta);
			if (idDettaglio == null) {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");
			}
			List<ModelContrattoFornitore> forn = new ArrayList<ModelContrattoFornitore>();
			forn = contrattiDao.esisteDocumentoSuContratto(numeroRichiesta, idContratto);
			if (forn != null && forn.size() > 0) {
				for (ModelContrattoFornitore verifica : forn) {
					// se esiste almento una inviata esco con errore che non si cancella contratto
					// mai
					if (verifica.getStato().equalsIgnoreCase(Constants.DA_VALIDARE)) {
						param[0] = numeroRichiesta;
						listerrorservice
								.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR25.getCode(), param));
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.UNPROCESSABLE_ENTITY,
										listerrorservice),
								"contratto con documenti di spesa associati ed inviata a bandi");
					}
				}
				// trovate delle dichiarazioni vanno eliminate prima di procedere sono tutte
				// caricate
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR24.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.UNPROCESSABLE_ENTITY, listerrorservice),
						"contratto con documenti di spesa associati e non inviata a bandi");
			}

			boolean esito = contrattiDao.eliminaAllegatiContratto(idContratto);
			if (esito)
				return Response.status(200).entity("Contratto eliminato").build();
			else {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR05.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"errore sui contratti");
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

}

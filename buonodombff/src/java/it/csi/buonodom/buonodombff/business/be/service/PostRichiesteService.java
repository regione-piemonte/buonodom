/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.business.be.service.util.FilesEncrypt;
import it.csi.buonodom.buonodombff.dto.ModelAnteprimaRichiesta;
import it.csi.buonodom.buonodombff.dto.ModelBozzaRichiesta;
import it.csi.buonodom.buonodombff.dto.ModelRichiesta;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.rest.ResponseRest;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class PostRichiesteService extends BaseService {

	@Autowired
	private FilesEncrypt filesEncrypt;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	ServizioRestService servizioRestService;

	public Response richiestePost(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, ModelBozzaRichiesta richiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		long tempoPartenza = System.currentTimeMillis();
		log.info("richiestepost - BEGIN time: ");
		String[] param = new String[1];
		try {
			Long idStato = richiesteDao.selectIdStato(Constants.BOZZA);

			Long idContributo = richiesteDao.selectIdContributo(Constants.BUONODOM);
			String codStato = richiesteDao.selectCodStato(Constants.BOZZA);
			String codContributo = richiesteDao.selectCodContributo(Constants.BUONODOM);
			Long idSportello = richiesteDao.selectIdSportello();

			Long idTitolo = richiesteDao.selectIdTitolo(richiesta.getStudioDestinatario());
			Long idRapporto = richiesteDao.selectIdRapporto(richiesta.getDelega());
			Long idAsl = richiesteDao.selectIdAsl(richiesta.getAslDestinatario());

			long tempoId = System.currentTimeMillis();

			log.info("richiestePost richiesteDao.selectId " + System.currentTimeMillis() + " millis: "
					+ (tempoPartenza - tempoId));
			Date dataAttuale = new Date();

			List<ErroreDettaglioExt> listError = validateGeneric.validatePostRichieste(xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, richiesta,
					idStato, idContributo, idSportello, idTitolo, idAsl, idRapporto);

			long tempoValidate = System.currentTimeMillis();
			log.info("richiestePost richiesteDao.validatePostRichieste " + System.currentTimeMillis() + " millis: "
					+ (tempoPartenza - tempoId));

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			Boolean residentePiemonte = richiesteDao
					.isResidenzaPiemontese(richiesta.getDestinatario().getProvinciaResidenza());
			// validate
			if (!residentePiemonte) {
				param[0] = richiesta.getDestinatario().getCognome() + " " + richiesta.getDestinatario().getNome();
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR19.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.UNPROCESSABLE_ENTITY, listerrorservice),
						"destinatario non piemontese");
			}
			if (richiesteDao.presenzaDomanda(richiesta.getDestinatario().getCf())) {
				param[0] = richiesta.getDestinatario().getCf();
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR09.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.CONFLICT, listerrorservice),
						"domanda gia' presente");
			}
			Long idDomanda = richiesteDao.insertTDomanda(richiesta, idContributo, idSportello,
					shibIdentitaCodiceFiscale);
			String codDomandaDet = codStato + "_";
			Long idDomandaDet = richiesteDao.insertTDomandaDettaglio(richiesta, shibIdentitaCodiceFiscale, idDomanda,
					idSportello, idStato, idRapporto, idTitolo, idAsl, codDomandaDet);
			String numeroDomanda = richiesteDao.selectNumeroDomanda(idDomanda);
			ModelAnteprimaRichiesta anteprimaRichiesta = new ModelAnteprimaRichiesta();
			anteprimaRichiesta.setNumero(numeroDomanda);
			anteprimaRichiesta.setStato(codStato);
			anteprimaRichiesta.setDataAggiornamento(dataAttuale);
			anteprimaRichiesta.setRichiedente(richiesta.getRichiedente());
			anteprimaRichiesta.setDestinatario(richiesta.getDestinatario());
			anteprimaRichiesta.setNote(richiesta.getNote());

			long timeInsertDomanda = System.currentTimeMillis();

			log.info("richiestePost richiesteDao.insertTDomanda " + System.currentTimeMillis() + " millis: "
					+ (tempoValidate - timeInsertDomanda));

			ResponseRest responserest = servizioRestService.getCreaDomanda(xRequestId, xForwardedFor, xCodiceServizio,
					securityContext, httpHeaders, httpRequest, numeroDomanda);

			long timeChiamaRest = System.currentTimeMillis();

			log.info("richiestePost richiesteDao.insertTDomanda " + System.currentTimeMillis() + " millis: "
					+ (timeInsertDomanda - timeChiamaRest));

			if (responserest.getStatusCode() == 0) {
				// errore
				logError(metodo, "Errore generico crea domanda ", null);
				param[0] = "Errore Crea Domanda";
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
			} else {
				return Response.status(200).entity(anteprimaRichiesta).build();
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

	public Response richiestePut(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelRichiesta richiesta,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();

		String[] param = new String[1];
		try {
			Long idDettaglio = richiesteDao.selectIdDettaglio(numeroRichiesta);
			if (idDettaglio == null) {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");
			}
			Long idTitolo = richiesteDao.selectIdTitolo(richiesta.getStudioDestinatario());
			Long idRapporto = richiesteDao.selectIdRapporto(richiesta.getDelega());
			Long idAsl = richiesteDao.selectIdAsl(richiesta.getAslDestinatario());
			Long idValutazione = richiesteDao
					.selectIdValutazioneMultidimensionale(richiesta.getValutazioneMultidimensionale());
			Long idRelazione = null;
			Long idContratto = null;
			Long idTipoAssistente = null;
			if (richiesta.getContratto() != null) {
				idRelazione = richiesteDao.selectIdRapporto(richiesta.getContratto().getRelazioneDestinatario());
				idContratto = richiesteDao.selectIdContratto(richiesta.getContratto().getTipo());
				idTipoAssistente = richiesteDao
						.selectIdTipoAssistente(richiesta.getContratto().getTipoSupportoFamiliare());
			}
			// validate
			ModelRichiesta richiestaDb = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			List<ErroreDettaglioExt> listError = validateGeneric.validatePutRichieste(xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, richiesta,
					idTitolo, idRapporto, idAsl, idRelazione, idValutazione, idContratto, numeroRichiesta, richiestaDb,
					idTipoAssistente);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			richiesteDao.updateTDettaglioDomanda(idDettaglio, richiesta, idTitolo, idRapporto, idAsl, idValutazione,
					idRelazione, idContratto, shibIdentitaCodiceFiscale, idTipoAssistente);

			ResponseRest responserest = servizioRestService.getCreaDomanda(xRequestId, xForwardedFor, xCodiceServizio,
					securityContext, httpHeaders, httpRequest, richiesta.getNumero());

			if (responserest.getStatusCode() == 0) {
				// errore
				logError(metodo, "Errore generico crea domanda ", null);
				param[0] = "Errore Crea Domanda";
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
			} else {
				return Response.ok().entity(richiesta).build();
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

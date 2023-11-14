/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.business.be.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombo.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombo.dto.CambioStatoPopUp;
import it.csi.buonodom.buonodombo.dto.ModelEmailDetId;
import it.csi.buonodom.buonodombo.dto.ModelRichiesta;
import it.csi.buonodom.buonodombo.dto.ModelVerifiche;
import it.csi.buonodom.buonodombo.dto.UserInfo;
import it.csi.buonodom.buonodombo.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombo.exception.DatabaseException;
import it.csi.buonodom.buonodombo.exception.ResponseErrorException;
import it.csi.buonodom.buonodombo.filter.IrideIdAdapterFilter;
import it.csi.buonodom.buonodombo.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodombo.integration.dao.custom.EntiGestoriDao;
import it.csi.buonodom.buonodombo.integration.dao.custom.GraduatoriaDao;
import it.csi.buonodom.buonodombo.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodombo.util.Constants;
import it.csi.buonodom.buonodombo.util.ErrorBuilder;
import it.csi.buonodom.buonodombo.util.SendEmailSMTP;
import it.csi.buonodom.buonodombo.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombo.util.enumerator.ErrorParamEnum;
import it.csi.buonodom.buonodombo.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombo.util.validator.impl.ValidateGenericImpl;

@Service
public class EntiGestoriService extends BaseService {

	@Autowired
	EntiGestoriDao entigestoriDao;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	SendEmailSMTP sendEmailSMTP;

	@Autowired
	CodParametroDao parametroDao;

	@Autowired
	GraduatoriaDao graduatoriaDao;

	// Get lista degli Enti Gestori
	public Response getEntiGestori(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String[] param = new String[1];
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		try {
			return Response.ok().entity(entigestoriDao.selectEntiGestori()).build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			param[0] = "Errore riguardante database";
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
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

	public Response richiediRettifica(CambioStatoPopUp datiPopUp, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName(); // nome del metodo, inserito nei log in caso di errore

		String[] param = new String[1];
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErrorBuilder error = null;

		try {
			ModelEmailDetId richiesta = richiesteDao.selectEmailDetId(datiPopUp.getNumerodomanda());
			if (richiesta != null) {
				entigestoriDao.updateNoteEnteDettaglio(richiesta.getDomandaDetId(), userInfo.getCodFisc(),
						datiPopUp.getNotaEnte());
				entigestoriDao.updateRettificaDettaglio(richiesta.getDomandaDetId(), userInfo.getCodFisc(), true, null,
						null);
				String bodyMessage = parametroDao.selectValoreParametroFromCod(Constants.BODY_MESSAGE,
						Constants.PARAMETRO_GENERICO);
				String objectMessage = parametroDao
						.selectValoreParametroFromCod(Constants.OBJECT_MESSAGE, Constants.PARAMETRO_GENERICO)
						.replace("{0}", datiPopUp.getNumerodomanda());
				sendEmailSMTP.send(richiesta.getEmail(), bodyMessage, objectMessage);
				return Response.status(200).entity(true).build();
			} else {
				param[0] = datiPopUp.getNumerodomanda();
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");

			}
		} catch (DatabaseException e) {
			param[0] = "Errore riguardante database";
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore stato richiesta:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			param[0] = e.getMessage();
			logError(metodo, "Errore generico ", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	public Response salvaVerificaEnte(String numeroRichiesta, ModelVerifiche verifica, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName(); // nome del metodo, inserito nei log in caso di errore

		String[] param = new String[1];
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErrorBuilder error = null;

		try {
			listerrorservice = validateGeneric.checkEmptyString(listerrorservice, verifica.getNoteEnteGestore(),
					ErrorParamEnum.VERIFICA_NOTE.getCode());

			if (!listerrorservice.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
						"errore in validate");
			}

			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			if (richiesta != null) {

				entigestoriDao.salvaVerificaEnteDettaglio(richiesta.getDomandaDetId(), userInfo.getCodFisc(),
						verifica.getVerificaEgPunteggio(), verifica.isVerificaEgIncompatibilita(),
						verifica.getNoteEnteGestore());

				if (richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA)
						|| richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA)) {

					long idGraduatoria = graduatoriaDao
							.selectGraduatoriaIdByDomandaDettaglioId(richiesta.getDomandaDetId());
					if (idGraduatoria != 0) {
						if (graduatoriaDao.checkStatoGraduatoria(idGraduatoria, Constants.PROVVISORIA)) {
							graduatoriaDao.updateRGraduatoriaStato(idGraduatoria, Constants.DA_AGGIORNARE,
									userInfo.getCodFisc());
						}
					}
				}
				return Response.status(200).entity(true).build();
			} else {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");

			}
		} catch (DatabaseException e) {
			param[0] = "Errore riguardante database";
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore stato richiesta:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			param[0] = e.getMessage();
			logError(metodo, "Errore generico ", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	public Response concludiVerificaEnte(String numeroRichiesta, ModelVerifiche verifica,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName(); // nome del metodo, inserito nei log in caso di errore

		String[] param = new String[1];
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErrorBuilder error = null;

		try {

			listerrorservice = validateGeneric.checkEmptyString(listerrorservice, verifica.getNoteEnteGestore(),
					ErrorParamEnum.VERIFICA_NOTE.getCode());

			if (!listerrorservice.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
						"errore in validate");
			}

			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			if (richiesta != null) {
				entigestoriDao.salvaVerificaEnteDettaglio(richiesta.getDomandaDetId(), userInfo.getCodFisc(),
						verifica.getVerificaEgPunteggio(), verifica.isVerificaEgIncompatibilita(),
						verifica.getNoteEnteGestore());

				entigestoriDao.updateRettificaDettaglio(richiesta.getDomandaDetId(), userInfo.getCodFisc(),
						verifica.isVerificaEgRichiesta(), verifica.isVerificaEgInCorso(),
						verifica.isVerificaEgConclusa());
				return Response.status(200).entity(true).build();
			} else {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");

			}
		} catch (DatabaseException e) {
			param[0] = "Errore riguardante database";
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore stato richiesta:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			param[0] = e.getMessage();
			logError(metodo, "Errore generico ", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	public Response updateToVerificaInCorso(String numeroDomande[], SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {

		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName(); // nome del metodo, inserito nei log in caso di errore
		String[] param = new String[1];
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErrorBuilder error = null;
		try {
			entigestoriDao.updateToVerificaInCorso(numeroDomande, userInfo.getCodFisc());
			return Response.status(200).entity(true).build();
		} catch (DatabaseException e) {
			param[0] = "Errore riguardante database";
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (Exception e) {
			param[0] = e.getMessage();
			logError(metodo, "Errore generico ", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}
}

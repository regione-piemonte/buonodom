/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.dto.ModelCronologia;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelBuono;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.integration.dao.custom.BuonoDao;
import it.csi.buonodom.buonodombff.integration.service.LogAuditService;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class CronologiaBuonoService extends BaseService {

	@Autowired
	LogAuditService logaudit;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	BuonoDao buonoDao;

	public Response getCronologiaRichieste(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		List<ModelCronologia> cronologia = new ArrayList<ModelCronologia>();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		try {
			// validate
			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono == null) {
				param[0] = "Buono per la domanda " + numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR23.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"buono inesistente");
			}
			List<ErroreDettaglioExt> listError = validateGeneric.validateCronologia(xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest,
					buono.getRichiedenteCf());

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			cronologia = buonoDao.selectCronologiaBuono(buono.getBuonoId());

			return Response.status(200).entity(cronologia).build();

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

	public Response postCronologiaRichieste(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, String stato, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		ModelCronologia cronologia = new ModelCronologia();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		try {
			// validate
			List<ErroreDettaglioExt> listError = validateGeneric.validatePostCronologia(xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, stato);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			boolean esisteStato = buonoDao.esisteStato(stato);
			if (!esisteStato) {
				param[0] = "Stato " + stato;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR23.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
						"Stato buono non esiste");
			}
			ModelBuono buono = buonoDao.selectBuono(numeroRichiesta);
			if (buono != null) {
				if (!buono.getRichiedenteCf().equals(shibIdentitaCodiceFiscale)) {
					param[0] = numeroRichiesta;
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR12.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
							"Il codice fiscale del richiedente della domanda non corrisponde");
				}

				listError = validateGeneric.checkCambioStatoCoerenteBuono(listError, stato, buono.getStato());
				if (!listError.isEmpty()) {
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
							"stato non coerente");
				}
				buonoDao.updateStatoBuono(buono.getBuonoId(), buono.getRichiedenteCf(), buono.getBuonoStatoId());
				buonoDao.insertBuonoStato(stato, buono.getRichiedenteCf(), buono.getBuonoId(), buono.getIban(),
						buono.getIbanIntestatario());

				cronologia.setStato(stato);
				cronologia.setDataAggiornamento(new Date());
				cronologia.setNumero(buono.getBuonoCod());
				cronologia.setNote(null);
			} else {
				param[0] = "Buono per la domanda " + numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR23.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"buono inesistente");
			}
			return Response.status(200).entity(cronologia).build();

		} catch (DatabaseException e) {
			param[0] = e.getMessage();
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
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

}

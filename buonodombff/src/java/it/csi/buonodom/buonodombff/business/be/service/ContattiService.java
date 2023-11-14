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

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.dto.custom.Preferences;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.Util;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.rest.ResponseRest;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class ContattiService extends BaseService {

	@Autowired
	ServizioRestService restbase;

	@Autowired
	ValidateGenericImpl validateGeneric;

	public Response execute(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		String[] param = new String[1];
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErrorBuilder error = null;
		try {
			// validate
			List<ErroreDettaglioExt> listError = validateGeneric.validateListaDecodifica(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			ResponseRest responserest = restbase.getContatti(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
					xCodiceServizio, securityContext, httpHeaders, httpRequest);
			if (responserest.getStatusCode() == 0) {
				// errore
				logError(metodo, "Errore generico get contatti ", null);
				param[0] = "Errore get contatti";
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
			} else {
				return Response.ok().entity(responserest.getJson()).build();
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			e.printStackTrace();
			logError(metodo, "Errore generico ", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	public Response executePreferenze(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		String[] param = new String[1];
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErrorBuilder error = null;
		Preferences preferenze = new Preferences();
		List<String> channels = new ArrayList<String>();
		try {
			// validate
			List<ErroreDettaglioExt> listError = validateGeneric.validateListaDecodifica(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);
			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			ResponseRest responserest = restbase.getPreferenze(shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
					xCodiceServizio, securityContext, httpHeaders, httpRequest);
			if (responserest.getStatusCode() == 0) {
				// errore
				logError(metodo, "Errore generico get preferenze ", null);
				param[0] = "Errore get preferenze";
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
			} else {
				preferenze = new ObjectMapper().readValue(responserest.getJson(), Preferences.class);
				if (Util.isValorizzato(preferenze.getChannels())) {
					for (String canale : preferenze.getChannels().split(",")) {
						channels.add(canale);
					}
				}
				return Response.ok().entity(channels).build();
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			e.printStackTrace();
			logError(metodo, "Errore generico ", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}
}

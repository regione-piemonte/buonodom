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
import it.csi.buonodom.buonodombff.dto.ModelFornitore;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.integration.dao.custom.AllegatoBuonoDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.FornitoriDao;
import it.csi.buonodom.buonodombff.integration.service.LogAuditService;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class FornitoriService extends BaseService {

	@Autowired
	LogAuditService logaudit;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	FornitoriDao fornitoriDao;

	@Autowired
	AllegatoBuonoDao allegatoBuonoDao;

	public Response getFornitori(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		List<ModelFornitore> fornitori = new ArrayList<ModelFornitore>();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		try {
			String richiedenteCf = allegatoBuonoDao.selectCfRichiedenteNumeroDomanda(numeroRichiesta);
			if (richiedenteCf != null) {
				if (!richiedenteCf.equals(shibIdentitaCodiceFiscale)) {
					param[0] = numeroRichiesta;
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR12.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
							"Il codice fiscale del richiedente della domanda non corrisponde");
				}
			}
			// Validazione della richiesta
			List<ErroreDettaglioExt> listError = validateGeneric.validateFornitori(xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						/*
						 * Error 400, la richiesta fatta dal client è errata.
						 */
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			// Ottengo i dati
			fornitori = fornitoriDao.selectFornitori(numeroRichiesta);
			if (fornitori == null) {
				throw new ResponseErrorException(
						/*
						 * Error 404, la risorsa cercata non è stata trovata.
						 */
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listError),
						"Errore riguardante database");
			} else {
				return Response.status(200).entity(fornitori).build();
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

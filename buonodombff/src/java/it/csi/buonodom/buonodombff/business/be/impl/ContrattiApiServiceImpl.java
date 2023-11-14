/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonodom.buonodombff.business.be.ContrattiApi;
import it.csi.buonodom.buonodombff.business.be.service.ContrattiService;
import it.csi.buonodom.buonodombff.dto.ModelContratto;
import it.csi.buonodom.buonodombff.dto.ModelContrattoAllegati;

@Component
public class ContrattiApiServiceImpl implements ContrattiApi {

	@Autowired
	ContrattiService contrattiService;

	@Override
	public Response addContratto(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContrattoAllegati contrattoAllegati,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return contrattiService.addContratto(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, contrattoAllegati, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response getContratti(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return contrattiService.getContratti(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response putContratto(String numeroRichiesta, Integer idContratto, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContratto contratto,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return contrattiService.putContratto(numeroRichiesta, idContratto, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, contratto, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response delContratto(String numeroRichiesta, Integer idContratto, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return contrattiService.delContratto(numeroRichiesta, idContratto, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

}

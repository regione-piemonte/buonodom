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

import it.csi.buonodom.buonodombff.business.be.RichiesteApi;
import it.csi.buonodom.buonodombff.business.be.service.GetRichiesteService;
import it.csi.buonodom.buonodombff.business.be.service.PostRichiesteService;
import it.csi.buonodom.buonodombff.business.be.service.RichiestePostService;
import it.csi.buonodom.buonodombff.dto.ModelBozzaRichiesta;
import it.csi.buonodom.buonodombff.dto.ModelRichiesta;

@Component
public class RichiesteApiServiceImpl implements RichiesteApi {
	@Autowired
	RichiestePostService richiestePostService;

	@Autowired
	GetRichiesteService richiesteService;

	@Autowired
	PostRichiesteService postRichiesteService;

	@Override
	public Response richiesteGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return richiesteService.getRichieste(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
				securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response richiesteNumeroRichiestaGet(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return richiesteService.getNumeroRichiesta(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response richiesteNumeroRichiestaPut(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelRichiesta richiesta,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return postRichiesteService.richiestePut(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, richiesta, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response richiestePost(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, ModelBozzaRichiesta richiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return postRichiesteService.richiestePost(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
				richiesta, securityContext, httpHeaders, httpRequest);
	}

}

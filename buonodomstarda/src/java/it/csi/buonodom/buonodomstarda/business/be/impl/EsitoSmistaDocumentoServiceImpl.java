/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomstarda.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonodom.buonodomstarda.business.be.CallbackApi;
import it.csi.buonodom.buonodomstarda.business.be.service.EsitoSmistaDocumentoService;
import it.csi.buonodom.buonodomstarda.dto.EsitoSmistaDocumentoRequest;

@Component
public class EsitoSmistaDocumentoServiceImpl implements CallbackApi {

	@Autowired
	EsitoSmistaDocumentoService esitosmista;

	@Override
	public Response postCallbackSmistaDocumento(EsitoSmistaDocumentoRequest body, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return esitosmista.executePost(body, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response putCallbackSmistaDocumento(EsitoSmistaDocumentoRequest body, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return esitosmista.executePut(body, securityContext, httpHeaders, httpRequest);
	}

}

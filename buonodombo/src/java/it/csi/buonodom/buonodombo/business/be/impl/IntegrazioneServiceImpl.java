/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonodom.buonodombo.business.be.IntegrazioneApi;
import it.csi.buonodom.buonodombo.business.be.service.IntegrazioneService;

@Component
public class IntegrazioneServiceImpl implements IntegrazioneApi {

	@Autowired
	IntegrazioneService integrazione;

	@Override
	public Response atecoGet(String piva, String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return integrazione.atecoGet(piva, numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

}

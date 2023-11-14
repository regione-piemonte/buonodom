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

import it.csi.buonodom.buonodombff.business.be.PreferenzeApi;
import it.csi.buonodom.buonodombff.business.be.service.ContattiService;

@Component
public class PreferenzeApiServiceImpl implements PreferenzeApi {
	@Autowired
	ContattiService contattiService;

	public Response preferenzeGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return contattiService.executePreferenze(xRequestId, xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale,
				securityContext, httpHeaders, httpRequest);
	}
}

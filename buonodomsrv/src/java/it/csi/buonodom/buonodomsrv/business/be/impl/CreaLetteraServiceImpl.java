/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonodom.buonodomsrv.business.be.CreaLetteraApi;
import it.csi.buonodom.buonodomsrv.business.be.service.CreaLetteraService;

@Component
public class CreaLetteraServiceImpl implements CreaLetteraApi {

	@Autowired
	CreaLetteraService creaLettera;

	@Override
	public Response GetCreaLettera(String numeroRichiesta, String tipoLettera, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return creaLettera.creaLettera(numeroRichiesta, tipoLettera, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonodom.buonodombandisrv.business.be.AcquisizioneDomandeBandiApi;
import it.csi.buonodom.buonodombandisrv.business.be.service.ServiziVersoBandiService;

@Component
public class AcquisizioneDomandeBandiServiceImpl implements AcquisizioneDomandeBandiApi {

	@Autowired
	ServiziVersoBandiService acquisisciDomanda;

	@Override
	public Response acquisizioneDomandaBandi(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return acquisisciDomanda.acquisizioneDomandaBandi(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

}

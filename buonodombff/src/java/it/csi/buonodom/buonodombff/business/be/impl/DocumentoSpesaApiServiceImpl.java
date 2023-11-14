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

import it.csi.buonodom.buonodombff.business.be.DocumentoSpesaApi;
import it.csi.buonodom.buonodombff.business.be.service.DocumentoSpesaService;
import it.csi.buonodom.buonodombff.dto.ModelDocumentoSpesa;

@Component
public class DocumentoSpesaApiServiceImpl implements DocumentoSpesaApi {

	@Autowired
	DocumentoSpesaService documentoSpesaService;

	public Response addRendicontazione(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelDocumentoSpesa documentoSpesa,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return documentoSpesaService.addRendicontazione(numeroRichiesta, documentoSpesa, xRequestId, xForwardedFor,
				xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

	public Response getRendicontazione(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return documentoSpesaService.getRendicontazione(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}

}

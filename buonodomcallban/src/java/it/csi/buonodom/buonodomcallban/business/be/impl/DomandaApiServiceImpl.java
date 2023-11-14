/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonodom.buonodomcallban.business.be.DomandaApi;
import it.csi.buonodom.buonodomcallban.business.be.service.base.CallBackBandiService;
import it.csi.buonodom.buonodomcallban.dto.PayloadEsitoAcquisizione;
import it.csi.buonodom.buonodomcallban.dto.PayloadRevDomanda;

@Component
public class DomandaApiServiceImpl implements DomandaApi {

	@Autowired
	CallBackBandiService callBackBandi;

	public Response domandaNumeroDomandaEsitoAcquisizionePost(String xRequestId, String numeroDomanda,
			PayloadEsitoAcquisizione payloadEsitoAcquisizione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return callBackBandi.acquisizionePost(xRequestId, numeroDomanda, payloadEsitoAcquisizione, securityContext,
				httpHeaders, httpRequest);
	}

	public Response domandaNumeroDomandaEsitoAcquisizionePut(String xRequestId, String numeroDomanda,
			PayloadEsitoAcquisizione payloadEsitoAcquisizione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return callBackBandi.acquisizionePut(xRequestId, numeroDomanda, payloadEsitoAcquisizione, securityContext,
				httpHeaders, httpRequest);
	}

	public Response domandaNumeroDomandaRevocaPut(String xRequestId, String numeroDomanda,
			PayloadRevDomanda payloadRevDomanda, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return callBackBandi.revocaPut(xRequestId, numeroDomanda, payloadRevDomanda, securityContext, httpHeaders,
				httpRequest);
	}
}

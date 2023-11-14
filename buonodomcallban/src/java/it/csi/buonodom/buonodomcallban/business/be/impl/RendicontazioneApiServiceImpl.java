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

import it.csi.buonodom.buonodomcallban.business.be.RendicontazioneApi;
import it.csi.buonodom.buonodomcallban.business.be.service.base.CallBackBandiService;
import it.csi.buonodom.buonodomcallban.dto.PayloadEsitoAcquisizioneSpesa;

@Component
public class RendicontazioneApiServiceImpl implements RendicontazioneApi {

	@Autowired
	CallBackBandiService callBackBandi;

	public Response rendicontazioneIdDichiarazionespesaBuonodomEsitoAcquisizionePost(String xRequestId,
			String idDichiarazionespesaBuonodom, PayloadEsitoAcquisizioneSpesa payloadEsitoAcquisizioneSpesa,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return callBackBandi.acquisizioneDichiarazionePost(xRequestId, idDichiarazionespesaBuonodom,
				payloadEsitoAcquisizioneSpesa, securityContext, httpHeaders, httpRequest);
	}

	public Response rendicontazioneIdDichiarazionespesaBuonodomEsitoAcquisizionePut(String xRequestId,
			String idDichiarazionespesaBuonodom, PayloadEsitoAcquisizioneSpesa payloadEsitoAcquisizioneSpesa,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return callBackBandi.acquisizioneDichiarazionePut(xRequestId, idDichiarazionespesaBuonodom,
				payloadEsitoAcquisizioneSpesa, securityContext, httpHeaders, httpRequest);
	}
}

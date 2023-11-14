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

import it.csi.buonodom.buonodombff.business.be.PeriodoRendicontazioneApi;
import it.csi.buonodom.buonodombff.business.be.service.PeriodoRendicontazioneService;
import it.csi.buonodom.buonodombff.dto.ModelSabbatici;

@Component
public class PeriodoRendicontazioneApiServiceImpl implements PeriodoRendicontazioneApi {

	@Autowired
	PeriodoRendicontazioneService periodoRendicontazioneService;

	public Response mesiSabbatici(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelSabbatici sabbatici,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return periodoRendicontazioneService.mesiSabbatici(numeroRichiesta, xRequestId, xForwardedFor, xCodiceServizio,
				shibIdentitaCodiceFiscale, sabbatici, securityContext, httpHeaders, httpRequest);
	}

	public Response periodoRendicontazione(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return periodoRendicontazioneService.periodoRendicontazione(numeroRichiesta, xRequestId, xForwardedFor,
				xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest);
	}
}

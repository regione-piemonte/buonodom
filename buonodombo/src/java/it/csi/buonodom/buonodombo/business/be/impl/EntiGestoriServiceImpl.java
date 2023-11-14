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

import it.csi.buonodom.buonodombo.business.be.EntiGestoriApi;
import it.csi.buonodom.buonodombo.business.be.service.EntiGestoriService;
import it.csi.buonodom.buonodombo.dto.CambioStatoPopUp;
import it.csi.buonodom.buonodombo.dto.ModelVerifiche;

@Component
public class EntiGestoriServiceImpl implements EntiGestoriApi {

	@Autowired
	EntiGestoriService entigestoriService;

	@Override
	public Response richiediRettifica(CambioStatoPopUp nota, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

		return entigestoriService.richiediRettifica(nota, securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response getEntiGestori(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return entigestoriService.getEntiGestori(securityContext, httpHeaders, httpRequest);
	}

	@Override
	public Response salvaVerificaEnte(String numeroRichiesta, ModelVerifiche verifica, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return entigestoriService.salvaVerificaEnte(numeroRichiesta, verifica, securityContext, httpHeaders,
				httpRequest);
	}

	@Override
	public Response concludiVerificaEnte(String numeroRichiesta, ModelVerifiche verifica,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return entigestoriService.concludiVerificaEnte(numeroRichiesta, verifica, securityContext, httpHeaders,
				httpRequest);
	}

	@Override
	public Response updateToVerificaInCorso(String numeroDomande[], SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		return entigestoriService.updateToVerificaInCorso(numeroDomande, securityContext, httpHeaders, httpRequest);
	}
}

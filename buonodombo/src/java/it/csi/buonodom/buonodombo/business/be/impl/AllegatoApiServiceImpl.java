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

import it.csi.buonodom.buonodombo.business.be.AllegatoApi;
import it.csi.buonodom.buonodombo.business.be.service.AllegatiService;

@Component
public class AllegatoApiServiceImpl implements AllegatoApi {

	@Autowired
	AllegatiService allegatiService;

	@Override
	public Response allegatoNumeroRichiestaTipoAllegatoGet(String tipoAllegato, String numeroRichiesta,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return allegatiService.getAllegato(tipoAllegato, numeroRichiesta, securityContext, httpHeaders, httpRequest);
	}

}

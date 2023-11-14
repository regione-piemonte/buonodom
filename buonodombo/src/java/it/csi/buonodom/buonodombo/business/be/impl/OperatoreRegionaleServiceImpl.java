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

import it.csi.buonodom.buonodombo.business.be.OperatoreRegionaleAPI;
import it.csi.buonodom.buonodombo.business.be.service.OperatoreRegionaleService;
import it.csi.buonodom.buonodombo.dto.ModelSportello;

@Component
public class OperatoreRegionaleServiceImpl implements OperatoreRegionaleAPI {
	@Autowired
	OperatoreRegionaleService operatoreRegionaleService;

	@Override
	public Response creaSportello(ModelSportello nSportello, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		return operatoreRegionaleService.creaSportello(nSportello, securityContext, httpHeaders, httpRequest);
	}

}

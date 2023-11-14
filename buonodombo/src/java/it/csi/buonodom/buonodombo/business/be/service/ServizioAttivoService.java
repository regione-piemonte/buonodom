/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.business.be.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombo.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombo.dto.ModelInfoServizio;
import it.csi.buonodom.buonodombo.integration.GetDettaglioImpresaService;
import it.csi.buonodom.buonodombo.util.Constants;

@Service
public class ServizioAttivoService extends BaseService {
	@Autowired
	GetDettaglioImpresaService impresaService;

	public Response execute(String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ModelInfoServizio info = new ModelInfoServizio();
		info.setData(new Date());
		info.setNome(Constants.COMPONENT_NAME);
		info.setDescrizione("Api per la gestione del backoffice del buono");
		info.setServizioAttivo(true);
		return Response.ok().entity(info).build();
	}
}

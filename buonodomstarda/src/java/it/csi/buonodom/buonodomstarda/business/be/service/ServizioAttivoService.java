/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomstarda.business.be.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodomstarda.business.be.service.base.BaseService;
import it.csi.buonodom.buonodomstarda.dto.ModelInfoServizio;
import it.csi.buonodom.buonodomstarda.util.Constants;

@Service
public class ServizioAttivoService extends BaseService {

	public Response execute(String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ModelInfoServizio info = new ModelInfoServizio();
		info.setData(new Date());
		info.setNome(Constants.COMPONENT_NAME);
		info.setDescrizione("Api per la gestione del buono per la domiciliarita");
		info.setServizioAttivo(true);
		return Response.ok().entity(info).build();
	}
}

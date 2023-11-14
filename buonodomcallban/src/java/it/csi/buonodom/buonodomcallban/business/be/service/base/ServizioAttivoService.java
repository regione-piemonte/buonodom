/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.business.be.service.base;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodomcallban.dto.InfoServizio;
import it.csi.buonodom.buonodomcallban.util.Constants;

@Service
public class ServizioAttivoService extends BaseService {

	public Response execute(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		InfoServizio info = new InfoServizio();
		info.setData(new Date());
		info.setNome(Constants.COMPONENT_NAME);
		info.setDescrizione("Api per la gestione delle chiamate provenienti dal servizio bandi");
		info.setServizioAttivo(true);
		return Response.ok().entity(info).build();
	}
}

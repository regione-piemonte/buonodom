/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.buonodom.buonodombo.dto.CambioStatoPopUp;
import it.csi.buonodom.buonodombo.dto.ModelVerifiche;

@Path("/entigestori")

public interface EntiGestoriApi {

	@GET
	@Produces({ "application/json" })

	public Response getEntiGestori(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/richiedirettifica")
	@Produces({ "application/json" })

	public Response richiediRettifica(CambioStatoPopUp nota, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/salvaverificaente/{numero_richiesta}")
	@Produces({ "application/json" })

	public Response salvaVerificaEnte(@PathParam("numero_richiesta") String numeroRichiesta, ModelVerifiche verifica,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/concludiverificaente/{numero_richiesta}")
	@Produces({ "application/json" })

	public Response concludiVerificaEnte(@PathParam("numero_richiesta") String numeroRichiesta, ModelVerifiche verifica,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/updateToVerificaInCorso")
	@Produces({ "application/json" })

	public Response updateToVerificaInCorso(String numeroDomande[], @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}

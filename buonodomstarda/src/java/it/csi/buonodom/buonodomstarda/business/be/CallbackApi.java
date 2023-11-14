/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.buonodom.buonodomstarda.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.buonodom.buonodomstarda.dto.EsitoSmistaDocumentoRequest;

@Path("/callback")
@Consumes({ "application/json" })
@Produces({ "application/json" })

public interface CallbackApi {

	@POST
	@Path("/esitoSmistaDocumento")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })

	public Response postCallbackSmistaDocumento(EsitoSmistaDocumentoRequest body,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@PUT
	@Path("/esitoSmistaDocumento")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })

	public Response putCallbackSmistaDocumento(EsitoSmistaDocumentoRequest body,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}

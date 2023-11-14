/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.buonodom.buonodomcallban.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.buonodom.buonodomcallban.dto.PayloadEsitoAcquisizione;

@Path("/domanda")

public interface DomandaApi {

	@POST
	@Produces({ "application/json" })
	@Path("/{numero_domanda}/esito-acquisizione")

	public Response domandaNumeroDomandaEsitoAcquisizionePost(@HeaderParam("X-Request-Id") String xRequestId,
			@PathParam("numero_domanda") String numeroDomanda, PayloadEsitoAcquisizione payloadEsitoAcquisizione,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@PUT
	@Produces({ "application/json" })
	@Path("/{numero_domanda}/esito-acquisizione")
	public Response domandaNumeroDomandaEsitoAcquisizionePut(@HeaderParam("X-Request-Id") String xRequestId,
			@PathParam("numero_domanda") String numeroDomanda, PayloadEsitoAcquisizione payloadEsitoAcquisizione,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}

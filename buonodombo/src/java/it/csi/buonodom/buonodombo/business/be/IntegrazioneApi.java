/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.buonodom.buonodombo.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/integrazione")

public interface IntegrazioneApi {

	@GET
	@Path("/{piva}/{numero_richiesta}")

	@Produces({ "application/json" })

	public Response atecoGet(@PathParam("piva") String piva, @PathParam("numero_richiesta") String numeroRichiesta,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

}

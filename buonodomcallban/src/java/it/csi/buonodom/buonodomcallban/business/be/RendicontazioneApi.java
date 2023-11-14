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

import it.csi.buonodom.buonodomcallban.dto.PayloadEsitoAcquisizioneSpesa;

@Path("/rendicontazione")

public interface RendicontazioneApi {

	@POST
	@Produces({ "application/json" })
	@Path("/{id_dichiarazionespesa_buonodom}/esito-acquisizione")
	public Response rendicontazioneIdDichiarazionespesaBuonodomEsitoAcquisizionePost(
			@HeaderParam("X-Request-Id") String xRequestId,
			@PathParam("id_dichiarazionespesa_buonodom") String idDichiarazionespesaBuonodom,
			PayloadEsitoAcquisizioneSpesa payloadEsitoAcquisizioneSpesa, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@PUT
	@Produces({ "application/json" })
	@Path("/{id_dichiarazionespesa_buonodom}/esito-acquisizione")
	public Response rendicontazioneIdDichiarazionespesaBuonodomEsitoAcquisizionePut(
			@HeaderParam("X-Request-Id") String xRequestId,
			@PathParam("id_dichiarazionespesa_buonodom") String idDichiarazionespesaBuonodom,
			PayloadEsitoAcquisizioneSpesa payloadEsitoAcquisizioneSpesa, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
}

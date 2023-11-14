/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.buonodom.buonodombff.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.buonodom.buonodombff.dto.ModelSabbatici;

@Path("/periodo-rendicontazione")

public interface PeriodoRendicontazioneApi {

	@PUT
	@Path("/{numero_richiesta}")

	@Produces({ "application/json" })

	public Response mesiSabbatici(@PathParam("numero_richiesta") String numeroRichiesta,
			@HeaderParam("X-Request-Id") String xRequestId, @HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale, ModelSabbatici sabbatici,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/{numero_richiesta}")

	@Produces({ "application/json" })

	public Response periodoRendicontazione(@PathParam("numero_richiesta") String numeroRichiesta,
			@HeaderParam("X-Request-Id") String xRequestId, @HeaderParam("X-Forwarded-For") String xForwardedFor,
			@HeaderParam("X-Codice-Servizio") String xCodiceServizio,
			@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}

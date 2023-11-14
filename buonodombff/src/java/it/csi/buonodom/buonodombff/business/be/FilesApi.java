/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.buonodom.buonodombff.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/files")

public interface FilesApi {

	@POST

	@Consumes({ "multipart/form-data" })
	@Produces({ "application/json" })

	public Response uploadFile(MultipartFormDataInput input, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
}

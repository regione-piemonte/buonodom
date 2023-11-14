/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.dto.Errore;
import it.csi.buonodom.buonodombff.dto.ModelDatiFile;

@Service
public class FilesService extends BaseService {

	public Response execute(MultipartFormDataInput input, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		if (input.getParts().size() > 0) {

			String s = "";
			ModelDatiFile model = new ModelDatiFile();

			Map<String, List<InputPart>> formMultiData = input.getFormDataMap();
			List<InputPart> inputParts = formMultiData.get("uploadedFile");
			for (InputPart inputPart : inputParts) {
				String bodyAsString = "";
				try {
					/*
					 * MediaType media = inputPart.getMediaType(); bodyAsString =
					 * inputPart.getBodyAsString();
					 */
					MediaType media = inputPart.getMediaType();
					InputStream inputStream = inputPart.getBody(InputStream.class, null);

					// byte [] bytes = IOUtils .toByteArray(inputStream);

					bodyAsString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
					System.out.println(
							"Body as string: " + bodyAsString + "Typs: " + media.getType() + media.getSubtype());
				} catch (IOException e) {
					bodyAsString = "[[error wile parsing body: " + e + "]]";
				}
			}

			List<InputPart> inputPartsText = formMultiData.get("dati");

			for (InputPart inputPart : inputPartsText) {
				String bodyAsString = "";
				try {
					MediaType media = inputPart.getMediaType();
					bodyAsString = inputPart.getBodyAsString();
					System.out.println(
							"Body as string: " + bodyAsString + "Typs: " + media.getType() + media.getSubtype());
					ObjectMapper mapper = new ObjectMapper();
					model = mapper.readValue(bodyAsString, new TypeReference<ModelDatiFile>() {
					});

				} catch (IOException e) {
					bodyAsString = "[[error wile parsing body: " + e + "]]";
				}
			}

			/*
			 * for (int i=0; i<input.getParts().size(); i++) { if(i == 0) { String
			 * bodyAsString = ""; try { bodyAsString =
			 * "[["+input.getParts().get(i).getBodyAsString()+"]]"; } catch (IOException e)
			 * { bodyAsString = "[[error wile parsing body: "+e+"]]"; }
			 * System.out.println("i: " + i);
			 * s+="part["+i+"]-media: "+input.getParts().get(i).getMediaType()
			 * +", body as string:"+ bodyAsString;
			 * 
			 * }if(i == 1) { String bodyAsString2 = ""; try { bodyAsString2 =
			 * input.getParts().get(i).getBodyAsString(); ObjectMapper mapper = new
			 * ObjectMapper(); model = mapper.readValue(bodyAsString2 ,new
			 * TypeReference<ModelDatiFile>() { }); } catch (IOException e) { bodyAsString2
			 * = "[[error wile parsing body: "+e+"]]"; } } }
			 */
			return Response.ok(model).build();
		} else {
			Errore err = new Errore();
			err.setCode("400");
			err.setTitle("elenco di file vuoto");
			return Response.serverError().entity(err).status(400).build();

		}

	}

	/*
	 * String fileName = "";
	 * 
	 * Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
	 * List<InputPart> inputParts = uploadForm.get("uploadedFile");
	 * 
	 * for (InputPart inputPart : inputParts) {
	 * 
	 * try {
	 * 
	 * MultivaluedMap<String, String> header = inputPart.getHeaders(); fileName =
	 * getFileName(header);
	 * 
	 * //convert the uploaded file to inputstream InputStream inputStream =
	 * inputPart.getBody(InputStream.class,null);
	 * 
	 * byte [] bytes = IOUtils.toByteArray(inputStream);
	 * 
	 * //constructs upload file path fileName = UPLOADED_FILE_PATH + fileName;
	 * 
	 * writeFile(bytes,fileName);
	 * 
	 * System.out.println("Done");
	 * 
	 * } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * }
	 */
}

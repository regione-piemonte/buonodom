/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.business.be.service.util.FilesEncrypt;
import it.csi.buonodom.buonodombff.dto.ModelDatiFile;
import it.csi.buonodom.buonodombff.dto.ModelRichiesta;
import it.csi.buonodom.buonodombff.exception.ErrorBuilder;

@Service
public class RichiestePostService extends BaseService {

	@Autowired
	private FilesEncrypt filesEncrypt;

	public Response execute(MultipartFormDataInput input, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		final String methodName = "execute";

		ModelRichiesta richiesta = null;

		if (input.getParts().size() > 0) {

			String s = "";
			ModelDatiFile model = new ModelDatiFile();

			Map<String, List<InputPart>> formMultiData = input.getFormDataMap();

			// documento_delega
			List<InputPart> inputParts = formMultiData.get("documento_delega");

			if (inputParts != null) {
				for (InputPart inputPart : inputParts) {

					try {

						MediaType media = inputPart.getMediaType();
						InputStream inputStream = inputPart.getBody(InputStream.class, null);

						// TODO inserire percorso e nome corretto
						filesEncrypt.creaFileCifrato(Cipher.ENCRYPT_MODE, inputStream,
								new File("D:\\cifratoCIdentita.jpg"));

					} catch (IOException e) {
						e.printStackTrace();
						final String title = "File documento_delega non valido";
						logError(methodName, title, e);
						return ErrorBuilder.from(400, title).response();
					}
				}
			}

			// documento_delegante
			List<InputPart> inputPartsDelegante = formMultiData.get("documento_delegante");

			if (inputPartsDelegante != null) {
				for (InputPart inputPart : inputPartsDelegante) {
					String bodyAsString = "";
					try {
						MediaType media = inputPart.getMediaType();
						InputStream inputStream = inputPart.getBody(InputStream.class, null);

						// TODO inserire percorso e nome corretto
						filesEncrypt.creaFileCifrato(Cipher.ENCRYPT_MODE, inputStream,
								new File("D:\\CartaidentitaEgidiocifrato.pdf"));

					} catch (IOException e) {
						e.printStackTrace();
						final String title = "File documento_delega non valido";
						logError(methodName, title, e);
						return ErrorBuilder.from(400, title).response();
					}
				}
			}

			// richiesta

			List<InputPart> inputPartsRichiesta = formMultiData.get("richiesta");

			for (InputPart inputPart : inputPartsRichiesta) {
				String bodyAsString = "";
				try {
					MediaType media = inputPart.getMediaType();
					bodyAsString = inputPart.getBodyAsString();
					ObjectMapper mapper = new ObjectMapper();
					richiesta = mapper.readValue(bodyAsString, new TypeReference<ModelRichiesta>() {
					});

				} catch (IOException e) {
					e.printStackTrace();
					return ErrorBuilder.from(400, "Oggetto richiesta non valido").response();

				}
			}
		}

		else {
			return ErrorBuilder.from(400, "elenco di file vuoto").response();

		}

		return Response.ok(richiesta).build();
	}

}

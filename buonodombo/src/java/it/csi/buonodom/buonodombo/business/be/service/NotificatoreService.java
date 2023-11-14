/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.business.be.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonodom.buonodombo.dto.Contact;
import it.csi.buonodom.buonodombo.dto.Preferences;
import it.csi.buonodom.buonodombo.util.LoggerUtil;
import it.csi.buonodom.buonodombo.util.enumerator.ApiHeaderParamEnum;

@Service
public class NotificatoreService extends LoggerUtil {

	@Autowired
	ApiManagerServiceClient tokenApiManager;

	@Value("${notificatore.contact.url}")
	private String urlNotificatoreContact;
	@Value("${notificatore.contact.token}")
	private String tokenApplicativoContact;
	@Value("${notificatore.richiedente.applicazione}")
	private String nomeApplicazione;

	private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	public Contact sendContact(String cfSoggetto, String iride) throws Exception {
		HttpResponse<String> response = createGetRequestToken(cfSoggetto, iride,
				urlNotificatoreContact + "/" + cfSoggetto + "/contacts");
		Contact contatto = new Contact();
		if (!(response.statusCode() == 200 || response.statusCode() == 201 || response.statusCode() == 401)) {
			logError("sendNotificaEvento: ", response.body());
		} else {
			// lascio solo utenza email e telefono
			if (response.body().indexOf("push") != -1) {
				String contattireturn = response.body().substring(0, response.body().indexOf("push") - 2) + "}";
				contatto = new ObjectMapper().readValue(contattireturn, Contact.class);
			}
		}
		return contatto;
	}

	public Preferences sendPreferenze(String cfSoggetto, String iride) throws Exception {
		HttpResponse<String> response = createGetRequestToken(cfSoggetto, iride,
				urlNotificatoreContact + "/" + cfSoggetto + "/preferences/" + nomeApplicazione);
		Preferences preferenze = new Preferences();
		if (!(response.statusCode() == 200 || response.statusCode() == 201 || response.statusCode() == 401)) {
			logError("sendPreferenzeEvento: ", response.body());
		} else {
			preferenze = new ObjectMapper().readValue(response.body(), Preferences.class);
		}
		return preferenze;
	}

	public HttpResponse createGetRequestToken(String cfSoggetto, String iride, String url) throws Exception {
		logInfo("sendNotificaEvento ",
				"params: \n" + ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode() + ": " + iride + ", \n"
						+ ApiHeaderParamEnum.X_AUTHENTICATION.getCode() + ": " + tokenApplicativoContact + "\n");
		String token = tokenApiManager.getAccessToken();
		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url))
				.setHeader(ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode(), iride)
				.setHeader(ApiHeaderParamEnum.X_AUTHENTICATION.getCode(), tokenApplicativoContact)
				.setHeader(ApiHeaderParamEnum.AUTHORIZATION.getCode(), token)
				.setHeader("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		// print status code
		logInfo("sendNotificaEvento ", "status:" + response.statusCode());
		logInfo("sendNotificaEvento", "response: " + response.toString());
		logInfo("sendNotificaEvento", "responsebody: " + response.body());
		if (response.statusCode() == 401) {
			token = tokenApiManager.refreshToken();
			request = HttpRequest.newBuilder().GET().uri(URI.create(url))
					.setHeader(ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode(), iride)
					.setHeader(ApiHeaderParamEnum.X_AUTHENTICATION.getCode(), tokenApplicativoContact)
					.setHeader(ApiHeaderParamEnum.AUTHORIZATION.getCode(), token)
					.setHeader("Content-Type", "application/json").build();
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			logError("token scaduto nuovo token: ", token);
			logInfo("sendNotificaEvento ", "status:" + response.statusCode());
			logInfo("sendNotificaEvento", "response: " + response.toString());
			logInfo("sendNotificaEvento", "responsebody: " + response.body());
		}

		return response;
	}

}

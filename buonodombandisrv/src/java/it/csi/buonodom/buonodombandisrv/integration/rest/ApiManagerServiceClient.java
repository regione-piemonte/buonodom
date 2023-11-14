/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.rest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonodom.buonodombandisrv.dto.apimanager.TokenApiManager;
import it.csi.buonodom.buonodombandisrv.util.LoggerUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class ApiManagerServiceClient extends LoggerUtil {
	private OkHttpClient okHttpClient;
	@Value("${consumerKey}")
	private String consumerKey;
	@Value("${consumerSecret}")
	private String consumerSecret;
	@Value("${apiManagerUrl}")
	private String apiManagerUrl;

	private volatile String token = null;

	private OkHttpClient getHttpClient() {
		if (okHttpClient == null) {
			okHttpClient = new OkHttpClient.Builder().connectTimeout(45, TimeUnit.SECONDS)
					.writeTimeout(45, TimeUnit.SECONDS).readTimeout(45, TimeUnit.SECONDS)
					.addInterceptor(new BasicAuthInterceptor(consumerKey, consumerSecret)).build();
		}

		return okHttpClient;
	}

	private synchronized void callToken() throws IOException {
		final String methodName = "callToken";
		String strBody = "grant_type=client_credentials";
		RequestBody body = RequestBody.create(strBody, MediaType.parse("application/x-www-form-urlencoded"));
		Request reqToken = new Request.Builder().url(apiManagerUrl).post(body).build();
		Response respToken;
		try {
			getHttpClient();
			respToken = okHttpClient.newCall(reqToken).execute();
		} catch (IOException e) {
			throw e;
		}
		String json = respToken.body().string();
		if (respToken.isSuccessful()) {
			ObjectMapper mapper = new ObjectMapper();
			TokenApiManager tokenApi = mapper.readValue(json, new TypeReference<TokenApiManager>() {
			});
			token = new StringBuffer("Bearer").append(" ").append(tokenApi.getAccessToken()).toString();
		} else {

		}

	}

	public String getAccessToken() throws IOException {
		final String methodName = "getAccessToken";
		if (this.token == null) {
			callToken();
		}
		return this.token;
	}

	public String refreshToken() throws IOException {
		callToken();
		return this.token;
	}

}

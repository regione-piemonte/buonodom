/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.rest;

import java.io.File;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.buonodom.buonodombandisrv.util.Constants;
import it.csi.buonodom.buonodombandisrv.util.LoggerUtil;
import it.csi.buonodom.buonodombandisrv.util.rest.ResponseRest;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class RestBaseService extends LoggerUtil {

	protected abstract OkHttpClient getHttpClient();

	public ResponseRest eseguiGet(HttpHeaders headers, String url, boolean shibboleth, List<String> headerName)
			throws Exception {

		Request request = createGetRequest(headers, url, shibboleth);
		Response resp = execute(request);
		ResponseRest response = new ResponseRest();
		response.setStatusCode(resp.code());
		String jsonRit = resp.body().string(); // con questa chiamata ho chiuso la response
		response.setJson(jsonRit);
		if (headerName != null) {
			headerName.stream().forEach(c -> response.addHeaders(c, resp.header(c)));
		}

		return response;
	}

	public ResponseRest eseguiPost(HttpHeaders headers, String url, String jsonBody, boolean shibboleth)
			throws Exception {

		Request request = createPostRequest(headers, url, jsonBody, shibboleth);
		Response resp = execute(request);
		ResponseRest response = new ResponseRest();
		response.setStatusCode(resp.code());
		String jsonRit = resp.body().string(); // con questa chiamata ho chiuso la response
		response.setJson(jsonRit);
		return response;
	}

	public ResponseRest eseguiPostMultipart(HttpHeaders headers, String url, String keynome, String valuenome, File xml,
			boolean shibboleth) throws Exception {

		Request request = createPostRequestMultipart(headers, url, keynome, valuenome, xml, shibboleth);
		Response resp = execute(request);
		ResponseRest response = new ResponseRest();
		response.setStatusCode(resp.code());
		String jsonRit = resp.body().string(); // con questa chiamata ho chiuso la response
		response.setJson(jsonRit);
		return response;
	}

	public ResponseRest eseguiPostMultipartZip(HttpHeaders headers, String url, String keynomefile, String keynometext,
			String json, String valuenome, File zip, boolean shibboleth) throws Exception {

		Request request = createPostRequestMultipartZip(headers, url, keynomefile, keynometext, json, valuenome, zip,
				shibboleth);

		Response resp = execute(request);
		ResponseRest response = new ResponseRest();
		response.setStatusCode(resp.code());
		String jsonRit = resp.body().string(); // con questa chiamata ho chiuso la response
		response.setJson(jsonRit);
		return response;
	}

	public ResponseRest eseguiPut(HttpHeaders headers, String url, String jsonBody, boolean shibboleth)
			throws Exception {

		Request request = createPutRequest(headers, url, jsonBody, shibboleth);
		Response resp = execute(request);
		ResponseRest response = new ResponseRest();
		response.setStatusCode(resp.code());
		String jsonRit = resp.body().string(); // con questa chiamata ho chiuso la response
		response.setJson(jsonRit);
		return response;
	}

	private Response execute(Request request) throws Exception {
		final String METHOD_NAME = "execute";
		try {
			OkHttpClient okHttpClient = getHttpClient();
			return okHttpClient.newCall(request).execute();
		} catch (Exception e) {
			logError(METHOD_NAME, "OkHttp post error", e);
			throw new Exception("OkHttp post error", e);
		}
	}

	private String getHeaderParam(HttpHeaders httpHeaders, String headerParam) {
		List<String> values = httpHeaders.getRequestHeader(headerParam);
		if (values == null || values.isEmpty()) {
			return null;
		}
		return values.get(0);
	}

	private Request createGetRequest(HttpHeaders headers, String url, boolean shibboleth) {
		Request request = addCustomHeaders(headers, new Request.Builder(), shibboleth).url(url).build();
		logInfo("createGetRequest", "url: %s" + url);
		return request;
	}

	private String getStringFromBody(Object body) {
		String jsonFromBody = null;
		try {
			if (body != null) {
				ObjectMapper mapper = new ObjectMapper();
				jsonFromBody = mapper.writeValueAsString(body);

			}
		} catch (Exception e) {
			String msg = String.format("Error in body class %s ", body != null ? body.getClass() : null);
			logError("getStringFromBody", msg, e);
			throw new RestClientException(msg, e);
		}
		return jsonFromBody;
	}

	private Request.Builder addCustomHeaders(HttpHeaders httpHeaders, Request.Builder reqBuilder, boolean shibboleth) {

		reqBuilder.addHeader(Constants.X_CODICE_SERVIZIO, getHeaderParam(httpHeaders, Constants.X_CODICE_SERVIZIO))
				.addHeader(Constants.X_REQUEST_ID, getHeaderParam(httpHeaders, Constants.X_REQUEST_ID));

		if (shibboleth) {
			reqBuilder.addHeader(Constants.SHIB_IDENTITA_CODICE_FISCALE,
					getHeaderParam(httpHeaders, Constants.SHIB_IDENTITA_CODICE_FISCALE));
		}
		String xForwardedFor = getHeaderParam(httpHeaders, Constants.X_FORWARDED_FOR);
		if (!StringUtils.isEmpty(xForwardedFor)) {
			reqBuilder.addHeader(Constants.X_FORWARDED_FOR, xForwardedFor);
		}

		return reqBuilder;
	}

	private Request createPostRequest(HttpHeaders headers, String url, String json, boolean shibboleth) {
		RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
		Request request = addCustomHeaders(headers, new Request.Builder(), shibboleth).url(url).post(body).build();

		return request;
	}

	private Request createPostRequestMultipart(HttpHeaders headers, String url, String keynome, String valuenome,
			File xml, boolean shibboleth) {
		RequestBody body = new MultipartBody.Builder()
				.addFormDataPart(keynome, valuenome, RequestBody.create(xml, MediaType.parse("application/xml")))
				.setType(MultipartBody.FORM).build();

		Request request = new Request.Builder().addHeader("Content-Type", body.contentType().toString()).url(url)
				.post(body).build();

		return request;
	}

	private Request createPostRequestMultipartZip(HttpHeaders headers, String url, String keynomefile,
			String keynometext, String json, String valuenome, File zip, boolean shibboleth) {
		RequestBody body = new MultipartBody.Builder()
				.addFormDataPart(keynomefile, valuenome, RequestBody.create(zip, MediaType.parse("application/zip")))
				.setType(MultipartBody.FORM)
				.addFormDataPart(keynometext, valuenome, RequestBody.create(json, MediaType.parse("application/json")))
				.setType(MultipartBody.FORM).build();

		Request request = new Request.Builder().addHeader("Content-Type", body.contentType().toString()).url(url)
				.post(body).build();

		return request;
	}

	private Request createPutRequest(HttpHeaders headers, String url, String json, boolean shibboleth) {
		RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
		Request request = addCustomHeaders(headers, new Request.Builder(), shibboleth).url(url).put(body).build();

		return request;
	}

	private Request createDeleteRequest(HttpHeaders headers, String url, boolean shibboleth) {
		Request request = addCustomHeaders(headers, new Request.Builder(), shibboleth).url(url).delete().build();
		return request;
	}

}

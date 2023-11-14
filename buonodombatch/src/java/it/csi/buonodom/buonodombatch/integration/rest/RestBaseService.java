/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombatch.integration.rest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import it.csi.buonodom.buonodombatch.configuration.Configuration;
import it.csi.buonodom.buonodombatch.dao.BuonodomBatchDAO;
import it.csi.buonodom.buonodombatch.logger.BatchLoggerFactory;
import it.csi.buonodom.buonodombatch.util.Constants;
import it.csi.buonodom.buonodombatch.util.rest.ResponseRest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class RestBaseService extends BatchLoggerFactory {

	private String buonodomsrvusername = Configuration.get("buonodomsrvusername");
	private String buonodomsrvpassword = Configuration.get("buonodomsrvpassword");
	private String buonodomsrvurl = Configuration.get("buonodomsrvurl");

	private OkHttpClient okHttpClient;

	private static RestBaseService instance;

	private RestBaseService() {
	}

	public static RestBaseService getInstance(BuonodomBatchDAO dao) {
		if (instance == null) {
			instance = new RestBaseService();
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public static RestBaseService getInstance() {
		return instance;
	}

	private OkHttpClient getHttpClient() {
		if (okHttpClient == null) {
			okHttpClient = new OkHttpClient.Builder().connectTimeout(45, TimeUnit.SECONDS)
					.writeTimeout(45, TimeUnit.SECONDS).readTimeout(45, TimeUnit.SECONDS)
					.addInterceptor(new BasicAuthInterceptor(this.buonodomsrvusername, this.buonodomsrvpassword))
					.build();
		}

		return okHttpClient;
	}

	private OkHttpClient getHttpClientLocal() {
		if (okHttpClient == null) {
			okHttpClient = new OkHttpClient.Builder().connectTimeout(45, TimeUnit.SECONDS)
					.writeTimeout(45, TimeUnit.SECONDS).readTimeout(45, TimeUnit.SECONDS)
					.addInterceptor(new BasicAuthInterceptor(this.buonodomsrvusername, this.buonodomsrvpassword))
					.build();
		}

		return okHttpClient;
	}

	public ResponseRest eseguiGet(String cf, String url, boolean shibboleth, List<String> headerName, boolean iride)
			throws Exception {
		Request request = createGetRequest(cf, url, shibboleth, iride);
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

//	public ResponseRest eseguiPost(HttpHeaders headers, String url, String jsonBody, boolean shibboleth,boolean iride) throws Exception{
//		
//		Request request = createPostRequest(headers, url, jsonBody, shibboleth,iride);
//		Response resp = execute(request);
//		ResponseRest response = new ResponseRest(); 
//		response.setStatusCode(resp.code());
//		String jsonRit = resp.body().string(); //con questa chiamata ho chiuso la response
//		response.setJson(jsonRit);
//		return response;
//	}

//	public ResponseRest eseguiPut(HttpHeaders headers, String url, String jsonBody, boolean shibboleth,boolean iride) throws Exception{
//		
//		Request request = createPutRequest(headers, url, jsonBody, shibboleth,iride);
//		Response resp = execute(request);
//		ResponseRest response = new ResponseRest(); 
//		response.setStatusCode(resp.code());
//		String jsonRit = resp.body().string(); //con questa chiamata ho chiuso la response
//		response.setJson(jsonRit);
//		return response;
//	}		

	private Response execute(Request request) throws Exception {
		final String METHOD_NAME = "execute";
		try {
			String os = System.getProperty("os.name");
			if (os.toLowerCase().contains("win")) {
				getHttpClientLocal();
			} else
				getHttpClient();
			return okHttpClient.newCall(request).execute();
		} catch (Exception e) {
			getLogger(this).error(METHOD_NAME + " OkHttp post error", e);
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

	private Request createGetRequest(String cf, String url, boolean shibboleth, boolean iride) {
		Request request = addCustomHeaders(cf, Constants.XREQUESTID, Constants.XFORWARDEDFOR, Constants.XCODICESERVIZIO,
				new Request.Builder(), shibboleth, iride).url(url).build();
		getLogger(this).info("createGetRequest url: %s" + url);
		return request;
	}
//	private String getStringFromBody(Object body) {
//		String jsonFromBody = null;
//		try {
//			if(body != null) {
//				ObjectMapper mapper = new ObjectMapper();
//				jsonFromBody = mapper.writeValueAsString(body);
//				
//			}
//		}catch(Exception e) {
//			String msg =  String.format("Error in body class %s ", body!= null?body.getClass():null);
//			getLogger(this).error("getStringFromBody" + msg, e);
//			throw new RestClientException(msg, e);
//		}
//		return jsonFromBody;
//	}

	private Request.Builder addCustomHeaders(String cf, String xRequestId, String xForwardedFor, String xCodiceServizio,
			Request.Builder reqBuilder, boolean shibboleth, boolean iride) {

		reqBuilder.addHeader(Constants.X_CODICE_SERVIZIO, xCodiceServizio).addHeader(Constants.X_REQUEST_ID,
				xRequestId);
		if (iride) {
			reqBuilder.addHeader(Constants.SHIB_IRIDE_IDENTITADIGITALE, cf);
		}
		if (shibboleth) {
			reqBuilder.addHeader(Constants.SHIB_IDENTITA_CODICE_FISCALE, cf);
		}
		if (!StringUtils.isEmpty(xForwardedFor)) {
			reqBuilder.addHeader(Constants.X_FORWARDED_FOR, xForwardedFor);
		}
		return reqBuilder;
	}

//    private Request createPostRequest(HttpHeaders headers, String url, String json, boolean shibboleth,boolean iride){
//        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8") );
//        Request request = addCustomHeaders(headers, new Request.Builder(), shibboleth, iride)
//                .url(url)
//                .post(body)
//                .build();
//
//        return request;
//    }
//    
//    private Request createPutRequest(HttpHeaders headers, String url, String json, boolean shibboleth,boolean iride){
//    	RequestBody	body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8") );
//    	Request request = addCustomHeaders(headers, new Request.Builder(), shibboleth,iride)
//    			.url(url)
//    			.put(body)
//    			.build();
//
//    	return request;
//    }    

//    private Request createDeleteRequest(HttpHeaders headers, String url, boolean shibboleth,boolean iride) {
//    	 Request request = addCustomHeaders(headers, new Request.Builder(), shibboleth,iride)
//    			 .url(url)
//    			 .delete()
//    			 .build();
//    	 return request;
//    }

	public String getBuonodomsrvusername() {
		return buonodomsrvusername;
	}

	public void setBuonodomsrvusername(String buonodomsrvusername) {
		this.buonodomsrvusername = buonodomsrvusername;
	}

	public String getBuonodomsrvpassword() {
		return buonodomsrvpassword;
	}

	public void setBuonodomsrvpassword(String buonodomsrvpassword) {
		this.buonodomsrvpassword = buonodomsrvpassword;
	}

	public String getBuonodomsrvurl() {
		return buonodomsrvurl;
	}

	public void setBuonodomsrvurl(String buonodomsrvurl) {
		this.buonodomsrvurl = buonodomsrvurl;
	}

}

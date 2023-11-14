/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import okhttp3.OkHttpClient;

@Component
public class BandiRestBaseService extends RestBaseService {
	private OkHttpClient okHttpClient;

	@Value("${bandiacquisizionedomandeuser}")
	private String bandiacquisizionedomandeuser;

	@Value("${bandiacquisizionedomandepassword}")
	private String bandiacquisizionedomandepassword;

	@Override
	protected OkHttpClient getHttpClient() {
		if (okHttpClient == null) {
			okHttpClient = new OkHttpClient.Builder()
					.addInterceptor(
							new BasicAuthInterceptor(bandiacquisizionedomandeuser, bandiacquisizionedomandepassword))
					.build();
		}

		return okHttpClient;
	}

}

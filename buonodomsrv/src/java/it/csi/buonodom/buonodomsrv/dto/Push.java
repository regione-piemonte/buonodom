/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Push {

	private String title;
	private String body;

	@JsonProperty("title")

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("body")
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Push() {
		super();
	}

}

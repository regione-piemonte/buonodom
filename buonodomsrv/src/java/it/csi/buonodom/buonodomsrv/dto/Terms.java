/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Terms {

	private String hashedTerms;
	private String acceptedAt;

	@JsonProperty("hashed_terms")
	public String getHashedTerms() {
		return hashedTerms;
	}

	public void setHashedTerms(String hashedTerms) {
		this.hashedTerms = hashedTerms;
	}

	@JsonProperty("accepted_at")
	public String getAcceptedAt() {
		return acceptedAt;
	}

	public void setAcceptedAt(String acceptedAt) {
		this.acceptedAt = acceptedAt;
	}

	public Terms() {
		super();
	}

}

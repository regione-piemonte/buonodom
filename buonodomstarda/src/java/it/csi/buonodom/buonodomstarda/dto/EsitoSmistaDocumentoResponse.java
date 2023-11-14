/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomstarda.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EsitoSmistaDocumentoResponse {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private ResultType esito = null;

	/**
	 **/

	@JsonProperty("esito")

	public ResultType getEsito() {
		return esito;
	}

	public void setEsito(ResultType esito) {
		this.esito = esito;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EsitoSmistaDocumentoResponse esitoSmistaDocumentoResponse = (EsitoSmistaDocumentoResponse) o;
		return Objects.equals(esito, esitoSmistaDocumentoResponse.esito);
	}

	@Override
	public int hashCode() {
		return Objects.hash(esito);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EsitoSmistaDocumentoResponse {\n");

		sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

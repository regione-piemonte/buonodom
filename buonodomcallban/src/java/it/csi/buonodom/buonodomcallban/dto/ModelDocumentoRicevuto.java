/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDocumentoRicevuto {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String idDocumentoBuonodom = null;
	private String idDocumentoBandi = null;

	/**
	 * identificativo del documento di spesa su buonodom
	 **/

	@JsonProperty("id_documento_buonodom")

	public String getIdDocumentoBuonodom() {
		return idDocumentoBuonodom;
	}

	public void setIdDocumentoBuonodom(String idDocumentoBuonodom) {
		this.idDocumentoBuonodom = idDocumentoBuonodom;
	}

	/**
	 * identificativo del documento di spesa su sistema bandi
	 **/

	@JsonProperty("id_documento_bandi")

	public String getIdDocumentoBandi() {
		return idDocumentoBandi;
	}

	public void setIdDocumentoBandi(String idDocumentoBandi) {
		this.idDocumentoBandi = idDocumentoBandi;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelDocumentoRicevuto modelDocumentoRicevuto = (ModelDocumentoRicevuto) o;
		return Objects.equals(idDocumentoBuonodom, modelDocumentoRicevuto.idDocumentoBuonodom)
				&& Objects.equals(idDocumentoBandi, modelDocumentoRicevuto.idDocumentoBandi);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idDocumentoBuonodom, idDocumentoBandi);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelDocumentoRicevuto {\n");

		sb.append("    idDocumentoBuonodom: ").append(toIndentedString(idDocumentoBuonodom)).append("\n");
		sb.append("    idDocumentoBandi: ").append(toIndentedString(idDocumentoBandi)).append("\n");
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

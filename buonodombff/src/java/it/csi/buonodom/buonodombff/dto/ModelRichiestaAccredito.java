/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelRichiestaAccredito {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String iban = null;
	private String intestatario = null;

	/**
	 * IBAN conto corrente di accredito
	 **/

	@JsonProperty("iban")

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	/**
	 * intestatario conto corrente di accredito
	 **/

	@JsonProperty("intestatario")

	public String getIntestatario() {
		return intestatario;
	}

	public void setIntestatario(String intestatario) {
		this.intestatario = intestatario;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelRichiestaAccredito modelRichiestaAccredito = (ModelRichiestaAccredito) o;
		return Objects.equals(iban, modelRichiestaAccredito.iban)
				&& Objects.equals(intestatario, modelRichiestaAccredito.intestatario);
	}

	@Override
	public int hashCode() {
		return Objects.hash(iban, intestatario);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelRichiestaAccredito {\n");

		sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
		sb.append("    intestatario: ").append(toIndentedString(intestatario)).append("\n");
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

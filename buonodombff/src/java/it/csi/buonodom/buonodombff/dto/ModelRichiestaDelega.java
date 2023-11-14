/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelRichiestaDelega {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private BigDecimal codice = null;
	private String etichetta = null;

	/**
	 **/

	@JsonProperty("codice")

	public BigDecimal getCodice() {
		return codice;
	}

	public void setCodice(BigDecimal codice) {
		this.codice = codice;
	}

	/**
	 **/

	@JsonProperty("etichetta")

	public String getEtichetta() {
		return etichetta;
	}

	public void setEtichetta(String etichetta) {
		this.etichetta = etichetta;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelRichiestaDelega modelRichiestaDelega = (ModelRichiestaDelega) o;
		return Objects.equals(codice, modelRichiestaDelega.codice)
				&& Objects.equals(etichetta, modelRichiestaDelega.etichetta);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codice, etichetta);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelRichiestaDelega {\n");

		sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
		sb.append("    etichetta: ").append(toIndentedString(etichetta)).append("\n");
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

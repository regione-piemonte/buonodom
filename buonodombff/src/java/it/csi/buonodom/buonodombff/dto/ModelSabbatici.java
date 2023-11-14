/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelSabbatici {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private List<String> sabbatici = new ArrayList<String>();

	/**
	 * eventuali mesi sabbatici (massimo due elementi)
	 **/

	@JsonProperty("sabbatici")

	public List<String> getSabbatici() {
		return sabbatici;
	}

	public void setSabbatici(List<String> sabbatici) {
		this.sabbatici = sabbatici;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelSabbatici modelSabbatici = (ModelSabbatici) o;
		return Objects.equals(sabbatici, modelSabbatici.sabbatici);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sabbatici);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelSabbatici {\n");

		sb.append("    sabbatici: ").append(toIndentedString(sabbatici)).append("\n");
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

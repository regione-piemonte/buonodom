/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelFruizione {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private ModelFruizioneFruizione fruizione = null;
	private ModelFruizioneRendicontazione rendicontazione = null;
	private List<String> sabbatici = new ArrayList<String>();
	private List<String> nonRendicontati = new ArrayList<String>();

	/**
	 **/

	@JsonProperty("fruizione")

	public ModelFruizioneFruizione getFruizione() {
		return fruizione;
	}

	public void setFruizione(ModelFruizioneFruizione fruizione) {
		this.fruizione = fruizione;
	}

	/**
	 **/

	@JsonProperty("rendicontazione")

	public ModelFruizioneRendicontazione getRendicontazione() {
		return rendicontazione;
	}

	public void setRendicontazione(ModelFruizioneRendicontazione rendicontazione) {
		this.rendicontazione = rendicontazione;
	}

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

	@JsonProperty("non_rendicontati")

	public List<String> getNonRendicontati() {
		return nonRendicontati;
	}

	public void setNonRendicontati(List<String> nonRendicontati) {
		this.nonRendicontati = nonRendicontati;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelFruizione modelFruizione = (ModelFruizione) o;
		return Objects.equals(fruizione, modelFruizione.fruizione)
				&& Objects.equals(rendicontazione, modelFruizione.rendicontazione)
				&& Objects.equals(sabbatici, modelFruizione.sabbatici)
				&& Objects.equals(nonRendicontati, modelFruizione.nonRendicontati);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fruizione, rendicontazione, sabbatici, nonRendicontati);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelFruizione {\n");

		sb.append("    fruizione: ").append(toIndentedString(fruizione)).append("\n");
		sb.append("    rendicontazione: ").append(toIndentedString(rendicontazione)).append("\n");
		sb.append("    sabbatici: ").append(toIndentedString(sabbatici)).append("\n");
		sb.append("    nonRendicontati: ").append(toIndentedString(nonRendicontati)).append("\n");
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

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelFruizioneRendicontazione {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dataInizio = null;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dataFine = null;

	/**
	 **/

	@JsonProperty("data_inizio")

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	/**
	 **/

	@JsonProperty("data_fine")

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelFruizioneRendicontazione modelFruizioneRendicontazione = (ModelFruizioneRendicontazione) o;
		return Objects.equals(dataInizio, modelFruizioneRendicontazione.dataInizio)
				&& Objects.equals(dataFine, modelFruizioneRendicontazione.dataFine);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataInizio, dataFine);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelFruizioneRendicontazione {\n");

		sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
		sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
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

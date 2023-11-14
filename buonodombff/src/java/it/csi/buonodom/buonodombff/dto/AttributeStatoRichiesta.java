/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class AttributeStatoRichiesta {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	/**
	 * codice stato della richiesta
	 */
	public enum CodiceEnum {
		BOZZA("BOZZA"),

		INVIATA("INVIATA"),

		AMMESSA("AMMESSA"),

		NON_AMMESSA("NON_AMMESSA"),

		DA_RETTIFICARE("DA_RETTIFICARE"),

		IN_RETTIFICA("IN_RETTIFICA"),

		RETTIFICATA("RETTIFICATA"),

		AMMESSA_RISERVA("AMMESSA_RISERVA"),

		REVOCATA("REVOCATA"),

		ANNULLATA("ANNULLATA"),

		DINIEGO("DINIEGO"),

		PREAVVISO_DINIEGO("PREAVVISO_DINIEGO"),

		PRESA_IN_CARICO("PRESA_IN_CARICO"),

		IN_PAGAMENTO("IN_PAGAMENTO");

		private String value;

		CodiceEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}
	}

	private CodiceEnum codice = null;

	/**
	 * etichetta stato della richiesta
	 */
	public enum EtichettaEnum {
		BOZZA("Bozza"),

		INVIATA("Inviata"),

		AMMESSA("Ammessa"),

		NON_AMMESSA("Non ammessa"),

		DA_RETTIFICARE("Da rettificare"),

		IN_RETTIFICA("In Rettifica"),

		RETTIFICATA("Rettificata"),

		AMMESSA_CON_RISERVA("Ammessa con riserva"),

		REVOCATA("Revocata"),

		ANNULLATA("Annullata"),

		PERFEZIONATA("Perfezionata"),

		DINIEGO("Diniego"),

		PREAVVISO_DI_DINIEGO("Preavviso di diniego"),

		PRESA_IN_CARICO("Presa in carico"),

		IN_PAGAMENTO("In pagamento");

		private String value;

		EtichettaEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}
	}

	private EtichettaEnum etichetta = null;

	/**
	 * codice stato della richiesta
	 **/

	@JsonProperty("codice")

	public CodiceEnum getCodice() {
		return codice;
	}

	public void setCodice(CodiceEnum codice) {
		this.codice = codice;
	}

	/**
	 * etichetta stato della richiesta
	 **/

	@JsonProperty("etichetta")

	public EtichettaEnum getEtichetta() {
		return etichetta;
	}

	public void setEtichetta(EtichettaEnum etichetta) {
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
		AttributeStatoRichiesta attributeStatoRichiesta = (AttributeStatoRichiesta) o;
		return Objects.equals(codice, attributeStatoRichiesta.codice)
				&& Objects.equals(etichetta, attributeStatoRichiesta.etichetta);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codice, etichetta);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class AttributeStatoRichiesta {\n");

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

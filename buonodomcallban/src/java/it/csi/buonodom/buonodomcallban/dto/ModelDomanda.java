/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDomanda {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String numeroDomanda = null;
	private String cfBeneficiario = null;
	private String cfRichiedente = null;

	/**
	 * numero della domanda
	 **/

	@JsonProperty("numero_domanda")

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	/**
	 * codice fiscale del beneficiario
	 **/

	@JsonProperty("cf_beneficiario")

	public String getCfBeneficiario() {
		return cfBeneficiario;
	}

	public void setCfBeneficiario(String cfBeneficiario) {
		this.cfBeneficiario = cfBeneficiario;
	}

	/**
	 * codice fiscale del richiedente
	 **/

	@JsonProperty("cf_richiedente")

	public String getCfRichiedente() {
		return cfRichiedente;
	}

	public void setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelDomanda modelDomanda = (ModelDomanda) o;
		return Objects.equals(numeroDomanda, modelDomanda.numeroDomanda)
				&& Objects.equals(cfBeneficiario, modelDomanda.cfBeneficiario)
				&& Objects.equals(cfRichiedente, modelDomanda.cfRichiedente);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numeroDomanda, cfBeneficiario, cfRichiedente);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelDomanda {\n");

		sb.append("    numeroDomanda: ").append(toIndentedString(numeroDomanda)).append("\n");
		sb.append("    cfBeneficiario: ").append(toIndentedString(cfBeneficiario)).append("\n");
		sb.append("    cfRichiedente: ").append(toIndentedString(cfRichiedente)).append("\n");
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

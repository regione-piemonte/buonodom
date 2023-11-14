/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelFornitore {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private Integer id = null;
	private String nome = null;
	private String cognome = null;
	private String cf = null;
	private String denominazione = null;
	private String piva = null;

	/**
	 * id per riferimento (non presente nel body della POST)
	 **/

	@JsonProperty("id")

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * nome
	 **/

	@JsonProperty("nome")

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * cognome
	 **/

	@JsonProperty("cognome")

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * codice fiscale
	 **/

	@JsonProperty("cf")

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	/**
	 * denominazione azienda
	 **/

	@JsonProperty("denominazione")

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * partita iva
	 **/

	@JsonProperty("piva")

	public String getPiva() {
		return piva;
	}

	public void setPiva(String piva) {
		this.piva = piva;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelFornitore modelFornitore = (ModelFornitore) o;
		return Objects.equals(id, modelFornitore.id) && Objects.equals(nome, modelFornitore.nome)
				&& Objects.equals(cognome, modelFornitore.cognome) && Objects.equals(cf, modelFornitore.cf)
				&& Objects.equals(denominazione, modelFornitore.denominazione)
				&& Objects.equals(piva, modelFornitore.piva);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nome, cognome, cf, denominazione, piva);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelFornitore {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
		sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
		sb.append("    cf: ").append(toIndentedString(cf)).append("\n");
		sb.append("    denominazione: ").append(toIndentedString(denominazione)).append("\n");
		sb.append("    piva: ").append(toIndentedString(piva)).append("\n");
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

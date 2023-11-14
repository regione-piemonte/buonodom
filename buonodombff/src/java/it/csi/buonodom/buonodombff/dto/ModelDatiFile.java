/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDatiFile {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String nome = null;
	private String cog = null;

	/**
	 **/

	@JsonProperty("nome")

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 **/

	@JsonProperty("cog")

	public String getCog() {
		return cog;
	}

	public void setCog(String cog) {
		this.cog = cog;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelDatiFile modelDatiFile = (ModelDatiFile) o;
		return Objects.equals(nome, modelDatiFile.nome) && Objects.equals(cog, modelDatiFile.cog);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome, cog);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelDatiFile {\n");

		sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
		sb.append("    cog: ").append(toIndentedString(cog)).append("\n");
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

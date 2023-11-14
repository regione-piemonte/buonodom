/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDocumentoSpesaDettaglio {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private Integer idAllegato = null;
	private String tipo = null;
	private BigDecimal importo = null;
	private Date data = null;

	/**
	 * id dell&#39;allegato
	 **/

	@JsonProperty("id_allegato")

	public Integer getIdAllegato() {
		return idAllegato;
	}

	public void setIdAllegato(Integer idAllegato) {
		this.idAllegato = idAllegato;
	}

	/**
	 * tipo di allegato (giustificativo/quietanza) codice ottenuto dal servizio di
	 * decodifica
	 **/

	@JsonProperty("tipo")

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * importo dell&#39;allegato (in euro)
	 **/

	@JsonProperty("importo")

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	/**
	 * data dell&#39;allegato dal frontend viene popolato come YYYY-MM-DD
	 **/

	@JsonProperty("data")

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelDocumentoSpesaDettaglio modelDocumentoSpesaDettaglio = (ModelDocumentoSpesaDettaglio) o;
		return Objects.equals(idAllegato, modelDocumentoSpesaDettaglio.idAllegato)
				&& Objects.equals(tipo, modelDocumentoSpesaDettaglio.tipo)
				&& Objects.equals(importo, modelDocumentoSpesaDettaglio.importo)
				&& Objects.equals(data, modelDocumentoSpesaDettaglio.data);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idAllegato, tipo, importo, data);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelDocumentoSpesaDettaglio {\n");

		sb.append("    idAllegato: ").append(toIndentedString(idAllegato)).append("\n");
		sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
		sb.append("    importo: ").append(toIndentedString(importo)).append("\n");
		sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

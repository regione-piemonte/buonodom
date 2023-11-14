/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomstarda.dto.custom;

import it.csi.buonodom.buonodomstarda.dto.ErroreDettaglio;

public class ErroreDettaglioExt extends ErroreDettaglio {
	private Integer erroreId;

	public Integer getErroreId() {
		return erroreId;
	}

	public void setErroreId(Integer erroreId) {
		this.erroreId = erroreId;
	}

	public ErroreDettaglio wrap() {
		ErroreDettaglio erroreDettaglio = new ErroreDettaglio();
		erroreDettaglio.setChiave(this.getChiave());
		erroreDettaglio.setValore(this.getValore());
		return erroreDettaglio;

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ErroreDettaglio {\n");

		sb.append("    chiave: ").append(toIndentedString(getChiave())).append("\n");
		sb.append("    valore: ").append(toIndentedString(getValore())).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

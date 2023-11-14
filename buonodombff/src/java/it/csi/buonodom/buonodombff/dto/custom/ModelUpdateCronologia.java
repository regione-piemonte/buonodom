/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto.custom;

public class ModelUpdateCronologia {

	private long idDettaglio;
	private String detCod;

	public long getIdDettaglio() {
		return idDettaglio;
	}

	public void setIdDettaglio(long idDettaglio) {
		this.idDettaglio = idDettaglio;
	}

	public String getDetCod() {
		return detCod;
	}

	public void setDetCod(String detCod) {
		this.detCod = detCod;
	}

}

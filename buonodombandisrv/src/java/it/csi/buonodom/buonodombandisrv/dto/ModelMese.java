/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelMese {

	private String valore = null;
	private Boolean sabbatico = null;

	/**
	 * codice utilizzato internamente
	 **/

	@JsonProperty("Valore")

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	@JsonProperty("Sabbatico")
	public boolean isSabbatico() {
		return sabbatico;
	}

	public void setSabbatico(boolean sabbatico) {
		this.sabbatico = sabbatico;
	}

}

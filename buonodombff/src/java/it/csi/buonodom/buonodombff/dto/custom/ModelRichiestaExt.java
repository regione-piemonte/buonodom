/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto.custom;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.csi.buonodom.buonodombff.dto.ModelRichiesta;

public class ModelRichiestaExt extends ModelRichiesta {

	private String domandaDetCod = null;
	private BigDecimal domandaDetId = null;
	private BigDecimal domandaId = null;
	private BigDecimal sportelloId = null;

	public BigDecimal getDomandaId() {
		return domandaId;
	}

	public void setDomandaId(BigDecimal domandaId) {
		this.domandaId = domandaId;
	}

	public BigDecimal getDomandaDetId() {
		return domandaDetId;
	}

	public void setDomandaDetId(BigDecimal domandaDetId) {
		this.domandaDetId = domandaDetId;
	}

	public BigDecimal getSportelloId() {
		return sportelloId;
	}

	public void setSportelloId(BigDecimal sportelloId) {
		this.sportelloId = sportelloId;
	}

	@JsonProperty("domanda_det_cod")

	public String getDomandaDetCod() {
		return domandaDetCod;
	}

	public void setDomandaDetCod(String domandaDetCod) {
		this.domandaDetCod = domandaDetCod;
	}
}

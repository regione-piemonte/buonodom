/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.dto;

import java.math.BigDecimal;

public class ModelEmailDetId {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private BigDecimal domandaDetId = null;
	private String email = null;

	public BigDecimal getDomandaDetId() {
		return domandaDetId;
	}

	public void setDomandaDetId(BigDecimal domandaDetId) {
		this.domandaDetId = domandaDetId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "ModelEmailDetId [domandaDetId=" + domandaDetId + ", email=" + email + "]";
	}

}

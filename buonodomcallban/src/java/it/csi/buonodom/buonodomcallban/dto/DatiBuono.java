/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.dto;

import java.math.BigDecimal;

public class DatiBuono {

	private String iban = null;
	private String ibanIntestatario = null;
	private BigDecimal buonoId = null;

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getIbanIntestatario() {
		return ibanIntestatario;
	}

	public void setIbanIntestatario(String ibanIntestatario) {
		this.ibanIntestatario = ibanIntestatario;
	}

	public BigDecimal getBuonoId() {
		return buonoId;
	}

	public void setBuonoId(BigDecimal buonoId) {
		this.buonoId = buonoId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DatiBuono [iban=");
		builder.append(iban);
		builder.append(", ibanIntestatario=");
		builder.append(ibanIntestatario);
		builder.append(", buonoId=");
		builder.append(buonoId);
		builder.append("]");
		return builder.toString();
	}

}

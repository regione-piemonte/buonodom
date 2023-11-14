/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.dto;

import java.math.BigDecimal;

public class DatiDichiarazioneDocSpesa {

	private String docSpesaCod = null;
	private String docSpesaStatoCod = null;
	private BigDecimal docSpesaId = null;

	public String getDocSpesaCod() {
		return docSpesaCod;
	}

	public void setDocSpesaCod(String docSpesaCod) {
		this.docSpesaCod = docSpesaCod;
	}

	public String getDocSpesaStatoCod() {
		return docSpesaStatoCod;
	}

	public void setDocSpesaStatoCod(String docSpesaStatoCod) {
		this.docSpesaStatoCod = docSpesaStatoCod;
	}

	public BigDecimal getDocSpesaId() {
		return docSpesaId;
	}

	public void setDocSpesaId(BigDecimal docSpesaId) {
		this.docSpesaId = docSpesaId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DatiDichiarazioneDocSpesa [docSpesaCod=");
		builder.append(docSpesaCod);
		builder.append(", docSpesaStatoCod=");
		builder.append(docSpesaStatoCod);
		builder.append(", docSpesaId=");
		builder.append(docSpesaId);
		builder.append("]");
		return builder.toString();
	}

}

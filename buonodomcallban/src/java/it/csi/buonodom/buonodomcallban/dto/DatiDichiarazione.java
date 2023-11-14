/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.dto;

import java.math.BigDecimal;
import java.util.List;

public class DatiDichiarazione {

	private String dicSpesaCod = null;
	private String dicSpesaStatoCod = null;
	private BigDecimal dicSpesaId = null;
	private BigDecimal buonoId = null;
	private String domandaNumero = null;
	private List<DatiDichiarazioneDocSpesa> docSpesa = null;

	public String getDicSpesaCod() {
		return dicSpesaCod;
	}

	public void setDicSpesaCod(String dicSpesaCod) {
		this.dicSpesaCod = dicSpesaCod;
	}

	public String getDicSpesaStatoCod() {
		return dicSpesaStatoCod;
	}

	public void setDicSpesaStatoCod(String dicSpesaStatoCod) {
		this.dicSpesaStatoCod = dicSpesaStatoCod;
	}

	public BigDecimal getDicSpesaId() {
		return dicSpesaId;
	}

	public void setDicSpesaId(BigDecimal dicSpesaId) {
		this.dicSpesaId = dicSpesaId;
	}

	public BigDecimal getBuonoId() {
		return buonoId;
	}

	public void setBuonoId(BigDecimal buonoId) {
		this.buonoId = buonoId;
	}

	public String getDomandaNumero() {
		return domandaNumero;
	}

	public void setDomandaNumero(String domandaNumero) {
		this.domandaNumero = domandaNumero;
	}

	public List<DatiDichiarazioneDocSpesa> getDocSpesa() {
		return docSpesa;
	}

	public void setDocSpesa(List<DatiDichiarazioneDocSpesa> docSpesa) {
		this.docSpesa = docSpesa;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DatiDichiarazione [dicSpesaCod=");
		builder.append(dicSpesaCod);
		builder.append(", dicSpesaStatoCod=");
		builder.append(dicSpesaStatoCod);
		builder.append(", dicSpesaId=");
		builder.append(dicSpesaId);
		builder.append(", buonoId=");
		builder.append(buonoId);
		builder.append(", domandaNumero=");
		builder.append(domandaNumero);
		builder.append(", docSpesa=");
		builder.append(docSpesa);
		builder.append("]");
		return builder.toString();
	}
}

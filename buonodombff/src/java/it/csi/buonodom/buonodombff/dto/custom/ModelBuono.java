/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto.custom;

public class ModelBuono {
	private String buonoCod = null;
	private Long buonoId = null;
	private String richiedenteCf = null;
	private String stato = null;
	private Long buonoStatoId = null;
	private String iban = null;
	private String IbanIntestatario = null;

	public String getBuonoCod() {
		return buonoCod;
	}

	public void setBuonoCod(String buonoCod) {
		this.buonoCod = buonoCod;
	}

	public Long getBuonoId() {
		return buonoId;
	}

	public void setBuonoId(Long buonoId) {
		this.buonoId = buonoId;
	}

	public String getRichiedenteCf() {
		return richiedenteCf;
	}

	public void setRichiedenteCf(String richiedenteCf) {
		this.richiedenteCf = richiedenteCf;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Long getBuonoStatoId() {
		return buonoStatoId;
	}

	public void setBuonoStatoId(Long buonoStatoId) {
		this.buonoStatoId = buonoStatoId;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getIbanIntestatario() {
		return IbanIntestatario;
	}

	public void setIbanIntestatario(String ibanIntestatario) {
		IbanIntestatario = ibanIntestatario;
	}

}

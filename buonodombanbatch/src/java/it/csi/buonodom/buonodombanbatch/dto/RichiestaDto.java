/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombanbatch.dto;

import java.util.Date;
import java.util.Objects;

public class RichiestaDto {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String cf = null;
	private long domandaDetId = 0;
	private String domandaNumero = null;
	private String richiedenteCf = null;
	private String stato = null;
	private Date inizioValidita = null;
	private Integer giorni = null;
	private Long sportelloId = null;
	private long domandaId = 0;
	private String iban = null;
	private String ibanIntestatario = null;

	/**
	 * Codice univoco di errore interno
	 **/

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public Integer getGiorni() {
		return giorni;
	}

	public void setGiorni(Integer giorni) {
		this.giorni = giorni;
	}

	public Date getInizioValidita() {
		return inizioValidita;
	}

	public void setInizioValidita(Date inizioValidita) {
		this.inizioValidita = inizioValidita;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public long getDomandaDetId() {
		return domandaDetId;
	}

	public void setDomandaDetId(long domandaDetId) {
		this.domandaDetId = domandaDetId;
	}

	public String getDomandaNumero() {
		return domandaNumero;
	}

	public void setDomandaNumero(String domandaNumero) {
		this.domandaNumero = domandaNumero;
	}

	public String getRichiedenteCf() {
		return richiedenteCf;
	}

	public void setRichiedenteCf(String richiedenteCf) {
		this.richiedenteCf = richiedenteCf;
	}

	public Long getSportelloId() {
		return sportelloId;
	}

	public void setSportelloId(Long sportelloId) {
		this.sportelloId = sportelloId;
	}

	public long getDomandaId() {
		return domandaId;
	}

	public void setDomandaId(long domandaId) {
		this.domandaId = domandaId;
	}

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RichiestaDto [cf=");
		builder.append(cf);
		builder.append(", domandaDetId=");
		builder.append(domandaDetId);
		builder.append(", domandaNumero=");
		builder.append(domandaNumero);
		builder.append(", richiedenteCf=");
		builder.append(richiedenteCf);
		builder.append(", stato=");
		builder.append(stato);
		builder.append(", inizioValidita=");
		builder.append(inizioValidita);
		builder.append(", giorni=");
		builder.append(giorni);
		builder.append(", sportelloId=");
		builder.append(sportelloId);
		builder.append(", domandaId=");
		builder.append(domandaId);
		builder.append(", iban=");
		builder.append(iban);
		builder.append(", ibanIntestatario=");
		builder.append(ibanIntestatario);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(cf, domandaDetId, domandaId, domandaNumero, giorni, iban, ibanIntestatario, inizioValidita,
				richiedenteCf, sportelloId, stato);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RichiestaDto other = (RichiestaDto) obj;
		return Objects.equals(cf, other.cf) && domandaDetId == other.domandaDetId && domandaId == other.domandaId
				&& Objects.equals(domandaNumero, other.domandaNumero) && Objects.equals(giorni, other.giorni)
				&& Objects.equals(iban, other.iban) && Objects.equals(ibanIntestatario, other.ibanIntestatario)
				&& Objects.equals(inizioValidita, other.inizioValidita)
				&& Objects.equals(richiedenteCf, other.richiedenteCf) && Objects.equals(sportelloId, other.sportelloId)
				&& Objects.equals(stato, other.stato);
	}

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDocumentoSpesa {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private Integer id = null;
	private String tipologia = null;
	private String numero = null;
	private Integer idFornitore = null;
	private List<ModelDocumentoSpesaDettaglio> dettagli = new ArrayList<ModelDocumentoSpesaDettaglio>();
	private List<String> mesi = new ArrayList<String>();
	private String stato = null;
	private String note = null;
	private String periodoFine = null;

	/**
	 * id per riferimento (non presente nel body della POST)
	 **/

	@JsonProperty("id")

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * tipologia di documento di spesa (codice ottenuto da servizio decodifiche)
	 **/

	@JsonProperty("tipologia")

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	/**
	 * numero del documento di spesa
	 **/

	@JsonProperty("numero")

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * id del fornitore recuperasto dal servizio
	 **/

	@JsonProperty("id_fornitore")

	public Integer getIdFornitore() {
		return idFornitore;
	}

	public void setIdFornitore(Integer idFornitore) {
		this.idFornitore = idFornitore;
	}

	/**
	 **/

	@JsonProperty("dettagli")

	public List<ModelDocumentoSpesaDettaglio> getDettagli() {
		return dettagli;
	}

	public void setDettagli(List<ModelDocumentoSpesaDettaglio> dettagli) {
		this.dettagli = dettagli;
	}

	/**
	 **/

	@JsonProperty("mesi")

	public List<String> getMesi() {
		return mesi;
	}

	public void setMesi(List<String> mesi) {
		this.mesi = mesi;
	}

	/**
	 * indica se la rendicontaione è stata comunicata/accettata/respinta
	 **/

	@JsonProperty("stato")

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	/**
	 * note per motivazione di rendicontazione respinta
	 **/

	@JsonProperty("note")

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@JsonProperty("periodo_fine")

	public String getPeriodoFine() {
		return periodoFine;
	}

	public void setPeriodoFine(String periodoFine) {
		this.periodoFine = periodoFine;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelDocumentoSpesa [id=");
		builder.append(id);
		builder.append(", tipologia=");
		builder.append(tipologia);
		builder.append(", numero=");
		builder.append(numero);
		builder.append(", idFornitore=");
		builder.append(idFornitore);
		builder.append(", dettagli=");
		builder.append(dettagli);
		builder.append(", mesi=");
		builder.append(mesi);
		builder.append(", stato=");
		builder.append(stato);
		builder.append(", note=");
		builder.append(note);
		builder.append(", periodoFine=");
		builder.append(periodoFine);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(dettagli, id, idFornitore, mesi, note, numero, periodoFine, stato, tipologia);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelDocumentoSpesa other = (ModelDocumentoSpesa) obj;
		return Objects.equals(dettagli, other.dettagli) && Objects.equals(id, other.id)
				&& Objects.equals(idFornitore, other.idFornitore) && Objects.equals(mesi, other.mesi)
				&& Objects.equals(note, other.note) && Objects.equals(numero, other.numero)
				&& Objects.equals(periodoFine, other.periodoFine) && Objects.equals(stato, other.stato)
				&& Objects.equals(tipologia, other.tipologia);
	}

}

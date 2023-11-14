/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelInvioDichiarazioneSpesa {

	private String numeroDomanda = null;
	private String codiceBando = null;
	private String dataDichiarazioneSpesa = null;
	private String dicSpesaCod = null;
	private List<ModelMese> mese = null;
	private String nomeFile = null;
	private List<ModelInvioDocumentoSpesa> documentoDiSpesa = null;
	private List<ModelInvioFornitore> fornitore = null;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelInvioDichiarazioneSpesa [numeroDomanda=");
		builder.append(numeroDomanda);
		builder.append(", codiceBando=");
		builder.append(codiceBando);
		builder.append(", dataDichiarazioneSpesa=");
		builder.append(dataDichiarazioneSpesa);
		builder.append(", dicSpesaCod=");
		builder.append(dicSpesaCod);
		builder.append(", mese=");
		builder.append(mese);
		builder.append(", nomeFile=");
		builder.append(nomeFile);
		builder.append(", documentoDiSpesa=");
		builder.append(documentoDiSpesa);
		builder.append(", fornitore=");
		builder.append(fornitore);
		builder.append("]");
		return builder.toString();
	}

	@JsonProperty("Numero_domanda")
	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	@JsonProperty("Codice_bando")
	public String getCodiceBando() {
		return codiceBando;
	}

	public void setCodiceBando(String codiceBando) {
		this.codiceBando = codiceBando;
	}

	@JsonProperty("Data_Dichiarazione_di_spesa")

	public String getDataDichiarazioneSpesa() {
		return dataDichiarazioneSpesa;
	}

	public void setDataDichiarazioneSpesa(String dataDichiarazioneSpesa) {
		this.dataDichiarazioneSpesa = dataDichiarazioneSpesa;
	}

	@JsonProperty("Mensilita")
	public List<ModelMese> getMese() {
		return mese;
	}

	public void setMese(List<ModelMese> mese) {
		this.mese = mese;
	}

	@JsonProperty("Nome_file")
	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	@JsonProperty("Documenti_di_spesa")
	public List<ModelInvioDocumentoSpesa> getDocumentoDiSpesa() {
		return documentoDiSpesa;
	}

	public void setDocumentoDiSpesa(List<ModelInvioDocumentoSpesa> documentoDiSpesa) {
		this.documentoDiSpesa = documentoDiSpesa;
	}

	@JsonProperty("Fornitori")
	public List<ModelInvioFornitore> getFornitore() {
		return fornitore;
	}

	public void setFornitore(List<ModelInvioFornitore> fornitore) {
		this.fornitore = fornitore;
	}

	@JsonProperty("id_dichiarazione_buonodom")
	public String getDicSpesaCod() {
		return dicSpesaCod;
	}

	public void setDicSpesaCod(String dicSpesaCod) {
		this.dicSpesaCod = dicSpesaCod;
	}

}

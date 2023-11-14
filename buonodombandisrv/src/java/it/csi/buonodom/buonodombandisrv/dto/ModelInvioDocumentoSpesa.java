/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelInvioDocumentoSpesa {

	private String dataDocumento = null;
	private String numeroDocumento = null;
	private String docSpesaCod = null;
	private String importoTotaleDocumento = null;
	private String importoQuietanziato = null;
	private String descrizioneDocumento = null;
	private String codiceVoceDiSpesa = null;
	private String codiceFiscaleFornitore = null;
	private String tipologiaDocumento = null;
	private List<ModelDocumento> documento = null;

	@JsonProperty("Numero_documento")
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	@JsonProperty("Importo_totale_documento")
	public String getImportoTotaleDocumento() {
		return importoTotaleDocumento;
	}

	public void setImportoTotaleDocumento(String importoTotaleDocumento) {
		this.importoTotaleDocumento = importoTotaleDocumento;
	}

	@JsonProperty("Importo_quietanziato")
	public String getImportoQuietanziato() {
		return importoQuietanziato;
	}

	public void setImportoQuietanziato(String importoQuietanziato) {
		this.importoQuietanziato = importoQuietanziato;
	}

	@JsonProperty("Codice_voce_di_spesa")
	public String getCodiceVoceDiSpesa() {
		return codiceVoceDiSpesa;
	}

	public void setCodiceVoceDiSpesa(String codiceVoceDiSpesa) {
		this.codiceVoceDiSpesa = codiceVoceDiSpesa;
	}

	@JsonProperty("Codice_fiscale_fornitore")
	public String getCodiceFiscaleFornitore() {
		return codiceFiscaleFornitore;
	}

	public void setCodiceFiscaleFornitore(String codiceFiscaleFornitore) {
		this.codiceFiscaleFornitore = codiceFiscaleFornitore;
	}

	@JsonProperty("Tipologia_documento")
	public String getTipologiaDocumento() {
		return tipologiaDocumento;
	}

	public void setTipologiaDocumento(String tipologiaDocumento) {
		this.tipologiaDocumento = tipologiaDocumento;
	}

	@JsonProperty("Documenti")
	public List<ModelDocumento> getDocumento() {
		return documento;
	}

	public void setDocumento(List<ModelDocumento> documento) {
		this.documento = documento;
	}

	@JsonProperty("id_documento_buono_dom")
	public String getDocSpesaCod() {
		return docSpesaCod;
	}

	public void setDocSpesaCod(String docSpesaCod) {
		this.docSpesaCod = docSpesaCod;
	}

	@JsonProperty("Data_documento")
	public String getDataDocumento() {
		return dataDocumento;
	}

	public void setDataDocumento(String dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	@JsonProperty("Descrizione_documento")
	public String getDescrizioneDocumento() {
		return descrizioneDocumento;
	}

	public void setDescrizioneDocumento(String descrizioneDocumento) {
		this.descrizioneDocumento = descrizioneDocumento;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelInvioDocumentoSpesa [dataDocumento=");
		builder.append(dataDocumento);
		builder.append(", numeroDocumento=");
		builder.append(numeroDocumento);
		builder.append(", docSpesaCod=");
		builder.append(docSpesaCod);
		builder.append(", importoTotaleDocumento=");
		builder.append(importoTotaleDocumento);
		builder.append(", importoQuietanziato=");
		builder.append(importoQuietanziato);
		builder.append(", descrizioneDocumento=");
		builder.append(descrizioneDocumento);
		builder.append(", codiceVoceDiSpesa=");
		builder.append(codiceVoceDiSpesa);
		builder.append(", codiceFiscaleFornitore=");
		builder.append(codiceFiscaleFornitore);
		builder.append(", tipologiaDocumento=");
		builder.append(tipologiaDocumento);
		builder.append(", documento=");
		builder.append(documento);
		builder.append("]");
		return builder.toString();
	}

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelInvioFornitore {

	private String codiceFiscaleFornitore;
//	private String denominazioneFornitore;
//	private String cognome;
//	private String nome;
//	private String dataInizioContratto;
//	private String dataFineContratto;
	private List<ModelDocumento> documento;

	public ModelInvioFornitore() {
	}

	@JsonProperty("Codice_fiscale_fornitore")
	public String getCodiceFiscaleFornitore() {
		return codiceFiscaleFornitore;
	}

	public void setCodiceFiscaleFornitore(String codiceFiscaleFornitore) {
		this.codiceFiscaleFornitore = codiceFiscaleFornitore;
	}

//	 @JsonProperty("Denominazione_Fornitore") 
//	public String getDenominazioneFornitore() {
//		return denominazioneFornitore;
//	}
//
//
//	public void setDenominazioneFornitore(String denominazioneFornitore) {
//		this.denominazioneFornitore = denominazioneFornitore;
//	}
//
//	 @JsonProperty("Cognome") 
//	public String getCognome() {
//		return cognome;
//	}
//
//
//	public void setCognome(String cognome) {
//		this.cognome = cognome;
//	}
//
//	 @JsonProperty("Nome") 
//	public String getNome() {
//		return nome;
//	}
//
//
//	public void setNome(String nome) {
//		this.nome = nome;
//	}
//
//	 @JsonProperty("Data_Inizio_Contratto") 
//	public String getDataInizioContratto() {
//		return dataInizioContratto;
//	}
//
//
//	public void setDataInizioContratto(String dataInizioContratto) {
//		this.dataInizioContratto = dataInizioContratto;
//	}
//
//	 @JsonProperty("Data_Fine_Contratto") 
//	public String getDataFineContratto() {
//		return dataFineContratto;
//	}
//
//
//	public void setDataFineContratto(String dataFineContratto) {
//		this.dataFineContratto = dataFineContratto;
//	}

	@JsonProperty("Documenti")
	public List<ModelDocumento> getDocumento() {
		return documento;
	}

	public void setDocumento(List<ModelDocumento> documento) {
		this.documento = documento;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelInvioFornitore [codiceFiscaleFornitore=");
		builder.append(codiceFiscaleFornitore);
//		builder.append(", denominazioneFornitore=");
//		builder.append(denominazioneFornitore);
//		builder.append(", cognome=");
//		builder.append(cognome);
//		builder.append(", nome=");
//		builder.append(nome);
//		builder.append(", dataInizioContratto=");
//		builder.append(dataInizioContratto);
//		builder.append(", dataFineContratto=");
//		builder.append(dataFineContratto);
		builder.append(", documento=");
		builder.append(documento);
		builder.append("]");
		return builder.toString();
	}

}

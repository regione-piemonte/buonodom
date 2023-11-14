/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelContratto {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private Integer id = null;
	private String tipo = null;
	private ModelPersonaSintesi intestatario = null;
	private String relazioneDestinatario = null;
	private ModelPersonaSintesi assistenteFamiliare = null;
	private String pivaAssitenteFamiliare = null;
	private String tipoSupportoFamiliare = null;
	private Date dataInizio = null;
	private Date dataFine = null;
	private ModelContrattoAgenzia agenzia = null;
	private Boolean incompatibilitaPerContratto = null;

	/**
	 * id per riferimento (non presente nel body della POST e nella domanda)
	 **/

	@JsonProperty("id")

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * tipologia di contratto (codice ottenuto da servizio decodifiche)
	 **/

	@JsonProperty("tipo")

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 **/

	@JsonProperty("intestatario")

	public ModelPersonaSintesi getIntestatario() {
		return intestatario;
	}

	public void setIntestatario(ModelPersonaSintesi intestatario) {
		this.intestatario = intestatario;
	}

	/**
	 * eventuale relazione tra destinatario ed intestatario del contratto (codice
	 * ottenuto da servizio decodifiche) può assumere uno dei seguenti valori: -
	 * POTESTA_GENITORIALE - NUCLEO_FAMILIARE - CONIUGE - PARENTE_PRIMO_GRADO -
	 * TUTELA - CURATELA - AMMINISTRATORE_SOSTEGNO - ALTRO
	 **/

	@JsonProperty("relazione_destinatario")

	public String getRelazioneDestinatario() {
		return relazioneDestinatario;
	}

	public void setRelazioneDestinatario(String relazioneDestinatario) {
		this.relazioneDestinatario = relazioneDestinatario;
	}

	/**
	 **/

	@JsonProperty("assistente_familiare")

	public ModelPersonaSintesi getAssistenteFamiliare() {
		return assistenteFamiliare;
	}

	public void setAssistenteFamiliare(ModelPersonaSintesi assistenteFamiliare) {
		this.assistenteFamiliare = assistenteFamiliare;
	}

	/**
	 * presente solo per tipo di contratto partita iva
	 **/

	@JsonProperty("piva_assitente_familiare")

	public String getPivaAssitenteFamiliare() {
		return pivaAssitenteFamiliare;
	}

	public void setPivaAssitenteFamiliare(String pivaAssitenteFamiliare) {
		this.pivaAssitenteFamiliare = pivaAssitenteFamiliare;
	}

	/**
	 * ASSISTENTE_FAMILIARE oppure EDUCATORE_PROFESSIONALE
	 **/

	@JsonProperty("tipo_supporto_familiare")

	public String getTipoSupportoFamiliare() {
		return tipoSupportoFamiliare;
	}

	public void setTipoSupportoFamiliare(String tipoSupportoFamiliare) {
		this.tipoSupportoFamiliare = tipoSupportoFamiliare;
	}

	/**
	 * dal frontend viene popolato come YYYY-MM-DD non presente solo quando
	 * incompatibilita_per_contratto è true
	 **/

	@JsonProperty("data_inizio")

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	/**
	 * dal frontend viene popolato come YYYY-MM-DD non presente solo quando
	 * incompatibilita_per_contratto è true
	 **/

	@JsonProperty("data_fine")

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	/**
	 **/

	@JsonProperty("agenzia")

	public ModelContrattoAgenzia getAgenzia() {
		return agenzia;
	}

	public void setAgenzia(ModelContrattoAgenzia agenzia) {
		this.agenzia = agenzia;
	}

	/**
	 * true nessun contratto in essere (ma impegno ad attivarlo) false il contratto
	 * esiste
	 **/

	@JsonProperty("incompatibilita_per_contratto")

	public Boolean isIncompatibilitaPerContratto() {
		return incompatibilitaPerContratto;
	}

	public void setIncompatibilitaPerContratto(Boolean incompatibilitaPerContratto) {
		this.incompatibilitaPerContratto = incompatibilitaPerContratto;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelContratto modelContratto = (ModelContratto) o;
		return Objects.equals(id, modelContratto.id) && Objects.equals(tipo, modelContratto.tipo)
				&& Objects.equals(intestatario, modelContratto.intestatario)
				&& Objects.equals(relazioneDestinatario, modelContratto.relazioneDestinatario)
				&& Objects.equals(assistenteFamiliare, modelContratto.assistenteFamiliare)
				&& Objects.equals(pivaAssitenteFamiliare, modelContratto.pivaAssitenteFamiliare)
				&& Objects.equals(tipoSupportoFamiliare, modelContratto.tipoSupportoFamiliare)
				&& Objects.equals(dataInizio, modelContratto.dataInizio)
				&& Objects.equals(dataFine, modelContratto.dataFine) && Objects.equals(agenzia, modelContratto.agenzia)
				&& Objects.equals(incompatibilitaPerContratto, modelContratto.incompatibilitaPerContratto);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tipo, intestatario, relazioneDestinatario, assistenteFamiliare, pivaAssitenteFamiliare,
				tipoSupportoFamiliare, dataInizio, dataFine, agenzia, incompatibilitaPerContratto);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelContratto {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
		sb.append("    intestatario: ").append(toIndentedString(intestatario)).append("\n");
		sb.append("    relazioneDestinatario: ").append(toIndentedString(relazioneDestinatario)).append("\n");
		sb.append("    assistenteFamiliare: ").append(toIndentedString(assistenteFamiliare)).append("\n");
		sb.append("    pivaAssitenteFamiliare: ").append(toIndentedString(pivaAssitenteFamiliare)).append("\n");
		sb.append("    tipoSupportoFamiliare: ").append(toIndentedString(tipoSupportoFamiliare)).append("\n");
		sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
		sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
		sb.append("    agenzia: ").append(toIndentedString(agenzia)).append("\n");
		sb.append("    incompatibilitaPerContratto: ").append(toIndentedString(incompatibilitaPerContratto))
				.append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

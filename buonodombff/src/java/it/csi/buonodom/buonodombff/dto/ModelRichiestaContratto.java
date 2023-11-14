/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.sql.Timestamp;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelRichiestaContratto {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String tipo = null;
	private ModelPersonaSintesi intestatario = null;
	private String relazioneDestinatario = null;
	private ModelPersonaSintesi assistenteFamiliare = null;
	private Timestamp dataInizio = null;
	private Timestamp dataFine = null;
	private ModelRichiestaContrattoAgenzia agenzia = null;
	private Boolean incompatibilitaPerContratto = null;
	private String pivaAssitenteFamiliare = null;
	private String tipoSupportoFamiliare = null;

	/**
	 * tipologia di contratto (codice ottenuto da servizio decodifiche) NULL nel
	 * caso in cui incompatibilita_per_contratto &#x3D;&#x3D; true
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
	 * ottenuto da servizio decodifiche)
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
	 * non presente solo quando incompatibilita_per_contratto è true
	 **/
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

	/**/

	@JsonProperty("data_inizio")

	public Timestamp getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Timestamp dataInizio) {
		this.dataInizio = dataInizio;
	}

	/**
	 * non presente solo quando incompatibilita_per_contratto è true
	 **/

	@JsonProperty("data_fine")

	public Timestamp getDataFine() {
		return dataFine;
	}

	public void setDataFine(Timestamp dataFine) {
		this.dataFine = dataFine;
	}

	/**
	 **/

	@JsonProperty("agenzia")

	public ModelRichiestaContrattoAgenzia getAgenzia() {
		return agenzia;
	}

	public void setAgenzia(ModelRichiestaContrattoAgenzia agenzia) {
		this.agenzia = agenzia;
	}

	/**
	 * true nessun contratto in essere (ma impegno ad attivarlo) false il contratto
	 * esiste ed è indicata la tipologia nel campo \&quot;tipo\&quot;
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
		ModelRichiestaContratto modelRichiestaContratto = (ModelRichiestaContratto) o;
		return Objects.equals(tipo, modelRichiestaContratto.tipo)
				&& Objects.equals(intestatario, modelRichiestaContratto.intestatario)
				&& Objects.equals(relazioneDestinatario, modelRichiestaContratto.relazioneDestinatario)
				&& Objects.equals(assistenteFamiliare, modelRichiestaContratto.assistenteFamiliare)
				&& Objects.equals(pivaAssitenteFamiliare, modelRichiestaContratto.pivaAssitenteFamiliare)
				&& Objects.equals(tipoSupportoFamiliare, modelRichiestaContratto.tipoSupportoFamiliare)
				&& Objects.equals(dataInizio, modelRichiestaContratto.dataInizio)
				&& Objects.equals(dataFine, modelRichiestaContratto.dataFine)
				&& Objects.equals(agenzia, modelRichiestaContratto.agenzia)
				&& Objects.equals(incompatibilitaPerContratto, modelRichiestaContratto.incompatibilitaPerContratto);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tipo, intestatario, relazioneDestinatario, assistenteFamiliare, dataInizio, dataFine,
				agenzia, incompatibilitaPerContratto, assistenteFamiliare, pivaAssitenteFamiliare);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelRichiestaContratto {\n");

		sb.append("    tipo: ").append(toIndentedString(tipo)).append("\n");
		sb.append("    intestatario: ").append(toIndentedString(intestatario)).append("\n");
		sb.append("    relazioneDestinatario: ").append(toIndentedString(relazioneDestinatario)).append("\n");
		sb.append("    assistenteFamiliare: ").append(toIndentedString(assistenteFamiliare)).append("\n");
		sb.append("    dataInzio: ").append(toIndentedString(dataInizio)).append("\n");
		sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
		sb.append("    agenzia: ").append(toIndentedString(agenzia)).append("\n");
		sb.append("    incompatibilitaPerContratto: ").append(toIndentedString(incompatibilitaPerContratto))
				.append("\n");
		sb.append("    pivaAssitenteFamiliare: ").append(toIndentedString(pivaAssitenteFamiliare)).append("\n");
		sb.append("    tipoSupportoFamiliare: ").append(toIndentedString(tipoSupportoFamiliare)).append("\n");
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

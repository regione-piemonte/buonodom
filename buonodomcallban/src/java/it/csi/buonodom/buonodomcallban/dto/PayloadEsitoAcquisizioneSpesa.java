/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadEsitoAcquisizioneSpesa {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private ModelEsitoAcquisizione esitoAcquisizione = null;
	private List<ModelDocumentoRicevuto> documenti = new ArrayList<ModelDocumentoRicevuto>();
	private String idDichiarazioneSpesaBandi = null;

	/**
	 **/

	@JsonProperty("esito_acquisizione")

	public ModelEsitoAcquisizione getEsitoAcquisizione() {
		return esitoAcquisizione;
	}

	public void setEsitoAcquisizione(ModelEsitoAcquisizione esitoAcquisizione) {
		this.esitoAcquisizione = esitoAcquisizione;
	}

	/**
	 **/

	@JsonProperty("documenti")

	public List<ModelDocumentoRicevuto> getDocumenti() {
		return documenti;
	}

	public void setDocumenti(List<ModelDocumentoRicevuto> documenti) {
		this.documenti = documenti;
	}

	/**
	 * id della dichiarazione spesa generato dal sistema bandi
	 **/

	@JsonProperty("id_dichiarazione_spesa_bandi")

	public String getIdDichiarazioneSpesaBandi() {
		return idDichiarazioneSpesaBandi;
	}

	public void setIdDichiarazioneSpesaBandi(String idDichiarazioneSpesaBandi) {
		this.idDichiarazioneSpesaBandi = idDichiarazioneSpesaBandi;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PayloadEsitoAcquisizioneSpesa payloadEsitoAcquisizioneSpesa = (PayloadEsitoAcquisizioneSpesa) o;
		return Objects.equals(esitoAcquisizione, payloadEsitoAcquisizioneSpesa.esitoAcquisizione)
				&& Objects.equals(documenti, payloadEsitoAcquisizioneSpesa.documenti)
				&& Objects.equals(idDichiarazioneSpesaBandi, payloadEsitoAcquisizioneSpesa.idDichiarazioneSpesaBandi);
	}

	@Override
	public int hashCode() {
		return Objects.hash(esitoAcquisizione, documenti, idDichiarazioneSpesaBandi);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PayloadEsitoAcquisizioneSpesa {\n");

		sb.append("    esitoAcquisizione: ").append(toIndentedString(esitoAcquisizione)).append("\n");
		sb.append("    documenti: ").append(toIndentedString(documenti)).append("\n");
		sb.append("    idDichiarazioneSpesaBandi: ").append(toIndentedString(idDichiarazioneSpesaBandi)).append("\n");
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

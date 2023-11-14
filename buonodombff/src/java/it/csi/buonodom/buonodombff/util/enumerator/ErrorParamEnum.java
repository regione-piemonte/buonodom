/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.util.enumerator;

public enum ErrorParamEnum {

	X_REQUEST_ID("x-Request-Id"), SHIB_IDENTITA_CODICEFISCALE("Shib-Identita-CodiceFiscale"),
	X_FORWARDED_FOR("x-Forwarded-for"), X_CODICE_SERVIZIO("x-Codice-Servizio"),
	X_CODICE_VERTICALE("x-Codice-Verticale"), CF("CF"), CF_RICHIEDENTE("CF Richiedente"),
	CF_DESTINATARIO("CF Destinatario"), CF_ASSISTENTE("CF Assistente Familiare"), CF_DATORE("CF Datore di Lavoro"),
	CF_AGENZIA("CF Agenzia"), NOME_RICHIEDENTE("Nome Richiedente"), NOME_DESTINATARIO("Nome Destinatario"),
	NOME_ASSISTENTE("Nome Assistente Familiare"), NOME_DATORE("Nome Datore di Lavoro"),
	COGNOME_RICHIEDENTE("Cognome Richiedente"), COGNOME_DESTINATARIO("Cognome Destinatario"),
	COGNOME_ASSISTENTE("Cognome Assistente Familiare"), COGNOME_DATORE("Cognome Datore di Lavoro"),
	DATA_NASCITA_RICHIEDENTE("Data di Nascita Richiedente"), DATA_NASCITA_DESTINATARIO("Data di Nascita Destinatario"),
	DATA_NASCITA_ASSISTENTE("Data di Nascita Assistente Familiare"),
	DATA_NASCITA_DATORE("Data di Nascita Datore di Lavoro"), STATO_NASCITA_RICHIEDENTE("Stato di Nascita Richiedente"),
	STATO_NASCITA_DESTINATARIO("Stato di Nascita Destinatario"),
	STATO_NASCITA_ASSISTENTE("Stato di Nascita Assistente Familiare"),
	STATO_NASCITA_DATORE("Stato di Nascita Datore di Lavoro"),
	COMUNE_NASCITA_RICHIEDENTE("Comune di Nascita Richiedente"),
	COMUNE_NASCITA_DESTINATARIO("Comune di Nascita Destinatario"),
	COMUNE_NASCITA_ASSISTENTE("Comune di Nascita Assistente Familiare"),
	COMUNE_NASCITA_DATORE("Comune di Nascita Datore di Lavoro"),
	PROVINCIA_NASCITA_RICHIEDENTE("Provincia di Nascita Richiedente"),
	PROVINCIA_NASCITA_DESTINATARIO("Provincia di Nascita Destinatario"),
	PROVINCIA_NASCITA_ASSISTENTE("Provincia di Nascita Assistente Familiare"),
	PROVINCIA_NASCITA_DATORE("Provincia di Nascita Datore di Lavoro"),
	INDIRIZZO_RESIDENZA_RICHIEDENTE("Indirizzo di Residenza Richiedente"),
	INDIRIZZO_RESIDENZA_DESTINATARIO("Indirizzo di Residenza Destinatario"),
	INDIRIZZO_RESIDENZA_ASSISTENTE("Indirizzo di Residenza Assistente Familiare"),
	INDIRIZZO_RESIDENZA_DATORE("Indirizzo di Residenza Datore di Lavoro"),
	COMUNE_RESIDENZA_RICHIEDENTE("Comune di Residenza Richiedente"),
	COMUNE_RESIDENZA_DESTINATARIO("Comune di Residenza Destinatario"),
	COMUNE_RESIDENZA_ASSISTENTE("Comune di Residenza Assistente Familiare"),
	COMUNE_RESIDENZA_DATORE("Comune di Residenza Datore di Lavoro"),
	PROVINCIA_RESIDENZA_RICHIEDENTE("Provincia di Residenza Richiedente"),
	PROVINCIA_RESIDENZA_DESTINATARIO("Provincia di Residenza Destinatario"),
	PROVINCIA_RESIDENZA_ASSISTENTE("Provincia di Residenza Assistente Familiare"),
	PROVINCIA_RESIDENZA_DATORE("Provincia di Residenza Datore di Lavoro"), PUNTEGGIO_SOCIALE("Punteggio Sociale"),
	INTESTATARIO_IBAN("Intestatario IBAN"), IBAN("IBAN"), NOTA_CONTRODEDUZIONE("Nota Controdeduzione"),
	STATO_DOMANDA("Stato Domanda"), TIPO_CONTRIBUTO("Tipo Contributo"),
	TITOLO_STUDIO_DESTINATARIO("Titolo Studio Destinatario"), TIPO_RAPPORTO("Tipo Rapporto"),
	TIPO_RELAZIONE("Tipo Relazione"), VALUTAZIONE_MULTIDIMENSIONALE("Valutazione Dimensionale"),
	TIPO_CONTRATTO("Tipo Contratto"), ASL_DESTINATARIO("Asl Destinatario"),
	TIPO_SUPPORTO_FAMILIARE("Tipo Supporto Familiare"), SPORTELLO_ATTIVO("Sportello Attivo"),
	LUNGHEZZA_IBAN("Lunghezza IBAN errata"), SPORTELLO_CHIUSO("Sportello Chiuso");

	private final String code;

	ErrorParamEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}

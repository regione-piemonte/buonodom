/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.util.enumerator;

public enum ErrorParamEnum {

	X_REQUEST_ID("x-Request-Id"), SHIB_IDENTITA_CODICEFISCALE("Shib-Identita-CodiceFiscale"),
	X_FORWARDED_FOR("x-Forwarded-for"), X_CODICE_SERVIZIO("x-Codice-Servizio"),
	X_CODICE_VERTICALE("x-Codice-Verticale"), PIVA("Partita iva"), VALORE_ISEE("Valore Isee"), ISEE("Isee"),
	SCADENZA_ISEE("Data scadenza Isee"), RILASCIO_ISEE("Data rilascio"), VERIFICA_CONFORME("Conformita'"),
	VERIFICA_NOTE("Nota ente gestore");

	private final String code;

	ErrorParamEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}

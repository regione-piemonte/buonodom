/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.util.enumerator;

public enum ErrorParamEnum {

	MESSAGGE_UUID("Messaggio UUID"), X_REQUEST_ID("x-Request-Id"), CODICE_ERRORE("Codice Errore"), ERRORE("Errore"),
	MESSAGGIO_ERRORE("Messaggio Errore"), ESITO_OK_KO("Esito Operazione"),
	ESITOACQUISIZIONE("Esito Acquisizione Domanda manca"), MANCA_ESITO("Esito diverso da OK o KO"),
	IDDICHIARAZIONESPESABANDI("Dichiarazione di spesa bandi manca"),
	IDDOCUMENTOSPESABANDI("Documento di spesa bandi manca"),
	IDDOCUMENTOSPESABUONODOM("Documento di spesa buonodom manca"),
	DOCUMENTOSPESABUONODOM("Documento di spesa buonodom presenti"), DOCUMENTOSPESA("Documenti di spesa mancano"),
	ESITO("Esito manca");

	private final String code;

	ErrorParamEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}

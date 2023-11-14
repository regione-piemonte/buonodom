/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomstarda.util.enumerator;

public enum ErrorParamEnum {

	MESSAGGE_UUID("Messaggio UUID"), IDDOCUMENTOFRUITORE("Id Documento Fruitore"),
	ESITOSMISTA("Esito Smista Documento"), ESITO("Esito");

	private final String code;

	ErrorParamEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}

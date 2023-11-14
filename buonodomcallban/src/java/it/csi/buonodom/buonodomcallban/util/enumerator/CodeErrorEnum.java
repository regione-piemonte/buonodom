/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.util.enumerator;

public enum CodeErrorEnum {
	ERR01("ERR01"), ERR03("ERR03"), ERR02("ERR02"), ERRSTARD01("ERRSTARD01"), ERR16("ERR16"), ERR17("ERR17");

	private final String code;

	CodeErrorEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}

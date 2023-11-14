/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.util.enumerator;

public enum StatusEnum {

	BAD_REQUEST(400, "BAD_REQUEST", "BAD_REQUEST TITLE"), NOT_FOUND(404, "NOT FOUND", "NOT FOUND TITLE"),
	SERVER_ERROR(500, "SERVER_ERROR", "SERVER_ERROR TITLE"),
	SERVER_ERROR_INTERROGAMEF(500, "SERVER_ERROR INTERROGAMEF", "SERVER_ERROR INTERROGAMEF TITLE"),
	SERVER_ERROR_FATAL(500, "SERVER_ERROR_FATAL", "SERVER_ERROR FATAL"), FORBIDDEN(403, "FORBIDDEN", "FORBIDDEN"),
	CONFLICT(409, "CONFLICT", "CONFLICT"),
	UNPROCESSABLE_ENTITY(422, "UNPROCESSABLE ENTITY", "UNPROCESSABLE ENTITY TITLE");

	private final Integer status;
	private final String code;
	private final String title;

	StatusEnum(Integer status, String code, String title) {
		this.status = status;
		this.code = code;
		this.title = title;

	}

	public String getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

	public Integer getStatus() {
		return status;
	}
}

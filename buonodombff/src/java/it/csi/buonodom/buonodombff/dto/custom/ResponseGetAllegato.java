/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto.custom;

public class ResponseGetAllegato {

	private byte[] allegato;
	private String fileName;

	public byte[] getAllegato() {
		return allegato;
	}

	public void setAllegato(byte[] allegato) {
		this.allegato = allegato;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}

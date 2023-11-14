/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.dto.custom;

import it.csi.buonodom.buonodombo.dto.ErroreDettaglio;

public class ErroreDettaglioExt extends ErroreDettaglio {
	private Integer erroreId;

	public Integer getErroreId() {
		return erroreId;
	}

	public void setErroreId(Integer erroreId) {
		this.erroreId = erroreId;
	}

	public ErroreDettaglio wrap() {
		ErroreDettaglio erroreDettaglio = new ErroreDettaglio();
		erroreDettaglio.setChiave(this.getChiave());
		erroreDettaglio.setValore(this.getValore());
		return erroreDettaglio;

	}

	@Override
	public String toString() {
		return "ErroreDettaglioExt [erroreId=" + erroreId + "]";
	}

}

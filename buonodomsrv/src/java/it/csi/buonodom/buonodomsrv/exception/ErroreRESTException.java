/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.exception;

import javax.ws.rs.core.MediaType;

import it.csi.buonodom.buonodomsrv.dto.Errore;

public class ErroreRESTException extends RESTException {
	public ErroreRESTException(Errore errore, String message) {
		super(errore.getStatus(), MediaType.APPLICATION_JSON_TYPE, errore, message);
	}

	public ErroreRESTException(Errore errore) {
		super(errore.getStatus(), MediaType.APPLICATION_JSON_TYPE, errore, errore.toString());
	}
}

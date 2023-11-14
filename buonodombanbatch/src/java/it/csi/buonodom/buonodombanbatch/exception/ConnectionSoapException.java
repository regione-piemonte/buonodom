/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombanbatch.exception;

public class ConnectionSoapException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7946929978410044787L;

	public ConnectionSoapException(String s) {
		super(s);
	}

	public ConnectionSoapException() {
		super();
	}
}

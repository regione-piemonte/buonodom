/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/

package it.csi.buonodom.buonodombo.external.ateco;

import javax.xml.ws.WebFault;

/**
 * This class was generated by Apache CXF 3.5.3 2023-02-10T18:39:40.752+01:00
 * Generated source version: 3.5.3
 */

@WebFault(name = "SystemException", targetNamespace = "http://business.aaeporch.aaep.csi.it/")
public class SystemException_Exception extends Exception {

	private it.csi.buonodom.buonodombo.external.ateco.SystemException faultInfo;

	public SystemException_Exception() {
		super();
	}

	public SystemException_Exception(String message) {
		super(message);
	}

	public SystemException_Exception(String message, java.lang.Throwable cause) {
		super(message, cause);
	}

	public SystemException_Exception(String message,
			it.csi.buonodom.buonodombo.external.ateco.SystemException systemException) {
		super(message);
		this.faultInfo = systemException;
	}

	public SystemException_Exception(String message,
			it.csi.buonodom.buonodombo.external.ateco.SystemException systemException, java.lang.Throwable cause) {
		super(message, cause);
		this.faultInfo = systemException;
	}

	public it.csi.buonodom.buonodombo.external.ateco.SystemException getFaultInfo() {
		return this.faultInfo;
	}
}

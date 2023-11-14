/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.external.ateco;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by Apache CXF 3.5.3 2023-02-10T18:39:40.799+01:00
 * Generated source version: 3.5.3
 *
 */
@WebServiceClient(name = "OrchestratoreImplService", wsdlLocation = "http://tst-xxx.xxx.piemonte.it/aaeporch/OrchestratoreService?wsdl", targetNamespace = "http://business.aaeporch.aaep.csi.it/")
public class OrchestratoreImplService extends Service {

	public final static URL WSDL_LOCATION;

	public final static QName SERVICE = new QName("http://business.aaeporch.aaep.csi.it/", "OrchestratoreImplService");
	public final static QName OrchestratoreImplPort = new QName("http://business.aaeporch.aaep.csi.it/",
			"OrchestratoreImplPort");
	static {
		URL url = null;
		try {
			url = new URL("http://tst-xxx.xxx.piemonte.it/aaeporch/OrchestratoreService?wsdl");
		} catch (MalformedURLException e) {
			java.util.logging.Logger.getLogger(OrchestratoreImplService.class.getName()).log(
					java.util.logging.Level.INFO, "Can not initialize the default wsdl from {0}",
					"http://tst-xxx.xxx.piemonte.it/aaeporch/OrchestratoreService?wsdl");
		}
		WSDL_LOCATION = url;
	}

	public OrchestratoreImplService(URL wsdlLocation) {
		super(wsdlLocation, SERVICE);
	}

	public OrchestratoreImplService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public OrchestratoreImplService() {
		super(WSDL_LOCATION, SERVICE);
	}

	public OrchestratoreImplService(WebServiceFeature... features) {
		super(WSDL_LOCATION, SERVICE, features);
	}

	public OrchestratoreImplService(URL wsdlLocation, WebServiceFeature... features) {
		super(wsdlLocation, SERVICE, features);
	}

	public OrchestratoreImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
		super(wsdlLocation, serviceName, features);
	}

	/**
	 *
	 * @return returns OrchestratoreIntf
	 */
	@WebEndpoint(name = "OrchestratoreImplPort")
	public OrchestratoreIntf getOrchestratoreImplPort() {
		return super.getPort(OrchestratoreImplPort, OrchestratoreIntf.class);
	}

	/**
	 *
	 * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure
	 *                 on the proxy. Supported features not in the
	 *                 <code>features</code> parameter will have their default
	 *                 values.
	 * @return returns OrchestratoreIntf
	 */
	@WebEndpoint(name = "OrchestratoreImplPort")
	public OrchestratoreIntf getOrchestratoreImplPort(WebServiceFeature... features) {
		return super.getPort(OrchestratoreImplPort, OrchestratoreIntf.class, features);
	}

}

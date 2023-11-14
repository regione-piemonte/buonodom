/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration;

import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.cxf.frontend.ClientProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombo.external.ateco.CSIException_Exception;
import it.csi.buonodom.buonodombo.external.ateco.Impresa;
import it.csi.buonodom.buonodombo.external.ateco.OrchestratoreImplService;
import it.csi.buonodom.buonodombo.external.ateco.OrchestratoreIntf;
import it.csi.buonodom.buonodombo.external.ateco.Utente;
import it.csi.buonodom.buonodombo.util.Constants;
import it.csi.buonodom.buonodombo.util.LoggerUtil;

@Service
public class GetDettaglioImpresaService extends LoggerUtil {

	public final static QName SERVICE_NAME_GET_IMPRESA = new QName("http://business.aaeporch.aaep.csi.it/",
			"OrchestratoreImplService");

	@Value("${atecoUrl}")
	private String atecoUrl;

	public Impresa dettaglioImpresa(String pivaImpresa) {

		URL wsdlURL;

		try {
			String consensiServiceUrl = atecoUrl + "?wsdl";
			wsdlURL = new URL(consensiServiceUrl);
			OrchestratoreImplService cs = new OrchestratoreImplService(wsdlURL, SERVICE_NAME_GET_IMPRESA);
			OrchestratoreIntf port = cs.getOrchestratoreImplPort();

			org.apache.cxf.endpoint.Client client = ClientProxy.getClient(port);
			org.apache.cxf.endpoint.Endpoint cxfEndpoint = client.getEndpoint();

			Utente utente = new Utente();
			Impresa atecoResponse = port.getDettaglioImpresa(utente, Constants.AAEP, pivaImpresa);

			return atecoResponse;

		} catch (CSIException_Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Errore chiamata servizio : " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Errore chiamata servizio : " + e.getMessage());
		}
	}

}

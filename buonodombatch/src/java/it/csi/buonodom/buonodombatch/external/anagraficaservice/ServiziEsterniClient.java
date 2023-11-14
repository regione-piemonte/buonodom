/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombatch.external.anagraficaservice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.Handler;

import it.csi.buonodom.buonodombatch.configuration.Configuration;
import it.csi.buonodom.buonodombatch.dao.BuonodomBatchDAO;

public class ServiziEsterniClient {

	private String usernameInterroga = Configuration.get("interrogaMefUserBe");
	private String passwordInterroga = Configuration.get("interrogaMefPassBe");
	private String urlInterroga = Configuration.get("interrogaMefServiceUrl");

	private static ServiziEsterniClient instance;

	private ServiziEsterniClient() {
	}

	public static ServiziEsterniClient getInstance(BuonodomBatchDAO dao) {
		if (instance == null) {
			instance = new ServiziEsterniClient();
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public static ServiziEsterniClient getInstance() {
		return instance;
	}

	public InterrogaMefEsenredd1Soap creaServizio() {
		String endpoint = this.getUrlInterroga();

		InterrogaMefEsenredd1 service = new InterrogaMefEsenredd1();
		InterrogaMefEsenredd1Soap port = service.getInterrogaMefEsenredd1Soap();

		setWSSecurity((BindingProvider) port);
		return port;
	}

	public InterrogaMefEsenreddRes chiamaGetInterrogaMef(String codicefiscale, InterrogaMefEsenredd1Soap port)
			throws Exception {

		InterrogaMefEsenreddRes dati = null;

		try {

			System.out.println("--- Request getInterroga CF " + codicefiscale + " ---");
			dati = port.interrogaMefEsenredd(codicefiscale);

			System.out.println("--- Response getInterroga ---");
			JAXBContext jaxRes = JAXBContext.newInstance(InterrogaMefEsenreddRes.class);
			Marshaller jaxMres = jaxRes.createMarshaller();
			jaxMres.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxMres.marshal(dati, System.out);
			System.out.println("---------------------------\n");

		} catch (WebServiceException ws) {
			ws.printStackTrace();
			throw ws;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return dati;

	}

	private void setWSSecurity(BindingProvider prov) {
		prov.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.getUrlInterroga());

		if (this.usernameInterroga != null && this.passwordInterroga != null) {
			List<Handler> handlerChain = new ArrayList<Handler>();
			handlerChain.add(new WSSecurityHeaderSOAPHandler(this.usernameInterroga, this.passwordInterroga));
			prov.getBinding().setHandlerChain(handlerChain);
		}
	}

	public String getUsernameInterroga() {
		return usernameInterroga;
	}

	public void setUsernameInterroga(String usernameInterroga) {
		this.usernameInterroga = usernameInterroga;
	}

	public String getPasswordInterroga() {
		return passwordInterroga;
	}

	public void setPasswordInterroga(String passwordInterroga) {
		this.passwordInterroga = passwordInterroga;
	}

	public String getUrlInterroga() {
		return urlInterroga;
	}

	public void setUrlInterroga(String urlInterroga) {
		this.urlInterroga = urlInterroga;
	}

}

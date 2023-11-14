/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.business;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.csi.buonodom.buonodomsrv.business.be.impl.CreaDomandaServiceImpl;
import it.csi.buonodom.buonodomsrv.business.be.impl.CreaLetteraServiceImpl;
import it.csi.buonodom.buonodomsrv.business.be.impl.NotificheApiServiceImpl;
import it.csi.buonodom.buonodomsrv.business.be.impl.RecuperoErroriServiceImpl;
import it.csi.buonodom.buonodomsrv.business.be.impl.ServizioAttivoServiceImpl;
import it.csi.buonodom.buonodomsrv.business.be.impl.SmistaDocumentoPartenzaServiceImpl;
import it.csi.buonodom.buonodomsrv.business.be.impl.SmistaDocumentoServiceImpl;

@ApplicationPath("api/v1")
public class BuonodomRestApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public BuonodomRestApplication() {

		final String METHOD_NAME = "RestApplication";

		singletons.add(new ServizioAttivoServiceImpl());
		singletons.add(new CreaDomandaServiceImpl());
		singletons.add(new NotificheApiServiceImpl());
		singletons.add(new CreaLetteraServiceImpl());
		singletons.add(new SmistaDocumentoServiceImpl());
		singletons.add(new SmistaDocumentoPartenzaServiceImpl());
		singletons.add(new RecuperoErroriServiceImpl());
	}

	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}

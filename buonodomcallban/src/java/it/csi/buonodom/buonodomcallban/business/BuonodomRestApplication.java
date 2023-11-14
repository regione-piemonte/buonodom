/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.business;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.csi.buonodom.buonodomcallban.business.be.impl.DomandaApiServiceImpl;
import it.csi.buonodom.buonodomcallban.business.be.impl.ServizioAttivoServiceImpl;

@ApplicationPath("api/v1")
public class BuonodomRestApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public BuonodomRestApplication() {

		final String METHOD_NAME = "RestApplication";

		singletons.add(new ServizioAttivoServiceImpl());
		singletons.add(new DomandaApiServiceImpl());
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

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.csi.buonodom.buonodombff.business.be.impl.AllegatoApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.AllegatoBuonoApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.AnagraficaApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.ContattiApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.ContrattiApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.CronologiaApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.DecodificheApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.DocumentoSpesaApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.FilesApiServiceeImpl;
import it.csi.buonodom.buonodombff.business.be.impl.FornitoriApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.PeriodoRendicontazioneApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.PreferenzeApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.RendicontazioneApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.RichiesteApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.ServizioAttivoApiServiceImpl;
import it.csi.buonodom.buonodombff.business.be.impl.SportelliApiServiceImpl;

@ApplicationPath("api/v1")
public class BuonodomRestApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public BuonodomRestApplication() {

		final String METHOD_NAME = "RestApplication";

		singletons.add(new ServizioAttivoApiServiceImpl());
		singletons.add(new FilesApiServiceeImpl());
		singletons.add(new RichiesteApiServiceImpl());
		singletons.add(new AllegatoApiServiceImpl());
		singletons.add(new AnagraficaApiServiceImpl());
		singletons.add(new CronologiaApiServiceImpl());
		singletons.add(new SportelliApiServiceImpl());
		singletons.add(new DecodificheApiServiceImpl());
		singletons.add(new ContattiApiServiceImpl());
		singletons.add(new PreferenzeApiServiceImpl());
		singletons.add(new FornitoriApiServiceImpl());
		singletons.add(new ContrattiApiServiceImpl());
		singletons.add(new AllegatoBuonoApiServiceImpl());
		singletons.add(new RendicontazioneApiServiceImpl());
		singletons.add(new DocumentoSpesaApiServiceImpl());
		singletons.add(new PeriodoRendicontazioneApiServiceImpl());
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

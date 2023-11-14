/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.business.be.service;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombandisrv.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombandisrv.integration.rest.BandiRestBaseService;
import it.csi.buonodom.buonodombandisrv.integration.rest.BuonodomRestBaseService;
import it.csi.buonodom.buonodombandisrv.util.rest.ResponseRest;

@Service
public class ServizioRestService extends BaseService {
	@Value("${buonodomsrvurl}")
	private String buonodomsrvurl;

	@Value("${bandiacquisizionedomandeurl}")
	private String bandiacquisizionedomandeurl;

	@Autowired
	BandiRestBaseService restbaseBandi;

	@Autowired
	BuonodomRestBaseService restbaseBuono;

	public ResponseRest getContatti(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			String os = System.getProperty("os.name");
			if (os.toLowerCase().contains("win")) {
				response = restbaseBuono.eseguiGet(httpHeaders,
						"http://localhost:8080/buonodomsrv/api/v1/notifiche/contatti", true, null);
			} else {
				response = restbaseBuono.eseguiGet(httpHeaders, buonodomsrvurl + "/notifiche/contatti", true, null);
			}
			log.info(metodo + " - Fine ");
		} catch (Exception e) {
			log.error(metodo + " - errore " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest postAcquisizioneDomande(String numeroRichiesta, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, String keynome, String valuenome, File xml) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			response = restbaseBandi
					.eseguiPostMultipart(
							httpHeaders, bandiacquisizionedomandeurl + "acquisizioneDomande/getDomandeConcesse"
									+ "?numeroDomanda=" + numeroRichiesta + "&codiceBando=DOM",
							keynome, valuenome, xml, true);
			log.info(metodo + " - Fine ");
		} catch (Exception e) {
			log.error(metodo + " - errore " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest postInviaRendicontazioneDomande(String numeroRichiesta, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest, String keynomefile, String keynometext,
			String json, String valuenome, File zip) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			response = restbaseBandi.eseguiPostMultipartZip(httpHeaders,
					bandiacquisizionedomandeurl + "dichiarazioneSpesa/acquisisciDichiarazioneSpesa", keynomefile,
					keynometext, json, valuenome, zip, true);
			log.info(metodo + " - Fine ");
		} catch (Exception e) {
			log.error(metodo + " - errore " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	public ResponseRest postSetFornitore(String fornitore, String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		ResponseRest response = new ResponseRest();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		log.info(metodo + " - Inizio ");
		try {
			response = restbaseBandi.eseguiPost(httpHeaders,
					bandiacquisizionedomandeurl + "gestioneFornitori/setFornitore", fornitore, true);
			log.info(metodo + " - Fine ");
		} catch (Exception e) {
			log.error(metodo + " - errore " + e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
}

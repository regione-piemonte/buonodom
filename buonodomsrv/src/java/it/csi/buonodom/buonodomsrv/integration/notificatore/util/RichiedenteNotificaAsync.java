/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.integration.notificatore.util;

import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import it.csi.buonodom.buonodomsrv.dto.ModelPersonaSintesi;
import it.csi.buonodom.buonodomsrv.exception.DatabaseException;
import it.csi.buonodom.buonodomsrv.integration.notificatore.NotificatoreService;
import it.csi.buonodom.buonodomsrv.util.LoggerUtil;

@Component
public class RichiedenteNotificaAsync extends LoggerUtil {

	@Autowired
	NotificatoreService notificatoreService;

	@Async
	public HttpResponse<String> notifyAsync(String shibIdentitaCodiceFiscale, ModelPersonaSintesi richiedente,
			String xRequestId, String xCodiceServizio, String tipoNotifica, String destinatario, String numero,
			String motivo, String stato) {

		StringBuffer sb = new StringBuffer(
				" NOTIFICA INVIATA PER " + richiedente.getCf() + "- STATO NOTIFICA " + tipoNotifica);
		HttpResponse<String> response = null;
		try {
			response = notificatoreService.notificaEventoRichiedente(shibIdentitaCodiceFiscale, richiedente, xRequestId,
					tipoNotifica, destinatario, numero, motivo, stato);

			sb.append(" OK");

		} catch (DatabaseException e1) {
			logError("notifyAsync", "Errore riguardante database:", e1.getMessage());
			sb.append("Errore riguardante database:" + e1.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logError("notifyAsync", "Exception invio notifica:" + e.getCause() + " - " + e.getMessage());
			sb.append("Exception invio notifica:" + e.getCause() + " - " + e.getMessage());
		}
		logInfo("execute-notifyAsync", sb.toString());
		return response;
	}

	@Async
	public void notifyContactAsync(String cf, String shibIdentitaCodiceFiscale) {

		StringBuffer sb = new StringBuffer(" NOTIFICA INVIATA PER " + cf);
		try {
			notificatoreService.notificaContact(cf, shibIdentitaCodiceFiscale);

			sb.append(" OK");

		} catch (DatabaseException e1) {
			logError("notifyAsync", "Errore riguardante database:" + e1.getMessage());
			sb.append("Errore riguardante database:" + e1.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logError("notifyAsync", "Exception invio notifica:" + e.getCause() + " - " + e.getMessage());
			sb.append("Exception invio notifica:" + e.getCause() + " - " + e.getMessage());
		}
		logInfo("execute-notifyAsync", sb.toString());
	}

}

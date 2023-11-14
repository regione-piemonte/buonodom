/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.integration.service;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodomsrv.dto.Contact;
import it.csi.buonodom.buonodomsrv.dto.ModelPersonaSintesi;
import it.csi.buonodom.buonodomsrv.dto.ModelRichiesta;
import it.csi.buonodom.buonodomsrv.dto.Preferences;
import it.csi.buonodom.buonodomsrv.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodomsrv.exception.DatabaseException;
import it.csi.buonodom.buonodomsrv.exception.ResponseErrorException;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodomsrv.integration.notificatore.NotificatoreService;
import it.csi.buonodom.buonodomsrv.integration.notificatore.util.RichiedenteNotificaAsync;
import it.csi.buonodom.buonodomsrv.util.Constants;
import it.csi.buonodom.buonodomsrv.util.ErrorBuilder;
import it.csi.buonodom.buonodomsrv.util.LoggerUtil;
import it.csi.buonodom.buonodomsrv.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodomsrv.util.enumerator.HeaderEnum;
import it.csi.buonodom.buonodomsrv.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodomsrv.util.validator.impl.ValidateGenericImpl;

@Service
public class NotificaCittadino extends LoggerUtil {

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	RichiedenteNotificaAsync richiedentenotificaasync;

	@Autowired
	private NotificatoreService notificatore;

	@Autowired
	CodParametroDao parametroDao;

	public Response execute(String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String xCodiceVerticale, String numeroRichiesta, String tipoNotifica,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		ModelRichiesta richiesta = new ModelRichiesta();
		HttpResponse<String> response = null;
		try {

			List<ErroreDettaglioExt> listError = validateGeneric.validateNotifica(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, numeroRichiesta, tipoNotifica, securityContext, httpHeaders,
					httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			if (richiesta != null) {
				ModelPersonaSintesi richiedente = new ModelPersonaSintesi();
				richiedente.setCf(richiesta.getRichiedente().getCf());
				richiedente.setCognome(richiesta.getRichiedente().getCognome());
				richiedente.setNome(richiesta.getRichiedente().getNome());
				richiedente.setComuneNascita(richiesta.getRichiedente().getComuneNascita());
				richiedente.setDataNascita(richiesta.getRichiedente().getDataNascita());
				richiedente.setProvinciaNascita(richiesta.getRichiedente().getProvinciaNascita());
				richiedente.setStatoNascita(richiesta.getRichiedente().getStatoNascita());
				String destinatario = null;
				if (richiesta.getDestinatario() != null) {
					if (richiesta.getDestinatario().getCf() != null) {
						if (!richiesta.getDestinatario().getCf().equalsIgnoreCase(shibIdentitaCodiceFiscale)) {
							destinatario = richiesta.getDestinatario().getNome() + " "
									+ richiesta.getDestinatario().getCognome();
						}
					}
				}
				String parametronotificatore = parametroDao.selectValoreParametroFromCod(Constants.CHIAMA_NOTIFICATORE,
						Constants.PARAMETRO_GENERICO);
				boolean verificasechiamareNotificatore = true;
				if (parametronotificatore != null)
					verificasechiamareNotificatore = parametronotificatore.equalsIgnoreCase("TRUE") ? true : false;
				if (verificasechiamareNotificatore) {
					response = notificaCittadino(richiedente, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor,
							xCodiceServizio, tipoNotifica, destinatario, richiesta.getNumero(), richiesta.getNote(),
							richiesta.getStato());
				}
				if (!(response.statusCode() == 200 || response.statusCode() == 201)) {
					param[0] = "Errore di invio notifica " + response.body() + " per domanda " + richiesta.getNumero();
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice),
							"errore in validate");
				}
				return Response.ok().entity(richiedente).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId).build();
			} else {
				param[0] = "Richiesta non trovata";
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
						"errore in validate");
			}
		}

		catch (DatabaseException e) {
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			logError(metodo, "Errore generico ", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	private HttpResponse<String> notificaCittadino(ModelPersonaSintesi richiedente, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String tipoNotifica, String destinatario,
			String numero, String motivo, String stato) {

		return richiedentenotificaasync.notifyAsync(shibIdentitaCodiceFiscale, richiedente, xRequestId, xCodiceServizio,
				tipoNotifica, destinatario, numero, motivo, stato);

	}

	public Response executeContatti(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		try {

			List<ErroreDettaglioExt> listError = validateGeneric.validateContatti(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			String parametrocontatti = parametroDao.selectValoreParametroFromCod(Constants.CHIAMA_CONTATTI,
					Constants.PARAMETRO_GENERICO);
			boolean verificasechiamarecontatti = true;
			Contact contatto = new Contact();
			if (parametrocontatti != null)
				verificasechiamarecontatti = parametrocontatti.equalsIgnoreCase("TRUE") ? true : false;
			if (verificasechiamarecontatti) {
				contatto = notificatore.notificaContact(shibIdentitaCodiceFiscale, shibIdentitaCodiceFiscale);
			} else {
				contatto.setEmail("provamail@testcarico.it");
				contatto.setSms("cellcarico");
				contatto.setPhone("telcarico");
				contatto.setUserId(shibIdentitaCodiceFiscale);
			}
			return Response.ok().entity(contatto).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId).build();

		}

		catch (DatabaseException e) {
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			logError(metodo, "Errore generico ", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	public Response executePreferenze(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		try {

			List<ErroreDettaglioExt> listError = validateGeneric.validateContatti(shibIdentitaCodiceFiscale, xRequestId,
					xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			String parametrocontatti = parametroDao.selectValoreParametroFromCod(Constants.CHIAMA_CONTATTI,
					Constants.PARAMETRO_GENERICO);
			boolean verificasechiamarecontatti = true;
			Preferences preferenze = new Preferences();
			if (parametrocontatti != null)
				verificasechiamarecontatti = parametrocontatti.equalsIgnoreCase("TRUE") ? true : false;
			if (verificasechiamarecontatti) {
				preferenze = notificatore.notificaPreferenze(shibIdentitaCodiceFiscale, shibIdentitaCodiceFiscale);
			} else {
				preferenze.setChannels("push,email");
				preferenze.setUuid("152b8fc3-a960-4cde-825b-4b3d2bbb5ed3");
				preferenze.setServiceName("welfdom");
				preferenze.setUserId(shibIdentitaCodiceFiscale);
			}
			return Response.ok().entity(preferenze).header(HeaderEnum.X_REQUEST_ID.getCode(), xRequestId).build();

		}

		catch (DatabaseException e) {
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			logError(metodo, "Errore generico ", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}
}

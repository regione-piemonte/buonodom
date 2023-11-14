/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.dto.ModelAllegato;
import it.csi.buonodom.buonodombff.dto.ModelRichiesta;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelRichiesteExt;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.SportelliDao;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class GetRichiesteService extends BaseService {

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	SportelliDao sportelliDao;

	public Response getRichieste(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		List<ModelRichiesteExt> richieste = new ArrayList<ModelRichiesteExt>();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErroreDettaglioExt errore = new ErroreDettaglioExt();
		String[] param = new String[1];
		try {
			// validate stessa delle decodifica
			List<ErroreDettaglioExt> listError = validateGeneric.validateRichieste(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			richieste = richiesteDao.selectRichieste(shibIdentitaCodiceFiscale);

			return Response.status(200).entity(richieste).build();

		} catch (DatabaseException e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			e.printStackTrace();
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore generico ", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	public Response getNumeroRichiesta(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		ModelRichiesta richiesta = new ModelRichiesta();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErroreDettaglioExt errore = new ErroreDettaglioExt();
		String[] param = new String[1];
		long tempoPartenza = System.currentTimeMillis();
		log.info("richiesteget - BEGIN time: ");

		try {
			// validate stessa delle decodifica
			List<ErroreDettaglioExt> listError = validateGeneric.validateRichieste(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);

			long tempoValidate = System.currentTimeMillis();

			log.info("richiesteget - tempoValidete: " + System.currentTimeMillis() + " millis: "
					+ (tempoPartenza - tempoValidate));

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			if (richiesta != null) {
				if (!richiesta.getRichiedente().getCf().equals(shibIdentitaCodiceFiscale)) {
					param[0] = numeroRichiesta;
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR12.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
							"Il codice fiscale del richiedente della domanda non corrisponde");
				}
				if (richiesta.getStato().equalsIgnoreCase(Constants.BOZZA)) {
					boolean inCorso = sportelliDao.isSportelliCorrente(richiesta.getNumero());
					if (!inCorso) {
						listError = validateGeneric.checkSportelloCorrente(listError, inCorso);
						if (!listError.isEmpty()) {
							throw new ResponseErrorException(
									ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
									"sportello chiuso");
						}
					}
				}
				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(numeroRichiesta);

				long tempoAllegati = System.currentTimeMillis();
				log.info("richiesteget - tempoAllegati: " + System.currentTimeMillis() + " millis: "
						+ (tempoValidate - tempoAllegati));

				if (!allegati.isEmpty())
					richiesta.setAllegati(allegati);
				List<String> rettificare = new ArrayList<String>();
				rettificare = richiesteDao.selectRettificareFromNumeroRichiesta(numeroRichiesta);

				long tempoRettificare = System.currentTimeMillis();
				log.info("richiesteget - tempoAllegati: " + System.currentTimeMillis() + " millis: "
						+ (tempoAllegati - tempoRettificare));

				if (!rettificare.isEmpty())
					richiesta.setRettificare(rettificare);

			} else {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");

			}

			return Response.status(200).entity(richiesta).build();

		} catch (DatabaseException e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			e.printStackTrace();
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore generico ", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}
}

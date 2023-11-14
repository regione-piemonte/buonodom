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
import javax.xml.ws.WebServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.dto.ModelPersona;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.external.anagraficaservice.InterrogaMefEsenredd1Soap;
import it.csi.buonodom.buonodombff.external.anagraficaservice.InterrogaMefEsenreddRes;
import it.csi.buonodom.buonodombff.external.anagraficaservice.InterrogaMefEsenreddResponseBody;
import it.csi.buonodom.buonodombff.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.Converter;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class AnagraficaService extends BaseService {

	@Autowired
	InterrogaMefEsenredd1Soap anagraficaService;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	CodParametroDao codParametroDao;

	public Response anagraficaCfGet(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, String cf) {

		ErrorBuilder error = null;
		ModelPersona datianagrafici = new ModelPersona();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		try {
			// validate
			List<ErroreDettaglioExt> listError = validateGeneric.validateAnagrafica(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, cf, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			boolean verificasechiamare = true;
			String parametro = codParametroDao.selectValoreParametroFromCod(Constants.CHIAMA_INTERROGAMEF,
					Constants.PARAMETRO_GENERICO);
			if (parametro != null)
				verificasechiamare = parametro.equalsIgnoreCase("TRUE") ? true : false;
			InterrogaMefEsenreddRes response = new InterrogaMefEsenreddRes();
			if (verificasechiamare) {
				response = anagraficaService.interrogaMefEsenredd(cf);
			} else {
				response.setEsitomef("OK");
				InterrogaMefEsenreddResponseBody bodymock = new InterrogaMefEsenreddResponseBody();
				bodymock.setCodicefiscale(cf);
				bodymock.setCognome("CARICO");
				bodymock.setComunenasc("TORINO");
				bodymock.setComuneResidenza("TORINO");
				bodymock.setDatanascita("02/01/1990");
				bodymock.setIndirizzoResidenza("VIA ROMA 10");
				bodymock.setNazionenasc("ITALIA");
				bodymock.setNome("TEST");
				bodymock.setProvincianasc("TO");
				bodymock.setProvinciaResidenza("TO");
				response.setBody(bodymock);
			}
			if (response.getEsitomef().contains("Codice di errore: 17")) {
				// errore deve continuare con il solo cf Codice di errore: 17 Lassistito risulta
				// con indirizzo assente. Invitare il soggetto a recarsi presso ufficio
				// anagrafico del comune di competenza

				datianagrafici.setCf(cf);
				datianagrafici.setCognome(null);
				datianagrafici.setNome(null);
				datianagrafici.setComuneNascita(null);
				datianagrafici.setComuneResidenza(null);
				datianagrafici.setDataNascita(null);
				datianagrafici.setIndirizzoResidenza(null);
				datianagrafici.setProvinciaNascita(null);
				datianagrafici.setProvinciaResidenza(null);
				datianagrafici.setStatoNascita(null);
				datianagrafici.setDataDecesso(null);
				return Response.status(200).entity(datianagrafici).build();
			} else if (!response.getEsitomef().equalsIgnoreCase("OK")) {
				logError(metodo, "Errore durante la chiamata a InterrogaMef: ");
				param[0] = cf;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR07.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"errore in interroga mef");

			} else if (response.getBody() != null) {
				datianagrafici.setCf(response.getBody().getCodicefiscale());
				datianagrafici.setCognome(response.getBody().getCognome());
				datianagrafici.setNome(response.getBody().getNome());
				datianagrafici.setComuneNascita(response.getBody().getComunenasc());
				datianagrafici.setComuneResidenza(response.getBody().getComuneResidenza());
				datianagrafici.setDataNascita(Converter.getDataWithoutTimeItalian(response.getBody().getDatanascita()));
				datianagrafici.setIndirizzoResidenza(response.getBody().getIndirizzoResidenza());
				datianagrafici.setProvinciaNascita(response.getBody().getProvincianasc());
				datianagrafici.setProvinciaResidenza(response.getBody().getProvinciaResidenza());
				datianagrafici.setStatoNascita(response.getBody().getNazionenasc());
				datianagrafici.setDataDecesso(Converter.getDataWithoutTimeItalian(response.getBody().getDatadecesso()));
				return Response.status(200).entity(datianagrafici).build();
			}
		} catch (WebServiceException e) {
			e.printStackTrace();
			logError(metodo, "Errore durante la chiamata a InterrogaMef: ", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			e.printStackTrace();
			logError(metodo, "Errore generico ", e);
			param[0] = e.getMessage();
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}
}

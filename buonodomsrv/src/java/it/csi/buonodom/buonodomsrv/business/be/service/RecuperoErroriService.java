/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.business.be.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodomsrv.business.be.service.base.BaseService;
import it.csi.buonodom.buonodomsrv.dto.ModelGetAllegatoExt;
import it.csi.buonodom.buonodomsrv.dto.ModelRichiestaRecupero;
import it.csi.buonodom.buonodomsrv.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodomsrv.exception.DatabaseException;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodomsrv.util.Constants;
import it.csi.buonodom.buonodomsrv.util.ErrorBuilder;
import it.csi.buonodom.buonodomsrv.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodomsrv.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodomsrv.util.validator.impl.ValidateGenericImpl;

@Service
public class RecuperoErroriService extends BaseService {

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	private SmistaDocumentoPartenzaService smistaDocumentoPartenzaService;

	@Autowired
	private CreaLetteraService creaLetteraService;

	public Response recuperoErroriStardasPartenza(String tipolettera, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<ModelRichiestaRecupero> richieste = new ArrayList<ModelRichiestaRecupero>();
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErroreDettaglioExt errore = new ErroreDettaglioExt();
		String[] param = new String[1];
		ModelGetAllegatoExt lettera = new ModelGetAllegatoExt();
		String letteradacreare = null;
		try {
			if (tipolettera.equalsIgnoreCase(Constants.LETTERA_DINIEGO)) {
				richieste = richiesteDao.selectNumeroRichiesteRecupero(Constants.DINIEGO);
			}

			if (richieste != null && richieste.size() > 0) {
				for (ModelRichiestaRecupero richiesta : richieste) {
					// verifico se esiste allegato lettera nel caso negativo la creo
					lettera = richiesteDao.selectAllegatiProtPartenza(richiesta.getDomandaDetId(),
							Constants.LETTERA_DINIEGO);
					// non trova la lettera si deve rigenerare
					if (lettera == null) {
						if (richiesta.getNote() != null) {
							if (richiesta.getNote().contains("decesso del destinatario"))
								letteradacreare = Constants.DINIEGO_SCADENZA_REQUISTI_DECESSO;
							else if (richiesta.getNote().contains("cambio di residenza"))
								letteradacreare = Constants.DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA;
							else
								letteradacreare = Constants.LETTERA_DINIEGO;
							creaLetteraService.creaLettera(richiesta.getNumero(), letteradacreare, xRequestId,
									xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale, securityContext,
									httpHeaders, httpRequest);
						}
					}
					smistaDocumentoPartenzaService.execute(richiesta.getNumero(), Constants.LETTERA_DINIEGO,
							shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest);
				}
			}
			return Response.ok().entity(richieste).build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
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

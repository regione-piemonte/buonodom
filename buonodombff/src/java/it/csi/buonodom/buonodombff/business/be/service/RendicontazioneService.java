/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.dto.ModelDocumentoSpesaDettaglio;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelGetAllegato;
import it.csi.buonodom.buonodombff.dto.custom.ModelRichiestaExt;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.integration.dao.custom.AllegatoBuonoDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.RendicontazioneDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class RendicontazioneService extends BaseService {

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	RendicontazioneDao rendicontazioneDao;

	@Autowired
	AllegatoBuonoDao allegatoBuonoDao;

	public Response deleteRendicontazione(String numeroRichiesta, Integer idDocumentoSpesa, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		List<ErroreDettaglioExt> listError = new ArrayList<ErroreDettaglioExt>();
		try {

			// VALIDAZIONE 400
			listError = validateGeneric.validateDeleteRendicontazione(numeroRichiesta, idDocumentoSpesa, xRequestId,
					xForwardedFor, xCodiceServizio, shibIdentitaCodiceFiscale);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			ModelRichiestaExt richiesta = richiesteDao.selectNumeroRichiestaExt(numeroRichiesta);
			if (richiesta != null) {
				listError = validateGeneric.checkCodFiscaleAndShibIden(listError, richiesta.getRichiedente().getCf(),
						shibIdentitaCodiceFiscale);
				if (!listError.isEmpty()) {
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
							"errore in validate");
				}
				boolean esisteDocumento = rendicontazioneDao.esisteDocumentoSpesaPerId(idDocumentoSpesa);
				if (!esisteDocumento) {
					param[0] = "Documento di spesa " + idDocumentoSpesa.toString();
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR23.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
							"documento di spesa inesistente");
				} else {
					// posso procedere con le cancellazioni
					List<ModelDocumentoSpesaDettaglio> dettagli = new ArrayList<ModelDocumentoSpesaDettaglio>();
					dettagli = rendicontazioneDao.eliminaDocumentoDiSpesa(numeroRichiesta, idDocumentoSpesa);
					// cancello allegati da file system
					if (dettagli != null && dettagli.size() > 0) {
						String os = System.getProperty("os.name");
						boolean locale = false;
						if (os.toLowerCase().contains("win")) {
							locale = true;
						}
						for (ModelDocumentoSpesaDettaglio dettaglio : dettagli) {
							// cancello da db
							ModelGetAllegato allegato = allegatoBuonoDao.selectAllegatoBuono(dettaglio.getIdAllegato());
							if (rendicontazioneDao.eliminaAllegatoSpesa(dettaglio.getIdAllegato()) > 0) {
								File f = null;

								if (!locale)
									f = new File(allegato.getFilePath() + "/" + allegato.getFileName());
								else
									f = new File(allegato.getFilePath() + "\\" + allegato.getFileName());
								logInfo("cancello allegato al buono di tipo " + dettaglio.getTipo()
										+ dettaglio.getIdAllegato(),
										allegato.getFilePath() + "\\" + allegato.getFileName());
								f.delete();
								File dir = new File(allegato.getFilePath());
								if (dir.exists()) {
									dir.delete();
								}
							}
						}
					}
				}
			} else {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");
			}
			return Response.status(200).entity("OK").build();

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

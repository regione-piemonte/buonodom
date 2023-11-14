/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.dto.ModelAllegato;
import it.csi.buonodom.buonodombff.dto.ModelCronologia;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelGetAllegatoExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelRichiestaExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelUpdateCronologia;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.integration.dao.custom.AllegatoDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.CronologiaDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.SportelliDao;
import it.csi.buonodom.buonodombff.integration.service.LogAuditService;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.rest.ResponseRest;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class CronologiaService extends BaseService {

	@Autowired
	LogAuditService logaudit;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	CronologiaDao cronologiaDao;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	AllegatoDao allegatoDao;

	@Autowired
	ServizioRestService servizioRestService;

	@Autowired
	SportelliDao sportelliDao;

	public Response getCronologiaRichieste(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		List<ModelCronologia> cronologia = new ArrayList<ModelCronologia>();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		try {
			// validate
			String cf = cronologiaDao.selectCf(numeroRichiesta);
			if (cf == null) {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");
			}
			List<ErroreDettaglioExt> listError = validateGeneric.validateCronologia(xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, cf);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}

			cronologia = cronologiaDao.selectCronologia(numeroRichiesta);

			return Response.status(200).entity(cronologia).build();

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

	public Response postCronologiaRichieste(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, String stato, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) {
		ErrorBuilder error = null;
		ModelCronologia cronologia = new ModelCronologia();
		ModelUpdateCronologia update = new ModelUpdateCronologia();
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		boolean statidacontrollare = false;
		try {
			if (stato.equalsIgnoreCase(Constants.ANNULLATA) || stato.equalsIgnoreCase(Constants.IN_RETTIFICA)
					|| stato.equalsIgnoreCase(Constants.CONTRODEDOTTA)
					|| stato.equalsIgnoreCase(Constants.RINUNCIATA)) {
				statidacontrollare = true;
			}
			// validate
			List<ErroreDettaglioExt> listError = validateGeneric.validatePostCronologia(xRequestId, xForwardedFor,
					xCodiceServizio, shibIdentitaCodiceFiscale, securityContext, httpHeaders, httpRequest, stato);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			ModelRichiestaExt richiesta = richiesteDao.selectNumeroRichiestaExt(numeroRichiesta);
			if (richiesta != null) {
				if (!richiesta.getRichiedente().getCf().equals(shibIdentitaCodiceFiscale)) {
					param[0] = numeroRichiesta;
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR12.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
							"Il codice fiscale del richiedente della domanda non corrisponde");
				}

				if (!stato.equalsIgnoreCase(Constants.ANNULLATA) && !stato.equalsIgnoreCase(Constants.RINUNCIATA)) {
					Boolean residentePiemonte = richiesteDao
							.isResidenzaPiemontese(richiesta.getDestinatario().getProvinciaResidenza());
					// validate
					if (!residentePiemonte) {
						param[0] = richiesta.getDestinatario().getCognome() + " "
								+ richiesta.getDestinatario().getNome();
						listerrorservice
								.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR19.getCode(), param));
						throw new ResponseErrorException(ErrorBuilder.generateErrorBuilderError(
								StatusEnum.UNPROCESSABLE_ENTITY, listerrorservice), "destinatario non piemontese");
					}
				}
				listError = validateGeneric.checkCambioStatoCoerente(listError, stato, richiesta.getStato());
				if (!listError.isEmpty()) {
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
							"stato non coerente");
				}
				if (stato.equalsIgnoreCase(Constants.INVIATA)) {
					boolean inCorso = sportelliDao.isSportelliCorrente(richiesta.getNumero());
					if (!inCorso) {
						listError = validateGeneric.checkSportelloCorrente(listError, inCorso);
						if (!listError.isEmpty()) {
							throw new ResponseErrorException(
									ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
									"cambio stato non possibile");
						}
					}
				}
				if (!stato.equalsIgnoreCase(Constants.ANNULLATA) && !stato.equalsIgnoreCase(Constants.RINUNCIATA)) {
					listError = validateGeneric.checkSeCambioStatoPossibile(listError, richiesta);
					if (!listError.isEmpty()) {
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
								"cambio stato non possibile");
					}
				}
				if (stato.equalsIgnoreCase(Constants.CONTRODEDOTTA)) {
					listError = validateGeneric.notaControdeduzione(listError, richiesta.getNoteRichiedente());
					if (!listError.isEmpty()) {
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
								"cambio stato non possibile");
					}
				}
				List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
				allegati = richiesteDao.selectAllegatiFromNumeroRichiesta(numeroRichiesta);

				if (!allegati.isEmpty())
					richiesta.setAllegati(allegati);
				String detCod = richiesteDao.selectDetCod(numeroRichiesta);
				listError = validateGeneric.checkAllegato(listError, richiesta, detCod, shibIdentitaCodiceFiscale,
						stato, statidacontrollare);
				if (!listError.isEmpty()) {
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
							"errore in validate");
				}
				if (!stato.equalsIgnoreCase(Constants.ANNULLATA) && !stato.equalsIgnoreCase(Constants.RINUNCIATA)) {
					listError = validateGeneric.checkAllegatoFileSystem(listError, richiesta, detCod,
							shibIdentitaCodiceFiscale, stato);
					if (!listError.isEmpty()) {
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listError),
								"errore in allega documenti");
					}
				}
				if (stato.equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
					listError = validateGeneric.checkEliminazioneIncompatibilita(listError, richiesta);
					if (!listError.isEmpty()) {
						throw new ResponseErrorException(
								ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listError),
								"errore in perfezionamento domanda");
					}
				}
				update = cambioStatoDomanda(richiesta, stato, shibIdentitaCodiceFiscale, numeroRichiesta, xRequestId,
						xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);
				cronologia.setStato(stato);
				cronologia.setDataAggiornamento(new Date());
				cronologia.setNumero(numeroRichiesta);
				cronologia.setNote(richiesta.getNote());
				// se lo stato in_rettifica allora devo portarmi appresso anche i dati da
				// modificare facendo update su lla tabella dati da modificare
				if (stato.equalsIgnoreCase(Constants.IN_RETTIFICA)) {
					List<ModelAllegato> allegatinew = new ArrayList<ModelAllegato>();
					allegatinew = richiesteDao.selectAllegatiFromNumeroRichiesta(numeroRichiesta);
					richiesteDao.updateDatiDaModificare(update.getIdDettaglio(), richiesta.getDomandaDetId());
					for (ModelAllegato allegato : allegati) {
						ModelAllegato al = allegatinew.stream()
								.filter(f -> allegato.getTipo().equalsIgnoreCase(f.getTipo())).findFirst().orElse(null);
						if (al != null) {
							richiesteDao.updateallegatiDaModificare(update.getIdDettaglio(), allegato.getId(),
									al.getId());
						}
					}
				}
				if (stato.equalsIgnoreCase(Constants.INVIATA)) {
					richiesteDao.updateDataDomandaValDomanda(numeroRichiesta, shibIdentitaCodiceFiscale);
					// chiamo smista documento STARDAS
					servizioRestService.getStartDas(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta);
					servizioRestService.getInviaNotifica(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta, "INVIO_CITTADINO");
				} else if (stato.equalsIgnoreCase(Constants.RETTIFICATA)) {
					// chiamo smista documento STARDAS
					servizioRestService.getStartDas(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta);
					servizioRestService.getInviaNotifica(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta, "RETTIFICA_CITTADINO");
				} else if (stato.equalsIgnoreCase(Constants.CONTRODEDOTTA)) {
					// chiamo smista documento STARDAS
					servizioRestService.getStartDas(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta);
					servizioRestService.getInviaNotifica(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta, "CONTRODEDOTTA_CITTADINO");
				} else if (stato.equalsIgnoreCase(Constants.RINUNCIATA)) {
					// verifico se lo stato precedente della domanda ammessa e ammessa con riserva
					// aggiorno la graduatoria se esiste
					if (richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA)
							|| richiesta.getStato().equalsIgnoreCase(Constants.AMMESSA_RISERVA)) {
						long idGraduatoria = richiesteDao.selectGraduatoriaIdBySportelloId(richiesta.getSportelloId());
						if (richiesteDao.checkStatoGraduatoria(idGraduatoria, Constants.PROVVISORIA)) {
							richiesteDao.updateRGraduatoriaStato(idGraduatoria, Constants.DA_AGGIORNARE,
									richiesta.getRichiedente().getCf());
						}
					}
					// chiamo smista documento STARDAS
					servizioRestService.getStartDas(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta);
					servizioRestService.getInviaNotifica(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta, "RINUNCIATA_CITTADINO");
				} else if (stato.equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
					// chiamo smista documento STARDAS
					servizioRestService.getStartDas(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta);
					servizioRestService.getInviaNotifica(xRequestId, xForwardedFor, xCodiceServizio, securityContext,
							httpHeaders, httpRequest, numeroRichiesta, "PERFEZIONATA_IN_PAGAMENTO_CITTADINO");
				}

			} else {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");

			}
			return Response.status(200).entity(cronologia).build();

		} catch (DatabaseException e) {
			param[0] = e.getMessage();
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			param[0] = e.getMessage();
			logError(metodo, "Errore generico ", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		}
		Response esito = error.generateResponseError();
		return esito;
	}

	private ModelUpdateCronologia cambioStatoDomanda(ModelRichiestaExt richiesta, String stato, String codFicale,
			String numeroRichiesta, String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws DatabaseException {

		ModelUpdateCronologia update = richiesteDao.insertTDettaglioDomandaCambioStato(richiesta, stato, codFicale);
		richiesteDao.updateDataFineValDettaglioDomanda(richiesta.getDomandaDetId());

		long idDettaglio = update.getIdDettaglio();
		String detCod = update.getDetCod();
		if (!richiesta.getAllegati().isEmpty()) {
			List<ModelGetAllegatoExt> allegati = allegatoDao.selectAllegati(richiesta.getDomandaDetId());
			if (!allegati.isEmpty()) {
				for (ModelGetAllegatoExt a : allegati) {
					if (!a.getAllegatoTipoCod().equalsIgnoreCase(Constants.DOMANDA)) {
						allegatoDao.insertAllegatoConAllegatoTipoId(a.getFileName(), a.getFileType(), a.getFilePath(),
								richiesta.getSportelloId(), BigDecimal.valueOf(idDettaglio), detCod, a.getAllegatoId(),
								codFicale, codFicale);
					}
				}
			}
		}
		ResponseRest responserest = servizioRestService.getCreaDomanda(xRequestId, xForwardedFor, xCodiceServizio,
				securityContext, httpHeaders, httpRequest, richiesta.getNumero());

		update.setDetCod(detCod);
		update.setIdDettaglio(idDettaglio);

		return update;
	}
}

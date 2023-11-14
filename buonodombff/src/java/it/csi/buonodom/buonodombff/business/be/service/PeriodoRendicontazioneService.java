/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombff.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombff.dto.ModelDocumentoSpesa;
import it.csi.buonodom.buonodombff.dto.ModelFruizione;
import it.csi.buonodom.buonodombff.dto.ModelFruizioneFruizione;
import it.csi.buonodom.buonodombff.dto.ModelFruizioneRendicontazione;
import it.csi.buonodom.buonodombff.dto.ModelSabbatici;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ResponseErrorException;
import it.csi.buonodom.buonodombff.integration.dao.custom.AllegatoBuonoDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.FornitoriDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.GetListeDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.RendicontazioneDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.Converter;
import it.csi.buonodom.buonodombff.util.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.Util;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombff.util.validator.impl.ValidateGenericImpl;

@Service
public class PeriodoRendicontazioneService extends BaseService {

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	RichiesteDao richiesteDao;

	@Autowired
	FornitoriDao fornitoriDao;

	@Autowired
	RendicontazioneDao rendicontazioneDao;

	@Autowired
	AllegatoBuonoDao allegatoBuonoDao;

	@Autowired
	GetListeDao getListeDao;

	public Response periodoRendicontazione(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		try {
			// validate
			List<ErroreDettaglioExt> listError = validateGeneric.validateListaDecodifica(shibIdentitaCodiceFiscale,
					xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			String[] richiesta = richiesteDao.selectDataInPagamento(numeroRichiesta);
			if (richiesta != null) {
				listError = validateGeneric.checkCodFiscaleAndShibIden(listError, richiesta[0],
						shibIdentitaCodiceFiscale);
				if (!listError.isEmpty()) {
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
							"errore in validate");
				}
			} else {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");
			}
			ModelFruizione periodo = getPeriodoFruizione(numeroRichiesta, richiesta);
			return Response.status(200).entity(periodo).build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
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

	public Response mesiSabbatici(String numeroRichiesta, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelSabbatici sabbatici,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ErrorBuilder error = null;

		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		String[] param = new String[1];
		try {
			// validate
			// verifica che sono differenti
			if (sabbatici != null && sabbatici.getSabbatici() != null) {
				List<String> mesidistinti = new ArrayList<String>(new HashSet<String>(sabbatici.getSabbatici()));
				sabbatici.setSabbatici(mesidistinti);
			}
			List<ErroreDettaglioExt> listError = validateGeneric.validateMesiSabbatici(shibIdentitaCodiceFiscale,
					sabbatici, xRequestId, xForwardedFor, xCodiceServizio, securityContext, httpHeaders, httpRequest);

			if (!listError.isEmpty()) {
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
						"errore in validate");
			}
			String[] richiesta = richiesteDao.selectDataInPagamento(numeroRichiesta);
			if (richiesta != null) {
				listError = validateGeneric.checkCodFiscaleAndShibIden(listError, richiesta[0],
						shibIdentitaCodiceFiscale);
				if (!listError.isEmpty()) {
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listError),
							"errore in validate");
				}
			} else {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");
			}
			// se mesi sabbatici vuoto o array vuoto allora cancello i mesi sabbatici
			// inseriti
			if (sabbatici == null || sabbatici.getSabbatici().size() == 0) {
				rendicontazioneDao.eliminaMesiSabbatici(numeroRichiesta);
			} else {
				// verifico se esiste un documento di spesa per quel buono in quel periodo
				String meseesiste = rendicontazioneDao.documentoSpesaRendicontato(numeroRichiesta,
						sabbatici.getSabbatici());
				if (meseesiste != null) {
					param[0] = "Documento di spesa gia' presente per il mese " + meseesiste;
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
							"Documento di spesa gia' presente per il mese " + meseesiste);
				}
			}
			// se supera controlli sostityuisco i mesi esistentio con i nuovi faccio delete
			// ed insert
			rendicontazioneDao.eliminaMesiSabbatici(numeroRichiesta);
			// se esce il mese non presente posso fare la insert mella tabella
			for (String mesi : sabbatici.getSabbatici()) {
				rendicontazioneDao.insertMesiSabbatici(numeroRichiesta, Converter.getInt(mesi.substring(0, 4)),
						Converter.getInt(mesi.substring(5)), Converter.getDataWithoutTime(mesi + "-01"),
						Converter.getDataWithoutTime(
								mesi + "-" + Util.getDayOfMonth(Converter.getDataWithoutTime(mesi + "-01"))),
						richiesta[0], richiesta[0]);
			}
			ModelFruizione periodo = getPeriodoFruizione(numeroRichiesta, richiesta);
			return Response.status(200).entity(periodo).build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			param[0] = e.getMessage();
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

	private ModelFruizione getPeriodoFruizione(String numeroRichiesta, String[] richiesta) throws DatabaseException {
		// verifico lo stato precedente della domanda
		int mesi = rendicontazioneDao.selectMesiBando(numeroRichiesta);
		ModelFruizione periodo = new ModelFruizione();
		ModelFruizioneFruizione fruizione = new ModelFruizioneFruizione();
		ModelFruizioneRendicontazione fuizionerendicontazione = new ModelFruizioneRendicontazione();
		String statoPrecedente = richiesteDao.selectStatoPrecedente(numeroRichiesta);
		if (statoPrecedente.contains(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
			// prendo come periodo il mese successivo alla data inizio validita dello stato
			// in pagamento
			fruizione.setDataInizio(Converter.getDataWithoutTime(richiesta[1]));
		} else {
			// prendo come periodo il mese successivo alla data dello sportello a cui fa
			// riferimento la domanda
			fruizione.setDataInizio(
					Converter.getDataWithoutTime(rendicontazioneDao.selectDataSportelloChiuso(numeroRichiesta)));
		}
		// mettere data fine come data inizio e mesi finanziamento
		if (mesi != 0)
			fruizione.setDataFine(Converter
					.aggiungiGiorniAData(Converter.aggiungiAnnoAData(fruizione.getDataInizio(), mesi / 12), -1));
		else
			fruizione.setDataFine(fruizione.getDataInizio());
		periodo.setFruizione(fruizione);
		// prendo il periodo di rendicontazione
		String[] periodorend = rendicontazioneDao.selectPeriodoRendicontazioneAperto();
		if (periodorend != null) {
			fuizionerendicontazione.setDataInizio(Converter.getDataWithoutTime(periodorend[0]));
			fuizionerendicontazione.setDataFine(Converter.getDataWithoutTime(periodorend[1]));
		}
		periodo.setRendicontazione(fuizionerendicontazione);
		// prendo i mesi sabbatici
		List<String> mesiSabbatici = rendicontazioneDao.selectMesiSabbatici(numeroRichiesta);
		periodo.setSabbatici(mesiSabbatici);
		// modifico il periodo di fruizione se ci sono i mesi sabbatici
		fruizione.setDataFine(Converter.aggiungiMesiAData(fruizione.getDataFine(), mesiSabbatici.size()));
		// prendo i mesi totali della fruizione e li metto in un array
		ArrayList<String> mesitotali = new ArrayList<String>();
		for (int i = 0; i < mesi + mesiSabbatici.size(); i++) {
			mesitotali.add(Converter.getDataAnnoMese(Converter.aggiungiMesiAData(fruizione.getDataInizio(), i)));
		}
		// aggiungo il periodi rendicontati
		// chiamo la get per avere i dati del documento di spesa
		List<ModelDocumentoSpesa> spesa = rendicontazioneDao.selectDocumentoSpesa(numeroRichiesta, mesiSabbatici);
		if (spesa != null && spesa.size() > 0) {
			for (ModelDocumentoSpesa docspesa : spesa) {
				mesitotali.removeAll(docspesa.getMesi());
			}
		}
		periodo.setNonRendicontati(mesitotali);
		return periodo;
	}
}

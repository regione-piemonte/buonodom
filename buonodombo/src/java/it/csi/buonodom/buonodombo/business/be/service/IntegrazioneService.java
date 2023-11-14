/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.business.be.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombo.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombo.dto.Esito;
import it.csi.buonodom.buonodombo.dto.ModelAteco;
import it.csi.buonodom.buonodombo.dto.ModelRichiesta;
import it.csi.buonodom.buonodombo.dto.UserInfo;
import it.csi.buonodom.buonodombo.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombo.exception.DatabaseException;
import it.csi.buonodom.buonodombo.exception.ResponseErrorException;
import it.csi.buonodom.buonodombo.external.ateco.AttivitaEconomica;
import it.csi.buonodom.buonodombo.external.ateco.Impresa;
import it.csi.buonodom.buonodombo.filter.IrideIdAdapterFilter;
import it.csi.buonodom.buonodombo.integration.GetDettaglioImpresaService;
import it.csi.buonodom.buonodombo.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodombo.util.Constants;
import it.csi.buonodom.buonodombo.util.Converter;
import it.csi.buonodom.buonodombo.util.ErrorBuilder;
import it.csi.buonodom.buonodombo.util.Util;
import it.csi.buonodom.buonodombo.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombo.util.enumerator.ErrorParamEnum;
import it.csi.buonodom.buonodombo.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombo.util.validator.impl.ValidateGenericImpl;

@Service
public class IntegrazioneService extends BaseService {
	@Autowired
	GetDettaglioImpresaService impresaService;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	RichiesteDao richiesteDao;

	public Response atecoGet(String piva, String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		Impresa impresa = new Impresa();
		List<AttivitaEconomica> codiciATECO = new ArrayList<AttivitaEconomica>();
		String[] param = new String[1];
		ModelAteco ateco = new ModelAteco();
		Esito risultato = new Esito();
		BigDecimal iddomanda = BigDecimal.ZERO;
		try {

			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			String pivadomanda = null;
			if (richiesta != null) {
				iddomanda = richiesta.getDomandaDetId();
				if (richiesta.getContratto() != null) {
					if (richiesta.getContratto().getTipo() != null) {
						if (richiesta.getContratto().getTipo().equals(Constants.COOPERATIVA)) {
							if (richiesta.getContratto().getAgenzia() != null) {
								if (Util.isValorizzato(richiesta.getContratto().getAgenzia().getCf())) {
									pivadomanda = richiesta.getContratto().getAgenzia().getCf();
								}
							}
						} else if (richiesta.getContratto().getTipo().equals(Constants.PARTITA_IVA)) {
							if (Util.isValorizzato(richiesta.getContratto().getPivaAssitenteFamiliare())) {
								pivadomanda = richiesta.getContratto().getPivaAssitenteFamiliare();
							}
						}
					}
				}
				if (!piva.equalsIgnoreCase(pivadomanda)) {
					logError(metodo, "Errore partita iva non coerente con la domanda: ");
					param[0] = ErrorParamEnum.PIVA.getCode();
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
							"errore in chiamata ad ateco");
				}
				impresa = impresaService.dettaglioImpresa(piva);
				int anno = 0;
				if (impresa != null) {
					if (impresa.getCodiciATECO().size() > 0) {
						for (AttivitaEconomica atecocod : impresa.getCodiciATECO()) {
							int annorif = Converter.getInt(atecocod.getAnnoDiRiferimento());
							if (annorif > anno) {
								ateco.setAtecoCod(atecocod.getCodiceATECO());
								ateco.setAtecoDesc(atecocod.getDescrizione());
								ateco.setAtecoVerificatoInData(new Date());
								anno = annorif;
							}
						}
					}
					if (ateco.getAtecoCod() != null) {
						// aggiorno db
						richiesteDao.updateAtecoDettaglioDomanda(iddomanda, ateco.getAtecoCod(), ateco.getAtecoDesc(),
								userInfo.getCodFisc());
						risultato.setCodice("OK");
						risultato.setMessaggio(ateco.getAtecoCod());
						return Response.ok().entity(ateco).build();
					}
				}
			}
			risultato.setCodice("KO");
			risultato.setMessaggio("Domanda non trovata o Ateco non trovato");
			return Response.ok().entity(risultato).build();

		} catch (DatabaseException e) {
			e.printStackTrace();
			logError(metodo, "Errore riguardante database:", e);
			param[0] = "Errore riguardante database";
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
		} catch (Exception e) {
			e.printStackTrace();
			logError(metodo, "Errore generico ", e);
			param[0] = e.getMessage();
			if (param[0].contains("cercaPerCodiceFiscaleAAEP: Nessun record trovato")) {
				risultato.setCodice("KO");
				risultato.setMessaggio(validateGeneric.getValueGenericSuccess(Constants.MSG002, null));
				ateco.setAtecoCod(risultato.getMessaggio());
				ateco.setAtecoDesc("");
				ateco.setAtecoVerificatoInData(new Date());
				// aggiorno db con messaggio nel cod ateco
				try {
					richiesteDao.updateAtecoDettaglioDomanda(iddomanda, ateco.getAtecoCod(), null,
							userInfo.getCodFisc());
				} catch (DatabaseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return Response.ok().entity(ateco).build();
			} else {
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
				error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
			}
		}
		Response esito = error.generateResponseError();
		return esito;
	}
}

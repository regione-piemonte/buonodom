/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.util.rest.validator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.csi.buonodom.buonodomcallban.dto.ModelDocumentoRicevuto;
import it.csi.buonodom.buonodomcallban.dto.PayloadEsitoAcquisizione;
import it.csi.buonodom.buonodomcallban.dto.PayloadEsitoAcquisizioneSpesa;
import it.csi.buonodom.buonodomcallban.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodomcallban.exception.DatabaseException;
import it.csi.buonodom.buonodomcallban.util.Constants;
import it.csi.buonodom.buonodomcallban.util.Util;
import it.csi.buonodom.buonodomcallban.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodomcallban.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericImpl extends BaseValidate {

	public List<ErroreDettaglioExt> validateAcquisizione(String xRequestId, PayloadEsitoAcquisizione esito,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglioExt> result = new ArrayList<>();

		// inserimento controllo specifico citid
		String[] param = new String[1];
		if (StringUtils.isEmpty(xRequestId)) {
			param[0] = ErrorParamEnum.X_REQUEST_ID.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (esito != null) {
			if (esito.getEsitoAcquisizione() != null) {
				if (!Util.isValorizzato(esito.getEsitoAcquisizione().getEsito())) {
					param[0] = ErrorParamEnum.ESITO_OK_KO.getCode();
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				} else if (esito.getEsitoAcquisizione().getEsito().equalsIgnoreCase(Constants.KO)) {
					if (!Util.isValorizzato(esito.getEsitoAcquisizione().getUuid())) {
						param[0] = ErrorParamEnum.MESSAGGE_UUID.getCode();
						result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
					}
					if (esito.getEsitoAcquisizione().getErrore() != null) {
						if (!Util.isValorizzato(esito.getEsitoAcquisizione().getErrore().getCodice())) {
							param[0] = ErrorParamEnum.CODICE_ERRORE.getCode();
							result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
						}
						if (!Util.isValorizzato(esito.getEsitoAcquisizione().getErrore().getMessaggio())) {
							param[0] = ErrorParamEnum.MESSAGGIO_ERRORE.getCode();
							result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
						}
					} else {
						param[0] = ErrorParamEnum.ERRORE.getCode();
						result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
					}
				} else if (esito.getEsitoAcquisizione().getEsito().equalsIgnoreCase(Constants.OK)) {
					if (!Util.isValorizzato(esito.getEsitoAcquisizione().getUuid())) {
						param[0] = ErrorParamEnum.MESSAGGE_UUID.getCode();
						result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
					}
				}
				// esito non ok o ko
				else {
					param[0] = ErrorParamEnum.MANCA_ESITO.getCode();
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				}
			} else {
				param[0] = ErrorParamEnum.ESITOACQUISIZIONE.getCode();
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}
		} else {
			param[0] = ErrorParamEnum.ESITO.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> validateRendicontazioneAcquisizione(String xRequestId,
			PayloadEsitoAcquisizioneSpesa esito, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglioExt> result = new ArrayList<>();

		// inserimento controllo specifico citid
		String[] param = new String[1];
		if (StringUtils.isEmpty(xRequestId)) {
			param[0] = ErrorParamEnum.X_REQUEST_ID.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (esito != null) {
			if (esito.getEsitoAcquisizione() != null) {
				if (!Util.isValorizzato(esito.getEsitoAcquisizione().getEsito())) {
					param[0] = ErrorParamEnum.ESITO_OK_KO.getCode();
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				} else if (esito.getEsitoAcquisizione().getEsito().equalsIgnoreCase(Constants.KO)) {
					if (esito.getDocumenti() != null && esito.getDocumenti().size() > 0) {
						param[0] = ErrorParamEnum.DOCUMENTOSPESABUONODOM.getCode();
						result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
					}
					if (!Util.isValorizzato(esito.getEsitoAcquisizione().getUuid())) {
						param[0] = ErrorParamEnum.MESSAGGE_UUID.getCode();
						result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
					}
					if (esito.getEsitoAcquisizione().getErrore() != null) {
						if (!Util.isValorizzato(esito.getEsitoAcquisizione().getErrore().getCodice())) {
							param[0] = ErrorParamEnum.CODICE_ERRORE.getCode();
							result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
						}
						if (!Util.isValorizzato(esito.getEsitoAcquisizione().getErrore().getMessaggio())) {
							param[0] = ErrorParamEnum.MESSAGGIO_ERRORE.getCode();
							result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
						}
					} else {
						param[0] = ErrorParamEnum.ERRORE.getCode();
						result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
					}
				} else if (esito.getEsitoAcquisizione().getEsito().equalsIgnoreCase(Constants.OK)) {
					if (esito.getEsitoAcquisizione().getErrore() != null
							&& Util.isValorizzato(esito.getEsitoAcquisizione().getErrore().getCodice())) {
						param[0] = ErrorParamEnum.ERRORE.getCode();
						result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
					}
					if (!Util.isValorizzato(esito.getIdDichiarazioneSpesaBandi())) {
						param[0] = ErrorParamEnum.IDDICHIARAZIONESPESABANDI.getCode();
						result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
					}
					if (!Util.isValorizzato(esito.getEsitoAcquisizione().getUuid())) {
						param[0] = ErrorParamEnum.MESSAGGE_UUID.getCode();
						result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
					}
					if (esito.getDocumenti() != null && esito.getDocumenti().size() > 0) {
						for (ModelDocumentoRicevuto doc : esito.getDocumenti()) {
							if (!Util.isValorizzato(doc.getIdDocumentoBandi())) {
								param[0] = ErrorParamEnum.IDDOCUMENTOSPESABANDI.getCode();
								result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
								break;
							}
							if (!Util.isValorizzato(doc.getIdDocumentoBuonodom())) {
								param[0] = ErrorParamEnum.IDDOCUMENTOSPESABUONODOM.getCode();
								result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
								break;
							}
						}
					} else {
						param[0] = ErrorParamEnum.IDDOCUMENTOSPESABUONODOM.getCode();
						result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
					}
				}
				// esito non ok o ko
				else {
					param[0] = ErrorParamEnum.MANCA_ESITO.getCode();
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				}
			} else {
				param[0] = ErrorParamEnum.ESITOACQUISIZIONE.getCode();
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}
		} else {
			param[0] = ErrorParamEnum.ESITO.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

}

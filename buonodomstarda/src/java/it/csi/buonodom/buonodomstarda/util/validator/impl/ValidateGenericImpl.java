/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomstarda.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodomstarda.dto.EsitoSmistaDocumentoRequest;
import it.csi.buonodom.buonodomstarda.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodomstarda.exception.DatabaseException;
import it.csi.buonodom.buonodomstarda.util.Util;
import it.csi.buonodom.buonodomstarda.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodomstarda.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericImpl extends BaseValidate {

	public List<ErroreDettaglioExt> validateSmista(EsitoSmistaDocumentoRequest body, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		// inserimento controllo specifico citid
		String[] param = new String[1];
		if (body.getEsitoSmistaDocumento() != null) {
			if (body.getEsitoSmistaDocumento().getEsito() != null) {
				if (!Util.isValorizzato(body.getEsitoSmistaDocumento().getEsito().getMessageUUID())) {
					param[0] = ErrorParamEnum.MESSAGGE_UUID.getCode();
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				}

				if (!Util.isValorizzato(body.getEsitoSmistaDocumento().getEsito().getIdDocumentoFruitore())) {
					param[0] = ErrorParamEnum.IDDOCUMENTOFRUITORE.getCode();
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				}
			} else {
				param[0] = ErrorParamEnum.ESITO.getCode();
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}
		} else {
			param[0] = ErrorParamEnum.ESITOSMISTA.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

}

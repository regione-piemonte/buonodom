/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.util.validator.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.csi.buonodom.buonodomsrv.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodomsrv.exception.DatabaseException;
import it.csi.buonodom.buonodomsrv.util.Util;
import it.csi.buonodom.buonodomsrv.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodomsrv.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericImpl extends BaseValidate {

	private List<ErroreDettaglioExt> commonCheck(List<ErroreDettaglioExt> result, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio) throws DatabaseException {

		String[] param = new String[1];
		if (StringUtils.isEmpty(xRequestId)) {
			param[0] = ErrorParamEnum.X_REQUEST_ID.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(shibIdentitaCodiceFiscale)) {
			param[0] = ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(xForwardedFor)) {
			param[0] = ErrorParamEnum.X_FORWARDED_FOR.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(xCodiceServizio)) {
			param[0] = ErrorParamEnum.X_CODICE_SERVIZIO.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	private List<ErroreDettaglioExt> checkNull(List<ErroreDettaglioExt> result, Object id, String tipoId)
			throws DatabaseException {

		String[] param = new String[1];
		if (id == null) {
			param[0] = tipoId;
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		}
		return result;
	}

	private List<ErroreDettaglioExt> checkEmptyString(List<ErroreDettaglioExt> result, String element,
			String tipoElement) throws DatabaseException {

		String[] param = new String[1];
		if (StringUtils.isEmpty(element)) {
			param[0] = tipoElement;
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		return result;
	}

	public List<ErroreDettaglioExt> validateDomanda(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		// inserimento controllo specifico citid
		String[] param = new String[1];
		if (!Util.isValorizzato(numeroRichiesta)) {
			param[0] = ErrorParamEnum.NUMERODOMANDA.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> validateLettera(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String numeroRichiesta, String tipoLettera,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		// inserimento controllo specifico citid
		String[] param = new String[1];
		if (!Util.isValorizzato(numeroRichiesta)) {
			param[0] = ErrorParamEnum.NUMERODOMANDA.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (!Util.isValorizzato(tipoLettera)) {
			param[0] = ErrorParamEnum.TIPOLETTERA.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> validateNotifica(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String numeroRichiesta, String tipoNotifica,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		// inserimento controllo specifico citid
		String[] param = new String[1];
		if (!Util.isValorizzato(numeroRichiesta)) {
			param[0] = ErrorParamEnum.NUMERODOMANDA.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (!Util.isValorizzato(tipoNotifica)) {
			param[0] = ErrorParamEnum.TIPONOTIFICA.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		return result;
	}

	public List<ErroreDettaglioExt> validateContatti(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		return result;
	}
}

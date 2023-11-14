/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.util.validator.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.csi.buonodom.buonodombo.business.be.service.NotificatoreService;
import it.csi.buonodom.buonodombo.dto.Contact;
import it.csi.buonodom.buonodombo.dto.ModelIsee;
import it.csi.buonodom.buonodombo.dto.Preferences;
import it.csi.buonodom.buonodombo.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombo.exception.DatabaseException;
import it.csi.buonodom.buonodombo.util.Constants;
import it.csi.buonodom.buonodombo.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombo.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericImpl extends BaseValidate {

	@Autowired
	NotificatoreService contattiService;

	public List<ErroreDettaglioExt> validateAtecoCod(String shibIdentitaCodiceFiscale, String xRequestId,
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

	public List<ErroreDettaglioExt> validateIsee(ModelIsee isee, String proviene) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglioExt> result = new ArrayList<>();
		if (proviene.equalsIgnoreCase(Constants.AMMISSIBILE)) {
			String[] param = new String[1];
			if (isee.isIseeVerificatoConforme() != null && !isee.isIseeVerificatoConforme()) {
				param[0] = ErrorParamEnum.VERIFICA_CONFORME.getCode();
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}
			if (isee.getIseeValore() == null || isee.getIseeValore() == BigDecimal.ZERO) {
				param[0] = ErrorParamEnum.ISEE.getCode();
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}
		} else if ((isee.isIseeVerificatoConforme() != null && isee.isIseeVerificatoConforme())
				|| isee.isIseeVerificatoConforme() == null) {
			String[] param = new String[1];
			if (isee.getIseeValore() == null || isee.getIseeValore() == BigDecimal.ZERO) {
				param[0] = ErrorParamEnum.VALORE_ISEE.getCode();
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}
			if (isee.getIseeDataRilascio() == null) {
				param[0] = ErrorParamEnum.RILASCIO_ISEE.getCode();
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}
		}
		return result;
	}

	public List<ErroreDettaglioExt> validateRichieste(String shibIdentitaCodiceFiscale, String xRequestId,
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

	public List<ErroreDettaglioExt> validateAllegato(String xForwardedFor, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheckGetAllegato(result, shibIdentitaCodiceFiscale, xForwardedFor);

		return result;
	}

	private List<ErroreDettaglioExt> commonCheckGetAllegato(List<ErroreDettaglioExt> result,
			String shibIdentitaCodiceFiscale, String xForwardedFor) throws DatabaseException {

		String[] param = new String[1];

		if (StringUtils.isEmpty(shibIdentitaCodiceFiscale)) {
			param[0] = ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(xForwardedFor)) {
			param[0] = ErrorParamEnum.X_FORWARDED_FOR.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

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
		} else if (!formalCheckCitId(shibIdentitaCodiceFiscale)) {
			param[0] = shibIdentitaCodiceFiscale;
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
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

	private List<ErroreDettaglioExt> checkNumeroDomanda(List<ErroreDettaglioExt> result, String numeroParam,
			String numeroPaylod) throws DatabaseException {

		String[] param = new String[1];
		if (!numeroParam.equals(numeroPaylod)) {
			param[0] = numeroParam + " diverso da " + numeroPaylod;
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		}
		return result;
	}

	private List<ErroreDettaglioExt> checkCodFiscaleAndShibIden(List<ErroreDettaglioExt> result, String codFiscale,
			String shibIdentita) throws DatabaseException {

		String[] param = new String[1];
		if (codFiscale != null) {
			if (!codFiscale.equals(shibIdentita)) {
				param[0] = codFiscale;
				result.add(getValueGenericError(CodeErrorEnum.ERR12.getCode(), param));
			}
		}
		return result;
	}

	public List<ErroreDettaglioExt> checkEmptyString(List<ErroreDettaglioExt> result, String element,
			String tipoElement) throws DatabaseException {

		String[] param = new String[1];
		if (StringUtils.isEmpty(element)) {
			param[0] = tipoElement;
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		return result;
	}

	public List<ErroreDettaglioExt> checkContatti(List<ErroreDettaglioExt> result, String cfrichiedente,
			String cfutente) throws DatabaseException {
		String[] param = new String[1];
		boolean contattoesiste = false;
		// prendo i contatti del richiedente
		Contact contatto = new Contact();
		Preferences preferenze = new Preferences();
		try {
			contatto = contattiService.sendContact(cfrichiedente, cfutente);
			if (contatto.getEmail() != null) {
				contattoesiste = true;
			}
			preferenze = contattiService.sendPreferenze(cfrichiedente, cfutente);
			boolean canaliemail = false;
			if (preferenze.getChannels() != null) {
				if (preferenze.getChannels().toUpperCase().contains("EMAIL")) {
					canaliemail = true;
				}
			}
			if (!contattoesiste || !canaliemail) {
				param[0] = "Contatti";
				result.add(getValueGenericError(CodeErrorEnum.ERR20.getCode(), param));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<ErroreDettaglioExt> checkCambioStatoCoerente(List<ErroreDettaglioExt> result, String statoDiArrivo,
			String statoDiPartenza) throws DatabaseException {
		String[] param = new String[1];
		boolean incoerente = false;

		switch (statoDiArrivo) {
		case Constants.ANNULLATA:
			if (!statoDiPartenza.equals(Constants.BOZZA))
				incoerente = true;
			break;
		case Constants.INVIATA:
			if (!statoDiPartenza.equals(Constants.BOZZA))
				incoerente = true;
			break;
		case Constants.RETTIFICATA:
			if (!statoDiPartenza.equals(Constants.IN_RETTIFICA))
				incoerente = true;
			break;
		case Constants.DA_RETTIFICARE:
			if (!statoDiPartenza.equals(Constants.PRESA_IN_CARICO))
				incoerente = true;
			break;
		case Constants.PERFEZIONATA_IN_PAGAMENTO:
			if (!statoDiPartenza.equals(Constants.PREAVVISO_DINIEGO)
					|| !statoDiPartenza.equals(Constants.AMMESSA_RISERVA))
				incoerente = true;
			break;

		default:
			incoerente = false;
		}

		if (statoDiArrivo.equals(statoDiPartenza))
			incoerente = true;

		if (incoerente) {
			param[0] = "Stato";
			result.add(getValueGenericError(CodeErrorEnum.ERR13.getCode(), param));
		}
		return result;
	}

	private boolean getAllegato(String filePath, String fileName) {

		String os = System.getProperty("os.name");
		boolean locale = false;
		File file = null;

		try {
			if (os.toLowerCase().contains("win")) {
				locale = true;
			}
			if (!locale)
				file = new File(filePath + "/" + fileName);
			else
				file = new File(filePath + "\\" + fileName);

			if (!file.exists()) {
				return false;
			} else
				return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logError("verifica ellegato", "Errore generico ", e);
			e.printStackTrace();
			return false;
		}

	}
}

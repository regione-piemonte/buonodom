/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.util.validator.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.CreateTemplateMessage;
import it.csi.buonodom.buonodombff.util.LoggerUtil;
import it.csi.buonodom.buonodombff.util.Util;

public abstract class BaseValidate extends LoggerUtil {

	@Autowired
	CodParametroDao codParametroDao;

	public ErroreDettaglioExt getValueGenericError(String key, String[] param) {
		String value;
		try {
			value = codParametroDao.selectValoreParametroFromCod(key, Constants.PARAMETRO_ERRORE_TIPO).trim();
		} catch (DatabaseException e) {
			return null;
		}
		if (param != null) {
			if (param.length > 0) {
				Map<String, Object> parameter = new HashMap<String, Object>();
				for (Integer i = 0; i < param.length; i++) {
					if (param[i] != null)
						parameter.put(i.toString(), param[i]);
				}
				value = CreateTemplateMessage.generateTextByTemplateAndMap(value, parameter);
			}
		}
		return setError(key, value);
	}

	public String getValueGenericSuccess(String key, String[] param) {
		String value;
		try {
			value = codParametroDao.selectValoreParametroFromCod(key, Constants.PARAMETRO_MESSAGGIO_TIPO).trim();
		} catch (DatabaseException e) {
			return null;
		}
		if (param != null) {
			if (param.length > 0) {
				Map<String, Object> parameter = new HashMap<String, Object>();
				for (Integer i = 0; i < param.length; i++) {
					if (param[i] != null)
						parameter.put(i.toString(), param[i]);
				}
				value = CreateTemplateMessage.generateTextByTemplateAndMap(value, parameter);
			}
		}
		return value;
	}

	private Map<String, Object> generateParamList(String paramKey, String paramValue) {
		Map<String, Object> parameter = new HashMap<>();
		parameter.put(paramKey, paramValue);
		return parameter;
	}

	private ErroreDettaglioExt setError(String key, String value) {
		ErroreDettaglioExt erroreDettaglio = new ErroreDettaglioExt();
		erroreDettaglio.setChiave(key);
		erroreDettaglio.setValore(value);
		return erroreDettaglio;
	}

	protected boolean formalCheckCF(String cf) {

		if (cf.length() != 11 && cf.length() != 16)
			return true;

		String regexCF = "[a-zA-Z]{6}\\d\\d[a-zA-Z]\\d\\d[a-zA-Z]\\d\\d\\d[a-zA-Z]";
		String regexPIVA = "[0-9]{11}";

		if (cf.length() == 16 && !Pattern.matches(regexCF, cf)) {
			return true;
		}

		if (cf.length() == 11 && !Pattern.matches(regexPIVA, cf)) {
			return true;
		}

		return false;
	}

	protected boolean formalCheckCitId(String citId) {

		if (Util.isValorizzato(citId)) {
			if (citId.length() == 11 || citId.length() == 16)
				return true;
		}
		return false;
	}

	protected boolean formalCheckDataNascita(String dataNascita) {
		return Util.isData(dataNascita, "yyyy-MM-dd", null);
	}

	protected boolean formalCheckDateRicerca(Date dataInizio, Date dataFine) throws DatabaseException {

		if (dataInizio == null || dataFine == null)
			return false;
		if (dataInizio.compareTo(dataFine) == 1) {
			return true;
		}
		return false;
	}

}

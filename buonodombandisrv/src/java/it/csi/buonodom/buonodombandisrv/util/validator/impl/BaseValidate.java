/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.util.validator.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.buonodom.buonodombandisrv.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombandisrv.exception.DatabaseException;
import it.csi.buonodom.buonodombandisrv.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodombandisrv.util.Constants;
import it.csi.buonodom.buonodombandisrv.util.CreateTemplateMessage;
import it.csi.buonodom.buonodombandisrv.util.LoggerUtil;

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

}

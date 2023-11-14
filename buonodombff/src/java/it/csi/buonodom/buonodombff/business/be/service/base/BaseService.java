/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.service.base;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import it.csi.buonodom.buonodombff.dto.ErroreDettaglio;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.exception.ErrorBuilder;
import it.csi.buonodom.buonodombff.util.LoggerUtil;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.StatusEnum;

public abstract class BaseService extends LoggerUtil {

	/*
	 * @Autowired protected CodDErroreDao codDErroreDao;
	 * 
	 * @Autowired protected CodRMessaggioErroreService codRMessaggioErroreService;
	 */
	protected Response commonError(HttpServletRequest httpRequest, CodeErrorEnum codeErrorEnum, StatusEnum statusEnum)
			throws DatabaseException {

		ErroreDettaglio erroreDettaglio = new ErroreDettaglio();
		erroreDettaglio.setChiave(codeErrorEnum.getCode());
		// erroreDettaglio.setValore(codDErroreDao.selectErroreDescFromErroreCod(codeErrorEnum.getCode()));
		List<ErroreDettaglio> result = new ArrayList<>();

		result.add(erroreDettaglio);
		// codRMessaggioErroreService.saveError(result, httpRequest);
		return ErrorBuilder.from(statusEnum).exception().getResponse();

	}
}

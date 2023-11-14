/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.business.be.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombo.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombo.dto.ModelSportello;
import it.csi.buonodom.buonodombo.dto.UserInfo;
import it.csi.buonodom.buonodombo.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombo.exception.DatabaseException;
import it.csi.buonodom.buonodombo.filter.IrideIdAdapterFilter;
import it.csi.buonodom.buonodombo.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodombo.integration.dao.custom.OperatoreRegionaleDao;
import it.csi.buonodom.buonodombo.integration.dao.custom.RicercaDao;
import it.csi.buonodom.buonodombo.util.Constants;
import it.csi.buonodom.buonodombo.util.ErrorBuilder;
import it.csi.buonodom.buonodombo.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombo.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombo.util.validator.impl.ValidateGenericImpl;

@Service
public class OperatoreRegionaleService extends BaseService {

	@Autowired
	OperatoreRegionaleDao operatoreRegionaleDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	CodParametroDao codParametroDao;

	@Autowired
	RicercaDao ricercaDao;

	public Response creaSportello(ModelSportello nSportello, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String[] param = new String[1];
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			// Ottengo la lista degli sportelli
			List<ModelSportello> listaSportelli = ricercaDao.selectSportelli();
			ModelSportello ultimoSportello = listaSportelli.get(0);
			Date dataFineUltimoSportello = dateFormat.parse(ultimoSportello.getDataFine());
			Calendar c1Usportello = Calendar.getInstance();
			c1Usportello.setTime(dataFineUltimoSportello);

			// Controlli validazione date nuovo sportello
			Date dataInizio = dateFormat.parse(nSportello.getDataInizio());
			Date dataFine = dateFormat.parse(nSportello.getDataFine());
			Calendar c1Nsportello = Calendar.getInstance();
			Calendar c2Nsportello = Calendar.getInstance();
			c1Nsportello.setTime(dataInizio);
			c2Nsportello.setTime(dataFine);

			// Comparazione date
			int compareResult1 = c1Usportello.compareTo(c1Nsportello);
			int compareResult2 = c1Nsportello.compareTo(c2Nsportello);

			// Se DataFine uSportello e' precedente a DataInizio nSportello
			// Se DataInizio e' precedente a DataFine
			if (compareResult1 < 0 && compareResult2 < 0) {
				// Creo nuovo sportello
				operatoreRegionaleDao.creaSportello(nSportello, userInfo.getCodFisc());
				logInfo(metodo, "Sportello inserito correttamente");
				// Init delle directory su server
				initDirectory(nSportello.getCodSportello());
				logInfo(metodo, "Directory create con successo");
				return Response.ok().entity(true).build();
			} else {
				logInfo(metodo, "Errore: datInizio nuovo sportello errata");
				return Response.ok().entity(false).build();
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
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

	private void initDirectory(String sportelloCod) throws DatabaseException {
		String[] param = new String[1];
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();

		String alfabeto[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };
		// Cartella di root del S.O. del server
		StringBuilder path = new StringBuilder();
		path.append(codParametroDao.selectValoreParametroFromCod(Constants.PATH_ARCHIVIAZIONE,
				Constants.PARAMETRO_GENERICO));
		String os = System.getProperty("os.name");
		// Controllo se mi trovo in locale oppure sul server
		boolean locale = false;
		if (os.toLowerCase().contains("win")) {
			path.delete(0, path.length());
			path.append("c:\\temp\\");
			locale = true;
		} else {
			path.append("/");
		}
		try {
			path.append(sportelloCod);
			// Creazione cartella dello sportello
			File dir = new File(path.toString());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// Controllo se Linux o Windows
			if (!locale) { // Linux
				String initialPath = path.toString();
				for (String lettera : alfabeto) {
					path.append("/" + lettera);
					dir = new File(path.toString());
					if (!dir.exists()) {
						dir.mkdirs();
					}
					path.delete(0, path.length());
					path.append(initialPath);
				}
			} else { // Windows
				String initialPath = path.toString();
				for (String lettera : alfabeto) {
					path.append("\\" + lettera);
					dir = new File(path.toString());
					if (!dir.exists()) {
						dir.mkdirs();
					}
					path.delete(0, path.length());
					path.append(initialPath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore creazione directory nuovo sportello", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
		}
	}

}

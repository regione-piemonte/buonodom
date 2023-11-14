/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.business.be.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombo.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombo.dto.ModelDomandeAperta;
import it.csi.buonodom.buonodombo.dto.ModelFiltriDomandeAperte;
import it.csi.buonodom.buonodombo.dto.ModelSportelli;
import it.csi.buonodom.buonodombo.dto.ModelSportello;
import it.csi.buonodom.buonodombo.dto.ModelStati;
import it.csi.buonodom.buonodombo.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombo.exception.DatabaseException;
import it.csi.buonodom.buonodombo.integration.dao.custom.RicercaDao;
import it.csi.buonodom.buonodombo.util.ErrorBuilder;
import it.csi.buonodom.buonodombo.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombo.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombo.util.validator.impl.ValidateGenericImpl;

@Service
public class RicercaService extends BaseService {

	@Autowired
	RicercaDao ricercaDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	public Response getStati(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest,
			String tipoMenu) {
		String[] param = new String[1];
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
//		ErroreDettaglioExt errore = new ErroreDettaglioExt();
		try {
			List<ModelStati> listaStati = ricercaDao.selectStati(tipoMenu);

			return Response.ok().entity(listaStati).build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			param[0] = "Errore riguardante database";
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

//	public Response getSportelli(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
//		String[] param = new String[1];
//		ErrorBuilder error = null;
//		String metodo = new Object() {
//		}.getClass().getEnclosingMethod().getName();
//		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
//		ErroreDettaglioExt errore = new ErroreDettaglioExt();
//		try {
//			List<ModelSportello> listaSportelli = ricercaDao.selectSportelli();
//			ModelSportelli sportelli = new ModelSportelli();
//			sportelli.setSportelli(listaSportelli);
//			ModelSportello s = listaSportelli.stream().filter(f -> f.isCorrente()).findFirst().orElse(null);
//			if(s!=null) {
//				sportelli.setSportelloCorrente(s);
//			} else {
//				sportelli.setSportelloCorrente(listaSportelli.get(0));
//			}
//			
//			return Response.ok().entity(sportelli).build();
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//			param[0] = e.getMessage();
//			logError(metodo, "Errore riguardante database:", e);
//			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
//			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
//		} catch (Exception e) {
//			e.printStackTrace();
//			param[0] = e.getMessage();
//			logError(metodo, "Errore generico ", e);
//			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
//			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
//		}
//		Response esito = error.generateResponseError();
//		return esito;
//	}

	public Response getSportelli(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String[] param = new String[1];
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
//		ErroreDettaglioExt errore = new ErroreDettaglioExt();
		try {
			List<ModelSportello> listaSportelli = ricercaDao.selectSportelli();
			ModelSportelli sportelli = new ModelSportelli();
			sportelli.setSportelli(listaSportelli);
			ModelSportello s = listaSportelli.stream().filter(f -> f.isCorrente()).findFirst().orElse(null);
			if (s != null) {
				sportelli.setSportelloCorrente(s);
			} else {
				sportelli.setSportelloCorrente(listaSportelli.get(0));
			}
			return Response.ok().entity(sportelli).build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			param[0] = "Errore riguardante database";
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

	public Response ricercaDomandeAperte(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest, ModelFiltriDomandeAperte filtri) {
//		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		List<ModelDomandeAperta> lista = new ArrayList<ModelDomandeAperta>();
		String[] param = new String[1];
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
//		ErroreDettaglioExt errore = new ErroreDettaglioExt();
		try {
			List<ModelStati> stati = ricercaDao.selectStati(filtri.getMenu());
			lista = ricercaDao.selectDomandeAperte(filtri, stati);

			return Response.ok().entity(lista).build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			param[0] = "Errore riguardante database";
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

}

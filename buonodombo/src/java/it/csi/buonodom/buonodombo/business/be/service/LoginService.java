/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.business.be.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombo.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombo.dto.ModelProfiliBuonodom;
import it.csi.buonodom.buonodombo.dto.ModelRuoloBuonodom;
import it.csi.buonodom.buonodombo.dto.ModelUserInfoBuonodom;
import it.csi.buonodom.buonodombo.dto.ModelUserRuolo;
import it.csi.buonodom.buonodombo.dto.UserInfo;
import it.csi.buonodom.buonodombo.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombo.exception.DatabaseException;
import it.csi.buonodom.buonodombo.exception.ResponseErrorException;
import it.csi.buonodom.buonodombo.filter.IrideIdAdapterFilter;
import it.csi.buonodom.buonodombo.integration.dao.custom.LoginDao;
import it.csi.buonodom.buonodombo.util.ErrorBuilder;
import it.csi.buonodom.buonodombo.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombo.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombo.util.validator.impl.ValidateGenericImpl;

@Service
public class LoginService extends BaseService {

	@Autowired
	LoginDao loginDao;

	@Autowired
	ValidateGenericImpl validateGeneric;

	public Response login(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		List<ModelUserRuolo> lista = new ArrayList<ModelUserRuolo>();
		String[] param = new String[1];
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		try {

			// TEST, CF SOGGETTO GESTORE ENTE
			// userInfo.setCodFisc("ENTEAA10A11C000F");

			lista = loginDao.selectRuoli(userInfo.getCodFisc());

			if (lista == null || lista.isEmpty()) {
				param[0] = userInfo.getCodFisc();
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR06.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.BAD_REQUEST, listerrorservice),
						"utente non presente");
			}
			ModelUserInfoBuonodom utente = new ModelUserInfoBuonodom();

			utente.setCodFisc(lista.get(0).getCodFisc());
			utente.setNome(lista.get(0).getNome());
			utente.setCognome(lista.get(0).getCognome());
			List<ModelRuoloBuonodom> ruoli = new ArrayList<ModelRuoloBuonodom>();
			for (ModelUserRuolo ruolo : lista) {
				ModelRuoloBuonodom r = new ModelRuoloBuonodom();
				r.setCodRuolo(ruolo.getCodRuolo());
				r.setDescRuolo(ruolo.getDescRuolo());
				List<ModelProfiliBuonodom> profili = loginDao.selectProfili(r.getCodRuolo());
				for (ModelProfiliBuonodom profilo : profili) {
					profilo.setListaAzioni(loginDao.selectAzioni(profilo.getCodProfilo()));
				}
				r.setListaProfili(profili);
				ruoli.add(r);
			}
			utente.setListaRuoli(ruoli);

			utente.setListaEntiGestore(loginDao.selectEntiGestore(userInfo.getCodFisc()));

			return Response.ok().entity(utente).build();
		} catch (DatabaseException e) {
			e.printStackTrace();
			param[0] = e.getMessage();
			logError(metodo, "Errore riguardante database:", e);
			listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR03.getCode(), param));
			error = ErrorBuilder.generateErrorBuilderError(StatusEnum.SERVER_ERROR, listerrorservice);
		} catch (ResponseErrorException e) {
			e.printStackTrace();
			logError(metodo, "Errore generico response:", e);
			error = e.getResponseError();
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

	public Response logout(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String[] param = new String[1];
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		try {
			httpRequest.getSession(false).setMaxInactiveInterval(1);
			httpRequest.getSession().removeAttribute("XSRF_SESSION_TOKEN");
			httpRequest.getSession().removeAttribute("X-XSRF-TOKEN");
			httpRequest.getSession().removeAttribute("XSRF-TOKEN");
			httpRequest.getSession().removeAttribute(IrideIdAdapterFilter.IRIDE_ID_SESSIONATTR);
			httpRequest.getSession().removeAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
			httpRequest.getSession().removeAttribute(IrideIdAdapterFilter.AUTH_ID_MARKER);
//		javax.servlet.http.Cookie[] cookies = httpRequest.getCookies();
			NewCookie[] elencocookie = null;
//		if (cookies!=null) {
//			elencocookie = new NewCookie[cookies.length+1];
//			logInfo("Cookie Numero ",Integer.toString(cookies.length));
//		for (int i = 0; i < cookies.length; i++) {
//			logInfo("Cookie " + i,cookies[i].getName() + " " + cookies[i].getValue());
//			Cookie cookie = new Cookie(cookies[i].getName(), "deleted");
//			logInfo("Cookie " + i,cookie.getName() + " " + cookie.getValue());
//			NewCookie newCookie = new NewCookie(cookie, null, 0, new Date(0), securityContext.isSecure(), true);	
//			elencocookie[i] = newCookie;
//		}
//		Cookie cookie = new Cookie("XSRF-TOKEN", "deleted");
//		logInfo("Cookie ",cookie.getName() + " " + cookie.getValue());
//		NewCookie newCookie = new NewCookie(cookie, null, 0, new Date(0), securityContext.isSecure(), true);		
//		elencocookie[cookies.length] = newCookie;
			// cancello solo Sessionid e XSRF-TOKEN
			elencocookie = new NewCookie[2];
			Cookie cookie = new Cookie("JSESSIONID", "deleted");
			NewCookie newCookie = new NewCookie(cookie, null, 0, new Date(0), securityContext.isSecure(), true);
			elencocookie[0] = newCookie;
			cookie = new Cookie("XSRF-TOKEN", "deleted");
			newCookie = new NewCookie(cookie, null, 0, new Date(0), securityContext.isSecure(), true);
			elencocookie[1] = newCookie;
//		}
			httpRequest.getSession().invalidate();
			return Response.ok().entity(userInfo).cookie(elencocookie).build();
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

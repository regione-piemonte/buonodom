/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.business.be.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombo.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombo.business.be.service.util.FilesEncrypt;
import it.csi.buonodom.buonodombo.dto.ModelRichiesta;
import it.csi.buonodom.buonodombo.dto.UserInfo;
import it.csi.buonodom.buonodombo.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombo.dto.custom.ModelGetAllegato;
import it.csi.buonodom.buonodombo.dto.custom.ResponseGetAllegato;
import it.csi.buonodom.buonodombo.exception.DatabaseException;
import it.csi.buonodom.buonodombo.exception.ResponseErrorException;
import it.csi.buonodom.buonodombo.filter.IrideIdAdapterFilter;
import it.csi.buonodom.buonodombo.integration.dao.custom.AllegatoDao;
import it.csi.buonodom.buonodombo.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodombo.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodombo.integration.service.LogAuditService;
import it.csi.buonodom.buonodombo.util.ErrorBuilder;
import it.csi.buonodom.buonodombo.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombo.util.enumerator.StatusEnum;
import it.csi.buonodom.buonodombo.util.validator.impl.ValidateGenericImpl;

@Service
public class AllegatiService extends BaseService {

	@Autowired
	LogAuditService logaudit;

	@Autowired
	ValidateGenericImpl validateGeneric;

	@Autowired
	private FilesEncrypt filesEncrypt;

	@Autowired
	CodParametroDao parametroDao;

	@Autowired
	AllegatoDao allegatoDao;

	@Autowired
	RichiesteDao richiesteDao;

	public Response getAllegato(String tipoAllegato, String numeroRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		ErrorBuilder error = null;
		String metodo = new Object() {
		}.getClass().getEnclosingMethod().getName();
		List<ErroreDettaglioExt> listerrorservice = new ArrayList<ErroreDettaglioExt>();
		ErroreDettaglioExt errore = new ErroreDettaglioExt();
		String[] param = new String[2];
		try {

			ModelRichiesta richiesta = richiesteDao.selectNumeroRichiesta(numeroRichiesta);
			// ModelAllegato archiviadomanda = new ModelAllegato();
			ResponseGetAllegato domanda = new ResponseGetAllegato();
			if (richiesta != null) {
				domanda = getDomanda(richiesta, tipoAllegato);
				if (domanda == null) {
					param[0] = tipoAllegato;
					param[1] = numeroRichiesta;
					listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR11.getCode(), param));
					throw new ResponseErrorException(
							ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
							"allegato inesistente");
				}
			} else {
				param[0] = numeroRichiesta;
				listerrorservice.add(validateGeneric.getValueGenericError(CodeErrorEnum.ERR10.getCode(), param));
				throw new ResponseErrorException(
						ErrorBuilder.generateErrorBuilderError(StatusEnum.NOT_FOUND, listerrorservice),
						"domanda inesistente");
			}

			return Response.status(200).entity(domanda.getAllegato())
					.header("Content-Disposition", "attachment; filename=\"" + domanda.getFileName() + "\"").build();

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

	private ResponseGetAllegato getDomanda(ModelRichiesta richiesta, String tipoAllegato)
			throws DatabaseException, IOException {
		ModelGetAllegato allegato = allegatoDao.selectGetAllegato(tipoAllegato, richiesta.getDomandaDetId());
		if (allegato == null) {
			return null;
		}
		ResponseGetAllegato getAllegato = new ResponseGetAllegato();
		String os = System.getProperty("os.name");
		boolean locale = false;
		File file = null;
		if (os.toLowerCase().contains("win")) {
			locale = true;
		}
		if (!locale)
			file = new File(allegato.getFilePath() + "/" + allegato.getFileName());
		else
			file = new File(allegato.getFilePath() + "\\" + allegato.getFileName());

		byte[] bytes = Files.readAllBytes(file.toPath());

		byte[] allegatoDecifrato = filesEncrypt.creaFileDeCifratoByte(Cipher.DECRYPT_MODE, bytes);
		getAllegato.setAllegato(allegatoDecifrato);
		getAllegato.setFileName(allegato.getFileName());

		return getAllegato;
	}
}

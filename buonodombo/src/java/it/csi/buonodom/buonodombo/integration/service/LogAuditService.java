/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.buonodom.buonodombo.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombo.dto.custom.LogAudit;
import it.csi.buonodom.buonodombo.exception.DatabaseException;
import it.csi.buonodom.buonodombo.integration.dao.custom.LogAuditDao;

@Service
public class LogAuditService extends BaseService {

	@Autowired
	LogAuditDao logAuditDao;

	public void insertLogAudit(String idApp, String ipAddress, String utente, String operazione, String oggOper,
			String keyOper, String uuid, String requestPayload, String responsePayload, Integer esitoChiamata,
			HttpServletRequest httpRequest) {
		try {
			LogAudit logaudit = new LogAudit();
			logaudit.setIdApp(idApp);
			logaudit.setIpAddress(ipAddress);
			logaudit.setUtente(utente);
			logaudit.setOperazione(operazione);
			logaudit.setOggOper(oggOper);
			logaudit.setKeyOper(keyOper);
			logaudit.setUuid(uuid);
			logaudit.setRequestPayload(requestPayload);
			logaudit.setResponsePayload(responsePayload);
			logaudit.setEsitoChiamata(esitoChiamata);
			logAuditDao.insertLogAudit(logaudit);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertLogAudit(LogAudit logaudit) {
		try {
			logAuditDao.insertLogAudit(logaudit);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

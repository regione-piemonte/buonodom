/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.buonodom.buonodombff.business.be.FilesApi;
import it.csi.buonodom.buonodombff.business.be.service.FilesService;

@Component
public class FilesApiServiceeImpl implements FilesApi {

	@Autowired
	FilesService fileService;

	@Override
	public Response uploadFile(MultipartFormDataInput input, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		// TODO Auto-generated method stub
		return fileService.execute(input, securityContext, httpHeaders, httpRequest);
	}

}

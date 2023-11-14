/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import it.csi.buonodom.buonodombandisrv.dto.Errore;
import it.csi.buonodom.buonodombandisrv.dto.ErroreDettaglio;
import it.csi.buonodom.buonodombandisrv.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombandisrv.util.enumerator.StatusEnum;

public class ErrorBuilder {
	private Errore errore;
	private List<ErroreDettaglioExt> detail;

	private ErrorBuilder() {
		this.errore = new Errore();
		this.detail = new ArrayList<>();
	}

	private Errore build() {
		List<ErroreDettaglio> collect = detail.stream().map(ErroreDettaglioExt::wrap).collect(Collectors.toList());
		errore.setDetail(collect);
		return this.errore;
	}

	public ErrorBuilder link(String link) {
		if (errore.getLinks() == null) {
			errore.setLinks(new ArrayList<>());
		}
		errore.getLinks().add(link);
		return this;
	}

	public ErrorBuilder detail(ErroreDettaglioExt erroreDettaglio) {
		detail.add(erroreDettaglio);
		return this;
	}

	public ErrorBuilder detail(List<ErroreDettaglioExt> erroreDettaglio) {
		if (erroreDettaglio != null) {
			detail = Stream.concat(detail.stream(), erroreDettaglio.stream()).collect(Collectors.toList());

		}
		return this;
	}

	public ErrorBuilder status(Integer status) {
		errore.setStatus(status);
		return this;
	}

	public ErrorBuilder code(String code) {
		errore.setCode(code);
		return this;
	}

	public ErrorBuilder title(String title) {
		errore.setTitle(title);
		return this;
	}

	public List<ErroreDettaglioExt> getDetal() {
		return detail;
	}

	public static ErrorBuilder from(StatusEnum request) {
		return new ErrorBuilder().status(request.getStatus()).code(request.getCode()).title(request.getTitle());
	}

	public static Response generateResponseErrorProvider(StatusEnum request, List<ErroreDettaglioExt> error) {
		ErrorBuilder returnError = new ErrorBuilder().status(request.getStatus()).code(request.getCode())
				.title(request.getTitle()).detail(error);
		return Response.status(request.getStatus().intValue()).entity(returnError.build())
				.type(MediaType.APPLICATION_JSON).build();

	}

	public static ErrorBuilder generateErrorBuilderError(StatusEnum request, List<ErroreDettaglioExt> error) {
		return new ErrorBuilder().status(request.getStatus()).code(request.getCode()).title(request.getTitle())
				.detail(error);
	}

	public Response generateResponseError() {
		return Response.status(errore.getStatus() != null ? errore.getStatus() : StatusEnum.SERVER_ERROR.getStatus())
				.entity(build()).build();
	}

	public static Response generateResponseError(Status status, String code, String message) {
		ErrorBuilder returnError = new ErrorBuilder().status(status.getStatusCode()).code(code).title(message);
		return Response.status(status).entity(returnError).build();
	}

	public static Response generateResponseError(Status status, String message) {
		ErrorBuilder returnError = new ErrorBuilder().status(status.getStatusCode()).title(message);
		return Response.status(status).entity(returnError).build();
	}

	public static Response generateResponseError(Status status, String code, String message,
			List<ErroreDettaglioExt> error) {
		ErrorBuilder returnError = new ErrorBuilder().status(status.getStatusCode()).code(code).title(message)
				.detail(error);
		return Response.status(status).entity(returnError).build();
	}

	public static ErroreDettaglioExt buildErroreDettaglio(String chiave, String valore) {
		ErroreDettaglioExt ed = new ErroreDettaglioExt();
		ed.setChiave(chiave);
		ed.setValore(valore);
		return ed;
	}

	public static List<ErroreDettaglioExt> buidErrorListByFieldNotValidException(String field, String value) {
		return Collections.singletonList(buildErroreDettaglio(field, value));
	}

	public <T> ErrorBuilder detail(List<T> list, Function<T, ErroreDettaglioExt> mapper) {
		if (list != null) {
			list.stream().map(mapper).forEach(d -> detail(d));
		}
		return this;
	}
}

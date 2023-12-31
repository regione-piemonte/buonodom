/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import it.csi.buonodom.buonodombo.dto.Errore;
import it.csi.buonodom.buonodombo.dto.ErroreDettaglio;
import it.csi.buonodom.buonodombo.util.enumerator.StatusEnum;

public class ErrorBuilder {
	private Errore errore;

	public static ErrorBuilder from(StatusEnum aas, Object... args) {
		return new ErrorBuilder().status(aas.getStatus()).code(aas.name()).title(aas.getTitle());
	}

	public static ErrorBuilder from(Status s) {
		return new ErrorBuilder().status(s.getStatusCode()).code(s.name());
	}

	public static ErrorBuilder from(int status, String code) {
		return new ErrorBuilder().status(status).code(code);
	}

	private ErrorBuilder() {
		this.errore = new Errore();
	}

	public ErrorBuilder status(int status) {
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

	public ErrorBuilder dettaglio(String chiave, String valore) {
		ErroreDettaglio dettaglio = new ErroreDettaglio();
		dettaglio.setChiave(chiave);
		dettaglio.setValore(valore);
		return dettaglio(dettaglio);
	}

	private ErrorBuilder dettaglio(ErroreDettaglio dettaglio) {
		if (errore.getDetail() == null) {
			errore.setDetail(new ArrayList<>());
		}
		errore.getDetail().add(dettaglio);
		return this;
	}

	public <T> ErrorBuilder dettagli(List<T> list, Function<T, ErroreDettaglio> mapper) {
		if (list != null) {
			list.stream().map(mapper).forEach(d -> dettaglio(d));
		}
		return this;
	}

	public ErrorBuilder link(String link) {
		if (errore.getLinks() == null) {
			errore.setLinks(new ArrayList<>());
		}
		errore.getLinks().add(link);
		return this;
	}

	public ErrorBuilder throwable(Throwable t) {
		if (t == null) {
			return this;
		}
		this.dettaglio("throwable", t.getMessage());
		addCause(t, 1);
		return this;
	}

	private void addCause(Throwable t, int i) {
		if (t.getCause() != null) {
			this.dettaglio("cause" + i, t.getCause().getMessage());
			i++;
			addCause(t.getCause(), i);
		}
	}

	public Errore build() {
		return this.errore;
	}

	public ErroreRESTException exception() {
		return new ErroreRESTException(this.errore);
	}

	public ErroreRESTException exception(String message) {
		return new ErroreRESTException(this.errore, message);
	}

	public Response response() {
		return this.exception().getResponse();
	}

}

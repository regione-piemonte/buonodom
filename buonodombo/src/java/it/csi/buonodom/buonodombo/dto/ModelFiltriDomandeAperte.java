/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.dto;

import java.util.Arrays;

public class ModelFiltriDomandeAperte {
	String codSportello;
	String numeroDomanda;
	String statoDomanda;
	String destinatario;
	String richiedente;
	String menu;
	long[] entiGestori;
	String statoVerificaEnteGestore;

	public String getCodSportello() {
		return codSportello;
	}

	public void setCodSportello(String codSportello) {
		this.codSportello = codSportello;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getStatoDomanda() {
		return statoDomanda;
	}

	public void setStatoDomanda(String statoDomanda) {
		this.statoDomanda = statoDomanda;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getRichiedente() {
		return richiedente;
	}

	public void setRichiedente(String richiedente) {
		this.richiedente = richiedente;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public long[] getEntiGestori() {
		return entiGestori;
	}

	public void setEntiGestori(long[] entiGestori) {
		this.entiGestori = entiGestori;
	}

	public String getStatoVerificaEnteGestore() {
		return statoVerificaEnteGestore;
	}

	public void setStatoVerificaEnteGestore(String statoVerificaEnteGestore) {
		this.statoVerificaEnteGestore = statoVerificaEnteGestore;
	}

	@Override
	public String toString() {
		return "ModelFiltriDomandeAperte [codSportello=" + codSportello + ", numeroDomanda=" + numeroDomanda
				+ ", statoDomanda=" + statoDomanda + ", destinatario=" + destinatario + ", richiedente=" + richiedente
				+ ", menu=" + menu + ", entiGestori=" + Arrays.toString(entiGestori) + ", statoVerificaEnteGestore="
				+ statoVerificaEnteGestore + "]";
	}

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.dto;

import java.util.Date;

public class ModelVerificheDomanda {

	private String numeroDomanda;
	// private String statoDomanda;
	private Date dataInizioValidita;
	private String misure;
	private String tipo;
//	private String incompatibilita;
//	private String isee;
//	private Date dataVerificaIsee;
//	private String ateco;
//	private Date dataVerificaAteco;
//	private boolean controlloMisura = false;
//	private boolean controlloIsee = false;
//	private boolean controlloAteco = false;
//	private boolean controlloInc = false;
	private String fonte;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

//	public String getStatoDomanda() {
//		return statoDomanda;
//	}
//	public void setStatoDomanda(String statoDomanda) {
//		this.statoDomanda = statoDomanda;
//	}
	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public String getMisure() {
		return misure;
	}

	public void setMisure(String misure) {
		this.misure = misure;
	}

//	public String getIncompatibilita() {
//		return incompatibilita;
//	}
//	public void setIncompatibilita(String incompatibilita) {
//		this.incompatibilita = incompatibilita;
//	}
//	public String getIsee() {
//		return isee;
//	}
//	public void setIsee(String isee) {
//		this.isee = isee;
//	}
//	public Date getDataVerificaIsee() {
//		return dataVerificaIsee;
//	}
//	public void setDataVerificaIsee(Date dataVerificaIsee) {
//		this.dataVerificaIsee = dataVerificaIsee;
//	}
//	public String getAteco() {
//		return ateco;
//	}
//	public void setAteco(String ateco) {
//		this.ateco = ateco;
//	}
//	public Date getDataVerificaAteco() {
//		return dataVerificaAteco;
//	}
//	public void setDataVerificaAteco(Date dataVerificaAteco) {
//		this.dataVerificaAteco = dataVerificaAteco;
//	}
//	public boolean isControlloMisura() {
//		return controlloMisura;
//	}
//	public void setControlloMisura(boolean controlloMisura) {
//		this.controlloMisura = controlloMisura;
//	}
//	public boolean isControlloIsee() {
//		return controlloIsee;
//	}
//	public void setControlloIsee(boolean controlloIsee) {
//		this.controlloIsee = controlloIsee;
//	}
//	public boolean isControlloAteco() {
//		return controlloAteco;
//	}
//	public void setControlloAteco(boolean controlloAteco) {
//		this.controlloAteco = controlloAteco;
//	}
//	public boolean isControlloInc() {
//		return controlloInc;
//	}
//	public void setControlloInc(boolean controlloInc) {
//		this.controlloInc = controlloInc;
//	}
	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	@Override
	public String toString() {
		return "ModelVerificheDomanda [numeroDomanda=" + numeroDomanda + ", dataInizioValidita=" + dataInizioValidita
				+ ", misure=" + misure + ", tipo=" + tipo + ", fonte=" + fonte + "]";
	}

}

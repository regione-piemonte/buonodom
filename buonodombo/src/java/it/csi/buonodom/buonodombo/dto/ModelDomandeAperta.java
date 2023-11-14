/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.dto;

public class ModelDomandeAperta {
	Integer idDomanda;
	String numeroDomanda;
	String cfDestinatario;
	String nomeDestinatario;
	String cognomeDestinatario;
	String cfRichiedente;
	String nomeRichiedente;
	String cognomeRichiedente;
	ModelStati stato;
	String dataDomanda;
	ModelEnteGestore enteGestore;
	ModelVerifiche verifiche;

	public Integer getIdDomanda() {
		return idDomanda;
	}

	public void setIdDomanda(Integer idDomanda) {
		this.idDomanda = idDomanda;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getCfDestinatario() {
		return cfDestinatario;
	}

	public void setCfDestinatario(String cfDestinatario) {
		this.cfDestinatario = cfDestinatario;
	}

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}

	public String getCognomeDestinatario() {
		return cognomeDestinatario;
	}

	public void setCognomeDestinatario(String cognomeDestinatario) {
		this.cognomeDestinatario = cognomeDestinatario;
	}

	public String getCfRichiedente() {
		return cfRichiedente;
	}

	public void setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
	}

	public String getNomeRichiedente() {
		return nomeRichiedente;
	}

	public void setNomeRichiedente(String nomeRichiedente) {
		this.nomeRichiedente = nomeRichiedente;
	}

	public String getCognomeRichiedente() {
		return cognomeRichiedente;
	}

	public void setCognomeRichiedente(String cognomeRichiedente) {
		this.cognomeRichiedente = cognomeRichiedente;
	}

	public ModelStati getStato() {
		return stato;
	}

	public void setStato(ModelStati stato) {
		this.stato = stato;
	}

	public String getDataDomanda() {
		return dataDomanda;
	}

	public void setDataDomanda(String dataDomanda) {
		this.dataDomanda = dataDomanda;
	}

	public ModelVerifiche getVerifiche() {
		return verifiche;
	}

	public void setVerifiche(ModelVerifiche verifiche) {
		this.verifiche = verifiche;
	}

	public ModelEnteGestore getEnteGestore() {
		return enteGestore;
	}

	public void setEnteGestore(ModelEnteGestore enteGestore) {
		this.enteGestore = enteGestore;
	}

	@Override
	public String toString() {

		return "ModelDomandeAperta [idDomanda=" + idDomanda + ", numeroDomanda=" + numeroDomanda + ", cfDestinatario="
				+ cfDestinatario + ", nomeDestinatario=" + nomeDestinatario + ", cognomeDestinatario="
				+ cognomeDestinatario + ", cfRichiedente=" + cfRichiedente + ", nomeRichiedente=" + nomeRichiedente
				+ ", cognomeRichiedente=" + cognomeRichiedente + ", stato=" + stato + ", dataDomanda=" + dataDomanda
				+ ", enteGestore=" + enteGestore + ", verifiche=" + verifiche + "]";
	}

}

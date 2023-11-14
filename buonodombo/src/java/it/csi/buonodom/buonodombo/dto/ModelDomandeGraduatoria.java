/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.dto;

import java.math.BigDecimal;

public class ModelDomandeGraduatoria {
	private Long posizioneGraduatoria;
	private String numeroDomanda;
	private String destinatarioNome;
	private String destinatarioCognome;
	private String destinatarioCF;
	private Long punteggioSociale;
	private BigDecimal isee;
	private String dataInvioDomanda;
	private String statoDomanda;
	private BigDecimal importoTotale;
	private String areaInterna;
	private ModelVerifiche verifiche;

	public Long getPosizioneGraduatoria() {
		return posizioneGraduatoria;
	}

	public void setPosizioneGraduatoria(Long posizioneGraduatoria) {
		this.posizioneGraduatoria = posizioneGraduatoria;
	}

	public String getNumeroDomanda() {
		return numeroDomanda;
	}

	public void setNumeroDomanda(String numeroDomanda) {
		this.numeroDomanda = numeroDomanda;
	}

	public String getDestinatarioNome() {
		return destinatarioNome;
	}

	public void setDestinatarioNome(String destinatarioNome) {
		this.destinatarioNome = destinatarioNome;
	}

	public String getDestinatarioCognome() {
		return destinatarioCognome;
	}

	public void setDestinatarioCognome(String destinatarioCognome) {
		this.destinatarioCognome = destinatarioCognome;
	}

	public String getDestinatarioCF() {
		return destinatarioCF;
	}

	public void setDestinatarioCF(String destinatarioCF) {
		this.destinatarioCF = destinatarioCF;
	}

	public Long getPunteggioSociale() {
		return punteggioSociale;
	}

	public void setPunteggioSociale(Long punteggioSociale) {
		this.punteggioSociale = punteggioSociale;
	}

	public BigDecimal getIsee() {
		return isee;
	}

	public void setIsee(BigDecimal isee) {
		this.isee = isee;
	}

	public String getDataInvioDomanda() {
		return dataInvioDomanda;
	}

	public void setDataInvioDomanda(String dataInvioDomanda) {
		this.dataInvioDomanda = dataInvioDomanda;
	}

	public String getStatoDomanda() {
		return statoDomanda;
	}

	public void setStatoDomanda(String statoDomanda) {
		this.statoDomanda = statoDomanda;
	}

	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}

	public String getAreaInterna() {
		return areaInterna;
	}

	public void setAreaInterna(String areaInterna) {
		this.areaInterna = areaInterna;
	}

	public ModelVerifiche getVerifiche() {
		return verifiche;
	}

	public void setVerifiche(ModelVerifiche verifiche) {
		this.verifiche = verifiche;
	}

	@Override
	public String toString() {
		return "ModelDomandeGraduatoria [posizioneGraduatoria=" + posizioneGraduatoria + ", numeroDomanda="
				+ numeroDomanda + ", destinatarioNome=" + destinatarioNome + ", destinatarioCognome="
				+ destinatarioCognome + ", destinatarioCF=" + destinatarioCF + ", punteggioSociale=" + punteggioSociale
				+ ", isee=" + isee + ", dataInvioDomanda=" + dataInvioDomanda + ", statoDomanda=" + statoDomanda
				+ ", importoTotale=" + importoTotale + ", areaInterna=" + areaInterna + "]";
	}

}

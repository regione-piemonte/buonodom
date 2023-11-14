/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/

package it.csi.buonodom.buonodombo.external.ateco;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per fallimento complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="fallimento"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codOrganoGiudiz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="curatoreFallim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataChiusuraFallim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataFallimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataNascPersCausaSF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataSentenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="denomSoggCausaFallim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrOrganoGiudiz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrProvTribunFallim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idAzienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idFallimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idFonteDato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idPersona" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomePersSoggCausaFallim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numFallimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numSentenzaFallim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvTrinunFallim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tribunaleFallim" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fallimento", propOrder = { "codOrganoGiudiz", "curatoreFallim", "dataChiusuraFallim", "dataFallimento",
		"dataNascPersCausaSF", "dataSentenza", "denomSoggCausaFallim", "descrOrganoGiudiz", "descrProvTribunFallim",
		"idAzienda", "idFallimento", "idFonteDato", "idPersona", "nomePersSoggCausaFallim", "numFallimento",
		"numSentenzaFallim", "siglaProvTrinunFallim", "tribunaleFallim" })
public class Fallimento {

	protected String codOrganoGiudiz;
	protected String curatoreFallim;
	protected String dataChiusuraFallim;
	protected String dataFallimento;
	protected String dataNascPersCausaSF;
	protected String dataSentenza;
	protected String denomSoggCausaFallim;
	protected String descrOrganoGiudiz;
	protected String descrProvTribunFallim;
	protected String idAzienda;
	protected String idFallimento;
	protected String idFonteDato;
	protected String idPersona;
	protected String nomePersSoggCausaFallim;
	protected String numFallimento;
	protected String numSentenzaFallim;
	protected String siglaProvTrinunFallim;
	protected String tribunaleFallim;

	/**
	 * Recupera il valore della proprietà codOrganoGiudiz.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodOrganoGiudiz() {
		return codOrganoGiudiz;
	}

	/**
	 * Imposta il valore della proprietà codOrganoGiudiz.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodOrganoGiudiz(String value) {
		this.codOrganoGiudiz = value;
	}

	/**
	 * Recupera il valore della proprietà curatoreFallim.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCuratoreFallim() {
		return curatoreFallim;
	}

	/**
	 * Imposta il valore della proprietà curatoreFallim.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCuratoreFallim(String value) {
		this.curatoreFallim = value;
	}

	/**
	 * Recupera il valore della proprietà dataChiusuraFallim.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataChiusuraFallim() {
		return dataChiusuraFallim;
	}

	/**
	 * Imposta il valore della proprietà dataChiusuraFallim.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataChiusuraFallim(String value) {
		this.dataChiusuraFallim = value;
	}

	/**
	 * Recupera il valore della proprietà dataFallimento.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataFallimento() {
		return dataFallimento;
	}

	/**
	 * Imposta il valore della proprietà dataFallimento.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataFallimento(String value) {
		this.dataFallimento = value;
	}

	/**
	 * Recupera il valore della proprietà dataNascPersCausaSF.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataNascPersCausaSF() {
		return dataNascPersCausaSF;
	}

	/**
	 * Imposta il valore della proprietà dataNascPersCausaSF.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataNascPersCausaSF(String value) {
		this.dataNascPersCausaSF = value;
	}

	/**
	 * Recupera il valore della proprietà dataSentenza.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataSentenza() {
		return dataSentenza;
	}

	/**
	 * Imposta il valore della proprietà dataSentenza.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataSentenza(String value) {
		this.dataSentenza = value;
	}

	/**
	 * Recupera il valore della proprietà denomSoggCausaFallim.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDenomSoggCausaFallim() {
		return denomSoggCausaFallim;
	}

	/**
	 * Imposta il valore della proprietà denomSoggCausaFallim.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDenomSoggCausaFallim(String value) {
		this.denomSoggCausaFallim = value;
	}

	/**
	 * Recupera il valore della proprietà descrOrganoGiudiz.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrOrganoGiudiz() {
		return descrOrganoGiudiz;
	}

	/**
	 * Imposta il valore della proprietà descrOrganoGiudiz.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrOrganoGiudiz(String value) {
		this.descrOrganoGiudiz = value;
	}

	/**
	 * Recupera il valore della proprietà descrProvTribunFallim.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrProvTribunFallim() {
		return descrProvTribunFallim;
	}

	/**
	 * Imposta il valore della proprietà descrProvTribunFallim.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrProvTribunFallim(String value) {
		this.descrProvTribunFallim = value;
	}

	/**
	 * Recupera il valore della proprietà idAzienda.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdAzienda() {
		return idAzienda;
	}

	/**
	 * Imposta il valore della proprietà idAzienda.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdAzienda(String value) {
		this.idAzienda = value;
	}

	/**
	 * Recupera il valore della proprietà idFallimento.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdFallimento() {
		return idFallimento;
	}

	/**
	 * Imposta il valore della proprietà idFallimento.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdFallimento(String value) {
		this.idFallimento = value;
	}

	/**
	 * Recupera il valore della proprietà idFonteDato.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdFonteDato() {
		return idFonteDato;
	}

	/**
	 * Imposta il valore della proprietà idFonteDato.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdFonteDato(String value) {
		this.idFonteDato = value;
	}

	/**
	 * Recupera il valore della proprietà idPersona.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdPersona() {
		return idPersona;
	}

	/**
	 * Imposta il valore della proprietà idPersona.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdPersona(String value) {
		this.idPersona = value;
	}

	/**
	 * Recupera il valore della proprietà nomePersSoggCausaFallim.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNomePersSoggCausaFallim() {
		return nomePersSoggCausaFallim;
	}

	/**
	 * Imposta il valore della proprietà nomePersSoggCausaFallim.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNomePersSoggCausaFallim(String value) {
		this.nomePersSoggCausaFallim = value;
	}

	/**
	 * Recupera il valore della proprietà numFallimento.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumFallimento() {
		return numFallimento;
	}

	/**
	 * Imposta il valore della proprietà numFallimento.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumFallimento(String value) {
		this.numFallimento = value;
	}

	/**
	 * Recupera il valore della proprietà numSentenzaFallim.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumSentenzaFallim() {
		return numSentenzaFallim;
	}

	/**
	 * Imposta il valore della proprietà numSentenzaFallim.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumSentenzaFallim(String value) {
		this.numSentenzaFallim = value;
	}

	/**
	 * Recupera il valore della proprietà siglaProvTrinunFallim.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaProvTrinunFallim() {
		return siglaProvTrinunFallim;
	}

	/**
	 * Imposta il valore della proprietà siglaProvTrinunFallim.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaProvTrinunFallim(String value) {
		this.siglaProvTrinunFallim = value;
	}

	/**
	 * Recupera il valore della proprietà tribunaleFallim.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTribunaleFallim() {
		return tribunaleFallim;
	}

	/**
	 * Imposta il valore della proprietà tribunaleFallim.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setTribunaleFallim(String value) {
		this.tribunaleFallim = value;
	}

}

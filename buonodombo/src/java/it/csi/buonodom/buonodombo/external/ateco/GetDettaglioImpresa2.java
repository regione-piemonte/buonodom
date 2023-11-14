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
 * Classe Java per getDettaglioImpresa2 complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="getDettaglioImpresa2"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="utente" type="{http://business.aaeporch.aaep.csi.it/}utente" minOccurs="0"/&gt;
 *         &lt;element name="fonte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="statoCessSEDE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvCCIAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numIscrizREA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDettaglioImpresa2", propOrder = { "utente", "fonte", "codiceFiscale", "statoCessSEDE",
		"siglaProvCCIAA", "numIscrizREA" })
public class GetDettaglioImpresa2 {

	protected Utente utente;
	protected String fonte;
	protected String codiceFiscale;
	protected String statoCessSEDE;
	protected String siglaProvCCIAA;
	protected String numIscrizREA;

	/**
	 * Recupera il valore della proprietà utente.
	 * 
	 * @return possible object is {@link Utente }
	 * 
	 */
	public Utente getUtente() {
		return utente;
	}

	/**
	 * Imposta il valore della proprietà utente.
	 * 
	 * @param value allowed object is {@link Utente }
	 * 
	 */
	public void setUtente(Utente value) {
		this.utente = value;
	}

	/**
	 * Recupera il valore della proprietà fonte.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFonte() {
		return fonte;
	}

	/**
	 * Imposta il valore della proprietà fonte.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setFonte(String value) {
		this.fonte = value;
	}

	/**
	 * Recupera il valore della proprietà codiceFiscale.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	/**
	 * Imposta il valore della proprietà codiceFiscale.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodiceFiscale(String value) {
		this.codiceFiscale = value;
	}

	/**
	 * Recupera il valore della proprietà statoCessSEDE.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getStatoCessSEDE() {
		return statoCessSEDE;
	}

	/**
	 * Imposta il valore della proprietà statoCessSEDE.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setStatoCessSEDE(String value) {
		this.statoCessSEDE = value;
	}

	/**
	 * Recupera il valore della proprietà siglaProvCCIAA.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaProvCCIAA() {
		return siglaProvCCIAA;
	}

	/**
	 * Imposta il valore della proprietà siglaProvCCIAA.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaProvCCIAA(String value) {
		this.siglaProvCCIAA = value;
	}

	/**
	 * Recupera il valore della proprietà numIscrizREA.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumIscrizREA() {
		return numIscrizREA;
	}

	/**
	 * Imposta il valore della proprietà numIscrizREA.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumIscrizREA(String value) {
		this.numIscrizREA = value;
	}

}

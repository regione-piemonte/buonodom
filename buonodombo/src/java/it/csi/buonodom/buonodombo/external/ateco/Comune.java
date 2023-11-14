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
 * Classe Java per comune complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="comune"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="descComune" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idComune" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="istatComune" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comune", propOrder = { "descComune", "idComune", "istatComune" })
public class Comune {

	protected String descComune;
	protected String idComune;
	protected String istatComune;

	/**
	 * Recupera il valore della proprietà descComune.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescComune() {
		return descComune;
	}

	/**
	 * Imposta il valore della proprietà descComune.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescComune(String value) {
		this.descComune = value;
	}

	/**
	 * Recupera il valore della proprietà idComune.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdComune() {
		return idComune;
	}

	/**
	 * Imposta il valore della proprietà idComune.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdComune(String value) {
		this.idComune = value;
	}

	/**
	 * Recupera il valore della proprietà istatComune.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIstatComune() {
		return istatComune;
	}

	/**
	 * Imposta il valore della proprietà istatComune.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIstatComune(String value) {
		this.istatComune = value;
	}

}

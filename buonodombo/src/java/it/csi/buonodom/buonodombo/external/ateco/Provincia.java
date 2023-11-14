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
 * Classe Java per provincia complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="provincia"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="descProvincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="istatProvincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "provincia", propOrder = { "descProvincia", "istatProvincia", "siglaProvincia" })
public class Provincia {

	protected String descProvincia;
	protected String istatProvincia;
	protected String siglaProvincia;

	/**
	 * Recupera il valore della proprietà descProvincia.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescProvincia() {
		return descProvincia;
	}

	/**
	 * Imposta il valore della proprietà descProvincia.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescProvincia(String value) {
		this.descProvincia = value;
	}

	/**
	 * Recupera il valore della proprietà istatProvincia.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIstatProvincia() {
		return istatProvincia;
	}

	/**
	 * Imposta il valore della proprietà istatProvincia.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIstatProvincia(String value) {
		this.istatProvincia = value;
	}

	/**
	 * Recupera il valore della proprietà siglaProvincia.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	/**
	 * Imposta il valore della proprietà siglaProvincia.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaProvincia(String value) {
		this.siglaProvincia = value;
	}

}

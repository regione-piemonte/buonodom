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
 * Classe Java per contatti complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="contatti"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroVerde" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sitoWeb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contatti", propOrder = { "email", "fax", "numeroVerde", "sitoWeb", "telefono" })
public class Contatti {

	protected String email;
	protected String fax;
	protected String numeroVerde;
	protected String sitoWeb;
	protected String telefono;

	/**
	 * Recupera il valore della proprietà email.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Imposta il valore della proprietà email.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setEmail(String value) {
		this.email = value;
	}

	/**
	 * Recupera il valore della proprietà fax.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * Imposta il valore della proprietà fax.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setFax(String value) {
		this.fax = value;
	}

	/**
	 * Recupera il valore della proprietà numeroVerde.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumeroVerde() {
		return numeroVerde;
	}

	/**
	 * Imposta il valore della proprietà numeroVerde.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumeroVerde(String value) {
		this.numeroVerde = value;
	}

	/**
	 * Recupera il valore della proprietà sitoWeb.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSitoWeb() {
		return sitoWeb;
	}

	/**
	 * Imposta il valore della proprietà sitoWeb.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSitoWeb(String value) {
		this.sitoWeb = value;
	}

	/**
	 * Recupera il valore della proprietà telefono.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Imposta il valore della proprietà telefono.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setTelefono(String value) {
		this.telefono = value;
	}

}

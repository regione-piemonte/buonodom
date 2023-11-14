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
 * Classe Java per dettagliAlboArtigiano complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="dettagliAlboArtigiano"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dataDeliberaIscrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrIterIscrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="flgIterIscrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroIscrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="provinciaIscrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dettagliAlboArtigiano", propOrder = { "dataDeliberaIscrizione", "descrIterIscrizione",
		"flgIterIscrizione", "numeroIscrizione", "provinciaIscrizione" })
public class DettagliAlboArtigiano {

	protected String dataDeliberaIscrizione;
	protected String descrIterIscrizione;
	protected String flgIterIscrizione;
	protected String numeroIscrizione;
	protected String provinciaIscrizione;

	/**
	 * Recupera il valore della proprietà dataDeliberaIscrizione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataDeliberaIscrizione() {
		return dataDeliberaIscrizione;
	}

	/**
	 * Imposta il valore della proprietà dataDeliberaIscrizione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataDeliberaIscrizione(String value) {
		this.dataDeliberaIscrizione = value;
	}

	/**
	 * Recupera il valore della proprietà descrIterIscrizione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrIterIscrizione() {
		return descrIterIscrizione;
	}

	/**
	 * Imposta il valore della proprietà descrIterIscrizione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrIterIscrizione(String value) {
		this.descrIterIscrizione = value;
	}

	/**
	 * Recupera il valore della proprietà flgIterIscrizione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFlgIterIscrizione() {
		return flgIterIscrizione;
	}

	/**
	 * Imposta il valore della proprietà flgIterIscrizione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setFlgIterIscrizione(String value) {
		this.flgIterIscrizione = value;
	}

	/**
	 * Recupera il valore della proprietà numeroIscrizione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumeroIscrizione() {
		return numeroIscrizione;
	}

	/**
	 * Imposta il valore della proprietà numeroIscrizione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumeroIscrizione(String value) {
		this.numeroIscrizione = value;
	}

	/**
	 * Recupera il valore della proprietà provinciaIscrizione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getProvinciaIscrizione() {
		return provinciaIscrizione;
	}

	/**
	 * Imposta il valore della proprietà provinciaIscrizione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setProvinciaIscrizione(String value) {
		this.provinciaIscrizione = value;
	}

}

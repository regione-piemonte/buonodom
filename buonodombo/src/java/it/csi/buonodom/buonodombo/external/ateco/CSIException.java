/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/

package it.csi.buonodom.buonodombo.external.ateco;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per CSIException complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="CSIException"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nestedExcClassName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="stackTraceMessage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="nestedExcMsg" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CSIException", propOrder = { "nestedExcClassName", "stackTraceMessage", "nestedExcMsg" })
public class CSIException {

	@XmlElement(required = true, nillable = true)
	protected String nestedExcClassName;
	@XmlElement(required = true, nillable = true)
	protected String stackTraceMessage;
	@XmlElement(required = true, nillable = true)
	protected String nestedExcMsg;

	/**
	 * Recupera il valore della proprietà nestedExcClassName.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNestedExcClassName() {
		return nestedExcClassName;
	}

	/**
	 * Imposta il valore della proprietà nestedExcClassName.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNestedExcClassName(String value) {
		this.nestedExcClassName = value;
	}

	/**
	 * Recupera il valore della proprietà stackTraceMessage.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getStackTraceMessage() {
		return stackTraceMessage;
	}

	/**
	 * Imposta il valore della proprietà stackTraceMessage.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setStackTraceMessage(String value) {
		this.stackTraceMessage = value;
	}

	/**
	 * Recupera il valore della proprietà nestedExcMsg.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNestedExcMsg() {
		return nestedExcMsg;
	}

	/**
	 * Imposta il valore della proprietà nestedExcMsg.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNestedExcMsg(String value) {
		this.nestedExcMsg = value;
	}

}

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
 * Classe Java per getDettaglioPersonaInfocResponse complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="getDettaglioPersonaInfocResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="return" type="{http://business.aaeporch.aaep.csi.it/}persona" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDettaglioPersonaInfocResponse", propOrder = { "_return" })
public class GetDettaglioPersonaInfocResponse {

	@XmlElement(name = "return")
	protected Persona _return;

	/**
	 * Recupera il valore della proprietà return.
	 * 
	 * @return possible object is {@link Persona }
	 * 
	 */
	public Persona getReturn() {
		return _return;
	}

	/**
	 * Imposta il valore della proprietà return.
	 * 
	 * @param value allowed object is {@link Persona }
	 * 
	 */
	public void setReturn(Persona value) {
		this._return = value;
	}

}

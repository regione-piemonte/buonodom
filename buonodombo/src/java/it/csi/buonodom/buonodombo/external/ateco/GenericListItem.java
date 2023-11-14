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
 * Classe Java per genericListItem complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="genericListItem"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="descrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="priorita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "genericListItem", propOrder = { "descrizione", "priorita" })
public class GenericListItem {

	protected String descrizione;
	protected String priorita;

	/**
	 * Recupera il valore della proprietà descrizione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Imposta il valore della proprietà descrizione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrizione(String value) {
		this.descrizione = value;
	}

	/**
	 * Recupera il valore della proprietà priorita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPriorita() {
		return priorita;
	}

	/**
	 * Imposta il valore della proprietà priorita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setPriorita(String value) {
		this.priorita = value;
	}

}

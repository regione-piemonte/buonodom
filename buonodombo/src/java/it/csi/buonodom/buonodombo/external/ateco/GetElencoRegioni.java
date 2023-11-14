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
 * Classe Java per getElencoRegioni complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="getElencoRegioni"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="utente" type="{http://business.aaeporch.aaep.csi.it/}utente" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getElencoRegioni", propOrder = { "utente" })
public class GetElencoRegioni {

	protected Utente utente;

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

}

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
 * Classe Java per utente complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="utente"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codiceFiscaleUtente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cognomeUtente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idUtente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomeUtente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "utente", propOrder = { "codiceFiscaleUtente", "cognomeUtente", "idUtente", "nomeUtente" })
public class Utente {

	protected String codiceFiscaleUtente;
	protected String cognomeUtente;
	protected String idUtente;
	protected String nomeUtente;

	/**
	 * Recupera il valore della proprietà codiceFiscaleUtente.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceFiscaleUtente() {
		return codiceFiscaleUtente;
	}

	/**
	 * Imposta il valore della proprietà codiceFiscaleUtente.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodiceFiscaleUtente(String value) {
		this.codiceFiscaleUtente = value;
	}

	/**
	 * Recupera il valore della proprietà cognomeUtente.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCognomeUtente() {
		return cognomeUtente;
	}

	/**
	 * Imposta il valore della proprietà cognomeUtente.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCognomeUtente(String value) {
		this.cognomeUtente = value;
	}

	/**
	 * Recupera il valore della proprietà idUtente.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdUtente() {
		return idUtente;
	}

	/**
	 * Imposta il valore della proprietà idUtente.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdUtente(String value) {
		this.idUtente = value;
	}

	/**
	 * Recupera il valore della proprietà nomeUtente.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNomeUtente() {
		return nomeUtente;
	}

	/**
	 * Imposta il valore della proprietà nomeUtente.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNomeUtente(String value) {
		this.nomeUtente = value;
	}

}

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
 * Classe Java per cercaPersoneInfoc complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="cercaPersoneInfoc"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="utente" type="{http://business.aaeporch.aaep.csi.it/}utente" minOccurs="0"/&gt;
 *         &lt;element name="codiceFiscaleAzienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="soloRappresentantiLegali" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cercaPersoneInfoc", propOrder = { "utente", "codiceFiscaleAzienda", "soloRappresentantiLegali" })
public class CercaPersoneInfoc {

	protected Utente utente;
	protected String codiceFiscaleAzienda;
	protected Boolean soloRappresentantiLegali;

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
	 * Recupera il valore della proprietà codiceFiscaleAzienda.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceFiscaleAzienda() {
		return codiceFiscaleAzienda;
	}

	/**
	 * Imposta il valore della proprietà codiceFiscaleAzienda.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodiceFiscaleAzienda(String value) {
		this.codiceFiscaleAzienda = value;
	}

	/**
	 * Recupera il valore della proprietà soloRappresentantiLegali.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isSoloRappresentantiLegali() {
		return soloRappresentantiLegali;
	}

	/**
	 * Imposta il valore della proprietà soloRappresentantiLegali.
	 * 
	 * @param value allowed object is {@link Boolean }
	 * 
	 */
	public void setSoloRappresentantiLegali(Boolean value) {
		this.soloRappresentantiLegali = value;
	}

}

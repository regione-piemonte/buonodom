/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/

package it.csi.buonodom.buonodombo.external.ateco;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per getDettaglioSede complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="getDettaglioSede"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="utente" type="{http://business.aaeporch.aaep.csi.it/}utente" minOccurs="0"/&gt;
 *         &lt;element name="fonte" type="{http://business.aaeporch.aaep.csi.it/}tipologiaFonte" minOccurs="0"/&gt;
 *         &lt;element name="sede" type="{http://business.aaeporch.aaep.csi.it/}sede" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDettaglioSede", propOrder = { "utente", "fonte", "sede" })
public class GetDettaglioSede {

	protected Utente utente;
	@XmlSchemaType(name = "string")
	protected TipologiaFonte fonte;
	protected Sede sede;

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
	 * Recupera il valore della proprietà fonte.
	 * 
	 * @return possible object is {@link TipologiaFonte }
	 * 
	 */
	public TipologiaFonte getFonte() {
		return fonte;
	}

	/**
	 * Imposta il valore della proprietà fonte.
	 * 
	 * @param value allowed object is {@link TipologiaFonte }
	 * 
	 */
	public void setFonte(TipologiaFonte value) {
		this.fonte = value;
	}

	/**
	 * Recupera il valore della proprietà sede.
	 * 
	 * @return possible object is {@link Sede }
	 * 
	 */
	public Sede getSede() {
		return sede;
	}

	/**
	 * Imposta il valore della proprietà sede.
	 * 
	 * @param value allowed object is {@link Sede }
	 * 
	 */
	public void setSede(Sede value) {
		this.sede = value;
	}

}

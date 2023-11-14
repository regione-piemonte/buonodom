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
 * Classe Java per cercaVariazioniAnagrafiche complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="cercaVariazioniAnagrafiche"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="utente" type="{http://business.aaeporch.aaep.csi.it/}utente" minOccurs="0"/&gt;
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataInizioPeriodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataFinePeriodo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipologiaVariazioneAnagrafica" type="{http://business.aaeporch.aaep.csi.it/}tipologiaVariazioneAnagrafica" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cercaVariazioniAnagrafiche", propOrder = { "utente", "codiceFiscale", "dataInizioPeriodo",
		"dataFinePeriodo", "tipologiaVariazioneAnagrafica" })
public class CercaVariazioniAnagrafiche {

	protected Utente utente;
	protected String codiceFiscale;
	protected String dataInizioPeriodo;
	protected String dataFinePeriodo;
	@XmlSchemaType(name = "string")
	protected TipologiaVariazioneAnagrafica tipologiaVariazioneAnagrafica;

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
	 * Recupera il valore della proprietà codiceFiscale.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	/**
	 * Imposta il valore della proprietà codiceFiscale.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodiceFiscale(String value) {
		this.codiceFiscale = value;
	}

	/**
	 * Recupera il valore della proprietà dataInizioPeriodo.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataInizioPeriodo() {
		return dataInizioPeriodo;
	}

	/**
	 * Imposta il valore della proprietà dataInizioPeriodo.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataInizioPeriodo(String value) {
		this.dataInizioPeriodo = value;
	}

	/**
	 * Recupera il valore della proprietà dataFinePeriodo.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataFinePeriodo() {
		return dataFinePeriodo;
	}

	/**
	 * Imposta il valore della proprietà dataFinePeriodo.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataFinePeriodo(String value) {
		this.dataFinePeriodo = value;
	}

	/**
	 * Recupera il valore della proprietà tipologiaVariazioneAnagrafica.
	 * 
	 * @return possible object is {@link TipologiaVariazioneAnagrafica }
	 * 
	 */
	public TipologiaVariazioneAnagrafica getTipologiaVariazioneAnagrafica() {
		return tipologiaVariazioneAnagrafica;
	}

	/**
	 * Imposta il valore della proprietà tipologiaVariazioneAnagrafica.
	 * 
	 * @param value allowed object is {@link TipologiaVariazioneAnagrafica }
	 * 
	 */
	public void setTipologiaVariazioneAnagrafica(TipologiaVariazioneAnagrafica value) {
		this.tipologiaVariazioneAnagrafica = value;
	}

}

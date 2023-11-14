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
 * Classe Java per rappresentanteLegale complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="rappresentanteLegale"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cap" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codComuneResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrComuneResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="indirizzo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nomeVia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroCivico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipoVia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rappresentanteLegale", propOrder = { "cap", "codComuneNascita", "codComuneResidenza", "codiceFiscale",
		"cognome", "dataNascita", "descrComuneNascita", "descrComuneResidenza", "indirizzo", "nome", "nomeVia",
		"numeroCivico", "sesso", "tipoVia" })
public class RappresentanteLegale {

	protected String cap;
	protected String codComuneNascita;
	protected String codComuneResidenza;
	protected String codiceFiscale;
	protected String cognome;
	protected String dataNascita;
	protected String descrComuneNascita;
	protected String descrComuneResidenza;
	protected String indirizzo;
	protected String nome;
	protected String nomeVia;
	protected String numeroCivico;
	protected String sesso;
	protected String tipoVia;

	/**
	 * Recupera il valore della proprietà cap.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCap() {
		return cap;
	}

	/**
	 * Imposta il valore della proprietà cap.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCap(String value) {
		this.cap = value;
	}

	/**
	 * Recupera il valore della proprietà codComuneNascita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodComuneNascita() {
		return codComuneNascita;
	}

	/**
	 * Imposta il valore della proprietà codComuneNascita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodComuneNascita(String value) {
		this.codComuneNascita = value;
	}

	/**
	 * Recupera il valore della proprietà codComuneResidenza.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodComuneResidenza() {
		return codComuneResidenza;
	}

	/**
	 * Imposta il valore della proprietà codComuneResidenza.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodComuneResidenza(String value) {
		this.codComuneResidenza = value;
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
	 * Recupera il valore della proprietà cognome.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * Imposta il valore della proprietà cognome.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCognome(String value) {
		this.cognome = value;
	}

	/**
	 * Recupera il valore della proprietà dataNascita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataNascita() {
		return dataNascita;
	}

	/**
	 * Imposta il valore della proprietà dataNascita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataNascita(String value) {
		this.dataNascita = value;
	}

	/**
	 * Recupera il valore della proprietà descrComuneNascita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrComuneNascita() {
		return descrComuneNascita;
	}

	/**
	 * Imposta il valore della proprietà descrComuneNascita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrComuneNascita(String value) {
		this.descrComuneNascita = value;
	}

	/**
	 * Recupera il valore della proprietà descrComuneResidenza.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrComuneResidenza() {
		return descrComuneResidenza;
	}

	/**
	 * Imposta il valore della proprietà descrComuneResidenza.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrComuneResidenza(String value) {
		this.descrComuneResidenza = value;
	}

	/**
	 * Recupera il valore della proprietà indirizzo.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIndirizzo() {
		return indirizzo;
	}

	/**
	 * Imposta il valore della proprietà indirizzo.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIndirizzo(String value) {
		this.indirizzo = value;
	}

	/**
	 * Recupera il valore della proprietà nome.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Imposta il valore della proprietà nome.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNome(String value) {
		this.nome = value;
	}

	/**
	 * Recupera il valore della proprietà nomeVia.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNomeVia() {
		return nomeVia;
	}

	/**
	 * Imposta il valore della proprietà nomeVia.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNomeVia(String value) {
		this.nomeVia = value;
	}

	/**
	 * Recupera il valore della proprietà numeroCivico.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumeroCivico() {
		return numeroCivico;
	}

	/**
	 * Imposta il valore della proprietà numeroCivico.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumeroCivico(String value) {
		this.numeroCivico = value;
	}

	/**
	 * Recupera il valore della proprietà sesso.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSesso() {
		return sesso;
	}

	/**
	 * Imposta il valore della proprietà sesso.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSesso(String value) {
		this.sesso = value;
	}

	/**
	 * Recupera il valore della proprietà tipoVia.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTipoVia() {
		return tipoVia;
	}

	/**
	 * Imposta il valore della proprietà tipoVia.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setTipoVia(String value) {
		this.tipoVia = value;
	}

}

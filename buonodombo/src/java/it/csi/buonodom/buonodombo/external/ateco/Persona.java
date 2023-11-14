/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/

package it.csi.buonodom.buonodombo.external.ateco;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per persona complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="persona"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrTipoPersona" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idAzienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idFonteDato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idPersona" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="listaCariche" type="{http://business.aaeporch.aaep.csi.it/}carica" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipoPersona" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "persona", propOrder = { "codiceFiscale", "cognome", "descrTipoPersona", "idAzienda", "idFonteDato",
		"idPersona", "listaCariche", "nome", "tipoPersona" })
@XmlSeeAlso({ PersonaINFOC.class, PersonaINFOCListItem.class, ReferenteSILP.class })
public class Persona {

	protected String codiceFiscale;
	protected String cognome;
	protected String descrTipoPersona;
	protected String idAzienda;
	protected String idFonteDato;
	protected String idPersona;
	@XmlElement(nillable = true)
	protected List<Carica> listaCariche;
	protected String nome;
	protected String tipoPersona;

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
	 * Recupera il valore della proprietà descrTipoPersona.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrTipoPersona() {
		return descrTipoPersona;
	}

	/**
	 * Imposta il valore della proprietà descrTipoPersona.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrTipoPersona(String value) {
		this.descrTipoPersona = value;
	}

	/**
	 * Recupera il valore della proprietà idAzienda.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdAzienda() {
		return idAzienda;
	}

	/**
	 * Imposta il valore della proprietà idAzienda.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdAzienda(String value) {
		this.idAzienda = value;
	}

	/**
	 * Recupera il valore della proprietà idFonteDato.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdFonteDato() {
		return idFonteDato;
	}

	/**
	 * Imposta il valore della proprietà idFonteDato.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdFonteDato(String value) {
		this.idFonteDato = value;
	}

	/**
	 * Recupera il valore della proprietà idPersona.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdPersona() {
		return idPersona;
	}

	/**
	 * Imposta il valore della proprietà idPersona.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdPersona(String value) {
		this.idPersona = value;
	}

	/**
	 * Gets the value of the listaCariche property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the listaCariche property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getListaCariche().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Carica }
	 * 
	 * 
	 */
	public List<Carica> getListaCariche() {
		if (listaCariche == null) {
			listaCariche = new ArrayList<Carica>();
		}
		return this.listaCariche;
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
	 * Recupera il valore della proprietà tipoPersona.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTipoPersona() {
		return tipoPersona;
	}

	/**
	 * Imposta il valore della proprietà tipoPersona.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setTipoPersona(String value) {
		this.tipoPersona = value;
	}

}

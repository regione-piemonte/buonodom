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
 * Classe Java per attivitaEconomica complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="attivitaEconomica"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="annoDiRiferimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codImportanzaAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codImportanzaRI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codiceATECO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataInizio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrImportanzaAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrImportanzaRI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attivitaEconomica", propOrder = { "annoDiRiferimento", "codImportanzaAA", "codImportanzaRI",
		"codiceATECO", "dataCessazione", "dataInizio", "descrImportanzaAA", "descrImportanzaRI", "descrizione" })
public class AttivitaEconomica {

	protected String annoDiRiferimento;
	protected String codImportanzaAA;
	protected String codImportanzaRI;
	protected String codiceATECO;
	protected String dataCessazione;
	protected String dataInizio;
	protected String descrImportanzaAA;
	protected String descrImportanzaRI;
	protected String descrizione;

	/**
	 * Recupera il valore della proprietà annoDiRiferimento.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAnnoDiRiferimento() {
		return annoDiRiferimento;
	}

	/**
	 * Imposta il valore della proprietà annoDiRiferimento.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setAnnoDiRiferimento(String value) {
		this.annoDiRiferimento = value;
	}

	/**
	 * Recupera il valore della proprietà codImportanzaAA.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodImportanzaAA() {
		return codImportanzaAA;
	}

	/**
	 * Imposta il valore della proprietà codImportanzaAA.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodImportanzaAA(String value) {
		this.codImportanzaAA = value;
	}

	/**
	 * Recupera il valore della proprietà codImportanzaRI.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodImportanzaRI() {
		return codImportanzaRI;
	}

	/**
	 * Imposta il valore della proprietà codImportanzaRI.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodImportanzaRI(String value) {
		this.codImportanzaRI = value;
	}

	/**
	 * Recupera il valore della proprietà codiceATECO.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceATECO() {
		return codiceATECO;
	}

	/**
	 * Imposta il valore della proprietà codiceATECO.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodiceATECO(String value) {
		this.codiceATECO = value;
	}

	/**
	 * Recupera il valore della proprietà dataCessazione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataCessazione() {
		return dataCessazione;
	}

	/**
	 * Imposta il valore della proprietà dataCessazione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataCessazione(String value) {
		this.dataCessazione = value;
	}

	/**
	 * Recupera il valore della proprietà dataInizio.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataInizio() {
		return dataInizio;
	}

	/**
	 * Imposta il valore della proprietà dataInizio.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataInizio(String value) {
		this.dataInizio = value;
	}

	/**
	 * Recupera il valore della proprietà descrImportanzaAA.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrImportanzaAA() {
		return descrImportanzaAA;
	}

	/**
	 * Imposta il valore della proprietà descrImportanzaAA.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrImportanzaAA(String value) {
		this.descrImportanzaAA = value;
	}

	/**
	 * Recupera il valore della proprietà descrImportanzaRI.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrImportanzaRI() {
		return descrImportanzaRI;
	}

	/**
	 * Imposta il valore della proprietà descrImportanzaRI.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrImportanzaRI(String value) {
		this.descrImportanzaRI = value;
	}

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

}

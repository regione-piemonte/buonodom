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
 * Classe Java per cessazione complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="cessazione"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cessazione" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="codCausaleCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataDenunciaCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrCausaleCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cessazione", propOrder = { "cessazione", "codCausaleCessazione", "dataCessazione",
		"dataDenunciaCessazione", "descrCausaleCessazione" })
public class Cessazione {

	protected Boolean cessazione;
	protected String codCausaleCessazione;
	protected String dataCessazione;
	protected String dataDenunciaCessazione;
	protected String descrCausaleCessazione;

	/**
	 * Recupera il valore della proprietà cessazione.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isCessazione() {
		return cessazione;
	}

	/**
	 * Imposta il valore della proprietà cessazione.
	 * 
	 * @param value allowed object is {@link Boolean }
	 * 
	 */
	public void setCessazione(Boolean value) {
		this.cessazione = value;
	}

	/**
	 * Recupera il valore della proprietà codCausaleCessazione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodCausaleCessazione() {
		return codCausaleCessazione;
	}

	/**
	 * Imposta il valore della proprietà codCausaleCessazione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodCausaleCessazione(String value) {
		this.codCausaleCessazione = value;
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
	 * Recupera il valore della proprietà dataDenunciaCessazione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataDenunciaCessazione() {
		return dataDenunciaCessazione;
	}

	/**
	 * Imposta il valore della proprietà dataDenunciaCessazione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataDenunciaCessazione(String value) {
		this.dataDenunciaCessazione = value;
	}

	/**
	 * Recupera il valore della proprietà descrCausaleCessazione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrCausaleCessazione() {
		return descrCausaleCessazione;
	}

	/**
	 * Imposta il valore della proprietà descrCausaleCessazione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrCausaleCessazione(String value) {
		this.descrCausaleCessazione = value;
	}

}

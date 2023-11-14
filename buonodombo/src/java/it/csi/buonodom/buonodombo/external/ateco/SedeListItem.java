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
 * Classe Java per sedeListItem complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="sedeListItem"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://business.aaeporch.aaep.csi.it/}sede"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="descrStatoEstero" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="localita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numIscrizREA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvCCIAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sedeListItem", propOrder = { "descrStatoEstero", "localita", "numIscrizREA", "siglaProv",
		"siglaProvCCIAA" })
public class SedeListItem extends Sede {

	protected String descrStatoEstero;
	protected String localita;
	protected String numIscrizREA;
	protected String siglaProv;
	protected String siglaProvCCIAA;

	/**
	 * Recupera il valore della proprietà descrStatoEstero.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrStatoEstero() {
		return descrStatoEstero;
	}

	/**
	 * Imposta il valore della proprietà descrStatoEstero.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrStatoEstero(String value) {
		this.descrStatoEstero = value;
	}

	/**
	 * Recupera il valore della proprietà localita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLocalita() {
		return localita;
	}

	/**
	 * Imposta il valore della proprietà localita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setLocalita(String value) {
		this.localita = value;
	}

	/**
	 * Recupera il valore della proprietà numIscrizREA.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumIscrizREA() {
		return numIscrizREA;
	}

	/**
	 * Imposta il valore della proprietà numIscrizREA.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumIscrizREA(String value) {
		this.numIscrizREA = value;
	}

	/**
	 * Recupera il valore della proprietà siglaProv.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaProv() {
		return siglaProv;
	}

	/**
	 * Imposta il valore della proprietà siglaProv.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaProv(String value) {
		this.siglaProv = value;
	}

	/**
	 * Recupera il valore della proprietà siglaProvCCIAA.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaProvCCIAA() {
		return siglaProvCCIAA;
	}

	/**
	 * Imposta il valore della proprietà siglaProvCCIAA.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaProvCCIAA(String value) {
		this.siglaProvCCIAA = value;
	}

}

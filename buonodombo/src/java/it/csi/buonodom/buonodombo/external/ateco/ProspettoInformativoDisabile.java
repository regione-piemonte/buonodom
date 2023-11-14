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
 * Classe Java per prospettoInformativoDisabile complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="prospettoInformativoDisabile"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="annoRiferimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="controparte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataClassificazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataInvio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataProtocollo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrTipoProspDisab" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idProspDisab" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numClassificazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numProtocollo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "prospettoInformativoDisabile", propOrder = { "annoRiferimento", "controparte", "dataClassificazione",
		"dataInvio", "dataProtocollo", "descrTipoProspDisab", "idProspDisab", "numClassificazione", "numProtocollo",
		"siglaProv" })
public class ProspettoInformativoDisabile {

	protected String annoRiferimento;
	protected String controparte;
	protected String dataClassificazione;
	protected String dataInvio;
	protected String dataProtocollo;
	protected String descrTipoProspDisab;
	protected String idProspDisab;
	protected String numClassificazione;
	protected String numProtocollo;
	protected String siglaProv;

	/**
	 * Recupera il valore della proprietà annoRiferimento.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAnnoRiferimento() {
		return annoRiferimento;
	}

	/**
	 * Imposta il valore della proprietà annoRiferimento.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setAnnoRiferimento(String value) {
		this.annoRiferimento = value;
	}

	/**
	 * Recupera il valore della proprietà controparte.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getControparte() {
		return controparte;
	}

	/**
	 * Imposta il valore della proprietà controparte.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setControparte(String value) {
		this.controparte = value;
	}

	/**
	 * Recupera il valore della proprietà dataClassificazione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataClassificazione() {
		return dataClassificazione;
	}

	/**
	 * Imposta il valore della proprietà dataClassificazione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataClassificazione(String value) {
		this.dataClassificazione = value;
	}

	/**
	 * Recupera il valore della proprietà dataInvio.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataInvio() {
		return dataInvio;
	}

	/**
	 * Imposta il valore della proprietà dataInvio.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataInvio(String value) {
		this.dataInvio = value;
	}

	/**
	 * Recupera il valore della proprietà dataProtocollo.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataProtocollo() {
		return dataProtocollo;
	}

	/**
	 * Imposta il valore della proprietà dataProtocollo.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataProtocollo(String value) {
		this.dataProtocollo = value;
	}

	/**
	 * Recupera il valore della proprietà descrTipoProspDisab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrTipoProspDisab() {
		return descrTipoProspDisab;
	}

	/**
	 * Imposta il valore della proprietà descrTipoProspDisab.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrTipoProspDisab(String value) {
		this.descrTipoProspDisab = value;
	}

	/**
	 * Recupera il valore della proprietà idProspDisab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdProspDisab() {
		return idProspDisab;
	}

	/**
	 * Imposta il valore della proprietà idProspDisab.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdProspDisab(String value) {
		this.idProspDisab = value;
	}

	/**
	 * Recupera il valore della proprietà numClassificazione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumClassificazione() {
		return numClassificazione;
	}

	/**
	 * Imposta il valore della proprietà numClassificazione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumClassificazione(String value) {
		this.numClassificazione = value;
	}

	/**
	 * Recupera il valore della proprietà numProtocollo.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumProtocollo() {
		return numProtocollo;
	}

	/**
	 * Imposta il valore della proprietà numProtocollo.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumProtocollo(String value) {
		this.numProtocollo = value;
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

}

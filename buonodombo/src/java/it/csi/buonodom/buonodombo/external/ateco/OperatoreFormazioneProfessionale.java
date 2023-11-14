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
 * Classe Java per operatoreFormazioneProfessionale complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="operatoreFormazioneProfessionale"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://business.aaeporch.aaep.csi.it/}impresa"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codGruppoOperatore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codOperatore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codTipoOperatore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrGruppoOperatore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrTipoOperatore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "operatoreFormazioneProfessionale", propOrder = { "codGruppoOperatore", "codOperatore",
		"codTipoOperatore", "descrGruppoOperatore", "descrTipoOperatore" })
public class OperatoreFormazioneProfessionale extends Impresa {

	protected String codGruppoOperatore;
	protected String codOperatore;
	protected String codTipoOperatore;
	protected String descrGruppoOperatore;
	protected String descrTipoOperatore;

	/**
	 * Recupera il valore della proprietà codGruppoOperatore.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodGruppoOperatore() {
		return codGruppoOperatore;
	}

	/**
	 * Imposta il valore della proprietà codGruppoOperatore.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodGruppoOperatore(String value) {
		this.codGruppoOperatore = value;
	}

	/**
	 * Recupera il valore della proprietà codOperatore.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodOperatore() {
		return codOperatore;
	}

	/**
	 * Imposta il valore della proprietà codOperatore.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodOperatore(String value) {
		this.codOperatore = value;
	}

	/**
	 * Recupera il valore della proprietà codTipoOperatore.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodTipoOperatore() {
		return codTipoOperatore;
	}

	/**
	 * Imposta il valore della proprietà codTipoOperatore.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodTipoOperatore(String value) {
		this.codTipoOperatore = value;
	}

	/**
	 * Recupera il valore della proprietà descrGruppoOperatore.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrGruppoOperatore() {
		return descrGruppoOperatore;
	}

	/**
	 * Imposta il valore della proprietà descrGruppoOperatore.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrGruppoOperatore(String value) {
		this.descrGruppoOperatore = value;
	}

	/**
	 * Recupera il valore della proprietà descrTipoOperatore.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrTipoOperatore() {
		return descrTipoOperatore;
	}

	/**
	 * Imposta il valore della proprietà descrTipoOperatore.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrTipoOperatore(String value) {
		this.descrTipoOperatore = value;
	}

}

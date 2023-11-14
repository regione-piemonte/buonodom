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
 * Classe Java per sedeAAEP complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="sedeAAEP"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://business.aaeporch.aaep.csi.it/}sede"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codSedeInailComp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizioneAttivitaSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvUL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sedeAAEP", propOrder = { "codSedeInailComp", "descrizioneAttivitaSede", "siglaProvUL" })
public class SedeAAEP extends Sede {

	protected String codSedeInailComp;
	protected String descrizioneAttivitaSede;
	protected String siglaProvUL;

	/**
	 * Recupera il valore della proprietà codSedeInailComp.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodSedeInailComp() {
		return codSedeInailComp;
	}

	/**
	 * Imposta il valore della proprietà codSedeInailComp.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodSedeInailComp(String value) {
		this.codSedeInailComp = value;
	}

	/**
	 * Recupera il valore della proprietà descrizioneAttivitaSede.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrizioneAttivitaSede() {
		return descrizioneAttivitaSede;
	}

	/**
	 * Imposta il valore della proprietà descrizioneAttivitaSede.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrizioneAttivitaSede(String value) {
		this.descrizioneAttivitaSede = value;
	}

	/**
	 * Recupera il valore della proprietà siglaProvUL.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaProvUL() {
		return siglaProvUL;
	}

	/**
	 * Imposta il valore della proprietà siglaProvUL.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaProvUL(String value) {
		this.siglaProvUL = value;
	}

}

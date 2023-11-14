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
 * Classe Java per sezioneSpeciale complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="sezioneSpeciale"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codAlbo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codSezione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codiceSezSpec" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataFine" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataInizio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="flColtDir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idAzienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idFonteDato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idSezioneSpeciale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sezioneSpeciale", propOrder = { "codAlbo", "codSezione", "codiceSezSpec", "dataFine", "dataInizio",
		"flColtDir", "idAzienda", "idFonteDato", "idSezioneSpeciale" })
public class SezioneSpeciale {

	protected String codAlbo;
	protected String codSezione;
	protected String codiceSezSpec;
	protected String dataFine;
	protected String dataInizio;
	protected String flColtDir;
	protected String idAzienda;
	protected String idFonteDato;
	protected String idSezioneSpeciale;

	/**
	 * Recupera il valore della proprietà codAlbo.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodAlbo() {
		return codAlbo;
	}

	/**
	 * Imposta il valore della proprietà codAlbo.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodAlbo(String value) {
		this.codAlbo = value;
	}

	/**
	 * Recupera il valore della proprietà codSezione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodSezione() {
		return codSezione;
	}

	/**
	 * Imposta il valore della proprietà codSezione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodSezione(String value) {
		this.codSezione = value;
	}

	/**
	 * Recupera il valore della proprietà codiceSezSpec.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceSezSpec() {
		return codiceSezSpec;
	}

	/**
	 * Imposta il valore della proprietà codiceSezSpec.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodiceSezSpec(String value) {
		this.codiceSezSpec = value;
	}

	/**
	 * Recupera il valore della proprietà dataFine.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataFine() {
		return dataFine;
	}

	/**
	 * Imposta il valore della proprietà dataFine.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataFine(String value) {
		this.dataFine = value;
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
	 * Recupera il valore della proprietà flColtDir.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFlColtDir() {
		return flColtDir;
	}

	/**
	 * Imposta il valore della proprietà flColtDir.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setFlColtDir(String value) {
		this.flColtDir = value;
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
	 * Recupera il valore della proprietà idSezioneSpeciale.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdSezioneSpeciale() {
		return idSezioneSpeciale;
	}

	/**
	 * Imposta il valore della proprietà idSezioneSpeciale.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdSezioneSpeciale(String value) {
		this.idSezioneSpeciale = value;
	}

}

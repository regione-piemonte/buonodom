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
 * Classe Java per dettagliCameraCommercio complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="dettagliCameraCommercio"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="anno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataAggiornamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataCancellazioneREA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataIscrizioneREA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataIscrizioneRegistroImprese" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numIscrizionePosizioneREA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numRegistroImprese" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numero" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="provincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvinciaIscrizioneREA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tribunale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dettagliCameraCommercio", propOrder = { "anno", "dataAggiornamento", "dataCancellazioneREA",
		"dataIscrizioneREA", "dataIscrizioneRegistroImprese", "numIscrizionePosizioneREA", "numRegistroImprese",
		"numero", "provincia", "siglaProvincia", "siglaProvinciaIscrizioneREA", "tribunale" })
public class DettagliCameraCommercio {

	protected String anno;
	protected String dataAggiornamento;
	protected String dataCancellazioneREA;
	protected String dataIscrizioneREA;
	protected String dataIscrizioneRegistroImprese;
	protected String numIscrizionePosizioneREA;
	protected String numRegistroImprese;
	protected String numero;
	protected String provincia;
	protected String siglaProvincia;
	protected String siglaProvinciaIscrizioneREA;
	protected String tribunale;

	/**
	 * Recupera il valore della proprietà anno.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAnno() {
		return anno;
	}

	/**
	 * Imposta il valore della proprietà anno.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setAnno(String value) {
		this.anno = value;
	}

	/**
	 * Recupera il valore della proprietà dataAggiornamento.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataAggiornamento() {
		return dataAggiornamento;
	}

	/**
	 * Imposta il valore della proprietà dataAggiornamento.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataAggiornamento(String value) {
		this.dataAggiornamento = value;
	}

	/**
	 * Recupera il valore della proprietà dataCancellazioneREA.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataCancellazioneREA() {
		return dataCancellazioneREA;
	}

	/**
	 * Imposta il valore della proprietà dataCancellazioneREA.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataCancellazioneREA(String value) {
		this.dataCancellazioneREA = value;
	}

	/**
	 * Recupera il valore della proprietà dataIscrizioneREA.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataIscrizioneREA() {
		return dataIscrizioneREA;
	}

	/**
	 * Imposta il valore della proprietà dataIscrizioneREA.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataIscrizioneREA(String value) {
		this.dataIscrizioneREA = value;
	}

	/**
	 * Recupera il valore della proprietà dataIscrizioneRegistroImprese.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataIscrizioneRegistroImprese() {
		return dataIscrizioneRegistroImprese;
	}

	/**
	 * Imposta il valore della proprietà dataIscrizioneRegistroImprese.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataIscrizioneRegistroImprese(String value) {
		this.dataIscrizioneRegistroImprese = value;
	}

	/**
	 * Recupera il valore della proprietà numIscrizionePosizioneREA.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumIscrizionePosizioneREA() {
		return numIscrizionePosizioneREA;
	}

	/**
	 * Imposta il valore della proprietà numIscrizionePosizioneREA.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumIscrizionePosizioneREA(String value) {
		this.numIscrizionePosizioneREA = value;
	}

	/**
	 * Recupera il valore della proprietà numRegistroImprese.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumRegistroImprese() {
		return numRegistroImprese;
	}

	/**
	 * Imposta il valore della proprietà numRegistroImprese.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumRegistroImprese(String value) {
		this.numRegistroImprese = value;
	}

	/**
	 * Recupera il valore della proprietà numero.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * Imposta il valore della proprietà numero.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumero(String value) {
		this.numero = value;
	}

	/**
	 * Recupera il valore della proprietà provincia.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * Imposta il valore della proprietà provincia.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setProvincia(String value) {
		this.provincia = value;
	}

	/**
	 * Recupera il valore della proprietà siglaProvincia.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	/**
	 * Imposta il valore della proprietà siglaProvincia.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaProvincia(String value) {
		this.siglaProvincia = value;
	}

	/**
	 * Recupera il valore della proprietà siglaProvinciaIscrizioneREA.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaProvinciaIscrizioneREA() {
		return siglaProvinciaIscrizioneREA;
	}

	/**
	 * Imposta il valore della proprietà siglaProvinciaIscrizioneREA.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaProvinciaIscrizioneREA(String value) {
		this.siglaProvinciaIscrizioneREA = value;
	}

	/**
	 * Recupera il valore della proprietà tribunale.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTribunale() {
		return tribunale;
	}

	/**
	 * Imposta il valore della proprietà tribunale.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setTribunale(String value) {
		this.tribunale = value;
	}

}

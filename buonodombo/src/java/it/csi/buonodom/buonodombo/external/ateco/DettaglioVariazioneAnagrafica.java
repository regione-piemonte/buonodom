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
 * Classe Java per dettaglioVariazioneAnagrafica complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="dettaglioVariazioneAnagrafica"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dataVariaz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizioneNewIdFonteDato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizioneOldIdFonteDato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizioneTipoVariazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idAzienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idTipoVariazione" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="newDenominazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="newDescrizioneNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="newDescrizioneStatoAttiv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="newIdFonteDato" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="newIdNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="newIdStatoAttiv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="oldDenominazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="oldDescrizioneNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="oldDescrizioneStatoAttiv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="oldIdFonteDato" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="oldIdNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="oldIdStatoAttiv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dettaglioVariazioneAnagrafica", propOrder = { "dataVariaz", "descrizioneNewIdFonteDato",
		"descrizioneOldIdFonteDato", "descrizioneTipoVariazione", "idAzienda", "idTipoVariazione", "newDenominazione",
		"newDescrizioneNaturaGiuridica", "newDescrizioneStatoAttiv", "newIdFonteDato", "newIdNaturaGiuridica",
		"newIdStatoAttiv", "oldDenominazione", "oldDescrizioneNaturaGiuridica", "oldDescrizioneStatoAttiv",
		"oldIdFonteDato", "oldIdNaturaGiuridica", "oldIdStatoAttiv" })
public class DettaglioVariazioneAnagrafica {

	protected String dataVariaz;
	protected String descrizioneNewIdFonteDato;
	protected String descrizioneOldIdFonteDato;
	protected String descrizioneTipoVariazione;
	protected String idAzienda;
	protected long idTipoVariazione;
	protected String newDenominazione;
	protected String newDescrizioneNaturaGiuridica;
	protected String newDescrizioneStatoAttiv;
	protected long newIdFonteDato;
	protected String newIdNaturaGiuridica;
	protected String newIdStatoAttiv;
	protected String oldDenominazione;
	protected String oldDescrizioneNaturaGiuridica;
	protected String oldDescrizioneStatoAttiv;
	protected long oldIdFonteDato;
	protected String oldIdNaturaGiuridica;
	protected String oldIdStatoAttiv;

	/**
	 * Recupera il valore della proprietà dataVariaz.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataVariaz() {
		return dataVariaz;
	}

	/**
	 * Imposta il valore della proprietà dataVariaz.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataVariaz(String value) {
		this.dataVariaz = value;
	}

	/**
	 * Recupera il valore della proprietà descrizioneNewIdFonteDato.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrizioneNewIdFonteDato() {
		return descrizioneNewIdFonteDato;
	}

	/**
	 * Imposta il valore della proprietà descrizioneNewIdFonteDato.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrizioneNewIdFonteDato(String value) {
		this.descrizioneNewIdFonteDato = value;
	}

	/**
	 * Recupera il valore della proprietà descrizioneOldIdFonteDato.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrizioneOldIdFonteDato() {
		return descrizioneOldIdFonteDato;
	}

	/**
	 * Imposta il valore della proprietà descrizioneOldIdFonteDato.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrizioneOldIdFonteDato(String value) {
		this.descrizioneOldIdFonteDato = value;
	}

	/**
	 * Recupera il valore della proprietà descrizioneTipoVariazione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrizioneTipoVariazione() {
		return descrizioneTipoVariazione;
	}

	/**
	 * Imposta il valore della proprietà descrizioneTipoVariazione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrizioneTipoVariazione(String value) {
		this.descrizioneTipoVariazione = value;
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
	 * Recupera il valore della proprietà idTipoVariazione.
	 * 
	 */
	public long getIdTipoVariazione() {
		return idTipoVariazione;
	}

	/**
	 * Imposta il valore della proprietà idTipoVariazione.
	 * 
	 */
	public void setIdTipoVariazione(long value) {
		this.idTipoVariazione = value;
	}

	/**
	 * Recupera il valore della proprietà newDenominazione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNewDenominazione() {
		return newDenominazione;
	}

	/**
	 * Imposta il valore della proprietà newDenominazione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNewDenominazione(String value) {
		this.newDenominazione = value;
	}

	/**
	 * Recupera il valore della proprietà newDescrizioneNaturaGiuridica.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNewDescrizioneNaturaGiuridica() {
		return newDescrizioneNaturaGiuridica;
	}

	/**
	 * Imposta il valore della proprietà newDescrizioneNaturaGiuridica.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNewDescrizioneNaturaGiuridica(String value) {
		this.newDescrizioneNaturaGiuridica = value;
	}

	/**
	 * Recupera il valore della proprietà newDescrizioneStatoAttiv.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNewDescrizioneStatoAttiv() {
		return newDescrizioneStatoAttiv;
	}

	/**
	 * Imposta il valore della proprietà newDescrizioneStatoAttiv.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNewDescrizioneStatoAttiv(String value) {
		this.newDescrizioneStatoAttiv = value;
	}

	/**
	 * Recupera il valore della proprietà newIdFonteDato.
	 * 
	 */
	public long getNewIdFonteDato() {
		return newIdFonteDato;
	}

	/**
	 * Imposta il valore della proprietà newIdFonteDato.
	 * 
	 */
	public void setNewIdFonteDato(long value) {
		this.newIdFonteDato = value;
	}

	/**
	 * Recupera il valore della proprietà newIdNaturaGiuridica.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNewIdNaturaGiuridica() {
		return newIdNaturaGiuridica;
	}

	/**
	 * Imposta il valore della proprietà newIdNaturaGiuridica.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNewIdNaturaGiuridica(String value) {
		this.newIdNaturaGiuridica = value;
	}

	/**
	 * Recupera il valore della proprietà newIdStatoAttiv.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNewIdStatoAttiv() {
		return newIdStatoAttiv;
	}

	/**
	 * Imposta il valore della proprietà newIdStatoAttiv.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNewIdStatoAttiv(String value) {
		this.newIdStatoAttiv = value;
	}

	/**
	 * Recupera il valore della proprietà oldDenominazione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOldDenominazione() {
		return oldDenominazione;
	}

	/**
	 * Imposta il valore della proprietà oldDenominazione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setOldDenominazione(String value) {
		this.oldDenominazione = value;
	}

	/**
	 * Recupera il valore della proprietà oldDescrizioneNaturaGiuridica.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOldDescrizioneNaturaGiuridica() {
		return oldDescrizioneNaturaGiuridica;
	}

	/**
	 * Imposta il valore della proprietà oldDescrizioneNaturaGiuridica.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setOldDescrizioneNaturaGiuridica(String value) {
		this.oldDescrizioneNaturaGiuridica = value;
	}

	/**
	 * Recupera il valore della proprietà oldDescrizioneStatoAttiv.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOldDescrizioneStatoAttiv() {
		return oldDescrizioneStatoAttiv;
	}

	/**
	 * Imposta il valore della proprietà oldDescrizioneStatoAttiv.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setOldDescrizioneStatoAttiv(String value) {
		this.oldDescrizioneStatoAttiv = value;
	}

	/**
	 * Recupera il valore della proprietà oldIdFonteDato.
	 * 
	 */
	public long getOldIdFonteDato() {
		return oldIdFonteDato;
	}

	/**
	 * Imposta il valore della proprietà oldIdFonteDato.
	 * 
	 */
	public void setOldIdFonteDato(long value) {
		this.oldIdFonteDato = value;
	}

	/**
	 * Recupera il valore della proprietà oldIdNaturaGiuridica.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOldIdNaturaGiuridica() {
		return oldIdNaturaGiuridica;
	}

	/**
	 * Imposta il valore della proprietà oldIdNaturaGiuridica.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setOldIdNaturaGiuridica(String value) {
		this.oldIdNaturaGiuridica = value;
	}

	/**
	 * Recupera il valore della proprietà oldIdStatoAttiv.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOldIdStatoAttiv() {
		return oldIdStatoAttiv;
	}

	/**
	 * Imposta il valore della proprietà oldIdStatoAttiv.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setOldIdStatoAttiv(String value) {
		this.oldIdStatoAttiv = value;
	}

}

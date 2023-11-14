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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per variazioneAnagrafica complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="variazioneAnagrafica"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cciaa" type="{http://business.aaeporch.aaep.csi.it/}dettagliCameraCommercio" minOccurs="0"/&gt;
 *         &lt;element name="codiceCausaleCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codiciATECO" type="{http://business.aaeporch.aaep.csi.it/}attivitaEconomica" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="dataCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataCostituzione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataFineValidita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataInizioValidita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="denominazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrIscrSezArtigiani" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizioneCausaleCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizioneFontedato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizioneNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizioneStatoAttiv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizioneTipoAzienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dettagliVariazioniAnagrafiche" type="{http://business.aaeporch.aaep.csi.it/}dettaglioVariazioneAnagrafica" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="idAzienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idFonte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idStatoAttiv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idTipoAzienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="iscrSezArtigiani" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="oggettoSociale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="partitaIva" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "variazioneAnagrafica", propOrder = { "cciaa", "codiceCausaleCessazione", "codiceFiscale",
		"codiciATECO", "dataCessazione", "dataCostituzione", "dataFineValidita", "dataInizioValidita", "denominazione",
		"descrIscrSezArtigiani", "descrizioneCausaleCessazione", "descrizioneFontedato", "descrizioneNaturaGiuridica",
		"descrizioneStatoAttiv", "descrizioneTipoAzienda", "dettagliVariazioniAnagrafiche", "idAzienda", "idFonte",
		"idNaturaGiuridica", "idStatoAttiv", "idTipoAzienda", "iscrSezArtigiani", "oggettoSociale", "partitaIva" })
public class VariazioneAnagrafica {

	protected DettagliCameraCommercio cciaa;
	protected String codiceCausaleCessazione;
	protected String codiceFiscale;
	@XmlElement(nillable = true)
	protected List<AttivitaEconomica> codiciATECO;
	protected String dataCessazione;
	protected String dataCostituzione;
	protected String dataFineValidita;
	protected String dataInizioValidita;
	protected String denominazione;
	protected String descrIscrSezArtigiani;
	protected String descrizioneCausaleCessazione;
	protected String descrizioneFontedato;
	protected String descrizioneNaturaGiuridica;
	protected String descrizioneStatoAttiv;
	protected String descrizioneTipoAzienda;
	@XmlElement(nillable = true)
	protected List<DettaglioVariazioneAnagrafica> dettagliVariazioniAnagrafiche;
	protected String idAzienda;
	protected String idFonte;
	protected String idNaturaGiuridica;
	protected String idStatoAttiv;
	protected String idTipoAzienda;
	protected Boolean iscrSezArtigiani;
	protected String oggettoSociale;
	protected String partitaIva;

	/**
	 * Recupera il valore della proprietà cciaa.
	 * 
	 * @return possible object is {@link DettagliCameraCommercio }
	 * 
	 */
	public DettagliCameraCommercio getCciaa() {
		return cciaa;
	}

	/**
	 * Imposta il valore della proprietà cciaa.
	 * 
	 * @param value allowed object is {@link DettagliCameraCommercio }
	 * 
	 */
	public void setCciaa(DettagliCameraCommercio value) {
		this.cciaa = value;
	}

	/**
	 * Recupera il valore della proprietà codiceCausaleCessazione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodiceCausaleCessazione() {
		return codiceCausaleCessazione;
	}

	/**
	 * Imposta il valore della proprietà codiceCausaleCessazione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodiceCausaleCessazione(String value) {
		this.codiceCausaleCessazione = value;
	}

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
	 * Gets the value of the codiciATECO property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the codiciATECO property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getCodiciATECO().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link AttivitaEconomica }
	 * 
	 * 
	 */
	public List<AttivitaEconomica> getCodiciATECO() {
		if (codiciATECO == null) {
			codiciATECO = new ArrayList<AttivitaEconomica>();
		}
		return this.codiciATECO;
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
	 * Recupera il valore della proprietà dataCostituzione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataCostituzione() {
		return dataCostituzione;
	}

	/**
	 * Imposta il valore della proprietà dataCostituzione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataCostituzione(String value) {
		this.dataCostituzione = value;
	}

	/**
	 * Recupera il valore della proprietà dataFineValidita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataFineValidita() {
		return dataFineValidita;
	}

	/**
	 * Imposta il valore della proprietà dataFineValidita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataFineValidita(String value) {
		this.dataFineValidita = value;
	}

	/**
	 * Recupera il valore della proprietà dataInizioValidita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataInizioValidita() {
		return dataInizioValidita;
	}

	/**
	 * Imposta il valore della proprietà dataInizioValidita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataInizioValidita(String value) {
		this.dataInizioValidita = value;
	}

	/**
	 * Recupera il valore della proprietà denominazione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * Imposta il valore della proprietà denominazione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDenominazione(String value) {
		this.denominazione = value;
	}

	/**
	 * Recupera il valore della proprietà descrIscrSezArtigiani.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrIscrSezArtigiani() {
		return descrIscrSezArtigiani;
	}

	/**
	 * Imposta il valore della proprietà descrIscrSezArtigiani.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrIscrSezArtigiani(String value) {
		this.descrIscrSezArtigiani = value;
	}

	/**
	 * Recupera il valore della proprietà descrizioneCausaleCessazione.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrizioneCausaleCessazione() {
		return descrizioneCausaleCessazione;
	}

	/**
	 * Imposta il valore della proprietà descrizioneCausaleCessazione.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrizioneCausaleCessazione(String value) {
		this.descrizioneCausaleCessazione = value;
	}

	/**
	 * Recupera il valore della proprietà descrizioneFontedato.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrizioneFontedato() {
		return descrizioneFontedato;
	}

	/**
	 * Imposta il valore della proprietà descrizioneFontedato.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrizioneFontedato(String value) {
		this.descrizioneFontedato = value;
	}

	/**
	 * Recupera il valore della proprietà descrizioneNaturaGiuridica.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrizioneNaturaGiuridica() {
		return descrizioneNaturaGiuridica;
	}

	/**
	 * Imposta il valore della proprietà descrizioneNaturaGiuridica.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrizioneNaturaGiuridica(String value) {
		this.descrizioneNaturaGiuridica = value;
	}

	/**
	 * Recupera il valore della proprietà descrizioneStatoAttiv.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrizioneStatoAttiv() {
		return descrizioneStatoAttiv;
	}

	/**
	 * Imposta il valore della proprietà descrizioneStatoAttiv.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrizioneStatoAttiv(String value) {
		this.descrizioneStatoAttiv = value;
	}

	/**
	 * Recupera il valore della proprietà descrizioneTipoAzienda.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrizioneTipoAzienda() {
		return descrizioneTipoAzienda;
	}

	/**
	 * Imposta il valore della proprietà descrizioneTipoAzienda.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrizioneTipoAzienda(String value) {
		this.descrizioneTipoAzienda = value;
	}

	/**
	 * Gets the value of the dettagliVariazioniAnagrafiche property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the dettagliVariazioniAnagrafiche property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getDettagliVariazioniAnagrafiche().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link DettaglioVariazioneAnagrafica }
	 * 
	 * 
	 */
	public List<DettaglioVariazioneAnagrafica> getDettagliVariazioniAnagrafiche() {
		if (dettagliVariazioniAnagrafiche == null) {
			dettagliVariazioniAnagrafiche = new ArrayList<DettaglioVariazioneAnagrafica>();
		}
		return this.dettagliVariazioniAnagrafiche;
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
	 * Recupera il valore della proprietà idFonte.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdFonte() {
		return idFonte;
	}

	/**
	 * Imposta il valore della proprietà idFonte.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdFonte(String value) {
		this.idFonte = value;
	}

	/**
	 * Recupera il valore della proprietà idNaturaGiuridica.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdNaturaGiuridica() {
		return idNaturaGiuridica;
	}

	/**
	 * Imposta il valore della proprietà idNaturaGiuridica.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdNaturaGiuridica(String value) {
		this.idNaturaGiuridica = value;
	}

	/**
	 * Recupera il valore della proprietà idStatoAttiv.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdStatoAttiv() {
		return idStatoAttiv;
	}

	/**
	 * Imposta il valore della proprietà idStatoAttiv.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdStatoAttiv(String value) {
		this.idStatoAttiv = value;
	}

	/**
	 * Recupera il valore della proprietà idTipoAzienda.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdTipoAzienda() {
		return idTipoAzienda;
	}

	/**
	 * Imposta il valore della proprietà idTipoAzienda.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdTipoAzienda(String value) {
		this.idTipoAzienda = value;
	}

	/**
	 * Recupera il valore della proprietà iscrSezArtigiani.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isIscrSezArtigiani() {
		return iscrSezArtigiani;
	}

	/**
	 * Imposta il valore della proprietà iscrSezArtigiani.
	 * 
	 * @param value allowed object is {@link Boolean }
	 * 
	 */
	public void setIscrSezArtigiani(Boolean value) {
		this.iscrSezArtigiani = value;
	}

	/**
	 * Recupera il valore della proprietà oggettoSociale.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOggettoSociale() {
		return oggettoSociale;
	}

	/**
	 * Imposta il valore della proprietà oggettoSociale.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setOggettoSociale(String value) {
		this.oggettoSociale = value;
	}

	/**
	 * Recupera il valore della proprietà partitaIva.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPartitaIva() {
		return partitaIva;
	}

	/**
	 * Imposta il valore della proprietà partitaIva.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setPartitaIva(String value) {
		this.partitaIva = value;
	}

}

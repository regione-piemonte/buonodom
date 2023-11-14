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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per impresa complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="impresa"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cessazione" type="{http://business.aaeporch.aaep.csi.it/}cessazione" minOccurs="0"/&gt;
 *         &lt;element name="codNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codiciATECO" type="{http://business.aaeporch.aaep.csi.it/}attivitaEconomica" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="dataAggiornamento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataInizioValidita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrFonte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dettagliAlboArtigiano" type="{http://business.aaeporch.aaep.csi.it/}dettagliAlboArtigiano" minOccurs="0"/&gt;
 *         &lt;element name="dettagliCameraCommercio" type="{http://business.aaeporch.aaep.csi.it/}dettagliCameraCommercio" minOccurs="0"/&gt;
 *         &lt;element name="idAzienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idFonte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idNaturaGiuridica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="partitaIva" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="postaElettronicaCertificata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ragioneSociale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sedi" type="{http://business.aaeporch.aaep.csi.it/}sede" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "impresa", propOrder = { "cessazione", "codNaturaGiuridica", "codiceFiscale", "codiciATECO",
		"dataAggiornamento", "dataInizioValidita", "descrFonte", "descrNaturaGiuridica", "dettagliAlboArtigiano",
		"dettagliCameraCommercio", "idAzienda", "idFonte", "idNaturaGiuridica", "idSede", "partitaIva",
		"postaElettronicaCertificata", "ragioneSociale", "sedi" })
@XmlSeeAlso({ ImpresaINFOC.class, AziendaAAEP.class, AziendaSILP.class, OperatoreFormazioneProfessionale.class })
public class Impresa {

	protected Cessazione cessazione;
	protected String codNaturaGiuridica;
	protected String codiceFiscale;
	@XmlElement(nillable = true)
	protected List<AttivitaEconomica> codiciATECO;
	protected String dataAggiornamento;
	protected String dataInizioValidita;
	protected String descrFonte;
	protected String descrNaturaGiuridica;
	protected DettagliAlboArtigiano dettagliAlboArtigiano;
	protected DettagliCameraCommercio dettagliCameraCommercio;
	protected String idAzienda;
	protected String idFonte;
	protected String idNaturaGiuridica;
	protected String idSede;
	protected String partitaIva;
	protected String postaElettronicaCertificata;
	protected String ragioneSociale;
	@XmlElement(nillable = true)
	protected List<Sede> sedi;

	/**
	 * Recupera il valore della proprietà cessazione.
	 * 
	 * @return possible object is {@link Cessazione }
	 * 
	 */
	public Cessazione getCessazione() {
		return cessazione;
	}

	/**
	 * Imposta il valore della proprietà cessazione.
	 * 
	 * @param value allowed object is {@link Cessazione }
	 * 
	 */
	public void setCessazione(Cessazione value) {
		this.cessazione = value;
	}

	/**
	 * Recupera il valore della proprietà codNaturaGiuridica.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodNaturaGiuridica() {
		return codNaturaGiuridica;
	}

	/**
	 * Imposta il valore della proprietà codNaturaGiuridica.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodNaturaGiuridica(String value) {
		this.codNaturaGiuridica = value;
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
	 * Recupera il valore della proprietà descrFonte.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrFonte() {
		return descrFonte;
	}

	/**
	 * Imposta il valore della proprietà descrFonte.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrFonte(String value) {
		this.descrFonte = value;
	}

	/**
	 * Recupera il valore della proprietà descrNaturaGiuridica.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrNaturaGiuridica() {
		return descrNaturaGiuridica;
	}

	/**
	 * Imposta il valore della proprietà descrNaturaGiuridica.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrNaturaGiuridica(String value) {
		this.descrNaturaGiuridica = value;
	}

	/**
	 * Recupera il valore della proprietà dettagliAlboArtigiano.
	 * 
	 * @return possible object is {@link DettagliAlboArtigiano }
	 * 
	 */
	public DettagliAlboArtigiano getDettagliAlboArtigiano() {
		return dettagliAlboArtigiano;
	}

	/**
	 * Imposta il valore della proprietà dettagliAlboArtigiano.
	 * 
	 * @param value allowed object is {@link DettagliAlboArtigiano }
	 * 
	 */
	public void setDettagliAlboArtigiano(DettagliAlboArtigiano value) {
		this.dettagliAlboArtigiano = value;
	}

	/**
	 * Recupera il valore della proprietà dettagliCameraCommercio.
	 * 
	 * @return possible object is {@link DettagliCameraCommercio }
	 * 
	 */
	public DettagliCameraCommercio getDettagliCameraCommercio() {
		return dettagliCameraCommercio;
	}

	/**
	 * Imposta il valore della proprietà dettagliCameraCommercio.
	 * 
	 * @param value allowed object is {@link DettagliCameraCommercio }
	 * 
	 */
	public void setDettagliCameraCommercio(DettagliCameraCommercio value) {
		this.dettagliCameraCommercio = value;
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
	 * Recupera il valore della proprietà idSede.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdSede() {
		return idSede;
	}

	/**
	 * Imposta il valore della proprietà idSede.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdSede(String value) {
		this.idSede = value;
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

	/**
	 * Recupera il valore della proprietà postaElettronicaCertificata.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPostaElettronicaCertificata() {
		return postaElettronicaCertificata;
	}

	/**
	 * Imposta il valore della proprietà postaElettronicaCertificata.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setPostaElettronicaCertificata(String value) {
		this.postaElettronicaCertificata = value;
	}

	/**
	 * Recupera il valore della proprietà ragioneSociale.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRagioneSociale() {
		return ragioneSociale;
	}

	/**
	 * Imposta il valore della proprietà ragioneSociale.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setRagioneSociale(String value) {
		this.ragioneSociale = value;
	}

	/**
	 * Gets the value of the sedi property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the sedi property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSedi().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Sede }
	 * 
	 * 
	 */
	public List<Sede> getSedi() {
		if (sedi == null) {
			sedi = new ArrayList<Sede>();
		}
		return this.sedi;
	}

}

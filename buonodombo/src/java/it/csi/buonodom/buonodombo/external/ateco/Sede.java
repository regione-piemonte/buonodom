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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per sede complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="sede"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ateco" type="{http://business.aaeporch.aaep.csi.it/}attivitaEconomica" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="codCausaleCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codSettore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="contatti" type="{http://business.aaeporch.aaep.csi.it/}contatti" minOccurs="0"/&gt;
 *         &lt;element name="dataAggiornam" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataInizioAttivita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataInizioValidita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataNumeroDipendenti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="denominazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrCausaleCessazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrSettore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrTipoSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fonteDato" type="{http://business.aaeporch.aaep.csi.it/}tipologiaFonte" minOccurs="0"/&gt;
 *         &lt;element name="idAzienda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroDipendenti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="riferimento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipoSede" type="{http://business.aaeporch.aaep.csi.it/}tipologiaSede" minOccurs="0"/&gt;
 *         &lt;element name="ubicazione" type="{http://business.aaeporch.aaep.csi.it/}ubicazione" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sede", propOrder = { "ateco", "codCausaleCessazione", "codSettore", "contatti", "dataAggiornam",
		"dataCessazione", "dataInizioAttivita", "dataInizioValidita", "dataNumeroDipendenti", "denominazione",
		"descrCausaleCessazione", "descrSettore", "descrTipoSede", "fonteDato", "idAzienda", "idSede",
		"numeroDipendenti", "riferimento", "tipoSede", "ubicazione" })
@XmlSeeAlso({ SedeAAEP.class, SedeFP.class, SedeOccasionale.class, SedeINFOC.class, SedeFonteINFOC.class,
		SedeListItem.class, SedeSILP.class })
public class Sede {

	@XmlElement(nillable = true)
	protected List<AttivitaEconomica> ateco;
	protected String codCausaleCessazione;
	protected String codSettore;
	protected Contatti contatti;
	protected String dataAggiornam;
	protected String dataCessazione;
	protected String dataInizioAttivita;
	protected String dataInizioValidita;
	protected String dataNumeroDipendenti;
	protected String denominazione;
	protected String descrCausaleCessazione;
	protected String descrSettore;
	protected String descrTipoSede;
	@XmlSchemaType(name = "string")
	protected TipologiaFonte fonteDato;
	protected String idAzienda;
	protected String idSede;
	protected String numeroDipendenti;
	protected String riferimento;
	@XmlSchemaType(name = "string")
	protected TipologiaSede tipoSede;
	protected Ubicazione ubicazione;

	/**
	 * Gets the value of the ateco property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the ateco property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getAteco().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link AttivitaEconomica }
	 * 
	 * 
	 */
	public List<AttivitaEconomica> getAteco() {
		if (ateco == null) {
			ateco = new ArrayList<AttivitaEconomica>();
		}
		return this.ateco;
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
	 * Recupera il valore della proprietà codSettore.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodSettore() {
		return codSettore;
	}

	/**
	 * Imposta il valore della proprietà codSettore.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodSettore(String value) {
		this.codSettore = value;
	}

	/**
	 * Recupera il valore della proprietà contatti.
	 * 
	 * @return possible object is {@link Contatti }
	 * 
	 */
	public Contatti getContatti() {
		return contatti;
	}

	/**
	 * Imposta il valore della proprietà contatti.
	 * 
	 * @param value allowed object is {@link Contatti }
	 * 
	 */
	public void setContatti(Contatti value) {
		this.contatti = value;
	}

	/**
	 * Recupera il valore della proprietà dataAggiornam.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataAggiornam() {
		return dataAggiornam;
	}

	/**
	 * Imposta il valore della proprietà dataAggiornam.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataAggiornam(String value) {
		this.dataAggiornam = value;
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
	 * Recupera il valore della proprietà dataInizioAttivita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataInizioAttivita() {
		return dataInizioAttivita;
	}

	/**
	 * Imposta il valore della proprietà dataInizioAttivita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataInizioAttivita(String value) {
		this.dataInizioAttivita = value;
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
	 * Recupera il valore della proprietà dataNumeroDipendenti.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataNumeroDipendenti() {
		return dataNumeroDipendenti;
	}

	/**
	 * Imposta il valore della proprietà dataNumeroDipendenti.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataNumeroDipendenti(String value) {
		this.dataNumeroDipendenti = value;
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

	/**
	 * Recupera il valore della proprietà descrSettore.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrSettore() {
		return descrSettore;
	}

	/**
	 * Imposta il valore della proprietà descrSettore.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrSettore(String value) {
		this.descrSettore = value;
	}

	/**
	 * Recupera il valore della proprietà descrTipoSede.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrTipoSede() {
		return descrTipoSede;
	}

	/**
	 * Imposta il valore della proprietà descrTipoSede.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrTipoSede(String value) {
		this.descrTipoSede = value;
	}

	/**
	 * Recupera il valore della proprietà fonteDato.
	 * 
	 * @return possible object is {@link TipologiaFonte }
	 * 
	 */
	public TipologiaFonte getFonteDato() {
		return fonteDato;
	}

	/**
	 * Imposta il valore della proprietà fonteDato.
	 * 
	 * @param value allowed object is {@link TipologiaFonte }
	 * 
	 */
	public void setFonteDato(TipologiaFonte value) {
		this.fonteDato = value;
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
	 * Recupera il valore della proprietà numeroDipendenti.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumeroDipendenti() {
		return numeroDipendenti;
	}

	/**
	 * Imposta il valore della proprietà numeroDipendenti.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumeroDipendenti(String value) {
		this.numeroDipendenti = value;
	}

	/**
	 * Recupera il valore della proprietà riferimento.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRiferimento() {
		return riferimento;
	}

	/**
	 * Imposta il valore della proprietà riferimento.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setRiferimento(String value) {
		this.riferimento = value;
	}

	/**
	 * Recupera il valore della proprietà tipoSede.
	 * 
	 * @return possible object is {@link TipologiaSede }
	 * 
	 */
	public TipologiaSede getTipoSede() {
		return tipoSede;
	}

	/**
	 * Imposta il valore della proprietà tipoSede.
	 * 
	 * @param value allowed object is {@link TipologiaSede }
	 * 
	 */
	public void setTipoSede(TipologiaSede value) {
		this.tipoSede = value;
	}

	/**
	 * Recupera il valore della proprietà ubicazione.
	 * 
	 * @return possible object is {@link Ubicazione }
	 * 
	 */
	public Ubicazione getUbicazione() {
		return ubicazione;
	}

	/**
	 * Imposta il valore della proprietà ubicazione.
	 * 
	 * @param value allowed object is {@link Ubicazione }
	 * 
	 */
	public void setUbicazione(Ubicazione value) {
		this.ubicazione = value;
	}

}

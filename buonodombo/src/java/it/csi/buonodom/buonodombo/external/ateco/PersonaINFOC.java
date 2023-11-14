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
 * Classe Java per personaINFOC complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="personaINFOC"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://business.aaeporch.aaep.csi.it/}persona"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="altreIndicazResid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="altreInfoSedeSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="capResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="capSedeSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codCittadinanza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codComuneRes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codComuneSedeSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codFiscaleSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codLimitazCapacitaAgire" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codStatoNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codStatoRes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codStatoSedeSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codToponimoResid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codToponimoSedeSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataCostSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="denominazSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrCittadinanza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrComuneRes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrComuneSedeSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrFlagElettore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrFrazioneRes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrIndicatoriPotereF" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrStatoNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrStatoRes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrStatoSedeSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrToponimoResid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrToponimoSedeSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="flagElettore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="frazioneSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="indicPoteriFirma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="listaFallimenti" type="{http://business.aaeporch.aaep.csi.it/}fallimento" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="numCivicoResid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numCivicoSedeSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numIscrReaSocParif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numRISocParif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numTelefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="percentPartecip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="progrOrdineVisura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="progrUnitaLocale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quotaPartecipaz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="quotaPartecipazEuro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaCCIAASocParif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvSedeSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="valutaPartecip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="viaResidenza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="viaSedeSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="eMail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personaINFOC", propOrder = { "altreIndicazResid", "altreInfoSedeSP", "capResidenza", "capSedeSP",
		"codCittadinanza", "codComuneNascita", "codComuneRes", "codComuneSedeSP", "codFiscaleSP",
		"codLimitazCapacitaAgire", "codStatoNascita", "codStatoRes", "codStatoSedeSP", "codToponimoResid",
		"codToponimoSedeSP", "dataCostSP", "dataNascita", "denominazSP", "descrCittadinanza", "descrComuneNascita",
		"descrComuneRes", "descrComuneSedeSP", "descrFlagElettore", "descrFrazioneRes", "descrIndicatoriPotereF",
		"descrStatoNascita", "descrStatoRes", "descrStatoSedeSP", "descrToponimoResid", "descrToponimoSedeSP", "fax",
		"flagElettore", "frazioneSP", "indicPoteriFirma", "listaFallimenti", "numCivicoResid", "numCivicoSedeSP",
		"numIscrReaSocParif", "numRISocParif", "numTelefono", "percentPartecip", "progrOrdineVisura",
		"progrUnitaLocale", "quotaPartecipaz", "quotaPartecipazEuro", "sesso", "siglaCCIAASocParif", "siglaProvNascita",
		"siglaProvResidenza", "siglaProvSedeSP", "valutaPartecip", "viaResidenza", "viaSedeSP", "eMail" })
public class PersonaINFOC extends Persona {

	protected String altreIndicazResid;
	protected String altreInfoSedeSP;
	protected String capResidenza;
	protected String capSedeSP;
	protected String codCittadinanza;
	protected String codComuneNascita;
	protected String codComuneRes;
	protected String codComuneSedeSP;
	protected String codFiscaleSP;
	protected String codLimitazCapacitaAgire;
	protected String codStatoNascita;
	protected String codStatoRes;
	protected String codStatoSedeSP;
	protected String codToponimoResid;
	protected String codToponimoSedeSP;
	protected String dataCostSP;
	protected String dataNascita;
	protected String denominazSP;
	protected String descrCittadinanza;
	protected String descrComuneNascita;
	protected String descrComuneRes;
	protected String descrComuneSedeSP;
	protected String descrFlagElettore;
	protected String descrFrazioneRes;
	protected String descrIndicatoriPotereF;
	protected String descrStatoNascita;
	protected String descrStatoRes;
	protected String descrStatoSedeSP;
	protected String descrToponimoResid;
	protected String descrToponimoSedeSP;
	protected String fax;
	protected String flagElettore;
	protected String frazioneSP;
	protected String indicPoteriFirma;
	@XmlElement(nillable = true)
	protected List<Fallimento> listaFallimenti;
	protected String numCivicoResid;
	protected String numCivicoSedeSP;
	protected String numIscrReaSocParif;
	protected String numRISocParif;
	protected String numTelefono;
	protected String percentPartecip;
	protected String progrOrdineVisura;
	protected String progrUnitaLocale;
	protected String quotaPartecipaz;
	protected String quotaPartecipazEuro;
	protected String sesso;
	protected String siglaCCIAASocParif;
	protected String siglaProvNascita;
	protected String siglaProvResidenza;
	protected String siglaProvSedeSP;
	protected String valutaPartecip;
	protected String viaResidenza;
	protected String viaSedeSP;
	protected String eMail;

	/**
	 * Recupera il valore della proprietà altreIndicazResid.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAltreIndicazResid() {
		return altreIndicazResid;
	}

	/**
	 * Imposta il valore della proprietà altreIndicazResid.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setAltreIndicazResid(String value) {
		this.altreIndicazResid = value;
	}

	/**
	 * Recupera il valore della proprietà altreInfoSedeSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAltreInfoSedeSP() {
		return altreInfoSedeSP;
	}

	/**
	 * Imposta il valore della proprietà altreInfoSedeSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setAltreInfoSedeSP(String value) {
		this.altreInfoSedeSP = value;
	}

	/**
	 * Recupera il valore della proprietà capResidenza.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCapResidenza() {
		return capResidenza;
	}

	/**
	 * Imposta il valore della proprietà capResidenza.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCapResidenza(String value) {
		this.capResidenza = value;
	}

	/**
	 * Recupera il valore della proprietà capSedeSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCapSedeSP() {
		return capSedeSP;
	}

	/**
	 * Imposta il valore della proprietà capSedeSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCapSedeSP(String value) {
		this.capSedeSP = value;
	}

	/**
	 * Recupera il valore della proprietà codCittadinanza.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodCittadinanza() {
		return codCittadinanza;
	}

	/**
	 * Imposta il valore della proprietà codCittadinanza.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodCittadinanza(String value) {
		this.codCittadinanza = value;
	}

	/**
	 * Recupera il valore della proprietà codComuneNascita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodComuneNascita() {
		return codComuneNascita;
	}

	/**
	 * Imposta il valore della proprietà codComuneNascita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodComuneNascita(String value) {
		this.codComuneNascita = value;
	}

	/**
	 * Recupera il valore della proprietà codComuneRes.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodComuneRes() {
		return codComuneRes;
	}

	/**
	 * Imposta il valore della proprietà codComuneRes.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodComuneRes(String value) {
		this.codComuneRes = value;
	}

	/**
	 * Recupera il valore della proprietà codComuneSedeSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodComuneSedeSP() {
		return codComuneSedeSP;
	}

	/**
	 * Imposta il valore della proprietà codComuneSedeSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodComuneSedeSP(String value) {
		this.codComuneSedeSP = value;
	}

	/**
	 * Recupera il valore della proprietà codFiscaleSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodFiscaleSP() {
		return codFiscaleSP;
	}

	/**
	 * Imposta il valore della proprietà codFiscaleSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodFiscaleSP(String value) {
		this.codFiscaleSP = value;
	}

	/**
	 * Recupera il valore della proprietà codLimitazCapacitaAgire.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodLimitazCapacitaAgire() {
		return codLimitazCapacitaAgire;
	}

	/**
	 * Imposta il valore della proprietà codLimitazCapacitaAgire.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodLimitazCapacitaAgire(String value) {
		this.codLimitazCapacitaAgire = value;
	}

	/**
	 * Recupera il valore della proprietà codStatoNascita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodStatoNascita() {
		return codStatoNascita;
	}

	/**
	 * Imposta il valore della proprietà codStatoNascita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodStatoNascita(String value) {
		this.codStatoNascita = value;
	}

	/**
	 * Recupera il valore della proprietà codStatoRes.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodStatoRes() {
		return codStatoRes;
	}

	/**
	 * Imposta il valore della proprietà codStatoRes.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodStatoRes(String value) {
		this.codStatoRes = value;
	}

	/**
	 * Recupera il valore della proprietà codStatoSedeSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodStatoSedeSP() {
		return codStatoSedeSP;
	}

	/**
	 * Imposta il valore della proprietà codStatoSedeSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodStatoSedeSP(String value) {
		this.codStatoSedeSP = value;
	}

	/**
	 * Recupera il valore della proprietà codToponimoResid.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodToponimoResid() {
		return codToponimoResid;
	}

	/**
	 * Imposta il valore della proprietà codToponimoResid.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodToponimoResid(String value) {
		this.codToponimoResid = value;
	}

	/**
	 * Recupera il valore della proprietà codToponimoSedeSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodToponimoSedeSP() {
		return codToponimoSedeSP;
	}

	/**
	 * Imposta il valore della proprietà codToponimoSedeSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodToponimoSedeSP(String value) {
		this.codToponimoSedeSP = value;
	}

	/**
	 * Recupera il valore della proprietà dataCostSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataCostSP() {
		return dataCostSP;
	}

	/**
	 * Imposta il valore della proprietà dataCostSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataCostSP(String value) {
		this.dataCostSP = value;
	}

	/**
	 * Recupera il valore della proprietà dataNascita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataNascita() {
		return dataNascita;
	}

	/**
	 * Imposta il valore della proprietà dataNascita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataNascita(String value) {
		this.dataNascita = value;
	}

	/**
	 * Recupera il valore della proprietà denominazSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDenominazSP() {
		return denominazSP;
	}

	/**
	 * Imposta il valore della proprietà denominazSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDenominazSP(String value) {
		this.denominazSP = value;
	}

	/**
	 * Recupera il valore della proprietà descrCittadinanza.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrCittadinanza() {
		return descrCittadinanza;
	}

	/**
	 * Imposta il valore della proprietà descrCittadinanza.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrCittadinanza(String value) {
		this.descrCittadinanza = value;
	}

	/**
	 * Recupera il valore della proprietà descrComuneNascita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrComuneNascita() {
		return descrComuneNascita;
	}

	/**
	 * Imposta il valore della proprietà descrComuneNascita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrComuneNascita(String value) {
		this.descrComuneNascita = value;
	}

	/**
	 * Recupera il valore della proprietà descrComuneRes.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrComuneRes() {
		return descrComuneRes;
	}

	/**
	 * Imposta il valore della proprietà descrComuneRes.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrComuneRes(String value) {
		this.descrComuneRes = value;
	}

	/**
	 * Recupera il valore della proprietà descrComuneSedeSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrComuneSedeSP() {
		return descrComuneSedeSP;
	}

	/**
	 * Imposta il valore della proprietà descrComuneSedeSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrComuneSedeSP(String value) {
		this.descrComuneSedeSP = value;
	}

	/**
	 * Recupera il valore della proprietà descrFlagElettore.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrFlagElettore() {
		return descrFlagElettore;
	}

	/**
	 * Imposta il valore della proprietà descrFlagElettore.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrFlagElettore(String value) {
		this.descrFlagElettore = value;
	}

	/**
	 * Recupera il valore della proprietà descrFrazioneRes.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrFrazioneRes() {
		return descrFrazioneRes;
	}

	/**
	 * Imposta il valore della proprietà descrFrazioneRes.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrFrazioneRes(String value) {
		this.descrFrazioneRes = value;
	}

	/**
	 * Recupera il valore della proprietà descrIndicatoriPotereF.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrIndicatoriPotereF() {
		return descrIndicatoriPotereF;
	}

	/**
	 * Imposta il valore della proprietà descrIndicatoriPotereF.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrIndicatoriPotereF(String value) {
		this.descrIndicatoriPotereF = value;
	}

	/**
	 * Recupera il valore della proprietà descrStatoNascita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrStatoNascita() {
		return descrStatoNascita;
	}

	/**
	 * Imposta il valore della proprietà descrStatoNascita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrStatoNascita(String value) {
		this.descrStatoNascita = value;
	}

	/**
	 * Recupera il valore della proprietà descrStatoRes.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrStatoRes() {
		return descrStatoRes;
	}

	/**
	 * Imposta il valore della proprietà descrStatoRes.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrStatoRes(String value) {
		this.descrStatoRes = value;
	}

	/**
	 * Recupera il valore della proprietà descrStatoSedeSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrStatoSedeSP() {
		return descrStatoSedeSP;
	}

	/**
	 * Imposta il valore della proprietà descrStatoSedeSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrStatoSedeSP(String value) {
		this.descrStatoSedeSP = value;
	}

	/**
	 * Recupera il valore della proprietà descrToponimoResid.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrToponimoResid() {
		return descrToponimoResid;
	}

	/**
	 * Imposta il valore della proprietà descrToponimoResid.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrToponimoResid(String value) {
		this.descrToponimoResid = value;
	}

	/**
	 * Recupera il valore della proprietà descrToponimoSedeSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrToponimoSedeSP() {
		return descrToponimoSedeSP;
	}

	/**
	 * Imposta il valore della proprietà descrToponimoSedeSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrToponimoSedeSP(String value) {
		this.descrToponimoSedeSP = value;
	}

	/**
	 * Recupera il valore della proprietà fax.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * Imposta il valore della proprietà fax.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setFax(String value) {
		this.fax = value;
	}

	/**
	 * Recupera il valore della proprietà flagElettore.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFlagElettore() {
		return flagElettore;
	}

	/**
	 * Imposta il valore della proprietà flagElettore.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setFlagElettore(String value) {
		this.flagElettore = value;
	}

	/**
	 * Recupera il valore della proprietà frazioneSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFrazioneSP() {
		return frazioneSP;
	}

	/**
	 * Imposta il valore della proprietà frazioneSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setFrazioneSP(String value) {
		this.frazioneSP = value;
	}

	/**
	 * Recupera il valore della proprietà indicPoteriFirma.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIndicPoteriFirma() {
		return indicPoteriFirma;
	}

	/**
	 * Imposta il valore della proprietà indicPoteriFirma.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIndicPoteriFirma(String value) {
		this.indicPoteriFirma = value;
	}

	/**
	 * Gets the value of the listaFallimenti property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the listaFallimenti property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getListaFallimenti().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Fallimento }
	 * 
	 * 
	 */
	public List<Fallimento> getListaFallimenti() {
		if (listaFallimenti == null) {
			listaFallimenti = new ArrayList<Fallimento>();
		}
		return this.listaFallimenti;
	}

	/**
	 * Recupera il valore della proprietà numCivicoResid.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumCivicoResid() {
		return numCivicoResid;
	}

	/**
	 * Imposta il valore della proprietà numCivicoResid.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumCivicoResid(String value) {
		this.numCivicoResid = value;
	}

	/**
	 * Recupera il valore della proprietà numCivicoSedeSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumCivicoSedeSP() {
		return numCivicoSedeSP;
	}

	/**
	 * Imposta il valore della proprietà numCivicoSedeSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumCivicoSedeSP(String value) {
		this.numCivicoSedeSP = value;
	}

	/**
	 * Recupera il valore della proprietà numIscrReaSocParif.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumIscrReaSocParif() {
		return numIscrReaSocParif;
	}

	/**
	 * Imposta il valore della proprietà numIscrReaSocParif.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumIscrReaSocParif(String value) {
		this.numIscrReaSocParif = value;
	}

	/**
	 * Recupera il valore della proprietà numRISocParif.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumRISocParif() {
		return numRISocParif;
	}

	/**
	 * Imposta il valore della proprietà numRISocParif.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumRISocParif(String value) {
		this.numRISocParif = value;
	}

	/**
	 * Recupera il valore della proprietà numTelefono.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumTelefono() {
		return numTelefono;
	}

	/**
	 * Imposta il valore della proprietà numTelefono.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumTelefono(String value) {
		this.numTelefono = value;
	}

	/**
	 * Recupera il valore della proprietà percentPartecip.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPercentPartecip() {
		return percentPartecip;
	}

	/**
	 * Imposta il valore della proprietà percentPartecip.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setPercentPartecip(String value) {
		this.percentPartecip = value;
	}

	/**
	 * Recupera il valore della proprietà progrOrdineVisura.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getProgrOrdineVisura() {
		return progrOrdineVisura;
	}

	/**
	 * Imposta il valore della proprietà progrOrdineVisura.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setProgrOrdineVisura(String value) {
		this.progrOrdineVisura = value;
	}

	/**
	 * Recupera il valore della proprietà progrUnitaLocale.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getProgrUnitaLocale() {
		return progrUnitaLocale;
	}

	/**
	 * Imposta il valore della proprietà progrUnitaLocale.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setProgrUnitaLocale(String value) {
		this.progrUnitaLocale = value;
	}

	/**
	 * Recupera il valore della proprietà quotaPartecipaz.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getQuotaPartecipaz() {
		return quotaPartecipaz;
	}

	/**
	 * Imposta il valore della proprietà quotaPartecipaz.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setQuotaPartecipaz(String value) {
		this.quotaPartecipaz = value;
	}

	/**
	 * Recupera il valore della proprietà quotaPartecipazEuro.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getQuotaPartecipazEuro() {
		return quotaPartecipazEuro;
	}

	/**
	 * Imposta il valore della proprietà quotaPartecipazEuro.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setQuotaPartecipazEuro(String value) {
		this.quotaPartecipazEuro = value;
	}

	/**
	 * Recupera il valore della proprietà sesso.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSesso() {
		return sesso;
	}

	/**
	 * Imposta il valore della proprietà sesso.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSesso(String value) {
		this.sesso = value;
	}

	/**
	 * Recupera il valore della proprietà siglaCCIAASocParif.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaCCIAASocParif() {
		return siglaCCIAASocParif;
	}

	/**
	 * Imposta il valore della proprietà siglaCCIAASocParif.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaCCIAASocParif(String value) {
		this.siglaCCIAASocParif = value;
	}

	/**
	 * Recupera il valore della proprietà siglaProvNascita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaProvNascita() {
		return siglaProvNascita;
	}

	/**
	 * Imposta il valore della proprietà siglaProvNascita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaProvNascita(String value) {
		this.siglaProvNascita = value;
	}

	/**
	 * Recupera il valore della proprietà siglaProvResidenza.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaProvResidenza() {
		return siglaProvResidenza;
	}

	/**
	 * Imposta il valore della proprietà siglaProvResidenza.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaProvResidenza(String value) {
		this.siglaProvResidenza = value;
	}

	/**
	 * Recupera il valore della proprietà siglaProvSedeSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaProvSedeSP() {
		return siglaProvSedeSP;
	}

	/**
	 * Imposta il valore della proprietà siglaProvSedeSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaProvSedeSP(String value) {
		this.siglaProvSedeSP = value;
	}

	/**
	 * Recupera il valore della proprietà valutaPartecip.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getValutaPartecip() {
		return valutaPartecip;
	}

	/**
	 * Imposta il valore della proprietà valutaPartecip.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setValutaPartecip(String value) {
		this.valutaPartecip = value;
	}

	/**
	 * Recupera il valore della proprietà viaResidenza.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getViaResidenza() {
		return viaResidenza;
	}

	/**
	 * Imposta il valore della proprietà viaResidenza.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setViaResidenza(String value) {
		this.viaResidenza = value;
	}

	/**
	 * Recupera il valore della proprietà viaSedeSP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getViaSedeSP() {
		return viaSedeSP;
	}

	/**
	 * Imposta il valore della proprietà viaSedeSP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setViaSedeSP(String value) {
		this.viaSedeSP = value;
	}

	/**
	 * Recupera il valore della proprietà eMail.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEMail() {
		return eMail;
	}

	/**
	 * Imposta il valore della proprietà eMail.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setEMail(String value) {
		this.eMail = value;
	}

}

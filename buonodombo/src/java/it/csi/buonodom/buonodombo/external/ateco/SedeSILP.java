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
 * Classe Java per sedeSILP complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="sedeSILP"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://business.aaeporch.aaep.csi.it/}sede"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codCPI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codClasseAmpiezza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codINAIL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codINPS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codStatoSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codTipoSedeAAEP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codiciATECOSILP" type="{http://business.aaeporch.aaep.csi.it/}attivitaEconomica" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="dataUltAggiornamSILP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrCPI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrClasseAmpiezza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrRappLavoroPrevalente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrStatoSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrTipoSedeAAEP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrUbicazioneSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="flgMovimentazRappLavoro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="flgObbligoProspDisab" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="flgUbicazioneSede" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idRappLavoroPrevalente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="localita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="referenti" type="{http://business.aaeporch.aaep.csi.it/}referenteSILP" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sedeSILP", propOrder = { "codCPI", "codClasseAmpiezza", "codINAIL", "codINPS", "codStatoSede",
		"codTipoSedeAAEP", "codiciATECOSILP", "dataUltAggiornamSILP", "descrCPI", "descrClasseAmpiezza",
		"descrRappLavoroPrevalente", "descrStatoSede", "descrTipoSedeAAEP", "descrUbicazioneSede",
		"flgMovimentazRappLavoro", "flgObbligoProspDisab", "flgUbicazioneSede", "idRappLavoroPrevalente", "localita",
		"referenti" })
public class SedeSILP extends Sede {

	protected String codCPI;
	protected String codClasseAmpiezza;
	protected String codINAIL;
	protected String codINPS;
	protected String codStatoSede;
	protected String codTipoSedeAAEP;
	@XmlElement(nillable = true)
	protected List<AttivitaEconomica> codiciATECOSILP;
	protected String dataUltAggiornamSILP;
	protected String descrCPI;
	protected String descrClasseAmpiezza;
	protected String descrRappLavoroPrevalente;
	protected String descrStatoSede;
	protected String descrTipoSedeAAEP;
	protected String descrUbicazioneSede;
	protected String flgMovimentazRappLavoro;
	protected String flgObbligoProspDisab;
	protected String flgUbicazioneSede;
	protected String idRappLavoroPrevalente;
	protected String localita;
	@XmlElement(nillable = true)
	protected List<ReferenteSILP> referenti;

	/**
	 * Recupera il valore della proprietà codCPI.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodCPI() {
		return codCPI;
	}

	/**
	 * Imposta il valore della proprietà codCPI.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodCPI(String value) {
		this.codCPI = value;
	}

	/**
	 * Recupera il valore della proprietà codClasseAmpiezza.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodClasseAmpiezza() {
		return codClasseAmpiezza;
	}

	/**
	 * Imposta il valore della proprietà codClasseAmpiezza.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodClasseAmpiezza(String value) {
		this.codClasseAmpiezza = value;
	}

	/**
	 * Recupera il valore della proprietà codINAIL.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodINAIL() {
		return codINAIL;
	}

	/**
	 * Imposta il valore della proprietà codINAIL.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodINAIL(String value) {
		this.codINAIL = value;
	}

	/**
	 * Recupera il valore della proprietà codINPS.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodINPS() {
		return codINPS;
	}

	/**
	 * Imposta il valore della proprietà codINPS.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodINPS(String value) {
		this.codINPS = value;
	}

	/**
	 * Recupera il valore della proprietà codStatoSede.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodStatoSede() {
		return codStatoSede;
	}

	/**
	 * Imposta il valore della proprietà codStatoSede.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodStatoSede(String value) {
		this.codStatoSede = value;
	}

	/**
	 * Recupera il valore della proprietà codTipoSedeAAEP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodTipoSedeAAEP() {
		return codTipoSedeAAEP;
	}

	/**
	 * Imposta il valore della proprietà codTipoSedeAAEP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodTipoSedeAAEP(String value) {
		this.codTipoSedeAAEP = value;
	}

	/**
	 * Gets the value of the codiciATECOSILP property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the codiciATECOSILP property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getCodiciATECOSILP().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link AttivitaEconomica }
	 * 
	 * 
	 */
	public List<AttivitaEconomica> getCodiciATECOSILP() {
		if (codiciATECOSILP == null) {
			codiciATECOSILP = new ArrayList<AttivitaEconomica>();
		}
		return this.codiciATECOSILP;
	}

	/**
	 * Recupera il valore della proprietà dataUltAggiornamSILP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataUltAggiornamSILP() {
		return dataUltAggiornamSILP;
	}

	/**
	 * Imposta il valore della proprietà dataUltAggiornamSILP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataUltAggiornamSILP(String value) {
		this.dataUltAggiornamSILP = value;
	}

	/**
	 * Recupera il valore della proprietà descrCPI.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrCPI() {
		return descrCPI;
	}

	/**
	 * Imposta il valore della proprietà descrCPI.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrCPI(String value) {
		this.descrCPI = value;
	}

	/**
	 * Recupera il valore della proprietà descrClasseAmpiezza.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrClasseAmpiezza() {
		return descrClasseAmpiezza;
	}

	/**
	 * Imposta il valore della proprietà descrClasseAmpiezza.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrClasseAmpiezza(String value) {
		this.descrClasseAmpiezza = value;
	}

	/**
	 * Recupera il valore della proprietà descrRappLavoroPrevalente.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrRappLavoroPrevalente() {
		return descrRappLavoroPrevalente;
	}

	/**
	 * Imposta il valore della proprietà descrRappLavoroPrevalente.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrRappLavoroPrevalente(String value) {
		this.descrRappLavoroPrevalente = value;
	}

	/**
	 * Recupera il valore della proprietà descrStatoSede.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrStatoSede() {
		return descrStatoSede;
	}

	/**
	 * Imposta il valore della proprietà descrStatoSede.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrStatoSede(String value) {
		this.descrStatoSede = value;
	}

	/**
	 * Recupera il valore della proprietà descrTipoSedeAAEP.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrTipoSedeAAEP() {
		return descrTipoSedeAAEP;
	}

	/**
	 * Imposta il valore della proprietà descrTipoSedeAAEP.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrTipoSedeAAEP(String value) {
		this.descrTipoSedeAAEP = value;
	}

	/**
	 * Recupera il valore della proprietà descrUbicazioneSede.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrUbicazioneSede() {
		return descrUbicazioneSede;
	}

	/**
	 * Imposta il valore della proprietà descrUbicazioneSede.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrUbicazioneSede(String value) {
		this.descrUbicazioneSede = value;
	}

	/**
	 * Recupera il valore della proprietà flgMovimentazRappLavoro.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFlgMovimentazRappLavoro() {
		return flgMovimentazRappLavoro;
	}

	/**
	 * Imposta il valore della proprietà flgMovimentazRappLavoro.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setFlgMovimentazRappLavoro(String value) {
		this.flgMovimentazRappLavoro = value;
	}

	/**
	 * Recupera il valore della proprietà flgObbligoProspDisab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFlgObbligoProspDisab() {
		return flgObbligoProspDisab;
	}

	/**
	 * Imposta il valore della proprietà flgObbligoProspDisab.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setFlgObbligoProspDisab(String value) {
		this.flgObbligoProspDisab = value;
	}

	/**
	 * Recupera il valore della proprietà flgUbicazioneSede.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFlgUbicazioneSede() {
		return flgUbicazioneSede;
	}

	/**
	 * Imposta il valore della proprietà flgUbicazioneSede.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setFlgUbicazioneSede(String value) {
		this.flgUbicazioneSede = value;
	}

	/**
	 * Recupera il valore della proprietà idRappLavoroPrevalente.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdRappLavoroPrevalente() {
		return idRappLavoroPrevalente;
	}

	/**
	 * Imposta il valore della proprietà idRappLavoroPrevalente.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setIdRappLavoroPrevalente(String value) {
		this.idRappLavoroPrevalente = value;
	}

	/**
	 * Recupera il valore della proprietà localita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLocalita() {
		return localita;
	}

	/**
	 * Imposta il valore della proprietà localita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setLocalita(String value) {
		this.localita = value;
	}

	/**
	 * Gets the value of the referenti property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the referenti property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getReferenti().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link ReferenteSILP
	 * }
	 * 
	 * 
	 */
	public List<ReferenteSILP> getReferenti() {
		if (referenti == null) {
			referenti = new ArrayList<ReferenteSILP>();
		}
		return this.referenti;
	}

}

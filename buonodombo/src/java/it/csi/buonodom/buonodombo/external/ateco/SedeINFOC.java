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
 * Classe Java per sedeINFOC complex type.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;complexType name="sedeINFOC"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://business.aaeporch.aaep.csi.it/}sede"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="altreIndcazUL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codStradarioUL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codTipoUL1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codTipoUL2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codTipoUL3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codTipoUL4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codTipoUL5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataDenuncia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataDenunciaCessaz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrStradarioUL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrTipoUL1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrTipoUL2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrTipoUL3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrTipoUL4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrTipoUL5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="frazioneUL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="insegnaUL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="listaAlboArtg" type="{http://business.aaeporch.aaep.csi.it/}alboRuolo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="listaAlboRuolo" type="{http://business.aaeporch.aaep.csi.it/}alboRuolo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="listaDenunciaAttivita" type="{http://business.aaeporch.aaep.csi.it/}denunciaAttivita" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="numIscrizREA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numReaFuoriProv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvCCIAA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="superfVendita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="testoAttivita" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sedeINFOC", propOrder = { "altreIndcazUL", "codStradarioUL", "codTipoUL1", "codTipoUL2", "codTipoUL3",
		"codTipoUL4", "codTipoUL5", "dataDenuncia", "dataDenunciaCessaz", "descrStradarioUL", "descrTipoUL1",
		"descrTipoUL2", "descrTipoUL3", "descrTipoUL4", "descrTipoUL5", "frazioneUL", "insegnaUL", "listaAlboArtg",
		"listaAlboRuolo", "listaDenunciaAttivita", "numIscrizREA", "numReaFuoriProv", "siglaProvCCIAA", "superfVendita",
		"testoAttivita" })
public class SedeINFOC extends Sede {

	protected String altreIndcazUL;
	protected String codStradarioUL;
	protected String codTipoUL1;
	protected String codTipoUL2;
	protected String codTipoUL3;
	protected String codTipoUL4;
	protected String codTipoUL5;
	protected String dataDenuncia;
	protected String dataDenunciaCessaz;
	protected String descrStradarioUL;
	protected String descrTipoUL1;
	protected String descrTipoUL2;
	protected String descrTipoUL3;
	protected String descrTipoUL4;
	protected String descrTipoUL5;
	protected String frazioneUL;
	protected String insegnaUL;
	@XmlElement(nillable = true)
	protected List<AlboRuolo> listaAlboArtg;
	@XmlElement(nillable = true)
	protected List<AlboRuolo> listaAlboRuolo;
	@XmlElement(nillable = true)
	protected List<DenunciaAttivita> listaDenunciaAttivita;
	protected String numIscrizREA;
	protected String numReaFuoriProv;
	protected String siglaProvCCIAA;
	protected String superfVendita;
	@XmlElement(nillable = true)
	protected List<String> testoAttivita;

	/**
	 * Recupera il valore della proprietà altreIndcazUL.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAltreIndcazUL() {
		return altreIndcazUL;
	}

	/**
	 * Imposta il valore della proprietà altreIndcazUL.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setAltreIndcazUL(String value) {
		this.altreIndcazUL = value;
	}

	/**
	 * Recupera il valore della proprietà codStradarioUL.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodStradarioUL() {
		return codStradarioUL;
	}

	/**
	 * Imposta il valore della proprietà codStradarioUL.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodStradarioUL(String value) {
		this.codStradarioUL = value;
	}

	/**
	 * Recupera il valore della proprietà codTipoUL1.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodTipoUL1() {
		return codTipoUL1;
	}

	/**
	 * Imposta il valore della proprietà codTipoUL1.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodTipoUL1(String value) {
		this.codTipoUL1 = value;
	}

	/**
	 * Recupera il valore della proprietà codTipoUL2.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodTipoUL2() {
		return codTipoUL2;
	}

	/**
	 * Imposta il valore della proprietà codTipoUL2.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodTipoUL2(String value) {
		this.codTipoUL2 = value;
	}

	/**
	 * Recupera il valore della proprietà codTipoUL3.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodTipoUL3() {
		return codTipoUL3;
	}

	/**
	 * Imposta il valore della proprietà codTipoUL3.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodTipoUL3(String value) {
		this.codTipoUL3 = value;
	}

	/**
	 * Recupera il valore della proprietà codTipoUL4.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodTipoUL4() {
		return codTipoUL4;
	}

	/**
	 * Imposta il valore della proprietà codTipoUL4.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodTipoUL4(String value) {
		this.codTipoUL4 = value;
	}

	/**
	 * Recupera il valore della proprietà codTipoUL5.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodTipoUL5() {
		return codTipoUL5;
	}

	/**
	 * Imposta il valore della proprietà codTipoUL5.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCodTipoUL5(String value) {
		this.codTipoUL5 = value;
	}

	/**
	 * Recupera il valore della proprietà dataDenuncia.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataDenuncia() {
		return dataDenuncia;
	}

	/**
	 * Imposta il valore della proprietà dataDenuncia.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataDenuncia(String value) {
		this.dataDenuncia = value;
	}

	/**
	 * Recupera il valore della proprietà dataDenunciaCessaz.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataDenunciaCessaz() {
		return dataDenunciaCessaz;
	}

	/**
	 * Imposta il valore della proprietà dataDenunciaCessaz.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataDenunciaCessaz(String value) {
		this.dataDenunciaCessaz = value;
	}

	/**
	 * Recupera il valore della proprietà descrStradarioUL.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrStradarioUL() {
		return descrStradarioUL;
	}

	/**
	 * Imposta il valore della proprietà descrStradarioUL.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrStradarioUL(String value) {
		this.descrStradarioUL = value;
	}

	/**
	 * Recupera il valore della proprietà descrTipoUL1.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrTipoUL1() {
		return descrTipoUL1;
	}

	/**
	 * Imposta il valore della proprietà descrTipoUL1.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrTipoUL1(String value) {
		this.descrTipoUL1 = value;
	}

	/**
	 * Recupera il valore della proprietà descrTipoUL2.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrTipoUL2() {
		return descrTipoUL2;
	}

	/**
	 * Imposta il valore della proprietà descrTipoUL2.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrTipoUL2(String value) {
		this.descrTipoUL2 = value;
	}

	/**
	 * Recupera il valore della proprietà descrTipoUL3.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrTipoUL3() {
		return descrTipoUL3;
	}

	/**
	 * Imposta il valore della proprietà descrTipoUL3.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrTipoUL3(String value) {
		this.descrTipoUL3 = value;
	}

	/**
	 * Recupera il valore della proprietà descrTipoUL4.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrTipoUL4() {
		return descrTipoUL4;
	}

	/**
	 * Imposta il valore della proprietà descrTipoUL4.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrTipoUL4(String value) {
		this.descrTipoUL4 = value;
	}

	/**
	 * Recupera il valore della proprietà descrTipoUL5.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescrTipoUL5() {
		return descrTipoUL5;
	}

	/**
	 * Imposta il valore della proprietà descrTipoUL5.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDescrTipoUL5(String value) {
		this.descrTipoUL5 = value;
	}

	/**
	 * Recupera il valore della proprietà frazioneUL.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFrazioneUL() {
		return frazioneUL;
	}

	/**
	 * Imposta il valore della proprietà frazioneUL.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setFrazioneUL(String value) {
		this.frazioneUL = value;
	}

	/**
	 * Recupera il valore della proprietà insegnaUL.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInsegnaUL() {
		return insegnaUL;
	}

	/**
	 * Imposta il valore della proprietà insegnaUL.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setInsegnaUL(String value) {
		this.insegnaUL = value;
	}

	/**
	 * Gets the value of the listaAlboArtg property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the listaAlboArtg property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getListaAlboArtg().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link AlboRuolo }
	 * 
	 * 
	 */
	public List<AlboRuolo> getListaAlboArtg() {
		if (listaAlboArtg == null) {
			listaAlboArtg = new ArrayList<AlboRuolo>();
		}
		return this.listaAlboArtg;
	}

	/**
	 * Gets the value of the listaAlboRuolo property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the listaAlboRuolo property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getListaAlboRuolo().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link AlboRuolo }
	 * 
	 * 
	 */
	public List<AlboRuolo> getListaAlboRuolo() {
		if (listaAlboRuolo == null) {
			listaAlboRuolo = new ArrayList<AlboRuolo>();
		}
		return this.listaAlboRuolo;
	}

	/**
	 * Gets the value of the listaDenunciaAttivita property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the listaDenunciaAttivita property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getListaDenunciaAttivita().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link DenunciaAttivita }
	 * 
	 * 
	 */
	public List<DenunciaAttivita> getListaDenunciaAttivita() {
		if (listaDenunciaAttivita == null) {
			listaDenunciaAttivita = new ArrayList<DenunciaAttivita>();
		}
		return this.listaDenunciaAttivita;
	}

	/**
	 * Recupera il valore della proprietà numIscrizREA.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumIscrizREA() {
		return numIscrizREA;
	}

	/**
	 * Imposta il valore della proprietà numIscrizREA.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumIscrizREA(String value) {
		this.numIscrizREA = value;
	}

	/**
	 * Recupera il valore della proprietà numReaFuoriProv.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNumReaFuoriProv() {
		return numReaFuoriProv;
	}

	/**
	 * Imposta il valore della proprietà numReaFuoriProv.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setNumReaFuoriProv(String value) {
		this.numReaFuoriProv = value;
	}

	/**
	 * Recupera il valore della proprietà siglaProvCCIAA.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSiglaProvCCIAA() {
		return siglaProvCCIAA;
	}

	/**
	 * Imposta il valore della proprietà siglaProvCCIAA.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSiglaProvCCIAA(String value) {
		this.siglaProvCCIAA = value;
	}

	/**
	 * Recupera il valore della proprietà superfVendita.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSuperfVendita() {
		return superfVendita;
	}

	/**
	 * Imposta il valore della proprietà superfVendita.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSuperfVendita(String value) {
		this.superfVendita = value;
	}

	/**
	 * Gets the value of the testoAttivita property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the testoAttivita property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getTestoAttivita().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getTestoAttivita() {
		if (testoAttivita == null) {
			testoAttivita = new ArrayList<String>();
		}
		return this.testoAttivita;
	}

}

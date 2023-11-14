/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/

package it.csi.buonodom.buonodombo.external.ateco;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per tipologiaVariazioneAnagrafica.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;simpleType name="tipologiaVariazioneAnagrafica"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="VARIAZIONE_DENOMINAZIONE"/&gt;
 *     &lt;enumeration value="VARIAZIONE_NATURA_GIURIDICA"/&gt;
 *     &lt;enumeration value="VARIAZIONE_STATO_ATTIVITA"/&gt;
 *     &lt;enumeration value="VARIAZIONE_SEDE_LEGALE"/&gt;
 *     &lt;enumeration value="VARIAZIONE_RAPPRESENTANTE_LEGALE_AAEP"/&gt;
 *     &lt;enumeration value="VARIAZIONE_LEGALI_RAPPRESENTANTI_INFOCAMERE"/&gt;
 *     &lt;enumeration value="CAMBIO_FONTE"/&gt;
 *     &lt;enumeration value="VARIAZIONE_CESSAZIONE_IMPRESA"/&gt;
 *     &lt;enumeration value="PRIMO_INSERIMENTO_IN_AAEP"/&gt;
 *     &lt;enumeration value="VARIAZIONE_PROC_CONCORSUALI_INFOCAMERE"/&gt;
 *     &lt;enumeration value="VARIAZIONE_DELLE_SEDI_ESCLUSO_SEDE_LEGALE"/&gt;
 *     &lt;enumeration value="VARIAZIONE_CESSAZIONE_SEDE_LEGALE"/&gt;
 *     &lt;enumeration value="VARIAZIONE_CESSAZIONE_DELLE_SEDI_ESCLUSO_SEDE_LEGALE"/&gt;
 *     &lt;enumeration value="IMPRESA_NON_PERVENUTA_INFOCAMERE"/&gt;
 *     &lt;enumeration value="VARIAZIONE_POSIZIONE_REA"/&gt;
 *     &lt;enumeration value="VARIAZIONE_CODICE_FISCALE"/&gt;
 *     &lt;enumeration value="CESSAZIONE_LOCALIZZAZIONE_IN_PIEMONTE"/&gt;
 *     &lt;enumeration value="RIPRISTINO_LOCALIZZAZIONE_IN_PIEMONTE"/&gt;
 *     &lt;enumeration value="VARIAZIONE_INDIRIZZO_SEDE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "tipologiaVariazioneAnagrafica")
@XmlEnum
public enum TipologiaVariazioneAnagrafica {

	VARIAZIONE_DENOMINAZIONE, VARIAZIONE_NATURA_GIURIDICA, VARIAZIONE_STATO_ATTIVITA, VARIAZIONE_SEDE_LEGALE,
	VARIAZIONE_RAPPRESENTANTE_LEGALE_AAEP, VARIAZIONE_LEGALI_RAPPRESENTANTI_INFOCAMERE, CAMBIO_FONTE,
	VARIAZIONE_CESSAZIONE_IMPRESA, PRIMO_INSERIMENTO_IN_AAEP, VARIAZIONE_PROC_CONCORSUALI_INFOCAMERE,
	VARIAZIONE_DELLE_SEDI_ESCLUSO_SEDE_LEGALE, VARIAZIONE_CESSAZIONE_SEDE_LEGALE,
	VARIAZIONE_CESSAZIONE_DELLE_SEDI_ESCLUSO_SEDE_LEGALE, IMPRESA_NON_PERVENUTA_INFOCAMERE, VARIAZIONE_POSIZIONE_REA,
	VARIAZIONE_CODICE_FISCALE, CESSAZIONE_LOCALIZZAZIONE_IN_PIEMONTE, RIPRISTINO_LOCALIZZAZIONE_IN_PIEMONTE,
	VARIAZIONE_INDIRIZZO_SEDE;

	public String value() {
		return name();
	}

	public static TipologiaVariazioneAnagrafica fromValue(String v) {
		return valueOf(v);
	}

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/

package it.csi.buonodom.buonodombo.external.ateco;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per tipologiaSede.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;simpleType name="tipologiaSede"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SEDE_LEGALE"/&gt;
 *     &lt;enumeration value="UNITA_LOCALE"/&gt;
 *     &lt;enumeration value="SEDE_CORRISPONDENZA"/&gt;
 *     &lt;enumeration value="DOMICILIO_FISCALE"/&gt;
 *     &lt;enumeration value="UNITA_TECNICA"/&gt;
 *     &lt;enumeration value="SEDE_SECONDARIA"/&gt;
 *     &lt;enumeration value="SEDE_OCCASIONALE"/&gt;
 *     &lt;enumeration value="SEDE_AMMINISTRATIVA"/&gt;
 *     &lt;enumeration value="SEDE_NON_CODIFICATA_ALLA_FONTE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "tipologiaSede")
@XmlEnum
public enum TipologiaSede {

	SEDE_LEGALE, UNITA_LOCALE, SEDE_CORRISPONDENZA, DOMICILIO_FISCALE, UNITA_TECNICA, SEDE_SECONDARIA, SEDE_OCCASIONALE,
	SEDE_AMMINISTRATIVA, SEDE_NON_CODIFICATA_ALLA_FONTE;

	public String value() {
		return name();
	}

	public static TipologiaSede fromValue(String v) {
		return valueOf(v);
	}

}

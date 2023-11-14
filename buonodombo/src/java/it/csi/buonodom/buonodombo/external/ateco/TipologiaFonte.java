/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/

package it.csi.buonodom.buonodombo.external.ateco;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java per tipologiaFonte.
 * 
 * <p>
 * Il seguente frammento di schema specifica il contenuto previsto contenuto in
 * questa classe.
 * 
 * <pre>
 * &lt;simpleType name="tipologiaFonte"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="INFOC"/&gt;
 *     &lt;enumeration value="AAEP"/&gt;
 *     &lt;enumeration value="INAIL"/&gt;
 *     &lt;enumeration value="TRIBUTI"/&gt;
 *     &lt;enumeration value="SILP"/&gt;
 *     &lt;enumeration value="FP"/&gt;
 *     &lt;enumeration value="SIAP"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "tipologiaFonte")
@XmlEnum
public enum TipologiaFonte {

	INFOC, AAEP, INAIL, TRIBUTI, SILP, FP, SIAP;

	public String value() {
		return name();
	}

	public static TipologiaFonte fromValue(String v) {
		return valueOf(v);
	}

}

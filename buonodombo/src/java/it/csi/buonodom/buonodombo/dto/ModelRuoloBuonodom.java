/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.dto;

import java.util.List;

public class ModelRuoloBuonodom {
	String codRuolo;
	String descRuolo;
	List<ModelProfiliBuonodom> listaProfili;

	public String getCodRuolo() {
		return codRuolo;
	}

	public void setCodRuolo(String codRuolo) {
		this.codRuolo = codRuolo;
	}

	public String getDescRuolo() {
		return descRuolo;
	}

	public void setDescRuolo(String descRuolo) {
		this.descRuolo = descRuolo;
	}

	public List<ModelProfiliBuonodom> getListaProfili() {
		return listaProfili;
	}

	public void setListaProfili(List<ModelProfiliBuonodom> listaProfili) {
		this.listaProfili = listaProfili;
	}

	@Override
	public String toString() {
		return "ModelRuoloBuonodom [codRuolo=" + codRuolo + ", descRuolo=" + descRuolo + ", listaProfili="
				+ listaProfili + "]";
	}

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.dto;

import java.util.List;

public class ModelUserInfoBuonodom {
	String codFisc;
	String cognome;
	String nome;
	List<ModelRuoloBuonodom> listaRuoli;
	List<ModelEnteGestore> listaEntiGestore;

	public String getCodFisc() {
		return codFisc;
	}

	public void setCodFisc(String codFisc) {
		this.codFisc = codFisc;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<ModelRuoloBuonodom> getListaRuoli() {
		return listaRuoli;
	}

	public void setListaRuoli(List<ModelRuoloBuonodom> listaRuoli) {
		this.listaRuoli = listaRuoli;
	}

	public List<ModelEnteGestore> getListaEntiGestore() {
		return listaEntiGestore;
	}

	public void setListaEntiGestore(List<ModelEnteGestore> listaEntiGestore) {
		this.listaEntiGestore = listaEntiGestore;
	}

	@Override
	public String toString() {
		return "ModelUserInfoBuonodom [codFisc=" + codFisc + ", cognome=" + cognome + ", nome=" + nome + ", listaRuoli="
				+ listaRuoli + ", listaEntiGestore=" + listaEntiGestore + "]";
	}

}

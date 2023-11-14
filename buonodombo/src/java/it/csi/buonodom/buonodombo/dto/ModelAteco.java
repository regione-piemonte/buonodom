/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelAteco {
	private String atecoCod = null;
	private String atecoDesc = null;
	private Date atecoVerificatoInData = null;

	@JsonProperty("ateco_verificato_in_data")
	public Date getAtecoVerificatoInData() {
		return atecoVerificatoInData;
	}

	public void setAtecoVerificatoInData(Date atecoVerificatoInData) {
		this.atecoVerificatoInData = atecoVerificatoInData;
	}

	@JsonProperty("ateco_cod")

	public String getAtecoCod() {
		return atecoCod;
	}

	public void setAtecoCod(String atecoCod) {
		this.atecoCod = atecoCod;
	}

	@JsonProperty("ateco_desc")
	public String getAtecoDesc() {
		return atecoDesc;
	}

	public void setAtecoDesc(String atecoDesc) {
		this.atecoDesc = atecoDesc;
	}

	@Override
	public int hashCode() {
		return Objects.hash(atecoCod, atecoDesc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelAteco other = (ModelAteco) obj;
		return Objects.equals(atecoCod, other.atecoCod) && Objects.equals(atecoDesc, other.atecoDesc);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelAteco [atecoCod=");
		builder.append(atecoCod);
		builder.append(", atecoDesc=");
		builder.append(atecoDesc);
		builder.append("]");
		return builder.toString();
	}

}

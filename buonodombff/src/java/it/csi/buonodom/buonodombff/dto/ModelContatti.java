/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelContatti {
	// verra' utilizzata la seguente strategia serializzazione degli attributi:
	// [explicit-as-modeled]

	private String userId = null;
	private String sms = null;
	private String phone = null;
	private String email = null;

	/**
	 * CF dell&#39;utente
	 **/

	@JsonProperty("user_id")

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 **/

	@JsonProperty("sms")

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	/**
	 **/

	@JsonProperty("phone")

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * email dell&#39;utente
	 **/

	@JsonProperty("email")

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ModelContatti modelContatti = (ModelContatti) o;
		return Objects.equals(userId, modelContatti.userId) && Objects.equals(sms, modelContatti.sms)
				&& Objects.equals(phone, modelContatti.phone) && Objects.equals(email, modelContatti.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, sms, phone, email);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ModelContatti {\n");

		sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
		sb.append("    sms: ").append(toIndentedString(sms)).append("\n");
		sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

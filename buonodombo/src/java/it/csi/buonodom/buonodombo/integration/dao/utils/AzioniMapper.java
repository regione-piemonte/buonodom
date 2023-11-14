/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombo.dto.ModelAzione;

public class AzioniMapper implements RowMapper<ModelAzione> {

	@Override
	public ModelAzione mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelAzione azione = new ModelAzione();
		azione.setCodAzione(rs.getString("azione_cod"));
		azione.setDescAzione(rs.getString("azione_desc"));

		return azione;

	}

}

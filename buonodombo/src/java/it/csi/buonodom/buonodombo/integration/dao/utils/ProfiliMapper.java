/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombo.dto.ModelProfiliBuonodom;

public class ProfiliMapper implements RowMapper<ModelProfiliBuonodom> {

	@Override
	public ModelProfiliBuonodom mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelProfiliBuonodom profilo = new ModelProfiliBuonodom();
		profilo.setCodProfilo(rs.getString("profilo_cod"));
		profilo.setDescProfilo(rs.getString("profilo_desc"));

		return profilo;

	}

}

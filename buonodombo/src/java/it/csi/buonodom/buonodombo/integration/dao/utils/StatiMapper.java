/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombo.dto.ModelStati;

public class StatiMapper implements RowMapper<ModelStati> {

	@Override
	public ModelStati mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelStati stato = new ModelStati();
		stato.setCodStato(rs.getString("domanda_stato_cod"));
		stato.setDescStato(rs.getString("domanda_stato_desc"));

		return stato;

	}

}

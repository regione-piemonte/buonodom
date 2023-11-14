/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombff.dto.ModelSportello;

public class SportelliMapper implements RowMapper<ModelSportello> {

	@Override
	public ModelSportello mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelSportello result = new ModelSportello();
		result.setDataFine(rs.getDate("dataFine"));
		result.setDataInizio(rs.getDate("dataInizio"));
		result.setInCorso((Boolean) rs.getObject("inCorso"));

		return result;
	}

}

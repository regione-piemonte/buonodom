/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombff.dto.ModelDecodifica;

public class ListaDecodificaMapper implements RowMapper<ModelDecodifica> {

	@Override
	public ModelDecodifica mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		ModelDecodifica result = new ModelDecodifica();

		result.setCodice(resultSet.getString("codice"));
		result.setEtichetta(resultSet.getString("descrizione"));

		return result;
	}

}

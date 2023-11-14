/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombandisrv.dto.ModelParametro;

public class ListaParametroMapper implements RowMapper<ModelParametro> {

	@Override
	public ModelParametro mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		ModelParametro result = new ModelParametro();

		result.setCodice(resultSet.getString("parametro_cod"));
		result.setValore(resultSet.getString("parametro_valore"));

		return result;
	}

}

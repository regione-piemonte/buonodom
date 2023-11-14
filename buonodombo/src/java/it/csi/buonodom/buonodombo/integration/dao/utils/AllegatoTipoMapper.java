/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombo.dto.ModelAllegato;

public class AllegatoTipoMapper implements RowMapper<ModelAllegato> {

	@Override
	public ModelAllegato mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelAllegato result = new ModelAllegato();
		result.setTipo(rs.getString("allegato_tipo_cod"));
		result.setFilename(rs.getString("file_name"));

		return result;
	}

}

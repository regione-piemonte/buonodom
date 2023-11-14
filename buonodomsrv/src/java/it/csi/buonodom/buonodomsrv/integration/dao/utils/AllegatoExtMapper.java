/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodomsrv.dto.ModelGetAllegatoExt;

public class AllegatoExtMapper implements RowMapper<ModelGetAllegatoExt> {

	@Override
	public ModelGetAllegatoExt mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelGetAllegatoExt result = new ModelGetAllegatoExt();
		result.setFilePath(rs.getString("file_path"));
		result.setFileName(rs.getString("file_name"));
		result.setFileType(rs.getString("file_type"));
		result.setAllegatoId(rs.getBigDecimal("allegato_tipo_id"));
		result.setCodTipoAllegato(rs.getString("allegato_tipo_cod"));
		result.setSportelloId(rs.getBigDecimal("sportello_id"));
		result.setDescTipoAllegato(rs.getString("allegato_tipo_desc"));
		return result;
	}

}

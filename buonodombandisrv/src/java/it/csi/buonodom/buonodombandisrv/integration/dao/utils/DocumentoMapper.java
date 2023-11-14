/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombandisrv.dto.ModelDocumento;

public class DocumentoMapper implements RowMapper<ModelDocumento> {

	@Override
	public ModelDocumento mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelDocumento documento = new ModelDocumento();
		documento.setNomefile(rs.getString("file_name"));
		documento.setTipologia(rs.getString("allegato_tipo_cod"));
		documento.setPath(null);
		return documento;
	}

}

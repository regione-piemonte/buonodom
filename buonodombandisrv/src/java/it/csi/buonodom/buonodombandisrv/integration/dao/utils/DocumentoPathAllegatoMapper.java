/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombandisrv.dto.ModelDocumentoAllegato;

public class DocumentoPathAllegatoMapper implements RowMapper<ModelDocumentoAllegato> {

	@Override
	public ModelDocumentoAllegato mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelDocumentoAllegato documento = new ModelDocumentoAllegato();
		documento.setNomefile(rs.getString("file_name"));
		documento.setTipologia(rs.getString("allegato_tipo_cod"));
		documento.setPath(rs.getString("file_path"));
		documento.setAllegatoId(rs.getBigDecimal("allegato_id"));
		return documento;
	}

}

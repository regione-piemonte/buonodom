/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombandisrv.dto.ModelDocumentoSpesaDettaglio;

public class DocumentoSpesaDettaglioMapper implements RowMapper<ModelDocumentoSpesaDettaglio> {

	@Override
	public ModelDocumentoSpesaDettaglio mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelDocumentoSpesaDettaglio dettaglio = new ModelDocumentoSpesaDettaglio();
		dettaglio.setIdAllegato(rs.getInt("allegato_id"));
		dettaglio.setImporto(rs.getBigDecimal("doc_spesa_det_importo"));
		dettaglio.setData(rs.getDate("doc_spesa_det_data"));
		dettaglio.setTipo(rs.getString("doc_spesa_det_tipo_cod"));

		return dettaglio;
	}

}

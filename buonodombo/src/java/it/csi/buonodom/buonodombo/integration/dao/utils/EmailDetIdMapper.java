/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombo.dto.ModelEmailDetId;

public class EmailDetIdMapper implements RowMapper<ModelEmailDetId> {

	@Override
	public ModelEmailDetId mapRow(ResultSet rs, int rowNum) throws SQLException {

		// Richiesta
		ModelEmailDetId richiesta = new ModelEmailDetId();
		richiesta.setDomandaDetId(rs.getBigDecimal("domanda_det_id"));
		richiesta.setEmail(rs.getString("ente_gestore_email"));
		return richiesta;

	}

}

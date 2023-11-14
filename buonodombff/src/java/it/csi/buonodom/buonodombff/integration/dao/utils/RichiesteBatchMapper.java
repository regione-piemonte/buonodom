/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombff.dto.custom.ModelRichiestaBatch;

public class RichiesteBatchMapper implements RowMapper<ModelRichiestaBatch> {

	@Override
	public ModelRichiestaBatch mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelRichiestaBatch richiesta = new ModelRichiestaBatch();
		richiesta.setSportelloId(rs.getLong("sportello_id"));
		richiesta.setDomandaId(rs.getLong("domanda_id"));
		richiesta.setDomandaDetId(rs.getLong("domanda_det_id"));
		richiesta.setStato(rs.getString("domanda_stato_cod"));
		return richiesta;
	}

}

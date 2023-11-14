/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodomsrv.dto.ModelRichiestaRecupero;

public class DettaglioRichiestaRecuperoMapper implements RowMapper<ModelRichiestaRecupero> {

	@Override
	public ModelRichiestaRecupero mapRow(ResultSet rs, int rowNum) throws SQLException {

		// Richiesta
		ModelRichiestaRecupero richiesta = new ModelRichiestaRecupero();
		richiesta.setNumero(rs.getString("domanda_numero"));
		richiesta.setDomandaDetId(rs.getBigDecimal("domanda_det_id"));
		richiesta.setNote(rs.getString("note"));
		return richiesta;

	}

}

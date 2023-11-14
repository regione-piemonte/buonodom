/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombff.dto.ModelFornitore;

public class FornitoriMapper implements RowMapper<ModelFornitore> {

	@Override
	public ModelFornitore mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelFornitore fornitore = new ModelFornitore();
		fornitore.setId(rs.getInt("id"));
		fornitore.setNome(rs.getString("nome"));
		fornitore.setCognome(rs.getString("cognome"));
		fornitore.setCf(rs.getString("cf"));
		fornitore.setDenominazione(rs.getString("denominazione"));
		fornitore.setPiva(rs.getString("piva"));
		return fornitore;
	}

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombff.dto.ModelContrattoFornitore;

public class ContrattoFornitoreMapper implements RowMapper<ModelContrattoFornitore> {

	@Override
	public ModelContrattoFornitore mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelContrattoFornitore fornitore = new ModelContrattoFornitore();
		fornitore.setContrattoId(rs.getBigDecimal("contratto_id"));
		fornitore.setFornitoreId(rs.getBigDecimal("fornitore_id"));
		fornitore.setStato(rs.getString("doc_spesa_stato_cod"));
		return fornitore;
	}

}

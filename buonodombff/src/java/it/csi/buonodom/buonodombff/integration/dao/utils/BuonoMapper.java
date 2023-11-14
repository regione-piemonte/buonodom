/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombff.dto.custom.ModelBuono;

public class BuonoMapper implements RowMapper<ModelBuono> {

	@Override
	public ModelBuono mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelBuono result = new ModelBuono();
		result.setBuonoCod(rs.getString("buonoCod"));
		result.setStato(rs.getString("stato"));
		result.setBuonoId(rs.getLong("buonoId"));
		result.setBuonoStatoId(rs.getLong("buonoStatoId"));
		result.setRichiedenteCf(rs.getString("richiedenteCf"));
		result.setIban(rs.getString("iban"));
		result.setIbanIntestatario(rs.getString("iban_intestatario"));
		return result;
	}

}

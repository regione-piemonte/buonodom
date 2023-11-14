/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombandisrv.dto.DatiBuono;

public class DatiBuonoMapper implements RowMapper<DatiBuono> {

	@Override
	public DatiBuono mapRow(ResultSet rs, int rowNum) throws SQLException {

		DatiBuono buono = new DatiBuono();
		buono.setBuonoId(rs.getBigDecimal("buono_id"));
		buono.setIban(rs.getString("iban"));
		buono.setIbanIntestatario(rs.getString("iban_intestatario"));

		return buono;

	}

}

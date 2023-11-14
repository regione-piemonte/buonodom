/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodomcallban.dto.DatiDichiarazione;

public class DomandaMapper implements RowMapper<DatiDichiarazione> {

	@Override
	public DatiDichiarazione mapRow(ResultSet rs, int rowNum) throws SQLException {

		DatiDichiarazione dichiarazione = new DatiDichiarazione();
		dichiarazione.setBuonoId(rs.getBigDecimal("buono_id"));
		dichiarazione.setDomandaNumero(rs.getString("domanda_numero"));

		return dichiarazione;

	}

}

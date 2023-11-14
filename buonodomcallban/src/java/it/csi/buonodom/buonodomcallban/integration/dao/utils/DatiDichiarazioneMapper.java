/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodomcallban.dto.DatiDichiarazione;

public class DatiDichiarazioneMapper implements RowMapper<DatiDichiarazione> {

	@Override
	public DatiDichiarazione mapRow(ResultSet rs, int rowNum) throws SQLException {

		DatiDichiarazione dichiarazione = new DatiDichiarazione();
		dichiarazione.setDicSpesaCod(rs.getString("dic_spesa_cod"));
		dichiarazione.setDicSpesaStatoCod(rs.getString("dic_spesa_stato_cod"));
		dichiarazione.setDicSpesaId(rs.getBigDecimal("dic_spesa_id"));
		dichiarazione.setBuonoId(rs.getBigDecimal("buono_id"));
		dichiarazione.setDomandaNumero(rs.getString("domanda_numero"));

		return dichiarazione;

	}

}

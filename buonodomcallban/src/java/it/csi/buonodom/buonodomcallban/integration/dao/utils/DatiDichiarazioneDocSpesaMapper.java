/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodomcallban.dto.DatiDichiarazioneDocSpesa;

public class DatiDichiarazioneDocSpesaMapper implements RowMapper<DatiDichiarazioneDocSpesa> {

	@Override
	public DatiDichiarazioneDocSpesa mapRow(ResultSet rs, int rowNum) throws SQLException {

		DatiDichiarazioneDocSpesa dichiarazione = new DatiDichiarazioneDocSpesa();
		dichiarazione.setDocSpesaCod(rs.getString("doc_spesa_cod"));
		dichiarazione.setDocSpesaStatoCod(rs.getString("doc_spesa_stato_cod"));
		dichiarazione.setDocSpesaId(rs.getBigDecimal("doc_spesa_id"));

		return dichiarazione;

	}

}

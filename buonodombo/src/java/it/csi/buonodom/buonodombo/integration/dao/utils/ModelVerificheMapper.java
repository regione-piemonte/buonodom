/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombo.dto.ModelVerificheDomanda;

public class ModelVerificheMapper implements RowMapper<ModelVerificheDomanda> {

	@Override
	public ModelVerificheDomanda mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelVerificheDomanda result = new ModelVerificheDomanda();
		result.setNumeroDomanda(rs.getString("domanda_numero"));
		// result.setStatoDomanda(rs.getString("domanda_stato_desc"));
		result.setDataInizioValidita(rs.getTimestamp("validita_inizio"));
		// result.setAteco(rs.getString("ateco"));
//		result.setDataVerificaAteco(rs.getTimestamp("ateco_verificato_in_data"));
//		result.setIsee(rs.getString("isee"));
//		result.setDataVerificaIsee(rs.getTimestamp("isee_verificato_in_data"));
		result.setMisure(rs.getString("misure"));
//		result.setIncompatibilita(rs.getString("incompatibilita"));
		result.setTipo(rs.getString("tipo"));
		result.setFonte(rs.getString("fonte"));

		return result;
	}

}

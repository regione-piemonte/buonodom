/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombo.dto.ModelDescrizioneGraduatoria;

public class DescrizioneGraduatoriaMapper implements RowMapper<ModelDescrizioneGraduatoria> {

	@Override
	public ModelDescrizioneGraduatoria mapRow(ResultSet rs, int rNum) throws SQLException {
		ModelDescrizioneGraduatoria descrizione = new ModelDescrizioneGraduatoria();
		descrizione.setDescrizioneGraduatoria(rs.getString("descrizioneGraduatoria"));
		descrizione.setCodiceGraduatoria(rs.getString("codiceGraduatoria"));
		descrizione.setStatoGraduatoria(rs.getString("statoGraduatoria"));
		return descrizione;
	}

}

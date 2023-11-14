/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodomsrv.dto.ModelIncompatibilitaRichiesta;

public class IncompatibilitaMapper implements RowMapper<ModelIncompatibilitaRichiesta> {

	@Override
	public ModelIncompatibilitaRichiesta mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelIncompatibilitaRichiesta incompatibilita = new ModelIncompatibilitaRichiesta();
		incompatibilita.setNessunaIncompatibilita((Boolean) rs.getObject("nessuna_incompatibilita"));
		incompatibilita.setIncompatibilitaPerContratto((Boolean) rs.getObject("incompatibilita_per_contratto"));
		return incompatibilita;

	}

}

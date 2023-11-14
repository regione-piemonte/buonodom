/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombandisrv.dto.ModelInvioFornitore;

public class FornitoreMapper implements RowMapper<ModelInvioFornitore> {

	@Override
	public ModelInvioFornitore mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelInvioFornitore fornitore = new ModelInvioFornitore();
		fornitore.setCodiceFiscaleFornitore(rs.getString("fornitore_cf"));
//		fornitore.setCognome(rs.getString("fornitore_cognome"));
//		fornitore.setNome(rs.getString("fornitore_nome"));
//		fornitore.setDenominazioneFornitore(rs.getString("fornitore_denominazione"));
//		fornitore.setDataInizioContratto(rs.getString("contratto_data_inizio"));
//		fornitore.setDataFineContratto(rs.getString("contratto_data_fine"));

		return fornitore;
	}

}

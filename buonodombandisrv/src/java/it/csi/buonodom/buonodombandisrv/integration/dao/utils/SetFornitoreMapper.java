/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombandisrv.dto.ModelSetFornitore;

public class SetFornitoreMapper implements RowMapper<ModelSetFornitore> {

	@Override
	public ModelSetFornitore mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelSetFornitore fornitore = new ModelSetFornitore();
		fornitore.setCodiceFiscale(rs.getString("fornitore_cf"));
		fornitore.setCognome(rs.getString("fornitore_cognome"));
		fornitore.setNome(rs.getString("fornitore_nome"));
		fornitore.setDenominazione(rs.getString("fornitore_denominazione"));
		fornitore.setDataInizio(rs.getString("contratto_data_inizio"));
		fornitore.setDataFine(rs.getString("contratto_data_fine"));
		fornitore.setPartitaIva(rs.getString("fornitore_piva"));
		fornitore.setCodiceFormaGiuridica(null);

		return fornitore;
	}

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombff.dto.ModelContratto;
import it.csi.buonodom.buonodombff.dto.ModelContrattoAgenzia;
import it.csi.buonodom.buonodombff.dto.ModelPersonaSintesi;

public class ContrattoMapper implements RowMapper<ModelContratto> {

	@Override
	public ModelContratto mapRow(ResultSet rs, int rowNum) throws SQLException {
		ModelContratto result = new ModelContratto();
		result.setId(rs.getInt("contratto_id"));
		result.setTipo(rs.getString("contratto_tipo_cod"));

		// Intestatario
		ModelPersonaSintesi intestatario = new ModelPersonaSintesi();
		intestatario.setCf(rs.getString("datore_di_lavoro_cf"));
		intestatario.setNome(rs.getString("datore_di_lavoro_nome"));
		intestatario.setCognome(rs.getString("datore_di_lavoro_cognome"));
		intestatario.setDataNascita(rs.getDate("datore_di_lavoro_nascita_data"));
		intestatario.setStatoNascita(rs.getString("datore_di_lavoro_nascita_stato"));
		intestatario.setComuneNascita(rs.getString("datore_di_lavoro_nascita_comune"));
		intestatario.setProvinciaNascita(rs.getString("datore_di_lavoro_nascita_provincia"));
		result.setIntestatario(intestatario);

		result.setRelazioneDestinatario(rs.getString("rapporto_tipo_cod"));

		// Assistente famigliare
		ModelPersonaSintesi assistenteFamigliare = new ModelPersonaSintesi();
		assistenteFamigliare.setCf(rs.getString("fornitore_cf"));
		assistenteFamigliare.setNome(rs.getString("fornitore_nome"));
		assistenteFamigliare.setCognome(rs.getString("fornitore_cognome"));
		assistenteFamigliare.setDataNascita(rs.getDate("fornitore_nascita_data"));
		assistenteFamigliare.setStatoNascita(rs.getString("fornitore_nascita_stato"));
		assistenteFamigliare.setComuneNascita(rs.getString("fornitore_nascita_comune"));
		assistenteFamigliare.setProvinciaNascita(rs.getString("fornitore_nascita_provincia"));
		result.setAssistenteFamiliare(assistenteFamigliare);

		result.setPivaAssitenteFamiliare(rs.getString("fornitore_piva"));
		result.setTipoSupportoFamiliare(rs.getString("fornitore_tipo_cod"));
		result.setDataInizio(rs.getTimestamp("contratto_data_inizio"));
		result.setDataFine(rs.getTimestamp("contratto_data_fine"));

		// Assistente famigliare
		ModelContrattoAgenzia agenzia = new ModelContrattoAgenzia();
		agenzia.setCf(null);
		result.setAgenzia(agenzia);

		result.setIncompatibilitaPerContratto(null);

		return result;
	}

}

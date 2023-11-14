/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombo.dto.ModelDomandeAperta;
import it.csi.buonodom.buonodombo.dto.ModelEnteGestore;
import it.csi.buonodom.buonodombo.dto.ModelStati;
import it.csi.buonodom.buonodombo.dto.ModelVerifiche;

public class DomandeAperteMapper implements RowMapper<ModelDomandeAperta> {

	@Override
	public ModelDomandeAperta mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelDomandeAperta domanda = new ModelDomandeAperta();
		domanda.setIdDomanda(rs.getInt("domanda_id"));
		domanda.setNumeroDomanda(rs.getString("domanda_numero"));
		domanda.setCfDestinatario(rs.getString("beneficiario_cf"));
		domanda.setNomeDestinatario(rs.getString("destinatario_nome"));
		domanda.setCognomeDestinatario(rs.getString("destinatario_cognome"));
		domanda.setCfRichiedente(rs.getString("richiedente_cf"));
		domanda.setNomeRichiedente(rs.getString("richiedente_nome"));
		domanda.setCognomeRichiedente(rs.getString("richiedente_cognome"));
		domanda.setDataDomanda(rs.getString("domanda_data"));
		ModelStati stato = new ModelStati();
		stato.setCodStato(rs.getString("domanda_stato_cod"));
		stato.setDescStato(rs.getString("domanda_stato_desc"));
		domanda.setStato(stato);
		ModelVerifiche verifiche = new ModelVerifiche();
		verifiche.setVerificaEgRichiesta((Boolean) rs.getObject("verifica_eg_richiesta"));
		verifiche.setVerificaEgInCorso((Boolean) rs.getObject("verifica_eg_in_corso"));
		verifiche.setVerificaEgConclusa((Boolean) rs.getObject("verifica_eg_conclusa"));
		domanda.setVerifiche(verifiche);
		ModelEnteGestore enteGestore = new ModelEnteGestore();
		enteGestore.setIdEnteGestore(rs.getInt("ente_gestore_id"));
		enteGestore.setDenominazioneEnte(rs.getString("ente_gestore_denominazione"));
		domanda.setEnteGestore(enteGestore);

		return domanda;

	}

}

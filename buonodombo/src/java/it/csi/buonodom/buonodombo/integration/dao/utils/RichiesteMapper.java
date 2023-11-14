/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombo.dto.ModelPersona;
import it.csi.buonodom.buonodombo.dto.ModelProtocollo;
import it.csi.buonodom.buonodombo.dto.ModelRichiesta;
import it.csi.buonodom.buonodombo.util.Converter;

public class RichiesteMapper implements RowMapper<ModelRichiesta> {

	@Override
	public ModelRichiesta mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelRichiesta richiesta = new ModelRichiesta();
		ModelProtocollo protocollo = new ModelProtocollo();
		richiesta.setNumero(rs.getString("domanda_numero"));
		richiesta.setStato(rs.getString("domanda_stato_cod"));
		richiesta.setDataAggiornamento(rs.getDate("data_creazione"));
		richiesta.setNote(rs.getString("note"));
		protocollo.setNumero(rs.getString("protocollo_cod"));
		protocollo.setData(Converter.getDataWithoutTime(rs.getString("data_protocollo")));
		protocollo.setTipo(rs.getString("tipo_protocollo"));
		richiesta.setProtocollo(protocollo);
		ModelPersona richiedente = new ModelPersona();
		richiedente.setCf(rs.getString("richiedente_cf"));
		richiedente.setNome(rs.getString("richiedente_nome"));
		richiedente.setCognome(rs.getString("richiedente_cognome"));
		richiedente.setStatoNascita(rs.getString("richiedente_nascita_stato"));
		richiedente.setDataNascita(Converter.getDataWithoutTime(rs.getString("richiedente_nascita_data")));
		richiedente.setComuneNascita(rs.getString("richiedente_nascita_comune"));
		richiedente.setProvinciaNascita(rs.getString("richiedente_nascita_provincia"));
		richiedente.setIndirizzoResidenza(rs.getString("richiedente_residenza_indirizzo"));
		richiedente.setComuneResidenza(rs.getString("richiedente_residenza_comune"));
		richiedente.setProvinciaResidenza(rs.getString("richiedente_residenza_provincia"));
		richiesta.setRichiedente(richiedente);

		ModelPersona destinatario = new ModelPersona();
		destinatario.setCf(rs.getString("beneficiario_cf"));
		destinatario.setNome(rs.getString("destinatario_nome"));
		destinatario.setCognome(rs.getString("destinatario_cognome"));
		destinatario.setDataNascita(Converter.getDataWithoutTime(rs.getString("destinatario_nascita_data")));
		destinatario.setStatoNascita(rs.getString("destinatario_nascita_stato"));
		destinatario.setComuneNascita(rs.getString("destinatario_nascita_comune"));
		destinatario.setProvinciaNascita(rs.getString("destinatario_nascita_provincia"));
		destinatario.setIndirizzoResidenza(rs.getString("destinatario_residenza_indirizzo"));
		destinatario.setComuneResidenza(rs.getString("destinatario_residenza_comune"));
		destinatario.setProvinciaResidenza(rs.getString("destinatario_residenza_provincia"));
		richiesta.setDestinatario(destinatario);

		return richiesta;

	}

}

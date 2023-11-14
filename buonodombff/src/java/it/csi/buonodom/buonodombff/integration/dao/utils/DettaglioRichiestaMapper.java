/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombff.dto.ModelBozzaRichiestaDomicilioDestinatario;
import it.csi.buonodom.buonodombff.dto.ModelPersona;
import it.csi.buonodom.buonodombff.dto.ModelPersonaSintesi;
import it.csi.buonodom.buonodombff.dto.ModelProtocollo;
import it.csi.buonodom.buonodombff.dto.ModelRichiesta;
import it.csi.buonodom.buonodombff.dto.ModelRichiestaAccredito;
import it.csi.buonodom.buonodombff.dto.ModelRichiestaContratto;
import it.csi.buonodom.buonodombff.dto.ModelRichiestaContrattoAgenzia;
import it.csi.buonodom.buonodombff.util.Converter;

public class DettaglioRichiestaMapper implements RowMapper<ModelRichiesta> {

	@Override
	public ModelRichiesta mapRow(ResultSet rs, int rowNum) throws SQLException {

		// Richiesta
		ModelRichiesta richiesta = new ModelRichiesta();
		ModelProtocollo protocollo = new ModelProtocollo();
		richiesta.setNumero(rs.getString("domanda_numero"));
		richiesta.setStato(rs.getString("domanda_stato_cod"));
		richiesta.setDataAggiornamento(rs.getDate("data_creazione"));
		richiesta.setNote(rs.getString("note"));
		richiesta.setStudioDestinatario(rs.getString("titolo_studio_cod"));
		richiesta.setAslDestinatario(rs.getString("asl_cod"));
		richiesta.setPunteggioBisognoSociale(rs.getBigDecimal("punteggio_sociale"));
		richiesta.setLavoroDestinatario((Boolean) rs.getObject("situazione_lavorativa_attiva"));
		richiesta.setDelega(rs.getString("delega"));
		protocollo.setNumero(rs.getString("protocollo_cod"));
		protocollo.setData(Converter.getDataWithoutTime(rs.getString("data_protocollo")));
		protocollo.setTipo(rs.getString("tipo_protocollo"));
		richiesta.setProtocollo(protocollo);

		richiesta.setAttestazioneIsee((Boolean) rs.getObject("isee_conforme"));
		richiesta.setValutazioneMultidimensionale(rs.getString("valutazione_multidimensionale_cod"));
		richiesta.setNessunaIncompatibilita((Boolean) rs.getObject("nessuna_incompatibilita"));
		// Richiedente
		ModelPersona richiedente = new ModelPersona();
		richiedente.setCf(rs.getString("richiedente_cf"));
		richiedente.setNome(rs.getString("richiedente_nome"));
		richiedente.setCognome(rs.getString("richiedente_cognome"));
		richiedente.setStatoNascita(rs.getString("richiedente_nascita_stato"));
		richiedente.setDataNascita(rs.getDate("richiedente_nascita_data"));
		richiedente.setComuneNascita(rs.getString("richiedente_nascita_comune"));
		richiedente.setProvinciaNascita(rs.getString("richiedente_nascita_provincia"));
		richiedente.setIndirizzoResidenza(rs.getString("richiedente_residenza_indirizzo"));
		richiedente.setComuneResidenza(rs.getString("richiedente_residenza_comune"));
		richiedente.setProvinciaResidenza(rs.getString("richiedente_residenza_provincia"));
		richiesta.setRichiedente(richiedente);
		// destinatario
		ModelPersona destinatario = new ModelPersona();
		destinatario.setCf(rs.getString("beneficiario_cf"));
		destinatario.setNome(rs.getString("destinatario_nome"));
		destinatario.setCognome(rs.getString("destinatario_cognome"));
		destinatario.setDataNascita(rs.getDate("destinatario_nascita_data"));
		destinatario.setStatoNascita(rs.getString("destinatario_nascita_stato"));
		destinatario.setComuneNascita(rs.getString("destinatario_nascita_comune"));
		destinatario.setProvinciaNascita(rs.getString("destinatario_nascita_provincia"));
		destinatario.setIndirizzoResidenza(rs.getString("destinatario_residenza_indirizzo"));
		destinatario.setComuneResidenza(rs.getString("destinatario_residenza_comune"));
		destinatario.setProvinciaResidenza(rs.getString("destinatario_residenza_provincia"));
		richiesta.setDestinatario(destinatario);
		// domicilioDestinatario
		ModelBozzaRichiestaDomicilioDestinatario domicilioDestinatario = new ModelBozzaRichiestaDomicilioDestinatario();
		domicilioDestinatario.setComune(rs.getString("destinatario_domicilio_comune"));
		domicilioDestinatario.setProvincia(rs.getString("destinatario_domicilio_provincia"));
		domicilioDestinatario.setIndirizzo(rs.getString("destinatario_domicilio_indirizzo"));
		richiesta.setDomicilioDestinatario(domicilioDestinatario);
		// Contratto
		ModelRichiestaContratto contratto = new ModelRichiestaContratto();
		contratto.setTipo(rs.getString("contratto_tipo_cod"));
		contratto.setRelazioneDestinatario(rs.getString("relazione_destinatario"));
		contratto.setDataInizio(rs.getTimestamp("contratto_data_inizio"));
		contratto.setDataFine(rs.getTimestamp("contratto_data_fine"));
		contratto.setIncompatibilitaPerContratto((Boolean) rs.getObject("incompatibilita_per_contratto"));
		contratto.setTipoSupportoFamiliare(rs.getString("assistente_tipo_cod"));
		contratto.setPivaAssitenteFamiliare(rs.getString("assistente_familiare_piva"));
		ModelPersonaSintesi intestatario = new ModelPersonaSintesi();
		intestatario.setCf(rs.getString("datore_di_lavoro_cf"));
		intestatario.setNome(rs.getString("datore_di_lavoro_nome"));
		intestatario.setCognome(rs.getString("datore_di_lavoro_cognome"));
		intestatario.setDataNascita(rs.getDate("datore_di_lavoro_nascita_data"));
		intestatario.setStatoNascita(rs.getString("datore_di_lavoro_nascita_stato"));
		intestatario.setComuneNascita(rs.getString("datore_di_lavoro_nascita_comune"));
		intestatario.setProvinciaNascita(rs.getString("datore_di_lavoro_nascita_provincia"));
		contratto.setIntestatario(intestatario);

		ModelPersonaSintesi assistenteFamiliare = new ModelPersonaSintesi();
		assistenteFamiliare.setCf(rs.getString("assistente_familiare_cf"));
		assistenteFamiliare.setNome(rs.getString("assistente_familiare_nome"));
		assistenteFamiliare.setCognome(rs.getString("assistente_familiare_cognome"));
		assistenteFamiliare.setDataNascita(rs.getDate("assistente_familiare_nascita_data"));
		assistenteFamiliare.setStatoNascita(rs.getString("assistente_familiare_nascita_stato"));
		assistenteFamiliare.setComuneNascita(rs.getString("assistente_familiare_nascita_comune"));
		assistenteFamiliare.setProvinciaNascita(rs.getString("assistente_familiare_nascita_provincia"));
		contratto.setAssistenteFamiliare(assistenteFamiliare);

		ModelRichiestaContrattoAgenzia agenzia = new ModelRichiestaContrattoAgenzia();
		agenzia.setCf(rs.getString("contratto_cf_cooperativa"));
		contratto.setAgenzia(agenzia);

		richiesta.setContratto(contratto);
		// accredito
		ModelRichiestaAccredito accredito = new ModelRichiestaAccredito();
		accredito.setIban(rs.getString("iban"));
		accredito.setIntestatario(rs.getString("iban_intestatario"));
		richiesta.setAccredito(accredito);

		richiesta.setNoteRichiedente(rs.getString("note_richiedente"));

		return richiesta;

	}

}

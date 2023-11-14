/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.utils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombo.dto.ModelAteco;
import it.csi.buonodom.buonodombo.dto.ModelBozzaRichiestaDomicilioDestinatario;
import it.csi.buonodom.buonodombo.dto.ModelIsee;
import it.csi.buonodom.buonodombo.dto.ModelPersona;
import it.csi.buonodom.buonodombo.dto.ModelPersonaSintesi;
import it.csi.buonodom.buonodombo.dto.ModelProtocollo;
import it.csi.buonodom.buonodombo.dto.ModelRichiesta;
import it.csi.buonodom.buonodombo.dto.ModelRichiestaAccredito;
import it.csi.buonodom.buonodombo.dto.ModelRichiestaContratto;
import it.csi.buonodom.buonodombo.dto.ModelRichiestaContrattoAgenzia;
import it.csi.buonodom.buonodombo.dto.ModelVerifiche;
import it.csi.buonodom.buonodombo.util.Converter;

public class DettaglioRichiestaMapper implements RowMapper<ModelRichiesta> {

	@Override
	public ModelRichiesta mapRow(ResultSet rs, int rowNum) throws SQLException {

		// Richiesta
		ModelRichiesta richiesta = new ModelRichiesta();
		ModelProtocollo protocollo = new ModelProtocollo();
		richiesta.setNumero(rs.getString("domanda_numero"));
		richiesta.setDomandaDetId(rs.getBigDecimal("domanda_det_id"));
		richiesta.setSportelloId(rs.getBigDecimal("sportello_id"));
		richiesta.setStato(rs.getString("domanda_stato_cod"));
		richiesta.setDomandaStatoDesc(rs.getString("domanda_stato_desc"));
		richiesta.setDataAggiornamento(rs.getDate("data_creazione"));
		richiesta.setNote(rs.getString("note"));
		richiesta.setStudioDestinatario(rs.getString("titolo_studio_cod"));
		richiesta.setStudioDestinatarioDesc(rs.getString("titolo_studio_desc"));
		richiesta.setAslDestinatario(rs.getString("asl_cod"));
		richiesta.setAslDestinatarioDesc(rs.getString("asl_azienda_desc"));
		richiesta.setPunteggioBisognoSociale(rs.getBigDecimal("punteggio_sociale"));
		richiesta.setLavoroDestinatario(rs.getString("situazione_lavorativa_attiva"));
		richiesta.setDelegaCod(rs.getString("delega_cod"));
		richiesta.setDelegaDesc(rs.getString("delega_desc"));
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
		contratto.setRelazioneDestinatarioDesc(rs.getString("relazione_destinatario_desc"));
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
		richiesta.setContributoTipoDesc(rs.getString("contributo_tipo_desc"));
		richiesta.setAreaDesc(rs.getString("area_desc"));
		ModelAteco ateco = new ModelAteco();
		ateco.setAtecoCod(rs.getString("ateco_cod"));
		ateco.setAtecoDesc(rs.getString("ateco_desc"));
		ateco.setAtecoVerificatoInData(rs.getTimestamp("ateco_verificato_in_data"));
		richiesta.setAteco(ateco);
		ModelIsee isee = new ModelIsee();
		isee.setIseeValore((BigDecimal) rs.getObject("isee_valore"));
		isee.setIseeDataRilascio(Converter.getDataWithoutTime(rs.getString("isee_data_rilascio")));
		isee.setIseeScadenza(Converter.getDataWithoutTime(rs.getString("isee_scadenza")));
		isee.setIseeVerificatoConforme((Boolean) rs.getObject("isee_verificato_conforme"));
		isee.setIseeVerificatoInData(rs.getTimestamp("isee_verificato_in_data"));
		richiesta.setIsee(isee);
		ModelVerifiche verifiche = new ModelVerifiche();
		verifiche.setVerificaEgConclusa((Boolean) rs.getObject("verifica_eg_conclusa"));
		verifiche.setVerificaEgIncompatibilita((Boolean) rs.getObject("verifica_eg_incompatibilita"));
		verifiche.setVerificaEgInCorso((Boolean) rs.getObject("verifica_eg_in_corso"));
		verifiche.setVerificaEgPunteggio(rs.getString("verifica_eg_punteggio_sociale"));
		verifiche.setVerificaEgRichiesta((Boolean) rs.getObject("verifica_eg_richiesta"));
		verifiche.setNoteEnteGestore(rs.getString("note_ente_gestore"));
		richiesta.setVerifiche(verifiche);
		richiesta.setNoteInterna(rs.getString("nota_interna"));
		richiesta.setNoteRichiedente(rs.getString("note_richiedente"));
		return richiesta;

	}

}

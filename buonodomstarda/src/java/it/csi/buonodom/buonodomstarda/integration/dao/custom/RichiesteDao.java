/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomstarda.integration.dao.custom;

import java.sql.Timestamp;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodomstarda.exception.DatabaseException;
import it.csi.buonodom.buonodomstarda.util.LoggerUtil;

@Repository
public class RichiesteDao extends LoggerUtil {

	public static final String SELECT_ID_DOMANDA = "select " + " btd.domanda_det_id   "
			+ " from bdom_t_domanda_dettaglio btd " + " where  " + " btd.domanda_det_cod = :domandadetcod";

	public static final String SELECT_STATO_DOMANDA = "select b.domanda_stato_cod "
			+ "from bdom_t_domanda_dettaglio a , bdom_d_domanda_stato b " + "where "
			+ "a.domanda_det_cod = :domandadetcod " + "and a.domanda_stato_id =b.domanda_stato_id";

	public static final String UPDATE_DETTAGLIO_DOMANDA_CHIUDI = "update bdom_t_domanda_dettaglio "
			+ "set validita_fine=now(), data_modifica=now(), utente_modifica='STARDAS' "
			+ "where domanda_det_cod = :domandadetcod";

	public static final String INSERT_DETTAGLIO_DOMANDA = "INSERT INTO bdom_t_domanda_dettaglio (domanda_id, sportello_id, domanda_stato_id, isee_valore, isee_data_rilascio, "
			+ "isee_scadenza, punteggio_sociale, nessuna_incompatibilita, incompatibilita_per_contratto, contratto_cf_cooperativa, assistente_familiare_nome, "
			+ "assistente_familiare_cognome, assistente_familiare_cf, assistente_familiare_nascita_stato, richiedente_nome, richiedente_cognome, richiedente_nascita_data, "
			+ "richiedente_nascita_stato, richiedente_nascita_comune, richiedente_nascita_provincia, richiedente_residenza_indirizzo, richiedente_residenza_comune, "
			+ "richiedente_residenza_provincia, destinatario_nome, destinatario_cognome, destinatario_nascita_data, destinatario_nascita_comune, "
			+ "destinatario_nascita_provincia, destinatario_nascita_stato, destinatario_residenza_indirizzo, destinatario_residenza_comune, "
			+ "destinatario_residenza_provincia, destinatario_domicilio_indirizzo, destinatario_domicilio_comune, destinatario_domicilio_provincia, "
			+ "situazione_lavorativa_attiva, datore_di_lavoro_nome, datore_di_lavoro_cognome, datore_di_lavoro_cf, datore_di_lavoro_nascita_data, "
			+ "datore_di_lavoro_nascita_comune, datore_di_lavoro_nascita_provincia, datore_di_lavoro_nascita_stato, iban, iban_intestatario, "
			+ "protocollo_cod, ateco_cod, note, contratto_tipo_id, rapporto_tipo_id, titolo_studio_id, area_id, asl_id, ruolo_cod, "
			+ "verifica_eg_richiesta, verifica_eg_in_corso, verifica_eg_conclusa, verifica_eg_punteggio_sociale, verifica_eg_incompatibilita, "
			+ "validita_inizio, validita_fine, data_creazione, data_modifica, data_cancellazione, utente_creazione, utente_modifica, "
			+ "utente_cancellazione, relazione_tipo_id, isee_conforme, valutazione_multidimensionale_id, assistente_familiare_nascita_comune, "
			+ "assistente_familiare_nascita_data, assistente_familiare_nascita_provincia) "
			+ "select domanda_id, sportello_id, (select domanda_stato_id  from bdom_d_domanda_stato "
			+ "where domanda_stato_cod =:domandastatocod), isee_valore, isee_data_rilascio, "
			+ "isee_scadenza, punteggio_sociale, nessuna_incompatibilita, incompatibilita_per_contratto, contratto_cf_cooperativa, assistente_familiare_nome, "
			+ "assistente_familiare_cognome, assistente_familiare_cf, assistente_familiare_nascita_stato, richiedente_nome, richiedente_cognome, richiedente_nascita_data, "
			+ "richiedente_nascita_stato, richiedente_nascita_comune, richiedente_nascita_provincia, richiedente_residenza_indirizzo, richiedente_residenza_comune, "
			+ "richiedente_residenza_provincia, destinatario_nome, destinatario_cognome, destinatario_nascita_data, destinatario_nascita_comune, "
			+ "destinatario_nascita_provincia, destinatario_nascita_stato, destinatario_residenza_indirizzo, destinatario_residenza_comune, "
			+ "destinatario_residenza_provincia, destinatario_domicilio_indirizzo, destinatario_domicilio_comune, destinatario_domicilio_provincia, "
			+ "situazione_lavorativa_attiva, datore_di_lavoro_nome, datore_di_lavoro_cognome, datore_di_lavoro_cf, datore_di_lavoro_nascita_data, "
			+ "datore_di_lavoro_nascita_comune, datore_di_lavoro_nascita_provincia, datore_di_lavoro_nascita_stato, iban, iban_intestatario, "
			+ "null, ateco_cod, note, contratto_tipo_id, rapporto_tipo_id, titolo_studio_id, area_id, asl_id, ruolo_cod, "
			+ "verifica_eg_richiesta, verifica_eg_in_corso, verifica_eg_conclusa, verifica_eg_punteggio_sociale, verifica_eg_incompatibilita, "
			+ "now(), null, now(), now(), null, 'STARDAS', 'STARDAS', "
			+ "null, relazione_tipo_id, isee_conforme, valutazione_multidimensionale_id, assistente_familiare_nascita_comune, "
			+ "assistente_familiare_nascita_data, assistente_familiare_nascita_provincia "
			+ "from bdom_t_domanda_dettaglio where domanda_det_cod =:domandadetcod";

	public static final String UPDATE_DETTAGLIO_DOMANDA_APRI = "update bdom_t_domanda_dettaglio "
			+ "set domanda_det_cod = :domandadetcod " + "where domanda_det_id = :domandadetid";

	public static final String INSERT_ALLEGATO_DOMANDA = "INSERT INTO bdom_t_allegato "
			+ "(file_name, file_type, file_path, sportello_id,domanda_det_id, domanda_det_cod, "
			+ "allegato_tipo_id, data_creazione,data_modifica, utente_creazione, utente_modifica) "
			+ "select file_name, file_type, file_path, sportello_id,:domandadetid,:domandadetcodnew,allegato_tipo_id,now(),now(),'STARDAS', 'STARDAS' "
			+ "from bdom_t_allegato where domanda_det_cod=:domandadetcod";

	public static final String UPDATE_DETTAGLIO_DOMANDA_PROTOCOLLO = "update bdom_t_domanda_dettaglio "
			+ "set data_modifica=now(), utente_modifica='STARDAS_CALLBACK', protocollo_cod= :protocollocod,data_protocollo=:dataprotocollo,tipo_protocollo=:tipoprotocollo "
			+ "where domanda_det_cod || '_' || :tipoDocumento = :idDocumentoFruitore";

	public static final String SELECT_MESSAGE_UUID = "select count(*) " + " from bdom_t_domanda_dettaglio btd "
			+ " where  " + "btd.messageuuid_protocollo = :messageuuid";

	public static final String SELECT_DOCUMENTO_FRUITORE = "select count(*) "
			+ " from bdom_t_domanda_dettaglio btd,bdom_t_allegato bta,bdom_d_allegato_tipo bdat "
			+ " where  bta.domanda_det_id = btd.domanda_det_id " + " and bdat.allegato_tipo_id = bta.allegato_tipo_id "
			+ "and btd.domanda_det_cod || '_' || bdat.allegato_tipo_cod = :idDocumentoFruitore";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public long updateProtocollo(String protocollocod, Timestamp dataprotocollo, String tipoprotocollo,
			String idDocumentoFruitore, String tipoDocumento) throws DatabaseException {
		try {

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("protocollocod", protocollocod, Types.VARCHAR);
			params.addValue("dataprotocollo", dataprotocollo, Types.TIMESTAMP);
			params.addValue("tipoprotocollo", tipoprotocollo, Types.VARCHAR);
			params.addValue("idDocumentoFruitore", idDocumentoFruitore, Types.VARCHAR);
			params.addValue("tipoDocumento", tipoDocumento, Types.VARCHAR);

			return jdbcTemplate.update(UPDATE_DETTAGLIO_DOMANDA_PROTOCOLLO, params);
		} catch (Exception e) {
			String methodName = "UPDATE_DETTAGLIO_DOMANDA_PROTOCOLLO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Long selectIdDomandaRichiesta(String domandadetcod) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandadetcod", domandadetcod);
		Long idDomanda = null;
		try {
			idDomanda = jdbcTemplate.queryForObject(SELECT_ID_DOMANDA, namedParameters, Long.class);

			return idDomanda;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public String selectStatoDomanda(String domandadetcod) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandadetcod", domandadetcod);
		String statoDomanda = null;
		try {
			statoDomanda = jdbcTemplate.queryForObject(SELECT_STATO_DOMANDA, namedParameters, String.class);

			return statoDomanda;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_STATO_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public boolean selectMessageUUID(String messageuuid) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("messageuuid", messageuuid);
		int countdomanda = 0;
		try {
			countdomanda = jdbcTemplate.queryForObject(SELECT_MESSAGE_UUID, namedParameters, Integer.class);

			return countdomanda > 0 ? true : false;

		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "SELECT_MESSAGE_UUID";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public boolean selectIdDocumentoFruitore(String idDocumentoFruitore) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idDocumentoFruitore",
				idDocumentoFruitore);
		int countdomanda = 0;
		try {
			countdomanda = jdbcTemplate.queryForObject(SELECT_DOCUMENTO_FRUITORE, namedParameters, Integer.class);

			return countdomanda > 0 ? true : false;

		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "SELECT_DOCUMENTO_FRUITORE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
}

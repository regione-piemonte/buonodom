/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodomcallban.dto.DatiBuono;
import it.csi.buonodom.buonodomcallban.dto.DatiDichiarazione;
import it.csi.buonodom.buonodomcallban.dto.DatiDichiarazioneDocSpesa;
import it.csi.buonodom.buonodomcallban.exception.DatabaseException;
import it.csi.buonodom.buonodomcallban.integration.dao.utils.DatiBuonoMapper;
import it.csi.buonodom.buonodomcallban.integration.dao.utils.DatiDichiarazioneDocSpesaMapper;
import it.csi.buonodom.buonodomcallban.integration.dao.utils.DatiDichiarazioneMapper;
import it.csi.buonodom.buonodomcallban.integration.dao.utils.DomandaMapper;
import it.csi.buonodom.buonodomcallban.util.LoggerUtil;

@Repository
public class LogBandiDao extends LoggerUtil {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String INSERT_LOG_BANDI = "insert into bdom_l_bandi_buono "
			+ "(domanda_numero,buono_id,messageuuid_bandi,operazione,esito,errore_cod,errore_messaggio,utente_creazione,utente_modifica,dic_spesa_cod,xml) values "
			+ "(:numeroDomanda, :buonoId, :messageUuid, :operazione, :esito, :errore, "
			+ ":erroreMessaggio, 'BUONODOMCALLBACK','BUONODOMCALLBACK',:dicSpesaCod,pgp_sym_encrypt(:xml, '@keyEncrypt@')::bytea)";

	public static final String SELECT_BUONO_CREATO = "select distinct btb.buono_id,brbs.iban,brbs.iban_intestatario "
			+ "from bdom_t_buono btb, bdom_r_buono_stato brbs,bdom_d_buono_stato bdbs , "
			+ "bdom_t_domanda_dettaglio btdd, bdom_t_domanda btd "
			+ "where btb.domanda_det_id = btdd.domanda_det_id and btb.sportello_id = btdd.sportello_id "
			+ "and btb.domanda_id = btdd.domanda_id " + "and btb.data_cancellazione is null "
			+ "and btd.data_cancellazione is null " + "and btdd.domanda_id = btd.domanda_id "
			+ "and btdd.data_cancellazione is null " + "and btdd.validita_inizio <= now() "
			+ "and btdd.validita_fine is null " + "and bdbs.data_cancellazione is null "
			+ "and bdbs.validita_fine is null " + "and bdbs.validita_inizio <= now() "
			+ "and bdbs.buono_stato_id = brbs.buono_stato_id " + "and bdbs.buono_stato_cod ='CREATO' "
			+ "and brbs.buono_id = btb.buono_id " + "and brbs.validita_inizio  <= now() "
			+ "and brbs.validita_fine is null " + "and brbs.data_cancellazione is null "
			+ "and btd.domanda_numero= :numeroDomanda";

	public static final String UPDATE_BUONO_UUID = "update bdom_t_buono set messageuuid_bandi = :messageuuidBandi, "
			+ "utente_modifica = 'BUONODOMCALLBACK', data_modifica = now() " + "from bdom_t_domanda_dettaglio btdd, "
			+ "bdom_t_domanda btd " + "where " + "bdom_t_buono.buono_id  = :buonoId and  "
			+ "bdom_t_buono.domanda_det_id = btdd.domanda_det_id and bdom_t_buono.sportello_id = btdd.sportello_id "
			+ "and bdom_t_buono.domanda_id = btdd.domanda_id " + "and btd.data_cancellazione is null " + "and "
			+ "btdd.domanda_id = btd.domanda_id " + "and " + "btdd.data_cancellazione is null " + "and "
			+ "btdd.validita_inizio <= now() " + "and " + "btdd.validita_fine is null " + "and "
			+ "btd.domanda_numero= :numeroDomanda";

	public static final String UPDATE_STATO_BUONO = "update bdom_r_buono_stato set validita_fine = now(), "
			+ "utente_modifica = 'BUONODOMCALLBACK', data_modifica = now() " + "where buono_id = :buonoId "
			+ "and validita_fine is null "
			+ "and buono_stato_id = (select buono_stato_id from bdom_d_buono_stato where buono_stato_cod = 'CREATO')";

	public static final String INSERT_STATO_BUONO_ATTIVO = "insert into bdom_r_buono_stato (buono_id,buono_stato_id, "
			+ "utente_creazione,utente_modifica,iban,iban_intestatario) "
			+ "VALUES (:buonoId,(select buono_stato_id from bdom_d_buono_stato where buono_stato_cod = 'ATTIVO'), "
			+ "'BUONODOMCALLBACK','BUONODOMCALLBACK',:iban,:ibanIntestatario)";

	public static final String SELECT_DICHIARAZIONE_SPESA = "select distinct btds.dic_spesa_id,btds.dic_spesa_cod, "
			+ "bddss.dic_spesa_stato_cod,btbf.buono_id,btd.domanda_numero "
			+ "from bdom_t_buono btb,bdom_t_periodo_rendicontazione btpr, bdom_t_buono_allegato btba, bdom_d_buono_allegato_tipo bdbat, "
			+ "bdom_t_dichiarazione_spesa btds,bdom_d_dichiarazione_spesa_stato bddss,bdom_r_dichiarazione_spesa_stato brdss, "
			+ "bdom_t_documento_spesa btds2 , bdom_d_documento_spesa_tipo bddst,bdom_r_documento_spesa_stato brdss2,bdom_r_documento_spesa_dettaglio_allegato brdsa, "
			+ "bdom_t_documento_spesa_dettaglio btdsd ,bdom_d_documento_spesa_dettaglio_tipo bddsdt,bdom_t_buono_fornitore btbf,bdom_d_documento_spesa_stato bddss2, "
			+ "bdom_t_domanda btd " + "where btdsd.doc_spesa_det_tipo_id = bddsdt.doc_spesa_det_tipo_id "
			+ "and btb.domanda_id = btd.domanda_id " + "and btdsd.doc_spesa_id = btds2.doc_spesa_id "
			+ "and btds2.doc_spesa_tipo_id = bddst.doc_spesa_tipo_id " + "and btds2.dic_spesa_id = btds.dic_spesa_id "
			+ "and btds2.fornitore_id = btbf.fornitore_id "
			+ "and bddss2.doc_spesa_stato_id = brdss2.doc_spesa_stato_id "
			+ "and bdbat.allegato_tipo_id = btba.allegato_tipo_id " + "and brdss2.doc_spesa_id = btds2.doc_spesa_id "
			+ "and brdsa.doc_spesa_det_id = btdsd.doc_spesa_det_id " + "and brdsa.allegato_id = btba.allegato_id "
			+ "and btbf.buono_id = btb.buono_id " + "and btds.periodo_id = btpr.periodo_id "
			+ "and btds.buono_id = btb.buono_id " + "and bddss.dic_spesa_stato_id = brdss.dic_spesa_stato_id "
			+ "and brdss.dic_spesa_id = btds.dic_spesa_id " + "and btb.data_cancellazione is null "
			+ "and btd.data_cancellazione is null " + "and brdss.validita_fine is null "
			+ "and brdss2.validita_fine is null " + "and btpr.data_cancellazione is null "
			+ "and bddss.dic_spesa_stato_cod = :dicSpesaStatoCod " + "and btds.dic_spesa_cod = :dicSpesaCod";

	public static final String SELECT_DICHIARAZIONE_SPESA_DETTAGLIO = "select distinct btds2.doc_spesa_cod, bddss2.doc_spesa_stato_cod,btds2.doc_spesa_id "
			+ "from bdom_t_buono btb,bdom_t_periodo_rendicontazione btpr, bdom_t_buono_allegato btba, bdom_d_buono_allegato_tipo bdbat, "
			+ "bdom_t_dichiarazione_spesa btds,bdom_d_dichiarazione_spesa_stato bddss,bdom_r_dichiarazione_spesa_stato brdss, "
			+ "bdom_t_documento_spesa btds2 , bdom_d_documento_spesa_tipo bddst,bdom_r_documento_spesa_stato brdss2,bdom_r_documento_spesa_dettaglio_allegato brdsa, "
			+ "bdom_t_documento_spesa_dettaglio btdsd ,bdom_d_documento_spesa_dettaglio_tipo bddsdt,bdom_t_buono_fornitore btbf,bdom_d_documento_spesa_stato bddss2, "
			+ "bdom_t_domanda btd " + "where btdsd.doc_spesa_det_tipo_id = bddsdt.doc_spesa_det_tipo_id "
			+ "and btb.domanda_id = btd.domanda_id " + "and btdsd.doc_spesa_id = btds2.doc_spesa_id "
			+ "and btds2.doc_spesa_tipo_id = bddst.doc_spesa_tipo_id " + "and btds2.dic_spesa_id = btds.dic_spesa_id "
			+ "and btds2.fornitore_id = btbf.fornitore_id "
			+ "and bddss2.doc_spesa_stato_id = brdss2.doc_spesa_stato_id "
			+ "and bdbat.allegato_tipo_id = btba.allegato_tipo_id " + "and brdss2.doc_spesa_id = btds2.doc_spesa_id "
			+ "and brdsa.doc_spesa_det_id = btdsd.doc_spesa_det_id " + "and brdsa.allegato_id = btba.allegato_id "
			+ "and btbf.buono_id = btb.buono_id " + "and btds.periodo_id = btpr.periodo_id "
			+ "and btds.buono_id = btb.buono_id " + "and bddss.dic_spesa_stato_id = brdss.dic_spesa_stato_id "
			+ "and brdss.dic_spesa_id = btds.dic_spesa_id " + "and btb.data_cancellazione is null "
			+ "and btd.data_cancellazione is null " + "and brdss.validita_fine is null "
			+ "and brdss2.validita_fine is null " + "and btpr.data_cancellazione is null "
			+ "and bddss2.doc_spesa_stato_cod = :docSpesaStatoCod " + "and btds.dic_spesa_cod = :dicSpesaCod";

	public static final String SELECT_BUONO_DOMANDA = "select distinct btbf.buono_id,btd.domanda_numero "
			+ "from bdom_t_buono btb,bdom_t_periodo_rendicontazione btpr, bdom_t_buono_allegato btba, bdom_d_buono_allegato_tipo bdbat, "
			+ "bdom_t_dichiarazione_spesa btds,bdom_d_dichiarazione_spesa_stato bddss,bdom_r_dichiarazione_spesa_stato brdss, "
			+ "bdom_t_documento_spesa btds2 , bdom_d_documento_spesa_tipo bddst,bdom_r_documento_spesa_stato brdss2,bdom_r_documento_spesa_dettaglio_allegato brdsa, "
			+ "bdom_t_documento_spesa_dettaglio btdsd ,bdom_d_documento_spesa_dettaglio_tipo bddsdt,bdom_t_buono_fornitore btbf,bdom_d_documento_spesa_stato bddss2, "
			+ "bdom_t_domanda btd " + "where btdsd.doc_spesa_det_tipo_id = bddsdt.doc_spesa_det_tipo_id "
			+ "and btb.domanda_id = btd.domanda_id " + "and btdsd.doc_spesa_id = btds2.doc_spesa_id "
			+ "and btds2.doc_spesa_tipo_id = bddst.doc_spesa_tipo_id " + "and btds2.dic_spesa_id = btds.dic_spesa_id "
			+ "and btds2.fornitore_id = btbf.fornitore_id "
			+ "and bddss2.doc_spesa_stato_id = brdss2.doc_spesa_stato_id "
			+ "and bdbat.allegato_tipo_id = btba.allegato_tipo_id " + "and brdss2.doc_spesa_id = btds2.doc_spesa_id "
			+ "and brdsa.doc_spesa_det_id = btdsd.doc_spesa_det_id " + "and brdsa.allegato_id = btba.allegato_id "
			+ "and btbf.buono_id = btb.buono_id " + "and btds.periodo_id = btpr.periodo_id "
			+ "and btds.buono_id = btb.buono_id " + "and bddss.dic_spesa_stato_id = brdss.dic_spesa_stato_id "
			+ "and brdss.dic_spesa_id = btds.dic_spesa_id " + "and btb.data_cancellazione is null "
			+ "and btd.data_cancellazione is null " + "and brdss.validita_fine is null "
			+ "and brdss2.validita_fine is null " + "and btpr.data_cancellazione is null "
			+ "and btds.dic_spesa_cod = :dicSpesaCod";

	public static final String SELECT_ID_BUONO_ULTIMO = "select distinct btb.buono_id from bdom_t_buono btb, bdom_r_buono_stato brbs,bdom_d_buono_stato bdbs , "
			+ "bdom_t_domanda_dettaglio btdd, bdom_t_domanda btd "
			+ "where btb.domanda_det_id = btdd.domanda_det_id and btb.sportello_id = btdd.sportello_id "
			+ "and btb.domanda_id = btdd.domanda_id " + "and btb.data_cancellazione is null "
			+ "and btd.data_cancellazione is null " + "and btdd.domanda_id = btd.domanda_id "
			+ "and btdd.data_cancellazione is null " + "and btdd.validita_fine is null "
			+ "and bdbs.data_cancellazione is null " + "and bdbs.validita_fine is null "
			+ "and bdbs.buono_stato_id = brbs.buono_stato_id " + "and brbs.buono_id = btb.buono_id "
			+ "and brbs.validita_fine is null " + "and brbs.data_cancellazione is null "
			+ "and btd.domanda_numero= :numeroDomanda";

	public static final String UPDATE_DICHIARAZIONE_SPESA_UUID = "update bdom_t_dichiarazione_spesa set messageuuid_bandi = :messageuuidBandi, dic_spesa_cod_bandi = :dicSpesaCodBandi,"
			+ "utente_modifica = 'BUONODOMCALLBACK', data_modifica = now() " + "where dic_spesa_cod = :dicSpesaCod";

	public static final String UPDATE_STATO_DICHIARAZIONE_SPESA = "update bdom_r_dichiarazione_spesa_stato set validita_fine = now(), "
			+ "utente_modifica = 'BUONODOMCALLBACK', data_modifica = now() " + "where dic_spesa_id = :dicSpesaId "
			+ "and validita_fine is null "
			+ "and dic_spesa_stato_id = (select dic_spesa_stato_id from bdom_d_dichiarazione_spesa_stato where dic_spesa_stato_cod = 'CARICATA')";

	public static final String INSERT_STATO_DICHIARAZIONE_SPESA_INVIATA = "insert into bdom_r_dichiarazione_spesa_stato (dic_spesa_id,dic_spesa_stato_id, "
			+ "utente_creazione,utente_modifica) "
			+ "VALUES (:dicSpesaId,(select dic_spesa_stato_id from bdom_d_dichiarazione_spesa_stato where dic_spesa_stato_cod = 'INVIATA'), "
			+ "'BUONODOMCALLBACK','BUONODOMCALLBACK')";

	public static final String UPDATE_STATO_DOCUMENTO_SPESA = "update bdom_r_documento_spesa_stato set validita_fine = now(), "
			+ "utente_modifica = 'BUONODOMCALLBACK', data_modifica = now() " + "where doc_spesa_id = :docSpesaId "
			+ "and validita_fine is null "
			+ "and doc_spesa_stato_id = (select doc_spesa_stato_id from bdom_d_documento_spesa_stato where doc_spesa_stato_cod = 'DA_INVIARE')";

	public static final String INSERT_STATO_DOCUMENTO_SPESA_DA_VALIDARE = "INSERT INTO bdom_r_documento_spesa_stato (doc_spesa_id, doc_spesa_stato_id, importo_totale_pagato, "
			+ "importo_rendicontato,importo_quietanzato, importo_validato, importo_erogato, doc_spesa_stato_note, utente_creazione, utente_modifica) "
			+ "(SELECT doc_spesa_id, (select doc_spesa_stato_id from bdom_d_documento_spesa_stato where doc_spesa_stato_cod = 'DA_VALIDARE'), "
			+ "importo_totale_pagato, importo_rendicontato, importo_quietanzato, importo_validato, importo_erogato, doc_spesa_stato_note, "
			+ "'BUONODOMCALLBACK','BUONODOMCALLBACK' FROM bdom_r_documento_spesa_stato "
			+ "where doc_spesa_id = :docSpesaId and doc_spesa_stato_id = (select doc_spesa_stato_id from bdom_d_documento_spesa_stato where doc_spesa_stato_cod = 'DA_INVIARE') "
			+ "and validita_fine is not null)";

	public static final String UPDATE_DOCUMENTO_SPESA_BANDI = "update bdom_t_documento_spesa set doc_spesa_cod_bandi = :docSpesaCodBandi,"
			+ "utente_modifica = 'BUONODOMCALLBACK', data_modifica = now() " + "where doc_spesa_id = :docSpesaId";

	public long insertLogBandi(String numeroDomanda, BigDecimal buonoId, String messageUuid, String esito,
			String errore, String erroreMessaggio, String operazione, String dicSpesaCod, String xml)
			throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR);
		params.addValue("buonoId", buonoId, Types.BIGINT);
		params.addValue("messageUuid", messageUuid, Types.VARCHAR);
		params.addValue("esito", esito, Types.VARCHAR);
		params.addValue("errore", errore, Types.VARCHAR);
		params.addValue("erroreMessaggio", erroreMessaggio, Types.VARCHAR);
		params.addValue("operazione", operazione, Types.VARCHAR);
		params.addValue("dicSpesaCod", dicSpesaCod, Types.VARCHAR);
		params.addValue("xml", xml, Types.VARCHAR);

		jdbcTemplate.update(INSERT_LOG_BANDI, params, keyHolder, new String[] { "bandi_buono_id" });
		return keyHolder.getKey().longValue();
	}

	public DatiBuono selectBuono(String numeroDomanda) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			return jdbcTemplate.queryForObject(SELECT_BUONO_CREATO, namedParameters, new DatiBuonoMapper());

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_BUONO_CREATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long updateBuonoConUUID(String numeroDomanda, BigDecimal buonoId, String messageuuidBandi)
			throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR).addValue("buonoId", buonoId, Types.BIGINT)
				.addValue("messageuuidBandi", messageuuidBandi, Types.VARCHAR);
		return jdbcTemplate.update(UPDATE_BUONO_UUID, params);
	}

	public long chiudoStatoBuono(BigDecimal buonoId) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("buonoId", buonoId, Types.BIGINT);
		return jdbcTemplate.update(UPDATE_STATO_BUONO, params);
	}

	public long aproRecordBuonoStatoAttivo(BigDecimal buonoId, String iban, String ibanIntestatario)
			throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("buonoId", buonoId, Types.BIGINT).addValue("iban", iban, Types.VARCHAR)
				.addValue("ibanIntestatario", ibanIntestatario, Types.VARCHAR);
		;
		return jdbcTemplate.update(INSERT_STATO_BUONO_ATTIVO, params);
	}

	public DatiDichiarazione selectDatiRendicontazione(String dicSpesaCod, String dicSpesaStatoCod)
			throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("dicSpesaCod", dicSpesaCod)
				.addValue("dicSpesaStatoCod", dicSpesaStatoCod);
		try {
			return jdbcTemplate.queryForObject(SELECT_DICHIARAZIONE_SPESA, namedParameters,
					new DatiDichiarazioneMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_DICHIARAZIONE_SPESA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public DatiDichiarazione selectDatiRendicontazioneDocSpesa(String dicSpesaCod, String dicSpesaStatoCod,
			String docSpesaStatoCod) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("dicSpesaCod", dicSpesaCod)
				.addValue("docSpesaStatoCod", docSpesaStatoCod);
		DatiDichiarazione dichiarazione = new DatiDichiarazione();
		List<DatiDichiarazioneDocSpesa> dichiarazionedocSpesa = new ArrayList<DatiDichiarazioneDocSpesa>();
		try {
			dichiarazione = selectDatiRendicontazione(dicSpesaCod, dicSpesaStatoCod);
			if (dichiarazione != null) {
				dichiarazionedocSpesa = jdbcTemplate.query(SELECT_DICHIARAZIONE_SPESA_DETTAGLIO, namedParameters,
						new DatiDichiarazioneDocSpesaMapper());
				if (dichiarazionedocSpesa != null && dichiarazionedocSpesa.size() > 0) {
					dichiarazione.setDocSpesa(dichiarazionedocSpesa);
				}
			}
			return dichiarazione;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_DICHIARAZIONE_SPESA_DETTAGLIO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public DatiDichiarazione selectBuonoDomanda(String dicSpesaCod) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("dicSpesaCod", dicSpesaCod);
		try {
			return jdbcTemplate.queryForObject(SELECT_BUONO_DOMANDA, namedParameters, new DomandaMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_BUONO_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public BigDecimal selectBuonoId(String numeroDomanda) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			return jdbcTemplate.queryForObject(SELECT_ID_BUONO_ULTIMO, namedParameters, BigDecimal.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_BUONO_ULTIMO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateDichiarazioneSpesaConUUID(String dicSpesaCod, String messageuuidBandi, String dicSpesaCodBandi)
			throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("dicSpesaCod", dicSpesaCod, Types.VARCHAR)
				.addValue("messageuuidBandi", messageuuidBandi, Types.VARCHAR)
				.addValue("dicSpesaCodBandi", dicSpesaCodBandi, Types.VARCHAR);
		return jdbcTemplate.update(UPDATE_DICHIARAZIONE_SPESA_UUID, params);
	}

	public long chiudoStatoDichiarazioneCaricata(BigDecimal dicSpesaId) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("dicSpesaId", dicSpesaId, Types.BIGINT);
		return jdbcTemplate.update(UPDATE_STATO_DICHIARAZIONE_SPESA, params);
	}

	public long aproRecordDichiarazioneInviata(BigDecimal dicSpesaId) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("dicSpesaId", dicSpesaId, Types.BIGINT);
		return jdbcTemplate.update(INSERT_STATO_DICHIARAZIONE_SPESA_INVIATA, params);
	}

	public long chiudoStatoDocSpesaDaInviare(BigDecimal docSpesaId) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("docSpesaId", docSpesaId, Types.BIGINT);
		return jdbcTemplate.update(UPDATE_STATO_DOCUMENTO_SPESA, params);
	}

	public long aproRecordDocSpesaDaValidare(BigDecimal docSpesaId) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("docSpesaId", docSpesaId, Types.BIGINT);
		return jdbcTemplate.update(INSERT_STATO_DOCUMENTO_SPESA_DA_VALIDARE, params);
	}

	public long updateDocumentoSpesa(BigDecimal docSpesaId, String docSpesaCodBandi) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("docSpesaId", docSpesaId, Types.BIGINT).addValue("docSpesaCodBandi", docSpesaCodBandi,
				Types.VARCHAR);
		return jdbcTemplate.update(UPDATE_DOCUMENTO_SPESA_BANDI, params);
	}
}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.custom;

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

import it.csi.buonodom.buonodombff.dto.ModelCronologia;
import it.csi.buonodom.buonodombff.dto.custom.ModelBuono;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.integration.dao.utils.BuonoMapper;
import it.csi.buonodom.buonodombff.integration.dao.utils.CronologiaMapper;
import it.csi.buonodom.buonodombff.util.LoggerUtil;

@Repository
public class BuonoDao extends LoggerUtil {

	public static final String SELECT_CRONOLOGIA_BUONO = "select " + "btb.buono_cod as numero, "
			+ "bdbs.buono_stato_cod as codStato, " + "brbs.validita_inizio as dataCreazione, null note " + "from "
			+ "bdom_t_buono btb , " + "bdom_d_buono_stato bdbs, " + "bdom_r_buono_stato brbs "
			+ "where btb.buono_id = :buonoId " + "and bdbs.buono_stato_id = brbs.buono_stato_id "
			+ "and brbs.buono_id = btb.buono_id " + "and btb.data_cancellazione is null "
			+ "and bdbs.data_cancellazione is null "
			+ "and brbs.data_cancellazione is null order by brbs.validita_inizio";

	public static final String UPDATE_BUONO_STATO = "update bdom_r_buono_stato set validita_fine = now(), "
			+ "utente_modifica = :utente_modifica " + "where buono_id = :buonoId and validita_fine is null "
			+ "and buono_stato_id=:buonoStatoId";

	public static final String INSERT_INTO_BUONO_STATO = "insert into bdom_r_buono_stato (buono_id,buono_stato_id, "
			+ "utente_creazione,utente_modifica,iban,iban_intestatario) values (:buonoId, "
			+ "(select buono_stato_id from bdom_d_buono_stato where buono_stato_cod= :buonoStatoCod), "
			+ ":utente_creazione,:utente_modifica,:iban,:ibanIntestatario)";

	public static final String ESISTE_STATO_BUONO = "select count(*) from " + "bdom_d_buono_stato bdbs where "
			+ "bdbs.buono_stato_cod = :buonoStatoCod " + "and bdbs.data_cancellazione is null";

	public static final String GET_BUONO = "select distinct btd.richiedente_cf richiedenteCf,btb.buono_id buonoId,bdbs.buono_stato_cod stato,"
			+ "btb.buono_cod buonoCod,bdbs.buono_stato_id buonoStatoId,brbs.iban,brbs.iban_intestatario from "
			+ "bdom_t_domanda btd, " + "bdom_t_buono btb , " + "bdom_d_buono_stato bdbs, " + "bdom_r_buono_stato brbs "
			+ "where " + "btd.domanda_numero = :numeroDomanda " + "and btd.domanda_id = btb.domanda_id "
			+ "and bdbs.buono_stato_id = brbs.buono_stato_id " + "and brbs.buono_id = btb.buono_id "
			+ "and btd.data_cancellazione is null " + "and btb.data_cancellazione is null "
			+ "and bdbs.data_cancellazione is null " + "and brbs.data_cancellazione is null "
			+ "and brbs.validita_inizio  <= now() " + "and brbs.validita_fine is null";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ModelCronologia> selectCronologiaBuono(Long buonoId) throws DatabaseException {
		List<ModelCronologia> cronologia = new ArrayList<ModelCronologia>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("buonoId", buonoId);
		try {
			cronologia = jdbcTemplate.query(SELECT_CRONOLOGIA_BUONO, namedParameters, new CronologiaMapper());
			return cronologia;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_CRONOLOGIA_RICHIESTE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long updateStatoBuono(Long buonoId, String utenteModifica, Long buonoStatoId) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("buonoId", buonoId, Types.BIGINT).addValue("utente_modifica", utenteModifica, Types.VARCHAR)
				.addValue("buonoStatoId", buonoStatoId, Types.BIGINT);
		return jdbcTemplate.update(UPDATE_BUONO_STATO, params);
	}

	public long insertBuonoStato(String buonoStatoCod, String codFiscale, Long buonoId, String iban,
			String ibanIntestatario) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("buonoStatoCod", buonoStatoCod, Types.VARCHAR);
		params.addValue("utente_creazione", codFiscale, Types.VARCHAR);
		params.addValue("utente_modifica", codFiscale, Types.VARCHAR);
		params.addValue("buonoId", buonoId, Types.BIGINT);
		params.addValue("iban", iban, Types.VARCHAR);
		params.addValue("ibanIntestatario", ibanIntestatario, Types.VARCHAR);

		jdbcTemplate.update(INSERT_INTO_BUONO_STATO, params, keyHolder, new String[] { "r_buono_stato_id" });
		return keyHolder.getKey().longValue();
	}

	public boolean esisteStato(String buonoStatoCod) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("buonoStatoCod", buonoStatoCod);
		try {
			int conta = jdbcTemplate.queryForObject(ESISTE_STATO_BUONO, namedParameters, Integer.class);
			return conta > 0;
		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "ESISTE_STATO_BUONO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelBuono selectBuono(String numeroRichiesta) throws DatabaseException {
		ModelBuono buono = new ModelBuono();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			buono = jdbcTemplate.queryForObject(GET_BUONO, namedParameters, new BuonoMapper());
			return buono;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "GET_BUONO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}

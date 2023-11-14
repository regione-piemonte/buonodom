/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodombff.dto.ModelCronologia;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.integration.dao.utils.CronologiaMapper;
import it.csi.buonodom.buonodombff.util.LoggerUtil;

@Repository
public class CronologiaDao extends LoggerUtil {

	public static final String SELECT_CRONOLOGIA_RICHIESTE = "select " + "	btd.domanda_numero as numero, "
			+ "	bdds.domanda_stato_cod as codStato, " + "	btdd.data_creazione as dataCreazione, " + "	btdd.note "
			+ "from " + "	bdom_t_domanda btd, " + "	bdom_t_domanda_dettaglio btdd, "
			+ "	bdom_d_domanda_stato bdds " + "where " + "	btd.domanda_numero = :numeroDomanda " + "	and  "
			+ "	btdd.domanda_id = btd.domanda_id  " + "	and  " + "	btdd.data_cancellazione is null  " + "	and  "
			+ "	btd.data_cancellazione is null " + "	and  " + "	bdds.domanda_stato_id = btdd.domanda_stato_id";

	public static final String SELECT_CF_RICHIESTE = "select btd.richiedente_cf  " + "from bdom_t_domanda btd, "
			+ "bdom_t_domanda_dettaglio btdd  " + "where btd.data_cancellazione is null  "
			+ "and btd.domanda_numero = :numeroDomanda " + "and btd.domanda_id = btdd.domanda_id  "
			+ "and btdd.validita_inizio <= now() " + "and btdd.validita_fine is null  "
			+ "and btdd.data_cancellazione is null ";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ModelCronologia> selectCronologia(String numeroRichiesta) throws DatabaseException {
		List<ModelCronologia> cronologia = new ArrayList<ModelCronologia>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			cronologia = jdbcTemplate.query(SELECT_CRONOLOGIA_RICHIESTE, namedParameters, new CronologiaMapper());
			return cronologia;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_CRONOLOGIA_RICHIESTE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public String selectCf(String numeroRichiesta) throws DatabaseException {
		String cf = null;
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			cf = jdbcTemplate.queryForObject(SELECT_CF_RICHIESTE, namedParameters, String.class);
			return cf;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_CF_RICHIESTE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodombandisrv.dto.DatiBuono;
import it.csi.buonodom.buonodombandisrv.exception.DatabaseException;
import it.csi.buonodom.buonodombandisrv.integration.dao.utils.DatiBuonoMapper;
import it.csi.buonodom.buonodombandisrv.util.LoggerUtil;

@Repository
public class LogBandiDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String INSERT_LOG_BANDI = "insert into bdom_l_bandi_buono "
			+ "(domanda_numero,buono_id,messageuuid_bandi,operazione,xml,esito,errore_cod,errore_messaggio,utente_creazione,utente_modifica) values "
			+ "(:numeroDomanda, :buonoId, :messageUuid, :operazione, pgp_sym_encrypt(:xml, '@keyEncrypt@')::bytea, :esito, :errore, "
			+ ":erroreMessaggio, 'BUONODOMBANDISRV','BUONODOMBANDISRV')";

	public static final String SELECT_BUONO_CREATO = "select distinct btb.buono_id,brbs.iban,brbs.iban_intestatario "
			+ "from bdom_t_buono btb,bdom_r_buono_stato brbs,bdom_d_buono_stato bdbs "
			+ "where btb.domanda_id = :domandaId and btb.sportello_id = :sportelloId "
			+ "and btb.data_cancellazione is null " + "and bdbs.data_cancellazione is null "
			+ "and bdbs.validita_fine is null " + "and bdbs.validita_inizio <= now() "
			+ "and bdbs.buono_stato_id = brbs.buono_stato_id " + "and bdbs.buono_stato_cod = :stato "
			+ "and brbs.buono_id = btb.buono_id " + "and brbs.validita_inizio  <= now() "
			+ "and brbs.validita_fine is null " + "and brbs.data_cancellazione is null";

	public long insertLogBandi(String numeroDomanda, BigDecimal buonoId, String messageUuid, String xml, String esito,
			String errore, String erroreMessaggio, String operazione) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR);
		params.addValue("buonoId", buonoId, Types.BIGINT);
		params.addValue("messageUuid", messageUuid, Types.VARCHAR);
		params.addValue("xml", xml, Types.VARCHAR);
		params.addValue("esito", esito, Types.VARCHAR);
		params.addValue("errore", errore, Types.VARCHAR);
		params.addValue("erroreMessaggio", erroreMessaggio, Types.VARCHAR);
		params.addValue("operazione", operazione, Types.VARCHAR);

		jdbcTemplate.update(INSERT_LOG_BANDI, params, keyHolder, new String[] { "bandi_buono_id" });
		return keyHolder.getKey().longValue();
	}

	public DatiBuono selectBuono(BigDecimal sportelloId, BigDecimal domandaId, String stato) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sportelloId", sportelloId)
				.addValue("domandaId", domandaId).addValue("stato", stato);
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
}
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
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodombff.dto.ModelFornitore;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.integration.dao.utils.FornitoriMapper;
import it.csi.buonodom.buonodombff.util.LoggerUtil;

@Repository
public class FornitoriDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String SELECT_FORNITORI = "SELECT distinct btbf.fornitore_id AS id, btbf.fornitore_nome AS nome, btbf.fornitore_cognome AS cognome, "
			+ "       btbf.fornitore_cf AS cf, btbf.fornitore_denominazione AS denominazione, btc.fornitore_piva AS piva "
			+ "FROM bdom_t_buono btb, bdom_t_domanda btd, bdom_t_buono_fornitore btbf, bdom_t_contratto btc  "
			+ "WHERE btd.domanda_numero = :numero_domanda " + "  AND btd.domanda_id = btb.domanda_id "
			+ "  AND btb.buono_id = btbf.buono_id " + "  AND btbf.fornitore_id = btc.fornitore_id  "
			+ "  AND btbf.data_cancellazione IS NULL " + "  AND btb.data_cancellazione IS NULL "
			+ "  AND btd.data_cancellazione IS NULL " + "GROUP BY btbf.fornitore_id, btc.fornitore_piva; ";

	public List<ModelFornitore> selectFornitori(String numero_domanda) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		List<ModelFornitore> fornitori = new ArrayList<ModelFornitore>();
		try {
			params.addValue("numero_domanda", numero_domanda);
			fornitori = jdbcTemplate.query(SELECT_FORNITORI, params, new FornitoriMapper());
			return fornitori;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_FORNITORI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
}

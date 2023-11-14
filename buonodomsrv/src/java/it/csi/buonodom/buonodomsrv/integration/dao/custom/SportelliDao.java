/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.integration.dao.custom;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodomsrv.dto.ModelSportello;
import it.csi.buonodom.buonodomsrv.exception.DatabaseException;
import it.csi.buonodom.buonodomsrv.integration.dao.utils.SportelliMapper;
import it.csi.buonodom.buonodomsrv.util.LoggerUtil;

@Repository
public class SportelliDao extends LoggerUtil {

	public static final String SELECT_SPORTELLI = "select sportello_cod,sportello_anno ,sportello_desc "
			+ "from bdom_t_sportello " + "where data_cancellazione is null " + "and sportello_id=:sportelloId";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public ModelSportello selectSportelli(BigDecimal sportelloId) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sportelloId", sportelloId);
		ModelSportello sportello = new ModelSportello();
		try {
			sportello = jdbcTemplate.queryForObject(SELECT_SPORTELLI, namedParameters, new SportelliMapper());
			return sportello;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_SPORTELLI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

}

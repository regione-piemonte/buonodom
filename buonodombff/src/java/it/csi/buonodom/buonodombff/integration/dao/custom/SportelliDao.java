/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.custom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodombff.dto.ModelSportello;
import it.csi.buonodom.buonodombff.dto.custom.ModelSportelloExt;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.integration.dao.utils.SportelliExtMapper;
import it.csi.buonodom.buonodombff.integration.dao.utils.SportelliMapper;
import it.csi.buonodom.buonodombff.util.LoggerUtil;

@Repository
public class SportelliDao extends LoggerUtil {

	public static final String SELECT_SPORTELLI = "select spo.validita_inizio::date as dataInizio,spo.validita_fine::date as dataFine, "
			+ "case  "
			+ "	when now()::date BETWEEN spo.validita_inizio::date and COALESCE(spo.validita_fine::date, now()::date) then true  "
			+ "	else false " + "end inCorso " + "from bdom_t_sportello spo,bdom_d_contributo_tipo con "
			+ "where spo.contributo_tipo_id =con.contributo_tipo_id  " + "and spo.data_cancellazione is null  "
			+ "and con.data_cancellazione is null " + "and con.contributo_tipo_cod = 'DOM' "
			+ "and now()::date <= spo.validita_fine::date; ";

	public static final String SELECT_SPORTELLI_COD = "select sportello_cod,sportello_anno ,sportello_desc "
			+ "from bdom_t_sportello " + "where data_cancellazione is null " + "and sportello_id=:sportelloId";

	public static final String SPORTELLI_IS_IN_CORSO = "select " + "	case "
			+ "		when now()::date between spo.validita_inizio::date and coalesce(spo.validita_fine::date, now()::date) then true "
			+ "		else false " + "	end inCorso " + "from " + "	bdom_t_sportello spo, "
			+ "	bdom_d_contributo_tipo con, bdom_t_domanda btd " + "where "
			+ "	spo.contributo_tipo_id = con.contributo_tipo_id " + "	and spo.data_cancellazione is null "
			+ "	and con.data_cancellazione is null " + " and btd.data_cancellazione is null "
			+ "	and con.contributo_tipo_cod = 'DOM' "
			+ " and spo.sportello_id = btd.sportello_id and btd.domanda_numero  = :numeroDomanda";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ModelSportello> selectSportelli() throws DatabaseException {
		List<ModelSportello> sportello = new ArrayList<ModelSportello>();
		try {
			sportello = jdbcTemplate.query(SELECT_SPORTELLI, new SportelliMapper());
			return sportello;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_SPORTELLI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public ModelSportelloExt selectSportelliCod(BigDecimal sportelloId) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sportelloId", sportelloId);
		ModelSportelloExt sportello = new ModelSportelloExt();
		try {
			sportello = jdbcTemplate.queryForObject(SELECT_SPORTELLI_COD, namedParameters, new SportelliExtMapper());
			return sportello;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_SPORTELLI_COD";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public boolean isSportelliCorrente(String numeroDomanda) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		boolean inCorso = false;
		try {
			inCorso = jdbcTemplate.queryForObject(SPORTELLI_IS_IN_CORSO, namedParameters, Boolean.class);
			return inCorso;
		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "SPORTELLI_IS_IN_CORSO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

}

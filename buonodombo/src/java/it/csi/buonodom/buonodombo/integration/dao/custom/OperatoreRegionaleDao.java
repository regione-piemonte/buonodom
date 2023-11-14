/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodombo.business.be.service.base.BaseService;
import it.csi.buonodom.buonodombo.dto.ModelSportello;
import it.csi.buonodom.buonodombo.exception.DatabaseException;

@Repository
public class OperatoreRegionaleDao extends BaseService {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String INSERT_NEW_SPORTELLO = "insert into bdom_t_sportello "
			+ "	(sportello_cod, sportello_desc, " + "		validita_inizio, validita_fine, "
			+ "		sportello_anno, contributo_tipo_id, " + "		utente_creazione, utente_modifica) "
			+ "values(:sportelloCod, :spotelloDesc, :dataInizio::date, "
			+ "		:dataFine::date, :anno, (select bdct.contributo_tipo_id " + "from bdom_d_contributo_tipo bdct "
			+ "where bdct.data_cancellazione is null and " + "bdct.contributo_tipo_cod = 'DOM') , "
			+ "		:utenteCreazione, :utenteModifica) ";

	public long creaSportello(ModelSportello nSportello, String utente) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("sportelloCod", nSportello.getCodSportello())
				.addValue("spotelloDesc", nSportello.getDescSportello())
				.addValue("dataInizio", nSportello.getDataInizio()).addValue("dataFine", nSportello.getDataFine())
				.addValue("anno", nSportello.getAnno()).addValue("utenteCreazione", utente)
				.addValue("utenteModifica", utente);

		try {
			return jdbcTemplate.update(INSERT_NEW_SPORTELLO, namedParameters);
		} catch (Exception e) {
			String methodName = "INSERT_NEW_SPORTELLO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.custom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodombo.dto.ModelDomandeAperta;
import it.csi.buonodom.buonodombo.dto.ModelFiltriDomandeAperte;
import it.csi.buonodom.buonodombo.dto.ModelSportello;
import it.csi.buonodom.buonodombo.dto.ModelStati;
import it.csi.buonodom.buonodombo.exception.DatabaseException;
import it.csi.buonodom.buonodombo.integration.dao.utils.DomandeAperteMapper;
import it.csi.buonodom.buonodombo.integration.dao.utils.SportelloMapper;
import it.csi.buonodom.buonodombo.integration.dao.utils.StatiMapper;
import it.csi.buonodom.buonodombo.util.Constants;
import it.csi.buonodom.buonodombo.util.LoggerUtil;

@Repository
public class RicercaDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String SELECT_STATI_BY_MENU = "select bdds.domanda_stato_cod, bdds.domanda_stato_desc  "
			+ "from bdom_d_domanda_stato bdds, bdom_r_menu_fe_domanda_stato brmfds, bdom_d_menu_fe bdmf "
			+ "where bdds.domanda_stato_id = brmfds.domanda_stato_id  " + "and brmfds.menu_fe_id = bdmf.menu_fe_id  "
			+ "and bdds.data_cancellazione is null  "
			+ "and now()::date between bdds.validita_inizio::date and coalesce(bdds.validita_fine::date, now()::date) "
			+ "and brmfds.data_cancellazione is null  "
			+ "and now()::date between brmfds.validita_inizio::date and coalesce(brmfds.validita_fine::date, now()::date) "
			+ "and bdmf.data_cancellazione is null  "
			+ "and now()::date between bdmf.validita_inizio::date and coalesce(bdmf.validita_fine::date, now()::date) "
			+ "and bdmf.menu_fe_cod = :codMenu " + "order by bdds.domanda_stato_cod";

	private static final String SELECT_SPORTELLI = "SELECT bts.sportello_cod, bts.sportello_desc, "
			+ " to_char(bts.validita_inizio,'YYYY-MM-DD') as validita_inizio, "
			+ " to_char(bts.validita_fine,'YYYY-MM-DD') as validita_fine, " + " bts.sportello_anno, " + "case "
			+ "	when " + "		now()::date between bts.validita_inizio::date "
			+ "		and coalesce(bts.validita_fine::date, now()::date) " + "	then true " + "	else false "
			+ "	end as is_corrente " + "from bdom_t_sportello bts " + "where bts.data_cancellazione is null "
			+ "order by bts.validita_inizio desc";

	private static final String SELECT_DOMANDE_APERTE_PART1 = "select " + "	btd.domanda_id, "
			+ "	btd.domanda_numero, " + "	btd.beneficiario_cf, " + "	btdd.destinatario_nome, "
			+ "	btdd.destinatario_cognome, " + "	btd.richiedente_cf, " + "	btdd.richiedente_nome, "
			+ "	btdd.richiedente_cognome, " + "	bdds.domanda_stato_cod,  " + "	bdds.domanda_stato_desc, "
			+ "	to_char(btd.domanda_data,'DD/MM/YYYY') as domanda_data, "
			+ " btdd.verifica_eg_richiesta, btdd.verifica_eg_in_corso, btdd.verifica_eg_conclusa, btdd.verifica_eg_incompatibilita, "
			+ " bteg.ente_gestore_denominazione, bteg.ente_gestore_id " + "from " + "	bdom_t_domanda btd, "
			+ "	bdom_d_domanda_stato bdds, " + "	bdom_t_domanda_dettaglio btdd "
			+ " left join  bdom_d_comune bdc on bdc.comune_desc = btdd.destinatario_residenza_comune and "
			+ " now()::date between bdc.validita_inizio::date and coalesce(bdc.validita_fine::date, now()::date) "
			+ " and bdc.data_cancellazione is null "
			+ " left join bdom_r_ente_gestore_comune bregc on bdc.comune_id = bregc.comune_id and bregc.data_cancellazione is null "
			+ " and now()::date between bregc.validita_inizio::date and coalesce(bregc.validita_fine::date, now()::date) "
			+ " left join bdom_t_ente_gestore bteg on bregc.ente_gestore_id = bteg.ente_gestore_id and bteg.data_cancellazione is null "
			+ " and now()::date between bteg.validita_inizio::date and coalesce(bteg.validita_fine::date, now()::date) ";

	private static final String SELECT_DOMANDE_APERTE_PART2 = "where " + "	btd.domanda_id = btdd.domanda_id "
			+ "	and btdd.domanda_stato_id = bdds.domanda_stato_id  " + "	and btd.data_cancellazione is null "
			+ "	and btdd.data_cancellazione is null " + "	and btdd.validita_fine is null "
			+ " and now()::date between btdd.validita_inizio::date and coalesce(btdd.validita_fine::date, now()::date) "
			+ " and now()::date between bdds.validita_inizio::date and coalesce(bdds.validita_fine::date, now()::date) ";

	public List<ModelStati> selectStati(String codMenu) throws DatabaseException {
		List<ModelStati> stati = new ArrayList<ModelStati>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codMenu", codMenu);
		try {
			stati = jdbcTemplate.query(SELECT_STATI_BY_MENU, namedParameters, new StatiMapper());
			return stati;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_STATI_BY_MENU";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelSportello> selectSportelli() throws DatabaseException {
		List<ModelSportello> sportelli = new ArrayList<ModelSportello>();
		try {
			sportelli = jdbcTemplate.query(SELECT_SPORTELLI, new SportelloMapper());
			return sportelli;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_SPORTELLI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelDomandeAperta> selectDomandeAperte(ModelFiltriDomandeAperte filtri, List<ModelStati> stati)
			throws DatabaseException {
		List<ModelDomandeAperta> domande = new ArrayList<ModelDomandeAperta>();
		String query = "";
		query += SELECT_DOMANDE_APERTE_PART1;
		if (filtri.getCodSportello() != null) {
			query += ", bdom_t_sportello bts ";
		}
		query += SELECT_DOMANDE_APERTE_PART2;
		if (filtri.getCodSportello() != null) {
			query += "and bts.sportello_id = btd.sportello_id " + "and bts.sportello_id = btdd.sportello_id "
					+ "and bts.sportello_cod = :codSportello ";
		}

		long[] entiGestori = filtri.getEntiGestori();
		if (entiGestori != null) {
			query += "and btdd.destinatario_residenza_comune in ( " + "SELECT bdc.comune_desc "
					+ "FROM bdom_d_comune bdc "
					+ "inner JOIN bdom_r_ente_gestore_comune bregc ON bdc.comune_id = bregc.comune_id "
					+ "inner JOIN bdom_t_ente_gestore bteg  ON bregc.ente_gestore_id = bteg.ente_gestore_id "
					+ "WHERE bteg.ente_gestore_id  in ( ";
			for (int i = 0; i < entiGestori.length; i++) {
				if (i != (entiGestori.length - 1)) {
					query += Long.toString(entiGestori[i]) + ", ";
				} else {
					query += Long.toString(entiGestori[i]);
				}
			}
			query += " ) " + "order by bdc.comune_desc ) ";
		}
		if (filtri.getNumeroDomanda() != null) {
			query += "and btd.domanda_numero LIKE UPPER(:numeroDomanda) ";
		}
		if (filtri.getStatoDomanda() != null) {
			query += "and bdds.domanda_stato_cod = :codStato ";
		}
		if (filtri.getDestinatario() != null) {
			query += "and (UPPER(btdd.destinatario_nome) LIKE UPPER(:destinatario) "
					+ "or UPPER(btdd.destinatario_cognome) LIKE UPPER(:destinatario) "
					+ "or UPPER(btdd.destinatario_nome || ' ' || btdd.destinatario_cognome) LIKE UPPER(:destinatario) "
					+ "or UPPER(btdd.destinatario_cognome || ' ' || btdd.destinatario_nome) LIKE UPPER(:destinatario) "
					+ "or UPPER(btd.beneficiario_cf) LIKE UPPER(:destinatario)) ";
		}
		if (filtri.getRichiedente() != null) {
			query += "and (UPPER(btdd.richiedente_nome) LIKE UPPER(:richiedente) "
					+ "or UPPER(btdd.richiedente_cognome) LIKE UPPER(:richiedente) "
					+ "or UPPER(btdd.richiedente_nome || ' ' || btdd.richiedente_cognome) LIKE UPPER(:richiedente) "
					+ "or UPPER(btdd.richiedente_cognome || ' ' || btdd.richiedente_nome) LIKE UPPER(:richiedente) "
					+ "or UPPER(btd.richiedente_cf) LIKE UPPER(:richiedente)) ";
		}
		if (filtri.getStatoVerificaEnteGestore() != null) {
			switch (filtri.getStatoVerificaEnteGestore()) {
			case Constants.RICHIESTA_VERIFICA:
				query += "and btdd.verifica_eg_richiesta IS NOT NULL ";
				break;
			case Constants.VERIFICA_IN_CORSO:
				query += "and btdd.verifica_eg_in_corso IS NOT NULL ";
				break;
			case Constants.VERIFICA_EFFETTUATA:
				query += "and btdd.verifica_eg_conclusa IS NOT NULL ";
				break;
			case Constants.IN_VERIFICA:
				query += "and ( btdd.verifica_eg_richiesta IS NOT NULL ";
				query += "or btdd.verifica_eg_in_corso IS NOT NULL ";
				query += "or btdd.verifica_eg_conclusa IS NOT NULL) ";
				break;
			case Constants.VERIFICA_NON_RICHIESTA:
				query += "and btdd.verifica_eg_richiesta IS NULL ";
				query += "and btdd.verifica_eg_in_corso IS NULL ";
				query += "and btdd.verifica_eg_conclusa IS NULL ";
				break;
			default:
				break;
			}
		}
		query += "and bdds.domanda_stato_cod in (";
		for (int i = 0; i < stati.size(); i++) {
			if (i == (stati.size() - 1)) {
				query += "'" + stati.get(i).getCodStato() + "'";
			} else {
				query += "'" + stati.get(i).getCodStato() + "', ";
			}
		}
		query += ") ";
		query += "ORDER BY btd.domanda_data DESC";
		MapSqlParameterSource params = new MapSqlParameterSource();
		if (filtri.getCodSportello() != null) {
			params.addValue("codSportello", filtri.getCodSportello());
		}
		if (filtri.getNumeroDomanda() != null) {
			params.addValue("numeroDomanda", "%" + filtri.getNumeroDomanda() + "%");
		}
		if (filtri.getStatoDomanda() != null) {
			params.addValue("codStato", filtri.getStatoDomanda());
		}
		if (filtri.getDestinatario() != null) {
			params.addValue("destinatario", "%" + filtri.getDestinatario() + "%");
		}
		if (filtri.getRichiedente() != null) {
			params.addValue("richiedente", "%" + filtri.getRichiedente() + "%");
		}

		try {
			domande = jdbcTemplate.query(query, params, new DomandeAperteMapper());
			return domande;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_DOMANDE_APERTE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

}

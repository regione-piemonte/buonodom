/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodombo.dto.ModelEnteGestore;
import it.csi.buonodom.buonodombo.exception.DatabaseException;
import it.csi.buonodom.buonodombo.integration.dao.utils.EnteGestoreMapper;
import it.csi.buonodom.buonodombo.util.LoggerUtil;

@Repository
public class EntiGestoriDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static String SELECT_ENTI_GESTORI = "select " + "bteg.ente_gestore_denominazione, bteg.ente_gestore_id "
			+ "from bdom_t_ente_gestore bteg " + "order by bteg.ente_gestore_denominazione ";

	public static final String UPDATE_NOTA_ENTE_DETTAGLIO_DOMANDA = "UPDATE bdom_t_domanda_dettaglio "
			+ "SET data_modifica = now(), utente_modifica = :utenteModifica, note_regione = :notaregione "
			+ "WHERE domanda_det_id=:idDomanda";

	public static final String UPDATE_RETTIFICA_ENTE_DETTAGLIO_DOMANDA = "UPDATE bdom_t_domanda_dettaglio "
			+ "SET data_modifica = now(), utente_modifica = :utenteModifica, verifica_eg_richiesta = :verificaegrichiesta, "
			+ "verifica_eg_in_corso = :verificaegincorso, verifica_eg_conclusa = :verificaegconclusa "
			+ "WHERE domanda_det_id=:idDomanda";

	public static final String SALVA_VERIFICA_ENTE_DETTAGLIO_DOMANDA = "UPDATE bdom_t_domanda_dettaglio "
			+ "SET data_modifica = now(), utente_modifica = :utenteModifica, verifica_eg_punteggio_sociale = :verificaegpunteggiosociale, "
			+ "verifica_eg_incompatibilita = :verificaegincompatibilita, note_ente_gestore =:noteentegestore, verifica_eg_data = now() "
			+ "WHERE domanda_det_id=:idDomanda";

	public static final String UPDATE_TO_VERIFICA_IN_CORSO = "UPDATE bdom_t_domanda_dettaglio "
			+ "SET verifica_eg_richiesta = null, " + "verifica_eg_in_corso = true, " + "verifica_eg_conclusa = null, "
			+ "data_modifica = now(), " + "utente_modifica = :codFiscale " + "WHERE domanda_det_id in ( "
			+ "	select btdd.domanda_det_id " + "	from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd "
			+ "	where btd.data_cancellazione is null " + "	and btdd.data_cancellazione is null "
			+ "	and btdd.validita_fine is null " + "	and btdd.verifica_eg_richiesta = true "
			+ "	and btdd.verifica_eg_in_corso is null " + "	and btdd.verifica_eg_conclusa is null "
			+ "	and now()::date between btdd.validita_inizio::date and coalesce(btdd.validita_fine::date, now()::date) "
			+ "	and btdd.domanda_id = btd.domanda_id " + "	and btd.domanda_numero in (";

	public List<ModelEnteGestore> selectEntiGestori() throws DatabaseException {
		List<ModelEnteGestore> entiGestori = new ArrayList<ModelEnteGestore>();
		try {
			entiGestori = jdbcTemplate.query(SELECT_ENTI_GESTORI, new EnteGestoreMapper());
			return entiGestori;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ENTI_GESTORI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateRettificaDettaglio(BigDecimal idDettaglio, String utenteModifica, Boolean verificaegrichiesta,
			Boolean verificaegincorso, Boolean verificaegconclusa) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idDomanda", idDettaglio, Types.BIGINT)
					.addValue("utenteModifica", utenteModifica, Types.VARCHAR)
					.addValue("verificaegrichiesta", verificaegrichiesta, Types.BOOLEAN)
					.addValue("verificaegincorso", verificaegincorso, Types.BOOLEAN)
					.addValue("verificaegconclusa", verificaegconclusa, Types.BOOLEAN);

			return jdbcTemplate.update(UPDATE_RETTIFICA_ENTE_DETTAGLIO_DOMANDA, params);
		} catch (Exception e) {
			String methodName = "UPDATE_RETTIFICA_ENTE_DETTAGLIO_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long salvaVerificaEnteDettaglio(BigDecimal idDettaglio, String utenteModifica,
			String verificaegpunteggiosociale, Boolean verificaegincompatibilita, String noteentegestore)
			throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idDomanda", idDettaglio, Types.BIGINT)
					.addValue("utenteModifica", utenteModifica, Types.VARCHAR)
					.addValue("verificaegincompatibilita", verificaegincompatibilita, Types.BOOLEAN)
					.addValue("verificaegpunteggiosociale", verificaegpunteggiosociale, Types.VARCHAR)
					.addValue("noteentegestore", noteentegestore, Types.VARCHAR);

			return jdbcTemplate.update(SALVA_VERIFICA_ENTE_DETTAGLIO_DOMANDA, params);
		} catch (Exception e) {
			String methodName = "SALVA_VERIFICA_ENTE_DETTAGLIO_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateNoteEnteDettaglio(BigDecimal idDettaglio, String utenteModifica, String notaregione)
			throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idDomanda", idDettaglio, Types.BIGINT).addValue("notaregione", notaregione, Types.VARCHAR)
					.addValue("utenteModifica", utenteModifica, Types.VARCHAR);
			return jdbcTemplate.update(UPDATE_NOTA_ENTE_DETTAGLIO_DOMANDA, params);
		} catch (Exception e) {
			String methodName = "UPDATE_NOTA_ENTE_DETTAGLIO_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateToVerificaInCorso(String numeroDomande[], String codFiscale) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("codFiscale", codFiscale, Types.VARCHAR);
			String query = "";
			query += UPDATE_TO_VERIFICA_IN_CORSO;
			for (int i = 0; i < numeroDomande.length; i++) {
				if (i != (numeroDomande.length - 1)) {
					query += "'" + numeroDomande[i] + "', ";
				} else {
					query += "'" + numeroDomande[i] + "'";
				}
			}
			query += " )) ";
			return jdbcTemplate.update(query, params);
		} catch (Exception e) {
			String methodName = "UPDATE_NOTE_DETTAGLIO_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}

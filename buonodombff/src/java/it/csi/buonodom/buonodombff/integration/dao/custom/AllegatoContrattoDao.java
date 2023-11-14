/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.custom;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodombff.dto.custom.ModelGetAllegato;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.integration.dao.utils.AllegatoGetMapper;
import it.csi.buonodom.buonodombff.util.LoggerUtil;

@Repository
public class AllegatoContrattoDao extends LoggerUtil {

	public static final String SELECT_ALLEGATO_CONTRATTO = "select btba.file_name as filePath, btba.file_path as fileName "
			+ "from bdom_t_contratto btc, bdom_r_contratto_allegato brca, "
			+ "	bdom_t_buono_allegato btba, bdom_d_buono_allegato_tipo bdbat "
			+ "where btba.allegato_id = brca.contratto_id " + "and brca.contratto_id = btc.contratto_id "
			+ "and btc.contratto_id = :contrattoId " + "and btba.allegato_tipo_id = bdbat.allegato_tipo_id "
			+ "and bdbat.allegato_tipo_cod = :tipoAllegato " + "and btba.data_cancellazione is null "
			+ "and brca.data_cancellazione is null " + "and btba.data_cancellazione is null; ";

	public static final String SELECT_CF_RICHIEDENTE_FROM_CONTRATTO = "select btd.richiedente_cf  "
			+ "from bdom_t_domanda btd, bdom_t_contratto btc, bdom_t_buono btb "
			+ "where btc.contratto_id = :contrattoId " + "and btb.buono_id = btc.buono_id "
			+ "and btb.domanda_id = btd.domanda_id " + "and btd.data_cancellazione is null "
			+ "and btb.data_cancellazione is null " + "and btc.data_cancellazione is null;";

	public static final String UPDATE_CONTRATTO_ALLEGATO = "update " + "	bdom_t_buono_allegato " + "set "
			+ "	data_modifica = now(), " + "	utente_modifica =:cfmodifica, " + "	file_name =:nomefile, "
			+ "	file_type =:tipofile, " + "	file_path =:pathfile " + "from "
			+ "	bdom_t_buono_allegato btba, bdom_r_contratto_allegato brca, " + "	bdom_t_contratto btc "
			+ "where btba.allegato_id = brca.allegato_id " + "and btba.allegato_tipo_id = ( "
			+ "	select bdbat.allegato_tipo_id " + "	from bdom_d_buono_allegato_tipo bdbat "
			+ "	where bdbat.allegato_tipo_cod = :allegatoTipo " + ") " + "and brca.contratto_id = btc.contratto_id "
			+ "and btc.contratto_id = :contrattoId " + "and btba.data_cancellazione is null "
			+ "and brca.data_cancellazione is null " + "and btc.data_cancellazione is null;";

	public static final String INSERT_CONTRATTO_ALLEGATO = "insert " + "	into " + "	bdom_t_buono_allegato   "
			+ "(file_name, " + "	file_type, " + "	file_path, " + "	allegato_tipo_id, " + "	utente_creazione, "
			+ "	utente_modifica) " + "values( " + ":file_name, " + ":file_type, " + ":file_path, " + "( "
			+ "	select bdbat.allegato_tipo_id " + "	from bdom_d_buono_allegato_tipo bdbat "
			+ "	where bdbat.allegato_tipo_cod = :allegatoTipo " + "), " + ":utente_creazione, " + ":utente_modifica);";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public ModelGetAllegato selectAllegatoContratto(String tipoAllegato, Integer idContratto) throws DatabaseException {

		ModelGetAllegato allegato = new ModelGetAllegato();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contrattoId", idContratto)
				.addValue("tipoAllegato", tipoAllegato);
		try {
			allegato = jdbcTemplate.queryForObject(SELECT_ALLEGATO_CONTRATTO, namedParameters, new AllegatoGetMapper());

			return allegato;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ALLEGATO_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public String selectCfRichiedenteFromContratto(Integer idContratto) throws DatabaseException {
		String cf = "";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contrattoId", idContratto);
		try {
			cf = jdbcTemplate.queryForObject(SELECT_CF_RICHIEDENTE_FROM_CONTRATTO, namedParameters,
					(rs, rowNum) -> rs.getString("richiedente_cf"));

			return cf;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_CF_RICHIEDENTE_FROM_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long updateAllegato(Integer idContratto, String cfmodifica, String nomefile, String tipofile,
			String pathfile, String tipoAllegato) {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("cfmodifica", cfmodifica, Types.VARCHAR).addValue("contrattoId", idContratto, Types.INTEGER)
				.addValue("nomefile", nomefile, Types.VARCHAR).addValue("tipofile", tipofile, Types.VARCHAR)
				.addValue("allegatoTipo", tipoAllegato, Types.VARCHAR).addValue("pathfile", pathfile, Types.VARCHAR)
				.addValue("cod", tipoAllegato);
		return jdbcTemplate.update(UPDATE_CONTRATTO_ALLEGATO, params);
	}

	public long insertAllegato(String nomefile, String tipofile, String pathfile, String tipoAllegato,
			String cfinserisci, String cfmodifica) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("file_name", nomefile, Types.VARCHAR);
		params.addValue("file_type", tipofile, Types.VARCHAR);
		params.addValue("file_path", pathfile, Types.VARCHAR);
		params.addValue("allegatoTipo", tipoAllegato, Types.VARCHAR);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		jdbcTemplate.update(INSERT_CONTRATTO_ALLEGATO, params, keyHolder, new String[] { "allegato_id" });
		return keyHolder.getKey().longValue();
	}

}

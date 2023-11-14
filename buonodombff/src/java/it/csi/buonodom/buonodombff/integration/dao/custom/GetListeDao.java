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

import it.csi.buonodom.buonodombff.dto.ModelDecodifica;
import it.csi.buonodom.buonodombff.dto.ModelDecodificaRapporto;
import it.csi.buonodom.buonodombff.dto.ModelDecodificaRapportoAllegato;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.integration.dao.utils.ListaDecodificaMapper;
import it.csi.buonodom.buonodombff.integration.dao.utils.ListaModelAllegatoRapportoMapper;
import it.csi.buonodom.buonodombff.util.LoggerUtil;

@Repository
public class GetListeDao extends LoggerUtil {

	private static final String SELECT_TIPI_ALLEGATI = "select allegato_tipo_cod as codice ,allegato_tipo_desc as descrizione "
			+ "from bdom_d_allegato_tipo "
			+ "where now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date) and data_cancellazione is null";

	private static final String SELECT_TITOLI_STUDIO = "select titolo_studio_cod as codice ,titolo_studio_desc as descrizione "
			+ "from bdom_d_titolo_studio "
			+ "where now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date) and data_cancellazione is null";

	private static final String SELECT_TIPI_CONTRATTI = "select contratto_tipo_cod as codice ,contratto_tipo_desc as descrizione "
			+ "from bdom_d_contratto_tipo "
			+ "where now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date) and data_cancellazione is null";

//	private static final String SELECT_TIPI_RAPPORTO = "select a.rapporto_tipo_cod as codice ,a.rapporto_tipo_desc as descrizione, false as richiesta_delega "
//			+ "from bdom_d_rapporto_tipo a "
//			+ "where "
//			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null and " 
//			+ "a.rapporto_tipo_id not in ( "
//			+ "select a.rapporto_tipo_id "
//			+ "from bdom_d_rapporto_tipo a,bdom_d_allegato_tipo b, bdom_r_rapporto_allegato_tipo c "
//			+ "where a.rapporto_tipo_id = c.rapporto_tipo_id "
//			+ "and c.allegato_obbligatorio = true "
//			+ "and b.allegato_tipo_cod ='DELEGA' "
//			+ "and b.allegato_tipo_id = c.allegato_tipo_id and "
//			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null and "
//			+ "now()::date BETWEEN b.validita_inizio::date and COALESCE(b.validita_fine::date, now()::date) and b.data_cancellazione is null and "
//			+ "now()::date BETWEEN c.validita_inizio::date and COALESCE(c.validita_fine::date, now()::date) and c.data_cancellazione is null "
//			+ ") "
//			+ "union "
//			+ "select a.rapporto_tipo_cod as codice ,a.rapporto_tipo_desc as descrizione, c.allegato_obbligatorio as richiesta_delega "
//			+ "from bdom_d_rapporto_tipo a,bdom_d_allegato_tipo b, bdom_r_rapporto_allegato_tipo c "
//			+ "where a.rapporto_tipo_id = c.rapporto_tipo_id "
//			+ "and c.allegato_obbligatorio = true "
//			+ "and b.allegato_tipo_cod ='DELEGA' "
//			+ "and b.allegato_tipo_id = c.allegato_tipo_id and "
//			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null and "
//			+ "now()::date BETWEEN b.validita_inizio::date and COALESCE(b.validita_fine::date, now()::date) and b.data_cancellazione is null and "
//			+ "now()::date BETWEEN c.validita_inizio::date and COALESCE(c.validita_fine::date, now()::date) and c.data_cancellazione is null";

	private static final String SELECT_TIPI_RAPPORTO = "select a.rapporto_tipo_cod as codice ,a.rapporto_tipo_desc as descrizione "
			+ "from bdom_d_rapporto_tipo a " + "where "
			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null "
			+ "order by a.rapporto_tipo_cod";

	private static final String SELECT_TIPI_RAPPORTO_ALLEGATI = "select c.allegato_obbligatorio as allegato_obbligatorio,b.allegato_tipo_cod "
			+ "from bdom_d_rapporto_tipo a,bdom_d_allegato_tipo b, bdom_r_rapporto_allegato_tipo c "
			+ "where a.rapporto_tipo_id = c.rapporto_tipo_id " + "and b.allegato_tipo_id = c.allegato_tipo_id and "
			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null and "
			+ "now()::date BETWEEN b.validita_inizio::date and COALESCE(b.validita_fine::date, now()::date) and b.data_cancellazione is null and "
			+ "now()::date BETWEEN c.validita_inizio::date and COALESCE(c.validita_fine::date, now()::date) and c.data_cancellazione is null and "
			+ "a.rapporto_tipo_cod = :rapportotipocod " + "order by a.rapporto_tipo_cod";

	private static final String SELECT_STATI_DOMANDA = "select domanda_stato_cod as codice ,domanda_stato_desc as descrizione "
			+ "from bdom_d_domanda_stato "
			+ "where now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date) and data_cancellazione is null";

	private static final String SELECT_ASL = "select asl_cod as codice ,asl_azienda_desc as descrizione "
			+ "from bdom_d_asl where asl_regione_cod ='010' and "
			+ "now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date) and data_cancellazione is null";

	private static final String SELECT_VALUTAZIONI_MULTIDIMENSIONALE = "select valutazione_multidimensionale_cod  as codice ,valutazione_multidimensionale_desc  as descrizione "
			+ "from bdom_d_valutazione_multidimensionale " + "where " + "now()::date BETWEEN validita_inizio::date "
			+ "and COALESCE(validita_fine::date, now()::date) " + "and data_cancellazione is null";

	private static final String SELECT_TIPI_ASSISTENTE = "select a.assistente_tipo_cod as codice ,a.assistente_tipo_desc as descrizione "
			+ "from bdom_d_assistente_tipo a " + "where "
			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null "
			+ "order by a.assistente_tipo_cod";

	private static final String SELECT_TIPI_DOCUMENTO_SPESA = "Select a.doc_spesa_tipo_cod as codice, a.doc_spesa_tipo_desc as descrizione "
			+ "from bdom_d_documento_spesa_tipo a " + "where "
			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null "
			+ "order by a.doc_spesa_tipo_cod";

	private static final String SELECT_TIPI_DOCUMENTO_SPESA_DETTAGLIO = "Select a.doc_spesa_det_tipo_cod as codice, a.doc_spesa_det_tipo_desc as descrizione "
			+ "from bdom_d_documento_spesa_dettaglio_tipo a " + "where "
			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null "
			+ "order by a.doc_spesa_det_tipo_cod ";

	private static final String SELECT_TIPI_ALLEGATO_BUONO = "Select a.allegato_tipo_cod as codice, a.allegato_tipo_desc as descrizione "
			+ "from bdom_d_buono_allegato_tipo a " + "where "
			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null "
			+ "order by a.allegato_tipo_cod ";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ModelDecodifica> selectAsl() throws DatabaseException {
		List<ModelDecodifica> lista = new ArrayList<ModelDecodifica>();

		try {
			lista = jdbcTemplate.query(SELECT_ASL, new ListaDecodificaMapper());
			return lista;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ASL";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public List<ModelDecodifica> selectTipiAllegati() throws DatabaseException {
		List<ModelDecodifica> lista = new ArrayList<ModelDecodifica>();

		try {
			lista = jdbcTemplate.query(SELECT_TIPI_ALLEGATI, new ListaDecodificaMapper());
			return lista;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_TIPI_ALLEGATI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public List<ModelDecodifica> selectTitoliStudio() throws DatabaseException {
		List<ModelDecodifica> lista = new ArrayList<ModelDecodifica>();

		try {
			lista = jdbcTemplate.query(SELECT_TITOLI_STUDIO, new ListaDecodificaMapper());
			return lista;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_TITOLI_STUDIO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public List<ModelDecodifica> selectTipiContratti() throws DatabaseException {
		List<ModelDecodifica> lista = new ArrayList<ModelDecodifica>();

		try {
			lista = jdbcTemplate.query(SELECT_TIPI_CONTRATTI, new ListaDecodificaMapper());
			return lista;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_TIPI_CONTRATTI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public List<ModelDecodificaRapporto> selectTipiRapporto() throws DatabaseException {
		List<ModelDecodificaRapporto> lista = new ArrayList<ModelDecodificaRapporto>();
		List<ModelDecodifica> listadec = new ArrayList<ModelDecodifica>();
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		List<ModelDecodificaRapportoAllegato> modelAllegato = new ArrayList<ModelDecodificaRapportoAllegato>();
		try {
			listadec = jdbcTemplate.query(SELECT_TIPI_RAPPORTO, new ListaDecodificaMapper());
			for (ModelDecodifica dec : listadec) {
				namedParameters.addValue("rapportotipocod", dec.getCodice());
				modelAllegato = jdbcTemplate.query(SELECT_TIPI_RAPPORTO_ALLEGATI, namedParameters,
						new ListaModelAllegatoRapportoMapper());
				ModelDecodificaRapporto listamod = new ModelDecodificaRapporto();
				listamod.setCodice(dec.getCodice());
				listamod.setEtichetta(dec.getEtichetta());
				listamod.setAllegato(modelAllegato);
				lista.add(listamod);
			}
			return lista;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_TIPI_RAPPORTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public List<ModelDecodifica> selectStatiDomanda() throws DatabaseException {
		List<ModelDecodifica> lista = new ArrayList<ModelDecodifica>();

		try {
			lista = jdbcTemplate.query(SELECT_STATI_DOMANDA, new ListaDecodificaMapper());
			return lista;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_STATI_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public List<ModelDecodifica> selectTipiAssistente() throws DatabaseException {
		List<ModelDecodifica> lista = new ArrayList<ModelDecodifica>();

		try {
			lista = jdbcTemplate.query(SELECT_TIPI_ASSISTENTE, new ListaDecodificaMapper());
			return lista;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_TIPI_ASSISTENTE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public List<ModelDecodifica> selectValutazioniMultidimensionali() throws DatabaseException {
		List<ModelDecodifica> lista = new ArrayList<ModelDecodifica>();

		try {
			lista = jdbcTemplate.query(SELECT_VALUTAZIONI_MULTIDIMENSIONALE, new ListaDecodificaMapper());
			return lista;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_VALUTAZIONI_MULTIDIMENSIONALE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public List<ModelDecodifica> selectTipiDocumentoSpesa() throws DatabaseException {
		List<ModelDecodifica> lista = new ArrayList<ModelDecodifica>();

		try {
			lista = jdbcTemplate.query(SELECT_TIPI_DOCUMENTO_SPESA, new ListaDecodificaMapper());
			return lista;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_TIPI_DOCUMENTO_SPESA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public List<ModelDecodifica> selectTipiDocumentoSpesaDettaglio() throws DatabaseException {
		List<ModelDecodifica> lista = new ArrayList<ModelDecodifica>();

		try {
			lista = jdbcTemplate.query(SELECT_TIPI_DOCUMENTO_SPESA_DETTAGLIO, new ListaDecodificaMapper());
			return lista;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_TIPI_DOCUMENTO_SPESA_DETTAGLIO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public List<ModelDecodifica> selectTipiAllegatoBuono() throws DatabaseException {
		List<ModelDecodifica> lista = new ArrayList<ModelDecodifica>();

		try {
			lista = jdbcTemplate.query(SELECT_TIPI_ALLEGATO_BUONO, new ListaDecodificaMapper());
			return lista;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_TIPI_ALLEGATO_BUONO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}
}

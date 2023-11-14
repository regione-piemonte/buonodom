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

import it.csi.buonodom.buonodombo.dto.ModelAzione;
import it.csi.buonodom.buonodombo.dto.ModelEnteGestore;
import it.csi.buonodom.buonodombo.dto.ModelProfiliBuonodom;
import it.csi.buonodom.buonodombo.dto.ModelUserRuolo;
import it.csi.buonodom.buonodombo.exception.DatabaseException;
import it.csi.buonodom.buonodombo.integration.dao.utils.AzioniMapper;
import it.csi.buonodom.buonodombo.integration.dao.utils.EnteGestoreMapper;
import it.csi.buonodom.buonodombo.integration.dao.utils.ProfiliMapper;
import it.csi.buonodom.buonodombo.integration.dao.utils.UserRuoloMapper;
import it.csi.buonodom.buonodombo.util.LoggerUtil;

@Repository
public class LoginDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String SELECT_RUOLI_BY_CF = "select bdr.ruolo_cod, bdr.ruolo_desc,bts.soggetto_cf, bts.soggetto_nome, bts.soggetto_cognome "
			+ "from bdom_d_ruolo bdr, bdom_r_soggetto_ruolo brsr, bdom_t_soggetto bts  "
			+ "where bdr.ruolo_id = brsr.ruolo_id  " + "and brsr.soggetto_id = bts.soggetto_id  "
			+ "and bts.soggetto_cf = :codFiscale " + "and bts.data_cancellazione is null  "
			+ "and brsr.data_cancellazione is null  "
			+ "and now()::date between brsr.validita_inizio::date and coalesce(brsr.validita_fine::date, now()::date) "
			+ "and bdr.data_cancellazione is null  "
			+ "and now()::date between bdr.validita_inizio::date and coalesce(bdr.validita_fine::date, now()::date) "
			+ "order by bdr.ruolo_cod";

	private static final String SELECT_ENTI_GESTORE_BY_CF = "select bteg.ente_gestore_denominazione, bteg.ente_gestore_id "
			+ "from bdom_t_soggetto bts ,bdom_r_soggetto_ente_gestore brseg , bdom_t_ente_gestore bteg "
			+ "where bts.data_cancellazione is null " + "and brseg.data_cancellazione is null "
			+ "and now()::date between brseg.validita_inizio::date and coalesce(brseg.validita_fine::date, now()::date) "
			+ "and bteg.data_cancellazione is null "
			+ "and now()::date between bteg.validita_inizio::date and coalesce(bteg.validita_fine::date, now()::date) "
			+ "and bts.soggetto_id = brseg.soggetto_id " + "and brseg.ente_gestore_id = bteg.ente_gestore_id "
			+ "and bts.soggetto_cf  = :codFiscale " + "order by bteg.ente_gestore_denominazione ";

	private static final String SELECT_PROFILI_BY_COD_RUOLO = "select " + "	bdp.profilo_cod, " + "	bdp.profilo_desc "
			+ "from " + "	bdom_d_profilo bdp, " + "	bdom_d_ruolo bdr, " + "	bdom_r_ruolo_profilo brrp " + "where "
			+ "	bdr.ruolo_cod = :codRuolo " + "	and  " + "bdr.ruolo_id = brrp.ruolo_id " + "	and  "
			+ "brrp.profilo_id = bdp.profilo_id " + "	and  " + "bdr.data_cancellazione is null " + "	and "
			+ "bdp.data_cancellazione is null " + "	and  "
			+ "now()::date between bdp.validita_inizio::date and coalesce(bdp.validita_fine::date, now()::date) "
			+ "	and  " + "brrp.data_cancellazione is null " + "	and  "
			+ "now()::date between brrp.validita_inizio::date and coalesce(brrp.validita_fine::date, now()::date) "
			+ "order by " + "	bdp.profilo_cod";

	private static final String SELECT_AZIONI_BY_COD_PROFILO = "select " + "	bta.azione_cod, "
			+ "	bta.azione_desc " + "from " + "	bdom_t_azione bta, " + "	bdom_d_profilo bdp, "
			+ "	bdom_r_profilo_azione brpa " + "where " + "	bdp.profilo_cod = :codProfilo "
			+ "	and bta.azione_id = brpa.azione_id " + "	and bdp.profilo_id = brpa.profilo_id "
			+ " and bdp.data_cancellazione is null " + " and bta.data_cancellazione is null " + "	and  "
			+ " now()::date between bdp.validita_inizio::date and coalesce(bdp.validita_fine::date, now()::date) "
			+ " and brpa.data_cancellazione is null " + "	and  "
			+ " now()::date between brpa.validita_inizio::date and coalesce(brpa.validita_fine::date, now()::date) "
			+ " order by " + "	bta.azione_cod";

	public List<ModelUserRuolo> selectRuoli(String codFiscale) throws DatabaseException {
		List<ModelUserRuolo> ruoli = new ArrayList<ModelUserRuolo>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codFiscale", codFiscale);
		try {
			ruoli = jdbcTemplate.query(SELECT_RUOLI_BY_CF, namedParameters, new UserRuoloMapper());
			return ruoli;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_RUOLI_BY_CF";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelEnteGestore> selectEntiGestore(String codFiscale) throws DatabaseException {
		List<ModelEnteGestore> enti = new ArrayList<ModelEnteGestore>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codFiscale", codFiscale);
		try {
			enti = jdbcTemplate.query(SELECT_ENTI_GESTORE_BY_CF, namedParameters, new EnteGestoreMapper() {
			});
			return enti;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ENTI_GESTORE_BY_CF";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelProfiliBuonodom> selectProfili(String codRuolo) throws DatabaseException {
		List<ModelProfiliBuonodom> profili = new ArrayList<ModelProfiliBuonodom>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codRuolo", codRuolo);
		try {
			profili = jdbcTemplate.query(SELECT_PROFILI_BY_COD_RUOLO, namedParameters, new ProfiliMapper());
			return profili;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_PROFILI_BY_COD_RUOLO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelAzione> selectAzioni(String codProfilo) throws DatabaseException {
		List<ModelAzione> azioni = new ArrayList<ModelAzione>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codProfilo", codProfilo);
		try {
			azioni = jdbcTemplate.query(SELECT_AZIONI_BY_COD_PROFILO, namedParameters, new AzioniMapper());
			return azioni;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_AZIONI_BY_COD_PROFILO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
//	
//	public ModelRichiesta selectNumeroRichiesta(String numeroRichiesta) throws DatabaseException {
//		ModelRichiesta richieste = new ModelRichiesta();
//		
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
//		try {
//			richieste = jdbcTemplate.queryForObject(SELECT_NUMERO_RICHIESTA, namedParameters, new DettaglioRichiestaMapper());
//			return richieste;
//		} catch (EmptyResultDataAccessException e) {
//			return null; 
//		} catch (Exception e) {
//			String methodName = "SELECT_NUMERO_RICHIESTA";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//	
//	public List<ModelAllegato> selectAllegatiFromNumeroRichiesta(String numeroRichiesta) throws DatabaseException{
//		
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroRichiesta", numeroRichiesta);
//		Long idDomanda = null;
//		List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
//		try {
//			idDomanda = jdbcTemplate.queryForObject(SELECT_ID_DOMANDA, namedParameters, Long.class);
//			SqlParameterSource namedParametersAllegati = new MapSqlParameterSource().addValue("idDomanda", idDomanda);
//			allegati= jdbcTemplate.query(SELECT_ALLEGATI, namedParametersAllegati, new AllegatoTipoMapper());
//			
//			return allegati;
//			
//		} catch (EmptyResultDataAccessException e) {
//			return null; 
//		} catch (Exception e) {
//			String methodName = "SELECT_ALLEGATI";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//		
//	}
//	
//	public List<String> selectRettificareFromNumeroRichiesta(String numeroRichiesta) throws DatabaseException{
//		
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroRichiesta", numeroRichiesta);
//		Long idDomanda = null;
//		List<String> campiModificare = new ArrayList<String>();
//		try {
//			idDomanda = jdbcTemplate.queryForObject(SELECT_ID_DOMANDA, namedParameters, Long.class);
//			SqlParameterSource namedParametersCampi = new MapSqlParameterSource().addValue("idDomanda", idDomanda);
//			campiModificare= jdbcTemplate.queryForList(SELECT_NOME_CAMPO_DA_MODIFICARE, namedParametersCampi, String.class);
//			
//			return campiModificare;
//			
//		} catch (EmptyResultDataAccessException e) {
//			return null; 
//		} catch (Exception e) {
//			String methodName = "SELECT_NOME_CAMPO_DA_MODIFICARE";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//		
//	}
//
//	public Long selectIdStato(String codStato) throws DatabaseException {
//		Long idStato = null;
//		if (StringUtils.isEmpty(codStato)) {
//			return idStato;
//		}
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codStato", codStato);
//		try {
//			idStato = jdbcTemplate.queryForObject(SELECT_ID_STATO, namedParameters, Long.class);
//			return idStato;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_ID_STATO";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//	
//	public String selectCodStato(String codStato) throws DatabaseException {
//		String cod = null;
//
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codStato", codStato);
//		try {
//			cod = jdbcTemplate.queryForObject(SELECT_COD_STATO, namedParameters, String.class);
//			return cod;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_COD_STATO";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//
//	public Long selectIdSportello() throws DatabaseException {
//		Long idSportello = null;
//	
//		SqlParameterSource namedParameters = new MapSqlParameterSource();
//		try {
//			idSportello = jdbcTemplate.queryForObject(SELECT_ID_SPORTELLI, namedParameters, Long.class);
//			return idSportello;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_ID_SPORTELLI";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//
//	public Long selectIdContributo(String codContributo) throws DatabaseException {
//		Long idContributo = null;
//		if (StringUtils.isEmpty(codContributo)) {
//			return idContributo;
//		}
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codContributo", codContributo);
//		try {
//			idContributo = jdbcTemplate.queryForObject(SELECT_ID_CONTRIBUTO, namedParameters, Long.class);
//			return idContributo;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_ID_CONTRIBUTO";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//	
//	public String selectCodContributo(String codContributo) throws DatabaseException {
//		String cod = null;
//
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codContributo", codContributo);
//		try {
//			cod = jdbcTemplate.queryForObject(SELECT_COD_CONTRIBUTO, namedParameters, String.class);
//			return cod;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_COD_CONTRIBUTO";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//
//	public Long selectIdTitolo(String codTitolo) throws DatabaseException {
//		Long idTitolo = null;
//		if (StringUtils.isEmpty(codTitolo)) {
//			return idTitolo;
//		}
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codTitolo", codTitolo);
//		try {
//			idTitolo = jdbcTemplate.queryForObject(SELECT_ID_TITOLO, namedParameters, Long.class);
//			return idTitolo;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_ID_TITOLO";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//
//	public Long selectIdRapporto(String codRapporto) throws DatabaseException {
//		Long idRapporto = null;
//		if (StringUtils.isEmpty(codRapporto)) {
//			return idRapporto;
//		}
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codRapporto", codRapporto);
//		try {
//			idRapporto = jdbcTemplate.queryForObject(SELECT_ID_RAPPORTO, namedParameters, Long.class);
//			return idRapporto;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_ID_RAPPORTO";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//	
//	public Long selectIdValutazioneMultidimensionale(String codValutazioneMultidimensionale) throws DatabaseException {
//		Long idValutazioneMultidimensionale = null;
//		if (StringUtils.isEmpty(codValutazioneMultidimensionale)) {
//			return idValutazioneMultidimensionale;
//		}
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codValutazioneMultidimensionale", codValutazioneMultidimensionale);
//		try {
//			idValutazioneMultidimensionale = jdbcTemplate.queryForObject(SELECT_ID_VALUTAZIONE_MULTIDIMENSIONALE, namedParameters, Long.class);
//			return idValutazioneMultidimensionale;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_ID_VALUTAZIONE_MULTIDIMENSIONALE";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//	
//	public Long selectIdContratto(String codContratto) throws DatabaseException {
//		Long idStato = null;
//		if (StringUtils.isEmpty(codContratto)) {
//			return idStato;
//		}
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codContratto", codContratto);
//		try {
//			idStato = jdbcTemplate.queryForObject(SELECT_ID_CONTRATTO, namedParameters, Long.class);
//			return idStato;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_ID_CONTRATTO";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//
//	public Long selectIdAsl(String codAsl) throws DatabaseException {
//		Long idAsl = null;
//		if (StringUtils.isEmpty(codAsl)) {
//			return idAsl;
//		}
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codAsl", codAsl);
//		try {
//			idAsl = jdbcTemplate.queryForObject(SELECT_ID_ASL, namedParameters, Long.class);
//			return idAsl;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_ID_ASL";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//	
//	public Long selectIdDettaglio(String numeroDomanda) throws DatabaseException {
//		Long idDettaglio = null;
//		if (StringUtils.isEmpty(numeroDomanda)) {
//			return idDettaglio;
//		}
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
//		try {
//			idDettaglio = jdbcTemplate.queryForObject(SELECT_ID_DETTAGLIO_DOMANDA, namedParameters, Long.class);
//			return idDettaglio;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_ID_DETTAGLIO_DOMANDA";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//
//	public Boolean presenzaDomanda(String codFiscale) throws DatabaseException {
//		Boolean isPresente = null;
//
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("cfDestinatario", codFiscale);
//		try {
//			isPresente = jdbcTemplate.queryForObject(DOMANDA_PRESENTE, namedParameters, Boolean.class);
//			return isPresente;
//		} catch (EmptyResultDataAccessException e) {
//			return false;
//		} catch (Exception e) {
//			String methodName = "DOMANDA_PRESENTE";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//
//	public Boolean residentePiemonte(String codProvincia) throws DatabaseException {
//		Boolean isPresente = null;
//
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codProvincia", codProvincia);
//		try {
//			isPresente = jdbcTemplate.queryForObject(RESIDENTE_PIEMONTE, namedParameters, Boolean.class);
//			return isPresente;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "RESIDENTE_PIEMONTE";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//
//	public long insertTDomanda(ModelBozzaRichiesta richiesta, Long idContributo, Long idSportello,
//			String codFiscale) throws DatabaseException {
//
//		KeyHolder keyHolder = new GeneratedKeyHolder();
//		MapSqlParameterSource params = new MapSqlParameterSource();
//
//		params.addValue("cfRichiedente", richiesta.getRichiedente().getCf(), Types.VARCHAR);
//		params.addValue("cfDestinatario", richiesta.getDestinatario().getCf(), Types.VARCHAR);
//		params.addValue("idTipoContributo", idContributo, Types.BIGINT);
//		params.addValue("idSportello", idSportello, Types.BIGINT);
//		params.addValue("utenteCrea", codFiscale, Types.VARCHAR);
//		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);
//
//		jdbcTemplate.update(INSERT_T_DOMANDA, params, keyHolder, new String[] { "domanda_id" });
//		return keyHolder.getKey().longValue();
//	}
//
//	public long insertTDomandaDettaglio(ModelBozzaRichiesta richiesta, String codFiscale, Long idDomanda, Long idSportello,
//			Long idStatoDomanda, Long idTipoRapporto, Long idTitoloStudio, Long idAsl, String codDetDomanda) throws DatabaseException {
//
//		KeyHolder keyHolder = new GeneratedKeyHolder();
//		MapSqlParameterSource params = new MapSqlParameterSource();
//
//		//params.addValue("codDetDomanda", codDetDomanda, Types.VARCHAR);
//		params.addValue("idDomanda", idDomanda, Types.BIGINT);
//		params.addValue("idSportello", idSportello, Types.BIGINT);
//		params.addValue("idStatoDomanda", idStatoDomanda, Types.BIGINT);
////		params.addValue("residentePiemonte", residentePiemonte, Types.BOOLEAN);
//		params.addValue("nomeRichiedente", richiesta.getRichiedente().getNome(), Types.VARCHAR);
//		params.addValue("cognomeRichiedente", richiesta.getRichiedente().getCognome(), Types.VARCHAR);
//		params.addValue("dataNascitaRichiedente", richiesta.getRichiedente().getDataNascita(), Types.DATE);
//		params.addValue("statoNascitaRichiedente", richiesta.getRichiedente().getStatoNascita(), Types.VARCHAR);
//		params.addValue("comuneNascitaRichiedente", richiesta.getRichiedente().getComuneNascita(), Types.VARCHAR);
//		params.addValue("provinciaNascitaRichiedente", richiesta.getRichiedente().getProvinciaNascita(), Types.VARCHAR);
//		params.addValue("indirizzoResidenzaRichiedente", richiesta.getRichiedente().getIndirizzoResidenza(), Types.VARCHAR);
//		params.addValue("comuneResidenzaRichiedente", richiesta.getRichiedente().getComuneResidenza(), Types.VARCHAR);
//		params.addValue("provinciaResidenzaRichiedente", richiesta.getRichiedente().getProvinciaResidenza(), Types.VARCHAR);
//		params.addValue("nomeDestinatario", richiesta.getDestinatario().getNome(), Types.VARCHAR);
//		params.addValue("cognomeDestinatario", richiesta.getDestinatario().getCognome(), Types.VARCHAR);
//		params.addValue("dataNascitaDestinatario", richiesta.getDestinatario().getDataNascita(), Types.DATE);
//		params.addValue("statoNascitaDestinatario", richiesta.getDestinatario().getStatoNascita(), Types.VARCHAR);
//		params.addValue("comuneNascitaDestinatario", richiesta.getDestinatario().getComuneNascita(), Types.VARCHAR);
//		params.addValue("provinciaNascitaDestinatario", richiesta.getDestinatario().getProvinciaNascita(), Types.VARCHAR);
//		params.addValue("indirizzoResidenzaDestinatario", richiesta.getDestinatario().getIndirizzoResidenza(), Types.VARCHAR);
//		params.addValue("comuneResidenzaDestinatario", richiesta.getDestinatario().getComuneResidenza(), Types.VARCHAR);
//		params.addValue("provinciaResidenzaDestinatario", richiesta.getDestinatario().getProvinciaResidenza(), Types.VARCHAR);
//		if(richiesta.getDomicilioDestinatario()!=null) {
//			params.addValue("indirizzoDomicilioDestinatario", richiesta.getDomicilioDestinatario().getIndirizzo(), Types.VARCHAR);
//			params.addValue("comuneDomicilioDestinatario", richiesta.getDomicilioDestinatario().getComune(), Types.VARCHAR);
//			params.addValue("provinciaDomicilioDestinatario", richiesta.getDomicilioDestinatario().getProvincia(), Types.VARCHAR);
//		}else {
//			params.addValue("indirizzoDomicilioDestinatario", null, Types.VARCHAR);
//			params.addValue("comuneDomicilioDestinatario", null, Types.VARCHAR);
//			params.addValue("provinciaDomicilioDestinatario", null, Types.VARCHAR);
//		}
//		params.addValue("situazioneLavorativa", richiesta.isLavoroDestinatario(), Types.BOOLEAN);
//		params.addValue("note", richiesta.getNote(), Types.VARCHAR);
//		params.addValue("idTipoRapporto", idTipoRapporto, Types.BIGINT);
//		params.addValue("idTitoloStudio", idTitoloStudio, Types.BIGINT);
//		params.addValue("idAsl", idAsl, Types.BIGINT);
//		params.addValue("utenteCrea", codFiscale, Types.VARCHAR);
//		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);
//
//		jdbcTemplate.update(INSERT_T_DOMANDA_DETTAGLIO, params, keyHolder, new String[] { "domanda_det_id" });
//		long iddettaglio = keyHolder.getKey().longValue(); 
//		updateDettaglioDomanda(iddettaglio, codDetDomanda + iddettaglio);
//		return iddettaglio;
//	}
//	
//	
//	public long updateDettaglioDomanda(long idDettaglio,String detCod) throws DatabaseException {
//
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("idDomanda", idDettaglio, Types.BIGINT).addValue("detCod", detCod, Types.VARCHAR);		
//		return jdbcTemplate.update(UPDATE_DET_COD, params);
//	}
//	
//	public ModelRichiestaExt selectNumeroRichiestaExt(String numeroRichiesta) throws DatabaseException {
//		ModelRichiestaExt richieste = new ModelRichiestaExt();
//		
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
//		try {
//			richieste = jdbcTemplate.queryForObject(SELECT_NUMERO_RICHIESTA_COD, namedParameters, new DettaglioRichiestaExtMapper());
//			return richieste;
//		} catch (EmptyResultDataAccessException e) {
//			return null; 
//		} catch (Exception e) {
//			String methodName = "SELECT_NUMERO_RICHIESTA_COD";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//
//	public long updateTDettaglioDomanda(Long idDettaglio, ModelRichiesta richiesta, Long idTitolo, Long idRapporto, Long idAsl, Long idValutazione, Long idRelazione, 
//			Long idContratto, String codFiscale, Long idTipoAssistente) throws DatabaseException {
//
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("idDettaglio", idDettaglio, Types.BIGINT);
//		params.addValue("punteggioSociale", richiesta.getPunteggioBisognoSociale(), Types.BIGINT);
//		params.addValue("nessunaIncompatibilita", richiesta.isNessunaIncompatibilita(), Types.BOOLEAN);
//		if(richiesta.getContratto()!=null) {
//			params.addValue("incompatibilitaPerContratto", richiesta.getContratto().isIncompatibilitaPerContratto(), Types.BOOLEAN);
//			if(richiesta.getContratto().getAgenzia()!=null) {
//				params.addValue("contrattoCfCooperativa", richiesta.getContratto().getAgenzia().getCf(), Types.VARCHAR);
//			}else {
//				params.addValue("contrattoCfCooperativa", null, Types.VARCHAR);
//			}
//			if(richiesta.getContratto().getAssistenteFamiliare()!=null) {
//				params.addValue("assistenteFamiliareNome", richiesta.getContratto().getAssistenteFamiliare().getNome(), Types.VARCHAR);
//				params.addValue("assistenteFamiliareCognome", richiesta.getContratto().getAssistenteFamiliare().getCognome(), Types.VARCHAR);
//				params.addValue("assistenteFamiliareCf", richiesta.getContratto().getAssistenteFamiliare().getCf(), Types.VARCHAR);
//				params.addValue("assistenteFamiliareNascitaStato", richiesta.getContratto().getAssistenteFamiliare().getStatoNascita(), Types.VARCHAR);
//				params.addValue("assistenteFamiliareNascitaComune", richiesta.getContratto().getAssistenteFamiliare().getComuneNascita(), Types.VARCHAR);
//				params.addValue("assistenteFamiliareNascitaData", richiesta.getContratto().getAssistenteFamiliare().getDataNascita(), Types.DATE);
//				params.addValue("assistenteFamiliareNascitaProvincia", richiesta.getContratto().getAssistenteFamiliare().getProvinciaNascita(), Types.VARCHAR);
//			} else {
//				params.addValue("assistenteFamiliareNome", null, Types.VARCHAR);
//				params.addValue("assistenteFamiliareCognome", null, Types.VARCHAR);
//				params.addValue("assistenteFamiliareCf", null, Types.VARCHAR);
//				params.addValue("assistenteFamiliareNascitaStato", null, Types.VARCHAR);
//				params.addValue("assistenteFamiliareNascitaComune", null, Types.VARCHAR);
//				params.addValue("assistenteFamiliareNascitaData", null, Types.DATE);
//				params.addValue("assistenteFamiliareNascitaProvincia", null, Types.VARCHAR);
//			}
//			if(richiesta.getContratto().getIntestatario()!=null) {
//				params.addValue("datoreNome", richiesta.getContratto().getIntestatario().getNome(), Types.VARCHAR);
//				params.addValue("datoreCognome", richiesta.getContratto().getIntestatario().getCognome(), Types.VARCHAR);
//				params.addValue("datoreCf", richiesta.getContratto().getIntestatario().getCf(), Types.VARCHAR);
//				params.addValue("datoreNascitaStato", richiesta.getContratto().getIntestatario().getStatoNascita(), Types.VARCHAR);
//				params.addValue("datoreNascitaComune", richiesta.getContratto().getIntestatario().getComuneNascita(), Types.VARCHAR);
//				params.addValue("datoreNascitaData", richiesta.getContratto().getIntestatario().getDataNascita(), Types.DATE);
//				params.addValue("datoreNascitaProvincia", richiesta.getContratto().getIntestatario().getProvinciaNascita(), Types.VARCHAR);
//			}else {
//				params.addValue("datoreNome", null, Types.VARCHAR);
//				params.addValue("datoreCognome", null, Types.VARCHAR);
//				params.addValue("datoreCf", null, Types.VARCHAR);
//				params.addValue("datoreNascitaStato", null, Types.VARCHAR);
//				params.addValue("datoreNascitaComune", null, Types.VARCHAR);
//				params.addValue("datoreNascitaData", null, Types.DATE);
//				params.addValue("datoreNascitaProvincia", null, Types.VARCHAR);
//			}
//			params.addValue("idContratto", idContratto, Types.BIGINT);
//			params.addValue("idRelazione", idRelazione, Types.BIGINT);
//			params.addValue("dataInizioContratto", richiesta.getContratto().getDataInizio(), Types.TIMESTAMP);
//			params.addValue("dataFineContratto", richiesta.getContratto().getDataFine(), Types.TIMESTAMP);
//			params.addValue("assistenteFamiliarePiva", richiesta.getContratto().getPivaAssitenteFamiliare(), Types.VARCHAR);
//			params.addValue("assistenteTipoId", idTipoAssistente, Types.BIGINT);
//		}else {
//			params.addValue("incompatibilitaPerContratto", null, Types.BOOLEAN);
//			params.addValue("contrattoCfCooperativa", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareNome", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareCognome", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareCf", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareNascitaStato", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareNascitaComune", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareNascitaData", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareNascitaProvincia", null, Types.VARCHAR);
//			params.addValue("datoreNome", null, Types.VARCHAR);
//			params.addValue("datoreCognome", null, Types.VARCHAR);
//			params.addValue("datoreCf", null, Types.VARCHAR);
//			params.addValue("datoreNascitaStato", null, Types.VARCHAR);
//			params.addValue("datoreNascitaComune", null, Types.VARCHAR);
//			params.addValue("datoreNascitaData", null, Types.VARCHAR);
//			params.addValue("datoreNascitaProvincia", null, Types.VARCHAR);
//			params.addValue("idContratto", null, Types.BIGINT);
//			params.addValue("idRelazione", null, Types.BIGINT);
//			params.addValue("dataInizioContratto", null, Types.VARCHAR);
//			params.addValue("dataFineContratto", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliarePiva", null, Types.VARCHAR);
//			params.addValue("assistenteTipoId", null, Types.BIGINT);
//		}
//		if(richiesta.getDomicilioDestinatario()!=null) {
//			params.addValue("indirizzoDomicilioDestinatario", richiesta.getDomicilioDestinatario().getIndirizzo(), Types.VARCHAR);
//			params.addValue("comuneDomicilioDestinatario", richiesta.getDomicilioDestinatario().getComune(), Types.VARCHAR);
//			params.addValue("provinciaDomicilioDestinatario", richiesta.getDomicilioDestinatario().getProvincia(), Types.VARCHAR);
//		}else {
//			params.addValue("indirizzoDomicilioDestinatario", null, Types.VARCHAR);
//			params.addValue("comuneDomicilioDestinatario", null, Types.VARCHAR);
//			params.addValue("provinciaDomicilioDestinatario", null, Types.VARCHAR);
//		}
//		params.addValue("situazioneLavorativa", richiesta.isLavoroDestinatario(), Types.BOOLEAN);
//		if(richiesta.getAccredito()!=null) {
//			params.addValue("iban",  richiesta.getAccredito().getIban(), Types.VARCHAR);
//			params.addValue("ibanIntestatario",  richiesta.getAccredito().getIntestatario(), Types.VARCHAR);
//		}else {
//			params.addValue("iban",  null, Types.VARCHAR);
//			params.addValue("ibanIntestatario",  null, Types.VARCHAR);
//		}
//		params.addValue("note",  richiesta.getNote(), Types.VARCHAR);
//		
//		params.addValue("idRapporto", idRapporto, Types.BIGINT);
//		params.addValue("idTitoloStudio", idTitolo, Types.BIGINT);
//		params.addValue("idAsl", idAsl, Types.BIGINT);
//		params.addValue("idValutazione", idValutazione, Types.BIGINT);
//		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);
//		params.addValue("iseeConforme", richiesta.isAttestazioneIsee(), Types.BOOLEAN);
//
//		return jdbcTemplate.update(UPDATE_T_DETTAGLIO_DOMANDA, params);
//	}
//	
//	public ModelUpdateCronologia insertTDettaglioDomandaCambioStato(ModelRichiestaExt richiesta,String codStato, String codFiscale) throws DatabaseException {
//		
//		ModelUpdateCronologia result = new ModelUpdateCronologia();
//		SqlParameterSource namedParametersIdDomanda = new MapSqlParameterSource().addValue("numeroRichiesta", richiesta.getNumero());
//		Long idDomanda = jdbcTemplate.queryForObject(SELECT_ID_DOMANDA_PRINCIPALE, namedParametersIdDomanda, Long.class);
//		Long idstato = selectIdStato(codStato);
//		Long idAsl = selectIdAsl(richiesta.getAslDestinatario());
//		Long idTitoloStudio = selectIdTitolo(richiesta.getStudioDestinatario());
//		Long idTipoRapporto = selectIdRapporto(richiesta.getDelega());
//		KeyHolder keyHolder = new GeneratedKeyHolder();
//		MapSqlParameterSource params = new MapSqlParameterSource();
//
//		
//		params.addValue("idDomanda", idDomanda, Types.BIGINT);
//		params.addValue("idSportello", richiesta.getSportelloId(), Types.BIGINT);
//		params.addValue("idStatoDomanda", idstato, Types.BIGINT);
//		params.addValue("nomeRichiedente", richiesta.getRichiedente().getNome(), Types.VARCHAR);
//		params.addValue("cognomeRichiedente", richiesta.getRichiedente().getCognome(), Types.VARCHAR);
//		params.addValue("dataNascitaRichiedente", richiesta.getRichiedente().getDataNascita(), Types.DATE);
//		params.addValue("statoNascitaRichiedente", richiesta.getRichiedente().getStatoNascita(), Types.VARCHAR);
//		params.addValue("comuneNascitaRichiedente", richiesta.getRichiedente().getComuneNascita(), Types.VARCHAR);
//		params.addValue("provinciaNascitaRichiedente", richiesta.getRichiedente().getProvinciaNascita(), Types.VARCHAR);
//		params.addValue("indirizzoResidenzaRichiedente", richiesta.getRichiedente().getIndirizzoResidenza(), Types.VARCHAR);
//		params.addValue("comuneResidenzaRichiedente", richiesta.getRichiedente().getComuneResidenza(), Types.VARCHAR);
//		params.addValue("provinciaResidenzaRichiedente", richiesta.getRichiedente().getProvinciaResidenza(), Types.VARCHAR);
//		params.addValue("nomeDestinatario", richiesta.getDestinatario().getNome(), Types.VARCHAR);
//		params.addValue("cognomeDestinatario", richiesta.getDestinatario().getCognome(), Types.VARCHAR);
//		params.addValue("dataNascitaDestinatario", richiesta.getDestinatario().getDataNascita(), Types.DATE);
//		params.addValue("statoNascitaDestinatario", richiesta.getDestinatario().getStatoNascita(), Types.VARCHAR);
//		params.addValue("comuneNascitaDestinatario", richiesta.getDestinatario().getComuneNascita(), Types.VARCHAR);
//		params.addValue("provinciaNascitaDestinatario", richiesta.getDestinatario().getProvinciaNascita(), Types.VARCHAR);
//		params.addValue("indirizzoResidenzaDestinatario", richiesta.getDestinatario().getIndirizzoResidenza(), Types.VARCHAR);
//		params.addValue("comuneResidenzaDestinatario", richiesta.getDestinatario().getComuneResidenza(), Types.VARCHAR);
//		params.addValue("provinciaResidenzaDestinatario", richiesta.getDestinatario().getProvinciaResidenza(), Types.VARCHAR);
//		if(richiesta.getDomicilioDestinatario()!=null) {
//			params.addValue("indirizzoDomicilioDestinatario", richiesta.getDomicilioDestinatario().getIndirizzo(), Types.VARCHAR);
//			params.addValue("comuneDomicilioDestinatario", richiesta.getDomicilioDestinatario().getComune(), Types.VARCHAR);
//			params.addValue("provinciaDomicilioDestinatario", richiesta.getDomicilioDestinatario().getProvincia(), Types.VARCHAR);
//		}else {
//			params.addValue("indirizzoDomicilioDestinatario", null, Types.VARCHAR);
//			params.addValue("comuneDomicilioDestinatario", null, Types.VARCHAR);
//			params.addValue("provinciaDomicilioDestinatario", null, Types.VARCHAR);
//		}
//		params.addValue("situazioneLavorativa", richiesta.isLavoroDestinatario(), Types.BOOLEAN);
//		params.addValue("note", richiesta.getNote(), Types.VARCHAR);
//		params.addValue("idTipoRapporto", idTipoRapporto, Types.BIGINT);
//		params.addValue("idTitoloStudio", idTitoloStudio, Types.BIGINT);
//		params.addValue("idAsl", idAsl, Types.BIGINT);
//		params.addValue("utenteCrea", codFiscale, Types.VARCHAR);
//		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);
//		
//		
//
//		jdbcTemplate.update(INSERT_T_DOMANDA_DETTAGLIO, params, keyHolder, new String[] { "domanda_det_id" });
//		long idDettaglio = keyHolder.getKey().longValue(); 
//		updateDettaglioDomanda(idDettaglio, codStato +"_"+ idDettaglio);
//		updateTDettaglioDomandaCambioStato(idDettaglio, richiesta, codFiscale);
//		result.setDetCod(codStato +"_"+ idDettaglio);
//		result.setIdDettaglio(idDettaglio);
//		
//		return result;
//	}
//	
//	private long updateTDettaglioDomandaCambioStato(Long idDettaglio, ModelRichiestaExt richiesta,String codFiscale) throws DatabaseException{
//		
//		Long idAsl = selectIdAsl(richiesta.getAslDestinatario());
//		Long idTitolo = selectIdTitolo(richiesta.getStudioDestinatario());
//		Long idRapporto = selectIdRapporto(richiesta.getDelega());
//		Long idValutazione = selectIdValutazioneMultidimensionale(richiesta.getValutazioneMultidimensionale());
//		
//		
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("idDettaglio", idDettaglio, Types.BIGINT);
//		params.addValue("punteggioSociale", richiesta.getPunteggioBisognoSociale(), Types.BIGINT);
//		params.addValue("nessunaIncompatibilita", richiesta.isNessunaIncompatibilita(), Types.BOOLEAN);
//		if(richiesta.getContratto()!=null) {
//			Long idContratto = selectIdContratto(richiesta.getContratto().getTipo());
//			Long idRelazione = selectIdRapporto(richiesta.getContratto().getRelazioneDestinatario());
//			params.addValue("incompatibilitaPerContratto", richiesta.getContratto().isIncompatibilitaPerContratto(), Types.BOOLEAN);
//			if(richiesta.getContratto().getAgenzia()!=null) {
//				params.addValue("contrattoCfCooperativa", richiesta.getContratto().getAgenzia().getCf(), Types.VARCHAR);
//			}else {
//				params.addValue("contrattoCfCooperativa", null, Types.VARCHAR);
//			}
//			if(richiesta.getContratto().getAssistenteFamiliare()!=null) {
//				params.addValue("assistenteFamiliareNome", richiesta.getContratto().getAssistenteFamiliare().getNome(), Types.VARCHAR);
//				params.addValue("assistenteFamiliareCognome", richiesta.getContratto().getAssistenteFamiliare().getCognome(), Types.VARCHAR);
//				params.addValue("assistenteFamiliareCf", richiesta.getContratto().getAssistenteFamiliare().getCf(), Types.VARCHAR);
//				params.addValue("assistenteFamiliareNascitaStato", richiesta.getContratto().getAssistenteFamiliare().getStatoNascita(), Types.VARCHAR);
//				params.addValue("assistenteFamiliareNascitaComune", richiesta.getContratto().getAssistenteFamiliare().getComuneNascita(), Types.VARCHAR);
//				params.addValue("assistenteFamiliareNascitaData", richiesta.getContratto().getAssistenteFamiliare().getDataNascita(), Types.DATE);
//				params.addValue("assistenteFamiliareNascitaProvincia", richiesta.getContratto().getAssistenteFamiliare().getProvinciaNascita(), Types.VARCHAR);
//			} else {
//				params.addValue("assistenteFamiliareNome", null, Types.VARCHAR);
//				params.addValue("assistenteFamiliareCognome", null, Types.VARCHAR);
//				params.addValue("assistenteFamiliareCf", null, Types.VARCHAR);
//				params.addValue("assistenteFamiliareNascitaStato", null, Types.VARCHAR);
//				params.addValue("assistenteFamiliareNascitaComune", null, Types.VARCHAR);
//				params.addValue("assistenteFamiliareNascitaData", null, Types.DATE);
//				params.addValue("assistenteFamiliareNascitaProvincia", null, Types.VARCHAR);
//			}
//			if(richiesta.getContratto().getIntestatario()!=null) {
//				params.addValue("datoreNome", richiesta.getContratto().getIntestatario().getNome(), Types.VARCHAR);
//				params.addValue("datoreCognome", richiesta.getContratto().getIntestatario().getCognome(), Types.VARCHAR);
//				params.addValue("datoreCf", richiesta.getContratto().getIntestatario().getCf(), Types.VARCHAR);
//				params.addValue("datoreNascitaStato", richiesta.getContratto().getIntestatario().getStatoNascita(), Types.VARCHAR);
//				params.addValue("datoreNascitaComune", richiesta.getContratto().getIntestatario().getComuneNascita(), Types.VARCHAR);
//				params.addValue("datoreNascitaData", richiesta.getContratto().getIntestatario().getDataNascita(), Types.DATE);
//				params.addValue("datoreNascitaProvincia", richiesta.getContratto().getIntestatario().getProvinciaNascita(), Types.VARCHAR);
//			}else {
//				params.addValue("datoreNome", null, Types.VARCHAR);
//				params.addValue("datoreCognome", null, Types.VARCHAR);
//				params.addValue("datoreCf", null, Types.VARCHAR);
//				params.addValue("datoreNascitaStato", null, Types.VARCHAR);
//				params.addValue("datoreNascitaComune", null, Types.VARCHAR);
//				params.addValue("datoreNascitaData", null, Types.DATE);
//				params.addValue("datoreNascitaProvincia", null, Types.VARCHAR);
//			}
//			params.addValue("idContratto", idContratto, Types.BIGINT);
//			params.addValue("idRelazione", idRelazione, Types.BIGINT);
//			params.addValue("dataInizioContratto", richiesta.getContratto().getDataInizio(), Types.TIMESTAMP);
//			params.addValue("dataFineContratto", richiesta.getContratto().getDataFine(), Types.TIMESTAMP);
//		}else {
//			params.addValue("incompatibilitaPerContratto", null, Types.BOOLEAN);
//			params.addValue("contrattoCfCooperativa", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareNome", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareCognome", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareCf", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareNascitaStato", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareNascitaComune", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareNascitaData", null, Types.VARCHAR);
//			params.addValue("assistenteFamiliareNascitaProvincia", null, Types.VARCHAR);
//			params.addValue("datoreNome", null, Types.VARCHAR);
//			params.addValue("datoreCognome", null, Types.VARCHAR);
//			params.addValue("datoreCf", null, Types.VARCHAR);
//			params.addValue("datoreNascitaStato", null, Types.VARCHAR);
//			params.addValue("datoreNascitaComune", null, Types.VARCHAR);
//			params.addValue("datoreNascitaData", null, Types.VARCHAR);
//			params.addValue("datoreNascitaProvincia", null, Types.VARCHAR);
//			params.addValue("idContratto", null, Types.BIGINT);
//			params.addValue("idRelazione", null, Types.BIGINT);
//			params.addValue("dataInizioContratto", null, Types.VARCHAR);
//			params.addValue("dataFineContratto", null, Types.VARCHAR);
//		}
//		if(richiesta.getDomicilioDestinatario()!=null) {
//			params.addValue("indirizzoDomicilioDestinatario", richiesta.getDomicilioDestinatario().getIndirizzo(), Types.VARCHAR);
//			params.addValue("comuneDomicilioDestinatario", richiesta.getDomicilioDestinatario().getComune(), Types.VARCHAR);
//			params.addValue("provinciaDomicilioDestinatario", richiesta.getDomicilioDestinatario().getProvincia(), Types.VARCHAR);
//		}else {
//			params.addValue("indirizzoDomicilioDestinatario", null, Types.VARCHAR);
//			params.addValue("comuneDomicilioDestinatario", null, Types.VARCHAR);
//			params.addValue("provinciaDomicilioDestinatario", null, Types.VARCHAR);
//		}
//		params.addValue("situazioneLavorativa", richiesta.isLavoroDestinatario(), Types.BOOLEAN);
//		if(richiesta.getAccredito()!=null) {
//			params.addValue("iban",  richiesta.getAccredito().getIban(), Types.VARCHAR);
//			params.addValue("ibanIntestatario",  richiesta.getAccredito().getIntestatario(), Types.VARCHAR);
//		}else {
//			params.addValue("iban",  null, Types.VARCHAR);
//			params.addValue("ibanIntestatario",  null, Types.VARCHAR);
//		}
//		params.addValue("note",  richiesta.getNote(), Types.VARCHAR);
//		
//		params.addValue("idRapporto", idRapporto, Types.BIGINT);
//		params.addValue("idTitoloStudio", idTitolo, Types.BIGINT);
//		params.addValue("idAsl", idAsl, Types.BIGINT);
//		params.addValue("idValutazione", idValutazione, Types.BIGINT);
//		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);
//		params.addValue("iseeConforme", richiesta.isAttestazioneIsee(), Types.BOOLEAN);
//
//		return jdbcTemplate.update(UPDATE_T_DETTAGLIO_DOMANDA, params);
//		
//	}
//	
//	public long updateDataFineValDettaglioDomanda(BigDecimal idDettaglio) throws DatabaseException {
//
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("idDomanda", idDettaglio, Types.BIGINT);		
//		return jdbcTemplate.update(UPDATE_DATA_FINE_VAL_DETTAGLIO_DOMANDA, params);
//	}
//	
//	public long updateDataDomandaValDomanda(String numeroRichiesta, String codFiscale) throws DatabaseException {
//
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("numeroRichiesta", numeroRichiesta, Types.VARCHAR);		
//		params.addValue("codFiscale", codFiscale, Types.VARCHAR);		
//		return jdbcTemplate.update(UPDATE_DATA_DOMANDA_VAL_DOMANDA, params);
//	}
//	
//	public String selectNumeroDomanda(Long idDomanda) throws DatabaseException {
//		String cod = null;
//
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idDomanda", idDomanda);
//		try {
//			cod = jdbcTemplate.queryForObject(SELECT_NUMERO_DOMANDA_PRINCIPALE, namedParameters, String.class);
//			return cod;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_NUMERO_DOMANDA_PRINCIPALE";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//	
//	public String selectDetCod(String numeroDomanda) throws DatabaseException {
//		String cod = null;
//
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroRichiesta", numeroDomanda);
//		try {
//			cod = jdbcTemplate.queryForObject(SELECT_DETCOD_DOMANDA, namedParameters, String.class);
//			return cod;
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		} catch (Exception e) {
//			String methodName = "SELECT_DETCOD_DOMANDA";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//	
//public boolean isResidenzaPiemontese(String sigla) throws DatabaseException {
//		
//		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sigla", sigla);
//		try {
//			Integer numprovincia = jdbcTemplate.queryForObject(SELECT_PROVINCIA_PIEMONTE, namedParameters, Integer.class);
//			return numprovincia.intValue()>0;
//				
//		} catch (EmptyResultDataAccessException e) {
//			return false; 
//		} catch (Exception e) {
//			String methodName = "SELECT_PROVINCIA_PIEMONTE";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//
//public Long selectIdTipoAssistente(String codTipoAssistente) throws DatabaseException {
//	Long idTipoAssistente = null;
//	if (StringUtils.isEmpty(codTipoAssistente)) {
//		return idTipoAssistente;
//	}
//	SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codTipoAssistente", codTipoAssistente);
//	try {
//		idTipoAssistente = jdbcTemplate.queryForObject(SELECT_ID_TIPO_ASSISTENTE, namedParameters, Long.class);
//		return idTipoAssistente;
//	} catch (EmptyResultDataAccessException e) {
//		return null;
//	} catch (Exception e) {
//		String methodName = "SELECT_ID_TIPO_ASSISTENTE";
//		logError(methodName, e.getMessage());
//		throw new DatabaseException(e);
//	}
//
//}
//
//public long updateAtecoDettaglipDomanda(BigDecimal idDettaglio,String atecoCod, String atecoDesc, String codFiscale) throws DatabaseException {
//
//	MapSqlParameterSource params = new MapSqlParameterSource();
//	try {
//	params.addValue("atecoCod", atecoCod, Types.VARCHAR);		
//	params.addValue("atecoDesc", atecoDesc, Types.VARCHAR);	
//	params.addValue("codFiscale", codFiscale, Types.VARCHAR);	
//	params.addValue("idDettaglio", idDettaglio, Types.BIGINT);	
//	return jdbcTemplate.update(UPDATE_ATECO_COD_DETTAGLIO, params);
//	} catch (Exception e) {
//		String methodName = "UPDATE_ATECO_COD_DETTAGLIO";
//		logError(methodName, e.getMessage());
//		throw new DatabaseException(e);
//	}
//}
}

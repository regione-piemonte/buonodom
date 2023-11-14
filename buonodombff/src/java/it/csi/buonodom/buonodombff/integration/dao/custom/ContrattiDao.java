/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.custom;

import java.io.File;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import it.csi.buonodom.buonodombff.dto.ModelAllegato;
import it.csi.buonodom.buonodombff.dto.ModelContratto;
import it.csi.buonodom.buonodombff.dto.ModelContrattoAllegati;
import it.csi.buonodom.buonodombff.dto.ModelContrattoFornitore;
import it.csi.buonodom.buonodombff.dto.custom.ModelGetAllegato;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.integration.dao.utils.AllegatoContrattoMapper;
import it.csi.buonodom.buonodombff.integration.dao.utils.AllegatoGetMapper;
import it.csi.buonodom.buonodombff.integration.dao.utils.ContrattoFornitoreMapper;
import it.csi.buonodom.buonodombff.integration.dao.utils.ContrattoMapper;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.LoggerUtil;

@Repository
public class ContrattiDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String SELECT_CONTRATTI_BY_NUMERO_RICHIESTA = "select " + "btc.contratto_id, "
			+ "btc.datore_di_lavoro_nome, " + "btc.datore_di_lavoro_cognome, " + "btc.datore_di_lavoro_cf, "
			+ "btc.datore_di_lavoro_nascita_data , " + "btc.datore_di_lavoro_nascita_comune, "
			+ "btc.datore_di_lavoro_nascita_provincia, " + "btc.datore_di_lavoro_nascita_stato, "
			+ "btc.contratto_data_inizio, " + "btc.contratto_data_fine, " + "btbf.fornitore_nome, "
			+ "btbf.fornitore_cognome, " + "btbf.fornitore_cf, " + "btbf.fornitore_nascita_data, "
			+ "btbf.fornitore_nascita_stato, " + "btbf.fornitore_nascita_comune, "
			+ "btbf.fornitore_nascita_provincia, " + "bdft.fornitore_tipo_cod, " + "btc.fornitore_piva, "
			+ "bdct.contratto_tipo_cod, " + "bdrt.rapporto_tipo_cod " + "from " + "bdom_d_contratto_tipo bdct, "
			+ "bdom_d_fornitore_tipo bdft, " + "bdom_t_buono_fornitore btbf, " + "bdom_t_buono btb, "
			+ "bdom_t_domanda btd, " + "bdom_t_contratto btc "
			+ "left join bdom_d_rapporto_tipo bdrt on btc.rapporto_tipo_id = bdrt.rapporto_tipo_id " + "where "
			+ "btc.contratto_tipo_id = bdct.contratto_tipo_id " + "and btc.fornitore_tipo_id = bdft.fornitore_tipo_id "
			+ "and btbf.fornitore_id = btc.fornitore_id " + "and btc.data_cancellazione is null "
			+ "and bdct.data_cancellazione is null " + "and bdft.data_cancellazione is null "
			+ "and bdct.validita_fine is null " + "and bdrt.data_cancellazione is null "
			+ "and bdrt.validita_fine is null " + "and btbf.data_cancellazione is null "
			+ "and btb.data_cancellazione is null " + "and btd.domanda_numero = :numero_richiesta "
			+ "and btd.domanda_id = btb.domanda_id " + "and btbf.buono_id = btb.buono_id "
			+ "and btc.buono_id = btb.buono_id " + "and bdft.validita_fine is null";

	public static final String SELECT_CONTRATTI_BY_NUMERO_RICHIESTA_APERTO = "select " + "btc.contratto_id, "
			+ "btc.datore_di_lavoro_nome, " + "btc.datore_di_lavoro_cognome, " + "btc.datore_di_lavoro_cf, "
			+ "btc.datore_di_lavoro_nascita_data , " + "btc.datore_di_lavoro_nascita_comune, "
			+ "btc.datore_di_lavoro_nascita_provincia, " + "btc.datore_di_lavoro_nascita_stato, "
			+ "btc.contratto_data_inizio, " + "btc.contratto_data_fine, " + "btbf.fornitore_nome, "
			+ "btbf.fornitore_cognome, " + "btbf.fornitore_cf, " + "btbf.fornitore_nascita_data, "
			+ "btbf.fornitore_nascita_stato, " + "btbf.fornitore_nascita_comune, "
			+ "btbf.fornitore_nascita_provincia, " + "bdft.fornitore_tipo_cod, " + "btc.fornitore_piva, "
			+ "bdct.contratto_tipo_cod, " + "bdrt.rapporto_tipo_cod " + "from " + "bdom_d_contratto_tipo bdct, "
			+ "bdom_d_fornitore_tipo bdft, " + "bdom_t_buono_fornitore btbf, " + "bdom_t_buono btb, "
			+ "bdom_t_domanda btd, " + "bdom_t_contratto btc "
			+ "left join bdom_d_rapporto_tipo bdrt on btc.rapporto_tipo_id = bdrt.rapporto_tipo_id " + "where "
			+ "btc.contratto_tipo_id = bdct.contratto_tipo_id " + "and btc.fornitore_tipo_id = bdft.fornitore_tipo_id "
			+ "and btbf.fornitore_id = btc.fornitore_id " + "and btc.data_cancellazione is null "
			+ "and bdct.data_cancellazione is null " + "and bdft.data_cancellazione is null "
			+ "and btc.contratto_data_fine is null " + "and bdct.validita_fine is null "
			+ "and bdrt.data_cancellazione is null " + "and bdrt.validita_fine is null "
			+ "and btbf.data_cancellazione is null " + "and btb.data_cancellazione is null "
			+ "and btd.domanda_numero = :numero_richiesta " + "and btd.domanda_id = btb.domanda_id "
			+ "and btbf.buono_id = btb.buono_id " + "and btc.buono_id = btb.buono_id "
			+ "and bdft.validita_fine is null";

	public static final String SELECT_CONTRATTO_BY_ID = "select " + "btc.contratto_id, " + "btc.datore_di_lavoro_nome, "
			+ "btc.datore_di_lavoro_cognome, " + "btc.datore_di_lavoro_cf, " + "btc.datore_di_lavoro_nascita_data , "
			+ "btc.datore_di_lavoro_nascita_comune, " + "btc.datore_di_lavoro_nascita_provincia, "
			+ "btc.datore_di_lavoro_nascita_stato, " + "btc.contratto_data_inizio, " + "btc.contratto_data_fine, "
			+ "btbf.fornitore_nome, " + "btbf.fornitore_cognome, " + "btbf.fornitore_cf, "
			+ "btbf.fornitore_nascita_data, " + "btbf.fornitore_nascita_stato, " + "btbf.fornitore_nascita_comune, "
			+ "btbf.fornitore_nascita_provincia, " + "bdft.fornitore_tipo_cod, " + "btc.fornitore_piva, "
			+ "bdct.contratto_tipo_cod, " + "bdrt.rapporto_tipo_cod " + "from " + "bdom_d_contratto_tipo bdct, "
			+ "bdom_d_fornitore_tipo bdft, " + "bdom_t_buono_fornitore btbf, " + "bdom_t_buono btb, "
			+ "bdom_t_domanda btd, " + "bdom_t_contratto btc "
			+ "left join bdom_d_rapporto_tipo bdrt on btc.rapporto_tipo_id = bdrt.rapporto_tipo_id " + "where "
			+ "btc.contratto_tipo_id = bdct.contratto_tipo_id " + "and btc.fornitore_tipo_id = bdft.fornitore_tipo_id "
			+ "and btbf.fornitore_id = btc.fornitore_id " + "and btc.data_cancellazione is null "
			+ "and bdct.data_cancellazione is null " + "and bdft.data_cancellazione is null "
			+ "and bdct.validita_fine is null " + "and bdrt.data_cancellazione is null "
			+ "and bdrt.validita_fine is null " + "and btbf.data_cancellazione is null "
			+ "and btb.data_cancellazione is null " + "and btd.domanda_numero = :numero_richiesta "
			+ "and btd.domanda_id = btb.domanda_id " + "and btbf.buono_id = btb.buono_id "
			+ "and btc.buono_id = btb.buono_id " + "and bdft.validita_fine is null "
			+ "and btc.contratto_id = :contratto_id";

	public static final String SELECT_ALLEGATI_CONTRATTO = "select " + "	btba.allegato_id , "
			+ "	btba.file_name, " + "	bdbat.allegato_tipo_cod " + "from " + "	bdom_t_contratto btc, "
			+ "	bdom_r_contratto_allegato brca, " + "	bdom_t_buono_allegato btba, "
			+ "	bdom_d_buono_allegato_tipo bdbat " + "where " + "	btba.allegato_id  = brca.allegato_id   "
			+ "	and brca.contratto_id = btc.contratto_id " + "	and btba.allegato_tipo_id = bdbat.allegato_tipo_id "
			+ "	and btba.data_cancellazione is null " + "	and brca.data_cancellazione is null "
			+ "	and btba.data_cancellazione is null " + " and bdbat.validita_fine is null "
			+ "	and btc.contratto_id = :contratto_id;";

	public static final String SELECT_ID_FORNITORE_TIPO = "select bdft.fornitore_tipo_id " + "from "
			+ "	bdom_d_fornitore_tipo bdft  " + "where " + "	bdft.data_cancellazione is null " + "	and "
			+ "	bdft.fornitore_tipo_cod = :fornitore_tipo_cod "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String CHECK_ESISTENZA_ASSISTENTE_FAMIGLIARE = "select distinct btbf.fornitore_id from bdom_t_buono_fornitore btbf where btbf.data_cancellazione is null  "
			+ "   and buono_id = (select distinct btb.buono_id  from bdom_t_buono btb,bdom_t_domanda btd "
			+ "		where btb.data_cancellazione is null " + "		and btb.domanda_id = btd.domanda_id "
			+ "		and btd.data_cancellazione is null " + "		and btd.domanda_numero = :numero_richiesta) "
			+ "    and btbf.fornitore_cf = :fornitore_cf";

	public static final String CHECK_DATA_INIZIO_CONTRATTO = "select " + "	case " + "		when ( " + "		select "
			+ "			max(btc.contratto_data_fine) " + "		from " + "			bdom_t_contratto btc, "
			+ "			bdom_t_buono btb " + "		where " + "			btb.data_cancellazione is null "
			+ "			and btc.data_cancellazione is null "
			// + " and btc.contratto_data_inizio < now() "
			+ "			and btc.buono_id = btb.buono_id " + "			and btb.domanda_id = ( " + "			select "
			+ "				btd.domanda_id " + "			from " + "				bdom_t_domanda btd "
			+ "			where " + "				btd.data_cancellazione is null "
			+ "				and btd.domanda_numero = :numero_richiesta ) " + "			) >= :data_inizio then true "
			+ "		else false " + "	end;";

	public static final String CHECK_STESSO_FORNITORE = "with dataContratto as (select "
			+ "max(btc.contratto_data_fine::date) contratto_data_fine " + "from " + "bdom_t_contratto btc, "
			+ "bdom_t_buono btb " + "where " + "btb.data_cancellazione is null " + "and btc.data_cancellazione is null "
			+ "and btc.buono_id = btb.buono_id " + "and btc.fornitore_id = :fornitoreId " + "and btb.domanda_id = ( "
			+ "select " + "btd.domanda_id " + "from " + "bdom_t_domanda btd " + "where "
			+ "btd.data_cancellazione is null " + "and btd.domanda_numero = :numero_richiesta ) ) " + "select "
			+ "case " + "when  dataContratto.contratto_data_fine is null then true "
			+ "when dataContratto.contratto_data_fine::date >= :data_inizio::date then true " + "else false " + "end "
			+ "from dataContratto ";

	public static final String CHECK_CONTRATTI_FORNITORE = "select "
			+ "case when btc.contratto_data_fine is null then to_char(btc.contratto_data_inizio,'yyyy-MM-dd') ||';'||'9999-12-31' "
			+ "else to_char(btc.contratto_data_inizio,'yyyy-MM-dd')  ||';'||to_char(btc.contratto_data_fine,'yyyy-MM-dd') "
			+ "end datetot " + "from " + "bdom_t_contratto btc, " + "bdom_t_buono btb " + "where "
			+ "btb.data_cancellazione is null " + "and btc.data_cancellazione is null "
			+ "and btc.buono_id = btb.buono_id " + "and btc.fornitore_id  = :fornitoreId " + "and btb.domanda_id = ( "
			+ "select " + "btd.domanda_id " + "from " + "bdom_t_domanda btd " + "where "
			+ "btd.data_cancellazione is null " + "and btd.domanda_numero = :numero_richiesta )";

	public static final String CHECK_ESISTENZA_ALLEGATO = "select " + "	case " + "		when ( " + "		select "
			+ "			btba.file_name " + "		from " + "			bdom_t_buono_allegato btba " + "		where "
			+ "			btba.data_cancellazione is null "
			+ "			and btba.allegato_id = :allegato_id ) = :file_name " + "		and  " + "			( "
			+ "		select " + "			bdat.allegato_tipo_cod " + "		from "
			+ "			bdom_t_buono_allegato btba, " + "			bdom_d_buono_allegato_tipo bdat " + "		where "
			+ "			btba.allegato_tipo_id = bdat.allegato_tipo_id "
			+ "			and btba.data_cancellazione is null " + "			and bdat.data_cancellazione is null "
			+ "			and now() between coalesce(bdat.validita_inizio, "
			+ "			now()) and coalesce(bdat.validita_fine, " + "			now()) "
			+ "				and btba.allegato_id = :allegato_id ) = :allegato_tipo_cod  " + "			then true "
			+ "		else false " + "	end;";

	public static final String INSERT_CONTRATTO = "INSERT INTO bdom_t_contratto (datore_di_lavoro_nome, "
			+ "    datore_di_lavoro_cognome, datore_di_lavoro_cf, datore_di_lavoro_nascita_data, "
			+ "    datore_di_lavoro_nascita_comune, datore_di_lavoro_nascita_provincia, "
			+ "    datore_di_lavoro_nascita_stato, contratto_data_inizio,  contratto_data_fine, "
			+ "    fornitore_id, fornitore_tipo_id, fornitore_piva, contratto_tipo_id, "
			+ "    rapporto_tipo_id,  buono_id,  utente_creazione,  utente_modifica " + ")  "
			+ "select distinct :datore_di_lavoro_nome, :datore_di_lavoro_cognome,"
			+ ":datore_di_lavoro_cf, :datore_di_lavoro_nascita_data::DATE, "
			+ ":datore_di_lavoro_nascita_comune, :datore_di_lavoro_nascita_provincia, "
			+ ":datore_di_lavoro_nascita_stato, :contratto_data_inizio::DATE, :contratto_data_fine::DATE, "
			+ ":fornitoreId,bdft.fornitore_tipo_id,:fornitore_piva,bdct.contratto_tipo_id,bdrt.rapporto_tipo_id,btb.buono_id, "
			+ ":utente_creazione,  :utente_modifica "
			+ "from bdom_t_buono_fornitore btbf , bdom_d_fornitore_tipo bdft, bdom_d_contratto_tipo bdct , "
			+ "bdom_d_rapporto_tipo bdrt ,bdom_t_domanda btd,bdom_t_buono btb "
			+ "where btbf.data_cancellazione is null " + "and bdft.data_cancellazione is null "
			+ "and bdct.data_cancellazione is null " + "and bdrt.data_cancellazione is null "
			+ "and btd.data_cancellazione is null " + "and btbf.fornitore_id = :fornitoreId "
			+ "and btd.domanda_numero = :numero_richiesta " + "and btb.data_cancellazione is null "
			+ "and btb.domanda_id = btd.domanda_id " + "and btbf.buono_id = btb.buono_id "
			+ "and bdft.fornitore_tipo_cod = :fornitore_tipo_cod "
			+ "and bdct.contratto_tipo_cod = :contratto_tipo_cod " + "and bdrt.rapporto_tipo_cod = :rapporto_tipo_cod";

	public static final String INSERT_CONTRATTO_RAPPORTO_NULL = "INSERT INTO bdom_t_contratto (datore_di_lavoro_nome, "
			+ "    datore_di_lavoro_cognome, datore_di_lavoro_cf, datore_di_lavoro_nascita_data, "
			+ "    datore_di_lavoro_nascita_comune, datore_di_lavoro_nascita_provincia, "
			+ "    datore_di_lavoro_nascita_stato, contratto_data_inizio,  contratto_data_fine, "
			+ "    fornitore_id, fornitore_tipo_id, fornitore_piva, contratto_tipo_id, "
			+ "    buono_id,  utente_creazione,  utente_modifica " + ")  "
			+ "select distinct :datore_di_lavoro_nome, :datore_di_lavoro_cognome,"
			+ ":datore_di_lavoro_cf, :datore_di_lavoro_nascita_data::DATE, "
			+ ":datore_di_lavoro_nascita_comune, :datore_di_lavoro_nascita_provincia, "
			+ ":datore_di_lavoro_nascita_stato, :contratto_data_inizio::DATE, :contratto_data_fine::DATE, "
			+ ":fornitoreId,bdft.fornitore_tipo_id,:fornitore_piva,bdct.contratto_tipo_id,btb.buono_id, "
			+ ":utente_creazione,  :utente_modifica "
			+ "from bdom_t_buono_fornitore btbf , bdom_d_fornitore_tipo bdft, bdom_d_contratto_tipo bdct , "
			+ "bdom_d_rapporto_tipo bdrt ,bdom_t_domanda btd,bdom_t_buono btb "
			+ "where btbf.data_cancellazione is null " + "and bdft.data_cancellazione is null "
			+ "and bdct.data_cancellazione is null " + "and bdrt.data_cancellazione is null "
			+ "and btd.data_cancellazione is null " + "and btbf.fornitore_id = :fornitoreId "
			+ "and btd.domanda_numero = :numero_richiesta " + "and btb.data_cancellazione is null "
			+ "and btb.domanda_id = btd.domanda_id " + "and btbf.buono_id = btb.buono_id "
			+ "and bdft.fornitore_tipo_cod = :fornitore_tipo_cod "
			+ "and bdct.contratto_tipo_cod = :contratto_tipo_cod";

	public static final String INSERT_R_CONTRATTO_ALLEGATO = "INSERT INTO buonodom.bdom_r_contratto_allegato ( "
			+ "    contratto_id,  allegato_id,  validita_inizio,   utente_creazione, "
			+ "    utente_modifica ) VALUES ( :contratto_id,  :allegato_id,   now(), "
			+ "    :utente_creazione,  :utente_modifica ); ";

	public static final String INSERT_FORNITORE = "INSERT INTO buonodom.bdom_t_buono_fornitore ( "
			+ "    fornitore_nome,   fornitore_cognome,   fornitore_cf, "
			+ "    fornitore_denominazione,    fornitore_nascita_data,  fornitore_nascita_stato, "
			+ "    fornitore_nascita_provincia, fornitore_nascita_comune,  buono_id, "
			+ "    utente_creazione, utente_modifica) VALUES (  :fornitore_nome, "
			+ "    :fornitore_cognome,    :fornitore_cf,   :fornitore_denominazione, "
			+ "    :fornitore_nascita_data,     :fornitore_nascita_stato,    :fornitore_nascita_provincia, "
			+ "    :fornitore_nascita_comune,    "
			+ "    (select distinct btb.buono_id  from bdom_t_buono btb,bdom_t_domanda btd "
			+ "		where btb.data_cancellazione is null " + "		and btb.domanda_id = btd.domanda_id "
			+ "		and btd.data_cancellazione is null " + "		and btd.domanda_numero = :numero_richiesta), "
			+ ":utente_creazione,   :utente_modifica " + ")";

	public static final String UPDATE_FORNITORE = "update buonodom.bdom_t_buono_fornitore set "
			+ "    fornitore_nome = :fornitore_nome , fornitore_cognome = :fornitore_cognome, "
			+ "    fornitore_denominazione = :fornitore_denominazione, fornitore_nascita_data = :fornitore_nascita_data, "
			+ "    fornitore_nascita_stato = :fornitore_nascita_stato, "
			+ "    fornitore_nascita_provincia = :fornitore_nascita_provincia,  fornitore_nascita_comune = :fornitore_nascita_comune, "
			+ "    utente_modifica = :utente_modifica where buono_id = "
			+ "    (select distinct btb.buono_id  from bdom_t_buono btb,bdom_t_domanda btd "
			+ "		where btb.data_cancellazione is null " + "		and btb.domanda_id = btd.domanda_id "
			+ "		and btd.data_cancellazione is null " + "		and btd.domanda_numero = :numero_richiesta) "
			+ "and fornitore_cf = :fornitoreCf";

	public static final String UPDATE_CONTRATTO_DATA_FINE = "UPDATE bdom_t_contratto "
			+ "SET contratto_data_fine = :dataFine, data_modifica = now(), utente_modifica = :utente_modifica "
			+ "WHERE contratto_id = :contratto_id AND data_cancellazione IS NULL";

	public static final String UPDATE_R_CONTRATTO_ALLEGATO_DATA_FINE = "UPDATE bdom_r_contratto_allegato "
			+ "SET validita_fine = now(), data_modifica = now(), utente_modifica = :utente_modifica "
			+ "WHERE contratto_id = :contratto_id AND data_cancellazione IS NULL " + "AND validita_fine is null";

	public static final String ESISTE_SPESA_SU_CONTRATTO = "select distinct con.contratto_id ,con.fornitore_id,bddss2.doc_spesa_stato_cod "
			+ "from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, "
			+ "bdom_t_buono btb,bdom_t_periodo_rendicontazione btpr, "
			+ "bdom_t_dichiarazione_spesa btds,bdom_d_dichiarazione_spesa_stato bddss,bdom_r_dichiarazione_spesa_stato brdss,bdom_t_contratto con, "
			+ "bdom_t_documento_spesa docs, bdom_d_documento_spesa_stato bddss2 , bdom_r_documento_spesa_stato brdss2 "
			+ "where btd.domanda_id = btdd.domanda_id " + "and btds.periodo_id = btpr.periodo_id "
			+ "and btds.buono_id = btb.buono_id " + "and bddss.dic_spesa_stato_id = brdss.dic_spesa_stato_id "
			+ "and brdss.dic_spesa_id = btds.dic_spesa_id " + "and btd.domanda_numero = :numeroDomanda "
			+ "and bdds.domanda_stato_cod ='IN_PAGAMENTO' " + "and btb.domanda_det_id = btdd.domanda_det_id "
			+ "and btb.sportello_id = btdd.sportello_id " + "and btb.domanda_id = btd.domanda_id "
			+ "and btdd.validita_fine is null " + "and btd.data_cancellazione is null "
			+ "and btdd.data_cancellazione is null " + "and bdds.data_cancellazione is null "
			+ "and btb.data_cancellazione is null " + "and brdss.validita_fine is null "
			+ "and con.data_cancellazione is null " + "and con.buono_id = btb.buono_id "
			+ "and btds.buono_id = con.buono_id " + "and docs.dic_spesa_id = btds.dic_spesa_id "
			+ "and docs.data_cancellazione is null " + "and docs.fornitore_id = con.fornitore_id "
			+ "and bddss2.data_cancellazione is null " + "and bddss.data_cancellazione is null "
			+ "and brdss2.validita_fine is null " + "and brdss2.data_cancellazione is null "
			+ "and btpr.data_cancellazione is null " + "and bddss2.doc_spesa_stato_id = brdss2.doc_spesa_stato_id "
			+ "and brdss2.doc_spesa_id = docs.doc_spesa_id " + "and con.contratto_id = :contrattoId";

	public static final String SELECT_CONTA_CONTRATTI_PER_FORNITORE = "with tab1 as "
			+ "(select distinct con.fornitore_id from " + "bdom_t_contratto con,bdom_t_buono_fornitore forn "
			+ "where forn.data_cancellazione is null " + "and con.data_cancellazione is null "
			+ "and forn.fornitore_id = con.fornitore_id " + "and con.contratto_id  = :contrattoId) "
			+ "select count(distinct contratto_id) from " + "bdom_t_contratto con,tab1 "
			+ "where  con.fornitore_id = tab1.fornitore_id";

	public static final String SELECT_ID_ALLEGATI_CONTRATTO = "select allegato_id from bdom_r_contratto_allegato "
			+ "where contratto_id = :contrattoId";

	public static final String DELETE_R_ALLEGATI_CONTRATTO = "delete from bdom_r_contratto_allegato "
			+ "where contratto_id = :contrattoId";

	public static final String DELETE_ALLEGATO_CONTRATTO = "delete from bdom_t_buono_allegato "
			+ "where allegato_id = :allegatoId";

	public static final String DELETE_CONTRATTO = "delete from bdom_t_contratto " + "where contratto_id = :contrattoId";

	public static final String DELETE_FORNITORE = "delete from bdom_t_buono_fornitore "
			+ "where fornitore_id = :fornitoreId";

	public static final String SELECT_ID_FORNITORE = "select distinct con.fornitore_id from "
			+ "bdom_t_contratto con,bdom_t_buono_fornitore forn " + "where forn.data_cancellazione is null "
			+ "and con.data_cancellazione is null " + "and forn.fornitore_id = con.fornitore_id "
			+ "and con.contratto_id = :contrattoId";

	public static final String SELECT_ALLEGATO_CONTRATTO = "select btba.file_name , btba.file_path "
			+ "from bdom_t_buono_allegato btba " + "where btba.allegato_id = :allegatoId "
			+ "and btba.data_cancellazione is null;";

	public ModelContratto insertContratto(ModelContratto contratto, String numeroRichiesta,
			String shibIdentitaCodiceFiscale, int fornitoreId) throws DatabaseException {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("datore_di_lavoro_cf", contratto.getIntestatario().getCf(), Types.VARCHAR);
		namedParameters.addValue("datore_di_lavoro_nome", contratto.getIntestatario().getNome(), Types.VARCHAR);
		namedParameters.addValue("datore_di_lavoro_cognome", contratto.getIntestatario().getCognome(), Types.VARCHAR);
		namedParameters.addValue("datore_di_lavoro_nascita_data", contratto.getIntestatario().getDataNascita(),
				Types.DATE);
		namedParameters.addValue("datore_di_lavoro_nascita_comune", contratto.getIntestatario().getComuneNascita(),
				Types.VARCHAR);
		namedParameters.addValue("datore_di_lavoro_nascita_provincia",
				contratto.getIntestatario().getProvinciaNascita(), Types.VARCHAR);
		namedParameters.addValue("datore_di_lavoro_nascita_stato", contratto.getIntestatario().getStatoNascita(),
				Types.VARCHAR);
		namedParameters.addValue("contratto_data_inizio", contratto.getDataInizio(), Types.DATE);
		namedParameters.addValue("contratto_data_fine", contratto.getDataFine(), Types.DATE);
		namedParameters.addValue("fornitore_tipo_cod", contratto.getTipoSupportoFamiliare(), Types.VARCHAR);
		namedParameters.addValue("fornitore_piva", contratto.getPivaAssitenteFamiliare(), Types.VARCHAR);
		namedParameters.addValue("contratto_tipo_cod", contratto.getTipo(), Types.VARCHAR);
		namedParameters.addValue("rapporto_tipo_cod", contratto.getRelazioneDestinatario(), Types.VARCHAR);
		namedParameters.addValue("numero_richiesta", numeroRichiesta, Types.VARCHAR);
		namedParameters.addValue("utente_creazione", shibIdentitaCodiceFiscale, Types.VARCHAR);
		namedParameters.addValue("utente_modifica", shibIdentitaCodiceFiscale, Types.VARCHAR);
		namedParameters.addValue("fornitoreId", fornitoreId, Types.BIGINT);

		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			if (contratto.getRelazioneDestinatario() != null)
				jdbcTemplate.update(INSERT_CONTRATTO, namedParameters, keyHolder, new String[] { "contratto_id" });
			else
				jdbcTemplate.update(INSERT_CONTRATTO_RAPPORTO_NULL, namedParameters, keyHolder,
						new String[] { "contratto_id" });
			contratto.setId(keyHolder.getKey().intValue());

			return contratto;

		} catch (Exception e) {
			String methodName = "insertContratto";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelContrattoAllegati> selectContrattiByNumeroRichiesta(String numeroRichiesta)
			throws DatabaseException {
		List<ModelContratto> contratti = new ArrayList<ModelContratto>();
		List<ModelContrattoAllegati> contrattiAllegati = new ArrayList<ModelContrattoAllegati>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numero_richiesta", numeroRichiesta);

		try {
			contratti = jdbcTemplate.query(SELECT_CONTRATTI_BY_NUMERO_RICHIESTA, namedParameters,
					new ContrattoMapper());
			for (int i = 0; i < contratti.size(); i++) {
				ModelContrattoAllegati contrattoAllegati = new ModelContrattoAllegati();
				contrattoAllegati.setContratto(contratti.get(i));
				contrattoAllegati.setAllegati(selectAllegatiContratto(contratti.get(i).getId()));
				contrattiAllegati.add(contrattoAllegati);
			}
			return contrattiAllegati;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectContrattiByNumeroRichiesta";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelContratto selectContrattiByNumeroRichiestaAperto(String numeroRichiesta) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numero_richiesta", numeroRichiesta);

		try {
			return jdbcTemplate.queryForObject(SELECT_CONTRATTI_BY_NUMERO_RICHIESTA_APERTO, namedParameters,
					new ContrattoMapper());

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_CONTRATTI_BY_NUMERO_RICHIESTA_APERTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public void chiudiContratto(Integer contratto_id, String shibIdentitaCodiceFiscale, Date dataFine)
			throws DatabaseException {

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("contratto_id", contratto_id, Types.BIGINT);
			namedParameters.addValue("utente_modifica", shibIdentitaCodiceFiscale, Types.VARCHAR);
			namedParameters.addValue("dataFine", dataFine, Types.DATE);

			jdbcTemplate.update(UPDATE_R_CONTRATTO_ALLEGATO_DATA_FINE, namedParameters);

			jdbcTemplate.update(UPDATE_CONTRATTO_DATA_FINE, namedParameters);

		} catch (EmptyResultDataAccessException e) {
		} catch (Exception e) {
			String methodName = "chiudiContratto";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelContratto selectContrattoById(Integer idContratto, String numeroRichiesta) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contratto_id", idContratto)
				.addValue("numero_richiesta", numeroRichiesta);
		ModelContratto contratto = new ModelContratto();
		try {
			contratto = jdbcTemplate.queryForObject(SELECT_CONTRATTO_BY_ID, namedParameters, new ContrattoMapper());
			return contratto;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectContrattoById";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int insertAssistenteFamigliare(ModelContratto contratto, String numeroRichiesta,
			String shibIdentitaCodiceFiscale) throws DatabaseException {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		if (contratto.getTipo().equals(Constants.COOPERATIVA)) {
			namedParameters.addValue("fornitore_cf", contratto.getAgenzia().getCf(), Types.VARCHAR);
			namedParameters.addValue("fornitore_nome", null, Types.VARCHAR);
			namedParameters.addValue("fornitore_cognome", null, Types.VARCHAR);
			namedParameters.addValue("fornitore_denominazione", null, Types.VARCHAR);
			namedParameters.addValue("fornitore_nascita_data", null, Types.DATE);
			namedParameters.addValue("fornitore_nascita_stato", null, Types.VARCHAR);
			namedParameters.addValue("fornitore_nascita_provincia", null, Types.VARCHAR);
			namedParameters.addValue("fornitore_nascita_comune", null, Types.VARCHAR);
		} else {
			namedParameters.addValue("fornitore_cf", contratto.getAssistenteFamiliare().getCf(), Types.VARCHAR);
			namedParameters.addValue("fornitore_nome", contratto.getAssistenteFamiliare().getNome(), Types.VARCHAR);
			namedParameters.addValue("fornitore_cognome", contratto.getAssistenteFamiliare().getCognome(),
					Types.VARCHAR);
			namedParameters.addValue("fornitore_denominazione", contratto.getAssistenteFamiliare().getNome() + " "
					+ contratto.getAssistenteFamiliare().getCognome(), Types.VARCHAR);
			namedParameters.addValue("fornitore_nascita_data", contratto.getAssistenteFamiliare().getDataNascita(),
					Types.DATE);
			namedParameters.addValue("fornitore_nascita_stato", contratto.getAssistenteFamiliare().getStatoNascita(),
					Types.VARCHAR);
			namedParameters.addValue("fornitore_nascita_provincia",
					contratto.getAssistenteFamiliare().getProvinciaNascita(), Types.VARCHAR);
			namedParameters.addValue("fornitore_nascita_comune", contratto.getAssistenteFamiliare().getComuneNascita(),
					Types.VARCHAR);
		}
		namedParameters.addValue("numero_richiesta", numeroRichiesta, Types.VARCHAR);
		namedParameters.addValue("utente_creazione", shibIdentitaCodiceFiscale, Types.VARCHAR);
		namedParameters.addValue("utente_modifica", shibIdentitaCodiceFiscale, Types.VARCHAR);

		try {
			jdbcTemplate.update(INSERT_FORNITORE, namedParameters, keyHolder, new String[] { "fornitore_id" });
			return keyHolder.getKey().intValue();
		} catch (Exception e) {
			String methodName = "insertAssistenteFamigliare";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int updateAssistenteFamigliare(ModelContratto contratto, String numeroRichiesta,
			String shibIdentitaCodiceFiscale, String fornitoreCf) throws DatabaseException {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("fornitoreCf", fornitoreCf, Types.VARCHAR);
		if (contratto.getTipo().equals(Constants.COOPERATIVA)) {
			namedParameters.addValue("fornitore_nome", null, Types.VARCHAR);
			namedParameters.addValue("fornitore_cognome", null, Types.VARCHAR);
			namedParameters.addValue("fornitore_denominazione", null, Types.VARCHAR);
			namedParameters.addValue("fornitore_nascita_data", null, Types.DATE);
			namedParameters.addValue("fornitore_nascita_stato", null, Types.VARCHAR);
			namedParameters.addValue("fornitore_nascita_provincia", null, Types.VARCHAR);
			namedParameters.addValue("fornitore_nascita_comune", null, Types.VARCHAR);
		} else {
			namedParameters.addValue("fornitore_nome", contratto.getAssistenteFamiliare().getNome(), Types.VARCHAR);
			namedParameters.addValue("fornitore_cognome", contratto.getAssistenteFamiliare().getCognome(),
					Types.VARCHAR);
			namedParameters.addValue("fornitore_denominazione", contratto.getAssistenteFamiliare().getNome() + " "
					+ contratto.getAssistenteFamiliare().getCognome(), Types.VARCHAR);
			namedParameters.addValue("fornitore_nascita_data", contratto.getAssistenteFamiliare().getDataNascita(),
					Types.DATE);
			namedParameters.addValue("fornitore_nascita_stato", contratto.getAssistenteFamiliare().getStatoNascita(),
					Types.VARCHAR);
			namedParameters.addValue("fornitore_nascita_provincia",
					contratto.getAssistenteFamiliare().getProvinciaNascita(), Types.VARCHAR);
			namedParameters.addValue("fornitore_nascita_comune", contratto.getAssistenteFamiliare().getComuneNascita(),
					Types.VARCHAR);
			namedParameters.addValue("numero_richiesta", numeroRichiesta, Types.VARCHAR);
			namedParameters.addValue("utente_modifica", shibIdentitaCodiceFiscale, Types.VARCHAR);
		}
		namedParameters.addValue("numero_richiesta", numeroRichiesta, Types.VARCHAR);
		namedParameters.addValue("utente_modifica", shibIdentitaCodiceFiscale, Types.VARCHAR);
		try {
			return jdbcTemplate.update(UPDATE_FORNITORE, namedParameters);

		} catch (Exception e) {
			String methodName = "UPDATE_FORNITORE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int checkEsistenzaAssistenteFamigliare(String fornitore_cf, String numero_richiesta)
			throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("fornitore_cf", fornitore_cf)
				.addValue("numero_richiesta", numero_richiesta);
		try {
			return jdbcTemplate.queryForObject(CHECK_ESISTENZA_ASSISTENTE_FAMIGLIARE, namedParameters, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "checkEsistenzaAssistenteFamigliare";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int[] insertAllegatiContratto(Integer contratto_id, List<ModelAllegato> allegati_id,
			String shibIdentitaCodiceFiscale) throws DatabaseException {
		SqlParameterSource[] batchArgs = new SqlParameterSource[allegati_id.size()];
		for (int i = 0; i < allegati_id.size(); i++) {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("allegato_id", allegati_id.get(i).getId(), Types.BIGINT);
			params.addValue("contratto_id", contratto_id, Types.BIGINT);
			params.addValue("utente_creazione", shibIdentitaCodiceFiscale, Types.VARCHAR);
			params.addValue("utente_modifica", shibIdentitaCodiceFiscale, Types.VARCHAR);
			batchArgs[i] = params;
		}

		try {
			return jdbcTemplate.batchUpdate(INSERT_R_CONTRATTO_ALLEGATO, batchArgs);

		} catch (Exception e) {
			String methodName = "insertAllegatiContratto";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelAllegato> selectAllegatiContratto(Integer idContratto) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contratto_id", idContratto);
		List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
		try {
			allegati = jdbcTemplate.query(SELECT_ALLEGATI_CONTRATTO, namedParameters, new AllegatoContrattoMapper());
			return allegati;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectAllegatiContratto";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Long selectIdFornitoreTipo(String fornitore_tipo_cod) throws DatabaseException {
		Long idStato = null;
		if (StringUtils.isEmpty(fornitore_tipo_cod)) {
			return idStato;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("fornitore_tipo_cod",
				fornitore_tipo_cod);
		try {
			idStato = jdbcTemplate.queryForObject(SELECT_ID_FORNITORE_TIPO, namedParameters, Long.class);
			return idStato;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Boolean checkEsistenzaAllegati(List<ModelAllegato> allegati) throws DatabaseException {
		Boolean controllo = true;
		try {
			for (int i = 0; i < allegati.size(); i++) {
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue("allegato_id", allegati.get(i).getId());
				params.addValue("file_name", allegati.get(i).getFilename());
				params.addValue("allegato_tipo_cod", allegati.get(i).getTipo());

				controllo = jdbcTemplate.queryForObject(CHECK_ESISTENZA_ALLEGATO, params, Boolean.class);

				if (!controllo) {
					return false;
				}

			}
			return true;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "checkEsistenzaAllegati";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Boolean checkSovrapposizioneDateContratto(String numero_richiesta, Date data_inizio)
			throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("numero_richiesta", numero_richiesta);
		params.addValue("data_inizio", data_inizio);

		try {
			return jdbcTemplate.queryForObject(CHECK_DATA_INIZIO_CONTRATTO, params, Boolean.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "checkEsistenzaAllegati";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Boolean checkSovrapposizioneFornitore(String numero_richiesta, Date data_inizio, int fornitoreId)
			throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("numero_richiesta", numero_richiesta);
		params.addValue("data_inizio", data_inizio);
		params.addValue("fornitoreId", fornitoreId);

		try {
			if (fornitoreId != 0)
				return jdbcTemplate.queryForObject(CHECK_STESSO_FORNITORE, params, Boolean.class);
			else
				return false;
		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "CHECK_STESSO_FORNITORE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<String> checkSovrapposizioneContrattiFornitore(String numero_richiesta, int fornitoreId)
			throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("numero_richiesta", numero_richiesta);
		try {
			if (fornitoreId != 0)
				return jdbcTemplate.queryForList(CHECK_CONTRATTI_FORNITORE, params, String.class);
			else
				return null;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "CHECK_CONTRATTI_FORNITORE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelContrattoFornitore> esisteDocumentoSuContratto(String numeroDomanda, Integer contrattoId)
			throws DatabaseException {
		List<ModelContrattoFornitore> contratti = new ArrayList<ModelContrattoFornitore>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda)
				.addValue("contrattoId", contrattoId);

		try {
			contratti = jdbcTemplate.query(ESISTE_SPESA_SU_CONTRATTO, namedParameters, new ContrattoFornitoreMapper());

			return contratti;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "ESISTE_SPESA_SU_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Integer selectContrattoFornitore(Integer contrattoId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contrattoId", contrattoId);
		try {
			return jdbcTemplate.queryForObject(SELECT_CONTA_CONTRATTI_PER_FORNITORE, namedParameters, Integer.class);

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_CONTA_CONTRATTI_PER_FORNITORE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<Integer> selectIdAllegatiContratto(Integer contrattoId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contrattoId", contrattoId);
		try {
			return jdbcTemplate.queryForList(SELECT_ID_ALLEGATI_CONTRATTO, namedParameters, Integer.class);

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_ALLEGATI_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public boolean eliminaAllegatiContratto(Integer contrattoId) throws DatabaseException {
		boolean esitoserv = false;
		try {
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contrattoId", contrattoId);
			List<Integer> contratti = selectIdAllegatiContratto(contrattoId);

			int esitook = jdbcTemplate.update(DELETE_R_ALLEGATI_CONTRATTO, namedParameters);
			// esito positivo
			if (esitook > 0) {
				for (Integer id : contratti) {
					// cancello da file system
					String os = System.getProperty("os.name");
					boolean locale = false;
					if (os.toLowerCase().contains("win")) {
						locale = true;
					}
					ModelGetAllegato allegato = selectAllegatoContratto(id);
					File f = null;
					if (!locale)
						f = new File(allegato.getFilePath() + "/" + allegato.getFileName());
					else
						f = new File(allegato.getFilePath() + "\\" + allegato.getFileName());
					logInfo("cancello allegato del contratto " + id,
							allegato.getFilePath() + "\\" + allegato.getFileName());
					f.delete();
					File dir = new File(allegato.getFilePath());
					if (dir.exists()) {
						dir.delete();
					}
					eliminaAllegati(id);
				}
				// verifico se il fornitore associato ad altri contratti se no lo elimino
				// altrimenti lascio
				int contafornitori = selectContrattoFornitore(contrattoId);
				if (contafornitori == 1) {
					int forn = selectIdFonitore(contrattoId);
					eliminaContratto(contrattoId);
					eliminaFornitore(forn);
				} else {
					eliminaContratto(contrattoId);
				}
				esitoserv = true;
			}
		} catch (Exception e) {
			String methodName = "DELETE_R_ALLEGATI_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
		return esitoserv;
	}

	public void eliminaAllegati(Integer allegatoId) throws DatabaseException {
		try {
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("allegatoId", allegatoId);
			jdbcTemplate.update(DELETE_ALLEGATO_CONTRATTO, namedParameters);
		} catch (Exception e) {
			String methodName = "DELETE_ALLEGATO_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public void eliminaContratto(Integer contrattoId) throws DatabaseException {
		try {
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contrattoId", contrattoId);
			jdbcTemplate.update(DELETE_CONTRATTO, namedParameters);
		} catch (Exception e) {
			String methodName = "DELETE_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public void eliminaFornitore(Integer fornitoreId) throws DatabaseException {
		try {
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("fornitoreId", fornitoreId);
			jdbcTemplate.update(DELETE_FORNITORE, namedParameters);
		} catch (Exception e) {
			String methodName = "DELETE_FORNITORE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Integer selectIdFonitore(Integer contrattoId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contrattoId", contrattoId);
		try {
			return jdbcTemplate.queryForObject(SELECT_ID_FORNITORE, namedParameters, Integer.class);

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_FORNITORE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelGetAllegato selectAllegatoContratto(Integer idAllegato) throws DatabaseException {

		ModelGetAllegato allegato = new ModelGetAllegato();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("allegatoId", idAllegato);
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
}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodombff.dto.ModelDocumentoSpesa;
import it.csi.buonodom.buonodombff.dto.ModelDocumentoSpesaDettaglio;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.integration.dao.utils.DocumentoSpesaDettaglioMapper;
import it.csi.buonodom.buonodombff.integration.dao.utils.DocumentoSpesaMapper;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.Converter;
import it.csi.buonodom.buonodombff.util.LoggerUtil;

@Repository
public class RendicontazioneDao extends LoggerUtil {

	public static final String INSERT_DICHIARAZIONE_SPESA = "INSERT INTO bdom_t_dichiarazione_spesa "
			+ "(periodo_id, dic_spesa_periodo_desc, buono_id,  " + "utente_creazione, utente_modifica) " + " VALUES("
			+ "(select periodo_id from bdom_t_periodo_rendicontazione where "
			+ "now()::date BETWEEN periodo_inizio::date and COALESCE(periodo_fine::date, now()::date) and data_cancellazione is null), "
			+ "(select 'Periodo dal ' || to_char(periodo_inizio,'dd/MM/yyyy') || ' al ' || to_char(periodo_fine,'dd/MM/yyyy') from bdom_t_periodo_rendicontazione where "
			+ "now()::date BETWEEN periodo_inizio::date and COALESCE(periodo_fine::date, now()::date) and data_cancellazione is null), "
			+ "(select distinct btb.buono_id  from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, "
			+ "bdom_t_buono btb " + "where btd.domanda_id = btdd.domanda_id "
			+ "and btd.domanda_numero = :numeroDomanda " + "and btb.domanda_det_id = btdd.domanda_det_id "
			+ "and btb.sportello_id = btdd.sportello_id " + "and btb.domanda_id = btd.domanda_id "
			+ "and btdd.validita_fine is null " + "and btd.data_cancellazione is null "
			+ "and btdd.data_cancellazione is null " + "and btb.data_cancellazione is null), "
			+ ":utente_creazione, :utente_modifica)";

	public static final String INSERT_R_DICHIARAZIONE_SPESA = "INSERT INTO bdom_r_dichiarazione_spesa_stato "
			+ "(dic_spesa_id, dic_spesa_stato_id,  " + "utente_creazione, utente_modifica) " + " VALUES(:dicSpesaId,"
			+ "(select dic_spesa_stato_id from bdom_d_dichiarazione_spesa_stato where "
			+ "validita_fine is null and data_cancellazione is null and dic_spesa_stato_cod = :dicSpesaStatoCod), "
			+ ":utente_creazione, :utente_modifica)";

	public static final String INSERT_DOCUMENTO_SPESA = "INSERT INTO bdom_t_documento_spesa "
			+ "(doc_spesa_periodo_inizio, doc_spesa_periodo_fine,doc_spesa_tipo_id,dic_spesa_id,fornitore_id, doc_numero, "
			+ "utente_creazione, utente_modifica) " + " VALUES( :docSpesaPeriodoInizio, :docSpesaPeriodoFine, "
			+ "(select doc_spesa_tipo_id from bdom_d_documento_spesa_tipo where "
			+ "validita_fine is null and data_cancellazione is null and doc_spesa_tipo_cod = :docSpesaTipoCod), "
			+ " :dicSpesaId, :fornitoreId, :docNumero, " + ":utente_creazione, :utente_modifica)";

	public static final String INSERT_R_DOCUMENTO_SPESA_STATO = "INSERT INTO bdom_r_documento_spesa_stato "
			+ "(doc_spesa_id, doc_spesa_stato_id, importo_totale_pagato, importo_quietanzato, importo_rendicontato,doc_spesa_stato_note, "
			+ "utente_creazione, utente_modifica) " + " VALUES(:docSpesaId,"
			+ "(select doc_spesa_stato_id from bdom_d_documento_spesa_stato where "
			+ "validita_fine is null and data_cancellazione is null and doc_spesa_stato_cod = :docSpesaStatoCod), "
			+ ":importoTotalePagato, :importoQuietanzato, (select distinct btgf.finanziamento_importo_mensile from bdom_t_graduatoria_dettaglio btgd, "
			+ "bdom_t_graduatoria_finanziamento btgf " + "where btgd.domanda_id = :domandaId "
			+ "and btgf.graduatoria_id = btgd.graduatoria_id "
			+ "and btgf.finanziamento_id = btgd.finanziamento_id and btgd.importo!=0), :docSpesaStatoNote, "
			+ ":utente_creazione, :utente_modifica)";

	public static final String INSERT_DOCUMENTO_SPESA_DETTAGLIO = "INSERT INTO bdom_t_documento_spesa_dettaglio "
			+ "(doc_spesa_det_importo, doc_spesa_det_tipo_id,doc_spesa_id,doc_spesa_det_data, "
			+ "utente_creazione, utente_modifica) " + " VALUES( :docSpesaDetImporto, "
			+ "(select doc_spesa_det_tipo_id from bdom_d_documento_spesa_dettaglio_tipo where "
			+ "validita_fine is null and data_cancellazione is null and doc_spesa_det_tipo_cod = :docSpesaDetTipoCod), "
			+ ":docSpesaId, :docSpesaDetData, " + ":utente_creazione, :utente_modifica)";

	public static final String INSERT_R_DOCUMENTO_SPESA_ALLEGATO = "INSERT INTO bdom_r_documento_spesa_dettaglio_allegato "
			+ "(doc_spesa_det_id, allegato_id, " + "utente_creazione, utente_modifica) "
			+ " VALUES(:docSpesaDetId, :allegatoId, " + ":utente_creazione, :utente_modifica)";

	public static final String ESISTE_DICHIARAZIONE_SPESA = "select distinct btds.dic_spesa_id "
			+ "from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, "
			+ "bdom_t_buono btb,bdom_t_periodo_rendicontazione btpr, "
			+ "bdom_t_dichiarazione_spesa btds,bdom_d_dichiarazione_spesa_stato bddss,bdom_r_dichiarazione_spesa_stato brdss "
			+ "where btd.domanda_id = btdd.domanda_id " + "and btds.periodo_id = btpr.periodo_id "
			+ "and btds.buono_id = btb.buono_id " + "and bddss.dic_spesa_stato_id = brdss.dic_spesa_stato_id "
			+ "and brdss.dic_spesa_id = btds.dic_spesa_id " + "and btd.domanda_numero = :numeroDomanda "
			+ "and bdds.domanda_stato_cod ='IN_PAGAMENTO' " + "and bddss.dic_spesa_stato_cod = :dicSpesaStatoCod "
			+ "and btb.domanda_det_id = btdd.domanda_det_id " + "and btb.sportello_id = btdd.sportello_id "
			+ "and btb.domanda_id = btd.domanda_id " + "and btdd.validita_fine is null "
			+ "and btd.data_cancellazione is null " + "and btdd.data_cancellazione is null "
			+ "and bdds.data_cancellazione is null " + "and btb.data_cancellazione is null "
			+ "and btpr.data_cancellazione is null " + "and brdss.validita_fine is null "
			+ "and now()::date BETWEEN btpr.periodo_inizio::date and COALESCE(btpr.periodo_fine::date, now()::date) "
			+ "and btpr.data_cancellazione is null";

	public static final String GET_DOCUMENTI_SPESA_PER_DICHIARAZIONE = "select a.dic_spesa_id from bdom_t_documento_spesa a "
			+ "where a.dic_spesa_id in ( " + "select distinct btds.dic_spesa_id "
			+ "from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, "
			+ "bdom_t_buono btb,bdom_t_periodo_rendicontazione btpr, "
			+ "bdom_t_dichiarazione_spesa btds,bdom_d_dichiarazione_spesa_stato bddss,bdom_r_dichiarazione_spesa_stato brdss "
			+ "where btd.domanda_id = btdd.domanda_id " + "and btds.periodo_id = btpr.periodo_id "
			+ "and btds.buono_id = btb.buono_id " + "and bddss.dic_spesa_stato_id = brdss.dic_spesa_stato_id "
			+ "and brdss.dic_spesa_id = btds.dic_spesa_id " + "and btd.domanda_numero = :numeroDomanda "
			+ "and bdds.domanda_stato_cod ='IN_PAGAMENTO' " + "and bddss.dic_spesa_stato_cod = :dicSpesaStatoCod "
			+ "and btb.domanda_det_id = btdd.domanda_det_id " + "and btb.sportello_id = btdd.sportello_id "
			+ "and btb.domanda_id = btd.domanda_id " + "and btdd.validita_fine is null "
			+ "and btd.data_cancellazione is null " + "and btdd.data_cancellazione is null "
			+ "and bdds.data_cancellazione is null " + "and btb.data_cancellazione is null "
			+ "and btpr.data_cancellazione is null " + "and brdss.validita_fine is null "
			+ "and now()::date BETWEEN btpr.periodo_inizio::date and COALESCE(btpr.periodo_fine::date, now()::date) "
			+ "and btpr.data_cancellazione is null)";

	public static final String GET_DOCUMENTI_SPESA_STATO = "select count(*) from bdom_r_documento_spesa_stato brdss, bdom_d_documento_spesa_stato bddss "
			+ "where brdss.doc_spesa_id = :docSpesaId " + "and brdss.doc_spesa_stato_id = bddss.doc_spesa_stato_id "
			+ "and bddss.doc_spesa_stato_cod ='DA_INVIARE' " + "and brdss.validita_fine is null "
			+ "and brdss.data_cancellazione is null " + "and bddss.data_cancellazione is null";

	public static final String DELETE_DOCUMENTO_SPESA = "DELETE from bdom_r_documento_spesa_dettaglio_allegato a "
			+ "where a.doc_spesa_det_id in (select doc_spesa_det_id from bdom_t_documento_spesa_dettaglio where doc_spesa_id=:docSpesaId and data_cancellazione is null) and a.data_cancellazione is null;"
			+ " " + "DELETE from bdom_t_documento_spesa_dettaglio a "
			+ "where a.doc_spesa_id = :docSpesaId and a.data_cancellazione is null;" + " "
			+ "DELETE from bdom_r_documento_spesa_stato a "
			+ "where a.doc_spesa_id = :docSpesaId and a.data_cancellazione is null;" + " "
			+ "DELETE from bdom_t_documento_spesa a "
			+ "where a.doc_spesa_id = :docSpesaId and a.data_cancellazione is null;";

	public static final String DELETE_ALLEGATO_DOCUMENTO_SPESA = "delete from bdom_t_buono_allegato a "
			+ "where allegato_id = :allegatoId and a.data_cancellazione is null;";

	public static final String DELETE_DICHIARAZIONE_SPESA = "delete from bdom_r_dichiarazione_spesa_stato a "
			+ "where a.dic_spesa_id = :dicSpesaId and a.data_cancellazione is null;" + " "
			+ "delete from bdom_r_dichiarazione_spesa_allegato a where a.dic_spesa_id = :dicSpesaId and a.data_cancellazione is null;"
			+ " " + "delete from bdom_t_dichiarazione_spesa a "
			+ "where a.dic_spesa_id = :dicSpesaId  and a.data_cancellazione is null;";

	public static final String GET_DOCUMENTO_SPESA = "select distinct btds2.doc_spesa_id,bddst.doc_spesa_tipo_cod , btds2.doc_numero ,btds2.fornitore_id, "
			+ "bddss2.doc_spesa_stato_cod,brdss2.doc_spesa_stato_note,to_char(btds2.doc_spesa_periodo_inizio,'yyyy-mm') doc_spesa_periodo_inizio,"
			+ "to_char(btds2.doc_spesa_periodo_fine,'yyyy-mm') doc_spesa_periodo_fine,to_char(btpr.periodo_fine,'yyyy-MM-dd') periodo_fine "
			+ "from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, "
			+ "bdom_t_buono btb,bdom_t_periodo_rendicontazione btpr, bdom_t_buono_allegato btba, bdom_d_buono_allegato_tipo bdbat, "
			+ "bdom_t_dichiarazione_spesa btds,bdom_d_dichiarazione_spesa_stato bddss,bdom_r_dichiarazione_spesa_stato brdss, "
			+ "bdom_t_documento_spesa btds2 , bdom_d_documento_spesa_tipo bddst,bdom_r_documento_spesa_stato brdss2,bdom_r_documento_spesa_dettaglio_allegato brdsa, "
			+ "bdom_t_documento_spesa_dettaglio btdsd ,bdom_d_documento_spesa_dettaglio_tipo bddsdt,bdom_t_buono_fornitore btbf,bdom_d_documento_spesa_stato bddss2 "
			+ "where btd.domanda_id = btdd.domanda_id "
			+ "and btdsd.doc_spesa_det_tipo_id = bddsdt.doc_spesa_det_tipo_id "
			+ "and btdsd.doc_spesa_id = btds2.doc_spesa_id " + "and btds2.doc_spesa_tipo_id = bddst.doc_spesa_tipo_id "
			+ "and btds2.dic_spesa_id = btds.dic_spesa_id " + "and btds2.fornitore_id = btbf.fornitore_id "
			+ "and bddss2.doc_spesa_stato_id = brdss2.doc_spesa_stato_id "
			+ "and bdbat.allegato_tipo_id = btba.allegato_tipo_id " + "and brdss2.doc_spesa_id = btds2.doc_spesa_id "
			+ "and brdsa.doc_spesa_det_id = btdsd.doc_spesa_det_id " + "and brdsa.allegato_id = btba.allegato_id "
			+ "and btbf.buono_id = btb.buono_id " + "and btds.periodo_id = btpr.periodo_id "
			+ "and btds.buono_id = btb.buono_id " + "and bddss.dic_spesa_stato_id = brdss.dic_spesa_stato_id "
			+ "and brdss.dic_spesa_id = btds.dic_spesa_id " + "and btd.domanda_numero = :numeroDomanda "
			+ "and bdds.domanda_stato_cod ='IN_PAGAMENTO' " + "and btb.domanda_det_id = btdd.domanda_det_id "
			+ "and btb.sportello_id = btdd.sportello_id " + "and btb.domanda_id = btd.domanda_id "
			+ "and btdd.validita_fine is null " + "and btpr.data_cancellazione is null "
			+ "and btd.data_cancellazione is null " + "and btdd.data_cancellazione is null "
			+ "and bdds.data_cancellazione is null " + "and btb.data_cancellazione is null";

	public static final String GET_DOCUMENTO_SPESA_DETTAGLIO = "select distinct "
			+ "brdsa.allegato_id ,bddsdt.doc_spesa_det_tipo_cod ,btdsd.doc_spesa_det_data ,btdsd.doc_spesa_det_importo "
			+ "from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, "
			+ "bdom_t_buono btb,bdom_t_periodo_rendicontazione btpr, bdom_t_buono_allegato btba, bdom_d_buono_allegato_tipo bdbat, "
			+ "bdom_t_dichiarazione_spesa btds,bdom_d_dichiarazione_spesa_stato bddss,bdom_r_dichiarazione_spesa_stato brdss, "
			+ "bdom_t_documento_spesa btds2 , bdom_d_documento_spesa_tipo bddst,bdom_r_documento_spesa_stato brdss2,bdom_r_documento_spesa_dettaglio_allegato brdsa, "
			+ "bdom_t_documento_spesa_dettaglio btdsd ,bdom_d_documento_spesa_dettaglio_tipo bddsdt,bdom_t_buono_fornitore btbf,bdom_d_documento_spesa_stato bddss2 "
			+ "where btd.domanda_id = btdd.domanda_id "
			+ "and btdsd.doc_spesa_det_tipo_id = bddsdt.doc_spesa_det_tipo_id "
			+ "and btdsd.doc_spesa_id = btds2.doc_spesa_id " + "and btds2.doc_spesa_tipo_id = bddst.doc_spesa_tipo_id "
			+ "and btds2.dic_spesa_id = btds.dic_spesa_id " + "and btds2.fornitore_id = btbf.fornitore_id "
			+ "and bddss2.doc_spesa_stato_id = brdss2.doc_spesa_stato_id "
			+ "and bdbat.allegato_tipo_id = btba.allegato_tipo_id " + "and brdss2.doc_spesa_id = btds2.doc_spesa_id "
			+ "and brdsa.doc_spesa_det_id = btdsd.doc_spesa_det_id " + "and brdsa.allegato_id = btba.allegato_id "
			+ "and btbf.buono_id = btb.buono_id " + "and btds.periodo_id = btpr.periodo_id "
			+ "and btds.buono_id = btb.buono_id " + "and bddss.dic_spesa_stato_id = brdss.dic_spesa_stato_id "
			+ "and brdss.dic_spesa_id = btds.dic_spesa_id " + "and btd.domanda_numero = :numeroDomanda "
			+ "and bdds.domanda_stato_cod ='IN_PAGAMENTO' " + "and btb.domanda_det_id = btdd.domanda_det_id "
			+ "and btb.sportello_id = btdd.sportello_id " + "and btb.domanda_id = btd.domanda_id "
			+ "and btdd.validita_fine is null " + "and btd.data_cancellazione is null "
			+ "and btdd.data_cancellazione is null " + "and bdds.data_cancellazione is null "
			+ "and btpr.data_cancellazione is null " + "and btb.data_cancellazione is null "
			+ "and btds2.doc_spesa_id= :docSpesaId";

	public static final String ESISTE_DOCUMENTO_SPESA = "select distinct to_char(btds2.doc_spesa_periodo_inizio,'yyyy-mm') ||';'|| to_char(btds2.doc_spesa_periodo_fine,'yyyy-mm') periodo "
			+ "from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, "
			+ "bdom_t_buono btb,bdom_t_periodo_rendicontazione btpr, bdom_t_buono_allegato btba, bdom_d_buono_allegato_tipo bdbat, "
			+ "bdom_t_dichiarazione_spesa btds,bdom_d_dichiarazione_spesa_stato bddss,bdom_r_dichiarazione_spesa_stato brdss, "
			+ "bdom_t_documento_spesa btds2 , bdom_d_documento_spesa_tipo bddst,bdom_r_documento_spesa_stato brdss2,bdom_r_documento_spesa_dettaglio_allegato brdsa, "
			+ "bdom_t_documento_spesa_dettaglio btdsd ,bdom_d_documento_spesa_dettaglio_tipo bddsdt,bdom_t_buono_fornitore btbf,bdom_d_documento_spesa_stato bddss2 "
			+ "where btd.domanda_id = btdd.domanda_id "
			+ "and btdsd.doc_spesa_det_tipo_id = bddsdt.doc_spesa_det_tipo_id "
			+ "and btdsd.doc_spesa_id = btds2.doc_spesa_id " + "and btds2.doc_spesa_tipo_id = bddst.doc_spesa_tipo_id "
			+ "and btds2.dic_spesa_id = btds.dic_spesa_id " + "and btds2.fornitore_id = btbf.fornitore_id "
			+ "and bddss2.doc_spesa_stato_id = brdss2.doc_spesa_stato_id "
			+ "and bdbat.allegato_tipo_id = btba.allegato_tipo_id " + "and brdss2.doc_spesa_id = btds2.doc_spesa_id "
			+ "and brdsa.doc_spesa_det_id = btdsd.doc_spesa_det_id " + "and brdsa.allegato_id = btba.allegato_id "
			+ "and btbf.buono_id = btb.buono_id " + "and btds.periodo_id = btpr.periodo_id "
			+ "and btds.buono_id = btb.buono_id " + "and bddss.dic_spesa_stato_id = brdss.dic_spesa_stato_id "
			+ "and brdss.dic_spesa_id = btds.dic_spesa_id " + "and btd.domanda_numero = :numeroDomanda "
			+ "and bdds.domanda_stato_cod ='IN_PAGAMENTO' " + "and btb.domanda_det_id = btdd.domanda_det_id "
			+ "and btb.sportello_id = btdd.sportello_id " + "and btb.domanda_id = btd.domanda_id "
			+ "and btdd.validita_fine is null " + "and btd.data_cancellazione is null "
			+ "and btdd.data_cancellazione is null " + "and bdds.data_cancellazione is null "
			+ "and btb.data_cancellazione is null " + "and btpr.data_cancellazione is null "
			+ "and bddst.doc_spesa_tipo_cod= :docSpesaTipoCod";

	public static final String PERIODO_DOCUMENTO_SPESA_RENDICONTATO = "select distinct to_char(btds2.doc_spesa_periodo_inizio,'yyyy-mm') ||';'|| to_char(btds2.doc_spesa_periodo_fine,'yyyy-mm') periodo "
			+ "from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, "
			+ "bdom_t_buono btb,bdom_t_periodo_rendicontazione btpr, bdom_t_buono_allegato btba, bdom_d_buono_allegato_tipo bdbat, "
			+ "bdom_t_dichiarazione_spesa btds,bdom_d_dichiarazione_spesa_stato bddss,bdom_r_dichiarazione_spesa_stato brdss, "
			+ "bdom_t_documento_spesa btds2 , bdom_d_documento_spesa_tipo bddst,bdom_r_documento_spesa_stato brdss2,bdom_r_documento_spesa_dettaglio_allegato brdsa, "
			+ "bdom_t_documento_spesa_dettaglio btdsd ,bdom_d_documento_spesa_dettaglio_tipo bddsdt,bdom_t_buono_fornitore btbf,bdom_d_documento_spesa_stato bddss2 "
			+ "where btd.domanda_id = btdd.domanda_id "
			+ "and btdsd.doc_spesa_det_tipo_id = bddsdt.doc_spesa_det_tipo_id "
			+ "and btdsd.doc_spesa_id = btds2.doc_spesa_id " + "and btds2.doc_spesa_tipo_id = bddst.doc_spesa_tipo_id "
			+ "and btds2.dic_spesa_id = btds.dic_spesa_id " + "and btds2.fornitore_id = btbf.fornitore_id "
			+ "and bddss2.doc_spesa_stato_id = brdss2.doc_spesa_stato_id "
			+ "and bdbat.allegato_tipo_id = btba.allegato_tipo_id " + "and brdss2.doc_spesa_id = btds2.doc_spesa_id "
			+ "and brdsa.doc_spesa_det_id = btdsd.doc_spesa_det_id " + "and brdsa.allegato_id = btba.allegato_id "
			+ "and btbf.buono_id = btb.buono_id " + "and btds.periodo_id = btpr.periodo_id "
			+ "and btds.buono_id = btb.buono_id " + "and bddss.dic_spesa_stato_id = brdss.dic_spesa_stato_id "
			+ "and brdss.dic_spesa_id = btds.dic_spesa_id " + "and btd.domanda_numero = :numeroDomanda "
			+ "and bdds.domanda_stato_cod ='IN_PAGAMENTO' " + "and btb.domanda_det_id = btdd.domanda_det_id "
			+ "and btb.sportello_id = btdd.sportello_id " + "and btb.domanda_id = btd.domanda_id "
			+ "and btdd.validita_fine is null " + "and btd.data_cancellazione is null "
			+ "and btdd.data_cancellazione is null " + "and bdds.data_cancellazione is null "
			+ "and btpr.data_cancellazione is null " + "and btb.data_cancellazione is null";

	public static final String DATA_SPORTELLO = "select to_char((bts.validita_fine + interval '1 months'),'yyyy-mm-01') "
			+ "from bdom_t_domanda btd, bdom_t_sportello bts " + "where btd.domanda_numero  = :numeroDomanda "
			+ "and btd.data_cancellazione is null " + "and bts.data_cancellazione is null "
			+ "and bts.sportello_id = btd.sportello_id";

	public static final String SELECT_MESI_BANDO = "select distinct btgf.finanziamento_mesi from bdom_t_graduatoria_dettaglio btgd, bdom_t_graduatoria_finanziamento btgf, "
			+ "bdom_t_domanda btd " + "where btd.domanda_numero = :numeroDomanda "
			+ "and btd.domanda_id = btgd.domanda_id " + "and btgf.graduatoria_id = btgd.graduatoria_id "
			+ "and btgd.finanziamento_id = btgf.finanziamento_id " + "and btd.data_cancellazione is null "
			+ "and btgd.data_cancellazione is null " + "and btgf.data_cancellazione is null " + "and btgd.importo!=0";

	public static final String SELECT_PERIODO_RENDICONTAZIONE_APERTO = "select distinct to_char(periodo_inizio,'yyyy-mm-dd') ||';'|| to_char(periodo_fine,'yyyy-mm-dd') periodo "
			+ "from bdom_t_periodo_rendicontazione where "
			+ "now()::date BETWEEN periodo_inizio::date and COALESCE(periodo_fine::date, now()::date) and data_cancellazione is null";

	public static final String SELECT_MESI_SABBATICI = "select distinct to_char(btbms.mese_sabbatico_inizio,'yyyy-mm') "
			+ "meseSabbatico from bdom_t_buono_mese_sabbatico btbms, " + "bdom_t_buono btb, bdom_t_domanda btd "
			+ "where btb.buono_id = btbms.buono_id " + "and btb.domanda_id = btd.domanda_id "
			+ "and btd.domanda_numero = :numeroDomanda " + "and btd.data_cancellazione is null "
			+ "and now() between coalesce (btbms.validita_inizio, now()) " + "and coalesce(btbms.validita_fine,now()) "
			+ "and btbms.data_cancellazione is null " + "and btb.data_cancellazione is null";

	public static final String INSERT_MESI_SABBATICI = "INSERT INTO bdom_t_buono_mese_sabbatico (anno, mese, mese_sabbatico_inizio, mese_sabbatico_fine, "
			+ "buono_id, utente_creazione, utente_modifica) "
			+ "VALUES(:anno, :mese, :meseInizioSabbatico, :meseFineSabbatico, " + "(select distinct btb.buono_id from "
			+ "bdom_t_buono btb, bdom_t_domanda btd " + "where btb.domanda_id = btd.domanda_id "
			+ "and btd.domanda_numero = :numeroDomanda " + "and btd.data_cancellazione is null "
			+ "and btb.data_cancellazione is null), " + ":utente_creazione, :utente_modifica)";

	public static final String DELETE_MESI_SABBATICI = "delete from bdom_t_buono_mese_sabbatico " + "where buono_id in "
			+ "(select distinct btb.buono_id from " + "bdom_t_buono btb, bdom_t_domanda btd "
			+ "where btb.domanda_id = btd.domanda_id " + "and btd.domanda_numero = :numeroDomanda "
			+ "and btd.data_cancellazione is null " + "and btb.data_cancellazione is null)";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<Integer> insertDichiarazioneSpesa(String numeroDomanda, String cfinserisci, String cfmodifica)
			throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		List<Integer> esistedichiarazione = esisteDichiarazioneSpesa(numeroDomanda, Constants.CARICATA);
		if (esistedichiarazione.size() == 0) {
			jdbcTemplate.update(INSERT_DICHIARAZIONE_SPESA, params, keyHolder, new String[] { "dic_spesa_id" });
			int chiave = keyHolder.getKey().intValue();
			List<Integer> listkey = new ArrayList<Integer>();
			if (chiave != 0)
				insertRDichiarazioneSpesaStato(chiave, Constants.CARICATA, cfinserisci, cfmodifica);
			listkey.add(chiave);
			return listkey;
		}
		return esistedichiarazione;
	}

	public int insertRDichiarazioneSpesaStato(int dicSpesaId, String dicSpesaStatoCod, String cfinserisci,
			String cfmodifica) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("dicSpesaId", dicSpesaId, Types.BIGINT);
		params.addValue("dicSpesaStatoCod", dicSpesaStatoCod, Types.VARCHAR);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		jdbcTemplate.update(INSERT_R_DICHIARAZIONE_SPESA, params, keyHolder, new String[] { "r_dic_spesa_stato_id" });
		return keyHolder.getKey().intValue();
	}

	public int insertDocumentoSpesa(List<String> mesi, String docSpesaTipoCod, int dicSpesaId, Integer fornitoreId,
			String cfinserisci, String cfmodifica, String docNumero, BigDecimal importoTotalePagato,
			BigDecimal importoQuietanzato, BigDecimal domandaId, String docSpesaStatoNote, String statoSpesa)
			throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		ArrayList<Date> periodo = new ArrayList<Date>();
		periodo = calcoloPeriodoSpesa(mesi);
		params.addValue("docSpesaPeriodoInizio", periodo.get(0), Types.DATE);
		params.addValue("docSpesaPeriodoFine", periodo.get(1), Types.DATE);
		params.addValue("dicSpesaId", dicSpesaId, Types.BIGINT);
		params.addValue("fornitoreId", fornitoreId, Types.BIGINT);
		params.addValue("docSpesaTipoCod", docSpesaTipoCod, Types.VARCHAR);
		params.addValue("docNumero", docNumero, Types.VARCHAR);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		jdbcTemplate.update(INSERT_DOCUMENTO_SPESA, params, keyHolder, new String[] { "doc_spesa_id" });
		int chiave = keyHolder.getKey().intValue();
		if (chiave != 0)
			insertRDocumentoSpesaStato(chiave, statoSpesa, cfinserisci, cfmodifica, importoTotalePagato,
					importoQuietanzato, domandaId, docSpesaStatoNote);
		return chiave;
	}

	public int insertRDocumentoSpesaStato(int docSpesaId, String docSpesaStatoCod, String cfinserisci,
			String cfmodifica, BigDecimal importoTotalePagato, BigDecimal importoQuietanzato, BigDecimal domandaId,
			String docSpesaStatoNote) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("docSpesaId", docSpesaId, Types.BIGINT);
		params.addValue("docSpesaStatoCod", docSpesaStatoCod, Types.VARCHAR);
		params.addValue("docSpesaStatoNote", docSpesaStatoNote, Types.VARCHAR);
		params.addValue("importoTotalePagato", importoTotalePagato, Types.NUMERIC);
		params.addValue("importoQuietanzato", importoQuietanzato, Types.NUMERIC);
		params.addValue("domandaId", domandaId, Types.BIGINT);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		jdbcTemplate.update(INSERT_R_DOCUMENTO_SPESA_STATO, params, keyHolder, new String[] { "r_doc_spesa_stato_id" });
		return keyHolder.getKey().intValue();
	}

	public int insertDocumentoSpesaDettaglio(String docSpesaDetTipoCod, int docSpesaId, String cfinserisci,
			String cfmodifica, BigDecimal docSpesaDetImporto, Date docSpesaDetData, Integer allegatoId)
			throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("docSpesaDetData", docSpesaDetData, Types.DATE);
		params.addValue("docSpesaId", docSpesaId, Types.BIGINT);
		params.addValue("docSpesaDetTipoCod", docSpesaDetTipoCod, Types.VARCHAR);
		params.addValue("docSpesaDetImporto", docSpesaDetImporto, Types.NUMERIC);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		jdbcTemplate.update(INSERT_DOCUMENTO_SPESA_DETTAGLIO, params, keyHolder, new String[] { "doc_spesa_det_id" });
		int chiave = keyHolder.getKey().intValue();
		if (chiave != 0)
			insertRDocumentoSpesaDettaglioAllegato(chiave, allegatoId, cfinserisci, cfmodifica);
		return chiave;
	}

	public int insertRDocumentoSpesaDettaglioAllegato(int docSpesaDetId, Integer allegatoId, String cfinserisci,
			String cfmodifica) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("docSpesaDetId", docSpesaDetId, Types.BIGINT);
		params.addValue("allegatoId", allegatoId, Types.BIGINT);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		jdbcTemplate.update(INSERT_R_DOCUMENTO_SPESA_ALLEGATO, params, keyHolder,
				new String[] { "doc_spesa_det_allegato_id" });
		return keyHolder.getKey().intValue();
	}

	private ArrayList<Date> calcoloPeriodoSpesa(List<String> mesi) {
		ArrayList<Date> periodo = new ArrayList<Date>();
		try {
			if (mesi.size() > 0) {
				String[] dataperiodo = new String[mesi.size()];
				for (int index = 0; index < mesi.size(); index++) {
					if (Converter.getInt(mesi.get(index).replace("-", "")) > 0) {
						dataperiodo[index] = mesi.get(index);
					}
				}
				if (dataperiodo.length > 0) {
					Arrays.sort(dataperiodo);
					// calcola il numero di giorni per mese
					// un solo mese devo mettere periodo inizio e fine del mese
					YearMonth yearMonthObject = YearMonth.of(
							Converter.getInt(dataperiodo[dataperiodo.length - 1].substring(0, 4)),
							Converter.getInt(dataperiodo[dataperiodo.length - 1].substring(5)));
					int daysInMonth = yearMonthObject.lengthOfMonth();
					periodo.add(Converter.getDataWithoutTime(dataperiodo[0] + "-01"));
					periodo.add(Converter.getDataWithoutTime(dataperiodo[dataperiodo.length - 1] + "-" + daysInMonth));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return periodo;
	}

	public List<ModelDocumentoSpesa> selectDocumentoSpesa(String numeroDomanda, List<String> mesiSabbatici)
			throws DatabaseException {
		List<ModelDocumentoSpesa> spesa = new ArrayList<ModelDocumentoSpesa>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			spesa = jdbcTemplate.query(GET_DOCUMENTO_SPESA, namedParameters, new DocumentoSpesaMapper());
			for (ModelDocumentoSpesa spesasingola : spesa) {
				if (mesiSabbatici != null) {
					Set<String> mesifinali = new HashSet<String>();
					for (String mesi : spesasingola.getMesi()) {
						boolean trovato = false;
						for (String mesisab : mesiSabbatici) {
							if (mesi.equalsIgnoreCase(mesisab)) {
								trovato = true;
								break;
							}
						}
						if (!trovato)
							mesifinali.add(mesi);
					}
					ArrayList arrayList = new ArrayList(mesifinali);
					Collections.sort(arrayList);
					spesasingola.setMesi(arrayList);
				}
				spesasingola.setDettagli(selectDocumentoSpesaDettaglio(numeroDomanda, spesasingola.getId()));
			}
			return spesa;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "GET_DOCUMENTO_SPESA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelDocumentoSpesaDettaglio> selectDocumentoSpesaDettaglio(String numeroDomanda, int docSpesaId)
			throws DatabaseException {
		List<ModelDocumentoSpesaDettaglio> spesadettaglio = new ArrayList<ModelDocumentoSpesaDettaglio>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda)
				.addValue("docSpesaId", docSpesaId);
		try {
			spesadettaglio = jdbcTemplate.query(GET_DOCUMENTO_SPESA_DETTAGLIO, namedParameters,
					new DocumentoSpesaDettaglioMapper());
			return spesadettaglio;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "GET_DOCUMENTO_SPESA_DETTAGLIO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public boolean esisteDocumentoSpesa(String numeroDomanda, String docSpesaTipoCod, List<String> mesi)
			throws DatabaseException {
		try {
			List<String> periodostringa = new ArrayList<String>();
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda)
					.addValue("docSpesaTipoCod", docSpesaTipoCod);

			periodostringa = jdbcTemplate.queryForList(ESISTE_DOCUMENTO_SPESA, namedParameters, String.class);
			for (String valore : periodostringa) {
				for (String mesilista : mesi) {
					if (valore.contains(mesilista)) {
						return true;
					}
				}
			}

		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "ESISTE_DOCUMENTO_SPESA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
		return false;
	}

	public String documentoSpesaRendicontato(String numeroDomanda, List<String> mesi) throws DatabaseException {
		try {
			List<String> periodostringa = new ArrayList<String>();
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);

			periodostringa = jdbcTemplate.queryForList(PERIODO_DOCUMENTO_SPESA_RENDICONTATO, namedParameters,
					String.class);
			for (String valore : periodostringa) {
				for (String mesilista : mesi) {
					if (valore.contains(mesilista)) {
						return mesilista;
					}
				}
			}

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "PERIODO_DOCUMENTO_SPESA_RENDICONTATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
		return null;
	}

	public boolean esisteDocumentoSpesaPerId(int docSpesaId) throws DatabaseException {
		try {
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("docSpesaId", docSpesaId);

			Integer valore = jdbcTemplate.queryForObject(GET_DOCUMENTI_SPESA_STATO, namedParameters, Integer.class);

			return valore.intValue() > 0;

		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "GET_DOCUMENTI_SPESA_STATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<Integer> esisteDichiarazioneSpesa(String numeroDomanda, String dicSpesaStatoCod)
			throws DatabaseException {
		try {
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda)
					.addValue("dicSpesaStatoCod", dicSpesaStatoCod);

			List<Integer> valore = jdbcTemplate.queryForList(ESISTE_DICHIARAZIONE_SPESA, namedParameters,
					Integer.class);

			return valore;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "ESISTE_DICHIARAZIONE_SPESA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<Integer> contaNumeroDocumentiSpesaPerDichiarazioneSpesa(String numeroDomanda, String dicSpesaStatoCod)
			throws DatabaseException {
		try {
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda)
					.addValue("dicSpesaStatoCod", dicSpesaStatoCod);

			List<Integer> valore = jdbcTemplate.queryForList(GET_DOCUMENTI_SPESA_PER_DICHIARAZIONE, namedParameters,
					Integer.class);

			return valore;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "GET_DOCUMENTI_SPESA_PER_DICHIARAZIONE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelDocumentoSpesaDettaglio> eliminaDocumentoDiSpesa(String numeroDomanda, int docSpesaId)
			throws DatabaseException {
		List<ModelDocumentoSpesaDettaglio> dettagli = new ArrayList<ModelDocumentoSpesaDettaglio>();
		try {
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("docSpesaId", docSpesaId);
			dettagli = selectDocumentoSpesaDettaglio(numeroDomanda, docSpesaId);
			// veririca se devo cancellare la dichiarazione
			List<Integer> numerodoc = contaNumeroDocumentiSpesaPerDichiarazioneSpesa(numeroDomanda, Constants.CARICATA);
			long esito = jdbcTemplate.update(DELETE_DOCUMENTO_SPESA, namedParameters);
			if (esito > 0) {
				// se 1 posso cancellare altrimenti non posso
				if (numerodoc != null && numerodoc.size() == 1) {
					namedParameters = new MapSqlParameterSource().addValue("dicSpesaId", numerodoc.get(0));
					jdbcTemplate.update(DELETE_DICHIARAZIONE_SPESA, namedParameters);
					return dettagli;
				}
			}
		} catch (Exception e) {
			String methodName = "DELETE_DOCUMENTO_SPESA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
		return dettagli;
	}

	public int eliminaAllegatoSpesa(int allegatoId) throws DatabaseException {

		try {
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("allegatoId", allegatoId);
			return jdbcTemplate.update(DELETE_ALLEGATO_DOCUMENTO_SPESA, namedParameters);
		} catch (Exception e) {
			String methodName = "DELETE_DOCUMENTO_SPESA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String selectDataSportelloChiuso(String numeroRichiesta) throws DatabaseException {
		String dettaglio = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			dettaglio = jdbcTemplate.queryForObject(DATA_SPORTELLO, namedParameters, String.class);
			return dettaglio;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "DATA_SPORTELLO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String[] selectPeriodoRendicontazioneAperto() throws DatabaseException {
		String[] periodo = new String[2];
		try {
			SqlParameterSource namedParameters = new MapSqlParameterSource();
			String valori = jdbcTemplate.queryForObject(SELECT_PERIODO_RENDICONTAZIONE_APERTO, namedParameters,
					String.class);
			periodo = valori.split(";");
			return periodo;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_PERIODO_RENDICONTAZIONE_APERTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int selectMesiBando(String numeroDomanda) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			return jdbcTemplate.queryForObject(SELECT_MESI_BANDO, namedParameters, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			String methodName = "SELECT_MESI_BANDO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<String> selectMesiSabbatici(String numeroDomanda) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			return jdbcTemplate.queryForList(SELECT_MESI_SABBATICI, namedParameters, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_MESI_SABBATICI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public int insertMesiSabbatici(String numeroDomanda, int anno, int mese, Date meseInizioSabbatico,
			Date meseFineSabbatico, String cfinserisci, String cfmodifica) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("numeroDomanda", numeroDomanda, Types.VARCHAR);
		params.addValue("anno", anno, Types.BIGINT);
		params.addValue("mese", mese, Types.BIGINT);
		params.addValue("meseInizioSabbatico", meseInizioSabbatico, Types.DATE);
		params.addValue("meseFineSabbatico", meseFineSabbatico, Types.DATE);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		jdbcTemplate.update(INSERT_MESI_SABBATICI, params, keyHolder, new String[] { "mese_sabbatico_id" });
		return keyHolder.getKey().intValue();
	}

	public int eliminaMesiSabbatici(String numeroDomanda) throws DatabaseException {

		try {
			SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
			return jdbcTemplate.update(DELETE_MESI_SABBATICI, namedParameters);
		} catch (Exception e) {
			String methodName = "DELETE_MESI_SABBATICI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}

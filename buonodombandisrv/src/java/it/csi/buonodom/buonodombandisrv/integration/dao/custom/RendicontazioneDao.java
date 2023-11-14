/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodombandisrv.dto.ModelAllegatoContratto;
import it.csi.buonodom.buonodombandisrv.dto.ModelDocumento;
import it.csi.buonodom.buonodombandisrv.dto.ModelDocumentoAllegato;
import it.csi.buonodom.buonodombandisrv.dto.ModelDocumentoSpesa;
import it.csi.buonodom.buonodombandisrv.dto.ModelDocumentoSpesaDettaglio;
import it.csi.buonodom.buonodombandisrv.dto.ModelInvioDocumentoSpesa;
import it.csi.buonodom.buonodombandisrv.dto.ModelInvioFornitore;
import it.csi.buonodom.buonodombandisrv.dto.ModelSetFornitore;
import it.csi.buonodom.buonodombandisrv.exception.DatabaseException;
import it.csi.buonodom.buonodombandisrv.integration.dao.utils.DocumentoPathAllegatoMapper;
import it.csi.buonodom.buonodombandisrv.integration.dao.utils.DocumentoPathMapper;
import it.csi.buonodom.buonodombandisrv.integration.dao.utils.DocumentoSpesaDettaglioMapper;
import it.csi.buonodom.buonodombandisrv.integration.dao.utils.DocumentoSpesaMapper;
import it.csi.buonodom.buonodombandisrv.integration.dao.utils.FornitoreMapper;
import it.csi.buonodom.buonodombandisrv.integration.dao.utils.InvioDocumentoSpesaDettaglioMapper;
import it.csi.buonodom.buonodombandisrv.integration.dao.utils.SetFornitoreMapper;
import it.csi.buonodom.buonodombandisrv.util.Converter;
import it.csi.buonodom.buonodombandisrv.util.LoggerUtil;
import it.csi.buonodom.buonodombandisrv.util.Util;

@Repository
public class RendicontazioneDao extends LoggerUtil {

	public static final String INSERT_R_DOCUMENTO_SPESA_STATO = "INSERT INTO bdom_r_documento_spesa_stato "
			+ "(doc_spesa_id, doc_spesa_stato_id, importo_totale_pagato, importo_quietanzato, importo_rendicontato,doc_spesa_stato_note, "
			+ "utente_creazione, utente_modifica) " + " VALUES(:docSpesaId, "
			+ "(select doc_spesa_stato_id from bdom_d_documento_spesa_stato where "
			+ "validita_fine is null and data_cancellazione is null and doc_spesa_stato_cod = :docSpesaStatoCod), "
			+ ":importoTotalePagato, :importoQuietanzato, (select distinct btgf.finanziamento_importo_mensile from bdom_t_graduatoria_dettaglio btgd, "
			+ "bdom_t_graduatoria_finanziamento btgf " + "where btgd.domanda_id = :domandaId "
			+ "and btgf.graduatoria_id = btgd.graduatoria_id "
			+ "and btgf.finanziamento_id = btgd.finanziamento_id and btgd.importo!=0), :docSpesaStatoNote, "
			+ ":utente_creazione, :utente_modifica)";

	public static final String GET_DOCUMENTO_SPESA = "select distinct btds2.doc_spesa_id,bddst.doc_spesa_tipo_cod , btds2.doc_numero ,btds2.fornitore_id, "
			+ "bddss2.doc_spesa_stato_cod,brdss2.doc_spesa_stato_note,to_char(btds2.doc_spesa_periodo_inizio,'yyyy-mm') doc_spesa_periodo_inizio, "
			+ "to_char(btds2.doc_spesa_periodo_fine,'yyyy-mm') doc_spesa_periodo_fine, btds.dic_spesa_cod,btbf.fornitore_cf "
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
			+ "and btdd.validita_fine is null " + "and bddss2.doc_spesa_stato_cod = :docSpesaStatoCod "
			+ "and btd.data_cancellazione is null " + "and btdd.data_cancellazione is null "
			+ "and bdds.data_cancellazione is null " + "and btpr.data_cancellazione is null "
			+ "and brdss.validita_fine is null " + "and brdss2.validita_fine is null "
			+ "and btb.data_cancellazione is null " + "and btpr.validita_fine is not null "
			+ "and btpr.validita_fine::date = (select max(validita_fine::date) "
			+ "from bdom_t_periodo_rendicontazione where data_cancellazione is null and validita_fine is not null)";

	public static final String GET_DOCUMENTO_SPESA_FORNITORI = "select distinct to_char(btds2.fornitore_id) || ';' || to_char(btc.contratto_id) fornContr "
			+ "from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, "
			+ "bdom_t_buono btb,bdom_t_periodo_rendicontazione btpr, bdom_t_buono_allegato btba, bdom_d_buono_allegato_tipo bdbat, "
			+ "bdom_t_dichiarazione_spesa btds,bdom_d_dichiarazione_spesa_stato bddss,bdom_r_dichiarazione_spesa_stato brdss, "
			+ "bdom_t_documento_spesa btds2 , bdom_d_documento_spesa_tipo bddst,bdom_r_documento_spesa_stato brdss2,bdom_r_documento_spesa_dettaglio_allegato brdsa, "
			+ "bdom_t_documento_spesa_dettaglio btdsd ,bdom_d_documento_spesa_dettaglio_tipo bddsdt,bdom_t_buono_fornitore btbf,bdom_d_documento_spesa_stato bddss2, bdom_t_contratto btc "
			+ "where btd.domanda_id = btdd.domanda_id " + "and btc.fornitore_id = btds2.fornitore_id "
			+ "and btc.buono_id = btb.buono_id " + "and btbf.buono_id = btc.buono_id "
			+ "and btds.buono_id = btc.buono_id " + "and btc.data_cancellazione is null "
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
			+ "and btdd.validita_fine is null " + "and bddss2.doc_spesa_stato_cod = :docSpesaStatoCod "
			+ "and btd.data_cancellazione is null " + "and btdd.data_cancellazione is null "
			+ "and bdds.data_cancellazione is null " + "and brdss.validita_fine is null "
			+ "and brdss2.validita_fine is null " + "and btpr.data_cancellazione is null "
			+ "and btb.data_cancellazione is null " + "and btpr.validita_fine is not null "
			+ "and btpr.validita_fine::date = (select max(validita_fine::date) "
			+ "from bdom_t_periodo_rendicontazione where data_cancellazione is null and validita_fine is not null)";

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
			+ "and btdd.validita_fine is null " + "and bddss2.doc_spesa_stato_cod = :docSpesaStatoCod "
			+ "and btd.data_cancellazione is null " + "and btdd.data_cancellazione is null "
			+ "and bdds.data_cancellazione is null " + "and btb.data_cancellazione is null "
			+ "and btpr.data_cancellazione is null " + "and brdss.validita_fine is null "
			+ "and brdss2.validita_fine is null " + "and btds2.doc_spesa_id= :docSpesaId "
			+ "and btpr.validita_fine is not null " + "and btpr.validita_fine::date = (select max(validita_fine::date) "
			+ "from bdom_t_periodo_rendicontazione where data_cancellazione is null and validita_fine is not null)";

	public static final String SELECT_DET_COD_ALLEGATO = "select count(*) from bdom_t_buono_allegato a, bdom_d_buono_allegato_tipo b "
			+ "where domanda_det_cod=:detcod " + "and b.allegato_tipo_cod =:tipodom "
			+ "and a.allegato_tipo_id = b.allegato_tipo_id";

	public static final String SELECT_BUONO_COD_FROM_NUMERO_DOMANDA = "select distinct btb.buono_cod from bdom_t_buono btb, bdom_r_buono_stato brbs,bdom_d_buono_stato bdbs , "
			+ "bdom_t_domanda_dettaglio btdd, bdom_t_domanda btd "
			+ "where btb.domanda_det_id = btdd.domanda_det_id and btb.sportello_id = btdd.sportello_id "
			+ "and btb.domanda_id = btdd.domanda_id " + "and btb.data_cancellazione is null "
			+ "and btd.data_cancellazione is null " + "and btdd.domanda_id = btd.domanda_id "
			+ "and btdd.data_cancellazione is null " + "and btdd.validita_inizio <= now() "
			+ "and btdd.validita_fine is null " + "and bdbs.data_cancellazione is null "
			+ "and bdbs.validita_fine is null " + "and bdbs.validita_inizio <= now() "
			+ "and bdbs.buono_stato_id = brbs.buono_stato_id " + "and bdbs.buono_stato_cod !='REVOCATO' "
			+ "and brbs.buono_id = btb.buono_id " + "and brbs.validita_inizio  <= now() "
			+ "and brbs.validita_fine is null " + "and brbs.data_cancellazione is null "
			+ "and btd.domanda_numero= :numeroDomanda";

	public static final String UPDATE_CONTRATTO_ALLEGATO = "update bdom_t_buono_allegato set  "
			+ "	file_name =:nomefile, file_path =:pathfile, data_modifica = now(), "
			+ "	utente_modifica = :cfmodifica  where allegato_id = :allegatoId;";

	public static final String INSERT_CONTRATTO_ALLEGATO = "insert into bdom_t_buono_allegato   "
			+ "(file_name,	file_type, 	file_path, 	allegato_tipo_id, " + "	utente_creazione, utente_modifica) "
			+ "values(:file_name,:file_type, "
			+ ":file_path, (select bdbat.allegato_tipo_id from bdom_d_buono_allegato_tipo bdbat "
			+ "	where bdbat.allegato_tipo_cod = :allegatoTipo and bdbat.data_cancellazione is null), "
			+ ":utente_creazione,:utente_modifica);";

	public static final String CHECK_ESISTENZA_TIPO_ALLEGATO = "select count(bdbat.allegato_tipo_id)  "
			+ "from bdom_d_buono_allegato_tipo bdbat " + "where bdbat.allegato_tipo_cod = :allegatoTipo "
			+ "and bdbat.data_cancellazione is null";

	public static final String INSERT_R_DICHIARAZIONE_SPESA_ALLEGATO = "INSERT INTO bdom_r_dichiarazione_spesa_allegato ( "
			+ "    dic_spesa_id,  allegato_id,  utente_creazione, "
			+ "    utente_modifica ) VALUES ( :dic_spesa_id,  :allegato_id, "
			+ "    :utente_creazione,  :utente_modifica ); ";

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
			+ "and btpr.data_cancellazione is null " + "and btpr.validita_fine is not null "
			+ "and btpr.validita_fine::date = (select max(validita_fine::date) "
			+ "from bdom_t_periodo_rendicontazione where data_cancellazione is null and validita_fine is not null) "
			+ "and brdss.validita_fine is null";

	public static final String UPDATE_R_DICHIARAZIONE_SPESA = "update bdom_r_dichiarazione_spesa_stato set "
			+ "validita_fine = now(), utente_modifica = :utente_modifica "
			+ "where dic_spesa_id = :dicSpesaId and validita_fine is null";

	public static final String INSERT_R_DICHIARAZIONE_SPESA = "INSERT INTO bdom_r_dichiarazione_spesa_stato "
			+ "(dic_spesa_id, dic_spesa_stato_id,  " + "utente_creazione, utente_modifica) " + " VALUES(:dicSpesaId,"
			+ "(select dic_spesa_stato_id from bdom_d_dichiarazione_spesa_stato where "
			+ "validita_fine is null and data_cancellazione is null and dic_spesa_stato_cod = :dicSpesaStatoCod), "
			+ ":utente_creazione, :utente_modifica)";

	public static final String SELECT_MESI_SABBATICI = "select distinct to_char(btbms.mese_sabbatico_inizio,'yyyy-mm') "
			+ "meseSabbatico from bdom_t_buono_mese_sabbatico btbms, " + "bdom_t_buono btb, bdom_t_domanda btd "
			+ "where btb.buono_id = btbms.buono_id " + "and btb.domanda_id = btd.domanda_id "
			+ "and btd.domanda_numero = :numeroDomanda " + "and btd.data_cancellazione is null "
			+ "and now() between coalesce (btbms.validita_inizio, now()) " + "and coalesce(btbms.validita_fine,now()) "
			+ "and btbms.data_cancellazione is null " + "and btb.data_cancellazione is null";

	public static final String SELECT_FORNITORE = "select distinct fornitore_nome ,fornitore_cognome ,fornitore_denominazione ,fornitore_cf, "
			+ "to_char(b.contratto_data_inizio,'yyyy-mm-dd') contratto_data_inizio,to_char(b.contratto_data_fine,'yyyy-mm-dd') contratto_data_fine "
			+ "from bdom_t_buono_fornitore a,bdom_t_contratto b " + "where a.fornitore_id = :fornitoreId "
			+ "and b.contratto_id = :contrattoId " + "and b.fornitore_id = a.fornitore_id";

	public static final String SELECT_DETTAGLIO_SPESA = "select distinct "
			+ "a.doc_numero,replace(trim(to_char(ROUND(f.importo_totale_pagato, 2 ),'999999999.00')),'.',',') importo_totale_pagato, "
			+ "replace(trim(to_char(ROUND(f.importo_quietanzato, 2 ),'999999999.00')),'.',',') importo_quietanzato, "
			+ "h.doc_spesa_tipo_cod, a.doc_spesa_cod "
			+ "from bdom_t_documento_spesa a,bdom_t_documento_spesa_dettaglio b, bdom_r_documento_spesa_dettaglio_allegato c,bdom_t_buono_allegato d, "
			+ "bdom_d_buono_allegato_tipo e,bdom_r_documento_spesa_stato f,bdom_d_documento_spesa_stato g,bdom_d_documento_spesa_tipo h "
			+ "where a.doc_spesa_id  = :docSpesaId " + "and h.doc_spesa_tipo_id = a.doc_spesa_tipo_id "
			+ "and f.doc_spesa_id = a.doc_spesa_id " + "and g.doc_spesa_stato_id = f.doc_spesa_stato_id "
			+ "and a.doc_spesa_id = b.doc_spesa_id " + "and c.doc_spesa_det_id = b.doc_spesa_det_id "
			+ "and d.allegato_id = c.allegato_id " + "and e.allegato_tipo_id = d.allegato_tipo_id ";

	public static final String SELECT_DETTAGLIO_SPESA_ALLEGATO = "select distinct "
			+ "d.file_name,e.allegato_tipo_cod,d.file_path "
			+ "from bdom_t_documento_spesa a,bdom_t_documento_spesa_dettaglio b, bdom_r_documento_spesa_dettaglio_allegato c,bdom_t_buono_allegato d, "
			+ "bdom_d_buono_allegato_tipo e,bdom_r_documento_spesa_stato f,bdom_d_documento_spesa_stato g,bdom_d_documento_spesa_tipo h "
			+ "where a.doc_spesa_id  = :docSpesaId " + "and h.doc_spesa_tipo_id = a.doc_spesa_tipo_id "
			+ "and f.doc_spesa_id = a.doc_spesa_id " + "and g.doc_spesa_stato_id = f.doc_spesa_stato_id "
			+ "and a.doc_spesa_id = b.doc_spesa_id " + "and c.doc_spesa_det_id = b.doc_spesa_det_id "
			+ "and d.allegato_id = c.allegato_id " + "and e.allegato_tipo_id = d.allegato_tipo_id ";

	public static final String SELECT_DATA_DOCUMENTO_SPESA = "select distinct to_char(b.doc_spesa_det_data,'yyyy-mm-dd') doc_spesa_det_data "
			+ "from bdom_t_documento_spesa a,bdom_t_documento_spesa_dettaglio b, bdom_r_documento_spesa_dettaglio_allegato c,bdom_t_buono_allegato d, "
			+ "bdom_d_buono_allegato_tipo e,bdom_r_documento_spesa_stato f,bdom_d_documento_spesa_stato g,bdom_d_documento_spesa_tipo h "
			+ "where a.doc_spesa_id  = :docSpesaId " + "and h.doc_spesa_tipo_id = a.doc_spesa_tipo_id "
			+ "and f.doc_spesa_id = a.doc_spesa_id " + "and g.doc_spesa_stato_id = f.doc_spesa_stato_id "
			+ "and a.doc_spesa_id = b.doc_spesa_id " + "and c.doc_spesa_det_id = b.doc_spesa_det_id "
			+ "and d.allegato_id = c.allegato_id " + "and e.allegato_tipo_cod ='GIUSTIFICATIVO' "
			+ "and e.allegato_tipo_id = d.allegato_tipo_id ";

	public static final String SELECT_IMPORTO_MENSILE = "select distinct replace(trim(to_char(btgf.finanziamento_importo_mensile,'999999999.00')),'.',',') finanziamento_importo_mensile "
			+ "from bdom_t_graduatoria_dettaglio btgd, " + "bdom_t_graduatoria_finanziamento btgf "
			+ "where btgd.domanda_id = :domandaId " + "and btgf.graduatoria_id = btgd.graduatoria_id "
			+ "and btgf.finanziamento_id = btgd.finanziamento_id and btgd.importo!=0";

	public static final String SELECT_SETFORNITORE = "select distinct fornitore_cf, "
			+ "case when bdct.contratto_tipo_cod='ASSISTENTE_FAMILIARE' or bdct.contratto_tipo_cod='PARTITA_IVA' then "
			+ "fornitore_nome " + "else null " + "end fornitore_nome, "
			+ "case when bdct.contratto_tipo_cod='ASSISTENTE_FAMILIARE' or bdct.contratto_tipo_cod='PARTITA_IVA' then "
			+ "fornitore_cognome " + "else null " + "end fornitore_cognome, "
			+ "case when bdct.contratto_tipo_cod='ASSISTENTE_FAMILIARE' then " + "null " + "else 'N.D.' "
			+ "end fornitore_denominazione, "
			+ "case when bdct.contratto_tipo_cod='PARTITA_IVA' and length(trim(b.fornitore_piva))<=11 then "
			+ "b.fornitore_piva " + "else null " + "end fornitore_piva, "
			+ "to_char(b.contratto_data_inizio,'yyyy-mm-dd') contratto_data_inizio,to_char(b.contratto_data_fine,'yyyy-mm-dd') contratto_data_fine "
			+ "from bdom_t_buono_fornitore a,bdom_t_contratto b,bdom_d_contratto_tipo bdct "
			+ "where b.fornitore_id = :fornitoreId " + "and b.fornitore_id = a.fornitore_id "
			+ "and b.contratto_id = :contrattoId " + "and b.contratto_tipo_id = bdct.contratto_tipo_id";

	public static final String SELECT_DETTAGLIO_SPESA_ALLEGATO_ID = "select distinct "
			+ "d.file_name,e.allegato_tipo_cod,d.file_path,d.allegato_id "
			+ "from bdom_t_documento_spesa a,bdom_t_documento_spesa_dettaglio b, bdom_r_documento_spesa_dettaglio_allegato c,bdom_t_buono_allegato d, "
			+ "bdom_d_buono_allegato_tipo e,bdom_r_documento_spesa_stato f,bdom_d_documento_spesa_stato g,bdom_d_documento_spesa_tipo h "
			+ "where a.doc_spesa_id  = :docSpesaId " + "and h.doc_spesa_tipo_id = a.doc_spesa_tipo_id "
			+ "and f.doc_spesa_id = a.doc_spesa_id " + "and g.doc_spesa_stato_id = f.doc_spesa_stato_id "
			+ "and a.doc_spesa_id = b.doc_spesa_id " + "and c.doc_spesa_det_id = b.doc_spesa_det_id "
			+ "and d.allegato_id = c.allegato_id " + "and e.allegato_tipo_id = d.allegato_tipo_id ";

	public static final String SELECT_ALLEGATI_CONTRATTO_ID = "select distinct "
			+ "d.file_name,e.allegato_tipo_cod,d.file_path, d.allegato_id "
			+ "from bdom_t_buono_fornitore a,bdom_t_contratto b,bdom_r_contratto_allegato c,bdom_t_buono_allegato d, "
			+ "bdom_d_buono_allegato_tipo e " + "where a.fornitore_id = :fornitoreId "
			+ "and b.fornitore_id = a.fornitore_id " + "and c.contratto_id = b.contratto_id "
			+ "and b.contratto_id = :contrattoId " + "and d.allegato_id = c.allegato_id "
			+ "and e.allegato_tipo_id = d.allegato_tipo_id";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

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

	public List<ModelDocumentoSpesa> selectDocumentoSpesa(String numeroDomanda, String docSpesaStatoCod,
			List<String> mesiSabbatici) throws DatabaseException {
		List<ModelDocumentoSpesa> spesa = new ArrayList<ModelDocumentoSpesa>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda)
				.addValue("docSpesaStatoCod", docSpesaStatoCod);
		try {
			spesa = jdbcTemplate.query(GET_DOCUMENTO_SPESA, namedParameters, new DocumentoSpesaMapper());
			for (ModelDocumentoSpesa spesasingola : spesa) {
				if (mesiSabbatici != null) {
					List<String> mesifinali = new ArrayList<String>();
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
					spesasingola.setMesi(mesifinali);
				}
				spesasingola.setDettagli(
						selectDocumentoSpesaDettaglio(numeroDomanda, spesasingola.getId(), docSpesaStatoCod));
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

	public List<String> selectDocumentoSpesaFornitori(String numeroDomanda, String docSpesaStatoCod)
			throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda)
				.addValue("docSpesaStatoCod", docSpesaStatoCod);
		try {
			List<String> fornitori = jdbcTemplate.queryForList(GET_DOCUMENTO_SPESA_FORNITORI, namedParameters,
					String.class);
			return fornitori;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "GET_DOCUMENTO_SPESA_FORNITORI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelDocumentoSpesaDettaglio> selectDocumentoSpesaDettaglio(String numeroDomanda, int docSpesaId,
			String docSpesaStatoCod) throws DatabaseException {
		List<ModelDocumentoSpesaDettaglio> spesadettaglio = new ArrayList<ModelDocumentoSpesaDettaglio>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda)
				.addValue("docSpesaId", docSpesaId).addValue("docSpesaStatoCod", docSpesaStatoCod);
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

	public boolean selectEsisteAllegato(String detcod, String tipodom) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("detcod", detcod).addValue("tipodom",
				tipodom);
		try {
			Integer numallegati = jdbcTemplate.queryForObject(SELECT_DET_COD_ALLEGATO, namedParameters, Integer.class);
			if (numallegati.intValue() > 0)
				return true;
			else
				return false;
		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "SELECT_DET_COD_ALLEGATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public String selectBuonoCodFromNumeroDomanda(String numeroDomanda) throws DatabaseException {
		String buonoCod = "";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			buonoCod = jdbcTemplate.queryForObject(SELECT_BUONO_COD_FROM_NUMERO_DOMANDA, namedParameters,
					(rs, rowNum) -> rs.getString("buono_cod"));

			return buonoCod;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_CF_RICHIEDENTE_FROM_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public int insertAllegato(String nomefile, String tipofile, String pathfile, String tipoAllegato,
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
		return keyHolder.getKey().intValue();
	}

	public void updateAllegato(String nomefile, String pathfile, String cfmodifica, int allegatoId) {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("nomefile", nomefile, Types.VARCHAR).addValue("pathfile", pathfile, Types.VARCHAR)
				.addValue("cfmodifica", cfmodifica, Types.VARCHAR).addValue("allegatoId", allegatoId, Types.BIGINT);

		jdbcTemplate.update(UPDATE_CONTRATTO_ALLEGATO, params);

	}

	public boolean checkEsistenzaTipoAllegato(String allegatoTipo) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("allegatoTipo", allegatoTipo);
		try {
			Integer numallegati = jdbcTemplate.queryForObject(CHECK_ESISTENZA_TIPO_ALLEGATO, namedParameters,
					Integer.class);
			return numallegati.intValue() > 0;

		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "SELECT_DET_COD_ALLEGATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public int insertAllegatoDichiarazioneSpesa(int dicSpesaId, int allegati_id, String shibIdentitaCodiceFiscale)
			throws DatabaseException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("allegato_id", allegati_id, Types.BIGINT);
		params.addValue("dic_spesa_id", dicSpesaId, Types.BIGINT);
		params.addValue("utente_creazione", shibIdentitaCodiceFiscale, Types.VARCHAR);
		params.addValue("utente_modifica", shibIdentitaCodiceFiscale, Types.VARCHAR);

		try {
			jdbcTemplate.update(INSERT_R_DICHIARAZIONE_SPESA_ALLEGATO, params, keyHolder,
					new String[] { "dic_spesa_allegato_id" });
			return keyHolder.getKey().intValue();

		} catch (Exception e) {
			String methodName = "INSERT_R_DICHIARAZIONE_SPESA_ALLEGATO";
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

	public int chiudoDichiarazioneInviata(int dicSpesaId, String dicSpesaStatoCod, String cfinserisci,
			String cfmodifica) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("dicSpesaId", dicSpesaId, Types.BIGINT);
		params.addValue("utente_modifica", cfmodifica, Types.VARCHAR);

		jdbcTemplate.update(UPDATE_R_DICHIARAZIONE_SPESA, params);

		params.addValue("dicSpesaStatoCod", dicSpesaStatoCod, Types.VARCHAR);
		params.addValue("utente_creazione", cfinserisci, Types.VARCHAR);

		jdbcTemplate.update(INSERT_R_DICHIARAZIONE_SPESA, params, keyHolder, new String[] { "r_dic_spesa_stato_id" });
		return keyHolder.getKey().intValue();
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

	public ModelInvioFornitore selectFornitore(int fornitoreId, int contrattoId) throws DatabaseException {
		ModelInvioFornitore fornitore = new ModelInvioFornitore();
		List<ModelDocumentoAllegato> documento = new ArrayList<ModelDocumentoAllegato>();
		List<ModelDocumento> documentiForn = new ArrayList<ModelDocumento>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contrattoId", contrattoId)
				.addValue("fornitoreId", fornitoreId);
		try {
			fornitore = jdbcTemplate.queryForObject(SELECT_FORNITORE, namedParameters, new FornitoreMapper());
			documento = jdbcTemplate.query(SELECT_ALLEGATI_CONTRATTO_ID, namedParameters,
					new DocumentoPathAllegatoMapper());
			for (ModelDocumentoAllegato doc : documento) {
				ModelDocumento documentoForn = new ModelDocumento();
				documentoForn.setNomefile(doc.getAllegatoId() + "_" + doc.getNomefile());
				documentoForn.setPath(null);
				documentoForn.setTipologia(doc.getTipologia());
				documentiForn.add(documentoForn);
			}
			fornitore.setDocumento(documentiForn);
			return fornitore;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_FORNITORE e SELECT_ALLEGATI_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public ModelInvioDocumentoSpesa selectDettaglioSpesa(int docSpesaId, String fornitoreCf) throws DatabaseException {
		ModelInvioDocumentoSpesa docSpesa = new ModelInvioDocumentoSpesa();
		List<ModelDocumentoAllegato> documento = new ArrayList<ModelDocumentoAllegato>();
		List<ModelDocumento> documentiSpesa = new ArrayList<ModelDocumento>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("docSpesaId", docSpesaId);
		try {
			docSpesa = jdbcTemplate.queryForObject(SELECT_DETTAGLIO_SPESA, namedParameters,
					new InvioDocumentoSpesaDettaglioMapper());
			String docSpesaData = jdbcTemplate.queryForObject(SELECT_DATA_DOCUMENTO_SPESA, namedParameters,
					String.class);
			docSpesa.setDataDocumento(docSpesaData);
			if (docSpesa.getDataDocumento() != null)
				docSpesa.setDescrizioneDocumento(
						Util.nomeMese(Converter.getInt(docSpesa.getDataDocumento().substring(5, 7))) + " "
								+ docSpesa.getDataDocumento().substring(0, 4));
			else
				docSpesa.setDescrizioneDocumento(null);
			documento = jdbcTemplate.query(SELECT_DETTAGLIO_SPESA_ALLEGATO_ID, namedParameters,
					new DocumentoPathAllegatoMapper());
			for (ModelDocumentoAllegato doc : documento) {
				ModelDocumento documentoSpesa = new ModelDocumento();
				documentoSpesa.setNomefile(doc.getAllegatoId() + "_" + doc.getNomefile());
				documentoSpesa.setPath(null);
				documentoSpesa.setTipologia(doc.getTipologia());
				documentiSpesa.add(documentoSpesa);
			}
			docSpesa.setCodiceFiscaleFornitore(fornitoreCf);
			docSpesa.setDocumento(documentiSpesa);
			return docSpesa;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_DETTAGLIO_SPESA e SELECT_DATA_DOCUMENTO_SPESA e SELECT_DETTAGLIO_SPESA_ALLEGATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelDocumento> selectAllegatiSpesa(int docSpesaId) throws DatabaseException {

		List<ModelDocumento> documento = new ArrayList<ModelDocumento>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("docSpesaId", docSpesaId);
		try {
			documento = jdbcTemplate.query(SELECT_DETTAGLIO_SPESA_ALLEGATO, namedParameters, new DocumentoPathMapper());
			return documento;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_DETTAGLIO_SPESA_ALLEGATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String selectImportoMensile(BigDecimal domandaId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandaId", domandaId);
		try {
			return jdbcTemplate.queryForObject(SELECT_IMPORTO_MENSILE, namedParameters, String.class);

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_IMPORTO_MENSILE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelSetFornitore selectSetFornitore(int fornitoreId, int contrattoId) throws DatabaseException {
		ModelSetFornitore fornitore = new ModelSetFornitore();
		List<ModelDocumentoAllegato> documento = new ArrayList<ModelDocumentoAllegato>();
		List<ModelAllegatoContratto> allegati = new ArrayList<ModelAllegatoContratto>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("contrattoId", contrattoId)
				.addValue("fornitoreId", fornitoreId);
		try {
			String os = System.getProperty("os.name");
			boolean locale = false;
			if (os.toLowerCase().contains("win")) {
				locale = true;
			}
			fornitore = jdbcTemplate.queryForObject(SELECT_SETFORNITORE, namedParameters, new SetFornitoreMapper());
			documento = jdbcTemplate.query(SELECT_ALLEGATI_CONTRATTO_ID, namedParameters,
					new DocumentoPathAllegatoMapper());
			for (ModelDocumentoAllegato doc : documento) {
				ModelAllegatoContratto allegato = new ModelAllegatoContratto();
				if (!locale)
					allegato.setPath(doc.getPath() + "/" + doc.getNomefile());
				else
					allegato.setPath(doc.getPath() + "\\" + doc.getNomefile());
				allegato.setNomeFile(doc.getAllegatoId() + "_" + doc.getNomefile());
				allegati.add(allegato);
			}
			fornitore.setFiles(allegati);
			return fornitore;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_SETFORNITORE e SELECT_ALLEGATI_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelDocumentoAllegato> selectAllegatiSpesaId(int docSpesaId) throws DatabaseException {

		List<ModelDocumentoAllegato> documento = new ArrayList<ModelDocumentoAllegato>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("docSpesaId", docSpesaId);
		try {
			documento = jdbcTemplate.query(SELECT_DETTAGLIO_SPESA_ALLEGATO_ID, namedParameters,
					new DocumentoPathAllegatoMapper());
			return documento;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_DETTAGLIO_SPESA_ALLEGATO_ID";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelDocumentoAllegato> selectAllegatiContrattoId(int fornitoreId, int contrattoId)
			throws DatabaseException {

		List<ModelDocumentoAllegato> documento = new ArrayList<ModelDocumentoAllegato>();
		SqlParameterSource namedParameters1 = new MapSqlParameterSource().addValue("fornitoreId", fornitoreId)
				.addValue("contrattoId", contrattoId);
		try {
			documento.addAll(jdbcTemplate.query(SELECT_ALLEGATI_CONTRATTO_ID, namedParameters1,
					new DocumentoPathAllegatoMapper()));
			return documento;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ALLEGATI_CONTRATTO_ID";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}

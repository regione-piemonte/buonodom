/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombanbatch.dao;

public class SQLStatements {

	public static final String INSERT_LOG_AUDIT = "insert into csi_log_audit "
			+ " (data_ora,id_app,ip_address,utente,operazione,ogg_oper,key_oper,uuid,request_payload,response_payload,esito_chiamata) values "
			+ " (now(), 'BUONODOMBANBATCH' , '10.1.1.1' ,'BUONODOMBANBATCH', 'update','invioBandi', 'Batch_invioBandi',null,"
			+ "pgp_sym_encrypt(?, ?)::bytea, pgp_sym_encrypt(?, ?)::bytea, ?)";

	public static final String GETRICHIESTE = "select distinct a.beneficiario_cf,d.domanda_det_id,c.domanda_stato_cod,d.validita_inizio::date, "
			+ "now()::date-d.validita_inizio::date giorni,a.richiedente_cf,a.domanda_numero,d.sportello_id,a.domanda_id,d.iban,d.iban_intestatario "
			+ "from bdom_t_domanda a, bdom_d_contributo_tipo b, "
			+ "bdom_d_domanda_stato c, bdom_t_domanda_dettaglio d "
			+ "where a.contributo_tipo_id = b.contributo_tipo_id " + "and b.contributo_tipo_cod ='DOM' "
			+ "and a.sportello_id =d.sportello_id " + "and c.domanda_stato_id = d.domanda_stato_id "
			+ "and d.domanda_id =a.domanda_id " + "and b.validita_fine is null " + "and d.validita_fine is null "
			+ "and c.validita_fine is null " + "and c.domanda_stato_cod in ('IN_PAGAMENTO') "
			+ "and a.data_cancellazione is null " + "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and d.data_cancellazione is null " + "and d.domanda_det_id not in "
			+ "(select a.domanda_det_id "
			+ "from  bdom_d_batch_stato b, bdom_d_batch c,bdom_s_batch_esecuzione_det d,bdom_s_batch_esecuzione a "
			+ "INNER join (select a.domanda_det_id from bdom_s_batch_esecuzione a, bdom_d_batch_stato b, bdom_d_batch c,bdom_s_batch_esecuzione_det d "
			+ "where a.batchesecstato_id = b.batchesecstato_id " + "and b.batchesecstato_cod = 'STATO_OK' "
			+ "and a.batch_id=c.batch_id " + "and c.batch_cod='PUBBLICAZIONE' " + "and a.data_cancellazione is null "
			+ "and b.validita_inizio  <= now() " + "and b.validita_fine is null " + "and b.data_cancellazione is null "
			+ "and d.data_cancellazione is null " + "and a.batchesecstato_id = d.batchesecstato_id "
			+ "and d.batchesec_id = a.batchesec_id " + "group by a.domanda_det_id "
			+ "HAVING COUNT(*)=6) p on p.domanda_det_id=a.domanda_det_id "
			+ "where a.batchesecstato_id = b.batchesecstato_id " + "and b.batchesecstato_cod = 'STATO_OK' "
			+ "and a.batch_id=c.batch_id " + "and c.batch_cod='PUBBLICAZIONE' " + "and a.data_cancellazione is null "
			+ "and b.validita_inizio  <= now() " + "and b.validita_fine is null " + "and b.data_cancellazione is null "
			+ "and d.data_cancellazione is null " + "and a.batchesecstato_id = d.batchesecstato_id "
			+ "and d.batchesec_id = a.batchesec_id) " + "order by a.domanda_id";

	public static final String CHECK_STATO_GRADUATORIA = "select ( " + "	case  " + "		when ( "
			+ "			select bdgs.graduatoria_stato_cod  "
			+ "			from bdom_r_graduatoria_stato brgs, bdom_d_graduatoria_stato bdgs "
			+ "			where bdgs.graduatoria_stato_id  = brgs.graduatoria_stato_id "
			+ "			and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now()) "
			+ "			and brgs.graduatoria_id  = ( " + "				select btg.graduatoria_id  "
			+ "				from bdom_t_graduatoria btg, bdom_r_graduatoria_stato brgs  "
			+ "				where btg.graduatoria_id = brgs.graduatoria_id "
			+ "				and btg.data_cancellazione is null "
			+ "				and brgs.data_cancellazione is null  "
			+ "				and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now()) "
			+ "				and btg.sportello_id in ( " + "					select bts.sportello_id "
			+ "					from bdom_t_sportello bts " + "					where bts.data_cancellazione is null "
			+ "					and bts.sportello_id = ?) " + "				) " + "		) = 'PUBBLICATA' "
			+ "		then true " + "		else false " + "	end " + ") as controllo ";

	public static final String DESC_GRADUATORIA_PUBBLICATA = "select " + "btg.graduatoria_cod " + "from "
			+ "bdom_t_graduatoria btg, " + "bdom_r_graduatoria_stato brgs, " + "bdom_d_graduatoria_stato bdgs "
			+ "where " + "btg.graduatoria_id = brgs.graduatoria_id "
			+ "and bdgs.graduatoria_stato_id = brgs.graduatoria_stato_id " + "and btg.data_cancellazione is null "
			+ "and brgs.data_cancellazione is null " + "and bdgs.graduatoria_stato_cod = 'PUBBLICATA' "
			+ "and now() between coalesce(brgs.validita_inizio, " + "now()) and coalesce(brgs.validita_fine, "
			+ "now()) " + "and btg.sportello_id in ( " + "select " + "bts.sportello_id " + "from "
			+ "bdom_t_sportello bts " + "where " + "bts.data_cancellazione is null " + "and bts.sportello_id = ?)";

	public static final String DESC_GRADUATORIA_ULTIMA_PUBBLICATA = "select c.graduatoria_cod from bdom_r_graduatoria_stato a, bdom_d_graduatoria_stato b,bdom_t_graduatoria c "
			+ "where a.graduatoria_stato_id = b.graduatoria_stato_id " + "and c.graduatoria_id=a.graduatoria_id "
			+ "and a.data_cancellazione is null " + "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and b.graduatoria_stato_cod ='PUBBLICATA' "
			+ "and now() between coalesce(a.validita_inizio, now()) and coalesce(a.validita_fine,now()) "
			+ "and now() between coalesce(b.validita_inizio, now()) and coalesce(b.validita_fine,now()) "
			+ "order by a.validita_inizio desc " + "limit 1";

	public static final String GETRICHIESTENONFINANZIATE = "select distinct a.beneficiario_cf,d.domanda_det_id,c.domanda_stato_cod,d.validita_inizio::date, "
			+ "now()::date-d.validita_inizio::date giorni,a.richiedente_cf,a.domanda_numero,d.sportello_id,a.domanda_id "
			+ "from bdom_t_domanda a, bdom_d_contributo_tipo b, "
			+ "bdom_d_domanda_stato c, bdom_t_domanda_dettaglio d "
			+ "where a.contributo_tipo_id = b.contributo_tipo_id " + "and b.contributo_tipo_cod ='DOM' "
			+ "and a.sportello_id =d.sportello_id " + "and c.domanda_stato_id = d.domanda_stato_id "
			+ "and d.domanda_id =a.domanda_id " + "and b.validita_fine is null " + "and d.validita_fine is null "
			+ "and c.validita_fine is null " + "and c.domanda_stato_cod in ('AMMESSA','AMMESSA_RISERVA') "
			+ "and a.data_cancellazione is null " + "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and d.data_cancellazione is null order by a.domanda_id";

	public static final String SELECT_PRECEDENTE_STATO = "with tab1 as ( "
			+ "select a.domanda_det_cod, max(a.validita_inizio) validita_inizio from bdom_t_domanda_dettaglio a, "
			+ "bdom_t_domanda b,bdom_d_domanda_stato c " + "where a.domanda_id = b.domanda_id "
			+ "and b.domanda_numero  = ? " + "and c.domanda_stato_id = a.domanda_stato_id "
			+ "and a.validita_fine is not null " + "and a.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and b.data_cancellazione is null " + "and c.validita_fine is null "
			+ "group by a.domanda_det_cod " + "order by validita_inizio desc " + "limit 1) "
			+ "select domanda_det_cod from tab1";

	public static final String SELECT_BUONO_CREATO = "select distinct buono_id from bdom_t_buono where domanda_id = ? and sportello_id = ? "
			+ "and data_cancellazione is null";

	public static final String INSERT_INTO_BUONO = "insert into bdom_t_buono (domanda_det_id,sportello_id, "
			+ "domanda_id, utente_creazione,utente_modifica) values (?,?,?,'BUONODOMBANBATCH','BUONODOMBANBATCH')";

	public static final String INSERT_INTO_BUONO_STATO = "insert into bdom_r_buono_stato (buono_id,buono_stato_id, "
			+ "utente_creazione,utente_modifica,iban,iban_intestatario) " + "values "
			+ "(?,(select buono_stato_id from bdom_d_buono_stato where buono_stato_cod=?),'BUONODOMBANBATCH','BUONODOMBANBATCH',?,?)";

	public static final String INSERT_INTO_BUONO_FORNITORE = "insert into bdom_t_buono_fornitore (fornitore_nome,fornitore_cognome,fornitore_cf,buono_id, "
			+ "fornitore_nascita_data, fornitore_nascita_stato, fornitore_nascita_provincia, fornitore_nascita_comune, "
			+ "utente_creazione,utente_modifica) "
			+ "(select btdd.assistente_familiare_nome ,btdd.assistente_familiare_cognome , " + "case "
			+ "when bdct.contratto_tipo_cod ='ASSISTENTE_FAMILIARE' then btdd.assistente_familiare_cf "
			+ "when bdct.contratto_tipo_cod ='PARTITA_IVA' then btdd.assistente_familiare_cf "
			+ "when bdct.contratto_tipo_cod ='COOPERATIVA' then btdd.contratto_cf_cooperativa " + "else null "
			+ "end, ?, "
			+ "btdd.assistente_familiare_nascita_data, btdd.assistente_familiare_nascita_stato,btdd.assistente_familiare_nascita_provincia, btdd.assistente_familiare_nascita_comune, "
			+ "'BUONODOMBANBATCH','BUONODOMBANBATCH' from bdom_t_domanda_dettaglio btdd,"
			+ "bdom_t_domanda btd, bdom_d_contratto_tipo bdct " + "where btdd.domanda_det_id = ? "
			+ "and bdct.contratto_tipo_id=btdd.contratto_tipo_id " + "and btd.domanda_id = btdd.domanda_id)";

	public static final String INSERT_INTO_BUONO_CONTRATTO = "insert into bdom_t_contratto (datore_di_lavoro_nome, datore_di_lavoro_cognome, datore_di_lavoro_cf, "
			+ "datore_di_lavoro_nascita_data, datore_di_lavoro_nascita_comune, datore_di_lavoro_nascita_provincia, datore_di_lavoro_nascita_stato,"
			+ "fornitore_piva, contratto_data_inizio, "
			+ "contratto_data_fine, fornitore_tipo_id, contratto_tipo_id, rapporto_tipo_id, buono_id, fornitore_id, utente_creazione,utente_modifica) "
			+ "(select btdd.datore_di_lavoro_nome ,btdd.datore_di_lavoro_cognome ,btdd.datore_di_lavoro_cf , btdd.datore_di_lavoro_nascita_data , "
			+ "btdd.datore_di_lavoro_nascita_comune,btdd.datore_di_lavoro_nascita_provincia ,btdd.datore_di_lavoro_nascita_stato , "
			+ "case " + "when bdct.contratto_tipo_cod ='PARTITA_IVA' then btdd.assistente_familiare_piva "
			+ "else null " + "end,btdd.contratto_data_inizio ,btdd.contratto_data_fine, "
			+ "(select distinct fornitore_tipo_id from bdom_d_fornitore_tipo a,bdom_d_assistente_tipo b,bdom_t_domanda_dettaglio btdd "
			+ "where a.fornitore_tipo_cod=b.assistente_tipo_cod "
			+ "and btdd.assistente_tipo_id = b.assistente_tipo_id " + "and btdd.domanda_det_id = ?), "
			+ "btdd.contratto_tipo_id, btdd.rapporto_tipo_id,?,?, "
			+ "'BUONODOMBANBATCH','BUONODOMBANBATCH' from bdom_t_domanda_dettaglio btdd, "
			+ "bdom_t_domanda btd , bdom_d_contratto_tipo bdct " + "where btdd.domanda_det_id = ? "
			+ "and btd.domanda_id = btdd.domanda_id "
			+ "and bdct.contratto_tipo_id = btdd.contratto_tipo_id and bdct.contratto_tipo_cod != 'NESSUN_CONTRATTO')";

	public static final String INSERT_INTO_BUONO_ALLEGATO = "insert into bdom_t_buono_allegato (file_name , file_type , file_path,allegato_tipo_id,"
			+ "utente_creazione,utente_modifica) " + "(select file_name , file_type , file_path, "
			+ "(select allegato_tipo_id from bdom_d_buono_allegato_tipo where allegato_tipo_cod=bdat.allegato_tipo_cod),'BUONODOMBANBATCH','BUONODOMBANBATCH' "
			+ "  from bdom_t_allegato bta, bdom_d_allegato_tipo bdat ,bdom_d_contratto_tipo bdct,bdom_t_domanda_dettaglio btdd "
			+ "where bdat.allegato_tipo_id = bta.allegato_tipo_id "
			+ "and bdct.contratto_tipo_id = btdd.contratto_tipo_id " + "and btdd.domanda_det_id = bta.domanda_det_id "
			+ "and case when bdct.contratto_tipo_cod ='ASSISTENTE_FAMILIARE' then bdat.allegato_tipo_cod in ('CONTRATTO_LAVORO','DENUNCIA_INPS') "
			+ "when  bdct.contratto_tipo_cod ='COOPERATIVA' then bdat.allegato_tipo_cod in ('CONTRATTO_LAVORO_COOP') "
			+ "when  bdct.contratto_tipo_cod ='PARTITA_IVA' then bdat.allegato_tipo_cod in ('LETTERA_INCARICO') "
			+ "end " + "and bta.domanda_det_id = ?)";

	public static final String INSERT_INTO_R_CONTRATTO_ALLEGATO = "insert into bdom_r_contratto_allegato (contratto_id , allegato_id,"
			+ "utente_creazione,utente_modifica) values (?,?,'BUONODOMBANBATCH','BUONODOMBANBATCH')";

	public static final String SELECT_PARAMETRO = "SELECT a.parametro_valore FROM bdom_c_parametro a,bdom_c_parametro_tipo b where "
			+ "b.parametro_tipo_id = a.parametro_tipo_id and "
			+ "now()::date BETWEEN b.validita_inizio::date and COALESCE(b.validita_fine::date, now()::date) and b.data_cancellazione is null and "
			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null and "
			+ "a.parametro_cod =? and " + "b.parametro_tipo_cod =?";

	public static final String GET_DOMANDE_INVIO_DICHIARAZIONE_BANDI = "select distinct btd.beneficiario_cf,btdd.domanda_det_id,bdds.domanda_stato_cod,btdd.validita_inizio::date,"
			+ "now()::date-btdd.validita_inizio::date giorni,btd.richiedente_cf,btd.domanda_numero,btdd.sportello_id,btd.domanda_id "
			+ "from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, "
			+ "bdom_t_buono btb,bdom_t_periodo_rendicontazione btpr, "
			+ "bdom_t_dichiarazione_spesa btds,bdom_d_dichiarazione_spesa_stato bddss,bdom_r_dichiarazione_spesa_stato brdss, "
			+ "bdom_d_buono_stato bdbs ,bdom_r_buono_stato brbs " + "where btd.domanda_id = btdd.domanda_id "
			+ "and btds.periodo_id = btpr.periodo_id " + "and btds.buono_id = btb.buono_id "
			+ "and bddss.dic_spesa_stato_id = brdss.dic_spesa_stato_id " + "and brdss.dic_spesa_id = btds.dic_spesa_id "
			+ "and bdds.domanda_stato_cod ='IN_PAGAMENTO' " + "and bddss.dic_spesa_stato_cod = ? "
			+ "and btb.domanda_det_id = btdd.domanda_det_id " + "and btb.sportello_id = btdd.sportello_id "
			+ "and btb.domanda_id = btd.domanda_id " + "and btdd.validita_fine is null "
			+ "and btd.data_cancellazione is null " + "and btdd.data_cancellazione is null "
			+ "and bdds.data_cancellazione is null " + "and btb.data_cancellazione is null "
			+ "and btpr.data_cancellazione is null " + "and brdss.validita_fine is null "
			+ "and btpr.validita_fine is not null " + "and bdbs.buono_stato_id = brbs.buono_stato_id "
			+ "and brbs.buono_id = btb.buono_id " + "and bdbs.buono_stato_cod ='ATTIVO' "
			+ "and bdbs.data_cancellazione is null " + "and brbs.data_cancellazione is null "
			+ "and btpr.validita_fine::date = (select max(validita_fine::date) "
			+ "from bdom_t_periodo_rendicontazione where data_cancellazione is null and validita_fine is not null)";

}

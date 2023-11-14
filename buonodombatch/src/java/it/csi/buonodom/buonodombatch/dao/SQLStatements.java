/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombatch.dao;

public class SQLStatements {

	public static final String INSERT_LOG_AUDIT = "insert into csi_log_audit "
			+ " (data_ora,id_app,ip_address,utente,operazione,ogg_oper,key_oper,uuid,request_payload,response_payload,esito_chiamata) values "
			+ " (now(), 'BUONODOMBATCH' , '10.1.1.1' ,'BUONODOMBATCH', 'update','revocaSeDeceduto', 'Batch_revocaSeDeceduto',null,"
			+ "pgp_sym_encrypt(?, ?)::bytea, pgp_sym_encrypt(?, ?)::bytea, ?)";

	public static final String GETRICHIESTE = "select distinct a.beneficiario_cf,d.domanda_det_id,c.domanda_stato_cod,d.validita_inizio::date, "
			+ "now()::date-d.validita_inizio::date giorni,a.richiedente_cf,a.domanda_numero,d.sportello_id,a.domanda_id "
			+ "from bdom_t_domanda a, bdom_d_contributo_tipo b, "
			+ "bdom_d_domanda_stato c, bdom_t_domanda_dettaglio d "
			+ "where a.contributo_tipo_id = b.contributo_tipo_id " + "and b.contributo_tipo_cod ='DOM' "
			+ "and a.sportello_id =d.sportello_id " + "and c.domanda_stato_id = d.domanda_stato_id "
			+ "and d.domanda_id =a.domanda_id " + "and b.validita_fine is null " + "and d.validita_fine is null "
			+ "and c.validita_fine is null "
			+ "and c.domanda_stato_cod not in ('BOZZA','NON_AMMESSA','REVOCATA','ANNULLATA','DINIEGO','NON_AMMISSIBILE','RINUNCIATA','IN_PAGAMENTO') "
			+ "and a.data_cancellazione is null " + "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and d.data_cancellazione is null order by a.domanda_id";

	public static final String GETRICHIESTE_DINIEGO = "select distinct a.beneficiario_cf,d.domanda_det_id,c.domanda_stato_cod,d.validita_inizio::date, "
			+ "now()::date-d.validita_inizio::date giorni,a.richiedente_cf,a.domanda_numero,d.sportello_id,a.domanda_id "
			+ "from bdom_t_domanda a, bdom_d_contributo_tipo b, "
			+ "bdom_d_domanda_stato c, bdom_t_domanda_dettaglio d "
			+ "where a.contributo_tipo_id = b.contributo_tipo_id " + "and b.contributo_tipo_cod ='DOM' "
			+ "and a.sportello_id =d.sportello_id " + "and c.domanda_stato_id = d.domanda_stato_id "
			+ "and d.domanda_id =a.domanda_id " + "and b.validita_fine is null " + "and d.validita_fine is null "
			+ "and c.validita_fine is null " + "and c.domanda_stato_cod in ('DINIEGO') "
			+ "and (lower(d.note) not like lower('%decesso del destinatario%') "
			+ "and lower(d.note) not like lower('%cambio di residenza fuori Piemonte del destinatario%') "
			+ "or d.note is null) " + "and bts.validita_fine is not null "
			+ "and now()::date >= bts.validita_fine::date " + "and a.data_cancellazione is null "
			+ "and b.data_cancellazione is null " + "and c.data_cancellazione is null "
			+ "and d.data_cancellazione is null order by a.domanda_id";

	public static final String SELECT_PARAMETRO = "SELECT a.parametro_valore FROM bdom_c_parametro a,bdom_c_parametro_tipo b where "
			+ "b.parametro_tipo_id = a.parametro_tipo_id and "
			+ "now()::date BETWEEN b.validita_inizio::date and COALESCE(b.validita_fine::date, now()::date) and b.data_cancellazione is null and "
			+ "now()::date BETWEEN a.validita_inizio::date and COALESCE(a.validita_fine::date, now()::date) and a.data_cancellazione is null and "
			+ "a.parametro_cod =? and " + "b.parametro_tipo_cod =?";

	public static final String UPDATE_DETTAGLIO_DOMANDA_CHIUDI = "update bdom_t_domanda_dettaglio "
			+ "set validita_fine=now(), data_modifica=now(), utente_modifica='BATCH' " + "where domanda_det_id = ?";

	public static final String INSERT_DETTAGLIO_DOMANDA = "INSERT INTO bdom_t_domanda_dettaglio (domanda_id, sportello_id, domanda_stato_id, isee_valore, isee_data_rilascio, "
			+ "isee_scadenza, punteggio_sociale, nessuna_incompatibilita, incompatibilita_per_contratto, contratto_cf_cooperativa, assistente_familiare_nome, "
			+ "assistente_familiare_cognome, assistente_familiare_cf, assistente_familiare_nascita_stato, richiedente_nome, richiedente_cognome, richiedente_nascita_data, "
			+ "richiedente_nascita_stato, richiedente_nascita_comune, richiedente_nascita_provincia, richiedente_residenza_indirizzo, richiedente_residenza_comune, "
			+ "richiedente_residenza_provincia, destinatario_nome, destinatario_cognome, destinatario_nascita_data, destinatario_nascita_comune, "
			+ "destinatario_nascita_provincia, destinatario_nascita_stato, destinatario_residenza_indirizzo, destinatario_residenza_comune, "
			+ "destinatario_residenza_provincia, destinatario_domicilio_indirizzo, destinatario_domicilio_comune, destinatario_domicilio_provincia, "
			+ "situazione_lavorativa_attiva, datore_di_lavoro_nome, datore_di_lavoro_cognome, datore_di_lavoro_cf, datore_di_lavoro_nascita_data, "
			+ "datore_di_lavoro_nascita_comune, datore_di_lavoro_nascita_provincia, datore_di_lavoro_nascita_stato, iban, iban_intestatario, "
			+ "protocollo_cod, ateco_cod, note, contratto_tipo_id, rapporto_tipo_id, titolo_studio_id, area_id, asl_id, ruolo_cod, "
			+ "verifica_eg_richiesta, verifica_eg_in_corso, verifica_eg_conclusa, verifica_eg_punteggio_sociale, verifica_eg_incompatibilita, "
			+ "validita_inizio, validita_fine, data_creazione, data_modifica, data_cancellazione, utente_creazione, utente_modifica, "
			+ "utente_cancellazione, relazione_tipo_id, isee_conforme, valutazione_multidimensionale_id, assistente_familiare_nascita_comune, "
			+ "assistente_familiare_nascita_data, assistente_familiare_nascita_provincia, data_protocollo, tipo_protocollo, contratto_data_inizio, contratto_data_fine, "
			+ "messageuuid_protocollo, assistente_familiare_piva, assistente_tipo_id, ateco_desc,nota_interna,isee_verificato_conforme,isee_verificato_in_data,ateco_verificato_in_data, "
			+ "note_richiedente,verifica_eg_data, note_regione, note_ente_gestore) "
			+ "select domanda_id, sportello_id, (select domanda_stato_id  from bdom_d_domanda_stato "
			+ "where domanda_stato_cod = ?), isee_valore, isee_data_rilascio, "
			+ "isee_scadenza, punteggio_sociale, nessuna_incompatibilita, incompatibilita_per_contratto, contratto_cf_cooperativa, assistente_familiare_nome, "
			+ "assistente_familiare_cognome, assistente_familiare_cf, assistente_familiare_nascita_stato, richiedente_nome, richiedente_cognome, richiedente_nascita_data, "
			+ "richiedente_nascita_stato, richiedente_nascita_comune, richiedente_nascita_provincia, richiedente_residenza_indirizzo, richiedente_residenza_comune, "
			+ "richiedente_residenza_provincia, destinatario_nome, destinatario_cognome, destinatario_nascita_data, destinatario_nascita_comune, "
			+ "destinatario_nascita_provincia, destinatario_nascita_stato, destinatario_residenza_indirizzo, destinatario_residenza_comune, "
			+ "destinatario_residenza_provincia, destinatario_domicilio_indirizzo, destinatario_domicilio_comune, destinatario_domicilio_provincia, "
			+ "situazione_lavorativa_attiva, datore_di_lavoro_nome, datore_di_lavoro_cognome, datore_di_lavoro_cf, datore_di_lavoro_nascita_data, "
			+ "datore_di_lavoro_nascita_comune, datore_di_lavoro_nascita_provincia, datore_di_lavoro_nascita_stato, iban, iban_intestatario, "
			+ "null, ateco_cod, ?, contratto_tipo_id, rapporto_tipo_id, titolo_studio_id, area_id, asl_id, ruolo_cod, "
			+ "verifica_eg_richiesta, verifica_eg_in_corso, verifica_eg_conclusa, verifica_eg_punteggio_sociale, verifica_eg_incompatibilita, "
			+ "now(), null, now(), now(), null, 'BATCH', 'BATCH', "
			+ "utente_cancellazione, relazione_tipo_id, isee_conforme, valutazione_multidimensionale_id, assistente_familiare_nascita_comune, "
			+ "assistente_familiare_nascita_data, assistente_familiare_nascita_provincia, null, "
			+ "null, contratto_data_inizio, contratto_data_fine, null, assistente_familiare_piva, assistente_tipo_id, ateco_desc, "
			+ "null , isee_verificato_conforme,isee_verificato_in_data,ateco_verificato_in_data,note_richiedente,verifica_eg_data, null, note_ente_gestore "
			+ "from bdom_t_domanda_dettaglio where domanda_det_id =?";

	public static final String UPDATE_DETTAGLIO_DOMANDA_APRI = "update bdom_t_domanda_dettaglio "
			+ "set domanda_det_cod = ? " + "where domanda_det_id = ?";

	public static final String INSERT_ALLEGATO_DOMANDA = "INSERT INTO bdom_t_allegato "
			+ "(file_name, file_type, file_path, sportello_id,domanda_det_id, domanda_det_cod, "
			+ "allegato_tipo_id, data_creazione,data_modifica, utente_creazione, utente_modifica) "
			+ "select file_name, file_type, file_path, sportello_id,?,?,allegato_tipo_id,now(),now(),'BATCH', 'BATCH' "
			+ "from bdom_t_allegato where domanda_det_id=?";

	public static final String SELECT_PROVINCIA_PIEMONTE = "select count(*) from bdom_d_provincia "
			+ "where provincia_sigla_automobilistica = ? " + "and data_cancellazione is null "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_PRECEDENTE_STATO = "with tab1 as( "
			+ "select max(a.validita_inizio) validita_inizio from bdom_t_domanda_dettaglio a, "
			+ "bdom_t_domanda b,bdom_d_domanda_stato c " + "where a.domanda_id = b.domanda_id "
			+ "and b.domanda_numero  = ? " + "and c.domanda_stato_id =a.domanda_stato_id "
			+ "and c.domanda_stato_cod = ? " + "and a.validita_fine is not null " + "and a.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and b.data_cancellazione is null "
			+ "and c.validita_fine is null) " + "select now()::date-tab1.validita_inizio::date giorni " + "from tab1 ";

	public static final String SELECT_ULTIMO_SPORTELLO_CHIUSO = "select bts.sportello_id "
			+ "from bdom_t_sportello bts " + "where bts.validita_fine::date = (select max(validita_fine) "
			+ "from bdom_t_sportello " + "where data_cancellazione is null " + "and validita_fine::date < now()::date "
			+ ")::date ";

	public static final String SELECT_INCOMPATIBILITA_DOMANDA = "select "
			+ "case when nessuna_incompatibilita=false or incompatibilita_per_contratto=true then true "
			+ " else false " + " end incompatibilita " + " from bdom_t_domanda_dettaglio "
			+ " where domanda_det_id = ? ";

	public static final String SELECT_GRADUATORIA_ID_BY_DOMANDA_DETTAGLIO_ID = "select (case when ((select count(*)  from bdom_t_graduatoria btg, bdom_t_graduatoria_dettaglio btgd, bdom_t_domanda_dettaglio btdd  "
			+ "	where btg.graduatoria_id = btgd.graduatoria_id  " + "	and btdd.domanda_id = btgd.domanda_id "
			+ "	and btg.data_cancellazione is null " + "	and btgd.data_cancellazione is null "
			+ "	and btdd.data_cancellazione is null "
			+ "	and btg.data_creazione = (select max(btg2.data_creazione) from bdom_t_graduatoria btg2 where btg2.data_cancellazione is null) "
			+ "	and btdd.domanda_det_id  = ? "
			+ "	) > 0) then (select btg.graduatoria_id  from bdom_t_graduatoria btg, bdom_t_graduatoria_dettaglio btgd, bdom_t_domanda_dettaglio btdd  "
			+ "	where btg.graduatoria_id = btgd.graduatoria_id  " + "	and btdd.domanda_id = btgd.domanda_id "
			+ "	and btg.data_cancellazione is null " + "	and btgd.data_cancellazione is null "
			+ "	and btdd.data_cancellazione is null "
			+ "	and btg.data_creazione = (select max(btg2.data_creazione) from bdom_t_graduatoria btg2 where btg2.data_cancellazione is null) "
			+ "	and btdd.domanda_det_id  = ? " + "	) " + "else 0 end ) ";

	public static final String CHECK_STATO_GRADUATORIA_BY_ID = "select ( " + "	case  " + "		when ( "
			+ "			select bdgs.graduatoria_stato_cod  "
			+ "			from bdom_r_graduatoria_stato brgs, bdom_d_graduatoria_stato bdgs "
			+ "			where bdgs.graduatoria_stato_id  = brgs.graduatoria_stato_id "
			+ " 		and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now())"
			+ "			and brgs.graduatoria_id  = ? " + "		) = ? " + "		then true " + "		else false "
			+ "	end " + ") as controllo ";

	public static final String UPDATE_R_GRADUATORIA_STATO = "UPDATE bdom_r_graduatoria_stato   "
			+ "set validita_fine = now() "
			+ "where now() between coalesce (validita_inizio, now()) and coalesce(validita_fine,now()) "
			+ "and data_cancellazione is null " + "and graduatoria_id = ?";

	public static final String INSERT_R_GRADUATORIA_STATO = "INSERT INTO bdom_r_graduatoria_stato (graduatoria_id, graduatoria_stato_id, utente_creazione, utente_modifica) "
			+ "values (?, " + "(select graduatoria_stato_id  from bdom_d_graduatoria_stato  "
			+ "where graduatoria_stato_cod = ? " + "and data_cancellazione is null  "
			+ "and now() between coalesce (validita_inizio, now()) and coalesce(validita_fine,now()) " + "), ?, ?) ";

	public static final String SELECT_PRECEDENTE_STATO_COD = "with tab1 as ( "
			+ "select a.domanda_det_cod, max(a.validita_inizio) validita_inizio from bdom_t_domanda_dettaglio a, "
			+ "bdom_t_domanda b,bdom_d_domanda_stato c " + "where a.domanda_id = b.domanda_id "
			+ "and b.domanda_numero  = ? " + "and c.domanda_stato_id = a.domanda_stato_id "
			+ "and a.validita_fine is not null " + "and a.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and b.data_cancellazione is null " + "and c.validita_fine is null "
			+ "group by a.domanda_det_cod " + "order by validita_inizio desc " + "limit 1) "
			+ "select domanda_det_cod from tab1";
}

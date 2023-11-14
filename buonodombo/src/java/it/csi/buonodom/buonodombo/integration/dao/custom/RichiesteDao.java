/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.integration.dao.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.buonodom.buonodombo.dto.ModelAllegato;
import it.csi.buonodom.buonodombo.dto.ModelDatiDaModificare;
import it.csi.buonodom.buonodombo.dto.ModelEmailDetId;
import it.csi.buonodom.buonodombo.dto.ModelIsee;
import it.csi.buonodom.buonodombo.dto.ModelRichiesta;
import it.csi.buonodom.buonodombo.dto.ModelUpdateCronologia;
import it.csi.buonodom.buonodombo.dto.ModelVerificheDomanda;
import it.csi.buonodom.buonodombo.dto.ModelVisualizzaCronologia;
import it.csi.buonodom.buonodombo.exception.DatabaseException;
import it.csi.buonodom.buonodombo.integration.dao.utils.AllegatoTipoMapper;
import it.csi.buonodom.buonodombo.integration.dao.utils.DettaglioRichiestaMapper;
import it.csi.buonodom.buonodombo.integration.dao.utils.EmailDetIdMapper;
import it.csi.buonodom.buonodombo.integration.dao.utils.ListaDatiDaModificareMapper;
import it.csi.buonodom.buonodombo.integration.dao.utils.ModelVerificheMapper;
import it.csi.buonodom.buonodombo.integration.dao.utils.ModelVisualizzaCroMapper;
import it.csi.buonodom.buonodombo.util.Constants;
import it.csi.buonodom.buonodombo.util.LoggerUtil;
import it.csi.buonodom.buonodombo.util.Util;

@Repository
public class RichiesteDao extends LoggerUtil {

	public static final String SELECT_NUMERO_RICHIESTA = "select " + " btdd.domanda_det_id, " + " btdd.sportello_id, "
			+ "bdct2.contributo_tipo_desc ," + "	btd.domanda_numero, " + "	bdds.domanda_stato_cod, "
			+ "	bdds.domanda_stato_desc, " + "	btdd.data_creazione , " + "	btd.richiedente_cf, "
			+ "	btdd.richiedente_nome, " + "	btdd.richiedente_cognome, " + "	btdd.richiedente_nascita_data, "
			+ "	btdd.richiedente_nascita_stato, " + "	btdd.richiedente_nascita_comune, "
			+ "	btdd.richiedente_nascita_provincia, "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.richiedente_residenza_indirizzo::bytea,'@keyEncrypt@'),'UTF8') as richiedente_residenza_indirizzo, "
			+ "	btdd.richiedente_residenza_comune, " + "	btdd.richiedente_residenza_provincia, "
			+ "	btd.beneficiario_cf, " + "	btdd.destinatario_nome, " + "	btdd.destinatario_cognome, "
			+ "	btdd.destinatario_nascita_data, " + "	btdd.destinatario_nascita_stato, "
			+ "	btdd.destinatario_nascita_comune, " + "	btdd.destinatario_nascita_provincia, "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_residenza_indirizzo::bytea,'@keyEncrypt@'),'UTF8') as destinatario_residenza_indirizzo, "
			+ "	btdd.destinatario_residenza_comune, " + "	btdd.destinatario_residenza_provincia, "
			+ " convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_domicilio_indirizzo::bytea,'@keyEncrypt@'),'UTF8') as destinatario_domicilio_indirizzo, "
			+ "	btdd.destinatario_domicilio_comune, " + "	btdd.destinatario_domicilio_provincia, "
			+ "	btdd.isee_valore, " + "	btdd.isee_data_rilascio , " + "	btdd.isee_scadenza, "
			+ "	btdd.punteggio_sociale, " + "	btdd.nessuna_incompatibilita, "
			+ "	btdd.incompatibilita_per_contratto, " + "	btdd.contratto_cf_cooperativa, "
			+ " btdd.assistente_familiare_nascita_comune, " + "	btdd.assistente_familiare_nascita_data, "
			+ " btdd.assistente_familiare_nascita_provincia, " + "	btdd.assistente_familiare_cf, "
			+ "	btdd.assistente_familiare_nome, " + "	btdd.assistente_familiare_cognome, "
			+ "	btdd.assistente_familiare_nascita_stato, " + "	btdd.datore_di_lavoro_cf, "
			+ "	btdd.datore_di_lavoro_nome, " + "	btdd.datore_di_lavoro_cognome, "
			+ "	btdd.datore_di_lavoro_nascita_data, " + "	btdd.datore_di_lavoro_nascita_stato, "
			+ "	btdd.datore_di_lavoro_nascita_comune, " + "	btdd.datore_di_lavoro_nascita_provincia, " + "	case "
			+ "	when btdd.situazione_lavorativa_attiva = true then 'Attivo' "
			+ "	when btdd.situazione_lavorativa_attiva = false then 'Inattivo' " + "	else '' "
			+ "	end situazione_lavorativa_attiva, " + "	btdd.iban, " + "	btdd.iban_intestatario, "
			+ "	btdd.protocollo_cod, " + "	btdd.data_protocollo, " + "	btdd.tipo_protocollo, " + "	btdd.ateco_cod, "
			+ "	btdd.ateco_desc, " + "	btdd.note, " + "	bdct.contratto_tipo_cod, "
			+ "	bdrt.rapporto_tipo_cod as delega_cod, " + "	bdrt.rapporto_tipo_desc as delega_desc, "
			+ "	bdts.titolo_studio_cod, " + "	bdts.titolo_studio_desc, " + "	bda.asl_cod, "
			+ "	bda.asl_azienda_desc, " + "	btdd.verifica_eg_richiesta," + "	btdd.verifica_eg_in_corso,"
			+ "	btdd.verifica_eg_conclusa," + "	btdd.verifica_eg_punteggio_sociale,"
			+ "	btdd.verifica_eg_incompatibilita," + "	btdd.isee_conforme, "
			+ " relazione.rapporto_tipo_cod as relazione_destinatario,"
			+ " relazione.rapporto_tipo_desc as relazione_destinatario_desc,"
			+ " bdvm.valutazione_multidimensionale_cod, " + "	btdd.contratto_data_inizio, "
			+ "	btdd.contratto_data_fine, " + "	btdd.assistente_familiare_piva, " + "	bdat.assistente_tipo_cod,"
			+ "	bda2.area_desc, " + "	btdd.nota_interna, " + "	btdd.note_richiedente, "
			+ " btdd.isee_verificato_conforme, " + " btdd.isee_verificato_in_data, "
			+ "	btdd.ateco_verificato_in_data, btdd.note_ente_gestore " + "	from " + "	bdom_t_domanda btd ,"
			+ "	bdom_t_domanda_dettaglio btdd  "
			+ "	left join bdom_d_rapporto_tipo bdrt on  btdd.rapporto_tipo_id = bdrt.rapporto_tipo_id "
			+ " left join bdom_d_rapporto_tipo relazione on  btdd.relazione_tipo_id = relazione.rapporto_tipo_id "
			+ "	left join bdom_d_domanda_stato bdds on bdds.domanda_stato_id  = btdd.domanda_stato_id "
			+ "	left join bdom_d_contratto_tipo bdct on btdd.contratto_tipo_id = bdct.contratto_tipo_id "
			+ " left join bdom_d_assistente_tipo bdat on btdd.assistente_tipo_id= bdat.assistente_tipo_id "
			+ " left join bdom_d_area bda2  on btdd.area_id = bda2.area_id "
			+ " left join bdom_d_valutazione_multidimensionale bdvm on btdd.valutazione_multidimensionale_id= bdvm.valutazione_multidimensionale_id,"
			+ "	bdom_d_titolo_studio bdts, " + "	bdom_d_asl bda ," + "	bdom_d_contributo_tipo bdct2 " + "	where "
			+ "	btd.data_cancellazione is null " + "	and btd.contributo_tipo_id = bdct2.contributo_tipo_id "
			+ "	and  " + "	btdd.domanda_id = btd.domanda_id  " + "	and  " + "	btdd.data_cancellazione is null  "
			+ "	and  " + "	btdd.validita_inizio <= now() " + "	and " + "	btdd.validita_fine is null   " + "	and  "
			+ "	btdd.titolo_studio_id=bdts.titolo_studio_id " + "	and  " + "	btdd.asl_id = bda.asl_id " + "	and "
			+ "	btd.domanda_numero=  :numeroDomanda";

	public static final String SELECT_ALLEGATI = "select  " + "	bdat.allegato_tipo_cod," + " bta.file_name "
			+ "	from  " + "	bdom_t_domanda_dettaglio btdd, " + "	bdom_d_allegato_tipo bdat, "
			+ "	bdom_t_allegato bta  " + "	where  " + "	btdd.domanda_det_id = bta.domanda_det_id " + "	and  "
			+ "	btdd.sportello_id = bta.sportello_id " + "	and  " + "	bta.allegato_tipo_id = bdat.allegato_tipo_id "
			+ "	and btdd.domanda_det_id = :idDomanda" + " and btdd.validita_inizio <= now() "
			+ " and btdd.validita_fine is null " + " and btdd.data_cancellazione is null "
			+ " and bta.data_cancellazione is null ";

	public static final String UPDATE_DET_COD = "UPDATE bdom_t_domanda_dettaglio " + "SET domanda_det_cod=:detCod "
			+ "WHERE domanda_det_id=:idDomanda";

	public static final String UPDATE_DATA_FINE_VAL_DETTAGLIO_DOMANDA = "UPDATE bdom_t_domanda_dettaglio "
			+ "SET validita_fine = now() " + "WHERE domanda_det_id=:idDomanda";

	public static final String UPDATE_DATA_DOMANDA_VAL_DOMANDA = "UPDATE bdom_t_domanda "
			+ "SET data_modifica = now(), " + " utente_modifica = :codFiscale "
			+ "WHERE domanda_numero=:numeroRichiesta";

	public static final String UPDATE_ATECO_COD_DETTAGLIO = "UPDATE bdom_t_domanda_dettaglio "
			+ "SET ateco_cod = :atecoCod, " + " ateco_desc = :atecoDesc, " + " data_modifica = now(), "
			+ " ateco_verificato_in_data = now(), " + " utente_modifica = :codFiscale "
			+ "WHERE domanda_det_id=:idDettaglio";

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
			+ "messageuuid_protocollo, assistente_familiare_piva, assistente_tipo_id, ateco_desc,nota_interna,isee_verificato_conforme,isee_verificato_in_data,ateco_verificato_in_data,note_richiedente, "
			+ "verifica_eg_data, note_regione, note_ente_gestore  ) "
			+ "select domanda_id, sportello_id, (select domanda_stato_id  from bdom_d_domanda_stato "
			+ "where domanda_stato_cod =:domandastatocod), isee_valore, isee_data_rilascio, "
			+ "isee_scadenza, punteggio_sociale, nessuna_incompatibilita, incompatibilita_per_contratto, contratto_cf_cooperativa, assistente_familiare_nome, "
			+ "assistente_familiare_cognome, assistente_familiare_cf, assistente_familiare_nascita_stato, richiedente_nome, richiedente_cognome, richiedente_nascita_data, "
			+ "richiedente_nascita_stato, richiedente_nascita_comune, richiedente_nascita_provincia, richiedente_residenza_indirizzo, richiedente_residenza_comune, "
			+ "richiedente_residenza_provincia, destinatario_nome, destinatario_cognome, destinatario_nascita_data, destinatario_nascita_comune, "
			+ "destinatario_nascita_provincia, destinatario_nascita_stato, destinatario_residenza_indirizzo, destinatario_residenza_comune, "
			+ "destinatario_residenza_provincia, destinatario_domicilio_indirizzo, destinatario_domicilio_comune, destinatario_domicilio_provincia, "
			+ "situazione_lavorativa_attiva, datore_di_lavoro_nome, datore_di_lavoro_cognome, datore_di_lavoro_cf, datore_di_lavoro_nascita_data, "
			+ "datore_di_lavoro_nascita_comune, datore_di_lavoro_nascita_provincia, datore_di_lavoro_nascita_stato, iban, iban_intestatario, "
			+ "null, ateco_cod, null, contratto_tipo_id, rapporto_tipo_id, titolo_studio_id, area_id, asl_id, ruolo_cod, "
			+ "verifica_eg_richiesta, verifica_eg_in_corso, verifica_eg_conclusa, verifica_eg_punteggio_sociale, verifica_eg_incompatibilita, "
			+ "now(), validita_fine, now(), now(), data_cancellazione, :utenteCreazione, :utenteModifica, "
			+ "utente_cancellazione, relazione_tipo_id, isee_conforme, valutazione_multidimensionale_id, assistente_familiare_nascita_comune, "
			+ "assistente_familiare_nascita_data, assistente_familiare_nascita_provincia, null, "
			+ "null, contratto_data_inizio, contratto_data_fine, null, assistente_familiare_piva, assistente_tipo_id, ateco_desc,"
			+ "null,isee_verificato_conforme,isee_verificato_in_data,ateco_verificato_in_data,note_richiedente, "
			+ "verifica_eg_data, null, note_ente_gestore "
			+ "from bdom_t_domanda_dettaglio where domanda_det_id =:domandadetid";

	public static final String SELECT_LISTA_ALLEGATI_RETTIFICA_IN_DOMANDA = "select b.allegato_tipo_cod as codice, b.allegato_tipo_desc as descrizione,e.allegato_id as id, "
			+ "null as nome_tabella from bdom_d_allegato_tipo_modificabile a, bdom_d_allegato_tipo b,"
			+ "bdom_t_domanda c,bdom_t_domanda_dettaglio d,bdom_t_allegato e "
			+ "where a.allegato_tipo_id =b.allegato_tipo_id " + "and c.domanda_numero = :numerodomanda "
			+ "and c.domanda_id = d.domanda_id " + "and e.domanda_det_id = d.domanda_det_id "
			+ "and e.allegato_tipo_id = a.allegato_tipo_id " + "and a.data_cancellazione is null "
			+ "and b.data_cancellazione is null " + "and c.data_cancellazione is null "
			+ "and d.data_cancellazione is null " + "and e.data_cancellazione is null "
			+ "and d.validita_inizio <= now() " + "and d.validita_fine is null " + "and a.validita_inizio <= now() "
			+ "and a.validita_fine is null " + "and b.validita_inizio <= now() " + "and b.validita_fine is null "
			+ "and b.allegato_tipo_cod !='DOMANDA' " + "order by b.allegato_tipo_desc";

	public static final String SELECT_LISTA_CAMPI_RETTIFICA = "select a.riferimento_db_campo as codice, a.campo_modificabile_desc  as descrizione,campo_modificabile_id as id, "
			+ "riferimento_db_tabella as nome_tabella from bdom_d_campo_modificabile a "
			+ "where a.data_cancellazione is null " + "and a.validita_inizio <= now() "
			+ "and a.validita_fine is null ";

	public static final String INSERT_T_DATO_DA_MODIFICARE = "insert into bdom_t_dato_da_modificare "
			+ "(allegato_id, domanda_det_id, sportello_id, data_creazione, data_modifica, utente_creazione, utente_modifica, campo_modificabile_id) "
			+ "	values "
			+ " (:allegatoid, :domandadetid, :sportelloid, now(), now(), :utenteCrea, :utenteModifica, :campomodificabileid)";

	public static final String SELECT_CRONOLOGIA = "with datoutente as(select distinct concat(btdd2.richiedente_nome,' ',btdd2.richiedente_cognome) utente ,btdd2.domanda_det_id "
			+ " from bdom_t_domanda_dettaglio btdd2, " + " bdom_t_domanda btd2, " + " bdom_d_domanda_stato bdds2 "
			+ " where " + " btd2.domanda_numero = :numeroDomanda " + " and " + " btdd2.domanda_id = btd2.domanda_id "
			+ " and " + " btdd2.data_cancellazione is null " + " and " + " btd2.data_cancellazione is null " + " and "
			+ " btdd2.domanda_det_id not in " + " ( " + " select  btdd3.domanda_det_id "
			+ " from bdom_t_domanda_dettaglio btdd3, " + " bdom_t_domanda btd3, " + " bdom_d_domanda_stato bdds3, "
			+ " bdom_t_soggetto bts " + " where " + " btd3.domanda_numero = :numeroDomanda " + " and "
			+ " btdd3.domanda_id = btd3.domanda_id " + " and " + " btdd3.data_cancellazione is null " + " and "
			+ " btd3.data_cancellazione is null " + " and " + " bdds3.domanda_stato_id = btdd3.domanda_stato_id "
			+ " and " + " bts.soggetto_cf =btdd3.utente_creazione " + " ) " + " union "
			+ " select distinct concat(bts.soggetto_nome ,' ',bts.soggetto_cognome) utente,btdd3.domanda_det_id "
			+ " from bdom_t_domanda_dettaglio btdd3, " + " bdom_t_domanda btd3, " + " bdom_d_domanda_stato bdds3, "
			+ " bdom_t_soggetto bts " + " where " + " btd3.domanda_numero = :numeroDomanda " + " and "
			+ " btdd3.domanda_id = btd3.domanda_id " + " and " + " btdd3.data_cancellazione is null " + " and "
			+ " btd3.data_cancellazione is null " + " and " + " bdds3.domanda_stato_id = btdd3.domanda_stato_id "
			+ " and " + " bts.soggetto_cf =btdd3.utente_creazione) " + "select  " + "	btd.domanda_numero,  "
			+ "	bdds.domanda_stato_desc,  " + "	btdd.note, " + "	btdd.nota_interna , " + "	btdd.validita_inizio, "
			+ " a.utente, btdd.note_regione, " + "case  "
			+ "	when btdd.utente_creazione = btd.richiedente_cf then true  "
			+ "	when btdd.utente_creazione <> btd.richiedente_cf then false " + "end as isRichiedente " + "from  "
			+ "	bdom_t_domanda btd,  " + "	bdom_t_domanda_dettaglio btdd,  " + "	bdom_d_domanda_stato bdds, "
			+ "	datoutente a  " + " where  " + "	btd.domanda_numero = :numeroDomanda  " + "	and   "
			+ "	btdd.domanda_id = btd.domanda_id   " + "	and   " + "	btdd.data_cancellazione is null   " + "	and   "
			+ "	btd.data_cancellazione is null  " + "	and   "
			+ "	bdds.domanda_stato_id = btdd.domanda_stato_id and a.domanda_det_id= btdd.domanda_det_id"
			+ " order by btdd.validita_inizio desc 	";

	public static final String DELETE_DATO_DA_MODIFICARE = "delete from bdom_t_dato_da_modificare "
			+ "where domanda_det_id = :domandadetid";

	public static final String UPDATE_NOTE_DETTAGLIO_DOMANDA = "UPDATE bdom_t_domanda_dettaglio "
			+ "SET data_modifica = now(), note = :notacittadino,nota_interna = :notainterna "
			+ "WHERE domanda_det_id=:idDomanda";

	public static final String SELECT_VERIFICHE = "with tab1 as( "
			+ "	select max(btdd.validita_inizio) validita_inizio "
			+ "	from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds "
			+ "	where btd.domanda_id = btdd.domanda_id " + "	and btd.domanda_numero =  :numeroDomanda "
			+ "	and bdds.domanda_stato_id = btdd.domanda_stato_id "
			+ "	and bdds.domanda_stato_cod in ('INVIATA','PERFEZIONATA_IN_PAGAMENTO') "
			+ "	and btdd.data_cancellazione is null " + "	and btd.data_cancellazione is null "
			+ "	and bdds.data_cancellazione is null " + "	and bdds.validita_fine is null ), " + "	tab2 as ( "
			+ "	select btd.domanda_numero, bdds.domanda_stato_id, " + "	case "
			+ "		when btdd.nessuna_incompatibilita = false " + "			then 'Presente' "
			+ "		when btdd.nessuna_incompatibilita = true " + "			then 'Assente' "
			+ "		when btdd.nessuna_incompatibilita is null " + "			then 'Assente' " + "	end as misure, "
			+ "	tab1.validita_inizio, 'NESSUNA_INCOMPATIBILITA' tipo, 'Cittadino' fonte, 1 ordine "
			+ "	from  bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds , tab1 "
			+ "	where btd.domanda_numero =  :numeroDomanda " + "	and btdd.domanda_id = btd.domanda_id "
			+ "	and btdd.data_cancellazione is null " + "	and btd.data_cancellazione is null "
			+ "	and btdd.validita_fine is null " + "	and bdds.data_cancellazione is null "
			+ "	and bdds.validita_fine is null " + "	and bdds.domanda_stato_id = btdd.domanda_stato_id " + "	union "
			+ "	select btd.domanda_numero, bdds.domanda_stato_id, " + "	case "
			+ "	when btdd.incompatibilita_per_contratto  = false " + "		then 'Assente' "
			+ "	when btdd.incompatibilita_per_contratto = true " + "		then 'Presente' "
			+ "	when btdd.incompatibilita_per_contratto is null  " + "		then 'Assente' " + "	end as misure,  "
			+ "	tab1.validita_inizio, 'INCOMPATIBILITA_PER_CONTRATTO' tipo, 'Cittadino' fonte, 2 ordine "
			+ "	from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds , tab1 "
			+ "	where btd.domanda_numero = :numeroDomanda " + "	and btdd.domanda_id = btd.domanda_id "
			+ "	and btdd.data_cancellazione is null  " + "	and btd.data_cancellazione is null "
			+ "	and btdd.validita_fine is null " + "	and bdds.data_cancellazione is null "
			+ "	and bdds.validita_fine is null " + "	and bdds.domanda_stato_id = btdd.domanda_stato_id " + "union "
			+ "	select btd.domanda_numero, bdds.domanda_stato_id, " + "	case  " + "	when btdd.isee_conforme = true "
			+ "		then 'Conforme' " + "	when btdd.isee_conforme = false " + "		then 'Non conforme' "
			+ "	when btdd.isee_conforme is null " + "		then 'Non verificato' " + "	end as misure, "
			+ "	tab1.validita_inizio, 'ISEE_CITTADINO' tipo, 'Cittadino' fonte, 3 ordine "
			+ "	from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds , tab1 "
			+ "	where btd.domanda_numero = :numeroDomanda " + "	and btdd.domanda_id = btd.domanda_id "
			+ "	and btdd.data_cancellazione is null " + "	and btd.data_cancellazione is null "
			+ "	and btdd.validita_fine is null " + "	and bdds.data_cancellazione is null "
			+ "	and bdds.validita_fine is null " + "	and bdds.domanda_stato_id = btdd.domanda_stato_id " + "	union "
			+ "	select btd.domanda_numero, bdds.domanda_stato_id, "
			+ "	case  when btdd.isee_verificato_conforme = true "
			+ "		then concat(translate(to_char(btdd.isee_valore , '999,999,999.99'), ',.', '.,'), ' ', '(Conforme)') "
			+ "	when btdd.isee_verificato_conforme = false "
			+ "		then concat(translate(to_char(btdd.isee_valore , '999,999,999.99'), ',.', '.,'), ' ', '(Non conforme)') "
			+ "	when btdd.isee_verificato_conforme is null  " + "		then 'Non verificato' " + "	end as isee, "
			+ "	btdd.isee_verificato_in_data, 'ISEE' tipo, 'Operatore Regionale' fonte, 4 ordine "
			+ "	from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, tab1 "
			+ "	where btd.domanda_numero = :numeroDomanda " + "	and btdd.domanda_id = btd.domanda_id "
			+ "	and btdd.data_cancellazione is null " + "	and btd.data_cancellazione is null "
			+ "	and btdd.validita_fine is null " + "	and bdds.data_cancellazione is null "
			+ "	and bdds.validita_fine is null " + "	and bdds.domanda_stato_id = btdd.domanda_stato_id " + "	union "
			+ "	select btd.domanda_numero, bdds.domanda_stato_id, " + "	case "
			+ "		when btdd.ateco_cod is not null  " + "			then concat(btdd.ateco_cod,' ',btdd.ateco_desc) "
			+ "	when btdd.ateco_cod is null " + "		then 'Non verificato' " + "	end as ateco, "
			+ "	btdd.ateco_verificato_in_data, 'ATECO' tipo, 'Operatore Regionale' fonte, 5 ordine "
			+ "	from  bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, tab1 "
			+ "	where btd.domanda_numero = :numeroDomanda  " + "	and btdd.domanda_id = btd.domanda_id "
			+ "	and btdd.data_cancellazione is null " + "	and btd.data_cancellazione is null  "
			+ "	and btdd.validita_fine is null " + "	and bdds.data_cancellazione is null "
			+ "	and bdds.validita_fine is null " + "	and bdds.domanda_stato_id = btdd.domanda_stato_id " + "	union "
			+ "	select btd.domanda_numero, bdds.domanda_stato_id, "
			+ "	case  when btdd.verifica_eg_incompatibilita = true  " + "		then 'Si' "
			+ "	when btdd.verifica_eg_incompatibilita = false " + "		then 'No' "
			+ "	when btdd.verifica_eg_incompatibilita is null " + "		then 'Non verificato' " + "	end as confomita, "
			+ "	btdd.verifica_eg_data , 'CONFORMITA_PSOCIALE' tipo, 'Ente Gestore' fonte, 6 ordine "
			+ "	from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, tab1 "
			+ "	where btd.domanda_numero = :numeroDomanda " + "	and btdd.domanda_id = btd.domanda_id "
			+ "	and btdd.data_cancellazione is null " + "	and btd.data_cancellazione is null "
			+ "	and btdd.validita_fine is null " + "	and bdds.data_cancellazione is null "
			+ "	and bdds.validita_fine is null " + "	and ( btdd.verifica_eg_richiesta is not null "
			+ "		or btdd.verifica_eg_in_corso is not null " + "		or btdd.verifica_eg_conclusa is not null ) "
			+ "	and bdds.domanda_stato_id = btdd.domanda_stato_id " + "	union "
			+ "	select btd.domanda_numero, bdds.domanda_stato_id, " + "	case "
			+ "	when btdd.verifica_eg_punteggio_sociale is null " + "		then 'Non verificato' "
			+ "	when btdd.verifica_eg_punteggio_sociale  is not null "
			+ "		then btdd.verifica_eg_punteggio_sociale " + "	end as psociale, "
			+ "	btdd.verifica_eg_data , 'PUNTEGGIO_PSOCIALE' tipo, 'Ente Gestore' fonte, 7 ordine "
			+ "	from bdom_t_domanda btd, bdom_t_domanda_dettaglio btdd, bdom_d_domanda_stato bdds, tab1 "
			+ "	where btd.domanda_numero = :numeroDomanda " + "	and btdd.domanda_id = btd.domanda_id "
			+ "	and btdd.data_cancellazione is null " + "	and btd.data_cancellazione is null "
			+ "	and btdd.validita_fine is null " + "	and bdds.data_cancellazione is null "
			+ "	and bdds.validita_fine is null " + "	and ( btdd.verifica_eg_richiesta is not null "
			+ "		or btdd.verifica_eg_in_corso is not null " + "		or btdd.verifica_eg_conclusa is not null ) "
			+ "	and bdds.domanda_stato_id = btdd.domanda_stato_id " + "	) "
			+ "select tab2.domanda_numero, tab2.misure,tab2.validita_inizio,tab2.tipo, tab2.fonte "
			+ "from tab2, bdom_d_domanda_stato bdds " + "where tab2.domanda_stato_id  = bdds.domanda_stato_id "
			+ "order by ordine ";

	public static final String SELECT_ALLEGATO_ID = "select allegato_id from bdom_t_allegato "
			+ "where domanda_det_id = :domandaDetId "
			+ "and allegato_tipo_id = (select allegato_tipo_id  from bdom_t_allegato "
			+ "where allegato_id =:allegatoId)";

	public static final String UPDATE_NOTA_INTERNA_DETTAGLIO_DOMANDA = "UPDATE bdom_t_domanda_dettaglio "
			+ "SET data_modifica = now(), nota_interna = :notainterna, utente_modifica= :utentemodifica "
			+ "WHERE domanda_det_id=:idDomanda";

	public static final String UPDATE_ISEE_DETTAGLIO_DOMANDA = "UPDATE bdom_t_domanda_dettaglio "
			+ "SET data_modifica = now(),isee_valore = :iseevalore,isee_data_rilascio= :iseedatarilascio, utente_modifica= :utentemodifica,"
			+ "isee_scadenza= :iseescadenza,isee_verificato_conforme = :iseeverificatoconforme,isee_verificato_in_data = now() "
			+ "WHERE domanda_det_id=:idDomanda";

	public static final String SELECT_EMAIL_DOMANDA_DET_ID = "select " + "	d.ente_gestore_email, "
			+ "	a.domanda_det_id " + "from " + "	bdom_t_domanda e, " + "	bdom_t_domanda_dettaglio a, "
			+ "	bdom_d_comune b, " + "	bdom_r_ente_gestore_comune c, " + "	bdom_t_ente_gestore d " + "where "
			+ "	a.destinatario_residenza_comune = b.comune_desc " + "	and b.comune_id = c.comune_id "
			+ "	and c.ente_gestore_id = d.ente_gestore_id " + "	and e.domanda_id = a.domanda_id "
			+ "	and e.domanda_numero = :numeroDomanda " + "	and e.data_cancellazione is null "
			+ "	and a.data_cancellazione is null " + "	and a.validita_fine is null "
			+ "	and b.data_cancellazione is null "
			+ "	and now() between b.validita_inizio and coalesce(b.validita_fine, now()) "
			+ "	and c.data_cancellazione is null "
			+ "	and now() between c.validita_inizio and coalesce(c.validita_fine, now()) "
			+ "	and d.data_cancellazione is null "
			+ "	and now() between d.validita_inizio and coalesce(d.validita_fine, now())";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<ModelDatiDaModificare> selectListaAllegatoRettifica(String numeroDomanda) throws DatabaseException {
		List<ModelDatiDaModificare> listaAllegati = new ArrayList<ModelDatiDaModificare>();
		// List<ModelDatiDaModificare> listaAllegatiTutti = new
		// ArrayList<ModelDatiDaModificare>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numerodomanda", numeroDomanda);
		try {
			// prendo tutti
			// listaAllegatiTutti = jdbcTemplate.query(SELECT_LISTA_ALLEGATI_RETTIFICA, new
			// ListaDatiDaModificareMapper());
			listaAllegati = jdbcTemplate.query(SELECT_LISTA_ALLEGATI_RETTIFICA_IN_DOMANDA, namedParameters,
					new ListaDatiDaModificareMapper());
//			for (ModelDatiDaModificare allegato:listaAllegati) {
//			listaAllegatiTutti.remove(allegato);
//			}
//			listaAllegatiTutti.addAll(listaAllegati);
			// return listaAllegatiTutti;
			return listaAllegati;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_LISTA_ALLEGATI_RETTIFICA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelDatiDaModificare> selectListaCampiRettifica(String numeroDomanda) throws DatabaseException {
		List<ModelDatiDaModificare> listaCampi = new ArrayList<ModelDatiDaModificare>();
		List<ModelDatiDaModificare> listaCampiOut = new ArrayList<ModelDatiDaModificare>();
		try {
			listaCampi = jdbcTemplate.query(SELECT_LISTA_CAMPI_RETTIFICA, new ListaDatiDaModificareMapper());
			// prendo solo i dati che esistono nella domanda
			String SELECT_DOMANDA_SINGOLO_CAMPO = "select count(*) "
					+ " from bdom_t_domanda a,bdom_t_domanda_dettaglio b " + "where a.domanda_numero = :numeroDomanda "
					+ "and a.domanda_id = b.domanda_id " + "and a.data_cancellazione is null "
					+ "and b.data_cancellazione is null " + "and b.validita_inizio <= now() "
					+ "and b.validita_fine is null ";
			Integer conta = 0;
			for (ModelDatiDaModificare campi : listaCampi) {
				String finequery = "and b." + campi.getCodice() + " is not null ";
				if (campi.getCodice().equalsIgnoreCase(Constants.CONTRATTO_TIPO_ID)
						|| campi.getCodice().equalsIgnoreCase(Constants.ASSISTENTE_TIPO_ID)) {
					finequery = finequery + "and b.contratto_tipo_id not in "
							+ "(select contratto_tipo_id from bdom_d_contratto_tipo c "
							+ "where c.contratto_tipo_cod='NESSUN_CONTRATTO') ";
				}
				SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda",
						numeroDomanda);
				conta = jdbcTemplate.queryForObject(SELECT_DOMANDA_SINGOLO_CAMPO + finequery, namedParameters,
						Integer.class);
				if (conta > 0) {
					listaCampiOut.add(campi);
				}
				finequery = "";
			}
			return listaCampiOut;
			// return listaCampi;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_LISTA_CAMPI_RETTIFICA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public ModelRichiesta selectNumeroRichiesta(String numeroRichiesta) throws DatabaseException {
		ModelRichiesta richieste = new ModelRichiesta();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			richieste = jdbcTemplate.queryForObject(SELECT_NUMERO_RICHIESTA, namedParameters,
					new DettaglioRichiestaMapper());
			return richieste;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_NUMERO_RICHIESTA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelAllegato> selectAllegatiFromNumeroRichiesta(BigDecimal domandaDetId) throws DatabaseException {

		List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
		try {
			SqlParameterSource namedParametersAllegati = new MapSqlParameterSource().addValue("idDomanda",
					domandaDetId);
			allegati = jdbcTemplate.query(SELECT_ALLEGATI, namedParametersAllegati, new AllegatoTipoMapper());

			return allegati;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ALLEGATI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long insertTDatoDaModificare(BigDecimal allegatoId, BigDecimal idSportello, BigDecimal domandaDetId,
			String codFiscale, BigDecimal campoModificabileId) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("allegatoid", allegatoId, Types.BIGINT);
			params.addValue("sportelloid", idSportello, Types.BIGINT);
			params.addValue("domandadetid", domandaDetId, Types.BIGINT);
			params.addValue("campomodificabileid", campoModificabileId, Types.BIGINT);
			params.addValue("utenteCrea", codFiscale, Types.VARCHAR);
			params.addValue("utenteModifica", codFiscale, Types.VARCHAR);

			jdbcTemplate.update(INSERT_T_DATO_DA_MODIFICARE, params, keyHolder,
					new String[] { "dato_da_modificare_id" });
			return keyHolder.getKey().longValue();
		} catch (Exception e) {
			String methodName = "INSERT_T_DATO_DA_MODIFICARE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long DeleteTDatoDaModificare(BigDecimal domandaDetId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandadetid", domandaDetId);
		try {
			return jdbcTemplate.update(DELETE_DATO_DA_MODIFICARE, namedParameters);
		} catch (Exception e) {
			String methodName = "DELETE_DATO_DA_MODIFICARE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateDettaglioDomanda(long idDettaglio, String detCod) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idDomanda", idDettaglio, Types.BIGINT).addValue("detCod", detCod, Types.VARCHAR);
			return jdbcTemplate.update(UPDATE_DET_COD, params);
		} catch (Exception e) {
			String methodName = "UPDATE_DET_COD";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateNoteDettaglio(Long idDettaglio, String notacittadino, String notainterna)
			throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idDomanda", idDettaglio, Types.BIGINT).addValue("notainterna", notainterna, Types.VARCHAR)
					.addValue("notacittadino", notacittadino, Types.VARCHAR);
			return jdbcTemplate.update(UPDATE_NOTE_DETTAGLIO_DOMANDA, params);
		} catch (Exception e) {
			String methodName = "UPDATE_NOTE_DETTAGLIO_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelUpdateCronologia insertTDettaglioDomandaCambioStato(ModelRichiesta richiesta, String codStato,
			String codFiscale, String notacittadino, String notainterna) throws DatabaseException {
		try {
			ModelUpdateCronologia result = new ModelUpdateCronologia();
			KeyHolder keyHolder = new GeneratedKeyHolder();
			MapSqlParameterSource params = new MapSqlParameterSource();

			params.addValue("domandastatocod", codStato, Types.VARCHAR);
			params.addValue("domandadetid", richiesta.getDomandaDetId(), Types.BIGINT);
			params.addValue("utenteCreazione", codFiscale, Types.VARCHAR);
			params.addValue("utenteModifica", codFiscale, Types.VARCHAR);

			jdbcTemplate.update(INSERT_DETTAGLIO_DOMANDA, params, keyHolder, new String[] { "domanda_det_id" });
			long idDettaglio = keyHolder.getKey().longValue();
			updateDettaglioDomanda(idDettaglio, codStato + "_" + idDettaglio);
			// inserisco le note
			if (Util.isValorizzato(notainterna) || Util.isValorizzato(notacittadino)) {
				updateNoteDettaglio(idDettaglio, notacittadino, notainterna);
			}
			result.setDetCod(codStato + "_" + idDettaglio);
			result.setIdDettaglio(idDettaglio);

			return result;
		} catch (Exception e) {
			String methodName = "INSERT_DETTAGLIO_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long salvaIsee(BigDecimal idDettaglio, ModelIsee isee, String codFiscale) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("iseevalore", isee.getIseeValore(), Types.NUMERIC);
			params.addValue("iseedatarilascio", isee.getIseeDataRilascio(), Types.DATE);
			params.addValue("utentemodifica", codFiscale, Types.VARCHAR);
			params.addValue("iseescadenza", isee.getIseeScadenza(), Types.DATE);
			params.addValue("iseeverificatoconforme", isee.isIseeVerificatoConforme(), Types.BOOLEAN);
			params.addValue("idDomanda", idDettaglio, Types.BIGINT);

			return jdbcTemplate.update(UPDATE_ISEE_DETTAGLIO_DOMANDA, params);
		} catch (Exception e) {
			String methodName = "UPDATE_ISEE_DETTAGLIO_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long salvaNota(BigDecimal idDettaglio, String nota, String codFiscale) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("notainterna", nota, Types.VARCHAR);
			params.addValue("utentemodifica", codFiscale, Types.VARCHAR);
			params.addValue("idDomanda", idDettaglio, Types.BIGINT);

			return jdbcTemplate.update(UPDATE_NOTA_INTERNA_DETTAGLIO_DOMANDA, params);
		} catch (Exception e) {
			String methodName = "UPDATE_NOTA_INTERNA_DETTAGLIO_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateDataFineValDettaglioDomanda(BigDecimal idDettaglio) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idDomanda", idDettaglio, Types.BIGINT);
			return jdbcTemplate.update(UPDATE_DATA_FINE_VAL_DETTAGLIO_DOMANDA, params);
		} catch (Exception e) {
			String methodName = "UPDATE_DATA_FINE_VAL_DETTAGLIO_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateDataDomandaValDomanda(String numeroRichiesta, String codFiscale) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("numeroRichiesta", numeroRichiesta, Types.VARCHAR);
			params.addValue("codFiscale", codFiscale, Types.VARCHAR);
			return jdbcTemplate.update(UPDATE_DATA_DOMANDA_VAL_DOMANDA, params);
		} catch (Exception e) {
			String methodName = "UPDATE_DATA_DOMANDA_VAL_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public long updateAtecoDettaglioDomanda(BigDecimal idDettaglio, String atecoCod, String atecoDesc,
			String codFiscale) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("atecoCod", atecoCod, Types.VARCHAR);
			params.addValue("atecoDesc", atecoDesc, Types.VARCHAR);
			params.addValue("codFiscale", codFiscale, Types.VARCHAR);
			params.addValue("idDettaglio", idDettaglio, Types.BIGINT);
			return jdbcTemplate.update(UPDATE_ATECO_COD_DETTAGLIO, params);
		} catch (Exception e) {
			String methodName = "UPDATE_ATECO_COD_DETTAGLIO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelVisualizzaCronologia> selectCronologia(String numeroRichiesta) throws DatabaseException {
		List<ModelVisualizzaCronologia> cronologia = new ArrayList<ModelVisualizzaCronologia>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			cronologia = jdbcTemplate.query(SELECT_CRONOLOGIA, namedParameters, new ModelVisualizzaCroMapper());
			return cronologia;
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		} catch (Exception e) {
			String methodName = "SELECT_CRONOLOGIA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelVerificheDomanda> selectVerifiche(String numeroRichiesta) throws DatabaseException {
		List<ModelVerificheDomanda> verifiche = new ArrayList<ModelVerificheDomanda>();
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			verifiche = jdbcTemplate.query(SELECT_VERIFICHE, namedParameters, new ModelVerificheMapper());
			return verifiche;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_VERIFICHE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public BigDecimal selectAllegatoId(BigDecimal domandaDetId, BigDecimal allegatoId) throws DatabaseException {
		BigDecimal newAllegatoId = null;
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("domandaDetId", domandaDetId)
				.addValue("allegatoId", allegatoId);
		try {
			newAllegatoId = jdbcTemplate.queryForObject(SELECT_ALLEGATO_ID, namedParameters, BigDecimal.class);
			return newAllegatoId;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ALLEGATO_ID";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public ModelEmailDetId selectEmailDetId(String numeroRichiesta) throws DatabaseException {
		ModelEmailDetId richieste = new ModelEmailDetId();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			richieste = jdbcTemplate.queryForObject(SELECT_EMAIL_DOMANDA_DET_ID, namedParameters,
					new EmailDetIdMapper());
			return richieste;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_EMAIL_DOMANDA_DET_ID";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

}
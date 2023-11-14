/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.integration.dao.custom;

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
import org.springframework.util.StringUtils;

import it.csi.buonodom.buonodombff.dto.ModelAllegato;
import it.csi.buonodom.buonodombff.dto.ModelBozzaRichiesta;
import it.csi.buonodom.buonodombff.dto.ModelRichiesta;
import it.csi.buonodom.buonodombff.dto.custom.ModelRichiestaExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelRichiesteExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelUpdateCronologia;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.integration.dao.utils.AllegatoTipoMapper;
import it.csi.buonodom.buonodombff.integration.dao.utils.DettaglioRichiestaExtMapper;
import it.csi.buonodom.buonodombff.integration.dao.utils.DettaglioRichiestaMapper;
import it.csi.buonodom.buonodombff.integration.dao.utils.RichiesteMapper;
import it.csi.buonodom.buonodombff.util.LoggerUtil;
import it.csi.buonodom.buonodombff.util.Util;

@Repository
public class RichiesteDao extends LoggerUtil {

	public static final String SELECT_RICHIESTE = "select " + "	btd.domanda_numero, " + "	bdds.domanda_stato_cod, "
			+ "	bdds.domanda_stato_desc, " + "	btdd.data_creazione, " + "	btd.richiedente_cf, "
			+ "	btdd.richiedente_nome, " + "	btdd.richiedente_cognome, " + "	btdd.richiedente_nascita_data, "
			+ "	btdd.richiedente_nascita_stato, " + "	btdd.richiedente_nascita_comune, "
			+ "	btdd.richiedente_nascita_provincia, "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.richiedente_residenza_indirizzo::bytea,'@keyEncrypt@'),'UTF8') as richiedente_residenza_indirizzo, "
			+ "	btdd.richiedente_residenza_comune, " + "	btdd.richiedente_residenza_provincia, "
			+ "	btd.beneficiario_cf, " + "	btdd.destinatario_nome, " + "	btdd.destinatario_cognome, "
			+ "	btdd.destinatario_nascita_data, " + "	btdd.destinatario_nascita_stato, "
			+ "	btdd.destinatario_nascita_comune, " + "	btdd.destinatario_nascita_provincia, "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_residenza_indirizzo::bytea,'@keyEncrypt@'),'UTF8') as destinatario_residenza_indirizzo, "
			+ "	btdd.destinatario_residenza_comune, " + "	btdd.destinatario_residenza_provincia, " + "	btdd.note, "
			+ "	btdd.protocollo_cod, " + "	btdd.data_protocollo, " + "	btdd.tipo_protocollo, "
			+ " case when btb.buono_id is not null then true " + " else false " + "	end esistebuono " + " from "
			+ "	bdom_t_domanda_dettaglio btdd, " + "	bdom_d_domanda_stato bdds, " + "	bdom_t_domanda btd "
			+ " left join bdom_t_buono btb on  btb.domanda_id = btd.domanda_id and btb.sportello_id = btd.sportello_id and btb.data_cancellazione is null "
			+ " where " + "	btd.richiedente_cf = :codFiscale " + "	and " + "	btd.data_cancellazione is null "
			+ "	and  " + "	btdd.domanda_id = btd.domanda_id  " + "	and  " + "	btdd.data_cancellazione is null  "
			+ "	and  " + "	btdd.validita_inizio <= now() " + " and  "
			+ "	bdds.domanda_stato_id  =btdd.domanda_stato_id " + "	and " + "	btdd.validita_fine is null ";

	public static final String SELECT_NUMERO_RICHIESTA = "select " + "	btd.domanda_numero, "
			+ "	bdds.domanda_stato_cod, " + "	bdds.domanda_stato_desc, " + "	btdd.data_creazione , "
			+ "	btd.richiedente_cf, " + "	btdd.richiedente_nome, " + "	btdd.richiedente_cognome, "
			+ "	btdd.richiedente_nascita_data, " + "	btdd.richiedente_nascita_stato, "
			+ "	btdd.richiedente_nascita_comune, " + "	btdd.richiedente_nascita_provincia, "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.richiedente_residenza_indirizzo::bytea,'@keyEncrypt@'),'UTF8') as richiedente_residenza_indirizzo, "
			+ "	btdd.richiedente_residenza_comune, " + "	btdd.richiedente_residenza_provincia, "
			+ "	btd.beneficiario_cf, " + "	btdd.destinatario_nome, " + "	btdd.destinatario_cognome, "
			+ "	btdd.destinatario_nascita_data, " + "	btdd.destinatario_nascita_stato, "
			+ "	btdd.destinatario_nascita_comune, " + "	btdd.destinatario_nascita_provincia, "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_residenza_indirizzo::bytea,'@keyEncrypt@'),'UTF8') as destinatario_residenza_indirizzo, "
			+ "	btdd.destinatario_residenza_comune, " + "	btdd.destinatario_residenza_provincia, "
			+ " convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_domicilio_indirizzo::bytea,'@keyEncrypt@'),'UTF8') as destinatario_domicilio_indirizzo, "
			+ "	btdd.destinatario_domicilio_comune, " + "	btdd.destinatario_domicilio_provincia," + "	btdd.note, "
			+ "	bdts.titolo_studio_cod, " + "	bda.asl_cod,  " + "	btdd.punteggio_sociale, "
			+ "	bdrt.rapporto_tipo_cod as delega, " + "	btdd.isee_valore, " + "	bdct.contratto_tipo_cod, "
			+ "	btdd.datore_di_lavoro_cf, " + "	btdd.datore_di_lavoro_nome, " + "	btdd.datore_di_lavoro_cognome, "
			+ "	btdd.datore_di_lavoro_nascita_data, " + "	btdd.datore_di_lavoro_nascita_stato, "
			+ "	btdd.datore_di_lavoro_nascita_comune, " + "	btdd.datore_di_lavoro_nascita_provincia, "
			+ "	btdd.assistente_familiare_cf, " + "	btdd.assistente_familiare_nome, "
			+ "	btdd.assistente_familiare_cognome, " + "	btdd.assistente_familiare_nascita_stato, "
			+ "	btdd.contratto_cf_cooperativa,  " + "	btdd.incompatibilita_per_contratto, " + "	btdd.iban, "
			+ "	btdd.iban_intestatario," + "	btdd.isee_conforme, "
			+ " relazione.rapporto_tipo_cod as relazione_destinatario," + " btdd.nessuna_incompatibilita,"
			+ " btdd.situazione_lavorativa_attiva, " + " bdvm.valutazione_multidimensionale_cod, "
			+ "	btdd.assistente_familiare_nascita_comune, " + "	btdd.assistente_familiare_nascita_data,  "
			+ "	btdd.assistente_familiare_nascita_provincia, " + "	btdd.contratto_data_inizio, "
			+ "	btdd.contratto_data_fine, " + "	btdd.protocollo_cod, " + "	btdd.data_protocollo, "
			+ "	btdd.tipo_protocollo, " + "	btdd.assistente_familiare_piva, " + "	btdd.note_richiedente, "
			+ "	bdat.assistente_tipo_cod " + "	from " + "	bdom_t_domanda btd, " + "	bdom_t_domanda_dettaglio btdd  "
			+ "	left join bdom_d_rapporto_tipo bdrt on  btdd.rapporto_tipo_id = bdrt.rapporto_tipo_id "
			+ " left join bdom_d_rapporto_tipo relazione on  btdd.relazione_tipo_id = relazione.rapporto_tipo_id "
			+ "	left join bdom_d_domanda_stato bdds on bdds.domanda_stato_id  = btdd.domanda_stato_id "
			+ "	left join bdom_d_contratto_tipo bdct on btdd.contratto_tipo_id = bdct.contratto_tipo_id "
			+ " left join bdom_d_assistente_tipo bdat on btdd.assistente_tipo_id= bdat.assistente_tipo_id "
			+ " left join bdom_d_valutazione_multidimensionale bdvm on btdd.valutazione_multidimensionale_id= bdvm.valutazione_multidimensionale_id,   "
			+ "	bdom_d_titolo_studio bdts, " + "	bdom_d_asl bda " + "	where " + "	btd.data_cancellazione is null "
			+ "	and  " + "	btdd.domanda_id = btd.domanda_id  " + "	and  " + "	btdd.data_cancellazione is null  "
			+ "	and  " + "	btdd.validita_inizio <= now() " + "	and " + "	btdd.validita_fine is null   " + "	and  "
			+ "	btdd.titolo_studio_id=bdts.titolo_studio_id  " + "	and  " + "	btdd.asl_id = bda.asl_id 	 "
			+ "	and " + "	btd.domanda_numero= :numeroDomanda";

	public static final String SELECT_ALLEGATI = "select bta.allegato_id, " + "	bdat.allegato_tipo_cod,"
			+ " bta.file_name " + "	from  " + "	bdom_t_domanda_dettaglio btdd, " + "	bdom_d_allegato_tipo bdat, "
			+ "	bdom_t_allegato bta  " + "	where  " + "	btdd.domanda_det_id = bta.domanda_det_id " + "	and  "
			+ "	btdd.sportello_id = bta.sportello_id " + "	and  " + "	bta.allegato_tipo_id = bdat.allegato_tipo_id "
			+ "	and btdd.domanda_det_id = :idDomanda" + " and btdd.validita_inizio <= now() "
			+ " and btdd.validita_fine is null " + " and btdd.data_cancellazione is null "
			+ " and bta.data_cancellazione is null ";

	public static final String SELECT_ID_DOMANDA = "select " + " btdd.domanda_det_id   "
			+ " from bdom_t_domanda_dettaglio btdd, " + " bdom_t_domanda btd  " + " where  "
			+ " btdd.domanda_id = btd.domanda_id " + " and  " + " btd.domanda_numero = :numeroRichiesta "
			+ " and btd.data_cancellazione is null " + " and btdd.validita_inizio <= now() "
			+ " and btdd.validita_fine is null " + " and btdd.data_cancellazione is null";

	public static final String SELECT_DETCOD_DOMANDA = "select " + " btdd.domanda_det_cod   "
			+ " from bdom_t_domanda_dettaglio btdd, " + " bdom_t_domanda btd  " + " where  "
			+ " btdd.domanda_id = btd.domanda_id " + " and  " + " btd.domanda_numero = :numeroRichiesta "
			+ " and btd.data_cancellazione is null " + " and btdd.validita_inizio <= now() "
			+ " and btdd.validita_fine is null " + " and btdd.data_cancellazione is null";

	public static final String SELECT_NUMERO_DOMANDA_PRINCIPALE = "select " + " btd.domanda_numero  " + " from  "
			+ " bdom_t_domanda btd  " + " where  " + " btd.domanda_id = :idDomanda"
			+ " and btd.data_cancellazione is null ";

	public static final String SELECT_NOME_CAMPO_DA_MODIFICARE = "select distinct a.riferimento_yaml_campo from "
			+ "bdom_d_campo_modificabile a, " + "bdom_t_dato_da_modificare b, " + "bdom_t_domanda_dettaglio c, "
			+ "bdom_t_domanda d " + "where c.domanda_det_id = b.domanda_det_id "
			+ "and d.domanda_numero  = :numeroRichiesta " + "and b.campo_modificabile_id = a.campo_modificabile_id "
			+ "and b.sportello_id = c.sportello_id " + "and d.domanda_id  = c.domanda_id "
			+ "and a.data_cancellazione is null " + "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and d.data_cancellazione is null "
			+ "and a.validita_inizio <= now() " + "and a.validita_fine is null " + "and c.validita_inizio <= now() "
			+ "and c.validita_fine is null " + "union " + "select distinct a.riferimento_yaml_campo from "
			+ "bdom_d_allegato_tipo_modificabile a, " + "bdom_t_dato_da_modificare b, " + "bdom_t_domanda_dettaglio c, "
			+ "bdom_t_domanda d, " + "bdom_t_allegato e " + "where c.domanda_det_id = b.domanda_det_id "
			+ "and d.domanda_numero  = :numeroRichiesta " + "and b.allegato_id  = e.allegato_id "
			+ "and a.allegato_tipo_id=e.allegato_tipo_id " + "and e.domanda_det_id = c.domanda_det_id "
			+ "and b.sportello_id = c.sportello_id " + "and d.domanda_id  = c.domanda_id "
			+ "and a.data_cancellazione is null " + "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and d.data_cancellazione is null "
			+ "and e.data_cancellazione is null " + "and a.validita_inizio <= now() " + "and a.validita_fine is null "
			+ "and c.validita_inizio <= now() " + "and c.validita_fine is null";

	public static final String SELECT_ID_STATO = "select bdds.domanda_stato_id " + "from bdom_d_domanda_stato bdds "
			+ "where bdds.domanda_stato_cod = :codStato " + "AND bdds.data_cancellazione is null "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_CONTRIBUTO = "select bdct.contributo_tipo_id "
			+ "from bdom_d_contributo_tipo bdct " + "where bdct.contributo_tipo_cod = :codContributo "
			+ "AND bdct.data_cancellazione is null "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String DOMANDA_PRESENTE = "select distinct bdds.domanda_stato_cod not in ('DINIEGO', 'ANNULLATA', 'RINUNCIATA') "
			+ "from bdom_t_domanda_dettaglio btdd, " + "bdom_t_domanda btd, " + "bdom_d_domanda_stato bdds  "
			+ "where  " + "btdd.domanda_id = btd.domanda_id  " + "AND "
			+ "bdds.domanda_stato_id = btdd.domanda_stato_id  " + "and " + "btd.beneficiario_cf = :cfDestinatario "
			+ "and  " + "btd.data_cancellazione is null " + "and  " + "btdd.data_cancellazione is null  " + "and "
			+ "bdds.data_cancellazione is null " + "and " + "btdd.validita_inizio <= now() " + "and  "
			+ "btdd.validita_fine is null " + "and  "
			+ "bdds.domanda_stato_cod not in ('DINIEGO', 'ANNULLATA', 'RINUNCIATA') ";

	public static final String SELECT_ID_SPORTELLI = "select " + " spo.sportello_id idSportello  "
			+ "from bdom_t_sportello spo,bdom_d_contributo_tipo con  "
			+ "where spo.contributo_tipo_id =con.contributo_tipo_id   " + "and spo.data_cancellazione is null   "
			+ "and con.data_cancellazione is null  " + "and con.contributo_tipo_cod = 'DOM'  "
			+ "and  now()::date BETWEEN spo.validita_inizio::date and COALESCE(spo.validita_fine::date, now()::date) ";

	public static final String SELECT_ID_TITOLO = "select bdts.titolo_studio_id  " + "from "
			+ "	bdom_d_titolo_studio bdts " + "where " + "	bdts.data_cancellazione is null " + "	and "
			+ "	bdts.titolo_studio_cod = :codTitolo "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_RAPPORTO = "select bdrt.rapporto_tipo_id " + "from "
			+ "	bdom_d_rapporto_tipo bdrt  " + "where " + "	bdrt.data_cancellazione is null " + "	and "
			+ "	bdrt.rapporto_tipo_cod = :codRapporto "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_VALUTAZIONE_MULTIDIMENSIONALE = "select bdrt.valutazione_multidimensionale_id "
			+ "from " + "	bdom_d_valutazione_multidimensionale bdrt  " + "where "
			+ "	bdrt.data_cancellazione is null " + "	and "
			+ "	bdrt.valutazione_multidimensionale_cod = :codValutazioneMultidimensionale "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_CONTRATTO = "select bdrt.contratto_tipo_id " + "from "
			+ "	bdom_d_contratto_tipo bdrt  " + "where " + "	bdrt.data_cancellazione is null " + "	and "
			+ "	bdrt.contratto_tipo_cod = :codContratto "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_ASL = "	select " + "	bda.asl_id  " + "from " + "	bdom_d_asl bda "
			+ "where " + "	bda.data_cancellazione is null " + "	and " + "	bda.asl_cod = :codAsl "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_DETTAGLIO_DOMANDA = "select btdd.domanda_det_id  "
			+ "from bdom_t_domanda_dettaglio btdd, " + "bdom_t_domanda btd  " + "where "
			+ "btd.domanda_numero = :numeroDomanda " + "and btd.domanda_id = btdd.domanda_id  "
			+ "and btdd.validita_inizio <= now() " + "and btdd.validita_fine is null  "
			+ "and btdd.data_cancellazione is null  " + "and btd.data_cancellazione is null;";

	// non utilizzato per nuova tabella provincia
	public static final String RESIDENTE_PIEMONTE = "select :codProvincia in ((select distinct bdc.provincia_sigla_automobilistica as provincia "
			+ "from bdom_d_comune bdc  " + "where bdc.data_cancellazione is null  "
			+ "and bdc.validita_inizio <= now() " + "and bdc.validita_fine is null "
			+ "and bdc.regione_istat_cod = '1'))";

	public static final String SELECT_COD_CONTRIBUTO = "select bdct.contributo_tipo_cod "
			+ "from bdom_d_contributo_tipo bdct " + "where bdct.contributo_tipo_cod = :codContributo "
			+ "AND bdct.data_cancellazione is null "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_COD_STATO = "select bdds.domanda_stato_cod " + "from bdom_d_domanda_stato bdds "
			+ "where bdds.domanda_stato_cod = :codStato " + "AND bdds.data_cancellazione is null "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String INSERT_T_DOMANDA = "insert " + "	into " + "	bdom_t_domanda " + "("
			+ "	richiedente_cf, " + "	beneficiario_cf, " + "	contributo_tipo_id, " + "	sportello_id, "
			+ "	data_creazione, " + "	data_modifica, " + "	utente_creazione, " + "	utente_modifica) " + "values("
			+ ":cfRichiedente, " + ":cfDestinatario, " + ":idTipoContributo, " + ":idSportello, " + "now(), "
			+ "now(), " + ":utenteCrea, " + ":utenteModifica)";

	public static final String INSERT_T_DOMANDA_DETTAGLIO = "insert " + "	into " + "	bdom_t_domanda_dettaglio "
			+ "(domanda_id, " + "	sportello_id, " + "	domanda_stato_id, " + "	richiedente_nome, "
			+ "	richiedente_cognome, " + "	richiedente_nascita_data, " + "	richiedente_nascita_stato, "
			+ "	richiedente_nascita_comune, " + "	richiedente_nascita_provincia, "
			+ "	richiedente_residenza_indirizzo, " + "	richiedente_residenza_comune, "
			+ "	richiedente_residenza_provincia, " + "	destinatario_nome, " + "	destinatario_cognome, "
			+ "	destinatario_nascita_data, " + "	destinatario_nascita_comune, "
			+ "	destinatario_nascita_provincia, " + "	destinatario_nascita_stato, "
			+ "	destinatario_residenza_indirizzo, " + "	destinatario_residenza_comune, "
			+ "	destinatario_residenza_provincia, " + "	destinatario_domicilio_indirizzo, "
			+ "	destinatario_domicilio_comune, " + "	destinatario_domicilio_provincia, "
			+ "	situazione_lavorativa_attiva, " + "	note, " + "	rapporto_tipo_id, " + "	titolo_studio_id, "
			+ "	asl_id, " + "	validita_inizio, " + "	data_creazione, " + "	data_modifica, "
			+ "	utente_creazione, " + "	utente_modifica,area_id) " + "values(:idDomanda, " + ":idSportello, "
			+ ":idStatoDomanda, " + ":nomeRichiedente, " + ":cognomeRichiedente, " + ":dataNascitaRichiedente, "
			+ ":statoNascitaRichiedente, " + ":comuneNascitaRichiedente, " + ":provinciaNascitaRichiedente, "
			+ "pgp_sym_encrypt(:indirizzoResidenzaRichiedente, '@keyEncrypt@')::bytea, "
			+ ":comuneResidenzaRichiedente, " + ":provinciaResidenzaRichiedente, " + ":nomeDestinatario, "
			+ ":cognomeDestinatario, " + ":dataNascitaDestinatario, " + ":comuneNascitaDestinatario, "
			+ ":provinciaNascitaDestinatario, " + ":statoNascitaDestinatario, "
			+ "pgp_sym_encrypt(:indirizzoResidenzaDestinatario, '@keyEncrypt@')::bytea, "
			+ ":comuneResidenzaDestinatario, " + ":provinciaResidenzaDestinatario, "
			+ "pgp_sym_encrypt(:indirizzoDomicilioDestinatario, '@keyEncrypt@')::bytea, "
			+ ":comuneDomicilioDestinatario, " + ":provinciaDomicilioDestinatario, " + ":situazioneLavorativa, "
			+ ":note, " + ":idTipoRapporto, " + ":idTitoloStudio, " + ":idAsl, " + "now(), " + "now(), " + "now(), "
			+ ":utenteCrea, "
			+ ":utenteModifica,(select distinct b.area_id from bdom_d_comune a, bdom_d_area b, bdom_r_area_comune c "
			+ "where a.comune_id = c.comune_id " + "and b.area_id=c.area_id "
			+ "and upper(a.comune_desc)=upper(:comuneResidenzaDestinatario) "
			+ "and upper(a.provincia_sigla_automobilistica)=upper(:provinciaResidenzaDestinatario)))";

	public static final String UPDATE_DET_COD = "UPDATE bdom_t_domanda_dettaglio " + "SET domanda_det_cod=:detCod "
			+ "WHERE domanda_det_id=:idDomanda";

	public static final String SELECT_NUMERO_RICHIESTA_COD = "select " + " btdd.domanda_det_id, " + " btd.domanda_id, "
			+ " btdd.sportello_id, " + "	btd.domanda_numero, " + " btdd.domanda_det_cod, "
			+ "	bdds.domanda_stato_cod, " + "	bdds.domanda_stato_desc, " + "	btdd.data_creazione , "
			+ "	btd.richiedente_cf, " + "	btdd.richiedente_nome, " + "	btdd.richiedente_cognome, "
			+ "	btdd.richiedente_nascita_data, " + "	btdd.richiedente_nascita_stato, "
			+ "	btdd.richiedente_nascita_comune, " + "	btdd.richiedente_nascita_provincia, "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.richiedente_residenza_indirizzo::bytea,'@keyEncrypt@'),'UTF8') as richiedente_residenza_indirizzo, "
			+ "	btdd.richiedente_residenza_comune, " + "	btdd.richiedente_residenza_provincia, "
			+ "	btd.beneficiario_cf, " + "	btdd.destinatario_nome, " + "	btdd.destinatario_cognome, "
			+ "	btdd.destinatario_nascita_data, " + "	btdd.destinatario_nascita_stato, "
			+ "	btdd.destinatario_nascita_comune, " + "	btdd.destinatario_nascita_provincia, "
			+ "	convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_residenza_indirizzo::bytea,'@keyEncrypt@'),'UTF8') as destinatario_residenza_indirizzo, "
			+ "	btdd.destinatario_residenza_comune, " + "	btdd.destinatario_residenza_provincia, "
			+ " convert_from(pgp_sym_decrypt_bytea(btdd.destinatario_domicilio_indirizzo::bytea,'@keyEncrypt@'),'UTF8') as destinatario_domicilio_indirizzo, "
			+ "	btdd.destinatario_domicilio_comune, " + "	btdd.destinatario_domicilio_provincia," + "	btdd.note, "
			+ "	bdts.titolo_studio_cod, " + "	bda.asl_cod,  " + "	btdd.punteggio_sociale, "
			+ "	bdrt.rapporto_tipo_cod as delega, " + "	btdd.isee_valore, " + "	bdct.contratto_tipo_cod, "
			+ "	btdd.datore_di_lavoro_cf, " + "	btdd.datore_di_lavoro_nome, " + "	btdd.datore_di_lavoro_cognome, "
			+ "	btdd.datore_di_lavoro_nascita_data, " + "	btdd.datore_di_lavoro_nascita_stato, "
			+ "	btdd.datore_di_lavoro_nascita_comune, " + "	btdd.datore_di_lavoro_nascita_provincia, "
			+ "	btdd.assistente_familiare_cf, " + "	btdd.assistente_familiare_nome, "
			+ "	btdd.assistente_familiare_cognome, " + "	btdd.assistente_familiare_nascita_stato, "
			+ "	btdd.contratto_cf_cooperativa,  " + "	btdd.incompatibilita_per_contratto,  " + "	btdd.iban, "
			+ "	btdd.iban_intestatario," + " relazione.rapporto_tipo_cod as relazione_destinatario,"
			+ " btdd.nessuna_incompatibilita," + " btdd.situazione_lavorativa_attiva, "
			+ " bdvm.valutazione_multidimensionale_cod, " + "	btdd.assistente_familiare_nascita_comune, "
			+ "	btdd.assistente_familiare_nascita_data,  " + "	btdd.assistente_familiare_nascita_provincia, "
			+ "	btdd.contratto_data_inizio, " + "	btdd.contratto_data_fine," + "	btdd.isee_conforme, "
			+ "	btdd.assistente_familiare_piva, " + "	btdd.note_richiedente, " + "	bdat.assistente_tipo_cod "
			+ "	from " + "	bdom_t_domanda btd, " + "	bdom_t_domanda_dettaglio btdd  "
			+ "	left join bdom_d_rapporto_tipo bdrt on  btdd.rapporto_tipo_id = bdrt.rapporto_tipo_id "
			+ " left join bdom_d_rapporto_tipo relazione on  btdd.relazione_tipo_id = relazione.rapporto_tipo_id "
			+ "	left join bdom_d_domanda_stato bdds on bdds.domanda_stato_id  = btdd.domanda_stato_id "
			+ "	left join bdom_d_contratto_tipo bdct on btdd.contratto_tipo_id = bdct.contratto_tipo_id "
			+ " left join bdom_d_valutazione_multidimensionale bdvm on btdd.valutazione_multidimensionale_id= bdvm.valutazione_multidimensionale_id "
			+ " left join bdom_d_assistente_tipo bdat on btdd.assistente_tipo_id= bdat.assistente_tipo_id, "
			+ "	bdom_d_titolo_studio bdts, " + "	bdom_d_asl bda " + "	where " + "	btd.data_cancellazione is null "
			+ "	and  " + "	btdd.domanda_id = btd.domanda_id  " + "	and  " + "	btdd.data_cancellazione is null  "
			+ "	and  " + "	btdd.validita_inizio <= now() " + "	and " + "	btdd.validita_fine is null   " + "	and  "
			+ "	btdd.titolo_studio_id=bdts.titolo_studio_id  " + "	and  " + "	btdd.asl_id = bda.asl_id 	 "
			+ "	and " + "	btd.domanda_numero= :numeroDomanda";

	public static final String UPDATE_T_DETTAGLIO_DOMANDA = "update " + "	buonodom.bdom_t_domanda_dettaglio " + "set "
			+ "	punteggio_sociale = :punteggioSociale, " + "	nessuna_incompatibilita = :nessunaIncompatibilita, "
			+ "	incompatibilita_per_contratto = :incompatibilitaPerContratto, "
			+ "	contratto_cf_cooperativa = :contrattoCfCooperativa, "
			+ "	assistente_familiare_nome = :assistenteFamiliareNome, "
			+ "	assistente_familiare_cognome = :assistenteFamiliareCognome, "
			+ "	assistente_familiare_cf = :assistenteFamiliareCf, "
			+ "	assistente_familiare_nascita_stato = :assistenteFamiliareNascitaStato, "
			+ "	destinatario_domicilio_indirizzo = pgp_sym_encrypt(:indirizzoDomicilioDestinatario, '@keyEncrypt@')::bytea, "
			+ "	destinatario_domicilio_comune = :comuneDomicilioDestinatario, "
			+ "	destinatario_domicilio_provincia = :provinciaDomicilioDestinatario, "
			+ "	situazione_lavorativa_attiva = :situazioneLavorativa, " + "	datore_di_lavoro_nome = :datoreNome, "
			+ "	datore_di_lavoro_cognome = :datoreCognome, " + "	datore_di_lavoro_cf = :datoreCf, "
			+ "	datore_di_lavoro_nascita_data = :datoreNascitaData, "
			+ "	datore_di_lavoro_nascita_comune = :datoreNascitaComune, "
			+ "	datore_di_lavoro_nascita_provincia = :datoreNascitaProvincia, "
			+ "	datore_di_lavoro_nascita_stato = :datoreNascitaStato, " + "	iban = :iban, "
			+ "	iban_intestatario = :ibanIntestatario, " + "	note = :note, "
			+ "	note_richiedente = :noterichiedente, " + "	contratto_tipo_id = :idContratto, "
			+ "	rapporto_tipo_id = :idRapporto, " + "	titolo_studio_id = :idTitoloStudio, " + "	asl_id = :idAsl, "
			+ "	data_modifica = now(), " + "	utente_modifica = :utenteModifica, "
			+ "	relazione_tipo_id = :idRelazione, " + "	isee_conforme = :iseeConforme, "
			+ "	valutazione_multidimensionale_id = :idValutazione, "
			+ "	assistente_familiare_nascita_comune = :assistenteFamiliareNascitaComune, "
			+ "	assistente_familiare_nascita_data = :assistenteFamiliareNascitaData, "
			+ "	assistente_familiare_nascita_provincia = :assistenteFamiliareNascitaProvincia, "
			+ "	contratto_data_inizio = :dataInizioContratto, " + "	contratto_data_fine = :dataFineContratto, "
			+ "	assistente_familiare_piva = :assistenteFamiliarePiva, " + "	assistente_tipo_id = :assistenteTipoId "
			+ " where " + "	domanda_det_id = :idDettaglio;";

	public static final String UPDATE_DATA_FINE_VAL_DETTAGLIO_DOMANDA = "UPDATE bdom_t_domanda_dettaglio "
			+ "SET validita_fine = now() " + "WHERE domanda_det_id=:idDomanda";

	public static final String UPDATE_DATA_DOMANDA_VAL_DOMANDA = "UPDATE bdom_t_domanda " + "SET domanda_data = now(),"
			+ " data_modifica = now(), " + " utente_modifica = :codFiscale " + "WHERE domanda_numero=:numeroRichiesta";

	private static final String SELECT_PROVINCIA_PIEMONTE = "select count(*) from bdom_d_provincia "
			+ "where provincia_sigla_automobilistica = :sigla " + "and data_cancellazione is null "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

	public static final String SELECT_ID_TIPO_ASSISTENTE = "select bdrt.assistente_tipo_id " + "from "
			+ "	bdom_d_assistente_tipo bdrt  " + "where " + "	bdrt.data_cancellazione is null " + "	and "
			+ "	bdrt.assistente_tipo_cod = :codTipoAssistente "
			+ "and now()::date BETWEEN validita_inizio::date and COALESCE(validita_fine::date, now()::date)";

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
			+ "messageuuid_protocollo, assistente_familiare_piva, assistente_tipo_id, ateco_desc,nota_interna,isee_verificato_conforme,isee_verificato_in_data, "
			+ "ateco_verificato_in_data,note_richiedente,verifica_eg_data, note_regione, note_ente_gestore) "
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

	public static final String UPDATE_CAMPO_DA_MODIFICARE = "update bdom_t_dato_da_modificare set "
			+ "domanda_det_id = :domandadetidnew " + "where domanda_det_id = :domandadetidold ";

	public static final String UPDATE_ALLEGATO_ID_DA_MODIFICARE = "update bdom_t_dato_da_modificare set "
			+ "allegato_id = :allegatoidnew "
			+ "where allegato_id = :allegatoidold and domanda_det_id = :domandadetidnew";

	public static final String SELECT_NOTA_CITTADINO = "with tab1 as (select btdd.note,btdd.domanda_det_id  from "
			+ "bdom_t_domanda btd, " + "bdom_t_domanda_dettaglio btdd " + "where btd.domanda_numero = :numeroDomanda "
			+ "and btd.domanda_id = btdd.domanda_id " + "and note is not null " + "order by 2 desc "
			+ "limit 1) select note from tab1";

	private static final String SELECT_GRADUATORIA_ID_BY_SPORTELLO_ID = "select (case when (select count(btg.graduatoria_id) from bdom_t_graduatoria btg, bdom_r_graduatoria_stato brgs  "
			+ "			where btg.graduatoria_id = brgs.graduatoria_id "
			+ "			and btg.data_cancellazione is null " + "			and brgs.data_cancellazione is null  "
			+ "			and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now()) "
			+ "			and btg.sportello_id = :idSportello) > 0 then "
			+ "(select btg.graduatoria_id from bdom_t_graduatoria btg, bdom_r_graduatoria_stato brgs  "
			+ "			where btg.graduatoria_id = brgs.graduatoria_id "
			+ "			and btg.data_cancellazione is null " + "			and brgs.data_cancellazione is null  "
			+ "			and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now()) "
			+ "			and btg.sportello_id = :idSportello) " + "else 0 end) ";

	private static final String CHECK_STATO_GRADUATORIA_BY_ID = "select ( " + "	case  " + "		when ( "
			+ "			select bdgs.graduatoria_stato_cod  "
			+ "			from bdom_r_graduatoria_stato brgs, bdom_d_graduatoria_stato bdgs "
			+ "			where bdgs.graduatoria_stato_id  = brgs.graduatoria_stato_id "
			+ " 		and now() between coalesce(brgs.validita_inizio, now()) and coalesce(brgs.validita_fine,now())"
			+ "			and brgs.graduatoria_id  = :idGraduatoria " + "		) = :statoGraduatoria " + "		then true "
			+ "		else false " + "	end " + ") as controllo ";

	private static final String UPDATE_R_GRADUATORIA_STATO = "UPDATE bdom_r_graduatoria_stato   "
			+ "set validita_fine = now() "
			+ "where now() between coalesce (validita_inizio, now()) and coalesce(validita_fine,now()) "
			+ "and data_cancellazione is null " + "and graduatoria_id = :idGraduatoria; " + "				 "
			+ "INSERT INTO bdom_r_graduatoria_stato (graduatoria_id, graduatoria_stato_id, utente_creazione, utente_modifica) "
			+ "values (:idGraduatoria, " + "(select graduatoria_stato_id  from bdom_d_graduatoria_stato  "
			+ "where graduatoria_stato_cod = :statoGraduatoria " + "and data_cancellazione is null  "
			+ "and now() between coalesce (validita_inizio, now()) and coalesce(validita_fine,now()) "
			+ "), :utenteCreazione, :utenteModifica) ";

	public static final String SELECT_PRECEDENTE_STATO = "with tab1 as ( "
			+ "select a.domanda_det_cod, max(a.validita_inizio) validita_inizio from bdom_t_domanda_dettaglio a, "
			+ "bdom_t_domanda b,bdom_d_domanda_stato c " + "where a.domanda_id = b.domanda_id "
			+ "and b.domanda_numero  = :numeroDomanda " + "and c.domanda_stato_id = a.domanda_stato_id "
			+ "and a.validita_fine is not null " + "and a.data_cancellazione is null "
			+ "and c.data_cancellazione is null " + "and b.data_cancellazione is null " + "and c.validita_fine is null "
			+ "group by a.domanda_det_cod " + "order by validita_inizio desc " + "limit 1) "
			+ "select domanda_det_cod from tab1";

	public static final String DATA_FINE_INPAGAMENTO = "select btd.richiedente_cf ||';'|| to_char((btdd.validita_inizio + interval '1 months'),'yyyy-mm-01') "
			+ "from " + "bdom_t_domanda btd, " + "bdom_t_domanda_dettaglio btdd, " + "bdom_d_domanda_stato bdds "
			+ "where " + "bdds.domanda_stato_id  = btdd.domanda_stato_id "
			+ "and bdds.domanda_stato_cod ='IN_PAGAMENTO' " + "and btd.data_cancellazione is null "
			+ "and btdd.domanda_id = btd.domanda_id " + "and btdd.data_cancellazione is null "
			+ "and btdd.validita_inizio <= now() " + "and btdd.validita_fine is null "
			+ "and btd.domanda_numero= :numeroDomanda";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public String selectNotaCittadino(String numeroDomanda) throws DatabaseException {
		String nota = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			nota = jdbcTemplate.queryForObject(SELECT_NOTA_CITTADINO, namedParameters, String.class);
			return nota;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_NOTA_CITTADINO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelRichiesteExt> selectRichieste(String codFiscale) throws DatabaseException {
		List<ModelRichiesteExt> richieste = new ArrayList<ModelRichiesteExt>();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codFiscale", codFiscale);
		try {
			richieste = jdbcTemplate.query(SELECT_RICHIESTE, namedParameters, new RichiesteMapper());
			return richieste;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_RICHIESTE";
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
			// prendo ultima nota
			if (!Util.isValorizzato(richieste.getNote())) {
				String nota = selectNotaCittadino(numeroRichiesta);
				if (Util.isValorizzato(nota)) {
					richieste.setNote(nota);
				}
			}
			return richieste;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_NUMERO_RICHIESTA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelAllegato> selectAllegatiFromNumeroRichiesta(String numeroRichiesta) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroRichiesta", numeroRichiesta);
		Long idDomanda = null;
		List<ModelAllegato> allegati = new ArrayList<ModelAllegato>();
		try {
			idDomanda = jdbcTemplate.queryForObject(SELECT_ID_DOMANDA, namedParameters, Long.class);
			SqlParameterSource namedParametersAllegati = new MapSqlParameterSource().addValue("idDomanda", idDomanda);
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

	public List<String> selectRettificareFromNumeroRichiesta(String numeroRichiesta) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroRichiesta", numeroRichiesta);
		List<String> campiModificare = new ArrayList<String>();
		try {
			campiModificare = jdbcTemplate.queryForList(SELECT_NOME_CAMPO_DA_MODIFICARE, namedParameters, String.class);

			return campiModificare;

		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_NOME_CAMPO_DA_MODIFICARE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Long selectIdStato(String codStato) throws DatabaseException {
		Long idStato = null;
		if (StringUtils.isEmpty(codStato)) {
			return idStato;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codStato", codStato);
		try {
			idStato = jdbcTemplate.queryForObject(SELECT_ID_STATO, namedParameters, Long.class);
			return idStato;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_STATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public String selectCodStato(String codStato) throws DatabaseException {
		String cod = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codStato", codStato);
		try {
			cod = jdbcTemplate.queryForObject(SELECT_COD_STATO, namedParameters, String.class);
			return cod;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_COD_STATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Long selectIdSportello() throws DatabaseException {
		Long idSportello = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource();
		try {
			idSportello = jdbcTemplate.queryForObject(SELECT_ID_SPORTELLI, namedParameters, Long.class);
			return idSportello;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_SPORTELLI";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Long selectIdContributo(String codContributo) throws DatabaseException {
		Long idContributo = null;
		if (StringUtils.isEmpty(codContributo)) {
			return idContributo;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codContributo", codContributo);
		try {
			idContributo = jdbcTemplate.queryForObject(SELECT_ID_CONTRIBUTO, namedParameters, Long.class);
			return idContributo;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_CONTRIBUTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public String selectCodContributo(String codContributo) throws DatabaseException {
		String cod = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codContributo", codContributo);
		try {
			cod = jdbcTemplate.queryForObject(SELECT_COD_CONTRIBUTO, namedParameters, String.class);
			return cod;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_COD_CONTRIBUTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Long selectIdTitolo(String codTitolo) throws DatabaseException {
		Long idTitolo = null;
		if (StringUtils.isEmpty(codTitolo)) {
			return idTitolo;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codTitolo", codTitolo);
		try {
			idTitolo = jdbcTemplate.queryForObject(SELECT_ID_TITOLO, namedParameters, Long.class);
			return idTitolo;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_TITOLO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Long selectIdRapporto(String codRapporto) throws DatabaseException {
		Long idRapporto = null;
		if (StringUtils.isEmpty(codRapporto)) {
			return idRapporto;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codRapporto", codRapporto);
		try {
			idRapporto = jdbcTemplate.queryForObject(SELECT_ID_RAPPORTO, namedParameters, Long.class);
			return idRapporto;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_RAPPORTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Long selectIdValutazioneMultidimensionale(String codValutazioneMultidimensionale) throws DatabaseException {
		Long idValutazioneMultidimensionale = null;
		if (StringUtils.isEmpty(codValutazioneMultidimensionale)) {
			return idValutazioneMultidimensionale;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codValutazioneMultidimensionale",
				codValutazioneMultidimensionale);
		try {
			idValutazioneMultidimensionale = jdbcTemplate.queryForObject(SELECT_ID_VALUTAZIONE_MULTIDIMENSIONALE,
					namedParameters, Long.class);
			return idValutazioneMultidimensionale;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_VALUTAZIONE_MULTIDIMENSIONALE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Long selectIdContratto(String codContratto) throws DatabaseException {
		Long idStato = null;
		if (StringUtils.isEmpty(codContratto)) {
			return idStato;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codContratto", codContratto);
		try {
			idStato = jdbcTemplate.queryForObject(SELECT_ID_CONTRATTO, namedParameters, Long.class);
			return idStato;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_CONTRATTO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Long selectIdAsl(String codAsl) throws DatabaseException {
		Long idAsl = null;
		if (StringUtils.isEmpty(codAsl)) {
			return idAsl;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codAsl", codAsl);
		try {
			idAsl = jdbcTemplate.queryForObject(SELECT_ID_ASL, namedParameters, Long.class);
			return idAsl;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_ASL";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Long selectIdDettaglio(String numeroDomanda) throws DatabaseException {
		Long idDettaglio = null;
		if (StringUtils.isEmpty(numeroDomanda)) {
			return idDettaglio;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroDomanda);
		try {
			idDettaglio = jdbcTemplate.queryForObject(SELECT_ID_DETTAGLIO_DOMANDA, namedParameters, Long.class);
			return idDettaglio;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_DETTAGLIO_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Boolean presenzaDomanda(String codFiscale) throws DatabaseException {
		Boolean isPresente = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("cfDestinatario", codFiscale);
		try {
			isPresente = jdbcTemplate.queryForObject(DOMANDA_PRESENTE, namedParameters, Boolean.class);
			return isPresente;
		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "DOMANDA_PRESENTE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Boolean residentePiemonte(String codProvincia) throws DatabaseException {
		Boolean isPresente = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codProvincia", codProvincia);
		try {
			isPresente = jdbcTemplate.queryForObject(RESIDENTE_PIEMONTE, namedParameters, Boolean.class);
			return isPresente;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "RESIDENTE_PIEMONTE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long insertTDomanda(ModelBozzaRichiesta richiesta, Long idContributo, Long idSportello, String codFiscale)
			throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("cfRichiedente", richiesta.getRichiedente().getCf(), Types.VARCHAR);
		params.addValue("cfDestinatario", richiesta.getDestinatario().getCf(), Types.VARCHAR);
		params.addValue("idTipoContributo", idContributo, Types.BIGINT);
		params.addValue("idSportello", idSportello, Types.BIGINT);
		params.addValue("utenteCrea", codFiscale, Types.VARCHAR);
		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);

		jdbcTemplate.update(INSERT_T_DOMANDA, params, keyHolder, new String[] { "domanda_id" });
		return keyHolder.getKey().longValue();
	}

	public long insertTDomandaDettaglio(ModelBozzaRichiesta richiesta, String codFiscale, Long idDomanda,
			Long idSportello, Long idStatoDomanda, Long idTipoRapporto, Long idTitoloStudio, Long idAsl,
			String codDetDomanda) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		// params.addValue("codDetDomanda", codDetDomanda, Types.VARCHAR);
		params.addValue("idDomanda", idDomanda, Types.BIGINT);
		params.addValue("idSportello", idSportello, Types.BIGINT);
		params.addValue("idStatoDomanda", idStatoDomanda, Types.BIGINT);
//		params.addValue("residentePiemonte", residentePiemonte, Types.BOOLEAN);
		params.addValue("nomeRichiedente", richiesta.getRichiedente().getNome(), Types.VARCHAR);
		params.addValue("cognomeRichiedente", richiesta.getRichiedente().getCognome(), Types.VARCHAR);
		params.addValue("dataNascitaRichiedente", richiesta.getRichiedente().getDataNascita(), Types.DATE);
		params.addValue("statoNascitaRichiedente", richiesta.getRichiedente().getStatoNascita(), Types.VARCHAR);
		params.addValue("comuneNascitaRichiedente", richiesta.getRichiedente().getComuneNascita(), Types.VARCHAR);
		params.addValue("provinciaNascitaRichiedente", richiesta.getRichiedente().getProvinciaNascita(), Types.VARCHAR);
		params.addValue("indirizzoResidenzaRichiedente", richiesta.getRichiedente().getIndirizzoResidenza(),
				Types.VARCHAR);
		params.addValue("comuneResidenzaRichiedente", richiesta.getRichiedente().getComuneResidenza(), Types.VARCHAR);
		params.addValue("provinciaResidenzaRichiedente", richiesta.getRichiedente().getProvinciaResidenza(),
				Types.VARCHAR);
		params.addValue("nomeDestinatario", richiesta.getDestinatario().getNome(), Types.VARCHAR);
		params.addValue("cognomeDestinatario", richiesta.getDestinatario().getCognome(), Types.VARCHAR);
		params.addValue("dataNascitaDestinatario", richiesta.getDestinatario().getDataNascita(), Types.DATE);
		params.addValue("statoNascitaDestinatario", richiesta.getDestinatario().getStatoNascita(), Types.VARCHAR);
		params.addValue("comuneNascitaDestinatario", richiesta.getDestinatario().getComuneNascita(), Types.VARCHAR);
		params.addValue("provinciaNascitaDestinatario", richiesta.getDestinatario().getProvinciaNascita(),
				Types.VARCHAR);
		params.addValue("indirizzoResidenzaDestinatario", richiesta.getDestinatario().getIndirizzoResidenza(),
				Types.VARCHAR);
		params.addValue("comuneResidenzaDestinatario", richiesta.getDestinatario().getComuneResidenza(), Types.VARCHAR);
		params.addValue("provinciaResidenzaDestinatario", richiesta.getDestinatario().getProvinciaResidenza(),
				Types.VARCHAR);
		if (richiesta.getDomicilioDestinatario() != null) {
			params.addValue("indirizzoDomicilioDestinatario", richiesta.getDomicilioDestinatario().getIndirizzo(),
					Types.VARCHAR);
			params.addValue("comuneDomicilioDestinatario", richiesta.getDomicilioDestinatario().getComune(),
					Types.VARCHAR);
			params.addValue("provinciaDomicilioDestinatario", richiesta.getDomicilioDestinatario().getProvincia(),
					Types.VARCHAR);
		} else {
			params.addValue("indirizzoDomicilioDestinatario", null, Types.VARCHAR);
			params.addValue("comuneDomicilioDestinatario", null, Types.VARCHAR);
			params.addValue("provinciaDomicilioDestinatario", null, Types.VARCHAR);
		}
		params.addValue("situazioneLavorativa", richiesta.isLavoroDestinatario(), Types.BOOLEAN);
		params.addValue("note", richiesta.getNote(), Types.VARCHAR);
		params.addValue("idTipoRapporto", idTipoRapporto, Types.BIGINT);
		params.addValue("idTitoloStudio", idTitoloStudio, Types.BIGINT);
		params.addValue("idAsl", idAsl, Types.BIGINT);
		params.addValue("utenteCrea", codFiscale, Types.VARCHAR);
		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);

		jdbcTemplate.update(INSERT_T_DOMANDA_DETTAGLIO, params, keyHolder, new String[] { "domanda_det_id" });
		long iddettaglio = keyHolder.getKey().longValue();
		updateDettaglioDomanda(iddettaglio, codDetDomanda + iddettaglio);
		return iddettaglio;
	}

	public long updateDettaglioDomanda(long idDettaglio, String detCod) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idDomanda", idDettaglio, Types.BIGINT).addValue("detCod", detCod, Types.VARCHAR);
		return jdbcTemplate.update(UPDATE_DET_COD, params);
	}

	public long updateDatiDaModificare(long domandadetidnew, BigDecimal domandadetidold) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("domandadetidnew", domandadetidnew, Types.BIGINT).addValue("domandadetidold", domandadetidold,
				Types.BIGINT);
		return jdbcTemplate.update(UPDATE_CAMPO_DA_MODIFICARE, params);
	}

	public long updateallegatiDaModificare(long domandadetidnew, long allegatoidold, long allegatoidnew)
			throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("domandadetidnew", domandadetidnew, Types.BIGINT)
				.addValue("allegatoidold", allegatoidold, Types.BIGINT)
				.addValue("allegatoidnew", allegatoidnew, Types.BIGINT);
		return jdbcTemplate.update(UPDATE_ALLEGATO_ID_DA_MODIFICARE, params);
	}

	public ModelRichiestaExt selectNumeroRichiestaExt(String numeroRichiesta) throws DatabaseException {
		ModelRichiestaExt richieste = new ModelRichiestaExt();

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			richieste = jdbcTemplate.queryForObject(SELECT_NUMERO_RICHIESTA_COD, namedParameters,
					new DettaglioRichiestaExtMapper());
			return richieste;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_NUMERO_RICHIESTA_COD";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public long updateTDettaglioDomanda(Long idDettaglio, ModelRichiesta richiesta, Long idTitolo, Long idRapporto,
			Long idAsl, Long idValutazione, Long idRelazione, Long idContratto, String codFiscale,
			Long idTipoAssistente) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idDettaglio", idDettaglio, Types.BIGINT);
		params.addValue("punteggioSociale", richiesta.getPunteggioBisognoSociale(), Types.BIGINT);
		params.addValue("nessunaIncompatibilita", richiesta.isNessunaIncompatibilita(), Types.BOOLEAN);
		if (richiesta.getContratto() != null) {
			params.addValue("incompatibilitaPerContratto", richiesta.getContratto().isIncompatibilitaPerContratto(),
					Types.BOOLEAN);
			if (richiesta.getContratto().getAgenzia() != null) {
				params.addValue("contrattoCfCooperativa", richiesta.getContratto().getAgenzia().getCf(), Types.VARCHAR);
			} else {
				params.addValue("contrattoCfCooperativa", null, Types.VARCHAR);
			}
			if (richiesta.getContratto().getAssistenteFamiliare() != null) {
				params.addValue("assistenteFamiliareNome", richiesta.getContratto().getAssistenteFamiliare().getNome(),
						Types.VARCHAR);
				params.addValue("assistenteFamiliareCognome",
						richiesta.getContratto().getAssistenteFamiliare().getCognome(), Types.VARCHAR);
				params.addValue("assistenteFamiliareCf", richiesta.getContratto().getAssistenteFamiliare().getCf(),
						Types.VARCHAR);
				params.addValue("assistenteFamiliareNascitaStato",
						richiesta.getContratto().getAssistenteFamiliare().getStatoNascita(), Types.VARCHAR);
				params.addValue("assistenteFamiliareNascitaComune",
						richiesta.getContratto().getAssistenteFamiliare().getComuneNascita(), Types.VARCHAR);
				params.addValue("assistenteFamiliareNascitaData",
						richiesta.getContratto().getAssistenteFamiliare().getDataNascita(), Types.DATE);
				params.addValue("assistenteFamiliareNascitaProvincia",
						richiesta.getContratto().getAssistenteFamiliare().getProvinciaNascita(), Types.VARCHAR);
			} else {
				params.addValue("assistenteFamiliareNome", null, Types.VARCHAR);
				params.addValue("assistenteFamiliareCognome", null, Types.VARCHAR);
				params.addValue("assistenteFamiliareCf", null, Types.VARCHAR);
				params.addValue("assistenteFamiliareNascitaStato", null, Types.VARCHAR);
				params.addValue("assistenteFamiliareNascitaComune", null, Types.VARCHAR);
				params.addValue("assistenteFamiliareNascitaData", null, Types.DATE);
				params.addValue("assistenteFamiliareNascitaProvincia", null, Types.VARCHAR);
			}
			if (richiesta.getContratto().getIntestatario() != null) {
				params.addValue("datoreNome", richiesta.getContratto().getIntestatario().getNome(), Types.VARCHAR);
				params.addValue("datoreCognome", richiesta.getContratto().getIntestatario().getCognome(),
						Types.VARCHAR);
				params.addValue("datoreCf", richiesta.getContratto().getIntestatario().getCf(), Types.VARCHAR);
				params.addValue("datoreNascitaStato", richiesta.getContratto().getIntestatario().getStatoNascita(),
						Types.VARCHAR);
				params.addValue("datoreNascitaComune", richiesta.getContratto().getIntestatario().getComuneNascita(),
						Types.VARCHAR);
				params.addValue("datoreNascitaData", richiesta.getContratto().getIntestatario().getDataNascita(),
						Types.DATE);
				params.addValue("datoreNascitaProvincia",
						richiesta.getContratto().getIntestatario().getProvinciaNascita(), Types.VARCHAR);
			} else {
				params.addValue("datoreNome", null, Types.VARCHAR);
				params.addValue("datoreCognome", null, Types.VARCHAR);
				params.addValue("datoreCf", null, Types.VARCHAR);
				params.addValue("datoreNascitaStato", null, Types.VARCHAR);
				params.addValue("datoreNascitaComune", null, Types.VARCHAR);
				params.addValue("datoreNascitaData", null, Types.DATE);
				params.addValue("datoreNascitaProvincia", null, Types.VARCHAR);
			}
			params.addValue("idContratto", idContratto, Types.BIGINT);
			params.addValue("idRelazione", idRelazione, Types.BIGINT);
			params.addValue("dataInizioContratto", richiesta.getContratto().getDataInizio(), Types.TIMESTAMP);
			params.addValue("dataFineContratto", richiesta.getContratto().getDataFine(), Types.TIMESTAMP);
			params.addValue("assistenteFamiliarePiva", richiesta.getContratto().getPivaAssitenteFamiliare(),
					Types.VARCHAR);
			params.addValue("assistenteTipoId", idTipoAssistente, Types.BIGINT);
		} else {
			params.addValue("incompatibilitaPerContratto", null, Types.BOOLEAN);
			params.addValue("contrattoCfCooperativa", null, Types.VARCHAR);
			params.addValue("assistenteFamiliareNome", null, Types.VARCHAR);
			params.addValue("assistenteFamiliareCognome", null, Types.VARCHAR);
			params.addValue("assistenteFamiliareCf", null, Types.VARCHAR);
			params.addValue("assistenteFamiliareNascitaStato", null, Types.VARCHAR);
			params.addValue("assistenteFamiliareNascitaComune", null, Types.VARCHAR);
			params.addValue("assistenteFamiliareNascitaData", null, Types.VARCHAR);
			params.addValue("assistenteFamiliareNascitaProvincia", null, Types.VARCHAR);
			params.addValue("datoreNome", null, Types.VARCHAR);
			params.addValue("datoreCognome", null, Types.VARCHAR);
			params.addValue("datoreCf", null, Types.VARCHAR);
			params.addValue("datoreNascitaStato", null, Types.VARCHAR);
			params.addValue("datoreNascitaComune", null, Types.VARCHAR);
			params.addValue("datoreNascitaData", null, Types.VARCHAR);
			params.addValue("datoreNascitaProvincia", null, Types.VARCHAR);
			params.addValue("idContratto", null, Types.BIGINT);
			params.addValue("idRelazione", null, Types.BIGINT);
			params.addValue("dataInizioContratto", null, Types.VARCHAR);
			params.addValue("dataFineContratto", null, Types.VARCHAR);
			params.addValue("assistenteFamiliarePiva", null, Types.VARCHAR);
			params.addValue("assistenteTipoId", null, Types.BIGINT);
		}
		if (richiesta.getDomicilioDestinatario() != null) {
			params.addValue("indirizzoDomicilioDestinatario", richiesta.getDomicilioDestinatario().getIndirizzo(),
					Types.VARCHAR);
			params.addValue("comuneDomicilioDestinatario", richiesta.getDomicilioDestinatario().getComune(),
					Types.VARCHAR);
			params.addValue("provinciaDomicilioDestinatario", richiesta.getDomicilioDestinatario().getProvincia(),
					Types.VARCHAR);
		} else {
			params.addValue("indirizzoDomicilioDestinatario", null, Types.VARCHAR);
			params.addValue("comuneDomicilioDestinatario", null, Types.VARCHAR);
			params.addValue("provinciaDomicilioDestinatario", null, Types.VARCHAR);
		}
		params.addValue("situazioneLavorativa", richiesta.isLavoroDestinatario(), Types.BOOLEAN);
		if (richiesta.getAccredito() != null) {
			params.addValue("iban", richiesta.getAccredito().getIban(), Types.VARCHAR);
			params.addValue("ibanIntestatario", richiesta.getAccredito().getIntestatario(), Types.VARCHAR);
		} else {
			params.addValue("iban", null, Types.VARCHAR);
			params.addValue("ibanIntestatario", null, Types.VARCHAR);
		}
		params.addValue("note", richiesta.getNote(), Types.VARCHAR);
		params.addValue("noterichiedente", richiesta.getNoteRichiedente(), Types.VARCHAR);

		params.addValue("idRapporto", idRapporto, Types.BIGINT);
		params.addValue("idTitoloStudio", idTitolo, Types.BIGINT);
		params.addValue("idAsl", idAsl, Types.BIGINT);
		params.addValue("idValutazione", idValutazione, Types.BIGINT);
		params.addValue("utenteModifica", codFiscale, Types.VARCHAR);
		params.addValue("iseeConforme", richiesta.isAttestazioneIsee(), Types.BOOLEAN);

		return jdbcTemplate.update(UPDATE_T_DETTAGLIO_DOMANDA, params);
	}

	public ModelUpdateCronologia insertTDettaglioDomandaCambioStato(ModelRichiestaExt richiesta, String codStato,
			String codFiscale) throws DatabaseException {

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
		result.setDetCod(codStato + "_" + idDettaglio);
		result.setIdDettaglio(idDettaglio);

		return result;
	}

	public long updateDataFineValDettaglioDomanda(BigDecimal idDettaglio) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idDomanda", idDettaglio, Types.BIGINT);
		return jdbcTemplate.update(UPDATE_DATA_FINE_VAL_DETTAGLIO_DOMANDA, params);
	}

	public long updateDataDomandaValDomanda(String numeroRichiesta, String codFiscale) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("numeroRichiesta", numeroRichiesta, Types.VARCHAR);
		params.addValue("codFiscale", codFiscale, Types.VARCHAR);
		return jdbcTemplate.update(UPDATE_DATA_DOMANDA_VAL_DOMANDA, params);
	}

	public String selectNumeroDomanda(Long idDomanda) throws DatabaseException {
		String cod = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idDomanda", idDomanda);
		try {
			cod = jdbcTemplate.queryForObject(SELECT_NUMERO_DOMANDA_PRINCIPALE, namedParameters, String.class);
			return cod;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_NUMERO_DOMANDA_PRINCIPALE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public String selectDetCod(String numeroDomanda) throws DatabaseException {
		String cod = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroRichiesta", numeroDomanda);
		try {
			cod = jdbcTemplate.queryForObject(SELECT_DETCOD_DOMANDA, namedParameters, String.class);
			return cod;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_DETCOD_DOMANDA";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public boolean isResidenzaPiemontese(String sigla) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("sigla", sigla);
		try {
			Integer numprovincia = jdbcTemplate.queryForObject(SELECT_PROVINCIA_PIEMONTE, namedParameters,
					Integer.class);
			return numprovincia.intValue() > 0;

		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (Exception e) {
			String methodName = "SELECT_PROVINCIA_PIEMONTE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Long selectIdTipoAssistente(String codTipoAssistente) throws DatabaseException {
		Long idTipoAssistente = null;
		if (StringUtils.isEmpty(codTipoAssistente)) {
			return idTipoAssistente;
		}
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codTipoAssistente",
				codTipoAssistente);
		try {
			idTipoAssistente = jdbcTemplate.queryForObject(SELECT_ID_TIPO_ASSISTENTE, namedParameters, Long.class);
			return idTipoAssistente;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_ID_TIPO_ASSISTENTE";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Long selectGraduatoriaIdBySportelloId(BigDecimal idSportello) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idSportello", idSportello);
		try {
			return jdbcTemplate.queryForObject(SELECT_GRADUATORIA_ID_BY_SPORTELLO_ID, params, Long.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "selectGraduatoriaIdBySportelloCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Boolean checkStatoGraduatoria(long idGraduatoria, String stato) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("idGraduatoria", idGraduatoria);
			params.addValue("statoGraduatoria", stato);
			return jdbcTemplate.queryForObject(CHECK_STATO_GRADUATORIA_BY_ID, params, Boolean.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "checkStatoGraduatoria";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public void updateRGraduatoriaStato(long idGraduatoria, String stato, String cf) throws DatabaseException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idGraduatoria", idGraduatoria);
			params.addValue("statoGraduatoria", stato);
			params.addValue("utenteCreazione", cf);
			params.addValue("utenteModifica", cf);
			jdbcTemplate.update(UPDATE_R_GRADUATORIA_STATO, params);
			return;
		} catch (Exception e) {
			String methodName = "updateRGraduatoriaStato";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String selectStatoPrecedente(String numeroRichiesta) throws DatabaseException {
		String dettaglio = null;

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			dettaglio = jdbcTemplate.queryForObject(SELECT_PRECEDENTE_STATO, namedParameters, String.class);
			return dettaglio;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "SELECT_PRECEDENTE_STATO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public String[] selectDataInPagamento(String numeroRichiesta) throws DatabaseException {

		String[] dettaglio = new String[2];

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("numeroDomanda", numeroRichiesta);
		try {
			String valori = jdbcTemplate.queryForObject(DATA_FINE_INPAGAMENTO, namedParameters, String.class);
			dettaglio = valori.split(";");
			return dettaglio;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			String methodName = "DATA_SPORTELLO";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

}

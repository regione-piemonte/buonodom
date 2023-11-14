/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.integration.notificatore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.csi.buonodom.buonodomsrv.dto.Contact;
import it.csi.buonodom.buonodomsrv.dto.DmaccREvnotDestMsg;
import it.csi.buonodom.buonodomsrv.dto.ModelIncompatibilitaRichiesta;
import it.csi.buonodom.buonodomsrv.dto.ModelPersonaSintesi;
import it.csi.buonodom.buonodomsrv.dto.Preferences;
import it.csi.buonodom.buonodomsrv.exception.DatabaseException;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.CodParametroDao;
import it.csi.buonodom.buonodomsrv.integration.dao.custom.RichiesteDao;
import it.csi.buonodom.buonodomsrv.integration.notificatore.dto.EmailPayload;
import it.csi.buonodom.buonodomsrv.integration.notificatore.dto.MexPayload;
import it.csi.buonodom.buonodomsrv.integration.notificatore.dto.NotificaCustom;
import it.csi.buonodom.buonodomsrv.integration.notificatore.dto.PayloadNotifica;
import it.csi.buonodom.buonodomsrv.integration.notificatore.dto.PushPayload;
import it.csi.buonodom.buonodomsrv.integration.notificatore.enumerator.NotificatoreEventCode;
import it.csi.buonodom.buonodomsrv.util.Constants;
import it.csi.buonodom.buonodomsrv.util.LoggerUtil;
import it.csi.buonodom.buonodomsrv.util.enumerator.ApiHeaderParamEnum;

@Service
public class NotificatoreService extends LoggerUtil {
//	https://gitlab.csi.it/user-notification-platform/unpdocumentazione/blob/master/documentazione%20fornitori/NOTIFY-specifiche.md

	@Value("${notificatore.richiedente.applicazione}")
	private String nomeApplicazione;
	@Value("${notificatore.richiedente.url}")
	private String urlNotificatore;
	@Value("${notificatore.richiedente.token}")
	private String tokenApplicativo;
	@Value("${notificatore.richiedente.tag}")
	private String tag;
	@Value("${notificatore.richiedente.templateId}")
	private String templateId;
	@Value("${notificatore.contact.url}")
	private String urlNotificatoreContact;
	@Value("${notificatore.contact.token}")
	private String tokenApplicativoContact;
	private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	private static final DateFormat DATE_FORMAT_ISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	@Autowired
	private CodParametroDao parametroDao;

	@Autowired
	private RichiesteDao richiestaDao;

	public HttpResponse<String> notificaEventoRichiedente(String cfMittente, ModelPersonaSintesi soggetto,
			String xRequestId, String tipoNotifica, String destinatario, String numero, String motivo, String stato)
			throws Exception {
		HttpResponse<String> response = notificaMessaggio(cfMittente, soggetto, xRequestId, tipoNotifica, destinatario,
				numero, motivo, stato);
		return response;
	}

	public Contact notificaContact(String cfMittente, String iride) throws Exception {
		return sendContact(cfMittente, iride);
	}

	public Preferences notificaPreferenze(String cfMittente, String iride) throws Exception {
		return sendPreferences(cfMittente, iride);
	}

	/**
	 * SEND notifica
	 * 
	 * @param cfSoggetto
	 * @param uuid
	 * @param codiceEventoTitle
	 * @param codiceEventoBody
	 * @throws DatabaseException
	 */
	private HttpResponse<String> notificaMessaggio(String cfMittente, ModelPersonaSintesi soggettoRicevente,
			String xRequestId, String tipoNotifica, String destinatario, String numero, String motivo, String stato)
			throws Exception {
		NotificaCustom notificaCustom = buildNotificaCustom(cfMittente, soggettoRicevente, tipoNotifica, destinatario,
				numero, motivo, stato);
		HttpResponse<String> result = sendNotificaEvento(notificaCustom, cfMittente, tokenApplicativo, xRequestId);
		return result;

	}

	private NotificaCustom buildNotificaCustom(String cfMittente, ModelPersonaSintesi soggettoRicevente,
			String tipoNotifica, String destinatario, String numero, String motivo, String stato)
			throws DatabaseException {
		NotificaCustom notifica = new NotificaCustom();
		String uuidIn = UUID.randomUUID().toString();
		notifica.setUuid(uuidIn);
		notifica.setExpireAt(DATE_FORMAT_ISO_8601.format(DateUtils.addHours(new Date(), 1)));
		PayloadNotifica payload = buildPayloadNotifica(cfMittente, soggettoRicevente, tipoNotifica, destinatario,
				uuidIn, numero, motivo, stato);
		notifica.setPayload(payload);
		return notifica;
	}

	private PayloadNotifica buildPayloadNotifica(String cfMittente, ModelPersonaSintesi soggettoRicevente,
			String tipoNotifica, String destinatario, String uuidIn, String numero, String motivo, String stato)
			throws DatabaseException {
		PayloadNotifica payload = new PayloadNotifica();
		payload.setId(uuidIn);
		payload.setUserId(soggettoRicevente.getCf());
		payload.setTag(tag);
		DmaccREvnotDestMsg eventoBody = new DmaccREvnotDestMsg();
		String datacontrodedotta = null;
		String urlCodCit = parametroDao.selectValoreParametroFromCod(NotificatoreEventCode.URL_BUONO_SOCIALE.getCode(),
				Constants.NOTIFICATORE);
		if (tipoNotifica.equalsIgnoreCase(Constants.INVIO_CITTADINO) && stato.equalsIgnoreCase(Constants.INVIATA)) {
			eventoBody
					.setSubject_mail(
							buildSubject(
									parametroDao.selectValoreParametroFromCod(
											NotificatoreEventCode.MSG_SUBJECT_INVIO.getCode(), Constants.NOTIFICATORE),
									NotificatoreEventCode.MSG_SUBJECT_INVIO.getTitle()));
			eventoBody
					.setSubject_push(
							buildSubject(
									parametroDao.selectValoreParametroFromCod(
											NotificatoreEventCode.MSG_SUBJECT_INVIO.getCode(), Constants.NOTIFICATORE),
									NotificatoreEventCode.MSG_SUBJECT_INVIO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			if (destinatario != null)
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_EMAIL_INVIO_DESTINATARIO.getCode(), Constants.NOTIFICATORE));
			else
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_EMAIL_INVIO.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_INVIO.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.RETTIFICA_CITTADINO)
				&& stato.equalsIgnoreCase(Constants.RETTIFICATA)) {
			eventoBody
					.setSubject_mail(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_RETTIFICA.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_RETTIFICA.getTitle()));
			eventoBody
					.setSubject_push(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_RETTIFICA.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_RETTIFICA.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_RETTIFICA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_RETTIFICA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_AMMESSA_RISERVA_CONTRATTO)
				&& stato.equalsIgnoreCase(Constants.AMMESSA_RISERVA_IN_PAGAMENTO)) {
			eventoBody.setSubject_mail(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_RISERVA_CONDIZIONATA.getCode(),
							Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_RISERVA_CONDIZIONATA.getTitle()));
			eventoBody.setSubject_push(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_RISERVA_CONDIZIONATA.getCode(),
							Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_RISERVA_CONDIZIONATA.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			ModelIncompatibilitaRichiesta incompatibilita = richiestaDao.selectIncompatibilitaRichiesta(numero);
			if (incompatibilita != null) {
				if (incompatibilita.isIncompatibilitaPerContratto() && !incompatibilita.isNessunaIncompatibilita()) {
					eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_BODY_MAIL_BO_AMMESSA_RISERVA_INCOMPATIBILITA_CONTRATTO.getCode(),
							Constants.NOTIFICATORE));
					eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_BODY_PUSH_BO_AMMESSA_RISERVA_INCOMPATIBILITA_CONTRATTO.getCode(),
							Constants.NOTIFICATORE));
				} else if (incompatibilita.isIncompatibilitaPerContratto()) {
					eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_RISERVA_CONTRATTO.getCode(),
							Constants.NOTIFICATORE));
					eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_BODY_PUSH_BO_AMMESSA_RISERVA_CONTRATTO.getCode(),
							Constants.NOTIFICATORE));
				} else if (!incompatibilita.isNessunaIncompatibilita()) {
					eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_RISERVA_INCOMPATIBILITA.getCode(),
							Constants.NOTIFICATORE));
					eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_BODY_PUSH_BO_AMMESSA_RISERVA_INCOMPATIBILITA.getCode(),
							Constants.NOTIFICATORE));
				}
			}
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_PERFEZIONATA_IN_PAGAMENTO)
				&& stato.equalsIgnoreCase(Constants.IN_PAGAMENTO)) {
			eventoBody.setSubject_mail(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_BO_PERFEZIONATA_IN_PAGAMENTO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_PERFEZIONATA_IN_PAGAMENTO.getTitle()));
			eventoBody.setSubject_push(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_BO_PERFEZIONATA_IN_PAGAMENTO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_PERFEZIONATA_IN_PAGAMENTO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_MAIL_BO_PERFEZIONATA_IN_PAGAMENTO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_PERFEZIONATA_IN_PAGAMENTO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO)
				&& stato.equalsIgnoreCase(Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO)) {
			eventoBody.setSubject_mail(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getTitle()));
			eventoBody.setSubject_push(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_MAIL_BO_PREAVVISO_DINIEGO_IN_PAGAMENTO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_PREAVVISO_DINIEGO_IN_PAGAMENTO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_DINIEGO) && stato.equalsIgnoreCase(Constants.DINIEGO)) {
			// query per trovare data controdeduzione
			datacontrodedotta = richiestaDao.selectDataControdedotta(numero);
			eventoBody
					.setSubject_mail(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getTitle()));
			eventoBody
					.setSubject_push(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			String statopernultimo = richiestaDao.selectStatoPenultimo(numero);
			String statoultimo = richiestaDao.selectStatoPrecedente(numero);
			boolean preavvisodiniego = false;
			boolean rettifica = false;
			boolean controdedotta = false;
			boolean preavvisodiniegoinpagamento = false;
			boolean perfezionatainpagamento = false;
			if (statopernultimo != null && statoultimo != null) {
				if (statopernultimo.contains(Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA)
						&& statoultimo.contains(Constants.NON_AMMISSIBILE)) {
					preavvisodiniego = true;
				} else if (statopernultimo.contains(Constants.DA_RETTIFICARE)
						|| statopernultimo.contains(Constants.IN_RETTIFICA)
								&& statoultimo.contains(Constants.NON_AMMISSIBILE)) {
					rettifica = true;
				} else if (statoultimo.contains(Constants.AMMESSA) || statoultimo.contains(Constants.AMMESSA_RISERVA)) {
					rettifica = true;
				} else if (statoultimo.contains(Constants.CONTRODEDOTTA)) {
					controdedotta = true;
				} else if (statoultimo.contains(Constants.NON_AMMISSIBILE)
						&& statopernultimo.contains(Constants.CONTRODEDOTTA)) {
					controdedotta = true;
				} else if (statoultimo.contains(Constants.PREAVVISO_DINIEGO_IN_PAGAMENTO)) {
					preavvisodiniegoinpagamento = true;
				} else if (statoultimo.contains(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
					perfezionatainpagamento = true;
				}
			}
			// messagio 3.4.3 si arriva da preavviso di diniego per non ammissibilita non
			// invia le controdeduzioni dopo averle richieste
			// prendo il penultimo stato e vedo se preavviso di diniego per non
			// ammissibilita
			if (preavvisodiniego) {
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_DOPO_PREAVVISO.getCode(),
						Constants.NOTIFICATORE));
				eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_PUSH_BO_DINIEGO_DOPO_PREAVVISO.getCode(),
						Constants.NOTIFICATORE));
			}
			// messagio 3.4.5 si arriva da in rettifica rettificata non richieste
			// controdeduzioni
			// prendo il penultimo stato e vedo se da rettificare e rettifica
			else if (rettifica) {
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SENZA_PREAVVISO.getCode(),
						Constants.NOTIFICATORE));
				eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_PUSH_BO_DINIEGO_SENZA_PREAVVISO.getCode(),
						Constants.NOTIFICATORE));
			}
			// messagio 3.4.4 si arriva da controdedotta
			// prendo il penultimo stato e vedo se da controdedotta
			else if (controdedotta) {
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_CON_CONTRODEDUZIONI.getCode(),
						Constants.NOTIFICATORE));
				eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_PUSH_BO_DINIEGO_CON_CONTRODEDUZIONI.getCode(),
						Constants.NOTIFICATORE));
			}
			// messaggio da definire
			else if (preavvisodiniegoinpagamento) {
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_MAIL_DINIEGO_DOPO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO.getCode(),
						Constants.NOTIFICATORE));
				eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_PUSH_DINIEGO_DOPO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO.getCode(),
						Constants.NOTIFICATORE));
			}
			// messaggio da definire
			else if (perfezionatainpagamento) {
				eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_MAIL_DINIEGO_DOPO_PERFEZIONATA_IN_PAGAMENTO.getCode(),
						Constants.NOTIFICATORE));
				eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
						NotificatoreEventCode.MSG_BODY_PUSH_DINIEGO_DOPO_PERFEZIONATA_IN_PAGAMENTO.getCode(),
						Constants.NOTIFICATORE));
			}
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_RETTIFICA)
				&& stato.equalsIgnoreCase(Constants.DA_RETTIFICARE)) {
			eventoBody
					.setSubject_mail(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_RETTIFICA.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_RETTIFICA.getTitle()));
			eventoBody
					.setSubject_push(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_RETTIFICA.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_RETTIFICA.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_BO_RETTIFICA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_RETTIFICA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_PREAVVISO_DINIEGO_NON_AMMISSIBILITA)
				&& stato.equalsIgnoreCase(Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA)) {
			eventoBody.setSubject_mail(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getTitle()));
			eventoBody.setSubject_push(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_PREAVVISO_DINIEGO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_BO_PREAVVISO_DINIEGO_NON_AMMISSIBILITA.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_PREAVVISO_DINIEGO_NON_AMMISSIBILITA.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.CONTRODEDOTTA_CITTADINO)
				&& stato.equalsIgnoreCase(Constants.CONTRODEDOTTA)) {
			eventoBody.setSubject_mail(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_CONTRODEDOTTA_CITTADINO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_CONTRODEDOTTA_CITTADINO.getTitle()));
			eventoBody.setSubject_push(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_CONTRODEDOTTA_CITTADINO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_CONTRODEDOTTA_CITTADINO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_CONTRODEDOTTA_CITTADINO.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_CONTRODEDOTTA_CITTADINO.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.RINUNCIATA_CITTADINO)
				&& stato.equalsIgnoreCase(Constants.RINUNCIATA)) {
			eventoBody.setSubject_mail(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_RINUNCIATA_CITTADINO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_RINUNCIATA_CITTADINO.getTitle()));
			eventoBody.setSubject_push(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_RINUNCIATA_CITTADINO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_RINUNCIATA_CITTADINO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_RINUNCIATA_CITTADINO.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_RINUNCIATA_CITTADINO.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_DINIEGO_SCADENZA_REQUISTI_DECESSO)
				&& stato.equalsIgnoreCase(Constants.DINIEGO)) {
			eventoBody
					.setSubject_mail(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getTitle()) + " per decadenza requisiti");
			eventoBody
					.setSubject_push(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getTitle1()) + " per decadenza requisiti");
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SCADENZA_REQUISTI_DECESSO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_DINIEGO_SCADENZA_REQUISTI.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA)
				&& stato.equalsIgnoreCase(Constants.DINIEGO)) {
			eventoBody
					.setSubject_mail(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getTitle()) + " per decadenza requisiti");
			eventoBody
					.setSubject_push(buildSubject(
							parametroDao.selectValoreParametroFromCod(
									NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getCode(), Constants.NOTIFICATORE),
							NotificatoreEventCode.MSG_SUBJECT_BO_DINIEGO.getTitle1()) + " per decadenza requisiti");
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_BO_DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_DINIEGO_SCADENZA_REQUISTI.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO_CITTADINO)
				&& stato.equalsIgnoreCase(Constants.PERFEZIONATA_IN_PAGAMENTO)) {
			eventoBody.setSubject_mail(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_PERFEZIONATA_IN_PAGAMENTO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_PERFEZIONATA_IN_PAGAMENTO.getTitle()));
			eventoBody.setSubject_push(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_PERFEZIONATA_IN_PAGAMENTO.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_PERFEZIONATA_IN_PAGAMENTO.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_PERFEZIONATA_IN_PAGAMENTO_CITTADINO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_PERFEZIONATA_IN_PAGAMENTO_CITTADINO.getCode(),
					Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_AMMESSA_FINANZIATA)
				&& stato.equalsIgnoreCase(Constants.IN_PAGAMENTO)) {
			eventoBody.setSubject_mail(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_FINANZIATA.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_FINANZIATA.getTitle()));
			eventoBody.setSubject_push(buildSubject(
					parametroDao.selectValoreParametroFromCod(
							NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_FINANZIATA.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_FINANZIATA.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_FINANZIATA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_AMMESSA_FINANZIATA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		} else if (tipoNotifica.equalsIgnoreCase(Constants.BO_AMMESSA_NON_FINANZIATA)
				&& (stato.equalsIgnoreCase(Constants.AMMESSA) || stato.equalsIgnoreCase(Constants.AMMESSA_RISERVA))) {
			eventoBody.setSubject_mail(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_NON_FINANZIATA.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_NON_FINANZIATA.getTitle()));
			eventoBody.setSubject_push(buildSubject(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_NON_FINANZIATA.getCode(), Constants.NOTIFICATORE),
					NotificatoreEventCode.MSG_SUBJECT_BO_AMMESSA_NON_FINANZIATA.getTitle1()));
			eventoBody.setSubject_mex(eventoBody.getSubject_push());
			eventoBody.setMsg_mail(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_EMAIL_BO_AMMESSA_NON_FINANZIATA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_push(parametroDao.selectValoreParametroFromCod(
					NotificatoreEventCode.MSG_BODY_PUSH_BO_AMMESSA_NON_FINANZIATA.getCode(), Constants.NOTIFICATORE));
			eventoBody.setMsg_mex(eventoBody.getMsg_push());
			logInfo("buildPayloadNotifica", "codiceEventoBody" + eventoBody.getSubject_mail());
		}
		EmailPayload email = buildEmailPayload(eventoBody, eventoBody.getSubject_mail(), soggettoRicevente, urlCodCit,
				destinatario, numero, motivo, datacontrodedotta);
		payload.setEmail(email);

		PushPayload push = buildPushPayload(eventoBody, eventoBody.getSubject_push(), soggettoRicevente, urlCodCit,
				destinatario, numero, motivo);
		payload.setPush(push);

		MexPayload mex = buildMexPayload(eventoBody, eventoBody.getSubject_mex(), soggettoRicevente, urlCodCit,
				destinatario, numero, motivo);
		payload.setMex(mex);
		return payload;

	}

	private MexPayload buildMexPayload(DmaccREvnotDestMsg eventoBody, String descrizioneEvento,
			ModelPersonaSintesi soggettoRicevente, String urlCodCit, String destinatario, String numero,
			String motivo) {
		MexPayload mex = new MexPayload();
		mex.setTitle(descrizioneEvento);
		String body = buildBody(eventoBody.getMsg_mex(), soggettoRicevente, urlCodCit, destinatario, numero, motivo,
				null);
		mex.setBody(body);
		mex.setCalltoaction(urlCodCit);
		return mex;
	}
//	sostituire nei vari body
//	{0}SOGGETTO_NOME_COGNOME richiente
//	{1} NUMERO DOMANDA
//	{2} SE DESTINATARIO DIVERSO DA RICHIEDENTE NOME_COGNOME
//	{3} modivi di revoca diniego ecc
//	{5}URL_SITO SOCIALE 
//  {4}DATA CONTRODEDOTTA

	private String buildBody(String msg, ModelPersonaSintesi soggettoRicevente, String urlCodCit, String destinatario,
			String numero, String motivo, String datacontrodedotta) {
		String result = msg;
		result = result.replace("{0}", soggettoRicevente.getNome() + " " + soggettoRicevente.getCognome());
		result = result.replace("{1}", numero);
		if (StringUtils.isNotBlank(urlCodCit)) {
			result = result.replace("{5}", urlCodCit);
		} else
			result = result.replace("{5}", "");
		if (StringUtils.isNotBlank(destinatario)) {
			result = result.replace("{2}", destinatario);
		} else
			result = result.replace("{2}", "");
		if (StringUtils.isNotBlank(motivo)) {
			result = result.replace("{3}", motivo);
		} else
			result = result.replace("{3}", "");
		if (StringUtils.isNotBlank(datacontrodedotta)) {
			result = result.replace("{4}", datacontrodedotta);
		} else
			result = result.replace("{4}", "");
		return result;
	}

	private String buildSubject(String msg, String tipoSubject) {
		String result = msg;
		result = result.replace("{0}", tipoSubject);
		return result;
	}

	private PushPayload buildPushPayload(DmaccREvnotDestMsg eventoBody, String descrizioneEvento,
			ModelPersonaSintesi soggettoRicevente, String urlCodCit, String destinatario, String numero,
			String motivo) {
		PushPayload push = new PushPayload();
		push.setTitle(descrizioneEvento);
		String body = buildBody(eventoBody.getMsg_push(), soggettoRicevente, urlCodCit, destinatario, numero, motivo,
				null);
		push.setBody(body);
		return push;
	}

	private EmailPayload buildEmailPayload(DmaccREvnotDestMsg eventoBody, String descrizioneEvento,
			ModelPersonaSintesi soggettoRicevente, String urlCodCit, String destinatario, String numero, String motivo,
			String datacontrodedotta) {
		EmailPayload email = new EmailPayload();
		email.setSubject(descrizioneEvento);
		String body = buildBody(eventoBody.getMsg_mail(), soggettoRicevente, urlCodCit, destinatario, numero, motivo,
				datacontrodedotta);
		email.setBody(body);
		email.setTemplateId(templateId);
		return email;
	}

	private HttpResponse<String> sendNotificaEvento(NotificaCustom notifica, String cfSoggetto, String tokenApplicativo,
			String xRequestId) throws Exception {
		logInfo("sendNotificaEvento ",
				"params: \n" + ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode() + ": " + cfSoggetto + ", \n"
						+ ApiHeaderParamEnum.X_AUTHENTICATION.getCode() + ": " + tokenApplicativo + "\n"
						+ ApiHeaderParamEnum.X_REQUEST_ID.getCode() + ": " + xRequestId + "\n" + " payload ID: "
						+ notifica.getPayload().getId() + "\n");

		String result = null;
		String jsonPayloadString = fromObjectToJsonString(notifica);
		logInfo("sendNotificaEvento-", "payload: " + jsonPayloadString);
		HttpRequest request = HttpRequest.newBuilder().POST(BodyPublishers.ofString(jsonPayloadString))
				.uri(URI.create(urlNotificatore))
				.setHeader(ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode(), cfSoggetto)
				.setHeader(ApiHeaderParamEnum.X_REQUEST_ID.getCode(), xRequestId)
				.setHeader(ApiHeaderParamEnum.X_AUTHENTICATION.getCode(), tokenApplicativo)
				.setHeader("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		// print status code
		logInfo("sendNotificaEvento ", "status:" + response.statusCode() + " - notifica_uuid: " + notifica.getUuid());
		logInfo("sendNotificaEvento", "response: " + response.toString());
		logInfo("sendNotificaEvento", "responsebody: " + response.body());

		if (!(response.statusCode() == 200 || response.statusCode() == 201)) {
			logError("sendNotificaEvento: ", response.body() + " - notifica_uuid:" + notifica.getUuid());
		}
		// result=response.body();
		return response;
	}

	private String fromObjectToJsonString(NotificaCustom notificaCustom) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(notificaCustom);
	}

	private Contact sendContact(String cfSoggetto, String iride) throws Exception {
		logInfo("sendNotificaEvento ",
				"params: \n" + ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode() + ": " + iride + ", \n"
						+ ApiHeaderParamEnum.X_AUTHENTICATION.getCode() + ": " + tokenApplicativoContact + "\n");

		HttpRequest request = HttpRequest.newBuilder().GET()
				.uri(URI.create(urlNotificatoreContact + "/" + cfSoggetto + "/contacts"))
				.setHeader(ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode(), iride)
				.setHeader(ApiHeaderParamEnum.X_AUTHENTICATION.getCode(), tokenApplicativoContact)
				.setHeader("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		// print status code
		logInfo("sendNotificaEvento ", "status:" + response.statusCode());
		logInfo("sendNotificaEvento", "response: " + response.toString());
		logInfo("sendNotificaEvento", "responsebody: " + response.body());
		Contact contatto = new Contact();
		if (!(response.statusCode() == 200 || response.statusCode() == 201)) {
			logError("sendNotificaEvento: ", response.body());
		} else {
			// lascio solo utenza email e telefono
			if (response.body().indexOf("push") != -1) {
				String contattireturn = response.body().substring(0, response.body().indexOf("push") - 2) + "}";
				contatto = new ObjectMapper().readValue(contattireturn, Contact.class);
			}
		}
		return contatto;
	}

	private Preferences sendPreferences(String cfSoggetto, String iride) throws Exception {
		logInfo("sendPreferenze ",
				"params: \n" + ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode() + ": " + iride + ", \n"
						+ ApiHeaderParamEnum.X_AUTHENTICATION.getCode() + ": " + tokenApplicativoContact + "\n");

		HttpRequest request = HttpRequest.newBuilder().GET()
				.uri(URI.create(urlNotificatoreContact + "/" + cfSoggetto + "/preferences/" + nomeApplicazione))
				.setHeader(ApiHeaderParamEnum.SHIB_IRIDE_IDENTITADIGITALE.getCode(), iride)
				.setHeader(ApiHeaderParamEnum.X_AUTHENTICATION.getCode(), tokenApplicativoContact)
				.setHeader("Content-Type", "application/json").build();

		HttpResponse<String> response = null;
		response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		// print status code
		logInfo("sendPreferenzeEvento ", "status:" + response.statusCode());
		logInfo("sendPreferenzeEvento", "response: " + response.toString());
		logInfo("sendPreferenzeEvento", "responsebody: " + response.body());
		Preferences preferenze = new Preferences();
		if (!(response.statusCode() == 200 || response.statusCode() == 201)) {
			logError("sendPreferenzeEvento: ", response.body());
		} else {
			preferenze = new ObjectMapper().readValue(response.body(), Preferences.class);
		}
		return preferenze;
	}
}

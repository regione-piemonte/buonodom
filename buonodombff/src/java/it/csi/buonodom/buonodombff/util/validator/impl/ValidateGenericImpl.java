/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.util.validator.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.csi.buonodom.buonodombff.dto.ModelAllegato;
import it.csi.buonodom.buonodombff.dto.ModelBozzaRichiesta;
import it.csi.buonodom.buonodombff.dto.ModelContratto;
import it.csi.buonodom.buonodombff.dto.ModelContrattoAllegati;
import it.csi.buonodom.buonodombff.dto.ModelDocumentoSpesa;
import it.csi.buonodom.buonodombff.dto.ModelDocumentoSpesaDettaglio;
import it.csi.buonodom.buonodombff.dto.ModelPersona;
import it.csi.buonodom.buonodombff.dto.ModelPersonaSintesi;
import it.csi.buonodom.buonodombff.dto.ModelRichiesta;
import it.csi.buonodom.buonodombff.dto.ModelRichiestaAccredito;
import it.csi.buonodom.buonodombff.dto.ModelSabbatici;
import it.csi.buonodom.buonodombff.dto.custom.ErroreDettaglioExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelGetAllegato;
import it.csi.buonodom.buonodombff.dto.custom.ModelGetAllegatoExt;
import it.csi.buonodom.buonodombff.dto.custom.ModelRichiestaExt;
import it.csi.buonodom.buonodombff.exception.DatabaseException;
import it.csi.buonodom.buonodombff.integration.dao.custom.AllegatoBuonoDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.AllegatoDao;
import it.csi.buonodom.buonodombff.integration.dao.custom.ContrattiDao;
import it.csi.buonodom.buonodombff.util.Constants;
import it.csi.buonodom.buonodombff.util.Converter;
import it.csi.buonodom.buonodombff.util.Util;
import it.csi.buonodom.buonodombff.util.enumerator.CodeErrorEnum;
import it.csi.buonodom.buonodombff.util.enumerator.ErrorParamEnum;

@Service
public class ValidateGenericImpl extends BaseValidate {

	@Autowired
	AllegatoDao allegatoDao;

	@Autowired
	AllegatoBuonoDao allegatoBuonoDao;

	@Autowired
	ContrattiDao contrattiDao;

	public List<ErroreDettaglioExt> validateAnagrafica(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String cf, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		// inserimento controllo specifico citid
		String[] param = new String[1];
		if (!Util.isValorizzato(cf)) {
			param[0] = ErrorParamEnum.CF.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (!formalCheckCitId(cf)) {
			param[0] = cf;
			result.add(getValueGenericError(CodeErrorEnum.ERR06.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> validateListaDecodifica(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheckDecodifica(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		return result;
	}

	public List<ErroreDettaglioExt> validateMesiSabbatici(String shibIdentitaCodiceFiscale, ModelSabbatici sabbatici,
			String xRequestId, String xForwardedFor, String xCodiceServizio, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");
		String[] param = new String[1];
//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheckDecodifica(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		if (sabbatici != null && sabbatici.getSabbatici().size() > 2) {
			param[0] = "Mesi Sabbatici";
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		} else {
			// verifico che siano stinghe di interi da 6
			for (String mesi : sabbatici.getSabbatici()) {
				if (mesi.length() != 7) {
					param[0] = "Mese sabbatico non corretto";
					result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
				} else {
					if (!Util.isInt(mesi.substring(0, 4)) || !Util.isInt(mesi.substring(5))
							|| Converter.getInt(mesi.substring(5)) < 1 || Converter.getInt(mesi.substring(5)) > 12) {
						param[0] = "Mese sabbatico non corretto";
						result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
					}
				}
			}
		}

		return result;
	}

	public List<ErroreDettaglioExt> validateRichieste(String shibIdentitaCodiceFiscale, String xRequestId,
			String xForwardedFor, String xCodiceServizio, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		return result;
	}

	public List<ErroreDettaglioExt> validateAllegato(String xForwardedFor, String shibIdentitaCodiceFiscale,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheckGetAllegato(result, shibIdentitaCodiceFiscale, xForwardedFor);

		return result;
	}

	public List<ErroreDettaglioExt> validateSportelli(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		return result;
	}

	public List<ErroreDettaglioExt> validateFornitori(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		return result;
	}

	public List<ErroreDettaglioExt> validateCronologia(String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale, SecurityContext securityContext, HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest, String cf) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = checkCodFiscaleAndShibIden(result, cf, shibIdentitaCodiceFiscale);

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		return result;
	}

	public List<ErroreDettaglioExt> validatePostCronologia(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest, String stato) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();
		String[] param = new String[1];

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (stato.isEmpty()) {
			param[0] = "Stato";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> validatePostRichieste(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest, ModelBozzaRichiesta richiesta,
			Long idStato, Long idContributo, Long idSportello, Long idTitolo, Long idAsl, Long idRelazione)
			throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();
		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		result = checkNull(result, idStato, ErrorParamEnum.STATO_DOMANDA.getCode());
		result = checkNull(result, idContributo, ErrorParamEnum.TIPO_CONTRIBUTO.getCode());
		result = checkNull(result, idSportello, ErrorParamEnum.SPORTELLO_ATTIVO.getCode());
		result = checkCodFiscaleAndShibIden(result, richiesta.getRichiedente().getCf(), shibIdentitaCodiceFiscale);
		result = checkEmptyString(result, richiesta.getStudioDestinatario(),
				ErrorParamEnum.TITOLO_STUDIO_DESTINATARIO.getCode());
		if (!StringUtils.isEmpty(richiesta.getStudioDestinatario())) {
			result = checkNull(result, idTitolo, ErrorParamEnum.TITOLO_STUDIO_DESTINATARIO.getCode());
		}
		if (!StringUtils.isEmpty(richiesta.getDelega())) {
			result = checkNull(result, idRelazione, ErrorParamEnum.TIPO_RAPPORTO.getCode());
		}
		result = checkEmptyString(result, richiesta.getAslDestinatario(), ErrorParamEnum.ASL_DESTINATARIO.getCode());
		if (!StringUtils.isEmpty(richiesta.getAslDestinatario())) {
			result = checkNull(result, idAsl, ErrorParamEnum.ASL_DESTINATARIO.getCode());
		}
		result = personaCheck(result, richiesta.getRichiedente(), Constants.RICHIEDENTE);
		result = personaCheck(result, richiesta.getDestinatario(), Constants.DESTINATARIO);
		return result;
	}

	public List<ErroreDettaglioExt> validatePostAllegati(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest, byte[] allegato,
			String xFilenameOriginale) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");
		CharSequence arr[] = { "\\", "|", "/", "?", "*", ":", "\"", "<", ">", ";" };
		boolean trovato = false;
//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		String[] param = new String[1];
		if (allegato == null || allegato.length < 1000) {
			param[0] = "Allegato";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (!Util.isValorizzato(xFilenameOriginale)) {
			param[0] = "Nome File";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		} else {
			for (CharSequence a : arr) {
				if (xFilenameOriginale.contains(a)) {
					trovato = true;
				}
			}
			if (trovato) {
				param[0] = "Nome File" + ": " + xFilenameOriginale;
				result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
			}
		}

		return result;
	}

	public List<ErroreDettaglioExt> validatePutRichieste(String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, SecurityContext securityContext,
			HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest, ModelRichiesta richiesta, Long idTitolo,
			Long idRapporto, Long idAsl, Long idRelazione, Long idValutazione, Long idContratto, String numeroDomanda,
			ModelRichiesta richiestaDb, Long idTipoAssistente) throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

//		2. Verifica parametri in input (Criteri di validazione della richiesta)
//		2a) Obbligatorieta'
		List<ErroreDettaglioExt> result = new ArrayList<>();
		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		result = checkNumeroDomanda(result, numeroDomanda, richiesta.getNumero());
		result = personaCheck(result, richiesta.getRichiedente(), Constants.RICHIEDENTE);
		result = personaCheck(result, richiesta.getDestinatario(), Constants.DESTINATARIO);
		result = checkCodFiscaleAndShibIden(result, richiestaDb.getRichiedente().getCf(), shibIdentitaCodiceFiscale);
		result = congruitaPersonaCheck(result, richiesta.getRichiedente(), richiestaDb.getRichiedente(),
				Constants.RICHIEDENTE);
		result = congruitaPersonaCheck(result, richiesta.getDestinatario(), richiestaDb.getDestinatario(),
				Constants.DESTINATARIO);
		result = checkEmptyString(result, richiesta.getStudioDestinatario(),
				ErrorParamEnum.TITOLO_STUDIO_DESTINATARIO.getCode());
		if (!StringUtils.isEmpty(richiesta.getStudioDestinatario())) {
			result = checkNull(result, idTitolo, ErrorParamEnum.TITOLO_STUDIO_DESTINATARIO.getCode());
		}
		result = checkEmptyString(result, richiesta.getAslDestinatario(), ErrorParamEnum.ASL_DESTINATARIO.getCode());
		if (!StringUtils.isEmpty(richiesta.getAslDestinatario())) {
			result = checkNull(result, idAsl, ErrorParamEnum.ASL_DESTINATARIO.getCode());
		}
		if (!StringUtils.isEmpty(richiesta.getDelega())) {
			result = checkNull(result, idRapporto, ErrorParamEnum.TIPO_RAPPORTO.getCode());
		}

		if (!StringUtils.isEmpty(richiesta.getValutazioneMultidimensionale())) {
			result = checkNull(result, idValutazione, ErrorParamEnum.VALUTAZIONE_MULTIDIMENSIONALE.getCode());
		}
		if (richiesta.getContratto() != null) {
			if (!StringUtils.isEmpty(richiesta.getContratto().getTipo())) {
				result = checkNull(result, idContratto, ErrorParamEnum.TIPO_CONTRATTO.getCode());
			}
			if (!StringUtils.isEmpty(richiesta.getContratto().getRelazioneDestinatario())) {
				result = checkNull(result, idRelazione, ErrorParamEnum.TIPO_RELAZIONE.getCode());
			}
			if (!StringUtils.isEmpty(richiesta.getContratto().getTipoSupportoFamiliare())) {
				result = checkNull(result, idTipoAssistente, ErrorParamEnum.TIPO_SUPPORTO_FAMILIARE.getCode());
			}
		}

		if (richiesta.getStato().equalsIgnoreCase(Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA)) {
			result = notaControdeduzione(result, richiesta.getNoteRichiedente());
		}

		return result;
	}

	private List<ErroreDettaglioExt> commonCheck(List<ErroreDettaglioExt> result, String shibIdentitaCodiceFiscale,
			String xRequestId, String xForwardedFor, String xCodiceServizio) throws DatabaseException {

		String[] param = new String[1];
		if (StringUtils.isEmpty(xRequestId)) {
			param[0] = ErrorParamEnum.X_REQUEST_ID.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(shibIdentitaCodiceFiscale)) {
			param[0] = ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		} else if (!formalCheckCitId(shibIdentitaCodiceFiscale)) {
			param[0] = shibIdentitaCodiceFiscale;
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		}

		if (StringUtils.isEmpty(xForwardedFor)) {
			param[0] = ErrorParamEnum.X_FORWARDED_FOR.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(xCodiceServizio)) {
			param[0] = ErrorParamEnum.X_CODICE_SERVIZIO.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	private List<ErroreDettaglioExt> commonCheckDecodifica(List<ErroreDettaglioExt> result,
			String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor, String xCodiceServizio)
			throws DatabaseException {

		String[] param = new String[1];
		if (StringUtils.isEmpty(xRequestId)) {
			param[0] = ErrorParamEnum.X_REQUEST_ID.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(shibIdentitaCodiceFiscale)) {
			param[0] = ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(xForwardedFor)) {
			param[0] = ErrorParamEnum.X_FORWARDED_FOR.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(xCodiceServizio)) {
			param[0] = ErrorParamEnum.X_CODICE_SERVIZIO.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	private List<ErroreDettaglioExt> commonCheckGetAllegato(List<ErroreDettaglioExt> result,
			String shibIdentitaCodiceFiscale, String xForwardedFor) throws DatabaseException {

		String[] param = new String[1];

		if (StringUtils.isEmpty(shibIdentitaCodiceFiscale)) {
			param[0] = ErrorParamEnum.SHIB_IDENTITA_CODICEFISCALE.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (StringUtils.isEmpty(xForwardedFor)) {
			param[0] = ErrorParamEnum.X_FORWARDED_FOR.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	private List<ErroreDettaglioExt> checkNull(List<ErroreDettaglioExt> result, Object id, String tipoId)
			throws DatabaseException {

		String[] param = new String[1];
		if (id == null) {
			param[0] = tipoId;
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		}
		return result;
	}

	private List<ErroreDettaglioExt> checkNumeroDomanda(List<ErroreDettaglioExt> result, String numeroParam,
			String numeroPaylod) throws DatabaseException {

		String[] param = new String[1];
		if (!numeroParam.equals(numeroPaylod)) {
			param[0] = numeroParam + " diverso da " + numeroPaylod;
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		}
		return result;
	}

	public List<ErroreDettaglioExt> checkCodFiscaleAndShibIden(List<ErroreDettaglioExt> result, String codFiscale,
			String shibIdentita) throws DatabaseException {

		String[] param = new String[1];
		if (codFiscale != null) {
			if (!codFiscale.equals(shibIdentita)) {
				param[0] = codFiscale;
				result.add(getValueGenericError(CodeErrorEnum.ERR12.getCode(), param));
			}
		}
		return result;
	}

	private List<ErroreDettaglioExt> checkEmptyString(List<ErroreDettaglioExt> result, String element,
			String tipoElement) throws DatabaseException {

		String[] param = new String[1];
		if (StringUtils.isEmpty(element)) {
			param[0] = tipoElement;
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		return result;
	}

	private List<ErroreDettaglioExt> cfCheck(List<ErroreDettaglioExt> result, String cf, String tipoPersona)
			throws DatabaseException {
		String[] param = new String[1];
		if (StringUtils.isEmpty(cf)) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.CF_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.CF_DESTINATARIO.getCode();
			}
			if (tipoPersona.equals(Constants.DATORE)) {
				param[0] = ErrorParamEnum.CF_DATORE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE)) {
				param[0] = ErrorParamEnum.CF_ASSISTENTE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE_FAMILIARE)) {
				param[0] = ErrorParamEnum.CF_ASSISTENTE.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		} else if (!formalCheckCitId(cf)) {
			param[0] = "cf: " + cf;
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		}
		return result;
	}

	private List<ErroreDettaglioExt> personaCheck(List<ErroreDettaglioExt> result, ModelPersona persona,
			String tipoPersona) throws DatabaseException {
		String[] param = new String[1];
		result = cfCheck(result, persona.getCf(), tipoPersona);

		if (StringUtils.isEmpty(persona.getNome())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.NOME_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.NOME_DESTINATARIO.getCode();
			}

			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getCognome())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.COGNOME_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.COGNOME_DESTINATARIO.getCode();
			}

			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getDataNascita())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.DATA_NASCITA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.DATA_NASCITA_DESTINATARIO.getCode();
			}

			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getStatoNascita())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.STATO_NASCITA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.STATO_NASCITA_DESTINATARIO.getCode();
			}

			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getComuneNascita())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.COMUNE_NASCITA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.COMUNE_NASCITA_DESTINATARIO.getCode();
			}

			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getProvinciaNascita())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.PROVINCIA_NASCITA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.PROVINCIA_NASCITA_DESTINATARIO.getCode();
			}

			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getIndirizzoResidenza())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.INDIRIZZO_RESIDENZA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.INDIRIZZO_RESIDENZA_DESTINATARIO.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getComuneResidenza())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.COMUNE_RESIDENZA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.COMUNE_RESIDENZA_DESTINATARIO.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getProvinciaResidenza())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.PROVINCIA_RESIDENZA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.PROVINCIA_RESIDENZA_DESTINATARIO.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	private List<ErroreDettaglioExt> personaSintesiCheck(List<ErroreDettaglioExt> result, ModelPersonaSintesi persona,
			String tipoPersona) throws DatabaseException {
		String[] param = new String[1];
		result = cfCheck(result, persona.getCf(), tipoPersona);

		if (StringUtils.isEmpty(persona.getNome())) {
			if (tipoPersona.equals(Constants.DATORE)) {
				param[0] = ErrorParamEnum.NOME_DATORE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE)) {
				param[0] = ErrorParamEnum.NOME_ASSISTENTE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE_FAMILIARE)) {
				param[0] = ErrorParamEnum.NOME_ASSISTENTE.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getCognome())) {
			if (tipoPersona.equals(Constants.DATORE)) {
				param[0] = ErrorParamEnum.COGNOME_DATORE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE)) {
				param[0] = ErrorParamEnum.COGNOME_ASSISTENTE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE_FAMILIARE)) {
				param[0] = ErrorParamEnum.COGNOME_ASSISTENTE.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getDataNascita())) {
			if (tipoPersona.equals(Constants.DATORE)) {
				param[0] = ErrorParamEnum.DATA_NASCITA_DATORE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE)) {
				param[0] = ErrorParamEnum.DATA_NASCITA_ASSISTENTE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE_FAMILIARE)) {
				param[0] = ErrorParamEnum.DATA_NASCITA_ASSISTENTE.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getStatoNascita())) {
			if (tipoPersona.equals(Constants.DATORE)) {
				param[0] = ErrorParamEnum.STATO_NASCITA_DATORE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE)) {
				param[0] = ErrorParamEnum.STATO_NASCITA_ASSISTENTE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE_FAMILIARE)) {
				param[0] = ErrorParamEnum.STATO_NASCITA_ASSISTENTE.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getComuneNascita())) {
			if (tipoPersona.equals(Constants.DATORE)) {
				param[0] = ErrorParamEnum.COMUNE_NASCITA_DATORE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE)) {
				param[0] = ErrorParamEnum.COMUNE_NASCITA_ASSISTENTE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE_FAMILIARE)) {
				param[0] = ErrorParamEnum.COMUNE_NASCITA_ASSISTENTE.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (StringUtils.isEmpty(persona.getProvinciaNascita())) {
			if (tipoPersona.equals(Constants.DATORE)) {
				param[0] = ErrorParamEnum.PROVINCIA_NASCITA_DATORE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE)) {
				param[0] = ErrorParamEnum.PROVINCIA_NASCITA_ASSISTENTE.getCode();
			}
			if (tipoPersona.equals(Constants.ASSISTENTE_FAMILIARE)) {
				param[0] = ErrorParamEnum.PROVINCIA_NASCITA_ASSISTENTE.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	private List<ErroreDettaglioExt> accreditoCheck(List<ErroreDettaglioExt> result, ModelRichiestaAccredito accredito)
			throws DatabaseException {
		String[] param = new String[1];

		if (accredito == null) {
			param[0] = ErrorParamEnum.IBAN.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (accredito != null && !Util.isValorizzato(accredito.getIban())) {
			param[0] = ErrorParamEnum.IBAN.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (accredito != null && !Util.isValorizzato(accredito.getIntestatario())) {
			param[0] = ErrorParamEnum.INTESTATARIO_IBAN.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (accredito != null && Util.isValorizzato(accredito.getIban()) && accredito.getIban().length() != 27) {
			param[0] = ErrorParamEnum.LUNGHEZZA_IBAN.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> notaControdeduzione(List<ErroreDettaglioExt> result, String nota)
			throws DatabaseException {
		String[] param = new String[1];

		if (!Util.isValorizzato(nota)) {
			param[0] = ErrorParamEnum.NOTA_CONTRODEDUZIONE.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> checkCambioStatoCoerente(List<ErroreDettaglioExt> result, String statoDiArrivo,
			String statoDiPartenza) throws DatabaseException {
		String[] param = new String[1];
		boolean incoerente = false;

		switch (statoDiArrivo) {
		case Constants.ANNULLATA:
			if (!statoDiPartenza.equals(Constants.BOZZA))
				incoerente = true;
			break;
		case Constants.INVIATA:
			if (!statoDiPartenza.equals(Constants.BOZZA))
				incoerente = true;
			break;
		case Constants.RETTIFICATA:
			if (!statoDiPartenza.equals(Constants.IN_RETTIFICA))
				incoerente = true;
			break;
		case Constants.DA_RETTIFICARE:
			if (!statoDiPartenza.equals(Constants.PRESA_IN_CARICO))
				incoerente = true;
			break;
		case Constants.IN_RETTIFICA:
			if (!statoDiPartenza.equals(Constants.DA_RETTIFICARE))
				incoerente = true;
			break;
		case Constants.PERFEZIONATA_IN_PAGAMENTO:
			if (!statoDiPartenza.equals(Constants.PREAVVISO_DI_DINIEGO_IN_PAGAMENTO)
					&& !statoDiPartenza.equals(Constants.AMMESSA_CON_RISERVA_IN_PAGAMENTO))
				incoerente = true;
			break;
		case Constants.CONTRODEDOTTA:
			if (!statoDiPartenza.equals(Constants.PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA))
				incoerente = true;
			break;
		case Constants.RINUNCIATA:
			if (statoDiPartenza.equals(Constants.ANNULLATA) || statoDiPartenza.equals(Constants.BOZZA)
					|| statoDiPartenza.equals(Constants.DINIEGO))
				incoerente = true;
			break;
		default:
			incoerente = false;
		}

		if (statoDiArrivo.equals(statoDiPartenza))
			incoerente = true;

		if (incoerente) {
			param[0] = "Stato";
			result.add(getValueGenericError(CodeErrorEnum.ERR13.getCode(), param));
		}
		return result;
	}

	public List<ErroreDettaglioExt> checkCambioStatoCoerenteBuono(List<ErroreDettaglioExt> result, String statoDiArrivo,
			String statoDiPartenza) throws DatabaseException {
		String[] param = new String[1];
		boolean incoerente = false;

		if (statoDiArrivo.equals(statoDiPartenza))
			incoerente = true;

		if (incoerente) {
			param[0] = "Stato";
			result.add(getValueGenericError(CodeErrorEnum.ERR13.getCode(), param));
		}
		return result;
	}

	public List<ErroreDettaglioExt> checkSeCambioStatoPossibile(List<ErroreDettaglioExt> result,
			ModelRichiestaExt richiesta) throws DatabaseException {
		String[] param = new String[1];

		if (richiesta.getPunteggioBisognoSociale() != null
				&& richiesta.getPunteggioBisognoSociale().compareTo(BigDecimal.valueOf(7)) < 0) {
			param[0] = "Punteggio Sociale";
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
		}
		if (richiesta.isNessunaIncompatibilita() == null) {
			param[0] = "Nessuna Incompatibilita'";
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
		}
		if (richiesta.isAttestazioneIsee() == null || !richiesta.isAttestazioneIsee()) {
			param[0] = "Isee Conforme";
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
		}
		if (Util.isAgeBetween18and65(richiesta.getDestinatario().getDataNascita())) {
			// boolean incoerente = richiesta.isLavoroDestinatario() == null ? true : false;
			if (richiesta.isLavoroDestinatario() == null) {
				param[0] = "Situazione lavorativa attiva";
				result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
			}

		}

		if (Util.isAgeMaggiorenne(richiesta.getDestinatario().getDataNascita())) {
			// boolean incoerente = richiesta.isLavoroDestinatario() == null ? true : false;
			if (richiesta.getContratto().getTipoSupportoFamiliare().equals(Constants.EDUCATORE_PROFESSIONALE)) {
				param[0] = "Tipo Supporto Familiare";
				result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
			}

		}

		if (richiesta.getAccredito() == null) {
			param[0] = ErrorParamEnum.IBAN.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
		}

		if (richiesta.getAccredito() != null && !Util.isValorizzato(richiesta.getAccredito().getIban())) {
			param[0] = ErrorParamEnum.IBAN.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
		}

		if (richiesta.getAccredito() != null && !Util.isValorizzato(richiesta.getAccredito().getIntestatario())) {
			param[0] = ErrorParamEnum.INTESTATARIO_IBAN.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
		}

		if (richiesta.getAccredito() != null && Util.isValorizzato(richiesta.getAccredito().getIban())
				&& richiesta.getAccredito().getIban().length() != 27) {
			param[0] = ErrorParamEnum.LUNGHEZZA_IBAN.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		}

		if ((!richiesta.getDestinatario().getCf().equals(richiesta.getRichiedente().getCf()))
				&& !Util.isValorizzato(richiesta.getDelega())) {
			param[0] = "Delega";
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
		}

		if (!Util.isValorizzato(richiesta.getAslDestinatario())) {
			param[0] = "ASL destinatario";
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
		}

		if (!Util.isValorizzato(richiesta.getStudioDestinatario())) {
			param[0] = "Titolo Studio";
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
		}

		if (!Util.isValorizzato(richiesta.getValutazioneMultidimensionale())) {
			param[0] = "Valutazione Multidimensionale";
			result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
		}
		// Controlli relativi al contratto
		if (richiesta.getContratto() != null) {
			if (richiesta.getContratto().getTipo() != null
					&& !richiesta.getContratto().getTipo().equals(Constants.NESSUN_CONTRATTO)) {
				// controlla la presenza del campo ti assistente
				if (!Util.isValorizzato(richiesta.getContratto().getTipoSupportoFamiliare())) {
					param[0] = "Tipo Assistente";
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				}
				if (richiesta.getContratto().getDataInizio() == null) {
					param[0] = "Data Inizio Contratto";
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				} else {
					if (richiesta.getContratto().getDataFine() != null) {
						if (richiesta.getContratto().getDataFine().compareTo(new Date()) <= 0) {
							param[0] = "Data Fine Contratto";
							result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
						} else if (richiesta.getContratto().getDataFine()
								.compareTo(richiesta.getContratto().getDataInizio()) <= 0) {
							param[0] = "Data Fine Contratto";
							result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
						}

					}
				}
			}
			if (richiesta.getContratto().isIncompatibilitaPerContratto() != null
					&& richiesta.getContratto().isIncompatibilitaPerContratto()) {
				if (!Util.isValorizzato(richiesta.getContratto().getTipo())
						&& !richiesta.getContratto().getTipo().equals(Constants.NESSUN_CONTRATTO)) {
					param[0] = "Incompatibilita' Contratto";
					result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
				}
			}

			if (richiesta.getContratto().getTipo() != null
					&& richiesta.getContratto().getTipo().equals(Constants.COOPERATIVA)
					&& !Util.isValorizzato(richiesta.getContratto().getAgenzia().getCf())) {
				param[0] = "CF Cooperativa";
				result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
			}

			if (richiesta.getContratto().getTipo() != null
					&& richiesta.getContratto().getTipo().equals(Constants.PARTITA_IVA)
					&& !Util.isValorizzato(richiesta.getContratto().getPivaAssitenteFamiliare())) {
				param[0] = "Partita Iva";
				result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
			}
			if (richiesta.getContratto().getTipo() != null
					&& (richiesta.getContratto().getTipo().equals(Constants.ASSISTENTE_FAMILIARE)
							|| richiesta.getContratto().getTipo().equals(Constants.PARTITA_IVA))) {
				if (richiesta.getContratto().getAssistenteFamiliare() != null) {
//					boolean coerenteAssitente = Stream
//							.of(richiesta.getContratto().getAssistenteFamiliare().getClass().getDeclaredFields())
//							.allMatch(f -> {
//								try {
//									f.setAccessible(true);
//									return f.get(richiesta.getContratto().getAssistenteFamiliare()) != null;
//								} catch (IllegalArgumentException | IllegalAccessException e) {
//									return false;
//								}
//							});
//
//					if (!coerenteAssitente) {
					if (richiesta.getContratto().getAssistenteFamiliare().getCf() == null) {
						param[0] = "Assitente Familiare";
						result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
					}
				} else {
					param[0] = "Assitente Familiare";
					result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
				}
			}
			if (richiesta.getContratto().getTipo() != null
					&& (richiesta.getContratto().getTipo().equals(Constants.ASSISTENTE_FAMILIARE)
							|| richiesta.getContratto().getTipo().equals(Constants.COOPERATIVA)
							|| richiesta.getContratto().getTipo().equals(Constants.PARTITA_IVA))) {
				if (richiesta.getContratto().getIntestatario() != null) {
					boolean coerenteIntestatario = Stream
							.of(richiesta.getContratto().getIntestatario().getClass().getDeclaredFields())
							.allMatch(f -> {
								try {
									f.setAccessible(true);
									return f.get(richiesta.getContratto().getIntestatario()) != null;
								} catch (IllegalArgumentException | IllegalAccessException e) {
									return false;
								}
							});

					if (!coerenteIntestatario) {
						param[0] = "Datore di lavoro";
						result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
					} else {
						if ((!richiesta.getDestinatario().getCf()
								.equals(richiesta.getContratto().getIntestatario().getCf())
								&& !richiesta.getRichiedente().getCf()
										.equals(richiesta.getContratto().getIntestatario().getCf()))
								&& !Util.isValorizzato(richiesta.getContratto().getRelazioneDestinatario())) {
							param[0] = "Tipo Relazione";
							result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
						}
					}

				} else {
					param[0] = "Datore di lavoro";
					result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
				}
			}
		}

		return result;
	}

	private List<ErroreDettaglioExt> congruitaPersonaCheck(List<ErroreDettaglioExt> result, ModelPersona persona,
			ModelPersona personaDb, String tipoPersona) throws DatabaseException {
		String[] param = new String[1];
		if (persona.getCf() != null && !persona.getCf().equalsIgnoreCase(personaDb.getCf())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.CF_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.CF_DESTINATARIO.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
		}

		if (persona.getNome() != null && !persona.getNome().equalsIgnoreCase(personaDb.getNome())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.NOME_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.NOME_DESTINATARIO.getCode();
			}

			result.add(getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
		}
		if (persona.getCognome() != null && !persona.getCognome().equalsIgnoreCase(personaDb.getCognome())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.COGNOME_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.COGNOME_DESTINATARIO.getCode();
			}

			result.add(getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
		}
		if (persona.getDataNascita() != null && !persona.getDataNascita().equals(personaDb.getDataNascita())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.DATA_NASCITA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.DATA_NASCITA_DESTINATARIO.getCode();
			}

			result.add(getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
		}
		if (persona.getStatoNascita() != null
				&& !persona.getStatoNascita().equalsIgnoreCase(personaDb.getStatoNascita())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.STATO_NASCITA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.STATO_NASCITA_DESTINATARIO.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
		}
		if (persona.getComuneNascita() != null
				&& !persona.getComuneNascita().equalsIgnoreCase(personaDb.getComuneNascita())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.COMUNE_NASCITA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.COMUNE_NASCITA_DESTINATARIO.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
		}
		if (persona.getProvinciaNascita() != null
				&& !persona.getProvinciaNascita().equalsIgnoreCase(personaDb.getProvinciaNascita())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.PROVINCIA_NASCITA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.PROVINCIA_NASCITA_DESTINATARIO.getCode();
			}

			result.add(getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
		}
		if (persona.getIndirizzoResidenza() != null
				&& !persona.getIndirizzoResidenza().equalsIgnoreCase(personaDb.getIndirizzoResidenza())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.INDIRIZZO_RESIDENZA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.INDIRIZZO_RESIDENZA_DESTINATARIO.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
		}
		if (persona.getComuneResidenza() != null
				&& !persona.getComuneResidenza().equalsIgnoreCase(personaDb.getComuneResidenza())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.COMUNE_RESIDENZA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.COMUNE_RESIDENZA_DESTINATARIO.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
		}
		if (persona.getProvinciaResidenza() != null
				&& !persona.getProvinciaResidenza().equalsIgnoreCase(personaDb.getProvinciaResidenza())) {
			if (tipoPersona.equals(Constants.RICHIEDENTE)) {
				param[0] = ErrorParamEnum.PROVINCIA_RESIDENZA_RICHIEDENTE.getCode();
			}
			if (tipoPersona.equals(Constants.DESTINATARIO)) {
				param[0] = ErrorParamEnum.PROVINCIA_RESIDENZA_DESTINATARIO.getCode();
			}
			result.add(getValueGenericError(CodeErrorEnum.ERR15.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> checkAllegato(List<ErroreDettaglioExt> result, ModelRichiestaExt richiesta,
			String detCod, String cf, String stato, boolean statidacontrollare) throws DatabaseException {
		String os = System.getProperty("os.name");
		CharSequence arr[] = { "\\", "|", "/", "?", "*", ":", "\"", "<", ">", ";" };
		boolean trovato = false;
		boolean locale = false;
		if (os.toLowerCase().contains("win")) {
			locale = true;
		}
		String[] param = new String[1];
		try {
			if (Util.isValorizzato(richiesta.getValutazioneMultidimensionale())) {
				if (richiesta.getValutazioneMultidimensionale().equals(Constants.PERSONA_PIU_65_ANNI)) {
					if (!statidacontrollare) {
						ModelAllegato verbaleUVG = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.VERBALE_UVG)).findFirst().orElse(null);
						if (verbaleUVG == null) {
							param[0] = "Verbale valutazione UVG";
							result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
						}
					}
					ModelAllegato verbaleUMVD = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.VERBALE_UMVD)).findFirst().orElse(null);
					if (verbaleUMVD != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, verbaleUMVD.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, verbaleUMVD.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo(Constants.PERSONA_PIU_65_ANNI + " cancello allegato per sostituzione verbale "
								+ Constants.VERBALE_UMVD + " con " + Constants.VERBALE_UVG,
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
				}
				if (richiesta.getValutazioneMultidimensionale().equals(Constants.PERSONA_DISABILE)) {
					if (!statidacontrollare) {
						ModelAllegato verbaleUMVD = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.VERBALE_UMVD)).findFirst().orElse(null);
						if (verbaleUMVD == null) {
							param[0] = "Verbale  valutazione UMVD";
							result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
						}
					}
					ModelAllegato verbaleUVG = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.VERBALE_UVG)).findFirst().orElse(null);
					if (verbaleUVG != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, verbaleUVG.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, verbaleUVG.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo(Constants.PERSONA_DISABILE + " cancello allegato per sostituzione verbale "
								+ Constants.VERBALE_UVG + " con " + Constants.VERBALE_UMVD,
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}

				}
			}
			if (richiesta.getContratto() != null) {
				if (Util.isValorizzato(richiesta.getContratto().getTipo())) {
					if (richiesta.getContratto().getTipo().equals(Constants.NESSUN_CONTRATTO)) {
						ModelAllegato contrattoLavoro = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.CONTRATTO_LAVORO)).findFirst().orElse(null);
						ModelAllegato contrattoLavoroCoop = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.CONTRATTO_LAVORO_COOP)).findFirst()
								.orElse(null);
						ModelAllegato denunciaInps = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.DENUNCIA_INPS)).findFirst().orElse(null);
						ModelAllegato letteraIncarico = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.LETTERA_INCARICO)).findFirst().orElse(null);
						if (contrattoLavoro != null) {
							ModelGetAllegato a = allegatoDao.selectAllegato(detCod, contrattoLavoro.getTipo(),
									richiesta.getDomandaDetId());
							allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, contrattoLavoro.getTipo(), cf);
							File f = null;
							if (!locale)
								f = new File(a.getFilePath() + "/" + a.getFileName());
							else
								f = new File(a.getFilePath() + "\\" + a.getFileName());
							logInfo("cancello allegato per cambio contratto da " + contrattoLavoro.getTipo() + " a "
									+ richiesta.getContratto().getTipo(), a.getFilePath() + "/" + a.getFileName());
							f.delete();
						}
						if (contrattoLavoroCoop != null) {
							ModelGetAllegato a = allegatoDao.selectAllegato(detCod, contrattoLavoroCoop.getTipo(),
									richiesta.getDomandaDetId());
							allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, contrattoLavoroCoop.getTipo(),
									cf);
							File f = null;
							if (!locale)
								f = new File(a.getFilePath() + "/" + a.getFileName());
							else
								f = new File(a.getFilePath() + "\\" + a.getFileName());
							logInfo("cancello allegato per cambio contratto da " + contrattoLavoroCoop.getTipo() + " a "
									+ richiesta.getContratto().getTipo(), a.getFilePath() + "/" + a.getFileName());
							f.delete();
						}
						if (denunciaInps != null) {
							ModelGetAllegato a = allegatoDao.selectAllegato(detCod, denunciaInps.getTipo(),
									richiesta.getDomandaDetId());
							allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, denunciaInps.getTipo(), cf);
							File f = null;
							if (!locale)
								f = new File(a.getFilePath() + "/" + a.getFileName());
							else
								f = new File(a.getFilePath() + "\\" + a.getFileName());
							logInfo("cancello allegato per cambio contratto da " + denunciaInps.getTipo() + " a "
									+ richiesta.getContratto().getTipo(), a.getFilePath() + "/" + a.getFileName());
							f.delete();
						}
						if (letteraIncarico != null) {
							ModelGetAllegato a = allegatoDao.selectAllegato(detCod, letteraIncarico.getTipo(),
									richiesta.getDomandaDetId());
							allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, letteraIncarico.getTipo(), cf);
							File f = null;
							if (!locale)
								f = new File(a.getFilePath() + "/" + a.getFileName());
							else
								f = new File(a.getFilePath() + "\\" + a.getFileName());
							logInfo("cancello allegato per cambio contratto da " + letteraIncarico.getTipo() + " a "
									+ richiesta.getContratto().getTipo(), a.getFilePath() + "/" + a.getFileName());
							f.delete();
						}
					}
					if (richiesta.getContratto().getTipo().equals(Constants.ASSISTENTE_FAMILIARE)) {
						if (!statidacontrollare) {
							ModelAllegato contrattoLavoro = richiesta.getAllegati().stream()
									.filter(f -> f.getTipo().equals(Constants.CONTRATTO_LAVORO)).findFirst()
									.orElse(null);
							ModelAllegato denunciaInps = richiesta.getAllegati().stream()
									.filter(f -> f.getTipo().equals(Constants.DENUNCIA_INPS)).findFirst().orElse(null);
							if (contrattoLavoro == null) {
								param[0] = "Copia contratto di lavoro o lettera di assunzione";
								result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
							}
							if (denunciaInps == null) {
								param[0] = "Copia denuncia rapporto di lavoro domestico presentata a INPS";
								result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
							}
						}
						ModelAllegato contrattoLavoroCoop = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.CONTRATTO_LAVORO_COOP)).findFirst()
								.orElse(null);
						if (contrattoLavoroCoop != null) {
							ModelGetAllegato a = allegatoDao.selectAllegato(detCod, contrattoLavoroCoop.getTipo(),
									richiesta.getDomandaDetId());
							allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, contrattoLavoroCoop.getTipo(),
									cf);
							File f = null;
							if (!locale)
								f = new File(a.getFilePath() + "/" + a.getFileName());
							else
								f = new File(a.getFilePath() + "\\" + a.getFileName());
							logInfo("cancello allegato per cambio contratto da " + contrattoLavoroCoop.getTipo() + " a "
									+ richiesta.getContratto().getTipo(), a.getFilePath() + "/" + a.getFileName());
							f.delete();
						}
						ModelAllegato letteraIncarico = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.LETTERA_INCARICO)).findFirst().orElse(null);
						if (letteraIncarico != null) {
							ModelGetAllegato a = allegatoDao.selectAllegato(detCod, letteraIncarico.getTipo(),
									richiesta.getDomandaDetId());
							allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, letteraIncarico.getTipo(), cf);
							File f = null;
							if (!locale)
								f = new File(a.getFilePath() + "/" + a.getFileName());
							else
								f = new File(a.getFilePath() + "\\" + a.getFileName());
							logInfo("cancello allegato per cambio contratto da " + letteraIncarico.getTipo() + " a "
									+ richiesta.getContratto().getTipo(), a.getFilePath() + "/" + a.getFileName());
							f.delete();
						}
					}

					if (richiesta.getContratto().getTipo().equals(Constants.COOPERATIVA)) {
						if (!statidacontrollare) {
							ModelAllegato contrattoLavoroCoop = richiesta.getAllegati().stream()
									.filter(f -> f.getTipo().equals(Constants.CONTRATTO_LAVORO_COOP)).findFirst()
									.orElse(null);
							if (contrattoLavoroCoop == null) {
								param[0] = "Copia contratto di lavoro coop";
								result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
							}
						}
						ModelAllegato contrattoLavoro = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.CONTRATTO_LAVORO)).findFirst().orElse(null);
						if (contrattoLavoro != null) {
							ModelGetAllegato a = allegatoDao.selectAllegato(detCod, contrattoLavoro.getTipo(),
									richiesta.getDomandaDetId());
							allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, contrattoLavoro.getTipo(), cf);
							File f = null;
							if (!locale)
								f = new File(a.getFilePath() + "/" + a.getFileName());
							else
								f = new File(a.getFilePath() + "\\" + a.getFileName());
							logInfo("cancello allegato per cambio contratto da " + contrattoLavoro.getTipo() + " a "
									+ richiesta.getContratto().getTipo(), a.getFilePath() + "/" + a.getFileName());
							f.delete();
						}
						ModelAllegato denunciaInps = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.DENUNCIA_INPS)).findFirst().orElse(null);
						if (denunciaInps != null) {
							ModelGetAllegato a = allegatoDao.selectAllegato(detCod, denunciaInps.getTipo(),
									richiesta.getDomandaDetId());
							allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, denunciaInps.getTipo(), cf);
							File f = null;
							if (!locale)
								f = new File(a.getFilePath() + "/" + a.getFileName());
							else
								f = new File(a.getFilePath() + "\\" + a.getFileName());
							logInfo("cancello allegato per cambio contratto da " + denunciaInps.getTipo() + " a "
									+ richiesta.getContratto().getTipo(), a.getFilePath() + "/" + a.getFileName());
							f.delete();
						}
						ModelAllegato letteraIncarico = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.LETTERA_INCARICO)).findFirst().orElse(null);
						if (letteraIncarico != null) {
							ModelGetAllegato a = allegatoDao.selectAllegato(detCod, letteraIncarico.getTipo(),
									richiesta.getDomandaDetId());
							allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, letteraIncarico.getTipo(), cf);
							File f = null;
							if (!locale)
								f = new File(a.getFilePath() + "/" + a.getFileName());
							else
								f = new File(a.getFilePath() + "\\" + a.getFileName());
							logInfo("cancello allegato per cambio contratto da " + letteraIncarico.getTipo() + " a "
									+ richiesta.getContratto().getTipo(), a.getFilePath() + "/" + a.getFileName());
							f.delete();
						}
					}
					if (richiesta.getContratto().getTipo().equals(Constants.PARTITA_IVA)) {
						if (!statidacontrollare) {
							ModelAllegato letteraIncarico = richiesta.getAllegati().stream()
									.filter(f -> f.getTipo().equals(Constants.LETTERA_INCARICO)).findFirst()
									.orElse(null);
							if (letteraIncarico == null) {
								param[0] = "Copia lettera di incarico";
								result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
							}
						}
						ModelAllegato contrattoLavoro = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.CONTRATTO_LAVORO)).findFirst().orElse(null);
						if (contrattoLavoro != null) {
							ModelGetAllegato a = allegatoDao.selectAllegato(detCod, contrattoLavoro.getTipo(),
									richiesta.getDomandaDetId());
							allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, contrattoLavoro.getTipo(), cf);
							File f = null;
							if (!locale)
								f = new File(a.getFilePath() + "/" + a.getFileName());
							else
								f = new File(a.getFilePath() + "\\" + a.getFileName());
							logInfo("cancello allegato per cambio contratto da " + contrattoLavoro.getTipo() + " a "
									+ richiesta.getContratto().getTipo(), a.getFilePath() + "/" + a.getFileName());
							f.delete();
						}
						ModelAllegato denunciaInps = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.DENUNCIA_INPS)).findFirst().orElse(null);
						if (denunciaInps != null) {
							ModelGetAllegato a = allegatoDao.selectAllegato(detCod, denunciaInps.getTipo(),
									richiesta.getDomandaDetId());
							allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, denunciaInps.getTipo(), cf);
							File f = null;
							if (!locale)
								f = new File(a.getFilePath() + "/" + a.getFileName());
							else
								f = new File(a.getFilePath() + "\\" + a.getFileName());
							logInfo("cancello allegato per cambio contratto da " + denunciaInps.getTipo() + " a "
									+ richiesta.getContratto().getTipo(), a.getFilePath() + "/" + a.getFileName());
							f.delete();
						}
						ModelAllegato contrattoLavoroCoop = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.CONTRATTO_LAVORO_COOP)).findFirst()
								.orElse(null);
						if (contrattoLavoroCoop != null) {
							ModelGetAllegato a = allegatoDao.selectAllegato(detCod, contrattoLavoroCoop.getTipo(),
									richiesta.getDomandaDetId());
							allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, contrattoLavoroCoop.getTipo(),
									cf);
							File f = null;
							if (!locale)
								f = new File(a.getFilePath() + "/" + a.getFileName());
							else
								f = new File(a.getFilePath() + "\\" + a.getFileName());
							logInfo("cancello allegato per cambio contratto da " + contrattoLavoroCoop.getTipo() + " a "
									+ richiesta.getContratto().getTipo(), a.getFilePath() + "/" + a.getFileName());
							f.delete();
						}
					}
				}
			}
			if (Util.isValorizzato(richiesta.getDelega())) {
				if (richiesta.getDelega().equals(Constants.NUCLEO_FAMILIARE)
						|| richiesta.getDelega().equals(Constants.CONIUGE)
						|| richiesta.getDelega().equals(Constants.PARENTE_PRIMO_GRADO)
						|| richiesta.getDelega().equals(Constants.CURATELA)
						|| richiesta.getDelega().equals(Constants.AMMINISTRAZIONE_SOSTEGNO)
						|| richiesta.getDelega().equals(Constants.ALTRO)) {
					if (!statidacontrollare) {
						ModelAllegato delega = richiesta.getAllegati().stream()
						// modifica specifiche 28 02 2023
//					.filter(f -> f.getTipo().equals(Constants.DELEGA)).findFirst().orElse(null);
								.filter(f -> f.getTipo().equals(Constants.PROCURA_SPECIALE)).findFirst().orElse(null);
						ModelAllegato cartaIdentita = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.CARTA_IDENTITA)).findFirst().orElse(null);
						if (delega == null) {
							// param[0] = "Delega";
							param[0] = "Procura speciale";
							result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
						}
						if (cartaIdentita == null) {
							param[0] = "Documento di Identita'";
							result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
						}
					}
					// modifica specifiche 28 02 2023
//			ModelAllegato procura = richiesta.getAllegati().stream()
//					.filter(f -> f.getTipo().equals(Constants.PROCURA_SPECIALE)).findFirst().orElse(null);
//			if (procura != null) {
//				ModelGetAllegato a = allegatoDao.selectAllegato(detCod, procura.getTipo(),
//						richiesta.getDomandaDetId());
//				allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, procura.getTipo(), cf);
//				File f = null;
//				if (!locale)
//					f = new File(a.getFilePath() + "/" + a.getFileName());
//				else
//					f = new File(a.getFilePath() + "\\" + a.getFileName());
//				logInfo("cancello allegato procura per " + richiesta.getDelega(), a.getFilePath() + "/" +a.getFileName());
//				f.delete();
//			}
					ModelAllegato delega = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.DELEGA)).findFirst().orElse(null);
					if (delega != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, delega.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, delega.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo("cancello allegato delega per " + richiesta.getDelega(),
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
					ModelAllegato tutela = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.NOMINA_TUTORE)).findFirst().orElse(null);
					if (tutela != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, tutela.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, tutela.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo("cancello allegato tutela per " + richiesta.getDelega(),
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
				} else if (richiesta.getDelega().equals(Constants.PROCURA_SPECIALE)) {
					if (!statidacontrollare) {
						ModelAllegato procura = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.PROCURA_SPECIALE)).findFirst().orElse(null);
						if (procura == null) {
							param[0] = "Procura speciale";
							result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
						}
					}
					// cancello delega e ci
					ModelAllegato delega = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.DELEGA)).findFirst().orElse(null);
					if (delega != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, delega.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, delega.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo("cancello allegato delega per " + richiesta.getDelega(),
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
					ModelAllegato cartaIdentita = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.CARTA_IDENTITA)).findFirst().orElse(null);
					if (cartaIdentita != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, cartaIdentita.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, cartaIdentita.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo("cancello allegato carta identita per " + richiesta.getDelega(),
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
					ModelAllegato tutela = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.NOMINA_TUTORE)).findFirst().orElse(null);
					if (tutela != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, tutela.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, tutela.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo("cancello allegato tutela per " + richiesta.getDelega(),
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
				} else if (richiesta.getDelega().equals(Constants.TUTELA)) {
					if (!statidacontrollare) {
						ModelAllegato tutela = richiesta.getAllegati().stream()
								.filter(f -> f.getTipo().equals(Constants.NOMINA_TUTORE)).findFirst().orElse(null);
						if (tutela == null) {
							param[0] = "Tutela";
							result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
						}
					}
					// cancello delega e ci
					ModelAllegato delega = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.DELEGA)).findFirst().orElse(null);
					if (delega != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, delega.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, delega.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo("cancello allegato delega per " + richiesta.getDelega(),
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
					ModelAllegato cartaIdentita = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.CARTA_IDENTITA)).findFirst().orElse(null);
					if (cartaIdentita != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, cartaIdentita.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, cartaIdentita.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo("cancello allegato carta identita per " + richiesta.getDelega(),
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
					ModelAllegato procura = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.PROCURA_SPECIALE)).findFirst().orElse(null);
					if (procura != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, procura.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, procura.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo("cancello allegato procura per " + richiesta.getDelega(),
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
				} else {
					ModelAllegato delega = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.DELEGA)).findFirst().orElse(null);
					if (delega != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, delega.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, delega.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo("cancello allegato delega per " + richiesta.getDelega(),
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
					ModelAllegato cartaIdentita = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.CARTA_IDENTITA)).findFirst().orElse(null);
					if (cartaIdentita != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, cartaIdentita.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, cartaIdentita.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo("cancello allegato carta identita per " + richiesta.getDelega(),
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
					ModelAllegato procura = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.PROCURA_SPECIALE)).findFirst().orElse(null);
					if (procura != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, procura.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, procura.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo("cancello allegato procura per " + richiesta.getDelega(),
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
					ModelAllegato tutela = richiesta.getAllegati().stream()
							.filter(f -> f.getTipo().equals(Constants.NOMINA_TUTORE)).findFirst().orElse(null);
					if (tutela != null) {
						ModelGetAllegato a = allegatoDao.selectAllegato(detCod, tutela.getTipo(),
								richiesta.getDomandaDetId());
						allegatoDao.updateDomandaAllegatoDataCancellazione(detCod, tutela.getTipo(), cf);
						File f = null;
						if (!locale)
							f = new File(a.getFilePath() + "/" + a.getFileName());
						else
							f = new File(a.getFilePath() + "\\" + a.getFileName());
						logInfo("cancello allegato tutela per " + richiesta.getDelega(),
								a.getFilePath() + "/" + a.getFileName());
						f.delete();
					}
				}
			}

			for (ModelAllegato allegato : richiesta.getAllegati()) {
				for (CharSequence a : arr) {
					if (allegato.getFilename().contains(a)) {
						if (!trovato) {
							param[0] += allegato.getFilename();
						} else {
							param[0] += ", " + allegato.getFilename();
						}
						trovato = true;
					}
				}
			}
			if (trovato) {
				result.add(getValueGenericError(CodeErrorEnum.ERR14.getCode(), param));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logError("controlla tipo allegati", "Errore generico ", e);
			e.printStackTrace();
		}

		return result;
	}

	public List<ErroreDettaglioExt> checkAllegatoFileSystem(List<ErroreDettaglioExt> result,
			ModelRichiestaExt richiesta, String detCod, String cf, String stato) throws DatabaseException {

		// verifico coerenzaa tra allegati db e allegati file system
		String os = System.getProperty("os.name");
		boolean locale = false;
		String[] param = new String[1];
		if (os.toLowerCase().contains("win")) {
			locale = true;
		}
		List<ModelGetAllegatoExt> allegati = new ArrayList<ModelGetAllegatoExt>();
		allegati = allegatoDao.selectAllegati(richiesta.getDomandaDetId());
		boolean trovato = true;
		for (ModelGetAllegatoExt allegato : allegati) {
			if (!getAllegato(allegato.getFilePath(), allegato.getFileName())) {
				trovato = false;
				break;
			}
		}
		if (!trovato) {
			result.add(getValueGenericError(CodeErrorEnum.ERR18.getCode(), null));
		}
		return result;
	}

	public List<ErroreDettaglioExt> checkEliminazioneIncompatibilita(List<ErroreDettaglioExt> result,
			ModelRichiestaExt richiesta) throws DatabaseException {

		if (richiesta.isNessunaIncompatibilita() != null && !richiesta.isNessunaIncompatibilita()) {
			// errore
			result.add(getValueGenericError(CodeErrorEnum.ERR22.getCode(), null));
		} else if (richiesta.getContratto() != null) {
			if (richiesta.getContratto().isIncompatibilitaPerContratto() != null
					&& richiesta.getContratto().isIncompatibilitaPerContratto()) {
				// errore
				result.add(getValueGenericError(CodeErrorEnum.ERR22.getCode(), null));
			}
		}

		return result;
	}

	private boolean getAllegato(String filePath, String fileName) {
		String os = System.getProperty("os.name");
		boolean locale = false;
		File file = null;

		try {
			if (os.toLowerCase().contains("win")) {
				locale = true;
			}
			if (!locale)
				file = new File(filePath + "/" + fileName);
			else
				file = new File(filePath + "\\" + fileName);

			if (!file.exists()) {
				return false;
			} else
				return true;
		} catch (Exception e) {
			logError("verifica ellegato", "Errore generico ", e);
			e.printStackTrace();
			return false;
		}

	}

	public List<ErroreDettaglioExt> checkSportelloCorrente(List<ErroreDettaglioExt> result, boolean inCorso)
			throws DatabaseException {
		String[] param = new String[1];

		if (!inCorso) {
			param[0] = ErrorParamEnum.SPORTELLO_CHIUSO.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR21.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> validateAddContratto(String numeroRichiesta, Long rapporto_tipo_id,
			Long contratto_tipo_id, Long fornitore_tipo_id, String xRequestId, String xForwardedFor,
			String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContrattoAllegati contrattoAllegati,
			String destinatario, int fornitoreId) throws DatabaseException {
		List<ErroreDettaglioExt> result = new ArrayList<>();
		String[] param = new String[1];

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
//		List<String> elencoDate = new ArrayList<String>();
//		elencoDate = contrattiDao.checkSovrapposizioneContrattiFornitore(numeroRichiesta,fornitoreId);
		if (contrattoAllegati == null) {
			param[0] = "Contratto";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		} else if (contrattoAllegati.getContratto() == null) {
			param[0] = "Contratto";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		} else if (fornitore_tipo_id == null) {
			param[0] = ErrorParamEnum.TIPO_SUPPORTO_FAMILIARE.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		} else if (contrattoAllegati.getAllegati() == null || contrattoAllegati.getAllegati().size() == 0) {
			param[0] = "Allegati";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		} else if (contratto_tipo_id == null) {
			param[0] = ErrorParamEnum.TIPO_CONTRATTO.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		} else if (contrattoAllegati.getContratto().getTipo().equals(Constants.NESSUN_CONTRATTO)) {
			param[0] = ErrorParamEnum.TIPO_CONTRATTO.getCode();
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		} else if (contrattoAllegati.getContratto().getDataInizio() == null) {
			param[0] = "Data Inizio Contratto";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		} else if (contrattoAllegati.getContratto().getDataFine() != null && contrattoAllegati.getContratto()
				.getDataFine().compareTo(contrattoAllegati.getContratto().getDataInizio()) < 0) {
			param[0] = "Data Fine Contratto minore o uguale data inizio contratto";
			result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
		}
		// verifico che non ci sia un contratto con stesso fornitore in essere
//		else  if (elencoDate !=null) {
//		Date dataFinePassata = null;
//		if (contrattoAllegati.getContratto().getDataFine()!=null)
//			dataFinePassata = contrattoAllegati.getContratto().getDataFine();
//		else
//			dataFinePassata = Converter.getDataWithoutTime("999-12-31");
//			
//			//splitta le date
//			for (String date : elencoDate) {
//				String[] dataesistente = new String[2];
//				dataesistente = date.split(";");
//				if (contrattoAllegati.getContratto().getDataInizio().compareTo(Converter.getDataWithoutTime(dataesistente[0]))<=0
//						&& contrattoAllegati.getContratto().getDataFine().compareTo(Converter.getDataWithoutTime(dataesistente[0]))>=0)  {
//					param[0] = "Impossibile creare un contratto sovrapposto con lo stesso fornitore";
//					result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
//				}
//			}
//		} 
		else if (contrattoAllegati.getContratto().getIntestatario() == null) {
			param[0] = "Datore di lavoro";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		} else {
			switch (contrattoAllegati.getContratto().getTipo()) {
			case Constants.ASSISTENTE_FAMILIARE:
				if (contrattoAllegati.getContratto().getAssistenteFamiliare() == null) {
					param[0] = "Assitente Familiare";
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				} else if (contrattoAllegati.getContratto().getAssistenteFamiliare().getCf() == null) {
					param[0] = "Cf assitente Familiare";
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				}

				result = cfCheck(result, contrattoAllegati.getContratto().getAssistenteFamiliare().getCf(),
						Constants.ASSISTENTE_FAMILIARE);

//				if (!Util.isValorizzato(contrattoAllegati.getContratto().getAssistenteFamiliare().getNome())) {
//					param[0] = "Nome assistente familiare";
//					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
//				} else if (!Util
//						.isValorizzato(contrattoAllegati.getContratto().getAssistenteFamiliare().getCognome())) {
//					param[0] = "Cognome assistente familiare";
//					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
//				} else if (contrattoAllegati.getContratto().getAssistenteFamiliare().getDataNascita() == null) {
//					param[0] = "Data nascita assistente familiare";
//					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
//				} else if (!Util
//						.isValorizzato(contrattoAllegati.getContratto().getAssistenteFamiliare().getComuneNascita())) {
//					param[0] = "Comune nascita assistente familiare";
//					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
//				} else if (!Util.isValorizzato(
//						contrattoAllegati.getContratto().getAssistenteFamiliare().getProvinciaNascita())) {
//					param[0] = "Provincia nascita assistente familiare";
//					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
//				} 
//				else if (!Util
//						.isValorizzato(contrattoAllegati.getContratto().getAssistenteFamiliare().getStatoNascita())) {
//					param[0] = "Stato nascita assistente familiare";
//					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
//				}

				ModelAllegato contrattoLavoro = contrattoAllegati.getAllegati().stream()
						.filter(f -> f.getTipo().equals(Constants.CONTRATTO_LAVORO)).findFirst().orElse(null);
				ModelAllegato denunciaInps = contrattoAllegati.getAllegati().stream()
						.filter(f -> f.getTipo().equals(Constants.DENUNCIA_INPS)).findFirst().orElse(null);
				if (contrattoLavoro == null) {
					param[0] = "Copia contratto di lavoro o lettera di assunzione";
					result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
				}
				if (denunciaInps == null) {
					param[0] = "Copia denuncia rapporto di lavoro domestico presentata a INPS";
					result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
				}
				if (contrattoAllegati.getAllegati().size() > 2) {
					param[0] = "numero allegati";
					result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
				}

				break;

			case Constants.PARTITA_IVA:
				if (contrattoAllegati.getContratto().getAssistenteFamiliare() == null) {
					param[0] = "Assitente Familiare";
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				} else if (contrattoAllegati.getContratto().getAssistenteFamiliare().getCf() == null) {
					param[0] = "Cf assitente Familiare";
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				}

				if (!Util.isValorizzato(contrattoAllegati.getContratto().getPivaAssitenteFamiliare())) {
					param[0] = "Partita Iva";
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				}

				result = cfCheck(result, contrattoAllegati.getContratto().getAssistenteFamiliare().getCf(),
						Constants.ASSISTENTE_FAMILIARE);

//				if (!Util.isValorizzato(contrattoAllegati.getContratto().getAssistenteFamiliare().getNome())) {
//					param[0] = "Nome assistente familiare";
//					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
//				} else if (!Util
//						.isValorizzato(contrattoAllegati.getContratto().getAssistenteFamiliare().getCognome())) {
//					param[0] = "Cognome assistente familiare";
//					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
//				} 
//				else if (contrattoAllegati.getContratto().getAssistenteFamiliare().getDataNascita() == null) {
//					param[0] = "Data nascita assistente familiare";
//					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
//				} else if (!Util
//						.isValorizzato(contrattoAllegati.getContratto().getAssistenteFamiliare().getComuneNascita())) {
//					param[0] = "Comune nascita assistente familiare";
//					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
//				} else if (!Util.isValorizzato(
//						contrattoAllegati.getContratto().getAssistenteFamiliare().getProvinciaNascita())) {
//					param[0] = "Provincia nascita assistente familiare";
//					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
//				} 
//				else if (!Util
//						.isValorizzato(contrattoAllegati.getContratto().getAssistenteFamiliare().getStatoNascita())) {
//					param[0] = "Stato nascita assistente familiare";
//					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
//				}

				ModelAllegato letteraIncarico = contrattoAllegati.getAllegati().stream()
						.filter(f -> f.getTipo().equals(Constants.LETTERA_INCARICO)).findFirst().orElse(null);
				if (letteraIncarico == null) {
					param[0] = "Copia lettera di incarico";
					result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
				}
				if (contrattoAllegati.getAllegati().size() > 1) {
					param[0] = "numero allegati";
					result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
				}
				break;

			case Constants.COOPERATIVA:
				if (contrattoAllegati.getContratto().getAgenzia() == null) {
					param[0] = "Cooperativa";
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				} else if (!Util.isValorizzato(contrattoAllegati.getContratto().getAgenzia().getCf())) {
					param[0] = "CF Cooperativa";
					result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
				}

				ModelAllegato contrattoLavoroCoop = contrattoAllegati.getAllegati().stream()
						.filter(f -> f.getTipo().equals(Constants.CONTRATTO_LAVORO_COOP)).findFirst().orElse(null);
				if (contrattoLavoroCoop == null) {
					param[0] = "Copia contratto di lavoro coop";
					result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
				}
				if (contrattoAllegati.getAllegati().size() > 1) {
					param[0] = "numero allegati";
					result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
				}
				break;
			}

			result = cfCheck(result, contrattoAllegati.getContratto().getIntestatario().getCf(), Constants.DATORE);

			if (!Util.isValorizzato(contrattoAllegati.getContratto().getIntestatario().getNome())) {
				param[0] = "Nome intestatario";
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			} else if (!Util.isValorizzato(contrattoAllegati.getContratto().getIntestatario().getCognome())) {
				param[0] = "Cognome intestatario";
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			} else if (contrattoAllegati.getContratto().getIntestatario().getDataNascita() == null) {
				param[0] = "Data nascita intestatario";
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			} else if (!Util.isValorizzato(contrattoAllegati.getContratto().getIntestatario().getComuneNascita())) {
				param[0] = "Comune nascita intestatario";
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			} else if (!Util.isValorizzato(contrattoAllegati.getContratto().getIntestatario().getProvinciaNascita())) {
				param[0] = "Provincia nascita intestatario";
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			} else if (!Util.isValorizzato(contrattoAllegati.getContratto().getIntestatario().getStatoNascita())) {
				param[0] = "Stato nascita intestatario";
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}

			if ((!destinatario.equals(contrattoAllegati.getContratto().getIntestatario().getCf())
					&& !shibIdentitaCodiceFiscale.equals(contrattoAllegati.getContratto().getIntestatario().getCf()))
					&& rapporto_tipo_id == null) {
				param[0] = "Tipo Relazione";
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}

		}

		return result;
	}

	public List<ErroreDettaglioExt> validateGetContratto(String numeroRichiesta, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale) throws DatabaseException {
		List<ErroreDettaglioExt> result = new ArrayList<>();
		String[] param = new String[1];

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (!Util.isValorizzato(numeroRichiesta)) {
			param[0] = "Numero Richiesta";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> validatePutContratto(String numeroRichiesta, Integer idContratto, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale, ModelContratto contratto)
			throws DatabaseException {
		List<ErroreDettaglioExt> result = new ArrayList<>();
		String[] param = new String[1];

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (!Util.isValorizzato(numeroRichiesta)) {
			param[0] = "Numero Richiesta";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (contratto == null) {
			param[0] = "Contratto";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (idContratto <= 0) {
			param[0] = "idContratto";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (contratto.getDataFine() == null) {
			param[0] = "Data fine contratto";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		return result;
	}

	public List<ErroreDettaglioExt> validateDelContratto(String numeroRichiesta, Integer idContratto, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale) throws DatabaseException {
		List<ErroreDettaglioExt> result = new ArrayList<>();
		String[] param = new String[1];

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (!Util.isValorizzato(numeroRichiesta)) {
			param[0] = "Numero Richiesta";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (idContratto <= 0) {
			param[0] = "idContratto";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> validateDeleteRendicontazione(String numeroRichiesta, Integer idDocumentoSpesa,
			String xRequestId, String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale)
			throws DatabaseException {
		List<ErroreDettaglioExt> result = new ArrayList<>();
		String[] param = new String[1];

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (!Util.isValorizzato(numeroRichiesta)) {
			param[0] = "Numero Richiesta";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (idDocumentoSpesa == 0) {
			param[0] = "idDocumentoSpesa";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> validateAddRendicontazione(String numeroRichiesta,
			ModelDocumentoSpesa documentoSpesa, String xRequestId, String xForwardedFor, String xCodiceServizio,
			String shibIdentitaCodiceFiscale) throws DatabaseException {
		List<ErroreDettaglioExt> result = new ArrayList<>();
		String[] param = new String[1];

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (!Util.isValorizzato(numeroRichiesta)) {
			param[0] = "Numero Richiesta";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		if (!Util.isValorizzato(documentoSpesa.getIdFornitore())) {
			param[0] = "idFornitore";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (!Util.isValorizzato(documentoSpesa.getNumero())) {
			param[0] = "Numero documento";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (!Util.isValorizzato(documentoSpesa.getTipologia())) {
			param[0] = "Tipologia Documento";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (!Util.isValorizzato(documentoSpesa.getStato())) {
			param[0] = "Stato documento di spesa";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		if (documentoSpesa.getMesi() == null || documentoSpesa.getMesi().size() == 0) {
			param[0] = "Mensilita'";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		for (int index = 0; index < documentoSpesa.getMesi().size(); index++) {
			if (Converter.getInt(documentoSpesa.getMesi().get(index).replace("-", "")) == 0
					|| documentoSpesa.getMesi().get(index).length() != 7
					|| Converter.getInt(documentoSpesa.getMesi().get(index).substring(5)) > 12
					|| Converter.getInt(documentoSpesa.getMesi().get(index).substring(5)) == 0) {
				param[0] = "Mensilita'";
				result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
			}
		}
		if (documentoSpesa.getDettagli() == null || documentoSpesa.getDettagli().size() == 0) {
			param[0] = "Dettaglio spesa";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}
		for (ModelDocumentoSpesaDettaglio spesa : documentoSpesa.getDettagli()) {
			if (!Util.isValorizzato(spesa.getTipo())) {
				param[0] = "Tipo dettaglio spesa";
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}
			if (!Util.isValorizzato(spesa.getIdAllegato())) {
				param[0] = "Allegato dettaglio spesa";
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}
			if (spesa.getData() == null) {
				param[0] = "Data dettaglio spesa";
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}
			if (spesa.getImporto() == null || spesa.getImporto() == BigDecimal.ZERO) {
				param[0] = "Importo dettaglio spesa";
				result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
			}
		}
		return result;
	}

	public List<ErroreDettaglioExt> validateGetRendicontazione(String numeroRichiesta, String xRequestId,
			String xForwardedFor, String xCodiceServizio, String shibIdentitaCodiceFiscale) throws DatabaseException {
		List<ErroreDettaglioExt> result = new ArrayList<>();
		String[] param = new String[1];

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);

		if (!Util.isValorizzato(numeroRichiesta)) {
			param[0] = "Numero Richiesta";
			result.add(getValueGenericError(CodeErrorEnum.ERR01.getCode(), param));
		}

		return result;
	}

	public List<ErroreDettaglioExt> checkAllegatoBuonoFileSystem(List<ErroreDettaglioExt> result, String filepath,
			String filename) throws DatabaseException {

		// verifico coerenzaa tra allegati db e allegati file system
		String os = System.getProperty("os.name");
		boolean locale = false;
		String[] param = new String[1];
		if (os.toLowerCase().contains("win")) {
			locale = true;
		}
		boolean trovato = true;
		if (!getAllegato(filepath, filename)) {
			result.add(getValueGenericError(CodeErrorEnum.ERR18.getCode(), null));
		}
		return result;
	}

	public List<ErroreDettaglioExt> checkAllegatoContrattoFileSystem(ModelContrattoAllegati contrattoAllegati)
			throws DatabaseException {
		String os = System.getProperty("os.name");
		boolean locale = false;
		List<ErroreDettaglioExt> result = new ArrayList<>();
		String[] param = new String[1];
		if (os.toLowerCase().contains("win")) {
			locale = true;
		}

		for (int i = 0; i < contrattoAllegati.getAllegati().size(); i++) {

			File file = null;

			ModelGetAllegato allegato = allegatoBuonoDao
					.selectAllegatoBuono(contrattoAllegati.getAllegati().get(i).getId().intValue());

			if (!locale)
				file = new File(allegato.getFilePath() + "/" + allegato.getFileName());
			else
				file = new File(allegato.getFilePath() + "\\" + allegato.getFileName());

			if (!file.exists()) {
				param[0] = "allegato: " + allegato.getFileName();
				result.add(getValueGenericError(CodeErrorEnum.ERR02.getCode(), param));
			}
		}
		return result;
	}

	public List<ErroreDettaglioExt> validateAccredito(ModelRichiestaAccredito accredito,
			String shibIdentitaCodiceFiscale, String xRequestId, String xForwardedFor, String xCodiceServizio,
			SecurityContext securityContext, HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
			throws DatabaseException {
		String methodName = "validate";
		logInfo(methodName, "BEGIN");

		List<ErroreDettaglioExt> result = new ArrayList<>();

		result = commonCheck(result, shibIdentitaCodiceFiscale, xRequestId, xForwardedFor, xCodiceServizio);
		accreditoCheck(result, accredito);

		return result;
	}
}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomcallban.util;

public class Constants {

	public final static String COMPONENT_NAME = "buonodomcallban";

	public static final String CONTEXT_CHIAVE_ID = "chiaveid";
	public static final String CONTEXT_TEMPO_PARTENZA = "tempopartenza";
	public static final String SHIB_IDENTITA_CODICE_FISCALE = "Shib-Identita-CodiceFiscale";
	public static final String X_REQUEST_ID = "X-Request-ID";
	public static final String X_CODICE_SERVIZIO = "X-Codice-Servizio";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";
	public static final String SHIB_IRIDE_IDENTITADIGITALE = "Shib-Iride-IdentitaDigitale";

	// PARAMETRI
	public static final String PARAMETRO_ERRORE_TIPO = "ERRORE";
	public static final String PARAMETRO_MESSAGGIO_TIPO = "MESSAGGIO";
	public static final String PARAMETRO_GENERICO = "GENERICO";
	// stardas esito postivo
	public static final String OK = "OK";
	public static final String OK_PARZIALE = "001";
	public static final String OK_DESCRIZIONE = "MSG001";
	public static final String KO = "KO";
	public static final String KO_DESCRIZIONE = "Segnalazione errori generici di sistema";
	public static final String KO_APPLICATIVI = "199";
	public static final String KO_DESCRIZIONE_APPLICATIVI = "Segnalazione di errori applicativi";
	public static final String CARICATA = "CARICATA";
	public static final String DA_INVIARE = "DA_INVIARE";
}

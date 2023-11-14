/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.util;

public class Constants {

	public final static String COMPONENT_NAME = "buonodombo";

	public static final String CONTEXT_CHIAVE_ID = "chiaveid";
	public static final String CONTEXT_TEMPO_PARTENZA = "tempopartenza";
	public static final String SHIB_IDENTITA_CODICE_FISCALE = "Shib-Identita-CodiceFiscale";
	public static final String X_REQUEST_ID = "X-Request-ID";
	public static final String X_CODICE_SERVIZIO = "X-Codice-Servizio";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";

	public static final String SHIB_IRIDE_IDENTITADIGITALE = "Shib-Iride-IdentitaDigitale";
	// nomi API per log
	public static final String ANAGRAFICA_API = "ANAGRAFICAAPI";

	// PARAMETRI
	public static final String PARAMETRO_ERRORE_TIPO = "ERRORE";
	public static final String PARAMETRO_MESSAGGIO_TIPO = "MESSAGGIO";
	public static final String PARAMETRO_GENERICO = "GENERICO";

	public static final String MSG002 = "MSG002";

	// CODICI CONTRIBUTI
	public static final String BUONODOM = "BUONODOMBO";

	// CODICI STATO DOMANDA
	public static final String BOZZA = "BOZZA";
	public static final String INVIATA = "INVIATA";
	public static final String ANNULLATA = "ANNULLATA";
	public static final String RETTIFICATA = "RETTIFICATA";
	public static final String IN_RETTIFICA = "IN_RETTIFICA";
	public static final String AMMESSA_RISERVA = "AMMESSA_RISERVA";
	public static final String AMMESSA = "AMMESSA";
	public static final String PREAVVISO_DINIEGO = "PREAVVISO_DINIEGO";
	public static final String PERFEZIONATA_IN_PAGAMENTO = "PERFEZIONATA_IN_PAGAMENTO";
	public static final String IN_PAGAMENTO = "IN_PAGAMENTO";
	public static final String DINIEGO = "DINIEGO";
	public static final String NON_AMMESSA = "NON_AMMESSA";
	public static final String AMMISSIBILE = "AMMISSIBILE";
	public static final String DA_RETTIFICARE = "DA_RETTIFICARE";
	public static final String REVOCATA = "REVOCATA";
	public static final String PRESA_IN_CARICO = "PRESA_IN_CARICO";
	public static final String NON_AMMISSIBILE = "NON_AMMISSIBILE";
	public static final String PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA = "PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA";
	public static final String CONTRODEDOTTA = "CONTRODEDOTTA";
	public static final String AMMESSA_RISERVA_IN_PAGAMENTO = "AMMESSA_RISERVA_IN_PAGAMENTO";

	public static final String LETTERA_DINIEGO = "LETTERA_DINIEGO";
	// CODICI STATO GRADUATORIA
	public static final String PROVVISORIA = "PROVVISORIA";
	public static final String DA_AGGIORNARE = "DA_AGGIORNARE";
	public static final String PUBBLICATA = "PUBBLICATA";

	// TIPO PERSONA
	public static final String RICHIEDENTE = "RICHIEDENTE";
	public static final String DESTINATARIO = "DESTINATARIO";
	public static final String ASSISTENTE = "ASSISTENTE";
	public static final String DATORE = "DATORE";

	// VALUTAZIONE MULTIDIMENSIONALE
	public static final String PERSONA_PIU_65_ANNI = "UVG";
	public static final String PERSONA_DISABILE = "UMVD";

	// ISEE
	public static final Integer ATTESTAZIONE_ISEE_NON_DISABILE = 50000;
	public static final Integer ATTESTAZIONE_ISEE_DISABILE = 65000;

	// path
	public static final String PATH_ARCHIVIAZIONE = "PATH_ARCHIVIAZIONE";

	// TIPOLOGIA FILE
	public static final String PDF = "pdf";
	public static final String JPEG = "jpeg";
	public static final String PNG = "png";

	// TIPO FILE
	public static final String DELEGA = "DELEGA";
	public static final String CARTA_IDENTITA = "CARTA_IDENTITA";
	public static final String CONTRATTO_LAVORO = "CONTRATTO_LAVORO";
	public static final String CONTRATTO_LAVORO_COOP = "CONTRATTO_LAVORO_COOP";
	public static final String DENUNCIA_INPS = "DENUNCIA_INPS";
	public static final String VERBALE_UVG = "VERBALE_UVG";
	public static final String VERBALE_UMVD = "VERBALE_UMVD";
	public static final String LETTERA_INCARICO = "LETTERA_INCARICO";
	public static final String DOMANDA = "DOMANDA";

	// TIPOLOGIA CONTRATTO
	public static final String NESSUN_CONTRATTO = "NESSUN_CONTRATTO";
	public static final String ASSISTENTE_FAMILIARE = "ASSISTENTE_FAMILIARE";
	public static final String COOPERATIVA = "COOPERATIVA";
	public static final String PARTITA_IVA = "PARTITA_IVA";

	// TIPO FILE
	public static final String NUCLEO_FAMILIARE = "NUCLEO_FAMILIARE";
	public static final String CONIUGE = "CONIUGE";
	public static final String PARENTE_PRIMO_GRADO = "PARENTE_PRIMO_GRADO";
	public static final String TUTELA = "TUTELA";
	public static final String CURATELA = "CURATELA";
	public static final String AMMINISTRAZIONE_SOSTEGNO = "AMMINISTRAZIONE_SOSTEGNO";
	public static final String POTESTA_GENITORIALE = "POTESTA_GENITORIALE";
	public static final String ALTRO = "ALTRO";
	public static final String PROCURA_SPECIALE = "PROCURA_SPECIALE";
	// abilita chiamate sistemi esterni
	public static final String CHIAMA_INTERROGAMEF = "CHIAMA_INTERROGAMEF";

	public static final String AAEP = "AAEP";
	public static final String CONTRATTO_TIPO_ID = "contratto_tipo_id";
	public static final String ASSISTENTE_TIPO_ID = "assistente_tipo_id";

	// TIPO VERIFICA ENTE GESTORE
	public static final String RICHIESTA_VERIFICA = "RICHIESTA_VERIFICA";
	public static final String VERIFICA_IN_CORSO = "VERIFICA_IN_CORSO";
	public static final String VERIFICA_EFFETTUATA = "VERIFICA_EFFETTUATA";
	public static final String VERIFICA_NON_RICHIESTA = "VERIFICA_NON_RICHIESTA";
	public static final String IN_VERIFICA = "IN_VERIFICA";

	// EMAIL
	public static final String OBJECT_MESSAGE = "OBJECT_EMAIL_ENTE";
	public static final String BODY_MESSAGE = "BODY_EMAIL_ENTE";
}

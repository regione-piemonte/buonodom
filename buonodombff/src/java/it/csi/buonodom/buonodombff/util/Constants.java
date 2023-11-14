/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombff.util;

public class Constants {

	public final static String COMPONENT_NAME = "buonodombff";

	public static final String CONTEXT_CHIAVE_ID = "chiaveid";
	public static final String CONTEXT_TEMPO_PARTENZA = "tempopartenza";
	public static final String SHIB_IDENTITA_CODICE_FISCALE = "Shib-Identita-CodiceFiscale";
	public static final String SHIB_IRIDE_IDENTITADIGITALE = "Shib-Iride-IdentitaDigitale";
	public static final String X_REQUEST_ID = "X-Request-ID";
	public static final String X_CODICE_SERVIZIO = "X-Codice-Servizio";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";
	// nomi API per log
	public static final String ANAGRAFICA_API = "ANAGRAFICAAPI";

	// PARAMETRI
	public static final String PARAMETRO_ERRORE_TIPO = "ERRORE";
	public static final String PARAMETRO_MESSAGGIO_TIPO = "MESSAGGIO";
	public static final String PARAMETRO_GENERICO = "GENERICO";

	// CODICI CONTRIBUTI
	public static final String BUONODOM = "DOM";

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
	public static final String DA_RETTIFICARE = "DA_RETTIFICARE";
	public static final String REVOCATA = "REVOCATA";
	public static final String PRESA_IN_CARICO = "PRESA_IN_CARICO";
	public static final String PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA = "PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA";
	public static final String AMMISSIBILE = "AMMISSIBILE";
	public static final String NON_AMMISSIBILE = "NON_AMMISSIBILE";
	public static final String CONTRODEDOTTA = "CONTRODEDOTTA";
	public static final String AMMESSA_CON_RISERVA_IN_PAGAMENTO = "AMMESSA_RISERVA_IN_PAGAMENTO";
	public static final String PREAVVISO_DI_DINIEGO_IN_PAGAMENTO = "PREAVVISO_DINIEGO_IN_PAGAMENTO";
	public static final String RINUNCIATA = "RINUNCIATA";
	public static final String RINUNCIATO = "RINUNCIATO";
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
	public static final String NOMINA_TUTORE = "NOMINA_TUTORE";
	public static final String DOMANDA = "DOMANDA";

	// TIPOLOGIA CONTRATTO
	public static final String NESSUN_CONTRATTO = "NESSUN_CONTRATTO";
	public static final String ASSISTENTE_FAMILIARE = "ASSISTENTE_FAMILIARE";
	public static final String COOPERATIVA = "COOPERATIVA";
	public static final String PARTITA_IVA = "PARTITA_IVA";

	// TIPO ASSISTENTE
	public static final String EDUCATORE_PROFESSIONALE = "EDUCATORE_PROFESSIONALE";
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
	public static final String CONTRODEDUZIONE = "CONTRODEDUZIONE";
	// abilita chiamate sistemi esterni
	public static final String CHIAMA_INTERROGAMEF = "CHIAMA_INTERROGAMEF";

	// graduatoria
	public static final String PROVVISORIA = "PROVVISORIA";
	public static final String DA_AGGIORNARE = "DA_AGGIORNARE";

	// BUONO
	public static final String CARICATA = "CARICATA";
	public static final String DA_INVIARE = "DA_INVIARE";
	public static final String GIUSTIFICATIVO = "GIUSTIFICATIVO";
	public static final String QUIETANZA = "QUIETANZA";
	public static final String DA_VALIDARE = "DA_VALIDARE";

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombatch.util;

public class Constants {

	public final static String COMPONENT_NAME = "buonodombatch";

	public static final String SHIB_IDENTITA_CODICE_FISCALE = "Shib-Identita-CodiceFiscale";
	public static final String SHIB_IRIDE_IDENTITADIGITALE = "Shib-Iride-IdentitaDigitale";
	public static final String X_REQUEST_ID = "X-Request-ID";
	public static final String X_CODICE_SERVIZIO = "X-Codice-Servizio";
	public static final String X_FORWARDED_FOR = "X-Forwarded-For";
	public static final String XREQUESTID = "buonodombatch";
	public static final String XCODICESERVIZIO = "BUONODOMBATCH";
	public static final String XFORWARDEDFOR = "127.127.127.127";
	// CODICI STATO DOMANDA
	public static final String BOZZA = "BOZZA";
	public static final String INVIATA = "INVIATA";
	public static final String ANNULLATA = "ANNULLATA";
	public static final String RETTIFICATA = "RETTIFICATA";
	public static final String IN_RETTIFICA = "IN_RETTIFICA";
	public static final String AMMESSA_RISERVA_IN_PAGAMENTO = "AMMESSA_RISERVA_IN_PAGAMENTO";
	public static final String AMMESSA = "AMMESSA";
	public static final String PREAVVISO_DINIEGO_IN_PAGAMENTO = "PREAVVISO_DINIEGO_IN_PAGAMENTO";
	public static final String IN_PAGAMENTO = "IN_PAGAMENTO";
	public static final String DINIEGO = "DINIEGO";
	public static final String NON_AMMESSA = "NON_AMMESSA";
	public static final String DA_RETTIFICARE = "DA_RETTIFICARE";
	public static final String AMMESSA_RISERVA = "AMMESSA_RISERVA";
	public static final String NON_AMMISSIBILE = "NON_AMMISSIBILE";
	public static final String PRESA_IN_CARICO = "PRESA_IN_CARICO";
	public static final String PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA = "PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA";
	public static final String AMMISSIBILE = "AMMISSIBILE";
	public static final String CONTRODEDOTTA = "CONTRODEDOTTA";
	public static final String PERFEZIONATA_IN_PAGAMENTO = "PERFEZIONATA_IN_PAGAMENTO";

	public static final String LETTERA_DINIEGO = "LETTERA_DINIEGO";
	public static final String DINIEGO_SCADENZA_REQUISTI_DECESSO = "DINIEGO_SCADENZA_REQUISTI_DECESSO";
	public static final String DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA = "DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA";
	public static final String BO_DINIEGO_SCADENZA_REQUISTI_DECESSO = "BO_DINIEGO_SCADENZA_REQUISTI_DECESSO";
	public static final String BO_DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA = "BO_DINIEGO_SCADENZA_REQUISTI_CAMBIO_RESIDENZA";
	// giorni batch

	public static final String BO_DINIEGO_SCADENZA_REQUISTI = "BO_DINIEGO_SCADENZA_REQUISTI";
	public static final String DA_RETTIFICARE_GIORNI = "DA_RETTIFICARE_GIORNI";
	public static final String CONTRODEDOTTA_GIORNI = "CONTRODEDOTTA_GIORNI";
	public static final String AMMESSA_RISERVA_PREAVVISO_DINIEGO_GIORNI = "AMMESSA_RISERVA_PREAVVISO_DINIEGO_GIORNI";
	public static final String PREAVVISO_DINIEGO_A_DINIEGO_GIORNI = "PREAVVISO_DINIEGO_A_DINIEGO_GIORNI";
	public static final String TIPO_PARAMETRO_GIORNI = "BATCH";
	public static final String PAUSA = "PAUSA";
	public static final String BO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO = "BO_PREAVVISO_DI_DINIEGO_IN_PAGAMENTO";
	public static final String BO_AMMESSA_RISERVA_CONTRATTO = "BO_AMMESSA_RISERVA_CONTRATTO";
	public static final String BO_DINIEGO = "BO_DINIEGO";
	public static final String PROVVISORIA = "PROVVISORIA";
	public static final String PUBBLICATA = "PUBBLICATA";
	public static final String DA_AGGIORNARE = "DA_AGGIORNARE";
	public static final String LETTERA_AMMISSIONE_FINANZIAMENTO = "LETTERA_AMMISSIONE_FINANZIAMENTO";

	public static final String BATCH_CONTROLLO_DECESSO_RESIDENZA = "BATCH_CONTROLLO_DECESSO_RESIDENZA";
	public static final String GENERICO = "GENERICO";

	public static final String NOTIFICA_AMMESSA_RISERVA_IN_PAGAMENTO = "NOTIFICA_AMMESSA_RISERVA_IN_PAGAMENTO";
	public static final String NOTIFICA_DINIEGO = "NOTIFICA_DINIEGO";
}

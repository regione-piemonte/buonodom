/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export default function () {
  return {
    config: {},
    sportelli: null,
    contatti: null,
    preferenze: null,
    utente_richiedente: null,
    utente_destinatario: null,
    spinner: false,
    spinner_stepper: false,
    stepperList: [
      {
        label: 'Informativa sulla privacy e condizioni per ricevere il buono'
      },
      {
        label: 'Destinatario buono'
      },
      {
        label: 'Compilazione modulo'
      },
      {
        label: 'Riepilogo e invio'
      }
    ],
    tipi_rapporto: null,
    titoli_studio: null,
    asl: null,
    tipi_contratto: null,
    stati_domanda: null,
    tipi_supporto_familiare : null,
    tipi_documento_rendicontazione: null,
    tipi_allegato_buono: null,
    tipi_dettaglio_documento_spesa: null,
    // SALVIAMO NELLO STORE OGNI VOLTA CHE CLICCHIAMO AVANTI
    // CAMPI ESTERNI ALL'OGGETTO
    step: 0,
    step_1: false,
    step_2: false,
    step_3: true,
    privacy_toogle: false,
    consenso_toogle: false,
    stepperForm: {
      domicilio_radio: null,
      destinatario_radio: null,
      tipo_intestatario: null,
      show_destinatario: null,
      show_assistente_familiare: null,
      show_altro_soggetto: null,
    },

    // OGGETTO EFFETTIVO DA INVIARE
    richiesta: {
      numero: null,
      stato: 'BOZZA',
      data_aggiornamento: null,
      richiedente: {
        cf: '',
        nome: null,
        cognome: null,
        data_nascita: null,
        stato_nascita: null,
        comune_nascita: null,
        provincia_nascita: null,
        indirizzo_residenza: null,
        comune_residenza: null,
        provincia_residenza: null
      },
      destinatario: {
        cf: '',
        nome: null,
        cognome: null,
        data_nascita: null,
        stato_nascita: null,
        comune_nascita: null,
        provincia_nascita: null,
        indirizzo_residenza: null,
        comune_residenza: null,
        provincia_residenza: null
      },
      note: null,
      studio_destinatario: null,
      lavoro_destinatario: null,
      domicilio_destinatario: {
        indirizzo: null,
        comune: null,
        provincia: ""
      },
      asl_destinatario: null,
      delega: null,
      attestazione_isee: false,
      punteggio_bisogno_sociale: null,
      contratto: {
        tipo: null,
        intestatario: {
          cf: null,
          nome: null,
          cognome: null,
          data_nascita: null,
          stato_nascita: null,
          comune_nascita: null,
          provincia_nascita: null,
        },
        relazione_destinatario: null,
        assistente_familiare: {
          cf: null,
          nome: null,
          cognome: null,
          data_nascita: null,
          stato_nascita: null,
          comune_nascita: null,
          provincia_nascita: null
        },
        piva_assitente_familiare: null,
        tipo_supporto_familiare: null,
        data_inizio: null,
        data_fine: null,
        agenzia: {
          cf: null
        },
        incompatibilita_per_contratto: null
      },
      nessuna_incompatibilita: null,
      accredito: {
        iban: null,
        intestatario: null
      }
    }
  }
}

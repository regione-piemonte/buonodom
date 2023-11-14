/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import store from "src/store/index";

export const setContatti = (context, contatti ) => {
  context.commit("SET_CONTATTI", { contatti });
};

export const setPreferenze = (context, preferenze ) => {
  context.commit("SET_PREFERENZE", { preferenze });
};

//LOGICHE LEGATE AL BUONO
export const setUtenteRichiedente = (context,  utente_richiedente ) => {
  context.commit("SET_UTENTE_RICHIEDENTE", { utente_richiedente });
};

export const setUtenteDestinatario = (context,  utente_destinatario ) => {
  context.commit("SET_UTENTE_DESTINATARIO", { utente_destinatario });
};

export const setSpinner = (context,  spinner ) => {
  context.commit("SET_SPINNER", { spinner });
};

export const setSpinnerStepper = (context,  spinner_stepper ) => {
  context.commit("SET_SPINNER_STEPPER", { spinner_stepper });
};

export const setSportelli = (context, { sportelli }) => {
  context.commit("SET_SPORTELLI", { sportelli });
};

export const setOptions = (context,  item ) => {
  context.commit("SET_OPTIONS",  item );
};

export const setStepperList = (context, { stepperList }) => {
  context.commit("SET_STEPPER_LIST", { stepperList });
};

export const setStep = (context, step ) => {
  context.commit("SET_STEP", { step });
};

export const setNextStep = (context, item ) => {
  context.commit("SET_NEXT_STEP", item);
};

export const resetAllStep = (context) => {
  context.commit("RESET_ALL_STEP");
};

export const bozzaAllStep = (context) => {
  context.commit("BOZZA_ALL_STEP");
};

export const setRichiesta = (context, item ) => {
  context.commit("SET_RICHIESTA", item);
};

export const setStepperForm = (context, item ) => {
  context.commit("SET_STEPPER_FORM", item);
};

export const resetAllState = async (context) => {
  let stepperForm = {
    stepperForm: {
      domicilio_radio: null,
      destinatario_radio: null,
      tipo_intestatario: null,
      show_destinatario: false,
    },
  };
  let richiesta = {
    // OGGETTO EFFETTIVO DA INVIARE
    numero: null,
    stato: "BOZZA",
    data_aggiornamento: null,
    richiedente: {
      cf: null,
      nome: null,
      cognome: null,
      data_nascita: null,
      stato_nascita: null,
      comune_nascita: null,
      provincia_nascita: null,
      indirizzo_residenza: null,
      comune_residenza: null,
      provincia_residenza: null,
    },
    destinatario: {
      cf: null,
      nome: null,
      cognome: null,
      data_nascita: null,
      stato_nascita: null,
      comune_nascita: null,
      provincia_nascita: null,
      indirizzo_residenza: null,
      comune_residenza: null,
      provincia_residenza: null,
    },
    note: null,
    studio_destinatario: null,
    lavoro_destinatario: null,
    domicilio_destinatario: {
      indirizzo: null,
      comune: null,
      provincia: "",
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
        provincia_nascita: null,
      },
      piva_assitente_familiare: null,
      tipo_supporto_familiare: null,
      data_inizio: null,
      data_fine: null,
      agenzia: {
        cf: null,
      },
      incompatibilita_per_contratto: null
    },
    nessuna_incompatibilita: null,
    accredito: {
      iban: null,
      intestatario: null,
    },
  };
  await store.dispatch("resetAllStep");
  await store.commit("SET_PRIVACY", false);
  await store.commit("SET_AGREMENT", false);
  await store.dispatch("setStepperForm", stepperForm);
  await store.dispatch("setRichiesta", richiesta);
};





/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */


// sportelli sono fondamentali senza essi l'applicativo non puÃ² fare nuove domande o continuare bozze
export const getSpinner = state => {
  return state.spinner;
};

export const getSpinnerStepper = state => {
  return state.spinner_stepper;
};

// sportelli sono fondamentali senza essi l'applicativo non puÃ² fare nuove domande o continuare bozze
export const getSportelli = state => {
  return state.sportelli;
};

// stepper list
export const getStepperList = state => {
  return state.stepperList;
};

// step attuale
export const getStep = state => {
  return state.step;
};

// utente richiedente
export const getUtenteRichiedente = state => {
  return state.utente_richiedente;
};

// utente destinatario
export const getUtenteDestinatario = state => {
  return state.utente_destinatario;
};

// situazione richiesta attuale
export const getRichiesta = state => {
  return state.richiesta;
};

export const getContatti = state => {
  return state.contatti;
};




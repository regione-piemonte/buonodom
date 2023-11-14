/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */


import {httpBuonoDom, httpScelSoc} from "src/boot/http";

const bffPath = ''

// DA RIVEDERE
export const getUser = (httpConfig = {}) => {
  return httpScelSoc.get(`/bff/identity`, httpConfig);
};

export const getNotifyContacts = (taxCode, httpConfig = {}) => {
  const url = `/notify-preferences/api/v1/users/${taxCode}/contacts`;
  return httpScelSoc.get(url, httpConfig);
};

// API VERTICALE BOZZE

// RICHIESTE

// GET
export const getServizioAttivo = (httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/servizio-attivo`, httpConfig);
};

export const getSportelli = (httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/sportelli`, httpConfig);
};

export const getContatti = (httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/contatti`, httpConfig);
};

export const getPreferenze = (httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/preferenze`, httpConfig);
};

export const getRichieste = (httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/richieste`, httpConfig);
};

export const getRichiesta = (id, httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/richieste/${id}`, httpConfig);
};


// POST
export const postRichiesta = (payload, httpConfig = {}) => {
  const url = `${bffPath}/richieste`;
  return httpBuonoDom.post(url, payload, httpConfig);
};


// PUT
export const putRichiesta = (id,payload, httpConfig = {}) => {
  const url = `${bffPath}/richieste/${id}`;
  return httpBuonoDom.put(url, payload, httpConfig);
};

// CRONOLOGIA

// GET
export const getCronologia = (id, httpConfig = {}) => {
  return httpBuonoDom.get(`/cronologia/${id}`, httpConfig);
};

// POST
export const postCronologia = (id, payload, httpConfig = {}) => {
  const url = `${bffPath}/cronologia/${id}`;
  return httpBuonoDom.post(url, payload, httpConfig);
};

// ALLEGATI

// GET
export const getAllegato = (id, type, httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/allegato/${id}/${type}`, httpConfig);
};

// POST
export const postAllegato = (id, type, payload, httpConfig = {}) => {
  const url = `${bffPath}/allegato/${id}/${type}`;
  return httpBuonoDom.post(url, payload, httpConfig);
};

// ANAGRAFICA

// GET
export const getAnagrafica= (cf, httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/anagrafica/${cf}`, httpConfig);
};

// POST
export const postAnagrafica = (name, payload, httpConfig = {}) => {
  const url = `${bffPath}/anagrafica/${name}`;
  return httpBuonoDom.post(url, payload, httpConfig);
};

// DECODIFICHE

// GET
// /asl /titoli-studio /tipi-rapporto /tipi-contratto /tipi-allegato /stati-domanda
export const getDecodifiche= (name, httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/decodifiche/${name}`, httpConfig);
};


// RENDICONTAZIONE CONTRATTI E MONITORAGGIO

// CONTRATTI
//GET
export const getContratti = (id, httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/contratti/${id}`, httpConfig);
};

// POST
export const postContratti = (id, payload, httpConfig = {}) => {
  const url = `${bffPath}/contratti/${id}`;
  return httpBuonoDom.post(url, payload, httpConfig);
};

// PUT
export const putContratti = (id_domanda, id_contract, payload, httpConfig = {}) => {
  const url = `${bffPath}/contratti/${id_domanda}/${id_contract}`;
  return httpBuonoDom.put(url, payload, httpConfig);
};

// DELETE
export const deleteContratti = (id_domanda, id_contract, httpConfig = {}) => {
  const url = `${bffPath}/contratti/${id_domanda}/${id_contract}`;
  return httpBuonoDom.delete(url, httpConfig);
};

// ACCREDITO

// GET
export const getAccredito = (id, httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/accredito/${id}`, httpConfig);
};

// POST
export const postAccredito = (id, payload, httpConfig = {}) => {
  const url = `${bffPath}/accredito/${id}`;
  return httpBuonoDom.post(url, payload, httpConfig);
};


// RENDICONTAZIONE
//GET
export const getFornitori = (id, httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/fornitori/${id}`, httpConfig);
};

//GET
export const getRendicontazione = (id, httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/documento-spesa/${id}`, httpConfig);
};

//GET
export const getPeriodoRendicontazione = (id, httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/periodo-rendicontazione/${id}`, httpConfig);
};

// POST
export const postRendicontazione = (id, payload, httpConfig = {}) => {
  const url = `${bffPath}/documento-spesa/${id}`;
  return httpBuonoDom.post(url, payload, httpConfig);
};

// PUT
export const putPeriodoRendicontazione = (id,payload, httpConfig = {}) => {
  const url = `${bffPath}/periodo-rendicontazione/${id}`;
  return httpBuonoDom.put(url, payload, httpConfig);
};

// DELETE
export const deleteRendicontazione = (id_domanda, id_rendicontazione, httpConfig = {}) => {
  const url = `${bffPath}/rendicontazione/${id_domanda}/${id_rendicontazione}`;
  return httpBuonoDom.delete(url, httpConfig);
};

// CRONOLOGIA BUONO

// GET
export const getCronologiaBuono = (id, httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/cronologia-buono/${id}`, httpConfig);
};

// POST
export const postCronologiaBuono = (id, payload, httpConfig = {}) => {
  const url = `${bffPath}/cronologia-buono/${id}`;
  return httpBuonoDom.post(url, payload, httpConfig);
};

// ALLEGATI BUONO
//GET
export const getAllegatoBuono = (id, httpConfig = {}) => {
  return httpBuonoDom.get(`${bffPath}/rendicontazione/${id}`, httpConfig);
};

export const postAllegatoBuono = ( id, type, payload, httpConfig = {}) => {
  const url = `${bffPath}/allegato-buono/${id}/${type}`
  return httpBuonoDom.post(url, payload, httpConfig);
};


/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelContrattoAgenzia } from "./ModelContrattoAgenzia";
import { ModelPersonaSintesi } from "./ModelPersonaSintesi";

export class ModelContratto {
  constructor(
    public id: number,
    public tipo: String,
    public intestatario: ModelPersonaSintesi,
    public relazione_destinatario: String,
    public assistente_familiare: ModelPersonaSintesi,
    public piva_assitente_familiare: String,
    public tipo_supporto_familiare: String,
    public data_inizio: Date,
    public data_fine: Date,
    public agenzia: ModelContrattoAgenzia,
    public incompatibilita_per_contratto: boolean,
  ) { }
}

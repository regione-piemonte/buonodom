/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelPersona } from "./ModelPersona";
import { ModelRichiestaContrattoAgenzia } from "./ModelRichiestaContrattoAgenzia";

export class ModelRichiestaContratto {
    constructor(
     public tipo: string,
	 public intestatario: ModelPersona,
	 public relazione_destinatario: string,
     public relazione_destinatario_desc: string,
	 public assistente_familiare: ModelPersona,
	 public piva_assitente_familiare: string,
	 public tipo_supporto_familiare: string,
	 public data_inizio: Date,
	 public data_fine: Date,
	 public agenzia: ModelRichiestaContrattoAgenzia,
	 public incompatibilita_per_contratto: boolean,
    ) {}
}

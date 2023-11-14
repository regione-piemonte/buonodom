/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelAllegatoBuono } from "./ModelAllegatoBuono"

export class ModelDocumentoSpesaDettaglio {
  constructor(
    public idDocSpesaDettaglio: Number,
    public documentoSpesaDettaglioData: Date,
    public importo: String,
    public allegati: ModelAllegatoBuono[]
  ) { }
}

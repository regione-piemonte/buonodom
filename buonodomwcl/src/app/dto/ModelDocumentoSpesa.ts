/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelDocumentoSpesaDettaglio } from "./ModelDocumentoSpesaDettaglio";

export class ModelDocumentoSpesa {
  constructor(
    public idDocSpesa: Number,
    public doc_tipo: String,
    public doc_numero: String,
    public periodoInizio: Date,
    public periodoFine: Date,
    public documentiSpesaDettaglio: ModelDocumentoSpesaDettaglio[]
  ) { }
}

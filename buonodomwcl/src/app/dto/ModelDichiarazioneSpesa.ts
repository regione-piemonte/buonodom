/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelAllegatoBuono } from "./ModelAllegatoBuono";
import { ModelDocumentoSpesa } from "./ModelDocumentoSpesa";


export class ModelDichiarazioneSpesa {
  constructor(
    public idDicSpesa: number,
    public dicSpesaCod: String,
    public dicSpesaPeriodoDesc: String,
    public dicSpesaData: Date,
    public dicSpesaCodBandi: String,
    public periodoInizio: Date,
    public periodoFine: Date,
    public allegatiDichiarazioneSpesa: ModelAllegatoBuono[],
    public documentiSpesa: ModelDocumentoSpesa[]
  ) { }
}

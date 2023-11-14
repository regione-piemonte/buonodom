/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelAllegatoBuono } from "./ModelAllegatoBuono";
import { ModelContratto } from "./ModelContratto";

export class ModelContrattoAllegati {
  constructor(
    public contratto: ModelContratto,
    public allegati: ModelAllegatoBuono[]
  ) { }
}

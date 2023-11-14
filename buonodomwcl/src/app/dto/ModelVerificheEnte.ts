/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelVerificheEnte {
  constructor(
    public presenzaAltreIncopatibilita: boolean,
    public noteEnte: String,
    public dataVerifica: Date
  ) { }
}

/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelRichiestaAccredito {
    constructor(
        public iban: string,
 		public intestatario: string,
    ) {}
}

/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelDomicilio {
    constructor(
        public indirizzo: string,
        public comune: string,
 		public provincia: string,
    ) {}
}

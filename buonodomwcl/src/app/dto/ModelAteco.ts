/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelAteco {
    constructor(
        public ateco_cod: string,
        public ateco_desc: string,
        public ateco_verificato_in_data: Date,
    ) {}
}

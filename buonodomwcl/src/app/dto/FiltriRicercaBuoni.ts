/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export class FiltriRicercaBuoni {
    constructor(
        public statoBuono: string,
        public numeroBuono: string,
        public richiedente: string,
        public destinatario: string,
        public numeroDomanda: string,
    ) { }
}
/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelVisualizzaVerifiche {
    constructor(
        public numeroDomanda: string,
        public tipo: string,
        public dataInizioValidita: Date,
        public misure: string,
        public fonte: string,
        public note: string,
        public noteRichiesta: string,
        public dataRichiesta: string

        /*public incompatibilita: string,
        public isee: string,
		public statoDomanda: string,
        public dataVerificaIsee: Date,
        public ateco: string,
        public dataVerificaAteco: Date,
        public controlloMisura :boolean,
        public controlloIsee : boolean,
        public controlloAteco : boolean,
        public controlloInc : boolean,*/
    ) {}
}

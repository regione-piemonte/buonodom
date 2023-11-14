/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelVerifiche } from "./ModelVerifiche";

export class ModelDomandeGraduatoria {
    constructor(
        public posizioneGraduatoria: number,
        public numeroDomanda: string,
        public destinatarioNome: string,
        public destinatarioCognome: string,
        public destinatarioCF: string,
        public punteggioSociale: number,
        public isee: number,
        public dataInvioDomanda: string,
        public statoDomanda: string,
        public importoTotale: number,
        public areaInterna: string,
        public verifiche: ModelVerifiche
    ) { }
    checked: boolean;
}
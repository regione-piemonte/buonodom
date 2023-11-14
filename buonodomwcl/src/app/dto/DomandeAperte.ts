/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ListaBuonodom } from "./ListaBuonodom";
import { ModelEnteGestore } from "./ModelEnteGestore";
import { ModelVerifiche } from "./ModelVerifiche";
import { ProfiloBuonodom } from "./ProfiloBuonodom";
import { RuoloBuonodom } from "./RuoloBuonodom";
import { StatiBuonodom } from "./StatiBuonodom";


export class DomandeAperte {
    constructor(
        public idDomanda: number,
        public numeroDomanda: string,
        public cfDestinatario: string,
        public nomeDestinatario: string,
        public cognomeDestinatario: string,
		public cfRichiedente: string,
        public nomeRichiedente: string,
        public cognomeRichiedente: string,
        public dataDomanda: string,
        public stato: StatiBuonodom,
        public enteGestore: ModelEnteGestore,
        public verifiche: ModelVerifiche,
        public dataVerificaBuono: string,
        public esitoVerificaBuono: boolean,
        public sportelloId: number,
        public sportelloCod: string,
        public sportelloDesc: string,
        public decorrenzaInizio: string,
        public decorrenzaFine: string,
    ){}    
    checked: boolean;
}

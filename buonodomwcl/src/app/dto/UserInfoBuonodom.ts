/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ModelEnteGestore } from "./ModelEnteGestore";
import { ProfiloBuonodom } from "./ProfiloBuonodom";
import { RuoloBuonodom } from "./RuoloBuonodom";


export class UserInfoBuonodom {
    constructor(
        public codFisc: string,
        public cognome: string,
        public nome: string,
		public listaRuoli: RuoloBuonodom[],
    public listaEntiGestore: ModelEnteGestore[]
    ){}
}

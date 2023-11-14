/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { AzioneBuonodom } from "./AzioneBuonodom";
import { ListaBuonodom } from "./ListaBuonodom";
import { ProfiloBuonodom } from "./ProfiloBuonodom";

export class RuoloBuonodom {
    constructor(
        public codRuolo: string,
        public descRuolo: string,
		public listaProfili: ProfiloBuonodom[],
    ) {}
}

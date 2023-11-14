/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { AzioneBuonodom } from "./AzioneBuonodom";
import { ListaBuonodom } from "./ListaBuonodom";

export class ProfiloBuonodom {
    constructor(
        public codProfilo: string,
        public descProfilo: string,
		public listaAzioni: AzioneBuonodom[]
    ) {}
}

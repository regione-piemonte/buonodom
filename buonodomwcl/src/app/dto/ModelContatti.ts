/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ModelContatti {
    constructor(
        public user_id: string,
        public sms: string,
 		public email: string,
	    public phone: string
    ) {}
}

/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export class BuonodomError {
    constructor(
        public errorDesc: string,
        public message: string,
        public name: string,
        public status: string,
        public statusText: string,
        public url: string,
        public date: Date,
    ) { }

    static toBuonodomError(json: any, errorDesc = "Si Ã¨ verificato un errore nell'applicativo.") {
	if (json.error.detail!=null){
		errorDesc = json.error.detail;
	}
        return new BuonodomError(
            json.error.detail[0].valore || errorDesc,
            json.message || '',
            json.name || '',
            json.status || '',
            json.statusText,
            json.url,
            new Date()
        );
    }
}

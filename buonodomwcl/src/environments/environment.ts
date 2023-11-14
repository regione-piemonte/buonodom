/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
    production: false,
    endpoint: "http://localhost:8080/buonodombo/api/v1",
    esci: "https://tst-buonodombo.sceltasociale.it/tst-buonodombo_443sliv2wrup/Shibboleth.sso/Logout",
    profilo: "https://tst-secure.sistemapiemonte.it/salute/lamiasalute/profilo.php?servizio=profilo",
    logoutscaduta:"http://www.sistemapiemonte.it/liv2/Shibboleth.sso/Logout?return=https://servizi.regione.piemonte.it/catalogo?cerca=&target_id%5B16%5D=16&tema_id%5B4%5D=4&sort_by=field_contatore_visualizzazioni_value"   
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.

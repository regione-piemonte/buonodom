/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Component } from '@angular/core';
import { BuonodomBOClient } from './BuonodomBOClient';
import { PROJECT_CONSTANTS } from '../constants/buonodom-constants';
@Component( {
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
} )
export class AppComponent {

    loading: String = '';
    loadingText: String = '';

    constructor(public client: BuonodomBOClient) {}

    ngOnInit() {        

        this.loading = PROJECT_CONSTANTS.LOADING;
        this.loadingText = PROJECT_CONSTANTS.LOADING_TEXT;


        this.client.spinEmitter.subscribe((value: boolean) => {
            if (value) {
                this.avviaSpinner();
            }
            else {
                this.stopSpinner();
            }
        });

    }

    avviaSpinner() {
        window.scrollTo( { top: 0, left: 0, behavior: 'smooth' } );
        $( '#overlay' ).fadeIn();
    }
    
    stopSpinner() {
        $( '#overlay' ).fadeOut();
    }
    
}

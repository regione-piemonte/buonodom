/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BuonodomBOClient } from '@buonodom-app/app/BuonodomBOClient';
import { UserInfoBuonodom } from '@buonodom-app/app/dto/UserInfoBuonodom';
import { PROJECT_CONSTANTS } from '@buonodom-app/constants/buonodom-constants';
import { environment } from '@buonodom-app/environments/environment';
import { BuonodomErrorService } from '../error/buonodom-error.service';

@Component({
  selector: 'app-backoffice-toolbar',
  templateUrl: './backoffice-toolbar.component.html',
  styleUrls: ['./backoffice-toolbar.component.css']
})
export class BackofficeToolbarComponent implements OnInit {

  titlePage: String = PROJECT_CONSTANTS.BACKOFFICE_TITLE;
  userLogged: UserInfoBuonodom;

   constructor(public client: BuonodomBOClient, private router: Router, private BuonodomError: BuonodomErrorService, private route: ActivatedRoute) { }

  ngOnInit() {
    /* this.client.login().subscribe(
      (response: UserInfoBuonodom ) => {
          this.userLogged = response;
          if (response.ruolo == ROLES.OPERATORE_REGIONALE || response.ruolo == ROLES.SUPERUSER){
            this.client.ruolo = response.ruolo;
            this.router.navigate( ['operatore-regionale'], { skipLocationChange: true } );
          }
          else  if (response.ruolo == ROLES.RESPONSABILE_ENTE){
            this.client.ruolo = response.ruolo;
            //prendo l'ente dalla map
            let map = new Map(Object.entries(this.userLogged.enteprofilo));
            for (let entry of map.entries()) {
                  this.router.navigate( ['responsabile-ente'], { skipLocationChange: true, state: { idEnte: entry[0]} } );
          }
          }
          else  if (response.ruolo == ROLES.RESPONSABILE_MULTIENTE){
            this.client.ruolo = response.ruolo;
            this.client.listaenti = this.userLogged.enteprofilo;
            this.router.navigate( ['responsabile-multiente'], { skipLocationChange: true, state: { idEnte: this.client.listaenti} } );
          }
          else{
            this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...Error, errorDesc : 'Errore nel ruolo' }))
          }
      },
      err => {}
    )*/
     this.client.selectprofiloazione().subscribe(
      (response: UserInfoBuonodom ) => {
          this.userLogged = response;
		  this.client.verificaprofilo(response);
      },
      err => {}
    )
  }

  goToPaginaPersonale() {
    window.open(environment.profilo, "_blank");
  }

  goLogout() {
	 this.client.logout().subscribe(
      (response: UserInfoBuonodom ) => {
	  window.open(environment.esci, "_self");
	 },
      err => {}
  )
  }


}

/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Component, OnInit } from '@angular/core';
import { BuonodomBOClient } from '@buonodom-app/app/BuonodomBOClient';
import { ParametroBuonodom } from '@buonodom-app/app/dto/ParametroBuonodom';
import { MENU } from '@buonodom-app/constants/buonodom-constants';

import { environment } from '@buonodom-app/environments/environment';


@Component({
  selector: 'app-project-toolbar',
  templateUrl: './project-toolbar.component.html',
  styleUrls: ['./project-toolbar.component.css']
})
export class ProjectToolbarComponent implements OnInit {


  projectTitle: String = 'Buono Domiciliarita\'';
  parametriperhelp:	ParametroBuonodom[];
  constructor(private client: BuonodomBOClient) { }

  ngOnInit() {
    //  this.client.getParametroPerInformativa(MENU.HELP).subscribe((msg: ParametroBuonodom[]) => {
    //    this.parametriperhelp = msg;
   //   });
  }

  goToParametro(url:string){
    window.open(url, "_blank");
  }

}

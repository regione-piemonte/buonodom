/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { ListaBuonodom } from '@buonodom-app/app/dto/ListaBuonodom';
import { ProfiloBuonodom } from '@buonodom-app/app/dto/ProfiloBuonodom';
import { UserInfoBuonodom } from '@buonodom-app/app/dto/UserInfoBuonodom';
import { BuonodomBOClient } from '@buonodom-app/app/BuonodomBOClient';
import { RuoloBuonodom } from '@buonodom-app/app/dto/RuoloBuonodom';

@Component({
  selector: 'app-selezione-profilo-applicativo',
  templateUrl: './selezione-profilo-applicativo.component.html',
  styleUrls: ['./selezione-profilo-applicativo.component.css']
})
export class SelezioneProfiloApplicativoComponent implements OnInit {

 navigation: Navigation;
 utente : UserInfoBuonodom;
 ruoloSelezionato: RuoloBuonodom;
 profiloSelezionato: ProfiloBuonodom;
 utenteSelezionato : UserInfoBuonodom;
 profilosel: ProfiloBuonodom[]=[];
 ruolosel: RuoloBuonodom[]=[];

 abilitaconferma: boolean = false;

  constructor(private router: Router, private route: ActivatedRoute,
    public client: BuonodomBOClient) {
   this.navigation = this.router.getCurrentNavigation();
   this.utente = this.navigation.extras? this.navigation.extras.state.utente : null;
 }

  ngOnInit() {
	this.client.spinEmitter.emit(true);
	this.ruoloSelezionato = new RuoloBuonodom("","",[]);
	if (this.utente.listaRuoli.length==1)
		this.ruoloSelezionato = this.utente.listaRuoli[0];
	this.client.spinEmitter.emit(false);
  }

 onClick() {
	 this.profilosel.push(this.profiloSelezionato);
     this.ruoloSelezionato.listaProfili = this.profilosel;
	 this.ruolosel.push(this.ruoloSelezionato);
	 this.utenteSelezionato = new UserInfoBuonodom(this.client.utenteloggato.codFisc,this.client.utenteloggato.cognome,this.client.utenteloggato.nome,this.ruolosel,this.client.utenteloggato.listaEntiGestore);
	 this.client.verificaprofilo(this.utenteSelezionato);
	}

	onSelectionRuoloChanged() {
		this.profiloSelezionato = new ProfiloBuonodom('','',null);
		this.abilitaconferma = false;
		if (this.ruoloSelezionato.listaProfili.length==1){
		this.profiloSelezionato = this.ruoloSelezionato.listaProfili[0];
		this.client.profilo = this.profiloSelezionato;
		this.abilitaconferma = true;
		}
	  }

	  onSelectionProfiloChanged() {
		this.abilitaconferma = true;
		this.client.profilo = this.profiloSelezionato;
	}

}

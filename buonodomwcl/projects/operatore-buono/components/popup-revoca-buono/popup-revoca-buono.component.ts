import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { BuonodomBOClient } from '@buonodom-app/app/BuonodomBOClient';
import { BuonodomErrorService } from '@buonodom-app/shared/error/buonodom-error.service';
import { CambioStatoPopUp } from '@buonodom-app/app/dto/CambioStatoPopUp';
import { BuonodomError } from '@buonodom-app/shared/error/buonodom-error.model';
import { ModelRichiesta } from '@buonodom-app/app/dto/ModelRichiesta';
import { ModelDatiDaModificare } from '@buonodom-app/app/dto/ModelDatiDaModificare';
import { STATO_DOMANDA } from '@buonodom-app/constants/buonodom-constants';

const enum PathApi { }


@Component({
  selector: 'app-popup-cambistato',
  templateUrl: './popup-revoca-buono.component.html',
  styleUrls: ['./popup-revoca-buono.component.css'],

})
export class PopuppopupRevocaBuonoComponent implements OnInit {


  dataSistema = new Date(new Date().getFullYear(), 0, 1);
  dataChiusura: Date;
  chiusuraForm: FormGroup;

  @Input() public notaPerRevoca: string;
  

  constructor(public client: BuonodomBOClient, public activeModal: NgbActiveModal,
    private fb: FormBuilder, private buonodomError: BuonodomErrorService, private router: Router, private route: ActivatedRoute) { }

  errorMessage = {
    error: { detail: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date
  }

  ngOnInit() {
    this.chiusuraForm = this.fb.group({
      notaInterna: [''],
      notaEnte: ['']
    });
  }


  conferma() {
    
  }

  



  setValueDataCreazione() {
    this.dataChiusura = this.chiusuraForm.controls.dataChiusura.value;
  }

}

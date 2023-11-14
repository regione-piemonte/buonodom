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
  selector: 'app-popup-ente',
  templateUrl: './popup-ente.component.html',
  styleUrls: ['./popup-ente.component.css'],

})
export class PopupEnteComponent implements OnInit {


  dataSistema = new Date(new Date().getFullYear(), 0, 1);
  dataChiusura: Date;
  chiusuraForm: FormGroup;
  nascondinotacittadino: boolean=false;

  @Input() public numerodomanda: string;
  @Input() public descRichiesta: string;

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
      notaEnte: ['', Validators.required]
    });
}


  conferma() {
    this.client.spinEmitter.emit(true);
    if (this.chiusuraForm.valid) {
      let chiuso: CambioStatoPopUp = new CambioStatoPopUp();
      chiuso.numerodomanda = this.numerodomanda;
      chiuso.notaEnte = this.chiusuraForm.controls.notaEnte.value;
      this.rettificaente(chiuso);
    } else {
      this.errorMessage.error.detail = "Nota per l\'ente gestore obbligatoria";
      this.buonodomError.handleWarning(BuonodomError.toBuonodomError({ ...this.errorMessage, errorDesc: this.errorMessage.error.detail }))

      this.client.spinEmitter.emit(false);
    }
  }

  rettificaente(chiuso: CambioStatoPopUp) {
    this.client.rettificaente(chiuso).subscribe(
      () => {
        this.activeModal.close(true);
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

}

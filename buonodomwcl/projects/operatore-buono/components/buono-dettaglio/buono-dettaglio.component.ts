import { animate, state, style, transition, trigger } from '@angular/animations';
import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { BuonodomBOClient } from '@buonodom-app/app/BuonodomBOClient';
import { ModelContrattoAllegati } from '@buonodom-app/app/dto/ModelContrattoAllegati';
import { ModelDecorrenzaBuono } from '@buonodom-app/app/dto/ModelDecorrenzaBuono';
import { ModelDichiarazioneSpesa } from '@buonodom-app/app/dto/ModelDichiarazioneSpesa';
import { ModelDocumentoSpesa } from '@buonodom-app/app/dto/ModelDocumentoSpesa';
import { AZIONE } from '@buonodom-app/constants/buonodom-constants';
import { BuonodomErrorService } from '@buonodom-app/shared/error/buonodom-error.service';
import { AppToastService } from '@buonodom-shared/toast/app-toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { forkJoin } from 'rxjs';


@Component({
  selector: 'app-buono-dettaglio',
  templateUrl: './buono-dettaglio.component.html',
  styleUrls: ['./buono-dettaglio.component.css'],
  providers: [DatePipe],
  animations: [
    trigger('rotatedState', [
      state('rotated', style({ transform: 'rotate(0)' })),
      state('default', style({ transform: 'rotate(-180deg)' })),
      transition('rotated => default', animate('100ms ease-out')),
      transition('default => rotated', animate('100ms ease-in'))
    ])
  ]
})

export class BuonoDettaglioComponent implements OnInit {
  errorMessage = {
    error: { descrizione: '' },
    message: '',
    name: '',
    status: '',
    statusText: '',
    url: '',
    date: Date
  }

  navigation: Navigation;
  numeroDomanda: string;
  paginaRitorno: string;

  // Documenti spesa
  dichiarazioniSpesa: ModelDichiarazioneSpesa[];
  documentiSpesaFiltrati: ModelDocumentoSpesa[];

  // Contratti
  contrattiAllegatiBuono: ModelContrattoAllegati[];
  contrattiAllegatiFiltrati: ModelContrattoAllegati[];

  // Filtro periodo
  periodoRendicontazioneForm: FormGroup;
  controlloDataFine: boolean = true;

  // Cambia Decorrenza
  decorrenzaBuono: ModelDecorrenzaBuono = new ModelDecorrenzaBuono(new Date(), new Date());
  cambiaDecorrenzaForm: FormGroup;

  constructor(private router: Router, private route: ActivatedRoute, private fb: FormBuilder, private dialog: MatDialog,
    public client: BuonodomBOClient, private modalService: NgbModal, public toastService: AppToastService, private buonodomError: BuonodomErrorService, public datePipe: DatePipe) {
    this.navigation = this.router.getCurrentNavigation();
    let domandaValues: string[] = [];
    this.route.fragment.subscribe((frag: string) => {
      domandaValues.push(frag);
    });

    this.numeroDomanda = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.numeroDomanda : domandaValues[0][0];
    this.paginaRitorno = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.paginaRitorno : domandaValues[0][0];
  }

  ngOnInit() {
    this.client.spinEmitter.emit(true);

    //Creazione form
    this.periodoRendicontazioneForm = this.fb.group({
      meseInizio: new FormControl(this.getCurrentYearMonth(), [Validators.required]),
      meseFine: new FormControl(this.getCurrentYearMonth(), [Validators.required]),
    });

    this.cambiaDecorrenzaForm = this.fb.group({
      decorrenzaInizio: new FormControl(this.getCurrentYearMonth(), [Validators.required]),
      decorrenzaFine: new FormControl({ value: this.getCurrentYearMonth(), disabled: true }, [Validators.required]),
    });

    //Servizi get
    forkJoin({
      allegati: this.client.getAllegatiBuono(this.numeroDomanda),
      contratti: this.client.getContrattiBuono(this.numeroDomanda),
      decorrenza: this.client.getDecorrenzaBuono(this.numeroDomanda),
    })
      .subscribe(({ allegati, contratti, decorrenza }) => {
        //Allegati
        if (allegati) {
          this.dichiarazioniSpesa = allegati

          this.documentiSpesaFiltrati = this.dichiarazioniSpesa.reduce(
            (acc: ModelDocumentoSpesa[], dichiarazioneSpesa: ModelDichiarazioneSpesa) => acc.concat(dichiarazioneSpesa.documentiSpesa),
            []
          );
        }

        //Contratti
        if (contratti) {
          this.contrattiAllegatiBuono = contratti
        }

        //Decorrenza
        if (decorrenza) {

          this.decorrenzaBuono = decorrenza
          this.cambiaDecorrenzaForm = this.fb.group({

            decorrenzaInizio: new FormControl(this.datePipe.transform(this.decorrenzaBuono.decorrenzaInizio, 'yyyy-MM'), [Validators.required]),
            decorrenzaFine: new FormControl({ value: this.datePipe.transform(this.decorrenzaBuono.decorrenzaFine, 'yyyy-MM'), disabled: true }, [Validators.required])

          });
        }

        //Filtro di default
        this.filtraPerPeriodo()
      });

    this.client.spinEmitter.emit(false);
  }

  //--------------FORM GETTERS------------
  get meseInizio() {
    return this.periodoRendicontazioneForm.get('meseInizio');
  }

  get meseFine() {
    return this.periodoRendicontazioneForm.get('meseFine');
  }

  get decorrenzaInizio() {
    return this.cambiaDecorrenzaForm.get('decorrenzaInizio');
  }

  get decorrenzaFine() {
    return this.cambiaDecorrenzaForm.get('decorrenzaFine');
  }

  //--------------METODI------------
  //Controllo azione utente
  checkAzione(azione: string): boolean {
    switch (azione) {
      case "richiesta_rettifica_ente":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_RichiediRettificaEnte);
      case "revoca_buono":
        return this.client.azioni.some(a => a.codAzione == AZIONE.OP_RevocaBuono);
      default:
        return false;
    }
  }

  //Fa il routing alla pagina precedente
  backButton() {
    switch (this.paginaRitorno) {
      case 'istruttoria':
        this.router.navigate(["/operatore-buono/istruttoria"], { relativeTo: this.route, skipLocationChange: true });
        break;
      case 'archivio-domande':
        this.router.navigate(["/operatore-buono/archivio-domande"], { relativeTo: this.route, skipLocationChange: true });
        break;
      case 'istanze-aperte':
        this.router.navigate(["/operatore-buono/istanze-aperte"], { relativeTo: this.route, skipLocationChange: true });
        break;
      case 'rendicontazione-op':
        this.router.navigate(["/operatore-buono/rendicontazione-op"], { relativeTo: this.route, skipLocationChange: true });
        break;
      case 'rendicontazione-ente':
        this.router.navigate(["/operatore-buono/rendicontazione-ente"], { relativeTo: this.route, skipLocationChange: true });
        break;
      default:
        this.router.navigate(['selezione-profilo-applicativo'], { skipLocationChange: true });
        break;
    }
  }

  //Prende mese e anno corrente
  getCurrentYearMonth(): string {
    const currentDate = new Date();
    return this.datePipe.transform(currentDate, 'yyyy-MM') || '';
  }

  //Esegue le validazioni delle date inserite
  validazioneDate() {
    if (this.meseInizio.value) {
      if (this.meseFine.value) {
        if (this.meseFine.value < this.meseInizio.value) {
          this.controlloDataFine = true;
        } else {
          this.controlloDataFine = false;
        }
      }
    }
  }

  // Filtra le liste in base al periodo selezionato
  filtraPerPeriodo() {
    //Parsing del periodo di filtro
    let meseFine = new Date(this.meseFine.value);
    meseFine.setDate(1);
    meseFine.setMonth(meseFine.getMonth() + 1);
    meseFine.setDate(meseFine.getDate() - 1);
    meseFine.setHours(23, 59, 59, 999);

    let meseInizio = new Date(this.meseInizio.value);
    meseInizio.setDate(1);
    meseInizio.setHours(0, 0, 0, 0);

    // Valorizzazione liste filtrate
    this.documentiSpesaFiltrati = this.dichiarazioniSpesa.reduce(
      (acc: ModelDocumentoSpesa[], dichiarazioneSpesa: ModelDichiarazioneSpesa) => acc.concat(dichiarazioneSpesa.documentiSpesa),
      []
    );
    this.contrattiAllegatiFiltrati = this.contrattiAllegatiBuono

    //Filtro delle liste
    this.documentiSpesaFiltrati = this.documentiSpesaFiltrati.filter(documentoSpesa => (
      (new Date(documentoSpesa.periodoInizio) <= meseFine) && (documentoSpesa.periodoFine === null || (new Date(documentoSpesa.periodoFine) >= meseInizio))));

    this.contrattiAllegatiFiltrati = this.contrattiAllegatiFiltrati.filter(contrattoAllegato => (
      (new Date(contrattoAllegato.contratto.data_inizio) <= meseFine) && (contrattoAllegato.contratto.data_fine === null || (new Date(contrattoAllegato.contratto.data_fine) >= meseInizio))));
  }

  salvaDecorrenza() {
    this.client.spinEmitter.emit(true);
    this.calcolaDifferenzaMesi()
    const dataInizio = new Date(this.decorrenzaInizio.value)
    const dataFine = new Date(this.decorrenzaFine.value)
    const nuovaDecorrenza = new ModelDecorrenzaBuono(dataInizio, dataFine)
    this.client.updateDecorrenzaBuono(this.numeroDomanda, nuovaDecorrenza).subscribe((decorrenza: ModelDecorrenzaBuono) => {
      if (decorrenza) {
        this.decorrenzaBuono = decorrenza
        this.cambiaDecorrenzaForm = this.fb.group({
          decorrenzaInizio: new FormControl(this.datePipe.transform(this.decorrenzaBuono.decorrenzaInizio, 'yyyy-MM'), [Validators.required]),
          decorrenzaFine: new FormControl({ value: this.datePipe.transform(this.decorrenzaBuono.decorrenzaFine, 'yyyy-MM'), disabled: true }, [Validators.required])
        });
      }
      this.client.spinEmitter.emit(false);
    },
      err => {
        this.client.spinEmitter.emit(false);
      });
  }

  calcolaDifferenzaMesi() {
    const data1Date = new Date(new Date(this.decorrenzaBuono.decorrenzaInizio));
    const data2Date = new Date(this.decorrenzaInizio.value);
    const data3Date = new Date(new Date(this.decorrenzaBuono.decorrenzaFine));

    const anniData1 = data1Date.getFullYear();
    const mesiData1 = data1Date.getMonth();
    const anniData2 = data2Date.getFullYear();
    const mesiData2 = data2Date.getMonth();
    const anniData3 = data3Date.getFullYear();
    const mesiData3 = data3Date.getMonth();

    const differenzaMesi = (anniData2 - anniData1) * 12 + (mesiData2 - mesiData1);

    const risultatoDate = new Date(anniData3, mesiData3 + differenzaMesi);

    this.decorrenzaFine.setValue(this.datePipe.transform(risultatoDate, 'yyyy-MM'));
  }

  //Richiama il servizio per scaricare un allegato
  scaricaAllegato(idAllegato: Number) {
    return this.client.scaricaAllegatoBuono(idAllegato)
  }

  //Rimuove tutti i '_' da una stringa
  replace_(string: String) {
    return string.replace(/_/g, " ")
  }

  getPeriodoRendicontazione(idRend: number) : String{
    for (let i = 0; i < this.dichiarazioniSpesa.length; i++) {
      let documenti = this.dichiarazioniSpesa[i].documentiSpesa;
      for (let j = 0; j < documenti.length; j++) {
        if(documenti[j].idDocSpesa === idRend) {
          return this.dichiarazioniSpesa[i].dicSpesaPeriodoDesc;
        }
      }
    }
    return '';
  }
}



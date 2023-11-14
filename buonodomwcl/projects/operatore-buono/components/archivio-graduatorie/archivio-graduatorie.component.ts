import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatPaginator, MatSort, MatTableDataSource, Sort } from '@angular/material';
import { BuonodomBOClient } from '@buonodom-app/app/BuonodomBOClient';
import { ModelDescrizioneGraduatoria } from '@buonodom-app/app/dto/ModelDescrizioneGraduatoria';
import { ModelDomandeGraduatoria } from '@buonodom-app/app/dto/ModelDomandeGraduatoria';
import { ModelParametriFinanziamento } from '@buonodom-app/app/dto/ModelParametriFinanziamento';
import { ModelVerifiche } from '@buonodom-app/app/dto/ModelVerifiche';
import { Sportello } from '@buonodom-app/app/dto/Sportello';
import { AppToastService } from '@buonodom-shared/toast/app-toast.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ExcelService } from '@buonodom-app/services/excel-service';

@Component({
  selector: 'app-archivio-graduatorie',
  templateUrl: './archivio-graduatorie.component.html',
  styleUrls: ['./archivio-graduatorie.component.css'],
})
export class ArchivioGraduatorieComponent implements OnInit, OnDestroy {

  // MATPAGINATOR
  displayedColumns: string[] = ['posizioneGraduatoria', 'numeroDomanda', 'destinatario', 'punteggioSociale', 'isee', 'dataInvioDomanda', 'statoDomanda', 'statoVerificaEnte', 'areaInterna', 'importoTotale'];
  listaDomandeGraduatoria: MatTableDataSource<ModelDomandeGraduatoria>;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  // listaDomandeGraduatoria: ModelDomandeGraduatoria[] = [];
  selezionaTutto: boolean;
  sortedData: ModelDomandeGraduatoria[];
  //Tabella Criteri
  displayedColumnsCriteri: string[] = ['AREE', 'DOMANDE IN GRADUATORIA', 'DOMANDE FINANZIABILI', 'IMPORTO RESIDUO/AREE', 'IMPORTO RESIDUO AREA NON INTERNA'];
  // Check Stati Graduatoria
  checkStatoGraduatoriaProvvisoria: boolean = null;
  checkStatoGraduatoriaAggiornamento: boolean = null;
  checkStatoGraduatoriaPubblicazione: boolean = null;

  importoTotaleRicevuto: number = 0;

  graduatoriaForm: FormGroup;

  constructor(private fb: FormBuilder, private modalService: NgbModal, public client: BuonodomBOClient, public appToastService: AppToastService, private dialog: MatDialog, private excelService: ExcelService) { }

  ngOnDestroy(): void {
    this.client.paginaSalvataGraduatoriaArchivio = this.paginator.pageIndex;
    this.client.ordinamentoSalvatoGraduatoriaArchivio = this.sort.active;
    this.client.versoSalvatoGraduatoriaArchivio = this.sort.direction;
    this.client.righePerPaginaGraduatoriaArchivio = this.paginator.pageSize;
  }

  ngOnInit(): void {
    this.client.spinEmitter.emit(true);

    this.graduatoriaForm = this.fb.group({
      codSportello: new FormControl("", Validators.required),
    });

    if (this.client.sportelloGraduatoriaArchivio) {
      this.creazioneForm();
      this.ricercaGraduatoria(this.client.sportelloGraduatoriaArchivio.codSportello);
    } else {
      this.client.getSportelliGraduatorie().subscribe(
        (response) => {
          if (response) {
            this.client.listaSportelliGraduatoriaArchivio = response as Sportello[];
            this.client.sportelloGraduatoriaArchivio = response[0] as Sportello;

            this.creazioneForm();
            this.ricercaGraduatoria(this.client.sportelloGraduatoriaArchivio.codSportello);
          }
        }
      );
      err => {
        this.client.spinEmitter.emit(false);
      }
    }

  }

  get codSportello() {
    return this.graduatoriaForm.get('codSportello');
  }



  creazioneForm() {
    this.graduatoriaForm = this.fb.group({
      codSportello: new FormControl(this.client.sportelloGraduatoriaArchivio.codSportello, Validators.required),
    });
    this.codSportello.valueChanges.subscribe((codSportello: String) => {
      if (codSportello) {
        const sportello = this.client.listaSportelliGraduatoriaArchivio
          .find(sportello => sportello.codSportello === codSportello);
        this.client.sportelloGraduatoriaArchivio = sportello;
        this.ricercaGraduatoria(this.client.sportelloGraduatoriaArchivio.codSportello);
      }
    });
  }

  ricercaGraduatoria(codSportello: string) {
    this.client.spinEmitter.emit(true);
    this.getDomandeGraduatoria(codSportello);
    this.getParametriFinanziamento(codSportello);
    this.getDescrizioneGraduatoria(codSportello);
    this.ricreaPaginator();
    this.client.spinEmitter.emit(false);
  }

  // -----------------------------------------------------------------OPERAZIONI_GRADUATORIA
  getDomandeGraduatoria(codSportello: string) {
    this.client.getDomandeGraduatoria(codSportello).subscribe(
      (response) => {
        this.client.ricercaDomGraduatoriaArchivio = response as ModelDomandeGraduatoria[];
        this.listaDomandeGraduatoria.data = this.client.ricercaDomGraduatoriaArchivio.slice() as ModelDomandeGraduatoria[];
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  azzeraDatiFinanziamento() {
    // Azzero i dati della graduatoria
    this.client.parametriFinanziabiliArchivio = null;
    this.client.soggettiFinanziabiliArchivio = null;
    this.client.soggettiFinanziatiArchivio = null;
    this.client.importoResiduoTotaleArchivio = null;
    this.client.importoTotaleMisuraArchivio = null;
    this.client.mesiPerDestinatarioArchivio = null;
    this.client.buonoMensileArchivio = null;
    this.client.totaleImportoDestinatarioArchivio = null;
  }

  getParametriFinanziamento(codSportello: string) {
    this.importoTotaleRicevuto = 0;
    this.azzeraDatiFinanziamento();
    this.client.getParametriFinanziamento(codSportello).subscribe(
      (response) => {
        this.client.parametriFinanziabiliArchivio = response as ModelParametriFinanziamento[];
        this.client.parametriFinanziabiliArchivio.forEach((element) => {
          this.client.soggettiFinanziabiliArchivio += element.soggettiFinanziabili;
          this.client.soggettiFinanziatiArchivio += element.soggettiFinanziati;
          this.client.importoResiduoTotaleArchivio += element.importoResiduo;
          this.client.importoTotaleMisuraArchivio += element.importoTotale;
          if (element.aree != null) {
            this.importoTotaleRicevuto += element.importoDistribuibile;
          }
        });
        this.client.mesiPerDestinatarioArchivio = this.client.parametriFinanziabiliArchivio[0].mesi;
        this.client.buonoMensileArchivio = this.client.parametriFinanziabiliArchivio[0].importoMensile;
        this.client.totaleImportoDestinatarioArchivio = this.client.mesiPerDestinatarioArchivio * this.client.buonoMensileArchivio;

      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  getDescrizioneGraduatoria(codSportello: string) {
    this.client.getDescrizioneGraduatoria(codSportello).subscribe(
      (response) => {
        this.client.graduatoriaDescrizioneArchivio = response as ModelDescrizioneGraduatoria;
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  checkPubblicazioneGraduatoria(codSportello: string) {
    this.client.getCheckPubblicazioneGraduatoria(codSportello).subscribe(
      (response) => {
        this.client.checkPubblicazioneGraduatoria = response as boolean;
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  checkStatoGraduatoria(codSportello: string, stato: string) {
    this.client.controlloStatoGraduatoria(codSportello, stato).subscribe(
      (response) => {
        this.client.checkStatoGraduatoria = response as boolean;
        switch (stato) {
          case 'PROVVISORIA':
            this.checkStatoGraduatoriaProvvisoria = this.client.checkStatoGraduatoria;
            break;
          case 'DA_AGGIORNARE':
            this.checkStatoGraduatoriaAggiornamento = this.client.checkStatoGraduatoria;
            break;
          case 'PUBBLICATA':
            this.checkStatoGraduatoriaPubblicazione = this.client.checkStatoGraduatoria;
            break;
          default:
            break;
        }
        this.client.checkStatoGraduatoria = response as boolean;
        this.client.spinEmitter.emit(false);
      },
      err => {
        this.client.spinEmitter.emit(false);
      }
    );
  }

  // -----------------------------------------------------------------UTILS

  ricreaPaginator() {
    this.listaDomandeGraduatoria = new MatTableDataSource<ModelDomandeGraduatoria>();
    this.listaDomandeGraduatoria.data = this.client.ricercaDomGraduatoriaArchivio.slice() as ModelDomandeGraduatoria[];
    this.sort.active = this.client.ordinamentoSalvatoGraduatoriaArchivio;
    this.sort.direction = this.client.versoSalvatoGraduatoriaArchivio;
    this.listaDomandeGraduatoria.sortingDataAccessor = (obj, property) => this.getProperty(obj, property);
    this.listaDomandeGraduatoria.sort = this.sort;
    this.paginator.pageSize = this.client.righePerPaginaGraduatoriaArchivio;
    this.paginator.pageIndex = this.client.paginaSalvataGraduatoriaArchivio;
    this.listaDomandeGraduatoria.paginator = this.paginator;
  }

  convertiNumeroInItaliano(numero: number): string {
    const numeroItaliano = Number(numero).toLocaleString('it-IT');
    return numeroItaliano;
  }

  /*
  * Ottengo il nome di ogni proprietà di un oggetto
  */
  getProperty = (obj: ModelDomandeGraduatoria, path: string) => (
    path.split('.').reduce((o: { [x: string]: any; }, p: string | number) => o && o[p], obj)
  )


  sortData(sort: Sort) {
    const data = this.listaDomandeGraduatoria.data.slice();
    if (!sort.active || sort.direction === '') {
      this.sortedData = data;
      return;
    }

    this.sortedData = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'posizioneGraduatoria':
          return this.compare(a.posizioneGraduatoria, b.posizioneGraduatoria, isAsc);
        case 'numeroDomanda':
          return this.compare(a.numeroDomanda, b.numeroDomanda, isAsc);
        case 'destinatario':
          return this.compare(a.destinatarioCF, b.destinatarioCF, isAsc);
        case 'punteggioSociale':
          return this.compare(a.punteggioSociale, b.punteggioSociale, isAsc);
        case 'isee':
          return this.compare(a.isee, b.isee, isAsc);
        case 'areaInterna':
          return this.compare(a.areaInterna, b.areaInterna, isAsc);
        case 'statoDomanda':
          return this.compare(a.statoDomanda, b.statoDomanda, isAsc);
        case 'importoTotale':
          let ordinamento = 0;
          if (a.importoTotale > b.importoTotale) {
            ordinamento = 1 * (isAsc ? 1 : -1);
          } else if (a.importoTotale < b.importoTotale) {
            ordinamento = -1 * (isAsc ? 1 : -1);
          } else {
            if (a.posizioneGraduatoria < b.posizioneGraduatoria) {
              ordinamento = -1;
            } else {
              ordinamento = 1;
            }
          }
          return ordinamento;
        // return this.compare(a.importoTotale, b.importoTotale, isAsc);
        case 'dataInvioDomanda':
          // Fix nel caso una delle due date sua nulla
          let dataStringA = a.dataInvioDomanda;
          let dataStringB = b.dataInvioDomanda;
          if (a.dataInvioDomanda === null) {
            if (isAsc !== true) {
              dataStringA = '01/01/1970';
            } else if (isAsc) {
              dataStringA = '20/12/2099';
            }
          }
          if (b.dataInvioDomanda === null) {
            if (isAsc !== true) {
              dataStringB = '01/01/1970';
            } else if (isAsc) {
              dataStringB = '20/12/2099';
            }
          }
          let dateA = new Date(dataStringA.split("/").reverse().join("-"));
          let dateB = new Date(dataStringB.split("/").reverse().join("-"));

          return this.compare(dateA.getTime(), dateB.getTime(), isAsc);
        case 'statoVerificaEnte':
          const verificaA = this.compareVerifiche(a.verifiche);
          const verificaB = this.compareVerifiche(b.verifiche);
          return this.compare(verificaA, verificaB, isAsc);
        default:
          return 0;
      }
    });
    this.listaDomandeGraduatoria.data = this.sortedData;
    this.client.ricercaDomGraduatoria = this.sortedData;
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  compareVerifiche(verifica: ModelVerifiche): string {
    if (verifica.verifica_eg_richiesta !== null) {
      return this.client.listaStatiVericaEnteGestore[0].replace(/\s+/g, "");
    } else if (verifica.verifica_eg_in_corso !== null) {
      return this.client.listaStatiVericaEnteGestore[1].replace(/\s+/g, "");
    } else if (verifica.verifica_eg_conclusa !== null) {
      return this.client.listaStatiVericaEnteGestore[2].replace(/\s+/g, "");
    } else {
      return "null";
    }
  }

  max(valore1: number, valore2: number): number {
    return Math.max(valore1, valore2);
  }


  //INIZIO PULSANTE SCARICA GRADUATORIA COME EXCEL
  scaricaGraduatoria() {
    let nomeFile = '';
    let tabellaGraduatoria = [];

    //STATO PUBBLICATE
    nomeFile = 'Scarico-Pubblicate';

    nomeFile = nomeFile + '_' + this.client.graduatoriaDescrizioneArchivio.codiceGraduatoria.trim();

    var intestazione = new Array();
    intestazione.push('Posizione graduatoria');
    intestazione.push('Numero Domanda');
    intestazione.push('Destinatario');
    intestazione.push('Punteggio sociale');
    intestazione.push('Isee');
    intestazione.push('Data invio Domanda');
    intestazione.push('Stato Domanda');
    intestazione.push('Stato verifica ente');
    intestazione.push('Area interna');
    intestazione.push('Importo totale');
    tabellaGraduatoria.push(intestazione);

    this.listaDomandeGraduatoria.data.forEach(element => {

      var datiDinamici = new Array();
      datiDinamici.push(element.posizioneGraduatoria);
      datiDinamici.push(element.numeroDomanda);
      datiDinamici.push(element.destinatarioNome + " " + element.destinatarioCognome + " " + element.destinatarioCF);
      datiDinamici.push(element.punteggioSociale);
      datiDinamici.push(element.isee);
      datiDinamici.push(element.dataInvioDomanda);
      datiDinamici.push(element.statoDomanda);
      if (element.verifiche != null) {
        if (element.verifiche.verifica_eg_richiesta == true) {
          datiDinamici.push('Richiesta verifica');
        }
        else if (element.verifiche.verifica_eg_in_corso == true) {
          datiDinamici.push('Verifica in corso');
        }
        else if (element.verifiche.verifica_eg_conclusa == true) {
          datiDinamici.push('Verifica effettuata');
        }
        else {
          datiDinamici.push('');
        }
      }
      if (element.areaInterna != null) {
        datiDinamici.push(element.areaInterna);
      }
      else {
        datiDinamici.push('');
      }
      datiDinamici.push(element.importoTotale);

      tabellaGraduatoria.push(datiDinamici);

    })

    this.exportAsXLSX(tabellaGraduatoria, nomeFile);
  }

  exportAsXLSX(tabellaGraduatoria, nomeFile): void {
    this.excelService.exportAsExcelFile(tabellaGraduatoria, nomeFile);
  }
  //FINE PULSANTE SCARICA GRADUATORIA COME EXCEL

}

/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { EventEmitter, Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { ConfigService } from "@buonodom-app/app/config.service";
import { UserInfoBuonodom } from "@buonodom-app/app/dto/UserInfoBuonodom";
import { AZIONE } from "@buonodom-app/constants/buonodom-constants";
import { BuonodomError } from "@buonodom-app/shared/error/buonodom-error.model";
import { BuonodomErrorService } from "@buonodom-app/shared/error/buonodom-error.service";
import { encodeAsHttpParams } from "@buonodom-app/shared/util";
import { CookieService } from "ngx-cookie-service";
import { Observable, of, throwError } from "rxjs";
import { map } from "rxjs/internal/operators/map";
import { catchError } from 'rxjs/operators';
import * as uuid from 'uuid';
import { AzioneBuonodom } from "./dto/AzioneBuonodom";
import { CambioStatoPopUp } from "./dto/CambioStatoPopUp";
import { DomandeAperte } from "./dto/DomandeAperte";
import { Feedback } from "./dto/Feedback";
import { FiltriDomandeAperte } from "./dto/FiltriDomandeAperte";
import { FiltriRicercaBuoni } from "./dto/FiltriRicercaBuoni";
import { ListaBuonodom } from "./dto/ListaBuonodom";
import { ModelArea } from "./dto/ModelArea";
import { ModelAteco } from "./dto/ModelAteco";
import { ModelCriteriGraduatoria } from "./dto/ModelCriteriGraduatoria";
import { ModelDatiDaModificare } from "./dto/ModelDatiDaModificare";
import { ModelDescrizioneGraduatoria } from "./dto/ModelDescrizioneGraduatoria";
import { ModelDichiarazioneSpesa } from "./dto/ModelDichiarazioneSpesa";
import { ModelDomandeGraduatoria } from "./dto/ModelDomandeGraduatoria";
import { ModelEnteGestore } from "./dto/ModelEnteGestore";
import { ModelIsee } from "./dto/ModelIsee";
import { ModelMessaggio } from "./dto/ModelMessaggio";
import { ModelNuovaGraduatoria } from "./dto/ModelNuovaGraduatoria";
import { ModelParametriFinanziamento } from "./dto/ModelParametriFinanziamento";
import { ModelRicercaBuono } from "./dto/ModelRicercaBuono";
import { ModelRichiesta } from "./dto/ModelRichiesta";
import { ModelStatiBuono } from "./dto/ModelStatiBuono";
import { ModelVerifiche } from "./dto/ModelVerifiche";
import { ModelVisualizzaCronologia } from "./dto/ModelVisualizzaCronologia";
import { ModelVisualizzaVerifiche } from "./dto/ModelVisualizzaVerifiche";
import { ParametroBuonodom } from "./dto/ParametroBuonodom";
import { PresaInCaricoModel } from "./dto/PresaInCaricoModel";
import { ProfiloBuonodom } from "./dto/ProfiloBuonodom";
import { RuoloBuonodom } from "./dto/RuoloBuonodom";
import { Sportelli } from "./dto/Sportelli";
import { Sportello } from "./dto/Sportello";
import { StatiBuonodom } from "./dto/StatiBuonodom";
import { ModelVerificheEnte } from "./dto/ModelVerificheEnte";
import { ModelContrattoAllegati } from "./dto/ModelContrattoAllegati";
import { ModelDecorrenzaBuono } from "./dto/ModelDecorrenzaBuono";



const enum PathApi {
	
  logout = '/loginController/logout',
  //Ricerca
  getStati = '/ricerca/stati',
  getSportelli = '/ricerca/sportelli',
  ricerca = '/ricerca',
  getEntiGestori = '/entigestori',
  //Presa in carico
  presaInCarico = '/richieste/presa_carico',
  selectprofilo = '/loginController/login',
  //richiesta
  getRichiestaNumero = '/richieste',
  //allegati
  getAllegatoDownload = '/allegato',
  //verifica ateco
  getAteco = '/integrazione',
  //rettifica
  getallegatiRettifica = '/richieste/listaallegati',
  getcampiRettifica = '/richieste/listacampi',
  datiDaModificarePost = '/richieste/richiedi_rettifica',
  //AMMETTI, RESPINGI, AMMESSA_CON_RISERVA, AMMESSA, IN_PAGAMENTO, DINIEGO
  ammissibile = '/richieste/ammissibile',
  nonammissibile = '/richieste/nonammissibile',
  richiedirettificaente = '/entigestori/richiedirettifica',
  // salvaverificaente = '/entigestori/salvaverificaente',
  salvaverificaente = '/entigestori/salvaverificaentenew',

  // concludiverificaente = '/entigestori/concludiverificaente',
  concludiverificaente = '/entigestori/salvaconcludiverificaente',
  getverificeente = '/entigestori/getverificheentegestore',
  preavvisopernonammissibilita = '/richieste/preavvisopernonammissibilita',
  verificacontatto = '/richieste/verificacontatto',


  salvadomandaisee = '/richieste/salvadomandaisee',
  salvadomandanota = '/richieste/salvadomandanota',
  ammessaConRiserva = '/richieste/ammessaconriserva',
  ammessaConRiservaInPagamento = '/richieste/ammessaconriservainpagamento',
  ammessa = '/richieste/ammessa',
  inPagamento = '/richieste/inpagamento',
  diniego = '/richieste/diniego',

  //da RICHIESTA VERIFICA a VERIFICA IN CORSO
  updateToVerificaInCorso = '/entigestori/updateToVerificaInCorso',

  // Messaggi
  getMsgApplicativo = '/liste/getMessaggioApplicativo',
  getMsgInformativi = '/liste/getMessaggiInformativi',
  getParametroPerInformativa = '/liste/getParametroPerInformativa',
  getMsgInformativiPerCod = '/liste/getMessaggiInformativipercodice',
  getParametro = '/liste/getParametro',

  // Ricerca Enti
  getListaStatoRendicontazione = '/entiGestoriAttivi/getListaStatoRendicontazione',
  getListaComuni = '/entiGestoriAttivi/getListaComuni',
  getListaTipoEnte = '/entiGestoriAttivi/getListaTipoEnte',
  getListaDenominazioni = '/entiGestoriAttivi/getListaDenominazioni',
  getListaDenominazioniWithComuniAssociati = '/entiGestoriAttivi/getListaDenominazioniWithComuniAssociati',
  searchSchedeEntiGestori = '/entiGestoriAttivi/searchSchedeEntiGestori',
  searchSchedeMultiEntiGestori = '/entiGestoriAttivi/searchSchedeMultiEntiGestori',
  getSchedaEnteGestore = '/entiGestoriAttivi/getSchedaEnteGestore',
  getCronologia = '/entiGestoriAttivi/getCronologia',
  getStorico = '/entiGestoriAttivi/getStorico',
  getStatiEnte = '/entiGestoriAttivi/getStatiEnte',
  getAnniEsercizio = '/entiGestoriAttivi/getAnniEsercizio',
  getCronologiaStato = '/datiEnte/getCronologiaStato',
  getLastStato = '/datiEnte/getLastStato',

  // Archivio Enti
  getListaStatoRendicontazioneArchivio = '/archivioDatiRendicontazione/getListaStatoRendicontazione',
  getListaComuniArchivio = '/archivioDatiRendicontazione/getListaComuni',
  getListaTipoEnteArchivio = '/archivioDatiRendicontazione/getListaTipoEnte',
  searchSchedeEntiGestoriArchivio = '/archivioDatiRendicontazione/searchArchivioRendicontazione',
  getCronologiaArchivio = '/richieste/cronologia',
  getVerifiche = '/richieste/verifiche',
  searchSchedeEntiGestoriRendicontazioneConclusa = '/archivioDatiRendicontazione/searchSchedeEntiGestoriRendicontazioneConclusa',
  getStatoRendicontazioneConclusa = '/archivioDatiRendicontazione/getStatoRendicontazioneConclusa',
  getAnniEsercizioArchivio = '/archivioDatiRendicontazione/getAnniEsercizio',
  // Dati Ente
  getListaProvince = '/datiEnte/getProvinciaSedeLegale',
  getSchedaEnte = '/datiEnte/getSchedaEnte',
  getSchedaAnagrafica = '/datiEnte/getSchedaAnagrafica',
  getSchedaAnagraficaStorico = '/datiEnte/getSchedaAnagraficaStorico',
  getPrestazioni = '/datiEnte/getPrestazioni',
  getPrestazioniResSemires = '/datiEnte/getPrestazioniResSemires',
  getComuniAssociati = '/datiEnte/getComuniAssociati',
  getComuniAnagraficaAssociati = '/datiEnte/getComuniAnagraficaAssociati',
  getComuniAnagraficaAssociatiStorico = '/datiEnte/getComuniAnagraficaAssociatiStorico',
  getPrestazioniAssociate = '/datiEnte/getPrestazioniAssociate',
  saveDatiEnte = '/datiEnte/saveDatiEnte',
  saveDatiAnagrafici = '/datiEnte/saveDatiAnagrafici',
  creaNuovoEnte = '/datiEnte/creaNuovoEnte',
  getAsl = '/datiEnte/getAsl',
  getDocumentiAssociati = '/datiEnte/getAllegatiAssociati',
  getAllegatoToDownload = '/datiEnte/getAllegatoToDownload',
  getListaPrestazioniValorizzateModA = '/datiEnte/getListaPrestazioniValorizzateModA',
  getListaPrestazioniValorizzateModB1 = '/datiEnte/getListaPrestazioniValorizzateModB1',
  getListaPrestazioniValorizzateModC = '/datiEnte/getListaPrestazioniValorizzateModC',
  getListaComuniValorizzatiModA1 = '/datiEnte/getListaComuniValorizzatiModA1',
  getListaComuniValorizzatiModA2 = '/datiEnte/getListaComuniValorizzatiModA2',
  getListaComuniValorizzatiModE = '/datiEnte/getListaComuniValorizzatiModE',
  getDatiEnteRendicontazione = '/datiEnte/getDatiEnteRendicontazione',
  getMotivazioniChiusura = '/datiEnte/getMotivazioniChiusura',
  closeEnte = '/datiEnte/closeEnte',
  ripristinoEnte = '/datiEnte/ripristinoEnte',
  getMotivazioniRipristino = '/datiEnte/getMotivazioniRipristino',
  getSchedaAnagraficaUnione = '/datiEnte/getEntiunione',
  unioneEnte = '/datiEnte/unioneEnte',
  getProvinciaComuneLibero = "/datiEnte/getProvinciaComuneLibero",
  getPrestazioneRegionale = "/datiEnte/getPrestazioneRegionale",

  //GRADUATORIA
  getultimosportellochiuso = '/graduatoria/getultimosportellochiuso',
  creaNuovaGraduatoria = '/graduatoria/creanuovagraduatoria',
  getCriteriGraduatoria = '/graduatoria/getgraduatoriaordinamento',
  getDomandeGraduatoria = '/graduatoria/getdomandegraduatoria',
  getParametriFinanziamento = '/graduatoria/getparametrifinanziamento',
  getDescrizioneGraduatoria = '/graduatoria/getgraduatoriadesc',
  pubblicazioneGraduatoria = '/graduatoria/pubblicagraduatoria',
  aggiornamentoGraduatoria = '/graduatoria/aggiornagraduatoria',
  simulazioneGraduatoria = '/graduatoria/simulagraduatoria',
  checkEsistenzaGraduatoria = '/graduatoria/checkesistenzagraduatoria',
  checkStatoGraduatoria = '/graduatoria/checkestatograduatoria',
  checkPubblicaGraduatoria = '/graduatoria/checkpubblicagraduatoria',
  getAree = '/graduatoria/getaree',

  //NUOVO SPORTELLO
  creaNuovoSportello = '/operatoreregionale/creanuovosportello',


  // Rendicontazione
  getInfoRendicontazioneOperatore = '/datiRendicontazione/getInfoRendicontazioneOperatore',
  confermaDati1 = '/datiRendicontazione/confermaDati1',
  richiestaRettifica1 = '/datiRendicontazione/richiestaRettifica1',
  confermaDati2 = '/datiRendicontazione/confermaDati2',
  richiestaRettifica2 = '/datiRendicontazione/richiestaRettifica2',
  inviatranche = '/datiRendicontazione/inviaTranche',
  getmodellipertranche = '/datiRendicontazione/getModelliTranche',
  gettranchepermodello = '/datiRendicontazione/getTranchePerModello',
  valida = '/datiRendicontazione/valida',
  storicizza = '/datiRendicontazione/storicizza',
  getmodelliassociati = "/datiRendicontazione/getModelliAssociati",
  getmodelli = "/datiRendicontazione/getModelli",
  getAllObbligo = "/datiRendicontazione/getAllObbligo",
  getVerificaModelliVuoto = "/datiRendicontazione/getVerificaModelliVuoto",


  //Archivio Graduatorie
  getSportelliGraduatoria = '/graduatoria/sportelli',

  // Modello A1
  getVociModelloA1 = '/modelloA1/getVociModelloA1',
  getDatiModelloA1 = '/modelloA1/getDatiModelloA1',
  saveModelloA1 = '/modelloA1/saveModelloA1',
  esportaModelloA1 = '/modelloA1/esportaModelloA1',

  // Modello A2
  getCausali = '/modelloA2/getCausali',
  getCausaliStatiche = '/modelloA2/getCausaliStatiche',
  getTrasferimentiA2 = '/modelloA2/getTrasferimentiA2',
  saveModelloA2 = '/modelloA2/saveModelloA2',
  esportaModelloA2 = '/modelloA2/esportaModelloA2',

  // Modello D
  getTipoVoceD = '/modelloD/getTipoVoceD',
  getVoceModD = '/modelloD/getVociModD',
  getRendicontazioneModD = '/modelloD/getRendicontazioneModDByIdScheda',
  saveModelloD = '/modelloD/saveModelloD',
  esportaModelloD = '/modelloD/esportaModelloD',

  // MacroagBuonodomati
  getMacroagBuonodomatiTotali = '/macroagBuonodomati/getRendicontazioneTotaliMacroagBuonodomati',
  getMacroagBuonodomati = '/macroagBuonodomati/getMacroagBuonodomati',
  getSpesaMissione = '/macroagBuonodomati/getSpesaMissione',
  getRendicontazioneMacroagBuonodomati = '/macroagBuonodomati/getRendicontazioneMacroagBuonodomatiByIdScheda',
  isMacroagBuonodomatiCompiled = '/macroagBuonodomati/isRendicontazioneTotaliMacroagBuonodomatiCompiled',
  saveMacroagBuonodomati = '/macroagBuonodomati/saveMacroagBuonodomati',
  esportaMacroagBuonodomati = '/macroagBuonodomati/esportaMacroagBuonodomati',

  // Modello A
  getListaVociModA = '/modelloA/getListaVociModA',
  saveModelloA = '/modelloA/saveModelloA',
  esportaModelloA = '/modelloA/esportaModelloA',


  //Modello B1
  getPrestazioniModB1 = '/modellob1/getPrestazioni',
  getElencoLblModB1 = '/modellob1/getElencoLbl',
  saveModelloB1 = '/modellob1/saveModello',
  esportaModelloB1 = '/modellob1/esportaModelloB1',

  // ModelloB
  getMissioniModB = '/modelloB/getMissioniModB',
  getRendicontazioneModB = '/modelloB/getRendicontazioneModB',
  getRendicontazioneTotaliSpese = '/macroagBuonodomati/getRendicontazioneTotaliSpese',
  getRendicontazioneTotaliMacroagBuonodomati = '/macroagBuonodomati/getRendicontazioneTotaliMacroagBuonodomati',
  getProgrammiMissioneTotaliModB = '/modellob1/getProgrammiMissioneTotaliModB',
  saveModelloB = '/modelloB/saveModelloB',
  esportaModelloB = '/modelloB/esportaModelloB',
  esportaIstat = '/modelloB/esportaIstat',
  canActiveModB = '/modelloB/canActiveModB',

  // ModelloE
  getAttivitaSocioAssist = '/modelloE/getAttivitaSocioAssist',
  getRendicontazioneModE = '/modelloE/getRendicontazioneModE',
  saveModelloE = '/modelloE/saveModelloE',
  esportaModelloE = '/modelloE/esportaModelloE',

  // ModelloC
  getPrestazioniC = '/modelloC/getPrestazioniModC',
  getRendicontazioneModC = '/modelloC/getRendicontazioneModC',
  saveModelloC = '/modelloC/saveModelloC',
  esportaModelloC = '/modelloC/esportaModelloC',

  // ModelloF
  getConteggiF = '/modelloF/getConteggiModF',
  getRendicontazioneModF = '/modelloF/getRendicontazioneModF',
  saveModelloF = '/modelloF/saveModelloF',
  esportaModelloF = '/modelloF/esportaModelloF',

  // Modello All D
  getVociAllD = '/modelloAllD/getVociAllD',
  getRendicontazioneModAllD = '/modelloAllD/getRendicontazioneModAllD',
  compilabileModelloAllD = '/modelloAllD/compilabileModelloAllD',
  saveModelloAllD = '/modelloAllD/saveModelloAllD',
  canActiveModFnps = '/modelloAllD/canActiveModFnps',
  esportaModuloFnps = '/modelloAllD/esportaModuloFnps',

  //check
  saveMotivazioneCheck = '/datiRendicontazione/saveMotivazioneCheck',
  checkModelloA = '/modelloA/checkModelloA',
  checkModelloA1 = '/modelloA1/checkModelloA1',
  checkMacroagBuonodomati = '/macroagBuonodomati/checkMacroagBuonodomati',
  checkModelloB1 = '/modellob1/checkModelloB1',
  checkModelloB = '/modelloB/checkModelloB',
  checkModelloC = '/modelloC/checkModelloC',
  checkModelloF = '/modelloF/checkModelloF',


  //cruscotto
  searchSchedeEntiGestoriCruscotto = '/cruscotto/searchSchedeEntiGestori',
  getModelliCruscotto = "/cruscotto/getModelli",
  getInfoModello = '/cruscotto/getInfoModello',
  getMaxAnnoRendicontazione = '/cruscotto/getMaxAnnoRendicontazione',

  //Configuratore
  getPrestazioniReg1 = '/configuratore/getPrestazioni',
  getPrestazioneRegionale1 = '/configuratore/getPrestazioneRegionale1',
  getTipologie = '/configuratore/getTipologie',
  getStrutture = '/configuratore/getStrutture',
  getQuote = '/configuratore/getQuote',
  getPrestColl = '/configuratore/getPrestColl',
  getMacroagBuonodomatiConf = '/configuratore/getMacroagBuonodomati',
  getUtenzeConf = '/configuratore/getUtenze',
  getMissioneProgConf = '/configuratore/getMissioneProg',
  getSpeseConf = '/configuratore/getSpese',
  getPrestazioni2Conf = '/configuratore/getPrestazioni2',
  getPrestazioniMinConf = '/configuratore/getPrestazioniMin',
  savePrestazione = '/configuratore/savePrestazione',
  getIstatConf = '/configuratore/getIstatConf',
  getUtenzeIstatConf = '/configuratore/getUtenzeIstatConf',
  getNomenclatoreConf = '/configuratore/getNomenclatoreConf',
  savePrestazione2 = '/configuratore/savePrestazione2',
  modificaPrestazione2 = '/configuratore/modificaPrestazione2',
  modificaPrestazione = '/configuratore/modificaPrestazione',
  getPrestazioneRegionale2 = '/configuratore/getPrestazioneRegionale2',
  getUtenzeIstatTransConf = '/configuratore/getUtenzeIstatTransConf',

  //CreaAnno
  creaNuovoAnno = '/entiGestoriAttivi/creaNuovoAnno',
  //Concludi
  concludiRendicontazione = '/entiGestoriAttivi/concludiRendicontazione',
  ripristinaRendicontazione = '/entiGestoriAttivi/ripristinaRendicontazione',

  //BUONO
  getStatiBuono = '/buono/statibuono',
  ricercaBuoni = '/buono/ricerca',

  //Buono Dettaglio
  getAllegatiBuono = '/buonodettaglio/getallegati',
  scaricaAllegatoBuono = '/buono/allegato',
  getContrattiBuono = '/buonodettaglio/getcontratti',
  getDecorrenzaBuono = '/buonodettaglio/getdecorrenza',
  updateDecorrenzaBuono = '/buonodettaglio/updatedecorrenza',
}

@Injectable()
export class BuonodomBOClient {

  public myUUIDV4 = uuid.v4();
  public messaggioFeedback: string = null;
  public idFeedback: number = null;
  public feedback: Feedback = null;
  baseUrl = ConfigService.getBEServer();
  public spinEmitter: EventEmitter<boolean> = new EventEmitter();
  public buttonViewEmitter: EventEmitter<boolean> = new EventEmitter();
  public utente: Observable<UserInfoBuonodom>;
  public listaenti: any[] = [];
  public listaEntiArc: any[] = [];
  public notSavedEvent = new CustomEvent('notSavedEvent', { bubbles: true });
  public changeTabEmitter = new CustomEvent('changeTabEmitter', { bubbles: true });
  public updateCronoEmitter = new CustomEvent('updateCronoEmitter', { bubbles: true });
  public utenteloggato: UserInfoBuonodom;
  public azioni: AzioneBuonodom[] = [];
  public profilo: ProfiloBuonodom;
  public ruolo: RuoloBuonodom;
  public listaSelezionata: ListaBuonodom;
  public listaStatiSalvato: StatiBuonodom[] = [];
  public listaSportelliSalvato: Sportelli;
  public ricercaDomAperte: DomandeAperte[] = [];
  public paginaSalvata: number = 0;
  public ordinamentoSalvato: string = null;
  public versoSalvato = null;
  public showFeedback: boolean;
  public filtroDomandeAperte: FiltriDomandeAperte;
  public righePerPagina: number = 10;

  //------Varibili per enti gestori
  public filtroDomandeAperteEnte: string;
  public listaEntiGestori: ModelEnteGestore[] = [];
  public listaDenominazioneEntiGestori: string[] = [];
  public listaIdEntiGestori: number[] = [];
  public listaStatiVericaEnteGestore: string[] = ['Si', 'No'];
  public listaEsitiVericaEnteGestore: string[] = ['Si', 'No'];
  public filteredEntiRegionali: Observable<string[]>;
  //-----------------------


  //------ArchivioComponent
  public listaStatiSalvatoArchivio: StatiBuonodom[] = [];
  public listaSportelliSalvatoArchivio: Sportelli = new Sportelli([], new Sportello('', '', '', '', '', false)); public filtroDomandeArchivio: FiltriDomandeAperte;
  public ricercaDomArchivio: DomandeAperte[] = [];
  public paginaSalvataArchivio: number = 0;
  public ordinamentoSalvatoArchivio: string = null;
  public versoSalvatoArchivio = null;
  public righePerPaginaArchivio: number = 10;
  //-----------------------

  //------GraduatoriaComponent
  public ricercaDomGraduatoria: ModelDomandeGraduatoria[] = [];
  public paginaSalvataGraduatoria: number = 0;
  public ordinamentoSalvatoGraduatoria: string = null;
  public versoSalvatoGraduatoria = null;
  public righePerPaginaGraduatoria: number = 10;
  public listaCriteriOrdinamento: ModelCriteriGraduatoria[] = [];
  public parametriFinanziabili: ModelParametriFinanziamento[] = [];
  public importoTotaleMisura: number = null;
  public buonoMensile: number = null;
  public mesiPerDestinatario: number = null;
  public importoResiduoTotale: number = null;
  public soggettiFinanziabili: number = null;
  public soggettiFinanziati: number = null;
  public totaleImportoDestinatario: number = null;
  public graduatoriaDescrizione: ModelDescrizioneGraduatoria = new ModelDescrizioneGraduatoria(" ", " ", " ");
  public checkPubblicazioneGraduatoria: boolean = false;
  public checkStatoGraduatoria: boolean = false;
  public checkEsistenzaGraduatoria: boolean = false;
  //-----------------------

  //------BUONO
  public listaStatiBuonoSalvato: ModelStatiBuono[] = [];
  public listaRicercaBuonoSalvato: ModelRicercaBuono[] = [];
  public filtriRicercaBuoni: FiltriRicercaBuoni;
  //------Rendicontazione Operatore Regionale
  public righePerPaginaRendicontazione: number = 10;
  public paginaSalvataRendicontazione: number = 0;
  public ordinamentoSalvatoRendicontazione: string = null;
  public versoSalvatoRendicontazione = null;
  //-----------------------


  //------ArchivioGraduatorieComponent
  public ricercaDomGraduatoriaArchivio: ModelDomandeGraduatoria[] = [];
  public paginaSalvataGraduatoriaArchivio: number = 0;
  public ordinamentoSalvatoGraduatoriaArchivio: string = null;
  public versoSalvatoGraduatoriaArchivio = null;
  public righePerPaginaGraduatoriaArchivio: number = 10;
  public listaCriteriOrdinamentoArchivio: ModelCriteriGraduatoria[] = [];
  public parametriFinanziabiliArchivio: ModelParametriFinanziamento[] = [];
  public importoTotaleMisuraArchivio: number = null;
  public buonoMensileArchivio: number = null;
  public mesiPerDestinatarioArchivio: number = null;
  public importoResiduoTotaleArchivio: number = null;
  public soggettiFinanziabiliArchivio: number = null;
  public soggettiFinanziatiArchivio: number = null;
  public totaleImportoDestinatarioArchivio: number = null;
  public graduatoriaDescrizioneArchivio: ModelDescrizioneGraduatoria = new ModelDescrizioneGraduatoria(" ", " ", " ");
  public checkPubblicazioneGraduatoriaArchivio: boolean = false;
  public checkStatoGraduatoriaArchivio: boolean = false;
  public checkEsistenzaGraduatoriaArchivio: boolean = false;
  public listaSportelliGraduatoriaArchivio: Sportello[] = null;
  public sportelloGraduatoriaArchivio: Sportello = null;
  //--------------------

  /*    public cronologia: CronologiaBuonodom = new CronologiaBuonodom();

     public operazione: string = "";
     public readOnly: boolean;
     public readOnlyII: boolean;
     public readOnlyIII: boolean;
     public mostrabottoniera: boolean;
     public nomemodello:string;
     public componinote:boolean;
     public statorendicontazione:string;

 // sezione azioni booleani
      public SalvaModelliI : boolean;
      public InviaI : boolean;
      public InviaII : boolean;
      public RichiestaRettificaI : boolean;
      public ConfermaDatiI : boolean;
      public RichiestaRettificaII : boolean;
      public ConfermaDatiII : boolean;
      public Valida : boolean;
      public Storicizza : boolean;
      public SalvaModelliII : boolean;
      public SalvaModelliIII : boolean;
      public CheckI: boolean;
      public CheckII: boolean;
      public Concludi: boolean;
      public inviaIIFatto : boolean = false;
      public inviaIFatto : boolean = false;

      public selectedLinkRend: any;
      public ricercaEnte: RicercaBuonodomOutput[]=[];
      public ricercaEnteArchivio: RicercaBuonodomOutput[]=[];
      public filtroEnte: RicercaBuonodom;
      public filtroEnteArchivio: RicercaBuonodom;
      public paginaSalvata: number = 0;
      public ordinamentoSalvato: string = null;
      public versoSalvato = null;
      public statiEnteSalvato: StatoEnte[]=[];
      public statiEnteSalvatoArchivio: StatoEnte[]=[];
      public listaStatiSalvato: StatoRendicontazioneBuonodom[]=[];
      public statoRendicontazioneConslusaArchivio: StatoRendicontazioneBuonodom;
      public listaStatiSalvatoArchivio: StatoRendicontazioneBuonodom[]=[];
      public listaComuniSalvato: ComuneBuonodom[]=[];
      public listaComuniSalvatoArchivio: ComuneBuonodom[]=[];
      public listaTipoEntiSalvato: TipoEnteBuonodom[]=[];
      public listaTipoEntiSalvatoArchivio: TipoEnteBuonodom[]=[];
      public listaDenominazioniEntiSalvato: EnteBuonodom[]=[];
      public listaDenominazioniEntiSalvatoArchivio: EnteBuonodom[]=[];
      public anniEsercizioSalvato: number[]=[];
      public anniEsercizioSalvatoArchivio: number[]=[];
     public tooltipRendicontazione : string = null;
     public listaComuniinitial: ComuneBuonodom[]=[];
     public listaComuniinitialArchivio: ComuneBuonodom[]=[];
     public listaComuniinitialCruscotto: ComuneBuonodom[]=[];
     public dettaglioPrestazione: boolean = false;
     public ricercaEnteCruscotto: RicercaBuonodomOutput[]=[];
      public filtroEnteCruscotto: RicercaBuonodomCruscotto;
      public paginaSalvataCruscotto: number = 0;
      public statiEnteSalvatoCruscotto: StatoEnte[]=[];
      public listaStatiSalvatoCruscotto: StatoRendicontazioneBuonodom[]=[];
      public listaComuniSalvatoCruscotto: ComuneBuonodom[]=[];
      public listaTipoEntiSalvatoCruscotto: TipoEnteBuonodom[]=[];
      public listaDenominazioniEntiSalvatoCruscotto: EnteBuonodom[]=[];
      public anniEsercizioSalvatoCruscotto: number[]=[];
      public modelliSalvatoCruscotto: ModelTabTrancheCruscotto[]=[];
      public statiRendicontazioneCruscotto: StatiCruscotto[]=[];
      public cruscotto: boolean = false;
      public goToCruscotto: boolean = false;
      public goToConfiguratore: boolean = false;
      public goToNuovaPrestazione: boolean = false;
      public nuovaPrestazione: boolean = false;
      public goToArchivio: boolean = false;*/
  constructor(private http: HttpClient, private BuonodomError: BuonodomErrorService, private cookieService: CookieService, private router: Router) { }



  getStati(tipoMenu: string): Observable<StatiBuonodom[]> {
    return this.http.get(this.baseUrl + PathApi.getStati, { ...encodeAsHttpParams({ tipoMenu: tipoMenu }) }).pipe(
      map((response: any) => {
        return response as StatiBuonodom[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    )
  }

  getSportelli(): Observable<Sportelli> {
    return this.http.get(this.baseUrl + PathApi.getSportelli).pipe(
      map((response: any) => {
        return response as Sportelli;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    )
  }

  ricercaDomandeAperte(filtri: FiltriDomandeAperte): Observable<DomandeAperte[]> {
    return this.http.post(this.baseUrl + PathApi.ricerca, filtri).pipe(
      map((response: any) => {
        return response as DomandeAperte[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    )
  }

  getEntiGestori(): Observable<ModelEnteGestore[]> {
    return this.http.get(this.baseUrl + PathApi.getEntiGestori).pipe(
      map((response: any) => {
        return response as ModelEnteGestore[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    )
  }


  presaInCarico(richieste: PresaInCaricoModel[]): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.presaInCarico, richieste).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))

        )
      }
      )
    )
  }


  logout(): Observable<UserInfoBuonodom> {
    return this.http.get(this.baseUrl + PathApi.logout
    ).pipe(
      map((response: any) => {
        this.utente = of(response as UserInfoBuonodom);
        return response as UserInfoBuonodom;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    );
  }

  selectprofiloazione(): Observable<UserInfoBuonodom> {
    return this.http.get(this.baseUrl + PathApi.selectprofilo
      // , {headers: this.getHeader()}
    ).pipe(
      map((response: any) => {
        this.utente = of(response as UserInfoBuonodom);
        return response as UserInfoBuonodom;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    );
  }

  verificaprofilo(response: UserInfoBuonodom) {

    this.utenteloggato = response;
    if (response.listaRuoli.length == 1) { //un solo ruolo
      if (response.listaRuoli[0].listaProfili.length == 1) {//un solo profilo
        this.ruolo = response.listaRuoli[0];
        this.profilo = this.ruolo.listaProfili[0];
        this.azioni = this.profilo.listaAzioni;
        this.redirecttab(this.azioni);
      }
      else {
        //aggiungere il routing alla selezione-profilo-applicativo
        this.router.navigate(['selezione-profilo-applicativo'], { skipLocationChange: true, state: { utente: response } });
      }
    }
    else {
      //aggiungere il routing alla selezione-profilo-applicativo
      this.router.navigate(['selezione-profilo-applicativo'], { skipLocationChange: true, state: { utente: response } });
    }
  }

  /*
  * Redirect a diverse applicazioni in base alle azioni di un utente
  */
  redirecttab(azioni: AzioneBuonodom[]) {

    if (azioni.some(a => a.codAzione == AZIONE.OP_RicercaDomandeAperte)) {
      this.router.navigate(['operatore-buono'], { skipLocationChange: true });
    } else if (azioni.some(azione => azione.codAzione == AZIONE.OP_Istruttoria)) {
      this.router.navigate(['operatore-buono/istruttoria'], { skipLocationChange: true });
    } else if (azioni.some(azione => azione.codAzione == AZIONE.OP_Graduatoria)) {
      this.router.navigate(['operatore-buono/graduatoria'], { skipLocationChange: true });
    } else if (azioni.some(azione => azione.codAzione == AZIONE.OP_Archivio)) {
      this.router.navigate(['operatore-buono/archivio-domande'], { skipLocationChange: true });
    }
    // else if (azioni.get("Archivio")[1])
    // this.router.navigate( ['operatore-regionale/archivio-dati-rendicontazione'], { skipLocationChange: true } );
    // else if (azioni.get("ConfiguratorePrestazioni")[1])
    // this.router.navigate( ['operatore-regionale/configuratore-prestazioni'], { skipLocationChange: true} );
    // else if (azioni.get("Cruscotto")[1])
    // this.router.navigate( ['operatore-regionale/cruscotto'], { skipLocationChange: true } );
    else
      this.router.navigate(['/redirect-page'], { skipLocationChange: true });
  }

  getRichiestaNumero(numerorichiesta: string): Observable<ModelRichiesta> {

    return this.http.get(this.baseUrl + PathApi.getRichiestaNumero + '/' + numerorichiesta
    ).pipe(
      map((response: any) => {
        return response as ModelRichiesta;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    );
  }


  getAllegato(numerorichiesta: string, tipo: string): string {
    return this.baseUrl + PathApi.getAllegatoDownload + '/' + numerorichiesta + '/' + tipo;
  }

  // concludiverificaente(verifica: ModelVerifiche, numerorichiesta: string): Observable<boolean> {
  //     return this.http.post(this.baseUrl + PathApi.concludiverificaente + '/' + numerorichiesta, verifica).pipe(
  //         map((response: any) => {
  //             return response as boolean;
  //         }),
  //         catchError((error: HttpErrorResponse) => {
  //             if (!error.error || error.error == null || error.error.code == null) {
  //                 this.spinEmitter.emit(false);
  //                 this.router.navigate(['/redirect-page'], { skipLocationChange: true });
  //                 return;
  //             }
  //             return throwError(
  //                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
  //             )
  //         }
  //         )
  //     );
  // }

  concludiverificaente(verifica: ModelVerificheEnte, numerorichiesta: string): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.concludiverificaente + '/' + numerorichiesta, verifica).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }


  getAteco(numerorichiesta: string, piva: string): Observable<ModelAteco> {

    return this.http.get(this.baseUrl + PathApi.getAteco + '/' + piva + '/' + numerorichiesta
    ).pipe(
      map((response: any) => {
        return response as ModelAteco;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    );
  }

  getAllegatiRettifica(numerorichiesta: string): Observable<ModelDatiDaModificare[]> {
    return this.http.get(this.baseUrl + PathApi.getallegatiRettifica + '/' + numerorichiesta).pipe(
      map((response: any) => {
        return response as ModelDatiDaModificare[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    )
  }
  getCampiRettifica(numerorichiesta: string): Observable<ModelDatiDaModificare[]> {
    return this.http.get(this.baseUrl + PathApi.getcampiRettifica + '/' + numerorichiesta).pipe(
      map((response: any) => {
        return response as ModelDatiDaModificare[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    )
  }

  datiDaModificarePost(ricerca: CambioStatoPopUp): Observable<ModelRichiesta> {

    return this.http.post(this.baseUrl + PathApi.datiDaModificarePost, ricerca
    ).pipe(
      map((response: any) => {
        return response as ModelRichiesta;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    );
  }


  ammissibile(ricerca: CambioStatoPopUp): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.ammissibile, ricerca).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  nonammissibile(ricerca: CambioStatoPopUp): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.nonammissibile, ricerca).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  rettificaente(ricerca: CambioStatoPopUp): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.richiedirettificaente, ricerca).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  salvaverificaente(verifica: ModelVerificheEnte, numerorichiesta: string): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.salvaverificaente + '/' + numerorichiesta, verifica).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  preavvisopernonammissibilita(ricerca: CambioStatoPopUp): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.preavvisopernonammissibilita, ricerca).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  verificacontatto(numerodomanda: string): Observable<ModelMessaggio> {
    return this.http.get(this.baseUrl + PathApi.verificacontatto + '/' + numerodomanda).pipe(
      map((response: any) => {
        return response as ModelMessaggio;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  // REST API, cambio stato domanda: AMMISSIBILE --> AMMESSA
  ammessa(cambioStato: CambioStatoPopUp): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.ammessa, cambioStato).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  // REST API, cambio stato domanda: AMMISSIBILE --> AMMESSA CON RISERVA
  ammessaConRiserva(cambioStato: CambioStatoPopUp): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.ammessaConRiserva, cambioStato).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  // REST API, cambio stato domanda: AMMESSA CON RISERVA --> AMMESSA CON RISERVA IN PAGAMENTO
  ammessaConRiservaInPagamento(cambioStato: CambioStatoPopUp): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.ammessaConRiservaInPagamento, cambioStato).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  salvaDomandaIsee(numerodomanda: string, isee: ModelIsee): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.salvadomandaisee + '/' + numerodomanda, isee).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    )
  }

  salvaDomandaNota(nota: CambioStatoPopUp): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.salvadomandanota, nota).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    )
  }


  // REST API, cambio stato domanda: PERFEZIONATA --> IN_PAGAMENTO oppure AMMESSA --> IN_PAGAMENTO
  inPagamento(cambioStato: CambioStatoPopUp): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.inPagamento, cambioStato).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.id == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  // REST API, cambio stato domanda: PERFEZIONATA --> DINIEGO oppure AMMESSA --> DINIEGO
  diniego(cambioStato: CambioStatoPopUp): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.diniego, cambioStato).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.id == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }


  /*
  respingi(ricerca: CambioStatoPopUp): Observable<boolean> {
      return this.http.post(this.baseUrl + PathApi.respingi,ricerca).pipe(
          map((response: any) => {
              return response as boolean;
          }),
          catchError((error: HttpErrorResponse) => {
              if (!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate(['/redirect-page'], { skipLocationChange: true });
                  return;
              }
              return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
              )
          }
          )
      );
  }
  */


  getCronologiaArchivio(numeroDomanda: string): Observable<ModelVisualizzaCronologia[]> {

    return this.http.get(this.baseUrl + PathApi.getCronologiaArchivio + '/' + numeroDomanda
    ).pipe(
      map((response: any) => {
        return response as ModelVisualizzaCronologia[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.id == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  getverifiche(numeroDomanda: string): Observable<ModelVisualizzaVerifiche[]> {

    return this.http.get(this.baseUrl + PathApi.getVerifiche + '/' + numeroDomanda
    ).pipe(
      map((response: any) => {
        return response as ModelVisualizzaVerifiche[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.id == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }


  updateToVerificaInCorso(numeroDomande: string[]): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.updateToVerificaInCorso, numeroDomande).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    )
  }

  getUltimoSportelloChiuso(): Observable<Sportello> {
    return this.http.get(this.baseUrl + PathApi.getultimosportellochiuso).pipe(
      map((response: any) => {
        return response as Sportello;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.id == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      })
    );
  }

  getAree(): Observable<ModelArea[]> {
    return this.http.get(this.baseUrl + PathApi.getAree).pipe(
      map((response: any) => {
        return response as ModelArea[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.id == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      })
    );
  }

  getCriteriGraduatoria(): Observable<ModelCriteriGraduatoria[]> {
    return this.http.get(this.baseUrl + PathApi.getCriteriGraduatoria).pipe(
      map((response: any) => {
        return response as ModelCriteriGraduatoria[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.id == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  creaNuovoSportello(newSportello: Sportello): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.creaNuovoSportello, newSportello).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.id == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  getDomandeGraduatoria(sportelloCod: string): Observable<ModelDomandeGraduatoria[]> {
    return this.http.get(this.baseUrl + PathApi.getDomandeGraduatoria + '/' + sportelloCod).pipe(
      map((response: any) => {
        return response as ModelDomandeGraduatoria[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  getParametriFinanziamento(sportelloCod: string): Observable<ModelParametriFinanziamento[]> {
    return this.http.get(this.baseUrl + PathApi.getParametriFinanziamento + '/' + sportelloCod).pipe(
      map((response: any) => {
        return response as ModelParametriFinanziamento[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  getDescrizioneGraduatoria(sportelloCod: string): Observable<ModelDescrizioneGraduatoria> {
    return this.http.get(this.baseUrl + PathApi.getDescrizioneGraduatoria + '/' + sportelloCod).pipe(
      map((response: any) => {
        return response as ModelDescrizioneGraduatoria;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  getCheckPubblicazioneGraduatoria(sportelloCod: string): Observable<boolean> {
    return this.http.get(this.baseUrl + PathApi.checkPubblicaGraduatoria + '/' + sportelloCod).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  creaNuovaGraduatoria(nuovaGraduatoria: ModelNuovaGraduatoria): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.creaNuovaGraduatoria, nuovaGraduatoria).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.id == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  pubblicazioneGraduatoria(sportelloCod: string): Observable<boolean> {
    return this.http.get(this.baseUrl + PathApi.pubblicazioneGraduatoria + '/' + sportelloCod).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  aggiornamentoGraduatoria(sportelloCod: string): Observable<boolean> {
    return this.http.get(this.baseUrl + PathApi.aggiornamentoGraduatoria + '/' + sportelloCod).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  simulazioneGraduatoria(nuovaGraduatoria: ModelNuovaGraduatoria): Observable<boolean> {
    return this.http.post(this.baseUrl + PathApi.simulazioneGraduatoria, nuovaGraduatoria).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.id == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }

  controlloEsistenzaGraduatoria(sportelloCod: string): Observable<boolean> {
    return this.http.get(this.baseUrl + PathApi.checkEsistenzaGraduatoria + '/' + sportelloCod).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  controlloStatoGraduatoria(sportelloCod: string, stato: string): Observable<boolean> {
    return this.http.get(this.baseUrl + PathApi.checkStatoGraduatoria + '/' + sportelloCod + '/' + stato).pipe(
      map((response: any) => {
        return response as boolean;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  /*
   * API BUONO
  */
  getStatiBuono(): Observable<ModelStatiBuono[]> {
    return this.http.get(this.baseUrl + PathApi.getStatiBuono).pipe(
      map((response: any) => {
        return response as ModelStatiBuono[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  ricercaBuoni(filtri: FiltriRicercaBuoni): Observable<ModelRicercaBuono[]> {
    return this.http.post(this.baseUrl + PathApi.ricercaBuoni, filtri).pipe(
      map((response: any) => {
        return response as ModelRicercaBuono[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    )
  }


  getAllegatiBuono(buonoCod: String): Observable<ModelDichiarazioneSpesa[]> {
    return this.http.get(this.baseUrl + PathApi.getAllegatiBuono + '/' + buonoCod).pipe(
      map((response: any) => {
        return response as ModelDichiarazioneSpesa[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  scaricaAllegatoBuono(idAllegato: Number): string {
    return this.baseUrl + PathApi.scaricaAllegatoBuono + '/' + idAllegato;
  }

  getVerificheEnte(numeroDomanda: String): Observable<ModelVerificheEnte> {
    return this.http.get(this.baseUrl + PathApi.getverificeente + '/' + numeroDomanda).pipe(
      map((response: any) => {
        return response as ModelVerificheEnte;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }


  getContrattiBuono(buonoCod: String): Observable<ModelContrattoAllegati[]> {
    return this.http.get(this.baseUrl + PathApi.getContrattiBuono + '/' + buonoCod).pipe(
      map((response: any) => {
        return response as ModelContrattoAllegati[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  getSportelliGraduatorie(): Observable<Sportello[]> {
    return this.http.get(this.baseUrl + PathApi.getSportelliGraduatoria).pipe(
      map((response: any) => {
        return response as Sportello[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.detail[0].valore }))
        )
      }
      )
    )
  }


  getDecorrenzaBuono(buonoCod: String): Observable<ModelDecorrenzaBuono> {
    return this.http.get(this.baseUrl + PathApi.getDecorrenzaBuono + '/' + buonoCod).pipe(
      map((response: any) => {
        return response as ModelDecorrenzaBuono;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }

  updateDecorrenzaBuono(buonoCod: String, decorrenza: ModelDecorrenzaBuono): Observable<ModelDecorrenzaBuono> {
    return this.http.post(this.baseUrl + PathApi.updateDecorrenzaBuono + '/' + buonoCod, decorrenza).pipe(
      map((response: any) => {
        return response as ModelDecorrenzaBuono;
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.code == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    )
  }


  /*
   getListaComuni(dataValidita: string): Observable<ComuneBuonodom[]> {

       return this.http.get(this.baseUrl + PathApi.getListaComuni,{ ...encodeAsHttpParams({ dataValidita: dataValidita})}
           ).pipe(
           map((response: any) => {
               return response as ComuneBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getListaTipoEnte(): Observable<TipoEnteBuonodom[]> {

       return this.http.get(this.baseUrl + PathApi.getListaTipoEnte
           ).pipe(
           map((response: any) => {
               return response as TipoEnteBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getListaDenominazioni(): Observable<EnteBuonodom[]> {

       return this.http.get<EnteBuonodom>(this.baseUrl + PathApi.getListaDenominazioni
           ).pipe(
           map((response: any) => {
               return response as EnteBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getListaDenominazioniWithComuniAssociati(): Observable<EnteBuonodomWithComuniAss[]> {

       return this.http.get<EnteBuonodomWithComuniAss[]>(this.baseUrl + PathApi.getListaDenominazioniWithComuniAssociati
           ).pipe(
           map((response: any) => {
               return response as EnteBuonodomWithComuniAss[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getSchedeEntiGestori( ricerca: RicercaBuonodom ): Observable<RicercaBuonodomOutput[]> {

       return this.http.post<RicercaBuonodom>(this.baseUrl + PathApi.searchSchedeEntiGestori, ricerca
           ).pipe(
           map((response: any) => {
               return response as RicercaBuonodomOutput[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getSchedeEntiGestoriRendicontazioneConclusa( ricerca: RicercaBuonodom ): Observable<RicercaBuonodomOutput[]> {

           return this.http.post<RicercaBuonodom>(this.baseUrl + PathApi.searchSchedeEntiGestoriRendicontazioneConclusa, ricerca
               ).pipe(
               map((response: any) => {
                   return response as RicercaBuonodomOutput[];
               }),
               catchError((error: HttpErrorResponse) => {
                   if(!error.error || error.error == null || error.error.id == null) {
                       this.spinEmitter.emit(false);
                       this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                       return;
                   }
                   return throwError(
                      this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                   )
               }
               )
           );
   }

   getSchedeMultiEntiGestori( listaenti: any[][] = [] ): Observable<RicercaBuonodomOutput[]> {

       return this.http.post<RicercaBuonodom>(this.baseUrl + PathApi.searchSchedeMultiEntiGestori, listaenti
           ).pipe(
           map((response: any) => {
               return response as RicercaBuonodomOutput[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getListaStatoRendicontazioneArchivio(): Observable<StatoRendicontazioneBuonodom[]> {

       return this.http.get(this.baseUrl + PathApi.getListaStatoRendicontazioneArchivio
           ).pipe(
           map((response: any) => {
               return response as StatoRendicontazioneBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getStatoRendicontazioneConclusa(): Observable<StatoRendicontazioneBuonodom> {

       return this.http.get(this.baseUrl + PathApi.getStatoRendicontazioneConclusa
           ).pipe(
           map((response: any) => {
               return response as StatoRendicontazioneBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getListaComuniArchivio(): Observable<ComuneBuonodom[]> {

       return this.http.get(this.baseUrl + PathApi.getListaComuniArchivio
           ).pipe(
           map((response: any) => {
               return response as ComuneBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getListaTipoEnteArchivio(): Observable<TipoEnteBuonodom[]> {

       return this.http.get(this.baseUrl + PathApi.getListaTipoEnteArchivio
           ).pipe(
           map((response: any) => {
               return response as TipoEnteBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getSchedeEntiGestoriArchivio( ricerca: RicercaArchivioBuonodom ): Observable<RicercaBuonodomOutput[]> {

       return this.http.post<RicercaBuonodom>(this.baseUrl + PathApi.searchSchedeEntiGestoriArchivio, ricerca
           ).pipe(
           map((response: any) => {
               return response as RicercaBuonodomOutput[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getCronologia( idRendicontazione: number ): Observable<CronologiaBuonodom[]> {
       return this.http.get(this.baseUrl + PathApi.getCronologia, { ...encodeAsHttpParams({ idRendicontazione: idRendicontazione })}
           ).pipe(
           map((response: any) => {
               return response as CronologiaBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getCronologiaArchivio( idScheda: number ): Observable<CronologiaBuonodom[]> {

       return this.http.get(this.baseUrl + PathApi.getCronologiaArchivio, { ...encodeAsHttpParams({ idScheda: idScheda })}
           ).pipe(
           map((response: any) => {
               return response as CronologiaBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getDatiEnteProvince(dataValidita: string): Observable<ProvinciaBuonodom[]> {
       return this.http.get(this.baseUrl + PathApi.getListaProvince, { ...encodeAsHttpParams({ dataValidita: dataValidita })}).pipe(
           map((response: any) => {
               return response as ProvinciaBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getDatiEnteRendicontazione(idRendicontazioneEnte: number,anno: number): Observable<DatiEnteBuonodom> {
       return  this.http.get(this.baseUrl + PathApi.getDatiEnteRendicontazione, {...encodeAsHttpParams({ idRendicontazioneEnte: idRendicontazioneEnte,annoGestione: anno })}).pipe(
           map((response: any) => {
               return response as DatiEnteBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getSchedaEnte(id: number,anno: number): Observable<EnteBuonodom> {
       return  this.http.get(this.baseUrl + PathApi.getSchedaEnte, {...encodeAsHttpParams({ idScheda: id,annoGestione: anno })}).pipe(
           map((response: any) => {
           this.statorendicontazione = response.rendicontazione.codStatoRendicontazione;
               return response as EnteBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getSchedaAnagrafica(id: number,dataValidita: string): Observable<AnagraficaEnteBuonodom> {
       return  this.http.get(this.baseUrl + PathApi.getSchedaAnagrafica, {...encodeAsHttpParams({ idScheda: id, dataValidita: dataValidita})}).pipe(
           map((response: any) => {
               return response as AnagraficaEnteBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getPrestazioni(anno:number): Observable<PrestazioneBuonodom[]> {
       return this.http.get(this.baseUrl + PathApi.getPrestazioni, {...encodeAsHttpParams({ anno: anno})}).pipe(
           map((response: any) => {
               return response as PrestazioneBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getComuniAssociati(id: number,annoGestione:number): Observable<ComuneAssociatoBuonodom[]> {
       return this.http.get(this.baseUrl + PathApi.getComuniAssociati, {...encodeAsHttpParams({ id: id,annoGestione: annoGestione })}).pipe(
           map((response: any) => {
               return response as ComuneAssociatoBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getComuniAnagraficaAssociati(id: number,dataValidita: string): Observable<ComuneAssociatoBuonodom[]> {
       return this.http.get(this.baseUrl + PathApi.getComuniAnagraficaAssociati, {...encodeAsHttpParams({ id: id,dataValidita: dataValidita})}).pipe(
           map((response: any) => {
               return response as ComuneAssociatoBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getPrestazioniAssociate(id: number): Observable<PrestazioneAssociataBuonodom[]> {
       return this.http.get(this.baseUrl + PathApi.getPrestazioniAssociate, {...encodeAsHttpParams({ id: id })}).pipe(
           map((response: any) => {
               return response as PrestazioneAssociataBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getAsl(dataValidita :string): Observable<AslBuonodom[]> {
       return this.http.get(this.baseUrl + PathApi.getAsl, { ...encodeAsHttpParams({ dataValidita: dataValidita })}).pipe(
           map((response: any) => {
               return response as AslBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getDocAssociati(id: number): Observable<AllegatoBuonodom[]> {
       return this.http.get(this.baseUrl + PathApi.getDocumentiAssociati, {...encodeAsHttpParams({ idScheda: id })}).pipe(
           map((response: any) => {
               return response as AllegatoBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getAllegatoToDownload(idAllegato: number): Observable<AllegatoBuonodom> {
       return this.http.get(this.baseUrl + PathApi.getAllegatoToDownload, {...encodeAsHttpParams({ idAllegato: idAllegato })}).pipe(
           map((response: any) => {
               return response as AllegatoBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   saveDatiEnte (datiEnteToSave: DatiEnteToSave): Observable<GenericResponseWarnErrBuonodom>{
       return this.http.post(this.baseUrl + PathApi.saveDatiEnte, datiEnteToSave).pipe(
           map((response: any) => {
               return response as GenericResponseWarnErrBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   saveDatiAnagrafici (datiEnteToSave: DatiAnagraficiToSave): Observable<GenericResponseWarnErrBuonodom>{
       return this.http.post(this.baseUrl + PathApi.saveDatiAnagrafici, datiEnteToSave).pipe(
           map((response: any) => {
               return response as GenericResponseWarnErrBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   saveModelloA1 (datiModelloA1: SalvaModelloA1Buonodom): Observable<ResponseSalvaModelloBuonodom>{
       return this.http.post(this.baseUrl + PathApi.saveModelloA1, datiModelloA1).pipe(
           map((response: any) => {
               return response as ResponseSalvaModelloBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getCausaliStatiche(): Observable<CausaleBuonodom[]> {
       return this.http.get(this.baseUrl + PathApi.getCausaliStatiche).pipe(
           map((response: any) => {
               return response as CausaleBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
     }

     getCausali(idEnte: number): Observable<CausaleBuonodom[]> {
       return this.http.get(this.baseUrl + PathApi.getCausali, {...encodeAsHttpParams({ id_ente: idEnte })}).pipe(
           map((response: any) => {
               return response as CausaleBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
     }

     getVociModelloA1(idEnte: number): Observable<VociModelloA1Buonodom[]> {
       return this.http.get(this.baseUrl + PathApi.getVociModelloA1, {...encodeAsHttpParams({ id_ente: idEnte })}).pipe(
           map((response: any) => {
               return response as VociModelloA1Buonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
     }

     getTrasferimentiA2(idEnte: number): Observable<GetTrasferimentiA2Output> {
       return this.http.get(this.baseUrl + PathApi.getTrasferimentiA2, {...encodeAsHttpParams({ id_ente: idEnte })}).pipe(
           map((response: any) => {
               return response as GetTrasferimentiA2Output;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
     }

  getDatiModelloA1(idEnte: number): Observable<DatiModelloA1Buonodom[]> {
       return this.http.get(this.baseUrl + PathApi.getDatiModelloA1, {...encodeAsHttpParams({ id_ente: idEnte })}).pipe(
           map((response: any) => {
               return response as DatiModelloA1Buonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
     }

   salvaModelloA2(salva: SalvaModelloA2Buonodom): Observable<ResponseSalvaModelloBuonodom> {
       return this.http.post(this.baseUrl + PathApi.saveModelloA2, salva).pipe(
           map((response: any) => {
               return response as ResponseSalvaModelloBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   salvaModelloE(salva: SalvaModelloEBuonodom): Observable<ResponseSalvaModelloBuonodom> {
       return this.http.post(this.baseUrl + PathApi.saveModelloE, salva).pipe(
           map((response: any) => {
               return response as ResponseSalvaModelloBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getTipoVoceD(): Observable<TipoVoceD[]> {
       return this.http.get(this.baseUrl + PathApi.getTipoVoceD).pipe(
           map((response: any) => {
               return response as TipoVoceD[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getVoceModD(): Observable<VoceModD[]> {
       return this.http.get(this.baseUrl + PathApi.getVoceModD).pipe(
           map((response: any) => {
               return response as VoceModD[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getRendicontazioneModD(idScheda: number): Observable<RendicontazioneModD> {
       return this.http.get(this.baseUrl + PathApi.getRendicontazioneModD,  {...encodeAsHttpParams({ idScheda: idScheda })}
           ).pipe(
           map((response: any) => {
               return response as RendicontazioneModD;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   saveModD(rendicontazioneModD: RendicontazioneModD, notaEnte: string, notaInterna: string): Observable<ResponseSalvaModelloBuonodom> {
       return this.http.post(this.baseUrl + PathApi.saveModelloD, rendicontazioneModD,  {...encodeAsHttpParams({ notaEnte: notaEnte, notaInterna: notaInterna })}
       ).pipe(
           map((response: any) => {
               return response as ResponseSalvaModelloBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getRendicontazioneModE(idScheda: number): Observable<RendicontazioneModE> {
       return this.http.get(this.baseUrl + PathApi.getRendicontazioneModE,  {...encodeAsHttpParams({ idScheda: idScheda })}
           ).pipe(
           map((response: any) => {
               return response as RendicontazioneModE;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getMacroagBuonodomati(): Observable<MacroagBuonodomati[]> {
       return this.http.get(this.baseUrl + PathApi.getMacroagBuonodomati).pipe(
           map((response: any) => {
               return response as MacroagBuonodomati[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getSpesaMissione(): Observable<SpesaMissione[]> {
       return this.http.get(this.baseUrl + PathApi.getSpesaMissione).pipe(
           map((response: any) => {
               return response as SpesaMissione[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getRendicontazioneMacroagBuonodomati(idScheda): Observable<RendicontazioneMacroagBuonodomati> {
       return this.http.get(this.baseUrl + PathApi.getRendicontazioneMacroagBuonodomati,  {...encodeAsHttpParams({ idScheda: idScheda })}
           ).pipe(
           map((response: any) => {
               return response as RendicontazioneMacroagBuonodomati;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   saveMacroagBuonodomati(rendicontazioneMacroagBuonodomati: RendicontazioneMacroagBuonodomati, notaEnte: string, notaInterna: string): Observable<ResponseSalvaModelloBuonodom> {
       return this.http.post(this.baseUrl + PathApi.saveMacroagBuonodomati, rendicontazioneMacroagBuonodomati,  {...encodeAsHttpParams({ notaEnte: notaEnte, notaInterna: notaInterna })}
       ).pipe(
           map((response: any) => {
               return response as ResponseSalvaModelloBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   saveModelloB(rendicontazioneModB: RendicontazioneModB, notaEnte: string, notaInterna: string): Observable<ResponseSalvaModelloBuonodom> {
       return this.http.post(this.baseUrl + PathApi.saveModelloB, rendicontazioneModB,  {...encodeAsHttpParams({ notaEnte: notaEnte, notaInterna: notaInterna })}
       ).pipe(
           map((response: any) => {
               return response as ResponseSalvaModelloBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getMissioniModB(): Observable<MissioniB[]> {
       return this.http.get(this.baseUrl + PathApi.getMissioniModB).pipe(
           map((response: any) => {
               return response as MissioniB[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getProgrammiMissioneTotaliModB(idScheda: number): Observable<ProgrammiMissioneTotaliModB[]> {
       return this.http.get(this.baseUrl + PathApi.getProgrammiMissioneTotaliModB, { ...encodeAsHttpParams({ idSchedaEnte: idScheda })}).pipe(
           map((response: any) => {
               return response as ProgrammiMissioneTotaliModB[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getRendicontazioneModB(idScheda: number): Observable<RendicontazioneModB> {
       return this.http.get(this.baseUrl + PathApi.getRendicontazioneModB, { ...encodeAsHttpParams({ idScheda: idScheda })}).pipe(
           map((rendicontazione: any) => {
               return rendicontazione as RendicontazioneModB;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getRendicontazioneTotaliSpese(idScheda: number): Observable<RendicontazioneTotaliSpeseMissioni> {
       return this.http.get(this.baseUrl + PathApi.getRendicontazioneTotaliSpese, { ...encodeAsHttpParams({ idScheda: idScheda })}).pipe(
           map((rendicontazione: any) => {
               return rendicontazione as RendicontazioneTotaliSpeseMissioni;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getRendicontazioneTotaliMacroagBuonodomati(idScheda: number): Observable<RendicontazioneTotaliMacroagBuonodomati> {
       return this.http.get(this.baseUrl + PathApi.getRendicontazioneTotaliMacroagBuonodomati, { ...encodeAsHttpParams({ idScheda: idScheda })}).pipe(
           map((rendicontazione: any) => {
               return rendicontazione as RendicontazioneTotaliMacroagBuonodomati;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getPrestazioniModC(idScheda: number): Observable<PrestazioniC[]> {
       return this.http.get(this.baseUrl + PathApi.getPrestazioniC, { ...encodeAsHttpParams({ idScheda: idScheda })}).pipe(
           map((response: any) => {
               return response as PrestazioniC[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getRendicontazioneModC(idScheda: number): Observable<RendicontazioneModC> {
       return this.http.get(this.baseUrl + PathApi.getRendicontazioneModC, { ...encodeAsHttpParams({ idScheda: idScheda })}).pipe(
           map((rendicontazione: any) => {
               return rendicontazione as RendicontazioneModC;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   saveModelloC(rendicontazioneModC: RendicontazioneModC, notaEnte: string, notaInterna: string): Observable<ResponseSalvaModelloBuonodom> {
       return this.http.post(this.baseUrl + PathApi.saveModelloC, rendicontazioneModC,  {...encodeAsHttpParams({ notaEnte: notaEnte, notaInterna: notaInterna })}
       ).pipe(
           map((response: any) => {
               return response as ResponseSalvaModelloBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getConteggiModF(): Observable<ConteggiF> {
       return this.http.get(this.baseUrl + PathApi.getConteggiF).pipe(
           map((response: any) => {
               return response as ConteggiF;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getRendicontazioneModF(idScheda: number): Observable<RendicontazioneModF> {
       return this.http.get(this.baseUrl + PathApi.getRendicontazioneModF, { ...encodeAsHttpParams({ idScheda: idScheda })}).pipe(
           map((rendicontazione: any) => {
               return rendicontazione as RendicontazioneModF;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   saveModelloF(rendicontazioneModF: RendicontazioneModF, notaEnte: string, notaInterna: string): Observable<ResponseSalvaModelloBuonodom> {
       return this.http.post(this.baseUrl + PathApi.saveModelloF, rendicontazioneModF,  {...encodeAsHttpParams({ notaEnte: notaEnte, notaInterna: notaInterna })}
       ).pipe(
           map((response: any) => {
               return response as ResponseSalvaModelloBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getMsgInformativi(section: string): Observable<MsgInformativo[]> {

       return this.http.get(this.baseUrl + PathApi.getMsgInformativi, { ...encodeAsHttpParams({ section: section })}
           ).pipe(
           map((response: any) => {
               return response as MsgInformativo[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getMsgApplicativo(param: string): Observable<MsgApplicativo> {

       return this.http.get(this.baseUrl + PathApi.getMsgApplicativo, { ...encodeAsHttpParams({ param: param })}
           ).pipe(
           map((response: any) => {
               return response as MsgApplicativo;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }


   getListaVociModA(idScheda): Observable<VociModelloA> {
       return this.http.get(this.baseUrl + PathApi.getListaVociModA,  {...encodeAsHttpParams({ idScheda: idScheda })}
       ).pipe(
           map((response: any) => {
               return response as VociModelloA;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   saveModelloA(salvaModelloA: SalvaModelloA): Observable<ResponseSalvaModelloBuonodom> {
       return this.http.post(this.baseUrl + PathApi.saveModelloA, salvaModelloA).pipe(
           map((response: any) => {
               return response as ResponseSalvaModelloBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getPrestazioniResSemires( codTipologia: string, codTipoStruttura: string, anno: number ): Observable<PrestazioneBuonodom[]> {

       return this.http.get(this.baseUrl + PathApi.getPrestazioniResSemires, { ...encodeAsHttpParams({ codTipologia: codTipologia, codTipoStruttura: codTipoStruttura, anno: anno })}
           ).pipe(
           map((response: any) => {
               return response as PrestazioneBuonodom[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   confermaDati1(idSchedaEnte: number, cronologiaprofilo: CronologiaProfilo): Observable<GenericResponseWarnErrBuonodom> {

       return this.http.post(this.baseUrl + PathApi.confermaDati1, cronologiaprofilo, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte })}
           ).pipe(
           map((response: any) => {
               return response as GenericResponseWarnErrBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   richiestaRettifica1(idSchedaEnte: number, cronologiaprofilo: CronologiaProfilo): Observable<GenericResponseWarnErrBuonodom> {

       return this.http.post(this.baseUrl + PathApi.richiestaRettifica1, cronologiaprofilo, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte })}
           ).pipe(
           map((response: any) => {
               return response as GenericResponseWarnErrBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   confermaDati2(idSchedaEnte: number, cronologiaprofilo: CronologiaProfilo): Observable<GenericResponseWarnErrBuonodom> {

       return this.http.post(this.baseUrl + PathApi.confermaDati2, cronologiaprofilo, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte })}
           ).pipe(
           map((response: any) => {
               return response as GenericResponseWarnErrBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   richiestaRettifica2(idSchedaEnte: number, cronologiaprofilo: CronologiaProfilo): Observable<GenericResponseWarnErrBuonodom> {

       return this.http.post(this.baseUrl + PathApi.richiestaRettifica2, cronologiaprofilo, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte })}
           ).pipe(
           map((response: any) => {
               return response as GenericResponseWarnErrBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   valida(idSchedaEnte: number, cronologiaprofilo: CronologiaProfilo): Observable<GenericResponseBuonodom> {

       return this.http.post(this.baseUrl + PathApi.valida, cronologiaprofilo, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte})}
           ).pipe(
           map((response: any) => {
               return response as GenericResponseBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   storicizza(idSchedaEnte: number, cronologiaprofilo: CronologiaProfilo): Observable<GenericResponseBuonodom> {

       return this.http.post(this.baseUrl + PathApi.storicizza, cronologiaprofilo, { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte})}
           ).pipe(
           map((response: any) => {
               return response as GenericResponseBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getInfoRendicontazioneOperatore(idEnte: any): Observable<RendicontazioneEnteBuonodom> {
       return this.http.get(this.baseUrl + PathApi.getInfoRendicontazioneOperatore, { ...encodeAsHttpParams({ id_ente: idEnte })}
           ).pipe(
           map((response: any) => {
               return response as RendicontazioneEnteBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getListaPrestazioniValorizzateModA(idSchedaEnte: number): Observable<string[]> {
       return this.http.get(this.baseUrl + PathApi.getListaPrestazioniValorizzateModA, { ...encodeAsHttpParams({ idScheda: idSchedaEnte })}
           ).pipe(
           map((response: any) => {
               return response as string[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getListaPrestazioniValorizzateModB1(idSchedaEnte: number): Observable<string[]> {
       return this.http.get(this.baseUrl + PathApi.getListaPrestazioniValorizzateModB1, { ...encodeAsHttpParams({ idScheda: idSchedaEnte })}
           ).pipe(
           map((response: any) => {
               return response as string[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getListaPrestazioniValorizzateModC(idSchedaEnte: number): Observable<string[]> {
       return this.http.get(this.baseUrl + PathApi.getListaPrestazioniValorizzateModC, { ...encodeAsHttpParams({ idScheda: idSchedaEnte })}
           ).pipe(
           map((response: any) => {
               return response as string[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getListaComuniValorizzatiModA1(idSchedaEnte: number): Observable<string[]> {
       return this.http.get(this.baseUrl + PathApi.getListaComuniValorizzatiModA1, { ...encodeAsHttpParams({ idScheda: idSchedaEnte })}
           ).pipe(
           map((response: any) => {
               return response as string[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getListaComuniValorizzatiModA2(idSchedaEnte: number): Observable<string[]> {
       return this.http.get(this.baseUrl + PathApi.getListaComuniValorizzatiModA2, { ...encodeAsHttpParams({ idScheda: idSchedaEnte })}
           ).pipe(
           map((response: any) => {
               return response as string[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getListaComuniValorizzatiModE(idSchedaEnte: number): Observable<string[]> {
       return this.http.get(this.baseUrl + PathApi.getListaComuniValorizzatiModE, { ...encodeAsHttpParams({ idScheda: idSchedaEnte })}
           ).pipe(
           map((response: any) => {
               return response as string[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   getAttivitaSocioAssist(): Observable<AttivitaSocioAssist[]> {
       return this.http.get(this.baseUrl + PathApi.getAttivitaSocioAssist
           ).pipe(
           map((response: any) => {
               return response as AttivitaSocioAssist[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       );
   }

   //MODELLO B1
   isMacroagBuonodomatiCompiled(idSchedaEnte: number): Observable<boolean> {
       return this.http.get(this.baseUrl + PathApi.isMacroagBuonodomatiCompiled, { ...encodeAsHttpParams({ idScheda: idSchedaEnte })}).pipe(
           map((response: any) => {
               return response as boolean;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   canActiveModB(idSchedaEnte: number): Observable<boolean> {
       return this.http.get(this.baseUrl + PathApi.canActiveModB, { ...encodeAsHttpParams({ idScheda: idSchedaEnte })}).pipe(
           map((response: any) => {
               return response as boolean;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getMacroagBuonodomatiTotali(idSchedaEnte: number): Observable<ModelRendicontazioneTotaliMacroagBuonodomati> {
       return this.http.get(this.baseUrl + PathApi.getMacroagBuonodomatiTotali,  { ...encodeAsHttpParams({ idScheda: idSchedaEnte })}).pipe(
           map((response: any) => {
               return response as ModelRendicontazioneTotaliMacroagBuonodomati;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getPrestazioniModB1(idSchedaEnte: number): Observable<ModelPrestazioniB1[]> {
       return this.http.get(this.baseUrl + PathApi.getPrestazioniModB1,  { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte })}).pipe(
           map((response: any) => {
               return response as ModelPrestazioniB1[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   getElencoLblModB1(idSchedaEnte: number, anno?: number): Observable<ModelloB1ElencoLbl> {
       return this.http.get(this.baseUrl + PathApi.getElencoLblModB1,  { ...encodeAsHttpParams({ idSchedaEnte: idSchedaEnte, anno: anno })}).pipe(
           map((response: any) => {
               return response as ModelloB1ElencoLbl;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }


   saveModelloB1 (dati: SalvaModelloB1): Observable<ResponseSalvaModelloBuonodom>{
       return this.http.post(this.baseUrl + PathApi.saveModelloB1, dati).pipe(
           map((response: any) => {
               return response as ResponseSalvaModelloBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }


      //invio tranche

    inviotranche (inviaModelli: InviaModelli): Observable<GenericResponseWarnErrBuonodom>{
       return this.http.post(this.baseUrl + PathApi.inviatranche, inviaModelli).pipe(
           map((response: any) => {
               return response as GenericResponseWarnErrBuonodom;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

   //MODELLO tab per tranche
   getModelliPerTranche(idRendicontazione :number,codProfilo :string): Observable<LinkModelli[]> {
       return this.http.get(this.baseUrl + PathApi.getmodellipertranche, { ...encodeAsHttpParams({idRendicontazione: idRendicontazione, codProfilo: codProfilo })}).pipe(
           map((response: any) => {
               return response as LinkModelli[];
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }

//MODELLO tab per tranche
   getTranchePerModello(idSchedaEnte: number,modello:string): Observable<ModelTabTranche> {
       return this.http.get(this.baseUrl + PathApi.gettranchepermodello, { ...encodeAsHttpParams({ id_ente: idSchedaEnte,modello: modello })}).pipe(
           map((response: any) => {
               return response as ModelTabTranche;
           }),
           catchError((error: HttpErrorResponse) => {
               if(!error.error || error.error == null || error.error.id == null) {
                   this.spinEmitter.emit(false);
                   this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                   return;
               }
               return throwError(
                  this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
               )
           }
           )
       )
   }*/

  getParametroPerInformativa(informativa: string): Observable<ParametroBuonodom[]> {

    return this.http.get(this.baseUrl + PathApi.getParametroPerInformativa, { ...encodeAsHttpParams({ informativa: informativa }) }
    ).pipe(
      map((response: any) => {
        return response as ParametroBuonodom[];
      }),
      catchError((error: HttpErrorResponse) => {
        if (!error.error || error.error == null || error.error.detail == null) {
          this.spinEmitter.emit(false);
          this.router.navigate(['/redirect-page'], { skipLocationChange: true });
          return;
        }
        return throwError(
          this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc: error.error.descrizione }))
        )
      }
      )
    );
  }
  /*
      getParametro(codParam: string): Observable<ParametroBuonodom> {

          return this.http.get(this.baseUrl + PathApi.getParametro, { ...encodeAsHttpParams({ codParam: codParam })}
              ).pipe(
              map((response: any) => {
                  return response as ParametroBuonodom;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                     this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              }
              )
          );
      }

      esportaModelloA1 (datiModelloA1: EsportaModelloA1Buonodom): Observable<Response | any>{
            var estratto : ResponseEsportaModelliBuonodom;
          return this.http.post(this.baseUrl + PathApi.esportaModelloA1, datiModelloA1).pipe(
              map((response: any) => {
                     estratto = response as ResponseEsportaModelliBuonodom;
                     let m = new Map<string, any>();
                      let name = estratto.messaggio;
                      let messaggio = estratto.descrizione;
                      m.set( "name", name );
                      m.set( "messaggio", messaggio );
                      m.set( "file", estratto.excel);
                      let stato = estratto.esito;
                      m.set( "status" , stato);
                      return m;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                     this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              }
              )
          )
      }

      esportaModelloD (datiModelloD: EsportaModelloDBuonodom): Observable<Response | any>{
          var estratto : ResponseEsportaModelliBuonodom;
          return this.http.post(this.baseUrl + PathApi.esportaModelloD, datiModelloD).pipe(
              map((response: any) => {
                  estratto = response as ResponseEsportaModelliBuonodom;
                  let m = new Map<string, any>();
                  let name = estratto.messaggio;
                  let messaggio = estratto.descrizione;
                  m.set( "name", name );
                  m.set( "messaggio", messaggio );
                  m.set( "file", estratto.excel);
                  let stato = estratto.esito;
                  m.set( "status" , stato);
                  return m;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                      this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              })
          )
      }

      esportaModelloB (datiModelloB: EsportaModelloBBuonodom): Observable<Response | any>{
          var estratto : ResponseEsportaModelliBuonodom;
          return this.http.post(this.baseUrl + PathApi.esportaModelloB, datiModelloB).pipe(
              map((response: any) => {
                  estratto = response as ResponseEsportaModelliBuonodom;
                  let m = new Map<string, any>();
                  let name = estratto.messaggio;
                  let messaggio = estratto.descrizione;
                  m.set( "name", name );
                  m.set( "messaggio", messaggio );
                  m.set( "file", estratto.excel);
                  let stato = estratto.esito;
                  m.set( "status" , stato);
                  return m;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                      this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              })
          )
      }

      esportaIstat (datiModelloB: EsportaModelloBBuonodom): Observable<Response | any>{
          var estratto : ResponseEsportaModelliBuonodom;
          return this.http.post(this.baseUrl + PathApi.esportaIstat, datiModelloB).pipe(
              map((response: any) => {
                  estratto = response as ResponseEsportaModelliBuonodom;
                  let m = new Map<string, any>();
                  let name = estratto.messaggio;
                  let messaggio = estratto.descrizione;
                  m.set( "name", name );
                  m.set( "messaggio", messaggio );
                  m.set( "file", estratto.excel);
                  let stato = estratto.esito;
                  m.set( "status" , stato);
                  return m;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                      this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              })
          )
      }

      esportaModelloE (datiModelloE: EsportaModelloEBuonodom): Observable<Response | any>{
          var estratto : ResponseEsportaModelliBuonodom;
          return this.http.post(this.baseUrl + PathApi.esportaModelloE, datiModelloE).pipe(
              map((response: any) => {
                  estratto = response as ResponseEsportaModelliBuonodom;
                  let m = new Map<string, any>();
                  let name = estratto.messaggio;
                  let messaggio = estratto.descrizione;
                  m.set( "name", name );
                  m.set( "messaggio", messaggio );
                  m.set( "file", estratto.excel);
                  let stato = estratto.esito;
                  m.set( "status" , stato);
                  return m;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                      this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              })
          )
      }

      esportaModelloC (datiModelloC: EsportaModelloCBuonodom): Observable<Response | any>{
          var estratto : ResponseEsportaModelliBuonodom;
          return this.http.post(this.baseUrl + PathApi.esportaModelloC, datiModelloC).pipe(
              map((response: any) => {
                  estratto = response as ResponseEsportaModelliBuonodom;
                  let m = new Map<string, any>();
                  let name = estratto.messaggio;
                  let messaggio = estratto.descrizione;
                  m.set( "name", name );
                  m.set( "messaggio", messaggio );
                  m.set( "file", estratto.excel);
                  let stato = estratto.esito;
                  m.set( "status" , stato);
                  return m;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                      this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              })
          )
      }

      esportaModelloF (datiModelloF: EsportaModelloFBuonodom): Observable<Response | any>{
          var estratto : ResponseEsportaModelliBuonodom;
          return this.http.post(this.baseUrl + PathApi.esportaModelloF, datiModelloF).pipe(
              map((response: any) => {
                  estratto = response as ResponseEsportaModelliBuonodom;
                  let m = new Map<string, any>();
                  let name = estratto.messaggio;
                  let messaggio = estratto.descrizione;
                  m.set( "name", name );
                  m.set( "messaggio", messaggio );
                  m.set( "file", estratto.excel);
                  let stato = estratto.esito;
                  m.set( "status" , stato);
                  return m;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                      this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              })
          )
      }

      getMsgInformativiPerCod(codice: string): Observable<MsgInformativo> {

          return this.http.get(this.baseUrl + PathApi.getMsgInformativiPerCod, { ...encodeAsHttpParams({ codice: codice })}
              ).pipe(
              map((response: any) => {
                  return response as MsgInformativo;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                     this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              }
              )
          );

      }

      esportaModelloA2 (datiModelloA2: EsportaModelloA2Buonodom): Observable<Response | any>{
          var estratto : ResponseEsportaModelliBuonodom;
          return this.http.post(this.baseUrl + PathApi.esportaModelloA2, datiModelloA2).pipe(
            map((response: any) => {
                  estratto = response as ResponseEsportaModelliBuonodom;
                  let m = new Map<string, any>();
                  let name = estratto.messaggio;
                  let messaggio = estratto.descrizione;
                  m.set( "name", name );
                  m.set( "messaggio", messaggio );
                  m.set( "file", estratto.excel);
                  let stato = estratto.esito;
                  m.set( "status" , stato);
                  return m;
          }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                     this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              }
              )
          )
      }

   esportaModelloA (datiModelloA: EsportaModelloA): Observable<Response | any>{
            var estratto : ResponseEsportaModelliBuonodom;
          return this.http.post(this.baseUrl + PathApi.esportaModelloA, datiModelloA).pipe(
              map((response: any) => {
                     estratto = response as ResponseEsportaModelliBuonodom;
                     let m = new Map<string, any>();
                      let name = estratto.messaggio;
                      let messaggio = estratto.descrizione;
                      m.set( "name", name );
                      m.set( "messaggio", messaggio );
                      m.set( "file", estratto.excel);
                      let stato = estratto.esito;
                      m.set( "status" , stato);
                      return m;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                     this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              }
              )
          )
      }

      esportaMacroagBuonodomati (datiMacroagBuonodomati: EsportaMacroagBuonodomatiBuonodom): Observable<Response | any>{
          var estratto : ResponseEsportaModelliBuonodom;
          return this.http.post(this.baseUrl + PathApi.esportaMacroagBuonodomati, datiMacroagBuonodomati).pipe(
            map((response: any) => {
                  estratto = response as ResponseEsportaModelliBuonodom;
                  let m = new Map<string, any>();
                  let name = estratto.messaggio;
                  let messaggio = estratto.descrizione;
                  m.set( "name", name );
                  m.set( "messaggio", messaggio );
                  m.set( "file", estratto.excel);
                  let stato = estratto.esito;
                  m.set( "status" , stato);
                  return m;
          }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                     this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              }
              )
          )
      }

      esportaModelloB1 (datiModelloB1: EsportaModelloB1Buonodom): Observable<Response | any>{
          var estratto : ResponseEsportaModelliBuonodom;
          return this.http.post(this.baseUrl + PathApi.esportaModelloB1, datiModelloB1).pipe(
              map((response: any) => {
                  estratto = response as ResponseEsportaModelliBuonodom;
                  let m = new Map<string, any>();
                  let name = estratto.messaggio;
                  let messaggio = estratto.descrizione;
                  m.set( "name", name );
                  m.set( "messaggio", messaggio );
                  m.set( "file", estratto.excel);
                  let stato = estratto.esito;
                  m.set( "status" , stato);
                  return m;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                      this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              })
          )
      }

      getVociAllD(idScheda: number): Observable<VociAllD> {
          return this.http.get(this.baseUrl + PathApi.getVociAllD, { ...encodeAsHttpParams({ idScheda: idScheda })}
          ).pipe(
              map((response: any) => {
                  return response as VociAllD;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                     this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              }
              )
          );
      }

      getRendicontazioneModAllD(idScheda: number): Observable<RendicontazioneModAllD> {
          return this.http.get(this.baseUrl + PathApi.getRendicontazioneModAllD, { ...encodeAsHttpParams({ idScheda: idScheda })}).pipe(
              map((rendicontazione: any) => {
                  return rendicontazione as RendicontazioneModAllD;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                     this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              }
              )
          )
      }

      isModuloFnpsIsCompiled(idSchedaEnte: number): Observable<boolean> {
          return this.http.get(this.baseUrl + PathApi.compilabileModelloAllD, { ...encodeAsHttpParams({ idScheda: idSchedaEnte })}).pipe(
              map((response: any) => {
                  return response as boolean;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                     this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              }
              )
          )
      }

      saveModelloAllD(rendicontazioneAllD: RendicontazioneModAllD, notaEnte: string, notaInterna: string): Observable<ResponseSalvaModelloBuonodom> {
          return this.http.post(this.baseUrl + PathApi.saveModelloAllD, rendicontazioneAllD,  {...encodeAsHttpParams({ notaEnte: notaEnte, notaInterna: notaInterna })}
          ).pipe(
              map((response: any) => {
                  return response as ResponseSalvaModelloBuonodom;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                     this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              }
              )
          )
      }

      canActiveModFnps(idSchedaEnte: number): Observable<boolean> {
          return this.http.get(this.baseUrl + PathApi.canActiveModFnps, { ...encodeAsHttpParams({ idScheda: idSchedaEnte })}).pipe(
              map((response: any) => {
                  return response as boolean;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                     this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              }
              )
          )
      }

      esportaModuloFnps (datiModuloFnps: EsportaModuloFnps): Observable<Response | any>{
          var estratto : ResponseEsportaModelliBuonodom;
          return this.http.post(this.baseUrl + PathApi.esportaModuloFnps, datiModuloFnps).pipe(
              map((response: any) => {
                  estratto = response as ResponseEsportaModelliBuonodom;
                  let m = new Map<string, any>();
                  let name = estratto.messaggio;
                  let messaggio = estratto.descrizione;
                  m.set( "name", name );
                  m.set( "messaggio", messaggio );
                  m.set( "file", estratto.excel);
                  let stato = estratto.esito;
                  m.set( "status" , stato);
                  return m;
              }),
              catchError((error: HttpErrorResponse) => {
                  if(!error.error || error.error == null || error.error.id == null) {
                      this.spinEmitter.emit(false);
                      this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                      return;
                  }
                  return throwError(
                      this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
                  )
              })
          )
      }*/



  /*getStatiEnte(): Observable<StatoEnte[]> {

      return this.http.get(this.baseUrl + PathApi.getStatiEnte
          ).pipe(
          map((response: any) => {
              return response as StatoEnte[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      );
  }

  getAnniEsercizio(): Observable<number[]> {

      return this.http.get(this.baseUrl + PathApi.getAnniEsercizio
          ).pipe(
          map((response: any) => {
              return response as number[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      );
  }

  getAnniEsercizioArchivio(): Observable<number[]> {

      return this.http.get(this.baseUrl + PathApi.getAnniEsercizioArchivio
          ).pipe(
          map((response: any) => {
              return response as number[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      );
  }

  getStorico( idScheda: number ): Observable<StoricoBuonodom[]> {
      return this.http.get(this.baseUrl + PathApi.getStorico, { ...encodeAsHttpParams({ idScheda: idScheda })}
          ).pipe(
          map((response: any) => {
              return response as StoricoBuonodom[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      );
  }

//MODELLO associato
  getModelliassociati(idRendicontazione: number): Observable<ModelTabTranche[]> {
      return this.http.get(this.baseUrl + PathApi.getmodelliassociati, { ...encodeAsHttpParams({ idRendicontazione: idRendicontazione })}).pipe(
          map((response: any) => {
              return response as ModelTabTranche[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

//MODELLO tutti ma atetnzione deve esserci la tranche
  getModelli(): Observable<ModelTabTranche[]> {
      return this.http.get(this.baseUrl + PathApi.getmodelli).pipe(
          map((response: any) => {
              return response as ModelTabTranche[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getAllObbligo(): Observable<ModelObbligo[]> {
      return this.http.get(this.baseUrl + PathApi.getAllObbligo).pipe(
          map((response: any) => {
              return response as ModelObbligo[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }
getSchedaAnagraficaStorico(id: number, dataFineValidita: string): Observable<AnagraficaEnteBuonodom> {
      return  this.http.get(this.baseUrl + PathApi.getSchedaAnagraficaStorico, {...encodeAsHttpParams({ idScheda: id, dataFineValidita: dataFineValidita})}).pipe(
          map((response: any) => {
              return response as AnagraficaEnteBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getVerificaModelliVuoto(modello: string,idRendicontazione: number): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.get(this.baseUrl + PathApi.getVerificaModelliVuoto,{ ...encodeAsHttpParams({modello: modello, idRendicontazione: idRendicontazione })}).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

getComuniAnagraficaAssociatiStorico(id: number, dataFineValidita: string, dataInizioValidita: string): Observable<ComuneAssociatoBuonodom[]> {
      return this.http.get(this.baseUrl + PathApi.getComuniAnagraficaAssociatiStorico, {...encodeAsHttpParams({ id: id, dataFineValidita: dataFineValidita, dataInizioValidita: dataInizioValidita})}).pipe(
          map((response: any) => {
              return response as ComuneAssociatoBuonodom[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getMotivazioneChiusura(): Observable<Motivazioni[]> {
      return this.http.get(this.baseUrl + PathApi.getMotivazioniChiusura).pipe(
          map((response: any) => {
              return response as Motivazioni[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getCronologiaStato( idScheda: number ): Observable<CronologiaBuonodom[]> {
      return this.http.get(this.baseUrl + PathApi.getCronologiaStato, { ...encodeAsHttpParams({ idScheda: idScheda })}
          ).pipe(
          map((response: any) => {
              return response as CronologiaBuonodom[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      );
  }

  closeEnte(chiuso: ChiusuraEnte): Observable<ResponseSalvaModelloBuonodom> {
      return this.http.post(this.baseUrl + PathApi.closeEnte, chiuso).pipe(
          map((response: any) => {
              return response as ResponseSalvaModelloBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      );
  }

  getLastStato( idScheda: number ): Observable<StatoEnteBuonodom> {
      return this.http.get(this.baseUrl + PathApi.getLastStato, { ...encodeAsHttpParams({ idScheda: idScheda })}
          ).pipe(
          map((response: any) => {
              return response as StatoEnteBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      );
  }

  ripristinoEnte(chiuso: ChiusuraEnte): Observable<ResponseSalvaModelloBuonodom> {
      return this.http.post(this.baseUrl + PathApi.ripristinoEnte, chiuso).pipe(
          map((response: any) => {
              return response as ResponseSalvaModelloBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      );
  }

  getMotivazioneRipristino(): Observable<Motivazioni[]> {
      return this.http.get(this.baseUrl + PathApi.getMotivazioniRipristino).pipe(
          map((response: any) => {
              return response as Motivazioni[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getSchedaAnagraficaunione(ricerca: RicercaListaEntiDaunire): Observable<AnagraficaEnteBuonodom[]> {
      return  this.http.post(this.baseUrl + PathApi.getSchedaAnagraficaUnione,ricerca).pipe(
          map((response: any) => {
              return response as AnagraficaEnteBuonodom[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  unisciEnte(unisci: UnisciEnte): Observable<boolean> {
      return  this.http.post(this.baseUrl + PathApi.unioneEnte,unisci).pipe(
          map((response: any) => {
              return response as boolean;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

     getProvinceComuniLiberi(dataValidita: string): Observable<ComuneBuonodom[]> {
      return this.http.get(this.baseUrl + PathApi.getProvinciaComuneLibero, { ...encodeAsHttpParams({ dataValidita: dataValidita })}).pipe(
          map((response: any) => {
              return response as ComuneBuonodom[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

creaNuovoEnte (datiEnteToSave: DatiAnagraficiToSave): Observable<GenericResponseWarnErrBuonodom>{
      return this.http.post(this.baseUrl + PathApi.creaNuovoEnte, datiEnteToSave).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  checkModelloA(idRendicontazione: number): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.checkModelloA, idRendicontazione).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  checkModelloA1(idRendicontazione: number): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.checkModelloA1, idRendicontazione).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  checkMacroagBuonodomati(idRendicontazione: number): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.checkMacroagBuonodomati, idRendicontazione).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  checkModelloB1(idRendicontazione: number): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.checkModelloB1, idRendicontazione).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  checkModelloB(idRendicontazione: number): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.checkModelloB, idRendicontazione).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  checkModelloC(idRendicontazione: number): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.checkModelloC, idRendicontazione).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  checkModelloF(idRendicontazione: number): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.checkModelloF, idRendicontazione).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }


  saveMotivazioneCheck (datiMotivazione: SalvaMotivazioneCheck): Observable<ResponseSalvaModelloBuonodom>{
      return this.http.post(this.baseUrl + PathApi.saveMotivazioneCheck, datiMotivazione).pipe(
          map((response: any) => {
              return response as ResponseSalvaModelloBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getDettaglioPrestazione(codPrestazione: string, annoGestione: number): Observable<DettaglioPrestazione> {
      return this.http.get(this.baseUrl + PathApi.getPrestazioneRegionale,  { ...encodeAsHttpParams({ codPrestRegionale: codPrestazione, annoGestione: annoGestione })}).pipe(
          map((response: any) => {
              return response as DettaglioPrestazione;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getSchedeEntiGestoriCruscotto( ricerca: RicercaBuonodomCruscotto ): Observable<RicercaBuonodomOutput[]> {

      return this.http.post<RicercaBuonodom>(this.baseUrl + PathApi.searchSchedeEntiGestoriCruscotto, ricerca
          ).pipe(
          map((response: any) => {
              return response as RicercaBuonodomOutput[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      );
  }

  getModelliCruscotto(): Observable<ModelTabTrancheCruscotto[]> {
      return this.http.get(this.baseUrl + PathApi.getModelliCruscotto).pipe(
          map((response: any) => {
              return response as ModelTabTrancheCruscotto[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getStati( ricerca: RicercaBuonodomCruscotto ): Observable<StatiCruscotto[]> {

      return this.http.post<RicercaBuonodom>(this.baseUrl + PathApi.getStati, ricerca
          ).pipe(
          map((response: any) => {
              return response as StatiCruscotto[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      );
  }

  infoModello (infoModello: InfoModello): Observable<GenericResponseWarnCheckErrBuonodom>{
      return this.http.post(this.baseUrl + PathApi.getInfoModello, infoModello).pipe(
          map((response: any) => {
              return response as GenericResponseWarnCheckErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getPrestazioniReg1(): Observable<DettaglioPrestazioneConf[]> {
      return this.http.get(this.baseUrl + PathApi.getPrestazioniReg1).pipe(
          map((response: any) => {
              return response as DettaglioPrestazioneConf[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getDettaglioPrestazioneReg1(idPrestazione: number): Observable<DettaglioPrestazioneConf> {
      return this.http.get(this.baseUrl + PathApi.getPrestazioneRegionale1,  { ...encodeAsHttpParams({ idPrestazione: idPrestazione})}).pipe(
          map((response: any) => {
              return response as DettaglioPrestazioneConf;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getTipologie(): Observable<ListeConfiguratore[]> {
      return this.http.get(this.baseUrl + PathApi.getTipologie).pipe(
          map((response: any) => {
              return response as ListeConfiguratore[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getStrutture(): Observable<ListeConfiguratore[]> {
      return this.http.get(this.baseUrl + PathApi.getStrutture).pipe(
          map((response: any) => {
              return response as ListeConfiguratore[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getQuote(): Observable<ListeConfiguratore[]> {
      return this.http.get(this.baseUrl + PathApi.getQuote).pipe(
          map((response: any) => {
              return response as ListeConfiguratore[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getPrestColl(): Observable<ListeConfiguratore[]> {
      return this.http.get(this.baseUrl + PathApi.getPrestColl).pipe(
          map((response: any) => {
              return response as ListeConfiguratore[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getMacroagBuonodomatiConf(): Observable<ListeConfiguratore[]> {
      return this.http.get(this.baseUrl + PathApi.getMacroagBuonodomatiConf).pipe(
          map((response: any) => {
              return response as ListeConfiguratore[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getUtenzeConf(): Observable<ListeConfiguratore[]> {
      return this.http.get(this.baseUrl + PathApi.getUtenzeConf).pipe(
          map((response: any) => {
              return response as ListeConfiguratore[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getMissioneProgConf(): Observable<ListeConfiguratore[]> {
      return this.http.get(this.baseUrl + PathApi.getMissioneProgConf).pipe(
          map((response: any) => {
              return response as ListeConfiguratore[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getSpeseConf(): Observable<ListeConfiguratore[]> {
      return this.http.get(this.baseUrl + PathApi.getSpeseConf).pipe(
          map((response: any) => {
              return response as ListeConfiguratore[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getIstatConf(): Observable<ListeConfiguratore[]> {
      return this.http.get(this.baseUrl + PathApi.getIstatConf).pipe(
          map((response: any) => {
              return response as ListeConfiguratore[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getUtenzeIstatConf(utenze: PrestUtenza[]): Observable<ListeConfiguratore[]> {
      return this.http.post(this.baseUrl + PathApi.getUtenzeIstatConf, utenze).pipe(
          map((response: any) => {
              return response as ListeConfiguratore[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getNomenclatoreConf(): Observable<Nomenclatore[]> {
      return this.http.get(this.baseUrl + PathApi.getNomenclatoreConf).pipe(
          map((response: any) => {
              return response as Nomenclatore[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getPrestazioni2Conf(): Observable<Prest1Prest2[]> {
      return this.http.get(this.baseUrl + PathApi.getPrestazioni2Conf).pipe(
          map((response: any) => {
              return response as Prest1Prest2[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getPrestazioniMinConf(): Observable<Prest1PrestMin[]> {
      return this.http.get(this.baseUrl + PathApi.getPrestazioniMinConf).pipe(
          map((response: any) => {
              return response as Prest1PrestMin[];
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  savePrestazione(dettaglioPrestazione: DettaglioPrestazioneConf): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.savePrestazione, dettaglioPrestazione
      ).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  modificaPrestazione(dettaglioPrestazione: DettaglioPrestazioneConf): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.modificaPrestazione, dettaglioPrestazione
      ).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  savePrestazione2(prest2: Prest1Prest2): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.savePrestazione2, prest2
      ).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  modificaPrestazione2(prest2: Prest1Prest2): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.modificaPrestazione2, prest2
      ).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getDettaglioPrestazioneReg2(idPrestazione: number): Observable<Prest1Prest2> {
      return this.http.get(this.baseUrl + PathApi.getPrestazioneRegionale2,  { ...encodeAsHttpParams({ idPrestazione: idPrestazione})}).pipe(
          map((response: any) => {
              return response as Prest1Prest2;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  creaNuovoAnno(creaAnno: ListaEntiAnno): Observable<GenericResponseWarnErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.creaNuovoAnno, creaAnno
      ).pipe(
          map((response: any) => {
              return response as GenericResponseWarnErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getMaxAnnoRendicontazione(): Observable<number> {
      return this.http.get(this.baseUrl + PathApi.getMaxAnnoRendicontazione).pipe(
          map((response: any) => {
              return response as number;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  getUtenzeIstatTransConf(codUtenza: string): Observable<ListeConfiguratore> {
      return this.http.get(this.baseUrl + PathApi.getUtenzeIstatTransConf,  { ...encodeAsHttpParams({ codUtenza: codUtenza})}).pipe(
          map((response: any) => {
              return response as ListeConfiguratore;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  concludiRendicontazione(enti: RicercaBuonodomOutput[]): Observable<GenericResponseWarnCheckErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.concludiRendicontazione, enti).pipe(
          map((response: any) => {
              return response as GenericResponseWarnCheckErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }

  ripristinaRendicontazione(enti: RicercaBuonodomOutput[]): Observable<GenericResponseWarnCheckErrBuonodom> {
      return this.http.post(this.baseUrl + PathApi.ripristinaRendicontazione, enti).pipe(
          map((response: any) => {
              return response as GenericResponseWarnCheckErrBuonodom;
          }),
          catchError((error: HttpErrorResponse) => {
              if(!error.error || error.error == null || error.error.id == null) {
                  this.spinEmitter.emit(false);
                  this.router.navigate( ['/redirect-page'], { skipLocationChange: true } );
                  return;
              }
              return throwError(
                 this.BuonodomError.handleError(BuonodomError.toBuonodomError({ ...error, errorDesc : error.error.descrizione }))
              )
          }
          )
      )
  }
*/
}

/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { HttpClientModule,HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { DateAdapter, MAT_DATE_LOCALE } from '@angular/material/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from "@angular/router";
import { BuonodomBOClient } from "@buonodom-app/app/BuonodomBOClient";
import { MomentDateFormatter } from "@buonodom-app/app/MomentDateFormatter";
import { CustomDialogComponent } from '@buonodom-app/shared/custom-dialog/custom-dialog.component';
import { TwoDigitDecimalNumberDirective } from '@buonodom-app/shared/directive/two-digit-decimal-number.directive';
import { BuonodomErrorService } from '@buonodom-app/shared/error/buonodom-error.service';
import { FeedbackComponent } from '@buonodom-app/shared/feedback-component/feedback.component';
import { CronologiaComponent } from '@buonodom-operatore/components/cronologia/cronologia.component';
import { DatiIstanzaEnteComponent } from '@buonodom-operatore/components/dati-istanza-ente/dati-istanza-ente.component';
import { DatiIstanzaComponent } from '@buonodom-operatore/components/dati-istanza/dati-istanza.component';
import { DettaglioArchivioComponent } from '@buonodom-operatore/components/dettaglio-archivio/dettaglio-archivio.component';
import { VisualizzaCronologiaComponent } from '@buonodom-operatore/components/visualizza-cronologia/visualizza-cronologia.component';
import { VisualizzaVerificheComponent } from '@buonodom-operatore/components/visualizza-verifiche/visualizza-verifiche.component';
import { RedirectPageComponent } from '@buonodom-shared/redirect-page/redirect-page.component';
import { ToastsContainerComponent } from '@buonodom-shared/toast/toasts-container.component';
import { NgbDateParserFormatter, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DataTablesModule } from 'angular-datatables';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { CookieService } from "ngx-cookie-service";
import { NgxLoadingModule } from 'ngx-loading';
import { OperatoreBuonoModule } from 'projects/operatore-buono/operatore-buono.module';
import { BackofficeToolbarComponent } from '../shared/backoffice-toolbar/backoffice-toolbar.component';
import { FooterComponent } from '../shared/footer/footer.component';
import { ProjectToolbarComponent } from '../shared/project-toolbar/project-toolbar.component';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CurrencyFormat } from './currencyFormatter';
import { CustomDateAdapter } from './format-datepicker';
import { MaterialModule } from './material.module';
import { SelezioneProfiloApplicativoComponent } from './pages/selezione-profilo-applicativo/selezione-profilo-applicativo.component';
import { BuonoDettaglioComponent } from '@buonodom-operatore/components/buono-dettaglio/buono-dettaglio.component';
import { RequestInterceptor } from '@buonodom-app/interceptors/request-interceptor';
const routes: Routes = [];


@NgModule({
  declarations: [
    AppComponent,
    BackofficeToolbarComponent,
    ProjectToolbarComponent,
    FooterComponent,
    ToastsContainerComponent,
    RedirectPageComponent,
    SelezioneProfiloApplicativoComponent,
    DatiIstanzaComponent,
    DettaglioArchivioComponent,
    CurrencyFormat,
    TwoDigitDecimalNumberDirective,
    CustomDialogComponent,
    FeedbackComponent,
    VisualizzaCronologiaComponent,
    VisualizzaVerificheComponent,
    CronologiaComponent,
    DatiIstanzaEnteComponent,
    BuonoDettaglioComponent
  ],
  entryComponents: [
    CustomDialogComponent,
    VisualizzaCronologiaComponent,
    VisualizzaVerificheComponent,
    CronologiaComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    DataTablesModule,
    NgbModule,
    AppRoutingModule,
    NgxLoadingModule.forRoot({}),
    BrowserAnimationsModule,
    MaterialModule,
    RouterModule,
    OperatoreBuonoModule,
    AppRoutingModule,
    NgMultiSelectDropDownModule,
  ],
  providers: [BuonodomBOClient,
    {
      provide: NgbDateParserFormatter,
      useClass: MomentDateFormatter
    },
    CookieService,
    BuonodomErrorService,
    { provide: MAT_DATE_LOCALE, useValue: 'en-GB' },
    { provide: DateAdapter, useClass: CustomDateAdapter },
	{provide: HTTP_INTERCEPTORS, useClass: RequestInterceptor, multi:true}
  ],
  bootstrap: [AppComponent]
})

export class AppModule { }

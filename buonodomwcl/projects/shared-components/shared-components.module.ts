import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DataTablesModule } from 'angular-datatables';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MaterialModule } from '@buonodom-app/app/material.module';
import { RouterModule } from '@angular/router';
import { VisualizzaCronologiaComponent } from '@buonodom-operatore/components/visualizza-cronologia/visualizza-cronologia.component';

@NgModule({
  declarations: [
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    DataTablesModule,
    NgbModule,
    MaterialModule,
    RouterModule,
    MaterialModule
  ],
  exports: [
  ],
  entryComponents: [
  ]
})
export class SharedComponentModule { }

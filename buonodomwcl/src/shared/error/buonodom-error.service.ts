/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Injectable } from '@angular/core';
import { AppToastService } from '@buonodom-shared/toast/app-toast.service';
import { BuonodomError } from './buonodom-error.model';

@Injectable()
export class BuonodomErrorService {

    constructor(private toastService: AppToastService) {}

    handleError(error: BuonodomError) {
        this.toastService.showServerError(error);
    }

    handleWarning(warn: BuonodomError) {
        this.toastService.showWarning(warn);
    }
}
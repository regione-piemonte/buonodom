/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export class Feedback {
    public constructor(
        public showFeedback: boolean,
        public tipoFeedback: string,
        public messaggioFeedback: string,
    ){}
}

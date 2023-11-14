/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import axios from "axios";
import { apiInterceptorRequestId } from "src/services/utils";

export const httpScelSoc = axios.create({
  baseURL: process.env.APP_API_BASE_URL_SCELSOC
});

export const httpBuonoDom = axios.create({
  baseURL: process.env.APP_API_BASE_URL_BUONODOM
});

[httpScelSoc, httpBuonoDom].forEach(http => apiInterceptorRequestId(http));

/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { store } from "src/store";

export const privacyPolicy = () => {
  let url = "";
  return url;
};

export const cookiePolicy = () => {
  let url = "";
  return url;
};

export const urlPiemonteTu = () => {
  let user = store.getters["getUser"];
  return user
    ? ""
    : "/";
};


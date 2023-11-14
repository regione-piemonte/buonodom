/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <div id="q-app">
    <the-guard>
      <router-view />
    </the-guard>
  </div>
</template>
<script>
import TheGuard from "./components/TheGuard";

import {
  getServizioAttivo,
} from "src/services/api";


export default {
  name: "App",
  components: {
    TheGuard,
  },
  async created () {
  }
};
</script>

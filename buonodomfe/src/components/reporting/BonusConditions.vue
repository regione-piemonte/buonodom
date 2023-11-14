/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <div>
    <!-- CARD CONSENSO -->
    <q-card class="q-mt-lg">
      <q-scroll-area :visible="visible" class="height-scroll-area">
        <h3>Dichiarazione di spesa</h3>
        <p>
          Io sottoscritto/a {{richiedente}}
          Consapevole delle sanzioni penali, nel caso di dichiarazioni non
          veritiere e falsitÃ  negli atti, richiamate dagli artt. 46, 47, 71, 75,
          76 del D.P.R. 445 del 28/12/2000, dichiaro che la documentazione
          giustificativa attestante la fruizione del servizio di assistenza
          domiciliare attivato per lâassegnazione del âBUONOâ nel mese di
          {{mese}} risulta conforme allâoriginale conservato e
          disponibile per eventuali controlli. Dichiaro inoltre di avere
          rispettato le condizioni e le modalitÃ  di fruizione del âBuono
          domiciliaritÃ . Richiedo pertanto la liquidazione del controvalore
          economico del âbuonoâ per un importo pari a â¬ 600,00 mensili, con
          riferimento alle mensilitÃ  di {{mesirendicontazioni}} e ai documenti
          sopra indicati.
        </p>
      </q-scroll-area>
    </q-card>
  </div>
</template>

<script>
export default {
  name: "BonusConditions",
  props: {

  },
  components: {},
  data () {
    return {
      visible: true,
    };
  },
  computed: {

  },
  methods: {

  },
  watch: {

  },
  async created () {
    // SERVE PER RIMETTERE IN CIMA IL COMPONENTE
    window.scrollTo(0, 0);
  },
};
</script>

<style lang="sass">
.q-stepper
  box-shadow: none
  background: transparent

.q-stepper--horizontal .q-stepper__line:after
  height: 3px
  background: #1976d2

.q-stepper--horizontal .q-stepper__line:before
  height: 3px
  background-color: #1976d2

.q-stepper__tab
  color: #1976d2

.q-scrollarea
  padding: 16px
</style>

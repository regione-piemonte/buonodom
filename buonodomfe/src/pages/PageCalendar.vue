/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <div>
    <lms-page class="page--calendar page--calendar-grid" padding v-if="!loading && idRichiesta">
      <BreadcrumbsCustom
        :name="utenteDestinatario.nome + ' ' + utenteDestinatario.cognome"
        :path1="'gestione'"
        :label1="'Gestione'"
        :path2="'calendario'"
        :label2="'Mesi sospesi'"
        :title="'Mesi sospesi'"
        :id="idRichiesta"
      ></BreadcrumbsCustom>
      <q-card>
        <q-card-section v-if="fruizione">
          <section class="q-mb-lg">
            <h2 class="q-mb-lg">
              Seleziona i mesi sospesi per la rendicontazione
            </h2>
            <!-- MESI -->
            <lms-banner type="info">
              <h2>Mese in sospensione</h2>
              <p>
                Periodo di 24 mesi Ã¨ possibile sospendere la rendicontazione
                della documentazione per un massimo di 2 mesi. Qualora venisse
                scelta l'opzione di sospesione, il periodo di rendicontazione
                verrÃ  esteso per progressivamente al numero di mesi selezionati
              </p>
            </lms-banner>
            <div
              v-if="
                !isSelectionValid && selectedMonths && selectedMonths.length > 2
              "
              class="q-my-lg"
            >
              <lms-banner type="negative">
                <h2>Attenzione</h2>
                <p>Selezionare un massimo di due mesi</p>
              </lms-banner>
            </div>

            <div class="q-gutter-sm check-radio--month">
              <div class="row q-gutter-y-lg">
                <div class="col-12 check-radio--month--col">
                  <div class="check-radio--month--cell">
                    <q-option-group
                      v-model="selectedMonths"
                      type="checkbox"
                      :options="getDateRangeOptions"
                    />
                  </div>
                </div>
              </div>
            </div>
            <p v-if="!isSelectionValid" class="sr-only">Il tasto, per inviare i mesi sospesi per la rendicontazione, Ã¨ bloccato perchÃ¨ la selezione non Ã¨ corretta</p>
            <div class="row button-action-stepper q-my-xxl">
              <div class="col-12 col-md second-block">
                <div class="row justify-end q-gutter-y-lg">
                  <div class="col-auto">
                    <q-btn
                      :disable="!isSelectionValid"
                      class="q-ml-xl"
                      color="primary"
                      label="INVIA"
                      @click="isConfirmed = true"
                    />
                  </div>
                </div>
              </div>
            </div>
          </section>
        </q-card-section>
        <template v-else>
          <q-card-section>
            <lms-banner type="negative">
              <h2>Nessun periodo disponibile</h2>
              <p>
                Non Ã¨ possibile selezionare mesi sabbatici per questa domanda
              </p>
            </lms-banner>
          </q-card-section>
        </template>
      </q-card>
    </lms-page>
    <!-- MODALE CONFERMA -->
    <q-dialog v-model="isConfirmed" :maximized="$q.screen.lt.md" persistent>
      <q-card style="max-width: 800px">
        <q-card-section>
          <lms-banner type="warning">
            <h2>Attenzione</h2>
            <p>
              Per i mesi selezionati non sarÃ  possibile rendicontare documenti di spesa.
            </p>
          </lms-banner>

          <div class="row justify-between">
            <div class="col-auto">
              <q-btn color="negative" outline @click="isConfirmed = false"
                >ANNULLA</q-btn
              >
            </div>
            <div class="col-auto">
              <q-btn color="primary" outline @click="inviaSelezione()"
                >CONFERMA</q-btn
              >
            </div>
          </div>
        </q-card-section>
      </q-card>
    </q-dialog>
    <!-- MODALE CONFERMA -->
  </div>
</template>

<script>
import store from "src/store/index";
import {
  getPeriodoRendicontazione,
  putPeriodoRendicontazione,
} from "src/services/api";
import moment from "moment";
import LmsBanner from "components/core/LmsBanner";
import { snackbarError } from "src/services/utils";
import BreadcrumbsCustom from 'src/components/reporting/BreadcrumbsCustom.vue';
export default {
  name: "PageCalendar",
  components: {
    LmsBanner, BreadcrumbsCustom
  },
  data () {
    return {
      loading: false,
      fruizione: null,
      selectedMonths: [],
      idRichiesta: null,
      isConfirmed: false,
    };
  },
  computed: {
    utenteDestinatario () {
      return store.getters["getUtenteDestinatario"];
    },
    startDate () {
      return moment(this.fruizione.data_inizio)
    },
    endDate () {
      return moment(this.fruizione.data_fine)
    },
    getDateRangeOptions () {
      const options = [];
      const currentDate = this.startDate.clone();
      while (currentDate.isSameOrBefore(this.endDate)) {
        options.push({
          value: this.formatYearMonthValue(currentDate),
          label: this.formatYearMonthLabel(currentDate),
        });
        currentDate.add(1, "month");
      }
      return options;
    },
    isSelectionValid () {
      return this.selectedMonths.length < 3;
    },
    oldMonths () {
      return this.selectedMonths.length > 0 ? this.selectedMonths.map((item) => {
        return moment(item).lang('it').format('MMMM YYYY')
      }) : [];
    },
  },
  methods: {
    formatYearMonthValue (date) {
      return date.format("YYYY-MM");
    },
    formatYearMonthLabel (date) {
      return date.lang('it').format("MMMM YYYY");
    },
    async inviaSelezione () {
      this.isConfirmed = false
      await store.dispatch("setSpinner", false);
      let sabbatici = {
        sabbatici: this.selectedMonths
      }
      try {
        let { data } = await putPeriodoRendicontazione(
          this.idRichiesta,
          sabbatici
        );
        await store.dispatch("setSpinner", false);
        this.$q.notify({
          type: "positive",
          message: "Mesi di sospensione del buono salvati",
        });
        this.$router.push({ name: 'gestione', params: { id: this.idRichiesta } });
      } catch (err) {

        snackbarError(err)
        
      }
    },
    setMonth (data) {
      if (data.hasOwnProperty('sabbatici')) {
        for (let item of data.sabbatici) {
          moment(item) >= moment(this.fruizione.data_inizio) && moment(item) <= moment(this.fruizione.data_fine) ? this.selectedMonths.push(item) : null;
        }
      } else {
        this.selectedMonths = []
      }
    }
  },
  async created () {
    window.scrollTo(0, 0);
    await store.dispatch("setSpinner", true);
    this.loading = true;
    this.idRichiesta = this.$route.params.id
      ? this.$route.params.id
      : null;
    if (this.utenteDestinatario == null) {
      this.$router.push("/");
      await store.dispatch("setSpinner", false);
      this.loading = false;
    }
    try {
      let { data } = await getPeriodoRendicontazione(this.idRichiesta);
      this.fruizione = data.fruizione;
      this.setMonth(data)
      await store.dispatch("setSpinner", false);
      this.loading = false
    } catch (err) {
      await store.dispatch("setSpinner", false);
      this.loading = false
      snackbarError(err);
    }
  }
};
</script>

/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <div>
    <div class="q-page lms-page" v-if="utenteDestinatario">
      <BreadcrumbsCustom
        :name="utenteDestinatario.nome + ' ' + utenteDestinatario.cognome"
        :path1="'gestione'"
        :label1="'Gestione'"
        :path2="'documentazionespese'"
        :label2="'Documentazione delle spese'"
        :path3="'aggiungidocumentospesa'"
        :label3="'Aggiungi documento spesa'"
        :title="'Aggiungi documento spesa'"
        :id="idRichiesta"
      ></BreadcrumbsCustom>

      <template v-if="getSetspinner">
        <div class="container">
          <div class="row">
            <div class="col-12 col-lg-8 offset-lg-2">
              <spinner></spinner>
            </div>
          </div>
        </div>
      </template>
      <template v-else>
        <q-card>
          <!-- FINE CONDIZIONI PER RICEVERE IL BUONO -->
          <Documentation
            @validare="validare($event)"
            @rendiconta="rendiconta($event)"
            :idRichiesta="idRichiesta"
          ></Documentation>

          <!-- CONDIZIONI PER RICEVERE IL BUONO -->
          <q-card-section>
            <div class="row q-my-xl justify-between">
              <div class="col-auto">
                <q-toggle
                  v-model="condizioni"
                  aria-labelledby="condizioni"
                  id="condizioniToggle"
                  name="condizioniToggle"
                  label="*Ho compreso e accetto le condizioni per ricevere il buono"
                />
              </div>
            </div>
          </q-card-section>

          <!-- PULSANTI -->
          <div class="row button-action-stepper q-my-xxl">
            <div class="col-12 col-md second-block">
              <div class="row justify-end q-gutter-y-lg">
                <div class="col-auto" v-if="condizioni">
                  <q-btn
                    :disable="valid !== false"
                    class="q-ml-xl"
                    color="primary"
                    @click="inviaRendicontazione()"
                    label="CONFERMA"
                    :aria-label="rendicontazione && rendicontazione.hasOwnProperty('mesi') && rendicontazione.mesi.length > 0 ? 'stai inviando la documentazione per: ' + rendicontazione.mesi : 'non sono ancora stati selezionati i mesi'"
                  />
                </div>
              </div>
            </div>
          </div>
        </q-card>
      </template>
    </div>
  </div>
</template>

<script>
import store from "src/store/index";
import { postRendicontazione, postAllegatoBuono } from "src/services/api";

import { snackbarError } from "src/services/utils";
import Documentation from "src/components/reporting/Documentation.vue";
import BreadcrumbsCustom from "src/components/reporting/BreadcrumbsCustom.vue";

export default {
  name: "PageReportingDocuments",
  components: { Documentation, BreadcrumbsCustom },
  data() {
    return {
      valid: null,
      condizioni: false,
      idRichiesta: null,
      visible: true,
      rendicontazione: null,
      allegati: null,
      allegatiOk: true,
      loading: false,
    };
  },
  methods: {
    validare(value) {
      this.valid = value;
    },
    rendiconta(item) {
      this.rendicontazione = item.obj;
      this.allegati = item.files;
    },
    async postAllegatoBuonoSingolo(type) {
      try {
        let { data } = await postAllegatoBuono(
          this.idRichiesta,
          type,
          this.allegati[type],

          {
            headers: {
              "Content-type": this.allegati[type].type,
              "X-Filename-Originale": this.allegati[type].name.replace(
                /[^0-9a-zA-Z-.]+/g,
                ""
              ),
            },
          }
        );
        let indice = this.rendicontazione["dettagli"].findIndex(
          (item) => item.tipo == type
        );
        this.rendicontazione["dettagli"][indice].id_allegato = data.allegato_id;
      } catch (err) {
        this.allegatiOk = false;
        snackbarError(err);
        await store.dispatch("setSpinner", false);
      }
    },
    async controlloAllegati(allegati) {
      for (let attribute in allegati) {
        try {
          if (allegati[attribute]) {
            await this.postAllegatoBuonoSingolo(attribute);
          }
        } catch (err) {
          snackbarError(err);
          this.allegatiOk = false;
        }
      }
    },
    async inviaRendicontazione() {
      await store.dispatch("setSpinner", true);
      await this.controlloAllegati(this.allegati);
      if (this.allegatiOk) {
        try {
          let { data } = await postRendicontazione(
            this.idRichiesta,
            this.rendicontazione
          );
          await store.dispatch("setSpinner", false);
          this.$q.notify({
            type: "positive",
            message: "Documento spesa aggiunto",
          });
          this.$router.push({
            name: "documentazionespese",
            params: { id: this.idRichiesta },
          });
        } catch (err) {
          await store.dispatch("setSpinner", false);
          snackbarError(err);
        }
      } else {
        this.allegatiOk = true;
        await store.dispatch("setSpinner", false);
        this.$q.notify({
          type: "negative",
          message: "Ci sono problemi con gli allegati",
        });
      }
    },
  },
  computed: {
    getSetspinner () {
      return store.getters["setSpinner"]
    },
    utenteDestinatario() {
      return store.getters["getUtenteDestinatario"];
    },
    validForm() {
      return this.valid;
    },
  },
  async created() {
    window.scrollTo(0, 0);
    this.idRichiesta = this.$route.params.id ? this.$route.params.id : null;
    if (this.utenteDestinatario == null) {
      this.$router.push("/");
    }
  },
};
</script>

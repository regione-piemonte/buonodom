/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <div v-if="idRichiesta && !loading">
    <div class="q-page lms-page">
      <BreadcrumbsCustom
        :name="utenteDestinatario.nome + ' ' + utenteDestinatario.cognome"
        :path1="'gestione'"
        :label1="'Gestione'"
        :path2="'rendicontazione'"
        :label2="'Documentazione delle spese'"
        :title="'Documentazione delle spese'"
        :id="idRichiesta"
      ></BreadcrumbsCustom>
      <lms-banner type="warning">
        <p>
          Ã necessario eseguire puntualmente la rendicontazione delle spese
          sostenute per avere il diritto all'erogazione del
          <strong>Buono domiciliaritÃ </strong>
        </p>
        <p v-if="rendicontazioni.length < 1">
          <strong>Non hai ancora allegato alcun documento di spesa</strong>
        </p>
      </lms-banner>
      <template v-if="rendicontazioni && rendicontazioni.length > 0">
        <q-card class="q-mt-xl q-mb-xl">
          <!-- CARD ELENCO RENDICONTAZIONI -->
          <q-card-section
            v-for="(oggetti, periodoFine) in gruppiPeriodoFine"
            :key="periodoFine"
          >
            <h2>
              Documenti salvati per il periodo con scadenza
              <span class="text-uppercase">{{
                formatDateEnd(periodoFine)
              }}</span>
              <tooltip
                :titleBanner="documentiSalvati.title"
                :textBanner="documentiSalvati.text"
              />
            </h2>
            <div
              v-for="(item, i) of oggetti"
              :key="i"
              :class="'border-left--' + stateDocument(item.stato)"
            >
              <p class="sr-only">
                Il documento numero {{ item.numero }} Ã¨ in stato da
                {{ item.stato.replace(/_/g, " ") }}
              </p>
              <div class="row q-mb-xl q-col-gutter-xl">
                <div class="col-4">
                  <dl>
                    <dt>Tipo documento</dt>
                    <dd>
                      {{ item.tipologia | tipologia }}
                    </dd>
                  </dl>
                </div>
                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Numero documento</dt>
                    <dd>
                      {{ item.numero }}
                    </dd>
                  </dl>
                </div>
                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Mesi di riferimento</dt>
                    <dd class="text-capitalize">
                      {{ convertiMesi(item.mesi) }}
                    </dd>
                  </dl>
                </div>
              </div>
              <h3>Assistente familiare</h3>
              <div
                v-for="item of filtredList(item.id_fornitore, fornitoriList)"
                :key="item.id"
              >
                <div class="row q-mb-xl q-col-gutter-xl">
                  <div class="col-4" v-if="item.nome">
                    <dl>
                      <dt>Nome</dt>
                      <dd>
                        {{ item.nome }}
                      </dd>
                    </dl>
                  </div>
                  <div class="col-12 col-md-4" v-if="item.cognome">
                    <dl>
                      <dt>Cognome</dt>
                      <dd>
                        {{ item.cognome }}
                      </dd>
                    </dl>
                  </div>
                  <div class="col-12 col-md-4" v-if="item.denominazione">
                    <dl>
                      <dt>Denominazione</dt>
                      <dd>
                        {{ item.denominazione }}
                      </dd>
                    </dl>
                  </div>
                </div>
                <div class="row q-mb-xl q-col-gutter-xl">
                  <div class="col-4" v-if="item.piva">
                    <dl>
                      <dt>Partita IVA</dt>
                      <dd>
                        {{ item.piva }}
                      </dd>
                    </dl>
                  </div>
                  <div class="col-12 col-md-4" v-if="item.cf">
                    <dl>
                      <dt>Codice Fiscale</dt>
                      <dd>
                        {{ item.cf }}
                      </dd>
                    </dl>
                  </div>
                </div>
              </div>
              <h3>Allegati</h3>
              <div class="row q-mb-xl q-col-gutter-xl">
                <div class="col-4">
                  <dl>
                    <dt id="nome-allegato">Nome</dt>
                  </dl>
                </div>
                <div class="col-12 col-md-4">
                  <dl>
                    <dt id="importo-allegato">Importo</dt>
                  </dl>
                </div>
                <div class="col-12 col-md-4">
                  <dl>
                    <dt id="data-allegato">Data</dt>
                  </dl>
                </div>
              </div>
              <div v-for="(allegato, i) of item.dettagli" :key="i">
                <div class="row q-mb-xl q-col-gutter-xl">
                  <div class="col-4">
                    <dl>
                      <dd aria-labelledby="nome-allegato">
                        <a
                          :href="url + '/' + allegato.id_allegato"
                          :aria-label="
                            'visualizza il documento ' +
                            allegato.tipo +
                            ' ' +
                            allegato.id_allegato
                          "
                          target="_blank"
                          class="text--info position-relative"
                        >
                          <span v-if="allegato.tipo === 'GIUSTIFICATIVO'"
                            >Documento sezione A</span
                          >
                          <span v-if="allegato.tipo === 'QUIETANZA'"
                            >Documento sezione B</span
                          >
                        </a>
                      </dd>
                    </dl>
                  </div>
                  <div class="col-12 col-md-4">
                    <dl>
                      <dd aria-labelledby="importo-allegato">
                        <span class="sr-only"
                          >L'importo del documento
                          {{ allegato.tipo + " " + allegato.id_allegato }}
                          Ã¨</span
                        >
                        <span aria-hidden="true">â¬</span>
                        {{ allegato.importo }} <span class="sr-only">euro</span>
                      </dd>
                    </dl>
                  </div>
                  <div class="col-12 col-md-4">
                    <dl>
                      <dd aria-labelledby="data-allegato">
                        <span class="sr-only"
                          >La data del documento
                          {{ allegato.tipo + " " + allegato.id_allegato }}
                          Ã¨</span
                        >{{ allegato.data | formatDate }}
                      </dd>
                    </dl>
                  </div>
                </div>
              </div>
              <div class="col-12 q-my-sm" style="float: right">
                <q-btn
                  v-if="item.stato == 'DA_INVIARE'"
                  color="negative"
                  @click="openModal(item)"
                  :aria-label="
                    'elimina il documento ' +
                    item.tipologia +
                    ' numero: ' +
                    item.numero
                  "
                  label="ELIMINA DOCUMENTO"
                ></q-btn>
              </div>
            </div>
          </q-card-section>
          <!-- FINE CARD ELENCO RENDICONTAZIONI -->
        </q-card>

        <!-- NUOVO DOCUMENTO SPESA-->
        <div class="row q-mb-sm">
          <div class="col-12">
            <q-btn
              @click="openApplication(idRichiesta, 'aggiungidocumentospesa')"
              color="primary"
              :aria-label="'aggiungi documento spesa: '"
              label="AGGIUNGI DOCUMENTO SPESA"
            ></q-btn>
          </div>
        </div>
        <!-- FINE  NUOVO DOCUMENTO SPESA -->
      </template>

      <template v-else>
        <div class="row q-mb-sm q-mt-sm">
          <div class="col-12">
            <q-btn
              @click="openApplication(idRichiesta, 'aggiungidocumentospesa')"
              color="primary"
              :aria-label="'aggiungi documento spesa: '"
              label="AGGIUNGI DOCUMENTO SPESA"
            ></q-btn>
          </div>
        </div>
      </template>
    </div>
    <!-- MODALE ELIMINAZIONE -->
    <q-dialog v-model="isCancelled" :maximized="$q.screen.lt.md" persistent>
      <q-card style="max-width: 800px">
        <q-card-section>
          <lms-banner type="warning">
            <h2>Sei sicuro di voler eliminare documento?</h2>
          </lms-banner>

          <div class="row justify-between">
            <div class="col-auto">
              <q-btn color="negative" outline @click="annulla()">ANNULLA</q-btn>
            </div>
            <div class="col-auto">
              <q-btn color="primary" outline @click="eliminaRendicontazione()"
                >CONFERMA</q-btn
              >
            </div>
          </div>
        </q-card-section>
      </q-card>
    </q-dialog>
    <!-- MODALE ELIMINAZIONE -->
  </div>
</template>
<script>
import moment from "moment";
import "moment/locale/it";
import store from "src/store/index";
import { getRendicontazione, deleteRendicontazione, getFornitori } from "src/services/api";
import { snackbarError } from "src/services/utils";
import LmsBanner from "components/core/LmsBanner";
import BreadcrumbsCustom from 'src/components/reporting/BreadcrumbsCustom.vue';
import Tooltip from "components/core/Tooltip";

moment.locale('it');

export default {
  name: "PageContractData",
  components: {
    LmsBanner, BreadcrumbsCustom, Tooltip
  },
  data () {
    return {
      url: window.location.origin + "/buonodombff/api/v1/allegato-buono",
      idRichiesta: null,
      loading: false,
      isCancelled: false,
      rendicontazioni: [],
      fornitoriList: null,
      selectedDocument: null,
      documentiSalvati: {
        title: 'Modifica documenti',
        text: 'Fino alla scadenza sarÃ  possibile modificare i documenti; dopo tale data i documenti salvati verranno trasmessi per la validazione e non saranno piÃ¹ modificabili'
      },
      gruppiPeriodoFine: []
    }
  },
  filters: {
    formatDate: function (date) {
      return date ? moment(date).format("DD/MM/YYYY") : null;
    },
    tipologia: function (item) {
      return item ? item.replace(/_/g, " ") : null;
    },

  },
  methods: {
    async getFornitori () {
      try {
        let { data } = await getFornitori(this.idRichiesta)
        this.fornitoriList = data
      } catch (err) {
        snackbarError(err)
      }
    },
    filtredList (id, list) {
      return list ? list.filter((item) => item.id == id) : null
    },
    stateDocument (state) {
      switch (state) {
        case 'DA_INVIARE':
          return 'primary';
        case 'RESPINTO':
          return 'error';
        case 'INVIATO':
          return 'success';
        default:
          return 'primary';
      }
    },
    formatDateEnd (date) {
      return date ? moment(date).format("DD MMMM YYYY") : null;
    },
    endingPeriodArray (period) {
      // Ordina l'array in base all'id in modo decrescente
      period.sort((a, b) => b.id - a.id);

      // Raggruppa gli oggetti in base al campo "periodo_fine"
      const gruppi = {};
      for (const oggetto of period) {
        const periodoFine = oggetto.periodo_fine;
        if (!gruppi[periodoFine]) {
          gruppi[periodoFine] = [];
        }
        gruppi[periodoFine].push(oggetto);
      }

      // Ordina i gruppi in base alla chiave "periodo_fine" in modo decrescente
      const gruppiOrdinati = Object.keys(gruppi)
        .sort((a, b) => new Date(b) - new Date(a))
        .reduce((obj, key) => {
          obj[key] = gruppi[key];
          return obj;
        }, {});

      this.gruppiPeriodoFine = gruppiOrdinati;
    },


    async openApplication (id, route) {
      this.$router.push({ name: route, params: { id: id } });
    },
    openModal (item) {
      this.isCancelled = true
      this.selectedDocument = item
    },
    convertiMesi (mesi) {
      let stringa = ""
      for (let i = 0;i < mesi.length;i++) {
        stringa += moment(mesi[i]).format('MMMM YYYY') + (mesi.length !== 1 && i !== mesi.length - 1 ? ', ' : '')
      }
      return stringa
    },
    async eliminaRendicontazione () {
      this.loading = true;
      await store.dispatch("setSpinner", true);
      this.isCancelled = false
      try {
        let { data } = await deleteRendicontazione(
          this.idRichiesta,
          this.selectedDocument.id,
          this.selectedDocument
        );
        await store.dispatch("setSpinner", false);
        this.$q.notify({
          type: "positive",
          message: "Documento spesa eliminato",
        });
        this.selectedDocument = null
        try {
          let { data } = await getRendicontazione(this.idRichiesta);
          this.rendicontazioni = data;
          this.endingPeriodArray(this.rendicontazioni);
          this.loading = false;
          await store.dispatch("setSpinner", false);
        } catch (err) {
          this.loading = false;
          snackbarError(err);
          await store.dispatch("setSpinner", false);
        }
      } catch (err) {
        await store.dispatch("setSpinner", false);
        snackbarError(err);
        this.selectedDocument = null
      }
    },
    annulla () {
      this.isCancelled = false;
      this.selectedDocument = null
    },
  },
  computed: {
    utenteDestinatario () {
      return store.getters["getUtenteDestinatario"];
    },
  },
  async created () {
    this.loading = true;
    await store.dispatch("setSpinner", true);
    window.scrollTo(0, 0);

    this.idRichiesta = this.$route.params.id
      ? this.$route.params.id
      : null;

    if (this.utenteDestinatario == null) {
      this.$router.push("/");
    } else {
      try {
        let { data } = await getRendicontazione(this.idRichiesta);
        this.rendicontazioni = data;
        this.endingPeriodArray(this.rendicontazioni);
        this.loading = false;
        await store.dispatch("setSpinner", false);
      } catch (err) {
        this.loading = false;
        snackbarError(err);
        await store.dispatch("setSpinner", false);
      }
      await this.getFornitori(this.idRichiesta)
    }
  },
};
</script>

/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <lms-page>
    <template v-if="!loading && richiesta?.destinatario">
      <BreadcrumbsCustom
        :name="
          richiesta.destinatario.nome + ' ' + richiesta.destinatario.cognome
        "
        :path1="'gestione'"
        :label1="'Gestione'"
        :id="idRichiesta"
      ></BreadcrumbsCustom>
      <h2 class="q-mb-md q-mt-md" no-back>
        Buono domiciliaritÃ  di {{ utenteRichiedente.nome }}
        {{ utenteRichiedente.cognome }}
      </h2>
      <!-- CARD DETTAGLI DOMANDA -->
      <div class="q-pt-xl">
        <q-card class="q-mt-xl q-mb-xl q-card--background">
          <q-card-section>
            <div class="row justify-between items-end">
              <div class="col-12 col-md-6">
                <h3 class="title-ico buono-dom">
                  RICHIESTA BUONO DOMICILIARITA'
                </h3>
                <dl
                  :title="'Richiesta Buono' + richiesta.numero"
                  class="info-grid"
                >
                  <dt>Destinatario:</dt>
                  <dd>
                    {{
                      richiesta.destinatario.nome +
                      " " +
                      richiesta.destinatario.cognome
                    }}
                  </dd>
                </dl>
              </div>
              <div class="col-12 col-md-6">
                <dl
                  :title="'Richiesta Buono' + richiesta.numero"
                  class="info-grid"
                >
                  <dt>Richiesta numero:</dt>
                  <dd>{{ richiesta.numero }}</dd>
                </dl>
              </div>
              <div
                v-if="richiesta.protocollo && richiesta.protocollo.numero"
                class="col-12 q-mt-md"
              >
                <dl
                  :title="'Richiesta Buono' + richiesta.numero"
                  class="info-grid"
                >
                  <dt>Protocollata:</dt>
                  <dd>
                    in data {{ richiesta.protocollo.data | formatDate }} -
                    numero {{ richiesta.protocollo.numero }}
                  </dd>
                </dl>
              </div>
            </div>
            <div class="row justify-between items-end q-mt-md">
              <div class="col-md-6">
                <dl :title="'Richiesta Buono' + richiesta.numero">
                  <dt>Stato:</dt>
                  <dd
                    :class="
                      'richiesta-stato richiesta-stato--' +
                      richiesta.stato.toLowerCase()
                    "
                    v-for="(item, i) of statiLista"
                    :key="i"
                  >
                    <template v-if="item.codice == richiesta.stato">{{
                      item.etichetta
                    }}</template>
                  </dd>
                </dl>
              </div>
              <div class="col-12 col-md">
                <dl :title="'Richiesta Buono' + richiesta.numero">
                  <dt>Data aggiornamento:</dt>
                  <dd>{{ richiesta.data_aggiornamento | formatDate }}</dd>
                </dl>
              </div>
            </div>
          </q-card-section>
        </q-card>
      </div>
      <!-- FINE CARD DETTAGLI DOMANDA -->

      <!-- CARD VERIFICA DATI CONTRATTO -->
      <q-card class="q-mt-xl q-mb-xl">
        <q-card-section>
          <div class="row justify-between items-end">
            <h2 class="col-12">
              Cosa devi fare per ottenere l'erogazione del Buono?
            </h2>
            <div class="row q-pa-xs q-mt-md items-start">
              <div class="col-auto q-pr-md">
                <img src="../../public/img/gestione/computer.svg" alt="" />
              </div>
              <div class="col-12 col-md">
                <h3>
                  VERIFICA LA CORRETTEZZA DEI DATI CONTRATTO PER L'ASSISTENTE
                  FAMILIARE O PER L'EDUCATORE
                </h3>
                <p>
                  Se hai cambiato assistente familiare, hai modificato il
                  contratto o ne hai stipulato un altro della durata di almeno
                  12 mesi, aggiorna i tuoi dati.
                </p>
              </div>
              <div class="col-12">
                <lms-banner type="warning">
                  <h2>Attenzione</h2>
                  <p class="q-mt-md">
                    Per ottenere lâerogazione del Buono Ã¨ necessario che i dati
                    dichiarati corrispondano a quelli presenti nei documenti di
                    spesa allegati in fase di rendicontazione
                  </p>
                </lms-banner>
              </div>
              <div class="col-12 q-mt-xl card-section--btn text-right">
                <q-btn
                  @click="openApplication(richiesta.numero, 'daticontratto')"
                  color="primary"
                  :aria-label="
                    'vai ai dati contrattuali domanda numero: ' +
                    richiesta.numero
                  "
                  label="VAI AI DATI CONTRATTUALI"
                ></q-btn>
              </div>
            </div>
          </div>
        </q-card-section>
      </q-card>
      <!-- FINE CARD VERIFICA DATI CONTRATTO -->

      <!-- CARD DOCUMENTI RENDICONTAZIONE -->
      <q-card class="q-mt-xl q-mb-xl">
        <q-card-section>
          <div class="row justify-between items-end">
            <h2 class="col-12">
              Cosa devi fare per ottenere l'erogazione del Buono?
            </h2>
            <div class="row q-pa-xs q-mt-md items-start">
              <div class="col-auto q-pr-md">
                <img src="../../public/img/gestione/email.svg" alt="" />
              </div>
              <div class="col-12 col-md">
                <h3>
                  INVIA I DOCUMENTI CHE CERTIFICANO LE SPESE CHE HAI SOSTENUTO
                </h3>
                <p>
                  Allega e invia le ricevute dei pagamenti per certificare le
                  spese che hai sostenuto mensilmente.
                </p>
              </div>
              <div class="col-12">
                <lms-banner type="warning">
                  <h2>Attenzione</h2>
                  <p class="q-mt-md">
                    La rendicontazione delle spese Ã¨
                    <strong>obbligatoria</strong> per ottenere l'erogazione del
                    Buono
                  </p>
                  <!-- <p>
                    Comincia a rendicontare entro il <strong v-if="closestMonth" class="text-uppercase">{{ closestMonth }}</strong> per non
                    far decadere la validitÃ  della tua richiesta
                  </p> -->
                </lms-banner>
              </div>
              <div class="col-12 q-mt-xl card-section--btn text-right">
                <q-btn
                  @click="
                    openApplication(richiesta.numero, 'documentazionespese')
                  "
                  color="primary"
                  :aria-label="
                    'vai alla documentazione domanda numero: ' +
                    richiesta.numero
                  "
                  label="VAI ALLA DOCUMENTAZIONE DELLE SPESE"
                ></q-btn>
              </div>
            </div>
          </div>
        </q-card-section>
      </q-card>
      <!-- FINE CARD DOCUMENTI RENDICONTAZIONE -->

      <!-- CARD CALENDARIO MENSILE -->
      <q-card class="q-mt-xl q-mb-xl">
        <q-card-section>
          <div class="row q-pa-xs q-mt-md items-start">
            <div class="col-12">
              <h3>Hai dei periodi in cui non hai usufruito di assistenza?</h3>
              <h4>
                Puoi indicare fino a due mesi in cui non hai usufruito di alcuna
                assistenza e per i quali non sei tenuto a dichiarare le spese
              </h4>
              <p>
                In questo modo non perderai il diritto all'erogazione del Buono
                per tutti gli altri mesi
              </p>
            </div>

            <div class="col-12 q-mt-xl text-right">
              <q-btn
                @click="openApplication(richiesta.numero, 'calendario')"
                color="primary"
                :aria-label="
                  'vai alla pagina per selezionare i mesi in cui non hai usufruto di assistenza per la pratica: ' +
                  richiesta.numero
                "
                label="VAI AL CALENDARIO MENSILE"
              ></q-btn>
            </div>
          </div>
        </q-card-section>
      </q-card>
      <!-- FINE CARD CALENDARIO MENSILE -->

      <!-- CARD IBAN -->
      <q-card class="q-mt-xl q-mb-xl">
        <q-card-section>
          <div class="row q-pa-xs q-mt-md items-start">
            <div class="col-12">
              <h3>Devi cambiare i dati di accredito bancari?</h3>
              <p>
                Se i dati qui riportati non sono corretti, puoi modificarli e
                aggiornarli.
              </p>
              <div class="row">
                <div class="col-6">
                  <strong>IBAN:</strong>
                  {{ accredito.iban ? accredito.iban : "-" }}
                </div>
                <div class="col-6">
                  <strong>Intestario:</strong>
                  {{ accredito.intestatario ? accredito.intestatario : "-" }}
                </div>
              </div>
            </div>

            <div class="col-12 q-mt-xl text-right">
              <q-btn
                @click="openModalBanking(accredito)"
                color="primary"
                :aria-label="
                  'modifica dati di accredito bancario' + richiesta.numero
                "
                label="MODIFICA ACCREDITO"
              ></q-btn>
            </div>
          </div>
        </q-card-section>
      </q-card>
      <!-- FINE CARD IBAN -->

      <!-- CARD RINUNCIA -->
      <q-card class="q-mt-xl q-mb-xl">
        <q-card-section v-if="cronologiaBuonoFlag">
          <div class="row q-pa-xs q-mt-md items-start">
            <div class="col-12 col-md flex-column-col">
              <div>
                <h3>Hai giÃ  rinuciato al buono</h3>
                <p>
                  Hai rinunciato al <b>Buono DomiciliaritÃ </b> con decorrenza
                  <b>{{ dataAggiornamento | formatDate }}</b>
                </p>
              </div>
            </div>
            <div class="col-auto q-pr-md">
              <img src="../../public/img/gestione/rinuncia.svg" alt="" />
            </div>
          </div>
        </q-card-section>
        <q-card-section v-else>
          <div class="row q-pa-xs q-mt-md items-start">
            <div class="col-12 col-md flex-column-col">
              <div>
                <h3>Rinuncia al buono</h3>
                <p>
                  Non hai piÃ¹ i requisiti per avere diritto all'erogazione del
                  Buono?
                </p>
              </div>
            </div>
            <div class="col-auto q-pr-md">
              <img src="../../public/img/gestione/rinuncia.svg" alt="" />
            </div>
            <div class="col-12 q-mt-xl text-right">
              <q-btn
                @click="openModalRenounce(richiesta)"
                color="negative"
                :aria-label="'rinuncia alla buono numero: ' + richiesta.numero"
                label="RINUNCIA BUONO "
              ></q-btn>
            </div>
          </div>
        </q-card-section>
      </q-card>
      <!-- FINE CARD RINUNCIA -->

      <!-- MODALE RINUNCIA -->
      <template v-if="openRenounceModal">
        <renunciation-modal
          :openModal="openRenounceModal"
          :applicationModal="applicationModal"
          typeModal="buono"
          @renounceApplication="renounceApplication($event)"
        ></renunciation-modal>
      </template>
      <!-- FINE MODALE RINUNCIA -->

      <!-- MODALE IBAN -->
      <template v-if="openBankingModal">
        <banking-modal
          :openModal="openBankingModal"
          :bankingModal="bankingModal"
          @changeBankingData="changeBankingData($event)"
        ></banking-modal>
        <!-- FINE MODALE IBAN -->
      </template>
    </template>
  </lms-page>
</template>

<script>
import moment from "moment"
import store from "src/store/index"
import { getRichiesta, getCronologiaBuono, postCronologiaBuono, getAccredito, postAccredito } from "src/services/api"
import { snackbarError } from "src/services/utils"
import LmsBanner from "components/core/LmsBanner"
import BreadcrumbsCustom from "src/components/reporting/BreadcrumbsCustom.vue"
import RenunciationModal from "components/RenunciationModal"
import BankingModal from "components/BankingModal"


export default {
  name: "PagePersonalArea",
  components: {
    LmsBanner,
    RenunciationModal,
    BankingModal,
    BreadcrumbsCustom,
    BankingModal,
  },
  data () {
    return {
      application: null,
      idRichiesta: null,
      cronologiaBuonoFlag: null,
      dataAggiornamento: null,
      loading: false,
      renounceOpen: false,
      bankingOpen: false,
      richiesta: null,
      accredito: {
        iban: null,
        intestatario: null
      },
      dateArray: [],
      closestMonth: null,
      url: window.location.origin + "/buonodombff/api/v1/allegato",
    }
  },
  filters: {
    formatDate: function (date) {
      return date ? moment(date).format("DD/MM/YYYY") : null
    },
  },
  methods: {
    async openApplication (id, route) {
      this.$router.push({ name: route, params: { id: id } })
    },
    openModalRenounce (application) {
      this.renounceOpen = true
      this.application = application
    },
    openModalBanking (application) {
      this.bankingOpen = true
      this.application = application
    },
    renounceApplication (value) {
      this.renounceOpen = false
      this.application = null
      if (value) {
        let payload = {
          stato: 'RINUNCIATO',
          decorrenza: value.date
        }
        this.settaStato(payload)
        this.application = null
      }
    },
    async changeBankingData (value) {
      this.bankingOpen = false
      if (value) {
        try {
          await postAccredito(this.idRichiesta, value)
          this.loading = false
          await store.dispatch("setSpinner", false)
          this.$q.notify({
            type: "positive",
            message: "Dati modificati",
          })
          this.accredito = value
        } catch (err) {
          await store.dispatch("setSpinner", false)
          this.loading = false
          snackbarError(err)
        }

      }
    },
    async settaStato (payload) {
      await store.dispatch("setSpinner", true)
      this.loading = true
      try {
        await postCronologiaBuono(this.richiesta.numero, payload)
        this.loading = false
        await store.dispatch("setSpinner", false)
        this.$q.notify({
          type: "positive",
          message: "Buono rinunciato",
        })
        this.$router.push("/")
      } catch (err) {
        await store.dispatch("setSpinner", false)
        this.loading = false
        snackbarError(err)
      }
    },
    // inizio funzione per trovare la data nell'array partendo dalla data odierna
    async getDateMonthActive () {
      // Ottieni la data odierna usando Moment.js
      const today = moment()
      // Imposta la data di partenza ad agosto 2023 (mese 8) per costruire l'array
      const startYear = 2023
      const startMonth = 8
      const numberOfMonths = 25 // Costruisci l'array per i successivi 25 mesi (ogni 3 mesi)

      this.createArrayActive(startMonth, startYear, numberOfMonths)

      // Trova il mese piÃ¹ vicino alla data odierna tra le date future
      this.closestMonth = this.dateArray.filter(date => date.isAfter(today)).reduce((acc, date) => (Math.abs(date.diff(today, 'days')) < Math.abs(acc.diff(today, 'days')) ? date : acc)).lang('it').format('DD MMMM YYYY');

    },
    getFirstDayOfMonth (year, month) {
      return moment({ year, month: month - 1, day: 1 })
    },
    createArrayActive (currentMonth, currentYear, numberOfMonths) {

      for (let i = 0;i < numberOfMonths;i++) {
        const currentDate = this.getFirstDayOfMonth(currentYear, currentMonth)
        this.dateArray.push(currentDate)
        currentMonth += 3
        if (currentMonth > 12) {
          currentMonth -= 12
          currentYear += 1
        }
      }
    },

    async getAccredito (id) {
      try {
        let { data } = await getAccredito(id)
        this.accredito = data
        this.loading = false
      } catch (err) {
        this.loading = false
        snackbarError(err)
        if (err.response.status === 404) {
          this.$router.push("/");
        }
        await store.dispatch("setSpinner", false)
      }
    },
    async getRichiesta (id) {
      try {
        let { data } = await getRichiesta(id)
        this.richiesta = data
        await store.dispatch("setUtenteDestinatario", data.destinatario)
        await this.getDateMonthActive();
        this.loading = false
      } catch (err) {
        this.loading = false
        snackbarError(err)
        await store.dispatch("setSpinner", false)
      }
    },
    async getCronologiaBuono (id) {
      try {
        let { data } = await getCronologiaBuono(id)
        for (let item of data) {
          console.log(item.stato.stato)
          if (item.stato.stato == 'RINUNCIATO') {
            this.cronologiaBuonoFlag = true
            this.dataAggiornamento = item.stato.decorrenza
          }
        }
      } catch (err) {
        snackbarError(err)
        await store.dispatch("setSpinner", false)
      }
    },
  },
  computed: {
    utenteRichiedente () {
      return store.getters["getUtenteRichiedente"]
    },
    statiLista () {
      return store.state.stati_domanda
    },
    openRenounceModal () {
      return this.renounceOpen
    },
    openBankingModal () {
      return this.bankingOpen;
    },
    applicationModal () {
      return this.application
    },
    bankingModal () {
      return this.accredito
    }
  },
  async created () {

    this.loading = true
    await store.dispatch("setSpinner", true)
    window.scrollTo(0, 0)

    this.idRichiesta = this.$route.params.id ? this.$route.params.id : null

    await this.getRichiesta(this.idRichiesta)
    await this.getAccredito(this.idRichiesta)
    await this.getCronologiaBuono(this.idRichiesta)
    await store.dispatch("setSpinner", false)
  },
}
</script>

<style lang="sass"></style>

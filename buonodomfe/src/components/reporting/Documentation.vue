/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <div>
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
      <section class="q-mb-lg page--calendar page--calendar-grid page--calendar-documentation">

        <!-- MESI -->

        <lms-banner type="info" v-if="periodo_rendicontazione">
          <h3>Informazione</h3>
          <p>
            Il tuo buono potrÃ  essere erogato per i seguenti mesi: da <span class="text-uppercase text-bold">{{
              periodo_rendicontazione.fruizione.data_inizio | formaDatePage }}</span> a <span
              class="text-uppercase text-bold">{{ periodo_rendicontazione.fruizione.data_fine | formaDatePage }}</span>.
          </p>
          <p v-if="periodo_rendicontazione.sabbatici">
            Hai dichiarato la sospensione del buono per : <span v-for="(periodo, i) in periodo_rendicontazione.sabbatici"
              :key="i" class="text-uppercase text-bold"
              :class="periodo_rendicontazione.sabbatici.length > 1 ? 'q-pr-sm' : ''">{{ periodo | formaDatePage }}</span>
          </p>
          <p>I mesi che non si possono selezionare sono quelli sospesi selezionati nella pagina Calendario o che non
            possono
            essere rendicontati.</p>
        </lms-banner>
        <template v-if="periodo_fruizione">
          <lms-banner type="warning"
            v-if="getDateRangeOptions.length === 0 || getDateRangeOptions.every(item => item.disable === true)">
            <h3>Informazione</h3>
            <p>
              Non sono disponibili mesi rendicontabili
            </p>
          </lms-banner>
        </template>
        <h2 class="q-mb-lg" v-if="periodo_fruizione.length > 0">Elenco dei mesi per la fruizione del buono</h2>
        <div class="row row--timelapse q-col-gutter-x-xs" v-if="periodo_fruizione.length > 0">
          <div v-for="(fruizione, index) in periodo_fruizione" :key="index" class="col-auto" aria-hidden="true">
            <button class="cicle-timelapse" aria-hidden="true"
              :class="fruizione.disable ? 'circle--disabled' : 'circle--primary'"
              :aria-label="!fruizione.disable ? 'E\' possibile usufruire del buono per il mese ' + fruizione.label : 'Non Ã¨ possibile usufruire del buono per il mese' + fruizione.label">
              <q-tooltip>
                {{ fruizione.label }}
              </q-tooltip>
            </button>

          </div>
        </div>
        <div class="row row--timelapse-second" v-if="periodo_rendicontazione">
          <div class="col col-timelapse--first">
            <span class="sr-only">Periodo di inizio della rendicontazione: </span>
            {{ periodo_rendicontazione.fruizione.data_inizio | formaDatePage }}
          </div>
          <div class="col col-timelapse--last">
            <span class="sr-only">Periodo di fine della rendicontazione: </span>
            {{ periodo_rendicontazione.fruizione.data_fine | formaDatePage }}
          </div>
        </div>
        <lms-banner type="warning">
          <h3>Informazione</h3>
          <p>
            Seleziona il mese che intendi rendicontare, quindi carica i documenti richiesti nelle due sezioni sottostanti
            (<strong>1 documento nella sezione A</strong> e <strong>1 documento nella sezione B</strong>).
          </p>
          <p>
            Leggi a fianco di ogni sezione la <strong>tipologia</strong> di documenti da caricare
          </p>
        </lms-banner>
        <!--calendario ne passano solo le prime 3-->
        <div class="q-gutter-sm check-radio--month" v-if="getDateRangeOptions.length > 0">
          <div class="row q-gutter-y-lg q-gutter-x-none">
            <div class="check-radio--month--col">
              <div class="check-radio--month--cell">
                <h2>Seleziona il mese per la rendicontazione*</h2>
                <p class="text--error q-mb-lg">
                  Il campo Ã¨ obbligatorio
                </p>
                <q-option-group v-model="rendicontazione.mese" type="radio" :options="getDateRangeOptions"
                  class="q-gutter-x-none" />
              </div>
            </div>
          </div>
        </div>
        <!-- FINE MESI -->
      </section>

      <!-- DOCUMENTAZIONE -->
      <div class="q-mb-lg">
        <h2>Sezione A - Bonifici stipendi / fatture</h2>
        <p class="text--error">
          Tutti i campi contrassegnati con * sono obbligatori
        </p>
      </div>
      <div v-if="fornitori_options && fornitori_options.length > 0 && spinner == false
        ">

        <div class="border-left--primary q-mt-md">
          <div class="row q-gutter-y-lg date-start-end q-mb-md">
            <div class="col-12 col-lg-8">
              <!-- GIUSTIFICATIVO -->
              <div class="row q-gutter-y-lg q-gutter-x-lg date-start-end q-mb-md">
                <div class="col-10">
                  <label for="selectFornitore">
                    *Assistente Familiare, Cooperativa o Educatore
                  </label>
                  <div class="q-gutter-md">
                    <q-select standard aria-label="*Assistente Familiare, Cooperativa o Educatore"
                      v-model="rendicontazione.fornitore" :options="fornitori_options"
                      :disable="fornitori_options.length == 1" id="selectFornitore">
                      <template v-slot:selected-item="scope">
                        {{ scope.opt.nome ? scope.opt.nome : "" }}
                        {{ scope.opt.cognome ? scope.opt.cognome : "" }}
                        {{
                          scope.opt.denominazione
                          ? scope.opt.denominazione
                          : ""
                        }}
                        {{ scope.opt.cf ? "(" + scope.opt.cf + ")" : "" }}
                        {{ scope.opt.piva ? "(" + scope.opt.piva + ")" : "" }}

                      </template>
                      <template v-slot:option="scope">
                        <q-item v-bind="scope.itemProps" v-on="scope.itemEvents">
                          <q-item-section>
                            <q-item-label caption>
                              {{ scope.opt.nome ? scope.opt.nome : "" }}
                              {{ scope.opt.cognome ? scope.opt.cognome : "" }}
                              {{
                                scope.opt.denominazione
                                ? scope.opt.denominazione
                                : ""
                              }}
                              {{ scope.opt.cf ? "(" + scope.opt.cf + ")" : "" }}
                              {{
                                scope.opt.piva ? "(" + scope.opt.piva + ")" : ""
                              }}
                            </q-item-label>
                          </q-item-section>
                        </q-item>
                      </template>
                    </q-select>
                    <p>
                      Se devi indicare un nuovo soggetto diverso da quello nel contratto che hai giÃ  caricato, aggiorna i
                      <q-btn @click="openApplication(idRichiesta, 'daticontratto')" flat color="primary"
                        class="btn--openapplication" :aria-label="'dati contrattuali: '"
                        label="Dati contrattuali"></q-btn>
                    </p>
                  </div>
                </div>

              </div>
              <div class="row q-gutter-y-lg q-gutter-x-lg date-start-end q-mb-md">
                <div class="col-8 col-lg-5">
                  <label id="tipologia-select">
                    *Tipologia
                  </label>
                  <div>
                    <lms-select v-model="rendicontazione.tipologia" :options="tipologia_options"
                      :rules="[(val) => val !== null || 'Inserire tipologia']" bottom-slots
                      aria-labelledby="tipologia-select" lazy-rules name="rendicontazione-tipologia-app" no-error-icon
                      required />
                  </div>
                </div>
                <div class="col-8 col-lg-5">
                  <label for="rendicontazioneNumero">
                    *Numero
                  </label>
                  <div>
                    <q-input v-model="rendicontazione.numero" id="rendicontazioneNumero"></q-input>
                  </div>
                </div>
              </div>
              <div class="row q-gutter-y-lg q-gutter-x-lg date-start-end">
                <div class="col-8 col-lg-5">
                  <label for="dataGiustificativo">*Data</label>
                  <q-input id="dataGiustificativo" v-model="rendicontazione.data_giustificativo" type="date"
                    min="1970-01-01" max="2099-12-31" @blur="$v.rendicontazione.data_giustificativo.$touch"
                    :error="$v.rendicontazione.data_giustificativo.$error">
                    <template v-slot:error>
                      La data Ã¨ obbligatoria, non puÃ² essere una data futura
                    </template>
                  </q-input>
                </div>

                <div class="col-8 col-lg-5">
                  <label for="importoGiustificativo">*Importo</label>
                  <q-input type="number" step="0.01" max="9999" id="importoGiustificativo"
                    v-model="rendicontazione.importo_giustificativo" @input="handleInputDecimal('importo_giustificativo')"
                    @blur="$v.rendicontazione.importo_giustificativo.$touch"
                    :error="$v.rendicontazione.importo_giustificativo.$error" suffix="â¬">
                    <template v-slot:error>
                      Il valore immesso deve essere compreso tra 0,01 e 9999
                    </template>
                  </q-input>
                </div>
              </div>
              <div class="row q-my-md q-gutter-y-lg">
                <div class="col-11">
                  <h3 class="flex hidden">
                    *Copia giustificativo di spesa
                    <!-- #### <tooltip
                :titleBanner="copiaGiustificativo.title"
                :textBanner="copiaGiustificativo.text"
              /> -->
                  </h3>
                  <div class="text-right">
                    <q-file ref="GIUSTIFICATIVO" outlined clearable v-model="rendicontazione.GIUSTIFICATIVO"
                      label="Giustificativo di spesa" class="q-mb-xl hidden" :accept="fileAccepted"
                      :max-file-size="fileSize" :filter="checkFileSize" @rejected="onRejected"></q-file>

                    <q-btn v-if="rendicontazione.GIUSTIFICATIVO == null" @click="caricaDocumento('GIUSTIFICATIVO')"
                      color="primary" label="*ALLEGA" class="btn--custom"
                      aria-label="allega il giustificativo spesa"></q-btn>
                  </div>

                  <div class="q-my-lg" v-if="rendicontazione.GIUSTIFICATIVO">
                    <q-btn icon="delete" class="q-mr-md" flat color="negative" title="elimina il documento"
                      @click="rendicontazione.GIUSTIFICATIVO = null"
                      :aria-label="'elimina il ' + rendicontazione.GIUSTIFICATIVO.name" />
                    {{ rendicontazione.GIUSTIFICATIVO.name }}
                  </div>
                </div>
              </div>
              <!-- FINE GIUSTIFICATIVO -->
            </div>
            <div class="col-12 col-lg-4">
              <aside>
                <lms-banner type="info" class="q-mb-xl" :icon="false">
                  <h3>Che cosa devo allegare</h3>
                  <p>
                    <strong>
                      Se ho Contratto di lavoro subordinato:
                    </strong>
                  <ul>
                    <li>
                      comprova del pagamento della retribuzione mensile spettante al lavoratore
                    </li>
                  </ul>

                  </p>
                  <p>
                    <strong>
                      Se ho Contratto stipulato con libero professionista:
                    </strong>
                  <ul>
                    <li>
                      fattura emessa dall'assistente familiare riportante: soggetto prestante il servizio, soggetto
                      fruitore
                      del servizio, luogo della prestazione e numero di ore erogate.
                    </li>
                  </ul>

                  </p>
                  <p>
                    <strong>
                      Se ho Contratto di prestazione con cooperativa, agenzia di somministrazione o altro soggetto
                      giuridico:
                    </strong>
                  <ul>
                    <li>
                      fattura emessa dalla cooperativa/agenzia/altro soggetto
                    </li>
                  </ul>


                  </p>
                </lms-banner>
              </aside>
            </div>
          </div>


        </div>
        <div class="q-mb-lg">
          <h2>
            Sezione B - Contributi INPS / pagamenti fatture
          </h2>
          <p class="text--error">
            Tutti i campi contrassegnati con * sono obbligatori
          </p>
        </div>

        <div class="border-left--primary q-mt-xxl">
          <div class="row q-gutter-y-lg date-start-end q-mt-md">
            <div class="col-12 col-lg-8">
              <!-- QUIETANZA -->
              <div class="row q-gutter-y-lg q-gutter-x-lg date-start-end q-mt-md">
                <div class="col-8 col-lg-5">
                  <label for="dataQuietanza">*Data</label>
                  <q-input id="dataQuietanza" v-model="rendicontazione.data_quietanza" type="date" min="1970-01-01"
                    max="2099-12-31" @blur="$v.rendicontazione.data_quietanza.$touch"
                    :error="$v.rendicontazione.data_quietanza.$error">
                    <template v-slot:error>
                      La data Ã¨ obbligatoria, non puÃ² essere una data futura
                    </template>
                  </q-input>
                </div>

                <div class="col-8 col-lg-5">
                  <label for="importoQuietanza">*Importo</label>
                  <q-input type="number" step="0.01" max="9999" v-model="rendicontazione.importo_quietanza"
                    @input="handleInputDecimal('importo_quietanza')" @blur="$v.rendicontazione.importo_quietanza.$touch"
                    :error="$v.rendicontazione.importo_quietanza.$error" suffix="â¬">
                    <template v-slot:error>
                      Il valore immesso deve essere compreso tra 0,01 e 9999
                    </template>
                  </q-input>
                </div>
              </div>
              <div class="row q-my-md q-gutter-y-lg">
                <div class="col-11">
                  <h3 class="flex hidden">
                    *Copia quietanza di pagamento
                    <!-- #### <tooltip
                  :titleBanner="copiaQuietanza.title"
                  :textBanner="copiaQuietanza.text"
                /> -->
                  </h3>
                  <div class="text-right">
                    <q-file ref="QUIETANZA" outlined clearable v-model="rendicontazione.QUIETANZA"
                      label="Quietanza di pagamento" class="q-mb-xl hidden" :accept="fileAccepted"
                      :max-file-size="fileSize" :filter="checkFileSize" @rejected="onRejected"></q-file>

                    <q-btn v-if="rendicontazione.QUIETANZA == null" @click="caricaDocumento('QUIETANZA')" color="primary"
                      label="*ALLEGA" class="btn--custom" aria-label="allega la quietanza di pagamento"></q-btn>
                  </div>

                  <div class="q-my-lg" v-if="rendicontazione.QUIETANZA">
                    <q-btn icon="delete" class="q-mr-md" flat color="negative" title="elimina la quietanza"
                      @click="rendicontazione.QUIETANZA = null" :aria-label="'elimina la quietanza ' + rendicontazione.QUIETANZA.name
                        " />
                    {{ rendicontazione.QUIETANZA.name }}
                  </div>

                </div>
              </div>
              <div class="row q-my-md q-gutter-y-lg">
                <div class="col-11">
                  <a class="text--info"
                    href=""
                    target="_blank"> per info dettagliate sulle modalitÃ  di pagamento ammissibili vedi FAQ n.15</a>
                </div>

              </div>
              <!-- FINE QUIETANZA -->
            </div>
            <div class="col-12 col-lg-4">
              <aside>
                <lms-banner type="info" :icon="false">
                  <h3>Che cosa devo allegare</h3>
                  <p>
                    <strong>
                      Se ho Contratto di lavoro subordinato:
                    </strong>
                  <ul>
                    <li>
                      attestazione del versamento dei contributi previdenziali ed assistenziali (f24 quietanzato).
                    </li>
                  </ul>
                  </p>
                  <p>
                    <strong>
                      Se ho Contratto stipulato con libero professionista:
                    </strong>
                  <ul>
                    <li>
                      comprova dell'avvenuto pagamento
                    </li>
                  </ul>
                  </p>
                  <p>
                    <strong>
                      Se ho Contratto di prestazione con cooperativa, agenzia di somministrazione o altro soggetto
                      giuridico:
                    </strong>
                  <ul>
                    <li>
                      comprova dell'avvenuto pagamento
                    </li>
                  </ul>
                  </p>
                </lms-banner>
              </aside>
            </div>
          </div>
        </div>
        <q-card class="q-mt-lg">
          <q-scroll-area class="height-scroll-area">
            <h2>Dichiarazione di spesa</h2>
            <p>
              Io sottoscritto/a <strong>{{ utenteRichiedente.nome }} {{ utenteRichiedente.cognome }}</strong>
              <br />
              Consapevole delle sanzioni penali, nel caso di dichiarazioni non
              veritiere e falsitÃ  negli atti, richiamate dagli artt. 46, 47, 71, 75,
              76 del D.P.R. 445 del 28/12/2000, dichiaro che la documentazione
              giustificativa attestante la fruizione del servizio di assistenza
              domiciliare attivato per l'assegnazione del âBUONOâ nel mese/i scelto/i
              risulta conforme all'originale conservato e
              disponibile per eventuali controlli.
            </p>
            <p>
              Dichiaro inoltre di avere
              rispettato le condizioni e le modalitÃ  di fruizione del âBuono
              domiciliaritÃ .
            </p>
            <p>
              Richiedo pertanto la liquidazione del controvalore
              economico del âbuonoâ per un importo pari a â¬ 600,00 mensili, con
              riferimento alle mensilitÃ  di
              <template v-if="rendicontazione.mese">
                <span class="text-uppercase text-bold"
                  :class="rendicontazione.mese ? 'q-pr-sm' : ''">{{ rendicontazione.mese| formaDatePage }}</span></template>
              <template v-else><strong class="text--error">SELEZIONA IL MESE</strong></template>
              e ai documenti sopra indicati.
            </p>
          </q-scroll-area>
        </q-card>
      </div>
      <div v-else-if="spinner == false && fornitori_options.length < 1">
        <lms-banner type="negative">
          <h3>Attenzione</h3>
          <p>
            Non risultano fornitori disponibili per procedere con la richiesta,
            contattare assistenza
          </p>
        </lms-banner>
      </div>
      <template v-if="spinner">
        <q-spinner color="primary" size="3em" />
      </template>
    </template>
  </div>
  <!-- FINE DOCUMENTAZIONE-->
</template>

<script>
import { QTooltip } from 'quasar'
import moment from "moment"
import store from "src/store/index"
import LmsSelect from "components/core/LmsSelect"
import { getFornitori } from "src/services/api"
import { getPeriodoRendicontazione } from "src/services/api"
import { snackbarError } from "src/services/utils"
import LmsBanner from "components/core/LmsBanner"

import { required, requiredIf } from "vuelidate/lib/validators"
import { ACCEPTED_FILES_TYPES, MAX_FILE_SIZE } from "src/services/config"

export default {
  name: "ContracData",
  props: {
    idRichiesta: String,
  },
  components: { LmsSelect, LmsBanner },
  data () {
    return {
      spinner: null,
      url: window.location.origin + "/buonodombff/api/v1/allegato",
      titolo_options: store.state.titoli_studio,
      tipologia_options: store.state.tipi_documento_rendicontazione,
      asl_options: store.state.asl,
      contratto_options: store.state.tipi_contratto,
      fornitori_options: [],
      fileSize: MAX_FILE_SIZE,
      fileAccepted: ACCEPTED_FILES_TYPES,
      copiaQuietanza: {
        title: "Copia quietanza",
        text: "Contratto di lavoro subordinato, di durata pari ad almeno 12 mesi, per un minimo di 16 ore settimanali di servizio e che preveda l'inquadramento dell'assistente nei livelli CS o DS stabiliti dal Contratto Collettivo Nazionale di Lavoro sulla disciplina del rapporto di lavoro domestico;",
      },
      copiaGiustificativo: {
        title: "Copia giustificativo",
        text: "Contratto di lavoro subordinato, di durata pari ad almeno 12 mesi, per un minimo di 16 ore settimanali di servizio e che preveda l'inquadramento dell'assistente nei livelli CS o DS stabiliti dal Contratto Collettivo Nazionale di Lavoro sulla disciplina del rapporto di lavoro domestico;",
      },
      rendicontazione: {
        tipologia: null,
        numero: null,
        fornitore: null,
        mese: null,
        GIUSTIFICATIVO: null,
        data_giustificativo: null,
        importo_giustificativo: null,
        QUIETANZA: null,
        data_quietanza: null,
        importo_quietanza: null,
      },
      periodo_rendicontazione: null,
      periodo_fruizione: [],
      periodoFruizioneOptions: [],
      periodoRendicontazioneOptions: [],
    }
  },
  filters: {
    formatDate: function (date) {
      return date ? moment(date).format("DD/MM/YYYY") : null
    },
    formaDatePage: function (date) {
      return date ? moment(date).lang('it').format("MMMM YYYY") : null
    },
  },
  validations: {
    rendicontazione: {
      tipologia: { required },
      numero: { required },
      fornitore: { required },
      mese: {
        required
      },
      GIUSTIFICATIVO: { required },
      data_giustificativo: {
        required,
        minValue (val) {
          return val ? new Date(val) < new Date() : true
        },
      },
      importo_giustificativo: {
        required,
        maxValue (value) {
          return value <= 9999 && value > 0 ? true : false
        },
      },
      QUIETANZA: { required },
      data_quietanza: {
        required,
        minValue (val) {
          return val ? new Date(val) < new Date() : true
        },
      },
      importo_quietanza: {
        required,
        maxValue (value) {
          return value <= 9999 && value > 0 ? true : false
        },
      },
    },
  },
  methods: {
    hasDisableProperty (array) {
      return array.every(item => item.prop === false)
    },
    // metodo per il cambio rotta a nuovocontratto/id_domanda
    async openApplication (id, route) {
      this.$router.push({ name: route, params: { id: id } })
    },
    // serve per troncare i valori dopo la virgola a 2
    //e come valore gli si passa una stringa con il nome dell'input
    handleInputDecimal (inputName) {
      const inputValue = this.rendicontazione[inputName]
      const truncatedValue = this.truncateDecimals(inputValue, 2)
      this.rendicontazione[inputName] = truncatedValue
    },
    truncateDecimals (value, decimalPlaces) {
      const parsedValue = parseFloat(value)
      if (isNaN(parsedValue)) {
        return ''
      }
      return parsedValue.toFixed(decimalPlaces)
    },
    rendicontazioneUtente (nome, cognome) {
      let utente = nome + " " + cognome
      return utente
    },
    caricaDocumento (type) {
      this.$refs[type].pickFiles()
    },
    checkFileSize (files) {
      return files.filter((file) => file.size < MAX_FILE_SIZE)
    },
    onRejected () {
      this.$q.notify({
        type: "negative",
        message: `File non idoneo, controlla tipo e grandezza`,
      })
    },
    formatYearMonth (date, format) {
      return date.lang("it").format(format)
    },

    timelineForm (date) {
      date.forEach((date) => {
        if (this.periodo_rendicontazione.sabbatici) {
          if (this.periodo_rendicontazione.sabbatici.includes(date.value)) {
            date.disable = true
          } else {
            date.disable = false
          }
        } else {
          date.disable = false
        }
      })
      this.periodo_fruizione = date
    },
    async getRendicontazione () {
      try {
        await store.dispatch("setSpinner", true)
        let { data } = await getPeriodoRendicontazione(this.idRichiesta)
        this.periodo_rendicontazione = data
        // creazione degli array di fruizione e rendicontazione
        this.createArrayDate(moment(this.periodo_rendicontazione.fruizione.data_inizio), moment(this.periodo_rendicontazione.fruizione.data_fine),
          this.periodoFruizioneOptions, true)
        this.createArrayDate(moment(this.periodo_rendicontazione.rendicontazione.data_inizio), moment(this.periodo_rendicontazione.rendicontazione.data_fine).subtract(1, 'day'), this.periodoRendicontazioneOptions, false)
        await store.dispatch("setSpinner", false)
      } catch (err) {
        snackbarError(err)
        await store.dispatch("setSpinner", false)
        this.$router.push({ name: 'documentazionespese', params: { id: this.idRichiesta } })
      }
    },
    async getFornitori () {
      try {
        this.spinner = true
        let { data } = await getFornitori(this.idRichiesta)

        this.fornitori_options = data
        if (this.fornitori_options.length == 1) {
          this.rendicontazione.fornitore = this.fornitori_options[0]
        }
        this.spinner = false
      } catch (err) {
        this.spinner = false
        snackbarError(err)
        this.$router.push({ name: 'documentazionespese', params: { id: this.idRichiesta } })
      }
    },
    createArrayDate (arrayStart, arrayEnd, arrayResult, disable) {
      const currentDate = arrayStart
      const lastDate = arrayEnd
      while (currentDate.isSameOrBefore(lastDate)) {
        arrayResult.push({
          value: this.formatYearMonth(currentDate, "YYYY-MM"),
          label: this.formatYearMonth(currentDate, "MMMM YYYY"),
          disable: disable
        })
        currentDate.add(1, "month")
      }
    },
    nonRendicontati (periodoRendicontazioneOptions) {
      // Trova la data minima nel secondo array
      let minDate = periodoRendicontazioneOptions.reduce((min, obj) => (obj.value < min ? obj.value : min), periodoRendicontazioneOptions[0].value)

      // Itera sul primo array e aggiungi le date antecedenti a quella minima nel secondo array
      let newObjects = this.periodo_rendicontazione.non_rendicontati.filter(date => moment(date) < moment(minDate)).map(date => ({
        value: this.formatYearMonth(moment(date), "YYYY-MM"),
        label: this.formatYearMonth(moment(date), "MMMM YYYY"),
        disable: false
      }))
      // Unisci i nuovi oggetti con il secondo array originale
      periodoRendicontazioneOptions = periodoRendicontazioneOptions.concat(newObjects)
      return periodoRendicontazioneOptions
    },
    createtDateRangeOptions (periodoRendicontazioneOptions) {
      if (this.periodo_rendicontazione) {

        this.timelineForm(this.periodoFruizioneOptions)


        // inserimento dei mesi presenti in rendicontazione che possono essere selezionati
        // il criterio Ã¨ che il primo mese di fruizione deve coincidere con il primo mese del calendario e anche se 
        // sono presenti mesi antecedenti in rendicontazione NON devono essere aggiunti all'array globale 
        const mergedArray = []

        for (let i = 0;i < periodoRendicontazioneOptions.length;i++) {
          const obj2 = periodoRendicontazioneOptions[i]
          const matchingObj = this.periodoFruizioneOptions.find(obj1 => obj1.value === obj2.value)

          if (matchingObj) {
            mergedArray.push(matchingObj)
          }
        }

        // merge degli array di fruizione e quello creato in precedenza
        periodoRendicontazioneOptions = [...mergedArray]

        if (this.periodo_rendicontazione.hasOwnProperty('non_rendicontati')) {
          periodoRendicontazioneOptions = this.nonRendicontati(periodoRendicontazioneOptions)
        }

        periodoRendicontazioneOptions = this.sortFilterArray(periodoRendicontazioneOptions)

      }
      return this.sabbathCheck(periodoRendicontazioneOptions)
    },
    sortFilterArray (periodoRendicontazioneOptions) {
      // Ordinare gli elementi in base al value - devono essere trasformati in date altrimenti non funziona
      // periodoRendicontazioneOptions Ã¨ il risultato finale
      periodoRendicontazioneOptions = periodoRendicontazioneOptions.sort((a, b) => new Date(a.value) - new Date(b.value))

      // eliminazione doppioni
      periodoRendicontazioneOptions = periodoRendicontazioneOptions.filter((item, index, self) => {
        return index === self.findIndex((t) => (
          t.value === item.value
        ))
      })

      return periodoRendicontazioneOptions
    },
    sabbathCheck (periodoRendicontazioneOptions) {
      // controllo che esista periodo_rendicontazione.sabbatici 
      if (this.periodo_rendicontazione.sabbatici) {
        periodoRendicontazioneOptions.forEach((oggetto) => {
          const isOptionIncluded = this.periodo_rendicontazione.sabbatici.includes(oggetto.value)
          const isOptionInRend = periodoRendicontazioneOptions.some((item) => item.value === oggetto.value)
          oggetto.disable = isOptionIncluded || (isOptionInRend && !this.periodo_rendicontazione.sabbatici)
        })
      } else {
        periodoRendicontazioneOptions.forEach((oggetto) => {
          oggetto.disable = false
        })
      }
      return periodoRendicontazioneOptions
    }
  },
  computed: {
    getSetspinner () {
      return store.getters["setSpinner"]
    },
    getDateRangeOptions () {
      return this.periodoRendicontazioneOptions.length > 0 ? this.createtDateRangeOptions(this.periodoRendicontazioneOptions) : []
    },
    utenteRichiedente () {
      return store.getters["getUtenteRichiedente"]
    },
    isInvalidValid () {
      return this.$v ? this.$v.$invalid : null
    },
    rendicontazioneCurrent () {
      return this.rendicontazione ? this.rendicontazione : null
    }
  },
  watch: {
    rendicontazioneCurrent: {
      handler () {
        let oggettoRendicotazione = {
          tipologia: this.rendicontazione.tipologia,
          numero: this.rendicontazione.numero,
          id_fornitore: this.rendicontazione.fornitore ? this.rendicontazione.fornitore.id : null,
          dettagli: [
            {
              id_allegato: null,
              tipo: "GIUSTIFICATIVO",
              importo: this.rendicontazione.importo_giustificativo,
              data: this.rendicontazione.data_giustificativo,
            },
            {
              id_allegato: null,
              tipo: "QUIETANZA",
              importo: this.rendicontazione.importo_quietanza,
              data: this.rendicontazione.data_quietanza,
            },
          ],
          mesi: [this.rendicontazione.mese],
          stato: "DA_INVIARE",
          note: null,
        }
        let allegatiRendicontazione = {
          GIUSTIFICATIVO: this.rendicontazione.GIUSTIFICATIVO,
          QUIETANZA: this.rendicontazione.QUIETANZA,
        }
        this.$emit("rendiconta", {
          obj: oggettoRendicotazione,
          files: allegatiRendicontazione,
        })
      },
      deep: true,
    },
    isInvalidValid () {
      this.$emit("validare", this.isInvalidValid)
    },

  },
  async created () {
    await this.getRendicontazione()
    await this.getFornitori()
  },
}
</script>

/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <div>
    <!-- DICHIARAZIONE -->
    <div class="q-mt-xl q-mb-xl">
      <p>
        <strong
          >Consapevole delle sanzioni penali richiamate dall'art. 76 del D.P.R.
          445/2000 nel caso di dichiarazioni non veritiere e falsitÃ  in atti con
          riferimento a quanto dichiarato DICHIARA che, alla data della
          presentazione della domanda:</strong
        >
      </p>
      <p class="text--error">
        Tutti i campi contrassegnati con * sono obbligatori
      </p>
    </div>
    <!-- CARD ISEE -->
    <q-card class="q-mt-xl q-mb-xl">
      <q-card-section>
        <h2>Attestazione <abbr title="Indicatore della Situazione Economica Equivalente">I.S.E.E.</abbr> socio - sanitario</h2>
        <div class="row q-my-xl justify-between">
          <div class="col-auto col-lg-1">
            <q-toggle
              v-model="richiesta.attestazione_isee"
              aria-labelledby="attestazioneIsee"
              id="attestazioneIseeToggle"
              name="attestazioneIseeToggle"
            />
          </div>
          <label class="col col-lg-10" id="attestazioneIsee" for="attestazioneIseeToggle">
            *Il destinatario Ã¨ in possesso di un attestato <abbr title="Indicatore della Situazione Economica Equivalente">I.S.E.E.</abbr> socio - sanitario in corso di validitÃ  avente un valore inferiore <strong>50.000 euro (65.000 euro per minori e disabili)</strong>
          </label>
          <div class="col-1">
            <tooltip
              :titleBanner="iseeTooltip.title"
              :textBanner="iseeTooltip.text"
            />
          </div>
        </div>
      </q-card-section>
    </q-card>
    <!-- REQUISITI COMPATIBILITA'-->
    <q-card class="q-mt-xl q-mb-xl">
      <q-card-section>
        <h2>Requisiti compatibilitÃ </h2>
        <label for="richiestaNoInc"
          >*Seleziona un'opzione di compatibilitÃ </label
        >
        <q-option-group
          id="richiestaNoInc"
          class="q-my-lg"
          :options="optionsRecCompatibility"
          type="radio"
          v-model="richiesta.nessuna_incompatibilita"
        />
      </q-card-section>
      <!-- ELENCO MISURE INCOMPATIBILI -->
      <q-list class="q-my-xl">
        <q-expansion-item
          dense
          dense-toggle
          expand-separator
          label="ELENCO MISURE INCOMPATIBILI con il Buono DomiciliaritÃ "
          header-class="bg-white text-black font-weight:bold"
        >
          <q-card class="border-left--primary">
            <q-card-section>
              <ol class="lower-roman">
                <li>
                  la percezione di "assegni di cura", ex <abbr title="Delibera giunta regionale">D.G.R.</abbr> numero 39-11190, del
                  06/04/2009, e <abbr title="Delibera giunta regionale">D.G.R.</abbr> numero 56-13332, del 15/02/2010;
                </li>
                <li>
                  la percezione di contributi dal Fondo per il sostegno del
                  ruolo di cura e assistenza del caregiver familiare, ex legge
                  numero 205/2017, articolo 1, cc. 254-256;
                </li>
                <li>
                  l'attivazione della misura "Home care premium" gestita da
                  <abbr title="Istituto nazionale di previdenza sociale">INPS</abbr>, qualora preveda l'erogazione di trasferimenti monetari
                  (a titolo di "prestazione prevalente") oppure interventi di
                  assistenza domiciliare per un numero di ore settimanali
                  superiore a 16 (oppure 8 nel caso di assistenza educativa
                  rivolta a minori con disabilitÃ ) erogati a titolo di
                  "prestazione integrativa";
                </li>
                <li>
                  altre misure aventi natura di trasferimento monetario
                  specificatamente destinato al sostegno della domiciliaritÃ , di
                  eventuale futura definizione, a titolaritÃ  regionale o
                  statale;
                </li>
                <li>
                  l'accoglienza definitiva presso strutture residenziali
                  sociosanitarie o sociali.
                </li>
              </ol>
            </q-card-section>
          </q-card>
        </q-expansion-item>
      </q-list>
      <!-- ELENCO MISURE COMPATIBILI -->
      <q-list class="q-my-xl">
        <q-expansion-item
          dense
          dense-toggle
          expand-separator
          label="ELENCO MISURE COMPATIBILI con il Buono DomiciliaritÃ "
          header-class="bg-white text-black font-weight:bold"
        >
          <q-card class="border-left--primary">
            <q-card-section>
              <ol class="lower-roman">
                <li>
                  gli interventi di assistenza domiciliare direttamente erogati
                  dagli Enti Gestori, ovvero i servizi professionali domiciliari
                  resi da operatori sociosanitari ed educatori professionali
                  (non vi rientrano gli interventi di natura professionale
                  sanitaria) garantiti dagli Enti Gestori; tali servizi si
                  intendono come compatibili con la misura di cui al presente
                  Avviso se il destinatario ne beneficia per un massimo di 16
                  ore settimanali;
                </li>
                <li>
                  contributi economici erogati dagli Enti Gestori a sostegno
                  delle famiglie affidatarie in caso di affidamento
                  etero-famigliare di minori con disabilitÃ ;
                </li>
                <li>
                  gli interventi di Assistenza Domiciliare Integrata, gestiti
                  dalle <abbr title="Azienda sanitaria locale">ASL</abbr>;
                </li>
                <li>
                  gli interventi di riabilitazione in regime ambulatoriale o
                  domiciliare;
                </li>
                <li>
                  il ricovero ospedaliero e/o riabilitativo (fino a 60 giorni
                  consecutivi);
                </li>
              </ol>
            </q-card-section>
          </q-card>
        </q-expansion-item>
      </q-list>
      <!-- CARD TIPOLOGIE -->
      <q-card-section class="q-my-xl">
        <strong>*Seleziona tipologia persona</strong>
        <div class="q-my-lg">
          <q-radio
            dense
            v-model="richiesta.valutazione_multidimensionale"
            val="UVG"
            label="Il destinatario Ã¨ persona di etÃ  pari o superiore a 65 anni, non autosufficiente con valutazione multidimensionale U.V.G. - ANZIANI"
          />
        </div>
        <q-radio
          dense
          v-model="richiesta.valutazione_multidimensionale"
          val="UMVD"
          label="Il destinatario Ã¨ persona con disabilitÃ , non autosufficiente con valutazione multidimensionale U.M.V.D. â DISABILI"
        />
      </q-card-section>
    </q-card>
    <!-- CARD PUNTEGGIO SOCIALE -->
    <q-card class="q-mt-xl q-mb-xl">
      <q-card-section>
        <h2>Punteggio bisogno sociale</h2>
        <p>
          Questo dato attesta il grado di <strong>"bisogno sociale"</strong>, e viene assegnato a seguito di <strong>"valutazione multidimensionale"</strong> (<strong><abbr
            title="UnitÃ  Valutativa Geriatrica"
            >U.V.G.</abbr> - ANZIANI</strong> o <strong><abbr title="UnitÃ  multidisciplinare di valutazione della disabilitÃ ">U.M.V.D.</abbr> DISABILI</strong>), effettuata presso la ASL di residenza (la visita per la valutazione dev'essere richiesta con impegnativa dal Medico di Famiglia).
        </p>
        <p>
          Il punteggio sociale assegnato puÃ² raggiungere un massimo di <strong>14 punti</strong>, cosÃ¬ suddivisi:
          <ol>
            <li>
              condizione abitativa: (max 2 punti);
            </li>
            <li>
              condizione economica: (max 4 punti);
            </li>
            <li>
              condizione famigliare: (max 4 punti);
            </li>
            <li>
              condizione assistenziale: (max 4 punti).
            </li>
          </ol>
        </p>
        <p>
          *Il destinatario Ã¨ giÃ  stato sottoposto a "<strong
            >valutazione multidimensionale</strong
          >" presso le UnitÃ  di Valutazione competenti (<abbr
            title="UnitÃ  Valutativa Geriatrica"
            >U.V.G.</abbr
          >
          - ANZIANI, o
          <abbr title="UnitÃ  multidisciplinare di valutazione della disabilitÃ "
            >U.M.V.D.</abbr
          >
          - DISABILI), ed Ã¨ in possesso di un punteggio sociale pari a:
        </p>
        <div class="q-mt-xl">
          <div class="row">
            <div class="col-10 col-md-8 col-lg-6">
              <q-input
                outlined
                type="number"
                v-model="richiesta.punteggio_bisogno_sociale"
                @blur="$v.richiesta.punteggio_bisogno_sociale.$touch"
                @keyup.enter="submit"
                :error="$v.richiesta.punteggio_bisogno_sociale.$error"
                :step="1"
                label="Inserisci il punteggio sociale"
              >
              
                <template v-slot:error>
                  Punteggio bisogno sociale deve essere minimo
                  {{
                    $v.richiesta.punteggio_bisogno_sociale.$params.between.min
                  }}, massimo
                  {{
                    $v.richiesta.punteggio_bisogno_sociale.$params.between.max
                  }}
                  e deve essere una cifra senza decimali
                </template>
              </q-input>
            </div>
          </div>
        </div>
        <div class="row q-my-xl">
          <div
            class="col-12 q-pa-xs"
            v-if="richiesta.valutazione_multidimensionale"
          >
            <q-file
              ref="VERBALE_UVG"
              outlined
              clearable
              v-model="allegati.VERBALE_UVG"
              class="q-mb-xl hidden"
              :accept="fileAccepted"
              :max-file-size="fileSize"
              :filter="checkFileSize"
              @rejected="onRejected"
            ></q-file>

            <q-file
              ref="VERBALE_UMVD"
              outlined
              clearable
              v-model="allegati.VERBALE_UMVD"
              class="q-mb-xl hidden"
              :accept="fileAccepted"
              :max-file-size="fileSize"
              :filter="checkFileSize"
              @rejected="onRejected"
            ></q-file>

            <div v-if="richiesta.valutazione_multidimensionale === 'UVG'">
              <!-- PRECEDENTE FILE UVG -->
              <div v-if="precedente_VERBALE_UVG">
                <q-btn
                  icon="delete"
                  class="q-mr-md"
                  flat
                  color="negative"
                  title="elimina il documento"
                  @click="precedente_VERBALE_UVG = null"
                  :aria-label="
                    'elimina il documento ' + precedente_VERBALE_UVG.filename
                  "
                />
                <a
                  :href="
                    url +
                    '/' +
                    richiesta.numero +
                    '/' +
                    precedente_VERBALE_UVG.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_VERBALE_UVG.filename }} </strong>
                </a>
              </div>

              <div v-if="!precedente_VERBALE_UVG && !allegati.VERBALE_UVG">
                <h2 class="q-my-xl">
                  Allega il Verbale o Lettera di Comunicazione della valutazione
                  UVG
                </h2>
                <h3 class="title-card--alert q-mb-xl">
                  ATTENZIONE: <u>NON ALLEGARE</u> il verbale di invaliditÃ  civile rilasciato dall'<abbr title="Istituto nazionale di previdenza sociale">INPS</abbr>
                </h3>
                <q-btn
                  @click="caricaDocumento('VERBALE_UVG')"
                  color="primary"
                  aria-label="allega il verbale o lettera di comunicazione della valutazione uvg"
                  label="*ALLEGA"
                ></q-btn>
              </div>
            </div>

            <div
              v-if="richiesta.valutazione_multidimensionale === 'UMVD'"
              class="q-my-xl"
            >
              <div v-if="precedente_VERBALE_UMVD">
                <q-btn
                  icon="delete"
                  class="q-mr-md"
                  flat
                  title="elimina il documento"
                  color="negative"
                  @click="precedente_VERBALE_UMVD = null"
                  :aria-label="
                    'elimina il documento ' + precedente_VERBALE_UMVD.filename
                  "
                />
                <a
                  :href="
                    url +
                    '/' +
                    richiesta.numero +
                    '/' +
                    precedente_VERBALE_UMVD.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_VERBALE_UMVD.filename }} </strong>
                </a>
              </div>
              <div v-if="!precedente_VERBALE_UMVD && !allegati.VERBALE_UMVD">
                <h2>
                  Allega il Verbale o Lettera di Comunicazione della valutazione
                  UMVD
                </h2>
                <h3 class="title-card--alert q-mb-xl">
                  ATTENZIONE: <u>NON ALLEGARE</u> il verbale di invaliditÃ  civile rilasciato dall'<abbr title="Istituto nazionale di previdenza sociale">INPS</abbr>
                </h3>
                <q-btn
                  @click="caricaDocumento('VERBALE_UMVD')"
                  color="primary"
                  aria-label="allega il verbale o lettera di comunicazione della valutazione umvd"
                  label="*ALLEGA"
                ></q-btn>
              </div>
            </div>
          </div>
        </div>

        <div v-if="allegati.VERBALE_UVG" class="q-my-lg">
          <strong>Lettera di comunicazione/Verbale UVG:</strong
          ><q-btn
            icon="delete"
            class="q-mr-md"
            flat
            color="negative"
            title="elimina il documento"
            @click="allegati.VERBALE_UVG = null"
            :aria-label="'elimina il documento ' + allegati.VERBALE_UVG.name"
          />{{ allegati.VERBALE_UVG.name }}
        </div>
        <div v-if="allegati.VERBALE_UMVD">
          <strong>Lettera di comunicazione/Verbale UMVD:</strong>
          <q-btn
            icon="delete"
            class="q-mr-md"
            flat
            color="negative"
            title="elimina il documento"
            @click="allegati.VERBALE_UMVD = null"
            :aria-label="'elimina il documento ' + allegati.VERBALE_UMVD.name"
          />{{ allegati.VERBALE_UMVD.name }}
        </div>
      </q-card-section>
      
    </q-card>
    <!-- ACCREDITO DEL BUONO -->
    <q-card class="q-mt-xl q-mb-xl">
      <q-card-section>
        <h2>Accredito del buono</h2>
        <p>
          In caso di assegnazione del buono, l'erogazione potrÃ  avvenire
          mediante accredito su conto corrente bancario:
        </p>

        <div class="row q-mb-xl q-gutter-y-lg">
          <div class="col-12 col-md-8">
            <div class="q-ma-xs">
              <q-input
                outlined
                label="*IBAN"
                v-model="richiesta.accredito.iban"
                @blur="$v.richiesta.accredito.iban.$touch"
                :error="$v.richiesta.accredito.iban.$error"
              >
                <template v-slot:error>
                  Campo iban obbligatorio e deve contenere 27
                  caratteri</template
                >
              </q-input>
            </div>
          </div>
          <div class="col-12 col-md-8">
            <div class="q-ma-xs">
              <q-input
                outlined
                v-model="richiesta.accredito.intestatario"
                label="*Intestato a"
                @blur="$v.richiesta.accredito.intestatario.$touch"
                :error="$v.richiesta.accredito.intestatario.$error"
              >
                <template v-slot:error> Campo intestatario iban obbligatorio </template>
              </q-input>
            </div>
          </div>
        </div>
      </q-card-section>
    </q-card>
    <!-- CARD DATI CONTRATTUALI -->
    <q-card
      class="q-mt-xl q-mb-xl"
    >
      <q-card-section>
        <h3>Dati contrattuali</h3>
      </q-card-section>

      <!-- TIPO SUPPORTO FAMILIARE -->
      <q-card-section class="q-my-xl">
        <strong>*Seleziona tipo supporto familiare</strong>
       
        <div class="q-my-lg">
          <q-radio
            dense
            v-model="richiesta.contratto.tipo_supporto_familiare"
            val="ASSISTENTE_FAMILIARE"
            label="Assistente familiare"
          />
        </div>
        <q-radio
          :disable="legalAge"
          dense
          v-model="richiesta.contratto.tipo_supporto_familiare"
          val="EDUCATORE_PROFESSIONALE"
          label="Educatore professionale"
        />
      </q-card-section>

      <q-card-section v-if="richiesta.contratto.tipo_supporto_familiare">
        <p>
          <strong>*Seleziona tipologia di contratto</strong>
        </p>

        <q-radio
          v-if="
            richiesta.contratto.tipo_supporto_familiare ==
            'ASSISTENTE_FAMILIARE'
          "
          dense
          v-model="richiesta.contratto.tipo"
          @keyup.enter="submit"
          :error="$v.richiesta.contratto.tipo.$error"
          val="ASSISTENTE_FAMILIARE"
          label="Il destinatario Ã¨ in possesso di un regolare contratto di lavoro con assistente familiare intestato (in qualitÃ  di datore di lavoro) a:"
        />
        <!-- CONTRATTO LAVORO DOMESTICO -->
        <template v-if="richiesta.contratto.tipo === 'ASSISTENTE_FAMILIARE'">
          <div class="border-left--primary">
            <label for="intstetarioTypeAssFam">
              *Scegli tra destinatario, richiedente o altro
            </label>

            <q-option-group
              id="intstetarioTypeAssFam"
              class="q-my-lg"
              :options="optionsIntestatarioType"
              type="radio"
              v-model="stepperForm.tipo_intestatario"
            />

            <!-- TIPO INTESTATARIO -->
            <div
              v-if="stepperForm.tipo_intestatario === 'ALTRO'"
              class="q-mb-xl"
            >
              <div class="row">
                <div class="col-12 col-lg-8">
                  <strong>*Lâintestatario del contratto/lettera di incarico opera in
                  qualitÃ  di</strong>

                  <lms-select
                    id="relazione-select"
                    v-model="richiesta.contratto.relazione_destinatario"
                    :options="rapporto_options"
                    :rules="[(val) => val !== null || 'Inserire relazione']"
                    bottom-slots
                    label-for="relazione-select"
                    lazy-rules
                    name="destinatario-relazione-app"
                    no-error-icon
                    required
                  />
                </div>
              </div>
              <p>
                Inserire il codice fiscale se il datore di lavoro Ã¨:
                <strong> Altro soggetto</strong>
              </p>

              <CheckCF
                :codice_fiscale="intestatarioCF"
                :type="'altro'"
                :destinatario="richiesta.destinatario.cf"
                @verificaCF="verificaCF($event)"
              />
              <!-- DATI ALTRO SOGGETTO  -->
              <template
                v-if="
                  stepperForm.show_altro_soggetto &&
                  richiesta.contratto.intestatario
                "
              >
                <div class="row q-mb-xl q-gutter-y-lg">
                  <div class="col-12">
                    <dl>
                      <dt>Codice fiscale</dt>
                      <dd>
                        {{ richiesta.contratto.intestatario.cf }}
                      </dd>
                    </dl>
                  </div>
                  <div class="col-12 col-md-4">
                    <dl>
                      <dt>Cognome</dt>
                      <dl>{{ richiesta.contratto.intestatario.cognome }}</dl>
                    </dl>
                  </div>

                  <div class="col-12 col-md-4">
                    <dl>
                      <dt>Nome</dt>
                      <dd>{{ richiesta.contratto.intestatario.nome }}</dd>
                    </dl>
                  </div>

                  <div class="col-12 col-md-4">
                    <dl>
                      <dt>Data di nascita</dt>
                      <dd>
                        {{
                          richiesta.contratto.intestatario.data_nascita
                            | formatDate
                        }}
                      </dd>
                    </dl>
                  </div>

                  <div class="col-12 col-md-4">
                    <dl>
                      <dt>Nato/a a</dt>
                      <dd>
                        {{ richiesta.contratto.intestatario.comune_nascita }}
                      </dd>
                    </dl>
                  </div>

                  <div class="col-12 col-md-4">
                    <dl>
                      <dt>Provincia</dt>
                      <dd>
                        {{ richiesta.contratto.intestatario.provincia_nascita }}
                      </dd>
                    </dl>
                  </div>

                  <div class="col-12 col-md-4">
                    <dl>
                      <dt>Stato</dt>
                      <dd>
                        {{ richiesta.contratto.intestatario.stato_nascita }}
                      </dd>
                    </dl>
                  </div>
                </div>
              </template>

              <template v-if="stepperForm.show_altro_soggetto == false">
                <q-spinner color="primary" size="3em" />
              </template>
            </div>
            <!-- FINE TIPO INTESTATARIO -->

            <!-- DATA INIZIO E FINE CONTRATTO -->
            <h2>Data inizio e data fine del contratto/Lettera di incarico</h2>
            <p>{{dataContrattoDeterminato}}</p>
            <div class="row q-gutter-y-lg q-gutter-x-lg date-start-end">
              <div class="col-8 col-lg-4">
                <label for="dataInizioContratto">*Data inizio</label>
                <q-input
                  id="dataInizioContratto"
                  v-model="richiesta.contratto.data_inizio"
                  type="date"
                  min="1970-01-01"
                  max="2099-12-31"
                  @blur="$v.richiesta.contratto.data_inizio.$touch"
                  :error="$v.richiesta.contratto.data_inizio.$error"
                >
                  <template v-slot:error>
                    <template
                      v-if="!$v.richiesta.contratto.data_inizio.required"
                    >
                      Data inizio Ã¨ obbligatoria
                    </template>
                  </template>
                </q-input>
                <p
                  class="text--error"
                  v-if="
                    richiesta.contratto.data_inizio !== null &&
                    richiesta.contratto.data_fine !== null &&
                    new Date(richiesta.contratto.data_inizio) >=
                      new Date(richiesta.contratto.data_fine)
                  "
                >
                  La data di inizio contratto deve essere inferiore alla data di
                  fine contratto
                </p>
              </div>

              <div class="col-8 col-lg-4">
                <label for="dataFineContratto">Data fine</label>
                <q-input
                  id="dataFineContratto"
                  v-model="richiesta.contratto.data_fine"
                  type="date"
                  min="1970-01-01"
                  max="2099-12-31"
                >
                </q-input>
                
                <p
                  class="text--error"
                  v-if="
                    richiesta.contratto.data_fine !== null &&
                    new Date(richiesta.contratto.data_fine) < new Date()
                  "
                >
                  La data di fine contratto deve essere maggiore di oggi
                </p>
              </div>
            </div>
            <!--FINE Data inizio e data fine del contratto-->

            <!-- DATI ASSISTENTE FAMILIARE -->
            <div class="q-my-xl">
              <h3>Dati assistente familiare</h3>

              <CheckCF
                :codice_fiscale="assistenteCF"
                :type="'assistente_familiare'"
                @verificaCF="verificaCF($event)"
              />
            </div>
            <!-- DATI ASSISTENTE FAMILIARE SE CORRISPONDE SARANNO NASCOSTI -->
            <template
              v-if="
                stepperForm.show_assistente_familiare &&
                richiesta.contratto.assistente_familiare
              "
            >
              <div class="row q-my-xl q-gutter-y-lg">
                <div class="col-12">
                  <dl>
                    <dt>Codice fiscale</dt>
                    <dd>
                      {{ richiesta.contratto.assistente_familiare.cf }}
                    </dd>
                  </dl>
                </div>
                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Cognome</dt>
                    <dd>
                      {{ richiesta.contratto.assistente_familiare.cognome }}
                    </dd>
                  </dl>
                </div>

                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Nome</dt>
                    <dd>
                      {{ richiesta.contratto.assistente_familiare.nome }}
                    </dd>
                  </dl>
                </div>

                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Data di nascita</dt>
                    <dd v-if="richiesta.contratto.assistente_familiare.data_nascita">
                      {{
                        richiesta.contratto.assistente_familiare.data_nascita
                          | formatDate
                      }}
                    </dd>
                    <dd v-else>
                      -
                    </dd>
                  </dl>
                </div>

                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Nato/a a</dt>
                    <dd>
                      {{
                        richiesta.contratto.assistente_familiare.comune_nascita
                      }}
                    </dd>
                  </dl>
                </div>

                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Provincia</dt>
                    <dd>
                      {{
                        richiesta.contratto.assistente_familiare
                          .provincia_nascita
                      }}
                    </dd>
                  </dl>
                </div>

                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Stato</dt>
                    <dd>
                      {{
                        richiesta.contratto.assistente_familiare.stato_nascita
                      }}
                    </dd>
                  </dl>
                </div>
              </div>
            </template>
            <template v-if="stepperForm.show_assistente_familiare == false">
              <q-spinner color="primary" size="3em" />
            </template>
              <div class="row q-my-xl q-gutter-y-lg">
                <div class="col-12">
                  <h3 class="flex">
                    *Copia contratto di lavoro o lettera di assunzione
                    <tooltip
                      :titleBanner="copiaContrattoTooltip.title"
                      :textBanner="copiaContrattoTooltip.text"
                    />
                  </h3>
                  <!-- PRECEDENTE FILE CONTRATTO LAVORO -->
                  <div v-if="precedente_CONTRATTO_LAVORO">
                    <q-btn
                      icon="delete"
                      class="q-mr-md"
                      flat
                      color="negative"
                      title="elimina il documento"
                      @click="precedente_CONTRATTO_LAVORO = null"
                      :aria-label="
                        'elimina il documento ' +
                        precedente_CONTRATTO_LAVORO.name
                      "
                    />
                    <a
                      :href="
                        url +
                        '/' +
                        richiesta.numero +
                        '/' +
                        precedente_CONTRATTO_LAVORO.tipo
                      "
                      target="_blank"
                    >
                      <strong
                        >{{ precedente_CONTRATTO_LAVORO.filename }}
                      </strong>
                    </a>
                  </div>
                  <div
                    v-if="
                      !precedente_CONTRATTO_LAVORO && !allegati.CONTRATTO_LAVORO
                    "
                  >
                    <q-file
                      ref="CONTRATTO_LAVORO"
                      outlined
                      clearable
                      v-model="allegati.CONTRATTO_LAVORO"
                      label="Documento contratto"
                      class="q-mb-xl hidden"
                      :accept="fileAccepted"
                      :max-file-size="fileSize"
                      :filter="checkFileSize"
                      @rejected="onRejected"
                    ></q-file>

                    <q-btn
                      @click="caricaDocumento('CONTRATTO_LAVORO')"
                      color="primary"
                      label="ALLEGA"
                      aria-label="allega il contratto di lavoro"
                    ></q-btn>
                  </div>

                  <div class="q-my-lg" v-if="allegati.CONTRATTO_LAVORO">
                    <q-btn
                      icon="delete"
                      class="q-mr-md"
                      flat
                      color="negative"
                      title="elimina il documento"
                      @click="allegati.CONTRATTO_LAVORO = null"
                      :aria-label="
                        'elimina il documento ' + allegati.CONTRATTO_LAVORO.name
                      "
                    />
                    {{ allegati.CONTRATTO_LAVORO.name }}
                  </div>
                </div>

                <div class="col-12">
                  <h3>
                    *Copia denuncia rapporto di lavoro domestico presentata a
                    INPS
                  </h3>
                  <!-- PRECEDENTE FILE INPS -->
                  <div v-if="precedente_DENUNCIA_INPS">
                    <q-btn
                      icon="delete"
                      class="q-mr-md"
                      flat
                      color="negative"
                      title="elimina il documento"
                      @click="precedente_DENUNCIA_INPS = null"
                      :aria-label="
                        'elimina il documento ' + precedente_DENUNCIA_INPS.name
                      "
                    />
                    <a
                      :href="
                        url +
                        '/' +
                        richiesta.numero +
                        '/' +
                        precedente_DENUNCIA_INPS.tipo
                      "
                      target="_blank"
                    >
                      <strong>{{ precedente_DENUNCIA_INPS.filename }} </strong>
                    </a>
                  </div>
                  <div
                    v-if="!precedente_DENUNCIA_INPS && !allegati.DENUNCIA_INPS"
                  >
                    <q-file
                      ref="DENUNCIA_INPS"
                      outlined
                      clearable
                      v-model="allegati.DENUNCIA_INPS"
                      label="Documento INPS"
                      class="q-mb-xl hidden"
                      :accept="fileAccepted"
                      :max-file-size="fileSize"
                      :filter="checkFileSize"
                      @rejected="onRejected"
                    ></q-file>

                    <q-btn
                      @click="caricaDocumento('DENUNCIA_INPS')"
                      color="primary"
                      label="ALLEGA"
                      aria-label="allega il documento di denuncia inps"
                    ></q-btn>
                  </div>
                  <div v-if="allegati.DENUNCIA_INPS" class="q-my-lg">
                    <q-btn
                      icon="delete"
                      class="q-mr-md"
                      flat
                      color="negative"
                      title="elimina il documento"
                      @click="allegati.DENUNCIA_INPS = null"
                      :aria-label="
                        'elimina il documento ' + allegati.DENUNCIA_INPS.name
                      "
                    />
                    {{ allegati.DENUNCIA_INPS.name }}
                  </div>
                </div>
              </div>
          </div>
        </template>
        <div class="row">
          <div class="col-10">
            <!-- CONTRATTO AGENZIA/COOPERATIVA -->
            <q-radio
              dense
              v-model="richiesta.contratto.tipo"
              val="COOPERATIVA"
              label="Il destinatario Ã¨ in possesso di un contratto di prestazione di servizi con cooperativa sociale/agenzia di somministrazione di lavoro intestato (in qualitÃ  di datore di lavoro) a:"
            />
          </div>
          <div class="col-2">
            <tooltip
              :titleBanner="destinatarioTooltip.title"
              :textBanner="destinatarioTooltip.text"
            />
          </div>
        </div>
        <template v-if="richiesta.contratto.tipo === 'COOPERATIVA'">
          <div class="border-left--primary">
            <label for="intstetarioTypeCoop">
              *Scegli tra destinatario, richiedente o altro
            </label>

            <q-option-group
              id="intstetarioTypeCoop"
              class="q-my-lg"
              :options="optionsIntestatarioType"
              type="radio"
              v-model="stepperForm.tipo_intestatario"
            />
            <!-- TIPO INTESTATARIO -->
            <div
              v-if="stepperForm.tipo_intestatario === 'ALTRO'"
              class="q-my-lg"
            >
              <div class="row">
                <div class="col-12 col-lg-8">
                  <label for="relazione-select">
                    *Lâintestatario del contratto/lettera di incarico opera in
                    qualitÃ  di
                  </label>
                  <lms-select
                    id="relazione-select"
                    v-model="richiesta.contratto.relazione_destinatario"
                    :options="rapporto_options"
                    :rules="[(val) => val !== null || 'Inserire relazione']"
                    bottom-slots
                    lazy-rules
                    name="destinatario-relazione-app"
                    no-error-icon
                    required
                  />
                </div>
              </div>

              <p>
                Inserire il codice fiscale se il datore di lavoro Ã¨:
                <strong> Altro soggetto</strong>
              </p>
              <CheckCF
                :codice_fiscale="intestatarioCF"
                :type="'altro'"
                :destinatario="richiesta.destinatario.cf"
                @verificaCF="verificaCF($event)"
              />
              <!-- DATI ALTRO SOGGETTO  -->
              <template
                v-if="
                  stepperForm.show_altro_soggetto &&
                  richiesta.contratto.intestatario
                "
              >
                <div class="row q-mb-xl q-gutter-y-lg">
                  <div class="col-12">
                    <dl>
                      <dt>Codice fiscale</dt>
                      <dd>
                        {{ richiesta.contratto.intestatario.cf }}
                      </dd>
                    </dl>
                  </div>
                  <div class="col-12 col-md-4">
                    <dl>
                      <dt>Cognome</dt>
                      <dl>{{ richiesta.contratto.intestatario.cognome }}</dl>
                    </dl>
                  </div>

                  <div class="col-12 col-md-4">
                    <dl>
                      <dt>Nome</dt>
                      <dd>{{ richiesta.contratto.intestatario.nome }}</dd>
                    </dl>
                  </div>

                  <div class="col-12 col-md-4">
                    <dl>
                      <dt>Data di nascita</dt>
                      <dd>
                        {{
                          richiesta.contratto.intestatario.data_nascita
                            | formatDate
                        }}
                      </dd>
                    </dl>
                  </div>

                  <div class="col-12 col-md-4">
                    <dl>
                      <dt>Nato/a a</dt>
                      <dd>
                        {{ richiesta.contratto.intestatario.comune_nascita }}
                      </dd>
                    </dl>
                  </div>

                  <div class="col-12 col-md-4">
                    <dl>
                      <dt>Provincia</dt>
                      <dd>
                        {{ richiesta.contratto.intestatario.provincia_nascita }}
                      </dd>
                    </dl>
                  </div>

                  <div class="col-12 col-md-4">
                    <dl>
                      <dt>Stato</dt>
                      <dd>
                        {{ richiesta.contratto.intestatario.stato_nascita }}
                      </dd>
                    </dl>
                  </div>
                </div>
              </template>
              <template v-if="stepperForm.show_altro_soggetto == false">
                <q-spinner color="primary" size="3em" />
              </template>
            </div>

            <!-- DATA INIZIO E FINE CONTRATTO -->
            <h2>Data inizio e data fine del contratto/Lettera di incarico </h2>
            <p>{{dataContrattoDeterminato}}</p>
            <div class="row q-gutter-y-lg q-gutter-x-lg">
              <div class="col-8 col-lg-4">
                <label for="dataInizioContratto">*Data inizio</label>
                <q-input
                  id="dataInizioContratto"
                  v-model="richiesta.contratto.data_inizio"
                  type="date"
                  min="1970-01-01"
                  max="2099-12-31"
                  @blur="$v.richiesta.contratto.data_inizio.$touch"
                  :error="$v.richiesta.contratto.data_inizio.$error"
                >
                  <template v-slot:error>
                    <template
                      v-if="!$v.richiesta.contratto.data_inizio.required"
                    >
                      Data inizio Ã¨ obbligatoria
                    </template>
                  </template>
                </q-input>
                <p
                  class="text--error"
                  v-if="
                    richiesta.contratto.data_inizio !== null &&
                    richiesta.contratto.data_fine !== null &&
                    new Date(richiesta.contratto.data_inizio) >=
                      new Date(richiesta.contratto.data_fine)
                  "
                >
                  La data di inizio contratto deve essere inferiore alla data di
                  fine contratto
                </p>
              </div>

              <div class="col-8 col-lg-4">
                <label for="dataFineContratto">Data fine</label>
                <q-input
                  id="dataFineContratto"
                  v-model="richiesta.contratto.data_fine"
                  type="date"
                  min="1970-01-01"
                  max="2099-12-31"
                >
                </q-input>
                
                <p
                  class="text--error"
                  v-if="
                    richiesta.contratto.data_fine !== null &&
                    new Date(richiesta.contratto.data_fine) < new Date()
                  "
                >
                  La data di fine contratto deve essere maggiore di oggi
                </p>
              </div>
            </div>
            <!--FINE DATA INIZIO E FINE CONTRATTO -->

            <!-- DATI COOPERATIVA -->
            <div class="row q-mt-xl">
              <div class="col-auto">
                <h3>Dati cooperativa/agenzia/altro soggetto giuridico</h3>
              </div>
              <div class="col">
                <tooltip
                  :titleBanner="datiCoopAgSoggTooltip.title"
                  :textBanner="datiCoopAgSoggTooltip.text"
                />
              </div>
            </div>

            <div class="row q-mb-md">
              <div class="col-12 col-sm-6 col-md-5">
                <q-input
                  outlined
                  v-model="richiesta.contratto.agenzia.cf"
                  label="*codice fiscale o P.IVA Cooperativa"
                  @focus="validaCF(richiesta.contratto.agenzia.cf)"
                  :error="$v.richiesta.contratto.agenzia.cf.$error"
                >
                </q-input>

                <div v-if="validaCFError" class="text--error">{{ validaCFError }}</div>
              </div>
            </div>
            <!--FINE DATI COOPERATIVA -->
            <!-- COPIA CONTRATTO DI PRESTAZIONE -->
            <div class="row q-mt-xl">
              <div class="col-10">
                <h3>
                  *Copia del contratto di prestazione di servizio di assistenza
                  domiciliare
                </h3>
              </div>
              <div class="col">
                <tooltip
                  :titleBanner="copiaContrattoCoopTooltip.title"
                  :textBanner="copiaContrattoCoopTooltip.text"
                />
              </div>
            </div>
            <div v-if="precedente_CONTRATTO_LAVORO_COOP">
              <q-btn
                icon="delete"
                class="q-mx-md"
                flat
                color="negative"
                title="elimina il documento"
                @click="precedente_CONTRATTO_LAVORO_COOP = null"
                :aria-label="
                  'elimina il documento ' + precedente_CONTRATTO_LAVORO_COOP
                "
              />
              <a
                :href="
                  url +
                  '/' +
                  richiesta.numero +
                  '/' +
                  precedente_CONTRATTO_LAVORO_COOP.tipo
                "
                target="_blank"
              >
                <strong
                  >{{ precedente_CONTRATTO_LAVORO_COOP.filename }}
                </strong>
              </a>
            </div>
            <!-- PULSANTE CARICA LAVORO COOP -->
            <div
              v-if="
                !precedente_CONTRATTO_LAVORO_COOP &&
                !allegati.CONTRATTO_LAVORO_COOP
              "
              class="row"
            >
              <div class="col-12 col-md-6">
                <q-file
                  ref="CONTRATTO_LAVORO_COOP"
                  outlined
                  clearable
                  v-model="allegati.CONTRATTO_LAVORO_COOP"
                  class="q-mb-xl hidden"
                  :accept="fileAccepted"
                  :max-file-size="fileSize"
                  :filter="checkFileSize"
                  @rejected="onRejected"
                ></q-file>
                <q-btn
                  @click="caricaDocumento('CONTRATTO_LAVORO_COOP')"
                  color="primary"
                  label="ALLEGA CONTRATTO"
                  class="q-mb-xl"
                ></q-btn>
              </div>
            </div>
            <div v-if="allegati.CONTRATTO_LAVORO_COOP" class="q-my-lg">
              <strong>Contratto:</strong>
              <q-btn
                icon="delete"
                class="q-mx-md"
                flat
                color="negative"
                title="elimina il documento"
                @click="allegati.CONTRATTO_LAVORO_COOP = null"
                :aria-label="
                  'elimina il documento ' + allegati.CONTRATTO_LAVORO_COOP.name
                "
              />
              {{ allegati.CONTRATTO_LAVORO_COOP.name }}
            </div>
          </div>
        </template>
        <div class="row">
          <div class="col-10">
            <!-- PARTITA IVA-->
            <q-radio
              dense
              v-model="richiesta.contratto.tipo"
              val="PARTITA_IVA"
              label="il destinatario Ã¨ in possesso di una lettera di incarico a assistente familiare/educatore professionale che esercita lâattivitÃ  come libero professionista intestato (in qualitÃ  di datore di lavoro) a:"
            />
          </div>
          <div class="col-2">
            <tooltip
              :titleBanner="partitaIVATooltip.title"
              :textBanner="partitaIVATooltip.text"
            />
          </div>
          <template v-if="richiesta.contratto.tipo === 'PARTITA_IVA'">
            <div class="border-left--primary">
              <label for="intstetarioTypeCoop">
                  *Scegli tra destinatario, richiedente o altro</label>

                
              <q-option-group
                id="intstetarioTypeCoop"
                class="q-my-lg"
                :options="optionsIntestatarioType"
                type="radio"
                v-model="stepperForm.tipo_intestatario"
              />
              <!-- TIPO INTESTATARIO -->
              <div
                v-if="stepperForm.tipo_intestatario === 'ALTRO'"
                class="q-my-lg"
              >
                <div class="row">
                  <div class="col-12 col-lg-8">
                    <label for="relazione-select">
                      *Lâintestatario del contratto/lettera di incarico opera in
                      qualitÃ  di
                    </label>
                    <lms-select
                      id="relazione-select"
                      v-model="richiesta.contratto.relazione_destinatario"
                      :options="rapporto_options"
                      :rules="[(val) => val !== null || 'Inserire relazione']"
                      bottom-slots
                      lazy-rules
                      name="destinatario-relazione-app"
                      no-error-icon
                      required
                    />
                  </div>
                </div>

                <p>
                  Inserire il codice fiscale se il datore di lavoro Ã¨:
                  <strong> Altro soggetto</strong>
                </p>
                <CheckCF
                  :codice_fiscale="intestatarioCF"
                  :type="'altro'"
                  :destinatario="richiesta.destinatario.cf"
                  @verificaCF="verificaCF($event)"
                />
                <!-- DATI ALTRO SOGGETTO  -->
                <template
                  v-if="
                    stepperForm.show_altro_soggetto &&
                    richiesta.contratto.intestatario
                  "
                >
                  <div class="row q-mb-xl q-gutter-y-lg">
                    <div class="col-12">
                      <dl>
                        <dt>Codice fiscale</dt>
                        <dd>
                          {{ richiesta.contratto.intestatario.cf }}
                        </dd>
                      </dl>
                    </div>
                    <div class="col-12 col-md-4">
                      <dl>
                        <dt>Cognome</dt>
                        <dl>{{ richiesta.contratto.intestatario.cognome }}</dl>
                      </dl>
                    </div>

                    <div class="col-12 col-md-4">
                      <dl>
                        <dt>Nome</dt>
                        <dd>{{ richiesta.contratto.intestatario.nome }}</dd>
                      </dl>
                    </div>

                    <div class="col-12 col-md-4">
                      <dl>
                        <dt>Data di nascita</dt>
                        <dd>
                          {{
                            richiesta.contratto.intestatario.data_nascita
                              | formatDate
                          }}
                        </dd>
                      </dl>
                    </div>

                    <div class="col-12 col-md-4">
                      <dl>
                        <dt>Nato/a a</dt>
                        <dd>
                          {{ richiesta.contratto.intestatario.comune_nascita }}
                        </dd>
                      </dl>
                    </div>

                    <div class="col-12 col-md-4">
                      <dl>
                        <dt>Provincia</dt>
                        <dd>
                          {{
                            richiesta.contratto.intestatario.provincia_nascita
                          }}
                        </dd>
                      </dl>
                    </div>

                    <div class="col-12 col-md-4">
                      <dl>
                        <dt>Stato</dt>
                        <dd>
                          {{ richiesta.contratto.intestatario.stato_nascita }}
                        </dd>
                      </dl>
                    </div>
                  </div>
                </template>
                <template v-if="stepperForm.show_altro_soggetto == false">
                  <q-spinner color="primary" size="3em" />
                </template>
              </div>

              <!-- DATA INIZIO E FINE CONTRATTO -->
              <h2>Data inizio e data fine del contratto/Lettera di incarico</h2>
              <p>{{dataContrattoDeterminato}}</p>
              <div class="row q-gutter-y-lg q-gutter-x-lg">
                <div class="col-8 col-lg-4">
                  <label for="dataInizioContratto">*Data inizio</label>
                  <q-input
                    id="dataInizioContratto"
                    v-model="richiesta.contratto.data_inizio"
                    type="date"
                    min="1970-01-01"
                    max="2099-12-31"
                    @blur="$v.richiesta.contratto.data_inizio.$touch"
                    :error="$v.richiesta.contratto.data_inizio.$error"
                  >
                    <template v-slot:error>
                      <template
                        v-if="!$v.richiesta.contratto.data_inizio.required"
                      >
                        Data inizio Ã¨ obbligatoria
                      </template>
                    </template>
                  </q-input>
                  <p
                    class="text--error"
                    v-if="
                      richiesta.contratto.data_inizio !== null &&
                      richiesta.contratto.data_fine !== null &&
                      new Date(richiesta.contratto.data_inizio) >=
                        new Date(richiesta.contratto.data_fine)
                    "
                  >
                    La data di inizio contratto deve essere inferiore alla data
                    di fine contratto
                  </p>
                </div>

                <div class="col-8 col-lg-4">
                  <label for="dataFineContratto">Data fine</label>
                  <q-input
                    id="dataFineContratto"
                    v-model="richiesta.contratto.data_fine"
                    type="date"
                    min="1970-01-01"
                    max="2099-12-31"
                  >
                  </q-input>
                  
                  <p
                    class="text--error"
                    v-if="
                      richiesta.contratto.data_fine !== null &&
                      new Date(richiesta.contratto.data_fine) < new Date()
                    "
                  >
                    La data di fine contratto deve essere maggiore di oggi
                  </p>
                </div>
              </div>
              <!--FINE DATA INIZIO E FINE CONTRATTO -->

              <!-- DATI ASSISTENTE FAMILIARE P.IVA -->
              <div class="row q-mt-xl">
                <div class="col-12">
                  <h3>
                    Dati
                    {{
                      radioSupportoFamiliare == "ASSISTENTE_FAMILIARE"
                        ? "assistente familiare"
                        : "educatore professionale"
                    }}
                  </h3>
                  <CheckCF
                    :codice_fiscale="assistenteCF"
                    :type="'assistente_familiare'"
                    @verificaCF="verificaCF($event)"
                  />
                </div>
                <!-- <div class="col-auto">
                  <tooltip
                    :titleBanner="datiCoopAgSoggTooltip.title"
                    :textBanner="datiCoopAgSoggTooltip.text"
                  />
                </div> -->
              </div>

                <!-- DATI ASSISTENTE FAMILIARE SE CORRISPONDE SARANNO NASCOSTI -->
                <template
                  v-if="
                    stepperForm.show_assistente_familiare &&
                    richiesta.contratto.assistente_familiare
                  "
                >
                  <div class="row q-my-xl q-gutter-y-lg">
                    <div class="col-12">
                      <dl>
                        <dt>Codice fiscale</dt>
                        <dd>
                          {{ richiesta.contratto.assistente_familiare.cf }}
                        </dd>
                      </dl>
                    </div>
                    <div class="col-12 col-md-4">
                      <dl>
                        <dt>Cognome</dt>
                        <dd>
                          {{ richiesta.contratto.assistente_familiare.cognome }}
                        </dd>
                      </dl>
                    </div>

                    <div class="col-12 col-md-4">
                      <dl>
                        <dt>Nome</dt>
                        <dd>
                          {{ richiesta.contratto.assistente_familiare.nome }}
                        </dd>
                      </dl>
                    </div>

                    <div class="col-12 col-md-4">
                      <dl>
                        <dt>Data di nascita</dt>
                        <dd>
                          {{
                            richiesta.contratto.assistente_familiare
                              .data_nascita | formatDate
                          }}
                        </dd>
                      </dl>
                    </div>

                    <div class="col-12 col-md-4">
                      <dl>
                        <dt>Nato/a a</dt>
                        <dd>
                          {{
                            richiesta.contratto.assistente_familiare
                              .comune_nascita
                          }}
                        </dd>
                      </dl>
                    </div>

                    <div class="col-12 col-md-4">
                      <dl>
                        <dt>Provincia</dt>
                        <dd>
                          {{
                            richiesta.contratto.assistente_familiare
                              .provincia_nascita
                          }}
                        </dd>
                      </dl>
                    </div>

                    <div class="col-12 col-md-4">
                      <dl>
                        <dt>Stato</dt>
                        <dd>
                          {{
                            richiesta.contratto.assistente_familiare
                              .stato_nascita
                          }}
                        </dd>
                      </dl>
                    </div>
                  </div>
                </template>
                <template v-if="stepperForm.show_assistente_familiare == false">
                  <q-spinner color="primary" size="3em" />
                </template>

              <div class="row q-mb-md">
                <div class="col-12 col-sm-9 col-lg-5">
                  <q-input
                    outlined
                    v-model="richiesta.contratto.piva_assitente_familiare"
                    :label="
                      radioSupportoFamiliare == 'ASSISTENTE_FAMILIARE'
                        ? '*P.IVA/C.F. assistente familiare'
                        : '*P.IVA/C.F. educatore professionale'
                    "
                    @focus="
                      validaCF(richiesta.contratto.piva_assitente_familiare)
                    "
                    :error="
                      $v.richiesta.contratto.piva_assitente_familiare.$error
                    "
                  >
                  </q-input>

                  <div v-if="validaPIVAError">{{ validaPIVAError }}</div>
                </div>
              </div>
              <!-- FINE DATI ASSISTENTE FAMILIARE P.IVA -->
              <!-- COPIA CONTRATTO DI PRESTAZIONE -->
              <div class="row q-mt-xl">
                <div class="col">
                  <h3 v-if="radioSupportoFamiliare == 'ASSISTENTE_FAMILIARE'">*Copia lettera d'incarico di assistenza domiciliare</h3>
                  <h3 v-else>*Copia lettera d'incarico di educatore professionale</h3>
                </div>
              </div>
              <div v-if="precedente_LETTERA_INCARICO">
                <q-btn
                  icon="delete"
                  class="q-mx-md"
                  flat
                  color="negative"
                  title="elimina il documento"
                  @click="precedente_LETTERA_INCARICO = null"
                  :aria-label="
                    'elimina il documento ' + precedente_LETTERA_INCARICO
                  "
                />
                <a
                  :href="
                    url +
                    '/' +
                    richiesta.numero +
                    '/' +
                    precedente_LETTERA_INCARICO.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_LETTERA_INCARICO.filename }} </strong>
                </a>
              </div>
              <!-- PULSANTE CARICA LETTERA IN CARICO -->
              <div
                v-if="
                  !precedente_LETTERA_INCARICO && !allegati.LETTERA_INCARICO
                "
                class="row"
              >
                <div class="col-12 col-md-6">
                  <q-file
                    ref="LETTERA_INCARICO"
                    outlined
                    clearable
                    v-model="allegati.LETTERA_INCARICO"
                    class="q-mb-xl hidden"
                    :accept="fileAccepted"
                    :max-file-size="fileSize"
                    :filter="checkFileSize"
                    @rejected="onRejected"
                  ></q-file>
                  <q-btn
                    @click="caricaDocumento('LETTERA_INCARICO')"
                    color="primary"
                    label="ALLEGA"
                    class="q-mb-xl"
                  ></q-btn>
                </div>
              </div>
              <div v-if="allegati.LETTERA_INCARICO" class="q-my-lg">
                <p>
                  <strong>Lettera incarico:</strong>
                </p>
                <q-btn
                  icon="delete"
                  class="q-mr-md"
                  flat
                  color="negative"
                  title="elimina il documento"
                  @click="allegati.LETTERA_INCARICO = null"
                  :aria-label="
                    'elimina il documento ' + allegati.LETTERA_INCARICO.name
                  "
                />
                {{ allegati.LETTERA_INCARICO.name }}
              </div>
            </div>
          </template>
        </div>
        <div>
          <!-- NESSUN CONTRATTO -->
          <q-radio
            dense
            v-model="richiesta.contratto.tipo"
            val="NESSUN_CONTRATTO"
            label="Il destinatario non Ã¨ in possesso di un contratto di lavoro con assistente familiare/educatore professionale oppure un contratto di prestazione di servizi, inoltre il destinatario si impegna a sottoscrivere entro 30 giorni un contratto di lavoro"
          />
        </div>
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
import moment, { invalid } from "moment";
import store from "src/store/index";
import CheckCF from "components/CheckCF";
import Tooltip from "components/core/Tooltip";
import {
  getAnagrafica,
  putRichiesta,
  getRichiesta,
  postAllegato,
} from "src/services/api";
import {
  required,
  requiredIf,
  between,
  minLength,
  maxLength,
  numeric,
  integer,
  maxValue,
  minValue,
} from "vuelidate/lib/validators";
import LmsSelect from "components/core/LmsSelect";
import { ACCEPTED_FILES_TYPES, MAX_FILE_SIZE } from "src/services/config";
const mustBeTrue = (value) => value == true;

export default {
  name: "FillInTheForm",
  props: ["saveCurrentPage", "saveDraft"],
  components: { CheckCF, LmsSelect, Tooltip },

  data () {
    return {
      legalAge: false,
      dataContrattoDeterminato:
        "La data di fine di contratto Ã¨ da indicare solo se Ã¨ a tempo determinato",
      iseeTooltip: {
        title: "Attestazione I.S.E.E socio - sanitario",
        text: "L'I.S.E.E socio - sanitario Ã¨ utile per l'accesso alle prestazioni sociosanitarie come l'assistenza domiciliare per le persone con disabilitÃ  e/o non autosufficienti, l'ospitalitÃ  alberghiera presso strutture residenziali e semiresidenziali per le persone che non possono essere assistite a domicilio. <br><br> Le persone disabili maggiorenni possono scegliere un nucleo piÃ¹ ristretto rispetto a quello ordinario. <br><br> Per esempio, una persona maggiorenne disabile non coniugata e senza figli, che vive con i genitori, in sede di calcolo ISEE puÃ² dichiarare solo i suoi redditi e patrimoni.",
      },
      punteggioSocialeTooltip: {
        title: "Punteggio Sociale",
        text: "Si tratta del valore indicato nella scheda di valutazione del bisogno sociale, effettuato a cura ente dell'ente gestore delle funzioni socio-assistenziali. <br> <br> La valutazione tiene conto dei seguenti elementi, e puÃ² raggiungere un punteggio massimo di 14 punti, cosÃ¬ suddivisi: <ul><li>condizione abitativa:  (max 2 punti)</li> <li>condizione economica:  (max 4 punti)</li> <li>condizione famigliare:  (max 4 punti)</li><li>condizione assistenziale: (max 4 punti)</li></ul>",
      },
      datiCoopAgSoggTooltip: {
        title: "Dati cooperativa/agenzia/altro soggetto giuridico",
        text: "Cooperativa sociale, agenzia di somministrazione di lavoro o altro soggetto giuridico fornitore di servizi di assistenza domiciliare.",
      },
      copiaContrattoTooltip: {
        title: "Copia contratto o lettera di assunzione",
        text: "Contratto di lavoro subordinato, di durata pari ad almeno 12 mesi, per un minimo di 16 ore settimanali di servizio e che preveda l'inquadramento dell'assistente nei livelli CS o DS stabiliti dal Contratto Collettivo Nazionale di Lavoro sulla disciplina del rapporto di lavoro domestico;",
      },
      letteraIncaricoTooltip: {
        title: "Copia contratto o lettera di assunzione",
        text: "Contratto di lavoro subordinato, di durata pari ad almeno 12 mesi, per un minimo di 16 ore settimanali di servizio e che preveda l'inquadramento dell'assistente nei livelli CS o DS stabiliti dal Contratto Collettivo Nazionale di Lavoro sulla disciplina del rapporto di lavoro domestico;",
      },
      copiaContrattoCoopTooltip: {
        title:
          "Copia del contratto di prestazione di servizio di assistenza domiciliare",
        text: "Contratto di prestazione di un servizio di assistenza domiciliare con idonea cooperativa sociale, agenzia di somministrazione di lavoro o altro soggetto giuridico fornitore di servizi di assistenza domiciliare, di durata pari ad almeno 12 mesi, per un minimo di 16 ore settimanali di servizio e che preveda, nel caso specifico di contratto con cooperativa, un livello di inquadramento almeno B1 del Contratto Collettivo Nazionale di Lavoro per le lavoratrici e i lavoratori delle cooperative del settore socio-sanitario assistenziale-educativo e di inserimento.",
      },
      destinatarioTooltip: {
        title: "Contratto destinatario",
        text: "Un contratto di prestazione di un servizio di assistenza domiciliare con idonea cooperativa sociale, agenzia di somministrazione di lavoro o altro soggetto giuridico fornitore di servizi di assistenza domiciliare",
      },
      partitaIVATooltip: {
        title: "Contratto partita IVA",
        text: "Un incarico professionale, di durata pari ad almeno 12 mesi, per un minimo di 16 ore settimanali per la prestazione di un servizio di assistenza domiciliare da parte di un assistente familiare che esercita lâattivitÃ  come libero professionista.",
      },
      assistenteCF: "",
      cooperativaCF: "",
      intestatarioCF: "",
      altroCF: "",
      allegatiOk: true,
      isMounted: false,
      optionsRecCompatibility: [
        {
          label:
            "Il destinatario non Ã¨ beneficiario di altre misure aventi natura di trasferimento monetario specificatamente destinato/a al sostegno della domiciliaritÃ ",
          value: true,
        },
        {
          label:
            "Il destinatario Ã¨ beneficiario di altre misure aventi natura di trasferimento monetario specificatamente destinato/a al sostegno della domiciliaritÃ , ed Ã¨ disponibile a rinunciarvi in caso di assegnazione del buono",
          value: false,
        },
      ],
      optionsIntestatarioType: [
        {
          label: "Destinatario",
          value: "DESTINATARIO",
        },
        {
          label: "Richiedente",
          value: "RICHIEDENTE",
        },
        {
          label: "Altro soggetto",
          value: "ALTRO",
        },
      ],
      url: window.location.origin + "/buonodombff/api/v1/allegato",
      fileSize: MAX_FILE_SIZE,
      fileAccepted: ACCEPTED_FILES_TYPES,
      precedente_VERBALE_UVG: null,
      precedente_VERBALE_UMVD: null,
      precedente_DENUNCIA_INPS: null,
      precedente_CONTRATTO_LAVORO: null,
      precedente_CONTRATTO_LAVORO_COOP: null,
      precedente_LETTERA_INCARICO: null,
      rapporto_options: store.state.tipi_rapporto,
      stepperForm: {
        tipo_intestatario: null,
        show_assistente_familiare: null,
        show_altro_soggetto: null,
      },
      // VALORI STORE RICHIESTA
      richiesta: {
        attestazione_isee: false,
        punteggio_bisogno_sociale: null,
        valutazione_multidimensionale: null,
        contratto: {
          tipo: null,
          intestatario: {
            cf: null,
            nome: null,
            cognome: null,
            data_nascita: null,
            stato_nascita: null,
            comune_nascita: null,
            provincia_nascita: null,
          },
          relazione_destinatario: null,
          assistente_familiare: {
            cf: null,
            nome: null,
            cognome: null,
            data_nascita: null,
            stato_nascita: null,
            comune_nascita: null,
            provincia_nascita: null,
          },
          piva_assitente_familiare: null,
          tipo_supporto_familiare: null,
          data_inizio: null,
          data_fine: null,
          agenzia: {
            cf: null,
          },
          incompatibilita_per_contratto: null,
        },
        nessuna_incompatibilita: null,
        accredito: {
          iban: null,
          intestatario: null,
        },
      },
      allegati: {
        VERBALE_UVG: null,
        VERBALE_UMVD: null,
        CONTRATTO_LAVORO: null,
        CONTRATTO_LAVORO_COOP: null,
        DENUNCIA_INPS: null,
        LETTERA_INCARICO: null,
      },
    };
  },
  filters: {
    formatDate: function (date) {
      return moment(date).format("DD/MM/YYYY");
    },
  },
  computed: {
    validaCFError () {
      return this.validaCF(this.richiesta.contratto.agenzia.cf)
        ? this.validaCF(this.richiesta.contratto.agenzia.cf)
        : null;
    },
    validaPIVAError () {
      return this.validaCF(this.richiesta.contratto.piva_assitente_familiare)
        ? this.validaCF(this.richiesta.contratto.piva_assitente_familiare)
        : null;
    },
    // PERCEPISCE CAMBIAMENTI SUL RADIO DEL PUNTEGGIO SOCIALE
    radioPunteggio () {
      return this.richiesta.valutazione_multidimensionale;
    },
    // PERCEPISCE CAMBIAMENTI SUL RADIO DEL CONTRATTO
    radioContratto () {
      return this.richiesta.contratto.tipo
        ? this.richiesta.contratto.tipo
        : null;
    },
    // PERCEPISCE CAMBIAMENTI TIPO SUPPORTO FAMILIARE
    radioSupportoFamiliare () {
      return this.richiesta.contratto.tipo_supporto_familiare
        ? this.richiesta.contratto.tipo_supporto_familiare
        : null;
    },
    radioStepIntestatario () {
      return this.stepperForm.tipo_intestatario
        ? this.stepperForm.tipo_intestatario
        : null;
    },
    isInvalidValid () {
      return this.$v ? this.$v.$invalid : null;
    },
    invalidFields () {
      return this.$v
        ? Object.keys(this.$v.$params).filter(
          (fieldName) => this.$v[fieldName].$invalid
        )
        : null;
    },
    richiestaStore () {
      return store.getters["getRichiesta"];
    },
    richiestaCurrent () {
      return this.richiesta ? this.richiesta : null;
    },
    allegatiCurrent () {
      return this.allegati ? this.allegati : null;
    },
  },
  validations: {
    richiesta: {
      attestazione_isee: {
        mustBeTrue,
      },
      nessuna_incompatibilita: { required },
      valutazione_multidimensionale: { required },
      punteggio_bisogno_sociale: {
        required,
        between: between(7, 14),
        integer,
      },
      accredito: {
        iban: { required, maxLength: maxLength(27), minLength: minLength(27) },
        intestatario: { required },
      },
      contratto: {
        tipo: { required },
        relazione_destinatario: {
          required: requiredIf(function () {
            return this.stepperForm.tipo_intestatario == "ALTRO";
          }),
        },
        intestatario: {
          cf: {
            required: requiredIf(function () {
              return this.stepperForm.tipo_intestatario == "ALTRO";
            }),
          },
        },
        assistente_familiare: {
          cf: {
            required: requiredIf(function () {
              return (
                this.richiesta.contratto.tipo == "ASSISTENTE_FAMILIARE" ||
                this.richiesta.contratto.tipo == "PARTITA_IVA"
              );
            }),
          },
        },

        piva_assitente_familiare: {
          required: requiredIf(function () {
            return this.richiesta.contratto.tipo == "PARTITA_IVA";
          }),
          minLength: minLength(11),
          maxLength: maxLength(16),
        },
        tipo_supporto_familiare: { required },
        data_inizio: {
          required: requiredIf(function () {
            return this.richiesta.contratto.tipo !== "NESSUN_CONTRATTO";
          }),
          maxValue (val, { data_fine }) {
            return data_fine ? new Date(data_fine) > new Date(val) : true;
          },
        },
        data_fine: {
          minValue (val) {
            return val
              ? new Date(val) > new Date()
              : true;
          },
        },
        agenzia: {
          cf: {
            required: requiredIf(function () {
              return this.richiesta.contratto.tipo == "COOPERATIVA";
            }),
            minLength: minLength(11),
            maxLength: maxLength(16),
          },
        },
      },
    },
    allegati: {
      VERBALE_UVG: {
        required: requiredIf(function () {
          return (
            this.richiesta.valutazione_multidimensionale == "UVG" &&
            this.precedente_VERBALE_UVG == null
          );
        }),
      },
      VERBALE_UMVD: {
        required: requiredIf(function () {
          return (
            this.richiesta.valutazione_multidimensionale == "UMVD" &&
            this.precedente_VERBALE_UMVD == null
          );
        }),
      },
      CONTRATTO_LAVORO: {
        required: requiredIf(function () {
          return (
            this.richiesta.contratto.tipo == "ASSISTENTE_FAMILIARE" &&
            this.precedente_CONTRATTO_LAVORO == null
          );
        }),
      },
      DENUNCIA_INPS: {
        required: requiredIf(function () {
          return (
            this.richiesta.contratto.tipo == "ASSISTENTE_FAMILIARE" &&
            this.precedente_DENUNCIA_INPS == null
          );
        }),
      },
      CONTRATTO_LAVORO_COOP: {
        required: requiredIf(function () {
          return (
            this.richiesta.contratto.tipo == "COOPERATIVA" &&
            this.precedente_CONTRATTO_LAVORO_COOP == null
          );
        }),
      },
      LETTERA_INCARICO: {
        required: requiredIf(function () {
          return (
            this.richiesta.contratto.tipo == "PARTITA_IVA" &&
            this.precedente_LETTERA_INCARICO == null
          );
        }),
      },
    },
    stepperForm: {
      tipo_intestatario: {
        required: requiredIf(function () {
          return this.richiesta.contratto.tipo !== "NESSUN_CONTRATTO";
        }),
      },
    },
  },
  methods: {
    // FA IL TOUCH DEL FORM
    submit () {
      this.$v.$touch();
      if (this.$v.$error) {
        this.$q.notify({
          type: "negative",
          message: "Ricontrolla i campi del form",
        });
        return;
      }
    },
    checkAge (date) {
      return moment().diff(new Date(date), "years");
    },
    resetFormContratto () {
      this.stepperForm.tipo_intestatario = null;
      this.stepperForm.show_assistente_familiare = null;
      this.stepperForm.show_altro_soggetto = null;
      this.richiesta.contratto = {
        tipo: this.richiesta.contratto.tipo,
        intestatario: {
          cf: null,
          nome: null,
          cognome: null,
          data_nascita: null,
          stato_nascita: null,
          comune_nascita: null,
        },
        relazione_destinatario: null,
        assistente_familiare: {
          cf: null,
          nome: null,
          cognome: null,
          data_nascita: null,
          stato_nascita: null,
          comune_nascita: null,
        },
        piva_assitente_familiare: null,
        tipo_supporto_familiare:
          this.richiesta.contratto.tipo_supporto_familiare,
        data_inizio: null,
        data_fine: null,
        agenzia: {
          cf: null,
        },
        incompatibilita_per_contratto: null,
      };

      this.allegati.CONTRATTO_LAVORO = null;
      this.allegati.CONTRATTO_LAVORO_COOP = null;
      this.allegati.DENUNCIA_INPS = null;
      this.allegati.LETTERA_INCARICO = null;
    },
    resetTipoIntestatario () {
      this.stepperForm.show_altro_soggetto = null;
      this.richiesta.contratto.intestatario = {
        cf: null,
        nome: null,
        cognome: null,
        data_nascita: null,
        stato_nascita: null,
        comune_nascita: null,
      };
      this.richiesta.contratto.relazione_destinatario = null;
    },
    resetAssistenteFamiliare () {
      this.stepperForm.show_assistente_familiare = null;
      this.richiesta.contratto.assistente_familiare = {
        cf: null,
        nome: null,
        cognome: null,
        data_nascita: null,
        stato_nascita: null,
        comune_nascita: null,
      };
      this.richiesta.contratto.relazione_destinatario = null
    },
    checkFileSize (files) {
      return files.filter((file) => file.size < MAX_FILE_SIZE);
    },

    onRejected () {
      this.$q.notify({
        type: "negative",
        message: `File non idoneo, controlla tipo e grandezza`,
      });
    },
    async saveStepForm (avanti) {
      if (avanti) {
        await store.dispatch("setSpinner", true);
      }
      // PROVIAMO PRIMA SE GLI ALLEGATI VANNO BENE DIVERSAMENTE ANNULLIAMO ANCHE LA PUT

      for (let attribute in this.allegati) {
        try {
          if (this.allegati[attribute]) {
            await this.postAllegatoSingolo(attribute);
          }
        } catch (err) {
          let message =
            err.response && err.response.data.detail
              ? err.response.data.detail[0].valore
              : "Problemi server, contattare assistenza";
          this.$q.notify({
            type: "negative",
            message: message,
          });
        }
      }
      if (this.stepperForm.tipo_intestatario === "DESTINATARIO") {
        this.richiesta.contratto.intestatario = JSON.parse(
          JSON.stringify(this.richiesta.destinatario)
        );
      }

      if (this.stepperForm.tipo_intestatario === "RICHIEDENTE") {
        this.richiesta.contratto.intestatario = JSON.parse(
          JSON.stringify(this.richiesta.richiedente)
        );
      }

      delete this.richiesta.contratto.intestatario.comune_residenza;
      delete this.richiesta.contratto.intestatario.indirizzo_residenza;
      delete this.richiesta.contratto.intestatario.provincia_residenza;

      if (this.allegatiOk) {
        try {
          let { data } = await putRichiesta(
            this.richiesta.numero,
            this.richiesta
          );
          await store.dispatch("setStepperForm", this.stepperForm);
          await store.dispatch(
            "setRichiesta",
            JSON.parse(JSON.stringify(data))
          );

          if (avanti) {
            await store.dispatch("setSpinner", false);
            await store.dispatch("setStep", 3);
          } else {
            this.$q.notify({
              type: "positive",
              message: "La bozza Ã¨ stata salvata correttamente",
              timeout: 6000,
            });
            this.$emit("sbloccaBozza", false);
          }
        } catch (err) {
          await store.dispatch("setSpinner", false);
          await store.dispatch("setNextStep", {
            bool: true,
            step: 2,
          });

          await store.dispatch("setStep", 2);
          let message =
            err.response && err.response.data.detail
              ? err.response.data.detail[0].valore
              : "Problemi server, contattare assistenza";
          this.$q.notify({
            type: "negative",
            message: message,
            timeout: 6000,
          });
          if (avanti) {
            this.$router.push("/");
          }
          console.error(err);
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

    caricaDocumento (type) {
      this.$refs[type].pickFiles();
    },

    async postAllegatoSingolo (type) {
      try {
        let { data } = await postAllegato(
          this.richiesta.numero,
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
      } catch (err) {
        this.allegatiOk = false;
        let message =
          err.response && err.response.data.detail
            ? err.response.data.detail[0].valore
            : "Problemi server, contattare assistenza";
        this.$q.notify({
          type: "negative",
          message: message,
        });
        await store.dispatch("setSpinner", false);
      }
    },
    validaCF (cf) {
      if (cf == null) return;
      let validi, i, s, set1, set2, setpari, setdisp;
      if (cf == "") return "";
      cf = cf.toUpperCase();
      if (cf.length != 16 && cf.length != 11)
        return (
          "La lunghezza del Codice Fiscale non Ã¨\n" +
          "corretta: il Codice Fiscale dovrebbe essere lungo\n" +
          "esattamente 16 caratteri e la P.IVA o il codice fiscale provvisorio, 11 caratteri.\n"
        );
      if (cf.length == 16) {
        validi = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (i = 0;i < 16;i++) {
          if (validi.indexOf(cf.charAt(i)) == -1)
            return (
              "Il codice fiscale contiene un carattere non valido `" +
              cf.charAt(i) +
              "'.\nI caratteri validi sono le lettere e le cifre.\n"
            );
        }
        set1 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        set2 = "ABCDEFGHIJABCDEFGHIJKLMNOPQRSTUVWXYZ";
        setpari = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        setdisp = "BAKPLCQDREVOSFTGUHMINJWZYX";
        s = 0;
        for (i = 1;i <= 13;i += 2)
          s += setpari.indexOf(set2.charAt(set1.indexOf(cf.charAt(i))));
        for (i = 0;i <= 14;i += 2)
          s += setdisp.indexOf(set2.charAt(set1.indexOf(cf.charAt(i))));
        if (s % 26 != cf.charCodeAt(15) - "A".charCodeAt(0))
          return "Il codice fiscale non Ã¨ corretto: il codice di controllo non corrisponde.\n";
      }
      if (cf.length == 11) {
        if (!/^[0-9]{11}$/.test(cf))
          return "Il codice fiscale provvisorio o P.IVA possono solo numeri.";
        let s = 0;
        for (let i = 0;i < 11;i++) {
          let n = cf.charCodeAt(i) - "0".charCodeAt(0);
          if ((i & 1) === 1) {
            n *= 2;
            if (n > 9) n -= 9;
          }
          s += n;
        }
        if (s % 10 !== 0)
          return "Il codice fiscale provvisorio non Ã¨ corretto: il codice di controllo non corrisponde.\n.";
      }
    },
    async verificaCF (obj) {
      if (obj.type == "assistente_familiare") {
        if (obj.error) {
          this.stepperForm.show_assistente_familiare = null;
        }
        if (obj.payload) {
          this.richiesta.contratto.assistente_familiare = JSON.parse(
            JSON.stringify(obj.payload)
          );
          delete this.richiesta.contratto.assistente_familiare.comune_residenza;
          delete this.richiesta.contratto.assistente_familiare
            .indirizzo_residenza;
          delete this.richiesta.contratto.assistente_familiare
            .provincia_residenza;
          this.stepperForm.show_assistente_familiare = true;
        }
      } else if (obj.type == "altro") {
        if (obj.error) {
          this.stepperForm.show_altro_soggetto = null;
        }

        if (obj.payload) {
          this.richiesta.contratto.intestatario = JSON.parse(
            JSON.stringify(obj.payload)
          );
          delete this.richiesta.contratto.intestatario.comune_residenza;
          delete this.richiesta.contratto.intestatario.indirizzo_residenza;
          delete this.richiesta.contratto.intestatario.provincia_residenza;
          this.stepperForm.show_altro_soggetto = true;
        }
      } else {
        console.log("type Ã¨ null");
      }
    },
  },
  watch: {
    // RESETTO GLI ALLEGATI SE CAMBIA
    radioPunteggio () {
      this.allegati.VERBALE_UVG = null;
      this.allegati.VERBALE_UMVD = null;
    },
    // RESETTO  SOLO IL FORM CONTRATTO
    radioContratto () {
      if (this.isMounted) {
        this.resetFormContratto();
        if (this.richiesta.contratto.tipo === "NESSUN_CONTRATTO") {
          this.richiesta.contratto.incompatibilita_per_contratto = true;
        }
      }
    },
    radioSupportoFamiliare () {
      if (this.isMounted) {
        this.resetFormContratto();
        this.richiesta.contratto.tipo = null;
      }
    },
    radioStepIntestatario () {
      if (this.isMounted) {
        this.resetTipoIntestatario();
      }
    },
    isInvalidValid () {
      store.dispatch("setNextStep", {
        bool: this.$v.$invalid,
        step: 2,
      });
    },
    saveCurrentPage: function () {
      this.saveStepForm(true);
    },
    saveDraft: function () {
      if (this.$v.richiesta.$anyError || this.$v.stepperForm.$anyError) {
        this.$q.notify({
          type: "negative",
          message: `Non Ã¨ possibile salvare in bozza con errori in pagina`,
        });
      } else {
        this.saveStepForm(false);
        this.$emit("sbloccaBozza", false);
      }
    },
    richiestaCurrent: {
      handler () {
        if (this.isMounted) {
          this.$emit("sbloccaBozza", true);
        }
      },
      deep: true,
    },
    allegatiCurrent: {
      handler () {
        if (this.isMounted) {
          this.$emit("sbloccaBozza", true);
        }
      },
      deep: true,
    },
  },
  async created () {
    await store.dispatch("setSpinner", false);
    // SERVE PER RIMETTERE IN CIMA IL COMPONENTE
    window.scrollTo(0, 0);
    // SE SIAMO IN EDIT PRENDIAMO ID NUMERO DALLA URL
    let id = this.$route.params.id
      ? this.$route.params.id
      : this.richiestaStore.numero
        ? this.richiestaStore.numero
        : null;
    try {
      let { data } = await getRichiesta(id);
      if (data.contratto.data_inizio)
        data.contratto.data_inizio = data.contratto.data_inizio.substring(
          0,
          10
        );
      if (data.contratto.data_fine)
        data.contratto.data_fine = data.contratto.data_fine.substring(0, 10);
      // SE CI SONO ALLEGATI PREPOLIAMO PER LA EDIT
      if (data["allegati"]) {
        let allegati = JSON.parse(JSON.stringify(data["allegati"]));
        for (let allegato of allegati) {
          this["precedente_" + allegato.tipo] = allegato;
        }
      }

      // PREVALORIZZA A FALSE ISEE ANZICHÃ¨ NULL
      if (!data.hasOwnProperty("attestazione_isee")) {
        data.attestazione_isee = false;
      }

      if (!data.contratto.hasOwnProperty("relazione_destinatario")) {
        data.contratto.relazione_destinatario = null
      }

      // BLOCCO ASSISTENTE FAMILIARE SE Ã¨ MAGGIORENNE
      if (this.checkAge(data.destinatario["data_nascita"]) >= 18) {
        data.contratto.tipo_supporto_familiare = "ASSISTENTE_FAMILIARE";
        this.legalAge = true;
      }

      this.richiesta = JSON.parse(JSON.stringify(data));

      await store.dispatch("setRichiesta", JSON.parse(JSON.stringify(data)));

      // PREPOLOLIAMO STEPFORM
      let richiedente = data["richiedente"]["cf"];
      let destinatario = data["destinatario"]["cf"];
      let intestatario = data["contratto"]["intestatario"]["cf"];

      if (
        this.richiesta.contratto.tipo == "ASSISTENTE_FAMILIARE" &&
        data["contratto"]["intestatario"]["cf"]
      ) {
        this.stepperForm = {
          tipo_intestatario:
            richiedente == intestatario
              ? "RICHIEDENTE"
              : destinatario == intestatario
                ? "DESTINATARIO"
                : "ALTRO",
          show_assistente_familiare: true,
        };
        this.stepperForm.show_altro_soggetto =
          this.stepperForm.tipo_intestatario == "ALTRO" ? true : null;
      }

      if (data["contratto"]["assistente_familiare"]["cf"]) {
        this.stepperForm.show_assistente_familiare = true;
      } else {
        this.stepperForm.show_assistente_familiare = null;
      }

      if (
        this.richiesta.contratto.tipo == "COOPERATIVA" &&
        data["contratto"]["intestatario"]["cf"]
      ) {
        this.stepperForm = {
          tipo_intestatario:
            richiedente == intestatario
              ? "RICHIEDENTE"
              : destinatario == intestatario
                ? "DESTINATARIO"
                : "ALTRO",
          show_assistente_familiare: null,
        };
        this.stepperForm.show_altro_soggetto =
          this.stepperForm.tipo_intestatario == "ALTRO" ? true : null;
      }

      if (
        this.richiesta.contratto.tipo == "PARTITA_IVA" &&
        data["contratto"]["intestatario"]["cf"]
      ) {
        this.stepperForm = {
          tipo_intestatario:
            richiedente == intestatario
              ? "RICHIEDENTE"
              : destinatario == intestatario
                ? "DESTINATARIO"
                : "ALTRO",
          show_assistente_familiare: this.stepperForm.show_assistente_familiare,
        };
        this.stepperForm.show_altro_soggetto =
          this.stepperForm.tipo_intestatario == "ALTRO" ? true : null;
      }

      if (this.richiesta.contratto.tipo == "NESSUN_CONTRATTO") {
        this.stepperForm = {
          tipo_intestatario: null,
          show_assistente_familiare: null,
          show_altro_soggetto: null,
        };
      }
    } catch (err) {
      await store.dispatch("resetAllStep");
      await store.dispatch("setSpinner", false);
      let message =
        err.response && err.response.data.detail
          ? err.response.data.detail[0].valore
          : "Problemi server, contattare assistenza";
      this.$q.notify({
        type: "negative",
        message: message,
      });
      this.$router.push("/");
      console.error(err);
    }
  },
  async updated () {
    // PROVVISORIO
    setTimeout(() => (this.isMounted = true), 1000);
    if (this.isInvalidValid) {
      store.dispatch("setNextStep", {
        bool: this.$v.$invalid,
        step: 2,
      });
    }
  },
};
</script>

<style lang="sass"></style>

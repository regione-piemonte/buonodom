/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <div>
    <div class="q-page lms-page">
      <!-- CARD DESTINATARIO BUONO  DISABILITATO IN PERFEZIONAMENTO -->
      <q-card class="q-mt-xl q-mb-xl">
        <!-- DATI DESTINATARIO -->
        <q-card-section>
          <div class="row q-mt-xl">
            <div class="col-12">
              <h2>Informazioni domanda</h2>
              <dl>
                <dt>Numero domanda</dt>
                <dd>
                  {{ richiesta.numero }}
                </dd>
              </dl>
            </div>
          </div>
          <!-- NOTE -->
          <div class="row q-mt-xl" v-if="richiesta.note">
            <div class="col-12">
              <lms-banner type="warning">
                <h2>Note di compilazione</h2>
                <p>
                  {{ richiesta.note }}
                </p>
              </lms-banner>
            </div>
          </div>
          <div class="row q-my-xl justify-between items-center">
            <div class="col-auto">
              <h2>Dati destinatario</h2>
            </div>
          </div>
          <div class="row q-mb-xl q-col-gutter-xl text-overlay--active">
            <div class="col-12">
              <dl>
                <dt>Codice fiscale</dt>
                <dd>
                  {{ richiesta.destinatario.cf }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Cognome</dt>
                <dd>
                  {{ richiesta.destinatario.cognome }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Nome</dt>
                <dd>
                  {{ richiesta.destinatario.nome }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Data di nascita</dt>
                <dd>
                  {{ richiesta.destinatario.data_nascita | formatDate }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Nato/a a</dt>
                <dd>
                  {{ richiesta.destinatario.comune_nascita }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Provincia</dt>
                <dd>
                  {{ richiesta.destinatario.provincia_nascita }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Stato nascita</dt>
                <dd>
                  {{ richiesta.destinatario.stato_nascita }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Indirizzo residenza</dt>
                <dd>
                  {{ richiesta.destinatario.indirizzo_residenza }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Comune residenza</dt>
                <dd>
                  {{ richiesta.destinatario.comune_residenza }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Provincia residenza</dt>
                <dd>
                  {{ richiesta.destinatario.provincia_residenza }}
                </dd>
              </dl>
            </div>
            <!-- DATI DOMICILIO -->
            <template>
              <div class="col-12 col-md-4">
                <dl>
                  <dt>Indirizzo domicilio</dt>
                  <dd>
                    {{ richiesta.domicilio_destinatario.indirizzo }}
                  </dd>
                </dl>
              </div>
              <div class="col-12 col-md-4">
                <dl>
                  <dt>Comune domicilio</dt>
                  <dd>
                    {{ richiesta.domicilio_destinatario.comune }}
                  </dd>
                </dl>
              </div>
              <div class="col-12 col-md-4">
                <dl>
                  <dt>Provincia domicilio</dt>
                  <dd>
                    {{ richiesta.domicilio_destinatario.provincia }}
                  </dd>
                </dl>
              </div>
            </template>
          </div>
          <!-- ASL -->
          <div class="row q-col-gutter-xl q-mb-xl">
            <div class="col-12 col-lg-8 text-overlay--active">
              <dl>
                <dt>Asl</dt>
                <dd v-for="(item, i) of asl_options" :key="i">
                  <template v-if="item.codice == richiesta.asl_destinatario">
                    {{ item.etichetta }}
                  </template>
                </dd>
              </dl>
            </div>

            <!-- STUDIO DESTINATARIO -->
            <div class="col-12 col-lg-8 text-overlay--active">
              <dl>
                <dt>Titolo studio</dt>
                <dd v-for="(item, i) of titolo_options" :key="i">
                  <template v-if="item.codice == richiesta.studio_destinatario">
                    {{ item.etichetta }}
                  </template>
                </dd>
              </dl>
            </div>
            <!-- LAVORO DESTINATARIO -->
            <div class="col-12 col-lg-4 text-overlay--active">
              <dl>
                <dt>Situazione lavorativa</dt>
                <dd>
                  {{
                    richiesta.lavoro_destinatario ? "Occupato" : "Non occupato"
                  }}
                </dd>
              </dl>
            </div>
          </div>
        </q-card-section>
        <!--FINE DATI DESTINATARIO -->

        <!-- I TUOI DATI SEMPRE DISABILITATO IN PERFEZIONAMENTO -->
        <q-card-section>
          <h3>I tuoi dati</h3>
          <div class="row q-mb-xl q-col-gutter-xl text-overlay--active">
            <div class="col-12">
              <dl>
                <dt>Codice fiscale</dt>
                <dd>
                  {{ richiesta.richiedente.cf }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Cognome</dt>
                <dd>
                  {{ richiesta.richiedente.cognome }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Nome</dt>
                <dd>
                  {{ richiesta.richiedente.nome }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Data di nascita</dt>
                <dd>
                  {{ richiesta.richiedente.data_nascita | formatDate }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Nato/a a</dt>
                <dd>
                  {{ richiesta.richiedente.comune_nascita }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Provincia</dt>
                <dd>
                  {{ richiesta.richiedente.provincia_nascita }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Stato nascita</dt>
                <dd>
                  {{ richiesta.richiedente.stato_nascita }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Indirizzo residenza</dt>
                <dd>
                  {{ richiesta.richiedente.indirizzo_residenza }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Comune residenza</dt>
                <dd>
                  {{ richiesta.richiedente.comune_residenza }}
                </dd>
              </dl>
            </div>
            <div class="col-12 col-md-4">
              <dl>
                <dt>Provincia residenza</dt>
                <dd>
                  {{ richiesta.richiedente.provincia_residenza }}
                </dd>
              </dl>
            </div>
          </div>
          <!-- RICHIEDENTE OPERA IN QUALITA' DI -->
          <div class="row q-mb-xl" v-if="richiesta.delega">
            <div class="text-overlay--active">
              <dl>
                <dt>Il richiedente opera in qualitÃ  di</dt>
                <dd>
                  <div v-for="(item, i) of rapporto_options" :key="i">
                    <template v-if="item.codice == richiesta.delega">
                      {{ item.etichetta }}
                    </template>
                  </div>
                </dd>
              </dl>
            </div>
          </div>
        </q-card-section>
        <!--FINE I TUOI DATI -->

        <!-- ALLEGATI -->
        <q-card-section>
          <div class="row q-mb-xl">
            <div v-if="allegatiValidi && allegatiValidi.length > 0">
              <div v-if="precedente_DELEGA && delegaAllowed">
                <a
                  :href="
                    url + '/' + richiesta.numero + '/' + precedente_DELEGA.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_DELEGA.filename }} </strong>
                </a>
              </div>
              <div v-if="precedente_CARTA_IDENTITA && cartaIdentitaAllowed">
                <a
                  :href="
                    url +
                    '/' +
                    richiesta.numero +
                    '/' +
                    precedente_CARTA_IDENTITA.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_CARTA_IDENTITA.filename }} </strong>
                </a>
              </div>
              <div v-if="precedente_PROCURA_SPECIALE && procuraAllowed">
                <a
                  :href="
                    url +
                    '/' +
                    richiesta.numero +
                    '/' +
                    precedente_PROCURA_SPECIALE.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_PROCURA_SPECIALE.filename }} </strong>
                </a>
              </div>
              <div v-if="precedente_NOMINA_TUTORE && nominaTutoreAllowed">
                <a
                  :href="
                    url +
                    '/' +
                    richiesta.numero +
                    '/' +
                    precedente_NOMINA_TUTORE.tipo
                  "
                  target="_blank"
                >
                  <strong>{{ precedente_NOMINA_TUTORE.filename }} </strong>
                </a>
              </div>
            </div>
          </div>
        </q-card-section>
        <!--FINE ALLEGATI -->
      </q-card>
      <!--FINE CARD DESTINATARIO BUONO -->

      <!-- CARD COMPILAZIONE MODULO -->
      <q-card class="q-mt-xl q-mb-xl">
        <q-card-section>
          <div class="row items-center justify-between">
            <div class="col-auto">
              <h3>Compilazione Modulo</h3>
            </div>
          </div>
        </q-card-section>

        <!-- ISEE RIMARRA' SEMPRE BLOCCATO IN PERFEZIONAMENTO-->
        <q-card-section>
          <h3>Attestazione I.S.E.E.</h3>
          <div class="row refinement-disabled">
            <div class="col-11">
              Il destinatario Ã¨ in possesso di un attestato I.S.E.E. socio -
              sanitario in corso di validitÃ  avente un valore inferiore a 50.000
              euro (65.000 euro per minori e disabili)
            </div>
          </div>
        </q-card-section>
        <!--FINE ISEE -->

        <!-- REQUISITI COMPATIBILITA'-->
        <q-card-section>
          <h2>Requisiti compatibilitÃ </h2>
          <label for="richiestaNoInc"
            >*Seleziona un'opzione di compatibilitÃ </label
          >

          <q-radio
            class="q-my-lg"
            :disable="richiesta.nessuna_incompatibilita == true"
            dense
            v-model="richiesta.nessuna_incompatibilita"
            :val="true"
            label="Il destinatario non Ã¨ beneficiario di altre misure aventi natura di trasferimento monetario specificatamente destinato/a al sostegno della domiciliaritÃ "
          />
          <q-radio
            :disable="true"
            dense
            v-model="richiesta.contratto.tipo"
            :val="false"
            label="Il destinatario Ã¨ beneficiario di altre misure aventi natura di trasferimento monetario specificatamente destinato/a al sostegno della domiciliaritÃ , ed Ã¨ disponibile a rinunciarvi in caso di assegnazione del buono"
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
                    la percezione di "assegni di cura", ex
                    <abbr title="Delibera giunta regionale">D.G.R.</abbr> numero
                    39-11190, del 06/04/2009, e
                    <abbr title="Delibera giunta regionale">D.G.R.</abbr> numero
                    56-13332, del 15/02/2010;
                  </li>
                  <li>
                    la percezione di contributi dal Fondo per il sostegno del
                    ruolo di cura e assistenza del caregiver familiare, ex legge
                    numero 205/2017, articolo 1, cc. 254-256;
                  </li>
                  <li>
                    l'attivazione della misura "Home care premium" gestita da
                    <abbr title="Istituto nazionale di previdenza sociale"
                      >INPS</abbr
                    >, qualora preveda l'erogazione di trasferimenti monetari (a
                    titolo di "prestazione prevalente") oppure interventi di
                    assistenza domiciliare per un numero di ore settimanali
                    superiore a 16 (oppure 8 nel caso di assistenza educativa
                    rivolta a minori con disabilitÃ ) erogati a titolo di
                    "prestazione integrativa";
                  </li>
                  <li>
                    altre misure aventi natura di trasferimento monetario
                    specificatamente destinato al sostegno della domiciliaritÃ ,
                    di eventuale futura definizione, a titolaritÃ  regionale o
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
                    gli interventi di assistenza domiciliare direttamente
                    erogati dagli Enti Gestori, ovvero i servizi professionali
                    domiciliari resi da operatori sociosanitari ed educatori
                    professionali (non vi rientrano gli interventi di natura
                    professionale sanitaria) garantiti dagli Enti Gestori; tali
                    servizi si intendono come compatibili con la misura di cui
                    al presente Avviso se il destinatario ne beneficia per un
                    massimo di 16 ore settimanali;
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
        <!-- REQUISITI COMPATIBILITA'-->
        <q-card-section>
          <strong>Tipologia persona</strong>
          <div>
            <p>
              {{
                richiesta.valutazione_multidimensionale == "UVG"
                  ? "Il destinatario Ã¨ persona di etÃ  pari o superiore a 65 anni, non autosufficiente"
                  : "Il destinatario Ã¨ persona con disabilitÃ , non autosufficiente"
              }}
            </p>
          </div>
        </q-card-section>
        <!--FINE REQUISITI COMPATIBILITA'-->

        <!-- PUNTEGGIO SOCIALE -->
        <q-card-section>
          <h3>Punteggio sociale</h3>
          <p>
            Il destinatario Ã¨ giÃ  stato sottoposto a âvalutazione
            multidimensionaleâ presso le UnitÃ  di Valutazione competenti (U.V.G.
            - ANZIANI, o U.M.V.D. - DISABILI), e di essere in possesso di un
            punteggio sociale pari a:
          </p>

          <p class="text-font--xxl">
            <strong>{{ richiesta.punteggio_bisogno_sociale }}</strong>
          </p>
          <div v-for="(allegato, i) in richiesta.allegati" :key="i">
            <div
              v-if="
                allegato.tipo == 'VERBALE_UVG' &&
                richiesta.valutazione_multidimensionale === 'UVG'
              "
            >
              Lettera di comunicazione/Verbale UVG:
              <a
                :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                target="_blank"
              >
                <strong>{{ allegato.filename }} </strong>
              </a>
            </div>
            <div
              v-if="
                allegato.tipo == 'VERBALE_UMVD' &&
                richiesta.valutazione_multidimensionale === 'UMVD'
              "
            >
              Lettera di comunicazione/Verbale UMVD:
              <a
                :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                target="_blank"
              >
                <strong>{{ allegato.filename }} </strong>
              </a>
            </div>
          </div>
        </q-card-section>
        <!--FINE PUNTEGGIO SOCIALE -->

        <!-- ACCREDITO BUONO -->
        <q-card-section>
          <h3>Accredito buono</h3>
          <p class="q-mb-md">
            In caso di assegnazione del buono, l'erogazione potrÃ  avvenire
            mediante accredito su conto corrente bancario:
          </p>
          <div class="row">
            <div class="col-12">
              <dl>
                <dt>IBAN</dt>
                <dd>
                  {{ richiesta.accredito.iban }}
                </dd>
              </dl>
            </div>
            <div class="col-12">
              <dl>
                <dt>Intestatario</dt>
                <dd>
                  {{ richiesta.accredito.intestatario }}
                </dd>
              </dl>
            </div>
          </div>
        </q-card-section>
        <!--FINE ACCREDITO BUONO -->

        <!-- DATI CONTRATTUALI -->
        <q-card-section>
          <h3>Dati contrattuali</h3>
          <!-- TIPO SUPPORTO FAMILIARE -->
          <q-card-section class="q-mb-xl">
            <div class="row">
              <div class="col-12">
                <dl>
                  <dt>Tipo supporto</dt>
                  <dd v-if="richiesta.contratto.tipo_supporto_familiare">
                    {{
                      richiesta.contratto.tipo_supporto_familiare ==
                      "ASSISTENTE_FAMILIARE"
                        ? "Assistente familiare"
                        : "Educatore professionale"
                    }}
                  </dd>
                </dl>
              </div>
            </div>
          </q-card-section>
          <!-- FINE TIPO SUPPORTO FAMILIARE -->

          <q-card-section v-if="richiesta.contratto.tipo_supporto_familiare">
            <p>
              <strong>*Seleziona tipologia di contratto</strong>
            </p>

            <q-radio
              v-if="
                richiesta.contratto.tipo_supporto_familiare ==
                'ASSISTENTE_FAMILIARE'
              "
              :disable="!richiesta.contratto.incompatibilita_per_contratto"
              dense
              v-model="richiesta.contratto.tipo"
              @keyup.enter="submit"
              :error="$v.richiesta.contratto.tipo.$error"
              val="ASSISTENTE_FAMILIARE"
              label="Il destinatario Ã¨ in possesso di un regolare contratto di lavoro con assistente familiare intestato (in qualitÃ  di datore di lavoro) a:"
            />

            <!-- CONTRATTO LAVORO DOMESTICO -->
            <template
              v-if="richiesta.contratto.tipo === 'ASSISTENTE_FAMILIARE'"
            >
              <div class="border-left--primary">
                <label for="intstetarioTypeAssFam">
                  *Scegli tra destinatario, richiedente o altro
                </label>

                <q-option-group
                  :disable="!richiesta.contratto.incompatibilita_per_contratto"
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
                      <strong
                        >*Lâintestatario del contratto/lettera di incarico opera
                        in qualitÃ  di</strong
                      >

                      <lms-select
                        :disable="
                          !richiesta.contratto.incompatibilita_per_contratto
                        "
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
                    v-if="richiesta.contratto.incompatibilita_per_contratto"
                  />
                  <!-- DATI ALTRO SOGGETTO  -->
                  <template
                    v-if="
                      stepperForm.show_altro_soggetto &&
                      richiesta.contratto.intestatario
                    "
                  >
                    <div
                      class="row q-mb-xl q-gutter-y-lg"
                      :class="
                        !richiesta.contratto.incompatibilita_per_contratto
                          ? 'text-overlay--active'
                          : ''
                      "
                    >
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
                          <dd>
                            {{ richiesta.contratto.intestatario.cognome }}
                          </dd>
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
                            {{
                              richiesta.contratto.intestatario.comune_nascita
                            }}
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
                <!-- FINE TIPO INTESTATARIO -->

                <!-- DATA INIZIO E FINE CONTRATTO -->
                <h2>
                  Data inizio e data fine del contratto/Lettera di incarico
                </h2>
                <p>{{ dataContrattoDeterminato }}</p>
                <div class="row q-gutter-y-lg q-gutter-x-lg date-start-end">
                  <div class="col-8 col-lg-4">
                    <label for="dataInizioContratto">*Data inizio</label>
                    <q-input
                      :disable="
                        !richiesta.contratto.incompatibilita_per_contratto
                      "
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
                      :disable="
                        !richiesta.contratto.incompatibilita_per_contratto
                      "
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
                <!--FINE DATA INIZIO CONTRATTO-->

                <!-- DATI ASSISTENTE FAMILIARE -->
                <div class="q-my-xl">
                  <h3>Dati assistente familiare</h3>

                  <CheckCF
                    :codice_fiscale="assistenteCF"
                    :type="'assistente_familiare'"
                    @verificaCF="verificaCF($event)"
                    v-if="richiesta.contratto.incompatibilita_per_contratto"
                  />
                </div>
                <!-- DATI ASSISTENTE FAMILIARE SE CORRISPONDE SARANNO NASCOSTI -->
                <template
                  v-if="
                    stepperForm.show_assistente_familiare &&
                    richiesta.contratto.assistente_familiare
                  "
                >
                  <div
                    class="row q-my-xl q-gutter-y-lg"
                    :class="
                      !richiesta.contratto.incompatibilita_per_contratto
                        ? 'text-overlay--active'
                        : ''
                    "
                  >
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
                        v-if="richiesta.contratto.incompatibilita_per_contratto"
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
                        !precedente_CONTRATTO_LAVORO &&
                        !allegati.CONTRATTO_LAVORO
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
                        aria-label="allega il contratto di lavoto"
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
                          'elimina il documento ' +
                          allegati.CONTRATTO_LAVORO.name
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
                        v-if="richiesta.contratto.incompatibilita_per_contratto"
                        icon="delete"
                        class="q-mr-md"
                        flat
                        color="negative"
                        title="elimina il documento"
                        @click="precedente_DENUNCIA_INPS = null"
                        :aria-label="
                          'elimina il documento ' +
                          precedente_DENUNCIA_INPS.name
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
                        <strong
                          >{{ precedente_DENUNCIA_INPS.filename }}
                        </strong>
                      </a>
                    </div>
                    <div
                      v-if="
                        !precedente_DENUNCIA_INPS && !allegati.DENUNCIA_INPS
                      "
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
                  :disable="!richiesta.contratto.incompatibilita_per_contratto"
                  dense
                  v-model="richiesta.contratto.tipo"
                  val="COOPERATIVA"
                  label="Il destinatario Ã¨ in possesso di un contratto di prestazione di servizi con cooperativa sociale/agenzia di somministrazione di lavoro intestato (in qualitÃ  di datore di lavoro) a:"
                />
              </div>
            </div>
            <template v-if="richiesta.contratto.tipo === 'COOPERATIVA'">
              <div class="border-left--primary">
                <label for="intstetarioTypeCoop">
                  *Scegli tra destinatario, richiedente o altro
                </label>

                <q-option-group
                  :disable="!richiesta.contratto.incompatibilita_per_contratto"
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
                        *Lâintestatario del contratto/lettera di incarico opera
                        in qualitÃ  di
                      </label>
                      <lms-select
                        :disable="
                          !richiesta.contratto.incompatibilita_per_contratto
                        "
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
                    v-if="richiesta.contratto.incompatibilita_per_contratto"
                  />
                  <!-- DATI ALTRO SOGGETTO  -->
                  <template
                    v-if="
                      stepperForm.show_altro_soggetto &&
                      richiesta.contratto.intestatario
                    "
                  >
                    <div
                      class="row q-mb-xl q-gutter-y-lg"
                      :class="
                        !richiesta.contratto.incompatibilita_per_contratto
                          ? 'text-overlay--active'
                          : ''
                      "
                    >
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
                          <dd>
                            {{ richiesta.contratto.intestatario.cognome }}
                          </dd>
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
                            {{
                              richiesta.contratto.intestatario.comune_nascita
                            }}
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
                <h2>
                  Data inizio e data fine del contratto/Lettera di incarico
                </h2>
                <p>{{ dataContrattoDeterminato }}</p>
                <div class="row q-gutter-y-lg q-gutter-x-lg">
                  <div class="col-8 col-lg-4">
                    <label for="dataInizioContratto">*Data inizio</label>
                    <q-input
                      :disable="
                        !richiesta.contratto.incompatibilita_per_contratto
                      "
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
                      :disable="
                        !richiesta.contratto.incompatibilita_per_contratto
                      "
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
                      :disable="
                        !richiesta.contratto.incompatibilita_per_contratto
                      "
                      outlined
                      v-model="richiesta.contratto.agenzia.cf"
                      label="*codice fiscale o P.IVA Cooperativa"
                      @focus="validaCF(richiesta.contratto.agenzia.cf)"
                      :error="$v.richiesta.contratto.agenzia.cf.$error"
                    >
                    </q-input>

                    <div v-if="validaCFError" class="text--error">
                      {{ validaCFError }}
                    </div>
                  </div>
                </div>
                <!--FINE DATI COOPERATIVA -->
                <!-- COPIA CONTRATTO DI PRESTAZIONE -->
                <div class="row q-mt-xl">
                  <div class="col-10">
                    <h3>
                      *Copia del contratto di prestazione di servizio di
                      assistenza domiciliare
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
                    v-if="richiesta.contratto.incompatibilita_per_contratto"
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
                      'elimina il documento ' +
                      allegati.CONTRATTO_LAVORO_COOP.name
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
                  :disable="!richiesta.contratto.incompatibilita_per_contratto"
                  dense
                  v-model="richiesta.contratto.tipo"
                  val="PARTITA_IVA"
                  label="il destinatario Ã¨ in possesso di una lettera di incarico a assistente familiare/educatore professionale che esercita lâattivitÃ  come libero professionista intestato (in qualitÃ  di datore di lavoro) a:"
                />
              </div>
              <template v-if="richiesta.contratto.tipo === 'PARTITA_IVA'">
                <div class="border-left--primary">
                  <label for="intstetarioTypeCoop">
                    *Scegli tra destinatario, richiedente o altro</label
                  >

                  <q-option-group
                    :disable="
                      !richiesta.contratto.incompatibilita_per_contratto
                    "
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
                          *Lâintestatario del contratto/lettera di incarico
                          opera in qualitÃ  di
                        </label>
                        <lms-select
                          :disable="
                            !richiesta.contratto.incompatibilita_per_contratto
                          "
                          id="relazione-select"
                          v-model="richiesta.contratto.relazione_destinatario"
                          :options="rapporto_options"
                          :rules="[
                            (val) => val !== null || 'Inserire relazione',
                          ]"
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
                      v-if="richiesta.contratto.incompatibilita_per_contratto"
                    />
                    <!-- DATI ALTRO SOGGETTO  -->
                    <template
                      v-if="
                        stepperForm.show_altro_soggetto &&
                        richiesta.contratto.intestatario
                      "
                    >
                      <div
                        class="row q-mb-xl q-gutter-y-lg"
                        :class="
                          !richiesta.contratto.incompatibilita_per_contratto
                            ? 'text-overlay--active'
                            : ''
                        "
                      >
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
                            <dl>
                              {{ richiesta.contratto.intestatario.cognome }}
                            </dl>
                          </dl>
                        </div>

                        <div class="col-12 col-md-4">
                          <dl>
                            <dt>Nome</dt>
                            <dd>
                              {{ richiesta.contratto.intestatario.nome }}
                            </dd>
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
                              {{
                                richiesta.contratto.intestatario.comune_nascita
                              }}
                            </dd>
                          </dl>
                        </div>

                        <div class="col-12 col-md-4">
                          <dl>
                            <dt>Provincia</dt>
                            <dd>
                              {{
                                richiesta.contratto.intestatario
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
                                richiesta.contratto.intestatario.stato_nascita
                              }}
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
                  <h2>
                    Data inizio e data fine del contratto/Lettera di incarico
                  </h2>
                  <p>{{ dataContrattoDeterminato }}</p>
                  <div class="row q-gutter-y-lg q-gutter-x-lg">
                    <div class="col-8 col-lg-4">
                      <label for="dataInizioContratto">*Data inizio</label>
                      <q-input
                        :disable="
                          !richiesta.contratto.incompatibilita_per_contratto
                        "
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
                        La data di inizio contratto deve essere inferiore alla
                        data di fine contratto
                      </p>
                    </div>

                    <div class="col-8 col-lg-4">
                      <label for="dataFineContratto">Data fine</label>
                      <q-input
                        :disable="
                          !richiesta.contratto.incompatibilita_per_contratto
                        "
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
                        v-if="richiesta.contratto.incompatibilita_per_contratto"
                      />
                    </div>
                  </div>
                  <!-- DATI ASSISTENTE FAMILIARE SE CORRISPONDE SARANNO NASCOSTI -->
                  <template
                    v-if="
                      stepperForm.show_assistente_familiare &&
                      richiesta.contratto.assistente_familiare
                    "
                  >
                    <div
                      class="row q-my-xl q-gutter-y-lg"
                      :class="
                        !richiesta.contratto.incompatibilita_per_contratto
                          ? 'text-overlay--active'
                          : ''
                      "
                    >
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
                            {{
                              richiesta.contratto.assistente_familiare.cognome
                            }}
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
                  <template
                    v-if="stepperForm.show_assistente_familiare == false"
                  >
                    <q-spinner color="primary" size="3em" />
                  </template>

                  <div class="row q-mb-md">
                    <div class="col-12 col-sm-9 col-lg-5">
                      <q-input
                        outlined
                        :disable="
                          !richiesta.contratto.incompatibilita_per_contratto
                        "
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
                      <h3
                        v-if="radioSupportoFamiliare == 'ASSISTENTE_FAMILIARE'"
                      >
                        *Copia lettera d'incarico di assistenza domiciliare
                      </h3>
                      <h3 v-else>
                        *Copia lettera d'incarico di educatore professionale
                      </h3>
                    </div>
                  </div>
                  <div v-if="precedente_LETTERA_INCARICO">
                    <q-btn
                      v-if="richiesta.contratto.incompatibilita_per_contratto"
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
                      <strong
                        >{{ precedente_LETTERA_INCARICO.filename }}
                      </strong>
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

            <q-radio
              disable
              dense
              v-model="richiesta.contratto.tipo"
              val="NESSUN_CONTRATTO"
              label="Il destinatario non Ã¨ in possesso di un contratto di lavoro con assistente familiare/educatore professionale oppure un contratto di prestazione di servizi, inoltre il destinatario si impegna a sottoscrivere entro 30 giorni un contratto di lavoro"
            />
          </q-card-section>
        </q-card-section>
        <!-- FINE DATI CONTRATTUALI -->
      </q-card>
      <!-- PULSANTI -->
      <div class="row button-action-stepper q-my-xxl">
        <div class="col-12 col-md second-block">
          <div class="row justify-end q-gutter-y-lg">
            <div class="col-auto">
              <q-btn
                class="q-ml-xl"
                color="primary"
                @click="perfezionaDomanda('PERFEZIONATA_IN_PAGAMENTO')"
                label="PERFEZIONA DOMANDA"
                :disable="isInvalidValid"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import moment from "moment";
import store from "src/store/index";
import LmsSelect from "components/core/LmsSelect";
import CheckCF from "components/CheckCF";
import Tooltip from "components/core/Tooltip";
import LmsBanner from "components/core/LmsBanner";
import {
  putRichiesta,
  getRichiesta,
  postAllegato,
  postCronologia,
} from "src/services/api";
import {
  required,
  requiredIf,
  minLength,
  maxLength,
} from "vuelidate/lib/validators";
import { ACCEPTED_FILES_TYPES, MAX_FILE_SIZE } from "src/services/config";
import { snackbarError } from "src/services/utils";

const mustBeTrue = (value) => value == true;

export default {
  name: "PageRefinement",
  components: { CheckCF, LmsSelect, Tooltip, LmsBanner },
  data () {
    return {
      id: null,
      allegatiOk: true,
      legalAge: false,
      assistenteCF: "",
      cooperativaCF: "",
      intestatarioCF: "",
      altroCF: "",
      isMounted: false,
      url: window.location.origin + "/buonodombff/api/v1/allegato",
      titolo_options: store.state.titoli_studio,
      rapporto_options: store.state.tipi_rapporto,
      asl_options: store.state.asl,
      contratto_options: store.state.tipi_contratto,
      modifica_modulo: false,
      fileSize: MAX_FILE_SIZE,
      fileAccepted: ACCEPTED_FILES_TYPES,
      precedente_NOMINA_TUTORE: null,
      precedente_PROCURA_SPECIALE: null,
      precedente_DELEGA: null,
      precedente_CARTA_IDENTITA: null,
      precedente_VERBALE_UVG: null,
      precedente_VERBALE_UMVD: null,
      precedente_DENUNCIA_INPS: null,
      precedente_CONTRATTO_LAVORO: null,
      precedente_CONTRATTO_LAVORO_COOP: null,
      precedente_LETTERA_INCARICO: null,

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

      copiaContrattoCoopTooltip: {
        title:
          "Copia del contratto di prestazione di servizio di assistenza domiciliare",
        text: "Contratto di prestazione di un servizio di assistenza domiciliare con idonea cooperativa sociale, agenzia di somministrazione di lavoro o altro soggetto giuridico fornitore di servizi di assistenza domiciliare, di durata pari ad almeno 12 mesi, per un minimo di 16 ore settimanali di servizio e che preveda, nel caso specifico di contratto con cooperativa, un livello di inquadramento almeno B1 del Contratto Collettivo Nazionale di Lavoro per le lavoratrici e i lavoratori delle cooperative del settore socio-sanitario assistenziale-educativo e di inserimento.",
      },
      datiCoopAgSoggTooltip: {
        title: "Dati cooperativa/agenzia/altro soggetto giuridico",
        text: "Cooperativa sociale, agenzia di somministrazione di lavoro o altro soggetto giuridico fornitore di servizi di assistenza domiciliare.",
      },
      copiaContrattoTooltip: {
        title: "Copia contratto o lettera di assunzione",
        text: "Contratto di lavoro subordinato, di durata pari ad almeno 12 mesi, per un minimo di 16 ore settimanali di servizio e che preveda l'inquadramento dell'assistente nei livelli CS o DS stabiliti dal Contratto Collettivo Nazionale di Lavoro sulla disciplina del rapporto di lavoro domestico;",
      },
      dataContrattoDeterminato:
        "La data di fine di contratto Ã¨ da indicare solo se Ã¨ a tempo determinato",
      optionsRichiestaValMulti: [
        {
          label:
            "Il destinatario Ã¨ persona di etÃ  pari o superiore a 65 anni, non autosufficiente",
          value: "UVG",
        },
        {
          label:
            "Il destinatario Ã¨ persona con disabilitÃ , non autosufficiente",
          value: "UMVD",
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
      stepperForm: {
        tipo_intestatario: null,
        show_assistente_familiare: null,
        show_altro_soggetto: null,
      },
      richiesta: {
        attestazione_isee: false,
        punteggio_bisogno_sociale: null,
        valutazione_multidimensionale: null,
        richiedente: {
          cf: null,
          nome: null,
          cognome: null,
          data_nascita: null,
          stato_nascita: null,
          comune_nascita: null,
          provincia_nascita: null,
          indirizzo_residenza: null,
          comune_residenza: null,
          provincia_residenza: null,
        },
        destinatario: {
          cf: null,
          nome: null,
          cognome: null,
          data_nascita: null,
          stato_nascita: null,
          comune_nascita: null,
          provincia_nascita: null,
          indirizzo_residenza: null,
          comune_residenza: null,
          provincia_residenza: null,
        },
        note: null,
        studio_destinatario: null,
        lavoro_destinatario: null,
        domicilio_destinatario: {
          indirizzo: null,
          comune: null,
          provincia: "",
        },
        asl_destinatario: null,
        delega: null,
        attestazione_isee: false,
        punteggio_bisogno_sociale: null,
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
        DELEGA: null,
        CARTA_IDENTITA: null,
        PROCURA_SPECIALE: null,
        NOMINA_TUTORE: null,
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
      return date ? moment(date).format("DD/MM/YYYY") : null;
    },
  },
  validations: {
    richiesta: {
      nessuna_incompatibilita: {
        mustBeTrue,
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
              return this.richiesta.contratto.tipo == "ASSISTENTE_FAMILIARE" ||
                this.richiesta.contratto.tipo == "PARTITA_IVA";
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
          required,
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
    textToUpper (val) {
      this.richiesta.domicilio_destinatario.provincia = val.toUpperCase();
    },
    // PER ORA FA SOLO IL TOUCH DEL FORM
    submit () {
      this.$v.$touch();
      if (this.$v.$error) {
        this.$q.notify({
          type: "negative",
          message: `Ricontrolla i campi del form`,
        });
        return;
      }
    },

    checkAge (date) {
      return moment().diff(new Date(date), "years");
    },

    async editApplication (id) {
      await store.dispatch("bozzaAllStep");
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
        snackbarError(err);
        await store.dispatch("setSpinner", false);
      }
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
        incompatibilita_per_contratto: this.richiesta.contratto.incompatibilita_per_contratto,
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

    async perfezionaDomanda (perfeziona) {
      if (
        perfeziona == "AMMESSA_RISERVA_IN_PAGAMENTO" ||
        perfeziona == "PREAVVISO_DINIEGO_IN_PAGAMENTO"
      ) {
        await store.dispatch("setSpinner", true);
      }
      for (let attribute in this.allegati) {
        try {
          if (this.allegati[attribute]) {
            await this.postAllegatoSingolo(attribute);
          }
        } catch (err) {
          snackbarError(err);
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
          this.richiesta.contratto.incompatibilita_per_contratto = false;
          let { data } = await putRichiesta(
            this.richiesta.numero,
            this.richiesta
          );
          await store.dispatch(
            "setRichiesta",
            JSON.parse(JSON.stringify(data))
          );
          try {
            if (perfeziona != this.richiesta.stato) {
              await postCronologia(
                this.richiesta.numero,
                "PERFEZIONATA_IN_PAGAMENTO"
              );
            }
            await store.dispatch("setSpinner", false);
            let { data } = await getRichiesta(this.richiesta.numero);
            this.richiesta = data;
            this.$q.notify({
              type: "positive",
              message: "Domanda perfezionata",
              timeout: 6000,
            });
            if (perfeziona == "PERFEZIONATA_IN_PAGAMENTO") {
              this.$router.push("/");
            }
          } catch (err) {
            snackbarError(err);
            await store.dispatch("setSpinner", false);
          }
        } catch (err) {
          await store.dispatch("setSpinner", false);
          snackbarError(err);
          if (perfeziona == "PERFEZIONATA_IN_PAGAMENTO") {
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
  computed: {
    allegatiValidi () {
      return this.richiesta.allegati
        ? this.richiesta.allegati.filter(
          (item) =>
            item.tipo == "DELEGA" ||
            item.tipo == "CARTA_IDENTITA" ||
            item.tipo == "PROCURA_SPECIALE" ||
            item.tipo == "NOMINA_TUTORE"
        )
        : null;
    },
    richiestaStore () {
      return store.getters["getRichiesta"];
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
    rapportoFiltrato () {
      return this.rapporto_options &&
        this.rapporto_options.filter(
          (tipo) => tipo.codice === this.richiesta.delega
        )[0]
        ? this.rapporto_options.filter(
          (tipo) => tipo.codice === this.richiesta.delega
        )[0]["allegato"]
        : null;
    },
    delegaAllowed () {
      return this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "DELEGA"
        )[0]
        : null;
    },
    delegaRequired () {
      return this.delegaAllowed && this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "DELEGA"
        )[0].allegato_obbligatorio
        : null;
    },
    cartaIdentitaRequired () {
      return this.cartaIdentitaAllowed && this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "CARTA_IDENTITA"
        )[0].allegato_obbligatorio
        : null;
    },
    cartaIdentitaAllowed () {
      return this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "CARTA_IDENTITA"
        )[0]
        : null;
    },
    procuraAllowed () {
      return this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "PROCURA_SPECIALE"
        )[0]
        : null;
    },
    procuraRequired () {
      return this.procuraAllowed && this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "PROCURA_SPECIALE"
        )[0].allegato_obbligatorio
        : null;
    },

    nominaTutoreAllowed () {
      return this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "NOMINA_TUTORE"
        )[0]
        : null;
    },
    nominaTutoreRequired () {
      return this.nominaTutoreAllowed && this.rapportoFiltrato
        ? this.rapportoFiltrato.filter(
          (tipo) => tipo.allegato_tipo_cod == "NOMINA_TUTORE"
        )[0].allegato_obbligatorio
        : null;
    },
    radioPunteggio () {
      return this.richiesta.valutazione_multidimensionale;
    },
    radioContratto () {
      return this.richiesta.contratto.tipo
        ? this.richiesta.contratto.tipo
        : null;
    },
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
  },
  watch: {
    radioPunteggio () {
      this.allegati.VERBALE_UVG = null;
      this.allegati.VERBALE_UMVD = null;
    },
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
  },
  async created () {
    await store.dispatch("setSpinner", true);
    // SERVE PER RIMETTERE IN CIMA IL COMPONENTE
    window.scrollTo(0, 0);
    // SE SIAMO IN EDIT PRENDIAMO ID NUMERO DALLA URL
    let id = this.$route.params.id ? this.$route.params.id : null;
    try {
      let { data } = await getRichiesta(id);
      this.richiesta = data;
      if (
        this.richiesta.stato != "AMMESSA_RISERVA_IN_PAGAMENTO" &&
        this.richiesta.stato != "PREAVVISO_DINIEGO_IN_PAGAMENTO"
      ) {
        await store.dispatch("setSpinner", false);
        this.$q.notify({
          type: "negative",
          message:
            "La domanda deve essere in stato 'da perfezionare' per essere modificabile",
          timeout: 6000,
        });
        this.$router.push("/");
        return;
      }
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
      if (this.checkAge(data["destinatario"]["data_nascita"]) >= 18) {
        this.legalAge = true;
      }

      // PER INPUT DATI COOPERATIVA PROVVISORIO

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
      await store.dispatch("setSpinner", false);
    } catch (err) {
      await store.dispatch("resetAllStep");
      await store.dispatch("setSpinner", false);
      snackbarError(err);
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

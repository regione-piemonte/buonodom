/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <div>
    <q-card>
      <!-- DATI CONTRATTUALI -->
      <q-card-section>
        <h3>Dati contrattuali</h3>
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
                    <strong
                      >*Lâintestatario del contratto/lettera di incarico opera
                      in qualitÃ  di</strong
                    >
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
                  :destinatario="destinatario.cf"
                  @verificaCF="verificaCF($event)"
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
              <!-- FINE TIPO INTESTATARIO -->

              <!-- DATA INIZIO E FINE CONTRATTO -->
              <h2>Data inizio e data fine del contratto/Lettera di incarico</h2>
              <p>{{ dataContrattoDeterminato }}</p>
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
              <!--FINE DATA INIZIO CONTRATTO-->

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
                          richiesta.contratto.assistente_familiare.data_nascita
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
                  :destinatario="destinatario.cf"
                  @verificaCF="verificaCF($event)"
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
              <p>{{ dataContrattoDeterminato }}</p>
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
                    :destinatario="destinatario.cf"
                    @verificaCF="verificaCF($event)"
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
                      'text-overlay--active'
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
                    <h3 v-if="radioSupportoFamiliare == 'ASSISTENTE_FAMILIARE'">
                      *Copia lettera d'incarico di assistenza domiciliare
                    </h3>
                    <h3 v-else>
                      *Copia lettera d'incarico di educatore professionale
                    </h3>
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
        </q-card-section>
      </q-card-section>
      <!-- FINE DATI CONTRATTUALI -->
    </q-card>
  </div>
</template>

<script>
import moment from "moment";
import store from "src/store/index";
import LmsSelect from "components/core/LmsSelect";
import CheckCF from "components/CheckCF";
import Tooltip from "components/core/Tooltip";

import {
  required,
  requiredIf,
  minLength,
  maxLength,
} from "vuelidate/lib/validators";
import { ACCEPTED_FILES_TYPES, MAX_FILE_SIZE } from "src/services/config";


const mustBeTrue = (value) => value == true;

export default {
  name: "ContractData",
  props: {
    destinatario: Object
  },
  components: { CheckCF, LmsSelect, Tooltip },
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
      fileSize: MAX_FILE_SIZE,
      fileAccepted: ACCEPTED_FILES_TYPES,
      precedente_DENUNCIA_INPS: null,
      precedente_CONTRATTO_LAVORO: null,
      precedente_CONTRATTO_LAVORO_COOP: null,
      precedente_LETTERA_INCARICO: null,
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

      },
      allegati: {
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
    checkAge (date) {
      return moment().diff(new Date(date), "years");
    },
    caricaDocumento (type) {
      this.$refs[type].pickFiles();
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
    richiestaCurrent () {
      return this.richiesta ? this.richiesta : null;
    },
    allegatiCurrent () {
      return this.allegati ? this.allegati : null;
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
    isInvalidValid () {
      this.$emit("validare", this.isInvalidValid);
    },
    radioContratto () {
      this.$emit("validare", this.isInvalidValid);
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
    richiestaCurrent: {
      handler () {
        this.$emit("richiedere", { richiesta: this.richiesta, stepperForm: this.stepperForm})
      },
      deep: true,
    },
    allegatiCurrent: {   
      handler () {
        this.$emit("allegare", this.allegati) 
      },
      deep: true,
    },
  },
  async created () {
    if (this.checkAge(this.destinatario["data_nascita"]) >= 18) {
      this.richiesta.contratto.tipo_supporto_familiare = "ASSISTENTE_FAMILIARE";
      this.legalAge = true;
    }
  },
  async updated () {
    // PROVVISORIO
    setTimeout(() => (this.isMounted = true), 1000);
  },
};
</script>

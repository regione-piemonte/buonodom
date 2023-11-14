/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <div>
    <div class="q-page lms-page">
      <!-- NOTE E ALLEGATO CONTRODEDUZIONE -->
      <q-card class="q-mt-xl q-mb-xl">
        <q-card-section>
          <div class="col-12 q-pa-xs">
            <h2>Controdeduzioni</h2>
            <label for="controdeduzione"
              ><span
                :class="
                  $v.richiesta.note_richiedente.$invalid ? 'required-field' : ''
                "
                >*</span
              >Note del richiedente</label
            >
            <q-input
              v-model="richiesta.note_richiedente"
              filled
              for="controdeduzione"
              autogrow
            />
          </div>
          <div class="col-12 q-pa-xs">
            <div>Allegato</div>
            <q-file
              ref="CONTRODEDUZIONE"
              outlined
              clearable
              v-model="allegati.CONTRODEDUZIONE"
              label="Documento controdeduzione"
              class="q-mb-xl hidden"
              :accept="fileAccepted"
              :max-file-size="fileSize"
              :filter="checkFileSize"
              @rejected="onRejected"
            ></q-file>

            <q-btn
              v-if="allegati.CONTRODEDUZIONE == null"
              @click="caricaDocumento('CONTRODEDUZIONE')"
              color="primary"
              label="ALLEGA"
            ></q-btn>

            <div v-if="allegati.CONTRODEDUZIONE" class="q-my-lg">
              <q-btn
                icon="delete"
                class="q-mr-md"
                flat
                color="negative"
                @click="allegati.CONTRODEDUZIONE = null"
                :aria-label="
                  'elimina il documento' + allegati.CONTRODEDUZIONE.name
                "
              />{{ allegati.CONTRODEDUZIONE.name }}
            </div>
          </div>
        </q-card-section>
      </q-card>
      <!-- NOTE E ALLEGATO CONTRODEDUZIONE-->

      <!-- CARD DESTINATARIO BUONO -->
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
          <div class="row q-my-xl justify-between items-center">
            <div class="col-auto">
              <h2>Dati destinatario</h2>
            </div>
          </div>
          <div class="row q-mb-xl q-col-gutter-xl">
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
          <div class="row q-mb-xl q-col-gutter-xl">
            <div class="col-12 col-lg-8">
              <dl>
                <dt>Asl</dt>
                <dd v-for="(item, i) of asl_options" :key="i">
                  <template v-if="item.codice == richiesta.asl_destinatario">
                    {{ item.etichetta }}
                  </template>
                </dd>
              </dl>
            </div>
            <div class="col-12 col-lg-8">
              <dl>
                <dt>Titolo studio</dt>
                <dd v-for="(item, i) of titolo_options" :key="i">
                  <template v-if="item.codice == richiesta.studio_destinatario">
                    {{ item.etichetta }}
                  </template>
                </dd>
              </dl>
            </div>
            <div class="col-12 col-lg-4" v-if="appropriateAge">
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

        <!-- I TUOI DATI -->
        <q-card-section>
          <h3>I tuoi dati</h3>
          <div class="row q-mb-xl q-col-gutter-xl">
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
          <div class="row q-mb-xl" v-if="richiesta.delega">
            <div class="col-12">
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
            <div>
              <!-- ALLEGATI-->
              <div v-for="(allegato, i) in richiesta.allegati" :key="i">
                <div v-if="allegato.tipo == 'DELEGA'">
                  Delega:
                  <a
                    :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                    target="_blank"
                  >
                    <strong>{{ allegato.filename }} </strong>
                  </a>
                </div>
                <div v-if="allegato.tipo == 'CARTA_IDENTITA'">
                  Carta identitÃ :
                  <a
                    :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                    target="_blank"
                  >
                    <strong>{{ allegato.filename }} </strong>
                  </a>
                </div>
                <div v-if="allegato.tipo == 'PROCURA_SPECIALE'">
                  Procura speciale:
                  <a
                    :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                    target="_blank"
                  >
                    <strong>{{ allegato.filename }} </strong>
                  </a>
                </div>

                <div v-if="allegato.tipo == 'NOMINA_TUTORE'">
                  Nomina tutore:
                  <a
                    :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                    target="_blank"
                  >
                    <strong>{{ allegato.filename }} </strong>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </q-card-section>
        <!--FINE ALLEGATI -->
      </q-card>
      <!--FINE CARD DESTINATARIO BUONO -->

      <!-- CARD COMPILAZIONE MODULO -->
      <q-card class="q-mt-xl q-mb-xl">
        <!-- COMPILAZIONE MODULO -->
        <q-card-section>
          <div class="row items-center justify-between">
            <div class="col-auto">
              <h3>Compilazione Modulo</h3>
            </div>
          </div>
        </q-card-section>
        <!--FINE  COMPILAZIONE MODULO -->

        <!-- ISEE -->
        <q-card-section>
          <h3>Attestazione I.S.E.E.</h3>
          <div class="row">
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
          <h3>Requisiti di compatibilitÃ </h3>
          <div class="">
            <p>
              {{
                richiesta.valutazione_multidimensionale == "UVG"
                  ? "Il destinatario Ã¨ persona di etÃ  pari o superiore a 65 anni, non autosufficiente"
                  : "Il destinatario Ã¨ persona con disabilitÃ , non autosufficiente"
              }}
            </p>
            <p>
              {{
                richiesta.nessuna_incompatibilita
                  ? "Il destinatario non Ã¨ beneficiario di altre misure aventi natura di trasferimento monetario specificatamente destinato/a al sostegno della domiciliaritÃ "
                  : "Il destinatario Ã¨ beneficiario di altre misure aventi natura di trasferimento monetario specificatamente destinato/a al sostegno della domiciliaritÃ , ed Ã¨ disponibile a rinunciarvi in caso di assegnazione del buono"
              }}
            </p>
          </div>
        </q-card-section>
        <!--FINE REQUISITI COMPATIBILITA'-->

        <!-- PUNTEGGIO SOCIALE -->
        <q-card-section>
          <h3>Punteggio sociale</h3>
          <p class="">
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
        <template v-if="richiesta.contratto.tipo != 'NESSUN_CONTRATTO'">
          <q-card-section>
            <h3>Dati contrattuali</h3>
          </q-card-section>
          <q-card-section class="q-mb-xl">
            <div class="row">
              <div class="col-12">
                <dl>
                  <dt>Tipo supporto</dt>
                  <dd>
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
          <q-card-section>
            <p>
              Il destinatario Ã¨ in possesso di un regolare contratto di lavoro
              con

              <span v-for="(item, i) of contratto_options" :key="i">
                <template v-if="item.codice == richiesta.contratto.tipo">
                  "<strong>{{ item.etichetta }}</strong
                  >"
                </template>
              </span>

              intestato (in qualitÃ  di datore di lavoro) a:
            </p>

            <!-- DATI INTESTATARIO -->
            <q-card-section>
              <div class="row q-my-xl justify-between items-center">
                <div class="col-auto">
                  <h2>Dati intestatario</h2>
                </div>
                <div class="col-auto"></div>
              </div>
              <div class="row q-mb-xl q-col-gutter-xl">
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
                    <dl>
                      {{ richiesta.contratto.intestatario.comune_nascita }}
                    </dl>
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
                    <dt>Stato nascita</dt>
                    <dd>
                      {{ richiesta.contratto.intestatario.stato_nascita }}
                    </dd>
                  </dl>
                </div>
                <div
                  class="col-12"
                  v-if="richiesta.contratto.relazione_destinatario"
                >
                  <dl>
                    <dt>
                      L'intestatario del contratto/lettera di incarico opera in
                      qualitÃ  di
                    </dt>
                    <dd>
                      <div v-for="(item, i) of rapporto_options" :key="i">
                        <template
                          v-if="
                            item.codice ==
                            richiesta.contratto.relazione_destinatario
                          "
                        >
                          {{ item.etichetta }}
                        </template>
                      </div>
                    </dd>
                  </dl>
                </div>
              </div>
            </q-card-section>
            <!--FINE DATI INTESTATARIO -->

            <!--DATA INIZIO E FINE CONTRATTO-->
            <div class="q-my-xl">
              <h3>Data di inizio e fine contratto</h3>
              <div class="row q-mb-xl q-col-gutter-xl">
                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Data inizio contratto</dt>
                    <dd>
                      {{ richiesta.contratto.data_inizio | formatDate }}
                    </dd>
                  </dl>
                </div>
                <div class="col-12 col-md-4">
                  <dl>
                    <dt>Data fine contratto</dt>
                    <dd v-if="richiesta.contratto.data_fine">
                      {{ richiesta.contratto.data_fine | formatDate }}
                    </dd>
                    <dd v-else>-</dd>
                  </dl>
                </div>
              </div>
            </div>
            <!--FINE DATA INIZIO E FINE CONTRATTO-->
          </q-card-section>
          <!--FINE DATI CONTRATTUALI -->

          <!-- DATI AZIENDA -->
          <q-card-section
            v-if="richiesta.contratto.agenzia && richiesta.contratto.agenzia.cf"
          >
            <h3>
              Dati cooperativa sociale/agenzia di somministrazione di lavoro
            </h3>
            <div class="row q-mb-xl">
              <div class="col-12">
                <dl>
                  <dt>Codice fiscale</dt>
                  <dd>
                    {{ richiesta.contratto.agenzia.cf }}
                  </dd>
                </dl>
              </div>
            </div>
          </q-card-section>
          <!--FINE DATI AZIENDA -->

          <!-- DATI ASSISTENTE FAMILIARE -->
          <q-card-section
            v-if="
              richiesta.contratto.assistente_familiare &&
              richiesta.contratto.assistente_familiare.cf
            "
          >
            <h3>
              Dati
              {{
                richiesta.contratto.tipo_supporto_familiare ==
                "ASSISTENTE_FAMILIARE"
                  ? "assistente familiare"
                  : "educatore professionale"
              }}
            </h3>
            <div class="row q-mb-xl q-col-gutter-xl">
              <div class="col-12">
                <dl>
                  <dt>Codice fiscale</dt>
                  <dd>
                    {{ richiesta.contratto.assistente_familiare.cf }}
                  </dd>
                </dl>
              </div>
              <div
                class="col-12"
                v-if="richiesta.contratto.piva_assitente_familiare"
              >
                <dl>
                  <dt>Partita IVA</dt>
                  <dd>
                    {{ richiesta.contratto.piva_assitente_familiare }}
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
                  <dd
                    v-if="richiesta.contratto.assistente_familiare.data_nascita"
                  >
                    {{
                      richiesta.contratto.assistente_familiare.data_nascita
                        | formatDate
                    }}
                  </dd>
                  <dd v-else>-</dd>
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
                      richiesta.contratto.assistente_familiare.provincia_nascita
                    }}
                  </dd>
                </dl>
              </div>
              <div class="col-12 col-md-4">
                <dl>
                  <dt>Stato nascita</dt>
                  <dd>
                    {{ richiesta.contratto.assistente_familiare.stato_nascita }}
                  </dd>
                </dl>
              </div>
            </div>
          </q-card-section>
          <!--FINE DATI ASSISTENTE FAMILIARE -->

          <!-- COPIA CONTRATTO LAVORO-->
          <q-card-section v-if="richiesta.contratto.assistente_familiare">
            <template v-if="richiesta.contratto.tipo == 'ASSISTENTE_FAMILIARE'">
              <div class="q-mt-xl q-mb-md">
                Copia contratto di lavoro o lettera di assunzione e copia
                denuncia rapporto di lavoro domestico presentata a INPS
              </div>

              <div>
                <div v-for="(allegato, i) in richiesta.allegati" :key="i">
                  <div v-if="allegato.tipo == 'CONTRATTO_LAVORO'">
                    Contratto:
                    <a
                      :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                      target="_blank"
                    >
                      <strong>{{ allegato.filename }} </strong>
                    </a>
                  </div>
                  <div v-if="allegato.tipo == 'DENUNCIA_INPS'">
                    Denuncia INPS:
                    <a
                      :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                      target="_blank"
                    >
                      <strong>{{ allegato.filename }} </strong>
                    </a>
                  </div>
                </div>
              </div>
            </template>

            <div v-if="richiesta.contratto.tipo == 'COOPERATIVA'">
              <div class="q-mt-xl q-mb-md">Copia contratto cooperativa</div>
              <div v-for="(allegato, i) in richiesta.allegati" :key="i">
                <div v-if="allegato.tipo == 'CONTRATTO_LAVORO_COOP'">
                  Contratto cooperativa:
                  <a
                    :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                    target="_blank"
                  >
                    <strong>{{ allegato.filename }} </strong>
                  </a>
                </div>
              </div>
            </div>

            <div v-if="richiesta.contratto.tipo == 'PARTITA_IVA'">
              <div class="q-mt-xl q-mb-md">Copia lettera di incarico</div>
              <div v-for="(allegato, i) in richiesta.allegati" :key="i">
                <div v-if="allegato.tipo == 'LETTERA_INCARICO'">
                  Lettera di incarico:
                  <a
                    :href="url + '/' + richiesta.numero + '/' + allegato.tipo"
                    target="_blank"
                  >
                    <strong>{{ allegato.filename }} </strong>
                  </a>
                </div>
              </div>
            </div>
          </q-card-section>
        </template>
        <template v-else>
          <q-card-section>
            <h3>Nessun contratto</h3>
            <p class="">
              Il destinatario non Ã¨ in possesso di un contratto di lavoro con
              assistente familiare/educatore professionale oppure un contratto
              di prestazione di servizi, inoltre
              <strong
                >il destinatario si impegna a sottoscrivere entro 30 giorni un
                contratto di lavoro</strong
              >
            </p>
          </q-card-section>
        </template>
      </q-card>
      <!-- PULSANTI -->
      <div class="row button-action-stepper q-my-xxl">
        <div class="col-12 col-md second-block">
          <div class="row justify-end q-gutter-y-lg">
            <div class="col-auto">
              <q-btn
                class="q-ml-xl"
                color="primary"
                @click="inviaControdeduzione()"
                label="INVIA CONTRODEDUZIONE"
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
export default {
  name: "PageCounterDeduction",
  components: {},
  data() {
    return {
      id: null,
      allegatiOk: true,
      url: window.location.origin + "/buonodombff/api/v1/allegato",
      titolo_options: store.state.titoli_studio,
      rapporto_options: store.state.tipi_rapporto,
      asl_options: store.state.asl,
      contratto_options: store.state.tipi_contratto,
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
      richiesta: {
        note_richiedente: "",
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
        CONTRODEDUZIONE: null,
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
      note_richiedente: { required },
    },
  },
  methods: {
    checkAge(date) {
      return moment().diff(new Date(date), "years");
    },
    textToUpper(val) {
      this.richiesta.domicilio_destinatario.provincia = val.toUpperCase();
    },
    // PER ORA FA SOLO IL TOUCH DEL FORM
    submit() {
      this.$v.$touch();
      if (this.$v.$error) {
        this.$q.notify({
          type: "negative",
          message: `Ricontrolla i campi del form`,
        });
        return;
      }
    },

    caricaDocumento(type) {
      this.$refs[type].pickFiles();
    },
    async postAllegatoSingolo(type) {
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
        let message = err.response && err.response.data.detail
          ? err.response.data.detail[0].valore
          : "Problemi server, contattare assistenza";
        this.$q.notify({
          type: "negative",
          message: message,
        });
        await store.dispatch("setSpinner", false);
      }
    },
    checkFileSize(files) {
      return files.filter((file) => file.size < MAX_FILE_SIZE);
    },

    onRejected() {
      this.$q.notify({
        type: "negative",
        message: `File non idoneo, controlla tipo e grandezza`,
      });
    },
    async inviaControdeduzione(invia) {
      // PROVIAMO PRIMA SE GLI ALLEGATI VANNO BENE DIVERSAMENTE ANNULLIAMO ANCHE LA PUT
      await store.dispatch("setSpinner", true);

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

      if (this.allegatiOk) {
        try {
          let { data } = await putRichiesta(
            this.richiesta.numero,
            this.richiesta
          );
          await store.dispatch(
            "setRichiesta",
            JSON.parse(JSON.stringify(data))
          );

          try {
            await postCronologia(this.richiesta.numero, "CONTRODEDOTTA");
            await store.dispatch("setSpinner", false);
            this.$q.notify({
              type: "positive",
              message: "Controdeduzione inviata",
              timeout: 6000,
            });
            this.$router.push("/");
          } catch (err) {
            let message = err.response && err.response.data.detail
              ? err.response.data.detail[0].valore
              : "Problemi server, contattare assistenza";
            this.$q.notify({
              type: "negative",
              message: message,
              timeout: 6000,
            });
            await store.dispatch("setSpinner", false);
          }
        } catch (err) {
          let message =
            err.response && err.response.data.detail
              ? err.response.data.detail[0].valore
              : "Problemi server, contattare assistenza";
          this.$q.notify({
            type: "negative",
            message: message,
            timeout: 6000,
          });
          await store.dispatch("setSpinner", false);
        }
      }
    },
  },
  computed: {
    appropriateAge() {
      return (
        this.checkAge(this.richiesta.destinatario.data_nascita) < 66 &&
        this.checkAge(this.richiesta.destinatario.data_nascita) >= 18
      );
    },

    richiestaStore() {
      return store.getters["getRichiesta"];
    },
    isInvalidValid() {
      return this.$v ? this.$v.$invalid : null;
    },
  },
  async created() {
    await store.dispatch("setSpinner", true);
    // SERVE PER RIMETTERE IN CIMA IL COMPONENTE
    window.scrollTo(0, 0);
    // SE SIAMO IN EDIT PRENDIAMO ID NUMERO DALLA URL
    let id = this.$route.params.id
      ? this.$route.params.id
      : null;
    // MAGARI USIAMO SOLO THIS ID PULENDO IL CODICE
    try {
      let { data } = await getRichiesta(id);
      this.richiesta = data;

      if (this.richiesta.stato != "PREAVVISO_DI_DINIEGO_PER_NON_AMMISSIBILITA") {
        await store.dispatch("setSpinner", false);
        this.$q.notify({
          type: "negative",
          message:
            "La domanda deve essere in stato 'preavviso di diniego per non ammissibilitÃ ' per essere modificabile",
          timeout: 6000,
        });
        this.$router.push("/");
        return;
      }
      if (data["allegati"]) {
        let allegati = JSON.parse(JSON.stringify(data["allegati"]));
        for (let allegato of allegati) {
          this["precedente_" + allegato.tipo] = allegato;
        }
      }
      await store.dispatch("setRichiesta", JSON.parse(JSON.stringify(data)));
      await store.dispatch("setSpinner", false);
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
};
</script>

<style lang="sass">
</style>

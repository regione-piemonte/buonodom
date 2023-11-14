/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <q-dialog v-model="open" :maximized="$q.screen.lt.md" persistent>
    <q-card style="max-width: 800px" v-if="bankingModal">
      <q-card-section>
        <lms-banner type="warning">
          <h2>CAMBIA DATI ACCREDITO</h2>
          <p>
            Puoi modificare IBAN e intestario precedentemente inseriti
          </p>
        </lms-banner>
        <p class="text--error">
          Tutti i campi contrassegnati con * sono obbligatori
        </p>
        <div
          class="row q-gutter-y-lg q-gutter-x-lg date-start-end"
        >
          <div class="row q-mb-xl q-gutter-y-lg">
          <div class="col-12 col-md-12">
            <div class="q-ma-xs">
              <q-input
                outlined
                label="*IBAN"
                v-model="accredito.iban"
                @blur="$v.accredito.iban.$touch"
                :error="$v.accredito.iban.$error"
              >
                <template v-slot:error>
                  Campo iban obbligatorio e deve contenere 27
                  caratteri</template
                >
              </q-input>
            </div>
          </div>
          <div class="col-12 col-md-12">
            <div class="q-ma-xs">
              <q-input
                outlined
                v-model="accredito.intestatario"
                label="*Intestato a"
                @blur="$v.accredito.intestatario.$touch"
                :error="$v.accredito.intestatario.$error"
              >
                <template v-slot:error> Campo intestatario iban obbligatorio </template>
              </q-input>
            </div>
          </div>
        </div>
        </div>

        <div class="row justify-between q-mt-md q-mb-sm">
          <div class="col-auto">
            <q-btn color="negative" outline @click="close()">ANNULLA</q-btn>
          </div>
          <div class="col-auto">
            <q-btn
              :disable="isInvalidValid"
              color="primary"
              outline
              @click="changeBankingData()"
              >CONFERMA</q-btn
            >
          </div>
        </div>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

      
<script>
import {
  required,
  minLength,
  maxLength,
} from "vuelidate/lib/validators";
import LmsBanner from "components/core/LmsBanner";
export default {
  name: "BankingModal",
  props: {
    openModal: Boolean,
    bankingModal: Object,
  },
  components: { LmsBanner },
  data () {
    return {
      accredito:{
        iban: null,
        intestatario: null
      },
    };
  },
   validations: {
      accredito: {
        iban: { required, maxLength: maxLength(27), minLength: minLength(27) },
        intestatario: { required },
      },
   },
  computed: {
    open () {
      return this.openModal;
    },
    isInvalidValid () {
      return this.$v ? this.$v.$invalid : null;
    },
  },
  methods: {
    async changeBankingData () {
      await this.$emit("changeBankingData", this.accredito);
      this.accredito = null
    },
    close () {
      this.accredito = null
      this.$emit("changeBankingData", null);
    }
  },
  created(){
    this.accredito = JSON.parse(JSON.stringify(this.bankingModal));
  }
};
</script>
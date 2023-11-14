/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <q-layout
    view="hHh lpr fff"
    class="lms-layout-app"
    id="app-scelsoc"
    ref="routerView"
  >
    <div aria-label="Navigazione nascosta per screen reader" role="navigation">
      <a href="#main-content" role="link" class="skip-link"
        >Vai al contenuto principale</a
      >
      <a href="#footer" class="skip-link" role="link">Vai al footer</a>
    </div>
    <!-- HEADER -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <lms-layout-header :menu="!$c.IS_ALPHA_CERT" @click-menu="toggleMenu">
      <template #right v-if="utenteRichiedente">
        <lms-layout-header-profile-button
          :name="utenteRichiedente.nome"
          :surname="utenteRichiedente.cognome"
          :tax-code="utenteRichiedente.cf"
        />
      </template>

      <!-- TOOLBAR DEL VERTICALE -->
      <!-- --------------------------------------------------------------------------------------------------------- -->
      <template #after>
        <div class="lms-layout-app-toolbar">
          <div class="container">
            <lms-layout-app-toolbar>
              <q-btn
                flat
                href=""
                @click.prevent="goHome"
                class="lms-layout-header__toolbar__title"
                aria-label="Torna alla homepage del servizio buono domiciliaritÃ "
              >
                Buono domiciliaritÃ 
              </q-btn>
              <template #right>
              </template>
            </lms-layout-app-toolbar>
          </div>
        </div>
      </template>
    </lms-layout-header>

    

    <!-- PAGINE -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <q-page-container class="page-main" id="main-content">
      <template v-if="spinner">
        <div class="container">
          <div class="row">
            <div class="col-12 col-lg-8 offset-lg-2">
              <spinner></spinner>
            </div>
          </div>
        </div>
      </template>
      <template v-if="loading == false">
        <router-view />
      </template>
    </q-page-container>

    <!-- FOOTER -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <lms-layout-footer
    />
  </q-layout>
</template>

<script>
import store from "src/store/index";
import { IS_PROD, IS_TEST, IS_DEV } from "src/services/config";
import {
  getSportelli,
  getContatti,
  getPreferenze,
  getAnagrafica,
  getDecodifiche,
} from "src/services/api";
import LmsLayoutAppToolbar from "src/components/core/LmsLayoutAppToolbar";
import LmsLayoutFooter from "src/components/core/LmsLayoutFooter";
import LmsLayoutHeader from "src/components/core/LmsLayoutHeader";
import Spinner from "src/components/core/Spinner.vue";
import LmsLayoutHeaderProfileButton from "src/components/core/LmsLayoutHeaderProfileButton";


export default {
  name: "LayoutApp",
  components: {
    LmsLayoutFooter,
    LmsLayoutAppToolbar,
    LmsLayoutHeaderProfileButton,
    LmsLayoutHeader,
    Spinner,
  },
  data() {
    return {
      loading: null,
      isMenuVisible: false,
      sportelli: null,
      richieste: null,
    };
  },
  computed: {
    utenteRichiedente() {
      return store.getters["getUtenteRichiedente"];
    },
    sportelliStore() {
      return store.getters["getSportelli"];
    },
    spinner() {
      return store.getters["getSpinner"];
    }
  },
  watch: {
    $route: function () {
      this.$nextTick(function () {
        setTimeout(() => {
          const focusTarget = this.$refs.routerView.$el;
          focusTarget.setAttribute("tabindex", "-1");
          focusTarget.focus();
          focusTarget.removeAttribute("tabindex");
        }, 0);
      });
    },
  },

  methods: {
    async goHome() {
      if (this.$route.name !== "personalarea") {
        this.$router.push("/");
      }
    },
    async decodificheLoop() {
      let arrayDecodifiche = [
        "asl",
        "tipi-rapporto",
        "titoli-studio",
        "tipi-contratto",
        "stati-domanda",
        "tipi-supporto-familiare",
        "tipi-documento-rendicontazione",
        "tipi-allegato-buono",
        "tipi-dettaglio-documento-spesa"
      ];

      for (const item of arrayDecodifiche) {
        try {
          let { data } = await getDecodifiche(item);
          let name = item.replace(/-/g, "_");
          let value = JSON.parse(JSON.stringify(data));
          let obj = {
            name: name,
            value: value,
          };
          store.dispatch("setOptions", obj);
        } catch (err) {
          let message = err.response && err.response.data.detail
            ? err.response.data.detail[0].valore
            : "Problemi server, contattare assistenza";
          this.$q.notify({
            type: "negative",
            message: message,
          });
          await store.dispatch("setSpinner", false);
          this.loading = false;
        }
      }
    },
    goPortal() {
      if (IS_PROD) {
        // PRODUZIONE
        window.location.href =
          "";
      } else if (IS_TEST) {
        // TEST E DEV
        window.location.href =
          "";
      }
    },

    onClickTitle() {
      this.$router.push({ name: "personalarea" });
    },
    toggleMenu() {
      this.isMenuVisible = !this.isMenuVisible;
    },
  },
  async created() {
    await store.dispatch("setSpinner", true);
    this.loading = true;
    if (!this.sportelliStore) {
      try {
        let { data } = await getSportelli();
        let sportelli = JSON.parse(JSON.stringify(data));
        store.dispatch("setSportelli", { sportelli });
      } catch (err) {
        let message =
          err.response && err.response.data.detail
            ? err.response.data.detail[0].valore
            : "Problemi server, contattare assistenza";
        this.$q.notify({
          type: "negative",
          message: message,
        });
        await store.dispatch("setSpinner", false);
        this.loading = false;
      }
    }

    if (!this.utenteRichiedente) {
      try {
        let contacts = null;
        try {
          let { data } = await getContatti();
          contacts = JSON.parse(JSON.stringify(data));
          if (!contacts || !contacts.user_id || !contacts["email"]) {
            this.goPortal();
          }
          await store.dispatch("setContatti", contacts);
        } catch (err) {
          this.goPortal();
          await store.dispatch("setSpinner", false);
          this.loading = false;
        }

        // CHIAMATA ANAGRAFICA SE CONTATTI NON VA IN DEV METTIAMO IL CF DI VALENTE
        try {
          let anagrafica = null;
          if (IS_TEST || IS_DEV) {
            anagrafica = {
              cf:
                contacts.user_id && IS_TEST
                  ? contacts.user_id
                  : "VLNFNS80A01H501U",
              nome: "UTENTE",
              cognome: "DI TEST",
              data_nascita: "1970-01-01",
              stato_nascita: "Italia",
              comune_nascita: "TO",
              provincia_nascita: "TO",
              indirizzo_residenza: "VIA DELLE TROFIE",
              comune_residenza: "TORINO",
              provincia_residenza: "TO",
            };
          } else {
            try {
              let { data } = await getAnagrafica(contacts.user_id);
              anagrafica = JSON.parse(JSON.stringify(data));
            } catch (err) {
              let message =
                err.response && err.response.data.detail
                  ? err.response.data.detail[0].valore
                  : "Problemi server, contattare assistenza";
              this.$q.notify({
                type: "negative",
                message: message,
              });
              await store.dispatch("setSpinner", false);
              this.loading = false;
            }
          }
          await store.dispatch("setUtenteRichiedente", anagrafica);
          await store.dispatch("setSpinner", false);
          this.loading = false;
        } catch (err) {
          let message =
            err.response && err.response.data.detail
              ? err.response.data.detail[0].valore
              : "Problemi server, contattare assistenza";
          this.$q.notify({
            type: "negative",
            message: message,
          });
          await store.dispatch("setSpinner", false);
          this.loading = false;
        }
        // CHIAMATA PREFERENZE
        let preferences = null;
        try {
          let { data } = await getPreferenze();
          preferences = JSON.parse(JSON.stringify(data));
          if (!preferences || preferences.indexOf("email") == -1) {
            this.goPortal();
          }
          await store.dispatch("setPreferenze", preferences);
        } catch (err) {
          await store.dispatch("setSpinner", false);
          this.goPortal();
          this.loading = false;
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
        await store.dispatch("setSpinner", false);
        this.loading = false;
      }
    }
    await this.decodificheLoop();
  },
  mounted() {
    this.$nextTick(() => {
      document.body.addEventListener(
        "keyup",
        function (e) {
          if (e.key === "Tab") {
            const focused = Array.from(
              document.getElementsByClassName("keyFocus")
            );
            focused.concat(document.getElementsByTagName("a"));
            new Promise((resolve) => {
              focused.forEach(function (element) {
                element.classList.remove("keyFocus");
              });
              resolve();
            })
              .then(() => {
                if (
                  document.activeElement.classList.contains(
                    "v-btn--disabled"
                  ) ||
                  document.activeElement.classList.contains(
                    "cursor-pointer-off"
                  ) ||
                  document.activeElement.classList.contains("disabled-card h3")
                ) {
                  document.activeElement.classList.remove("keyFocus");
                } else {
                  if (
                    document.activeElement.offsetParent.classList.value ===
                      "v-select__slot" ||
                    document.activeElement.offsetParent.classList.value ===
                      "v-input--selection-controls__input" ||
                    document.activeElement.offsetParent.classList.value ===
                      "v-toolbar__title"
                  ) {
                    document.activeElement.offsetParent.classList.add(
                      "keyFocus"
                    );
                  } else {
                    document.activeElement.classList.add("keyFocus");
                  }
                }
              })
              .catch((error) => {
                console.log(error);
              });
          }
          if (e.key === "Escape") {
            document.activeElement.classList.remove("keyFocus");
          }
        },
        false
      );
    });
  },
};
</script>

<style lang="sass">
.lms-menu-list__logo
  width: 100%
  max-width: 250px
  height: auto

.lms-notification-list__body > .lms-notification-list-item:not(:last-of-type)
  border-bottom: 1px solid rgba(0, 0, 0, .12)

.lms-notification-list-empty
  text-align: center
  padding: map-get($space-lg, 'y') map-get($space-lg, 'x')
  color: $lms-text-faded-color

.lms-notification-list-item-contacts-activation
  background-color: $blue-2
  padding: map-get($space-md, 'y') map-get($space-md, 'x')
</style>

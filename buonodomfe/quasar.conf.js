/*
 * This file runs in a Node context (it's NOT transpiled by Babel), so use only
 * the ES6 features that are supported by your Node version. https://node.green/
 */

// Configuration for your app
// https://quasar.dev/quasar-cli/quasar-conf-js
/* eslint-env node */
const appEnvs = require("./envs");

module.exports = function(/* ctx */) {
  // Aggiungiamo delle variabili utili all'applicazione
  let envs = appEnvs.get(process.env.ENV);
  let boot = ["config", "http", "filters", "globals"];

  // Aggiungiamo i mocks solo se necessari così da non aumentare inutilmente
  // la dimensione del bundle finale
  if (envs.APP_API_MOCKS_JS_ENABLED) {
    boot.push("mocks");
  }

  return {
    // https://quasar.dev/quasar-cli/supporting-ts
    supportTS: false,

    // https://quasar.dev/quasar-cli/prefetch-feature
    // preFetch: true,

    // animations: 'all', // --- includes all animations
    // https://quasar.dev/options/animations
    animations: ["fadeInUp", "fadeInDown", "slideInLeft", "slideInRight" , "slideOutRight" ],

    // app boot file (/src/boot)
    // --> boot files are part of "main.js"
    // https://quasar.dev/quasar-cli/boot-files
    boot,

    // https://quasar.dev/quasar-cli/quasar-conf-js#Property%3A-css
    css: ["app.sass"],

    // https://github.com/quasarframework/quasar/tree/dev/extras
    extras: [
      // 'ionicons-v4',
      // 'mdi-v5',
      "fontawesome-v5",
      // 'eva-icons',
      // 'themify',
      // 'line-awesome',
      // 'roboto-font-latin-ext', // this or either 'roboto-font', NEVER both!
      "material-icons-outlined",
      "roboto-font", // optional, you are not bound to it
      "material-icons" // optional, you are not bound to it
    ],

    // https://quasar.dev/quasar-cli/quasar-conf-js#Property%3A-framework
    framework: {
      iconSet: "material-icons", // Quasar icon set
      lang: "it", // Quasar language pack
      config: {},

      // Possible values for "importStrategy":
      // * 'auto' - (DEFAULT) Auto-import needed Quasar components & directives
      // * 'all'  - Manually specify what to import
      importStrategy: "auto",

      // For special cases outside of where "auto" importStrategy can have an impact
      // (like functional components as one of the examples),
      // you can manually specify Quasar components/directives to be available everywhere:
      //
      // components: [],
      // directives: [],

      // Quasar plugins
      plugins: ["Notify", "Dialog", "SessionStorage", "LocalStorage"]
    },

    // Full list of options: https://quasar.dev/quasar-cli/quasar-conf-js#Property%3A-build
    build: {
      vueRouterMode: "hash", 
      env: envs,
      extendWebpack(cfg) {
        cfg.module.rules.push({
          enforce: "pre",
          test: /\.(js|vue)$/,
          loader: "eslint-loader",
          exclude: /node_modules/
        });
      }
    },
    devServer: {
      https: false,
      port: 8080,
      open: true, 
    }
  };
};

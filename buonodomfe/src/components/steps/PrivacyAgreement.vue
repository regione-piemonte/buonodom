/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

<template>
  <div>
    <p>Per proseguire nella richiesta Ã¨ necessario dichiarare di aver preso in visione l'informativa sulla privacy ed aver accettato le condizioni per ricevere il buono.</p>
    <p class="text--error">
      Tutti i campi contrassegnati con * sono obbligatori
    </p>
    <!-- CARD INFORMATIVA -->
    <q-card class="q-mt-lg">
      <q-card-section>
        <h2>Informativa sul trattamento dei dati personali rilasciata ai sensi dell'art. 13 G.D.P.R. 2016/679.</h2>
      </q-card-section>
      <q-separator />
      <q-scroll-area :visible="visible" class="height-scroll-area">
        <p>
          La informiamo che i dati personali da Lei forniti alla Regione Piemonte saranno trattati secondo quanto previsto dal "Regolamento UE 2016/679 relativo alla protezione delle persone fisiche con riguardo al trattamento dei dati personali, nonchÃ© alla libera circolazione di tali dati" e che abroga la direttiva 95/46/CE (regolamento Generale sulla Protezione dei dati, di seguito GDPR).
        </p>
        <p>
          La informiamo, inoltre, che:
          <ul>
            <li>
              I dati personali a Lei riferiti verranno raccolti e trattati nel rispetto dei principi di correttezza, liceitÃ  e tutela della riservatezza, con modalitÃ  informatiche ed esclusivamente per finalitÃ  di trattamento dei dati personali dichiarati nella domanda e comunicati agli uffici regionali o alle amministrazioni individuate come competenti a valutarla. Il trattamento Ã¨ finalizzato all'espletamento delle funzioni istituzionali definite dal Regolamento (UE) 2021/1057 del Parlamento europeo e del Consiglio del 24 giugno 2021 che ha istituito il Fondo sociale europeo Plus, dalla D.G.R. n. 2-4852 del 08.4.2022 di approvazione della proposta di Programma Regionale Fondo Sociale Europeo Plus della Regione Piemonte 2021-2027, dalla Decisione di esecuzione della Commissione Europea C(2022)5299 del 18.7.2022 di approvazione del programma "PR Piemonte FSE+ 2021/2027 e dalla D.G.R. n. 4-5458 del 03.8.2022 di recepimento del Programma FSE+ 2021/2027 della Regione Piemonte, approvato dalla Commissione Europea con la succitata Decisione.
            </li>
            <li>
              I dati acquisiti a seguito della presente informativa saranno utilizzati esclusivamente per le finalitÃ  relative all'avviso pubblico per le quali vengono comunicati. Tali dati potranno essere dati anagrafici, identificativi, economici, sociali, sanitari, e verranno trattati in modalitÃ  manuale e/o digitale.
            </li>
            <li>
              Il conferimento dei Suoi dati ed il relativo trattamento sono obbligatori in relazione alle finalitÃ  sopra descritte; ne consegue che l'eventuale rifiuto a fornirli impedirÃ  l'erogazione del servizio richiesto. 
            </li>
            <li>
              I dati di contatto del Responsabile della protezione dati (D.P.O.) sono: <a href="mailtot"></a>;
            </li>
            <li>
              Titolare del trattamento dei dati personali Ã¨ la Giunta regionale. Per la misura in oggetto sono delegati al trattamento dei dati:
              <ul>
                <li>
                  Politiche per le pari opportunitÃ , diritti ed inclusione, progettazione ed innovazione sociale;
                </li>
                <li>
                  il Direttore "pro tempore" della Direzione A15 Istruzione Formazione e Lavoro.
                </li>
              </ul>
            </li>
            <li>
              Il Responsabile (esterno) del trattamento Ã¨:
              <ul>
                <li>
                  il Consorzio per il Sistema Informativo Piemonte (CSI), ente strumentale della Regione Piemonte, pec: <a href="mailto:"></a>;
                </li>
                <li>
                  Finpiemonte S.P.A., Organismo Intermedio - P.E.C.: <a href="mailto:"></a>;
                </li>
                <li>
                  Enti gestori delle funzioni socio-assistenziali - indirizzi diversi (<a href="" target="_blank"></a>)
                </li>
              </ul>
            </li>
            <li>
              I dati personali saranno trattati esclusivamente da soggetti incaricati e Responsabili (esterni) individuati dal Titolare o da soggetti incaricati individuati dal Responsabile (esterno), autorizzati ed istruiti in tal senso, adottando tutte le misure tecniche ed organizzative adeguate per tutelare i diritti, le libertÃ  e i legittimi interessi che Le sono riconosciuti per legge in qualitÃ  di Interessato.
            </li>
            <li>
              I Suoi dati, resi anonimi, potranno essere utilizzati anche per finalitÃ  statistiche (D.Lgs. 281/1999 e s.m.i.);
            </li>
            <li>
              I Suoi dati personali sono conservati per un periodo di 15 anni a partire dalla chiusura delle attivitÃ  connesse con l'Avviso pubblico, come previsto dal piano di fascicolazione dell'Ente.
            </li>
            <li>
              I Suoi dati personali non saranno in alcun modo oggetto di trasferimento in un Paese terzo extraeuropeo, nÃ© di comunicazione a terzi fuori dai casi previsti dalla normativa in vigore, nÃ© di processi decisionali automatizzati compresa la profilazione.
            </li>
            <li>
              Salve le ipotesi di pubblicazione sul sito istituzionale in adempimento degli obblighi di pubblicitÃ  legale e trasparenza, i suoi dati personali non saranno soggetti a diffusione.
            </li>
            <li>
              I suoi dati personali potranno essere comunicati a:
              <ul>
                <li>
                  Altre Direzioni/Settori della Regione Piemonte per gli adempimenti di legge o per lo svolgimento delle attivitÃ  istituzionali di competenza;
                </li>
                <li>
                  AutoritÃ  di Audit e AutoritÃ  Contabile del Programma FSE+ della Regione Piemonte;
                </li>
                <li>
                  Soggetti/autoritÃ  nei confronti dei quali la comunicazione e/o l'eventuale diffusione sia prevista da disposizione di legge, da regolamenti o dalla normativa comunitaria;
                </li>
                <li>
                  Soggetti pubblici e organi di controllo in attuazione delle proprie funzioni previste per legge e per lo svolgimento delle loro funzioni istituzionali quali Commissione europea ed altri Enti e/o autoritÃ  con finalitÃ  ispettive, contabile-amministrative e di verifica;
                </li>
                <li>
                  Soggetti privati richiedenti l'accesso documentale (art. 22 ss. L. 241/1990) o l'accesso civico (art. 5 D.Lgs. 33/2013), nei limiti e con le modalitÃ  previste dalla legge.
                </li>
              </ul>
            </li>
          </ul>
        </p>
        <p>
          Ai sensi dell'articolo 74, paragrafo 1, lettera c) del Regolamento (UE) 2021/1060 i dati contenuti nelle banche dati a disposizione della Commissione Europea saranno utilizzati attraverso l'applicativo informatico ARACHNE, fornito all'AutoritÃ  di Gestione dalla Commissione Europea, per l'individuazione degli indicatori di rischio di frode.
        </p>
        <h3>
          Diritti dell'interessato:
        </h3>
        <p>
          In ogni momento, Lei potrÃ  esercitare, ai sensi degli articoli dal 15 al 22 del Regolamento UE n. 2016/679, il diritto di a) chiedere la conferma dell'esistenza o meno di propri dati personali e la loro messa a disposizione in forma intellegibile; ottenere le indicazioni circa le finalitÃ  del trattamento, le categorie dei dati personali, i destinatari o le categorie di destinatari a cui i dati personali sono stati o saranno comunicati e, quando possibile, il periodo di conservazione; ottenere l'aggiornamento, la rettifica, la cancellazione dei dati, la trasformazione in forma anonima, la limitazione o il blocco dei dati trattati in violazione di legge; ottenere l'integrazione dei dati se vi Ã¨ interesse; opporsi per motivi legittimi al trattamento stesso.
          Ha inoltre il diritto di proporre reclamo, esercitabile ricorrendo al Titolare o ai delegati al trattamento ovvero al Responsabile Protezione Dati (D.P.O. della Regione Piemonte: <a href="mailto:"></a>, Piazza Castello 165, 10121 Torino.). 
          In ultima istanza, oltre alle tutele previste in sede amministrativa o giurisdizionale, Ã¨ ammesso comunque il reclamo all'AutoritÃ  Garante per la protezione dei dati personali (<a href="mailto:"></a>), nel caso si ritenga che il trattamento avvenga in violazione del Regolamento citato (art. 77 del Regolamento).
        </p>
      </q-scroll-area>
    </q-card>
    <q-toggle
      v-model="privacy_toogle"
      label="*Dichiaro di avere preso visione dell'informativa e di prestare il mio consenso al trattamento dei dati personali"
    />
    <!-- CARD CONSENSO -->
    <q-card class="q-mt-lg">
      <q-card-section>
        <h2>Condizioni per ricevere il buono</h2>
      </q-card-section>
      <q-separator />
      <q-scroll-area :visible="visible" class="height-scroll-area">
        <h3>Descrizione della misura</h3>
        <p>
        Il Buono domiciliaritÃ  Ã¨ un contributo mensile erogato dalla Regione del valore di 600 euro, riconosciuto al massimo per 24 mensilitÃ , spendibile per l'acquisto di servizi di assistenza familiare o di assistenza educativa nel caso di minori, a favore di persone non autosufficienti (anziani o disabili) residenti in Piemonte. 
        La misura Ã¨ finanziata dal Fondo Sociale Europeo Plus - programmazione 2021-2027.
        </p>
        <p>
        I destinatari sono:
          <ul> 
            <li>Persone ultrasessantacinquenni non autosufficienti</li>
            <li>Persone con disabilitÃ  non autosufficienti con i seguenti requisiti:
              <ul>
                <li>residenti in Piemonte</li>
                <li>giÃ  sottoposte a "valutazione multidimensionale" presso le UnitÃ  di Valutazione competenti (UnitÃ  di Valutazione Geriatrica, U.V.G., o UnitÃ  Multidisciplinare di Valutazione della DisabilitÃ , U.M.V.D.) e alle quali sia stato attribuito un punteggio sociale non inferiore a 7;</li>
                <li>in possesso di I.S.E.E. sociosanitario in corso di validitÃ  (attestazione rilasciata nel 2023 con scadenza 31/12/2023) avente un valore non superiore a 50.000 euro (65.000 euro se il destinatario Ã¨ minorenne). Non Ã¨ ammesso un ISEE corrente o che presenti anomalie.</li>
              </ul>
            </li>
          </ul>
        </p>

        <h3>Condizioni per ricevere il Buono domiciliaritÃ  - Assistenza familiare</h3>
        <p>Per ricevere il âBuono DomiciliaritÃ  - Assistenza familiare" occorre che sia stato attivato un servizio di assistenza familiare con la stipula di:
          <ul>
            <li>
            un regolare contratto di lavoro subordinato con un assistente familiare, di durata pari ad almeno 12 mesi, per un minimo di 16 ore settimanali di servizio (inquadramento dell'assistente nei livelli CS o DS del CCNL sul lavoro domestico)
            oppure
            </li>
            <li>
            un incarico professionale, che rispetti i medesimi requisiti di durata e numero minimo di ore settimanali e preveda pari requisiti professionali, per la prestazione di un servizio di assistenza domiciliare da parte di un assistente familiare che esercita l'attivitÃ  come libero professionista
            oppure
            </li>
            <li>
            un contratto di prestazione di un servizio di assistenza domiciliare con idonea cooperativa sociale, agenzia di somministrazione di lavoro o altro soggetto giuridico fornitore di servizi di assistenza domiciliare, che rispetti i medesimi requisiti di durata e numero minimo di ore settimanali.
            Condizioni per ricevere il Buono domiciliaritÃ  - Assistenza educativa professionale
            </li>
          </ul>
        </p>
        <p>
          Per l'erogazione del "Buono DomiciliaritÃ  - Assistenza educativa professionale" (rivolto esclusivamente a persone con disabilitÃ  minori di etÃ  al momento della presentazione della domanda) occorre che sia attivato un servizio di assistenza educativa con la stipula di 
          <ul>
            <li>
            un incarico professionale, di durata minima di 12 mesi e per un minimo di 8 ore settimanali per la prestazione di un servizio di assistenza educativa professionale da parte di un educatore professionale
            oppure
            </li>
            <li>
            un contratto di prestazione di un servizio di assistenza educativa, reso da un educatore professionale, sottoscritto con idonea cooperativa sociale, agenzia di somministrazione di lavoro o altro soggetto giuridico fornitore di servizi di assistenza educativa, di pari durata e numero minimo di ore di servizio
            </li>
          </ul>

          Il contratto/incarico potrÃ  essere stipulato/affidato, oltre che dal destinatario, anche da un familiare del destinatario o dal suo tutore/curatore/amministratore di sostegno.
        </p>

      </q-scroll-area>
    </q-card>
    <q-toggle
      v-model="consenso_toogle"
      label="*Ho compreso e accetto le condizioni per ricevere il buono"
    />
  </div>
</template>

<script>
export default {
  name: "PrivacyAgreement",
  props: ["saveCurrentPage"],
  components: {},
  data() {
    return {
      step: 0,
      visible: true,
      privacy_toogle: this.$store.state.privacy_toogle,
      consenso_toogle: this.$store.state.consenso_toogle,
    };
  },
  computed: {},
  methods: {
    async saveStepForm() {
      console.log("salvato privacy ");
      // NUMERO DELLO STEP
      // STEP_1, STEP_2 ETC...Â  Â  Â  
      await this.$store.dispatch("setNextStep", {
      bool: true,
      step: 1,
      });
      await this.$store.dispatch("setStep", 1)
    },
  },
  watch: {
    privacy_toogle() {
      this.$store.commit("SET_PRIVACY", this.privacy_toogle);
    },
    consenso_toogle() {
      this.$store.commit("SET_AGREMENT", this.consenso_toogle);
    },
    saveCurrentPage: function (newVal, oldVal) {
      this.saveStepForm();
    },
  },
  async created() {
    // SERVE PER RIMETTERE IN CIMA IL COMPONENTE
    window.scrollTo(0, 0);
  },
};
</script>

<style lang="sass">
.q-stepper
    box-shadow: none
    background: transparent

.q-stepper--horizontal .q-stepper__line:after
  height: 3px
  background: #1976d2

.q-stepper--horizontal .q-stepper__line:before
  height: 3px
  background-color: #1976d2

.q-stepper__tab
  color: #1976d2

.q-scrollarea
  padding: 16px
</style>

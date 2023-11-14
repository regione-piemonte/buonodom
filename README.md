# Prodotto

Pwa e servizi per la gestione del buono di domiciliarità

## Versione

3.0.0

## Descrizione del prodotto

Si tratta di una PWA e relativi servizi per raccogliere e gestire le richieste di buoni per la domiciliarità da parte dei cittadini. Il prodotto si occupa anche di tutta la parte di gestione dell'istruttoria della domanda del cittadino e della relativa parte di rendicontazione; settore welfare della Regione Piemonte.

Elenco componenti:

* [BUONODOMBANBATCH](buonodombanbatch) batch java per l'invio delle domande del cittadino verso i servizi di gestione bandi
* [BUONODOMBANDISRV](buonodombandisrv) componente server di comunicazione con i servizi di gestione bandi
* [BUONODOMBATCH](buonodombatch)  batch java per la gestione di alcuni passaggi di satto delle domande del cittadino
* [BUONODOMBFF](buonodombff)  API per la gestione della raccolta delle domande e delle rendicontazioni da parte del cittadino
* [BUONODOMBO](buonodombo)  API per la gestione dell'istruttoria della domanda da parte di operatori regionali e di back office
* [BUONODOMCALLBAN](buonodomcallban) API di callback per i servizi di gestione bandi 
* [BUONODOMSRV](buonodomsrv)    API per iservizi in comune tra la web app per il cittadino e il back office 
* [BUONODOMSTARDA](buonodomstarda) API di callback per i servizi del protocollo
* [BUONODOMWCL](buonodomwcl)     Componente web app per la web app del front office

LINK ai repository:xxx

## Configurazioni iniziali

Si rimanda ai readme delle singole componenti

* [BUONODOMBANBATCH](buonodombanbatch/README.md)
* [BUONODOMBANDISRV](buonodombandisrv/README.md)
* [BUONODOMBATCH](buonodombatch/README.md)
* [BUONODOMBFF](buonodombff/README.md)
* [BUONODOMBO](buonodombo/README.md)
* [BUONODOMCALLBAN](buonodomcallban/README.md)
* [BUONODOMSRV](buonodomsrv/README.md)
* [BUONODOMSARDA](buonodomstarda/README.md)
* [BUONODOMWCL](buonodomwcl/README.md)

## Prerequisiti di sistema

Server Web:
Apache 2.4.54

Application Server:
Wildfly 23

Tipo di database:
postgres v12

Dipendenze elencate nella cartella docs/wsdl

## Versioning (Obbligatorio)

Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).

## Authors

* [Egidio Bosio](https://github.com/egidio-bosio)
* Annarita Losurdo
* Maurizio Peisino


## Copyrights

“© Copyright Regione Piemonte – 2023”

## License

SPDX-License-Identifier: inserire il codice SPDX delle licenza
Veder il file LICENSE per i dettagli.
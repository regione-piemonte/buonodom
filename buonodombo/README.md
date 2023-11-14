# Componente di Prodotto

BUONODOMBO

## Versione

1.8.0

## Descrizione del prodotto

Si tratta dei servizi REST di backend for frontend (bff) utilizzati dalla componente web [buonodomwcl](../buonodomwcl). Si tratta della pwa di gestione dell'istruttoria delle domande di buono domiciliarità effettuata da operatori di back office e dagli istruttori regionali.

## Configurazioni iniziali

Utilizza i servizi della componente [buonodomsrv](../buonodomsrv/), [buonodombandisrv](../buonodombandisrv/) del prodotto medesimo.
Per generare il pacchetto lanciare il comando ant -Dtarget prod.

## Prerequisiti di sistema

Java:
Jdk 11

ANT:
Ant version 1.9.6

Server Web:
Apache 2.4.54

Application Server:
Wildfly 23

Tipo di database:
postgres v12

## Installazione

Per generare il pacchetto lanciare il comando ant -Dtarget prod  per generare l'ear

## Deployment

Inserire il file ear generato durante l'installazione sotto la cartella deployments del Wildfly

## Versioning

Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).

## Authors

* Annarita Losurdo
* [Egidio Bosio](https://github.com/egidio-bosio)


## Copyrights

“© Copyright Regione Piemonte – 2023”
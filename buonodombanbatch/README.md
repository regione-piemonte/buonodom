# Componente di Prodotto

BUONODOMBANBATCH

## Versione

1.1.0

## Descrizione del prodotto

Si tratta di batch sviluppato con linguaggio java che serve a mandare le domande e le rendicontazioni di buoni per la domiciliarità effettuate dai cittadini al sistema di gestione bandi.

## Configurazioni iniziali

I servizi richiamati da questo batch sono i servizi della componente [buonodombandisrv](../buonodombandisrv/) del prodotto medesimo.

## Prerequisiti di sistema

Java:
Jdk 11

ANT:
Ant version 1.9.6

Application Server:
Java stand alone application

Tipo di database:
postgres v12

## Installazione

Per generare il pacchetto lanciare il comando ant -Dtarget prod  per generare l'alberatura di deploy

## Deployment

Impostare il classpath alla cartella lib del progetto e far partire il batch lanciando il comando: $JAVA_HOME/bin/java it.csi.buonodom.buonodombanbatch.Main 

## Versioning

Per il versionamento del software si usa la tecnica Semantic Versioning (http://semver.org).

## Authors

* Annarita Losurdo
* [Egidio Bosio](https://github.com/egidio-bosio)



## Copyrights

“© Copyright Regione Piemonte – 2023”
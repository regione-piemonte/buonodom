/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombanbatch.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checker {

	/**
	 * Verifica che date (se valorizzata) sia una data valida nel formato passato
	 * come pattern nello specifico locale Se la data non e' valida return false.
	 * 
	 * @param data
	 * @param pattern
	 * @param locale
	 * @return
	 */
	public static boolean isData(String data, String pattern, Locale locale) {
		boolean value = true;
		if (!Checker.isValorizzato(data))
			return true;

		SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
		sdf.setLenient(false);

		Date dataLetta = null;
		try {
			dataLetta = sdf.parse(data);
		} catch (ParseException pe) {
			value = false;
		}

		if (value == true) {
			String dataFormattata = sdf.format(dataLetta);
			value = data.equals(dataFormattata);
		}

		return value;
	}

	/**
	 * Controllo di valorizzazione su di una stringa. Se null o vuoto ritorna false.
	 * 
	 * @param stringa
	 * @return
	 */
	public static boolean isValorizzato(String stringa) {
		if (stringa == null)
			return false;

		if (stringa.trim().length() == 0)
			return false;

		return true;
	}

	/**
	 * Controllo di valorizzazione di un long. Se � zero return false.
	 * 
	 * @param num num
	 * @return boolean
	 */
	public static boolean isValorizzato(long num) {
		if (num == 0)
			return false;
		return true;
	}

	/**
	 * Verifica che il codice fiscale (se valorizzato) sia corretto. Se e' errato
	 * return false.
	 * 
	 * @param codice
	 * @return boolean
	 */

	/**
	 * Verifica che la stringa data in input sia una stringa Alfabetica. Se si
	 * tratta di stringa numerica ritorna true , altrimenti ritorna false.
	 * 
	 * @param string String
	 * @return boolean valore di verita'
	 */
	public static boolean isAlfabeticString(String string) {
		boolean matchFound = false;
		String patternStr = "^[a-zA-Z]+$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(string.subSequence(0, string.length()));
		matchFound = matcher.find();
		return matchFound;
	}

	/**
	 * Verifica se le posizioni destinate ai caratteri numerici del codice fiscale
	 * contengano cifre o i caratteri alfabetici previsti in caso di omocodia.
	 * 
	 * @param codiceFiscale Codice Fiscale da verificare
	 * @return vero se il codice fiscale (in caso di omocodia) e' corretto
	 *         limitatamente alle posizioni per le cifre, false altrimenti.
	 */

	private static boolean chkCodiceFiscaleInOmocodia(String codiceFiscale) {

		if ((!isNumericString(codiceFiscale.substring(6, 8))) || (!isNumericString(codiceFiscale.substring(9, 11)))
				|| (!isNumericString(codiceFiscale.substring(12, 15)))) {

			StringBuffer nuovoCodiceFiscale = new StringBuffer();
			nuovoCodiceFiscale.append(codiceFiscale.substring(0, 6));

			if (!isNumericString(codiceFiscale.substring(6, 8))) {
				// C'e' un carattere alfabetico nell'anno del Codice Fiscale
				String annoCF = codiceFiscale.substring(6, 8);
				for (int i = 0; i < annoCF.length(); i++) {
					if (!isNumericString(String.valueOf(annoCF.charAt(i)))) {
						String numeroAnno = (String) getCaratterePerOmocodia(String.valueOf(annoCF.charAt(i)));
						if (numeroAnno == null) {
							return false;
						}
					}
				}
			}

			if (!isNumericString(codiceFiscale.substring(9, 11))) {
				// C'e' un carattere alfabetico nel giorno del Codice Fiscale
				String giornoCF = codiceFiscale.substring(9, 11);
				for (int i = 0; i < giornoCF.length(); i++) {
					if (!isNumericString(String.valueOf(giornoCF.charAt(i)))) {
						String numeroGiorno = (String) getCaratterePerOmocodia(String.valueOf(giornoCF.charAt(i)));
						if (numeroGiorno == null) {
							return false;
						}
					}
				}
			}

			if (!isNumericString(codiceFiscale.substring(12, 15))) {
				// C'e' un carattere alfabetico nel comune del Codice Fiscale
				String comuneCF = codiceFiscale.substring(12, 15);
				for (int i = 0; i < comuneCF.length(); i++) {
					if (!isNumericString(String.valueOf(comuneCF.charAt(i)))) {
						String numeroComune = (String) getCaratterePerOmocodia(String.valueOf(comuneCF.charAt(i)));
						if (numeroComune == null) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	public static boolean isDataItalian(String data) {
		return isData(data, "dd/MM/yyyy", null);
	}

	/**
	 * Verifica che la stringa data in input sia una stringa numerica. Se si tratta
	 * di stringa numerica ritorna true , altrimenti ritorna false.
	 * 
	 * @param string String
	 * @return boolean
	 */
	public static boolean isNumericString(String string) {
		boolean matchFound = false;
		String patternStr = "^[0-9]+$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(string.subSequence(0, string.length()));
		matchFound = matcher.find();
		return matchFound;
	}

	/**
	 * Metodo che realizza la conversione dei caratteri alfabetici in cifre ai fini
	 * del codice fiscale in caso di omocodia. Dato il carattere alfabetico in
	 * formato String restituisce la cifra corrispondente in formato String.
	 * 
	 * @param key carattere alfabetico da convertire
	 * @return String - la cifra risultato della conversione
	 * 
	 * @version 0.1
	 */
	private static String getCaratterePerOmocodia(String key) {
		Hashtable<String, String> carattereOmocodia = new Hashtable<String, String>();

		carattereOmocodia.put("L", new String("0"));
		carattereOmocodia.put("M", new String("1"));
		carattereOmocodia.put("N", new String("2"));
		carattereOmocodia.put("P", new String("3"));
		carattereOmocodia.put("Q", new String("4"));
		carattereOmocodia.put("R", new String("5"));
		carattereOmocodia.put("S", new String("6"));
		carattereOmocodia.put("T", new String("7"));
		carattereOmocodia.put("U", new String("8"));
		carattereOmocodia.put("V", new String("9"));

		return carattereOmocodia.get(key);
	}

	/**
	 * Questo metodo controlla la coerenza tra il codice fiscale (anche in caso di
	 * omocodia) passato come parametro e i dati anagrafici del soggetto. Nel caso
	 * in cui il codice fiscale sia coerente con i dati anagrafici passati, ritorna
	 * true. Esegue preliminarmente un controllo della validita' sintattica del
	 * codice fiscale inserito. Se il codice fiscale passato non e' valido
	 * sintatticamente, il metodo ritorna false. Se uno dei parametri in input e'
	 * null, stringa vuota o non valido il metodo restituisce false. Nell'ordine i
	 * parametri da passare sono: codice fiscale (String), cognome (String), nome
	 * (String), sesso (String), data di nascita (Date) e codice catastale del
	 * comune di nascita (String).
	 * 
	 * @param codiceFiscale codice fiscale della persona
	 * @param cognome       cognome della persona
	 * @param nome          nome della persona
	 * @param sesso         sesso della persona M o F
	 * @param data          data di nascita della persona
	 * @param comune        codice catastale del comune di nascita della persona
	 * @return esito del controllo
	 * 
	 * @version 0.1
	 */

	/**
	 * Metodo privato che decodifica opportunamente il codice fiscale passato come
	 * parametro in caso di omocodia.
	 * 
	 * @param codiceFiscale
	 * @return codiceFiscaleDecodificato
	 */
	private static String getCodiceFiscaleDecodificato(String codiceFiscale) {

		// creo uno StringBuffer nel quale costruire' il nuovo codice fiscale.
		// Nell'eventualita'
		// in cui uno dei sette caratteri numerici del codice fiscale contenga
		// delle lettere,
		// decodifico la lettera in numero. In caso contrario il metodo
		// restituisce
		// il codice fiscale passato come input

		if ((!isNumericString(codiceFiscale.substring(6, 8))) || (!isNumericString(codiceFiscale.substring(9, 11)))
				|| (!isNumericString(codiceFiscale.substring(12, 15)))) {

			codiceFiscale = codiceFiscale.toUpperCase();

			StringBuffer nuovoCodiceFiscale = new StringBuffer();
			nuovoCodiceFiscale.append(codiceFiscale.substring(0, 6));

			if (!isNumericString(codiceFiscale.substring(6, 8))) {
				// C'e' un carattere alfabetico nell'anno del Codice Fiscale
				String annoCF = codiceFiscale.substring(6, 8);
				for (int i = 0; i < annoCF.length(); i++) {
					if (!isNumericString(String.valueOf(annoCF.charAt(i)))) {
						String numeroAnno = (String) getCaratterePerOmocodia(String.valueOf(annoCF.charAt(i)));
						if (numeroAnno == null) {
							numeroAnno = " ";
						}
						nuovoCodiceFiscale.append(numeroAnno);
					} else {
						nuovoCodiceFiscale.append(annoCF.charAt(i));
					}
				}
			} else {
				nuovoCodiceFiscale.append(codiceFiscale.substring(6, 8));
			}

			nuovoCodiceFiscale.append(codiceFiscale.substring(8, 9));

			if (!isNumericString(codiceFiscale.substring(9, 11))) {
				// C'e' un carattere alfabetico nel giorno del Codice Fiscale
				String giornoCF = codiceFiscale.substring(9, 11);
				for (int i = 0; i < giornoCF.length(); i++) {
					if (!isNumericString(String.valueOf(giornoCF.charAt(i)))) {
						String numeroGiorno = (String) getCaratterePerOmocodia(String.valueOf(giornoCF.charAt(i)));
						if (numeroGiorno == null) {
							numeroGiorno = " ";
						}
						nuovoCodiceFiscale.append(numeroGiorno);
					} else {
						nuovoCodiceFiscale.append(giornoCF.charAt(i));
					}
				}
			} else {
				nuovoCodiceFiscale.append(codiceFiscale.substring(9, 11));
			}

			nuovoCodiceFiscale.append(codiceFiscale.substring(11, 12));

			if (!isNumericString(codiceFiscale.substring(12, 15))) {
				// C'e' un carattere alfabetico nel comune del Codice Fiscale
				String comuneCF = codiceFiscale.substring(12, 15);
				for (int i = 0; i < comuneCF.length(); i++) {
					if (!isNumericString(String.valueOf(comuneCF.charAt(i)))) {
						String numeroComune = (String) getCaratterePerOmocodia(String.valueOf(comuneCF.charAt(i)));
						if (numeroComune == null) {
							numeroComune = " ";
						}
						nuovoCodiceFiscale.append(numeroComune);
					} else {
						nuovoCodiceFiscale.append(comuneCF.charAt(i));
					}
				}
			} else {
				nuovoCodiceFiscale.append(codiceFiscale.substring(12, 15));
			}

			nuovoCodiceFiscale.append(codiceFiscale.substring(15, 16));
			codiceFiscale = nuovoCodiceFiscale.toString();
		}

		return codiceFiscale;
	}

	/**
	 * Metodo che calcola il codice fiscale in base ai dati anagrafici passati. Se
	 * uno dei parametri in input e' null, stringa vuota o non valido il metodo
	 * restituisce una stringa vuota. Nell'ordine i parametri da passare sono:
	 * cognome (String), nome (String), sesso (String), data di nascita (Date) e
	 * codice catastale del comune di nascita (String).
	 * 
	 * @param cognome cognome della persona
	 * @param nome    nome della persona
	 * @param sesso   sesso della persona M o F
	 * @param data    data di nascita della persona
	 * @param comune  codice catastale del comune di nascita della persona
	 * @return String - il codice fiscale calcolato in base a parametri in input
	 * 
	 */

	/**
	 * Passata una stringa in input, ritorna le sole consonanti o vocali delle
	 * stringa ai fini del calcolo del codice fiscale.
	 * <p>
	 * Per ottenere le vocali, passare conson = false <br/>
	 * Per ottenere le consonanti passare conson = true
	 * 
	 * @param stringa La stringa per la quale si vogliono ottenere le sole
	 *                consonanti o vocali
	 * @param cod     puo' essere false o true a seconda che si vogliano ottenere le
	 *                vocali o le consonanti della stringa
	 * @return String - La stringa contente le solo vocali o consonanti della
	 *         stringa passata in input
	 */
	private static String ottieniConsVoc(String stringa, boolean conson) {
		StringBuilder testo = new StringBuilder();
		int i = 0;
		char[] valChar = stringa.toCharArray();
		for (i = 0; i < valChar.length; i++) {
			if (Pattern.compile("[AEIOU]").matcher(String.valueOf((valChar[i]))).matches() ^ conson) {
				testo.append(valChar[i]);
			}
		}
		return testo.toString();
	}

	/**
	 * Restituisce il codice del mese ai fini del calcolo del codice fiscale.
	 * L'indice mese va da 0 a 11. 0 Gennaio, 1 Febbraio, ..., 11 Dicembre.
	 * 
	 * @param mese - intero da 0 a 11
	 * @return char - carattere associato al mese
	 * 
	 */
	private static char getCodiceMese(int mese) {
		char[] codiciMesi = { 'A', 'B', 'C', 'D', 'E', 'H', 'L', 'M', 'P', 'R', 'S', 'T' };
		return codiciMesi[mese];
	}

	/**
	 * Converte il carattere in base al carattere stesso e alla posizione ai fini
	 * del calcolo del codice fiscale.
	 * 
	 * @param pari
	 * @param dispari
	 * @return int
	 * 
	 */
	private static int convertiCaratteriPosizioneDispari(int pari, int dispari) {
		int[][] caratteri = {
				{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
						20, 21, 22, 23, 24, 25 },
				{ 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12,
						14, 16, 10, 22, 25, 24, 23 } };
		return caratteri[pari][dispari];
	}

	/**
	 * Verifica che l'indirizzo email passato come parametro sia sintatticamente
	 * corretto.
	 * 
	 * @param eMail indirizzo email da verificare
	 * @return boolean valore di verit�
	 */
	public static boolean isValidEmail(String eMail) {
		boolean matchFound = false;
		String patternStr = "^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(eMail);
		matchFound = matcher.find();
		return matchFound;
	}

	/**
	 * Verifica che l'url passata come parametro sia sintatticamente corretta.
	 * 
	 * @param url indirizzo url da verificare
	 * @return boolean valore di verit�
	 */
	public static boolean isValidUrl(String url) {
		boolean matchFound = false;
		String patternStr = "^((http|https|ftp)\\://|www\\.)[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,4}(/[a-zA-Z0-9\\-\\._\\?=\\,\\'\\+%\\$#~]*[^\\.\\,\\)\\(\\s])*$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(url);
		matchFound = matcher.find();
		return matchFound;
	}

	/**
	 * Verifica che l'anno passato come parametro sia sintatticamente corretto.
	 * 
	 * @param year anno da verificare
	 * @return boolean valore di verit�
	 */
	public static boolean isValidYear(String year) {
		boolean matchFound = false;
		String patternStr = "^([0-9]{4})$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(year);
		matchFound = matcher.find();
		return matchFound;
	}

	/**
	 * Verifica che il path passato come parametro sia sintatticamente corretto.
	 * 
	 * @param path percorso da verificare
	 * @return boolean valore di verit�
	 */
	public static boolean isValidPath(String path) {
		boolean matchFound = false;
		String patternStr = "([A-Z]:\\[^/:\\*\\?<>\\|]+\\.\\w{2,6})|(\\{2}[^/:\\*\\?<>\\|]+\\.\\w{2,6})";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(path);
		matchFound = matcher.find();
		return matchFound;
	}

	/**
	 * Restituisce vero se la partita IVA � corretta, false altrimenti. Ritorna
	 * false anche se la lunghezza � diversa da 11.
	 * 
	 * @param partitaIVA - la partita IVA da controllare.
	 * @return vero se corretta, false altrimenti.
	 */
	public static boolean isPartitaIVA(String partitaIVA) {
		int i, c, s;
		if (partitaIVA.length() != 11)
			return false;
		for (i = 0; i < 11; i++) {
			if (partitaIVA.charAt(i) < '0' || partitaIVA.charAt(i) > '9')
				return false;
		}
		s = 0;
		for (i = 0; i <= 9; i += 2)
			s += partitaIVA.charAt(i) - '0';
		for (i = 1; i <= 9; i += 2) {
			c = 2 * (partitaIVA.charAt(i) - '0');
			if (c > 9)
				c = c - 9;
			s += c;
		}
		if ((10 - s % 10) % 10 != partitaIVA.charAt(10) - '0')
			return false;
		else
			return true;
	}

	/**
	 * Verifica che la stringa contenga i caratteri validi per un nome o un cognome
	 * o altre stringhe del genere quindi non valgono i numeri n� i simboli
	 * eccetto l'apice ed il trattino (es: DELL'OLIO oppure DI-GEGLIE). I caratteri
	 * accentati non sono validi (es: cal� va scritto calo') Il blank � valido
	 * (es: EDOARDO FILIPPO). Non � un controllo di obbligatoriet� quindi se non
	 * valorizzato return true.
	 * 
	 * @param string String
	 * @return boolean
	 */
	public static boolean isNoSymbolNoNumberString(String string) {
		if (!isValorizzato(string))
			return true;
		return string.matches("[a-zA-Z\\s'-]+");
	}

	/**
	 * Verifica che string sia un intero.
	 * 
	 * @param string string
	 * @return true se string � un intero, false altrimenti
	 */
	public static boolean isInt(String string) {
		if (!isValorizzato(string))
			return false;

		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public static boolean isNumber15_3(BigDecimal bigDecimal) {
		return isNumberM_N(bigDecimal, 15, 3);
	}

	public static boolean isNumber15_3(Double d) {
		return isNumberM_N(BigDecimal.valueOf(d), 15, 3);
	}

	public static boolean isNumberM_N(Double d, int precision, int scale) {
		return isNumberM_N(BigDecimal.valueOf(d), precision, scale);
	}

	public static boolean isNumberM_N(BigDecimal bigDecimal, int precision, int scale) {
		if (bigDecimal == null) {
			return true;
		}

		bigDecimal.stripTrailingZeros();
		return bigDecimal.precision() <= precision && bigDecimal.scale() <= scale;
	}

}
/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombatch.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class Converter {

	public static boolean isValorizzato(String stringa) {
		if (stringa == null || stringa.trim().length() == 0)
			return false;
		return true;
	}

	/**
	 * Converte una stringa in un intero. Se la stringa non e' valorizzata
	 * restituisce 0
	 *
	 * @param stringa
	 * @return
	 */
	public static int getInt(String stringa) {
		if (isValorizzato(stringa))
			return Integer.parseInt(stringa);
		else
			return 0;
	}

	/**
	 * Converte una stringa in un long. Se la stringa non e' valorizzata restituisce
	 * 0
	 *
	 * @param stringa
	 * @return
	 */
	public static long getLong(String stringa) {
		if (isValorizzato(stringa))
			return Long.parseLong(stringa);
		else
			return 0;
	}

	/**
	 * Converte una stringa in un double. Se la stringa non e' valorizzata
	 * restituisce 0
	 *
	 * @param stringa
	 * @return
	 */
	public static double getDouble(String stringa) {
		if (isValorizzato(stringa))
			return Double.parseDouble(stringa);
		else
			return 0;
	}

	/**
	 * Converte una data in formato Date in una data in formato string in base al
	 * pattern e al locale forniti
	 *
	 * @param data
	 * @param pattern ad esempio "dd/MM/yyyy"
	 * @param locale
	 * @return
	 */
	public static String getData(Date data, String pattern) {
		if (data == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String dataString = sdf.format(data);
		return dataString;
	}

	/**
	 * Converte una data in formato string in una data in formato Date. Controlla: -
	 * se il parametro in ingresso e' valorizzato - se la stringa rispetta il
	 * formato passato come pattern - se il giorno e il mese rispettano la
	 * realta'(Es: mese non maggiore di 12, giorni di febbraio nn superiori a 28)
	 *
	 * @param data
	 * @param pattern ad esempio "dd/MM/yyyy"
	 * @param locale
	 * @return
	 */
	public static Date getData(String data, String pattern, Locale locale) {
		if (!Checker.isValorizzato(data))
			return null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
			sdf.setLenient(false);
			Date dataDate = sdf.parse(data);

			return dataDate;
		} catch (ParseException pe) {
			// throw new ApplicationException("Errore di conversione da stringa a data.");
			return null;
		}
	}

	/**
	 * Converte una data in formato string in una data in formato Date. Controlla: -
	 * se il parametro in ingresso � valorizzato - se la stringa rispetta il
	 * formato "dd/MM/yyyy" - se il giorno e il mese rispettano la realt�(Es: mese
	 * non maggiore di 12, giorni di febbraio nn superiori a 28)
	 *
	 * @param data data
	 * @return data nel formato Date
	 */
	public static Date getData(String data) {
		if (!Checker.isValorizzato(data))
			return null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);
			Date dataDate = sdf.parse(data);

			return dataDate;
		} catch (ParseException pe) {
			// throw new ApplicationException("Errore di conversione da stringa a data.");
			return null;
		}
	}

	/**
	 * Converte una data in formato Timestamp in una data in formato Date.
	 *
	 * @param data data
	 * @return data nel formato Date
	 */

	public static Date getData(Timestamp data) {
		return new Date(data.getTime());
	}

	public static Timestamp convertStringToTimestamp(String something) throws ParseException {
		SimpleDateFormat dateFormat = null;
		dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		Timestamp timestamp = null;
		Date parsedDate;

		parsedDate = dateFormat.parse(something);
		timestamp = new java.sql.Timestamp(parsedDate.getTime());

		return timestamp;
	}

	/**
	 * Restituisce giorno in formato string. (copiato perch� nelle sanita il
	 * Convertitore non � visibile)
	 *
	 * @param data data
	 * @return giorno
	 */
	public static String getGiorno(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(data);
	}

	/**
	 * Restituisce mese in formato string. (copiato perch� nelle sanita il
	 * Convertitore non � visibile)
	 *
	 * @param data data
	 * @return mese
	 */
	public static String getMese(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return sdf.format(data);
	}

	/**
	 * Restituisce anno in formato string. (copiato perch� nelle sanita il
	 * Convertitore non � visibile)
	 *
	 * @param data data
	 * @return anno
	 */
	public static String getAnno(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(data);
	}

	/**
	 * Restituisce lo stesso valore in input se pieno, se null restituisce "".
	 *
	 * @param value
	 * @return
	 */
	public static String getValoreStringa(String value) {
		if (value == null)
			value = "";
		return value;
	}

	/**
	 * Restituisce il valore intero del parametro passato se pieno se null
	 * restituisce 0.
	 *
	 * @param value
	 * @return
	 */
	public static int getValoreIntero(String param) {
		int value;
		if (param == null)
			value = 0;
		else
			value = Integer.parseInt(param);
		return value;
	}

	/**
	 * Converte una data in formato Date in una data in formato string. (copiato
	 * perch� nelle sanita il Convertitore non � visibile)
	 *
	 * @param data data
	 * @return data nel formato String
	 */
	public static String getData(Date data) {
		if (data == null)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataString = sdf.format(data);
		return dataString;
	}

	public static XMLGregorianCalendar getXMLGregorianCalendar(Date data) {
		if (data == null) {
			return null;
		}

		GregorianCalendar c = new GregorianCalendar();
		c.setTime(data);
		try {
			XMLGregorianCalendar t = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			t.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
			return t;
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date getData(XMLGregorianCalendar calendar) {
		if (calendar == null) {
			return null;
		}

		return calendar.toGregorianCalendar().getTime();
	}

	public static String getCodComuneByCodFiscale(String codFiscale) {
		return codFiscale.substring(11, 15);
	}

	public static java.sql.Date getDate(java.util.Date date) {
		java.util.Date utilDate = date;
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		return sqlDate;
	}

	public static Date aggiungiAnnoAData(Date dataScadenzaParametrizzata, int numAnni) {
		Calendar c = Calendar.getInstance();
		c.setTime(dataScadenzaParametrizzata);
		c.add(Calendar.YEAR, numAnni);

		return c.getTime();
	}

	public static String getDataToString(XMLGregorianCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		Date d = calendar.toGregorianCalendar().getTime();
		return getData(d);
	}

}

/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodomsrv.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	private static final String BASIC_AUTH = "Basic";

	public static boolean isValorizzato(String stringa) {
		if (stringa == null || stringa.equalsIgnoreCase("null"))
			return false;

		if (stringa.trim().length() == 0)
			return false;

		return true;
	}

	public static boolean isValorizzato(long num) {
		if (num == 0)
			return false;
		return true;
	}

	public static boolean isData(String data, String pattern, Locale locale) {
		boolean value = false;
		if (!isValorizzato(data))
			return false;
		SimpleDateFormat sdf;
		if (locale != null)
			sdf = new SimpleDateFormat(pattern, locale);
		else
			sdf = new SimpleDateFormat(pattern);

		sdf.setLenient(false);

		Date dataLetta = null;
		try {
			dataLetta = sdf.parse(data);
		} catch (ParseException pe) {
			value = true;
		}

		return value;
	}

	public static boolean isDataItalian(String data) {
		return isData(data, "dd/MM/yyyy", null);
	}

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

	public static boolean isXForwardedForValido(String xForwardedFor) {
		if (!xForwardedFor.contains(","))
			return isIpAddressValido(xForwardedFor);
		String[] split = xForwardedFor.split(Pattern.quote(","));
		for (String s : split) {
			s = s.trim();
			if (!isIpAddressValido(s)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNumericString(String string) {
		boolean matchFound = false;
		String patternStr = "^[0-9]+$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(string.subSequence(0, string.length()));
		matchFound = matcher.find();
		return matchFound;
	}

	public static boolean isIpAddressValido(String ip) {
		String[] split = ip.split(Pattern.quote("."));
		if (split.length != 4)
			return false;
		for (String s : split) {
			if (!isNumericString(s))
				return false;
		}
		return true;
	}

	public static boolean isUuidValido(String uuid) {
		if (uuid == null)
			return false;
		if (uuid.length() != 36)
			return false;
		String[] split = uuid.split(Pattern.quote("-"));
		if (split.length != 5)
			return false;
		if (split[0].length() != 8 || split[1].length() != 4 || split[2].length() != 4 || split[3].length() != 4
				|| split[4].length() != 12)
			return false;
		return true;
	}

	public static boolean stringToBoolean(String valore, String costante) {
		return valore.equalsIgnoreCase(costante) ? true : false;
	}

	public static <T> List<T> subList(List<T> list, Integer offset, Integer limit) {
		if (offset == null || offset < 0)
			offset = 0;
		if (limit == null || limit < -1)
			limit = -1;

		if (offset > 0) {
			if (offset >= list.size()) {
				return list.subList(0, 0); // return empty.
			}
			if (limit > -1) {
				// apply offset and limit
				return list.subList(offset, Math.min(offset + limit, list.size()));
			} else {
				// apply just offset
				return list.subList(offset, list.size());
			}
		} else if (limit > -1) {
			// apply just limit
			return list.subList(0, Math.min(limit, list.size()));
		} else {
			return list.subList(0, list.size());
		}
	}

	public static String booleanToString(boolean valore) {
		return Boolean.valueOf(valore).toString();
	}

	public static final String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = username + ":" + password;
		return BASIC_AUTH + " " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}

	/**
	 * @param in byte[]
	 * @return boolean - per file di pochi byte potrebbe restituire un falso
	 *         positivo - per immagini o pdf, (kb o Mb), se restituisce false,
	 *         sicuramente il file non e' codificato Base64
	 */
	public static boolean isBase64Encoded(byte[] in) {
		try {
			Base64.getDecoder().decode(in);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public static Date getStartOfDay(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			return calendar.getTime();
		}
		return null;
	}

	public static Date getEndOfDay(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			return calendar.getTime();
		}
		return null;
	}

	public static boolean isAgeBetween18and65(String data) {

		LocalDate dataNascita = LocalDate.parse(data);

		int eta = Period.between(dataNascita, LocalDate.now()).getYears();

		if (eta > 18 && eta < 65) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean isAttestazioneIsee(BigDecimal isee, String disabile) {

		if (isee != null && disabile != null && !disabile.isEmpty()) {
			if (disabile.equals(Constants.PERSONA_DISABILE)) {
				return isee.intValue() > Constants.ATTESTAZIONE_ISEE_DISABILE;
			} else {
				return isee.intValue() > Constants.ATTESTAZIONE_ISEE_NON_DISABILE;
			}

		}

		return false;

	}
}

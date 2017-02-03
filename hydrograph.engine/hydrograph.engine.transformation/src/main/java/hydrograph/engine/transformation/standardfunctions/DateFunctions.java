/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 *******************************************************************************/
package hydrograph.engine.transformation.standardfunctions;

import hydrograph.engine.transformation.standardfunctions.helper.StandardFunctionHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The class DateFunctions provides various Date and Time related functions.
 *
 */
public class DateFunctions {

	/**
	 * Returns number of days since 1st January 1900
	 * 
	 * @return number of days since 1st January 1900
	 */
	public static <T> Integer today() {
		int date = 0;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");
			Date d1 = formatter.parse("1 01 1900");
			Date d2 = new Date();
			int DateFraction = (1000 * 60 * 60 * 24);
			date = (int) ((d2.getTime() - d1.getTime()) / DateFraction);
		} catch (Exception e) {
			// since we are parsing static date value, parse function will never
			// throw exception
		}
		return date;
	}

	/**
	 * Returns current date and time in yyyyMMddHmmssS format
	 * 
	 * @return current date and time in yyyyMMddHmmssS format
	 */
	@SuppressWarnings("unchecked")
	public static <T> T now() {
		return (T) now("yyyyMMddHmmssS");
	}

	/**
	 * Returns current date and time in the required format
	 * 
	 * @param dateFormat the format for the date
	 * @return current date and time in the required format
	 */
	public static String now(String dateFormat) {
		DateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date();
		return sdf.format(date);
	}

	/**
	 * Formats and converts a date value into string representation in the
	 * desired new date format
	 * 
	 * @param inputValue
	 *            the date value in old format
	 * @param oldFormat
	 *            the date format of the date passed in {@code inputValue}
	 * @param newFormat
	 *            the desired date format
	 * @return string representation of date in new date format
	 *         <p>
	 *         the method returns null if any parameter is null
	 * @deprecated This method is deprecated, Use
	 *             {@link DateFunctions#dateFormatter(String inputValue, String oldFormat, String newFormat)}
	 *             instead
	 * @throws ParseException
	 *             if the date value passed in {@code inputValue} does not match
	 *             the {@code oldFormat}
	 */
	@Deprecated
	public static <T> String dateFormatter(T inputValue, String oldFormat, String newFormat) throws ParseException {
		if (inputValue == null || oldFormat == null || newFormat == null)
			return null;

		String oldDateString = String.valueOf(StandardFunctionHelper.convertComparableObjectToString(inputValue));
		String newDateString = null;

		SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
		sdf.setLenient(false);
		Date date = null;
		date = sdf.parse(oldDateString);
		sdf.applyPattern(newFormat);
		newDateString = sdf.format(date);

		return newDateString;
	}

	/**
	 * Formats and converts a date value into string representation in the
	 * desired new date format
	 * 
	 * @param inputValue
	 *            the date value in old format
	 * @param oldFormat
	 *            the date format of the date passed in {@code inputValue}
	 * @param newFormat
	 *            the desired date format
	 * @return string representation of date in new date format
	 *         <p>
	 *         the method returns null if any parameter is null
	 * @throws ParseException
	 *             if the date value passed in {@code inputValue} does not match
	 *             the {@code oldFormat}
	 */
	public static String dateFormatter(String inputValue, String oldFormat, String newFormat) throws ParseException {
		if (inputValue == null || oldFormat == null || newFormat == null)
			return null;
		String newDateString = null;

		SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
		sdf.setLenient(false);
		Date date = null;
		date = sdf.parse(inputValue);
		sdf.applyPattern(newFormat);
		newDateString = sdf.format(date);

		return newDateString;
	}

	/**
	 * Formats and converts a date value into string representation in the
	 * desired new date format
	 * 
	 * @param inputValue
	 *            the date value in old format
	 * @param oldFormat
	 *            the date format of the date passed in {@code inputValue}
	 * @param newFormat
	 *            the desired date format
	 * @return string representation of date in new date format
	 *         <p>
	 *         the method returns null if any parameter is null
	 * @throws ParseException
	 *             if the date value passed in {@code inputValue} does not match
	 *             the {@code oldFormat}
	 */
	public static String dateFormatter(Date inputValue, String oldFormat, String newFormat) throws ParseException {
		if (inputValue == null || oldFormat == null || newFormat == null)
			return null;
		String newDateString = null;

		SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
		sdf.setLenient(false);
		String date = null;
		date = sdf.format(inputValue);
		sdf.applyPattern(newFormat);
		newDateString = sdf.format(date);

		return newDateString;
	}

	/**
	 * Returns a date object from a string date value
	 * 
	 * @param inputDateInStringFormat
	 *            the date value in string
	 * @param dateFormat
	 *            the date format of the date value passed in
	 *            {@code inputDateInStringFormat}
	 * @return a date object of the corresponding date value
	 *         <p>
	 *         the method returns null if any parameter is null
	 * @deprecated This method is deprecated, Use
	 *             {@link DateFunctions#getDateFromString(String inputDateInStringFormat, String dateFormat)}
	 *             instead
	 * @throws ParseException
	 *             if the date value passed in {@code inputDateInStringFormat}
	 *             does not match the {@code dateFormat}
	 */
	@Deprecated
	public static <T> Date getDateFromString(T inputDateInStringFormat, String dateFormat) throws ParseException {
		if (inputDateInStringFormat == null || dateFormat == null)
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		Date date = sdf.parse((String) inputDateInStringFormat);
		return date;
	}

	/**
	 * Returns a date object from a string date value
	 * 
	 * @param inputDateInStringFormat
	 *            the date value in string
	 * @param dateFormat
	 *            the date format of the date value passed in
	 *            {@code inputDateInStringFormat}
	 * @return a date object of the corresponding date value
	 *         <p>
	 *         the method returns null if any parameter is null
	 * @throws ParseException
	 *             if the date value passed in {@code inputDateInStringFormat}
	 *             does not match the {@code dateFormat}
	 */
	public static Date getDateFromString(String inputDateInStringFormat, String dateFormat) throws ParseException {
		if (inputDateInStringFormat == null || dateFormat == null)
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		Date date = sdf.parse(inputDateInStringFormat);
		return date;
	}

	/**
	 * Returns a string value of the date
	 * 
	 * @param inputDate
	 *            the date to fetch the string value
	 * @param dateFormat
	 *            the date format of the date value passed in {@code inputDate}
	 * @return a string value of the date
	 *         <p>
	 *         the method returns null if any parameter is null
	 * 
	 * @throws ParseException
	 *             if the date value passed in {@code inputDate} does not match
	 *             the {@code dateFormat}
	 */
	public static <T> String getStringDateFromDateObject(T inputDate, String dateFormat) throws ParseException {
		if (inputDate == null || dateFormat == null)
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		String stringDate = sdf.format(inputDate);
		return stringDate;
	}

	/**
	 * Returns a string value of the date
	 * 
	 * @param inputDate
	 *            the date to fetch the string value
	 * @param dateFormat
	 *            the date format of the date value passed in {@code inputDate}
	 * @return a string value of the date
	 *         <p>
	 *         the method returns null if any parameter is null
	 * @throws ParseException
	 *             if the date value passed in {@code inputDate} does not match
	 *             the {@code dateFormat}
	 */
	public static String getStringDateFromDateObject(Date inputDate, String dateFormat) throws ParseException {
		if (inputDate == null || dateFormat == null)
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		String stringDate = sdf.format(inputDate);
		return stringDate;
	}

	/**
	 * Validates the string date value to the format specified
	 * 
	 * @param inputDate
	 *            the date value in string to be validated
	 * @param dateFormat
	 *            the date format to validate the string value against
	 * @return <b>{@code true}</b> if the date value passed in {@code inputDate}
	 *         matches the date format passed in {@code dateFormat}
	 *         <p>
	 *         <b>{@code false}</b> if the date value passed in
	 *         {@code inputDate} does not match the date format passed in
	 *         {@code dateFormat}
	 */
	public static boolean validateStringDate(String inputDate, String dateFormat) {
		if (dateFormat != null && !dateFormat.equals("") && inputDate != null && !inputDate.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			sdf.setLenient(false);
			try {
				sdf.parse(inputDate);
				return true;
			} catch (ParseException e) {
				return false;
			}
		}
		return false;
	}
}
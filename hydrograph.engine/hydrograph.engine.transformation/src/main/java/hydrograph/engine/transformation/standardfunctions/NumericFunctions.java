/*******************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.engine.transformation.standardfunctions;

import static hydrograph.engine.transformation.standardfunctions.StandardFunctionHelper.convertComparableObjectToString;

import java.math.BigDecimal;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumericFunctions {

	/**
	 * Retains just the decimal numbers 0-9 excluding the decimal point from the
	 * {@code inputValue}
	 * 
	 * @param inputValue
	 *            the value from which the decimals are to be retained
	 * @return the decimals from the {@code inputValue}
	 * 
	 * @deprecated This method is deprecated, Use
	 *             {@link NumericFunctions#decimalStrip(String inputValue)}
	 *             instead
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public static <T> T decimalStrip(T inputValue) {
		if (inputValue == null)
			return null;

		String input = StandardFunctionHelper.convertComparableObjectToString(inputValue);

		String filter = input.replaceAll("[^0-9-]", "");
		if (!filter.equals("")) {
			String regx = "^(-)(0+)(.*)|^(0+)(.*)";
			Matcher match = Pattern.compile(regx).matcher(filter);
			if (match.find()) {
				if (match.group(1) != null)
					return (T) (match.group(1) + match.group(3));
				else
					return (T) (match.group(5));
			}
			return (T) (filter);
		}
		return (T) "0";
	}

	/**
	 * Retains just the decimal numbers 0-9 excluding the decimal point from the
	 * {@code inputValue}
	 * 
	 * @param inputValue
	 *            the value from which the decimals are to be retained
	 * @return the decimals from the {@code inputValue}
	 */
	public static String decimalStrip(String inputValue) {
		if (inputValue == null)
			return null;

		String filter = inputValue.replaceAll("[^0-9-]", "");
		if (!filter.equals("")) {
			String regx = "^(-)(0+)(.*)|^(0+)(.*)";
			Matcher match = Pattern.compile(regx).matcher(filter);
			if (match.find()) {
				if (match.group(1) != null)
					return (match.group(1) + match.group(3));
				else
					return (match.group(5));
			}
			return (filter);
		}
		return "0";
	}

	/**
	 * Retains just the decimal numbers 0-9 including decimal point as specified
	 * in {@code decimalPoint} from the {@code inputValue}
	 * 
	 * @param inputValue
	 *            the value from which the decimals are to be retained
	 * @param includeDecimalPoint
	 * @return the decimals from the {@code inputValue}
	 * @deprecated This method is deprecated, Use
	 *             {@link NumericFunctions#decimalStrip(String inputValue, String decimal_point)}
	 *             instead
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public static <T> T decimalStrip(T inputValue, T decimal_point) {
		if (inputValue == null)
			return null;

		String input = StandardFunctionHelper.convertComparableObjectToString(inputValue);

		String filter = input.replaceAll("[^0-9-\\" + decimal_point + "]", "");
		// filter = input.replaceAll("[\\.]+$", "");
		if (!filter.equals("")) {
			String regx = "^(-)(0+)(.*)|^(0+)(.*)";
			Matcher match = Pattern.compile(regx).matcher(filter);
			if (match.find()) {
				if (match.group(1) != null)
					return (T) (match.group(1) + match.group(3));
				else
					return (T) (match.group(5));
			}
			return (T) (filter);
		}
		return (T) "0";
	}

	/**
	 * Retains just the decimal numbers 0-9 including decimal point as specified
	 * in {@code decimalPoint} from the {@code inputValue}
	 * 
	 * @param inputValue
	 *            the value from which the decimals are to be retained
	 * @param includeDecimalPoint
	 * @return the decimals from the {@code inputValue}
	 */
	public static String decimalStrip(String inputValue, String decimal_point) {
		if (inputValue == null)
			return null;

		String filter = inputValue.replaceAll("[^0-9-\\" + decimal_point + "]", "");
		// filter = input.replaceAll("[\\.]+$", "");
		if (!filter.equals("")) {
			String regx = "^(-)(0+)(.*)|^(0+)(.*)";
			Matcher match = Pattern.compile(regx).matcher(filter);
			if (match.find()) {
				if (match.group(1) != null)
					return (match.group(1) + match.group(3));
				else
					return (match.group(5));
			}
			return (filter);
		}
		return "0";
	}

	/**
	 * Returns the absolute value of the argument
	 * 
	 * @param inputValue
	 *            whose absolute value is to be determined
	 * @return the absolute value of the argument
	 */
	@SuppressWarnings("unchecked")
	public static <T> T mathAbs(T inputValue) {
		if (inputValue == null)
			return null;
		if (inputValue.getClass().getCanonicalName().equals("java.math.BigDecimal"))
			return (T) new BigDecimal(inputValue.toString()).abs();
		else if (inputValue.getClass().getCanonicalName().equals("java.lang.Float"))
			return (T) new Float(Math.abs((Float) inputValue));
		else if (inputValue.getClass().getCanonicalName().equals("java.lang.Long"))
			return (T) new Long(Math.abs((Long) inputValue));
		else if (inputValue.getClass().getCanonicalName().equals("java.lang.Integer"))
			return (T) new Integer(Math.abs((Integer) inputValue));
		else if (inputValue.getClass().getCanonicalName().equals("java.lang.Short"))
			return (T) new Short((short) Math.abs((Short) inputValue));
		return (T) Double.valueOf(Math.abs((Double) inputValue));
	}

	/**
	 * Returns a pseudorandom, uniformly distributed {@Code int} value between 0
	 * (inclusive) and the specified value (exclusive), drawn from this random
	 * number generator's sequence.
	 * 
	 * @param n
	 *            the bound on the random number to be returned. Must be
	 *            positive.
	 * @return the next pseudorandom, uniformly distributed int value between
	 *         {@code 0} (inclusive) and {@code n} (exclusive) from this random
	 *         number generator's sequence
	 */
	public static <T> Integer random(T n) {
		int n1 = 0;

		if (n == null)
			return null;

		Random r = new Random();
		if (n instanceof Double)
			n1 = (int) Math.floor((Double) n);
		else if (n instanceof Float)
			n1 = Math.round((Float) n);
		else if (n instanceof BigDecimal)
			n1 = ((BigDecimal) n).intValue();
		else if (n instanceof Long)
			n1 = ((Long) n).intValue();
		else if (n instanceof Short)
			n1 = ((Short) n).intValue();
		else
			n1 = (Integer) n;
		return r.nextInt(n1);
	}

	public static <T> Double getDoubleFromComparable(T inputValue) {
		return Double.parseDouble(convertComparableObjectToString(inputValue));
	}

}
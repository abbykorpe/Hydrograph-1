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
package hydrograph.engine.cascading.coercetype;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import cascading.CascadingException;
import cascading.tuple.type.DateType;
import cascading.util.Util;

public class StrictDateType extends DateType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5271252262132389033L;

	private transient SimpleDateFormat dateFormat;

	public StrictDateType(String dateFormatString) {
		super(dateFormatString);
	}

	@Override
	public SimpleDateFormat getDateFormat() {
		if (dateFormat != null)
			return dateFormat;

		dateFormat = new SimpleDateFormat(dateFormatString, getLocale());
		dateFormat.setLenient(false);
		dateFormat.setTimeZone(getZone());

		return dateFormat;
	}

	private Locale getLocale() {
		if (locale != null)
			return locale;

		return Locale.getDefault();
	}

	private TimeZone getZone() {
		if (zone != null)
			return zone;

		// return TimeZone.getTimeZone("UTC");
		return TimeZone.getDefault();
	}

	@Override
	public Object coerce(Object value, Type to) {
		if (value == null)
			return null;

		Class from = value.getClass();

		if (from != Long.class)
			throw new IllegalStateException("was not normalized");

		// no coercion, or already in canonical form
		if (to == Long.class || to == long.class || to == Object.class)
			return value;

		// coerce is internally called for keyFields in groupBy. 
		// if the data is already in canonical form and type to be coerced is 
		// DateType so no coercion required.
		if (to instanceof DateType) {
			return value;
		}

		if (to == String.class) {
			Calendar calendar = getCalendar();

			calendar.setTimeInMillis((Long) value);

			return getDateFormat().format(calendar.getTime());
		}

		throw new CascadingException(
				"unknown type coercion requested, from: " + Util.getTypeName(from) + " to: " + Util.getTypeName(to));
	}
}

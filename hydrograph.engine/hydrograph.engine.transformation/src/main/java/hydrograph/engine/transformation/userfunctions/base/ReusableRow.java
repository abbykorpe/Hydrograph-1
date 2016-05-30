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
package hydrograph.engine.transformation.userfunctions.base;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

public class ReusableRow implements Comparable<ReusableRow> {

	private ArrayList<String> fields;
	// private LinkedHashMap<String, Comparable> valueMap;

	private ArrayList<Comparable> values;
	private HashMap<String, Integer> fieldPos;

	/**
	 * Instantiates a new ReusableRow object with the provided fields
	 * 
	 * @param fields
	 *            The field names of the row
	 */
	public ReusableRow(ArrayList<String> fields) {
		this.fields = fields;
		values = new ArrayList<Comparable>();
		fieldPos = new HashMap<String, Integer>();
		int i = -1;
		for (String field : fields) {
			i = i + 1;
			values.add(null);
			fieldPos.put(field, new Integer(i));
		}

	}

	/**
	 * Resets all the fields to null
	 */
	public void reset() {
		for (int i = 0; i < values.size(); i++) {
			values.set(i, null);
		}
	}

	/**
	 * Returns all the field names in the row
	 * 
	 * @return An ArrayList<String> containing all the field names
	 */
	public ArrayList<String> getFieldNames() {
		return fields;
	}

	/**
	 * Returns the field name corresponding to the index
	 * 
	 * @param index
	 *            The index whose field name is to be retrieved
	 * @return The field name corresponding to the index
	 */
	public String getFieldName(int index) {
		verifyFieldExists(index);
		return fields.get(index);
	}

	/**
	 * The generic method to set a value to field
	 * 
	 * @param fieldName
	 *            The name of the field
	 * @param value
	 *            The value to set
	 */
	public void setField(String fieldName, Comparable value) {
		verifyFieldExists(fieldName);
		if (value instanceof Date) {
			Date date = (Date) value;
			values.set(fieldPos.get(fieldName), date.getTime());
			// } else if (value == null
			// || (value instanceof String && ((String) value).equals(""))) {
			// valueMap.put(fieldName, null);
		} else {
			values.set(fieldPos.get(fieldName), value);
		}
	}

	/**
	 * The generic method to set a value to field
	 * 
	 * @param index
	 *            The index of the field
	 * @param value
	 *            The value to set
	 */
	public void setField(int index, Comparable value) {
		if (value instanceof Date) {
			Date date = (Date) value;
			values.set(index, date.getTime());
			// } else if (value == null
			// || (value instanceof String && ((String) value).equals(""))) {
			// values.set(index, null);
		} else {
			values.set(index, value);
		}
	}

	/**
	 * A generic method to fetch a field value
	 * 
	 * @param fieldName
	 *            The name of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Comparable getField(String fieldName) {
		verifyFieldExists(fieldName);
		return (Comparable) values.get(fieldPos.get(fieldName));
	}

	/**
	 * A generic method to fetch a field value
	 * 
	 * @param index
	 *            The index of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Comparable getField(int index) {
		verifyFieldExists(index);
		return (Comparable) values.get(index);
	}

	/**
	 * A generic method to fetch a field values as collection instance
	 * 
	 * @return The collection of the fields
	 */
	public Collection<Comparable> getFields() {
		return (Collection<Comparable>) values.clone();
	}

	/**
	 * Fetches a string field value
	 * 
	 * @param fieldName
	 *            The name of the field whose value to be fetched
	 * @return The value of the field
	 */
	public String getString(String fieldName) {
		verifyFieldExists(fieldName);
		return (String) values.get(fieldPos.get(fieldName));
	}

	/**
	 * Fetches a string field value
	 * 
	 * @param index
	 *            The index of the field whose value to be fetched
	 * @return The value of the field
	 */
	public String getString(int index) {
		verifyFieldExists(index);
		return (String) values.get(index);
	}

	/**
	 * Fetches a float field value
	 * 
	 * @param fieldName
	 *            The name of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Float getFloat(String fieldName) {
		verifyFieldExists(fieldName);
		return (Float) values.get(fieldPos.get(fieldName));
	}

	/**
	 * Fetches a float field value
	 * 
	 * @param index
	 *            The index of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Float getFloat(int index) {
		verifyFieldExists(index);
		return (Float) values.get(index);
	}

	/**
	 * Fetches a double field value
	 * 
	 * @param fieldName
	 *            The name of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Double getDouble(String fieldName) {
		verifyFieldExists(fieldName);
		return (Double) values.get(fieldPos.get(fieldName));
	}

	/**
	 * Fetches a double field value
	 * 
	 * @param index
	 *            The index of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Double getDouble(int index) {
		verifyFieldExists(index);
		return (Double) values.get(index);
	}

	/**
	 * Fetches an integer field value
	 * 
	 * @param fieldName
	 *            The name of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Integer getInteger(String fieldName) {
		verifyFieldExists(fieldName);
		return (Integer) values.get(fieldPos.get(fieldName));
	}

	/**
	 * Fetches an integer field value
	 * 
	 * @param index
	 *            The index of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Integer getInteger(int index) {
		verifyFieldExists(index);
		return (Integer) values.get(index);
	}

	/**
	 * Fetches a long field value
	 * 
	 * @param fieldName
	 *            The name of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Long getLong(String fieldName) {
		verifyFieldExists(fieldName);
		return (Long) values.get(fieldPos.get(fieldName));
	}

	/**
	 * Fetches a long field value
	 * 
	 * @param index
	 *            The index of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Long getLong(int index) {
		verifyFieldExists(index);
		return (Long) values.get(index);
	}

	/**
	 * Fetches a short field value
	 * 
	 * @param fieldName
	 *            The name of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Short getShort(String fieldName) {
		verifyFieldExists(fieldName);
		return (Short) values.get(fieldPos.get(fieldName));
	}

	/**
	 * Fetches a short field value
	 * 
	 * @param index
	 *            The index of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Short getShort(int index) {
		verifyFieldExists(index);
		return (Short) values.get(index);
	}

	/**
	 * Fetches a boolean field value
	 * 
	 * @param fieldName
	 *            The name of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Boolean getBoolean(String fieldName) {
		verifyFieldExists(fieldName);
		return (Boolean) values.get(fieldPos.get(fieldName));
	}

	/**
	 * Fetches a boolean field value
	 * 
	 * @param index
	 *            The index of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Boolean getBoolean(int index) {
		verifyFieldExists(index);
		return (Boolean) values.get(index);
	}

	/**
	 * Fetches a big decimal field value
	 * 
	 * @param fieldName
	 *            The name of the field whose value to be fetched
	 * @return The value of the field
	 */
	public BigDecimal getBigDecimal(String fieldName) {
		verifyFieldExists(fieldName);
		return (BigDecimal) values.get(fieldPos.get(fieldName));
	}

	/**
	 * Fetches a big decimal field value
	 * 
	 * @param index
	 *            The index of the field whose value to be fetched
	 * @return The value of the field
	 */
	public BigDecimal getBigDecimal(int index) {
		verifyFieldExists(index);
		return (BigDecimal) values.get(index);
	}

	/**
	 * Sets a date value to the field
	 * 
	 * @param fieldName
	 *            The name of the field to set the value
	 * @param value
	 *            The value to be set
	 */
	public void setDate(String fieldName, Comparable value) {
		verifyFieldExists(fieldName);
		if (value instanceof Date) {
			Date date = (Date) value;
			values.set(fieldPos.get(fieldName), date.getTime());
		} else if (value == null
				|| (value instanceof String && ((String) value).equals(""))) {
			values.set(fieldPos.get(fieldName), null);
		}
	}

	/**
	 * Sets a date value to the field
	 * 
	 * @param index
	 *            The index of the field to set the value
	 * @param value
	 *            The value to be set
	 */
	public void setDate(int index, Comparable value) {
		if (value instanceof Date) {
			Date date = (Date) value;
			values.set(index, date.getTime());
		} else if (value == null
				|| (value instanceof String && ((String) value).equals(""))) {
			values.set(index, null);
		}
	}

	/**
	 * Fetches a date field value
	 * 
	 * @param fieldName
	 *            The name of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Date getDate(String fieldName) throws ParseException {
		verifyFieldExists(fieldName);

		int index = fieldPos.get(fieldName);
		if (values.get(index) != null) {
			Long date = (Long) values.get(index);
			return new Date(date);
		}
		return null;
	}

	/**
	 * Fetches a date field value
	 * 
	 * @param index
	 *            The index of the field whose value to be fetched
	 * @return The value of the field
	 */
	public Date getDate(int index) throws ParseException {
		verifyFieldExists(index);
		if (values.get(index) != null) {
			Long date = (Long) values.get(index);
			return new Date(date);
		}
		return null;
	}

	/**
	 * Fetches a date field value in specified format
	 * 
	 * @param fieldName
	 *            The name of the field whose value to be fetched
	 * @param dateFormat
	 *            The format in which the date value is to be fetched
	 * @return The value of the field as string
	 */
	public String getDate(String fieldName, String dateFormat)
			throws ParseException {
		verifyFieldExists(fieldName);
		int index = fieldPos.get(fieldName);
		if (values.get(index) != null) {
			Long date = (Long) values.get(index);
			DateFormat df = new SimpleDateFormat(dateFormat);
			return df.format(new Date(date));
		}
		return null;
	}

	/**
	 * Fetches a date field value in specified format
	 * 
	 * @param index
	 *            The index of the field whose value to be fetched
	 * @param dateFormat
	 *            The format in which the date value is to be fetched
	 * @return The value of the field as string
	 */
	public String getDate(int index, String dateFormat) throws ParseException {
		verifyFieldExists(index);
		if (values.get(index) != null) {
			Long date = (Long) values.get(index);
			DateFormat df = new SimpleDateFormat(dateFormat);
			return df.format(new Date(date));
		}
		return null;
	}

	/**
	 * Checks if the field name has been declared in the xml
	 * 
	 * @param fieldName
	 *            The field name that is to be validated
	 */
	private void verifyFieldExists(String fieldName) {
		// method called in all get field methods. If the code flow
		// encounters a field which is not declared in operation input fields,
		// exception will be raised. The exception will always be raised for the
		// first invalid field

		if (!fieldPos.containsKey(fieldName)) {
			throw new ReusableRowException(
					"ReusableRow can only be used to fetch values for fields it has been instantiated with. Missing field: '"
							+ fieldName + "'");

		}
	}

	/**
	 * Checks whether the index is within bounds
	 * 
	 * @param index
	 *            The field index to be checked
	 */
	private void verifyFieldExists(int index) {
		// method called in all get field methods. If the code flow
		// encounters an index which is greater than the # of input fields
		// declared, an exception will be raised. The exception will always be
		// raised for the first invalid field

		if (values.size() < index) {
			throw new ReusableRowException(
					"ReusableRow can only be used to fetch values for fields it has been instantiated with. Index out of bounds: Index: "
							+ index + ", Size: " + values.size());

		}
	}

	public class ReusableRowException extends RuntimeException {

		private static final long serialVersionUID = 4847210775806480201L;

		public ReusableRowException(String msg) {
			super(msg);
		}
	}

	@Override
	public int compareTo(ReusableRow other) {

		int c = 0;
		if (other == null || other.values == null || other.values.size() == 0)
			return 1;

		if (this.values == null || this.values.size() == 0)
			return -1;

		if (this.values.size() != other.values.size())
			return this.values.size() - other.values.size();

		for (int i = 0; i < this.values.size(); i++) {
			Object lhs = this.getField(i);
			Object rhs = other.getField(i);

			if (lhs == null && rhs == null) {
				continue;
			} else if (lhs == null && rhs != null) {
				return -1;
			} else if (lhs != null && rhs == null) {
				return 1;
			} else {
				// guaranteed to not be null
				if (lhs != null) { // additional check added for SonarQube
					c = ((Comparable) lhs).compareTo((Comparable) rhs);
				}
			}
			if (c != 0)
				return c;
		}

		return 0;
	}

	@Override
	public int hashCode() {
		int hash = 1;

		for (Object element : values)
			hash = 31 * hash + (element != null ? element.hashCode() : 0);

		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public String toString() {
		String result = "";
		final String fieldDelim = ":";
		final String fieldNameDelim = "=";

		for (String field : fields) {
			result += fieldDelim + field + fieldNameDelim + getField(field);
		}

		return result;
	}
}

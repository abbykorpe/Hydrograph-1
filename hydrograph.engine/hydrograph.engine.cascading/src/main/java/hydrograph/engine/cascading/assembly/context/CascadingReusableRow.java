package hydrograph.engine.cascading.assembly.context;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashSet;

import hydrograph.engine.transformation.userfunctions.base.ListBasedReusableRow;

public class CascadingReusableRow extends ListBasedReusableRow {

	public CascadingReusableRow(LinkedHashSet<String> fields) {
		super(fields);
	}

	/**
	 * The generic method to set a value to field
	 * 
	 * @param fieldName
	 *            The name of the field
	 * @param value
	 *            The value to set
	 */
	@Override
	public void setField(String fieldName, Comparable value) {
		if (value instanceof Date) {
			Date date = (Date) value;
			setFieldInternal(fieldName,date.getTime());
			// } else if (value == null
			// || (value instanceof String && ((String) value).equals(""))) {
			// valueMap.put(fieldName, null);
		} else {
			setFieldInternal(fieldName, value);
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
	@Override
	public void setField(int index, Comparable value) {
		if (value instanceof Date) {
			Date date = (Date) value;
			setFieldInternal(index,date.getTime());
			// } else if (value == null
			// || (value instanceof String && ((String) value).equals(""))) {
			// values.set(index, null);
		} else {
			setFieldInternal(index, value);
		}
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
		if (value instanceof Date) {
			Date date = (Date) value;
			setFieldInternal(fieldName, date.getTime());
		} else if (value == null || (value instanceof String && ((String) value).equals(""))) {
			setFieldInternal(fieldName, null);
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
			setFieldInternal(index, date.getTime());
		} else if (value == null || (value instanceof String && ((String) value).equals(""))) {
			setFieldInternal(index, null);
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
		if (getFieldInternal(fieldName) != null) {
			Long date = (Long) getFieldInternal(fieldName);
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
		if (getFieldInternal(index) != null) {
			Long date = (Long) getFieldInternal(index);
			return new Date(date);
		}
		return null;
	}
}

package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

// TODO: Auto-generated Javadoc
/**
 * The Class GridRow.
 * 
 * @author Bitwise
 */
public class GridRow {

		private String fieldName;

		private String dateFormat;

		private Integer dataType;

		private String scale;
		
		private String dataTypeValue;

		public String getDataTypeValue() {
			return dataTypeValue;
		}

		public void setDataTypeValue(String dataTypeValue) {
			this.dataTypeValue = dataTypeValue;
		}

		public String getScale() {
			return scale;
		}

		public void setScale(String scale) {
			this.scale = scale;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public String getDateFormat() {
			return dateFormat;
		}

		public void setDateFormat(String dateFormat) {
			this.dateFormat = dateFormat;
		}

		public Integer getDataType() {
			return dataType;
		}

		public void setDataType(Integer dataType) {
			this.dataType = dataType;
		}
		
		
		//NOTE: DO NOT CHANGE THIS METHOD UNLESS YOU KNOW WHAT YOU ARE DOING
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((fieldName == null) ? 0 : fieldName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GridRow other = (GridRow) obj;
			if (fieldName == null) {
				if (other.fieldName != null)
					return false;
			} else if (!fieldName.equals(other.fieldName))
				return false;
			return true;
		}

		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("GridRow [fieldName=");
			builder.append(fieldName);
			builder.append(", dateFormat=");
			builder.append(dateFormat);
			builder.append(", dataType=");
			builder.append(dataType);
			builder.append(", scale=");
			builder.append(scale);
			builder.append(", dataTypeValue=");
			builder.append(dataTypeValue);
			builder.append("]");
			return builder.toString();
		}

		/**
		 * Copy.
		 * 
		 * @return the grid row
		 */
		public GridRow copy() {
			GridRow tempschemaGrid = new GridRow();
			tempschemaGrid.setDataType(dataType);
			tempschemaGrid.setDateFormat(dateFormat);
			tempschemaGrid.setFieldName(fieldName);
			tempschemaGrid.setScale(scale);
			tempschemaGrid.setDataTypeValue(dataTypeValue);
			return tempschemaGrid;
		}
	}

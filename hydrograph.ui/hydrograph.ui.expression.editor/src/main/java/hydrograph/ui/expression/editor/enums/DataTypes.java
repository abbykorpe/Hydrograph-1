/********************************************************************************
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
 ******************************************************************************/

package hydrograph.ui.expression.editor.enums;

import org.apache.commons.lang.StringUtils;

public enum DataTypes {
	
	Integer("I","integer") {
		@Override
		protected String getDefaultValue() {
			return "0";
		}

		@Override
		protected String getDataTypeName() {
			return "int";
		}

		@Override
		protected Class<?> getDataTypeClass() {
			return java.lang.Integer.class;
		}
	},
	Float("F","float") {
		@Override
		protected String getDefaultValue() {
			return "1.0";
		}

		@Override
		protected String getDataTypeName() {
			return "float";
		}

		@Override
		protected Class<?> getDataTypeClass() {
			return java.lang.Float.class;
		}
	},
	Double("D","double") {
		@Override
		protected String getDefaultValue() {
			return "1.0";
		}

		@Override
		protected String getDataTypeName() {
			return "double";
		}

		@Override
		protected Class<?> getDataTypeClass() {
			return java.lang.Double.class;
		}
	},
	Long("J","long") {
		@Override
		protected String getDefaultValue() {
			return "1";
		}

		@Override
		protected String getDataTypeName() {
			return "long";
		}

		@Override
		protected Class<?> getDataTypeClass() {
			return java.lang.Long.class;
		}
	},
	Short("S","short") {
		@Override
		protected String getDefaultValue() {
			return "1";
		}

		@Override
		protected String getDataTypeName() {
			return "short";
		}

		@Override
		protected Class<?> getDataTypeClass() {
			return java.lang.Short.class;
		}
	},
	Boolean("Z","boolean") {
		@Override
		public String getDefaultValue() {
			return "false";
		}

		@Override
		protected String getDataTypeName() {
			// TODO Auto-generated method stub
			return "boolean";
		}

		@Override
		protected Class<?> getDataTypeClass() {
			return java.lang.Boolean.class;
		}
	},
	
	Void("V","void") {
		@Override
		public String getDefaultValue() {
			return "";
		}

		@Override
		protected String getDataTypeName() {
			return "void";
		}

		@Override
		protected Class<?> getDataTypeClass() {
			return java.lang.Void.class;
		}
	},
	
	String("S","String") {
		@Override
		public String getDefaultValue() {
			return "";
		}

		@Override
		protected String getDataTypeName() {
			return "String";
		}

		@Override
		protected Class<?> getDataTypeClass() {
			return java.lang.String.class;
		}
	};

	private final String reflectionValue;
	private String dataType;

	protected abstract String getDefaultValue();
	protected abstract String getDataTypeName();
	protected abstract Class<?> getDataTypeClass();
	
	DataTypes(String value,String dataType) {
		this.reflectionValue = value;
		this.dataType=dataType;
	}

	public String value() {
		return reflectionValue;
	}

	public static String getDefaulltValuefromString(String value) {
		for (DataTypes dataType : DataTypes.values()) {
			if (StringUtils.equalsIgnoreCase(dataType.reflectionValue, value)) {
				return dataType.getDefaultValue();
			}
		}
		return value;
	}
	
	public static String getDataTypefromString(String value) {
		for (DataTypes dataType : DataTypes.values()) {
			if (StringUtils.equalsIgnoreCase(dataType.reflectionValue, value)) {
				return dataType.getDataTypeName();
			}
		}
		return value;
	}
	
	public static Class<?> getDataTypeClassfromString(String dataTypeName) {
		for (DataTypes dataType : DataTypes.values()) {
			if (StringUtils.equalsIgnoreCase(dataType.dataType, dataTypeName)) {
				return dataType.getDataTypeClass();
			}
		}
		return null;
	}
	
}
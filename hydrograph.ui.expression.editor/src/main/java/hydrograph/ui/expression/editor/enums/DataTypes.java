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
	
	Integer("I") {
		@Override
		protected String getDefaultValue() {
			return "0";
		}

		@Override
		protected String getDataTypeName() {
			return "int";
		}
	},
	Float("F") {
		@Override
		protected String getDefaultValue() {
			return "1.0";
		}

		@Override
		protected String getDataTypeName() {
			return "float";
		}
	},
	Double("D") {
		@Override
		protected String getDefaultValue() {
			return "1.0";
		}

		@Override
		protected String getDataTypeName() {
			return "double";
		}
	},
	Long("J") {
		@Override
		protected String getDefaultValue() {
			return "1";
		}

		@Override
		protected String getDataTypeName() {
			return "long";
		}
	},
	Short("S") {
		@Override
		protected String getDefaultValue() {
			return "1";
		}

		@Override
		protected String getDataTypeName() {
			return "short";
		}
	},
	Boolean("Z") {
		@Override
		public String getDefaultValue() {
			return "false";
		}

		@Override
		protected String getDataTypeName() {
			// TODO Auto-generated method stub
			return "boolean";
		}
	};

	private final String value;

	protected abstract String getDefaultValue();
	protected abstract String getDataTypeName();
	
	DataTypes(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static String getDefaulltValuefromString(String value) {
		for (DataTypes dataType : DataTypes.values()) {
			if (StringUtils.equalsIgnoreCase(dataType.value, value)) {
				return dataType.getDefaultValue();
			}
		}
		return value;
	}
	
	public static String getDataTypefromString(String value) {
		for (DataTypes dataType : DataTypes.values()) {
			if (StringUtils.equalsIgnoreCase(dataType.value, value)) {
				return dataType.getDataTypeName();
			}
		}
		return value;
	}
}
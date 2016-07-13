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
package hydrograph.ui.dataviewer.filter;

import org.apache.commons.lang.StringUtils;

public class Condition{
	private String fieldName;
	private String relationalOperator;
	private String conditionalOperator;
	private String value1;
	private String value2;
	
	public Condition() {
		this.fieldName = "";
		this.relationalOperator = "";
		this.conditionalOperator = "";
		this.value1 = "";
		this.value2 = "";
	}
	
	public Condition copy(Condition source){
		Condition condition = new Condition();
		condition.setFieldName(source.getFieldName());
		condition.setRelationalOperator(source.getRelationalOperator());
		condition.setConditionalOperator(source.getConditionalOperator());
		condition.setValue1(source.getValue1());
		condition.setValue2(source.getValue2());
		return condition;
	}
	
	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getRelationalOperator() {
		return relationalOperator;
	}
	public void setRelationalOperator(String relationalOperator) {
		this.relationalOperator = relationalOperator;
	}
	public String getConditionalOperator() {
		return conditionalOperator;
	}
	public void setConditionalOperator(String conditionalOperator) {
		this.conditionalOperator = conditionalOperator;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	@Override
	public String toString() {
		String value2TextBoxValue = StringUtils.isNotBlank(value2)== true ?  ", value2=" + value2  : "";
		return "FilterConditions [fieldName=" + fieldName
				+ ", relationalOperator=" + relationalOperator
				+ ", conditionalOperator=" + conditionalOperator
				+ ", value1=" + value1 + value2TextBoxValue + "]";
	}
}
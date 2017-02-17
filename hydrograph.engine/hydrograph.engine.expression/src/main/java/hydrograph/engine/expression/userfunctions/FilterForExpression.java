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
package hydrograph.engine.expression.userfunctions;

import hydrograph.engine.expression.api.ValidationAPI;
import hydrograph.engine.transformation.userfunctions.base.FilterBase;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

import java.util.ArrayList;
import java.util.Properties;

public class FilterForExpression implements FilterBase {

	ValidationAPI validationAPI;
	String[] fieldNames;
	Object[] tuples;

	public void setValidationAPI(ValidationAPI validationAPI){
		this.validationAPI = validationAPI;
	}

	public FilterForExpression() {
	}

	public void callPrepare(String[] inputFieldNames,String[] inputFieldTypes){
		try {
			validationAPI.init(inputFieldNames,inputFieldTypes);
		} catch (Exception e) {
			throw new RuntimeException(
					"Exception in Filter Expression: "
							+ validationAPI.getExpr() + ",", e);
		}
	}

	@Override
	public void prepare(Properties props, ArrayList<String> inputFields) {
	}

	@Override
	public boolean isRemove(ReusableRow reusableRow) {
		fieldNames = new String[reusableRow.getFields().size()];
		tuples = new Object[reusableRow.getFields().size()];
		for(int i=0;i<reusableRow.getFields().size();i++){
			fieldNames[i] = reusableRow.getFieldName(i);
			tuples[i] = reusableRow.getField(i);
		}
		try {
			if ((Boolean) validationAPI.exec(tuples)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception in Filter expression: "
					+ validationAPI.getValidExpression()
					+ ".\nRow being processed: " + reusableRow.toString(), e);
		}
	}

	@Override
	public void cleanup() {
	}

}

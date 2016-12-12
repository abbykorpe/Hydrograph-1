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
package hydrograph.engine.expression.userfunctions;

import hydrograph.engine.expression.api.ValidationAPI;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;
import hydrograph.engine.transformation.userfunctions.base.TransformBase;

import java.util.ArrayList;
import java.util.Properties;

@SuppressWarnings("rawtypes")
public class TransformForExpression implements TransformBase {

	ValidationAPI validationAPI;
	String[] fieldNames;
	Object[] tuples;

	public void setValidationAPI(ValidationAPI validationAPI){
		this.validationAPI = validationAPI;
	}

	public TransformForExpression() {

	}

	@Override
	public void prepare(Properties props, ArrayList<String> inputFields,
			ArrayList<String> outputFields) {

	}

	@Override
	public void transform(ReusableRow inputRow, ReusableRow outputRow) {
		outputRow.reset();
		fieldNames = new String[inputRow.getFields().size()];
		tuples = new Object[inputRow.getFields().size()];
		for(int i=0;i<inputRow.getFields().size();i++){
			fieldNames[i] = inputRow.getFieldName(i);
			tuples[i] = inputRow.getObject(i);
		}
		try {
			outputRow.setField(0,
					(Comparable) validationAPI.execute(fieldNames, tuples));
		} catch (Exception e) {
			throw new RuntimeException("Exception in tranform expression: "
					+ validationAPI.getValidExpression()
					+ ".\nRow being processed: " + inputRow.toString(), e);
		}
	}

	@Override
	public void cleanup() {

	}

}

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

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;

import hydrograph.engine.expression.api.ValidationAPI;
import hydrograph.engine.transformation.userfunctions.base.AggregateTransformBase;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

@SuppressWarnings("rawtypes")
public class AggregateForExpression implements AggregateTransformBase {

	private ValidationAPI[] validationAPIs;
	private String[] initialValueExpressions;
	private Object accumulatorValue;
	private int counter;

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void setValidationAPI(ValidationAPI[] validationAPI) {
		this.validationAPIs = validationAPI;
	}

	public void setInitialValueExpression(String[] initialValueExpression) {
		this.initialValueExpressions = initialValueExpression;
	}

	public AggregateForExpression() {
	}
	
	public void callPrepare(){
		try {
			accumulatorValue = validationAPIs[counter]
					.execute(initialValueExpressions[counter]);
		} catch (Exception e) {
			throw new RuntimeException(
					"Exception in aggregate initial value expression: "
							+ initialValueExpressions + ".", e);
		}
	}

	@Override
	public void prepare(Properties props, ArrayList<String> inputFields,
			ArrayList<String> outputFields, ArrayList<String> keyFields) {

	}

	@Override
	public void aggregate(ReusableRow input) {
		String fieldNames[] = new String[input.getFieldNames().size() + 1];
		Object tuples[] = new Object[input.getFieldNames().size() + 1];
		int i = 0;
		for (; i < input.getFieldNames().size(); i++) {
			fieldNames[i] = input.getFieldNames().get(i);
			tuples[i] = input.getField(i);
		}
		fieldNames[i] = "accumulator";
		tuples[i] = accumulatorValue;
		try {
			accumulatorValue = validationAPIs[counter].execute(fieldNames,
					tuples, validationAPIs[counter].getExpr());
		} catch (Exception e) {
			throw new RuntimeException("Exception in aggregate expression: "
					+ validationAPIs[counter].getExpr() + ".\nRow being processed: "
					+ input.toString(), e);
		}
	}

	@Override
	public void onCompleteGroup(ReusableRow output) {
		output.setField(0, (Comparable) accumulatorValue);

		try {
			accumulatorValue = validationAPIs[counter]
					.execute(initialValueExpressions[counter]);
		} catch (Exception e) {
			throw new RuntimeException(
					"Exception in aggregate initial value expression: "
							+ initialValueExpressions + ".", e);
		}
	}

	@Override
	public void cleanup() {

	}

}

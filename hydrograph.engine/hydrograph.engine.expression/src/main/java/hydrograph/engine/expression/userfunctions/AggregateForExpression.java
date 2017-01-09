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

import java.util.ArrayList;
import java.util.Properties;

import hydrograph.engine.expression.utils.ExpressionWrapper;
import hydrograph.engine.transformation.userfunctions.base.AggregateTransformBase;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

@SuppressWarnings("rawtypes")
public class AggregateForExpression implements AggregateTransformBase {

	private ExpressionWrapper expressionWrapper;
	private Object accumulatorValue;

	public void setValidationAPI(ExpressionWrapper expressionWrapper) {
		this.expressionWrapper=expressionWrapper;
	}

	public AggregateForExpression() {
	}
	
	public void callPrepare(){
		try {
			accumulatorValue = expressionWrapper.getValidationAPI()
					.execute(expressionWrapper.getIntialValueExpression());
		} catch (Exception e) {
			throw new RuntimeException(
					"Exception in aggregate initial value expression: "
							+ expressionWrapper.getIntialValueExpression() + ".", e);
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
		fieldNames[i] = "_accumulator";
		tuples[i] = accumulatorValue;
		try {
			accumulatorValue = expressionWrapper.getValidationAPI().execute(fieldNames,
					tuples, expressionWrapper.getValidationAPI().getExpr());
		} catch (Exception e) {
			throw new RuntimeException("Exception in aggregate expression: "
					+ expressionWrapper.getValidationAPI().getExpr() + ".\nRow being processed: "
					+ input.toString(), e);
		}
	}

	@Override
	public void onCompleteGroup(ReusableRow output) {
		output.setField(0, (Comparable) accumulatorValue);

		try {
			accumulatorValue = expressionWrapper.getValidationAPI()
					.execute(expressionWrapper.getIntialValueExpression());
		} catch (Exception e) {
			throw new RuntimeException(
					"Exception in aggregate initial value expression: "
							+ expressionWrapper.getIntialValueExpression() + ".", e);
		}
	}

	@Override
	public void cleanup() {

	}

}

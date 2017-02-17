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

import java.util.ArrayList;
import java.util.Properties;

import hydrograph.engine.expression.api.ValidationAPI;
import hydrograph.engine.expression.utils.ExpressionWrapper;
import hydrograph.engine.transformation.userfunctions.base.NormalizeTransformBase;
import hydrograph.engine.transformation.userfunctions.base.OutputDispatcher;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

public class NormalizeForExpression implements NormalizeTransformBase {

	private ExpressionWrapper expressionWrapper;
	private ValidationAPI validationAPI;
	private String[] fieldNames;
	private Object[] tuples;
	private String countExpression;
	private int transformInstancesSize;
	private ArrayList<String> operationOutputFields;
	private ArrayList<String> listOfExpressions;

	public void setValidationAPI(ExpressionWrapper expressionWrapper){
		this.expressionWrapper = expressionWrapper;
	}
	public void setValidationAPI(ValidationAPI validationAPI){
		this.validationAPI = validationAPI;
	}

	public NormalizeForExpression() {
	}

	@Override
	public void prepare(Properties props) {

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void Normalize(ReusableRow inputRow, ReusableRow outputRow,
						  OutputDispatcher outputDispatcher) {

		try {
			int exprCount =(int) new ValidationAPI(expressionWrapper.getCountExpression(),"")
					.execute(expressionWrapper.getFieldNames(), expressionWrapper.getTuples());
			int i=0,j=0;
			for (j = 0; j < exprCount; j++) {
				try {
					for (int counter = 0; counter < expressionWrapper.getTransformInstancesSize(); counter++) {
						fieldNames = new String[inputRow.getFields().size() + 1];
						tuples = new Object[inputRow.getFields().size() + 1];
						for (i = 0; i < inputRow.getFieldNames().size(); i++) {
							fieldNames[i] = inputRow.getFieldName(i);
							tuples[i] = inputRow.getField(i);
						}
						fieldNames[i] = "_index";
						tuples[i] = j;
						Object obj = expressionWrapper.getValidationAPI().execute(fieldNames,
								tuples, expressionWrapper.getListOfExpressions().get(counter));
						outputRow.setField(
								expressionWrapper.getOperationOutputFields().get(counter),
								(Comparable) obj);
					}
					outputDispatcher.sendOutput();
				} catch (Exception e) {
					throw new RuntimeException(
							"Exception in normalize expression: "
									+ expressionWrapper.getListOfExpressions()
									.get(i)
									+ ".\nRow being processed: "
									+ inputRow.toString(), e);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception in normalize expression: "
					+ expressionWrapper.getCountExpression() + ".", e);
		}

	}

	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	public void setTuples(Object[] tuples) {
		this.tuples = tuples;
	}

	public void setCountExpression(String countExpression) {
		this.countExpression = countExpression;
	}

	public void setTransformInstancesSize(int transformInstancesSize) {
		this.transformInstancesSize = transformInstancesSize;
	}

	public void setOperationOutputFields(
			ArrayList<String> operationOutputFields) {
		this.operationOutputFields = operationOutputFields;
	}

	public void setListOfExpressions(ArrayList<String> listOfExpressions) {
		this.listOfExpressions = listOfExpressions;
	}


	@Override
	public void cleanup() {

	}

}

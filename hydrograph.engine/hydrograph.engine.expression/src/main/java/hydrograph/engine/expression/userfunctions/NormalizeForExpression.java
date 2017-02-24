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

	public void callPrepare(String[] inputFieldNames,String[] inputFieldTypes){
		try {
			validationAPI.init(inputFieldNames,inputFieldTypes);
		} catch (Exception e) {
			throw new RuntimeException(
					"Exception in Normalize Expression: "
							+ validationAPI.getExpr() + ",", e);
		}
	}

	@Override
	public void prepare(Properties props) {

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void Normalize(ReusableRow inputRow, ReusableRow outputRow,
						  OutputDispatcher outputDispatcher) {

		try {
//			int exprCount =(int) new ValidationAPI(expressionWrapper.getCountExpression(),"")
//					.execute(expressionWrapper.getFieldNames(), expressionWrapper.getTuples());
			int exprCount = 2;
			int i=0,j=0,counter=0;
			for (j = 0; j < exprCount; j++) {
				try {
//					for (counter = 0; counter < expressionWrapper.getTransformInstancesSize(); counter++) {
						fieldNames = new String[inputRow.getFields().size() + 1];
						tuples = new Object[inputRow.getFields().size() + 1];
						for (i = 0; i < inputRow.getFieldNames().size(); i++) {
							fieldNames[i] = inputRow.getFieldName(i);
							tuples[i] = inputRow.getField(i);
						}
						fieldNames[i] = "_index";
						tuples[i] = j;
						Object obj = expressionWrapper.getValidationAPI().execute(fieldNames,
								tuples);
						outputRow.setField(
								expressionWrapper.getOperationOutputFields().get(0),
								(Comparable) obj);
//					}
					outputDispatcher.sendOutput();
				} catch (Exception e) {
					throw new RuntimeException(
							"Exception in normalize expression:[\""
									+ expressionWrapper.getListOfExpressions().get(counter)
									+ "\"].", e);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception in normalize expression:[\""
					+ expressionWrapper.getCountExpression() + "\"].", e);
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

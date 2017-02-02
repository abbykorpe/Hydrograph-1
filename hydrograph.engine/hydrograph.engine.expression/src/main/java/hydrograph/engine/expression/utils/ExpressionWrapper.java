package hydrograph.engine.expression.utils;

import hydrograph.engine.expression.api.ValidationAPI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gurdits on 1/6/2017.
 */
public class ExpressionWrapper {

	private ValidationAPI validationAPI;
	private String intialValueExpression;
	private String[] fieldNames;
	private Object[] tuples;
	private String countExpression;
	private int transformInstancesSize;
	private ArrayList<String> operationOutputFields;
	private List<String> listOfExpressions;

	public ExpressionWrapper(ValidationAPI validationAPI, String intialValue) {
		this.validationAPI = validationAPI;
		this.intialValueExpression = intialValue;
	}

	public ExpressionWrapper(ValidationAPI validationAPI, String[] fieldNames, Object[] tuples, String countExpression,
			int transformInstancesSize, ArrayList<String> operationOutputFields, List<String> listOfExpressions) {
		this.validationAPI = validationAPI;
		this.fieldNames = fieldNames;
		this.tuples = tuples;
		this.countExpression = countExpression;
		this.transformInstancesSize = transformInstancesSize;
		this.operationOutputFields = operationOutputFields;
		this.listOfExpressions = listOfExpressions;
	}

	public ValidationAPI getValidationAPI() {
		return validationAPI;
	}

	public String getIntialValueExpression() {
		return intialValueExpression;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public Object[] getTuples() {
		return tuples;
	}

	public String getCountExpression() {
		return countExpression;
	}

	public int getTransformInstancesSize() {
		return transformInstancesSize;
	}

	public ArrayList<String> getOperationOutputFields() {
		return operationOutputFields;
	}

	public List<String> getListOfExpressions() {
		return listOfExpressions;
	}
}

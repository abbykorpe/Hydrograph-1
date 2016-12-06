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
package hydrograph.engine.core.component.entity.elements;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Properties;

/**
 * This is a POJO which holds the information for one operation in any of the
 * operation type components like
 * {@link hydrograph.engine.cascading.assembly.TransformAssembly Transform},
 * {@link hydrograph.engine.cascading.assembly.FilterAssembly Filter} etc. The
 * object of this class is supposed to be used in the entity classes for the
 * transform type components
 * 
 * @author Prabodh
 *
 */

public class Operation implements Serializable{


	private String operationId;
	private String operationClass;
	private String[] operationInputFields;
	private String[] operationOutputFields;
	private Properties operationProperties;
	private String expression;
	
	public Operation() {

	}


	/**
	 * @return the Expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * @param Expression
	 *            the Expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	/**
	 * @return the operationClass
	 */
	public String getOperationClass() {
		return operationClass;
	}

	/**
	 * @param operationClass
	 *            the operationClass to set
	 */
	public void setOperationClass(String operationClass) {
		this.operationClass = operationClass;
	}

	/**
	 * @return the operationInputFields
	 */
	public String[] getOperationInputFields() {
		return operationInputFields != null ? operationInputFields.clone() : null;
	}

	/**
	 * @param operationInputFields
	 *            the operationInputFields to set
	 */
	public void setOperationInputFields(String[] operationInputFields) {
		this.operationInputFields = operationInputFields != null ? operationInputFields.clone() : null;
	}

	/**
	 * @return the operationOutputFields
	 */
	public String[] getOperationOutputFields() {
		return operationOutputFields != null ? operationOutputFields.clone() : null;
	}

	/**
	 * @param operationOutputFields
	 *            the operationOutputFields to set
	 */
	public void setOperationOutputFields(String[] operationOutputFields) {
		this.operationOutputFields = operationOutputFields != null ? operationOutputFields.clone() : null;
	}

	/**
	 * @return the operationProperties
	 */
	public Properties getOperationProperties() {
		return operationProperties;
	}

	/**
	 * @param operationProperties
	 *            the operationProperties to set
	 */
	public void setOperationProperties(Properties operationProperties) {
		this.operationProperties = operationProperties;
	}

	/**
	 * @return the operationId
	 */
	public String getOperationId() {
		return operationId;
	}

	/**
	 * @param operationId
	 *            the operationId to set
	 */
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("\nOperationID: " + operationId + " | operation class: "
				+ operationClass + " | ");

		str.append("operation input fields: ");
		if (operationInputFields != null) {
			str.append(Arrays.toString(operationInputFields));
		}

		str.append(" | operation output fields: ");
		if (operationOutputFields != null) {
			str.append(Arrays.toString(operationOutputFields));
		}
		str.append(" | operation properties: " + operationProperties);

		return str.toString();
	}
}

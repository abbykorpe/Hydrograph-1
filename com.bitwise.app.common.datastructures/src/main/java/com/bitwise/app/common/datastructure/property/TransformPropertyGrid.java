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

 
package com.bitwise.app.common.datastructure.property;

import java.util.List;

public class TransformPropertyGrid {
	private List<OperationSystemProperties> opSysProperties;
	private List<NameValueProperty> nameValueProps;
	private List<OperationField> outputTreeFields;
	private List<TransformOperation> operation;
	
	public List<NameValueProperty> getNameValueProps() {
		return nameValueProps;
	}

	public void setNameValueProps(List<NameValueProperty> nameValueProps) {
		this.nameValueProps = nameValueProps;
	}

	public List<OperationField> getOutputTreeFields() {
		return outputTreeFields;
	}

	public void setOutputTreeFields(List<OperationField> outputTreeFields) {
		this.outputTreeFields = outputTreeFields;
	}

	public List<TransformOperation> getOperation() {
		return operation;
	}

	public void setOperation(List<TransformOperation> operation) {
		this.operation = operation;
	}

	public List<OperationSystemProperties> getOpSysProperties() {
		return opSysProperties;
	}

	public void setOpSysProperties(List<OperationSystemProperties> opSysProperties) {
		this.opSysProperties = opSysProperties;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TransformPropertyGrid [opSysProperties=");
		builder.append(opSysProperties);
		builder.append(", nameValueProps=");
		builder.append(nameValueProps);
		builder.append(", outputTreeFields=");
		builder.append(outputTreeFields);
		builder.append(", operation=");
		builder.append(operation);
		builder.append("]");
		return builder.toString();
	}

	
}

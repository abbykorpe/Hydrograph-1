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

import java.util.ArrayList;
import java.util.List;

public class TransformOperation {
	private String operationId="1";
	private List<OperationField> inputFields;
	private OperationClassProperty opClassProperty;
	private List<NameValueProperty> nameValueProps;
	private List schemaGridRowList;
	private String CUSTOM="custom";
	
	public TransformOperation() {
		inputFields=new ArrayList<>();
		opClassProperty=new OperationClassProperty(CUSTOM,"", false,"");
		nameValueProps=new ArrayList<>();
		schemaGridRowList= new ArrayList<>();
	}
	
	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public List<NameValueProperty> getNameValueProps() {
		return nameValueProps;
	}
	public void setNameValueProps(List<NameValueProperty> nameValueProps) {
		this.nameValueProps = nameValueProps;
	}
	public List<OperationField> getInputFields() {
		return inputFields;
	}
	public List getSchemaGridRowList() {
		return schemaGridRowList;
	}

	public void setSchemaGridRowList(List schemaGridRowList) {
		this.schemaGridRowList = schemaGridRowList;
	}

	public void setInputFields(List<OperationField> inputFields) {
		this.inputFields = inputFields;
	}
	public OperationClassProperty getOpClassProperty() {
		return opClassProperty;
	}
	public void setOpClassProperty(OperationClassProperty opClassProperty) {
		this.opClassProperty = opClassProperty;
	}
	
	
}

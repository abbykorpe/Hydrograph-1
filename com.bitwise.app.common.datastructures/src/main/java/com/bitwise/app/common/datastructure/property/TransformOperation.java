package com.bitwise.app.common.datastructure.property;

import java.util.ArrayList;
import java.util.List;

public class TransformOperation {
	private String operationId="1";
	private List<OperationField> inputFields;
	private OperationClassProperty opClassProperty;
	private List<NameValueProperty> nameValueProps;
	private List schemaGridRowList;
	
	public TransformOperation() {
		inputFields=new ArrayList<>();
		opClassProperty=new OperationClassProperty("", false,null);
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

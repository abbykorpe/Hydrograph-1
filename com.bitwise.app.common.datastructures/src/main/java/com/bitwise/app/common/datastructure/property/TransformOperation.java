package com.bitwise.app.common.datastructure.property;

import java.util.ArrayList;
import java.util.List;

public class TransformOperation {
	private List<OperationField> inputFields;
	private List<OperationField> outputFields;
	private OperationClassProperty opClassProperty;
	private List<NameValueProperty> nameValueProps;
	private List schemaGridRowList;
	
	public TransformOperation() {
		inputFields=new ArrayList<>();
		outputFields=new ArrayList<>();
		opClassProperty=new OperationClassProperty("", false);
		nameValueProps=new ArrayList<>();
		schemaGridRowList= new ArrayList<>();
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
	public List<OperationField> getOutputFields() {
		return outputFields;
	}
	public void setOutputFields(List<OperationField> outputFields) {
		this.outputFields = outputFields;
	}
	public OperationClassProperty getOpClassProperty() {
		return opClassProperty;
	}
	public void setOpClassProperty(OperationClassProperty opClassProperty) {
		this.opClassProperty = opClassProperty;
	}
	
	
}

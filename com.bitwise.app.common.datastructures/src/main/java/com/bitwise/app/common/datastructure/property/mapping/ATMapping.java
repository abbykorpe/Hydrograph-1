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

 
package com.bitwise.app.common.datastructure.property.mapping;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.bitwise.app.cloneableinterface.IDataStructure;
import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.datastructure.property.NameValueProperty;

/**
 * The class is a data structure to to save aggregate and transform mapping sheet 
 * 
 * @author Bitwise
 *
 */
public class ATMapping implements IDataStructure{
	
	private List<InputField> inputFields;
	private List<MappingSheetRow> mappingSheetRows;
	private List<NameValueProperty> mapAndPassthroughField;
	private List<FilterProperties> outputFieldList;
	
	public ATMapping() {
		inputFields = new LinkedList<>();
		mappingSheetRows = new LinkedList<>();
		mapAndPassthroughField=new ArrayList<>();
		outputFieldList=new ArrayList<>();
	}

	
	
	

	public List<NameValueProperty> getMapAndPassthroughField() {
		return mapAndPassthroughField;
	}





	public void setMapAndPassthroughField(List<NameValueProperty> mapAndPassthroughField) {
		this.mapAndPassthroughField = mapAndPassthroughField;
	}





	public List<FilterProperties> getOutputFieldList() {
		return outputFieldList;
	}


	public void setOutputFieldList(List<FilterProperties> outputFieldList) {
		this.outputFieldList = outputFieldList;
	}


	public ATMapping(List<InputField> inputFields,
			List<MappingSheetRow> mappingSheetRows) {
		this.inputFields = inputFields;
		this.mappingSheetRows = mappingSheetRows;
	}
	
	public ATMapping(List<InputField> inputFields,
			List<MappingSheetRow> mappingSheetRows,List<NameValueProperty> nameValueProperties,List<FilterProperties> outputFieldList ) {
		this.inputFields = inputFields;
		this.mappingSheetRows = mappingSheetRows;
		this.mapAndPassthroughField=nameValueProperties;
		this.outputFieldList=outputFieldList;
		
	}
	
	/**
	 * returns list of input fields
	 * 
	 * @return input fields
	 */
	public List<InputField> getInputFields() {
		return inputFields;
	}

	/**
	 * set list of input fields
	 * 
	 * @param inputFields
	 */
	public void setInputFields(List<InputField> inputFields) {
		this.inputFields = inputFields;
	}

	/**
	 * returns list of mapping sheet rows
	 * 
	 * @return mappingSheetRows
	 */
	public List<MappingSheetRow> getMappingSheetRows() {
		return mappingSheetRows;
	}

	/**
	 * set the list of mapping sheet rows.
	 * @param mappingSheetRows
	 */
	public void setMappingSheetRows(List<MappingSheetRow> mappingSheetRows) {
		this.mappingSheetRows = mappingSheetRows;
	}

	@Override
	public Object clone()  {
		List<InputField> inputFields = new LinkedList<>();
		List<MappingSheetRow> mappingSheetRows = new LinkedList<>();
	    List<NameValueProperty> nameValueProperty=new LinkedList<>();		
	    List<FilterProperties> outputFieldList=new LinkedList<>();
		inputFields.addAll(this.inputFields);
		nameValueProperty.addAll(this.mapAndPassthroughField);
		outputFieldList.addAll(this.outputFieldList);
		for(MappingSheetRow mappingSheetRow : this.mappingSheetRows){
			if(this.mappingSheetRows!=null)
			mappingSheetRows.add((MappingSheetRow) mappingSheetRow.clone());
		}
		
		ATMapping atMapping = new ATMapping(inputFields, mappingSheetRows,nameValueProperty,outputFieldList);
		
		return atMapping;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inputFields == null) ? 0 : inputFields.hashCode());
		result = prime * result + ((mappingSheetRows == null) ? 0 : mappingSheetRows.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ATMapping other = (ATMapping) obj;
		if (inputFields == null) {
			if (other.inputFields != null)
				return false;
		} else if (!inputFields.equals(other.inputFields))
			return false;
		if (mappingSheetRows == null) {
			if (other.mappingSheetRows != null)
				return false;
		} else if (!mappingSheetRows.equals(other.mappingSheetRows))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ATMapping [inputFields=" + inputFields + ", mappingSheetRows="
				+ mappingSheetRows + "]";
	}
}

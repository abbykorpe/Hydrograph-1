package com.bitwise.app.common.datastructure.property.mapping;

import java.util.LinkedList;
import java.util.List;

import com.bitwise.app.cloneableinterface.IDataStructure;

/**
 * The class is a data structure to to save aggregate and transform mapping sheet 
 * 
 * @author Bitwise
 *
 */
public class ATMapping implements IDataStructure{
	private List<InputField> inputFields;
	private List<MappingSheetRow> mappingSheetRows;
	
	public ATMapping() {
		inputFields = new LinkedList<>();
		mappingSheetRows = new LinkedList<>();
	}

	public ATMapping(List<InputField> inputFields,
			List<MappingSheetRow> mappingSheetRows) {
		this.inputFields = inputFields;
		this.mappingSheetRows = mappingSheetRows;
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
				
		inputFields.addAll(this.inputFields);
		
		for(MappingSheetRow mappingSheetRow : this.mappingSheetRows){
			if(this.mappingSheetRows!=null)
			mappingSheetRows.add((MappingSheetRow) mappingSheetRow.clone());
		}
		
		ATMapping atMapping = new ATMapping(inputFields, mappingSheetRows);
		
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

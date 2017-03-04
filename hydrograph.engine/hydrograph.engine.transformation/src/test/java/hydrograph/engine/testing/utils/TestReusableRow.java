package hydrograph.engine.testing.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

public class TestReusableRow extends ReusableRow implements Serializable{

	private Object[] inputRow;
	private Map<String,Integer> fieldMap;
	
	public TestReusableRow(LinkedHashSet<String> fields) {
		super(fields);
		fieldMap=new HashMap<>();
		int index=0;
		for(String field:fields){
			fieldMap.put(field, index++);
		}
	}
	
	public void setRow(Object[] row){
		inputRow=row;
	}

	@Override
	protected Comparable getFieldInternal(int index) {
		return (Comparable) inputRow[index];
	}

	@Override
	protected Comparable getFieldInternal(String fieldName) {
		return (Comparable) inputRow[fieldMap.get(fieldName)];
	}

	@Override
	protected void setFieldInternal(int index, Comparable value) {
		inputRow[index]=value;
	}

	@Override
	protected void setFieldInternal(String fieldName, Comparable value) {
		inputRow[fieldMap.get(fieldName)]=value;
	}

}

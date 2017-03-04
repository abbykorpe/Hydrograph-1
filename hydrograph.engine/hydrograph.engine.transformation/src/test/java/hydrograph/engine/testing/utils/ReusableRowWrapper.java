package hydrograph.engine.testing.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class ReusableRowWrapper {

	private List<TestReusableRow> inputReusableRowList;
	private List<TestReusableRow> outputReusableRowList;
	
	public ReusableRowWrapper(LinkedHashSet<String> inputFields, LinkedHashSet<String> outputFields,List<Object[]> inputData){
		inputReusableRowList=new ArrayList<>();
		for(Object[] data:inputData){
			TestReusableRow reusableRow=new TestReusableRow(inputFields);
			reusableRow.setRow(data);
			inputReusableRowList.add(reusableRow);
		}
		outputReusableRowList=new ArrayList<>();
		for(int i=0;i<inputData.size();i++){
			TestReusableRow reusableRow=new TestReusableRow(outputFields);
			reusableRow.setRow(new Object[outputFields.size()]);
			outputReusableRowList.add(reusableRow);
		}
		
	}
	
	public List<TestReusableRow> getInputReusableRowList(){
		return inputReusableRowList;
	}
	
	public List<TestReusableRow> getOutputReusableRowList(){
		return outputReusableRowList;
	}
}

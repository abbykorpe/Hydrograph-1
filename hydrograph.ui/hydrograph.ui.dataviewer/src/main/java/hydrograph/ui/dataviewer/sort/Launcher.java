package hydrograph.ui.dataviewer.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;



class NaturalComparator<T> implements Comparator<T> {
	
	int compareColumnIndex=0;
	boolean order;
	public NaturalComparator(int compareColumnIndex,boolean order) {
		this.compareColumnIndex = compareColumnIndex;
		this.order = order;
	}

	public int compare(T a, T b) {
		
		DataObject dataObject1 = ((DataObject)((RowObject)a).getColumnList().get(compareColumnIndex));
		DataObject dataObject2 = ((DataObject)((RowObject)b).getColumnList().get(compareColumnIndex));
		
		if(this.order){
			if(dataObject1.getDataType().equalsIgnoreCase("String")){
				return dataObject1.getValue().compareTo(dataObject2.getValue());
			}else if(dataObject1.getDataType().equalsIgnoreCase("Integer")){
				return Integer.valueOf(dataObject1.getValue()).compareTo(Integer.valueOf(dataObject2.getValue()));
			}
		}else{
			if(dataObject1.getDataType().equalsIgnoreCase("String")){
				return dataObject2.getValue().compareTo(dataObject1.getValue());
			}else if(dataObject1.getDataType().equalsIgnoreCase("Integer")){
				return Integer.valueOf(dataObject2.getValue()).compareTo(Integer.valueOf(dataObject1.getValue()));
			}
		}
		
		
		
		//return ((DataObject)((RowObject)a).getColumnList().get(compareColumnIndex)).getValue().compareTo(((DataObject)((RowObject)b).getColumnList().get(compareColumnIndex)).getValue());
		return 0;
	}
}

class RowObject<COLUMNS> implements Comparator<COLUMNS>{
	//Columns
	private List<COLUMNS> columnList;

	public RowObject(List<COLUMNS> columnList) {
		super();
		this.columnList = columnList;
	}

	public List<COLUMNS> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<COLUMNS> columnList) {
		this.columnList = columnList;
	}

	@Override
	public String toString() {
		return "{\"columnList\":\"" + columnList + "\"}";
	}

	@Override
	public int compare(COLUMNS arg0, COLUMNS arg1) {
		// TODO Auto-generated method stub
		return 0;
	}	
}

class DataObject{
	String value;
	String dataType;
	public DataObject(String value, String dataType) {
		super();
		this.value = value;
		this.dataType = dataType;
	}
	public String getValue() {
		return value;
	}
	public String getDataType() {
		return dataType;
	}
	@Override
	public String toString() {
		return "{\"value\":\"" + value + "\",\"dataType\":\"" + dataType
				+ "\"}";
	}	
}

public class Launcher {
	
	public static void main(String[] args) {
		
		DataObject dataObject0= new DataObject("Shrirang", "String");
		DataObject dataObject1= new DataObject("28", "Integer");
		LinkedList<DataObject> columnList1 = new LinkedList<>();
		columnList1.add(dataObject0);
		columnList1.add(dataObject1);
		RowObject rowObject1 = new RowObject<>(columnList1);
		
		DataObject dataObject2= new DataObject("Raj", "String");
		DataObject dataObject3= new DataObject("46", "Integer");
		LinkedList<DataObject> columnList2 = new LinkedList<>();
		columnList2.add(dataObject2);
		columnList2.add(dataObject3);
		RowObject rowObject2 = new RowObject<>(columnList2);
				
		DataObject dataObject4= new DataObject("Tara", "String");
		DataObject dataObject5= new DataObject("21", "Integer");
		LinkedList<DataObject> columnList3 = new LinkedList<>();
		columnList3.add(dataObject4);
		columnList3.add(dataObject5);
		RowObject rowObject3 = new RowObject<>(columnList3);
		
		
		List<RowObject> tableRows = new ArrayList<>();
		tableRows.add(rowObject1);
		tableRows.add(rowObject3);
		tableRows.add(rowObject2);
		
		//tableRows.add(rowObject3);
		//System.out.println(tableRows.toString());
		
		Collections.sort(tableRows, new NaturalComparator<RowObject>(0,false));
		System.out.println(tableRows.toString());
	}
}

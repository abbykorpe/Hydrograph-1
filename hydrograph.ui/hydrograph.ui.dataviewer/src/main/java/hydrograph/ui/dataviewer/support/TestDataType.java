package hydrograph.ui.dataviewer.support;

public class TestDataType {
	
	public static SortDataType getSortType(String sortDataType){
		
		for(SortDataType sortDataType1: SortDataType.values()){
			if(sortDataType1.getDataType().equals(sortDataType) ){
				return sortDataType1;
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getSortType(String.class.getName()));
	}
}

package hydrograph.ui.dataviewer.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.swt.widgets.Display;

	public class MainClass {
	public static void main(String[] args) {

		Map<String,String> fieldsAndTypes = new HashMap<>();
		fieldsAndTypes.put("firstName", "java.lang.String");
		fieldsAndTypes.put("lastName", "java.lang.String");
		fieldsAndTypes.put("age", "java.lang.Integer");
		fieldsAndTypes.put("dateOfBirth", "java.util.Date");
		
		FilterConditionsDialog test = new FilterConditionsDialog(Display.getDefault().getActiveShell());
		test.setFieldsAndTypes(fieldsAndTypes);
		test.open();
		
		
	/*	List<Condition> conditionsList = new ArrayList<>();
		conditionsList.add(new Condition("a1", "", "", ""));
		conditionsList.add(new Condition("a2", "and", "", ""));
		conditionsList.add(new Condition("a3", "or", "", ""));
		conditionsList.add(new Condition("a4", "and", "", ""));
		conditionsList.add(new Condition("a5", "or", "", ""));
		conditionsList.add(new Condition("a6", "and", "", ""));
		conditionsList.add(new Condition("a7", "or", "", ""));
		conditionsList.add(new Condition("a8", "and", "", ""));
		
		
		
		
		Map<Integer,List<List<Integer>>> groupSelectionMap = new TreeMap<>();
		List<List<Integer>> containerList = new ArrayList<>();*/

		/*List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		containerList.add(list);
		list = new ArrayList<>();
		list.add(3);
		list.add(4);
		list.add(5);
		containerList.add(list);
		list = new ArrayList<>();
		list.add(6);
		list.add(7);
		containerList.add(list);
		groupSelectionMap.put(1, containerList);
		
		containerList = new ArrayList<>();
		list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		containerList.add(list);
		list = new ArrayList<>();
		list.add(6);
		list.add(7);
		list.add(8);
		containerList.add(list);
		groupSelectionMap.put(2, containerList);*/
		
		/*List<Integer> list = new ArrayList<>();
		list.add(3);
		list.add(4);
		
		containerList.add(list);
		groupSelectionMap.put(1, containerList);
	
		
		containerList = new ArrayList<>();
		list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		containerList.add(list);
		
		groupSelectionMap.put(2, containerList);*/
		
		
		/*List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		containerList.add(list);
		list = new ArrayList<>();
		list.add(3);
		list.add(4);
		containerList.add(list);
		list = new ArrayList<>();
		list.add(6);
		list.add(7);
		list.add(8);
		containerList.add(list);
		groupSelectionMap.put(1, containerList);
		
		containerList = new ArrayList<>();
		list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		containerList.add(list);
		groupSelectionMap.put(2, containerList);*/
		
	/*	List<String> actualString = new LinkedList<>();
		for (int i = 0; i < conditionsList.size(); i++) {
			actualString.add(i, ""+(i + 1));
		}
	
		Set<Integer> treeSet  = (Set<Integer>) groupSelectionMap.keySet();
		if(treeSet.size() > 0){
			for (Integer position : treeSet) {
			List<List<Integer>> groupsInColumn = groupSelectionMap.get(position);
				for (int groupIndex = 0; groupIndex < groupsInColumn.size(); groupIndex++) {
					List<Integer> group = groupsInColumn.get(groupIndex);
					Integer firstItem = group.get(0);
					Integer firstItemIndex = actualString.indexOf("" + firstItem);
					actualString.add(firstItemIndex, "(");
					
					Integer lastItem = group.get(group.size()-1);
					Integer lastItemIndex = actualString.indexOf("" + lastItem);
					actualString.add(lastItemIndex + 1, ")");
				}
			}
		}
		
		int indexOfRelational = 1;
		for (int i = 2; i <= conditionsList.size(); i++) {
			
			int indexOfItem = actualString.indexOf("" + i);
			while(true){
				if((actualString.get(indexOfItem-1)).matches("\\d") 
						||(actualString.get(indexOfItem-1)).equalsIgnoreCase(")")){
					actualString.add(indexOfItem, conditionsList.get(indexOfRelational).getRelationalOperator());
					break;
				}else{
					indexOfItem = indexOfItem - 1;
				}
			}
			indexOfRelational += 1;
		}
		for (String item : actualString) {
			System.out.print(item + " ");
		}*/
	}
}
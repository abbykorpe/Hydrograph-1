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


package hydrograph.ui.common.util;

import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.datastructure.property.mapping.TransformMapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * @author Bitwise
 *
 */
public class TransformMappingFeatureUtility {
    
   public static final TransformMappingFeatureUtility INSTANCE= new TransformMappingFeatureUtility();
	
	private TransformMappingFeatureUtility()
	{
	}
	
	private List<FilterProperties> getObjectReferencePresentInOutputTable(
			List<FilterProperties> finalSortedList,
			MappingSheetRow mappingSheetRow) {
		List<FilterProperties> listToBeReturn=new ArrayList<>();
		for(FilterProperties filterProperties:mappingSheetRow.getOutputList())
		{
		   for(FilterProperties innerFilterProperties:finalSortedList)
		   {
			   if(filterProperties==innerFilterProperties)
			   {
				   listToBeReturn.add(innerFilterProperties);
				   break;
			   } 
			   
		   }  
		}
		return listToBeReturn;
	}
	private void setForegroundColorToBlack(Table inputtable, Table outputTable) {
		for(TableItem tableItem:inputtable.getItems())
		{	
			tableItem.setForeground(new Color(Display.getDefault(), 0, 0, 0));
		}
		for(TableItem tableItem:outputTable.getItems())
		{	
			tableItem.setForeground(new Color(Display.getDefault(), 0, 0, 0));
		}
	}
	public void highlightInputAndOutputFields
	(Text text,TableViewer inputFieldTableViewer,TableViewer outputFieldViewer,TransformMapping transformMapping,
			List<FilterProperties> finalSortedList ) 
	{
		Table inputtable=inputFieldTableViewer.getTable();
		Table outputTable=outputFieldViewer.getTable();
		
		if(text!=null)
		{	
		MappingSheetRow mappingSheetRow=null;
		setForegroundColorToBlack(inputtable, outputTable);
		for(MappingSheetRow mappingSheetRowIterate:transformMapping.getMappingSheetRows())
		{
			if(StringUtils.equals(text.getText(),mappingSheetRowIterate.getOperationID()))
			{
				mappingSheetRow=mappingSheetRowIterate;
				break;
			}	
		}
		for(FilterProperties filterProperties:mappingSheetRow.getInputFields())
		{
			for(TableItem tableItem:inputtable.getItems())
			{	
				
			if(StringUtils.equalsIgnoreCase(tableItem.getText(),filterProperties.getPropertyname()))
					{
				tableItem.setForeground(new Color(Display.getDefault(), 0, 128, 255));
				break;
					}		
			}
		}
		List<FilterProperties> templist = getObjectReferencePresentInOutputTable(
				finalSortedList, mappingSheetRow);
		for(FilterProperties filterProperties:mappingSheetRow.getOutputList())
		{
			for(TableItem tableItem:outputTable.getItems())
			{	
				
			if(StringUtils.equalsIgnoreCase(tableItem.getText(),filterProperties.getPropertyname())&&templist.contains(filterProperties))
					{
				tableItem.setForeground(new Color(Display.getDefault(),0, 128,255));
				break;
					}		
			}
		}
		
		}
		else
		{
			setForegroundColorToBlack(inputtable, outputTable);
		}	
	}
	public List<MappingSheetRow> getActiveMappingSheetRow(List<MappingSheetRow> mappingSheetRows)
	{
		List<MappingSheetRow> activeMappingSheetRow=new ArrayList<>();
		for(MappingSheetRow mappingSheetRow:mappingSheetRows)
		{
			if(mappingSheetRow.isActive())
			activeMappingSheetRow.add(mappingSheetRow);	
		}
		return activeMappingSheetRow;
	}
	public  void setCursorOnDeleteRow(TableViewer tableViewer,int[] indexes,List<?> gridList){
        Table table = tableViewer.getTable();
        //highlight after deletion
        if(indexes.length == 1 && gridList.size() > 0){//only one item is deleted
              if(gridList.size() == 1){//list contains only one element
                    table.select(0);// select the first element
                    tableViewer.editElement(tableViewer.getElementAt(0), 0);
              }
              else if(gridList.size() == indexes[0]){//deleted last item 
                    table.select(gridList.size() - 1);//select the last element which now at the end of the list
                    tableViewer.editElement(tableViewer.getElementAt(gridList.size() - 1), 0);
              }
              else if(gridList.size() > indexes[0]){//deleted element from middle of the list
                    table.select( indexes[0] == 0 ? 0 : (indexes[0] - 1) );//select the element from at the previous location
                    tableViewer.editElement(tableViewer.getElementAt(indexes[0] == 0 ? 0 : (indexes[0] - 1)), 0);
              }
        }
        else if(indexes.length >= 2){//multiple items are selected for deletion
              if(indexes[0] == 0){//delete from 0 to ...
                    if(gridList.size() >= 1){//list contains only one element
                          table.select(0);//select the remaining element
                          tableViewer.editElement(tableViewer.getElementAt(0), 0);
                    }
              }
              else{//delete started from element other than 0th element
                    table.select((indexes[0])-1);//select element before the start of selection   
                    tableViewer.editElement(tableViewer.getElementAt((indexes[0])-1), 0);
              }
        }
    }

}

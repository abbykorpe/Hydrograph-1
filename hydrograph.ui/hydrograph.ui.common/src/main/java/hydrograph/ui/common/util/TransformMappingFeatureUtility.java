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
}

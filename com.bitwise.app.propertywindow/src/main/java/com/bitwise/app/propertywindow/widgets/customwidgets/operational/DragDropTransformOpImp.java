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

 
package com.bitwise.app.propertywindow.widgets.customwidgets.operational;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;

import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.datastructure.property.NameValueProperty;
import com.bitwise.app.common.datastructure.property.mapping.MappingSheetRow;
import com.bitwise.app.propertywindow.widgets.utility.DragDropOperation;

public class DragDropTransformOpImp implements DragDropOperation {

	private List listOfInputFields;
	private boolean isSingleColumn;
	private TableViewer operationInputfieldtableviewer;
	private List listOfOutputFields;
	private TableViewer operationOutputFieldTableViewer;
	private List<FilterProperties> outputFieldList;
	
	public List<FilterProperties> getOutputFieldList() {
		return outputFieldList;
	}

	private List<NameValueProperty> mapAndPassThroughField;
	private TableViewer outputFieldTableViewer;
	private List<MappingSheetRow> mappingSheetRows;
	private TransformDialog transformDialogNew;
	
	public DragDropTransformOpImp(TransformDialog transformDialogNew,List<MappingSheetRow> mappingSheetRows,TableViewer outputFieldTableViewer,List<NameValueProperty> mapAndPassThroughField,List<FilterProperties> outputFieldList,List listOfOutputFields,List listOfInputFields, boolean isSingleColumn,TableViewer tableViewer,TableViewer t) {
		super();
		this.listOfInputFields = listOfInputFields;
		this.isSingleColumn = isSingleColumn;
		this.operationInputfieldtableviewer=tableViewer;
		this.listOfOutputFields=listOfOutputFields;
		this.operationOutputFieldTableViewer=t;
		this.outputFieldList=outputFieldList;
		this.mapAndPassThroughField=mapAndPassThroughField;
		this.outputFieldTableViewer=outputFieldTableViewer;
		this.transformDialogNew=transformDialogNew;
		this.mappingSheetRows=mappingSheetRows;
	}
	
	public DragDropTransformOpImp(TransformDialog transformDialogNew,List listOfFields, boolean isSingleColumn,TableViewer tableViewer) {
		super();
		this.listOfInputFields = listOfFields;
		this.isSingleColumn = isSingleColumn;
		this.operationInputfieldtableviewer=tableViewer;
		this.transformDialogNew=transformDialogNew;
		
	}

	@Override
	public void saveResult(String result) {
		 if(isSingleColumn){
			   FilterProperties inputField = new FilterProperties();
	           inputField.setPropertyname(result);
	           FilterProperties outputField = new FilterProperties();
	           outputField.setPropertyname(result);
	        	if(!listOfInputFields.contains(inputField))
	        	{	
	        		listOfInputFields.add(inputField);
	        		listOfOutputFields.add(outputField); 	
	        		outputFieldList.addAll(listOfOutputFields);
	        		transformDialogNew.refreshOutputTable();
	        		}	
	        	
	             operationOutputFieldTableViewer.refresh();
	        	
	        }
	        else{
	        	NameValueProperty field = new NameValueProperty();
	        	field.setPropertyName(result);
	        	field.setPropertyValue(result);
	        	
            		listOfInputFields.add(field);
	        		transformDialogNew.refreshOutputTable();
	        
	        }
		 operationInputfieldtableviewer.refresh();
		
	}

}

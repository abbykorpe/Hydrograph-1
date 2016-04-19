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

 
package hydrograph.ui.propertywindow.widgets.customwidgets.operational;

import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.propertywindow.widgets.utility.DragDropOperation;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;



public class DragDropTransformOpImp implements DragDropOperation {

	private List listOfInputFields;
	private boolean isSingleColumn;
	private TableViewer operationInputfieldtableviewer;
	private List listOfOutputFields;
	private TableViewer operationOutputFieldTableViewer;
	private Map<String,List<FilterProperties>> outputFieldMap;
	
	public Map<String,List<FilterProperties>> getOutputFieldList() {
		return outputFieldMap;
	}

	private List<NameValueProperty> mapAndPassThroughField;
	private TableViewer outputFieldTableViewer;
	private List<MappingSheetRow> mappingSheetRows;
	private TransformDialog transformDialogNew;
	
	public DragDropTransformOpImp(TransformDialog transformDialogNew,List<MappingSheetRow> mappingSheetRows,TableViewer outputFieldTableViewer,List<NameValueProperty> mapAndPassThroughField,Map<String,List<FilterProperties>> outputFieldMap,List listOfOutputFields,List listOfInputFields, boolean isSingleColumn,TableViewer tableViewer,TableViewer t) {
		super();
		this.listOfInputFields = listOfInputFields;
		this.isSingleColumn = isSingleColumn;
		this.operationInputfieldtableviewer=tableViewer;
		this.listOfOutputFields=listOfOutputFields;
		this.operationOutputFieldTableViewer=t;
		this.outputFieldMap=outputFieldMap;
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
		 transformDialogNew.showHideValidationMessage();
		
		
	}

}

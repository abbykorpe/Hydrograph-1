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
import hydrograph.ui.datastructure.property.mapping.TransformMapping;
import hydrograph.ui.propertywindow.widgets.utility.DragDropOperation;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;



public class DragDropTransformOpImp implements DragDropOperation {

	private List listOfInputFields;
	private List<NameValueProperty> mapAndPassThroughField;
	private boolean isSingleColumn;
	private TableViewer operationInputfieldtableviewer;
	private List listOfOutputFields;
	private TableViewer operationOutputFieldTableViewer;
	private Map<String,List<FilterProperties>> outputFieldMap;
	private TransformDialog transformDialogNew;
	
	private List outerOutputList;
	
	/**
	 * @param transformDialogNew
	 * @param mappingSheetRows
	 * @param outputFieldTableViewer
	 * @param mapAndPassThroughField
	 * @param outputFieldMap
	 * @param listOfOutputFields
	 * @param listOfInputFields
	 * @param isSingleColumn
	 * @param tableViewer
	 * @param t
	 */
	public DragDropTransformOpImp(TransformDialog transformDialogNew,Map<String,List<FilterProperties>> outputFieldMap,List listOfOutputFields,List listOfInputFields, boolean isSingleColumn,TableViewer tableViewer,TableViewer t,List outerOutputList) {
		super();
		this.listOfInputFields = listOfInputFields;
		this.isSingleColumn = isSingleColumn;
		this.operationInputfieldtableviewer=tableViewer;
		this.listOfOutputFields=listOfOutputFields;
		this.operationOutputFieldTableViewer=t;
		this.outputFieldMap=outputFieldMap;
		this.transformDialogNew=transformDialogNew;
		this.outerOutputList=outerOutputList;
	}
	
	public DragDropTransformOpImp(TransformDialog transformDialogNew,TransformMapping transformMapping,List inputFields, boolean isSingleColumn,boolean isExpression,TableViewer tableViewer) {
		super();
		this.mapAndPassThroughField = transformMapping.getMapAndPassthroughField();
		this.listOfInputFields=inputFields;
		this.isSingleColumn = isSingleColumn;
		this.operationInputfieldtableviewer=tableViewer;
		this.transformDialogNew=transformDialogNew;
		
		this.outerOutputList=transformMapping.getOutputFieldList();
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
	        		outerOutputList.add(outputField);
	        		transformDialogNew.refreshOutputTable();
	        		}	
	        	
	             operationOutputFieldTableViewer.refresh();
	        	
	        }
	        else{
	        	NameValueProperty field = new NameValueProperty();
	        	field.setPropertyName(result);
	        	field.setPropertyValue(result);
	        	mapAndPassThroughField.add(field);
	        	field.getAttachFilterProperty().setPropertyname(field.getPropertyValue());
	        		
	        		outerOutputList.add(field.getAttachFilterProperty());
	        		transformDialogNew.refreshOutputTable();
	        
	        }
		 operationInputfieldtableviewer.refresh();
		 transformDialogNew.showHideValidationMessage();
		 transformDialogNew.getComponent().setLatestChangesInSchema(false);
		}
	
	
	public Map<String,List<FilterProperties>> getOutputFieldList() {
		return outputFieldMap;
	}
	

}

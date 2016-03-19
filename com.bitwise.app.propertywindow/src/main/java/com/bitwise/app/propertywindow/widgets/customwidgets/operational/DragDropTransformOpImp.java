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

import com.bitwise.app.common.datastructure.property.NameValueProperty;
import com.bitwise.app.common.datastructure.property.OperationField;
import com.bitwise.app.propertywindow.widgets.utility.DragDropOperation;

public class DragDropTransformOpImp implements DragDropOperation {

	private List listOfFields;
	private boolean isSingleColumn;
	private TableViewer tableViewer;
	
	
	
	
	public DragDropTransformOpImp(List listOfFields, boolean isSingleColumn,TableViewer tableViewer) {
		super();
		this.listOfFields = listOfFields;
		this.isSingleColumn = isSingleColumn;
		this.tableViewer=tableViewer;
	}

	@Override
	public void saveResult(String result) {
		 if(isSingleColumn){
	        	OperationField field = new OperationField();
	        	field.setName(result);
	        	if(!listOfFields.contains(field))
	        		listOfFields.add(field);
	        }
	        else{
	        	NameValueProperty field = new NameValueProperty();
	        	field.setPropertyName(result);
	        	if(!listOfFields.contains(field))
	        		listOfFields.add(field);
	        }
		 tableViewer.refresh();
		
	}

}

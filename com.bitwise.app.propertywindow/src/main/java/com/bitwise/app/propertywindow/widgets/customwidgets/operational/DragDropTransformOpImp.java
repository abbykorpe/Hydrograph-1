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

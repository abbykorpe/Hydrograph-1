package com.bitwise.app.propertywindow.widgets.joinlookupproperty;

import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.TableViewer;

import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.propertywindow.widgets.utility.DragDropOperation;

public class DragDropLookupImp implements DragDropOperation{

	private List listOfFields;
	private boolean isSingleColumn;
	private TableViewer tableViewer;
	
	public DragDropLookupImp(List listOfFields, boolean isSingleColumn,TableViewer tableViewer){
		super();
		this.listOfFields = listOfFields;
		this.isSingleColumn = isSingleColumn;
		this.tableViewer = tableViewer;
	}
	
	@Override
	public void saveResult(String result) {

			LookupMapProperty property = new LookupMapProperty();
			property.setSource_Field(result);
			String[] temp=result.split(Pattern.quote("."));
			property.setOutput_Field(temp[1]);
			if(!listOfFields.contains(property))
				listOfFields.add(property);
		tableViewer.refresh();
	}


}

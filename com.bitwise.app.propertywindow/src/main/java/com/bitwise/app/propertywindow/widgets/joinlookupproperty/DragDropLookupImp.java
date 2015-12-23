package com.bitwise.app.propertywindow.widgets.joinlookupproperty;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.TableViewer;

import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.common.util.Constants;
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
			String[] data=result.split(Pattern.quote("."));
			if(!data[1].isEmpty()){
				Matcher match = Pattern.compile(Constants.REGEX).matcher(data[1]);
				if(match.matches()){
				property.setSource_Field(result);
				property.setOutput_Field(data[1]);
				
				if(!listOfFields.contains(property))
					listOfFields.add(property);
				}
			}
			
			
			
		tableViewer.refresh();
	}


}

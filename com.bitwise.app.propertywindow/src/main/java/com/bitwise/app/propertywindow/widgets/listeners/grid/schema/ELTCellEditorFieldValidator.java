package com.bitwise.app.propertywindow.widgets.listeners.grid.schema;

import java.util.List;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Table;

import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;

/**
 * The Class ELTCellEditorFieldValidator.
 * 
 * @author Bitwise
 */
public class ELTCellEditorFieldValidator implements ICellEditorValidator {

	private Table table;
	private List schemaGrids;
	private ControlDecoration fieldNameDecorator;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	
	/**
	 * Instantiates a new ELT cell editor field validator.
	 * 
	 * @param table
	 *            the table
	 * @param schemaGrids
	 *            the schema grids
	 * @param fieldNameDecorator
	 *            the field name decorator
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTCellEditorFieldValidator(Table table, List schemaGrids,
			ControlDecoration fieldNameDecorator,PropertyDialogButtonBar propertyDialogButtonBar) {
		super();
		this.table = table;
		this.schemaGrids = schemaGrids;
		this.fieldNameDecorator = fieldNameDecorator;
		this.propertyDialogButtonBar=propertyDialogButtonBar;
	}

	@Override
	public String isValid(Object value) {
		String fieldName=(String) value;
		if(fieldName.equals("") || !fieldName.matches("[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}||[\\w]*")){     
			fieldNameDecorator.show();   
		return "Error";   
	}else{  
		fieldNameDecorator.hide(); 
		    
	}
		String selectedGrid = table.getItem(table.getSelectionIndex()).getText();
		for (int i = 0; i < schemaGrids.size(); i++) {
				GridRow schemaGrid = (GridRow)schemaGrids.get(i);
				String stringValue = (String) value;
				if ((schemaGrid.getFieldName().equalsIgnoreCase(stringValue) &&
						!selectedGrid.equalsIgnoreCase(stringValue))) {
					
				fieldNameDecorator.show();
				/*propertyDialogButtonBar.enableOKButton(false);
				propertyDialogButtonBar.enableApplyButton(false);*/
				return "Error";
			}
			else{ 
				fieldNameDecorator.hide();
				/*propertyDialogButtonBar.enableOKButton(true);
				propertyDialogButtonBar.enableApplyButton(true);*/
			}
		}
		return null;
	}

}

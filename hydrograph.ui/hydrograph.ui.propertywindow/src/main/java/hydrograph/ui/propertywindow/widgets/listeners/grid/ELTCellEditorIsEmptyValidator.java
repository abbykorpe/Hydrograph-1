package hydrograph.ui.propertywindow.widgets.listeners.grid;

import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ICellEditorValidator;


/**
 * @author Bitwise
 *
 */
public class ELTCellEditorIsEmptyValidator implements ICellEditorValidator{

	private ControlDecoration fieldEmptyDecorator;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	
	
	public ELTCellEditorIsEmptyValidator(ControlDecoration fieldEmptyDecorator, PropertyDialogButtonBar propertyDialogButtonBar) {
		super();
		this.fieldEmptyDecorator = fieldEmptyDecorator;
		this.propertyDialogButtonBar = propertyDialogButtonBar;
	}
	
	
	@Override
	public String isValid(Object value) {
		String selectedGrid=(String) value;
		if(selectedGrid.isEmpty()){
			fieldEmptyDecorator.show();
			return "Error";
		}else{
			fieldEmptyDecorator.hide();
		}
		
		return null;
	}

}

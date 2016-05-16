package hydrograph.ui.propertywindow.widgets.listeners.grid;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ICellEditorValidator;


/**
 * @author Bitwise
 *
 */
public class ELTCellEditorIsEmptyValidator implements ICellEditorValidator{

	private ControlDecoration fieldEmptyDecorator;
	
	
	public ELTCellEditorIsEmptyValidator(ControlDecoration fieldEmptyDecorator) {
		super();
		this.fieldEmptyDecorator = fieldEmptyDecorator;
	}
	
	
	@Override
	public String isValid(Object value) {
		String selectedGrid=(String) value;
		if(StringUtils.isBlank(selectedGrid)){
			fieldEmptyDecorator.show();
			return "Field should not be empty";
		}else{
			fieldEmptyDecorator.hide();
		}
		
		return null;
	}

}

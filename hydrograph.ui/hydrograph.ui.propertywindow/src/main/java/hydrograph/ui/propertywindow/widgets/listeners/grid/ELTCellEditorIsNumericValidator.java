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

 
package hydrograph.ui.propertywindow.widgets.listeners.grid;

import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ICellEditorValidator;


// TODO: Auto-generated Javadoc
/**
 * The Class ELTCellEditorIsNumericValidator.
 * 
 * @author Bitwise
 */
public class ELTCellEditorIsNumericValidator implements ICellEditorValidator {

		private ControlDecoration scaleDecorator;
		private PropertyDialogButtonBar propertyDialogButtonBar;
	
	/**
	 * Instantiates a new ELT cell editor is numeric validator.
	 * 
	 * @param scaleDecorator
	 *            the scale decorator
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public ELTCellEditorIsNumericValidator(ControlDecoration scaleDecorator,PropertyDialogButtonBar propertyDialogButtonBar) {
		super();
			this.scaleDecorator = scaleDecorator;
			this.propertyDialogButtonBar=propertyDialogButtonBar;
	}

	@Override
	public String isValid(Object value) {
		String selectedGrid=(String) value;
		if(!selectedGrid.matches("\\d+") && !selectedGrid.isEmpty()){     
			scaleDecorator.show();   
		return "Error";   
	}else{  
		scaleDecorator.hide(); 
		    
	}
	return null; 
	}

}

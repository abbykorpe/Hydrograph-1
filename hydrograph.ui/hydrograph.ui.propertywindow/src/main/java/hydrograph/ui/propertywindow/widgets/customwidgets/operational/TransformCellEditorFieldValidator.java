
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

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;

import java.util.List;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Table;


public class TransformCellEditorFieldValidator implements ICellEditorValidator {
	
	private ControlDecoration fieldNameDecorator;
	private ControlDecoration isFieldNameAlphanumericDecorator;

	public TransformCellEditorFieldValidator(ControlDecoration fieldNameDecorator,ControlDecoration isFieldNameAlphanumericDecorator) 
	{
		super();
		this.fieldNameDecorator = fieldNameDecorator;
        this.isFieldNameAlphanumericDecorator=isFieldNameAlphanumericDecorator;
	 }

	@Override
	public String isValid(Object value) {
		String fieldName = (String) value;
		if(fieldName.equals("")){     
			fieldNameDecorator.show();   
			return "Error";   
		}else{  
			fieldNameDecorator.hide(); 
			if(isFieldNameAlphanumeric(fieldName))
				isFieldNameAlphanumericDecorator.hide();
			else{
				isFieldNameAlphanumericDecorator.show();
				return "Error";
			}
		}
		return null;
	}
	private boolean isFieldNameAlphanumeric(String fieldName){
		return (!fieldName.matches(Constants.REGEX))?false:true;
	}
}

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

 
package com.bitwise.app.propertywindow.widgets.customwidgets.joinlookupproperty;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.propertywindow.widgets.customwidgets.lookupproperty.ELTLookupMapWizard;

/**
 * Custom Table Cell modifier class for lookup component
 * 
 * @author Bitwise
 *
 */
public class LookupCellModifier  implements ICellModifier{
	private Viewer viewer;
	
	public LookupCellModifier(Viewer viewer){
		this.viewer = viewer;
	}
	
	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		LookupMapProperty mapProperty =(LookupMapProperty)element;
		
		 if (ELTLookupMapWizard.PROPERTY_NAME.equals(property))
		        return mapProperty.getSource_Field();
		    else if (ELTLookupMapWizard.PROPERTY_VALUE.equals(property))
		        return mapProperty.getOutput_Field();
		    else
		        return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
		      element = ((Item) element).getData();
		 
		LookupMapProperty mapp = (LookupMapProperty) element;
		
		    if (ELTLookupMapWizard.PROPERTY_NAME.equals(property)){
		      mapp.setSource_Field((String)value);
		    }  if (ELTLookupMapWizard.PROPERTY_VALUE.equals(property))
		        mapp.setOutput_Field((String)value);
		    
		    viewer.refresh();
		
	}

}

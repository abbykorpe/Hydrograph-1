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

 
package com.bitwise.app.propertywindow.widgets.filterproperty;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.widgets.customwidgets.lookupproperty.ELTLookupMapWizard;
import com.bitwise.app.propertywindow.widgets.customwidgets.operational.TransformDialog;


/**
 * The Class ELTCellModifier.
 * 
 * @author Bitwise
 */
public class ELTCellModifier implements ICellModifier{
	
	private Viewer viewer;
	TransformDialog transformDialog;	
	/**
	 * Instantiates a new ELT cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public ELTCellModifier(Viewer viewer) {
		this.viewer = viewer;
	}
	public ELTCellModifier(Viewer viewer,TransformDialog transformDialog) {
		this.viewer = viewer;
		this.transformDialog=transformDialog;
	}

	
	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		FilterProperties filter = (FilterProperties) element;
		if(Constants.COMPONENT_NAME.equals(property))
		return filter.getPropertyname();
		else if(ELTLookupMapWizard.OPERATIONAL_INPUT_FIELD.equals(property)){
			return filter.getPropertyname();
		}
		else  if (Messages.OUTPUT_FIELD.equals(property)||Messages.INNER_OPERATION_INPUT_FIELD.equals(property) ||Messages.INNER_OPERATION_OUTPUT_FIELD.equals(property) )
			return filter.getPropertyname();
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
			element = ((Item) element).getData();

		FilterProperties p = (FilterProperties) element;
		
			if(Constants.COMPONENT_NAME.equals(property))
					p.setPropertyname((String)value);
			else if(ELTLookupMapWizard.OPERATIONAL_INPUT_FIELD.equals(property)){
				  p.setPropertyname((String)value);
			}
			else if(Messages.INNER_OPERATION_INPUT_FIELD.equals(property))
			p.setPropertyname((String)value);
			else if(Messages.INNER_OPERATION_OUTPUT_FIELD.equals(property))
			{		
			p.setPropertyname((String )value);	
			transformDialog.refreshOutputTable();
			}
			else if(Messages.OUTPUT_FIELD.equals(property))
			{
				p.setPropertyname((String )value);	
			}
		// Force the viewer to refresh
		viewer.refresh();
		
	}

}

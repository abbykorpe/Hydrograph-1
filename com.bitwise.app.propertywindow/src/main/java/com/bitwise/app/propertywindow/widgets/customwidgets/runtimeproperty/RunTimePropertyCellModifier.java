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

 
package com.bitwise.app.propertywindow.widgets.customwidgets.runtimeproperty;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

/**
 * The Class RunTimePropertyCellModifier.
 * 
 * @author Bitwise
 */
public class RunTimePropertyCellModifier implements ICellModifier {
	private Viewer viewer;
	private static final String PROPERTY_NAME = "PROPERTY_NAME"; 
	private static final String PROPERTY_VALUE = "PROPERTY_VALUE";

	/**
	 * Instantiates a new run time property cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public RunTimePropertyCellModifier(Viewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * Returns whether the property can be modified
	 * 
	 * @param element
	 *            the element
	 * @param property
	 *            the property
	 * @return boolean
	 */
	public boolean canModify(Object element, String property) {
		// Allow editing of all values
		return true;
	}

	/**
	 * Returns the value for the property
	 * 
	 * @param element
	 *            the element
	 * @param property
	 *            the property
	 * @return Object
	 */
	public Object getValue(Object element, String property) {
		RuntimeProperties p = (RuntimeProperties) element;
		
		if (PROPERTY_NAME.equals(property)) {

			return p.getPropertyName();

		} else if (PROPERTY_VALUE.equals(property))
			return p.getPropertyValue();
		else
			return null;
	}

	/**
	 * Modifies the element
	 * 
	 * @param element
	 *            the element
	 * @param property
	 *            the property
	 * @param value
	 *            the value
	 */
	public void modify(Object element, String property, Object value) {

		if (element instanceof Item)
			element = ((Item) element).getData();

		RuntimeProperties p = (RuntimeProperties) element;
		
		if (PROPERTY_NAME.equals(property))
			p.setPropertyName(((String) value));

		else if (PROPERTY_VALUE.equals(property))
			p.setPropertyValue((String) value);
		// Force the viewer to refresh
		viewer.refresh();
	}
}
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

 
package com.bitwise.app.propertywindow.widgets.customwidgets.secondarykeys;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.ParameterUtil;



// TODO: Auto-generated Javadoc
/**
 * The Class RunTimePropertyCellModifier.
 * 
 * @author Bitwise
 */
public class SecondaryColumnKeysWidgetCellModifier implements ICellModifier {
	private Viewer viewer;
	private static final String COLUMNNAME = "Column Name"; //$NON-NLS-1$
	private static final String SORTORDER = "Sort Order"; //$NON-NLS-1$

	/**
	 * Instantiates a new run time property cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public SecondaryColumnKeysWidgetCellModifier(Viewer viewer) {
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
		SecondaryColumnKeysInformation p = (SecondaryColumnKeysInformation) element;
		if (SORTORDER.equals(property)) {
			if(ParameterUtil.INSTANCE.isParameter(p.getColumnName())){
				return false;
			}
		}
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
		SecondaryColumnKeysInformation p = (SecondaryColumnKeysInformation) element;

		if (COLUMNNAME.equals(property)) {

			return p.getColumnName();

		} else if (SORTORDER.equals(property)){
			if(ParameterUtil.INSTANCE.isParameter(p.getColumnName())){
				return Constants.NONE_SORT_ORDER;
			}
			return p.getSortOrder();
		}
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

		SecondaryColumnKeysInformation p = (SecondaryColumnKeysInformation) element;
		System.out.println("###modify");
		if (COLUMNNAME.equals(property)){
			System.out.println("modifying column name");
			if(ParameterUtil.INSTANCE.isParameter((String)value)){
				System.out.println("Setting sort order from Param");
				p.setSortOrder(Constants.NONE_SORT_ORDER);
			}
			p.setColumnName(((String) value));

		}else if (SORTORDER.equals(property)){
			if(!ParameterUtil.INSTANCE.isParameter(p.getColumnName()) && !Constants.NONE_SORT_ORDER.equals((String) value)){
				p.setSortOrder((String) value);
			}else
				p.setSortOrder(Constants.ASCENDING_SORT_ORDER);
		}
		// Force the viewer to refresh
		viewer.refresh();
	}
}
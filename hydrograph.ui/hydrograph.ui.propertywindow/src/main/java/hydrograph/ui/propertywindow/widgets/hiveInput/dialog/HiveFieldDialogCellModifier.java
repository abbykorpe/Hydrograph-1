
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

 
package hydrograph.ui.propertywindow.widgets.hiveInput.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;


/**
 * The Class RunTimePropertyCellModifier.
 * 
 * @author Bitwise
 */
public class HiveFieldDialogCellModifier implements ICellModifier {
	private Viewer viewer;


	/**
	 * Instantiates a new run time property cell modifier.
	 * 
	 * @param viewer
	 *            the viewer
	 */
	public HiveFieldDialogCellModifier(Viewer viewer) {
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
		
		HivePartitionFields hivePartitionFieldDialog=(HivePartitionFields)element;
		List<String> list=hivePartitionFieldDialog.getRowFields();
		return list.get(getIndex(property));
		
	}

	private int getIndex(String property) {
		Map<String, List<String>> map=	(Map<String, List<String>>) viewer.getData("Map");
		List<String> list=new ArrayList<>(map.keySet());
		return list.indexOf(property);
		
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
		
		Label errorLabel=(Label) viewer.getData("Error"); 
		
		if (element instanceof Item){
			element = ((Item) element).getData();
		}
	
		HivePartitionFields hivePartitionFieldDialog=(HivePartitionFields)element;
		hivePartitionFieldDialog.getRowFields().set(getIndex(property), (String)value);
		
		for(HivePartitionFields row:(List<HivePartitionFields>)viewer.getInput()){
			if(StringUtils.isBlank(row.getRowFields().get(0))){
				errorLabel.setVisible(true);
				break;
			}else
				errorLabel.setVisible(false);
		}
		viewer.refresh();
	}
	
}
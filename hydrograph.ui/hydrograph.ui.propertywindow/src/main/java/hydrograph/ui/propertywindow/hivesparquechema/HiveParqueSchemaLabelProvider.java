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
package hydrograph.ui.propertywindow.hivesparquechema;

import hydrograph.ui.datastructure.property.BasicSchemaGridRow;
import hydrograph.ui.propertywindow.widgets.customwidgets.schema.GeneralGridWidgetBuilder;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * The Class HiveParqueSchemaLabelProvider.
 * 
 * @author Bitwise
 *
 */
public class HiveParqueSchemaLabelProvider implements ITableLabelProvider,ITableColorProvider{

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns the column text
	 * 
	 * @param element
	 *            the element
	 * @param columnIndex
	 *            the column index
	 * @return String
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		BasicSchemaGridRow schemaGrid = (BasicSchemaGridRow) element;
		switch (columnIndex) {
		case 0:
			return schemaGrid.getFieldName(); 
		case 1:
			return GeneralGridWidgetBuilder.getDataTypeKey()[schemaGrid.getDataType().intValue()];   
		case 2:
			return schemaGrid.getPrecision();
		case 3:
			return schemaGrid.getScale(); 
		case 4:
			if(schemaGrid.getScaleType()!=null)
			{
				return GeneralGridWidgetBuilder.getScaleTypeKey()[schemaGrid.getScaleType().intValue()];
			}
			else
			{
				return GeneralGridWidgetBuilder.getScaleTypeKey()[0];
			}
		case 5:
			return schemaGrid.getDateFormat();
		case 6:
			return schemaGrid.getDescription(); 
		}
		return null;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		return new Color(Display.getDefault(), new RGB(255, 255, 230));
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}


}

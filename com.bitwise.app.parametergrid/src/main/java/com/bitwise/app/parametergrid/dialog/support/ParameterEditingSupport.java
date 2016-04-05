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

package com.bitwise.app.parametergrid.dialog.support;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.bitwise.app.parametergrid.constants.MultiParameterFileDialogConstants;
import com.bitwise.app.parametergrid.dialog.models.Parameter;

/**
 * 
 * Cell editor for Parameter Grid
 * 
 * @author Bitwise
 *
 */
public class ParameterEditingSupport extends EditingSupport {
	private final TableViewer viewer;
	private final CellEditor editor;
	private String columnName;
		
	public ParameterEditingSupport(TableViewer viewer,String columnName) {
		super(viewer);
		this.viewer = viewer;
		this.editor = new TextCellEditor(viewer.getTable());
		this.columnName = columnName;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if(MultiParameterFileDialogConstants.PARAMETER_NAME.equals(columnName))
			return ((Parameter) element).getParameterName();
		else if(MultiParameterFileDialogConstants.PARAMETER_VALUE.equals(columnName))
			return ((Parameter) element).getParameterValue();
		else
			return null;
	}

	@Override
	protected void setValue(Object element, Object userInputValue) {
		
		if(MultiParameterFileDialogConstants.PARAMETER_NAME.equals(columnName))
			((Parameter) element).setParameterName(String.valueOf(userInputValue));
		else if(MultiParameterFileDialogConstants.PARAMETER_VALUE.equals(columnName))
			((Parameter) element).setParameterValue(String.valueOf(userInputValue));
		
		viewer.update(element, null);
	}
}

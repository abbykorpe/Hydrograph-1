package com.bitwise.app.parametergrid.dialog.support;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.bitwise.app.parametergrid.constants.ParameterGridConstants;
import com.bitwise.app.parametergrid.dialog.models.Parameter;

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
		if(ParameterGridConstants.PARAMETER_NAME.equals(columnName))
			return ((Parameter) element).getParameterName();
		else if(ParameterGridConstants.PARAMETER_VALUE.equals(columnName))
			return ((Parameter) element).getParameterValue();
		else
			return null;
	}

	@Override
	protected void setValue(Object element, Object userInputValue) {
		
		if(ParameterGridConstants.PARAMETER_NAME.equals(columnName))
			((Parameter) element).setParameterName(String.valueOf(userInputValue));
		else if(ParameterGridConstants.PARAMETER_VALUE.equals(columnName))
			((Parameter) element).setParameterValue(String.valueOf(userInputValue));
		
		viewer.update(element, null);
	}
}

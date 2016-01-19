package com.bitwise.app.propertywindow.widgets.customwidgets.mapping.tables.inputtable;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Text;

import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.datastructures.InputField;


public class InputFieldEditingSupport extends EditingSupport{

	private TableViewer viewer;
	private CellEditor cellEditor;
	//public boolean editingsuport = true;
	public InputFieldEditingSupport(TableViewer  viewer) {
		super(viewer);
		this.viewer = viewer;
		//cellEditor = new TextCellEditor(viewer.getTable(), SWT.MULTI | SWT.WRAP | SWT.BORDER);
		cellEditor = new TextCellEditor(viewer.getTable());
		
		final Text aaa = (Text)cellEditor.getControl();
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return cellEditor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		return ((InputField)element).getFieldName();
	}

	@Override
	protected void setValue(Object element, Object userInputValue) {
		((InputField)element).setFieldName((String)userInputValue);
		viewer.update(element, null);
	}
}

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

 
package com.bitwise.app.propertywindow.widgets.listeners.grid;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.listeners.extended.GridCellEditorListener;

// TODO: Auto-generated Javadoc
/**
 * 
 * @author Bitwise
 * Oct 12, 2015
 * 
 */

public class GridChangeListener {
	private ArrayList<CellEditor> cellEditors;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private GridChangeListener(){
		
	}
	
	/**
	 * Instantiates a new grid change listener.
	 * 
	 * @param cellEditors
	 *            the cell editors
	 * @param propertyDialogButtonBar
	 *            the property dialog button bar
	 */
	public GridChangeListener(CellEditor[] cellEditors,PropertyDialogButtonBar propertyDialogButtonBar){
		this.cellEditors = new ArrayList<>();
		this.cellEditors.addAll(Arrays.asList(cellEditors));
		this.propertyDialogButtonBar = propertyDialogButtonBar;
	}
	
	/**
	 * Attach cell change listener.
	 */
	public void attachCellChangeListener(){
		for(CellEditor cellEditor : cellEditors){
			if(cellEditor instanceof TextCellEditor)
				cellEditor.addListener(new GridCellEditorListener(propertyDialogButtonBar));
			else if(cellEditor instanceof ComboBoxCellEditor){
				attachComboChangeListener(cellEditor);
			}
		}
	}
	private void attachComboChangeListener(CellEditor cellEditor) {
		((CCombo)cellEditor.getControl()).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				propertyDialogButtonBar.enableApplyButton(true);
				super.widgetSelected(e);
			}
			
		});
	}
}

package com.bitwise.app.propertywindow.widgets.customwidgets.mapping.tables.inputtable;


import java.util.ArrayList;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.datastructures.InputField;


public class InputTable {
	private Table table;
	private TableViewer tableViewer;
	
	public void createTable(Composite inputTableComposite){
		createButtonPanel(inputTableComposite);		
		tableViewer = createTableViewer(inputTableComposite);		
		addColumns(tableViewer);
		setDragSource();
	}

	private void setDragSource() {
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		DragSource dragSource = new DragSource(table, DND.DROP_MOVE | DND.DROP_COPY);
		dragSource.setTransfer(types);
		dragSource.addDragListener(new DragSourceAdapter() {
			  public void dragSetData(DragSourceEvent event) {
				    DragSource ds = (DragSource) event.widget;
				    Table table = (Table) ds.getControl();
				    TableItem[] selection = table.getSelection();

				    StringBuffer buff = new StringBuffer();
				    for (int i = 0, n = selection.length; i < n; i++) {
				      buff.append(", " + selection[i].getText());
				    }
				    event.data = buff.toString();
				  }
				});
	}

	public void addColumns(TableViewer tableViewer) {
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnInputFields = tableViewerColumn.getColumn();
		tblclmnInputFields.setWidth(208);
		tblclmnInputFields.setText("Input Fields");
		tableViewerColumn.setLabelProvider(new InputFieldColumnLabelProvider());		
		tableViewerColumn.setEditingSupport(new InputFieldEditingSupport(tableViewer));
		
		
	}

	private TableViewer createTableViewer(Composite inputTableComposite) {
		final TableViewer tableViewer = new TableViewer(inputTableComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI );
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer.setContentProvider(new TableContenetProvider());		
		addKeyListener(tableViewer);	
		ColumnViewerToolTipSupport.enableFor(tableViewer, ToolTip.NO_RECREATE); 
		return tableViewer;
	}

	private void addKeyListener(final TableViewer tableViewer) {
		tableViewer.getTable().addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				//DO Nothing
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.stateMask & SWT.SHIFT) != 0){
					if(e.keyCode == 97){
						Object obj=new InputField("Hello");
						tableViewer.add(obj);
						tableViewer.getTable().showItem(tableViewer.getTable().getItem(tableViewer.getTable().getItems().length-1));
					}
				}	
			}
		});
	}

	private void createButtonPanel(Composite inputTableComposite) {
		Composite composite_2 = new Composite(inputTableComposite, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(2, false);
		gl_composite_2.marginHeight = 0;
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_composite_2.heightHint = 26;
		composite_2.setLayoutData(gd_composite_2);
		
		Button btnAdd_1 = new Button(composite_2, SWT.NONE);
		btnAdd_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addInputField();
				
			}
		});
		btnAdd_1.setText("Add");
		
		Button btnRemove_1 = new Button(composite_2, SWT.NONE);
		btnRemove_1.setText("Remove");
	}
	
	private void addInputField() {		
		InputField inputField = new InputField("Hello");
		tableViewer.add(inputField);
	}
	
	public void setInput(ArrayList<InputField> inputData){		
		tableViewer.setInput(inputData);
	}
	
	public boolean isValidTable(){
		boolean valid = true;
		
		//table.getItems()[0].getText(0)

		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_ ]*$");
		for(TableItem item : table.getItems()){		
			if (!pattern.matcher(item.getText(0)).matches()) {
				valid=false;
			}
		}
		
		return valid;
	}
	
}

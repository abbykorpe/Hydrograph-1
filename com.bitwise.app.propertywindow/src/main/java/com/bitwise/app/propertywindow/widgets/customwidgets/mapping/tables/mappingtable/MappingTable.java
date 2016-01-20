package com.bitwise.app.propertywindow.widgets.customwidgets.mapping.tables.mappingtable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.datastructure.property.mapping.MappingSheetRow;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.datastructures.RowData;
import com.bitwise.app.propertywindow.widgets.dialogs.ELTOperationClassDialog;


public class MappingTable {
	private Table table;
	private TableViewer tableViewer;
	
	private WidgetConfig widgetConfig;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	
	private boolean validTable=true;
	
	
	public MappingTable(WidgetConfig widgetConfig, PropertyDialogButtonBar propertyDialogButtonBar){
		this.widgetConfig = widgetConfig;
		this.propertyDialogButtonBar = propertyDialogButtonBar;
	}
	
	public void createTable(Composite mappingTableComposite){
		createButtonPanel(mappingTableComposite);
		
		tableViewer = createTableViewer(mappingTableComposite);		 
		addColumns(tableViewer);
		
		addDropListener();
	}

	private void addDropListener() {
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		DropTarget dropTarget = new DropTarget(table, DND.DROP_MOVE | DND.DROP_COPY);
		dropTarget.setTransfer(types);
		dropTarget.addDropListener(new DropTargetAdapter() {
		  public void dragEnter(DropTargetEvent event) {
		    if (event.detail != DND.DROP_DEFAULT) {
		      event.detail = (event.operations & DND.DROP_COPY) != 0 ? DND.DROP_COPY : DND.DROP_NONE;
		    }
		    for (int i = 0, n = event.dataTypes.length; i < n; i++) {
		      if (TextTransfer.getInstance().isSupportedType(event.dataTypes[i])) {
		        event.currentDataType = event.dataTypes[i];
		      }
		    }
		  }

		  public void dragOver(DropTargetEvent event) {
		    event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
		  }

		  public void drop(DropTargetEvent event) {
		    if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
		      DropTarget target = (DropTarget) event.widget;
		      final Table table = (Table) target.getControl();
		      
		      final String data = (String) event.data;

		      Point pt = new Point(event.x, event.y);
		      TableItem item = table.getItem(table.toControl(pt));
		       
		      //System.out.println("+++ table Bounds " + table.getBounds() + " " + item + " " + MouseInfo.getPointerInfo().getLocation() + " " + table.toControl(pt));
		      
		      if(item==null){
		    	  item=addRow(table);
			      ((RowData)item.getData()).setIn(data);
			      ((RowData)item.getData()).setOut(data);
			      //((RowData)item.getData()).setClazz(data);
		      }else{
		    	  ((RowData)item.getData()).setIn(((RowData)item.getData()).getIn().getText() +  data);
		    	  ((RowData)item.getData()).setOut(((RowData)item.getData()).getOut().getText() + data);
		      }
		      
		      validateTextField(((RowData)item.getData()).getIn());
		      validateTextField(((RowData)item.getData()).getOut());
		      
			  
		    }
		  }
		});
	}

	private void validateTextField(Text text){
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_, \r\n]*$");
		String tmp=text.getText();
		if (!pattern.matcher(text.getText()).matches()) {
			text.setForeground(text.getDisplay().getSystemColor(SWT.COLOR_RED));
		}else{
			text.setForeground(text.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		}
		
		if(text.getText().startsWith(","))					
			text.setText(text.getText().replaceFirst(",", ""));
		
		if(text.getText().endsWith(","))
			text.setText(text.getText().substring(0,text.getText().length()-1));
	}
	
	private void addColumns(final TableViewer tableViewer_1) {
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnInputFields_1 = tableViewerColumn_1.getColumn();
		tblclmnInputFields_1.setWidth(180);
		tblclmnInputFields_1.setText("Field Mapping");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnOperationClass = tableViewerColumn_2.getColumn();
		tblclmnOperationClass.setWidth(200);
		tblclmnOperationClass.setText("Operation Class");
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnEditOpsclass = tableViewerColumn_3.getColumn();
		tblclmnEditOpsclass.setWidth(32);
	    tblclmnEditOpsclass.setResizable(false);
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnOutputFields = tableViewerColumn_4.getColumn();
		tblclmnOutputFields.setWidth(180);
		tblclmnOutputFields.setText("Output fields");
	}

	private TableViewer createTableViewer(Composite mappingTableComposite) {
		final TableViewer tableViewer_1 = new TableViewer(mappingTableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer_1.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return tableViewer_1;
	}

	private void createButtonPanel(Composite mappingTableComposite) {
		Composite composite_1 = new Composite(mappingTableComposite, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(2, false);
		gl_composite_1.marginHeight = 0;
		gl_composite_1.marginWidth = 0;
		gl_composite_1.verticalSpacing = 0;
		composite_1.setLayout(gl_composite_1);
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_composite_1.heightHint = 26;
		composite_1.setLayoutData(gd_composite_1);
		
		Button btnAdd = new Button(composite_1, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addRow(table);
			}
		});
		btnAdd.setText("Add");
		
		Button btnRemove = new Button(composite_1, SWT.NONE);
		btnRemove.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnRemove.setText("Remove");
	}
	
	protected TableItem addRow(final Table table) {
		TableItem tableItem = new TableItem(table, SWT.NONE);
		
		TableEditor editor = new TableEditor(table);
	      Text column1Txt = new Text(table, SWT.MULTI | SWT.WRAP | SWT.BORDER);
	      editor.grabHorizontal = true;
	      editor.setEditor(column1Txt,tableItem, 0);	      
	      editor.grabVertical=true;
	      attachMappingTableInputOutputFieldListeners(column1Txt);
	      
	      
	      editor = new TableEditor(table);
	      final Text column2Txt = new Text(table, SWT.MULTI | SWT.WRAP | SWT.BORDER);
	      editor.grabHorizontal = true;
	      editor.setEditor(column2Txt, tableItem, 1);
	      editor.grabVertical=true;
	      
	      editor = new TableEditor(table);		      
	      final Button button = new Button(table, SWT.NONE);
	      button.setText("Edit");
	      button.pack();
	      editor.minimumWidth = button.getSize().x;
	      editor.horizontalAlignment = SWT.LEFT;
	      editor.setEditor(button, tableItem, 2);
	      editor.grabVertical=true;
	      button.addSelectionListener(new SelectionAdapter() {
	    	  @Override
	    	public void widgetSelected(SelectionEvent e) {
	    		  
	    		  OperationClassProperty operationClassProperty = new OperationClassProperty("", false);
				ELTOperationClassDialog eltOperationClassDialog = new ELTOperationClassDialog(button.getShell(), propertyDialogButtonBar,operationClassProperty.clone(), widgetConfig);
					eltOperationClassDialog.open();
					if(!eltOperationClassDialog.getOperationClassProperty().equals(operationClassProperty)){
						operationClassProperty = eltOperationClassDialog.getOperationClassProperty();
						//propertyDialogButtonBar.enableApplyButton(true);
						column2Txt.setText(operationClassProperty.getOperationClassPath());
					} 
	    		  
	    		super.widgetSelected(e);
	    		//FilterOperationClassUtility.createNewClassWizard(column2Txt,widgetConfig);
	    		
	    	}
	      });
	      
	      
	      editor = new TableEditor(table);	
	      final Text column3Txt = new Text(table, SWT.WRAP  | SWT.MULTI  | SWT.BORDER);
	      editor.grabVertical = true;
	      editor.grabHorizontal = true;
	      editor.setEditor(column3Txt, tableItem, 3);
	      editor.grabVertical=true;		
	      attachMappingTableInputOutputFieldListeners(column3Txt);
	      
	      tableItem.setData("in", column1Txt);
	      tableItem.setData("OpClass", column2Txt);
	      tableItem.setData("out", column3Txt);
	      
	      RowData rowData = new RowData(column1Txt, column3Txt, column2Txt);
	      
	      tableItem.setData(rowData);
	      return tableItem;
	      
	}

	private void attachMappingTableInputOutputFieldListeners(final Text column3Txt) {
		column3Txt.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {		
				/*if(!column3Txt.getText().endsWith("\r\n")){
					column3Txt.setText(column3Txt.getText() + "\r\n");
				}*/
				int width=column3Txt.getSize().x;
				Point size = column3Txt.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				
				if(size.y < table.getSize().y - 100)
					column3Txt.setSize(width, size.y);
				else
					column3Txt.setSize(width, table.getSize().y - 100);
			}
		});
	      
	      column3Txt.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {			
				int width=column3Txt.getSize().x;				
				Point size = column3Txt.computeSize(SWT.DEFAULT, SWT.DEFAULT);				
				//column3Txt.setSize(width, size.y);
				if(size.y < table.getSize().y - 100)
					column3Txt.setSize(width, size.y);
				else
					column3Txt.setSize(width, table.getSize().y - 100);
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				//Do Nothing
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				//Do Nothing
			}
		});
	      	      
	      column3Txt.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				
				String tmpx = column3Txt.getText();
				column3Txt.setText(column3Txt.getText().replace(",\r\n", ","));
				
				column3Txt.setBackground(column3Txt.getDisplay().getSystemColor(SWT.COLOR_WHITE));
				Pattern pattern = Pattern.compile("^[a-zA-Z0-9_, \r\n]*$");
				String tmp=column3Txt.getText();
				if (!pattern.matcher(column3Txt.getText()).matches()) {
					column3Txt.setForeground(column3Txt.getDisplay().getSystemColor(SWT.COLOR_RED));
				}else{
					column3Txt.setForeground(column3Txt.getDisplay().getSystemColor(SWT.COLOR_BLACK));
				}
				
				if(column3Txt.getText().startsWith(","))					
					column3Txt.setText(column3Txt.getText().replaceFirst(",", ""));
				
				if(column3Txt.getText().endsWith(","))
					column3Txt.setText(column3Txt.getText().substring(0,column3Txt.getText().length()-1));
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				
//				column3Txt.setText(column3Txt.getText().replace(",", ",\r\n"));
				String text=column3Txt.getText().replace("\r\n", "");
				text=text.replace(", ", ",");
				text=text.replace(",", ", ");
				column3Txt.setText(text.replace(",", ",\r\n"));
				
				int width=column3Txt.getSize().x;				
				Point size = column3Txt.computeSize(SWT.DEFAULT, SWT.DEFAULT);				
				//column3Txt.setSize(width, size.y);
				if(size.y < table.getSize().y - 100)
					column3Txt.setSize(width, size.y);
				else
					column3Txt.setSize(width, table.getSize().y - 100);
				
				column3Txt.setBackground(column3Txt.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
				column3Txt.setSelection(0);
			}
		});
	      
	      column3Txt.addListener(SWT.MeasureItem, new Listener() {
		        public void handleEvent(Event event) {
		        	Point size = event.gc.textExtent(column3Txt.getText());
		        	System.out.println(size.x);
		        	if(size.x > 180){
		        		column3Txt.setText(column3Txt.getText() + "\\n");
		        		System.out.println("Addingh \\n");
		        	}
		        }
		    });
	}
	
	public boolean isValidTable(){
		return validTable;
	}

	public List<MappingSheetRow> getData() {
		
		List<MappingSheetRow> mappingSheetRows = new LinkedList<>();
		
		for(TableItem item : table.getItems()){
			String input = ((Text)item.getData("in")).getText();
			String operationClass = ((Text)item.getData("OpClass")).getText();
			String output = ((Text)item.getData("out")).getText();
			
			MappingSheetRow mappingSheetRow = new MappingSheetRow(Arrays.asList(input.split(",")), operationClass, Arrays.asList(output.split(",")));
			mappingSheetRows.add(mappingSheetRow);
		}
		
		return mappingSheetRows;
	}
	
	public void setData(List<MappingSheetRow> mappingSheetRows){
		for(MappingSheetRow mappingSheetRow : mappingSheetRows){
			TableItem item = addRow(table);
			
			((Text)item.getData("in")).setText(mappingSheetRow.getImputFields().toString().replace("[", "").replace("]", ""));
			((Text)item.getData("OpClass")).setText(mappingSheetRow.getOperationClass().toString());
			((Text)item.getData("out")).setText(mappingSheetRow.getOutputList().toString().replace("[", "").replace("]", ""));
		}
	}
}


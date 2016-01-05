package com.bitwise.app.propertywindow.widgets.gridwidgets.basic;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class ELTSWTWidgets {

	public ELTSWTWidgets(){
		
	}
	
	public TableViewer createTableViewer(Composite composite,String[] prop, int[] bounds, int columnWidth,IStructuredContentProvider iStructuredContentProvider,ITableLabelProvider iTableLabelProvider){
		final TableViewer tableViewer= new TableViewer(composite, SWT.BORDER |SWT.MULTI | SWT.FULL_SELECTION | SWT.VIRTUAL);
	    	
		Table table = tableViewer.getTable();
		table.setBounds(bounds[0],bounds[1],bounds[2],bounds[3]);
		tableViewer.setContentProvider(iStructuredContentProvider);
	    tableViewer.setLabelProvider( iTableLabelProvider);
		tableViewer.setColumnProperties(prop); 
		tableViewer.getTable().addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.keyCode == SWT.ARROW_UP) {
					e.doit = false;
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					e.doit = false;
				} else if (e.keyCode == SWT.TRAVERSE_ARROW_NEXT) {
					e.doit = false;
				} else if (e.keyCode == SWT.TRAVERSE_ARROW_PREVIOUS) {
					e.doit = false;
				}

			}
		});
		
		table.setVisible(true);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		/*createTableColumns(table,prop, columnWidth);
		CellEditor[] editors =createCellEditorList(table,1); 
		tableViewer.setCellEditors(editors);*/
		
		return tableViewer;
	}
	
	public  void createTableColumns(Table table,String[] fields, int width){
		for (String field : fields) {
			TableColumn tableColumn= new TableColumn(table, SWT.CENTER);
			tableColumn.setText(field);
			tableColumn.setWidth(width);
			//tableColumn.pack();
		}
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
	}
	
	public  CellEditor[] createCellEditorList(Table table,int size){
		CellEditor[] cellEditor = new CellEditor[size];
		for(int i=0;i<size;i++)
		addTextEditor(table,cellEditor, i);

		return cellEditor;
	}
	
	protected void addTextEditor(Table table, CellEditor[] cellEditor, int position){
		cellEditor[position]=new TextCellEditor(table, SWT.COLOR_GREEN);
	}
	
	public Label labelWidget(Composite parent, int style, int[] bounds, String value, Image image){
		Label label = new Label(parent, style);
		label.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		label.setText(value);
		label.setImage(image);
		
		return label;
	}
	
	public Button buttonWidget(Composite parent, int style, int[] bounds, String value){
		Button button = new Button(parent, style);
			button.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
			button.setText(value);
		
		return button;
	}
	
	public Text textBoxWidget(Composite parent, int style,int[] bounds, String text, boolean value){
		Text textWidget = new Text(parent, style);
		textWidget.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		textWidget.setText(text);
		textWidget.setEditable(value);
		
		return textWidget;
	}
	
	public Combo comboWidget(Composite parent, int style, int[] bounds, String[] items, int selectionIndex){
		Combo comboBox = new Combo(parent, style);
		comboBox.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		comboBox.setItems(items);
		comboBox.select(selectionIndex);
		
		return comboBox;		
	}
	
	public void applyDragFromTableViewer(Control sourceControl, int index){
	    Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
	    final String portLabel = "in" + index + ".";
	    int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
	    final Table table =(Table)sourceControl;
	     DragSource source = new DragSource(table, operations);
	    source.setTransfer(types);
	    final String[] columnData = new String[1];
	    source.addDragListener(new DragSourceListener() {
	      public void dragStart(DragSourceEvent event) {
	      TableItem[] selection = table.getSelection();
	      
	        if (selection[0].getText().length()>0) { 
	          event.doit = true;
	          columnData[0] = portLabel.concat(selection[0].getText());
	        } else {
	          event.doit = false;
	        }
	      }; 

	      public void dragSetData(DragSourceEvent event) {
	        event.data = columnData[0];
	      }

	      public void dragFinished(DragSourceEvent event) {
	        if (event.detail == DND.DROP_COPY){
	        	columnData[0]=null;
	        }
	      }
	    });

	}
	
	
}

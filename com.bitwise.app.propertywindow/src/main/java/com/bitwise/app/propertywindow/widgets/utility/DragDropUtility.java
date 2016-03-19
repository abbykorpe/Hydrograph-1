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

 
package com.bitwise.app.propertywindow.widgets.utility;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class DragDropUtility {
	
	
	TableViewer viewer;
	
	public static DragDropUtility INSTANCE = new DragDropUtility();
	private DragDropUtility(){
		
	}
	

	public void applyDrop(final TableViewer tableViewerOpOuter,DragDropOperation dragDropOperation) {
		
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		DropTarget target = new DropTarget(tableViewerOpOuter.getTable(), operations);
		target.setTransfer(types);
		target.addDropListener(new DradDropUtilityListener(dragDropOperation));
	}
	
	
	public void applyDragFromTableViewer(Control sourceControl){
	    Transfer[] types = new Transfer[] { TextTransfer.getInstance() };

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
	          columnData[0] = selection[0].getText();
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
	
	public void applyDragFromTableViewerOuter(final TableViewer tableViewer){
	    Transfer[] types = new Transfer[] { TextTransfer.getInstance() };

	    int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
//	    final Table table =(Table)sourceControl;
	     DragSource source = new DragSource(tableViewer.getTable(), operations);
	    source.setTransfer(types);
	    final String[] columnData = new String[1];
	    source.addDragListener(new DragSourceListener() {
	      public void dragStart(DragSourceEvent event) { 
//	      int[] selection = table.getSelectionIndices();
	      CellEditor[] cellEditors = tableViewer.getCellEditors();
	      cellEditors[1].getValue();
	        if (((String) cellEditors[1].getValue()).length()>0) { 
	          event.doit = true;
	          columnData[0] = (String) cellEditors[1].getValue();
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

class DradDropUtilityListener extends DropTargetAdapter{
	private String result; 
	private DragDropOperation dragDropOperation;
	
	  public DradDropUtilityListener(DragDropOperation dragDropOperation) {
		this.dragDropOperation=dragDropOperation;
	}

	public void dragOver(DropTargetEvent event) {
    	  event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL; 
      }

      public void drop(DropTargetEvent event) {
        if (event.data == null) {
        	event.detail = DND.DROP_NONE;
        	return;
        }
        result=(String) event.data;
        dragDropOperation.saveResult(result);
        
      }
 }

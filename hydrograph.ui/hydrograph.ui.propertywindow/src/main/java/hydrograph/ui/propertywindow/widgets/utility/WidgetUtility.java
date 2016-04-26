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

 
package hydrograph.ui.propertywindow.widgets.utility;

import hydrograph.ui.propertywindow.messages.Messages;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * The Class WidgetUtility.
 * 
 * @author Bitwise
 */
public class WidgetUtility {
	private static final String ERROR = "Error";
	

	private WidgetUtility(){
	}
	 
	/**
	 * Creates the table viewer.
	 * 
	 * @param tableViewer
	 *            the table viewer
	 * @param iStructuredContentProvider
	 *            the i structured content provider
	 * @param iTableLabelProvider
	 *            the i table label provider
	 * @return the table viewer
	 */
	public static TableViewer createTableViewer( TableViewer tableViewer,IStructuredContentProvider iStructuredContentProvider,ITableLabelProvider iTableLabelProvider){
		tableViewer.setContentProvider(iStructuredContentProvider);
		tableViewer.setLabelProvider(iTableLabelProvider);
		return tableViewer;

	}
	
	/**
	 * Creates the table columns.
	 * 
	 * @param table
	 *            the table
	 * @param fields
	 *            the fields
	 */
	public static void createTableColumns(Table table,String[] fields){
		for (String field : fields) {
			TableColumn tc = new TableColumn(table, SWT.CENTER);
			tc.setText(field);
			tc.setMoveable(true);
		}
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}
	
	
	/**
	 * This Method use to create error message decorator,Its show an error image with message on applied controller field. 
	 * @param control
	 * @param message
	 * @return ControlDecoration
	 */
	
	public static ControlDecoration addDecorator(Control control,String message){
		ControlDecoration txtDecorator = new ControlDecoration(control,SWT.LEFT);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		Image img = fieldDecoration.getImage();
		txtDecorator.setImage(img);
		txtDecorator.setDescriptionText(message);
		return txtDecorator; 
	} 
	


	/**
	 * Checks if is file extention.
	 * @param file the file
	 * @param extention the extention
	 * @return true, if is file extention
	 */
	public static boolean isFileExtention(String file,String extention) {
		return extention.equalsIgnoreCase(file.substring(file.lastIndexOf(".")));
	}

	/**
	 * Error message.
	 * 
	 * @param message
	 *            the message
	 */
	public static void errorMessage(String message) {
		Shell shell = new Shell();
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		messageBox.setText(ERROR);
		messageBox.setMessage(message);
		messageBox.open();
	}

	/**
	 * Elt confirm message.
	 * 
	 * @param message
	 *            the message
	 * @return true, if successful
	 */
	public static boolean eltConfirmMessage(String message){
		Shell shell = new Shell();
		
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		messageBox.setText(Messages.WARNING);
	    messageBox.setMessage(message);
	    int response = messageBox.open();
	    if (response == SWT.YES){
	    	return true;
	    }
	    return false;
	}

	/**
	 * Adds the text editor.
	 * @param table the table
	 * @param cellEditor the cell editor
	 * @param position the position
	 */
	public static void addTextEditor(Table table, CellEditor[] cellEditor, int position){
		cellEditor[position]=new TextCellEditor(table);
	}

	/**
	 * Adds the combo box.
	 * @param table the table
	 * @param cellEditor the cell editor
	 * @param data the data
	 * @param position the position
	 */
	public static void addComboBox(Table table, CellEditor[] cellEditor, String[] data, int position){
		cellEditor[position] = new ComboBoxCellEditor(table, data,SWT.READ_ONLY);		
	}
}

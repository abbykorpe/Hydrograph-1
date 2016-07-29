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

package hydrograph.ui.propertywindow.widgets.hiveInput.dialog;

import hydrograph.ui.datastructure.property.InputHivePartitionColumn;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.dialogs.FieldDialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

public class FieldDialogWithAddValue extends FieldDialog {

	private Map<String, List<InputHivePartitionColumn>> fieldsMap;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private boolean isAnyUpdatePerformed;
	
	public FieldDialogWithAddValue(Shell parentShell,PropertyDialogButtonBar propertyDialogButtonBar) {
		
		super(parentShell, propertyDialogButtonBar);
		this.propertyDialogButtonBar=propertyDialogButtonBar;
	}

	
	
	@Override
	protected Composite addButtonPanel(final Composite container) {
	
		Composite btncomposite= super.addButtonPanel(container);
		Button button=new Button(btncomposite, SWT.NONE);
		button.setText("Add Values");
		
		
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				FieldDialogForAddingValue addingValue=new FieldDialogForAddingValue(container.getShell());
				TableItem[] items = getTargetTableViewer().getTable().getItems();
				List<InputHivePartitionColumn> persitedList= new ArrayList<>();
				if(!fieldsMap.isEmpty()){
					
					ArrayList<String> arrayList = new ArrayList<>(fieldsMap.keySet());
					if(null!=fieldsMap.get(arrayList.get(0)) && !fieldsMap.get(arrayList.get(0)).isEmpty()){
						persitedList.addAll( fieldsMap.get(arrayList.get(0)));
					}
				}
				
				fieldsMap = new LinkedHashMap<>();
				
				if(items.length>0){
				
				for (TableItem tableItem : items) {
					
									
					fieldsMap.put((String)tableItem.getText(),persitedList);
						
					
				}
				addingValue.setProperties(fieldsMap);
				
				addingValue.open();
				
				addingValue.setProperties(fieldsMap);
				
				if(persitedList.size()!=fieldsMap.get(fieldsMap.keySet().iterator().next().toString()).size()){
					
					isAnyUpdatePerformed=true;
				}
				
				}else{
					
					if(createErrorDialog().open()==SWT.OK){}
				}
				}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		return btncomposite;
	}

	
	public void setFieldDialogRuntimeProperties(Map<String, List<InputHivePartitionColumn>> fieldsMap) {
		this.fieldsMap=fieldsMap;
		List<String> runtimePropertySet=new ArrayList<>(fieldsMap.keySet());
		super.setRuntimePropertySet(runtimePropertySet);
	}
	
	
	@Override
	protected void okPressed() {
		TableItem[] items = getTargetTableViewer().getTable().getItems();
		
		if (fieldsMap != null && fieldsMap.isEmpty() && items.length > 0) {
			if (items.length > 0) {

				for (TableItem tableItem : items) {
					fieldsMap.put((String) tableItem.getText(),
							new ArrayList<InputHivePartitionColumn>());
				}
			}
		}else if(items.length!=fieldsMap.size()||isItemsNameChanged(items,fieldsMap.keySet())){
			
			List<InputHivePartitionColumn> incomingList = fieldsMap.get(fieldsMap.keySet().iterator().next().toString());
			fieldsMap.clear();
			
			for (TableItem tableItem : items) {
				fieldsMap.put((String) tableItem.getText(),
						incomingList);
			}
			
		}
		
		if(isAnyUpdatePerformed){
			propertyDialogButtonBar.enableApplyButton(true);
		}
		
		
		if(!compareAndChangeColor(items)){
			int rc=Message_Dialog();
			   if(rc==0){
				   super.okPressed();
			   }
			   else if(rc==1){
				   return;
			   }
		}
		
		super.okPressed();
		
	}


	protected boolean compareAndChangeColor(TableItem[] items){
		boolean check_field=compare_fields(items);
		if(!check_field){
			Color color = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			for (TableItem tableItem : items){
				tableItem.setForeground(color);
			}
		}
		return check_field;
	}
	
	private int Message_Dialog()
	{
		String message="The partition fields should appear at the end of the schema in the same order. Please rearrange fields either in schema or in partition fields";
		
			MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Rearrange Fields", null,
			    message, MessageDialog.ERROR, new String[] { "Rearrange Schema",
			  "Rearrange Partition Fields" }, 0);
			int result = dialog.open();
			return result;
	}
	
	 
	private boolean compare_fields(TableItem[] items)
	{
		ListIterator<String> t_itr,s_itr;
		boolean is_equal=true;
		
		List<String> target_fields = new ArrayList<String>();
		if(items.length > 0){
			for (TableItem tableItem : items){
				target_fields.add((String) tableItem.getText());
			}
		
		
		List<String> source_field = new ArrayList<String>(sourceFieldsList);
		
		t_itr=target_fields.listIterator(target_fields.size());
		s_itr = source_field.listIterator(source_field.size());
		
		
		while(t_itr.hasPrevious() & s_itr.hasPrevious()){
			if(StringUtils.equals(s_itr.previous(),t_itr.previous())){
				is_equal=true;
			}
			else{
				is_equal=false;
				break;
			}
		}
		}
		return is_equal;
		
	}
	
	private boolean isItemsNameChanged(TableItem[] items, Set<String> keySet) {
		
		List<String> tempList = new ArrayList<>();
		for (TableItem tableItem : items) {
			tempList.add(tableItem.getText());
		}
		
		if(!ListUtils.isEqualList(tempList, new ArrayList<>(keySet))){
			return true;
		}
		return false;
	}


	public Map<String, List<InputHivePartitionColumn>> getFieldDialogRuntimeProperties(){
		
		return this.fieldsMap;
	}

	private MessageBox createErrorDialog() {
		MessageBox messageBox = new MessageBox(new Shell(), SWT.ERROR | SWT.OK);
		messageBox.setMessage(Messages.EMPTY_TARGET_FIELD_ERROR);
		messageBox.setText("Error");
		return messageBox;
	}
	
	@Override
	protected void operationOnDrop(DropTargetEvent event) {
		super.operationOnDrop(event);
		compareAndChangeColor(getTargetTableViewer().getTable().getItems());
		
	}

	@Override
	protected void checkFieldsOnStartup() {

		compareAndChangeColor(getTargetTableViewer().getTable().getItems());
	}
}



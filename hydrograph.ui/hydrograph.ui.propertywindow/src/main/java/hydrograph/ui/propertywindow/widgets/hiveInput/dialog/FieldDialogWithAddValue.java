package hydrograph.ui.propertywindow.widgets.hiveInput.dialog;

import hydrograph.ui.datastructure.property.InputHivePartitionColumn;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.dialogs.FieldDialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

public class FieldDialogWithAddValue extends FieldDialog {

	private Map<String, List<InputHivePartitionColumn>> fieldsMap;
	
	public FieldDialogWithAddValue(Shell parentShell,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(parentShell, propertyDialogButtonBar);
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
					
					ArrayList arrayList = new ArrayList(fieldsMap.keySet());
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
		}
		super.okPressed();
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
	
	
}



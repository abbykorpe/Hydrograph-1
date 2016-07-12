package hydrograph.ui.propertywindow.widgets.hiveInput.dialog;

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
				fieldsMap = new LinkedHashMap<>();
				for (TableItem tableItem : items) {
					
					fieldsMap.put((String)tableItem.getText(), new ArrayList<InputHivePartitionColumn>());
				}
				addingValue.setProperties(fieldsMap);
				
				addingValue.open();
				
				addingValue.setProperties(fieldsMap);
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
	
	
	public Map<String, List<InputHivePartitionColumn>> getFieldDialogRuntimeProperties(){
		
		return this.fieldsMap;
	}

}



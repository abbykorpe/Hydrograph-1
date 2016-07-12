package hydrograph.ui.propertywindow.widgets.hiveInput.dialog;

import hydrograph.ui.common.util.ImagePathConstant;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.propertywindow.widgets.customwidgets.runtimeproperty.PropertyContentProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class FieldDialogForAddingValue extends Dialog {
	
	private Table table;
	private TableViewer tableViewer;
	private HivePartitionFieldDialog fieldDialog;
	private Map<String, List<InputHivePartitionColumn>> fieldsMap;
	private List<HivePartitionFieldDialog> fieldsDialogList;
	private Button buttonDelete;
	private List<InputHivePartitionColumn> inputHivePartitionColumns;
	
	
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public FieldDialogForAddingValue(Shell parentShell) {
		super(parentShell);
		fieldsDialogList= new ArrayList();
		fieldsMap= new LinkedHashMap<String, List<InputHivePartitionColumn>>();
		
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
	
		Composite container = (Composite) super.createDialogArea(parent);
		
		addButtonPanel(container);
		
		
		Composite tableComposite = new Composite(container, SWT.NONE);
		tableComposite.setLayout(new GridLayout(1, false));
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		
		for (String strColumnName : fieldsMap.keySet()) {
			
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			TableColumn fieldColumn = tableViewerColumn.getColumn();
			fieldColumn.setWidth(100);
			fieldColumn.setText(strColumnName);


		}
		
		tableViewer.setCellModifier(new HiveFieldDialogCellModifier(tableViewer));
		// enables the tab functionality
		TableViewerEditor.create(tableViewer, new ColumnViewerEditorActivationStrategy(tableViewer),
			ColumnViewerEditor.KEYBOARD_ACTIVATION | ColumnViewerEditor.TABBING_HORIZONTAL
			| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL);	
		

		
		tableViewer.setColumnProperties(getColumnProperties(fieldsMap.keySet()));
		
		tableViewer.setContentProvider(new PropertyContentProvider());
		tableViewer.setLabelProvider(new HiveFieldDialogLableProvider());
		tableViewer.setInput(fieldsDialogList);
		
		CellEditor[] cellEditors= new CellEditor[fieldsMap.size()];
		
		for(int i=0;i<fieldsMap.size();i++){
			cellEditors[i]=new TextCellEditor(table);
		}
		
		tableViewer.setCellEditors(cellEditors);
		tableViewer.setData("Map", fieldsMap);
		return container;
	}
	
	
	private String[] getColumnProperties(Set<String> keySet) {
		Object [] tempArray=keySet.toArray();
		String[] str=new String[keySet.size()];
		for(int i=0;i<str.length;i++){
			str[i]=(String) tempArray[i];
		}
		return str;
		
	}


	protected void addButtonPanel(Composite container){
		
		Composite btnsComposite = new Composite(container, SWT.NONE);
		btnsComposite.setLayout(new GridLayout(2, false));
		GridData gd_btnsComposite = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnsComposite.heightHint = 35;
		btnsComposite.setLayoutData(gd_btnsComposite);
		
		Button buttonAdd = new Button(btnsComposite, SWT.NONE);
		buttonAdd.setBounds(0, 0, 75, 25);
		buttonAdd.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.ADD_BUTTON));
		buttonAdd.addSelectionListener(addButtonListner(tableViewer));
		
		buttonDelete = new Button(btnsComposite, SWT.NONE);
		GridData gd_btnDel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnDel.widthHint = 36;
		buttonDelete.setLayoutData(gd_btnDel);
		buttonDelete.setBounds(0, 0, 75, 25);
		buttonDelete.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.DELETE_BUTTON));
		buttonDelete.addSelectionListener(deleteButtonListner(tableViewer));
		
	}
	
	
	
	private SelectionListener addButtonListner(TableViewer tableView) {
       SelectionListener listener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				HivePartitionFieldDialog fieldDialog = 
						new HivePartitionFieldDialog();
				
				List<String> rowFields=new ArrayList();
				
				for (String string : fieldsMap.keySet()) {
					rowFields.add("");
				}
				fieldDialog.setRowFields(rowFields);
				fieldsDialogList.add(fieldDialog);
				tableViewer.refresh();
				tableViewer.editElement(tableViewer.getElementAt(fieldsDialogList.size() - 1), 0);
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			
		};
		return listener;
	}
	
	
	private SelectionListener deleteButtonListner(TableViewer tableView) {
	       SelectionListener listener = new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();				
					for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
						Object selectedObject = iterator.next();
						tableViewer.remove(selectedObject);
						fieldsDialogList.remove(selectedObject);
						
					}
					if (fieldsDialogList.size() < 1) {
						buttonDelete.setEnabled(false);
					} 
					
				
					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {}
			};
			return listener;
		}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,	true);
		createButton(parent, IDialogConstants.CANCEL_ID,IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(548, 485);
	}
	
		
	public void setProperties(Map<String, List<InputHivePartitionColumn>> fieldsMap) {
		
		this.fieldsMap=fieldsMap;
		
	}
	
	
	
	@Override
	protected void okPressed() {
		
		
		if(validate()){
			
			List<String> columnNameList= new ArrayList<>(fieldsMap.keySet()) ;
			inputHivePartitionColumns = new ArrayList<>();
							
			for (HivePartitionFieldDialog hivePartitionFieldDialog  : fieldsDialogList) {
					
				inputHivePartitionColumns.add(arrangeColumndata(new ArrayList(hivePartitionFieldDialog.getRowFields()),new ArrayList(columnNameList)));
				
			}
			
			fieldsMap.put(columnNameList.get(0), inputHivePartitionColumns);
			
			
		}
		
		super.okPressed();
	}

	private boolean validate() {
		
		return true;
	}
	
	private InputHivePartitionColumn arrangeColumndata(List<String> rowFields, List<String> columnNameList){
		
		InputHivePartitionColumn hivePartitionColumn=null;
		
		
		if(!columnNameList.isEmpty()&&columnNameList.get(0)!=null){
			hivePartitionColumn = new InputHivePartitionColumn();  
			hivePartitionColumn.setName(columnNameList.get(0));
			hivePartitionColumn.setValue(rowFields.get(0));
			
			columnNameList.remove(0);
			rowFields.remove(0);
			
			hivePartitionColumn.setInputHivePartitionColumn(arrangeColumndata(columnNameList,rowFields));
					
		}
			
		  
		return hivePartitionColumn;
		
		
	}
	
	
	
	
}

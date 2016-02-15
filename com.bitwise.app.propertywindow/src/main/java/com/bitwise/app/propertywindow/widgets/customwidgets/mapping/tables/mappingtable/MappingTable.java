package com.bitwise.app.propertywindow.widgets.customwidgets.mapping.tables.mappingtable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.datastructure.property.mapping.InputField;
import com.bitwise.app.common.datastructure.property.mapping.MappingSheetRow;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.utils.SWTResourceManager;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.datastructures.MappingDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.mapping.datastructures.RowData;
import com.bitwise.app.propertywindow.widgets.dialogs.ELTOperationClassDialog;


public class MappingTable {
	private Table table;
	private TableViewer tableViewer;	
	private WidgetConfig widgetConfig;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private MappingDialogButtonBar mappingDialogButtonBar;
	private List<InputField> inputTableFieldList;
	private boolean validTable=true;
	private static final Logger logger = LogFactory.INSTANCE.getLogger(MappingTable.class);
	private boolean rowChecked = false;
	private Image checkedImage,uncheckedImage;
	private String componentName;
	
	public MappingTable(WidgetConfig widgetConfig, PropertyDialogButtonBar propertyDialogButtonBar, MappingDialogButtonBar mappingDialogButtonBar, String componentName){
		this.widgetConfig = widgetConfig;
		this.propertyDialogButtonBar = propertyDialogButtonBar;
		this.mappingDialogButtonBar = mappingDialogButtonBar;
		this.componentName = componentName;
	}
	
	public void createTable(Composite mappingTableComposite){
		createImageObjects();
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
		      
		      if(item==null){
		    	  item=addRow(table);
			      ((RowData)item.getData()).setIn(data.trim());
			      ((RowData)item.getData()).setOut(data.trim());			      
		      }else{
		    	  ((RowData)item.getData()).setIn(((RowData)item.getData()).getIn().getText() +  data);
		    	  ((RowData)item.getData()).setOut(((RowData)item.getData()).getOut().getText() + data);
		      }
		      autoFormatText(((RowData)item.getData()).getIn());
		      autoFormatText(((RowData)item.getData()).getOut());
			  validateRow((RowData)item.getData());
		    }
		  }
		});
	}
	
	private void createImageObjects(){
		String imagePath = XMLConfigUtil.CONFIG_FILES_PATH + "/icons/uncheckall.png" ;  
		uncheckedImage = new Image(null,imagePath);		
		
		imagePath = XMLConfigUtil.CONFIG_FILES_PATH + "/icons/checkall.png" ;  
		checkedImage = new Image(null,imagePath);		
	}
	
	private void addColumns(final TableViewer tableViewer_1) {
		
		TableViewerColumn tableViewerColumn_0 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		final TableColumn tblclmnInputFields_0 = tableViewerColumn_0.getColumn();
		tblclmnInputFields_0.setWidth(30);

		tblclmnInputFields_0.setImage(uncheckedImage);
		tblclmnInputFields_0.setAlignment(SWT.LEFT);
		
		tblclmnInputFields_0.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				logger.debug("column selected");
				if(rowChecked){	
					tblclmnInputFields_0.setImage(uncheckedImage);
					rowChecked = false;
				}					
				else{		
					tblclmnInputFields_0.setImage(checkedImage);
					rowChecked = true;
				}
				selectRows(rowChecked);
			}
			
		});
		
		
		//tblclmnInputFields_0.setText("");
		
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
		tblclmnEditOpsclass.setWidth(21);
	    tblclmnEditOpsclass.setResizable(false);
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		TableColumn tblclmnOutputFields = tableViewerColumn_4.getColumn();
		tblclmnOutputFields.setWidth(180);
		tblclmnOutputFields.setText("Output fields");
	}

	protected void selectRows(boolean rowChecked) {
		for(TableItem item : table.getItems()){
			((Button)item.getData("chk")).setSelection(rowChecked);	
		}
	}

	private TableViewer createTableViewer(Composite mappingTableComposite) {
		final TableViewer tableViewer_1 = new TableViewer(mappingTableComposite, SWT.BORDER | SWT.FULL_SELECTION );
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
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub				
				int index=0;
				for(TableItem item : table.getItems()){
					if(((Button)item.getData("chk")).getSelection()){
						((Button)table.getItem(index).getData("chk")).dispose();
						((Text)table.getItem(index).getData("in")).dispose();
						((Text)table.getItem(index).getData("OpClass")).dispose();
						((Button)table.getItem(index).getData("edit")).dispose();
						((Text)table.getItem(index).getData("out")).dispose();
						table.remove(index);
						index--;
						
					}
					index++;		
					
					//validateRow((RowData) ((Text)table.getItem(index).getData("out")).getData("rowData"));
					
				}
				table.getColumns()[0].setWidth(31);
				table.getColumns()[0].setWidth(30);
				validateDuplicatesInOutputColumn();
			}
		});
	}
	
	protected TableItem addRow(final Table table) {
		TableItem tableItem = new TableItem(table, SWT.NONE);
		
		TableEditor editor = new TableEditor(table);		      
	      final Button buttonChk = new Button(table, SWT.CHECK);
	      buttonChk.pack();
	      editor.minimumWidth = buttonChk.getSize().x;
	      editor.horizontalAlignment = SWT.CENTER;
	      editor.setEditor(buttonChk, tableItem, 0);
	      editor.grabVertical=true;
		
		  editor = new TableEditor(table);
	      Text column1Txt = new Text(table, SWT.MULTI | SWT.WRAP | SWT.BORDER);
	      editor.grabHorizontal = true;
	      editor.setEditor(column1Txt,tableItem, 1);	      
	      editor.grabVertical=true;
	      
	      editor = new TableEditor(table);
	      final Text column2Txt = new Text(table, SWT.MULTI | SWT.WRAP | SWT.BORDER);
	      editor.grabHorizontal = true;
	      editor.setEditor(column2Txt, tableItem, 2);
	      editor.grabVertical=true;
	      column2Txt.setEnabled(false);
	      
	      
	      editor = new TableEditor(table);		      
	      final Button button = new Button(table, SWT.NONE);
	      button.setText("...");
	      button.pack();
	      editor.minimumWidth = button.getSize().x;
	      editor.horizontalAlignment = SWT.LEFT;
	      editor.setEditor(button, tableItem, 3);
	      editor.grabVertical=true;
	      button.addSelectionListener(new SelectionAdapter() {
	    	  @Override
	    	public void widgetSelected(SelectionEvent e) {
	    		  
	    		  OperationClassProperty operationClassProperty = (OperationClassProperty) column2Txt.getData()  ;
	    		  
	    		if(operationClassProperty == null){
	    			operationClassProperty = new OperationClassProperty("Custom","", false, "");
	    		}
				ELTOperationClassDialog eltOperationClassDialog = new ELTOperationClassDialog(button.getShell(),
						propertyDialogButtonBar, operationClassProperty.clone(), widgetConfig, componentName);
					eltOperationClassDialog.open();
					if(!eltOperationClassDialog.getOperationClassProperty().equals(operationClassProperty)){
						operationClassProperty = eltOperationClassDialog.getOperationClassProperty();
						column2Txt.setText(operationClassProperty.getOperationClassPath());
						column2Txt.setData(operationClassProperty);
					} 
	    		  
	    		super.widgetSelected(e);	    		
	    	}
	      });
	      
	      
	      editor = new TableEditor(table);	
	      final Text column3Txt = new Text(table, SWT.WRAP  | SWT.MULTI  | SWT.BORDER);
	      editor.grabVertical = true;
	      editor.grabHorizontal = true;
	      editor.setEditor(column3Txt, tableItem, 4);
	      editor.grabVertical=true;		

	      
	      tableItem.setData("chk", buttonChk);
	      tableItem.setData("in", column1Txt);
	      tableItem.setData("OpClass", column2Txt);
	      tableItem.setData("edit", button);
	      tableItem.setData("out", column3Txt);
	      
	      
	      RowData rowData = new RowData(column1Txt, column3Txt, column2Txt);	      
	      tableItem.setData(rowData);
	      
	      column1Txt.setData("rowData",rowData);
	      column2Txt.setData("rowData",rowData);
	      column3Txt.setData("rowData",rowData);
	      
	      
	      attachFieldValidators(column1Txt,column2Txt,column3Txt);
	      
	      validateRow(rowData);
	      //validateNewRow(rowData);
	      
	      return tableItem;
	      
	}

	private void attachFieldValidators(final Text column1Txt, final Text column2Txt,
			final Text column3Txt) {
		
		// TODO Listeners
		attachTextModifyListener(column1Txt);
		attachTextModifyListener(column3Txt);
		
		attachTextMouseListener(column1Txt);
		attachTextMouseListener(column3Txt);
		
		attachFocusListener(column1Txt);
		attachFocusListener(column3Txt);
		
		attachOpeartionClassModifyListener(column2Txt, column3Txt);
		
	}
	
	private void attachFocusListener(final Text columnText) {
		
		columnText.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				RowData rowData = (RowData)columnText.getData("rowData");
				validateRow(rowData);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				autoFormatText(columnText);
			}
		});		
	}
	
	
	
	private void validateRow(RowData rowData){
		//TODO - validate row
		
		boolean emptyInClass = true;
		boolean emptyOut = true;
		Text txtIn = (Text)rowData.getIn();
		Text txtClazz = (Text)rowData.getClazz();
		Text txtOut = (Text)rowData.getOut();
		
		txtIn.setBackground(txtIn.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		txtClazz.setBackground(txtIn.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		txtOut.setBackground(txtIn.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		txtIn.setToolTipText(null);
		txtClazz.setToolTipText(null);
		txtOut.setToolTipText(null);
		
		
		//---------------- Validation - Input fields and Opeartion class, both can not be blank at the same time
		if(txtIn.getText().replace(",", "").replace("\t\n", "").trim().equalsIgnoreCase("") &&
		   txtClazz.getText().replace(",", "").replace("\t\n", "").trim().equalsIgnoreCase("")){
			if(txtOut.getToolTipText() == null)
				txtOut.setToolTipText("Input fields and Opeartion class, both can not be blank at the same time");
			else
				txtOut.setToolTipText("- " + txtOut.getToolTipText() + "\n- " + "Input fields and Opeartion class, both can not be blank at the same time");
			
			emptyInClass = false;
		}

		
		//---------------- Validation - Output field can't be blank
		if(txtOut.getText().replace(",", "").replace("\t\n", "").trim().equalsIgnoreCase("")){
			
			if(txtOut.getToolTipText() == null)
				txtOut.setToolTipText("Output field can't be blank");
			else
				txtOut.setToolTipText("- " + txtOut.getToolTipText() + "\n- " + "Output field can't be blank");
			
			emptyOut = false;
		}
		
		//--------------- Text Validation on input fields and output fields
		boolean in = validateInputText(txtIn); 
		boolean out = validateInputText(txtOut);
		
		
		//-------------- validate - if the row has input field from input table
		boolean validInputFields = true;
		if(!txtIn.getText().replace(",", "").replace("\t\n", "").trim().equalsIgnoreCase("")){
			List<String> mTableInputFieldListTmp = Arrays.asList(txtIn.getText().split(","));
			List<String> mTableInputFieldList = new LinkedList<>();
			for(int index =0 ;index < mTableInputFieldListTmp.size();index++){
				mTableInputFieldList.add(mTableInputFieldListTmp.get(index).trim());
			}
			if(!getInputTableFieldList().containsAll(mTableInputFieldList)){
				validInputFields = false;
				txtIn.setForeground(txtIn.getDisplay().getSystemColor(SWT.COLOR_RED));
				if(txtIn.getToolTipText() == null)
					txtIn.setToolTipText("Invalid input fields");
				else
					txtIn.setToolTipText("- " + txtIn.getToolTipText() + "\n- " + "Invalid input fields");
			}
		}
		
		
		//-------------- validate - check if mapping table input fields has duplicates
		boolean validUniqInputFields = true;
		if(!txtIn.getText().replace(",", "").replace("\t\n", "").trim().equalsIgnoreCase("")){
			List<String> mTableInputFieldListTmp = Arrays.asList(txtIn.getText().split(","));
			List<String> mTableInputFieldList = new LinkedList<>();
			for(int index =0 ;index < mTableInputFieldListTmp.size();index++){
				mTableInputFieldList.add(mTableInputFieldListTmp.get(index).trim());
			}
			
			Set<String> set = new HashSet<String>(mTableInputFieldList);

			if(set.size() < mTableInputFieldList.size()){
				txtIn.setForeground(txtIn.getDisplay().getSystemColor(SWT.COLOR_RED));
				validUniqInputFields = false;
				if(txtIn.getToolTipText() == null)
					txtIn.setToolTipText("Duplicate fields");
				else
					txtIn.setToolTipText("- " + txtIn.getToolTipText() + "\n- " + "Duplicate fields");
			}
		}
		
		
		//-------------- validate - check if mapping table output fields has duplicates
				boolean validUniqOutputFields = true;
				if(!txtOut.getText().replace(",", "").replace("\t\n", "").trim().equalsIgnoreCase("")){
					List<String> mTableInputFieldListTmp = Arrays.asList(txtOut.getText().split(","));
					List<String> mTableInputFieldList = new LinkedList<>();
					for(int index =0 ;index < mTableInputFieldListTmp.size();index++){
						mTableInputFieldList.add(mTableInputFieldListTmp.get(index).trim());
					}
					
					Set<String> set = new HashSet<String>(mTableInputFieldList);

					if(set.size() < mTableInputFieldList.size()){
						txtOut.setForeground(txtIn.getDisplay().getSystemColor(SWT.COLOR_RED));
						validUniqOutputFields = false;
						if(txtOut.getToolTipText() == null)
							txtOut.setToolTipText("Duplicate fields");
						else
							txtOut.setToolTipText("- " + txtOut.getToolTipText() + "\n- " + "Duplicate fields");
					}
				}
				
				
		//--------------- calculating result
		if(emptyOut && emptyInClass){
			txtOut.setBackground(txtOut.getDisplay().getSystemColor(SWT.COLOR_WHITE));			
		}else{
			txtOut.setBackground(SWTResourceManager.getColor(250, 128, 114));
		}
		
		//boolean validUniqOutputColumns=validateDuplicatesInOutputColumn();
		boolean validUniqOutputColumns = validateDuplicatesInOutputColumn();
		if(in && out && emptyOut && emptyInClass && validInputFields && validUniqInputFields && validUniqOutputFields && validUniqOutputColumns){
			validTable = true;
			validateInputOutputMapping(txtIn, txtClazz, txtOut);			
		}else{
			validTable = false;
		}	
		
	}

	private Set<String> findDuplicates(List<String> listContainingDuplicates) {
		 
		final Set<String> setToReturn = new HashSet<String>();
		final Set<String> set1 = new HashSet<String>();
 
		for (String yourInt : listContainingDuplicates) {
			if (!set1.add(yourInt)) {
				setToReturn.add(yourInt);
			}
		}
		return setToReturn;
	}
	
	private boolean validateDuplicatesInOutputColumn() {
		
				
		//------------- validate duplicates in output columns
		boolean validUniqOutputColumns = true;
		List<String> mTableOutputFieldList = new LinkedList<>();
		for(TableItem item : table.getItems()){
			if(!((Text)item.getData("out")).getText().trim().equalsIgnoreCase(""))
				mTableOutputFieldList.add(((Text)item.getData("out")).getText());			
		}
		
		
		List<String> allOutputField = new LinkedList<>();
		for(String outputField : mTableOutputFieldList){
			allOutputField.addAll(Arrays.asList(outputField.split(",")));			
		}
		
		Set<String> duplicateFieldSet = findDuplicates(allOutputField);
		
		for(TableItem item : table.getItems()){
			if(!((Text)item.getData("out")).getText().trim().equalsIgnoreCase("")){
				if(((Text)item.getData("out")).getToolTipText() != null){
					if(((Text)item.getData("out")).getToolTipText().contains("Duplicate output")){
						((Text)item.getData("out")).setBackground(((Text)item.getData("out")).getDisplay().getSystemColor(SWT.COLOR_WHITE));
						
						String tooltip="";
						List<String> tooltipLines = Arrays.asList(((Text)item.getData("out")).getToolTipText().split(System.getProperty("line.separator")));
						for(String tooltipLine : tooltipLines){
							if(!tooltipLine.contains("Duplicate output")){
								tooltip = tooltip + tooltipLine + "\n";
							}
						}
						
						if(tooltip.equalsIgnoreCase("")){
							((Text)item.getData("out")).setToolTipText(null);
						}else{
							((Text)item.getData("out")).setToolTipText(tooltip);
						}
						
					}
				}
			}
							
		}
		/*for(String field : duplicateFieldSet){
			for(TableItem item : table.getItems()){				
				if(!((Text)item.getData("out")).getText().trim().equalsIgnoreCase("")){
					List<String> outputCell = Arrays.asList(((Text)item.getData("out")).getText().split(","));
					if(outputCell.contains(field)){	
						if(((Text)item.getData("out")).getToolTipText() != null){
							((Text)item.getData("out")).getToolTipText().contains("Duplicate output");
						}
						((Text)item.getData("out")).setBackground(((Text)item.getData("out")).getDisplay().getSystemColor(SWT.COLOR_WHITE));
					}
				}			
			}
		}*/
		
		
		for(String field : duplicateFieldSet){
			for(TableItem item : table.getItems()){
				
				if(!((Text)item.getData("out")).getText().trim().equalsIgnoreCase("")){
					List<String> outputCell = Arrays.asList(((Text)item.getData("out")).getText().split(","));
					if(outputCell.contains(field)){
						validUniqOutputColumns = false;		
						if(((Text)item.getData("out")).getToolTipText() == null)
							((Text)item.getData("out")).setToolTipText("Duplicate output");
						else{
							if(!((Text)item.getData("out")).getToolTipText().contains("Duplicate output"))
								((Text)item.getData("out")).setToolTipText("- " + ((Text)item.getData("out")).getToolTipText() + "\n- " + "Duplicate output");
						}
						((Text)item.getData("out")).setBackground(SWTResourceManager.getColor(250, 128, 114));
					}
				}			
			}
		}
		
		
		
		/*for(int i=0;i<mTableOutputFieldList.size();i++){
			int counter=0;
			for(int j=0;j<mTableOutputFieldList.size();j++){
				if(i!=j && i<j){
					if(mTableOutputFieldList.get(i).equalsIgnoreCase(mTableOutputFieldList.get(j))){
						((Text)table.getItem(j).getData("out")).setForeground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_RED));
						validUniqOutputColumns = false;		
						if(((Text)table.getItem(j).getData("out")).getToolTipText() == null)
							((Text)table.getItem(j).getData("out")).setToolTipText("Duplicate output");
						else{
							if(!((Text)table.getItem(j).getData("out")).getToolTipText().contains("Duplicate output"))
								((Text)table.getItem(j).getData("out")).setToolTipText("- " + ((Text)table.getItem(j).getData("out")).getToolTipText() + "\n- " + "Duplicate output");
						}
							
					}
				}
			}
		}*/
		//return validUniqOutputColumns;
		return validUniqOutputColumns;
	}
	
	private void validateInputOutputMapping(Text txtIn, Text txtClazz,
			Text txtOut) {
		if(txtClazz.getText().trim().equalsIgnoreCase("")){
			if(txtIn.getText().split(",").length != txtOut.getText().split(",").length){
				txtIn.setBackground(SWTResourceManager.getColor(250, 128, 114));
				txtOut.setBackground(SWTResourceManager.getColor(250, 128, 114));
				
				
				if(txtIn.getToolTipText() == null)
					txtIn.setToolTipText("In case of NO Operation class, number of input and number of output fields should be same");
				else
					txtIn.setToolTipText("- " + txtIn.getToolTipText() + "\n- " + "In case of NO Operation class, number of input and number of output fields should be same");
				
				
				if(txtOut.getToolTipText() == null)
					txtOut.setToolTipText("In case of NO Operation class, number of input and number of output fields should be same");
				//else
					//txtOut.setToolTipText("- " + txtOut.getToolTipText() + "\n- " + "In case of NO Operation class, number of input and number of output fields should be same");
				
				validTable = false;
			}
		}
	}

	private void attachOpeartionClassModifyListener(Text column2Txt, final Text column3Txt){
		column2Txt.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				column3Txt.setText("");
				column3Txt.setBackground(SWTResourceManager.getColor(250, 128, 114));
			}
		});
	}
	
	private void resizeTextBoxBasedOnUserInput(final Text columnText) {
		int width=columnText.getSize().x;
		Point size = columnText.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		
		if(size.y < table.getSize().y - 100)
			columnText.setSize(width, size.y);
		else
			columnText.setSize(width, table.getSize().y - 100);
	}

	
	private void attachTextModifyListener(final Text columnText){
		columnText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {		
				resizeTextBoxBasedOnUserInput(columnText);
			}
		});
	}
	
	
	private void attachTextMouseListener(final Text columnText){
		columnText.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {			
					resizeTextBoxBasedOnUserInput(columnText);
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
	}
	
	
	
	
	private boolean validateInputText(final Text columnText) {
		boolean valid = true;
		columnText.setText(columnText.getText().replace(",\r\n", ","));
		
		columnText.setBackground(columnText.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_,\r\n]*$");
		if (!pattern.matcher(columnText.getText()).matches()) {
			columnText.setForeground(columnText.getDisplay().getSystemColor(SWT.COLOR_RED));
			if(columnText.getToolTipText() == null)
				columnText.setToolTipText("The text field should match \"^[a-zA-Z0-9_,\\r\\n]*\"");
			else
				columnText.setToolTipText("- " + columnText.getToolTipText() + "\n- " + "The text field should match \"^[a-zA-Z0-9_,\\r\\n]*\"");
			
			valid = false;			
		}else{
			columnText.setForeground(columnText.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			//columnText.setToolTipText(null);
		}
		
		if(columnText.getText().startsWith(","))					
			columnText.setText(columnText.getText().replaceFirst(",", ""));
		
		if(columnText.getText().endsWith(","))
			columnText.setText(columnText.getText().substring(0,columnText.getText().length()-1));
		
		return valid;
	}
	
	private void autoFormatText(final Text columnText) {
		String text=columnText.getText().replace("\r\n", "");
		text=text.replace(" ", "");
		//text=text.replace(", ", ",");
		//text=text.replace(",", ", ");
		columnText.setText(text.replace(",", ",\r\n"));
		
		resizeTextBoxBasedOnUserInput(columnText);
		
		columnText.setBackground(columnText.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		columnText.setSelection(0);
	}
	
	
	public boolean isValidTable(){
		
		if(table.getItemCount() == 0){
			return true;
		}
		
		return validTable;
	}

	public List<MappingSheetRow> getData() {
		
		List<MappingSheetRow> mappingSheetRows = new LinkedList<>();
		
		for(TableItem item : table.getItems()){
			String input = ((Text)item.getData("in")).getText();
			OperationClassProperty operationClass =(OperationClassProperty)((Text)item.getData("OpClass")).getData();
			
			String output = ((Text)item.getData("out")).getText();
			
			MappingSheetRow mappingSheetRow = new MappingSheetRow(Arrays.asList(input.split(",")), operationClass, Arrays.asList(output.split(",")));
			mappingSheetRows.add(mappingSheetRow);
		}
		
		return mappingSheetRows;
	}
	
	
	private List<String> getInputTableFieldList(){
		List<String> list = new LinkedList<>();
		for(InputField inputField: inputTableFieldList){
			list.add(inputField.getFieldName());
		}
		return list;
	}
	
	public void setData(List<MappingSheetRow> mappingSheetRows, List<InputField> list){
		
		inputTableFieldList = list;
		
		for(MappingSheetRow mappingSheetRow : mappingSheetRows){
			TableItem item = addRow(table);
			
			((Text)item.getData("in")).setText(mappingSheetRow.getImputFields().toString().replace("[","").replace("]", "").replace(" ", ""));
			if(mappingSheetRow.getOperationClassProperty() != null){
				((Text)item.getData("OpClass")).setText(mappingSheetRow.getOperationClassProperty().getOperationClassPath());
				((Text)item.getData("OpClass")).setData(mappingSheetRow.getOperationClassProperty());
			}			
			((Text)item.getData("out")).setText(mappingSheetRow.getOutputList().toString().replace("[","").replace("]", "").replace(" ", ""));
			
			validateRow((RowData)((Text)item.getData("out")).getData("rowData"));
		}
	}
}


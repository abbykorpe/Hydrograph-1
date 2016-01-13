package com.bitwise.app.propertywindow.widgets.customwidgets.lookupproperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.common.datastructure.property.LookupMappingGrid;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.widgets.customwidgets.joinlookupproperty.JoinContentProvider;
import com.bitwise.app.propertywindow.widgets.customwidgets.joinlookupproperty.LookupCellModifier;
import com.bitwise.app.propertywindow.widgets.customwidgets.joinlookupproperty.LookupLabelProvider;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTCellModifier;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterContentProvider;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterLabelProvider;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTSWTWidgets;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridAddSelectionListener;

public class ELTLookupMapWizard extends Dialog {

	private Label propertyError;
	private TableViewer outputTableViewer;
	private Button okButton;
	private TableViewer viewer1 = null;
    private TableViewer viewer2 = null;
	private TableViewer[] inputTableViewer = new TableViewer[2];
	
	public static String PROPERTY_NAME = "Source Field";
	public static String PROPERTY_VALUE = "Output Field";
	public static final String OPERATIONAL_INPUT_FIELD = "Field Name";
	
	private String[] INPUT_COLUMN_NAME = {OPERATIONAL_INPUT_FIELD};
	private String[] COLUMN_NAME = {PROPERTY_NAME, PROPERTY_VALUE};
	
	private List<LookupMapProperty> joinOutputList;
	private List<FilterProperties> joinInputList1;
	private List<FilterProperties> joinInputList2;
	private List<List<FilterProperties>> joinInputList  = new ArrayList<>();
	private ELTSWTWidgets eltswtWidgets = new ELTSWTWidgets();
	private LookupMappingGrid lookupPropertyGrid;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ELTLookupMapWizard(Shell parentShell, LookupMappingGrid lookupPropertyGrid) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE |  SWT.WRAP | SWT.APPLICATION_MODAL);
		this.lookupPropertyGrid = lookupPropertyGrid;
	}
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText("Lookup Mapping");
		container.setLayout(new GridLayout(4, false));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Composite composite = new Composite(container, SWT.None);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 574;
		gd_composite.widthHint = 257;
		composite.setLayoutData(gd_composite);
		
		if(lookupPropertyGrid != null){
	    	if(lookupPropertyGrid.getLookupInputProperties()!= null && !lookupPropertyGrid.getLookupInputProperties().isEmpty()){
	    		joinInputList1 = lookupPropertyGrid.getLookupInputProperties().get(0);
	    		joinInputList2 = lookupPropertyGrid.getLookupInputProperties().get(1);
	    	}
	    	 else{
	    		 joinInputList1 = new ArrayList<>();
	    		 joinInputList2 = new ArrayList<>();
	 	    }
	    	viewer1= createComposite(composite,10,joinInputList1, 0);
	    	viewer2 = createComposite(composite, 290,joinInputList2, 1);
	    	
	    	if(lookupPropertyGrid.getLookupMapProperties() != null && !lookupPropertyGrid.getLookupMapProperties().isEmpty()){
	    		joinOutputList = lookupPropertyGrid.getLookupMapProperties();
	    	}
	    	else{
	    		joinOutputList = new ArrayList<>();
	    	}
	    }
	    if(joinInputList != null){
	    	joinInputList.add(joinInputList1);
	    	joinInputList.add(joinInputList2);
	    }
		
		Composite composite_1 = new Composite(container, SWT.None);
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.heightHint = 600;
		gd_composite_1.widthHint = 400;
		composite_1.setLayoutData(gd_composite_1);
		
		labelWidget(composite_1, SWT.None, new int[]{0, 6, 100, 18}, "Output Mapping");
		outputTableViewer = eltswtWidgets.createTableViewer(composite_1, COLUMN_NAME,new int[]{0, 30, 398, 538}, 196, new JoinContentProvider(), new LookupLabelProvider());
		outputTableViewer.getTable().addMouseListener(new MouseAdapter() {
	    	@Override
			public void mouseDoubleClick(MouseEvent e) {
	    		joinOutputProperty(outputTableViewer);
			}
			@Override
			public void mouseDown(MouseEvent e) {}
		});
		eltswtWidgets.createTableColumns(outputTableViewer.getTable(), COLUMN_NAME, 196);
	    CellEditor[] editors = eltswtWidgets.createCellEditorList(outputTableViewer.getTable(),2);
	    	//editors[0].setValidator(valueEditorValidation(joinOutputList,Messages.EmptyNameNotification,outputTableViewer));
	    	editors[1].setValidator(createValueEditorValidator(outputTableViewer));
	    
	    outputTableViewer.setColumnProperties(COLUMN_NAME);
	    outputTableViewer.setCellModifier(new LookupCellModifier(outputTableViewer));
	    outputTableViewer.setCellEditors(editors);
	    outputTableViewer.setInput(joinOutputList);
	    
	    outputTableViewer.getTable().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(((TableItem)event.item) != null){
					if(StringUtils.isNotBlank(((TableItem)event.item).getText())){	
					String[] data = (((TableItem)event.item).getText()).split(Pattern.quote("."));
					if(data != null && data.length == 2){
						FilterProperties filter =new FilterProperties();
						filter.setPropertyname(data[1]);
						if(joinInputList1.indexOf(filter) >= 0){
							inputTableViewer[0].getTable().setSelection(joinInputList1.indexOf(filter));
						}
						else if(joinInputList2.indexOf(filter) >= 0){
							inputTableViewer[1].getTable().setSelection(joinInputList2.indexOf(filter));
						}
					}
					}
				}
			}
		});
	  		TableViewerEditor.create(outputTableViewer, new ColumnViewerEditorActivationStrategy(outputTableViewer), 
	  			ColumnViewerEditor.KEYBOARD_ACTIVATION | ColumnViewerEditor.TABBING_HORIZONTAL | 
	  			ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL);
		
		propertyError = new Label(composite_1, SWT.None);
	    propertyError.setBounds(0, 572, 350, 25);
	    propertyError.setForeground(new Color(Display.getDefault(), 255, 0, 0));
	    propertyError.setVisible(false);
		
	    
		Composite composite_2 = new Composite(composite_1, SWT.None);
		composite_2.setBounds(276, 4, 110, 24);
		createLabel(composite_2);
		
		new Label(container, SWT.NONE);
	
		populateWidget();
		//if(joinOutputList!=null){
			//DragDropUtility.INSTANCE.applyDrop(outputTableViewer, new DragDropLookupImp(joinOutputList, false, outputTableViewer));
			dropData(outputTableViewer, joinOutputList, true);
		//}
		return container;
	}
	
	private TableViewer createComposite(Composite parent, int y, final List<FilterProperties> joinInputList, final int tableViewerIndex){	
		Composite comGrid = new Composite(parent, SWT.BORDER);
		comGrid.setLayoutData(new RowData(267, 136));
		comGrid.setLayout(new RowLayout(SWT.VERTICAL));
		comGrid.setBounds(15, y, 233, 268);
		
		labelWidget(comGrid, SWT.LEFT, new int[]{0, 5, 90, 20}, "Input Index : in"+tableViewerIndex);
		
		inputTableViewer[tableViewerIndex] = eltswtWidgets.createTableViewer(comGrid, INPUT_COLUMN_NAME, new int[]{0, 30, 229, 232}, 224, new ELTFilterContentProvider(), new ELTFilterLabelProvider());
		inputTableViewer[tableViewerIndex].getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				addRowToTable(inputTableViewer[tableViewerIndex], joinInputList);
			}
			@Override
			public void mouseDown(MouseEvent e) {
			}
		});
		eltswtWidgets.createTableColumns(inputTableViewer[tableViewerIndex].getTable(), INPUT_COLUMN_NAME, 224);
	    CellEditor[] editors =eltswtWidgets.createCellEditorList(inputTableViewer[tableViewerIndex].getTable(),1);
	    editors[0].setValidator(valueEditorValidation(joinInputList ,Messages.EMPTYFIELDMESSAGE, inputTableViewer[tableViewerIndex]));
	    inputTableViewer[tableViewerIndex].setCellModifier(new ELTCellModifier(inputTableViewer[tableViewerIndex]));
	    inputTableViewer[tableViewerIndex].setColumnProperties(INPUT_COLUMN_NAME);
	    inputTableViewer[tableViewerIndex].setCellEditors(editors);
	    inputTableViewer[tableViewerIndex].setInput(joinInputList);
	
	    eltswtWidgets.applyDragFromTableViewer(inputTableViewer[tableViewerIndex].getTable(), tableViewerIndex);
		addButton(comGrid, new int[]{170, 8, 25, 20}, inputTableViewer[tableViewerIndex], joinInputList);
		deleteButton(comGrid,new int[]{200, 8, 25, 20}, inputTableViewer[tableViewerIndex], joinInputList);
		return inputTableViewer[tableViewerIndex]; 
	}
	
	private void addButton(Composite parent, int[] bounds, final TableViewer viewer, final List<FilterProperties> joinInputList){
		
		Button bt = new Button(parent, SWT.PUSH);
		bt.setImage(new Image(null,XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/add.png"));
		bt.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		
		bt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addRowToTable(viewer,joinInputList);
			}
		});
	}
	
	private void deleteButton(Composite parent, int[] bounds, final TableViewer viewer, final List<FilterProperties> joinInputList){
			
			Button bt = new Button(parent, SWT.PUSH);
			bt.setImage(new Image(null,XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/delete.png"));
			bt.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
			bt.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
					for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
						Object selectedObject = iterator.next();
						viewer.remove(selectedObject);
						joinInputList.remove(selectedObject);
					}
				}
			});
		}
	private void createLabel(Composite parent){	
		
		Button button = buttonWidget(parent, SWT.CENTER|SWT.PUSH, new int[]{0, 0, 25, 20}, "",new Image(null,XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/add.png"));
		ELTGridAddSelectionListener listener = new ELTGridAddSelectionListener();
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				joinOutputProperty(outputTableViewer);
			}
		});	 
		 
		Label delete = eltswtWidgets.labelWidget(parent, SWT.CENTER, new int[]{25, 0, 25, 20}, "",new Image(null,XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/delete.png"));
		delete.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				IStructuredSelection selection = (IStructuredSelection) outputTableViewer.getSelection();
				for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
					Object selectedObject = iterator.next();
					outputTableViewer.remove(selectedObject);
					joinOutputList.remove(selectedObject);
				}
			}
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
		 
		Label upLabel = eltswtWidgets.labelWidget(parent, SWT.CENTER, new int[]{50, 0, 25, 20}, "",new Image(null,XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/up.png"));
		upLabel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				int index2 = 0;
				int index1 = outputTableViewer.getTable().getSelectionIndex();

				if (index1 > 0) {
					String text = outputTableViewer.getTable().getItem(index1)
							.getText(0);
					String text1 = outputTableViewer.getTable().getItem(index1)
							.getText(1);
					index2 = index1 - 1;
					String data = outputTableViewer.getTable().getItem(index2)
							.getText(0);
					String data2 = outputTableViewer.getTable().getItem(index2)
							.getText(1);

					LookupMapProperty property = new LookupMapProperty();
					property.setSource_Field(data);
					property.setOutput_Field(data2);
					joinOutputList.set(index1, property);

					property = new LookupMapProperty();
					property.setSource_Field(text);
					property.setOutput_Field(text1);
					joinOutputList.set(index2, property);
					outputTableViewer.refresh();
					outputTableViewer.getTable().setSelection(index1 - 1);
				}
			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
		 
		Label downLabel = eltswtWidgets.labelWidget(parent, SWT.CENTER, new int[]{74, 0, 25, 20}, "",new Image(null,XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/down.png"));
		downLabel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				int index1 = outputTableViewer.getTable().getSelectionIndex();
				int index2 = 0;
				
				if (index1 < joinOutputList.size() - 1) {
					String text = outputTableViewer.getTable().getItem(index1)
							.getText(0);
					String text1 = outputTableViewer.getTable().getItem(index1)
							.getText(1);

					index2 = index1 + 1;

					String data = outputTableViewer.getTable().getItem(index2)
							.getText(0);
					String data1 = outputTableViewer.getTable().getItem(index2)
							.getText(1);

					LookupMapProperty p = new LookupMapProperty();
					p.setSource_Field(data);
					p.setOutput_Field(data1);
					joinOutputList.set(index1, p);

					p = new LookupMapProperty();
					p.setSource_Field(text);
					p.setOutput_Field(text1);
					joinOutputList.set(index2, p);
					outputTableViewer.refresh();
					outputTableViewer.getTable().setSelection(index1 + 1);
				
			}
			}
			@Override
			public void mouseDown(MouseEvent e) {  
				}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) { }
		});

	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,	true);
		createButton(parent, IDialogConstants.CANCEL_ID,IDialogConstants.CANCEL_LABEL, false);
	}
	
	private boolean validation(){
		int propertycount = 0;
		int propertyValuecount = 0;
		for(LookupMapProperty join : joinOutputList){
			if(join.getSource_Field().trim().isEmpty()){
					outputTableViewer.getTable().setSelection(propertycount);
					propertyError.setVisible(true);
					propertyError.setText(Messages.EmptyNameNotification);
					okButton.setEnabled(false);
					return false;
				}else if(join.getOutput_Field().trim().isEmpty()){
					outputTableViewer.getTable().setSelection(propertyValuecount);
					propertyError.setVisible(true);
					propertyError.setText(Messages.EmptyValueNotification);
				}else{
					propertyError.setVisible(false);
					okButton.setEnabled(true);
				}
				
			propertycount++;
			propertyValuecount++;
		}
		return true;
	}
	
	private boolean inputTabvalidation(){
		int propertycount = 0;
		for(FilterProperties join : joinInputList.get(0)){
			if(!join.getPropertyname().trim().isEmpty()){
				Matcher match = Pattern.compile(Constants.REGEX).matcher(join.getPropertyname());
				if(!match.matches()){
					outputTableViewer.getTable().setSelection(propertycount);
					propertyError.setVisible(true);
					propertyError.setText(Messages.PROPERTY_NAME_ALLOWED_CHARACTERS);
					okButton.setEnabled(false);
					return false;
				}
				}else{
					outputTableViewer.getTable().setSelection(propertycount);
					propertyError.setVisible(true);
					propertyError.setText(Messages.EmptyNameNotification);
					okButton.setEnabled(false);
					return false;
				}
				
			propertycount++;
		}
		return true;
	}
	// Creates Value Validator for table's cells
	private ICellEditorValidator  valueEditorValidation(final List<FilterProperties> joinInputList, final String ErrorMessage,final TableViewer viewer) {
		ICellEditorValidator propertyValidator = new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				String selectedField = viewer.getTable().getItem(viewer.getTable().getSelectionIndex()).getText();
				String valueToValidate = String.valueOf(value).trim();
				Matcher match = Pattern.compile(Constants.REGEX).matcher(valueToValidate);
				if (valueToValidate.isEmpty()) {
					propertyError.setText(ErrorMessage);
					propertyError.setVisible(true);
					return "ERROR"; //$NON-NLS-1$
				} else if(!match.matches()){
					propertyError.setVisible(true);
					propertyError.setText(Messages.PROPERTY_NAME_ALLOWED_CHARACTERS);
					okButton.setEnabled(false);
				}else{
					propertyError.setVisible(false);
					//okButton.setEnabled(true);
				}
				
				for(FilterProperties property :joinInputList){
					if(!selectedField.equalsIgnoreCase(valueToValidate) && property.getPropertyname().trim().equalsIgnoreCase(valueToValidate)){
						propertyError.setVisible(true);
						propertyError.setText(Messages.RuntimePropertAlreadyExists);
						okButton.setEnabled(false);
					}else{
						propertyError.setVisible(false);
						//okButton.setEnabled(true);
					}
				}
				
				return null;
			}
		};
		return propertyValidator;
	}

		public void populateWidget(){
			if(lookupPropertyGrid != null){
				inputTableViewer[0].refresh();
				inputTableViewer[1].refresh();
				outputTableViewer.refresh();
			}
		}
		
		public void getLookupPropertyGrid(){
			lookupPropertyGrid.setLookupInputProperties(joinInputList);
			lookupPropertyGrid.setLookupMapProperties(joinOutputList);
		}
		
		
			
	public Button buttonWidget(Composite parent, int style, int[] bounds, String value, Image image){
		Button button = new Button(parent, style);
			button.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
			button.setText(value);
			button.setImage(image);
		
		return button;
	}

	public Label labelWidget(Composite parent, int style, int[] bounds, String value){
		Label label = new Label(parent, style);
		label.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		label.setText(value);
		//label.setImage(image);
		
		return label;
	}
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(700, 719);
	}
	
	// Creates CellValue Validator for table's cells
	private ICellEditorValidator createValueEditorValidator(final TableViewer viewer) {
		final List duplicate = new ArrayList<>();
		ICellEditorValidator propertyValidator = new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				for (LookupMapProperty temp : joinOutputList) {
					String outputField = temp.getOutput_Field();
					if (!duplicate.contains(outputField)) {
						propertyError.setVisible(false);
						duplicate.add(outputField);
					}else{
						propertyError.setText(Messages.RuntimePropertAlreadyExists);
						propertyError.setVisible(true);							
					}
					
				}
				propertyError.setVisible(false);
				return null;
			}
		};
		return propertyValidator;
	}
			
	private  void joinOutputProperty(TableViewer viewer){
		LookupMapProperty property = new LookupMapProperty();
		if(joinOutputList.size() != 0){
			if(!validation())
				return;
		property.setSource_Field("");
		property.setOutput_Field("");
		joinOutputList.add(property);
		viewer.refresh();
		viewer.editElement(viewer.getElementAt(joinOutputList.size()-1), 0);
		} else {
			property.setSource_Field("");
			property.setOutput_Field("");
			joinOutputList.add(property);
			viewer.refresh();
		}
	}
	
	private void addRowToTable(TableViewer viewer, List<FilterProperties> joinInputList){
		FilterProperties join = new FilterProperties();
		if(joinInputList.size() != 0){
			if(!inputTabvalidation())
				return;
			join.setPropertyname("");
			joinInputList.add(join);
			viewer.refresh();
			viewer.editElement(viewer.getElementAt(joinInputList.size() - 1), 0);
		} else {
			join.setPropertyname("");
			joinInputList.add(join);
			viewer.refresh();
			viewer.editElement(join, 0);
		}
	}
	
	public void dropData(final TableViewer tableViewer, final List<LookupMapProperty> listOfFields, final boolean isSingleColumn){
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		DropTarget target = new DropTarget(tableViewer.getTable(), operations);
		target.setTransfer(types);
		target.addDropListener(new DropTargetAdapter() {
		      public void dragOver(DropTargetEvent event) {
		    	  event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL; 
		      }
		      public void drop(DropTargetEvent event) {
		        if (event.data == null) {
		        	event.detail = DND.DROP_NONE;
		        	return;
		        }
		        if(isSingleColumn){
		        	List<String> tempList = new ArrayList<>();
		        	
		        	LookupMapProperty field = new LookupMapProperty();
		        	String[] data = ((String)event.data).split(Pattern.quote("."));
		        	if(!data[1].isEmpty()){
		        		Matcher match = Pattern.compile(Constants.REGEX).matcher(data[1]);
						if(match.matches()){
							field.setSource_Field((String)event.data);
							field.setOutput_Field(data[1]);	
							
							if(!listOfFields.contains(field))
								listOfFields.add(field);
								tableViewer.refresh(); 
							
							for(LookupMapProperty lookupMapProperty : joinOutputList){
								String outputField = lookupMapProperty.getOutput_Field();
								if(!tempList.contains(outputField)){
									propertyError.setVisible(false);
									tempList.add(outputField);
									tableViewer.refresh(); 
								}else{
									propertyError.setVisible(true);
									propertyError.setText(Messages.OUTPUT_FIELD_EXISTS);
									outputTableViewer.refresh();
									outputTableViewer.editElement(outputTableViewer.getElementAt(joinOutputList.indexOf(lookupMapProperty)), 1);
								}
							}
		        	}
		        	}
		        	tableViewer.refresh(); 
		        }
		      } 
		});
	}
}

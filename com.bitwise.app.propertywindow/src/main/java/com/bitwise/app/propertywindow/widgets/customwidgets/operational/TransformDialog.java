package com.bitwise.app.propertywindow.widgets.customwidgets.operational;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.bitwise.app.common.datastructure.property.NameValueProperty;
import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.datastructure.property.OperationField;
import com.bitwise.app.common.datastructure.property.OperationSystemProperties;
import com.bitwise.app.common.datastructure.property.PropertyField;
import com.bitwise.app.common.datastructure.property.TransformOperation;
import com.bitwise.app.common.datastructure.property.TransformPropertyGrid;
import com.bitwise.app.common.datastructures.tooltip.TootlTipErrorMessage;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.factory.ListenerFactory;
import com.bitwise.app.propertywindow.fixedwidthschema.ELTFixedWidget;
import com.bitwise.app.propertywindow.messagebox.ConfirmCancelMessageBox;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.customwidgets.AbstractWidget.ValidationStatus;
import com.bitwise.app.propertywindow.widgets.customwidgets.config.WidgetConfig;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultCheckBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTTable;
import com.bitwise.app.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTCellEditorTransformValidator;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridDetails;
import com.bitwise.app.propertywindow.widgets.listeners.grid.transform.ELTTransforAddOpSysSelectionListener;
import com.bitwise.app.propertywindow.widgets.listeners.grid.transform.ELTTransforAddPropValueListener;
import com.bitwise.app.propertywindow.widgets.listeners.grid.transform.ELTTransforAddSelectionListener;
import com.bitwise.app.propertywindow.widgets.utility.DragDropUtility;
import com.bitwise.app.propertywindow.widgets.utility.FilterOperationClassUtility;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;
import com.bitwise.app.propertywindow.widgets.utility.SWTResourceManager;
import com.bitwise.app.propertywindow.widgets.utility.WidgetUtility;

// TODO: Auto-generated Javadoc
/**
 * The Class ELTOperationClassDialog.
 * 
 * @author Bitwise
 */
public class TransformDialog extends Dialog {

	
	public static final String PROPERTY_NAME = "Source";
	public static final String PROPERTY_VALUE = "Target";
	public static final String PROPERTY_NAME_INNER = "Property Name";
	public static final String PROPERTY_VALUE_INNER = "Property Value";

	public static final String OPERATIONAL_INPUT_FIELD = "Operation Input Fields";
	public static final String OPERATIONAL_OUTPUT_INNER_FIELD = "Operation Output Fields";
	public static final String OPERATIONAL_OUTPUT_FIELD = "Output Fields";
	public static final String OPERATIONAL_SYSTEM_FIELD = "Input Fields";
	private static final String ADD_ICON = XMLConfigUtil.CONFIG_FILES_PATH + "/icons/add.png";
	private static final String DELETE_ICON = XMLConfigUtil.CONFIG_FILES_PATH + "/icons/delete.png";
	private long operationId=1;
	private Composite container;
	private CellEditor[] editors; 
	
	Map<Text,Button> opClassMap = new LinkedHashMap<Text, Button>();

	private static final String[] NAME_VALUE_COLUMN = {PROPERTY_NAME, PROPERTY_VALUE};
	private static final String[] NAME_VALUE_COLUMN_INNER = {PROPERTY_NAME_INNER, PROPERTY_VALUE_INNER};
	
	private ELTTransforAddSelectionListener eltTransforAddSelectionListener = new ELTTransforAddSelectionListener();
	private ELTTransforAddOpSysSelectionListener opSysSelectionListener = new ELTTransforAddOpSysSelectionListener();
	private ELTTransforAddPropValueListener eltTransforAddPropValueListener = new ELTTransforAddPropValueListener();
  
    private  List<OperationField> opOutputOuterFields = new ArrayList<OperationField>();
    private  List<NameValueProperty> opOuterClassProperty = new ArrayList<NameValueProperty>();  
    private  List<OperationSystemProperties> operationSystemProperties = new ArrayList<OperationSystemProperties>();
    private List<TransformOperation> transformOperationList = new ArrayList<>();
	
    private TableViewer innerOpInputTabViewer; 
	private TableViewer innerKeyValueTabViewer;
	private TableViewer outerKeyValueTabViewer; 
	private TableViewer	outerOpTabViewer;
	private CheckboxTableViewer opSystemPropertiesTabViewer;
	private Button applyButton;
	private ExpandBar expandBar = null;
	private PropertyDialogButtonBar propertyDialogButtonBar;
	private ValidationStatus validationStatus;
	private TransformPropertyGrid transformPropertyGrid;
	private WidgetConfig widgetConfig;

	// Operational class label.
	AbstractELTWidget fieldError = new ELTDefaultLable(Messages.FIELDNAMEERROR).lableWidth(250);
	
	private TootlTipErrorMessage tootlTipErrorMessage = new TootlTipErrorMessage();

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param operationClassProperty 
	 */
	public TransformDialog(Shell parentShell,PropertyDialogButtonBar propertyDialogButtonBar,TransformPropertyGrid transformPropertyGrid,WidgetConfig widgetConfig) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE |  SWT.WRAP | SWT.APPLICATION_MODAL);
		this.transformPropertyGrid = transformPropertyGrid;
		this.widgetConfig=widgetConfig;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);

		container.setLayout(new FormLayout());
		container.getShell().setText("Transform");
		
		propertyDialogButtonBar = new PropertyDialogButtonBar(container);
		
		opOutputOuterFields=transformPropertyGrid.getOutputTreeFields()!=null ?transformPropertyGrid.getOutputTreeFields():opOutputOuterFields;
		opOuterClassProperty=transformPropertyGrid.getNameValueProps()!=null ? transformPropertyGrid.getNameValueProps():opOuterClassProperty;
		operationSystemProperties=transformPropertyGrid.getOpSysProperties()!=null ? transformPropertyGrid.getOpSysProperties():operationSystemProperties; 
		  
		Composite middleContainerComposite = new Composite(container, SWT.NONE);
		middleContainerComposite.setLocation(-9, -315);
		FormData fd_middleContainerComposite = new FormData();
		fd_middleContainerComposite.top = new FormAttachment(0, 10);
		fd_middleContainerComposite.bottom = new FormAttachment(100, -10);
		middleContainerComposite.setLayoutData(fd_middleContainerComposite);
			
		Composite leftContainerComposite = new Composite(container, SWT.NONE);
		fd_middleContainerComposite.left = new FormAttachment(leftContainerComposite, 6);
		leftContainerComposite.setLocation(-181, -285);
		FormData fd_leftContainerComposite_1 = new FormData();
		fd_leftContainerComposite_1.top = new FormAttachment(0, 40);
		fd_leftContainerComposite_1.left = new FormAttachment(0, 10);
		fd_leftContainerComposite_1.right = new FormAttachment(100, -1067);
		fd_leftContainerComposite_1.bottom = new FormAttachment(100, -10);
		leftContainerComposite.setLayoutData(fd_leftContainerComposite_1);

		Composite inpuComposite = new Composite(container, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(middleContainerComposite, 1, SWT.TOP);
		fd_composite.bottom = new FormAttachment(leftContainerComposite, -6);
		fd_composite.right = new FormAttachment(middleContainerComposite, -1);
		inpuComposite.setLayoutData(fd_composite);
		
		ELTDefaultSubgroupComposite inputDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(inpuComposite);
		inputDefaultSubgroupComposite.createContainerWidget();
		
		AbstractELTWidget addButtonInput = getButton("");
		AbstractELTWidget deleteButtonInput = getButton(""); 
		inputDefaultSubgroupComposite.attachWidget(addButtonInput);
		inputDefaultSubgroupComposite.attachWidget(deleteButtonInput);
		Button addOutKeyValueButtonIn=(Button) addButtonInput.getSWTWidgetControl();
		addOutKeyValueButtonIn.setParent(inpuComposite);
		addOutKeyValueButtonIn.setBounds(25, 0, 18, 18); 
		addOutKeyValueButtonIn.setImage(SWTResourceManager.getImage(ADD_ICON));
		
		Button deleteOutKeyValueButtonIn = (Button) deleteButtonInput.getSWTWidgetControl();
		deleteOutKeyValueButtonIn.setParent(inpuComposite);
		deleteOutKeyValueButtonIn.setBounds(48, 0, 18, 18);
		deleteOutKeyValueButtonIn.setImage(SWTResourceManager.getImage(DELETE_ICON));	
		
		opSystemPropertiesTabViewer = CheckboxTableViewer.newCheckList(
	    		leftContainerComposite, SWT.BORDER);
	    opSystemPropertiesTabViewer.setContentProvider(new TransformGridContentProvider());
	    opSystemPropertiesTabViewer.setLabelProvider( new OperationSystemLabelProvider());
	    opSystemPropertiesTabViewer.setColumnProperties(new String[]{OPERATIONAL_SYSTEM_FIELD}); 
	    opSystemPropertiesTabViewer.setCellModifier(new OperationSystemCellModifier(opSystemPropertiesTabViewer));
	    
	    Table table = opSystemPropertiesTabViewer.getTable();
		table.setVisible(true);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(0, 10, 160, 445);
		
	    table.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) { 
	        	 if(((TableItem)event.item).getChecked()){  
			    	   OperationField opField = new OperationField();
			    	   opField.setName(((TableItem)event.item).getText()); 
			    	   if(!opOutputOuterFields.contains(opField)){
			    	   opOutputOuterFields.add(opField);
			    	   outerOpTabViewer.refresh(); 
			    	   } 
			      }else{
			    	   OperationField opField = new OperationField();
			    	   opField.setName(((TableItem)event.item).getText()); 
			    	   if(opOutputOuterFields.contains(opField)){
			    	   opOutputOuterFields.remove(opField);
			    	   outerOpTabViewer.refresh();   
			      } 
			      }
	        }
	      }); 

	    
	    opSystemPropertiesTabViewer.addCheckStateListener(new ICheckStateListener(){
	        @Override public void checkStateChanged(    CheckStateChangedEvent event){
	          updateOperationSystemProperties(event.getElement(),event.getChecked());
	        }
	      });

		
		createTableColumns(table,new String[]{OPERATIONAL_SYSTEM_FIELD});
		for (int columnIndex = 0, n = table.getColumnCount(); columnIndex < n; columnIndex++) {
			table.getColumn(columnIndex).setWidth(155);
		}  

		CellEditor[] editors_opSys = null; 
		editors_opSys =createCellEditorList(table,new String[]{OPERATIONAL_SYSTEM_FIELD}.length); 
		opSystemPropertiesTabViewer.setCellEditors(editors_opSys);
	    
		opSystemPropertiesTabViewer.setInput(operationSystemProperties); 
		for (OperationSystemProperties opSystemProperty : operationSystemProperties) {
			opSystemPropertiesTabViewer.setChecked(opSystemProperty, opSystemProperty.isChecked());

		}

		ELTDefaultSubgroupComposite leftContainerComposite1 = new ELTDefaultSubgroupComposite(leftContainerComposite);
		leftContainerComposite1.createContainerWidget();

		ELTTable eltOpSysOuterTable = new ELTTable(opSystemPropertiesTabViewer);
		leftContainerComposite1.attachWidget(eltOpSysOuterTable);
		editors=editors_opSys;
		ControlDecoration errorDecorator = setDecorator("Property name should not be same or blank");
		editors[0].setValidator(new ELTCellEditorTransformValidator((Table)eltOpSysOuterTable.getSWTWidgetControl(), operationSystemProperties, errorDecorator,propertyDialogButtonBar,true));
		
		DragDropUtility.INSTANCE.applyDragFromTableViewer(opSystemPropertiesTabViewer.getTable()); 	
		
		Composite rightContainerComposite = new Composite(container, SWT.NONE);
		fd_middleContainerComposite.right = new FormAttachment(100, -149);
		FormData fd_rightContainerComposite = new FormData();
		fd_rightContainerComposite.top = new FormAttachment(leftContainerComposite, 0, SWT.TOP);
		fd_rightContainerComposite.left = new FormAttachment(middleContainerComposite);
		fd_rightContainerComposite.bottom = new FormAttachment(100, -64);
		fd_rightContainerComposite.right = new FormAttachment(100);
		rightContainerComposite.setLayoutData(fd_rightContainerComposite);

		
		Composite outputComposite = new Composite(container, SWT.NONE);
		FormData fd_compositeOutput = new FormData();
		fd_compositeOutput.right = new FormAttachment(100);
		fd_compositeOutput.top = new FormAttachment(middleContainerComposite, 0, SWT.TOP);
		fd_compositeOutput.bottom = new FormAttachment(outputComposite, 0, SWT.BOTTOM);
		outputComposite.setLayoutData(fd_compositeOutput);
		
		ELTDefaultSubgroupComposite outputDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(outputComposite);
		outputDefaultSubgroupComposite.createContainerWidget();
		
		AbstractELTWidget deleteButtonOutput = getButton(""); 
		outputDefaultSubgroupComposite.attachWidget(deleteButtonOutput); 

		Button deleteButtonOut = (Button) deleteButtonOutput.getSWTWidgetControl();
		deleteButtonOut.setParent(outputComposite);
		deleteButtonOut.setBounds(0, 5, 18, 18);
		deleteButtonOut.setImage(SWTResourceManager.getImage(DELETE_ICON));	
		
		outerOpTabViewer = createTableViewer(rightContainerComposite, new String[]{OPERATIONAL_OUTPUT_FIELD},new TransformGridContentProvider(),new OperationLabelProvider());
		outerOpTabViewer.setCellModifier(new OperationGridCellModifier(outerOpTabViewer));
		outerOpTabViewer.setInput(opOutputOuterFields); 
		outerOpTabViewer.getTable().setBounds(10, 10, 150, 445);
		rightContainerComposite.setLocation(737, -315);
		for (int columnIndex = 0, n = outerOpTabViewer.getTable().getColumnCount(); columnIndex < n; columnIndex++) {
			outerOpTabViewer.getTable().getColumn(columnIndex).setWidth(145);
		}    
		DragDropUtility.INSTANCE.applyDrop(outerOpTabViewer,new DragDropTransformOpImp(opOutputOuterFields, true,outerOpTabViewer) );
		
		Composite topAddButtonComposite = new Composite(middleContainerComposite, SWT.NONE);
		topAddButtonComposite.setBounds(172, 0, 730, 35);
		
		Button btnAddOperation = new Button(topAddButtonComposite, SWT.NONE);
		
		btnAddOperation.setImage(SWTResourceManager.getImage(ADD_ICON));
		btnAddOperation.setBounds(694, 10, 36, 25);
			
		final ScrolledComposite expandBarScrolledComposite = new ScrolledComposite(middleContainerComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		expandBarScrolledComposite.setVisible(true);
		expandBarScrolledComposite.setBounds(10, 41, 902, 278);
		expandBarScrolledComposite.setExpandHorizontal(true);
		expandBarScrolledComposite.setExpandVertical(true); 
		expandBarScrolledComposite.setAlwaysShowScrollBars(true);
		expandBar = new ExpandBar(expandBarScrolledComposite, SWT.NONE);
		expandBar.setBounds(10, 41, 902, 278); 
		expandBar.setVisible(true); 
		Composite addDeleteButtonComposite = new Composite(middleContainerComposite, SWT.NONE);
		addDeleteButtonComposite.setBounds(10, 351, 720, 19);
		
		Composite nameValueComposite = new Composite(middleContainerComposite, SWT.NONE);
		
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite2 = new ELTDefaultSubgroupComposite(addDeleteButtonComposite);
		eltSuDefaultSubgroupComposite2.createContainerWidget();

		AbstractELTWidget addButton = getButton("");
		AbstractELTWidget deleteButton = getButton(""); 
		eltSuDefaultSubgroupComposite2.attachWidget(addButton);
		eltSuDefaultSubgroupComposite2.attachWidget(deleteButton);
		Button addOutKeyValueButton=(Button) addButton.getSWTWidgetControl();
		addOutKeyValueButton.setParent(addDeleteButtonComposite);
		addOutKeyValueButton.setBounds(680, 0, 18, 18);
		addOutKeyValueButton.setImage(SWTResourceManager.getImage(ADD_ICON));
		
		Button deleteOutKeyValueButton = (Button) deleteButton.getSWTWidgetControl();
		deleteOutKeyValueButton.setParent(addDeleteButtonComposite);
		deleteOutKeyValueButton.setBounds(700, 0, 18, 18);
		deleteOutKeyValueButton.setImage(SWTResourceManager.getImage(DELETE_ICON));	
		
		
		AbstractELTWidget mapFieldLableWidget = new ELTDefaultLable("Map Fields").lableWidth(250);
		eltSuDefaultSubgroupComposite2.attachWidget(mapFieldLableWidget);
		Label mapFieldLable=(Label) mapFieldLableWidget.getSWTWidgetControl();
		mapFieldLable.setParent(addDeleteButtonComposite);
		mapFieldLable.setBounds(300, 0, 100, 18);

		addOutKeyValueButton.setParent(addDeleteButtonComposite);

		nameValueComposite.setBounds(10, 372, 720, 163);  
		
		outerKeyValueTabViewer = createTableViewer(nameValueComposite, NAME_VALUE_COLUMN, new TransformGridContentProvider(),new PropertyLabelProvider());
		outerKeyValueTabViewer.setCellModifier(new PropertyGridCellModifier(outerKeyValueTabViewer));
		outerKeyValueTabViewer.setInput(opOuterClassProperty); 
		outerKeyValueTabViewer.getTable().setBounds(10, 10, 700, 143);
		
		
		for (int columnIndex = 0, n = outerKeyValueTabViewer.getTable().getColumnCount(); columnIndex < n; columnIndex++) {
			outerKeyValueTabViewer.getTable().getColumn(columnIndex).setWidth(347);
		}
		DragDropUtility.INSTANCE.applyDragFromTableViewerOuter(outerKeyValueTabViewer); 	
		DragDropUtility.INSTANCE.applyDrop(outerKeyValueTabViewer,new DragDropTransformOpImp(opOuterClassProperty, false,outerKeyValueTabViewer));
		ELTDefaultSubgroupComposite defaultnameValueComposite = new ELTDefaultSubgroupComposite(nameValueComposite);	
		defaultnameValueComposite.createContainerWidget();
  
		ELTTable eltPropOuterTable = new ELTTable(outerKeyValueTabViewer);
		defaultnameValueComposite.attachWidget(eltPropOuterTable);
	
		ControlDecoration errorPropValDecorator = setDecorator("Property name should not be same or blank");
		editors[0].setValidator(new ELTCellEditorTransformValidator((Table)eltPropOuterTable.getSWTWidgetControl(), opOuterClassProperty, errorPropValDecorator,propertyDialogButtonBar,false));

		
		  if(transformPropertyGrid!=null)  
		  {
			  if(transformPropertyGrid.getOperation()!=null) {
				  for (TransformOperation transformOperation : transformPropertyGrid.getOperation()) {   
					  ELTFixedWidget eltFixedWidget = new ELTFixedWidget(propertyDialogButtonBar);
					  addExpandItem(container, expandBarScrolledComposite,transformOperation,eltFixedWidget); 
				}
				  
			  }
			  else{ 
					ELTFixedWidget eltFixedWidget = new ELTFixedWidget(propertyDialogButtonBar);
					addExpandItem(container, expandBarScrolledComposite,new TransformOperation(),eltFixedWidget);   
			  }
		  }
		 
		btnAddOperation.addSelectionListener(new SelectionAdapter() {
			@Override 
			public void widgetSelected(SelectionEvent e) {
				for(ExpandItem expandItem : expandBar.getItems()){
					expandItem.setExpanded(false);
				}
				ELTFixedWidget eltFixedWidget = new ELTFixedWidget(propertyDialogButtonBar);
				TransformOperation transformOperation = new TransformOperation();
				transformOperation.setOperationId(++operationId);
				addExpandItem(container, expandBarScrolledComposite,transformOperation,eltFixedWidget);
			}
		}); 
		
		ListenerHelper helperPropertyValue=getListenerHelper(opOuterClassProperty, outerKeyValueTabViewer, fieldError,eltTransforAddPropValueListener);
		ListenerHelper helperInputOuter=getListenerHelper(operationSystemProperties, opSystemPropertiesTabViewer, fieldError,opSysSelectionListener);
		ListenerHelper helperOutputOuter=getListenerHelper(opOutputOuterFields, outerOpTabViewer, fieldError,opSysSelectionListener);

 
		/* 
		 * Listener attached for property name value Outer main grid
		 */
			try {
				addButton.attachListener(ListenerFactory.Listners.GRID_ADD_SELECTION.getListener(),propertyDialogButtonBar, helperPropertyValue, outerKeyValueTabViewer.getTable());
				deleteButton.attachListener(ListenerFactory.Listners.TRANSFORM_DELETE_SELECTION.getListener(),propertyDialogButtonBar, helperPropertyValue, outerKeyValueTabViewer.getTable());
				addButtonInput.attachListener(ListenerFactory.Listners.GRID_ADD_SELECTION.getListener(),propertyDialogButtonBar, helperInputOuter, opSystemPropertiesTabViewer.getTable());
				deleteButtonInput.attachListener(ListenerFactory.Listners.TRANSFORM_DELETE_SELECTION.getListener(),propertyDialogButtonBar, helperInputOuter, opSystemPropertiesTabViewer.getTable());
				deleteButtonOutput.attachListener(ListenerFactory.Listners.TRANSFORM_DELETE_SELECTION.getListener(),propertyDialogButtonBar, helperOutputOuter, outerOpTabViewer.getTable());
				eltPropOuterTable.attachListener(ListenerFactory.Listners.GRID_MOUSE_DOUBLE_CLICK.getListener(),	propertyDialogButtonBar, helperPropertyValue, outerKeyValueTabViewer.getTable());				
				eltOpSysOuterTable.attachListener(ListenerFactory.Listners.GRID_MOUSE_DOUBLE_CLICK.getListener(),	propertyDialogButtonBar, helperInputOuter, opSystemPropertiesTabViewer.getTable()); 
				
		
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			populateWidget();
		
		return container;
	}

	private void addExpandItem(Composite shell, ScrolledComposite expandBarScrolledComposite,TransformOperation transformOperation,ELTFixedWidget eltFixedWidget ) {
		Composite middleContainerComposite = new Composite(shell, SWT.NONE);
		FormData fd_middleContainerComposite = new FormData();
		fd_middleContainerComposite.top = new FormAttachment(0, 10);
		fd_middleContainerComposite.bottom = new FormAttachment(100, -10);
		middleContainerComposite.setLayoutData(fd_middleContainerComposite);
		Composite leftContainerComposite = new Composite(shell, SWT.NONE);
		fd_middleContainerComposite.left = new FormAttachment(leftContainerComposite, 6);
		FormData fd_leftContainerComposite = new FormData();
		fd_leftContainerComposite.top = new FormAttachment(0, 10);
		fd_leftContainerComposite.bottom = new FormAttachment(100, -10);
		fd_leftContainerComposite.right = new FormAttachment(0, 1400);
		fd_leftContainerComposite.left = new FormAttachment(0, 20);
		leftContainerComposite.setLayoutData(fd_leftContainerComposite);

		Composite rightContainerComposite = new Composite(shell, SWT.NONE);
		fd_middleContainerComposite.right = new FormAttachment(rightContainerComposite, -6);
		FormData fd_rightContainerComposite = new FormData();
		fd_rightContainerComposite.top = new FormAttachment(0, 10);
		fd_rightContainerComposite.bottom = new FormAttachment(100, -10);
		fd_rightContainerComposite.right = new FormAttachment(100, -10);
		fd_rightContainerComposite.left = new FormAttachment(0, 914);
		rightContainerComposite.setLayoutData(fd_rightContainerComposite);
		
		ExpandItem expandItem = new ExpandItem(expandBar, SWT.V_SCROLL | SWT.H_SCROLL);
		expandItem.setExpanded(true);
		expandItem.setText("Operation :"+transformOperation.getOperationId()); 
		expandItem.setHeight(350); 
		Composite expandItemContainerComposite = new Composite(expandBar, SWT.NONE);
		expandItemContainerComposite.setVisible(true);
		expandItem.setControl(expandItemContainerComposite);
		expandItemContainerComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Composite OpInputFieldComposite = new Composite(expandItemContainerComposite,SWT.NONE);
		OpInputFieldComposite.setLayoutData(new RowData(170, 500));
		OpInputFieldComposite.setSize(new Point(200, 0));
		OpInputFieldComposite.setVisible(true);

		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite2 = new ELTDefaultSubgroupComposite(OpInputFieldComposite);
		eltSuDefaultSubgroupComposite2.createContainerWidget();
		AbstractELTWidget addButton = getButton("");
		AbstractELTWidget deleteButton = getButton(""); 
		eltSuDefaultSubgroupComposite2.attachWidget(addButton);
		eltSuDefaultSubgroupComposite2.attachWidget(deleteButton);
		Button btnNewButton_4=(Button) addButton.getSWTWidgetControl();
		btnNewButton_4.setParent(OpInputFieldComposite);
		btnNewButton_4.setBounds(120, 0, 18, 18);
		btnNewButton_4.setImage(SWTResourceManager.getImage(ADD_ICON));
		
		Button btnNewButton_5 = (Button) deleteButton.getSWTWidgetControl();
		btnNewButton_5.setParent(OpInputFieldComposite);
		btnNewButton_5.setBounds(145, 0, 18, 18);
		btnNewButton_5.setImage(SWTResourceManager.getImage(DELETE_ICON));	
		
		innerOpInputTabViewer = createTableViewer(OpInputFieldComposite, new String[]{OPERATIONAL_INPUT_FIELD},new TransformGridContentProvider(),new OperationLabelProvider());
		innerOpInputTabViewer.setCellModifier(new OperationGridCellModifier(innerOpInputTabViewer));
		innerOpInputTabViewer.setInput(transformOperation.getInputFields());
			
		DragDropUtility.INSTANCE.applyDrop(innerOpInputTabViewer,new DragDropTransformOpImp(transformOperation.getInputFields(), true,innerOpInputTabViewer));
		ELTTable eltOpInTable = new ELTTable(innerOpInputTabViewer);
		 
		eltSuDefaultSubgroupComposite2.attachWidget(eltOpInTable);
		innerOpInputTabViewer.getTable().setBounds(1, 20, 160, 200);
		for (int columnIndex = 0, n = innerOpInputTabViewer.getTable().getColumnCount(); columnIndex < n; columnIndex++) {
			innerOpInputTabViewer.getTable().getColumn(columnIndex).setWidth(156);
		}

		
		 ControlDecoration errorDecorator = setDecorator("Operation input field should not be same or blank");
		 editors[0].setValidator(new ELTCellEditorTransformValidator((Table)eltOpInTable.getSWTWidgetControl(), transformOperation.getInputFields(), errorDecorator,propertyDialogButtonBar,true));

		
		Composite composite_4 = new Composite(expandItemContainerComposite, SWT.NONE);
		composite_4.setLayoutData(new RowData(800, 500));
		composite_4.setVisible(true);
		composite_4.setLayout(new RowLayout(SWT.VERTICAL));
		
		Composite fileSelectionComposite = new Composite(composite_4, SWT.NONE);
		fileSelectionComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		fileSelectionComposite.setLayoutData(new RowData(340, 80));
		
		AbstractELTWidget fileNameText = new ELTDefaultTextBox().grabExcessHorizontalSpace(true).textBoxWidth(150);
		AbstractELTWidget isParameterCheckbox = new ELTDefaultCheckBox("IsParam").checkBoxLableWidth(100);

		FilterOperationClassUtility.createOperationalClass(fileSelectionComposite, propertyDialogButtonBar, fileNameText, isParameterCheckbox, validationStatus, tootlTipErrorMessage, widgetConfig);

		Text fileName=(Text)fileNameText.getSWTWidgetControl(); 
		Button btnCheckButton=(Button) isParameterCheckbox.getSWTWidgetControl(); 
		if(!transformOperation.getOpClassProperty().getOperationClassPath().equalsIgnoreCase(""))
		{
				fileName.setText(transformOperation.getOpClassProperty().getOperationClassPath());  
				btnCheckButton.setSelection(transformOperation.getOpClassProperty().isParameter()); 
				fileName.setData("path", transformOperation.getOpClassProperty().getOperationClassFullPath());
		} 		

		Composite nameValueInnerComposite = new Composite(composite_4, SWT.NONE);
		innerKeyValueTabViewer = createTableViewer(nameValueInnerComposite, NAME_VALUE_COLUMN_INNER, new TransformGridContentProvider(),new PropertyLabelProvider());
		innerKeyValueTabViewer.setCellModifier(new PropertyGridInnerCellModifier(innerKeyValueTabViewer)); 
		innerKeyValueTabViewer.setInput(transformOperation.getNameValueProps());  
		innerKeyValueTabViewer.getTable().setBounds(12, 25, 300, 200); 
		for (int columnIndex = 0, n = innerKeyValueTabViewer.getTable().getColumnCount(); columnIndex < n; columnIndex++) {
			innerKeyValueTabViewer.getTable().getColumn(columnIndex).setWidth(148);
		}
		
		ELTDefaultSubgroupComposite defaultnameValueInnerComposite = new ELTDefaultSubgroupComposite(nameValueInnerComposite);
		defaultnameValueInnerComposite.createContainerWidget(); 
		
		ELTTable eltPropValueTable = new ELTTable(innerKeyValueTabViewer);
		defaultnameValueInnerComposite.attachWidget(eltPropValueTable);
		
		ControlDecoration errorPropValDecorator = setDecorator("Property name should not be same or blank");
		editors[0].setValidator(new ELTCellEditorTransformValidator((Table)eltPropValueTable.getSWTWidgetControl(), transformOperation.getNameValueProps(), errorPropValDecorator,propertyDialogButtonBar,false));

		
		
		AbstractELTWidget addInnerPropValueButton = getButton("");
		AbstractELTWidget deleteInnerPropValueButton = getButton("");
		
		defaultnameValueInnerComposite.attachWidget(addInnerPropValueButton);
		defaultnameValueInnerComposite.attachWidget(deleteInnerPropValueButton);
		Button btnInnerPropValueAddButton=(Button) addInnerPropValueButton.getSWTWidgetControl();
		btnInnerPropValueAddButton.setParent(nameValueInnerComposite);
		btnInnerPropValueAddButton.setBounds(250,0, 18, 18);
		btnInnerPropValueAddButton.setImage(SWTResourceManager.getImage(ADD_ICON));
		
		Button btnInnerPropValueDeleteButton=(Button) deleteInnerPropValueButton.getSWTWidgetControl();
		btnInnerPropValueDeleteButton.setParent(nameValueInnerComposite);
		btnInnerPropValueDeleteButton.setBounds(270, 0, 18, 18);
		btnInnerPropValueDeleteButton.setImage(SWTResourceManager.getImage(DELETE_ICON));
		
		AbstractELTWidget mapFieldLableWidget = new ELTDefaultLable("Properties").lableWidth(250);
		eltSuDefaultSubgroupComposite2.attachWidget(mapFieldLableWidget);
		Label mapFieldLable=(Label) mapFieldLableWidget.getSWTWidgetControl();
		mapFieldLable.setParent(nameValueInnerComposite);
		mapFieldLable.setBounds(100, 0, 60, 18); 


		Composite opOutputFieldComposite1 = new Composite(composite_4, SWT.NONE);
		opOutputFieldComposite1.setLayout(new RowLayout(SWT.HORIZONTAL));
		opOutputFieldComposite1.setLayoutData(new RowData(30, 10)); 
		
		Label opOutputFieldLable = new Label(opOutputFieldComposite1, SWT.NONE);
		opOutputFieldLable.setText(OPERATIONAL_OUTPUT_INNER_FIELD);
		opOutputFieldLable.setBounds(0, 0, 30, 18);   

		
		Composite opOutputFieldComposite = new Composite(composite_4, SWT.NONE);
		opOutputFieldComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		opOutputFieldComposite.setLayoutData(new RowData(600, 400)); 
		
		
		
		eltFixedWidget.setSchemaGridRowList(transformOperation.getSchemaGridRowList());
		TableViewer innerOpOutputTabViewer=	eltFixedWidget.createSchemaGrid(opOutputFieldComposite);
		DragDropUtility.INSTANCE.applyDragFromTableViewer(innerOpOutputTabViewer.getTable());  
		expandBarScrolledComposite.setContent(expandBar);
		expandBarScrolledComposite.setMinSize(expandBar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		ListenerHelper helperOpIn=getListenerHelper(transformOperation.getInputFields(), innerOpInputTabViewer, fieldError,eltTransforAddSelectionListener);  
		ListenerHelper helperPropertyValue=getListenerHelper(transformOperation.getNameValueProps(), innerKeyValueTabViewer, fieldError,eltTransforAddPropValueListener);
		try {
			
			/*
			 * Listener attached for operational input and operation output grid
			 */
			addButton.attachListener(ListenerFactory.Listners.GRID_ADD_SELECTION.getListener(),propertyDialogButtonBar, helperOpIn, innerOpInputTabViewer.getTable());
			deleteButton.attachListener(ListenerFactory.Listners.TRANSFORM_DELETE_SELECTION.getListener(),propertyDialogButtonBar, helperOpIn, innerOpInputTabViewer.getTable());
			eltOpInTable.attachListener(ListenerFactory.Listners.GRID_MOUSE_DOUBLE_CLICK.getListener(),	propertyDialogButtonBar, helperOpIn, innerOpInputTabViewer.getTable());

			/*
			 * Listener attached for property name value inner grid
			 */
			addInnerPropValueButton.attachListener(ListenerFactory.Listners.GRID_ADD_SELECTION.getListener(),propertyDialogButtonBar, helperPropertyValue, innerKeyValueTabViewer.getTable());
			deleteInnerPropValueButton.attachListener(ListenerFactory.Listners.TRANSFORM_DELETE_SELECTION.getListener(),propertyDialogButtonBar, helperPropertyValue, innerKeyValueTabViewer.getTable());
			eltPropValueTable.attachListener(ListenerFactory.Listners.GRID_MOUSE_DOUBLE_CLICK.getListener(),	propertyDialogButtonBar, helperPropertyValue, innerKeyValueTabViewer.getTable());

			opClassMap.put(fileName, btnCheckButton);
			
			transformOperation.setSchemaGridRowList((List)innerOpOutputTabViewer.getInput()); 
			transformOperationList.add(transformOperation); 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
 
	public TableViewer createTableViewer(Composite composite,String[] prop,IStructuredContentProvider iStructuredContentProvider,ITableLabelProvider iTableLabelProvider){
			TableViewer tableViewer= new TableViewer(composite,SWT.BORDER|SWT.FULL_SELECTION);
		    tableViewer.setContentProvider(iStructuredContentProvider);
		    tableViewer.setLabelProvider( iTableLabelProvider);
			tableViewer.setColumnProperties(prop); 
		
			Table table = tableViewer.getTable();
			table.setVisible(true);
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			createTableColumns(table,prop);
			editors =createCellEditorList(table,prop.length); 
			tableViewer.setCellEditors(editors);
			return tableViewer;
	}
	
	 public static void createTableColumns(Table table,String[] fields){
			for (String field : fields) {
				TableColumn tableColumn= new TableColumn(table, SWT.CENTER);
				tableColumn.setText(field);
				tableColumn.pack();
			}
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			
		}
		public static CellEditor[] createCellEditorList(Table table,int size){
			CellEditor[] cellEditor = new CellEditor[size];
			for(int i=0;i<size;i++)
			addTextEditor(table,cellEditor, i);

			return cellEditor;
		}
		protected static void addTextEditor(Table table, CellEditor[] cellEditor, int position){
			cellEditor[position]=new TextCellEditor(table, SWT.COLOR_GREEN);
		}

	
		private AbstractELTWidget getButton(String displayName) {
			// Create browse button.
			AbstractELTWidget button = new ELTDefaultButton(displayName)
					.buttonWidth(20);
			return button;
		}

		
		private ListenerHelper getListenerHelper(List<? extends PropertyField> opList,TableViewer tableViewer,AbstractELTWidget fieldError,GridWidgetCommonBuilder gridWidgetBuilder) {
				ListenerHelper helper = new ListenerHelper();
				ELTGridDetails value = new ELTGridDetails(opList, tableViewer, 
						(Label) fieldError.getSWTWidgetControl(), gridWidgetBuilder);
				helper.put(HelperType.SCHEMA_GRID, value);
				return helper;
		}		
	 
		
		   public void populateWidget() {

			   if(transformPropertyGrid!=null){ 
				   if(transformPropertyGrid.getOperation()!=null){
					   innerOpInputTabViewer.refresh(); 
					   innerKeyValueTabViewer.refresh(); 
					   outerKeyValueTabViewer.refresh();
					   outerOpTabViewer.refresh();
					   opSystemPropertiesTabViewer.refresh(); 
 
				   }
			   }
		        
		  }
		   
			public void setValidationStatus(ValidationStatus validationStatus) {
				this.validationStatus = validationStatus;
			}

			public ValidationStatus getValidationStatus() {
				return validationStatus;
			}

			public TransformPropertyGrid getTransformProperty() {
				TransformPropertyGrid transformPropertyGrid = new TransformPropertyGrid();
				transformPropertyGrid.setNameValueProps(opOuterClassProperty); 
				transformPropertyGrid.setOperation(transformOperationList); 
				transformPropertyGrid.setOpSysProperties(operationSystemProperties); 
				transformPropertyGrid.setOutputTreeFields(opOutputOuterFields);
				this.transformPropertyGrid=transformPropertyGrid;
				return transformPropertyGrid;
			}
			
			/**
			 * Create contents of the button bar.
			 * @param parent
			 */
			@Override
			protected void createButtonsForButtonBar(Composite parent) {
				Button okButton=createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
						true);
				Button cancelButton=createButton(parent, IDialogConstants.CANCEL_ID,
						IDialogConstants.CANCEL_LABEL, false);
				
				createApplyButton(parent);	
				
				
				propertyDialogButtonBar.setPropertyDialogButtonBar(okButton, applyButton, cancelButton);
			}
			
			private void createApplyButton(Composite parent) {
				applyButton = createButton(parent, IDialogConstants.NO_ID,
						"Apply", false);
				disableApplyButton();
			}
			
			private void disableApplyButton() {
				applyButton.setEnabled(false);
			}
			
			/**
			 * Return the initial size of the dialog.
			 */
			@Override
			protected Point getInitialSize() {
				return new Point(1253, 666);
			}

			private void setPropertyDialogSize() {
				container.getShell().setMinimumSize(450, 185);
			}

			@Override
			protected void cancelPressed() {
				if(applyButton.isEnabled()){
					ConfirmCancelMessageBox confirmCancelMessageBox = new ConfirmCancelMessageBox(container);
					MessageBox confirmCancleMessagebox = confirmCancelMessageBox.getMessageBox();

					if(confirmCancleMessagebox.open() == SWT.OK){
						super.close();
					}
				}else{
					super.close();
				}
				
			}

			@Override
			protected void okPressed() {
				updateOperationClassProperty();
				super.okPressed();
			}

			@Override
			protected void buttonPressed(int buttonId) {
				if(buttonId == 3){
					applyButton.setEnabled(false);
				}else{
					super.buttonPressed(buttonId);
				}
			}

			protected  ControlDecoration setDecorator(String errorMessage) {
				ControlDecoration errorDecorator = WidgetUtility.addDecorator(editors[0].getControl(),errorMessage);
				return errorDecorator;
			}

			
			public void updateOperationClassProperty(){
				int i=0;
				List<OperationClassProperty> operationClassProperties= new ArrayList<>();
				for (Map.Entry<Text, Button> entry : opClassMap.entrySet())
				{
				    OperationClassProperty operationClassProperty = new OperationClassProperty(entry.getKey().getText(), entry.getValue().getSelection(),(String)entry.getKey().getData("path"));
				    operationClassProperties.add(operationClassProperty);
				}
				for (TransformOperation transformOperation : transformOperationList) 
				{
					transformOperation.setOpClassProperty(operationClassProperties.get(i));
					i++;
				}

			}
			
		    public void updateOperationSystemProperties(Object element, boolean flag){
		    	OperationSystemProperties opSystemProperties= (OperationSystemProperties)element;
		    	if(operationSystemProperties.contains(opSystemProperties))
		    	{
		    		operationSystemProperties.remove(opSystemProperties);
		    		opSystemProperties.setChecked(flag); 
		    		operationSystemProperties.add(opSystemProperties); 
		    	}
		    	
		    }

			public String getTootlTipErrorMessage() {
				return tootlTipErrorMessage.getErrorMessage();
			}
}

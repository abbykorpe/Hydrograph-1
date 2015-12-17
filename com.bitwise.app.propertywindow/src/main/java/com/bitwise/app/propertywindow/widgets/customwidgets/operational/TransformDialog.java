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
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
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
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;
import org.slf4j.Logger;

import com.bitwise.app.common.datastructure.property.NameValueProperty;
import com.bitwise.app.common.datastructure.property.OperationClassProperty;
import com.bitwise.app.common.datastructure.property.OperationField;
import com.bitwise.app.common.datastructure.property.OperationSystemProperties;
import com.bitwise.app.common.datastructure.property.PropertyField;
import com.bitwise.app.common.datastructure.property.TransformOperation;
import com.bitwise.app.common.datastructure.property.TransformPropertyGrid;
import com.bitwise.app.common.datastructures.tooltip.TootlTipErrorMessage;
import com.bitwise.app.common.util.LogFactory;
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
 * The Class TransformDialog.
 */
public class TransformDialog extends Dialog {
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(TransformDialog.class);

	/** The Constant PROPERTY_NAME. */
	public static final String PROPERTY_NAME = "Source";
	
	/** The Constant PROPERTY_VALUE. */
	public static final String PROPERTY_VALUE = "Target";
	
	/** The Constant PROPERTY_NAME_INNER. */
	public static final String PROPERTY_NAME_INNER = "Property Name";
	
	/** The Constant PROPERTY_VALUE_INNER. */
	public static final String PROPERTY_VALUE_INNER = "Property Value";

	/** The Constant OPERATIONAL_INPUT_FIELD. */
	public static final String OPERATIONAL_INPUT_FIELD = "Operation Input Fields";
	
	/** The Constant OPERATIONAL_OUTPUT_INNER_FIELD. */
	public static final String OPERATIONAL_OUTPUT_INNER_FIELD = "Operation Output Fields";
	
	/** The Constant OPERATIONAL_OUTPUT_FIELD. */
	public static final String OPERATIONAL_OUTPUT_FIELD = "Output Fields";
	
	/** The Constant OPERATIONAL_SYSTEM_FIELD. */
	public static final String OPERATIONAL_SYSTEM_FIELD = "Input Fields";
	
	/** The Constant ADD_ICON. */
	private static final String ADD_ICON = XMLConfigUtil.CONFIG_FILES_PATH + "/icons/add.png";
	
	/** The Constant DELETE_ICON. */
	private static final String DELETE_ICON = XMLConfigUtil.CONFIG_FILES_PATH + "/icons/delete.png";
	
	/** The operation id. */
	private long operationId=1;
	
	/** The container. */
	private Composite container;
	
	/** The editors. */
	private CellEditor[] editors; 
	
	/** The operational Class map. */
	Map<Text,Button> opClassMap = new LinkedHashMap<Text, Button>();

	/** The Constant NAME_VALUE_COLUMN. */
	private static final String[] NAME_VALUE_COLUMN = {PROPERTY_NAME, PROPERTY_VALUE};
	
	/** The Constant NAME_VALUE_COLUMN_INNER. */
	private static final String[] NAME_VALUE_COLUMN_INNER = {PROPERTY_NAME_INNER, PROPERTY_VALUE_INNER};
	
	/** The elt transfor add selection listener. */
	private ELTTransforAddSelectionListener eltTransforAddSelectionListener = new ELTTransforAddSelectionListener();
	
	/** The op sys selection listener. */
	private ELTTransforAddOpSysSelectionListener opSysSelectionListener = new ELTTransforAddOpSysSelectionListener();
	
	/** The elt transfor add prop value listener. */
	private ELTTransforAddPropValueListener eltTransforAddPropValueListener = new ELTTransforAddPropValueListener();
  
    /** The op output outer fields. */
    private  List<OperationField> opOutputOuterFields = new ArrayList<OperationField>();
    
    /** The op outer class property. */
    private  List<NameValueProperty> opOuterClassProperty = new ArrayList<NameValueProperty>();  
    
    /** The operation system properties. */
    private  List<OperationSystemProperties> operationSystemProperties = new ArrayList<OperationSystemProperties>();
    
    /** The transform operation list. */
    private List<TransformOperation> transformOperationList = new ArrayList<>();
	
    /** The inner op input tab viewer. */
    private TableViewer innerOpInputTabViewer; 
	
	/** The inner key value tab viewer. */
	private TableViewer innerKeyValueTabViewer;
	
	/** The outer key value tab viewer. */
	private TableViewer outerKeyValueTabViewer; 
	
	/** The outer op tab viewer. */
	private TableViewer	outerOpTabViewer;
	
	/** The op system properties tab viewer. */
	private CheckboxTableViewer opSystemPropertiesTabViewer;
	
	/** The apply button. */
	private Button applyButton;
	
	/** The expand bar. */
	private ExpandBar expandBar = null;
	
	/** The property dialog button bar. */
	private PropertyDialogButtonBar propertyDialogButtonBar;
	
	/** The validation status. */
	private ValidationStatus validationStatus;
	
	/** The transform property grid. */
	private TransformPropertyGrid transformPropertyGrid;
	
	/** The widget config. */
	private WidgetConfig widgetConfig;

	/** The field error. */
	// Operational class label.
	AbstractELTWidget fieldError = new ELTDefaultLable(Messages.FIELDNAMEERROR).lableWidth(250);
	
	/** The tootl tip error message. */
	private TootlTipErrorMessage tootlTipErrorMessage = new TootlTipErrorMessage();

	/**
	 * Create the dialog.
	 *
	 * @param parentShell the parent shell
	 * @param propertyDialogButtonBar the property dialog button bar
	 * @param transformPropertyGrid the transform property grid
	 * @param widgetConfig the widget config
	 */
	public TransformDialog(Shell parentShell,PropertyDialogButtonBar propertyDialogButtonBar,TransformPropertyGrid transformPropertyGrid,WidgetConfig widgetConfig) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.RESIZE | SWT.TITLE |  SWT.WRAP | SWT.APPLICATION_MODAL);
		this.transformPropertyGrid = transformPropertyGrid;
		this.widgetConfig=widgetConfig;
	}	
	
	
	/**
	 * Create contents of the dialog.
	 *
	 * @param parent the parent
	 * @return the control
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());
		
		
		propertyDialogButtonBar = new PropertyDialogButtonBar(container);
	
		opOutputOuterFields=transformPropertyGrid.getOutputTreeFields()!=null ?transformPropertyGrid.getOutputTreeFields():opOutputOuterFields;
		opOuterClassProperty=transformPropertyGrid.getNameValueProps()!=null ? transformPropertyGrid.getNameValueProps():opOuterClassProperty;
		operationSystemProperties=transformPropertyGrid.getOpSysProperties()!=null ? transformPropertyGrid.getOpSysProperties():operationSystemProperties; 

		Composite leftButtonComposite = new Composite(container, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.left = new FormAttachment(0, 10);
		fd_composite.top = new FormAttachment(0, 10);
		leftButtonComposite.setLayoutData(fd_composite);
		
		ELTDefaultSubgroupComposite inputDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(leftButtonComposite);
		inputDefaultSubgroupComposite.createContainerWidget();
		
		AbstractELTWidget addButtonInput = getButton("");
		AbstractELTWidget deleteButtonInput = getButton(""); 
		inputDefaultSubgroupComposite.attachWidget(addButtonInput);
		inputDefaultSubgroupComposite.attachWidget(deleteButtonInput);
		Button addOutKeyValueButtonIn=(Button) addButtonInput.getSWTWidgetControl();
		addOutKeyValueButtonIn.setParent(leftButtonComposite);
		addOutKeyValueButtonIn.setBounds(85, 15, 18, 18);
		addOutKeyValueButtonIn.setImage(SWTResourceManager.getImage(ADD_ICON));
		
		Button deleteOutKeyValueButtonIn = (Button) deleteButtonInput.getSWTWidgetControl();
		deleteOutKeyValueButtonIn.setParent(leftButtonComposite);
		deleteOutKeyValueButtonIn.setBounds(107, 15, 18, 18);
		deleteOutKeyValueButtonIn.setImage(SWTResourceManager.getImage(DELETE_ICON));
				
		Composite leftOpInputComposite = new Composite(container, SWT.NONE);
		fd_composite.bottom = new FormAttachment(leftOpInputComposite, -6);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(leftButtonComposite, -9, SWT.RIGHT);
		fd_composite_1.top = new FormAttachment(0, 50);
		fd_composite_1.bottom = new FormAttachment(100);
		fd_composite_1.left = new FormAttachment(0, 10);
		leftOpInputComposite.setLayoutData(fd_composite_1);
		
		opSystemPropertiesTabViewer = CheckboxTableViewer.newCheckList(
				leftOpInputComposite, SWT.BORDER);
	    opSystemPropertiesTabViewer.setContentProvider(new TransformGridContentProvider());
	    opSystemPropertiesTabViewer.setLabelProvider( new OperationSystemLabelProvider());
	    opSystemPropertiesTabViewer.setColumnProperties(new String[]{OPERATIONAL_SYSTEM_FIELD}); 
	    opSystemPropertiesTabViewer.setCellModifier(new OperationSystemCellModifier(opSystemPropertiesTabViewer));
	    
	    Table table = opSystemPropertiesTabViewer.getTable();
		table.setVisible(true);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(0, 0, 123, 557);
		
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
			table.getColumn(columnIndex).setWidth(117);
		}  

		CellEditor[] editors_opSys = null; 
		editors_opSys =createCellEditorList(table,new String[]{OPERATIONAL_SYSTEM_FIELD}.length); 
		opSystemPropertiesTabViewer.setCellEditors(editors_opSys);
	    
		opSystemPropertiesTabViewer.setInput(operationSystemProperties); 
		for (OperationSystemProperties opSystemProperty : operationSystemProperties) {
			opSystemPropertiesTabViewer.setChecked(opSystemProperty, opSystemProperty.isChecked());

		}

		ELTDefaultSubgroupComposite leftContainerSubgroupComposite = new ELTDefaultSubgroupComposite(leftOpInputComposite);
		leftContainerSubgroupComposite.createContainerWidget();

		ELTTable eltOpSysOuterTable = new ELTTable(opSystemPropertiesTabViewer);
		leftContainerSubgroupComposite.attachWidget(eltOpSysOuterTable);
		editors=editors_opSys;
		ControlDecoration errorDecorator = setDecorator("Property name should not be same or blank");
		editors[0].setValidator(new ELTCellEditorTransformValidator((Table)eltOpSysOuterTable.getSWTWidgetControl(), operationSystemProperties, errorDecorator,propertyDialogButtonBar,true));
		
		DragDropUtility.INSTANCE.applyDragFromTableViewer(opSystemPropertiesTabViewer.getTable()); 	
//----------------------------------------------		
		
		Composite outputButtonComposite = new Composite(container, SWT.NONE);
		FormData fdOutputButtoncomposite = new FormData();
		fdOutputButtoncomposite.top = new FormAttachment(0, 25);
		fdOutputButtoncomposite.right = new FormAttachment(100, -9);
		outputButtonComposite.setLayoutData(fdOutputButtoncomposite);
		FormData fd_composite_2 = new FormData();
		fd_composite_2.top = new FormAttachment(leftOpInputComposite, 0, SWT.TOP);
		fd_composite_2.bottom = new FormAttachment(leftOpInputComposite, 0, SWT.BOTTOM);
		
		ELTDefaultSubgroupComposite outputDefaultSubgroupComposite = new ELTDefaultSubgroupComposite(outputButtonComposite);
		outputDefaultSubgroupComposite.createContainerWidget();
		
		AbstractELTWidget deleteButtonOutput = getButton(""); 
		outputDefaultSubgroupComposite.attachWidget(deleteButtonOutput); 

		Button deleteButtonOut = (Button) deleteButtonOutput.getSWTWidgetControl();
		deleteButtonOut.setParent(outputButtonComposite);
		deleteButtonOut.setBounds(100, 5, 18, 18);
		deleteButtonOut.setImage(SWTResourceManager.getImage(DELETE_ICON));	
	
		Composite rightOpOutputComposite = new Composite(container, SWT.NONE);
		fdOutputButtoncomposite.bottom = new FormAttachment(rightOpOutputComposite, -1);
		fdOutputButtoncomposite.left = new FormAttachment(rightOpOutputComposite, 0, SWT.LEFT);
		rightOpOutputComposite.setLayout(new ColumnLayout());
		FormData fd_OutputComposite = new FormData();
		fd_OutputComposite.right = new FormAttachment(100);
		fd_OutputComposite.top = new FormAttachment(leftOpInputComposite, 0, SWT.TOP);
		fd_OutputComposite.bottom = new FormAttachment(100);
		rightOpOutputComposite.setLayoutData(fd_OutputComposite);

		outerOpTabViewer = createTableViewer(rightOpOutputComposite, new String[]{OPERATIONAL_OUTPUT_FIELD},new TransformGridContentProvider(),new OperationLabelProvider());
		outerOpTabViewer.setCellModifier(new OperationGridCellModifier(outerOpTabViewer));
		outerOpTabViewer.setInput(opOutputOuterFields); 
		outerOpTabViewer.getTable().setBounds(10, 10, 123, 557);
//		rightOpOutputComposite.setLocation(737, -315);
		for (int columnIndex = 0, n = outerOpTabViewer.getTable().getColumnCount(); columnIndex < n; columnIndex++) {
			outerOpTabViewer.getTable().getColumn(columnIndex).setWidth(117);
		}    
		DragDropUtility.INSTANCE.applyDrop(outerOpTabViewer,new DragDropTransformOpImp(opOutputOuterFields, true,outerOpTabViewer) );
		
		Composite topAddButtonComposite = new Composite(container, SWT.NONE);
		FormData fd_AddButton_composite = new FormData();
		fd_AddButton_composite.right = new FormAttachment(outputButtonComposite, -6);
		fd_AddButton_composite.left = new FormAttachment(0, 215);
		fd_AddButton_composite.top = new FormAttachment(leftButtonComposite, 0, SWT.TOP);
		fd_AddButton_composite.bottom = new FormAttachment(0, 34);
		topAddButtonComposite.setLayoutData(fd_AddButton_composite);
		
		Composite middleComposite = new Composite(container, SWT.NONE);
		fd_composite.right = new FormAttachment(middleComposite, -3);
		fd_OutputComposite.left = new FormAttachment(0, 1119);
		middleComposite.setLayout(new ColumnLayout());
		FormData fd_middleComposite = new FormData();
		fd_middleComposite.top = new FormAttachment(topAddButtonComposite, 6);
		fd_middleComposite.left = new FormAttachment(0, 145);
		fd_middleComposite.right = new FormAttachment(rightOpOutputComposite, -6);

		Composite outerNameValueComposite = new Composite(container, SWT.NONE);
		fd_middleComposite.bottom = new FormAttachment(outerNameValueComposite, -6);
		FormData fd_outerNameValueComposite = new FormData();
		fd_outerNameValueComposite.top = new FormAttachment(0, 441);
		fd_outerNameValueComposite.bottom = new FormAttachment(100);
		fd_outerNameValueComposite.right = new FormAttachment(100, -159);
		fd_outerNameValueComposite.left = new FormAttachment(leftOpInputComposite, 15);
		outerNameValueComposite.setLayoutData(fd_outerNameValueComposite);
		
		ELTDefaultSubgroupComposite eltSuDefaultSubgroupComposite2 = new ELTDefaultSubgroupComposite(outerNameValueComposite);
		eltSuDefaultSubgroupComposite2.createContainerWidget();

		AbstractELTWidget addButton = getButton("");
		AbstractELTWidget deleteButton = getButton(""); 
		eltSuDefaultSubgroupComposite2.attachWidget(addButton);
		eltSuDefaultSubgroupComposite2.attachWidget(deleteButton);
		Button addOutKeyValueButton=(Button) addButton.getSWTWidgetControl();
		addOutKeyValueButton.setParent(outerNameValueComposite);
		addOutKeyValueButton.setBounds(750,3, 18, 18);
		addOutKeyValueButton.setImage(SWTResourceManager.getImage(ADD_ICON));
		
		Button deleteOutKeyValueButton = (Button) deleteButton.getSWTWidgetControl();
		deleteOutKeyValueButton.setParent(outerNameValueComposite);
		deleteOutKeyValueButton.setBounds(770, 3, 18, 18);
		deleteOutKeyValueButton.setImage(SWTResourceManager.getImage(DELETE_ICON));	
		
		
		AbstractELTWidget mapFieldLableWidget = new ELTDefaultLable("Map Fields").lableWidth(250);
		eltSuDefaultSubgroupComposite2.attachWidget(mapFieldLableWidget);
		Label mapFieldLable=(Label) mapFieldLableWidget.getSWTWidgetControl();
		mapFieldLable.setParent(outerNameValueComposite);
		mapFieldLable.setBounds(460, 0, 100, 18);

		addOutKeyValueButton.setParent(outerNameValueComposite);
		
		outerKeyValueTabViewer = createTableViewer(outerNameValueComposite, NAME_VALUE_COLUMN, new TransformGridContentProvider(),new PropertyLabelProvider());
		outerKeyValueTabViewer.setCellModifier(new PropertyGridCellModifier(outerKeyValueTabViewer));
		outerKeyValueTabViewer.setInput(opOuterClassProperty); 
		outerKeyValueTabViewer.getTable().setBounds(200, 28, 600, 160);			
		

		for (int columnIndex = 0, n = outerKeyValueTabViewer.getTable().getColumnCount(); columnIndex < n; columnIndex++) {
			outerKeyValueTabViewer.getTable().getColumn(columnIndex).setWidth(295);
		}
		DragDropUtility.INSTANCE.applyDragFromTableViewerOuter(outerKeyValueTabViewer); 	
		DragDropUtility.INSTANCE.applyDrop(outerKeyValueTabViewer,new DragDropTransformOpImp(opOuterClassProperty, false,outerKeyValueTabViewer));
		ELTDefaultSubgroupComposite defaultnameValueComposite = new ELTDefaultSubgroupComposite(outerNameValueComposite);	
		defaultnameValueComposite.createContainerWidget();
  
		ELTTable eltPropOuterTable = new ELTTable(outerKeyValueTabViewer);
		defaultnameValueComposite.attachWidget(eltPropOuterTable);
	
		ControlDecoration errorPropValDecorator = setDecorator("Property name should not be same or blank");
		editors[0].setValidator(new ELTCellEditorTransformValidator((Table)eltPropOuterTable.getSWTWidgetControl(), opOuterClassProperty, errorPropValDecorator,propertyDialogButtonBar,false));

		
		
		Button btnAddOperation = new Button(topAddButtonComposite, SWT.NONE);	
		btnAddOperation.setImage(SWTResourceManager.getImage(ADD_ICON));
		btnAddOperation.setBounds(863, 0, 25, 25);
		middleComposite.setLayoutData(fd_middleComposite);
		
		final ScrolledComposite scrolledComposite = new ScrolledComposite(middleComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		ColumnLayoutData cld_scrolledComposite = new ColumnLayoutData();
		cld_scrolledComposite.widthHint = 525;
		cld_scrolledComposite.heightHint = 342;
		scrolledComposite.setLayoutData(cld_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		final Composite expandBarOuterComposite = new Composite(scrolledComposite, SWT.NONE);
		ColumnLayout columnLayout= new ColumnLayout();
		expandBarOuterComposite.setLayout(columnLayout);
		expandBar = new ExpandBar(expandBarOuterComposite, SWT.NONE);
	
		
		  if(transformPropertyGrid!=null)  
		  {
			  if(transformPropertyGrid.getOperation()!=null) {
				  for (TransformOperation transformOperation : transformPropertyGrid.getOperation()) {   
					  ELTFixedWidget eltFixedWidget = new ELTFixedWidget(propertyDialogButtonBar);
					  addExpandItem(expandBarOuterComposite,scrolledComposite,transformOperation, eltFixedWidget ) ;
				}
				  
			  }
			  else{ 
					ELTFixedWidget eltFixedWidget = new ELTFixedWidget(propertyDialogButtonBar);
					addExpandItem(expandBarOuterComposite,scrolledComposite, new TransformOperation(), eltFixedWidget ) ;
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
				addExpandItem(expandBarOuterComposite,scrolledComposite, transformOperation, eltFixedWidget ) ;
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
				logger.error("Error occured while attaching listeners.");
			}
			populateWidget();	
		
		return container;
	}
	
	
	/**
	 * Adds the expand item.
	 *
	 * @param expandBarOuterComposite the expand bar outer composite
	 * @param scrolledComposite the scrolled composite
	 * @param transformOperation the transform operation
	 * @param eltFixedWidget the elt fixed widget
	 */
	private void addExpandItem(Composite expandBarOuterComposite,ScrolledComposite scrolledComposite,TransformOperation transformOperation,ELTFixedWidget eltFixedWidget ) {
		
		Button btnCheckButton_1 = new Button(scrolledComposite, SWT.CHECK);
		btnCheckButton_1.setBounds(10, 10, 10, 16);
		btnCheckButton_1.setText("Check Button");

		ExpandItem expandItem = new ExpandItem(expandBar, SWT.CHECK);
		expandItem.setExpanded(true);
		expandItem.setText("Operation :"+transformOperation.getOperationId()); 
		ScrolledComposite expandItemScrolledComposite = new ScrolledComposite(expandBar, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		expandItem.setControl(expandItemScrolledComposite);
		expandItemScrolledComposite.setExpandHorizontal(true);
		expandItemScrolledComposite.setExpandVertical(true);
		expandItem.setHeight(300);
		
		Composite innerMainComposite = new Composite(expandItemScrolledComposite, SWT.NONE);
		Composite innerOpInputComposite = new Composite(innerMainComposite, SWT.NONE);
		innerOpInputComposite.setBounds(4, 10, 135, 350);

		ELTDefaultSubgroupComposite innerOpInputSubgroupComposite = new ELTDefaultSubgroupComposite(innerOpInputComposite);
		innerOpInputSubgroupComposite.createContainerWidget();
		AbstractELTWidget addButton = getButton("");
		AbstractELTWidget deleteButton = getButton(""); 
		innerOpInputSubgroupComposite.attachWidget(addButton);
		innerOpInputSubgroupComposite.attachWidget(deleteButton);
		Button innerOpAddButton=(Button) addButton.getSWTWidgetControl();
		innerOpAddButton.setParent(innerOpInputComposite);
		innerOpAddButton.setBounds(94, 5, 18, 18);
		innerOpAddButton.setImage(SWTResourceManager.getImage(ADD_ICON));
		
		Button innerOpDeleteButton = (Button) deleteButton.getSWTWidgetControl();
		innerOpDeleteButton.setParent(innerOpInputComposite);
		innerOpDeleteButton.setBounds(116, 5, 18, 18);
		innerOpDeleteButton.setImage(SWTResourceManager.getImage(DELETE_ICON));	
		
		innerOpInputTabViewer = createTableViewer(innerOpInputComposite, new String[]{OPERATIONAL_INPUT_FIELD},new TransformGridContentProvider(),new OperationLabelProvider());
		innerOpInputTabViewer.setCellModifier(new OperationGridCellModifier(innerOpInputTabViewer));
		innerOpInputTabViewer.setInput(transformOperation.getInputFields());
			
		DragDropUtility.INSTANCE.applyDrop(innerOpInputTabViewer,new DragDropTransformOpImp(transformOperation.getInputFields(), true,innerOpInputTabViewer));
		ELTTable eltOpInTable = new ELTTable(innerOpInputTabViewer);
		 
		innerOpInputSubgroupComposite.attachWidget(eltOpInTable);
		innerOpInputTabViewer.getTable().setBounds(0, 30, 135, 300);
		for (int columnIndex = 0, n = innerOpInputTabViewer.getTable().getColumnCount(); columnIndex < n; columnIndex++) {
			innerOpInputTabViewer.getTable().getColumn(columnIndex).setWidth(130);
		}

		
		 ControlDecoration errorDecorator = setDecorator("Operation input field should not be same or blank");
		 editors[0].setValidator(new ELTCellEditorTransformValidator((Table)eltOpInTable.getSWTWidgetControl(), transformOperation.getInputFields(), errorDecorator,propertyDialogButtonBar,true));

		Composite innerOperationComposite = new Composite(innerMainComposite, SWT.NONE);
		innerOperationComposite.setBounds(140, 10, 266, 54);
		innerOperationComposite.setLayout(new ColumnLayout());		
		expandItemScrolledComposite.setContent(innerMainComposite);
		expandItemScrolledComposite.setMinSize(innerMainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setContent(expandBarOuterComposite);
		scrolledComposite.setMinSize(expandBarOuterComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		AbstractELTWidget fileNameText = new ELTDefaultTextBox().grabExcessHorizontalSpace(true).textBoxWidth(150);
		AbstractELTWidget isParameterCheckbox = new ELTDefaultCheckBox("IsParam").checkBoxLableWidth(100);
		FilterOperationClassUtility.createOperationalClass(innerOperationComposite, propertyDialogButtonBar, fileNameText, isParameterCheckbox, validationStatus, tootlTipErrorMessage, widgetConfig);

		Text fileName=(Text)fileNameText.getSWTWidgetControl(); 
		Button btnCheckButton=(Button) isParameterCheckbox.getSWTWidgetControl(); 
		if(!transformOperation.getOpClassProperty().getOperationClassPath().equalsIgnoreCase(""))
		{
				fileName.setText(transformOperation.getOpClassProperty().getOperationClassPath());  
				btnCheckButton.setSelection(transformOperation.getOpClassProperty().isParameter()); 
				fileName.setData("path", transformOperation.getOpClassProperty().getOperationClassFullPath());
		} 		

		
		innerOperationComposite.setSize(innerOperationComposite.computeSize(340, 90));

		Composite innerNameValueComposite = new Composite(innerMainComposite, SWT.NONE);
		innerNameValueComposite.setBounds(140, 125, 342, 261);
		
		innerKeyValueTabViewer = createTableViewer(innerNameValueComposite, NAME_VALUE_COLUMN_INNER, new TransformGridContentProvider(),new PropertyLabelProvider());
		innerKeyValueTabViewer.setCellModifier(new PropertyGridInnerCellModifier(innerKeyValueTabViewer)); 
		innerKeyValueTabViewer.setInput(transformOperation.getNameValueProps());  
		innerKeyValueTabViewer.getTable().setBounds(7, 25, 330, 200);
		for (int columnIndex = 0, n = innerKeyValueTabViewer.getTable().getColumnCount(); columnIndex < n; columnIndex++) {
			innerKeyValueTabViewer.getTable().getColumn(columnIndex).setWidth(160);
		}
		
		ELTDefaultSubgroupComposite defaultnameValueInnerComposite = new ELTDefaultSubgroupComposite(innerNameValueComposite);
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
		btnInnerPropValueAddButton.setParent(innerNameValueComposite);
		btnInnerPropValueAddButton.setBounds(290, 0, 18, 18);
		btnInnerPropValueAddButton.setImage(SWTResourceManager.getImage(ADD_ICON));
		
		Button btnInnerPropValueDeleteButton=(Button) deleteInnerPropValueButton.getSWTWidgetControl();
		btnInnerPropValueDeleteButton.setParent(innerNameValueComposite);
		btnInnerPropValueDeleteButton.setBounds(310, 0, 18, 18);
		btnInnerPropValueDeleteButton.setImage(SWTResourceManager.getImage(DELETE_ICON));
		
		AbstractELTWidget mapFieldLableWidget = new ELTDefaultLable("Properties").lableWidth(250);
		innerOpInputSubgroupComposite.attachWidget(mapFieldLableWidget);
		Label mapFieldLable=(Label) mapFieldLableWidget.getSWTWidgetControl();
		mapFieldLable.setParent(innerNameValueComposite);
		mapFieldLable.setBounds(130, 0, 60, 18); 
		
		//---------------------------------
		Composite innerOpOutputComposite = new Composite(innerMainComposite, SWT.NONE);
		innerOpOutputComposite.setBounds(480, 10, 402, 320);
		innerOpOutputComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		innerOpOutputComposite.setLayoutData(new RowData(400, 400)); 	
	
		Label opOutputLable= new Label(innerOpOutputComposite, SWT.NONE);
		opOutputLable.setText("Operation Output Fields");
		opOutputLable.setBounds(100, 10, 60, 18); 

		TableViewer innerOpOutputTabViewer=	eltFixedWidget.createSchemaGrid(innerOpOutputComposite);
		DragDropUtility.INSTANCE.applyDragFromTableViewer(innerOpOutputTabViewer.getTable());  
		innerOpOutputTabViewer.getTable().setBounds(0, 10, 402, 310); 
		innerOpOutputComposite.setSize(innerOpOutputComposite.computeSize(400, 400)); 
	
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
			logger.error("Error occured while attaching listeners.");
		}
		
	}

	
	

	/**
	 * Return the initial size of the dialog.
	 *
	 * @return the initial size
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(1267, 746);
	}
	

	/**
	 * Creates the table viewer.
	 *
	 * @param composite the composite
	 * @param prop the prop
	 * @param iStructuredContentProvider the i structured content provider
	 * @param iTableLabelProvider the i table label provider
	 * @return the table viewer
	 */
	public TableViewer createTableViewer(Composite composite,String[] prop,IStructuredContentProvider iStructuredContentProvider,ITableLabelProvider iTableLabelProvider){
			TableViewer tableViewer= new TableViewer(composite,SWT.BORDER|SWT.FULL_SELECTION);
		    tableViewer.setContentProvider(iStructuredContentProvider);
		    tableViewer.setLabelProvider( iTableLabelProvider);
			tableViewer.setColumnProperties(prop); 
		
			Table table = tableViewer.getTable();
			ColumnLayoutData cld_table = new ColumnLayoutData();
			cld_table.widthHint = 46;
			cld_table.heightHint = 545;
			table.setLayoutData(cld_table);
			table.setVisible(true);
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			createTableColumns(table,prop);
			editors =createCellEditorList(table,prop.length); 
			tableViewer.setCellEditors(editors);

			//enables the tab functionality
			TableViewerEditor.create(tableViewer, new ColumnViewerEditorActivationStrategy(tableViewer), 
					ColumnViewerEditor.KEYBOARD_ACTIVATION | ColumnViewerEditor.TABBING_HORIZONTAL | 
					ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL);

			return tableViewer;
	}
	
	 /**
 	 * Creates the table columns.
 	 *
 	 * @param table the table
 	 * @param fields the fields
 	 */
 	public static void createTableColumns(Table table,String[] fields){
			for (String field : fields) {
				TableColumn tableColumn= new TableColumn(table, SWT.CENTER);
				tableColumn.setText(field);
				tableColumn.pack();
			}
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			
		}
		
		/**
		 * Creates the cell editor list.
		 *
		 * @param table the table
		 * @param size the size
		 * @return the cell editor[]
		 */
		public static CellEditor[] createCellEditorList(Table table,int size){
			CellEditor[] cellEditor = new CellEditor[size];
			for(int i=0;i<size;i++)
			addTextEditor(table,cellEditor, i);

			return cellEditor;
		}
		
		/**
		 * Adds the text editor.
		 *
		 * @param table the table
		 * @param cellEditor the cell editor
		 * @param position the Integer
		 */
		protected static void addTextEditor(Table table, CellEditor[] cellEditor, int position){
			cellEditor[position]=new TextCellEditor(table, SWT.COLOR_GREEN);
		}

	
		/**
		 * Gets the button.
		 *
		 * @param displayName the display name
		 * @return the button
		 */
		private AbstractELTWidget getButton(String displayName) {
			// Create browse button.
			AbstractELTWidget button = new ELTDefaultButton(displayName)
					.buttonWidth(20);
			return button;
		}

		
		/**
		 * Gets the listener helper.
		 *
		 * @param opList the op list
		 * @param tableViewer the table viewer
		 * @param fieldError the field error
		 * @param gridWidgetBuilder the grid widget builder
		 * @return the listener helper
		 */
		private ListenerHelper getListenerHelper(List<? extends PropertyField> opList,TableViewer tableViewer,AbstractELTWidget fieldError,GridWidgetCommonBuilder gridWidgetBuilder) {
				ListenerHelper helper = new ListenerHelper();
				ELTGridDetails value = new ELTGridDetails(opList, tableViewer, 
						(Label) fieldError.getSWTWidgetControl(), gridWidgetBuilder);
				helper.put(HelperType.SCHEMA_GRID, value);
				return helper;
		}		
	 
		
		 /**
   		 * Populate widget.
   		 */
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
		   
			/**
			 * Sets the validation status.
			 *
			 * @param validationStatus the new validation status
			 */
			public void setValidationStatus(ValidationStatus validationStatus) {
				this.validationStatus = validationStatus;
			}

			/**
			 * Gets the validation status.
			 *
			 * @return the validation status
			 */
			public ValidationStatus getValidationStatus() {
				return validationStatus;
			}

			
			/**
			 * Gets the transform property.
			 *
			 * @return the transform property
			 */
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
			 *
			 * @param parent the parent
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
			
			/**
			 * Creates the apply button.
			 *
			 * @param parent the parent
			 */
			private void createApplyButton(Composite parent) {
				applyButton = createButton(parent, IDialogConstants.NO_ID,
						"Apply", false);
				disableApplyButton();
			}
			
			/**
			 * Disable apply button.
			 */
			private void disableApplyButton() {
				applyButton.setEnabled(false);
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
			 */
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

			/* (non-Javadoc)
			 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
			 */
			@Override
			protected void okPressed() {
				updateOperationClassProperty();
				super.okPressed();
			}

			/* (non-Javadoc)
			 * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
			 */
			@Override
			protected void buttonPressed(int buttonId) {
				if(buttonId == 3){
					applyButton.setEnabled(false);
				}else{
					super.buttonPressed(buttonId);
				}
			}

			/**
			 * Sets the decorator.
			 *
			 * @param errorMessage the error message
			 * @return the control decoration
			 */
			protected  ControlDecoration setDecorator(String errorMessage) {
				ControlDecoration errorDecorator = WidgetUtility.addDecorator(editors[0].getControl(),errorMessage);
				errorDecorator.hide(); 
				return errorDecorator;
			}

			
			/**
			 * Update operation class property.
			 */
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
			
		    /**
    		 * Update operation system properties.
    		 *
    		 * @param element the element
    		 * @param flag the flag
    		 */
    		public void updateOperationSystemProperties(Object element, boolean flag){
		    	OperationSystemProperties opSystemProperties= (OperationSystemProperties)element;
		    	if(operationSystemProperties.contains(opSystemProperties))
		    	{
		    		operationSystemProperties.remove(opSystemProperties);
		    		opSystemProperties.setChecked(flag); 
		    		operationSystemProperties.add(opSystemProperties); 
		    	}
		    	
		    }

			/**
			 * Gets the tootl tip error message.
			 *
			 * @return the tootl tip error message
			 */
			public String getTootlTipErrorMessage() {
				return tootlTipErrorMessage.getErrorMessage();
			}
}


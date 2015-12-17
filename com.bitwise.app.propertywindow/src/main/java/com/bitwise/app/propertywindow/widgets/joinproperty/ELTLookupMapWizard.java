package com.bitwise.app.propertywindow.widgets.joinproperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.bitwise.app.common.datastructure.property.NameValueProperty;
import com.bitwise.app.common.datastructure.property.OperationField;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.widgets.customwidgets.operational.DragDropTransformOpImp;
import com.bitwise.app.propertywindow.widgets.customwidgets.operational.OperationGridCellModifier;
import com.bitwise.app.propertywindow.widgets.customwidgets.operational.OperationLabelProvider;
import com.bitwise.app.propertywindow.widgets.customwidgets.operational.PropertyGridCellModifier;
import com.bitwise.app.propertywindow.widgets.customwidgets.operational.PropertyLabelProvider;
import com.bitwise.app.propertywindow.widgets.customwidgets.operational.TransformGridContentProvider;
import com.bitwise.app.propertywindow.widgets.utility.DragDropUtility;

public class ELTLookupMapWizard extends Dialog{
	
	private Table table;
	private TableViewer outputTableViewer;
	private TableViewer inputTableViewer;
	private CellEditor[] editors; 
	private Composite comGrid;
	public static String PROPERTY_NAME = "Property Name";
	public static String PROPERTY_VALUE = "Property Values";
	public static final String OPERATIONAL_INPUT_FIELD = "Operation Input Fields";
	private String[] COLUMN_NAME = {PROPERTY_NAME, PROPERTY_VALUE};
	private Map<String, String> joinPropertyMap = new TreeMap<>();
	private static List<NameValueProperty> joinOutputList  = new ArrayList<>();
	private static List<OperationField> joinInputList  = new ArrayList<>();
	 
	 
	
	private ControlDecoration txtDecorator;
	private Label propertyError;
	
	 
	public ELTLookupMapWizard(Composite parentShell) {
		super((Shell) parentShell);
		setShellStyle(SWT.CLOSE |SWT.RESIZE | SWT.TITLE |  SWT.WRAP | SWT.APPLICATION_MODAL);
		 
	}
	
	private static void joinOutputProperty(TableViewer tv){
		NameValueProperty property = new NameValueProperty();
		
		if(joinOutputList.size() != 0){
			/*if(!validation())
				return;*/
		property.setPropertyName("");
		property.setPropertyValue("");
		joinOutputList.add(property);
		tv.refresh();
		
		} else {
			
			property.setPropertyName("");
			property.setPropertyValue("");
			joinOutputList.add(property);
			tv.refresh();
		}
	}
	
	private static void joinInputProperty(TableViewer viewer){
		OperationField join = new OperationField();
		if(joinInputList.size() != 0){
			
			join.setName("");
			joinInputList.add(join);
			viewer.refresh();
		} else {
			join.setName("");
			joinInputList.add(join);
			viewer.refresh();
		}
	}
	
	private void loaProperties(TableViewer viewer){
		if(joinPropertyMap != null && !joinPropertyMap.isEmpty()){
			for(String key : joinPropertyMap.keySet()){
				NameValueProperty join = new NameValueProperty();
				if(!key.trim().isEmpty() || joinPropertyMap.get(key).trim().isEmpty())
				join.setPropertyName(key);
				join.setPropertyValue(joinPropertyMap.get(key));
				joinOutputList.add(join);
			}
		}
	}

	@Override
	public Control createDialogArea(Composite parent) {
		parent.setSize(870,650);
		parent.setLayout(new GridLayout(2, false));
		
		Composite composite_1 = new Composite(parent, SWT.BORDER);
		
	    GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	    gd_composite_1.heightHint = 580;
	    gd_composite_1.widthHint = 310;
	    composite_1.setLayoutData(gd_composite_1);
	    
	    ScrolledComposite scrolledComposite = new ScrolledComposite(composite_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	    scrolledComposite.setBounds(10, 10, 290, 560);
	    scrolledComposite.setExpandHorizontal(true);
	    scrolledComposite.setExpandVertical(true);
	
	    	
	     
	    createComposite(scrolledComposite,10);
	    createComposite(scrolledComposite, 290);
	    
	   // scrolledComposite.setContent(comGrid);
	    
	    Composite composite_2 = new Composite(parent, SWT.BORDER);
	    GridData data = new GridData(SWT.RIGHT, SWT.TOP);
	    data.heightHint = 600;
	    data.widthHint = 520;
	    composite_2.setLayoutData(data);
	    
	    Label label = new Label(composite_2, SWT.NONE);
	    label.setBounds(6, 8, 100, 18);
	    label.setText("Output Mapping");
	  
	    outputTableViewer = createTableViewer(composite_2, COLUMN_NAME,new int[]{0, 30, 398, 538}, 196, new JoinContentProvider(), new PropertyLabelProvider());
	    //CellEditor propertyNameEditor = new TextCellEditor(outputTableViewer.getTable());
	    //CellEditor propertyValueEditor = new TextCellEditor(outputTableViewer.getTable());
	    //propertyNameEditor.setValidator(nameEditorValidation(outputTableViewer.getTable(), Messages.EmptyNameNotification));
	    //propertyValueEditor.setValidator(valueEditorValidation(Messages.EmptyValueNotification));
	    outputTableViewer.setCellModifier(new PropertyGridCellModifier(outputTableViewer));
	    outputTableViewer.setInput(joinOutputList);
	    loaProperties(outputTableViewer);
	    
	       
	    outputTableViewer.getTable().addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
			 
				String data = ((TableItem)event.item).getText();
				TableItem[] item = inputTableViewer.getTable().getItems();
				 for(int i = 0; i<item.length; i++){
					 if(item[i].getText().equals(data)){
						 
						 inputTableViewer.getTable().setSelection(inputTableViewer.getTable().indexOf(item[i]));
					 }
				 }
			}
		});
	    inputTableViewer.getTable().addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
	    		  String data = ((TableItem)event.item).getText();
				TableItem[] item = outputTableViewer.getTable().getItems();
				for(int i=0; i<item.length; i++){
					if(item[i].getText().equals(data)){
						 outputTableViewer.getTable().setSelection(outputTableViewer.getTable().indexOf(item[i]));
					}
				}
			}
		});
	    
	    propertyError = new Label(composite_2, SWT.None);
	    propertyError.setBounds(0, 570, 350, 25);
	    propertyError.setForeground(new Color(Display.getDefault(), 255, 0, 0));
	    propertyError.setVisible(false);
	    
	    Composite buttonComposite = new Composite(composite_2, SWT.None);
	    buttonComposite.setBounds(296, 4, 100, 24);
	    createLabel(buttonComposite);
	    
	    Composite compositeLabel = new Composite(composite_2, SWT.None);
	    compositeLabel.setBounds(400, 40, 110, 200);
	    
	    final Button[] radio = new Button[3];
	    radio[0] = buttonWidget(compositeLabel, SWT.RADIO, new int[]{0, 0, 90, 20}, "None");
	    radio[0].setSelection(true);
	    radio[1] = buttonWidget(compositeLabel, SWT.RADIO, new int[]{0, 20, 90, 20}, "Copy of in0"); 
	    radio[2] = buttonWidget(compositeLabel, SWT.RADIO, new int[]{0, 40, 90, 20}, "Copy of in1");
	    
	    for(int i=0; i<radio.length;i++){
	    radio[i].addSelectionListener(new SelectionAdapter() {
	    	@Override
			public void widgetSelected(SelectionEvent event) {
	    		Button button = (Button)event.widget;
	    		if(!radio[0].equals(button)){
	    			outputTableViewer.getTable().setEnabled(false);
	    		}else{
	    			outputTableViewer.getTable().setEnabled(true);
	    		}
	    	}
		});
	    }
	     	
	    
	    DragDropUtility.INSTANCE.applyDrop(outputTableViewer, new DragDropTransformOpImp(joinOutputList, false, outputTableViewer));
	    		 
		return parent;
		
	}
	 
	private Control createComposite(Composite parent, int y){	
		comGrid = new Composite(parent, SWT.BORDER);
		 //comGrid.setLayout(new RowLayout(SWT.VERTICAL));
		comGrid.setBounds(15, y, 233, 268);
		Label label = new Label(comGrid, SWT.LEFT);
		label.setBounds(0, 5, 65, 20);
		label.setText("Input Index");
		
		inputTableViewer = createTableViewer(comGrid, new String[]{OPERATIONAL_INPUT_FIELD}, new int[]{0, 30, 229, 232}, 224, new TransformGridContentProvider(), new OperationLabelProvider());
		//CellEditor inputEditor = new TextCellEditor(inputTableViewer.getTable());
		//inputEditor.setValidator(nameEditorValidation(inputTableViewer.getTable(), Messages.EmptyNameNotification));
		inputTableViewer.setCellModifier(new  OperationGridCellModifier(inputTableViewer));
		inputTableViewer.setInput(joinInputList);
		
		
		addButton(comGrid, new int[]{200, 8, 25, 15}, inputTableViewer);
		DragDropUtility.INSTANCE.applyDragFromTableViewer(inputTableViewer.getTable());
		
		return comGrid;
	}
		
		
	
	public static TableViewer createTableViewer(Composite composite,String[] prop, int[] bounds, int columnWidth,IStructuredContentProvider iStructuredContentProvider,ITableLabelProvider iTableLabelProvider){
		final TableViewer tableViewer= new TableViewer(composite, SWT.SINGLE | SWT.BORDER |SWT.FULL_SELECTION);
	    tableViewer.setContentProvider(iStructuredContentProvider);
	    tableViewer.setLabelProvider( iTableLabelProvider);
		tableViewer.setColumnProperties(prop); 
	
		final Table table = tableViewer.getTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				joinOutputProperty(tableViewer);
				joinInputProperty(tableViewer);
			}

			@Override
			public void mouseDown(MouseEvent e) {

			}
		});
		
		tableViewer.getTable().addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.keyCode == SWT.ARROW_UP) {
					e.doit = false;
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					e.doit = false;
				} else if (e.keyCode == SWT.TRAVERSE_ARROW_NEXT) {
					e.doit = false;
				} else if (e.keyCode == SWT.TRAVERSE_ARROW_PREVIOUS) {
					e.doit = false;
				}

			}
		});
		
		table.setVisible(true);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(bounds[0],bounds[1],bounds[2],bounds[3]);
		createTableColumns(table,prop, columnWidth);
		CellEditor[] editors =createCellEditorList(table,1); 
		tableViewer.setCellEditors(editors);
		
		return tableViewer;
	}
	
	public static  void createTableColumns(Table table,String[] fields, int width){
		for (String field : fields) {
			TableColumn tableColumn= new TableColumn(table, SWT.CENTER);
			tableColumn.setText(field);
			tableColumn.setWidth(width);
			//tableColumn.pack();
		}
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
	}
	
	public static   CellEditor[] createCellEditorList(Table table,int size){
		CellEditor[] cellEditor = new CellEditor[size];
		for(int i=0;i<size;i++)
		addTextEditor(table,cellEditor, i);

		return cellEditor;
	}
	
	private void addButton(Composite parent, int[] bounds, final TableViewer viewer){
		/*Label addlabel = new Label(parent, SWT.None);
		addlabel.setText("+");
		addlabel.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);*/
		Button bt = new Button(parent, SWT.PUSH);
		bt.setText("+");
		bt.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		bt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				joinInputProperty(viewer);
			}
		});
	}
	
	private void createLabel(Composite parent){	
		
		String addIcon = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/add.png";
		Label label = labelWidget(parent, SWT.CENTER|SWT.None, new int[]{25, 0, 25, 20}, "", new Image(null, addIcon));
		label.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				joinOutputProperty(outputTableViewer);
				
			}
		});
		
		/*Button b1= new Button(parent, SWT.PUSH);
		b1.setText("+");
		b1.setBounds(0, 0, 25, 20);
		b1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				joinOutputProperty(outputTableViewer);
			}
		});*/
		
		 
		String deleteIcon = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/delete.png";
		labelWidget(parent, SWT.CENTER|SWT.None, new int[]{25, 0, 25, 20}, "", new Image(null, deleteIcon));
		
		 
		String upIcon = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/up.png";
		labelWidget(parent, SWT.CENTER|SWT.None, new int[]{50, 0, 25, 20}, "", new Image(null, upIcon));
		
		 
		String downIcon = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/down.png";
		labelWidget(parent, SWT.CENTER|SWT.None, new int[]{72, 0, 25, 20}, "",new Image(null, downIcon));


	}
	
	protected static void addTextEditor(Table table, CellEditor[] cellEditor, int position){
		cellEditor[position]=new TextCellEditor(table, SWT.COLOR_GREEN);
	}
	
	private boolean validation(){
		for(NameValueProperty join : joinOutputList){
			if(!join.getPropertyName().trim().isEmpty() && !join.getPropertyValue().trim().isEmpty()){
				Matcher match = Pattern.compile(Constants.REGEX).matcher(join.getPropertyName());
				if(!match.matches()){
					propertyError.setVisible(true);
					propertyError.setText(Messages.PROPERTY_NAME_ALLOWED_CHARACTERS);
					txtDecorator.setDescriptionText(Messages.PROPERTY_NAME_ALLOWED_CHARACTERS);
					txtDecorator.show();
					return false;
				}else{
					propertyError.setVisible(true);
					propertyError.setText(Messages.EmptyFiledNotification);
					return false;
				}
			}
		}
		return true;
	}
	
	public Label labelWidget(Composite parent, int style, int[] bounds, String value, Image image){
		Label label = new Label(parent, style);
		label.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		label.setText(value);
		label.setImage(image);
		
		return label;
	}
	
	public Button buttonWidget(Composite parent, int style, int[] bounds, String value){
		Button button = new Button(parent, style);
			button.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
			button.setText(value);
		
		return button;
	}
	// Creates CellNAme Validator for table's cells
		/*private ICellEditorValidator  nameEditorValidation(final Table table, final String ErrorMessage ) {
			ICellEditorValidator propertyValidator = new ICellEditorValidator() {
				@Override
				public String isValid(Object value) {
					//isAnyUpdatePerformed = true;
					String currentSelectedFld = table.getItem(table.getSelectionIndex()).getText();
					String valueToValidate = String.valueOf(value).trim();
					if (valueToValidate.isEmpty()) {
						propertyError.setText(ErrorMessage);
						propertyError.setVisible(true);
						 
						return "ERROR"; //$NON-NLS-1$
					}

					for (NameValueProperty temp : joinOutputList) {
						if (!currentSelectedFld.equalsIgnoreCase(valueToValidate)&& temp.getPropertyName().equalsIgnoreCase(valueToValidate)) {
							propertyError.setText(Messages.RuntimePropertAlreadyExists);
							propertyError.setVisible(true);
							 
							return "ERROR"; //$NON-NLS-1$
						} else
							 
							propertyError.setVisible(false);
					}

					return null;

				}
			};
			return propertyValidator;
		}*/

		/*// Creates Value Validator for table's cells
		private ICellEditorValidator  valueEditorValidation(final String ErrorMessage) {
			ICellEditorValidator propertyValidator = new ICellEditorValidator() {
				@Override
				public String isValid(Object value) {
					//isAnyUpdatePerformed = true;
					table.getItem(table.getSelectionIndex()).getText();
					String valueToValidate = String.valueOf(value).trim();
					if (valueToValidate.isEmpty()) {
						propertyError.setText(ErrorMessage);
						propertyError.setVisible(true);
						 
						return "ERROR"; //$NON-NLS-1$
					} else {
						 
						propertyError.setVisible(false);
					}
					return null;

				}
			};
			return propertyValidator;
		}*/
	

	public static void main(String[] args) {
		Display dis = new Display();
		Shell s = new Shell(dis);
		ELTLookupMapWizard wid = new ELTLookupMapWizard(s);
		wid.createDialogArea(s);
	}
}

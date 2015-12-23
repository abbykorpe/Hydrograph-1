package com.bitwise.app.propertywindow.widgets.joinproperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;

import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTJoinMapWidget;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTCellModifier;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterContentProvider;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterLabelProvider;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTSWTWidgets;
import com.bitwise.app.propertywindow.widgets.joinlookupproperty.DragDropLookupImp;
import com.bitwise.app.propertywindow.widgets.joinlookupproperty.JoinContentProvider;
import com.bitwise.app.propertywindow.widgets.joinlookupproperty.LookupCellModifier;
import com.bitwise.app.propertywindow.widgets.joinlookupproperty.LookupLabelProvider;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridAddSelectionListener;
import com.bitwise.app.propertywindow.widgets.utility.DragDropUtility;

public class JoinMapGrid extends Dialog {
	
	private int inputPortValue = Integer.parseInt(ELTJoinMapWidget.value);
	private Text text;
	private Text text_1;
	private Text text_2;
	private Label errorLabel;
	private TableViewer outputTableViewer;
	private TableViewer[] inputTableViewer = new TableViewer[inputPortValue];
	
	public static final String OPERATIONAL_INPUT_FIELD = "Field Name";
	public static String PROPERTY_NAME = "Source Field";
	public static String PROPERTY_VALUE = "Output Field";
	private String[] COLUMN_NAME = {PROPERTY_NAME, PROPERTY_VALUE};
	private String[] INPUT_COLUMN_NAME = {OPERATIONAL_INPUT_FIELD};
	
	
	private static List<LookupMapProperty> joinOutputList  = new ArrayList<>();
	private Map<String, String> joinPropertyMap = new TreeMap<>();
	private List<ArrayList<FilterProperties>> inputList = new ArrayList<>();
	private ELTSWTWidgets widget = new ELTSWTWidgets();

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public JoinMapGrid(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE |SWT.RESIZE | SWT.TITLE |  SWT.WRAP | SWT.APPLICATION_MODAL);
	}

	
	public int getInputPortValue() {
		return inputPortValue;
	}


	public void setInputPortValue(int inputPortValue) {
		this.inputPortValue = inputPortValue;
	}


	private void joinInputUpProperty(TableViewer viewer, List<FilterProperties> joinInputList){
		FilterProperties join = new FilterProperties();
		if(joinInputList.size() != 0){
			if(!inputSchemavalidate(joinInputList,viewer))
				return;
			join.setPropertyname("");
			joinInputList.add(join);
			viewer.refresh();
		} else {
			join.setPropertyname("");
			joinInputList.add(join);
			viewer.refresh();
		}
	}
	
	private  void joinOutputProperty(TableViewer tv){
		LookupMapProperty property = new LookupMapProperty();
		
		if(joinOutputList.size() != 0){
			
		property.setSource_Field("");
		property.setOutput_Field("");
		joinOutputList.add(property);
		tv.refresh();
		
		} else {
			
			property.setSource_Field("");
			property.setOutput_Field("");
				
			joinOutputList.add(property);
			tv.refresh();
		}
	}
	
	private void loaProperties(TableViewer viewer){
		if(joinPropertyMap != null && !joinPropertyMap.isEmpty()){
			for(String key : joinPropertyMap.keySet()){
				LookupMapProperty join = new LookupMapProperty();
				if(!key.trim().isEmpty() || joinPropertyMap.get(key).trim().isEmpty())
				join.setSource_Field(key);
				join.setOutput_Field(joinPropertyMap.get(key));
				joinOutputList.add(join);
			}
		}
	}
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(6, false));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Composite composite = new Composite(container, SWT.BORDER);
		composite.setLayout(new GridLayout(1, false));
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 595;
		gd_composite.widthHint = 281;
		composite.setLayoutData(gd_composite);
		
		final ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.V_SCROLL);
		GridData gd_scrolledComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_scrolledComposite.heightHint = 542;
		gd_scrolledComposite.widthHint = 240;
		scrolledComposite.setLayoutData(gd_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);	
			

		final ExpandBar expandBar = new ExpandBar(scrolledComposite, SWT.NONE);
				expandBar.setLayoutData(new RowData(200, 550));
				
		for(int i = 0; i<inputPortValue;i++){
			ExpandItem xpndtmItem = new ExpandItem(expandBar, SWT.NONE);
			xpndtmItem.setText("Input index : in"+i);
			ArrayList<FilterProperties> joinInputList = new ArrayList<>();
			
			Composite com = (Composite) createComposite(expandBar, 40, joinInputList, i);
			inputList.add(joinInputList);
			
			xpndtmItem.setHeight(270);
			xpndtmItem.setControl(com);
			xpndtmItem.setExpanded(false);
		}	
		
			expandBar.getItem(0).setExpanded(true);
		
			expandBar.setBackground(new Color(Display.getDefault(), new RGB(250, 250, 250)));
			 Listener updateScrolledSize = new Listener()
		    {
		        @Override
		        public void handleEvent(Event arg0)
		        {
		            Display.getDefault().asyncExec(new Runnable()
		            {
		                @Override
		                public void run()
		                {
		                	scrolledComposite.setMinSize(expandBar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		                }
		            });
		        }
		    };
		    
		    expandBar.addListener(SWT.Expand, updateScrolledSize);
		    expandBar.addListener(SWT.Collapse, updateScrolledSize);
		    scrolledComposite.setContent(expandBar);
		    scrolledComposite.setMinSize(expandBar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		    
		    errorLabel = new Label(composite, SWT.None);
		    errorLabel.setAlignment(SWT.LEFT_TO_RIGHT);
		    GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		    gd_lblNewLabel_1.heightHint = 20;
		    gd_lblNewLabel_1.widthHint = 260;
		    errorLabel.setLayoutData(gd_lblNewLabel_1);
		    errorLabel.setForeground(new Color(Display.getDefault(), 255, 0, 0));
		    errorLabel.setText("PropertyError");
		    errorLabel.setVisible(false);
		    new Label(container, SWT.NONE);
		    
		    Composite composite_1 = new Composite(container, SWT.BORDER);
		    GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		    gd_composite_1.widthHint = 398;
		    gd_composite_1.heightHint = 596;
		    composite_1.setLayoutData(gd_composite_1);
		    
		    Composite composite_5 = new Composite(composite_1, SWT.BORDER);
		    composite_5.setBounds(290, 4, 100, 24);
		    createLabel(composite_5);
		    
		    outputTableViewer = widget.createTableViewer(composite_1, COLUMN_NAME,new int[]{0, 30, 398, 538}, 196, new JoinContentProvider(), new LookupLabelProvider());
		    
		    Label lblNewLabel = new Label(composite_1, SWT.NONE);
		    lblNewLabel.setBounds(10, 11, 92, 15);
		    lblNewLabel.setText("Output Mapping");
		    outputTableViewer.getTable().addMouseListener(new MouseAdapter() {
		    	@Override
				public void mouseDoubleClick(MouseEvent e) {
		    		joinOutputProperty(outputTableViewer);
				}
				@Override
				public void mouseDown(MouseEvent e) {
				}
			});
		    widget.createTableColumns(outputTableViewer.getTable(), COLUMN_NAME, 196);
		    CellEditor[] editors =widget.createCellEditorList(outputTableViewer.getTable(),2);
		    for(int i = 0; i<editors.length;i++){
		    	//editors[0].setValidator(nameEditorValidation(outputTableViewer.getTable(),Messages.EmptyNameNotification));
		    	//editors[1].setValidator(valueEditorValidation(Messages.EmptyValueNotification, outputTableViewer));
		    }
		    outputTableViewer.setColumnProperties(COLUMN_NAME);
		    outputTableViewer.setCellModifier(new LookupCellModifier(outputTableViewer));
		    outputTableViewer.setCellEditors(editors);
		    outputTableViewer.setInput(joinOutputList);
		    loaProperties(outputTableViewer);
		    outputTableViewer.getTable().addListener(SWT.Selection, new Listener() {
				
				@Override
				public void handleEvent(Event event) {
					String[] data = (((TableItem)event.item).getText()).split(Pattern.quote("."));
		
					if(data != null && data.length == 2){
						FilterProperties filter =new FilterProperties();
						filter.setPropertyname(data[1]);
						
						for(int i=0;i<inputPortValue;i++){
							if(inputList.get(i).contains(filter)){
								ExpandItem item =expandBar.getItem(i);
								item.setExpanded(true);
								inputTableViewer[i].getTable().setSelection(inputList.get(i).indexOf(filter));
								
							}
						}
					}
				}
			});
		    
		    new Label(container, SWT.NONE);
		    
		    Composite composite_2 = new Composite(container, SWT.BORDER);
		    composite_2.setLayout(new RowLayout(SWT.HORIZONTAL));
		    GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		    gd_composite_2.heightHint = 595;
		    gd_composite_2.widthHint = 133;
		    composite_2.setLayoutData(gd_composite_2);
		    
		    ScrolledComposite scrolledComposite_1 = new ScrolledComposite(composite_2, SWT.BORDER | SWT.V_SCROLL);
		    scrolledComposite_1.setLayoutData(new RowData(100, 564));
		    
		    Composite composite_3 = new Composite(scrolledComposite_1, SWT.BORDER);
		    composite_3.setLayout(new RowLayout(SWT.VERTICAL));
		    
		    final Button[] radio = new Button[inputPortValue+1];
		    radio[0] = widget.buttonWidget(composite_3, SWT.RADIO, new int[]{0, 0, 90, 20}, "None");
		    radio[0].setSelection(true);
		    int j=20;
		    for(int i=1,k=0;i<radio.length;i++,k++){
		    	
		    	radio[i] = widget.buttonWidget(composite_3, SWT.RADIO, new int[]{0, j, 90, 20}, "Copy of in"+k);
		    	j=j+20;
		    }
		    scrolledComposite_1.setContent(composite_3);
		    scrolledComposite_1.setExpandHorizontal(true);
		    scrolledComposite_1.setExpandVertical(true);
		   
		    scrolledComposite_1.setMinSize(composite_3.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		    //scrolledComposite_1.setMinSize(50,100);
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

		    DragDropUtility.INSTANCE.applyDrop(outputTableViewer, new DragDropLookupImp(joinOutputList, false, outputTableViewer));
		    
		return container;
	}
	
	private Control createComposite(Composite parent, int y, final List<FilterProperties> joinInputList, final int tableViewerIndex){	
		Composite comGrid = new Composite(parent, SWT.BORDER);
		//comGrid.setLayout(new RowLayout(SWT.VERTICAL));
		comGrid.setBounds(15, y, 226, 200);
		
		labelWidget(comGrid, SWT.LEFT, new int[]{2, 8, 90, 20}, "Input Index : in"+tableViewerIndex);
		
		inputTableViewer[tableViewerIndex] = widget.createTableViewer(comGrid, INPUT_COLUMN_NAME, new int[]{2, 30, 229, 232}, 224, new ELTFilterContentProvider(), new ELTFilterLabelProvider());
		inputTableViewer[tableViewerIndex].getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				joinInputUpProperty(inputTableViewer[tableViewerIndex],joinInputList);
			}
			@Override
			public void mouseDown(MouseEvent e) {
			}
		});
		widget.createTableColumns(inputTableViewer[tableViewerIndex].getTable(), INPUT_COLUMN_NAME, 224);
	    CellEditor[] editors =widget.createCellEditorList(inputTableViewer[tableViewerIndex].getTable(),1);
	    editors[0].setValidator(inputFieldEditorValidator(inputTableViewer[tableViewerIndex], joinInputList, Messages.EmptyNameNotification));
	    inputTableViewer[tableViewerIndex].setCellModifier(new ELTCellModifier(inputTableViewer[tableViewerIndex]));
	    inputTableViewer[tableViewerIndex].setColumnProperties(INPUT_COLUMN_NAME);
	    inputTableViewer[tableViewerIndex].setCellEditors(editors);
	    inputTableViewer[tableViewerIndex].setInput(joinInputList);
	
		addButton(comGrid, new int[]{200, 8, 25, 20}, inputTableViewer[tableViewerIndex],joinInputList);
		widget.applyDragFromTableViewer(inputTableViewer[tableViewerIndex].getTable(), tableViewerIndex);
		return comGrid;
	}
		
		
	
	 
	
	private void addButton(Composite parent, int[] bounds, final TableViewer viewer, final List<FilterProperties> joinInputList){
	
		Button bt = new Button(parent, SWT.PUSH);
		bt.setText("+");
		bt.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		//viewer.editElement(viewer.getElementAt(joinInputList.size() == 0 ? joinInputList.size() : joinInputList.size() - 1), 0);
		bt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				joinInputUpProperty(viewer,joinInputList);
			}
		});
	}
	
	private void createLabel(Composite parent){	
		//String addIcon = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/add.png";
		Button button = widget.buttonWidget(parent, SWT.CENTER|SWT.PUSH, new int[]{0, 0, 25, 20}, "+");
		ELTGridAddSelectionListener listener = new ELTGridAddSelectionListener();
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				joinOutputProperty(outputTableViewer);
			}
		});
		
		/*Label addLabel = labelWidget(parent, SWT.CENTER|SWT.BORDER, new int[]{0, 0, 25, 20}, "+");
		
		addLabel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				joinOutputProperty(outputTableViewer);	
			}
			@Override
			public void mouseDown(MouseEvent e) {
				 
			}
				
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});*/
	 
		 
		//String deleteIcon = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/delete.png";
		Label delete = labelWidget(parent, SWT.CENTER|SWT.BORDER, new int[]{25, 0, 25, 20}, "*");
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
		 
		//String upIcon = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/up.png";
		Label upLabel = labelWidget(parent, SWT.CENTER|SWT.BORDER, new int[]{50, 0, 25, 20}, "^");
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
		 
		//String downIcon = XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/down.png";
		Label downLabel = labelWidget(parent, SWT.CENTER|SWT.BORDER, new int[]{74, 0, 25, 20}, "|");
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

	public Label labelWidget(Composite parent, int style, int[] bounds, String value){
		Label label = new Label(parent, style);
		label.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		label.setText(value);
		//label.setImage(image);
		
		return label;
	}
	
	protected boolean inputSchemavalidate(List<FilterProperties> inputList, TableViewer tableViewer) {

		int propertyCounter = 0;

		for (FilterProperties temp : inputList) {
			if (!temp.getPropertyname().trim().isEmpty()) {
				String Regex = "[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}||[\\w]*";
				Matcher matchs = Pattern.compile(Regex).matcher(temp.getPropertyname().trim());
				if (!matchs.matches()) {
					tableViewer.getTable().setSelection(propertyCounter);
					errorLabel.setVisible(true);
					errorLabel.setText(Messages.ALLOWED_CHARACTERS);
					 
					return false;
				}
			} else {
				tableViewer.getTable().setSelection(propertyCounter);
				errorLabel.setVisible(true);
				errorLabel.setText(Messages.EmptyNameNotification);
				 
				return false;
			}
			propertyCounter++;

		}
		return true;
	}
	
	private ICellEditorValidator inputFieldEditorValidator(final TableViewer tableViewer, final List<FilterProperties> propertyLst,final String ErrorMessage) {
		ICellEditorValidator propertyValidator = new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				
				String currentSelectedFld = tableViewer.getTable().getItem(tableViewer.getTable().getSelectionIndex()).getText();
				String valueToValidate = String.valueOf(value).trim();
				if (valueToValidate.isEmpty()) {
					errorLabel.setText(ErrorMessage);
					errorLabel.setVisible(true);
					 
					return "ERROR"; //$NON-NLS-1$
				} else {
					 errorLabel.setVisible(false);
					
				}

				for (FilterProperties temp : propertyLst) {
					if (!currentSelectedFld.equalsIgnoreCase(valueToValidate)
							&& temp.getPropertyname().trim().equalsIgnoreCase(valueToValidate)) {
						errorLabel.setText(Messages.RuntimePropertAlreadyExists);
						errorLabel.setVisible(true);
						 
						return "ERROR"; //$NON-NLS-1$
					} else
						 
					errorLabel.setVisible(false);
				}

				return null;
			}
		};
		return propertyValidator;
	}
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(870, 757);
	}
	
	public static void main(String[] args) {
		Display dis= new Display();
		Shell sh = new Shell(dis);
		JoinMapGrid test = new JoinMapGrid(sh);
		test.open();
	}
}

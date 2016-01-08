package com.bitwise.app.propertywindow.widgets.customwidgets.joinproperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import com.bitwise.app.common.datastructure.property.FilterProperties;
import com.bitwise.app.common.datastructure.property.JoinMappingGrid;
import com.bitwise.app.common.datastructure.property.LookupMapProperty;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.widgets.customwidgets.ELTJoinWidget;
import com.bitwise.app.propertywindow.widgets.customwidgets.joinlookupproperty.JoinContentProvider;
import com.bitwise.app.propertywindow.widgets.customwidgets.joinlookupproperty.LookupCellModifier;
import com.bitwise.app.propertywindow.widgets.customwidgets.joinlookupproperty.LookupLabelProvider;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTCellModifier;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterContentProvider;
import com.bitwise.app.propertywindow.widgets.filterproperty.ELTFilterLabelProvider;
import com.bitwise.app.propertywindow.widgets.gridwidgets.basic.ELTSWTWidgets;
import com.bitwise.app.propertywindow.widgets.listeners.grid.ELTGridAddSelectionListener;

public class JoinMapGrid extends Dialog {

	private static final String NONE = "None";
	private Label errorLabel;
	private TableViewer outputTableViewer;
	private   int j=20;
	private Button okButton;
	private int inputPortValue = ELTJoinWidget.value;
	private TableViewer[] inputTableViewer = new TableViewer[inputPortValue];
	private Button[] radio = new Button[inputPortValue+1];
	private Composite expandItemComposite;

	public static final String OPERATIONAL_INPUT_FIELD = "Field Name";
	protected static final String ERROR = null;
	public static String PROPERTY_NAME = "Source Field";
	public static String PROPERTY_VALUE = "Output Field";
	private String[] COLUMN_NAME = {PROPERTY_NAME, PROPERTY_VALUE};
	private String[] INPUT_COLUMN_NAME = {OPERATIONAL_INPUT_FIELD};

	private List<FilterProperties> joinInputList;
	private List<LookupMapProperty> joinOutputList;
	private List<List<FilterProperties>> joinSchemaList = new ArrayList<>();
	private ELTSWTWidgets widget = new ELTSWTWidgets();
	private JoinMappingGrid joinMappingGrid;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public JoinMapGrid(Shell parentShell, JoinMappingGrid joinPropertyGrid) {
		super(parentShell);
		setShellStyle(SWT.CLOSE |SWT.RESIZE | SWT.TITLE |  SWT.WRAP | SWT.APPLICATION_MODAL);
		this.joinMappingGrid = joinPropertyGrid;

	}

	public void getJoinPropertyGrid(){
		joinMappingGrid.setLookupInputProperties(joinSchemaList);
		joinMappingGrid.setLookupMapProperties(joinOutputList);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		System.out.println("JoinMapGrid: createDialogArea**************");
		final Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText("Join Mapping");
		container.setLayout(new GridLayout(6, false));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Composite composite = new Composite(container, SWT.None);
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

		for(int i = 0; i < inputPortValue;i++){
			if(joinMappingGrid!=null){
				if(joinMappingGrid.getLookupInputProperties()!=null && !joinMappingGrid.getLookupInputProperties().isEmpty()){
					if(i<joinMappingGrid.getLookupInputProperties().size()) 
						joinInputList = joinMappingGrid.getLookupInputProperties().get(i);
					else
						joinInputList = new ArrayList<>();
				}
				else{
					joinInputList = new ArrayList<>();
				}
			}
			if(joinSchemaList!=null){
				joinSchemaList.add(joinInputList);
			}
			expandItemComposite = (Composite) createComposite(expandBar, joinInputList, i);
		}	

		if(joinMappingGrid.getLookupMapProperties() != null && !joinMappingGrid.getLookupMapProperties().isEmpty()){
			joinOutputList = joinMappingGrid.getLookupMapProperties();
		}
		else{
			joinOutputList = new ArrayList<>();
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

		Composite composite_1 = new Composite(container, SWT.None);
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.widthHint = 398;
		gd_composite_1.heightHint = 596;
		composite_1.setLayoutData(gd_composite_1);

		Composite composite_5 = new Composite(composite_1, SWT.None);
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
		editors[0].setValidator(sourceEditorValidator(outputTableViewer, Messages.EmptyNameNotification, joinOutputList));
		editors[1].setValidator(outputFieldEditorValidator(outputTableViewer, Messages.EmptyNameNotification, joinOutputList));
		outputTableViewer.setColumnProperties(COLUMN_NAME);
		outputTableViewer.setCellModifier(new LookupCellModifier(outputTableViewer));
		outputTableViewer.setCellEditors(editors);
		outputTableViewer.setInput(joinOutputList);
		outputTableViewer.getTable().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				String[] data = (((TableItem)event.item).getText()).split(Pattern.quote("."));
				if(data != null && data.length == 2){
					FilterProperties filter =new FilterProperties();
					filter.setPropertyname(data[1]);
					for(int i=0;i<inputPortValue;i++){
						if(joinInputList != null && joinInputList.contains(filter)){
							ExpandItem item = expandBar.getItem(i);
							item.setExpanded(true);
							inputTableViewer[i].getTable().setSelection(joinInputList.indexOf(filter));
						}
					}
				}
			}
		});
		errorLabel = new Label(composite_1, SWT.None);
		errorLabel.setBounds(0, 576, 350, 25);
		errorLabel.setForeground(new Color(Display.getDefault(), 255, 0, 0));
		errorLabel.setVisible(false);

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

		radio[0] = widget.buttonWidget(composite_3, SWT.RADIO, new int[]{0, 0, 90, 20}, NONE);
		radio[0].setSelection(true);
		for(int i=1,k=0;i<radio.length;i++,k++){
			radio[i] = widget.buttonWidget(composite_3, SWT.RADIO, new int[]{0, j, 90, 20}, "Copy of in"+k);
			j=j+20;
		}
		scrolledComposite_1.setContent(composite_3);
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);
		scrolledComposite_1.setMinSize(composite_3.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		for(int i=0; i<radio.length;i++){
			radio[i].addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {

					Button button = (Button)event.widget;
					button.getSelection();
					if(button.getSelection()){
						if(NONE.equalsIgnoreCase(button.getText())){
							outputTableViewer.getTable().setEnabled(true);
							joinMappingGrid.setButtonText(button.getText());
							joinMappingGrid.setIsSelected(false);
						}else{
							radio[0].setSelection(false);
							outputTableViewer.getTable().setEnabled(false);  
							joinMappingGrid.setButtonText(button.getText());
							joinMappingGrid.setIsSelected(true);
						}
					}
				}
			});
		}
		populate();
		if(joinOutputList!=null){
			dropData(outputTableViewer, joinOutputList, true);
		}
		return container;
	}
	private void populate(){
		for(int i=0;i<radio.length;i++){
			if(joinMappingGrid.getButtonText() != null){
				if(joinMappingGrid.getButtonText().equalsIgnoreCase(radio[i].getText()))
					radio[i].setSelection(joinMappingGrid.isSelected() ? true : false);
					radio[0].setSelection(joinMappingGrid.isSelected() ? false : true);
			}else{
					radio[0].setSelection(!joinMappingGrid.isSelected() ? true : false);
			}
		}
	}

	private Control createComposite(ExpandBar expandBar, final List<FilterProperties> joinInputList, final int tableViewerIndex){	
		ExpandItem	xpndtmItem = new ExpandItem(expandBar, SWT.NONE);
		xpndtmItem.setText("Input index : in"+tableViewerIndex);

		Composite comGrid = new Composite(expandBar, SWT.BORDER);
		comGrid.setBounds(15, 0, 226, 200);

		xpndtmItem.setControl(comGrid);
		xpndtmItem.setHeight(270);
		xpndtmItem.setExpanded(false);
		labelWidget(comGrid, SWT.LEFT, new int[]{2, 5, 90, 20}, "Input Index : in"+tableViewerIndex);

		inputTableViewer[tableViewerIndex] = widget.createTableViewer(comGrid, INPUT_COLUMN_NAME, new int[]{2, 30, 229, 232}, 224, new ELTFilterContentProvider(), new ELTFilterLabelProvider());
		inputTableViewer[tableViewerIndex].getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				addRowToTable(inputTableViewer[tableViewerIndex],joinInputList);
			}
			@Override
			public void mouseDown(MouseEvent e) {
			}
		});
		widget.createTableColumns(inputTableViewer[tableViewerIndex].getTable(), INPUT_COLUMN_NAME, 224);
		CellEditor[] editors = widget.createCellEditorList(inputTableViewer[tableViewerIndex].getTable(),1);
		editors[0].setValidator(inputSchemaEditorValidation(joinInputList, Messages.EmptyNameNotification, inputTableViewer[tableViewerIndex]));
		inputTableViewer[tableViewerIndex].setCellModifier(new ELTCellModifier(inputTableViewer[tableViewerIndex]));
		inputTableViewer[tableViewerIndex].setColumnProperties(INPUT_COLUMN_NAME);
		inputTableViewer[tableViewerIndex].setCellEditors(editors);
		inputTableViewer[tableViewerIndex].setInput(joinInputList);

		addButton(comGrid, new int[]{170, 8, 25, 20}, inputTableViewer[tableViewerIndex], joinInputList);
		deleteButton(comGrid,new int[]{200, 8, 25, 20}, inputTableViewer[tableViewerIndex], joinInputList);
		widget.applyDragFromTableViewer(inputTableViewer[tableViewerIndex].getTable(), tableViewerIndex);

		return comGrid;
	}

	private void addButton(Composite parent, int[] bounds, final TableViewer viewer, final List<FilterProperties> joinInputList){
		Button bt = new Button(parent, SWT.PUSH);
		bt.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		bt.setImage(new Image(null,XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/add.png"));
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

		Label delete = widget.labelWidget(parent, SWT.CENTER, new int[]{25, 0, 25, 20}, "",new Image(null,XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/delete.png"));
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

		Label upLabel = widget.labelWidget(parent, SWT.CENTER, new int[]{50, 0, 25, 20}, "",new Image(null,XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/up.png"));
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


		Label downLabel = widget.labelWidget(parent, SWT.CENTER, new int[]{74, 0, 25, 20}, "",new Image(null,XMLConfigUtil.INSTANCE.CONFIG_FILES_PATH + "/icons/down.png"));
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

	public Button buttonWidget(Composite parent, int style, int[] bounds, String value, Image image){
		Button button = new Button(parent, style);
		button.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		button.setText(value);
		button.setImage(image);
		return button;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,true);
		okButton.addSelectionListener(new SelectionAdapter() {
		});
		createButton(parent, IDialogConstants.CANCEL_ID,IDialogConstants.CANCEL_LABEL, false);
	}

	protected boolean inputSchemavalidate(List<FilterProperties> inputList, TableViewer tableViewer) {
		int propertyCounter = 0;
		for (FilterProperties temp : inputList) {
			if (!temp.getPropertyname().trim().isEmpty()) {
				Matcher matchs = Pattern.compile(Constants.REGEX).matcher(temp.getPropertyname().trim());
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

	private boolean validateOutputSchema(){
		int propertycount = 0;
		int propertyValuecount = 0;
		for(LookupMapProperty join : joinOutputList){
			if(join.getSource_Field().trim().isEmpty()){
				outputTableViewer.getTable().setSelection(propertycount);
				errorLabel.setVisible(true);
				errorLabel.setText(Messages.EmptyNameNotification);
				return false;
			}else if(join.getOutput_Field().trim().isEmpty()){
				outputTableViewer.getTable().setSelection(propertyValuecount);
				errorLabel.setVisible(true);
				errorLabel.setText(Messages.EmptyValueNotification);
			}else{
				errorLabel.setVisible(false);
			}
			propertycount++;
			propertyValuecount++;
		}
		return true;
	}

	// Creates Value Validator for table's cells
	private ICellEditorValidator  inputSchemaEditorValidation(final List<FilterProperties> joinInputList, final String errorMessage, final TableViewer viewer) {
		ICellEditorValidator propertyValidator = new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				String currentSelectedFld = viewer.getTable().getItem(viewer.getTable().getSelectionIndex()).getText();
				String valueToValidate = String.valueOf(value).trim();
				if (StringUtils.isEmpty(valueToValidate)) {
					errorLabel.setText(errorMessage);
					errorLabel.setVisible(true);
					return ERROR;
				}
				for (FilterProperties temp : joinInputList) {
					if (!currentSelectedFld.equalsIgnoreCase(valueToValidate)&& temp.getPropertyname().equalsIgnoreCase(valueToValidate)) {
						errorLabel.setText(Messages.RuntimePropertAlreadyExists);
						errorLabel.setVisible(true);
						okButton.setEnabled(false);
						return ERROR;
					} 
					else{
						errorLabel.setVisible(false);
						okButton.setEnabled(true);
					}
				}
				return null;
			}
		};
		return propertyValidator;
	}

	// Creates CellValue Validator for table's cells
	private ICellEditorValidator outputFieldEditorValidator(final TableViewer viewer, final String errorMessage, final List<LookupMapProperty> propertyList) {
		ICellEditorValidator propertyValidator = new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				String currentSelectedFld = viewer.getTable().getItem(viewer.getTable().getSelectionIndex()).getText();
				String valueToValidate = String.valueOf(value).trim();
				if (StringUtils.isEmpty(valueToValidate)) {
					errorLabel.setText(errorMessage);
					errorLabel.setVisible(true);
					okButton.setEnabled(false);
					return ERROR;
				}else{okButton.setEnabled(true);}
				for (LookupMapProperty temp : propertyList) {
					if (!currentSelectedFld.equalsIgnoreCase(valueToValidate)&& temp.getOutput_Field().equalsIgnoreCase(valueToValidate)) {
						errorLabel.setText(Messages.RuntimePropertAlreadyExists);
						errorLabel.setVisible(true);
						okButton.setEnabled(false);
						return ERROR;
					} 
					else{
						errorLabel.setVisible(false);
						okButton.setEnabled(true);
					}
				}
				return null;
			}
		};
		return propertyValidator;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(870, 757);
	}

	// Creates CellNAme Validator for table's cells
	private ICellEditorValidator sourceEditorValidator(final TableViewer viewer, final String errorMessage, final List<LookupMapProperty> propertyList) {
		ICellEditorValidator propertyValidator = new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				String currentSelectedFld = viewer.getTable().getItem(viewer.getTable().getSelectionIndex()).getText();
				String valueToValidate = String.valueOf(value).trim();
				if (StringUtils.isEmpty(valueToValidate)) {
					errorLabel.setText(errorMessage);
					errorLabel.setVisible(true);
					okButton.setEnabled(false);
					return ERROR;
				}else{okButton.setEnabled(true);}
				for (LookupMapProperty temp : propertyList) {
					if (!currentSelectedFld.equalsIgnoreCase(valueToValidate)&& temp.getSource_Field().equalsIgnoreCase(valueToValidate)) {
						errorLabel.setText(Messages.RuntimePropertAlreadyExists);
						errorLabel.setVisible(true);
						okButton.setEnabled(false);
						return ERROR;
					} 
					else{
						errorLabel.setVisible(false);
						okButton.setEnabled(true);
					}
				}
				return null;
			}
		};
		return propertyValidator;
	}
	private void addRowToTable(TableViewer viewer, List<FilterProperties> joinInputList){
		FilterProperties join = new FilterProperties();
		if(joinInputList!=null && joinInputList.size() != 0){
			if(!inputSchemavalidate(joinInputList, viewer))
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

	private  void joinOutputProperty(TableViewer viewer){
		LookupMapProperty property = new LookupMapProperty();
		if(joinOutputList.size() != 0){
			if(!validateOutputSchema())
				return;
			property.setSource_Field("");
			property.setOutput_Field("");
			joinOutputList.add(property);
			viewer.refresh();
			viewer.editElement(viewer.getElementAt(joinOutputList.size() - 1), 0);
		} else {
			property.setSource_Field("");
			property.setOutput_Field("");
			joinOutputList.add(property);
			viewer.refresh();
			viewer.editElement(property, 0);
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

							for(LookupMapProperty lookupMapProperty : joinOutputList){
								String outputField = lookupMapProperty.getOutput_Field();
								if(!tempList.contains(outputField)){
									errorLabel.setText(Messages.OUTPUT_FIELD_EXISTS);
									errorLabel.setVisible(false);
									tempList.add(outputField);
								}else{
									errorLabel.setVisible(true);
									errorLabel.setText(Messages.OUTPUT_FIELD_EXISTS);
									//outputTableViewer.editElement(outputTableViewer.getElementAt(joinOutputList.indexOf(lookupMapProperty)), 1);
								}
							}
						}
						tableViewer.refresh(); 
					}
				}
			} 
		});
	}
}
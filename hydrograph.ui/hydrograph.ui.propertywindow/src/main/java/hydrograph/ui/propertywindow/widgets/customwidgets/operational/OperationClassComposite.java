package hydrograph.ui.propertywindow.widgets.customwidgets.operational;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.propertywindow.messages.Messages;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class OperationClassComposite extends Composite {

	private Text operationIdTextBox;
	private Text operationTextBox;
	private Text parameterTextBox;
	private Table inputTable,outputTable;
	private TableViewer inputTableViewer,outputTableViewer;
	private Button addButtonInputTable,deletButtonInputTable,browseButton,addButtonOutputTable,deletButtonOutputTable;
    private Button btnIsParam;
    private static final String OPERATION_OUTPUT_FIELD_TABLE_VIEWER = "operationOutputFieldTableViewer";
	private static final String INPUT_DELETE_BUTTON = "inputDeletButton";
	private static final String INPUT_ADD_BUTTON = "inputAddButton";
	private static final String OPERATION_INPUT_FIELD_TABLE_VIEWER = "operationInputFieldTableViewer";
	private static final String OPERATION_ID_TEXT_BOX = "operationIDTextBox";
	private static final String OPERATION_CLASS_TEXT_BOX = "operationClassTextBox";
	private static final String PARAMETER_TEXT_BOX = "parameterTextBox";
	private static final String OUTPUT_DELETE_BUTTON = "outputDeleteButton";
	private static final String OUTPUT_ADD_BUTTON = "outputAddButton";
	private static final String BTN_NEW_BUTTON = "btnNewButton";
	private Button switchToExpressionButton;
	private Button switchToClassButton;
	private Label lblSwitchTo;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public OperationClassComposite(Composite parent, int style,final MappingSheetRow mappingSheetRow,Component component) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		
		createOperationInputTable();
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite.heightHint = 191;
		gd_composite.widthHint = 272;
		composite.setLayoutData(gd_composite);
		new Label(composite, SWT.NONE);
		
		if(Constants.TRANSFORM.equalsIgnoreCase(component.getComponentName()))
		{
		lblSwitchTo = new Label(composite, SWT.NONE);
		lblSwitchTo.setText("Switch to");
		
		switchToClassButton = new Button(composite, SWT.RADIO);
		switchToClassButton.setText("Class");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		switchToClassButton.setSelection(true);
		new Label(composite, SWT.NONE);
		
		switchToExpressionButton = new Button(composite, SWT.RADIO);
		switchToExpressionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button toggleButton=(Button)e.widget;	
				toggleButton.getParent().getParent().setVisible(false);
			}
		});
		switchToExpressionButton.setText("Expression");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		}
		else
		{
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			
		}
		Label lblExpression = new Label(composite, SWT.NONE);
		GridData gd_lblExpression = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_lblExpression.minimumWidth = 70;
		gd_lblExpression.heightHint = 15;
		lblExpression.setLayoutData(gd_lblExpression);
		lblExpression.setText("Operation Id");
		
		operationIdTextBox = new Text(composite, SWT.BORDER);
		operationIdTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_lblNewLabel.minimumWidth = 50;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Class");
		
		operationTextBox = new Text(composite, SWT.BORDER);
		operationTextBox.setEditable(false);
		operationTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		browseButton= new Button(composite, SWT.NONE);
		GridData gd_button = new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1);
		gd_button.minimumWidth = 30;
		gd_button.widthHint = 34;
		browseButton.setLayoutData(gd_button);
		browseButton.setText("...");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblParameter = new Label(composite, SWT.NONE);
		GridData gd_lblParameter = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_lblParameter.minimumWidth = 50;
		lblParameter.setLayoutData(gd_lblParameter);
		lblParameter.setText("Parameter");
		
		parameterTextBox = new Text(composite, SWT.BORDER);
		parameterTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		parameterTextBox.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Text text=(Text)e.widget;
				mappingSheetRow.setWholeOperationParameterValue(text.getText());	
			}
		});
		parameterTextBox.setEnabled(mappingSheetRow.isWholeOperationParameter());
		if (mappingSheetRow.getWholeOperationParameterValue() != null)
		parameterTextBox.setText(mappingSheetRow.getWholeOperationParameterValue());
		btnIsParam= new Button(composite, SWT.CHECK);
		btnIsParam.setAlignment(SWT.CENTER);
		btnIsParam.setText(Messages.IS_PARAM);
		GridData gd_btnIsParam = new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1);
		gd_btnIsParam.minimumWidth = 70;
		btnIsParam.setLayoutData(gd_btnIsParam);
		btnIsParam.setSelection(mappingSheetRow.isWholeOperationParameter());
		
		createOperationOutputFieldTable();
		setAllWidgetsOnIsParamButton(btnIsParam);
		disabledWidgetsifWholeOperationIsParameter(btnIsParam,mappingSheetRow.isWholeOperationParameter());
		if (mappingSheetRow.getOperationClassPath() != null){
			operationTextBox.setText(mappingSheetRow.getOperationClassPath());
			}
		
		
         
	}
    
	

	private void disabledWidgetsifWholeOperationIsParameter(Button isParam,boolean isWholeOperationParameter) 
	{
		if (isWholeOperationParameter) {
			Button text = (Button) isParam;
			Text parameterTextBox = (Text) text.getData(PARAMETER_TEXT_BOX);
			TableViewer operationInputFieldTableViewer = (TableViewer) text.getData(OPERATION_INPUT_FIELD_TABLE_VIEWER);
			TableViewer operationalOutputFieldTableViewer = (TableViewer) text.getData(OPERATION_OUTPUT_FIELD_TABLE_VIEWER);
			Text operationClassTextBox = (Text) text.getData(OPERATION_CLASS_TEXT_BOX);
			Text operationIDTextBox = (Text) text.getData(OPERATION_ID_TEXT_BOX);
			Button btnNewButton = (Button) text.getData(BTN_NEW_BUTTON);
			Button inputAdd = (Button) text.getData(INPUT_ADD_BUTTON);

			Button inputDelete = (Button) text.getData(INPUT_DELETE_BUTTON);
			Button outputAdd = (Button) text.getData(OUTPUT_ADD_BUTTON);
			Button outputDelete = (Button) text.getData(OUTPUT_DELETE_BUTTON);
			parameterTextBox.setEnabled(true);

			operationInputFieldTableViewer.getTable().setEnabled(false);

			operationalOutputFieldTableViewer.getTable().setEnabled(false);
			operationClassTextBox.setEnabled(false);

			operationIDTextBox.setEnabled(false);

			btnNewButton.setEnabled(false);
			inputAdd.setEnabled(false);
			inputDelete.setEnabled(false);

			outputAdd.setEnabled(false);
			outputDelete.setEnabled(false);

		}
	}
	
	private void setAllWidgetsOnIsParamButton(Button isParam) {
		isParam.setData(PARAMETER_TEXT_BOX, parameterTextBox);
		isParam.setData(OPERATION_CLASS_TEXT_BOX, operationTextBox);
		isParam.setData(OPERATION_ID_TEXT_BOX, operationIdTextBox);
		isParam.setData(OPERATION_INPUT_FIELD_TABLE_VIEWER, inputTableViewer);
		isParam.setData(OPERATION_OUTPUT_FIELD_TABLE_VIEWER, outputTableViewer);
		isParam.setData(INPUT_ADD_BUTTON, addButtonInputTable);
		isParam.setData(INPUT_DELETE_BUTTON, deletButtonInputTable);
		isParam.setData(OUTPUT_ADD_BUTTON, addButtonOutputTable);
		isParam.setData(OUTPUT_DELETE_BUTTON, deletButtonOutputTable);
		isParam.setData(BTN_NEW_BUTTON,browseButton);
	}
	
	private void createOperationInputTable() {
		Composite operationInputFieldComposite = new Composite(this, SWT.NONE);
		operationInputFieldComposite.setLayout(new GridLayout(1, false));
		GridData gridDataOperationInputFieldComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gridDataOperationInputFieldComposite.heightHint = 200;
		gridDataOperationInputFieldComposite.widthHint = 159;
		operationInputFieldComposite.setLayoutData(gridDataOperationInputFieldComposite);
		
		Composite buttonComposite = new Composite(operationInputFieldComposite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(2, false));
		GridData gd_buttonComposite = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_buttonComposite.heightHint = 36;
		gd_buttonComposite.widthHint = 139;
		buttonComposite.setLayoutData(gd_buttonComposite);
		
		 addButtonInputTable = new Button(buttonComposite, SWT.NONE);
		 addButtonInputTable.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON));
		 deletButtonInputTable = new Button(buttonComposite, SWT.NONE);
		 deletButtonInputTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		 deletButtonInputTable.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON));
		
		 inputTableViewer = new TableViewer(operationInputFieldComposite, SWT.BORDER | SWT.FULL_SELECTION|SWT.MULTI);
		inputTable = inputTableViewer.getTable();
		inputTable.setLinesVisible(true);
		inputTable.setHeaderVisible(true);
		inputTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		inputTable.setVisible(true);
		
		addButtonInputTable.setToolTipText(Messages.ADD_SCHEMA_TOOLTIP);
		deletButtonInputTable.setToolTipText(Messages.DELETE_SCHEMA_TOOLTIP);
		
	}

	private void createOperationOutputFieldTable() {
		Composite operationOutputFieldComposite = new Composite(this, SWT.NONE);
		operationOutputFieldComposite.setLayout(new GridLayout(1, false));
		GridData gridDataOperationOutputFieldComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gridDataOperationOutputFieldComposite.heightHint = 200;
		gridDataOperationOutputFieldComposite.widthHint = 159;
		operationOutputFieldComposite.setLayoutData(gridDataOperationOutputFieldComposite);
		
		Composite buttonCompositeForOperationOutputField = new Composite(operationOutputFieldComposite, SWT.NONE);
		buttonCompositeForOperationOutputField.setLayout(new GridLayout(2, false));
		GridData gd_buttonCompositeForOperationOutputField = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_buttonCompositeForOperationOutputField.heightHint = 36;
		gd_buttonCompositeForOperationOutputField.widthHint = 139;
		buttonCompositeForOperationOutputField.setLayoutData(gd_buttonCompositeForOperationOutputField);
		
		addButtonOutputTable = new Button(buttonCompositeForOperationOutputField, SWT.NONE);
		addButtonOutputTable.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON));
		deletButtonOutputTable = new Button(buttonCompositeForOperationOutputField, SWT.NONE);
		deletButtonOutputTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		deletButtonOutputTable.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON));
		
		 outputTableViewer = new TableViewer(operationOutputFieldComposite, SWT.BORDER | SWT.FULL_SELECTION|SWT.MULTI);
		 outputTable = outputTableViewer.getTable();
		 outputTable.setLinesVisible(true);
		 outputTable.setHeaderVisible(true);
		 outputTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		 outputTable.setVisible(true);
	}

	public TableViewer getInputTableViewer() {
		return inputTableViewer;
	}

	public TableViewer getOutputTableViewer() {
		return outputTableViewer;
	}

	public Text getOperationIdTextBox() {
		return operationIdTextBox;
	}

	public Text getOperationTextBox() {
		return operationTextBox;
	}

	public Text getParameterTextBox() {
		return parameterTextBox;
	}

	public Button getAddButtonInputTable() {
		return addButtonInputTable;
	}

	public Button getDeletButtonInputTable() {
		return deletButtonInputTable;
	}

	public Button getBrowseButton() {
		return browseButton;
	}

	public Button getAddButtonOutputTable() {
		return addButtonOutputTable;
	}

	public Button getDeletButtonOutputTable() {
		return deletButtonOutputTable;
	}

	public Button getBtnIsParam() {
		return btnIsParam;
	}
    public Button getSwitchToExpressionButton()
    {
    return switchToExpressionButton;
    }
	public Button getSwitchToClassButton() {
		return switchToClassButton;
	}



	@Override
	protected void checkSubclass() {
	}

}
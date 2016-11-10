package hydrograph.ui.propertywindow.widgets.customwidgets.operational;



import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.expression.ExpressionEditorData;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.expression.editor.launcher.LaunchExpressionEditor;
import hydrograph.ui.expression.editor.util.FieldDataTypeMap;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;
import hydrograph.ui.propertywindow.messages.Messages;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

public class ExpressionComposite extends Composite {
	
	public static final String EXPRESSION_COMPOSITE_KEY="expression-composite";
	private Text expressionIdTextBox;
	private Text expressionTextBox;
	private Text parameterTextBox;
	private Text outputFieldTextBox;
	private Table table;
	private TableViewer tableViewer;
	private Button addButton,deletButton,browseButton;
    private Button btnIsParam;
    private Button switchToClassButton;
    private Button switchToExpressionButton;
    private Label lblNewLabel_1;
	private MappingSheetRow mappingSheetRow;
	private Component component;
	private Composite composite_1;
	private Composite composite_2;
	public ExpressionComposite(Composite parent, int style,final MappingSheetRow mappingSheetRow, final Component component) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		this.mappingSheetRow=mappingSheetRow;
		this.component=component;
		Composite selectColumnComposite = new Composite(this, SWT.NONE);
		selectColumnComposite.setLayout(new GridLayout(1, false));
		GridData gd_selectColumnComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_selectColumnComposite.heightHint = 200;
		gd_selectColumnComposite.widthHint = 159;
		selectColumnComposite.setLayoutData(gd_selectColumnComposite);
		
		Composite buttonComposite = new Composite(selectColumnComposite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(2, false));
		GridData gd_buttonComposite = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_buttonComposite.heightHint = 36;
		gd_buttonComposite.widthHint = 139;
		buttonComposite.setLayoutData(gd_buttonComposite);
		
		 addButton = new Button(buttonComposite, SWT.NONE);
		 addButton.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.ADD_ICON));
		 deletButton = new Button(buttonComposite, SWT.NONE);
		 deletButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		 deletButton.setImage(new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + Messages.DELETE_ICON));
		
		 tableViewer = new TableViewer(selectColumnComposite, SWT.BORDER | SWT.FULL_SELECTION|SWT.MULTI);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setVisible(true);
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite.heightHint = 195;
		gd_composite.widthHint = 184;
		composite.setLayoutData(gd_composite);
		new Label(composite, SWT.NONE);
		if(Constants.TRANSFORM.equalsIgnoreCase(component.getComponentName()))
		{
		lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setText("Switch to");
		
		switchToClassButton = new Button(composite, SWT.RADIO);
		switchToClassButton.setText("Class");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		switchToExpressionButton = new Button(composite, SWT.RADIO);
		switchToExpressionButton.setText("Expression");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		switchToExpressionButton.setSelection(true);
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
		lblExpression.setText("Expression Id");
		
		expressionIdTextBox = new Text(composite, SWT.BORDER);
		expressionIdTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 4);
		gd_composite_1.heightHint = 80;
		gd_composite_1.widthHint = 90;
		composite_1.setLayoutData(gd_composite_1);
		
		composite_2 = new Composite(composite_1, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gd_composite_2.heightHint = 75;
		gd_composite_2.widthHint = 80;
		composite_2.setLayoutData(gd_composite_2);
		composite_2.setLayout(new GridLayout(1, false));
		browseButton= new Button(composite_2, SWT.NONE);
		GridData gd_browseButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 0, 0);
		if(OSValidator.isMac()){
			gd_browseButton.widthHint = 40;
		}else{
			gd_browseButton.widthHint = 28;
		}
		gd_browseButton.heightHint = 25;
		browseButton.setLayoutData(gd_browseButton);
		browseButton.setText("...");
		browseButton.setToolTipText(Messages.EXPRESSION_COMPOSITE_BROWSE_BUTTON_TOOL_TIP);
		new Label(composite_2, SWT.NONE);
		btnIsParam= new Button(composite_2, SWT.CHECK);
		btnIsParam.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		GridData gd_btnIsParam = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
		gd_btnIsParam.widthHint = 75;
		gd_btnIsParam.heightHint = 19;
		btnIsParam.setLayoutData(gd_btnIsParam);
		btnIsParam.setText(Messages.IS_PARAM);
		btnIsParam.setSelection(mappingSheetRow.isWholeOperationParameter());
		browseButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				createExpressionEditorData();

				LaunchExpressionEditor launchExpressionEditor = new LaunchExpressionEditor();
				launchExpressionEditor.launchExpressionEditor(mappingSheetRow.getExpressionEditorData(),getInputSchema(component),mappingSheetRow.getOperationID());
				expressionTextBox.setText(mappingSheetRow.getExpressionEditorData().getExpression());
			}

			
		});
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_lblNewLabel.minimumWidth = 50;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Expression");
		
		expressionTextBox = new Text(composite, SWT.BORDER);
		expressionTextBox.setEditable(false);
		expressionTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
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
		parameterTextBox.setEnabled(mappingSheetRow.isWholeOperationParameter());
		
		parameterTextBox.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Text text=(Text)e.widget;
				mappingSheetRow.setWholeOperationParameterValue(text.getText());
			}
		});
		
		if (mappingSheetRow.getWholeOperationParameterValue() != null)
		parameterTextBox.setText(mappingSheetRow.getWholeOperationParameterValue());	
		Composite composite_4 = new Composite(this, SWT.NONE);
		composite_4.setLayout(new GridLayout(1, false));
		GridData gd_composite_4 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_composite_4.widthHint = 159;
		composite_4.setLayoutData(gd_composite_4);
		new Label(composite_4, SWT.NONE);
		new Label(composite_4, SWT.NONE);
		new Label(composite_4, SWT.NONE);
		
		Label lblOutputField = new Label(composite_4, SWT.NONE);
		GridData gd_lblOutputField = new GridData(SWT.CENTER, SWT.BOTTOM, false, false, 1, 1);
		gd_lblOutputField.heightHint = 29;
		lblOutputField.setLayoutData(gd_lblOutputField);
		lblOutputField.setText("Output Field");
		
		outputFieldTextBox = new Text(composite_4, SWT.BORDER);
		GridData gd_outputFieldTextBox = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_outputFieldTextBox.widthHint = 105;
		gd_outputFieldTextBox.heightHint = 20;
		outputFieldTextBox.setLayoutData(gd_outputFieldTextBox);
		if(!mappingSheetRow.getOutputList().isEmpty())
		{
			if(StringUtils.isNotBlank(mappingSheetRow.getOutputList().get(0).getPropertyname()))
			outputFieldTextBox.setText(mappingSheetRow.getOutputList().get(0).getPropertyname());
		}	
		setAllWidgetsOnIsParamButton(btnIsParam);
		disabledWidgetsifWholeExpressionIsParameter(btnIsParam,mappingSheetRow.isWholeOperationParameter());
		if(mappingSheetRow.getExpressionEditorData()!=null && StringUtils.isNotBlank(mappingSheetRow.getExpressionEditorData().getExpression()))
		{
			expressionTextBox.setText(mappingSheetRow.getExpressionEditorData().getExpression());
		}	
	}
	
	

	private void disabledWidgetsifWholeExpressionIsParameter(Button isParam,boolean isWholeOperationParameter) 
	{
		    if(isWholeOperationParameter)
		    {	
		    TableViewer tableViewer=(TableViewer)isParam.getData("inputFieldTable");
			Button addButton=(Button) isParam.getData("addButton");
			Button deleteButton=(Button) isParam.getData("deleteButton");
			Text expressionIdTextBox=(Text) isParam.getData("expressionIdTextBox");
			Button browseButton=(Button) isParam.getData("expressionEditorButton");
			Text outputFieldTextBox=(Text) isParam.getData("outputFieldTextBox");
			tableViewer.getTable().setEnabled(false);
			addButton.setEnabled(false);
			deleteButton.setEnabled(false);
			expressionIdTextBox.setEnabled(false);
			browseButton.setEnabled(false);
			outputFieldTextBox.setEnabled(false);
		    }
	}
	private void setAllWidgetsOnIsParamButton(Button isParam) {
		isParam.setData("inputFieldTable",tableViewer);
		isParam.setData("addButton",addButton);
		isParam.setData("deleteButton",deletButton);
		isParam.setData("expressionIdTextBox",expressionIdTextBox);
		isParam.setData("expressionEditorButton",browseButton);
		isParam.setData("outputFieldTextBox",outputFieldTextBox);
		isParam.setData("parameterTextBox",parameterTextBox);
		isParam.setData("expressionTextBox",expressionTextBox);
	}
     
	public TableViewer getTableViewer() {
		return tableViewer;
	}

	
	public Button getSwitchToClassButton() {
		return switchToClassButton;
	}

	public Text getExpressionIdTextBox() {
		return expressionIdTextBox;
	}

	public Text getExressionTextBox() {
		return expressionTextBox;
	}

	public Text getParameterTextBox() {
		return parameterTextBox;
	}

	public Text getOutputFieldTextBox() {
		return outputFieldTextBox;
	}

	public Button getAddButton() {
		return addButton;
	}
    
	public Button getIsParamButton() {
		return btnIsParam;
	}
	public Button getDeletButton() {
		return deletButton;
	}
    
	public Button getSwitchToExpressionButton() {
		return switchToExpressionButton;
	}



	@Override
	protected void checkSubclass() {
	}

	private List<FixedWidthGridRow> getInputSchema(Component component) {
		List<FixedWidthGridRow> fixedWidthGridRows=new ArrayList<>();
		for(Link link:component.getTargetConnections()){
			ComponentsOutputSchema componentsOutputSchema=SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
			if(componentsOutputSchema!=null && componentsOutputSchema.getFixedWidthGridRowsOutputFields()!=null){
				fixedWidthGridRows = componentsOutputSchema.getFixedWidthGridRowsOutputFields();
			}
			break;
		}
		return fixedWidthGridRows;
	}

	/**
	 * Creates data-structure for expression-editor.
	 * 
	 * @return
	 */
	public ExpressionEditorData createExpressionEditorData() {
		if (!mappingSheetRow.getInputFields().isEmpty()) {
			List<String> inputFieldNames = new ArrayList<>();
			for (FilterProperties filterProperties : mappingSheetRow.getInputFields()) {
				inputFieldNames.add(filterProperties.getPropertyname());
			}
			mappingSheetRow.getExpressionEditorData().getfieldsUsedInExpression().clear();
			mappingSheetRow.getExpressionEditorData().getSelectedInputFieldsForExpression().clear();
			mappingSheetRow.getExpressionEditorData().getSelectedInputFieldsForExpression()
					.putAll(FieldDataTypeMap.INSTANCE.createFieldDataTypeMap(inputFieldNames, getInputSchema(component)));
			mappingSheetRow.getExpressionEditorData().getfieldsUsedInExpression().addAll(inputFieldNames);
			
		}
		else{
		mappingSheetRow.getExpressionEditorData().getSelectedInputFieldsForExpression().clear();
		mappingSheetRow.getExpressionEditorData().getfieldsUsedInExpression().clear();
		}
		
		return mappingSheetRow.getExpressionEditorData();
	}
}

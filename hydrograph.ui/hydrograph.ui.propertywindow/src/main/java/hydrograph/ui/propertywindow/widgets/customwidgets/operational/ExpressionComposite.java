package hydrograph.ui.propertywindow.widgets.customwidgets.operational;



import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FilterProperties;
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
	private Text expressionIdTextBox;
	private Text expressionTextBox;
	private Text parameterTextBox;
	private Text outputFieldTextBox;
	private Table table;
	private TableViewer tableViewer;
	private Button addButton,deletButton,browseButton;
    private Button btnIsParam;
	public ExpressionComposite(Composite parent, int style,final MappingSheetRow mappingSheetRow, final Component component) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		
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
		gd_composite.heightHint = 181;
		gd_composite.widthHint = 184;
		composite.setLayoutData(gd_composite);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblExpression = new Label(composite, SWT.NONE);
		GridData gd_lblExpression = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_lblExpression.heightHint = 15;
		lblExpression.setLayoutData(gd_lblExpression);
		lblExpression.setText("Expression Id");
		
		expressionIdTextBox = new Text(composite, SWT.BORDER);
		expressionIdTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setText("Expression");
		
		expressionTextBox = new Text(composite, SWT.BORDER);
		expressionTextBox.setEditable(false);
		expressionTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		browseButton= new Button(composite, SWT.NONE);
		GridData gd_button = new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1);
		gd_button.widthHint = 35;
		browseButton.setLayoutData(gd_button);
		browseButton.setText("...");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		browseButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!mappingSheetRow.getInputFields().isEmpty()) {
					List<String> inputFieldNames = new ArrayList<>();
					for (FilterProperties filterProperties : mappingSheetRow.getInputFields()) {
						inputFieldNames.add(filterProperties.getPropertyname());
					}
					mappingSheetRow.getExpressionEditorData().getSelectedInputFieldsForExpression().clear();
					mappingSheetRow.getExpressionEditorData().getSelectedInputFieldsForExpression()
							.putAll(FieldDataTypeMap.INSTANCE.createFieldDataTypeMap(inputFieldNames, getInputSchema(component)));
				}
				LaunchExpressionEditor launchExpressionEditor = new LaunchExpressionEditor();
				launchExpressionEditor.launchExpressionEditor(mappingSheetRow.getExpressionEditorData());
			}
		});
		
		Label lblParameter = new Label(composite, SWT.NONE);
		lblParameter.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
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
		btnIsParam= new Button(composite, SWT.CHECK);
		btnIsParam.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		btnIsParam.setText("Is Param");
		btnIsParam.setSelection(mappingSheetRow.isWholeOperationParameter());
		Composite composite_4 = new Composite(this, SWT.NONE);
		composite_4.setLayout(new GridLayout(1, false));
		GridData gd_composite_4 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_composite_4.widthHint = 115;
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
		if(mappingSheetRow.getExpressionEditorData()!=null)
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
	}
     
	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public Text getText() {
		return expressionIdTextBox;
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

	@Override
	protected void checkSubclass() {
	}

	private ComponentsOutputSchema getInputSchema(Component component) {
		ComponentsOutputSchema componentsOutputSchema=null;
		for(Link link:component.getTargetConnections()){
			componentsOutputSchema=SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
		}
		return componentsOutputSchema;
	}

}

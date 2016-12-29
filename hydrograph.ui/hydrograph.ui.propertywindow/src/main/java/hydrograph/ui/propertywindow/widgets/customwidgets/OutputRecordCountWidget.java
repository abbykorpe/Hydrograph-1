/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package hydrograph.ui.propertywindow.widgets.customwidgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.common.util.ParameterUtil;
import hydrograph.ui.common.util.TransformMappingFeatureUtility;
import hydrograph.ui.datastructure.expression.ExpressionEditorData;
import hydrograph.ui.datastructure.property.BasicSchemaGridRow;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.NameValueProperty;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.datastructure.property.mapping.ErrorObject;
import hydrograph.ui.datastructure.property.mapping.InputField;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.datastructure.property.mapping.TransformMapping;
import hydrograph.ui.expression.editor.launcher.LaunchExpressionEditor;
import hydrograph.ui.expression.editor.util.ExpressionEditorUtil;
import hydrograph.ui.expression.editor.util.FieldDataTypeMap;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.schema.propagation.helper.SchemaPropagationHelper;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.OperationClassConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.operational.TransformDialog;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.AbstractELTWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultTextBox;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.utility.SchemaSyncUtility;
import hydrograph.ui.propertywindow.widgets.utility.SubjobUtility;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

/**
 * 
 * output record count widget for normalize component 
 * 
 * @author Bitwise
 *
 */
public class OutputRecordCountWidget extends AbstractWidget{

	
	private Button expressionRadioButton,operationRadioButton,editButton;
	private Text outputRecordCoundText;
	private LinkedHashMap<String, Object> property = new LinkedHashMap<>();
	private String propertyName;
	private List<FilterProperties> outputList;
	private TransformMapping transformMapping;
	private List<AbstractWidget> widgets;
	private ControlDecoration expressionValidateDecorator;
	public OutputRecordCountWidget(ComponentConfigrationProperty componentConfigrationProperty,
			ComponentMiscellaneousProperties componentMiscellaneousProperties,
			PropertyDialogButtonBar propertyDialogButtonBar) {
		super(componentConfigrationProperty, componentMiscellaneousProperties,
				propertyDialogButtonBar);
		transformMapping=(TransformMapping)componentConfigrationProperty.getPropertyValue();
		if(transformMapping==null)
		{
		transformMapping=new TransformMapping();
		transformMapping.setExpression(true);
		ExpressionEditorData expressionEditorData=new ExpressionEditorData("", "");
		transformMapping.setExpressionEditorData(expressionEditorData);
		}
		propertyName=componentConfigrationProperty.getPropertyName();
		outputList=new ArrayList<>();
	}
	
	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget container) {
		ELTDefaultSubgroupComposite eltDefaultSubgroupComposite=new ELTDefaultSubgroupComposite(container.getContainerControl());
        eltDefaultSubgroupComposite.createContainerWidget();
		eltDefaultSubgroupComposite.numberOfBasicWidgets(1);
		Composite radioButtonsComposite= createRadioButtonComposite(eltDefaultSubgroupComposite);
		attachRadioButtonToComposite(radioButtonsComposite);
		Composite labelTexBoxAndButtonComposite=createLabelTextBoxAndButtonComposite(eltDefaultSubgroupComposite);
		createLabelTextBoxAndButton(labelTexBoxAndButtonComposite);
		Composite compositeForNormalizeEditButtton=createcompositeForNormalizeEditButtton(eltDefaultSubgroupComposite);
		createTransformEditButtonAndLabel(compositeForNormalizeEditButtton);
	}

	private void createTransformEditButtonAndLabel(Composite compositeForNormalizeEditButtton) {
		OperationClassConfig operationClassConfig = (OperationClassConfig) widgetConfig;
		ELTDefaultLable defaultLable1 = new ELTDefaultLable(operationClassConfig.getComponentDisplayName()+" \n ");
		defaultLable1.attachWidget(compositeForNormalizeEditButtton);
		setPropertyHelpWidget((Control) defaultLable1.getSWTWidgetControl());
		
		 TransformMapping transformMappingPopulatedFromTooTipAction=
       		 (TransformMapping) getComponent().getTooltipInformation().get("operation").getPropertyValue();
		 if(transformMappingPopulatedFromTooTipAction!=null)
		 {	 
		 transformMapping.setAddPassThroughFields(transformMappingPopulatedFromTooTipAction.isAddPassThroughFields());
		 }
		ELTDefaultButton eltDefaultButton = new ELTDefaultButton(Constants.EDIT).grabExcessHorizontalSpace(false);
		if(OSValidator.isMac()){
			eltDefaultButton.buttonWidth(120);
		}
		eltDefaultButton.attachWidget(compositeForNormalizeEditButtton);
		if(getComponent().isContinuousSchemaPropogationAllow())
		getPropagatedSchema();
		if(transformMapping.isAddPassThroughFields())
		{	
		addPassThroughFields();
		}
		SchemaSyncUtility.INSTANCE.unionFilter(transformMapping.getOutputFieldList(), outputList);
		populateMappingOutputFieldIfTargetXmlImported();
		addSelectionListenerToEditButton(eltDefaultButton);
		propagateOuputFieldsToSchemaTabFromTransformWidget();
	}

	private void addSelectionListenerToEditButton(ELTDefaultButton eltDefaultButton) {
		((Button) eltDefaultButton.getSWTWidgetControl()).addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
                
				TransformMapping oldATMappings = (TransformMapping) transformMapping.clone();
				
				TransformDialog transformDialog=new TransformDialog(Display.getCurrent().getActiveShell(),getComponent(),widgetConfig,transformMapping);
				int returncode=transformDialog.open();
				outputList.clear();
                outputList = transformDialog.getFinalSortedList();
				if(transformDialog.isCancelPressed()||returncode==1)
				{
					transformMapping=oldATMappings;
				}
			 	
				if(transformDialog.isOkPressed())
               	{
					propagateOuputFieldsToSchemaTabFromTransformWidget();	
					SchemaSyncUtility.INSTANCE.autoSyncSchema(getSchemaForInternalPropagation(), getComponent(), widgets);
					showHideErrorSymbol(widgets);
               	}	

				if(!oldATMappings.equals(transformDialog.getATMapping()) && returncode==0)
				{
					propertyDialogButtonBar.enableApplyButton(true);
					
				}
				if(transformDialog.isNoButtonPressed())
				{
					propertyDialog.pressCancel();
				}	
				if(transformDialog.isYesButtonPressed()){
					propertyDialog.pressOK();	
				}

			}
		});
	}

	private Composite createcompositeForNormalizeEditButtton(ELTDefaultSubgroupComposite eltDefaultSubgroupComposite) {
		Composite composite=new Composite(eltDefaultSubgroupComposite.getContainerControl(),SWT.NONE);
		setLayoutAndDataToComposite(composite,3);
		return composite;
	}

	private Composite createLabelTextBoxAndButtonComposite(ELTDefaultSubgroupComposite eltDefaultSubgroupComposite) {
		Composite composite=new Composite(eltDefaultSubgroupComposite.getContainerControl(), SWT.NONE);
		setLayoutAndDataToComposite(composite,3);
		return composite;
		
	}

	private void setLayoutAndDataToComposite(Composite composite,int noOfColumn) {
		GridLayout gridLayout=new GridLayout(noOfColumn,false);
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 1;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing=10;
		composite.setLayout(gridLayout);
		GridData radioButtonCompositeGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		if(OSValidator.isMac()){
			radioButtonCompositeGridData.horizontalIndent = -3;
		}
		composite.setLayoutData(radioButtonCompositeGridData);
	}

	private void createLabelTextBoxAndButton(Composite textBobLabelButtonComposite) {
		AbstractELTWidget outputRecordCountLabel1=new ELTDefaultLable(Constants.OUTPUT_RECORD_COUNT);
		outputRecordCountLabel1.attachWidget(textBobLabelButtonComposite);
		Composite composite=new Composite(textBobLabelButtonComposite, SWT.NONE);
		setLayoutAndDataToComposite(composite,1);
		ELTDefaultTextBox outputRecordCountTextBox=new ELTDefaultTextBox();
		outputRecordCountTextBox.attachWidget(composite);
		outputRecordCoundText=(Text)outputRecordCountTextBox.getSWTWidgetControl();
		expressionValidateDecorator = WidgetUtility.addDecorator(outputRecordCoundText,"Invalid Expression");
		expressionValidateDecorator.setMarginWidth(3);
		addModifyListenerToRecordCountTextBox();
		if(transformMapping.getExpressionEditorData()!=null)
		{
			outputRecordCoundText.setText(transformMapping.getExpressionEditorData().getExpression());
			showHideDecorator();	
		}	
		AbstractELTWidget outputRecordCountButton=new ELTDefaultButton(Constants.EDIT);
		outputRecordCountButton.attachWidget(textBobLabelButtonComposite);
		editButton=(Button)outputRecordCountButton.getSWTWidgetControl();
	    intializeEnableOrDisableStateOfWidgetsBasedOnExpressionOrOperationSelected();
	    addListenerOnEditButtonToOpenExpressionEditor();
	}

	private void addModifyListenerToRecordCountTextBox() {
		outputRecordCoundText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
			 Text textBox=(Text)e.widget;
			 transformMapping.getExpressionEditorData().setExpression(textBox.getText());
            propertyDialogButtonBar.enableApplyButton(true);				
			}
		});
		
	}

	private void addListenerOnEditButtonToOpenExpressionEditor() {
		
		editButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) 
			{
				intializeExpresionEditorData();
				LaunchExpressionEditor launchExpressionEditor=new LaunchExpressionEditor();
				launchExpressionEditor.launchExpressionEditor(transformMapping.getExpressionEditorData(), 
						getInputSchema(getComponent()), getComponent().getComponentName());
				outputRecordCoundText.setText(transformMapping.getExpressionEditorData().getExpression());
				if(transformMapping.getExpressionEditorData().isValid())
				expressionValidateDecorator.hide();
				else
				expressionValidateDecorator.show();	
			}
		});
    		
	}
	
	private List<FixedWidthGridRow> getInputSchema(Component component) {
		
		List<FixedWidthGridRow> fixedWidthSchemaGridRows=new ArrayList<>();
		for(Link link:component.getTargetConnections()){
			Schema previousComponentSchema=SubjobUtility.INSTANCE.getSchemaFromPreviousComponentSchema(component,link);
			
			if(previousComponentSchema!=null &&!previousComponentSchema.getGridRow().isEmpty())
			{
				fixedWidthSchemaGridRows=SchemaSyncUtility.INSTANCE.
						convertGridRowsSchemaToFixedSchemaGridRows(previousComponentSchema.getGridRow());
			}
			break;
		}
		
		return fixedWidthSchemaGridRows;
	}
	
	private void intializeExpresionEditorData() {
		ExpressionEditorData expressionEditorData=transformMapping.getExpressionEditorData();
		List<String> inputFieldNames = new ArrayList<>();
		for(InputField inputField:transformMapping.getInputFields())
		{
			inputFieldNames.add(inputField.getFieldName());
		}
		expressionEditorData.getfieldsUsedInExpression().clear();
		expressionEditorData.getfieldsUsedInExpression().addAll(inputFieldNames);
		expressionEditorData.getSelectedInputFieldsForExpression().clear();
		expressionEditorData.setComponentName(getComponent().getComponentName());
		expressionEditorData.getSelectedInputFieldsForExpression().
		putAll(FieldDataTypeMap.INSTANCE.createFieldDataTypeMap(inputFieldNames, getInputSchema(getComponent())));
	}
	private void intializeEnableOrDisableStateOfWidgetsBasedOnExpressionOrOperationSelected() {
		if(!transformMapping.isExpression())
		{
        	editButton.setEnabled(false);
        	outputRecordCoundText.setEnabled(false);
		}	
	}

	private void attachRadioButtonToComposite(Composite radioButtonsComposite) {
      expressionRadioButton=new Button(radioButtonsComposite, SWT.RADIO);
      if(OSValidator.isMac()){
			expressionRadioButton.setText(Messages.MAC_EXPRESSION_EDITIOR_LABEL);
		}else {
		    expressionRadioButton.setText(Messages.WINDOWS_EXPRESSION_EDITIOR_LABEL);
		}
      operationRadioButton = new Button(radioButtonsComposite, SWT.RADIO);
	  operationRadioButton.setText(Messages.OPERATION_CALSS_LABEL);
	  addSelectionListenerToExpressionRadioButton(expressionRadioButton);
	  addSelectionListenerToOperationRadioButton(operationRadioButton);
	  if(transformMapping.isExpression())
	  {	  
	  expressionRadioButton.setSelection(true);
	  }
	  else
	  {	  
	  operationRadioButton.setSelection(true);	  
	  }
	}

	private void addSelectionListenerToExpressionRadioButton(Button expressionRadioButton) {
		expressionRadioButton.addMouseListener(new MouseAdapter() {

			public void mouseUp(MouseEvent e) {
				outputRecordCoundText.setEnabled(true);
				editButton.setEnabled(true);
				transformMapping.setExpression(true);
				propertyDialogButtonBar.enableApplyButton(true);
				if(!transformMapping.getExpressionEditorData().isValid())
				{
					expressionValidateDecorator.show();
				}
				removeOperationFieldFromOutputList();
				addExpressionOutputFieldToOuterListOfMappingWindow();
				setSchemaUpdated(false);
				outputList.clear();
				SchemaSyncUtility.INSTANCE.unionFilter(transformMapping.getOutputFieldList(), 
						outputList);
				propagateOuputFieldsToSchemaTabFromTransformWidget();
	 		}
		});}
	private void addExpressionOutputFieldToOuterListOfMappingWindow() {
        	for(MappingSheetRow mappingSheetRow:transformMapping.getMappingSheetRows())	
        	{
        		if(transformMapping.isExpression()&&mappingSheetRow.isExpression()&&mappingSheetRow.isActive())
        		{
        			transformMapping.getOutputFieldList().addAll(mappingSheetRow.getOutputList());
        		}	
        	}
	}
	private void addOperationOutputFieldToOuterListOfMappingWindow() {
    	for(MappingSheetRow mappingSheetRow:transformMapping.getMappingSheetRows())	
    	{
    		if(!transformMapping.isExpression()&&!mappingSheetRow.isExpression()&&mappingSheetRow.isActive())
    		{
    			transformMapping.getOutputFieldList().addAll(mappingSheetRow.getOutputList());
    		}	
    	}
}
	private void addSelectionListenerToOperationRadioButton(Button operationRadioButton) {
		operationRadioButton.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				outputRecordCoundText.setEnabled(false);
				editButton.setEnabled(false);
				transformMapping.setExpression(false);
				propertyDialogButtonBar.enableApplyButton(true);
				expressionValidateDecorator.hide();
				removeExpressionFieldFromOutputList();
				addOperationOutputFieldToOuterListOfMappingWindow();
				setSchemaUpdated(false);
				outputList.clear();
				SchemaSyncUtility.INSTANCE.unionFilter(transformMapping.getOutputFieldList(), 
						outputList);
				propagateOuputFieldsToSchemaTabFromTransformWidget();
				
			}

			
		});
	}
	private void removeExpressionFieldFromOutputList() {
		for(MappingSheetRow mappingSheetRow:transformMapping.getMappingSheetRows())
		{
			if(!transformMapping.isExpression()&&mappingSheetRow.isExpression()&&mappingSheetRow.isActive())
			transformMapping.getOutputFieldList().removeAll(mappingSheetRow.getOutputList());	
		}
	}
	
	private void removeOperationFieldFromOutputList() {
		for(MappingSheetRow mappingSheetRow:transformMapping.getMappingSheetRows())
		{
			if(transformMapping.isExpression()&&!mappingSheetRow.isExpression()&&mappingSheetRow.isActive())
			transformMapping.getOutputFieldList().removeAll(mappingSheetRow.getOutputList());	
		}
	}
	private void addPassThroughFields()
	{
		List<InputField> inputFieldList=transformMapping.getInputFields();	
		for(InputField inputField:inputFieldList)
		{
			NameValueProperty nameValueProperty=new NameValueProperty();
			nameValueProperty.setPropertyName(inputField.getFieldName());
			nameValueProperty.setPropertyValue(inputField.getFieldName());
			nameValueProperty.getFilterProperty().setPropertyname(inputField.getFieldName());
			
			if(!transformMapping.getMapAndPassthroughField().contains(nameValueProperty))
			{
			transformMapping.getOutputFieldList().add(nameValueProperty.getFilterProperty());	
			transformMapping.getMapAndPassthroughField().add(nameValueProperty);
			}
		
	     }
	   transformMapping.setAddPassThroughFields(false);
	}
	
	private void populateMappingOutputFieldIfTargetXmlImported() {
		if(!transformMapping.getMappingSheetRows().isEmpty())
		{
			List<MappingSheetRow> activeMappingSheetRow=TransformMappingFeatureUtility.INSTANCE.
					getActiveMappingSheetRow(transformMapping.getMappingSheetRows());
			if(activeMappingSheetRow.size()==transformMapping.getMappingSheetRows().size())
			{
				for(MappingSheetRow mappingSheetRow:transformMapping.getMappingSheetRows())
			 	{  
					transformMapping.getOutputFieldList().addAll(mappingSheetRow.getOutputList());
			 	}
				if(!transformMapping.getMapAndPassthroughField().isEmpty()&&
			 			transformMapping.getMapAndPassthroughField().get(0).getFilterProperty()==null)
			 	{
			 		backwardJobComapatabilityCode();	
			 	}
				for(NameValueProperty nameValueProperty:transformMapping.getMapAndPassthroughField())
			 	{
			 		transformMapping.getOutputFieldList().add(nameValueProperty.getFilterProperty());
			 	}	
			 	List<FilterProperties> finalSortedList=SchemaSyncUtility.INSTANCE.
			 	sortOutputFieldToMatchSchemaSequence(convertSchemaToFilterProperty(), 
			 			transformMapping);
			 	transformMapping.getOutputFieldList().clear();
			 	transformMapping.getOutputFieldList().addAll(finalSortedList);
				
			}	
				
		}
		else if(!transformMapping.getMapAndPassthroughField().isEmpty()&&transformMapping.getOutputFieldList().isEmpty())
		{
			if(transformMapping.getMapAndPassthroughField().get(0).getFilterProperty()==null)
			{	
			backwardJobComapatabilityCode();	
			}
			for(NameValueProperty nameValueProperty:transformMapping.getMapAndPassthroughField())
		 	{
		 		transformMapping.getOutputFieldList().add(nameValueProperty.getFilterProperty());
		 	}	
			List<FilterProperties> finalSortedList=SchemaSyncUtility.INSTANCE.
				 	sortOutputFieldToMatchSchemaSequence(convertSchemaToFilterProperty(), 
				 			transformMapping);
				 	transformMapping.getOutputFieldList().clear();
				 	transformMapping.getOutputFieldList().addAll(finalSortedList);
		}
	}
	private List<FilterProperties> convertSchemaToFilterProperty(){
		List<FilterProperties> outputFileds = new ArrayList<>();
		Schema schema = (Schema) getComponent().getProperties().get(Constants.SCHEMA_PROPERTY_NAME);
		    if(schema==null)
		    {	
			 return outputFileds;  
		    }
			 for (GridRow gridRow : schema.getGridRow()) {
				FilterProperties filterProperty = new FilterProperties();
				filterProperty.setPropertyname(gridRow.getFieldName());
				outputFileds.add(filterProperty);
			}
		return outputFileds;
	}
	
	private void backwardJobComapatabilityCode()
    {
    		List<NameValueProperty> tempNameValuePropertyList=new ArrayList<>();
    		for(NameValueProperty nameValueProperty:transformMapping.getMapAndPassthroughField())
    		{
    			NameValueProperty newNameValueProperty=new NameValueProperty();
    			newNameValueProperty.setPropertyName(nameValueProperty.getPropertyName());
    			newNameValueProperty.setPropertyValue(nameValueProperty.getPropertyValue());
    			newNameValueProperty.getFilterProperty().setPropertyname(nameValueProperty.getPropertyValue());
    			tempNameValuePropertyList.add(newNameValueProperty);
    			transformMapping.getOutputFieldList().add(newNameValueProperty.getFilterProperty());
    		}	
    		transformMapping.getMapAndPassthroughField().clear();
    		transformMapping.getMapAndPassthroughField().addAll(tempNameValuePropertyList);
    		tempNameValuePropertyList.clear();
    }	
	
	private void propagateOuputFieldsToSchemaTabFromTransformWidget() {

		if (transformMapping == null || transformMapping.getMappingSheetRows() == null)
			return;


		getSchemaForInternalPropagation().getGridRow().clear();
		getOperationFieldList().clear();

		List<String> finalPassThroughFields=new LinkedList<String>();
		Map<String, String> finalMapFields=new LinkedHashMap<String, String>();

		List<FilterProperties> operationFieldList=new LinkedList<FilterProperties>();

		for (MappingSheetRow mappingSheetRow : transformMapping.getMappingSheetRows()) {
			List<FilterProperties> operationFields = getOpeartionFields(mappingSheetRow);

			operationFieldList.addAll(operationFields);
			addOperationFieldsToSchema(operationFields);
		}
		List<String> passThroughFields = getPassThroughFields(transformMapping.getMapAndPassthroughField());
		Map<String, String> mapFields = getMapFields(transformMapping.getMapAndPassthroughField());
		finalMapFields.putAll(mapFields);
		finalPassThroughFields.addAll(passThroughFields);

		addPassthroughFieldsToSchema(passThroughFields);
		addMapFieldsToSchema(mapFields);

		addPassthroughFieldsAndMappingFieldsToComponentOuputSchema(finalMapFields, finalPassThroughFields);
		for(FilterProperties f:operationFieldList)
		{	
			getOperationFieldList().add(f.getPropertyname());
			
		}
		if(!outputList.isEmpty())
		{
			
		 List<GridRow> sortedList=new ArrayList<>();
		
		 for(int i=0;i<outputList.size();i++)
		 {
			 GridRow gridRowTemp = null;
			 for(GridRow gridRow:getSchemaForInternalPropagation().getGridRow())
			 {
				 if(StringUtils.equals(gridRow.getFieldName(), outputList.get(i).getPropertyname()))
				 {
					 gridRowTemp=gridRow;
					 break;
				 }
				 
			 }
			 if(gridRowTemp!=null)
			 sortedList.add(gridRowTemp);
		 } 
		 getSchemaForInternalPropagation().getGridRow().clear();
		 getSchemaForInternalPropagation().getGridRow().addAll(sortedList);
		 sortedList.clear();
		}
		else
		getSchemaForInternalPropagation().getGridRow().clear();	
	}




	private void addPassthroughFieldsAndMappingFieldsToComponentOuputSchema(Map<String, String> mapFields,
			List<String> passThroughFields) {
		ComponentsOutputSchema componentsOutputSchema = null;
		Map<String, ComponentsOutputSchema> schemaMap = (Map<String, ComponentsOutputSchema>) getComponent()
				.getProperties().get(Constants.SCHEMA_TO_PROPAGATE);
		if (schemaMap != null && schemaMap.get(Constants.FIXED_OUTSOCKET_ID) != null)
		{	
			componentsOutputSchema = schemaMap.get(Constants.FIXED_OUTSOCKET_ID);
		}
		else {
			componentsOutputSchema = new ComponentsOutputSchema();
			schemaMap = new LinkedHashMap<>();
			schemaMap.put(Constants.FIXED_OUTSOCKET_ID, componentsOutputSchema);
		}
		getComponent().getProperties().put(Constants.SCHEMA_TO_PROPAGATE, schemaMap);

		componentsOutputSchema.getPassthroughFields().clear();
		componentsOutputSchema.getMapFields().clear();
		componentsOutputSchema.getPassthroughFields().addAll(passThroughFields);
		componentsOutputSchema.getMapFields().putAll(mapFields);
	}

	// PLEASE DO NOT REMOVE THE CODE
	private GridRow getCurrentSchemaField(String fieldName) {
		Component component = getComponent();
		Schema schema = (Schema) component.getProperties().get(Constants.SCHEMA_PROPERTY_NAME);
		if (schema != null) {
			for (GridRow gridRow : schema.getGridRow()) {
				if(gridRow.getFieldName().equals(fieldName))
					return gridRow;
			}
		}
		return null;
	}
	/**
	 * Return gridrow object if schema is present on source component else return null.
	 * @param fieldName
	 * @return 
	 */
	private GridRow getFieldSchema(String fieldName) {
		List<GridRow> schemaGridRows = getInputFieldSchema();
		for (GridRow schemaGridRow : schemaGridRows) {
			if (schemaGridRow.getFieldName().equals(fieldName)) {
				return schemaGridRow;
			}
		}
		return null;
	}
	/**
	 * Get Input schema from target link.
	 * @return
	 */
	private List<GridRow> getInputFieldSchema() {
		ComponentsOutputSchema outputSchema = null;
		List<GridRow> schemaGridRows = new LinkedList<>();
		for (Link link : getComponent().getTargetConnections()) {
			outputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
			if (outputSchema != null)
				for (GridRow row : outputSchema.getSchemaGridOutputFields(null)) {
					schemaGridRows.add(row);
				}
		}
		return schemaGridRows;
	}

	/**
	 * Add Map field to internal schema object.
	 * @param mapFields
	 */
	private void addMapFieldsToSchema(Map<String, String> mapFields) {
		BasicSchemaGridRow tempSchemaGridRow = null;
		Schema schema = getSchemaForInternalPropagation();
		List<String> currentFieldsInProppogatedSchemaObject = new LinkedList<>();
		for (GridRow gridRow : schema.getGridRow()) {
			currentFieldsInProppogatedSchemaObject.add(gridRow.getFieldName());
		}

	  for (Map.Entry<String,String> entry: mapFields.entrySet()) {
			tempSchemaGridRow = (BasicSchemaGridRow) getFieldSchema(entry.getValue());
			BasicSchemaGridRow schemaGridRow=null ;
			if (tempSchemaGridRow != null) {
				schemaGridRow= (BasicSchemaGridRow) tempSchemaGridRow.copy();
				schemaGridRow.setFieldName(entry.getKey());
			}
			else{
				schemaGridRow = SchemaPropagationHelper.INSTANCE.createSchemaGridRow(entry.getKey());
			}
				if (!currentFieldsInProppogatedSchemaObject.contains(entry.getKey()) && !schema.getGridRow().contains(schemaGridRow)) {
							schema.getGridRow().add(schemaGridRow);
				} else {
					for (int index = 0; index < schema.getGridRow().size(); index++) {
						if (schema.getGridRow().get(index).getFieldName().equals(entry.getKey())) {
							schema.getGridRow().set(index, schemaGridRow);
						}
					}
				}
			 
			
		}

	}
	/**
	 * Add Pass through field to schema 
	 * @param passThroughFields
	 */
	private void addPassthroughFieldsToSchema(List<String> passThroughFields) {
		Schema schema = getSchemaForInternalPropagation();
		List<String> currentFieldsInProppogatedSchemaObject = new LinkedList<>();
		for (GridRow gridRow : schema.getGridRow()) {
			currentFieldsInProppogatedSchemaObject.add(gridRow.getFieldName());
		}

		for (String passThroughField : passThroughFields) {
			GridRow schemaGridRow= getFieldSchema(passThroughField);
			if(schemaGridRow!=null){
				BasicSchemaGridRow tempSchemaGrid =(BasicSchemaGridRow) schemaGridRow.copy();

				if (!currentFieldsInProppogatedSchemaObject.contains(passThroughField) && !schema.getGridRow().contains(tempSchemaGrid)) {
					schema.getGridRow().add(tempSchemaGrid);
				} else {
					for (int index = 0; index < schema.getGridRow().size(); index++) {
						if (schema.getGridRow().get(index).getFieldName().equals(passThroughField)) {
							schema.getGridRow().set(index, tempSchemaGrid);
						}
					}
				}
			}else{
				schema.getGridRow().add(SchemaPropagationHelper.INSTANCE.createSchemaGridRow(passThroughField));
			}
			}
	}
	/**
	 * 	Add Operation field to internal schema object
	 * 	@param operationFields
	 */
	private void addOperationFieldsToSchema(List<FilterProperties> operationFields) {
		Schema schema = getSchemaForInternalPropagation();
		GridRow schemaGridRow=null;
		List<String> currentFieldsInProppogatedSchemaObject = new LinkedList<>();
		for (GridRow gridRow : schema.getGridRow()) {
			currentFieldsInProppogatedSchemaObject.add(gridRow.getFieldName());
		}

		for (FilterProperties operationField : operationFields) {
			if(!ParameterUtil.isParameter(operationField.getPropertyname())){

				if(getCurrentSchemaField(operationField.getPropertyname())!=null){
					schemaGridRow=getCurrentSchemaField(operationField.getPropertyname());
					schemaGridRow=schemaGridRow.copy();
				}
				else
					schemaGridRow = SchemaPropagationHelper.INSTANCE.createSchemaGridRow(operationField.getPropertyname());




				if (!currentFieldsInProppogatedSchemaObject.contains(operationField.getPropertyname()) && !schema.getGridRow().contains(schemaGridRow)) {
					schema.getGridRow().add(schemaGridRow);
				} else {
					for (int index = 0; index < schema.getGridRow().size(); index++) {
						if (schema.getGridRow().get(index).getFieldName().equals(operationField.getPropertyname())) {
							schema.getGridRow().set(index, schemaGridRow);

						}
					}
				}
			}
		}
	}

	private Map<String,String> getMapFields(
			List<NameValueProperty> nameValueProperties) 
			{
		Map<String,String> mapField = new LinkedHashMap<>();
		if (!nameValueProperties.isEmpty()) {

			for (NameValueProperty nameValueProperty : nameValueProperties) {
				if (!(nameValueProperty.getPropertyName().equals(
						nameValueProperty.getPropertyValue()))) {
					mapField.put(nameValueProperty.getPropertyValue(),nameValueProperty.getPropertyName());
				}
			}

		}
		return mapField;
			}



	private List<String> getPassThroughFields(
			List<NameValueProperty> nameValueProperties) 
			{
		List<String> passthroughField = new LinkedList<>();
		if (!nameValueProperties.isEmpty()) {

			for (NameValueProperty nameValueProperty : nameValueProperties) {
				if (nameValueProperty.getPropertyName().equals(
						nameValueProperty.getPropertyValue())) {
					passthroughField.add(nameValueProperty.getPropertyValue());
				}
			}

		}
		return passthroughField;
			}
	private List<FilterProperties> getOpeartionFields(MappingSheetRow mappingSheetRow) {
		List<FilterProperties> operationFields = new LinkedList<>();
		operationFields.addAll(mappingSheetRow.getOutputList());
		return operationFields;
	}
	private void getPropagatedSchema() {
		InputField inputField = null;
		transformMapping.getInputFields().clear();
		for (Link link : getComponent().getTargetConnections()) {
			String sourceTerminalId=link.getSourceTerminal();
			List<BasicSchemaGridRow> basicSchemaGridRows=SchemaPropagationHelper.INSTANCE.
			getBasicSchemaGridRowList(Constants.INPUT_SOCKET_TYPE+getPortCount(sourceTerminalId)
					, link);
			if (basicSchemaGridRows != null){
				for (BasicSchemaGridRow row :basicSchemaGridRows ) {
					inputField = new InputField(row.getFieldName(), new ErrorObject(false, ""));
						transformMapping.getInputFields().add(inputField);
				}
			}
		}
	}

	private String getPortCount(String sourceTerminalId) {
		String portCount=null;
		if(StringUtils.startsWithIgnoreCase(sourceTerminalId, Constants.UNUSED_SOCKET_TYPE)){
			portCount=StringUtils.remove(sourceTerminalId, Constants.UNUSED_SOCKET_TYPE);
		}else if(StringUtils.startsWithIgnoreCase(sourceTerminalId, Constants.OUTPUT_SOCKET_TYPE)){
			portCount=StringUtils.remove(sourceTerminalId, Constants.OUTPUT_SOCKET_TYPE);
		}
		return portCount;
	}

	private Composite createRadioButtonComposite(ELTDefaultSubgroupComposite eltDefaultSubgroupComposite) {
		Composite radioButtonComposite=new Composite(eltDefaultSubgroupComposite.getContainerControl(), SWT.NONE);
		setLayoutAndDataToComposite(radioButtonComposite,2);
		return radioButtonComposite;
	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		property.put(propertyName, transformMapping);
		String expression=transformMapping.getExpressionEditorData().getExpression();
		ExpressionEditorUtil.validateExpression(expression,  transformMapping.getExpressionEditorData()
				.getCombinedFieldDatatypeMap()
				, transformMapping.getExpressionEditorData());
		showHideDecorator();	
        return property;
	}

	private void showHideDecorator() {
		if(transformMapping.isExpression()&&!transformMapping.getExpressionEditorData().isValid())
			expressionValidateDecorator.show();
			else 
			expressionValidateDecorator.hide();
	}

	@Override
	public boolean isWidgetValid() {
		return validateAgainstValidationRule(transformMapping);
	}

	@Override
	public void addModifyListener(Property property, ArrayList<AbstractWidget> widgetList) {
		widgets=widgetList;
		outputRecordCoundText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				showHideErrorSymbol(widgetList);
			}
		});
	}
}

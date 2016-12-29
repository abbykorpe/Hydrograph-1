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

package hydrograph.ui.propertywindow.widgets.customwidgets.operational;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.datastructure.expression.ExpressionEditorData;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.expression.editor.enums.DataTypes;
import hydrograph.ui.expression.editor.launcher.LaunchExpressionEditor;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.OperationClassConfig;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.WidgetConfig;

public class AggregateCumulateExpressionComposite extends AbstractExpressionComposite{

	
	private Label lblNewLabel_2;
	private Label lblNewLabel_3;
	private Label lblNewLabel_4;
	
	public AggregateCumulateExpressionComposite (Composite parent, int style, final MappingSheetRow mappingSheetRow,
			final Component component, WidgetConfig widgetConfig) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		configurationForTransformWidget = (OperationClassConfig) widgetConfig;

		this.mappingSheetRow = mappingSheetRow;
		this.component = component;
		Composite selectColumnComposite = new Composite(this, SWT.NONE);
		selectColumnComposite.setLayout(new GridLayout(1, false));
		GridData gd_selectColumnComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_selectColumnComposite.heightHint = 285;
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

		tableViewer = new TableViewer(selectColumnComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setVisible(true);
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(4, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
//		if (isAggregateOrCumulate) {
			gd_composite.heightHint = 285;
//		} else {
//			gd_composite.heightHint = 191;
//		}
		gd_composite.widthHint = 184;
		composite.setLayoutData(gd_composite);
		new Label(composite, SWT.NONE);

		lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setText("Switch to");

		Composite radioButtonComposite = new Composite(composite, SWT.NONE);
		GridLayout radioButtonCompositeLayout = new GridLayout(2, false);
		radioButtonCompositeLayout.marginWidth = 0;
		radioButtonComposite.setLayout(radioButtonCompositeLayout);
		radioButtonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		switchToExpressionButton = new Button(radioButtonComposite, SWT.RADIO);
		switchToExpressionButton.setText("Expression");

		switchToClassButton = new Button(radioButtonComposite, SWT.RADIO);
		switchToClassButton.setText("Class");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		switchToExpressionButton.setSelection(true);
//		if(!isAggregateOrCumulate && !isTransForm ){
//			radioButtonComposite.setVisible(false);
//			lblNewLabel_1.setVisible(false);
//		}else{
			radioButtonComposite.setVisible(true);
			lblNewLabel_1.setVisible(true);
//		}
		Label lblExpression = new Label(composite, SWT.NONE);
		GridData gd_lblExpression = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_lblExpression.minimumWidth = 70;
		gd_lblExpression.heightHint = 15;
		lblExpression.setLayoutData(gd_lblExpression);
		lblExpression.setText("Expression Id");

		GridData gd_composite_1, gd_composite_2, gd_browseButton;
		expressionIdTextBox = new Text(composite, SWT.BORDER);
//		if (isAggregateOrCumulate) {
			gd_composite_1 = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 6);
			gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_browseButton = new GridData(SWT.LEFT, SWT.TOP, false, false, 0, 1);
			expressionIdTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			
			lblNewLabel_3 = new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			
			lblNewLabel_2 = new Label(composite, SWT.NONE);
			lblNewLabel_2.setText("DataType");
			comboDataTypes = new Combo(composite, SWT.NONE);
			GridData comboLayout = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			comboLayout.widthHint=25;
			comboDataTypes.setLayoutData(comboLayout);
			if (OSValidator.isMac()) {
				gd_browseButton.widthHint = 40;
			} else {
				gd_browseButton.widthHint = 28;
			}
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			
			lblNewLabel_4 = new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);

			labelAccumulator = new Label(composite, SWT.NONE);
			labelAccumulator.setText("Accumulator");

			textAccumulator = new Text(composite, SWT.BORDER);
			textAccumulator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			if(!StringUtils.isBlank(mappingSheetRow.getAccumulator())){
				textAccumulator.setText(mappingSheetRow.getAccumulator());
			}
			if(StringUtils.isBlank(textAccumulator.getText())){
				textAccumulator.setBackground(new Color(null,255,255,000));
			}else{
				textAccumulator.setBackground(new Color(null,255,255,255));
			}
			addDataTypes();
			if(!StringUtils.isBlank(mappingSheetRow.getComboDataType())){
				comboDataTypes.setText(mappingSheetRow.getComboDataType());
			}
			
			isParamAccumulator = new Button(composite, SWT.CHECK);
			GridData gd_btnCheckButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_btnCheckButton.horizontalIndent = 12;
			isParamAccumulator.setLayoutData(gd_btnCheckButton);
			isParamAccumulator.setText("Is Parameter");
			new Label(composite, SWT.NONE);
			
			if(mappingSheetRow.isAccumulatorParameter()){
				isParamAccumulator.setSelection(true);
				comboDataTypes.setEnabled(false);
			}

			textAccumulator.addFocusListener(new FocusListener() {
				@Override
			public void focusLost(FocusEvent e) {
				Text textBox = (Text) e.widget;
				String parameterText = textBox.getText();

				if (!StringUtils.isBlank(parameterText) && isParamAccumulator.getSelection()) {
					parameterText = StringUtils.replace(
							StringUtils.replace(parameterText, Constants.PARAMETER_PREFIX, ""),
							Constants.PARAMETER_SUFFIX, "");
					textBox.setText(Constants.PARAMETER_PREFIX + parameterText + Constants.PARAMETER_SUFFIX);
				}
			}
				@Override
				public void focusGained(FocusEvent e) {
					Text textBox=(Text)e.widget;
					String parameterText=textBox.getText();
					parameterText=StringUtils.replace(StringUtils.replace(parameterText, Constants.PARAMETER_PREFIX, ""),Constants.PARAMETER_SUFFIX,"");
					textBox.setText(parameterText);
				}
			});
			
			isParamAccumulator.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					if(isParamAccumulator.getSelection()){
						comboDataTypes.setEnabled(false);
						if(!StringUtils.isBlank(textAccumulator.getText())){
							String text=StringUtils.replace(StringUtils.replace(textAccumulator.getText(),Constants.PARAMETER_PREFIX , ""),Constants.PARAMETER_SUFFIX,"");
							textAccumulator.setText(Constants.PARAMETER_PREFIX+text+Constants.PARAMETER_SUFFIX);
						}
						
					}else{
						comboDataTypes.setEnabled(true);
						String text=StringUtils.replace(StringUtils.replace(textAccumulator.getText(),Constants.PARAMETER_PREFIX , ""),Constants.PARAMETER_SUFFIX,"");
						textAccumulator.setText(text);
					}
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
			label = new Label(composite, SWT.NONE);
			new Label(composite, SWT.NONE);
			
			
			gd_composite_1 = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 4);

			composite_1 = new Composite(composite, SWT.NONE);
			composite_1.setLayout(new GridLayout(1, false));
			
//		} else {
//			expressionIdTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//			new Label(composite, SWT.NONE);
//			new Label(composite, SWT.NONE);
//			new Label(composite, SWT.NONE);
//			new Label(composite, SWT.NONE);
//
//			composite_1 = new Composite(composite, SWT.NONE);
//			composite_1.setLayout(new GridLayout(1, false));
//			gd_composite_1 = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 4);
//			gd_composite_2 = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
//			gd_browseButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 0, 0);
//		}
		if (OSValidator.isMac()) {
			gd_browseButton.widthHint = 40;
			gd_browseButton.horizontalIndent = -5;
		} else {
			gd_browseButton.widthHint = 28;
		}
		gd_composite_1.heightHint = 80;
		gd_composite_1.widthHint = 90;
		composite_1.setLayoutData(gd_composite_1);

		composite_2 = new Composite(composite_1, SWT.NONE);
		gd_composite_2.heightHint = 75;
		gd_composite_2.widthHint = 80;
		composite_2.setLayoutData(gd_composite_2);
		composite_2.setLayout(new GridLayout(1, false));
		browseButton = new Button(composite_2, SWT.NONE);
		gd_browseButton.heightHint = 25;
		browseButton.setLayoutData(gd_browseButton);
		browseButton.setText("...");
		browseButton.setToolTipText(Messages.EXPRESSION_COMPOSITE_BROWSE_BUTTON_TOOL_TIP);
		browseButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createExpressionEditorData();

				LaunchExpressionEditor launchExpressionEditor = new LaunchExpressionEditor();
				launchExpressionEditor.launchExpressionEditor(mappingSheetRow.getExpressionEditorData(),
						getInputSchema(component), mappingSheetRow.getOperationID());
				expressionTextBox.setText(mappingSheetRow.getExpressionEditorData().getExpression());
			}

		});
		new Label(composite_2, SWT.NONE);
		btnIsParam = new Button(composite_2, SWT.CHECK);
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
				Text text = (Text) e.widget;
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
		if (!mappingSheetRow.getOutputList().isEmpty()) {
			if (StringUtils.isNotBlank(mappingSheetRow.getOutputList().get(0).getPropertyname()))
				outputFieldTextBox.setText(mappingSheetRow.getOutputList().get(0).getPropertyname());
		}
		if (mappingSheetRow.getExpressionEditorData() != null
				&& StringUtils.isNotBlank(mappingSheetRow.getExpressionEditorData().getExpression())) {
			expressionTextBox.setText(mappingSheetRow.getExpressionEditorData().getExpression());
		}
		setAllWidgetsOnIsParamButtonForAggregateCumulate(btnIsParam);
		disabledWidgetsifWholeExpressionIsParameterForAggregateCumulate(btnIsParam, mappingSheetRow.isWholeOperationParameter());
	}

	
	private void addDataTypes(){
		comboDataTypes.setText(Messages.DATATYPE_STRING);
		comboDataTypes.add(Messages.DATATYPE_STRING);
		comboDataTypes.add(Messages.DATATYPE_INTEGER);
		comboDataTypes.add(Messages.DATATYPE_DOUBLE);
		comboDataTypes.add(Messages.DATATYPE_FLOAT);
		comboDataTypes.add(Messages.DATATYPE_SHORT);
		comboDataTypes.add(Messages.DATATYPE_BOOLEAN);
		comboDataTypes.add(Messages.DATATYPE_DATE);
		comboDataTypes.add(Messages.DATATYPE_BIGDECIMAL);
		comboDataTypes.add(Messages.DATATYPE_LONG);
			
	}
	private void disabledWidgetsifWholeExpressionIsParameterForAggregateCumulate(Button isParam, boolean isWholeOperationParameter) {
		if (isWholeOperationParameter) {
			Text textAccumulator = (Text)isParam.getData("textAccumulator");
			Button isParamAccumulator = (Button)isParam.getData("isParamAccumulator");
			Combo comboDataTypes = (Combo)isParam.getData("comboDataTypes");
			textAccumulator.setEnabled(false);
			isParamAccumulator.setEnabled(false);
			comboDataTypes.setEnabled(false);
			super.disabledWidgetsifWholeExpressionIsParameter(isParamAccumulator, isWholeOperationParameter);
		}
		
	}

	private void setAllWidgetsOnIsParamButtonForAggregateCumulate(Button isParam) {
		isParam.setData("comboDataTypes",comboDataTypes);
		isParam.setData("textAccumulator",textAccumulator);
		isParam.setData("isParamAccumulator",isParamAccumulator);
		super.setAllWidgetsOnIsParamButton(isParam);
	}

	
	@Override
	protected void checkSubclass() {
	}

	private List<FixedWidthGridRow> getInputSchema(Component component) {
		List<FixedWidthGridRow> fixedWidthGridRows = new ArrayList<>();
		for (Link link : component.getTargetConnections()) {
			ComponentsOutputSchema componentsOutputSchema = SchemaPropagation.INSTANCE.getComponentsOutputSchema(link);
			if (componentsOutputSchema != null && componentsOutputSchema.getFixedWidthGridRowsOutputFields() != null) {
				fixedWidthGridRows = componentsOutputSchema.getFixedWidthGridRowsOutputFields();
			}
			break;
		}
		return fixedWidthGridRows;
	}

	@Override
	public ExpressionEditorData createExpressionEditorData() {
		mappingSheetRow.getExpressionEditorData().getExtraFieldDatatypeMap().clear();
        mappingSheetRow.getExpressionEditorData().getExtraFieldDatatypeMap().put(Constants.ACCUMULATOR_VARIABLE,DataTypes.
        		getDataTypeClassfromString(mappingSheetRow.getComboDataType()));
        return super.createExpressionEditorData();
	}

}
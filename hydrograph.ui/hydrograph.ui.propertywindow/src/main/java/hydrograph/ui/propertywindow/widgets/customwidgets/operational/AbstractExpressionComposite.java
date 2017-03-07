/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.ui.propertywindow.widgets.customwidgets.operational;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.expression.ExpressionEditorData;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.GridRow;
import hydrograph.ui.datastructure.property.Schema;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.expression.editor.util.FieldDataTypeMap;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.OperationClassConfig;
import hydrograph.ui.propertywindow.widgets.utility.SchemaSyncUtility;

/**
 * @author Bitwise parent composite for all expression composite
 *
 */
public abstract class AbstractExpressionComposite extends Composite {
    
	public static final String EXPRESSION_COMPOSITE_KEY = "expression-composite";
	protected Text expressionIdTextBox;
	protected Text expressionTextBox;
	protected Text expression_text_1;
	protected Text parameterTextBox;
	protected Text outputFieldTextBox;
	protected Table table;
	protected TableViewer tableViewer;
	protected Button addButton, deletButton, browseButton,expressionbutton;
	protected Button btnIsParam;
	protected Button switchToClassButton;
	protected Button switchToExpressionButton;
	protected Label lblNewLabel_1;
	protected MappingSheetRow mappingSheetRow;
	protected Component component;
	protected Composite composite_1;
	protected Composite composite_2;
	protected OperationClassConfig configurationForTransformWidget;
	protected boolean isAggregateOrCumulate;
	protected Text textAccumulator;
	protected Label label;
	protected Label labelAccumulator;
	protected Combo comboDataTypes;
	protected boolean isTransForm;
	protected Button isParamAccumulator;
	
	public Button getIsParamAccumulator() {
		return isParamAccumulator;
	}

	public AbstractExpressionComposite(Composite parent, int style) {
		super(parent, style);
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
	
	public Text getExressionTextBox2() {
		return expression_text_1;
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

	public Text getTextAccumulator() {
		return textAccumulator;
	}


	public Combo getComboDataTypes() {
		return comboDataTypes;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Composite#checkSubclass()
	 */
	@Override
	protected void checkSubclass() {
	}
    
	/**
	 * @param isParam
	 * @param isWholeOperationParameter
	 */
	protected void disabledWidgetsifWholeExpressionIsParameter(Button isParam, boolean isWholeOperationParameter) {
		if (isWholeOperationParameter) {
			TableViewer tableViewer = (TableViewer) isParam.getData(Constants.INPUT_FIELD_TABLE);
			Button addButton = (Button) isParam.getData(Constants.ADD_BUTTON);
			Button deleteButton = (Button) isParam.getData(Constants.DELETE_BUTTON);
			Text expressionIdTextBox = (Text) isParam.getData(Constants.EXPRESSION_ID_TEXT_BOX);
			Button browseButton = (Button) isParam.getData(Constants.EXPRESSION_EDITOR_BUTTON);
			Button expressionbutton = (Button) isParam.getData(Constants.EXPRESSION_EDITOR_BUTTON1);
			Text outputFieldTextBox = (Text) isParam.getData(Constants.OUTPUT_FIELD_TEXT_BOX);
			tableViewer.getTable().setEnabled(false);
			addButton.setEnabled(false);
			deleteButton.setEnabled(false);
			expressionIdTextBox.setEnabled(false);
			browseButton.setEnabled(false);
			expressionbutton.setEnabled(false);
			outputFieldTextBox.setEnabled(false);
		}
	}
	
	 /**
	 * @param isParam
	 */
	 protected void setAllWidgetsOnIsParamButton(Button isParam) {
			isParam.setData(Constants.INPUT_FIELD_TABLE, tableViewer);
			isParam.setData(Constants.ADD_BUTTON, addButton);
			isParam.setData(Constants.DELETE_BUTTON, deletButton);
			isParam.setData(Constants.EXPRESSION_ID_TEXT_BOX, expressionIdTextBox);
			isParam.setData(Constants.EXPRESSION_EDITOR_BUTTON, browseButton);
			isParam.setData(Constants.EXPRESSION_EDITOR_BUTTON1, expressionbutton);
			isParam.setData(Constants.OUTPUT_FIELD_TEXT_BOX, outputFieldTextBox);
			isParam.setData(Constants.PARAMETER_TEXT_BOX, parameterTextBox);
			isParam.setData(Constants.EXPRESSION_TEXT_BOX, expressionTextBox);
			isParam.setData(Constants.EXPRESSION_TEXT_BOX1, expression_text_1);
		}
	
	/**
	 * @param component
	 * @return
	 */
	private List<FixedWidthGridRow> getInputSchema(Component component) {
		List<FixedWidthGridRow> fixedWidthGridRows = new ArrayList<>();
		Map<String,Schema> schemaMap=(TreeMap<String,Schema>)component.getProperties().get(Constants.PREVIOUS_COMPONENT_OLD_SCHEMA);
		for (Link link : component.getTargetConnections()) {
			if(schemaMap!=null)
			{
			Schema schema=schemaMap.get(link.getTargetTerminal());
			List<GridRow> gridRowList=null;
			if(schema!=null)
			gridRowList=schemaMap.get(link.getTargetTerminal()).getGridRow();
			fixedWidthGridRows.addAll(SchemaSyncUtility.INSTANCE.convertGridRowsSchemaToFixedSchemaGridRows(gridRowList));
			}
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
			mappingSheetRow.getExpressionEditorData().getSelectedInputFieldsForExpression().putAll(
					FieldDataTypeMap.INSTANCE.createFieldDataTypeMap(inputFieldNames, getInputSchema(component)));
			mappingSheetRow.getExpressionEditorData().getfieldsUsedInExpression().addAll(inputFieldNames);

		} else {
			mappingSheetRow.getExpressionEditorData().getSelectedInputFieldsForExpression().clear();
			mappingSheetRow.getExpressionEditorData().getfieldsUsedInExpression().clear();
		}

		return mappingSheetRow.getExpressionEditorData();
	}
}

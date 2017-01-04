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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.datastructure.expression.ExpressionEditorData;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.expression.editor.util.FieldDataTypeMap;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.OperationClassConfig;

/**
 * @author Bitwise parent composite for all expression composite
 *
 */
public abstract class AbstractExpressionComposite extends Composite {
    
	public static final String EXPRESSION_COMPOSITE_KEY = "expression-composite";
	protected Text expressionIdTextBox;
	protected Text expressionTextBox;
	protected Text parameterTextBox;
	protected Text outputFieldTextBox;
	protected Table table;
	protected TableViewer tableViewer;
	protected Button addButton, deletButton, browseButton;
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
			Text outputFieldTextBox = (Text) isParam.getData(Constants.OUTPUT_FIELD_TEXT_BOX);
			tableViewer.getTable().setEnabled(false);
			addButton.setEnabled(false);
			deleteButton.setEnabled(false);
			expressionIdTextBox.setEnabled(false);
			browseButton.setEnabled(false);
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
			isParam.setData(Constants.OUTPUT_FIELD_TEXT_BOX, outputFieldTextBox);
			isParam.setData(Constants.PARAMETER_TEXT_BOX, parameterTextBox);
			isParam.setData(Constants.EXPRESSION_TEXT_BOX, expressionTextBox);
		}
	
	/**
	 * @param component
	 * @return
	 */
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

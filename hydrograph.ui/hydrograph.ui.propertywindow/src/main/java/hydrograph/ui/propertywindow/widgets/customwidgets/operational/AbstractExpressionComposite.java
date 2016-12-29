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
import hydrograph.ui.datastructure.expression.ExpressionEditorData;
import hydrograph.ui.datastructure.property.ComponentsOutputSchema;
import hydrograph.ui.datastructure.property.FilterProperties;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;
import hydrograph.ui.datastructure.property.mapping.MappingSheetRow;
import hydrograph.ui.expression.editor.util.FieldDataTypeMap;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.schema.propagation.SchemaPropagation;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.OperationClassConfig;

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
	
	@Override
	protected void checkSubclass() {
	}
    
	protected void disabledWidgetsifWholeExpressionIsParameter(Button isParam, boolean isWholeOperationParameter) {
		if (isWholeOperationParameter) {
			TableViewer tableViewer = (TableViewer) isParam.getData("inputFieldTable");
			Button addButton = (Button) isParam.getData("addButton");
			Button deleteButton = (Button) isParam.getData("deleteButton");
			Text expressionIdTextBox = (Text) isParam.getData("expressionIdTextBox");
			Button browseButton = (Button) isParam.getData("expressionEditorButton");
			Text outputFieldTextBox = (Text) isParam.getData("outputFieldTextBox");
			tableViewer.getTable().setEnabled(false);
			addButton.setEnabled(false);
			deleteButton.setEnabled(false);
			expressionIdTextBox.setEnabled(false);
			browseButton.setEnabled(false);
			outputFieldTextBox.setEnabled(false);
		}
	}
	
	 protected void setAllWidgetsOnIsParamButton(Button isParam) {
			isParam.setData("inputFieldTable", tableViewer);
			isParam.setData("addButton", addButton);
			isParam.setData("deleteButton", deletButton);
			isParam.setData("expressionIdTextBox", expressionIdTextBox);
			isParam.setData("expressionEditorButton", browseButton);
			isParam.setData("outputFieldTextBox", outputFieldTextBox);
			isParam.setData("parameterTextBox", parameterTextBox);
			isParam.setData("expressionTextBox", expressionTextBox);
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

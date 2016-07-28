package hydrograph.ui.datastructure.expression;

import hydrograph.ui.common.cloneableinterface.IDataStructure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExpressionEditorData implements IDataStructure {
	private String expression;
	private List<String> fieldsUsedInExpression;
	private Map<String,Class<?>> selectedInputFieldsForExpression;

	public ExpressionEditorData(String expression) {
		this.expression = expression;
		fieldsUsedInExpression = new ArrayList<>();
		selectedInputFieldsForExpression = new LinkedHashMap<String,Class<?>>();
	}

	ExpressionEditorData(String expression, List<String> clonedListUsedFieldsInExpression,
			Map<String,Class<?>> clonedSelectedFieldsForExpression) {
		expression = this.expression;
		if (fieldsUsedInExpression != null)
			clonedListUsedFieldsInExpression.addAll(fieldsUsedInExpression);
		if (selectedInputFieldsForExpression != null)
			clonedSelectedFieldsForExpression.putAll(selectedInputFieldsForExpression);
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Map<String,Class<?>> getSelectedInputFieldsForExpression() {
		return selectedInputFieldsForExpression;
	}

	public List<String> getfieldsUsedInExpression() {
		return fieldsUsedInExpression;
	}

	@Override
	public ExpressionEditorData clone() {
		List<String> clonedList = new ArrayList<>();
		Map<String,Class<?>> clonedSelectedFieldsForExpression = new LinkedHashMap<String,Class<?>>();
		return new ExpressionEditorData("", clonedList, clonedSelectedFieldsForExpression);
	}

}

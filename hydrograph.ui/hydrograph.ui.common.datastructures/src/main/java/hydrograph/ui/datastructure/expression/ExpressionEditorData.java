package hydrograph.ui.datastructure.expression;

import hydrograph.ui.common.cloneableinterface.IDataStructure;
import hydrograph.ui.datastructure.property.FilterProperties;

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

	public ExpressionEditorData(String expression, List<String> clonedListUsedFieldsInExpression,
			Map<String,Class<?>> clonedSelectedFieldsForExpression) {
		this.expression=expression;
		this.fieldsUsedInExpression=clonedListUsedFieldsInExpression;
		this.selectedInputFieldsForExpression=clonedSelectedFieldsForExpression;
		
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
		String clonedExpression=this.expression;
		List<String> clonedFieldsUsedInExpression = new ArrayList<>();
		clonedFieldsUsedInExpression.addAll(this.fieldsUsedInExpression);
		Map<String,Class<?>> clonedSelectedInputFieldsForExpression = new LinkedHashMap<String,Class<?>>();
		
		for(Map.Entry<String, Class<?>> entry:selectedInputFieldsForExpression.entrySet())
		{
			clonedSelectedInputFieldsForExpression.put(entry.getKey(), entry.getValue());
		}	
		return new ExpressionEditorData(clonedExpression, clonedFieldsUsedInExpression, clonedSelectedInputFieldsForExpression);
	}

}

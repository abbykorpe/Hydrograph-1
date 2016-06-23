package hydrograph.ui.dataviewer.filter;

public class Condition{
	private String fieldName;
	private String relationalOperator;
	private String conditionalOperator;
	private String value;
	
	public Condition() {
		this.fieldName = "";
		this.relationalOperator = "";
		this.conditionalOperator = "";
		this.value = "";
	}
	
	public Condition copy(Condition source){
		Condition condition = new Condition();
		condition.setFieldName(source.getFieldName());
		condition.setRelationalOperator(source.getRelationalOperator());
		condition.setConditionalOperator(source.getConditionalOperator());
		condition.setValue(source.getValue());
		return condition;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getRelationalOperator() {
		return relationalOperator;
	}
	public void setRelationalOperator(String relationalOperator) {
		this.relationalOperator = relationalOperator;
	}
	public String getConditionalOperator() {
		return conditionalOperator;
	}
	public void setConditionalOperator(String conditionalOperator) {
		this.conditionalOperator = conditionalOperator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "FilterConditions [fieldName=" + fieldName
				+ ", relationalOperator=" + relationalOperator
				+ ", conditionalOperator=" + conditionalOperator
				+ ", value=" + value + "]";
	}
}
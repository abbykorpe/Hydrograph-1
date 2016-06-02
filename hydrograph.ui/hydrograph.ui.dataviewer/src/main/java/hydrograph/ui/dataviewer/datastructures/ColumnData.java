package hydrograph.ui.dataviewer.datastructures;


public class ColumnData {
	String value;
	Schema schema;
	public ColumnData(String value, Schema schema) {
		super();
		this.value = value;
		this.schema = schema;
	}
	
	public ColumnData(String value) {
		super();
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	@Override
	public String toString() {
		return "{\"value\":\"" + value + "\",\"schema\":" + schema + "}";
	}
}

package hydrograph.ui.dataviewer.datastructures;

public class Schema {
	String columnName;
	String dataType;
	String dateFormat;

	public Schema(String columnName, String dataType, String dateFormat) {
		super();
		this.columnName = columnName;
		this.dataType = dataType;
		this.dateFormat = dateFormat;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	@Override
	public String toString() {
		return "Schema [columnName=" + columnName + ", dataType=" + dataType + ", dateFormat=" + dateFormat + "]";
	}
	
	
	
}

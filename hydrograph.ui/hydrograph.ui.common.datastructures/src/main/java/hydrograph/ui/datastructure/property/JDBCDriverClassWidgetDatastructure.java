package hydrograph.ui.datastructure.property;

public class JDBCDriverClassWidgetDatastructure {
	boolean isParameter;
	String dataBaseValue = "";
	String jdbcDriverClassValue = "";

	public boolean isParameter() {
		return isParameter;
	}

	public void setParameter(boolean isParameter) {
		this.isParameter = isParameter;
	}

	public String getDataBaseValue() {
		return dataBaseValue;
	}

	public void setDataBaseValue(String dataBaseValue) {
		this.dataBaseValue = dataBaseValue;
	}

	public String getJdbcDriverClassValue() {
		return jdbcDriverClassValue;
	}

	public void setJdbcDriverClassValue(String jdbcDriverClassValue) {
		this.jdbcDriverClassValue = jdbcDriverClassValue;
	}

}

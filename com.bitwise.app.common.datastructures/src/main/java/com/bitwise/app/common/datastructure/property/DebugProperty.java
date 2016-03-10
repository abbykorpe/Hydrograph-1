package com.bitwise.app.common.datastructure.property;


/**
 * @author Bitwise
 *
 */
public class DebugProperty {

	private String limit;
	private String basePath;
	private int comboBoxIndex;
	private Boolean isDebug;
	
	public DebugProperty() {
		isDebug = Boolean.FALSE;
	}

	
	public boolean isDebug() {
		return isDebug;
	}


	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}


	public int getComboBoxIndex() {
		return comboBoxIndex;
	}
	public void setComboBoxIndex(int comboBoxIndex) {
		this.comboBoxIndex = comboBoxIndex;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DebugProperty [limit=");
		builder.append(limit);
		builder.append(", basePath=");
		builder.append(basePath);
		builder.append("]");
		return builder.toString();
	}
	
}

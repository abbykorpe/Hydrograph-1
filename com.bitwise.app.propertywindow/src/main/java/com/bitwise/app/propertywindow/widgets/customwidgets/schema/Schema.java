package com.bitwise.app.propertywindow.widgets.customwidgets.schema;

import java.util.List;


/**
 * This Schema class contains accessors methods.
 * 
 * @author Bitwise
 */

public class Schema {
	private String externalSchemaPath;
	private Boolean isExternal;
	private List<GridRow> gridRow;

	public String getExternalSchemaPath() {
		return externalSchemaPath;
	}

	public void setExternalSchemaPath(String externalSchemaPath) {
		this.externalSchemaPath = externalSchemaPath;
	}

	public Boolean getIsExternal() {
		return isExternal;
	}

	public void setIsExternal(Boolean isExternalPath) {
		this.isExternal = isExternalPath;
	}

	public List<GridRow> getGridRow() {
		return gridRow;
	}

	public void setGridRow(List<GridRow> gridRow) {
		this.gridRow = gridRow;
	}

	@Override
	public String toString() {
		return "ExternalSchema [externalSchemaPath=" + externalSchemaPath
				+ ", isExternalPath=" + isExternal + ", gridRow=" + gridRow
				+ "]";
	}
	
}

package hydrograph.ui.dataviewer.datastructures;


import java.util.List;

public class RowData {
	int rowNumber;
	List<ColumnData> columns;

	public RowData(List<ColumnData> columns,int rowNumber) {
		super();
		this.columns = columns;
		this.rowNumber = rowNumber;
	}
	
	
	public List<ColumnData> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnData> columns) {
		this.columns = columns;
	}
	
	public int getRowNumber() {
		return rowNumber;
	}


	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}


	@Override
	public String toString() {
		return "RowData [rowNumber=" + rowNumber + ", columns=" + columns + "]";
	}
	
	
}

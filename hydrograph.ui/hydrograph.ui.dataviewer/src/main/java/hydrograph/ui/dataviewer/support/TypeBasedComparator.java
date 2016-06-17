package hydrograph.ui.dataviewer.support;

import hydrograph.ui.dataviewer.datastructures.RowData;
import hydrograph.ui.dataviewer.datastructures.RowField;

import java.util.Comparator;


/**
 * Type specific Comparator
 * @author Bitwise
 *
 */
public class TypeBasedComparator implements Comparator<Object> {
	
	private SortOrder sortType;
	private int columnToSort;
	private SortDataType sortDataType;
	private String dateFormat;
	
	public TypeBasedComparator(SortOrder sortType, int columnToSort, SortDataType sortDataType, String dateFormat) {
		this.sortType = sortType;
		this.columnToSort = columnToSort;
		this.sortDataType = sortDataType;
		this.dateFormat = dateFormat;
	}

	public int compare(Object row1, Object row2) {
		
		RowField rowField1 = ((RowField)((RowData)row1).getRowFields().get(columnToSort));
		RowField rowField2 = ((RowField)((RowData)row2).getRowFields().get(columnToSort));
		
		switch (sortType) {
		case ASC:
			return sortDataType.compareTo(rowField1.getValue(), rowField2.getValue(), dateFormat);
		case DSC:
			return sortDataType.compareTo(rowField2.getValue(), rowField1.getValue(), dateFormat);
		default:
			return sortDataType.compareTo(rowField1.getValue(), rowField2.getValue(), dateFormat);
		}
	}
}
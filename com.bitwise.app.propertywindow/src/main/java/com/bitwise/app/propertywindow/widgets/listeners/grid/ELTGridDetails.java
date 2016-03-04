package com.bitwise.app.propertywindow.widgets.listeners.grid;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;

import com.bitwise.app.common.datastructure.property.GridRow;
import com.bitwise.app.propertywindow.widgets.utility.GridWidgetCommonBuilder;


/**
 * The Class ELTGridDetails.
 * 
 * @author Bitwise
 */
public class ELTGridDetails {

	private List<GridRow> grids;
	private TableViewer tableViewer;
	private Label label;
	private GridWidgetCommonBuilder gridWidgetCommonBuilder;
	
	/**
	 * Instantiates a new ELT grid details.
	 * 
	 * @param grids
	 *            the grids
	 * @param tableViewer
	 *            the table viewer
	 * @param label
	 *            the label
	 * @param gridWidgetCommonBuilder
	 *            the grid widget common builder
	 */
	public ELTGridDetails(List<GridRow> grids, TableViewer tableViewer,
			Label label,GridWidgetCommonBuilder gridWidgetCommonBuilder) {
		super();
		this.grids = grids;
		this.tableViewer = tableViewer;
		this.label = label;
		this.gridWidgetCommonBuilder=gridWidgetCommonBuilder;
	}
	
	public List<GridRow> getGrids() {
		return grids;
	}

	public void setGrids(List<GridRow> grids) {
		this.grids = grids;
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public void setTableViewer(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public GridWidgetCommonBuilder getGridWidgetCommonBuilder() {
		return gridWidgetCommonBuilder;
	}

	public void setGridWidgetCommonBuilder(
			GridWidgetCommonBuilder gridWidgetCommonBuilder) {
		this.gridWidgetCommonBuilder = gridWidgetCommonBuilder;
	}

	

}

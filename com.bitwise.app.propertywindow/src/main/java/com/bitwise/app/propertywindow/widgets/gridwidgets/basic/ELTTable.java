package com.bitwise.app.propertywindow.widgets.gridwidgets.basic;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

/**
 * The Class ELTTable.
 * 
 * @author Bitwise
 */
public class ELTTable extends AbstractELTWidget{

	Table table;
	
	/**
	 * Instantiates a new ELT table.
	 * 
	 * @param tableViewer
	 *            the table viewer
	 */
	public ELTTable(TableViewer tableViewer, int height, int width) {
		super();
		this.table = tableViewer.getTable();
		GridData gd_tableGridData = new GridData(SWT.FILL, SWT.FILL, false,false,0,0);
		gd_tableGridData.heightHint = height;
		gd_tableGridData.widthHint = width;
		this.table.setLayoutData(gd_tableGridData);
		
	}

	@Override
	public void attachWidget(Composite container) {
		widget=table;
		
	}
}

package com.bitwise.app.parametergrid.textgridwidget.rows;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.bitwise.app.common.util.SWTResourceManager;
import com.bitwise.app.parametergrid.textgridwidget.columns.TextGridColumnLayout;
import com.bitwise.app.parametergrid.textgridwidget.columns.TextGridRowLayout;

public class TextGridRowBuilder {
	private TextGridRowLayout textGridRow;
	private Composite container;
	private List<String> rowData;
	private boolean enabled=true;
	
	public TextGridRowBuilder(Composite container,TextGridRowLayout textGridColumns, List<String> rowData){
		this.textGridRow = textGridColumns;
		this.container = container;
		this.rowData = rowData;
		this.enabled = true;
	}
	
	public TextGridRowBuilder(Composite container,TextGridRowLayout textGridColumns, List<String> rowData,boolean enabled){
		this.textGridRow = textGridColumns;
		this.container = container;
		this.rowData = rowData;
		this.enabled = enabled;
	}
	
	public Composite addHeader(){
		Composite composite = new Composite(container, SWT.NONE);
		GridLayout gl_composite = new GridLayout(textGridRow.getNumberOfColumn() + 1, false);
		gl_composite.horizontalSpacing = 7;
		gl_composite.marginWidth = 1;
		gl_composite.marginHeight = 0;
		gl_composite.verticalSpacing = 1;
		composite.setLayout(gl_composite);
			
		Button rowSelection = new Button(composite, SWT.CHECK);
		
		Map<Integer, TextGridColumnLayout> columns = textGridRow.getTextGridColumns();
		for(int columnNumber:columns.keySet()){
			Text text = new Text(composite, SWT.BORDER);
			if(!columns.get(columnNumber).grabHorizantalAccessSpace()){
				GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_text.widthHint = columns.get(columnNumber).getColumnWidth();
				text.setLayoutData(gd_text);
				
				text.setEditable(false);
				text.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				text.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
			}else{
				text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				text.setBounds(0, 0, 76, 21);
				text.setFocus();
				
				text.setEditable(false);
				text.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				text.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));				
			}
			

			if(rowData!=null)
				text.setText(rowData.get(columnNumber));
		}
		
		return composite;
	}
	
	public Composite addRaw(){
		Composite composite = new Composite(container, SWT.NONE);
		GridLayout gl_composite = new GridLayout(textGridRow.getNumberOfColumn() + 1, false);
		gl_composite.horizontalSpacing = 7;
		gl_composite.marginWidth = 1;
		gl_composite.marginHeight = 0;
		gl_composite.verticalSpacing = 1;
		composite.setLayout(gl_composite);
			
		Button rowSelection = new Button(composite, SWT.CHECK);
		rowSelection.setEnabled(enabled);
		
		Map<Integer, TextGridColumnLayout> columns = textGridRow.getTextGridColumns();
		for(int columnNumber:columns.keySet()){
			Text text = new Text(composite, SWT.BORDER);
			if(!columns.get(columnNumber).grabHorizantalAccessSpace()){
				GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_text.widthHint = columns.get(columnNumber).getColumnWidth();
				text.setLayoutData(gd_text);
				
				text.setEditable(columns.get(columnNumber).isEditable());
				text.setEnabled(columns.get(columnNumber).isEnabled());
			}else{
				text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				text.setBounds(0, 0, 76, 21);
				text.setFocus();
				
				text.setEditable(columns.get(columnNumber).isEditable());
				text.setEnabled(columns.get(columnNumber).isEnabled());
			}
			
			if(rowData!=null)
				text.setText(rowData.get(columnNumber));
		}
		
		return composite;
	}
}

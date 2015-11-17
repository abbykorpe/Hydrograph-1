package com.bitwise.app.parametergrid.textgridwidget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;

import com.bitwise.app.parametergrid.textgridwidget.columns.TextGridColumnLayout;
import com.bitwise.app.parametergrid.textgridwidget.columns.TextGridRowLayout;
import com.bitwise.app.parametergrid.textgridwidget.rows.TextGridRowBuilder;

public class TextGrid {
	/*private Composite container;
	
	private int numberOfRows;
	
	private Composite innerComposite;	
	private ScrolledComposite textGridComposite;
	private ColumnLayoutData textGridCompositeLayoutData;
	
	private TextGridRowBuilder textGridRowBuilder;
		
	private Composite lastAddedRow;
	private int usedRows=0;*/
	
	//================================
	
	private Composite container;
	private List<Composite> textGrid;
	private Map<Integer,TextGridRowLayout> rowLayouts;
	
	private Composite innerComposite;	
	private ScrolledComposite textGridComposite;
	private ColumnLayoutData textGridCompositeLayoutData;
	
	private Composite lastAddedRow;
	//---------
	
	public TextGrid(Composite container){
		this.container = container;
		textGrid = new LinkedList<>();
		rowLayouts = new LinkedHashMap<>();
		initializeGridComposites();
	}
	
	
	public void addRow(TextGridRowLayout rowLayout,List<String> rowData){
		 TextGridRowBuilder textGridRowBuilder = new TextGridRowBuilder(innerComposite, rowLayout,rowData);	
		 lastAddedRow = textGridRowBuilder.addRaw();
		 textGrid.add(lastAddedRow);
	}
	
	public void addEmptyRow(TextGridRowLayout rowLayout){
		 TextGridRowBuilder textGridRowBuilder = new TextGridRowBuilder(innerComposite, rowLayout,null);	
		 lastAddedRow = textGridRowBuilder.addRaw();
		 textGrid.add(lastAddedRow);
	}
	
	public void addDisabledRow(TextGridRowLayout rowLayout,List<String> rowData){
		 TextGridRowBuilder textGridRowBuilder = new TextGridRowBuilder(innerComposite, rowLayout,rowData,false);	
		 lastAddedRow = textGridRowBuilder.addRaw();
		 textGrid.add(lastAddedRow);
	}
		
	public List<Composite> getGrid(){
		return textGrid;
	}
		
	public void scrollToLastRow(){
		textGridComposite.showControl(lastAddedRow.getChildren()[0]);
	}
	
	private void initializeGridComposites(){
		textGridComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		textGridCompositeLayoutData = new ColumnLayoutData();
		textGridCompositeLayoutData.heightHint = 267;
		textGridComposite.setLayoutData(textGridCompositeLayoutData);
		textGridComposite.setExpandVertical(true);
		textGridComposite.setExpandHorizontal(true);
		
		innerComposite = new Composite(textGridComposite, SWT.NONE);
		ColumnLayout cl_composite_1 = new ColumnLayout();
		cl_composite_1.maxNumColumns = 1;
		innerComposite.setLayout(cl_composite_1);
		
		textGridComposite.setContent(innerComposite);
	}
		
	public void refresh(){
		textGridComposite.setMinSize(innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		innerComposite.setBounds(innerComposite.getBounds().x, innerComposite.getBounds().y, innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	}
	
	public void setHeight(int height){
		textGridCompositeLayoutData.heightHint = height;
	}
		
	public List<List<String>> getData(){
		List<List<String>> gridData = new LinkedList<>();
		for(int i=0;i<textGrid.size();i++){
			Control[] rowColumns = textGrid.get(i).getChildren();
			//String[] rowData = new String[rowColumns.length];
			List<String> rowData = new LinkedList<>();
			for(int j=1;j<rowColumns.length;j++){
					//rowData[j] = ((Text)rowColumns[j]).getText();
				rowData.add(((Text)rowColumns[j]).getText());
			}
			
			gridData.add(rowData);
		}
		return gridData;
	}
	
	public void removeSelectedRows(){
		List<Control> removedRows = new LinkedList<>();
		for(Control row : textGrid){
			//rows.dispose();
			for(Control column : ((Composite)row).getChildren()){
				if(column instanceof Button){
					if(((Button)column).getSelection()){
						row.dispose();
						removedRows.add(row);
						break;
					}
				}				
			}
		}
		
		for(Control removedRow: removedRows){
			textGrid.remove(removedRow);
		}
		
		refresh();
	}
}

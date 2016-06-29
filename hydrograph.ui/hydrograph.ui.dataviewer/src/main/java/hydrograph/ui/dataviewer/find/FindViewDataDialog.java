/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package hydrograph.ui.dataviewer.find;

import java.util.LinkedList;

import hydrograph.ui.dataviewer.constants.Views;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.logging.factory.LogFactory;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;


/**
 * The Class FindViewDataDialog
 * @author Bitwise
 *
 */
public class FindViewDataDialog extends Dialog{

	private static final Logger logger = LogFactory.INSTANCE.getLogger(FindViewDataDialog.class);
	public static final int CLOSE = 9999;
	private TableViewer debugDataViewer;
	private StyledText formatedStyledText;
	private CTabItem cTabItem;
	private Text text;
	private int findRowIndex=0;
	private int findColIndex=0;
	private int cursor = -1;
	private int prevColSelection = 0;
	private int prevRowSelection = 0;
	
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param debugDataViewer 
	 */
	public FindViewDataDialog(Shell parentShell, TableViewer debugDataViewer, StyledText formatedStyledText, StyledText unFormatedStyledText, 
			CTabItem cTabItem) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.RESIZE);
		this.debugDataViewer = debugDataViewer;
		this.formatedStyledText = formatedStyledText;
		this.cTabItem = cTabItem;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText("Find");
		GridData parentCompositeData = new GridData(SWT.FILL, SWT.BEGINNING, true, true, 3, 3);
		parentCompositeData.heightHint = 150;
		parentCompositeData.widthHint = 300;
		parentCompositeData.grabExcessHorizontalSpace = true;
		parentCompositeData.grabExcessVerticalSpace = true;
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(parentCompositeData);
		

		container.getShell().addShellListener(new ShellListener() {
			@Override
			public void shellIconified(ShellEvent e) {}
			@Override
			public void shellDeiconified(ShellEvent e) {}
			@Override
			public void shellDeactivated(ShellEvent e) {	}
			@Override
			public void shellClosed(ShellEvent e) {
				CTabItem tabItem = cTabItem;
				if (tabItem.getData("VIEW_NAME").equals(Views.GRID_VIEW_NAME)) {
					logger.debug("Grid View");
					changeTableItemBgColor(debugDataViewer);
				} else if (tabItem.getData("VIEW_NAME").equals(Views.HORIZONTAL_VIEW_NAME)) {
					logger.debug("HORIZONTAL View");
				} else if (tabItem.getData("VIEW_NAME").equals(Views.FORMATTED_VIEW_NAME)) {
					logger.debug("FORMATTED View");
				} else if (tabItem.getData("VIEW_NAME").equals(Views.UNFORMATTED_VIEW_NAME)) {
					logger.debug("UNFORMATTED View");
				}
			}
			@Override
			public void shellActivated(ShellEvent e) {}
		});
		
		Composite composite_1 = new Composite(container, SWT.BORDER);
		GridLayout generalGroupLayout = new GridLayout(2, true);
		generalGroupLayout.verticalSpacing = 0;
		generalGroupLayout.marginWidth = 0;
		generalGroupLayout.marginHeight = 0;
		generalGroupLayout.horizontalSpacing = 0;
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, true, 3, 3);
		gridData.heightHint = 66;
		gridData.widthHint = 240;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		composite_1.setLayoutData(gridData);
		composite_1.setLayout(generalGroupLayout);
		
		Composite composite = new Composite(composite_1, SWT.None);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_composite.widthHint = 210;
		composite.setLayoutData(gd_composite);
		
		Label lblFind = new Label(composite, SWT.NONE);
		lblFind.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFind.setText("Find:  ");
		
		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		new Label(composite_1, SWT.None).setVisible(false);
		
		Composite composite_2 = new Composite(composite_1, SWT.None);
		GridData gd_composite_2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_2.heightHint = 40;
		gd_composite_2.widthHint = 230;
		composite_2.setLayoutData(gd_composite_2);
		
		final Button btnPrevious = new Button(composite_2, SWT.NONE);
		btnPrevious.setBounds(0, 0, 75, 25);
		btnPrevious.setText("Prev");
		btnPrevious.setEnabled(false);
		btnPrevious.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CTabItem tabItem = cTabItem;
				if (tabItem.getData("VIEW_NAME").equals(Views.GRID_VIEW_NAME)) {
					logger.debug("Grid View");
					reverseTableTraverse();
				} else if (tabItem.getData("VIEW_NAME").equals(Views.HORIZONTAL_VIEW_NAME)) {
					logger.debug("HORIZONTAL View");
				} else if (tabItem.getData("VIEW_NAME").equals(Views.FORMATTED_VIEW_NAME)) {
					logger.debug("FORMATTED View");
				} else if (tabItem.getData("VIEW_NAME").equals(Views.UNFORMATTED_VIEW_NAME)) {
					logger.debug("UNFORMATTED View");
				}
			}
		});
		
		final Button btnNext = new Button(composite_2, SWT.NONE);
		btnNext.setBounds(81, 0, 78, 25);
		btnNext.setText("Next");
		btnNext.setEnabled(false);
		GridData gd_btnNext = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		btnNext.setLayoutData(gd_btnNext);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CTabItem tabItem = cTabItem;
				if (tabItem.getData("VIEW_NAME").equals(Views.GRID_VIEW_NAME)) {
					logger.debug("Grid View");
					forwardTableTraverse();
				} else if (tabItem.getData("VIEW_NAME").equals(Views.HORIZONTAL_VIEW_NAME)) {
					logger.debug("HORIZONTAL View");
				} else if (tabItem.getData("VIEW_NAME").equals(Views.FORMATTED_VIEW_NAME)) {
					logger.debug("FORMATTED View");
				} else if (tabItem.getData("VIEW_NAME").equals(Views.UNFORMATTED_VIEW_NAME)) {
					logger.debug("UNFORMATTED View");
				}
			}
		});
		
		final Button btnAll = new Button(composite_2, SWT.NONE);
		btnAll.setBounds(166, 0, 78, 25);
		btnAll.setText("All");
		btnAll.setEnabled(false);
		GridData gd_btnNewButton = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		btnAll.setLayoutData(gd_btnNewButton);
		btnAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CTabItem tabItem = cTabItem;
				if (tabItem.getData("VIEW_NAME").equals(Views.GRID_VIEW_NAME)) {
					logger.debug("Grid View");
					selectAllInTable();
				} else if (tabItem.getData("VIEW_NAME").equals(Views.HORIZONTAL_VIEW_NAME)) {
					logger.debug("HORIZONTAL View");
				} else if (tabItem.getData("VIEW_NAME").equals(Views.FORMATTED_VIEW_NAME)) {
					logger.debug("FORMATTED View");
					CharSequence cs = text.getText();
					if(formatedStyledText.getText().contains(cs)){
						formatedStyledText.redraw();
					}
				} else if (tabItem.getData("VIEW_NAME").equals(Views.UNFORMATTED_VIEW_NAME)) {
					logger.debug("UNFORMATTED View");
				}
			}
		});
	 
		styleListnr(formatedStyledText);
		
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				Text txt = (Text)event.widget;
				if(StringUtils.isNotBlank(txt.getText())){
					btnPrevious.setEnabled(true);
					btnNext.setEnabled(true);
					btnAll.setEnabled(true);
				}else{
					btnPrevious.setEnabled(false);
					btnNext.setEnabled(false);
					btnAll.setEnabled(false);
				}
			}
		});

		//findEditor();
		
		return container;
	}
	
	private void styleListnr(StyledText styledText){
		if(styledText == null){
			return;
		}
		styledText.addLineStyleListener(new LineStyleListener() {
			@Override
			public void lineGetStyle(LineStyleEvent event) {
				String line = event.lineText;
				LinkedList list = new LinkedList();
			        while( (cursor = line.indexOf(text.getText(), cursor+1)) >= 0) {
			          list.add(getHighlightStyle(event.lineOffset+cursor, text.getText().length()));
			          event.styles = (StyleRange[]) list.toArray(new StyleRange[list.size()]);
			          break;
			        }
			}
			});
	}
	
	private void  findEditor(){
		CTabItem tabItem = cTabItem;
		if (tabItem.getData("VIEW_NAME").equals(Views.GRID_VIEW_NAME)) {
			logger.debug("Grid View");
		} else if (tabItem.getData("VIEW_NAME").equals(Views.HORIZONTAL_VIEW_NAME)) {
			logger.debug("HORIZONTAL View");
		} else if (tabItem.getData("VIEW_NAME").equals(Views.FORMATTED_VIEW_NAME)) {
			logger.debug("FORMATTED View");
		} else if (tabItem.getData("VIEW_NAME").equals(Views.UNFORMATTED_VIEW_NAME)) {
			logger.debug("UNFORMATTED View");
		}
	}
	
	private void forwardTableTraverse(){
		if(debugDataViewer.getData("SELECTED_ROW_INDEX")!=null){
			TableItem previousSelectedTableItem=debugDataViewer.getTable().getItem((int) debugDataViewer.getData("SELECTED_ROW_INDEX"));
			int colIndex=(int) debugDataViewer.getData("SEELCTED_COLUMN_INDEX");
			previousSelectedTableItem.setBackground(colIndex, Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		}
		Table table = debugDataViewer.getTable();
		TableItem[] tableItems = table.getItems();
		if(findColIndex == prevColSelection && findRowIndex == prevRowSelection){
			findColIndex++;
		}
		if(findRowIndex<0){findRowIndex =0;}
		for(;findRowIndex<tableItems.length;findRowIndex++){
			TableItem tableItem = tableItems[findRowIndex];
			for(;findColIndex <= table.getColumnCount();findColIndex++){
				CharSequence cs = text.getText();
				if(tableItem.getText(findColIndex).contains(cs)){
					System.out.println("In Next Button Row: "+findRowIndex +" &col: "+findColIndex);
					table.showItem(tableItem);
					table.showColumn(table.getColumn(findColIndex));
					tableItem.setBackground(findColIndex,Display.getDefault().getSystemColor(SWT.COLOR_DARK_YELLOW));
					debugDataViewer.setData("SELECTED_ROW_INDEX", findRowIndex);
					debugDataViewer.setData("SEELCTED_COLUMN_INDEX", findColIndex);
					findColIndex++;
					return;
				}
			}
			findColIndex=0;
		}
	}
	
	private void reverseTableTraverse(){
		if(debugDataViewer.getData("SELECTED_ROW_INDEX")!=null){
			TableItem previousSelectedTableItem=debugDataViewer.getTable().getItem((int) debugDataViewer.getData("SELECTED_ROW_INDEX"));
			findRowIndex = (int) debugDataViewer.getData("SELECTED_ROW_INDEX");
			findColIndex = (int) debugDataViewer.getData("SEELCTED_COLUMN_INDEX");
			previousSelectedTableItem.setBackground(findColIndex, Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			findColIndex -= 1;
		}
		 System.out.println("Index:::"+debugDataViewer.getData("SELECTED_ROW_INDEX")+":::"+debugDataViewer.getData("SEELCTED_COLUMN_INDEX"));
		Table table = debugDataViewer.getTable();
		TableItem[] tableItems = table.getItems();
		for(; findRowIndex >=0; findRowIndex--){
			TableItem tableItem = tableItems[findRowIndex];
			if(findColIndex < 0){ findColIndex = table.getColumnCount(); }
			for( ; findColIndex >= 0 ;findColIndex--){
				CharSequence cs = text.getText();
				if(tableItem.getText(findColIndex).contains(cs)){
					System.out.println("In prev Button Row: "+findRowIndex +" &col: "+findColIndex);
					table.showItem(tableItem);
					table.showColumn(table.getColumn(findColIndex));
					tableItem.setBackground(findColIndex,Display.getDefault().getSystemColor(SWT.COLOR_DARK_YELLOW));
					debugDataViewer.setData("SELECTED_ROW_INDEX", findRowIndex);
					debugDataViewer.setData("SEELCTED_COLUMN_INDEX", findColIndex);
					prevColSelection = findColIndex;
					prevRowSelection = findRowIndex;
					return ;
				}
			}
		}
	}
	
	private void selectAllInTable(){
		findRowIndex = 0;
		findColIndex = 0;
		Table table = debugDataViewer.getTable();
		TableItem[] tableItems = table.getItems();
		
		for(;findRowIndex<tableItems.length;findRowIndex++){
			TableItem tableItem = tableItems[findRowIndex];
			for(;findColIndex <= table.getColumnCount();findColIndex++){
				CharSequence cs = text.getText();
				if(tableItem.getText(findColIndex).contains(cs)){
					table.showItem(tableItem);
					table.showColumn(table.getColumn(findColIndex));
					tableItem.setBackground(findColIndex,Display.getDefault().getSystemColor(SWT.COLOR_DARK_YELLOW));
					debugDataViewer.setData("SELECTED_ROW_INDEX", findRowIndex);
					debugDataViewer.setData("SEELCTED_COLUMN_INDEX", findColIndex);
					findColIndex++;
				}
			}
			findColIndex=0;
		}
	}
	
	  private StyleRange getHighlightStyle(int startOffset, int length) {
		    StyleRange styleRange = new StyleRange();
		    styleRange.start = startOffset;
		    styleRange.length = length;
		    styleRange.background = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
		    return styleRange;
		  }
	  
	  private void clear(StyledText styledText){
		  StyleRange style3 = new StyleRange();
		    style3.start = 0;
		    style3.length = styledText.getText().length();
		    style3.background = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		    styledText.setStyleRange(style3);
	  }
	 
	  
	  private void changeTableItemBgColor(TableViewer debugDataViewer){
		  if(debugDataViewer == null){
			  return;
		  }
		  Table table = debugDataViewer.getTable();
			TableItem[] tableItems = table.getItems();
			for(int i=0;i<tableItems.length;i++){
				TableItem tableItem = tableItems[i];
				for(int j=0;j <= table.getColumnCount()-1;j++){
					tableItem.getText(j);
					tableItem.setBackground(j,Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
					}
				}
			
			debugDataViewer.setData("SELECTED_ROW_INDEX", null);
			debugDataViewer.setData("SEELCTED_COLUMN_INDEX", null);
	  }
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(530, 168);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button closeButton = createButton(parent, CLOSE, "Close", true);
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeTableItemBgColor(debugDataViewer);
				clear(formatedStyledText);
				close();
			}
		});
	}
}

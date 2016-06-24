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

package hydrograph.ui.dataviewer.dialog;
import hydrograph.ui.common.util.ImagePathConstant;
import hydrograph.ui.common.util.XMLConfigUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * SelectColumnActionDialog allows to choose columns to be visible in data viewer
 * 
 * @author Bitwise
 *
 */
public class SelectColumnActionDialog extends Dialog {
	private java.util.List<String> allColumns;
	private java.util.List<String> selectedColumns;
	private List listAllComlumns;
	private List listSelectedColumns;
	private Label moveUpLable;
	private Label moveDownLable;
	private Button okButton;
	private static final String SELECT_COLUMNS = "Select Columns";
	private static final String ALL_COLUMNS = "All Columns";
	private static final String SELECTED_COLUMNS = "Selected Columns";
	private static final String SKIP = "skip";
	/**
	 * @param parentShell
	 * @param selectColumnAction
	 * @param selectedColumns2 
	 * @param allColumns2 
	 */
	public SelectColumnActionDialog(Shell parentShell, java.util.List<String> allColumns, java.util.List<String> selectedColumns) {
		super(parentShell);
		this.allColumns  = new ArrayList<>();
		this.allColumns.addAll(allColumns);
		this.selectedColumns = new ArrayList<>();
		this.selectedColumns.addAll(selectedColumns);
		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL | SWT.RESIZE |SWT.MIN |SWT.MAX );
	}
	
	/**
	 * Creates the SelectColumn Window
	 */
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		SashForm sashForm = new SashForm(container, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Shell shell=container.getShell();
		shell.setText(SELECT_COLUMNS);
		Image table = new Image(shell.getDisplay(),XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.TABLE_ICON);
		shell.setImage(table);
		
		Composite allColumnsComposite = new Composite(sashForm, SWT.NONE);
		allColumnsComposite.setLayout(new GridLayout(2, false));
		Label allColumnsLabel = new Label(allColumnsComposite, SWT.NONE);
		GridData gd_lblAllColumns = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblAllColumns.widthHint = 141;
		allColumnsLabel.setLayoutData(gd_lblAllColumns);
		allColumnsLabel.setText(ALL_COLUMNS);
		new Label(allColumnsComposite, SWT.NONE);
		
		listAllComlumns = new List(allColumnsComposite, SWT.BORDER|SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
		listAllComlumns.setItems(Arrays.copyOf(allColumns.toArray(),allColumns.toArray().length,String[].class));
		listAllComlumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite allColumnsControlButtons = new Composite(allColumnsComposite, SWT.NONE);
		allColumnsControlButtons.setLayout(null);
		GridData gd_allColumnsControlButtons = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_allColumnsControlButtons.widthHint = 31;
		gd_allColumnsControlButtons.heightHint = 41;
		allColumnsControlButtons.setLayoutData(gd_allColumnsControlButtons);
		
		Label selectAllLabel = new Label(allColumnsControlButtons, SWT.NONE);
		selectAllLabel.setBounds(6, 70, 25, 25);
		Image selectAllColumns = new Image(shell.getDisplay(),XMLConfigUtil.CONFIG_FILES_PATH +ImagePathConstant.SELECT_ALL_ICON);
		selectAllLabel.setImage(selectAllColumns);
		
		Label selectLabel = new Label(allColumnsControlButtons, SWT.NONE);
		selectLabel.setBounds(6, 110, 25, 25);
		Image select = new Image(shell.getDisplay(),XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.SELECT_ICON);
		selectLabel.setImage(select);
		
		Label disSelectLabel = new Label(allColumnsControlButtons, SWT.NONE);
		disSelectLabel.setBounds(6, 150, 25, 25);
		Image disSelect = new Image(shell.getDisplay(),XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.DESELECT_ICON);
		disSelectLabel.setImage(disSelect);
		
		Label removeAll = new Label(allColumnsControlButtons, SWT.NONE);
		removeAll.setBounds(6, 190, 25, 25);
		Image disSelectAll = new Image(shell.getDisplay(),XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.DESELECT_ALL_ICON);
		removeAll.setImage(disSelectAll);
		
		Composite selectColumnComposite = new Composite(sashForm, SWT.NONE);
		selectColumnComposite.setLayout(new GridLayout(2, false));
		Label selectedColumnsLabel = new Label(selectColumnComposite, SWT.NONE);
		selectedColumnsLabel.setText(SELECTED_COLUMNS);
		new Label(selectColumnComposite, SWT.NONE);
		
		listSelectedColumns = new List(selectColumnComposite, SWT.BORDER|SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
		listSelectedColumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		listSelectedColumns.setItems(Arrays.copyOf(selectedColumns.toArray(),selectedColumns.toArray().length,String[].class));

		Composite moveElementsComposite = new Composite(selectColumnComposite, SWT.NONE);
		GridData gd_moveElementsComposite = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_moveElementsComposite.heightHint = 271;
		gd_moveElementsComposite.widthHint = 25;
		moveElementsComposite.setLayoutData(gd_moveElementsComposite);
		
		moveUpLable = new Label(moveElementsComposite, SWT.NONE);
		moveUpLable.setBounds(0, 114, 24, 24);
		Image up = new Image(shell.getDisplay(),XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.UP_ICON);
		moveUpLable.setImage(up);
		
		moveDownLable = new Label(moveElementsComposite, SWT.NONE);
		moveDownLable .setBounds(0, 160, 24, 25);
		Image down= new Image(shell.getDisplay(),XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.DOWN_ICON);
		moveDownLable.setImage(down);
		sashForm.setWeights(new int[] {294, 277});

		addListeners(selectAllLabel, selectLabel, disSelectLabel, removeAll);
		return container;
	}

	/**
	 * @param selectAllLabel
	 * @param selectLabel
	 * @param disSelectLabel
	 * @param removeAll
	 * Add listeners to move data up/down/left/right
	 */
	private void addListeners(Label selectAllLabel, Label selectLabel,Label disSelectLabel, Label removeAll) {
		selectLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (listAllComlumns.getSelection().length > 0) {
					for (String string : listAllComlumns.getSelection()) {
						allColumns.remove(string);
 						listAllComlumns.remove(string);
						selectedColumns.add(string);
						listSelectedColumns.add(string);
						okButton.setEnabled(true);
					}
				}
			}
		});		
		
		disSelectLabel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseUp(MouseEvent e) {
				if (listSelectedColumns.getSelection().length > 0) {
					for (String string : listSelectedColumns.getSelection()) {
						selectedColumns.remove(string);
						listSelectedColumns.remove(string);
						allColumns.add(string);
						listAllComlumns.add(string);
					}
					if(listSelectedColumns.getItemCount()==0){
						okButton.setEnabled(false);
					}
				}
			}
		});
		
		selectAllLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (int i = 0; i < allColumns.size(); i++) {
					listSelectedColumns.add(allColumns.get(i));
				}
				selectedColumns.addAll(allColumns);
				allColumns.clear();
				listAllComlumns.removeAll();
				okButton.setEnabled(true);
			}
		});

		removeAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (int i = 0; i < selectedColumns.size(); i++) {
					listAllComlumns.add(selectedColumns.get(i));
				}
				allColumns.addAll(selectedColumns);
				selectedColumns.clear();
				listSelectedColumns.removeAll();
				if(listSelectedColumns.getItemCount()==0){
				okButton.setEnabled(false);
				}
			}
		});
		
		moveDownLable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				int[] indices = listSelectedColumns.getSelectionIndices();
				Map<Integer, String> map = new HashMap<Integer, String>();
				if (Arrays.asList(indices).contains(selectedColumns.size() - 1))
					map.put(indices[indices.length - 1], SKIP);
				for (int index = indices.length - 1; index >= 0; index--) {
					if (indices[index] < listSelectedColumns.getItemCount() - 1 && !map.containsKey(indices[index] + 1)) {
						Collections.swap(selectedColumns, indices[index],indices[index] + 1);
						listSelectedColumns.setItems(Arrays.copyOf(selectedColumns.toArray(),
								selectedColumns.toArray().length, String[].class));
						int[] temp2 = new int[indices.length];
						for (int i = 0; i < indices.length; i++) {
							if (map.containsKey(indices[i]))
								temp2[i] = indices[i];
							else
								temp2[i] = indices[i] + 1;
						}
						listSelectedColumns.setSelection(temp2);
					}else
						map.put(indices[index], SKIP);
				}
			}
		});
		
		moveUpLable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				int[] indices = listSelectedColumns.getSelectionIndices();
				Map<Integer, String> map = new HashMap<Integer, String>();
				if (Arrays.asList(indices).contains(0))
					map.put(0, SKIP);
				for (int index : indices) {
					if (index > 0 && !map.containsKey(index - 1)) {
						Collections.swap(selectedColumns, index, index - 1);
						listSelectedColumns.setItems(Arrays.copyOf(selectedColumns.toArray(),
								selectedColumns.toArray().length, String[].class));
						int[] temp2 = new int[indices.length];
						for (int i = 0; i < indices.length; i++) {
							if (map.containsKey(indices[i]))
								temp2[i] = indices[i];
							else
								temp2[i] = indices[i] - 1;
						}
						listSelectedColumns.setSelection(temp2);
					} else
						map.put(index, SKIP);
				}
			}

		});
		
		listSelectedColumns.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e){
				
				if (listSelectedColumns.getSelection().length > 0) {
					for (String string : listSelectedColumns.getSelection()) {
						selectedColumns.remove(string);
						listSelectedColumns.remove(string);
						allColumns.add(string);
						listAllComlumns.add(string);
					}
					if(listSelectedColumns.getItemCount()==0){
					okButton.setEnabled(false);
					}
				}
			}
		});
		
		listAllComlumns.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (listAllComlumns.getSelection().length > 0) {
					for (String string : listAllComlumns.getSelection()) {
						allColumns.remove(string);
						listAllComlumns.remove(string);
						selectedColumns.add(string);
						listSelectedColumns.add(string);
						okButton.setEnabled(true);
					}
				}
			}

		});
	}
	
	/**
	 * Initialize the window
	 */
	protected Point getInitialSize() {
		return new Point(600, 400);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton=createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,true);
		if(listSelectedColumns.getItemCount()==0){
			okButton.setEnabled(false);
		}
		createButton(parent, IDialogConstants.CANCEL_ID,IDialogConstants.CANCEL_LABEL, false);
	}
	@Override
	public boolean close() {
		return super.close();
	}

	/**
	 * Return all columns List
	 * @return List
	 */
	public java.util.List<String> getAllColumns() {
		return allColumns;
	}

	/**
	 * Return list for selected columns
	 * @return List
	 */
	public java.util.List<String> getSelectedColumns() {
		return selectedColumns;
	}
}

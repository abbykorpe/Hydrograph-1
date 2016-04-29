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

package hydrograph.ui.parametergrid.dialog;

import hydrograph.ui.common.util.ImagePathConstant;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.common.util.XMLUtil;
import hydrograph.ui.datastructures.parametergrid.ParameterFile;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.parametergrid.constants.ErrorMessages;
import hydrograph.ui.parametergrid.constants.MessageType;
import hydrograph.ui.parametergrid.constants.MultiParameterFileDialogConstants;
import hydrograph.ui.parametergrid.dialog.models.Parameter;
import hydrograph.ui.parametergrid.dialog.models.ParameterWithFilePath;
import hydrograph.ui.parametergrid.dialog.support.ParameterEditingSupport;
import hydrograph.ui.parametergrid.utils.ParameterFileManager;
import hydrograph.ui.parametergrid.utils.SWTResourceManager;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.SerializationException;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;
import org.slf4j.Logger;


/**
 * 
 * UI for multi parameter file dialog
 * 
 * @author Bitwise
 * 
 */
public class MultiParameterFileDialog extends Dialog {
	private static final int PROPERTY_VALUE_COLUMN_INDEX = 1;

	private static final Logger logger = LogFactory.INSTANCE.getLogger(MultiParameterFileDialog.class);

	private CheckboxTableViewer filePathTableViewer;
	private TableViewer parameterTableViewer;
	private TableViewer parameterSearchTableViewer;
	private Text parameterFileTextBox;

	private List<ParameterFile> parameterFiles;
	private List<Parameter> parameters;
	private List<ParameterWithFilePath> parameterSearchBoxItems;
	private List<ParameterWithFilePath> parameterSearchBoxItemsFixed;
	private Image checkAllImage;
	private Image uncheckAllImage;
	private boolean selectAllFiles = true;
	private String activeProjectLocation;
	
	private final String HELP_LINK_TOOLTIP_TEXT="Only the check files will be considered while executing job\nand will be passed to job in same sequence as they are in grid";
	private boolean runGraph;
	
	private static final String DROP_BOX_TEXT = "\nDrop parameter file here to delete";
	private boolean okPressed;
	
	private static final Base64 base64 = new Base64();
	
	
	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	@Deprecated
	public MultiParameterFileDialog(Shell parentShell) {
		super(parentShell);
		if (parameterFiles == null)
			parameterFiles = new LinkedList<>();

		parameters = new LinkedList<>();
		parameterSearchBoxItems = new LinkedList<>();
		parameterSearchBoxItemsFixed = new LinkedList<>();
		checkAllImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.CHECKALL_ICON);
		uncheckAllImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.UNCHECKALL_ICON);
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public MultiParameterFileDialog(Shell parentShell, String activeProjectLocation) {
		super(parentShell);
		if (parameterFiles == null)
			parameterFiles = new LinkedList<>();

		parameters = new LinkedList<>();
		parameterSearchBoxItems = new LinkedList<>();
		parameterSearchBoxItemsFixed = new LinkedList<>();
		checkAllImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.CHECKALL_ICON);
		uncheckAllImage = new Image(null, XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.UNCHECKALL_ICON);

		this.activeProjectLocation = activeProjectLocation;
		

	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		getShell().setText(MultiParameterFileDialogConstants.PARAMETER_FILE_DIALOG_TEXT);
		
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));

		createParameterFilesBox(container);
		populateFilePathTableViewer();

		Composite composite = createParameterFileViewOuterComposite(container);
		createViewParameterFileBox(composite);
		ParameterFile jobSpecificFile = getJobSpecificFile();
		if (jobSpecificFile != null)
			populateViewParameterFileBox(jobSpecificFile);

		createParameterSearchBox(composite);

		return container;
	}

	private ParameterFile getJobSpecificFile() {
		ParameterFile jobSpecificFile = null;
		for (ParameterFile filePath : parameterFiles) {
			if (filePath.isJobSpecificFile()) {
				jobSpecificFile = filePath;
				break;
			}
		}
		return jobSpecificFile;
	}

	private void populateViewParameterFileBox(ParameterFile file) {
		parameterFileTextBox.setText(file.getPath());
		try {
			ParameterFileManager parameterFileManager = new ParameterFileManager(file.getPath());
			Map<String, String> parameterMap = new LinkedHashMap<>();
			parameterMap = parameterFileManager.getParameterMap();
			setGridData(parameters, parameterMap);
			parameterTableViewer.setData("CURRENT_PARAM_FILE", file.getPath());
		} catch (IOException ioException) {

			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);

			messageBox.setText(MessageType.ERROR.messageType());
			messageBox.setMessage(ErrorMessages.UNABLE_TO_POPULATE_PARAM_FILE + ioException.getMessage());
			messageBox.open();

			logger.debug("Unable to populate parameter file", ioException);

		}

		parameterTableViewer.refresh();
	}

	private void searchParameter(String text) {
		parameterSearchBoxItems.clear();

		for (ParameterWithFilePath parameterSearchBoxItem : parameterSearchBoxItemsFixed) {
			if (parameterSearchBoxItem.toString().toLowerCase().contains(text)) {
				parameterSearchBoxItems.add(parameterSearchBoxItem);
			}
		}
	}

	private void createParameterSearchBox(Composite composite) {
		Group grpAllProperties = new Group(composite, SWT.NONE);
		GridLayout gl_grpAllProperties = new GridLayout(1, false);
		gl_grpAllProperties.horizontalSpacing = 0;
		gl_grpAllProperties.verticalSpacing = 0;
		gl_grpAllProperties.marginHeight = 0;
		gl_grpAllProperties.marginWidth = 0;
		grpAllProperties.setLayout(gl_grpAllProperties);
		ColumnLayoutData cld_grpAllProperties = new ColumnLayoutData();
		cld_grpAllProperties.heightHint = 302;
		grpAllProperties.setLayoutData(cld_grpAllProperties);
		grpAllProperties.setText(MultiParameterFileDialogConstants.SEARCH_ALL_PARAMETERS);

		Composite composite_5 = new Composite(grpAllProperties, SWT.NONE);
		ColumnLayout cl_composite_5 = new ColumnLayout();
		cl_composite_5.rightMargin = 0;
		cl_composite_5.leftMargin = 0;
		cl_composite_5.maxNumColumns = 1;
		composite_5.setLayout(cl_composite_5);
		GridData gd_composite_5 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_composite_5.heightHint = 301;
		gd_composite_5.widthHint = 428;
		composite_5.setLayoutData(gd_composite_5);

		Composite composite_6 = new Composite(composite_5, SWT.NONE);
		composite_6.setLayout(new GridLayout(2, false));
		ColumnLayoutData cld_composite_6 = new ColumnLayoutData();
		cld_composite_6.widthHint = 415;
		cld_composite_6.heightHint = 33;
		composite_6.setLayoutData(cld_composite_6);

		Label lblSearch = new Label(composite_6, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSearch.setText("Search");

		final Text text_1 = new Text(composite_6, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_1.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (text_1.getText().isEmpty()) {
					populateParameterSearchBox();
				} else {
					searchParameter(text_1.getText().toLowerCase());
				}

				parameterSearchTableViewer.refresh();
			}
		});

		Composite composite_7 = new Composite(composite_5, SWT.NONE);
		composite_7.setLayout(new GridLayout(1, false));
		ColumnLayoutData cld_composite_7 = new ColumnLayoutData();
		cld_composite_7.widthHint = 413;
		cld_composite_7.heightHint = 258;
		composite_7.setLayoutData(cld_composite_7);

		parameterSearchTableViewer = new TableViewer(composite_7, SWT.BORDER | SWT.FULL_SELECTION);
		Table table_1 = parameterSearchTableViewer.getTable();
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		GridData gd_table_1 = new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1);
		gd_table_1.widthHint = 404;
		table_1.setLayoutData(gd_table_1);
		parameterSearchTableViewer.setContentProvider(new ArrayContentProvider());
		ColumnViewerToolTipSupport.enableFor(parameterSearchTableViewer, ToolTip.NO_RECREATE);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(parameterSearchTableViewer, SWT.NONE);
		TableColumn tblclmnFilePath_1 = tableViewerColumn.getColumn();
		tblclmnFilePath_1.setWidth(138);
		tblclmnFilePath_1.setText(MultiParameterFileDialogConstants.TABLE_COLUMN_LIST_OF_PARAMETER_FILES);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getToolTipText(Object element) {
				String tooltip = MultiParameterFileDialogConstants.PARAMETER_FILE + ": "
						+ ((ParameterWithFilePath) element).getParameterFile().getFilePathViewString() + "\n "
						+ MultiParameterFileDialogConstants.PARAMETER_NAME + ": "
						+ ((ParameterWithFilePath) element).getParameterName() + "\n "
						+ MultiParameterFileDialogConstants.PARAMETER_VALUE + ": "
						+ ((ParameterWithFilePath) element).getParameterValue();
				return tooltip;
			}

			@Override
			public Point getToolTipShift(Object object) {
				return new Point(5, 5);
			}

			@Override
			public int getToolTipDisplayDelayTime(Object object) {
				return 100; // msec
			}

			@Override
			public int getToolTipTimeDisplayed(Object object) {
				return 5000; // msec
			}

			@Override
			public Color getToolTipBackgroundColor(Object object) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			}

			@Override
			public String getText(Object element) {
				ParameterWithFilePath p = (ParameterWithFilePath) element;
				return p.getParameterFile().getFilePathViewString();
			}
		});

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(parameterSearchTableViewer, SWT.NONE);
		TableColumn tblclmnParameterName = tableViewerColumn_1.getColumn();
		tblclmnParameterName.setWidth(140);
		tblclmnParameterName.setText(MultiParameterFileDialogConstants.PARAMETER_NAME);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getToolTipText(Object element) {
				String tooltip = MultiParameterFileDialogConstants.PARAMETER_FILE + ": "
						+ ((ParameterWithFilePath) element).getParameterFile().getFilePathViewString() + "\n "
						+ MultiParameterFileDialogConstants.PARAMETER_NAME + ": "
						+ ((ParameterWithFilePath) element).getParameterName() + "\n "
						+ MultiParameterFileDialogConstants.PARAMETER_VALUE + ": "
						+ ((ParameterWithFilePath) element).getParameterValue();
				return tooltip;
			}

			@Override
			public Point getToolTipShift(Object object) {
				return new Point(5, 5);
			}

			@Override
			public int getToolTipDisplayDelayTime(Object object) {
				return 100; // msec
			}

			@Override
			public int getToolTipTimeDisplayed(Object object) {
				return 5000; // msec
			}

			@Override
			public Color getToolTipBackgroundColor(Object object) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			}

			@Override
			public String getText(Object element) {
				ParameterWithFilePath p = (ParameterWithFilePath) element;
				return p.getParameterName();
			}
		});

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(parameterSearchTableViewer, SWT.NONE);
		TableColumn tblclmnParameterValue = tableViewerColumn_2.getColumn();
		tblclmnParameterValue.setWidth(140);
		tblclmnParameterValue.setText(MultiParameterFileDialogConstants.PARAMETER_VALUE);
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getToolTipText(Object element) {
				String tooltip = MultiParameterFileDialogConstants.PARAMETER_FILE + ": "
						+ ((ParameterWithFilePath) element).getParameterFile().getFilePathViewString() + "\n "
						+ MultiParameterFileDialogConstants.PARAMETER_NAME + ": "
						+ ((ParameterWithFilePath) element).getParameterName() + "\n "
						+ MultiParameterFileDialogConstants.PARAMETER_VALUE + ": "
						+ ((ParameterWithFilePath) element).getParameterValue();
				return tooltip;
			}

			@Override
			public Point getToolTipShift(Object object) {
				return new Point(5, 5);
			}

			@Override
			public int getToolTipDisplayDelayTime(Object object) {
				return 100; // msec
			}

			@Override
			public int getToolTipTimeDisplayed(Object object) {
				return 5000; // msec
			}

			@Override
			public Color getToolTipBackgroundColor(Object object) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			}

			@Override
			public String getText(Object element) {
				return ((ParameterWithFilePath) element).getParameterValue();
			}
		});

		populateParameterSearchBox();
	}

	private void populateParameterSearchBox() {

		parameterSearchBoxItems.clear();
		parameterSearchBoxItemsFixed.clear();

		for (ParameterFile filePath : parameterFiles) {
			try {
				ParameterFileManager parameterFileManager = new ParameterFileManager(filePath.getPath());
				Map<String, String> parameterMap = new LinkedHashMap<>();
				parameterMap = parameterFileManager.getParameterMap();

				for (String paramater : parameterMap.keySet()) {
					ParameterWithFilePath parameterWithFilePath = new ParameterWithFilePath(paramater,
							parameterMap.get(paramater), filePath);

					if (!parameterSearchBoxItems.contains(parameterWithFilePath))
						parameterSearchBoxItems.add(parameterWithFilePath);
				}

			} catch (IOException ioException) {
				ioException.printStackTrace();
			}

		}
		if (parameterSearchBoxItems.size() != 0) {
			parameterSearchTableViewer.setInput(parameterSearchBoxItems);
			parameterSearchBoxItemsFixed.addAll(parameterSearchBoxItems);
		}

		parameterSearchTableViewer.refresh();
	}

	private void setGridData(List<Parameter> parameterList, Map<String, String> parameters) {
		parameterList.clear();
		for (String parameter : parameters.keySet()) {
			parameterList.add(new Parameter(parameter, parameters.get(parameter)));
		}
	}

	private void createViewParameterFileBox(Composite composite) {
		Group grpPropertyFileView = new Group(composite, SWT.NONE);
		ColumnLayout cl_grpPropertyFileView = new ColumnLayout();
		cl_grpPropertyFileView.bottomMargin = 0;
		cl_grpPropertyFileView.verticalSpacing = 0;
		cl_grpPropertyFileView.rightMargin = 1;
		cl_grpPropertyFileView.topMargin = 20;
		cl_grpPropertyFileView.maxNumColumns = 1;
		grpPropertyFileView.setLayout(cl_grpPropertyFileView);
		ColumnLayoutData cld_grpPropertyFileView = new ColumnLayoutData();
		cld_grpPropertyFileView.horizontalAlignment = ColumnLayoutData.LEFT;
		cld_grpPropertyFileView.widthHint = 427;
		cld_grpPropertyFileView.heightHint = 246;
		grpPropertyFileView.setLayoutData(cld_grpPropertyFileView);
		grpPropertyFileView.setText(MultiParameterFileDialogConstants.PARAMETER_FILE_VIEW);

		Composite composite_4 = new Composite(grpPropertyFileView, SWT.NONE);
		composite_4.setLayout(new GridLayout(1, false));
		ColumnLayoutData cld_composite_4 = new ColumnLayoutData();
		cld_composite_4.widthHint = 402;
		cld_composite_4.heightHint = 240;
		composite_4.setLayoutData(cld_composite_4);

		Composite composite_8 = new Composite(composite_4, SWT.NONE);
		ColumnLayout cl_composite_8 = new ColumnLayout();
		cl_composite_8.maxNumColumns = 5;
		composite_8.setLayout(cl_composite_8);
		GridData gd_composite_8 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_8.widthHint = 390;
		composite_8.setLayoutData(gd_composite_8);

		Button btnAdd_1 = new Button(composite_8, SWT.NONE);
		btnAdd_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Parameter parameter = new Parameter(MultiParameterFileDialogConstants.DefaultParameter, MultiParameterFileDialogConstants.DefaultValue);
				parameters.add(parameter);
				parameterTableViewer.refresh();
			}
		});
		btnAdd_1.setText(MultiParameterFileDialogConstants.ADD_BUTTON_TEXT);

		Button btnDelete = new Button(composite_8, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table table = parameterTableViewer.getTable();
				int selectionIndex = table.getSelectionIndex();
				int[] indexs=table.getSelectionIndices();
				if (selectionIndex == -1) {
					WidgetUtility.errorMessage(ErrorMessages.SELECT_ROW_TO_DELETE);
				} else {
					table.remove(indexs);
					List<Parameter> paremetersToRemove= new ArrayList<>();
					for (int index :indexs) { 
						Parameter parameter = parameters.get(index);
						paremetersToRemove.add(parameter);
					}
					parameters.removeAll(paremetersToRemove);
					parameterTableViewer.getTable().removeAll();
					parameterTableViewer.refresh();
				}
			}
		});
		btnDelete.setText(MultiParameterFileDialogConstants.DELETE_BUTTON_TEXT);

		Button btnUp = new Button(composite_8, SWT.NONE);
		btnUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table table = parameterTableViewer.getTable();
				int[] indexes = table.getSelectionIndices();
				for (int index : indexes) {

					if (index > 0) {
						Collections.swap((List<Parameter>) parameters, index, index - 1);
						parameterTableViewer.refresh();

					}
				}
			}
		});
		btnUp.setText(MultiParameterFileDialogConstants.UP_BUTTON_TEXT);

		Button btnDown = new Button(composite_8, SWT.NONE);
		btnDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table table = parameterTableViewer.getTable();
				int[] indexes = table.getSelectionIndices();
				for (int i = indexes.length - 1; i > -1; i--) {

					if (indexes[i] < parameters.size() - 1) {
						Collections.swap((List<Parameter>) parameters, indexes[i], indexes[i] + 1);
						parameterTableViewer.refresh();

					}
				}
			}
		});
		btnDown.setText(MultiParameterFileDialogConstants.DOWN_BUTTON_TEXT);

		Button btnSave = new Button(composite_8, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveParameters();
			}
		});
		btnSave.setText(MultiParameterFileDialogConstants.SAVE_BUTTON_TEXT);

		parameterTableViewer = new TableViewer(composite_4, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		Table table_2 = parameterTableViewer.getTable();
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);
		GridData gd_table_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table_2.heightHint = 188;
		gd_table_2.widthHint = 305;
		table_2.setLayoutData(gd_table_2);
		parameterTableViewer.setContentProvider(new ArrayContentProvider());

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(parameterTableViewer, SWT.NONE);
		TableColumn tblclmnParameterName_1 = tableViewerColumn_3.getColumn();
		tblclmnParameterName_1.setWidth(146);
		tblclmnParameterName_1.setText(MultiParameterFileDialogConstants.PARAMETER_NAME);
		tableViewerColumn_3.setEditingSupport(new ParameterEditingSupport(parameterTableViewer,
				MultiParameterFileDialogConstants.PARAMETER_NAME));
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Parameter p = (Parameter) element;
				return p.getParameterName();
			}
		});

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(parameterTableViewer, SWT.NONE);
		TableColumn tblclmnParameterValue_1 = tableViewerColumn_5.getColumn();
		tblclmnParameterValue_1.setWidth(189);
		tblclmnParameterValue_1.setText(MultiParameterFileDialogConstants.PARAMETER_VALUE);
		tableViewerColumn_5.setEditingSupport(new ParameterEditingSupport(parameterTableViewer,
				MultiParameterFileDialogConstants.PARAMETER_VALUE));
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Parameter p = (Parameter) element;
				return p.getParameterValue();
			}
		});
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(parameterTableViewer, SWT.NONE);
		TableColumn tblclmnEdit = tableViewerColumn.getColumn();
		tblclmnEdit.setWidth(72);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {			
			
			@Override
			public void update(ViewerCell cell) {
				final TableItem item = (TableItem) cell.getItem();

				//DO NOT REMOVE THIS CONDITION. The condition is return to prevent multiple updates on single item
				if(item.getData("UPDATED") == null){
					item.setData("UPDATED", "TRUE");	
				}else{
					return;
				}
								
				final Composite buttonPane = new Composite(parameterTableViewer.getTable(), SWT.NONE);
				buttonPane.setLayout(new FillLayout());

				final Button button = new Button(buttonPane, SWT.NONE);
				button.setText(MultiParameterFileDialogConstants.EDIT_BUTTON_TEXT);

				final TableEditor editor = new TableEditor(parameterTableViewer.getTable());
				editor.grabHorizontal = true;
				editor.grabVertical = true;
				editor.setEditor(buttonPane, item, cell.getColumnIndex());
				editor.layout();
				
				button.addSelectionListener(new SelectionAdapter() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {						
						String initialParameterValue = item.getText(PROPERTY_VALUE_COLUMN_INDEX);
						ParamterValueDialog paramterValueDialog = new ParamterValueDialog(
								getShell(), XMLUtil.formatXML(initialParameterValue));
						paramterValueDialog.open();

						int index = Arrays.asList(
								parameterTableViewer.getTable().getItems())
								.indexOf(item);
						
						if(StringUtils.isNotEmpty(paramterValueDialog.getParamterValue())){
							String newParameterValue = paramterValueDialog
									.getParamterValue().replaceAll("\r", "")
									.replaceAll("\n", "").replaceAll("\t", "")
									.replace("  ", "");
							parameters.get(index).setParameterValue(newParameterValue);
						}
						
						parameterTableViewer.refresh();
					}
				});
				
				item.addDisposeListener(new DisposeListener() {
					
					@Override
					public void widgetDisposed(DisposeEvent e) {
						button.dispose();
						buttonPane.dispose();
						editor.dispose();
					}
				});
			}
		});
		
		
		parameterTableViewer.setInput(parameters);
	}
		

	private void saveParameters() {
		if (!parameterFileTextBox.getText().isEmpty()) {
			String currentFilePath = (String) parameterTableViewer.getData(MultiParameterFileDialogConstants.CURRENT_PARAM_FILE);
			ParameterFileManager parameterFileManager = new ParameterFileManager(currentFilePath);
			Map<String, String> parameterMap = new LinkedHashMap<>();
			for (Parameter parameter : parameters) {
				parameterMap.put(parameter.getParameterName(), parameter.getParameterValue());
			}
			try {
				parameterFileManager.storeParameters(parameterMap);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		populateParameterSearchBox();
	}

	private Composite createParameterFileViewOuterComposite(Composite container) {
		Composite composite = new Composite(container, SWT.NONE);
		ColumnLayout cl_composite = new ColumnLayout();
		cl_composite.leftMargin = 0;
		cl_composite.rightMargin = 0;
		cl_composite.maxNumColumns = 1;
		composite.setLayout(cl_composite);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_composite.heightHint = 584;
		gd_composite.widthHint = 434;
		composite.setLayoutData(gd_composite);
		return composite;
	}

	private void createParameterFilesBox(Composite container) {
		Group grpPropertyFiles = new Group(container, SWT.NONE);
		grpPropertyFiles.setText(MultiParameterFileDialogConstants.TABLE_COLUMN_LIST_OF_PARAMETER_FILES);
		ColumnLayout cl_grpPropertyFiles = new ColumnLayout();
		cl_grpPropertyFiles.rightMargin = 0;
		cl_grpPropertyFiles.verticalSpacing = 0;
		cl_grpPropertyFiles.maxNumColumns = 1;
		cl_grpPropertyFiles.topMargin = 20;
		grpPropertyFiles.setLayout(cl_grpPropertyFiles);
		GridData gd_grpPropertyFiles = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_grpPropertyFiles.heightHint = 579;
		gd_grpPropertyFiles.widthHint = 340;
		grpPropertyFiles.setLayoutData(gd_grpPropertyFiles);

		createParameterFilesTable(grpPropertyFiles);
	}

	private void createParameterFilesTable(Group grpPropertyFiles) {
		Composite composite_2 = new Composite(grpPropertyFiles, SWT.NONE);
		ColumnLayoutData cld_composite_2 = new ColumnLayoutData();
		cld_composite_2.widthHint = 222;
		cld_composite_2.heightHint = 570;
		composite_2.setLayoutData(cld_composite_2);
		composite_2.setLayout(new GridLayout(1, false));

		Composite composite = new Composite(composite_2, SWT.NONE);
		composite.setLayout(new GridLayout(7, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_composite.heightHint = 33;
		composite.setLayoutData(gd_composite);

		final Button browseBtn = new Button(composite, SWT.NONE);
		browseBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(browseBtn.getShell(), SWT.OPEN);
				fileDialog.setText(MultiParameterFileDialogConstants.OPEN_FILE_DIALOG_NAME);

				if (activeProjectLocation != null) {
					fileDialog.setFilterPath(activeProjectLocation + "/" + MultiParameterFileDialogConstants.GLOBAL_PARAMETER_DIRECTORY_NAME);
				}

				String[] filterExt = { "*.properties" };
				fileDialog.setFilterExtensions(filterExt);
				String firstFile = fileDialog.open();
				if (firstFile != null) {
					parameterFileTextBox.setText(firstFile);

					if (isExistInParameterFileList(firstFile)) {
						MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);

						messageBox.setText(MessageType.INFO.messageType());
						messageBox.setMessage(ErrorMessages.FILE_EXIST);
						messageBox.open();
						return;
					}

					parameterFiles.add(new ParameterFile(fileDialog.getFileName(), firstFile, false, false));

					try {
						ParameterFileManager parameterFileManager = new ParameterFileManager(firstFile);
						parameterTableViewer.setData(MultiParameterFileDialogConstants.CURRENT_PARAM_FILE, firstFile);
						Map<String, String> parameterMap = new LinkedHashMap<>();
						parameterMap = parameterFileManager.getParameterMap();
						setGridData(parameters, parameterMap);
					} catch (IOException ioException) {
						MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);

						messageBox.setText(MessageType.ERROR.messageType());
						messageBox.setMessage(ErrorMessages.UNABLE_TO_POPULATE_PARAM_FILE + ioException.getMessage());
						messageBox.open();

						logger.debug("Unable to populate parameter file", ioException.getMessage());
					}

					filePathTableViewer.refresh();
					parameterTableViewer.refresh();
					populateParameterSearchBox();
				}
			}
		});
		browseBtn.setText(MultiParameterFileDialogConstants.ADD_BUTTON_TEXT);

		Button btnReload = new Button(composite, SWT.NONE);
		btnReload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				java.nio.file.Path path = Paths.get(parameterFileTextBox.getText());
				java.nio.file.Path fileName = path.getFileName();

				if (isExistInParameterFileList(path.toString())) {
					return;
				}

				parameterTableViewer.setData(MultiParameterFileDialogConstants.CURRENT_PARAM_FILE, parameterFileTextBox.getText());

				if (!parameterFileTextBox.getText().isEmpty()) {
					try {
						ParameterFileManager parameterFileManager = new ParameterFileManager(path.toString());
						parameterTableViewer.setData(MultiParameterFileDialogConstants.CURRENT_PARAM_FILE, path.toString());
						Map<String, String> parameterMap = new LinkedHashMap<>();
						parameterMap = parameterFileManager.getParameterMap();
						setGridData(parameters, parameterMap);

						parameterFiles.add(new ParameterFile(fileName.toString(), path.toString(), false, false));
						
						filePathTableViewer.refresh();
						parameterTableViewer.refresh();
						
						populateParameterSearchBox();

					} catch (IOException ioException) {
						MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);

						messageBox.setText(MessageType.ERROR.messageType());
						messageBox.setMessage(ErrorMessages.UNABLE_TO_POPULATE_PARAM_FILE + ioException.getMessage());
						messageBox.open();

						logger.debug("Unable to populate parameter file", ioException.getMessage());
					}

				}
			}
		});
		btnReload.setText(MultiParameterFileDialogConstants.RELOAD_BUTTON_TEXT);

		Button btnUp_1 = new Button(composite, SWT.NONE);
		btnUp_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table table = filePathTableViewer.getTable();
				int[] indexes = table.getSelectionIndices();
				for (int index : indexes) {

					if (index > 0) {
						Collections.swap((List<ParameterFile>) parameterFiles, index, index - 1);
						filePathTableViewer.refresh();

					}
				}
			}
		});
		btnUp_1.setText(MultiParameterFileDialogConstants.UP_BUTTON_TEXT);

		Button btnDown_1 = new Button(composite, SWT.NONE);
		btnDown_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table table = filePathTableViewer.getTable();
				int[] indexes = table.getSelectionIndices();
				for (int i = indexes.length - 1; i > -1; i--) {

					if (indexes[i] < parameterFiles.size() - 1) {
						Collections.swap((List<ParameterFile>) parameterFiles, indexes[i], indexes[i] + 1);
						filePathTableViewer.refresh();

					}
				}
			}
		});
		btnDown_1.setText(MultiParameterFileDialogConstants.DOWN_BUTTON_TEXT);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Link link = new Link(composite, SWT.NONE);
		link.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		link.setText(MultiParameterFileDialogConstants.HELP_LINK);
		link.setToolTipText(HELP_LINK_TOOLTIP_TEXT);

		Composite composite_3 = new Composite(composite_2, SWT.NONE);
		GridData gd_composite_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_3.widthHint = 326;
		composite_3.setLayoutData(gd_composite_3);
		composite_3.setLayout(new GridLayout(2, false));

		Label lblFile = new Label(composite_3, SWT.NONE);
		lblFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFile.setText(MultiParameterFileDialogConstants.FILE_LABEL_TEXT);

		parameterFileTextBox = new Text(composite_3, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 363;
		parameterFileTextBox.setLayoutData(gd_text);
		filePathTableViewer = CheckboxTableViewer.newCheckList(composite_2, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK
				| SWT.MULTI);
		Table table = filePathTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.heightHint = 445;
		table.setLayoutData(gd_table);
		filePathTableViewer.setContentProvider(new ArrayContentProvider());
		ColumnViewerToolTipSupport.enableFor(filePathTableViewer, ToolTip.NO_RECREATE);

		filePathTableViewer.addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				ParameterFile file = (ParameterFile) event.getElement();
				file.setChecked(event.getChecked());
			}
		});

		int operations = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		filePathTableViewer.addDragSupport(operations, transferTypes, new DragSourceListener() {

			@Override
			public void dragStart(DragSourceEvent event) {
				// Do Nothing
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				TableItem[] selectedTableItems = filePathTableViewer.getTable().getSelection();
				ArrayList<ParameterFile> filePathList = new ArrayList<ParameterFile>();
				for (TableItem selectedItem : selectedTableItems) {
					ParameterFile filePath = (ParameterFile) selectedItem.getData();
					filePathList.add(filePath);
				}
				try {
					event.data = serializeToString(filePathList);
				} catch (UnsupportedEncodingException e) {
					logger.debug(ErrorMessages.UNABLE_TO_REMOVE_JOB_SPECIFIC_FILE,e);
					
					MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);

					messageBox.setText(MessageType.INFO.messageType());
					messageBox.setMessage(ErrorMessages.UNABLE_TO_REMOVE_JOB_SPECIFIC_FILE);
					messageBox.open();
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// Do Nothing
			}
		});

		filePathTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) filePathTableViewer.getSelection();
				ParameterFile selectedFile = (ParameterFile) selection.getFirstElement();
				if (selectedFile != null) {
					populateViewParameterFileBox(selectedFile);
				}

			}
		});

		final TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(filePathTableViewer, SWT.NONE);
		TableColumn tblclmnFilePath = tableViewerColumn_4.getColumn();
		tblclmnFilePath.setWidth(333);
		tblclmnFilePath.setText(MultiParameterFileDialogConstants.TABLE_COLUMN_LIST_OF_PARAMETER_FILES);
		tableViewerColumn_4.getColumn().setImage(uncheckAllImage);

		tableViewerColumn_4.getColumn().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (selectAllFiles) {
					filePathTableViewer.setAllChecked(true);
					tableViewerColumn_4.getColumn().setImage(checkAllImage);
					selectAllFiles = false;

					for (ParameterFile file : parameterFiles) {
						file.setChecked(true);
					}

				} else {
					filePathTableViewer.setAllChecked(false);
					tableViewerColumn_4.getColumn().setImage(uncheckAllImage);
					selectAllFiles = true;
					for (ParameterFile file : parameterFiles) {
						file.setChecked(false);
					}
				}
			}
		});

		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getToolTipText(Object element) {
				return ((ParameterFile) element).getFilePathViewString();
			}

			@Override
			public Point getToolTipShift(Object object) {
				return new Point(5, 5);
			}

			@Override
			public int getToolTipDisplayDelayTime(Object object) {
				return 100; // msec
			}

			@Override
			public int getToolTipTimeDisplayed(Object object) {
				return 5000; // msec
			}

			@Override
			public Color getToolTipBackgroundColor(Object object) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			}

			@Override
			public Color getBackground(Object element) {

				return super.getBackground(element);
			}
			
			@Override
			public Color getForeground(Object element) {
				ParameterFile filePath = (ParameterFile) element;
				if (filePath.isJobSpecificFile())
					return new Color(Display.getDefault(), 0, 0, 255);
				return super.getForeground(element);
			}

			@Override
			public String getText(Object element) {
				ParameterFile p = (ParameterFile) element;
				return p.getFilePathViewString();
			}
		});

		Composite composite_1 = new Composite(composite_2, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.heightHint = 54;
		composite_1.setLayoutData(gd_composite_1);

		final Label lblDrop = new Label(composite_1, SWT.BORDER | SWT.SHADOW_NONE | SWT.CENTER);
		lblDrop.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblDrop.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		lblDrop.setAlignment(SWT.CENTER);
		lblDrop.setText(DROP_BOX_TEXT);

		DropTarget dt = new DropTarget(lblDrop, DND.DROP_MOVE);
		dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dt.addDropListener(new DropTargetAdapter() {

			public void drop(DropTargetEvent event) {
				List<ParameterFile> filesToRemove = new ArrayList<>();;
				try {
					
					filesToRemove = (List) deserializeFromString((String) event.data);
				} catch (UnsupportedEncodingException e) {
					logger.debug(ErrorMessages.UNABLE_TO_REMOVE_JOB_SPECIFIC_FILE,e);
					
					MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);

					messageBox.setText(MessageType.INFO.messageType());
					messageBox.setMessage(ErrorMessages.UNABLE_TO_REMOVE_JOB_SPECIFIC_FILE);
					messageBox.open();
				}
				
				ParameterFile jobSpecificFile = getJobSpecificFile();

				if (jobSpecificFile != null && filesToRemove.contains(jobSpecificFile)) {
					filesToRemove.remove(jobSpecificFile);

					MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION | SWT.OK);

					messageBox.setText(MessageType.INFO.messageType());
					messageBox.setMessage(ErrorMessages.UNABLE_TO_REMOVE_JOB_SPECIFIC_FILE);
					messageBox.open();
				}

				parameterFiles.removeAll(filesToRemove);
				filePathTableViewer.refresh();
				populateParameterSearchBox();
				populateViewParameterFileBox(jobSpecificFile);
			}
		});

	}

	protected boolean isExistInParameterFileList(String firstFile) {
		for (ParameterFile file : parameterFiles) {
			if (firstFile.equals(file.getPath())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(798, 688);
	}

	/**
	 * 
	 * Set parameter file list
	 * 
	 * @param parameterFiles
	 */
	public void setParameterFiles(List<ParameterFile> parameterFiles) {
		this.parameterFiles = parameterFiles;
	}

	private void populateFilePathTableViewer() {
		filePathTableViewer.setInput(parameterFiles);
		filePathTableViewer.refresh();

		for (ParameterFile file : parameterFiles) {
			if (file.isChecked()) {
				filePathTableViewer.setChecked(file, true);
			}
		}

	}

	@Override
	protected void okPressed() {
		List<ParameterFile> tempParameterFiles = new LinkedList<>();
		tempParameterFiles.addAll(parameterFiles);

		saveParameters();

		try {
			FileOutputStream fout;
			fout = new FileOutputStream(this.activeProjectLocation + "\\" + MultiParameterFileDialogConstants.PROJECT_METADATA_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			tempParameterFiles.remove(getJobSpecificFile());
			oos.writeObject(tempParameterFiles);
			oos.close();
			fout.close();
		} catch (FileNotFoundException fileNotFoundException) {
			runGraph = false;

			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);

			messageBox.setText(MessageType.ERROR.messageType());
			messageBox.setMessage(ErrorMessages.UNABLE_To_WRITE_PROJECT_METADAT_FILE + fileNotFoundException.getMessage());
			messageBox.open();

			logger.debug("Unable to write project metadata file", fileNotFoundException.getMessage());
			fileNotFoundException.printStackTrace();
		} catch (IOException ioException) {
			runGraph = false;
			MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);

			messageBox.setText(MessageType.ERROR.messageType());
			messageBox.setMessage(ErrorMessages.UNABLE_To_WRITE_PROJECT_METADAT_FILE + ioException.getMessage());
			messageBox.open();

			logger.debug(ErrorMessages.UNABLE_To_WRITE_PROJECT_METADAT_FILE, ioException.getMessage());
			ioException.printStackTrace();
		}
		runGraph = true;
		okPressed=true;
		super.okPressed();
	}

	/**
	 * 
	 * Returns true if we have all valid parameter file list
	 * 
	 * @return
	 */
	public boolean canRunGraph() {
		return runGraph;
	}

	@Override
	protected void cancelPressed() {
		runGraph = false;
		super.cancelPressed();
	}

	/**
	 * 
	 * Returns list of parameter files(comma separated)
	 * 
	 * @return
	 */
	public String getParameterFilesForExecution() {

		String activeParameterFiles = "";

		for (ParameterFile parameterFile : parameterFiles) {
			if (parameterFile.isChecked()) {
				activeParameterFiles = activeParameterFiles + parameterFile.getPath() + ",";
			}
		}
		if (activeParameterFiles.length() != 0)
			return activeParameterFiles.substring(0, activeParameterFiles.length() - 1);
		else
			return activeParameterFiles;
	}

	@Override
	public boolean close() {
		if(!okPressed)
			runGraph = false;
		
		return super.close();
	}
	
	/**
	 * 
	 * Serialize object to string. Serialized string will not be in human readable format 
	 * 
	 * @param input
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	private String serializeToString(Serializable input)
			throws UnsupportedEncodingException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
		ObjectOutputStream out = null;
		try {
			// stream closed in the finally
			out = new ObjectOutputStream(baos);
			out.writeObject(input);

		} catch (IOException ex) {
			throw new SerializationException(ex);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}

		byte[] repr = baos.toByteArray();
		String decoded = new String(base64.encode(repr));
		return decoded;
	}
	
	/**
	 * 
	 * deserialize string converted to Object.
	 * 
	 * @param input
	 * @return Object
	 * @throws UnsupportedEncodingException
	 */
	private Object deserializeFromString(String input)
			throws UnsupportedEncodingException {
		byte[] repr = base64.decode(input.getBytes());
		ByteArrayInputStream bais = new ByteArrayInputStream(repr);
		
		ObjectInputStream in = null;
		try {
			// stream closed in the finally
			in = new ObjectInputStream(bais);
			return in.readObject();

		} catch (ClassNotFoundException ex) {
			throw new SerializationException(ex);
		} catch (IOException ex) {
			throw new SerializationException(ex);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}
	}
}

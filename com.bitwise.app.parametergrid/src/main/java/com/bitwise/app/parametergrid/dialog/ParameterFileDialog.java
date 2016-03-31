package com.bitwise.app.parametergrid.dialog;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;

import com.bitwise.app.common.datastructures.parametergrid.FilePath;
import com.bitwise.app.parametergrid.constants.ParameterGridConstants;
import com.bitwise.app.parametergrid.dialog.models.Parameter;
import com.bitwise.app.parametergrid.dialog.models.ParameterWithFilePath;
import com.bitwise.app.parametergrid.dialog.support.ParameterEditingSupport;
import com.bitwise.app.parametergrid.utils.ParameterFileManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ParameterFileDialog extends Dialog {

	private CheckboxTableViewer filePathTableViewer;
	private TableViewer parameterTableViewer;
	private TableViewer parameterSearchTableViewer;
	private Text parameterFileTextBox;

	private List<FilePath> parameterFiles;
	private List<Parameter> parameters;
	private List<ParameterWithFilePath> parameterSearchBoxItems;
	private List<ParameterWithFilePath> parameterSearchBoxItemsFixed;
	private Image checkAllImage;
	private Image uncheckAllImage;
	private boolean selectAllFiles=true;
	private String activeProjectLocation;
	
	private boolean runGraph;
	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ParameterFileDialog(Shell parentShell) {
		super(parentShell);
		if (parameterFiles == null)
			parameterFiles = new LinkedList<>();

		parameters = new LinkedList<>();
		parameterSearchBoxItems = new LinkedList<>();
		parameterSearchBoxItemsFixed = new LinkedList<>();
		checkAllImage = new Image(null, "C:\\Users\\shrirangk\\Desktop\\DeskTopBackupMarch16\\eclipse-rcp-indigo-SR2-win32-x86_64\\eclipse\\config\\icons\\checkall.png");
		uncheckAllImage = new Image(null, "C:\\Users\\shrirangk\\Desktop\\DeskTopBackupMarch16\\eclipse-rcp-indigo-SR2-win32-x86_64\\eclipse\\config\\icons\\uncheckall.png");
	}

	
	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ParameterFileDialog(Shell parentShell,String activeProjectLocation) {
		super(parentShell);
		if (parameterFiles == null)
			parameterFiles = new LinkedList<>();

		parameters = new LinkedList<>();
		parameterSearchBoxItems = new LinkedList<>();
		parameterSearchBoxItemsFixed = new LinkedList<>();
		checkAllImage = new Image(null, "C:\\Users\\shrirangk\\Desktop\\DeskTopBackupMarch16\\eclipse-rcp-indigo-SR2-win32-x86_64\\eclipse\\config\\icons\\checkall.png");
		uncheckAllImage = new Image(null, "C:\\Users\\shrirangk\\Desktop\\DeskTopBackupMarch16\\eclipse-rcp-indigo-SR2-win32-x86_64\\eclipse\\config\\icons\\uncheckall.png");
		
		this.activeProjectLocation = activeProjectLocation;
	}
	
	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(2, false));

		createParameterFilesBox(container);
		populateFilePathTableViewer();

		Composite composite = createParameterFileViewOuterComposite(container);
		createViewParameterFileBox(composite);
		FilePath jobSpecificFile =getJobSpecificFile();
		if(jobSpecificFile!=null)
			populateViewParameterFileBox(jobSpecificFile);
		
		createParameterSearchBox(composite);

		return container;
	}

	private FilePath getJobSpecificFile() {
		FilePath jobSpecificFile = null;
		for (FilePath filePath : parameterFiles) {
			if (filePath.isJobSpecificFile()) {
				jobSpecificFile = filePath;
				break;
			}
		}
		return jobSpecificFile;
	}

	private void populateViewParameterFileBox(FilePath file) {
		parameterFileTextBox.setText(file.getPath());
		try {
			ParameterFileManager parameterFileManager = new ParameterFileManager(file.getPath());
			Map<String, String> parameterMap = new LinkedHashMap<>();
			parameterMap = parameterFileManager.getParameterMap();
			setGridData(parameters, parameterMap);
			parameterTableViewer.setData("CURRENT_PARAM_FILE", file.getPath());
		} catch (IOException ioException) {
			// isValidParameterFile = false;
			// logger.debug("Unable to get parameter Map ", e);
			ioException.printStackTrace();
		}

		parameterTableViewer.refresh();
	}
	
	private void searchParameter(String text) {
		parameterSearchBoxItems.clear();
		
		for(ParameterWithFilePath parameterSearchBoxItem: parameterSearchBoxItemsFixed){
			if(parameterSearchBoxItem.toString().contains(text)){
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
		grpAllProperties.setText("All Parameters");

		Composite composite_5 = new Composite(grpAllProperties, SWT.NONE);
		ColumnLayout cl_composite_5 = new ColumnLayout();
		cl_composite_5.rightMargin = 0;
		cl_composite_5.leftMargin = 0;
		cl_composite_5.maxNumColumns = 1;
		composite_5.setLayout(cl_composite_5);
		GridData gd_composite_5 = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_composite_5.heightHint = 301;
		gd_composite_5.widthHint = 496;
		composite_5.setLayoutData(gd_composite_5);

		Composite composite_6 = new Composite(composite_5, SWT.NONE);
		composite_6.setLayout(new GridLayout(2, false));
		ColumnLayoutData cld_composite_6 = new ColumnLayoutData();
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
				//searchParameterViewerFilter.setSearchText(text_1.getText());
				//parameterSearchTableViewer.refresh();
				if(text_1.getText().isEmpty()){
					populateParameterSearchBox();
				}else{
					searchParameter(text_1.getText());
				}
				
				parameterSearchTableViewer.refresh();
			}
		});
		

		Composite composite_7 = new Composite(composite_5, SWT.NONE);
		composite_7.setLayout(new GridLayout(1, false));
		ColumnLayoutData cld_composite_7 = new ColumnLayoutData();
		cld_composite_7.heightHint = 258;
		composite_7.setLayoutData(cld_composite_7);

		parameterSearchTableViewer = new TableViewer(composite_7, SWT.BORDER | SWT.FULL_SELECTION);
		Table table_1 = parameterSearchTableViewer.getTable();
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table_1.widthHint = 456;
		table_1.setLayoutData(gd_table_1);
		parameterSearchTableViewer.setContentProvider(new ArrayContentProvider());
		ColumnViewerToolTipSupport.enableFor(parameterSearchTableViewer, ToolTip.NO_RECREATE);

		/*SearchParameterViewerComparator searchParameterViewerComparator = new SearchParameterViewerComparator();
		parameterSearchTableViewer.setComparator(searchParameterViewerComparator);
		searchParameterViewerFilter = new SearchParameterViewerFilter();
		parameterSearchTableViewer.addFilter(searchParameterViewerFilter);*/
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(parameterSearchTableViewer, SWT.NONE);
		TableColumn tblclmnFilePath_1 = tableViewerColumn.getColumn();
		tblclmnFilePath_1.setWidth(195);
		tblclmnFilePath_1.setText("Parameter Files");
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getToolTipText(Object element) {
				String tooltip = "ParameterFile: "
						+ ((ParameterWithFilePath) element).getFilePath().getFilePathViewString() + "\n "
						+ "ParameterName: " + ((ParameterWithFilePath) element).getParameterName() + "\n "
						+ "ParameterValue: " + ((ParameterWithFilePath) element).getParameterValue();
				// return ((ParameterWithFilePath) element).getFilePath().getFilePathViewString();
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
				return p.getFilePath().getFilePathViewString();
			}
		});

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(parameterSearchTableViewer, SWT.NONE);
		TableColumn tblclmnParameterName = tableViewerColumn_1.getColumn();
		tblclmnParameterName.setWidth(140);
		tblclmnParameterName.setText("Parameter Name");
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getToolTipText(Object element) {
				String tooltip = "ParameterFile: "
						+ ((ParameterWithFilePath) element).getFilePath().getFilePathViewString() + "\n "
						+ "ParameterName: " + ((ParameterWithFilePath) element).getParameterName() + "\n "
						+ "ParameterValue: " + ((ParameterWithFilePath) element).getParameterValue();
				// return ((ParameterWithFilePath) element).getFilePath().getFilePathViewString();
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
		tblclmnParameterValue.setText("Parameter Value");
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getToolTipText(Object element) {
				String tooltip = "ParameterFile: "
						+ ((ParameterWithFilePath) element).getFilePath().getFilePathViewString() + "\n "
						+ "ParameterName: " + ((ParameterWithFilePath) element).getParameterName() + "\n "
						+ "ParameterValue: " + ((ParameterWithFilePath) element).getParameterValue();
				// return ((ParameterWithFilePath) element).getFilePath().getFilePathViewString();
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

		for (FilePath filePath : parameterFiles) {
			try {
				ParameterFileManager parameterFileManager = new ParameterFileManager(filePath.getPath());
				Map<String, String> parameterMap = new LinkedHashMap<>();
				parameterMap = parameterFileManager.getParameterMap();

				for (String paramater : parameterMap.keySet()) {
					ParameterWithFilePath parameterWithFilePath = new ParameterWithFilePath(paramater,
							parameterMap.get(paramater), filePath);
					
					if(!parameterSearchBoxItems.contains(parameterWithFilePath))
						parameterSearchBoxItems.add(parameterWithFilePath);
				}

			} catch (IOException ioException) {
				ioException.printStackTrace();
			}

		}
		if (parameterSearchBoxItems.size() != 0){
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
		cld_grpPropertyFileView.heightHint = 246;
		grpPropertyFileView.setLayoutData(cld_grpPropertyFileView);
		grpPropertyFileView.setText("Parameter file view");

		Composite composite_3 = new Composite(grpPropertyFileView, SWT.NONE);
		composite_3.setLayout(new GridLayout(4, false));
		ColumnLayoutData cld_composite_3 = new ColumnLayoutData();
		cld_composite_3.widthHint = 490;
		cld_composite_3.heightHint = 32;
		composite_3.setLayoutData(cld_composite_3);

		Label lblFile = new Label(composite_3, SWT.NONE);
		lblFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFile.setText("File");

		parameterFileTextBox = new Text(composite_3, SWT.BORDER);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 363;
		parameterFileTextBox.setLayoutData(gd_text);

		Button btnReload = new Button(composite_3, SWT.NONE);
		btnReload.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				java.nio.file.Path path = Paths.get(parameterFileTextBox.getText());
				java.nio.file.Path fileName = path.getFileName();

				parameterTableViewer.setData("CURRENT_PARAM_FILE", parameterFileTextBox.getText());

				if (!parameterFileTextBox.getText().isEmpty()) {
					if (parameterFiles.size() != 0) {
						parameterFiles.get(parameterFiles.size() - 1).setFileName(fileName.toString());
						parameterFiles.get(parameterFiles.size() - 1).setPath(path.toString());

					} else {
						parameterFiles.add(new FilePath(fileName.toString(), path.toString(),false,false));
					}

					try {
						ParameterFileManager parameterFileManager = new ParameterFileManager(path.toString());
						parameterTableViewer.setData("CURRENT_PARAM_FILE", path.toString());
						Map<String, String> parameterMap = new LinkedHashMap<>();
						parameterMap = parameterFileManager.getParameterMap();
						setGridData(parameters, parameterMap);

						filePathTableViewer.refresh();
						parameterTableViewer.refresh();
						populateParameterSearchBox();

					} catch (IOException ioException) {
						// isValidParameterFile = false;
						// logger.debug("Unable to get parameter Map ", e);
						ioException.printStackTrace();
					}

				}

				// saveParameters();
			}
		});
		btnReload.setText("Reload");

		final Button browseBtn = new Button(composite_3, SWT.NONE);
		browseBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(browseBtn.getShell(), SWT.OPEN);
				fileDialog.setText("Open");

				String[] filterExt = { "*.properties" };
				fileDialog.setFilterExtensions(filterExt);
				String firstFile = fileDialog.open();
				if (firstFile != null) {
					parameterFileTextBox.setText(firstFile);
					parameterFiles.add(new FilePath(fileDialog.getFileName(), firstFile,false,false));

					try {
						ParameterFileManager parameterFileManager = new ParameterFileManager(firstFile);
						parameterTableViewer.setData("CURRENT_PARAM_FILE", firstFile);
						Map<String, String> parameterMap = new LinkedHashMap<>();
						parameterMap = parameterFileManager.getParameterMap();
						setGridData(parameters, parameterMap);
					} catch (IOException ioException) {
						// isValidParameterFile = false;
						// logger.debug("Unable to get parameter Map ", e);
						ioException.printStackTrace();
					}

					filePathTableViewer.refresh();
					parameterTableViewer.refresh();
					populateParameterSearchBox();
				}
			}
		});
		browseBtn.setText("Add");

		Composite composite_4 = new Composite(grpPropertyFileView, SWT.NONE);
		composite_4.setLayout(new GridLayout(2, false));
		ColumnLayoutData cld_composite_4 = new ColumnLayoutData();
		cld_composite_4.heightHint = 204;
		composite_4.setLayoutData(cld_composite_4);

		parameterTableViewer = new TableViewer(composite_4, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		Table table_2 = parameterTableViewer.getTable();
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);
		GridData gd_table_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table_2.widthHint = 320;
		table_2.setLayoutData(gd_table_2);
		parameterTableViewer.setContentProvider(new ArrayContentProvider());

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(parameterTableViewer, SWT.NONE);
		TableColumn tblclmnParameterName_1 = tableViewerColumn_3.getColumn();
		tblclmnParameterName_1.setWidth(201);
		tblclmnParameterName_1.setText(ParameterGridConstants.PARAMETER_NAME);
		tableViewerColumn_3.setEditingSupport(new ParameterEditingSupport(parameterTableViewer,
				ParameterGridConstants.PARAMETER_NAME));
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Parameter p = (Parameter) element;
				return p.getParameterName();
			}
		});

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(parameterTableViewer, SWT.NONE);
		TableColumn tblclmnParameterValue_1 = tableViewerColumn_5.getColumn();
		tblclmnParameterValue_1.setWidth(208);
		tblclmnParameterValue_1.setText(ParameterGridConstants.PARAMETER_VALUE);
		tableViewerColumn_5.setEditingSupport(new ParameterEditingSupport(parameterTableViewer,
				ParameterGridConstants.PARAMETER_VALUE));
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Parameter p = (Parameter) element;
				return p.getParameterValue();
			}
		});

		parameterTableViewer.setInput(parameters);

		Composite composite_8 = new Composite(composite_4, SWT.NONE);
		ColumnLayout cl_composite_8 = new ColumnLayout();
		cl_composite_8.maxNumColumns = 1;
		composite_8.setLayout(cl_composite_8);
		GridData gd_composite_8 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_8.widthHint = 63;
		composite_8.setLayoutData(gd_composite_8);

		Button btnAdd_1 = new Button(composite_8, SWT.NONE);
		btnAdd_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Parameter parameter = new Parameter("DefaultParameter", "DefaultValue");
				parameters.add(parameter);
				parameterTableViewer.refresh();
				// parameterTableViewer.add(parameter);
			}
		});
		btnAdd_1.setText("Add");

		Button btnDelete = new Button(composite_8, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				Table table = parameterTableViewer.getTable();
				int temp = table.getSelectionIndex();
				int[] indexs = table.getSelectionIndices();
				if (temp == -1) {
					System.out.println("Please Select row to delete");
				} else {
					table.remove(indexs);
					List<Parameter> parametersToRemove = new LinkedList<>();
					for (int index : indexs) {
						parametersToRemove.add(parameters.get(index));
						parameters.remove(index);
					}
					parameterTableViewer.refresh();
				}

			}
		});
		btnDelete.setText("Delete");

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
		btnUp.setText("Up");

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
		btnDown.setText("Down");

		Button btnSave = new Button(composite_8, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveParameters();
			}
		});
		btnSave.setText("Save");
	}

	private void saveParameters() {
		if (!parameterFileTextBox.getText().isEmpty()) {
			String currentFilePath = (String) parameterTableViewer.getData("CURRENT_PARAM_FILE");
			ParameterFileManager parameterFileManager = new ParameterFileManager(currentFilePath);
			// parameterTableViewer.setData("CURRENT_PARAM_FILE", parameterFileTextBox.getText());
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
		gd_composite.widthHint = 505;
		composite.setLayoutData(gd_composite);
		return composite;
	}

	private void createParameterFilesBox(Composite container) {
		Group grpPropertyFiles = new Group(container, SWT.NONE);
		grpPropertyFiles.setText("Parameter files");
		ColumnLayout cl_grpPropertyFiles = new ColumnLayout();
		cl_grpPropertyFiles.rightMargin = 0;
		cl_grpPropertyFiles.verticalSpacing = 0;
		cl_grpPropertyFiles.maxNumColumns = 1;
		cl_grpPropertyFiles.topMargin = 20;
		grpPropertyFiles.setLayout(cl_grpPropertyFiles);
		GridData gd_grpPropertyFiles = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_grpPropertyFiles.heightHint = 579;
		gd_grpPropertyFiles.widthHint = 267;
		grpPropertyFiles.setLayoutData(gd_grpPropertyFiles);

		//createParameterFilesButtonBox(grpPropertyFiles);
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
		composite.setLayout(new GridLayout(3, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_composite.heightHint = 33;
		composite.setLayoutData(gd_composite);
		
		Button btnUp_1 = new Button(composite, SWT.NONE);
		btnUp_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table table = filePathTableViewer.getTable();
				int[] indexes = table.getSelectionIndices();
				for (int index : indexes) {

					if (index > 0) {
						Collections.swap((List<FilePath>) parameterFiles, index, index - 1);
						filePathTableViewer.refresh();

					}
				}
			}
		});
		btnUp_1.setText("Up");
		
		Button btnDown_1 = new Button(composite, SWT.NONE);
		btnDown_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table table = filePathTableViewer.getTable();
				int[] indexes = table.getSelectionIndices();
				for (int i = indexes.length - 1; i > -1; i--) {

					if (indexes[i] < parameterFiles.size() - 1) {
						Collections.swap((List<FilePath>) parameterFiles, indexes[i], indexes[i] + 1);
						filePathTableViewer.refresh();

					}
				}
			}
		});
		btnDown_1.setText("Down");

		Link link = new Link(composite, SWT.NONE);
		link.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		//link.setText("                              <a>Help</a>");
		link.setText("<a>Help</a>");
		link.setToolTipText("Only the check files will be considered while executing job\nand will be passed to job in same sequence as they are in grid");
		/*new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);*/
		//filePathTableViewer = new TableViewer(composite_2, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK | SWT.MULTI);
		filePathTableViewer = CheckboxTableViewer.newCheckList(composite_2, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK | SWT.MULTI);
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
				// TODO Auto-generated method stub
				FilePath file=(FilePath) event.getElement();
				file.setChecked(event.getChecked());
			}
		});
		
		int operations = DND.DROP_COPY| DND.DROP_MOVE;
	    Transfer[] transferTypes = new Transfer[]{TextTransfer.getInstance()};
	    filePathTableViewer.addDragSupport(operations, transferTypes , new DragSourceListener() {
	    		    	 
			@Override
			public void dragStart(DragSourceEvent event) {
				// Do Nothing
			}
			
			@Override
			public void dragSetData(DragSourceEvent event) {
				TableItem[] selectedTableItems = filePathTableViewer.getTable().getSelection();
				ArrayList<FilePath> filePathList = new ArrayList<FilePath>();
				for(TableItem selectedItem: selectedTableItems){
					FilePath filePath = (FilePath) selectedItem.getData();
					filePathList.add(filePath);
				}
				String gsonFilePathList = new Gson().toJson(filePathList);
				event.data = gsonFilePathList; 
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
				FilePath selectedFile = (FilePath) selection.getFirstElement();
				if (selectedFile != null) {
					populateViewParameterFileBox(selectedFile);
				}

			}
		});

		final TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(filePathTableViewer, SWT.NONE);
		TableColumn tblclmnFilePath = tableViewerColumn_4.getColumn();
		tblclmnFilePath.setWidth(249);
		tblclmnFilePath.setText("Parameter Files");
		tableViewerColumn_4.getColumn().setImage(uncheckAllImage);
				
		tableViewerColumn_4.getColumn().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (selectAllFiles) {
					filePathTableViewer.setAllChecked(true);
					tableViewerColumn_4.getColumn().setImage(checkAllImage);
					selectAllFiles = false;
					
					for(FilePath file: parameterFiles){
						file.setChecked(true);
					}
					
				} else {
					filePathTableViewer.setAllChecked(false);
					tableViewerColumn_4.getColumn().setImage(uncheckAllImage);
					selectAllFiles = true;
					for(FilePath file: parameterFiles){
						file.setChecked(false);
					}
				}
			}
		});
		
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getToolTipText(Object element) {
				return ((FilePath) element).getFilePathViewString();
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
				FilePath filePath = (FilePath) element;
				if(filePath.isJobSpecificFile())
					return new Color(Display.getDefault(), 0xFF, 0xDD, 0xDD);
				
				return super.getBackground(element);
			}
			
			@Override
			public String getText(Object element) {
				FilePath p = (FilePath) element;
				return p.getFilePathViewString();
			}
		});
		
		Composite composite_1 = new Composite(composite_2, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite_1.heightHint = 54;
		composite_1.setLayoutData(gd_composite_1);
		
		final Label lblDrop = new Label(composite_1, SWT.BORDER | SWT.SHADOW_NONE | SWT.CENTER);
		lblDrop.setAlignment(SWT.CENTER);
		lblDrop.setText("\nDrop parameter file here to delete");
		
		DropTarget dt = new DropTarget(lblDrop, DND.DROP_MOVE);
	    dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
	    dt.addDropListener(new DropTargetAdapter() {
	    	
	      public void drop(DropTargetEvent event) {
	    	  Type type = new TypeToken<List<FilePath>>(){}.getType();
	    	  List<FilePath> filesToRemove = new Gson().fromJson((String)event.data, type);
	    	  
	    	  FilePath jobSpecificFile=getJobSpecificFile();
	    	  
	    	  if(jobSpecificFile!=null && filesToRemove.contains(jobSpecificFile)){
	    		  filesToRemove.remove(jobSpecificFile);
	    	  }  
	    	  
	    	  parameterFiles.removeAll(filesToRemove);
	    	  filePathTableViewer.refresh();
	    	  populateParameterSearchBox();
	    	  populateViewParameterFileBox(jobSpecificFile);
	      }
	    });
	    
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

	public void setParameterFiles(List<FilePath> filePathList) {
		parameterFiles = filePathList;
	}

	private void populateFilePathTableViewer() {
		filePathTableViewer.setInput(parameterFiles);
		filePathTableViewer.refresh();

		for(FilePath file: parameterFiles){
			if(file.isChecked()){
				filePathTableViewer.setChecked(file, true);
			}
		}
		
	}
	
	@Override
	protected void okPressed() {
		List<FilePath> tempParameterFiles = new LinkedList<>();
		tempParameterFiles.addAll(parameterFiles);
		
		try {
			FileOutputStream fout;
			//fout = new FileOutputStream("C:\\Users\\shrirangk\\Desktop\\Paramfiles\\param.meta");
			fout = new FileOutputStream(this.activeProjectLocation + "\\project.metadata");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			tempParameterFiles.remove(getJobSpecificFile());
			oos.writeObject(tempParameterFiles);
			oos.close();
			fout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			runGraph=false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			runGraph=false;
			e.printStackTrace();
		}
		runGraph=true;
		
		super.okPressed();
	}
	
	public boolean canRunGraph(){
		return runGraph;
	}
	
	@Override
	protected void cancelPressed() {
		// TODO Auto-generated method stub
		runGraph = false;
		super.cancelPressed();
	}
	
	public String getParameterFilesForExecution(){
		
		String activeParameterFiles = "";
		
		for(FilePath parameterFile:parameterFiles){
			if(parameterFile.isChecked()){
				activeParameterFiles = activeParameterFiles + parameterFile.getPath() + ",";
			}
		}
		
		return activeParameterFiles.substring(0, activeParameterFiles.length()-1);
	}
}

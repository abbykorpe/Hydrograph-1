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

package hydrograph.ui.dataviewer;

import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.dataviewer.actions.ActionFactory;
import hydrograph.ui.dataviewer.actions.CopyAction;
import hydrograph.ui.dataviewer.actions.ExportAction;
import hydrograph.ui.dataviewer.actions.FilterAction;
import hydrograph.ui.dataviewer.actions.FindAction;
import hydrograph.ui.dataviewer.actions.FormattedViewAction;
import hydrograph.ui.dataviewer.actions.GridViewAction;
import hydrograph.ui.dataviewer.actions.PreferencesAction;
import hydrograph.ui.dataviewer.actions.ReloadAction;
import hydrograph.ui.dataviewer.actions.SelectAllAction;
import hydrograph.ui.dataviewer.actions.UnformattedViewAction;
import hydrograph.ui.dataviewer.actions.ViewDataGridMenuCreator;
import hydrograph.ui.dataviewer.adapters.CSVAdapter;
import hydrograph.ui.dataviewer.constants.ControlConstants;
import hydrograph.ui.dataviewer.constants.MenuConstants;
import hydrograph.ui.dataviewer.constants.StatusConstants;
import hydrograph.ui.dataviewer.constants.Views;
import hydrograph.ui.dataviewer.datastructures.RowData;
import hydrograph.ui.dataviewer.datastructures.StatusMessage;
import hydrograph.ui.dataviewer.listeners.DataViewerListeners;
import hydrograph.ui.dataviewer.preferances.ViewDataPreferences;
import hydrograph.ui.dataviewer.support.StatusManager;
import hydrograph.ui.dataviewer.utilities.SWTResourceManager;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.dataviewer.viewloders.DataViewLoader;
import hydrograph.ui.logging.factory.LogFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;

public class DebugDataViewer extends ApplicationWindow {
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(DebugDataViewer.class);
	
	private CTabFolder tabFolder;
	
	private StyledText unformattedViewTextarea;
	private StyledText formattedViewTextarea;
	private TableViewer horizontalViewTableViewer;
	private TableViewer gridViewTableViewer;
	
	private CSVAdapter csvAdapter;
	private Map<String,Control> windowControls;
	
	private static List<RowData> gridViewData;
	private static List<RowData> formattedViewData;
	private static List<RowData> unformattedViewData;
	
	private String database;
	private String tableName;
	
	private String windowName="Debug Data viewer";
	
	private ActionFactory actionFactory;
	
	private DataViewLoader dataViewLoader;
	private DataViewerListeners dataViewerListeners;
	private ViewDataPreferences viewDataPreferences=new ViewDataPreferences();
	private static final String PLUGIN_NAME="hydrograph.ui.dataviewer";
    private static final String DELIMITER="delimiter";
    private static final String QUOTE_CHARACTOR="quoteCharactor";
    private static final String INCLUDE_HEADERS="includeHeader";
    private static final String DEFAULT_DELIMITER= ",";
    private static final String DEFAULT_QUOTE_CHARACTOR="\"";
    private static final String DEFAULT="default";
    private static final String FILE_SIZE="VIEW_DATA_FILE_SIZE";
    private static final String PAGE_SIZE="VIEW_DATA_PAGE_SIZE";
    private static final String DEFAULT_FILE_SIZE="100";
    private static final String DEFAULT_PAGE_SIZE="100";
	
    private ReloadInformation reloadInformation;
	
	private StatusManager statusManager;
	
	/**
	 * Create the application window,
	 */
	public DebugDataViewer() {
		super(null);
		createActions();
		addCoolBar(SWT.FLAT);
		addMenuBar();
		addStatusLine();
		windowControls = new LinkedHashMap<>();
		gridViewData = new LinkedList<>();
		actionFactory = new ActionFactory(this);
	}

	
	/**
	 * Create the application window,
	 * @param reloadInformation 
	 */
	public DebugDataViewer(String filePath,String fileName,String windowName, ReloadInformation reloadInformation) {
		super(null);
		createActions();
		addCoolBar(SWT.FLAT);
		addMenuBar();
		addStatusLine();
		windowControls = new LinkedHashMap<>();
		gridViewData = new LinkedList<>();
		this.database = filePath;
		this.tableName = fileName;
		actionFactory = new ActionFactory(this);
		
		if(windowName!=null)
			this.windowName = "Data Viewer - " + windowName;
		
		
		this.reloadInformation = reloadInformation;
	}
	
	
	public ReloadInformation getReloadInformation() {
		return reloadInformation;
	}


	public String getWindowName() {
		return windowName;
	}
	
	public CSVAdapter getCsvAdapter() {
		return csvAdapter;
	}
	
	public DataViewLoader getDataViewLoader() {
		return dataViewLoader;
	}
	
	public StyledText getUnformattedViewTextarea() {
		return unformattedViewTextarea;
	}


	public StyledText getFormattedViewTextarea() {
		return formattedViewTextarea;
	}
	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		setDataViewerWindowTitle();
		try{
			initializeDataFileAdapter();
		} catch (Exception e) {
			MessageBox messageBox = new MessageBox(new Shell());
			messageBox.setText("Error");
			messageBox.setMessage("Unable to load debug file - " + e.getMessage());
			messageBox.open();
			logger.debug("Unable to load debug file",e);
			getShell().close();
			return null;
		}

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		tabFolder = new CTabFolder(container, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		tabFolder.setSelectionBackground(new Color(null, 14, 76, 145));
		tabFolder.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
		tabFolder.setSelectionForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));

		dataViewLoader = new DataViewLoader(unformattedViewTextarea,
				formattedViewTextarea, horizontalViewTableViewer,
				gridViewTableViewer, gridViewData, formattedViewData,
				unformattedViewData, csvAdapter, tabFolder);

		dataViewerListeners = new DataViewerListeners();
		createGridViewTabItem();
		createPaginationPanel(container);		
		
		
		dataViewerListeners.setCsvAdapter(csvAdapter);
		dataViewerListeners.setDataViewLoader(dataViewLoader);
		dataViewerListeners.setWindowControls(windowControls);
		
		dataViewerListeners.addTabFolderSelectionChangeListener(tabFolder);
		
		dataViewerListeners.setStatusManager(statusManager);
		statusManager.setCsvAdapter(csvAdapter);
		statusManager.setWindowControls(windowControls);
		statusManager.setStatus(new StatusMessage(StatusConstants.SUCCESS));
		
		//statusManager.enableJumpPagePanel(false);
		statusManager.enableInitialPaginationContols();
		
		tabFolder.setSelection(0);
		
		return container;
	}
	
	public StatusManager getStatusManager() {
		return statusManager;
	}
	
	public void initializeDataFileAdapter() throws Exception {
		csvAdapter = new CSVAdapter(database, tableName,Utils.getDefaultPageSize(), 0,this);
	}

	private void setDataViewerWindowTitle() {
		getShell().setText(windowName);
	}
	
	private void createPaginationPanel(Composite container) {
		Composite composite_2 = new Composite(container, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		GridLayout gl_composite_2 = new GridLayout(2, false);
		gl_composite_2.verticalSpacing = 0;
		gl_composite_2.marginWidth = 0;
		gl_composite_2.marginHeight = 0;
		gl_composite_2.horizontalSpacing = 0;
		composite_2.setLayout(gl_composite_2);
		
		createPageSwitchPanel(composite_2);
		createPageJumpPanel(composite_2);

	}


	private void createPageJumpPanel(Composite composite_2) {
		Composite composite_3 = new Composite(composite_2, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		composite_3.setLayout(new GridLayout(3, false));

		createJumpPageLabel(composite_3);

		createJumpPageTextBox(composite_3);

		createJumpPageButton(composite_3);

	}

	private void createJumpPageButton(Composite composite_3) {
		Button jumpPageButton = new Button(composite_3, SWT.NONE);
		dataViewerListeners.attachJumpToPageListener(jumpPageButton);
		jumpPageButton.setText("Go");
		windowControls.put(ControlConstants.JUMP_BUTTON,jumpPageButton);
	}

	private void createJumpPageTextBox(Composite composite_3) {
		Text jumpPageTextBox = new Text(composite_3, SWT.BORDER);
		jumpPageTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		jumpPageTextBox.addVerifyListener(new VerifyListener() {  
		    @Override  
		    public void verifyText(VerifyEvent e) {
		        String currentText = ((Text)e.widget).getText();
		        String pageNumberText =  currentText.substring(0, e.start) + e.text + currentText.substring(e.end);
		        try{ 
		        	long pageNumber = Long.valueOf(pageNumberText);  
		            if(pageNumber <1 ){  
		                e.doit = false;  
		            }  
		        }  
		        catch(NumberFormatException ex){  
		            if(!pageNumberText.equals(""))
		                e.doit = false;  
		        }  
		    }  
		});	
		
		dataViewerListeners.attachJumpToPageListener(jumpPageTextBox);
		windowControls.put(ControlConstants.JUMP_TEXT,jumpPageTextBox);
		
	}


	private void createJumpPageLabel(Composite composite_3) {
		Label label = new Label(composite_3, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Jump to Page: ");
	}


	private void createPageSwitchPanel(Composite composite_2) {
		Composite composite_3 = new Composite(composite_2, SWT.NONE);
		composite_3.setLayout(new GridLayout(3, false));
		
		createPreviousPageButton(composite_3);
		createPageNumberDisplay(composite_3);
		createNextPageButton(composite_3);
		
	}


	private void createNextPageButton(Composite composite_3) {
		Button nextPageButton = new Button(composite_3, SWT.NONE);
		dataViewerListeners.attachNextPageButtonListener(nextPageButton);
		nextPageButton.setText("Next");
		windowControls.put(ControlConstants.NEXT_BUTTON,nextPageButton);
		
	}


	private void createPageNumberDisplay(Composite composite_3) {
		Text pageNumberDisplayTextBox = new Text(composite_3, SWT.BORDER | SWT.CENTER);
		pageNumberDisplayTextBox.setEnabled(false);
		pageNumberDisplayTextBox.setEditable(false);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 178;
		pageNumberDisplayTextBox.setLayoutData(gd_text);
		
		windowControls.put(ControlConstants.PAGE_NUMBER_DISPLAY, pageNumberDisplayTextBox);
		
		Job job = new Job("Fetaching total number of rows") {
			  @Override
			  protected IStatus run(IProgressMonitor monitor) {
				  final StatusMessage status = csvAdapter.fetchRowCount();
				  
				  Display.getDefault().asyncExec(new Runnable() {
			      @Override
			      public void run() {
			    	  statusManager.setStatus(status);
			    	  statusManager.enableJumpPagePanel(true);
			      }
			    });
			    return Status.OK_STATUS;
			  }
			};
			
			job.schedule();
	}

	private void createPreviousPageButton(Composite composite_3) {
		Button button = new Button(composite_3, SWT.NONE);
		dataViewerListeners.attachPreviousPageButtonListener(button);
		button.setText("Previous");		
		windowControls.put(ControlConstants.PREVIOUS_BUTTON,button);
	}
		
	public void createUnformattedViewTabItem() {		
		if(isViewTabExist(Views.UNFORMATTED_VIEW_NAME)){
			CTabItem item=getViewTabItem(Views.UNFORMATTED_VIEW_NAME);
			tabFolder.setSelection(item);
			dataViewLoader.reloadloadViews();
			return;
		}
		
		CTabItem tbtmUnformattedView = new CTabItem(tabFolder, SWT.CLOSE);
		tbtmUnformattedView.setData("VIEW_NAME", Views.UNFORMATTED_VIEW_NAME);
		tbtmUnformattedView.setText(Views.UNFORMATTED_VIEW_DISPLAY_NAME);
		{
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmUnformattedView.setControl(composite);
			composite.setLayout(new GridLayout(1, false));
			{
				unformattedViewTextarea = new StyledText(composite, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
				unformattedViewTextarea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			}
		}
		
		tabFolder.setSelection(tbtmUnformattedView);
		dataViewLoader.setUnformattedViewTextarea(unformattedViewTextarea);
		dataViewLoader.reloadloadViews();
	}
	
	public void createFormatedViewTabItem() {
		
		if(isViewTabExist(Views.FORMATTED_VIEW_NAME)){
			CTabItem item=getViewTabItem(Views.FORMATTED_VIEW_NAME);
			tabFolder.setSelection(item);
			dataViewLoader.reloadloadViews();
			return;
		}
		
		CTabItem tbtmFormattedView = new CTabItem(tabFolder, SWT.CLOSE);
		tbtmFormattedView.setData("VIEW_NAME", Views.FORMATTED_VIEW_NAME);
		tbtmFormattedView.setText(Views.FORMATTED_VIEW_DISPLAYE_NAME);
		{
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmFormattedView.setControl(composite);
			composite.setLayout(new GridLayout(1, false));
			{
				formattedViewTextarea = new StyledText(composite, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
				formattedViewTextarea.setFont(SWTResourceManager.getFont("Courier New", 9, SWT.NORMAL));
				formattedViewTextarea.setEditable(false);
				formattedViewTextarea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			}
		}
		tabFolder.setSelection(tbtmFormattedView);
		dataViewLoader.setFormattedViewTextarea(formattedViewTextarea);
		dataViewLoader.reloadloadViews();
	}

	private void createHorizantalViewTabItem() {
		CTabItem tbtmHorizantalView = new CTabItem(tabFolder, SWT.CLOSE);
		tbtmHorizantalView.setData("VIEW_NAME", "HORIZANTAL_VIEW");
		tbtmHorizantalView.setText(Views.HORIZONTAL_VIEW_DISPLAY_NAME);
		{
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmHorizantalView.setControl(composite);
			composite.setLayout(new GridLayout(1, false));
			{
				ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
				scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				scrolledComposite.setExpandHorizontal(true);
				scrolledComposite.setExpandVertical(true);
				
				Composite stackLayoutComposite = new Composite(scrolledComposite, SWT.NONE);
				StackLayout stackLayout  = new StackLayout();
				stackLayoutComposite.setLayout(stackLayout);
				
				{
					Composite composite_4 = new Composite(stackLayoutComposite, SWT.NONE);
					GridLayout gl_composite_4 = new GridLayout(1, false);
					gl_composite_4.verticalSpacing = 0;
					gl_composite_4.marginWidth = 0;
					gl_composite_4.marginHeight = 0;
					gl_composite_4.horizontalSpacing = 0;
					composite_4.setLayout(gl_composite_4);
					{
						horizontalViewTableViewer = new TableViewer(composite_4, SWT.BORDER | SWT.FULL_SELECTION);
						Table table_1 = horizontalViewTableViewer.getTable();
						table_1.setLinesVisible(true);
						table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					}
					stackLayout.topControl = composite_4;
				}
				
				scrolledComposite.getShowFocusedControl();
				scrolledComposite.setShowFocusedControl(true);
				
				
				scrolledComposite.setContent(stackLayoutComposite);
				scrolledComposite.setMinSize(stackLayoutComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				
				installMouseWheelScrollRecursively(scrolledComposite);
				
				setTableLayoutToMappingTable(horizontalViewTableViewer);
				horizontalViewTableViewer.setContentProvider(new ArrayContentProvider());
				
				dataViewLoader.updateDataViewLists();
				horizontalViewTableViewer.setInput(gridViewData);
				horizontalViewTableViewer.refresh();
				
				for (int i = 0, n = horizontalViewTableViewer.getTable().getColumnCount(); i < n; i++)
					horizontalViewTableViewer.getTable().getColumn(i).pack();
				
				horizontalViewTableViewer.refresh();
			}
		}
	}
	
	private boolean isViewTabExist(String viewName){
		
		for(int index=0;index<tabFolder.getItemCount();index++){
			if(viewName.equals(tabFolder.getItem(index).getData("VIEW_NAME"))){
				return true;
			}
		}
		return false;
	}
	
	private CTabItem getViewTabItem(String viewName){
		
		for(int index=0;index<tabFolder.getItemCount();index++){
			if(viewName.equals(tabFolder.getItem(index).getData("VIEW_NAME"))){
				return tabFolder.getItem(index);
			}
		}
		return null;
	}
	
	public void createGridViewTabItem() {
		
		if(isViewTabExist(Views.GRID_VIEW_NAME)){
			CTabItem item=getViewTabItem(Views.GRID_VIEW_NAME);
			tabFolder.setSelection(item);
			return;
		}
		
		CTabItem tbtmGridview = new CTabItem(tabFolder, SWT.NONE);
		tbtmGridview.setData("VIEW_NAME", Views.GRID_VIEW_NAME);
		tbtmGridview.setText(Views.GRID_VIEW_DISPLAY_NAME);
		{
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmGridview.setControl(composite);
			
			
			composite.setLayout(new GridLayout(1, false));
			{
				ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
				scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				scrolledComposite.setExpandHorizontal(true);
				scrolledComposite.setExpandVertical(true);
				
				Composite stackLayoutComposite = new Composite(scrolledComposite, SWT.NONE);
				StackLayout stackLayout  = new StackLayout();
				stackLayoutComposite.setLayout(stackLayout);
				{
					Composite composite_1 = new Composite(stackLayoutComposite, SWT.NONE);
					GridLayout gl_composite_1 = new GridLayout(1, false);
					gl_composite_1.verticalSpacing = 0;
					gl_composite_1.marginWidth = 0;
					gl_composite_1.marginHeight = 0;
					gl_composite_1.horizontalSpacing = 0;
					composite_1.setLayout(gl_composite_1);
					composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
							1));
					{
						gridViewTableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
						Table table = gridViewTableViewer.getTable();
						table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
						table.setLinesVisible(true);
						table.setHeaderVisible(true);
						table.showSelection();
					}
					stackLayout.topControl = composite_1;
				}
				
				scrolledComposite.getShowFocusedControl();
				scrolledComposite.setShowFocusedControl(true);
				
				
				scrolledComposite.setContent(stackLayoutComposite);
				scrolledComposite.setMinSize(stackLayoutComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				
				/*installMouseWheelScrollRecursively(scrolledComposite);
				
				createGridViewTableColumns(gridViewTableViewer);
				TableColumnLayout layout = setTableLayoutToMappingTable(gridViewTableViewer);
				gridViewTableViewer.setContentProvider(new ArrayContentProvider());
				gridViewTableViewer.setInput(gridViewData);
				
				dataViewLoader.setGridViewTableViewer(gridViewTableViewer);
				dataViewLoader.updateDataViewLists();
								
				Table table = gridViewTableViewer.getTable();
				
				for (int columnIndex = 0, n = table.getColumnCount(); columnIndex < n; columnIndex++) {
					table.getColumn(columnIndex).pack();
				}
				gridViewTableViewer.refresh();
				for (int i = 0; i < table.getColumnCount(); i++) {
					layout.setColumnData(table.getColumn(i), new ColumnWeightData(1));
				}
				gridViewTableViewer.refresh();
				for (int columnIndex = 0, n = table.getColumnCount(); columnIndex < n; columnIndex++) {
					table.getColumn(columnIndex).pack();
				}*/
							
				
				
				createGridViewTableColumns(gridViewTableViewer);
				
				gridViewTableViewer.setContentProvider(new ArrayContentProvider());
				gridViewTableViewer.setInput(gridViewData);
				
				dataViewLoader.setGridViewTableViewer(gridViewTableViewer);
				dataViewLoader.updateDataViewLists();
				
				gridViewTableViewer.getTable().getColumn(0).pack();				
				
				gridViewTableViewer.refresh();
			}
		}
		
	}
	
	private TableColumnLayout setTableLayoutToMappingTable(TableViewer tableViewer) {
		TableColumnLayout layout = new TableColumnLayout();
		tableViewer.getControl().getParent().setLayout(layout);
		
		tableViewer.refresh();
		return layout;
	}
	
	private void createGridViewTableIndexColumn(final TableViewer tableViewer){
		final TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnItem = tableViewerColumn.getColumn();
		tblclmnItem.setWidth(100);

		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public Color getBackground(Object element) {
				return SWTResourceManager.getColor(SWT.COLOR_GRAY);
			}
			
			@Override
			public String getText(Object element) {
				RowData p = (RowData) element;
				return String.valueOf(p.getRowNumber());
			}					
		});
	}
	private void createGridViewTableColumns(final TableViewer tableViewer) {
		
		createGridViewTableIndexColumn(tableViewer);
		try {
			int index = 0;
			for(String columnName: csvAdapter.getColumnList()){
				final TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn tblclmnItem = tableViewerColumn.getColumn();
				tblclmnItem.setWidth(100);
				tblclmnItem.setText(columnName);
				tableViewerColumn.getColumn().setData("ID", index);
				tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {

					@Override
					public String getText(Object element) {
						RowData p = (RowData) element;
						return p.getColumns()
								.get((int) tableViewerColumn.getColumn()
										.getData("ID")).getValue();
					}					
				});
				
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		actionFactory = new ActionFactory(this);
		MenuManager menuManager = new MenuManager(MenuConstants.MENU);
		menuManager.setVisible(true);

		createFileMenu(menuManager);
		createEditMenu(menuManager);
		createViewMenu(menuManager);

		return menuManager;
	}
	
	private MenuManager createMenu(MenuManager menuManager, String menuName) {
		MenuManager menu = new MenuManager(menuName);
		menuManager.add(menu);
		menuManager.setVisible(true);
		return menu;
	}
	
	private void createFileMenu(MenuManager menuManager) {
		MenuManager fileMenu = createMenu(menuManager, MenuConstants.FILE);
		menuManager.add(fileMenu);
		fileMenu.setVisible(true);

		fileMenu.add(actionFactory.getAction(ExportAction.class.getName()));
		fileMenu.add(actionFactory.getAction(FilterAction.class.getName()));
	}
	
	private void createEditMenu(MenuManager menuManager) {
		MenuManager editMenu = createMenu(menuManager, MenuConstants.EDIT);
		menuManager.add(editMenu);
		editMenu.setVisible(true);
		
		editMenu.add(actionFactory.getAction(SelectAllAction.class.getName()));
		editMenu.add(actionFactory.getAction(CopyAction.class.getName()));
		editMenu.add(actionFactory.getAction(FindAction.class.getName()));
	}

	private void createViewMenu(MenuManager menuManager) {
		MenuManager viewMenu = createMenu(menuManager, MenuConstants.VIEW);
		menuManager.add(viewMenu);
		viewMenu.setVisible(true);
				
		viewMenu.add(actionFactory.getAction(GridViewAction.class.getName()));
		viewMenu.add(actionFactory.getAction(FormattedViewAction.class.getName()));
		viewMenu.add(actionFactory.getAction(UnformattedViewAction.class.getName()));
		viewMenu.add(new Separator());
		viewMenu.add(actionFactory.getAction(ReloadAction.class.getName()));
		viewDataPreferences = getViewDataPreferencesFromPreferenceFile();
		viewMenu.add(actionFactory.getAction(PreferencesAction.class.getName()));
	}
	
	
	
	
	public ViewDataPreferences getViewDataPreferencesFromPreferenceFile() {
		boolean includeHeaderValue = false;
		IScopeContext context = new InstanceScope();
		IEclipsePreferences eclipsePreferences = context.getNode(PLUGIN_NAME);
		String delimiter = eclipsePreferences.get(DELIMITER, DEFAULT);
		String quoteCharactor = eclipsePreferences.get(QUOTE_CHARACTOR,DEFAULT);
		String includeHeader = eclipsePreferences.get(INCLUDE_HEADERS, DEFAULT);
		String fileSize=eclipsePreferences.get(FILE_SIZE,DEFAULT);
		String pageSize=eclipsePreferences.get(PAGE_SIZE,DEFAULT);
		delimiter = delimiter.equalsIgnoreCase(DEFAULT) ? DEFAULT_DELIMITER : delimiter;
		quoteCharactor = quoteCharactor.equalsIgnoreCase(DEFAULT) ? DEFAULT_QUOTE_CHARACTOR : quoteCharactor;
		includeHeaderValue = includeHeader.equalsIgnoreCase(DEFAULT) ? true : false;
		fileSize = fileSize.equalsIgnoreCase(DEFAULT) ? DEFAULT_FILE_SIZE : fileSize;
		pageSize = pageSize.equalsIgnoreCase(DEFAULT) ? DEFAULT_PAGE_SIZE : pageSize;
		ViewDataPreferences viewDataPreferences = new ViewDataPreferences(delimiter, quoteCharactor, includeHeaderValue,fileSize,pageSize);
		return viewDataPreferences;
	}
	

	/**
	 * Create the coolbar manager.
	 * @return the coolbar manager
	 */
	@Override
	protected CoolBarManager createCoolBarManager(int style) {
		CoolBarManager coolBarManager = new CoolBarManager(style);

		actionFactory = new ActionFactory(this);
		
		ToolBarManager toolBarManager = new ToolBarManager();
		coolBarManager.add(toolBarManager);
		addtoolbarAction(
				toolBarManager,
				(XMLConfigUtil.CONFIG_FILES_PATH + "/icons/advicons/export.png"),
				actionFactory.getAction(ExportAction.class.getName()));
		addtoolbarAction(
				toolBarManager,
				(XMLConfigUtil.CONFIG_FILES_PATH + "/icons/advicons/lookup.png"),
				actionFactory.getAction(FindAction.class.getName()));
		addtoolbarAction(
				toolBarManager,
				(XMLConfigUtil.CONFIG_FILES_PATH + "/icons/advicons/refresh.png"),
				actionFactory.getAction(ReloadAction.class.getName()));
		addtoolbarAction(
				toolBarManager,
				(XMLConfigUtil.CONFIG_FILES_PATH + "/icons/advicons/filter.png"),
				actionFactory.getAction(FilterAction.class.getName()));
		Action dropDownAction = new Action("", SWT.DROP_DOWN) {
			@Override
			public void run() {
				tabFolder.showItem(tabFolder.getItem(0));
				tabFolder.setSelection(0);
			}
		};
		dropDownAction.setImageDescriptor(new ImageDescriptor() {

			@Override
			public ImageData getImageData() {
				return new ImageData(XMLConfigUtil.CONFIG_FILES_PATH
						+ "/icons/advicons/switchview.png");
			}
		});
		
		dropDownAction.setMenuCreator(new ViewDataGridMenuCreator(actionFactory));
		toolBarManager.add(dropDownAction);

		return coolBarManager;
	}

	private void addtoolbarAction(ToolBarManager toolBarManager,
			final String imagePath, Action action) {

		ImageDescriptor exportImageDescriptor = new ImageDescriptor() {
			@Override
			public ImageData getImageData() {
				ImageData imageData = new ImageData(imagePath);
				return imageData;
			}
		};
		action.setImageDescriptor(exportImageDescriptor);
		toolBarManager.add(action);
	}
	
	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		
		//DO NOTE DELETE BELOW CODE - this will be used as new feature
		
		 /*StatusField readOnlyStatus = new StatusField(10, "Hello", "test Hello"); 
		 StatusField readOnlyStatus2 = new StatusField(10, "Hi", "test Hi"); 
		
		 statusLineManager.add(readOnlyStatus);
		 statusLineManager.add(new Separator("Group1"));
		 statusLineManager.add(readOnlyStatus2);
		 
		 
		 statusLineManager.insert(0, readOnlyStatus);
		 
		 //statusLineManager.appendToGroup(statusLineManager.BEGIN_GROUP, readOnlyStatus);
		 statusLineManager.appendToGroup(statusLineManager.END_GROUP, readOnlyStatus2);*/
		statusManager = new StatusManager();
		statusManager.setStatusLineManager(statusLineManager);
		return statusLineManager;
	}


	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			DebugDataViewer window = new DebugDataViewer();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(848, 469);
	}
	
	/**
	 * make wheel scrolling available by installing a wheel listener on this
	 * scrollable's parent and hierarchy of children
	 * 
	 * @param scrollable
	 *            the scrolledComposite to wheel-scroll
	 */
	private static void installMouseWheelScrollRecursively(
			final ScrolledComposite scrollable) {
		MouseWheelListener scroller = createMouseWheelScroller(scrollable);
		if (scrollable.getParent() != null)
			scrollable.getParent().addMouseWheelListener(scroller);
		installMouseWheelScrollRecursively(scroller, scrollable);
	}

	private static MouseWheelListener createMouseWheelScroller(
			final ScrolledComposite scrollable) {
		return new MouseWheelListener() {

			@Override
			public void mouseScrolled(MouseEvent e) {
				Point currentScroll = scrollable.getOrigin();
				scrollable.setOrigin(currentScroll.x, currentScroll.y
						- (e.count * 5));
			}
		};
	}

	private static void installMouseWheelScrollRecursively(
			MouseWheelListener scroller, Control c) {
		c.addMouseWheelListener(scroller);
		if (c instanceof Composite) {
			Composite comp = (Composite) c;
			for (Control child : comp.getChildren()) {
				installMouseWheelScrollRecursively(scroller, child);
			}
		}
	}

	public TableViewer getTableViewer() {
		return gridViewTableViewer;
	}

	public ViewDataPreferences getViewDataPreferences() {
		return viewDataPreferences;
	}
	
	@Override
	public boolean close() {
		csvAdapter.dispose();
		return super.close();
	}

}

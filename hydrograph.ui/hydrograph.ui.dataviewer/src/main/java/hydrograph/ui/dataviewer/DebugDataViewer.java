package hydrograph.ui.dataviewer;

import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.dataviewer.actions.ActionFactory;
import hydrograph.ui.dataviewer.actions.ViewDataGridMenuCreator;
import hydrograph.ui.dataviewer.adapters.CSVAdapter;
import hydrograph.ui.dataviewer.constants.ADVConstants;
import hydrograph.ui.dataviewer.constants.Views;
import hydrograph.ui.dataviewer.datastructures.ColumnData;
import hydrograph.ui.dataviewer.datastructures.RowData;
import hydrograph.ui.dataviewer.listeners.DataViewerListeners;
import hydrograph.ui.dataviewer.utilities.Utils;
import hydrograph.ui.dataviewer.viewloders.DataViewLoader;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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
import org.eclipse.jface.viewers.ColumnWeightData;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class DebugDataViewer extends ApplicationWindow {
	
	private CTabFolder tabFolder;
	
	private StyledText unformattedViewTextarea;
	private StyledText formattedViewTextarea;
	private TableViewer horizontalViewTableViewer;
	private TableViewer gridViewTableViewer;
	private Text pageNumberDisplayTextBox;
	private Text jumpPageTextBox;
	private Button jumpPageButton;
	private Button nextPageButton;
	private StatusLineManager statusLineManager;
	
	private CSVAdapter csvAdapter;
	private List<Control> windowControls;
	
	private static List<RowData> gridViewData;
	private static List<RowData> formattedViewData;
	private static List<RowData> unformattedViewData;
	
	private String database;
	private String tableName;
	
	private String windowName="Debug Data viewer";
	
	private ActionFactory actionFactory;
	
	private DataViewLoader dataViewLoader;
	private DataViewerListeners dataViewerListeners;
	
	/**
	 * Create the application window,
	 */
	public DebugDataViewer() {
		super(null);
		createActions();
		addCoolBar(SWT.FLAT);
		addMenuBar();
		addStatusLine();
		windowControls = new LinkedList<>();
		gridViewData = new LinkedList<>();
		actionFactory = new ActionFactory(this);
	}

	
	/**
	 * Create the application window,
	 */
	public DebugDataViewer(String filePath,String fileName,String windowName) {
		super(null);
		createActions();
		addCoolBar(SWT.FLAT);
		addMenuBar();
		addStatusLine();
		windowControls = new LinkedList<>();
		gridViewData = new LinkedList<>();
		this.database = filePath;
		this.tableName = fileName;
		actionFactory = new ActionFactory(this);
		
		if(windowName!=null)
			this.windowName = "Data Viewer - " + windowName;
		
	}
	
	
	public String getWindowName() {
		return windowName;
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		setDataViewerWindowTitle();
		initializeDataFileAdapter();

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
		dataViewerListeners.setPageNumberDisplayTextBox(pageNumberDisplayTextBox);
		dataViewerListeners.setStatusLineManager(statusLineManager);
		dataViewerListeners.setWindowControls(windowControls);
		dataViewerListeners.setJumpPageTextBox(jumpPageTextBox);
		
		dataViewerListeners.addTabFolderSelectionChangeListener(tabFolder);
		
		dataViewerListeners.setDefaultStatusMessage();

		java.util.Date date = new java.util.Date();
		System.out.println("+++ ADV End: " + new Timestamp(date.getTime()));
		
		jumpPageTextBox.setEnabled(false);
		jumpPageButton.setEnabled(false);
		return container;

	}


	private void initializeDataFileAdapter() {
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
		{
			createJumpPageLabel(composite_3);
		}
		{
			createJumpPageTextBox(composite_3);
		}
		{
			createJumpPageButton(composite_3);
		}
	}


	private void createJumpPageButton(Composite composite_3) {
		jumpPageButton = new Button(composite_3, SWT.NONE);
		dataViewerListeners.attachJumpToPageListener(jumpPageButton,nextPageButton);
		jumpPageButton.setText("Go");
		jumpPageButton.setData("CONTROL_NAME","JUMP_BUTTON");
		windowControls.add(jumpPageButton);
	}

	private void createJumpPageTextBox(Composite composite_3) {
		jumpPageTextBox = new Text(composite_3, SWT.BORDER);
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
		
		dataViewerListeners.attachJumpToPageListener(jumpPageTextBox,nextPageButton);
		jumpPageTextBox.setData("CONTROL_NAME","JUMP_TEXT");
		windowControls.add(jumpPageTextBox);
		
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
		nextPageButton = new Button(composite_3, SWT.NONE);
		dataViewerListeners.attachNextPageButtonListener(nextPageButton);
		nextPageButton.setText("Next");
		nextPageButton.setData("CONTROL_NAME","NEXT_BUTTON");
		windowControls.add(nextPageButton);
		
	}


	private void createPageNumberDisplay(Composite composite_3) {
		pageNumberDisplayTextBox = new Text(composite_3, SWT.BORDER | SWT.CENTER);
		pageNumberDisplayTextBox.setEnabled(false);
		pageNumberDisplayTextBox.setEditable(false);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 178;
		pageNumberDisplayTextBox.setLayoutData(gd_text);
		
		//csvAdapter.getRowCount();
		
		Job job = new Job("Fetaching total number of rows") {
			  @Override
			  protected IStatus run(IProgressMonitor monitor) {
				  csvAdapter.fetchRowCount();
				  
				  Display.getDefault().asyncExec(new Runnable() {
			      @Override
			      public void run() {
			    	 dataViewerListeners.setDefaultStatusMessage();
			    	 jumpPageButton.setEnabled(true);
			    	 jumpPageTextBox.setEnabled(true);
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
		button.setData("CONTROL_NAME","PREVIOUS_BUTTON");
		
		windowControls.add(button);
	}
	
	/*public void setDefaultStatusMessage() {
		StringBuilder stringBuilder = new StringBuilder();
		
		statusLineManager.setErrorMessage(null);
		
		if(csvAdapter.getRowCount()!=null){
			stringBuilder.append("Record Count: " + csvAdapter.getRowCount() + " | ");
		}else{
			stringBuilder.append("Counting number of records... | ");
		}
		
		stringBuilder.append("Showing records from " + csvAdapter.getOFFSET() + " to " + (csvAdapter.getOFFSET() + csvAdapter.getPAGE_SIZE()) + " | ");
		statusLineManager.setMessage(stringBuilder.toString().substring(0,stringBuilder.length() -2));
		
		dataViewerListeners.updatePageNumberDisplayPanel();
	}*/

	
	public void createUnformattedViewTabItem() {
		
		if(isViewTabExist(Views.UNFORMATTED_VIEW_NAME)){
			CTabItem item=getViewTabItem(Views.UNFORMATTED_VIEW_NAME);
			tabFolder.setSelection(item);
			dataViewLoader.reloadloadViews();
			return;
		}
		
		CTabItem tbtmUnformattedView = new CTabItem(tabFolder, SWT.CLOSE);
		tbtmUnformattedView.setData("VIEW_NAME", Views.UNFORMATTED_VIEW_NAME);
		tbtmUnformattedView.setText("Unformatted View");
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
		tbtmFormattedView.setText("Formatted view");
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
		tbtmHorizantalView.setText("Horizantal view");
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
				
				//createHorizontalViewTableColumns(horizontalViewTableViewer);
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
		tbtmGridview.setText("Grid view");
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
				
				installMouseWheelScrollRecursively(scrolledComposite);
				
				createGridViewTableColumns(gridViewTableViewer);
				TableColumnLayout layout = setTableLayoutToMappingTable(gridViewTableViewer);
				gridViewTableViewer.setContentProvider(new ArrayContentProvider());
				gridViewTableViewer.setInput(gridViewData);
				
				dataViewLoader.setGridViewTableViewer(gridViewTableViewer);
				dataViewLoader.updateDataViewLists();
				
				/*gridViewTableViewer.refresh();
				
				for (int i = 0, n = gridViewTableViewer.getTable().getColumnCount(); i < n; i++)
					gridViewTableViewer.getTable().getColumn(i).pack();
								
				gridViewTableViewer.refresh();
				
				for (int i = 0; i < gridViewTableViewer.getTable().getColumnCount(); i++)
					gridViewTableViewer.getTable().getColumn(i).pack();*/
				
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
				}
												
				gridViewTableViewer.refresh();
			}
		}
		
	}
	
	private TableColumnLayout setTableLayoutToMappingTable(TableViewer tableViewer) {
		Table table =tableViewer.getTable();
		TableColumnLayout layout = new TableColumnLayout();
		tableViewer.getControl().getParent().setLayout(layout);

		/*for (int columnIndex = 0, n = table.getColumnCount(); columnIndex < n; columnIndex++) {
			table.getColumn(columnIndex).pack();
		}

		for (int i = 0; i < table.getColumnCount(); i++) {
			layout.setColumnData(table.getColumn(i), new ColumnWeightData(1));
		}*/
		
		
		tableViewer.refresh();
		return layout;
	}
	
	private void createGridViewTableIndexColumn(final TableViewer tableViewer){
		final TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnItem = tableViewerColumn.getColumn();
		tblclmnItem.setWidth(100);
		//tblclmnItem.setText("SR.No");
		//tableViewerColumn.getColumn().setData("ID", index);
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
		MenuManager menuManager = new MenuManager("menu");
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
		MenuManager fileMenu = createMenu(menuManager, "File");
		menuManager.add(fileMenu);
		fileMenu.setVisible(true);

		fileMenu.add(actionFactory.getAction("ExportAction"));
		fileMenu.add(actionFactory.getAction("FilterAction"));
	}
	
	private void createEditMenu(MenuManager menuManager) {
		MenuManager editMenu = createMenu(menuManager, "Edit");
		menuManager.add(editMenu);
		editMenu.setVisible(true);
		
		/*editMenu.add(new SelectAllAction("Select All",this));
		editMenu.add(new CopyAction("Copy",this));
		editMenu.add(new FindAction("Find",this));*/
		editMenu.add(actionFactory.getAction("SelectAllAction"));
		editMenu.add(actionFactory.getAction("CopyAction"));
		editMenu.add(actionFactory.getAction("FindAction"));
	}

	private void createViewMenu(MenuManager menuManager) {
		MenuManager viewMenu = createMenu(menuManager, "View");
		menuManager.add(viewMenu);
		viewMenu.setVisible(true);
		
		/*viewMenu.add(new GridViewAction("Grid View", this));
		viewMenu.add(new HorizontalViewAction("Horizontal View",this));
		viewMenu.add(new UnformattedViewAction("Unformatted View", this));
		viewMenu.add(new FormattedViewAction("Formatted View", this));
		viewMenu.add(new Separator());
		viewMenu.add(new ReloadAction("Reload", this));
		viewMenu.add(new PreferencesAction("Preferences",this));*/
		
		viewMenu.add(actionFactory.getAction("GridViewAction"));
		//viewMenu.add(actionFactory.getAction("HorizontalViewAction"));
		viewMenu.add(actionFactory.getAction("FormattedViewAction"));
		viewMenu.add(actionFactory.getAction("UnformattedViewAction"));
		viewMenu.add(new Separator());
		viewMenu.add(actionFactory.getAction("ReloadAction"));
		viewMenu.add(actionFactory.getAction("PreferencesAction"));
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
		/*Action action;
		action = new ExportAction("Export",this);*/
		addtoolbarAction(
				toolBarManager,
				(XMLConfigUtil.CONFIG_FILES_PATH + "/icons/advicons/export.png"),
				actionFactory.getAction("ExportAction"));
		addtoolbarAction(
				toolBarManager,
				(XMLConfigUtil.CONFIG_FILES_PATH + "/icons/advicons/lookup.png"),
				actionFactory.getAction("FindAction"));
		addtoolbarAction(
				toolBarManager,
				(XMLConfigUtil.CONFIG_FILES_PATH + "/icons/advicons/refresh.png"),
				actionFactory.getAction("ReloadAction"));
		addtoolbarAction(
				toolBarManager,
				(XMLConfigUtil.CONFIG_FILES_PATH + "/icons/advicons/filter.png"),
				actionFactory.getAction("FilterAction"));
		Action dropDownAction = new Action("", SWT.DROP_DOWN) {
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
		statusLineManager = new StatusLineManager();
		
		//DO NOTE DELETE BELOW CODE - this will be used as new feature
		
		 /*StatusField readOnlyStatus = new StatusField(10, "Hello", "test Hello"); 
		 StatusField readOnlyStatus2 = new StatusField(10, "Hi", "test Hi"); 
		
		 statusLineManager.add(readOnlyStatus);
		 statusLineManager.add(new Separator("Group1"));
		 statusLineManager.add(readOnlyStatus2);
		 
		 
		 statusLineManager.insert(0, readOnlyStatus);
		 
		 //statusLineManager.appendToGroup(statusLineManager.BEGIN_GROUP, readOnlyStatus);
		 statusLineManager.appendToGroup(statusLineManager.END_GROUP, readOnlyStatus2);*/
		 
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
		newShell.setText("New Application");
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

	
}

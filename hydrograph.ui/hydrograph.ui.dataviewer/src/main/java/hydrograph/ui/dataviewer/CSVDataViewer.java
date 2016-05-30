package hydrograph.ui.dataviewer;

import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.dataviewer.actions.ClearAction;
import hydrograph.ui.dataviewer.actions.CloseAction;
import hydrograph.ui.dataviewer.actions.CopyAction;
import hydrograph.ui.dataviewer.actions.ExportAction;
import hydrograph.ui.dataviewer.actions.FilterAction;
import hydrograph.ui.dataviewer.actions.FindAction;
import hydrograph.ui.dataviewer.actions.GoAction;
import hydrograph.ui.dataviewer.actions.GridViewAction;
import hydrograph.ui.dataviewer.actions.HorizontalViewAction;
import hydrograph.ui.dataviewer.actions.PreferencesAction;
import hydrograph.ui.dataviewer.actions.ReloadAction;
import hydrograph.ui.dataviewer.actions.SelectAllAction;
import hydrograph.ui.dataviewer.actions.StopAction;
import hydrograph.ui.dataviewer.actions.UnformattedViewAction;
import hydrograph.ui.dataviewer.actions.ViewDataGridMenuCreator;
import hydrograph.ui.dataviewer.datastructures.ColumnData;
import hydrograph.ui.dataviewer.datastructures.RowData;
import hydrograph.ui.dataviewer.datastructures.Schema;
import hydrograph.ui.dataviewer.support.MyViewerComparator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

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
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.json.JSONException;
import org.json.JSONObject;

public class CSVDataViewer extends ApplicationWindow {
	private Text text;
	StatusLineManager statusLineManager;
	private Table table;

	private static List<RowData> dataList = new LinkedList<>();
	TableViewer tableViewer;
	private static final int PAGE_LIMIT = 200;
	// private int offset=1999800;
	private long offset = 0;
	private MyViewerComparator comparator;

	private List<Schema> schemaList = new LinkedList<>();
	private Text pageCountLabel;
	private long recordCount;
	private long numberOfPages;
	private long currentPageNumber;
	private Composite formattedTextViewComposite;
	private StackLayout stackLayout;

	private Control virticalGridViewControl;

	private StyledText styledText;

	private Composite stackLayoutComposite;

	String directoryName;
	String fileName;

	/**
	 * Create the application window,
	 */
	public CSVDataViewer() {
		super(null);
		createActions();
		addCoolBar(SWT.FLAT);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {

		populateSchemaList();

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		createDataViewPanel(container);
		createPaginationPanel(container);

		return container;
	}

	private void populateSchemaList() {
		schemaList.add(new Schema("java.lang.String", null));
		schemaList.add(new Schema("java.lang.String", null));
		schemaList.add(new Schema("java.lang.Integer", null));
		schemaList.add(new Schema("java.lang.Long", null));
		schemaList.add(new Schema("java.lang.Double", null));
		schemaList.add(new Schema("java.lang.Double", null));
		schemaList.add(new Schema("java.lang.Float", null));
		schemaList.add(new Schema("java.lang.Float", null));
		schemaList.add(new Schema("java.lang.Short", null));
		schemaList.add(new Schema("java.util.Date", "YYYY-MM-DD"));
		schemaList.add(new Schema("java.util.Date", "DDMMYYYY"));
		schemaList.add(new Schema("java.util.Date", "DD.MMM.YYYY"));
		schemaList.add(new Schema("java.util.Date", "yyyy-MM-dd HH:mm:ss"));
		schemaList.add(new Schema("java.util.Date", "yyyy-MM-dd hh:mm:ss"));
		schemaList.add(new Schema("java.util.Date", "ddMMyy"));
		schemaList.add(new Schema("java.math.BigDecimal", null));
		schemaList.add(new Schema("java.lang.String", null));
		schemaList.add(new Schema("java.lang.String", null));
		schemaList.add(new Schema("java.lang.Integer", null));
		schemaList.add(new Schema("java.lang.Long", null));
		schemaList.add(new Schema("java.lang.Double", null));
		schemaList.add(new Schema("java.lang.Double", null));
		schemaList.add(new Schema("java.lang.Float", null));
		schemaList.add(new Schema("java.lang.Float", null));
		schemaList.add(new Schema("java.lang.Short", null));
		schemaList.add(new Schema("java.util.Date", "YYYY-MM-DD"));
		schemaList.add(new Schema("java.util.Date", "DDMMYYYY"));
		schemaList.add(new Schema("java.util.Date", "DD.MMM.YYYY"));
		schemaList.add(new Schema("java.util.Date", "yyyy-MM-dd HH:mm:ss"));
		schemaList.add(new Schema("java.util.Date", "yyyy-MM-dd hh:mm:ss"));
		schemaList.add(new Schema("java.util.Date", "ddMMyy"));
		schemaList.add(new Schema("java.math.BigDecimal", null));
	}

	/*
	 * private void populateSchemaList(){ schemaList.add(new
	 * Schema("java.lang.String", null)); schemaList.add(new
	 * Schema("java.lang.Double", null)); schemaList.add(new
	 * Schema("java.util.Date", "yyyy-MM-dd")); schemaList.add(new
	 * Schema("java.lang.Integer", null)); schemaList.add(new
	 * Schema("java.math.BigDecimal", null)); }
	 */

	private void createDataViewPanel(Composite container) {
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		/*ScrolledComposite scrolledComposite = new ScrolledComposite(composite,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);*/
		ScrolledComposite scrolledComposite = new ScrolledComposite(composite,
				SWT.BORDER );
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		stackLayoutComposite = new Composite(scrolledComposite, SWT.NONE);
		stackLayout = new StackLayout();
		stackLayoutComposite.setLayout(stackLayout);

		createFormattedTextView(stackLayoutComposite);

		virticalGridViewControl = createVerticalGridView(stackLayoutComposite);

		stackLayout.topControl = virticalGridViewControl;

		//scrolledComposite.getShowFocusedControl();
		//scrolledComposite.setShowFocusedControl(true);

		scrolledComposite.setContent(stackLayoutComposite);
		scrolledComposite.setMinSize(stackLayoutComposite.computeSize(
				SWT.DEFAULT, SWT.DEFAULT));

		installMouseWheelScrollRecursively(scrolledComposite);

	}

	private void populateDataList() {
		dataList.clear();
		try {
			Class.forName("org.relique.jdbc.csv.CsvDriver");
			Connection conn = DriverManager.getConnection("jdbc:relique:csv:"
					+ directoryName);
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("SELECT * FROM " + fileName
					+ " LIMIT " + PAGE_LIMIT + " OFFSET " + offset);
			int columnCount = results.getMetaData().getColumnCount();
			while (results.next()) {
				List<ColumnData> row = new LinkedList<>();
				for (int index = 0; index < columnCount; index++) {
					// row.add(new CellData("String",
					// results.getString(index+1)));
					row.add(new ColumnData(results.getString(index + 1),
							schemaList.get(index)));
				}
				dataList.add(new RowData(row));
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private SelectionAdapter getSelectionAdapter(final TableColumn column,
			final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				int dir = comparator.getDirection();
				tableViewer.getTable().setSortDirection(dir);
				tableViewer.getTable().setSortColumn(column);
				tableViewer.refresh();
			}
		};
		return selectionAdapter;
	}

	/*
	 * private void attachMouseScrollButtonListener(final ScrolledComposite
	 * scrolledComposite){ table.addMouseWheelListener(new MouseWheelListener()
	 * {
	 * 
	 * @Override public void mouseScrolled(MouseEvent e) {
	 * scrolledComposite.setFocus(); } });
	 * //scrolledComposite.setShowFocusedControl(true);
	 * table.addListener(SWT.MouseWheel, new Listener() {
	 * 
	 * @Override public void handleEvent(Event event) { int wheelCount =
	 * event.count; wheelCount = (int) Math.ceil(wheelCount / 3.0f); while
	 * (wheelCount < 0) { scrolledComposite.getVerticalBar().setIncrement(4);
	 * wheelCount++; }
	 * 
	 * while (wheelCount > 0) {
	 * scrolledComposite.getVerticalBar().setIncrement(-4); wheelCount--; }
	 * //scrolledComposite.setContent(composite_1);
	 * //scrolledComposite.setMinSize(composite_1.computeSize(SWT.DEFAULT,
	 * SWT.DEFAULT));
	 * //scrolledComposite.setContent(scrolledComposite.getParent()); } });
	 * 
	 * scrolledComposite.addListener(SWT.Activate, new Listener() { public void
	 * handleEvent(Event e) { scrolledComposite.setFocus(); } }); }
	 */

	private long getFileRecordCount() {
		long intRecordCount = 0;
		try {
			Class.forName("org.relique.jdbc.csv.CsvDriver");
			Connection conn = DriverManager.getConnection("jdbc:relique:csv:"
					+ directoryName);
			Statement stmt = conn.createStatement();
			// Read from file mytable.csv inside the ZIP file
			ResultSet results = stmt.executeQuery("SELECT COUNT(1) FROM "
					+ fileName);

			results.next();
			intRecordCount = results.getLong(1);

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return intRecordCount;
	}

	private void createTableColumns(final TableViewer tableViewer) {

		try {
			Class.forName("org.relique.jdbc.csv.CsvDriver");
			/*
			 * Connection conn = DriverManager.getConnection("jdbc:relique:csv:"
			 * + "C:\\Users\\shrirangk\\Desktop\\csvtest");
			 */
			Connection conn = DriverManager.getConnection("jdbc:relique:csv:"
					+ directoryName);
			Statement stmt = conn.createStatement();
			// ResultSet results =
			// stmt.executeQuery("SELECT * FROM Generated_Records LIMIT 1");
			ResultSet results = stmt.executeQuery("SELECT * FROM " + fileName
					+ " LIMIT 0");

			for (int index = 0; index < results.getMetaData().getColumnCount(); index++) {
				final TableViewerColumn tableViewerColumn = new TableViewerColumn(
						tableViewer, SWT.NONE);
				TableColumn tblclmnItem = tableViewerColumn.getColumn();
				tblclmnItem.setWidth(100);
				tblclmnItem.setText(results.getMetaData().getColumnName(
						index + 1));
				tableViewerColumn.getColumn().setData("ID", index);
				tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {

					@Override
					public String getText(Object element) {
						RowData p = (RowData) element;
						return p.getColumns()
								.get((int) tableViewerColumn.getColumn()
										.getData("ID")).getValue();
					}

					/*@Override
					public void update(ViewerCell cell) {
						// TODO Auto-generated method stub
						super.update(cell);

						Table table = tableViewer.getTable();
						for (int i = 0, n = table.getColumnCount(); i < n; i++)
							table.getColumn(i).pack();

					}*/
				});
				/*tableViewerColumn.getColumn().addSelectionListener(
						getSelectionAdapter(tableViewerColumn.getColumn(),
								index));*/
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private Control createVerticalGridView(Composite composite_1) {
		Composite composite = new Composite(composite_1, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		tableViewer = new TableViewer(composite, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.MULTI);
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.showSelection();

		/*
		 * createTableColumns(tableViewer);
		 * 
		 * tableViewer.setContentProvider(new ArrayContentProvider());
		 * comparator = new MyViewerComparator();
		 * tableViewer.setComparator(comparator);
		 * 
		 * tableViewer.setInput(dataList);
		 * 
		 * populateDataList(); tableViewer.refresh();
		 */

		// loadDataFile();

		return composite;
	}

	private void setTableLayoutToMappingTable(TableViewer tableViewer,
			Table table) {
		TableColumnLayout layout = new TableColumnLayout();
		tableViewer.getControl().getParent().setLayout(layout);

		for (int columnIndex = 0, n = table.getColumnCount(); columnIndex < n; columnIndex++) {
			table.getColumn(columnIndex).pack();
		}

		for (int i = 0; i < table.getColumnCount(); i++) {
			layout.setColumnData(table.getColumn(i), new ColumnWeightData(1));
		}
	}

	private void createFormattedTextView(Composite composite_1) {
		formattedTextViewComposite = new Composite(composite_1, SWT.NONE);
		formattedTextViewComposite.setLayout(new GridLayout(1, false));
		formattedTextViewComposite.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, true, 1, 1));

		styledText = new StyledText(formattedTextViewComposite, SWT.BORDER);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
	}

	private void clearTable() {
		TableColumn[] columns = tableViewer.getTable().getColumns();

		for (TableColumn column : columns) {
			column.dispose();
		}

		tableViewer.setInput(null);

		tableViewer.refresh();
	}

	public void loadDataFile(String directoryName, String fileName) {

		// clearTable();

		this.directoryName = directoryName;
		this.fileName = fileName;

		recordCount = getFileRecordCount();
		numberOfPages = (recordCount / PAGE_LIMIT);
		currentPageNumber = 1;

		createTableColumns(tableViewer);

		setTableLayoutToMappingTable(tableViewer, table);

		tableViewer.setContentProvider(new ArrayContentProvider());
		// SORT ALGO
		// comparator = new MyViewerComparator();
		// tableViewer.setComparator(comparator);

		tableViewer.setInput(dataList);

		populateDataList();
		tableViewer.refresh();

		pageCountLabel.setText(currentPageNumber + "/" + numberOfPages);
		setDefaultStatusText();
		// setTableLayoutToMappingTable(tableViewer, table);
		
		Table table = tableViewer.getTable();
		for (int i = 0, n = table.getColumnCount(); i < n; i++)
			table.getColumn(i).pack();
		
		tableViewer.refresh();
		
		
	}

	private void createPaginationPanel(Composite container) {

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(3, false));
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite_1.widthHint = 303;
		composite_1.setLayoutData(gd_composite_1);

		Button btnPrevious = new Button(composite_1, SWT.NONE);
		btnPrevious.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (fileName == null) {
					return;
				}

				if ((offset - PAGE_LIMIT) >= 0) {
					statusLineManager.setMessage("Loading page.....");
					offset = offset - PAGE_LIMIT;
					populateDataList();
					tableViewer.refresh();
					currentPageNumber--;
					pageCountLabel.setText(currentPageNumber + "/"
							+ numberOfPages);
				}
				setDefaultStatusText();
			}
		});
		btnPrevious.setText("Previous");

		pageCountLabel = new Text(composite_1, SWT.CENTER);
		pageCountLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		pageCountLabel.setText("0/0");
		pageCountLabel.setEnabled(false);
		Button btnNext = new Button(composite_1, SWT.NONE);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (fileName == null) {
					return;
				}

				currentPageNumber++;
				if (currentPageNumber <= numberOfPages) {
					statusLineManager.setMessage("Loading page.....");
					offset = offset + PAGE_LIMIT;
					populateDataList();
					tableViewer.refresh();
					pageCountLabel.setText(currentPageNumber + "/"
							+ numberOfPages);
				} else {
					currentPageNumber = numberOfPages;
				}
				setDefaultStatusText();
			}
		});
		btnNext.setText("Next");

		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayout(new GridLayout(3, false));
		composite_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false, 1, 1));

		Label lblGoToPage = new Label(composite_2, SWT.NONE);
		lblGoToPage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblGoToPage.setText("Go to page: ");

		text = new Text(composite_2, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1,
				1));

		Button btnGo = new Button(composite_2, SWT.NONE);
		btnGo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (fileName == null) {
					return;
				}

				if (isInteger(text.getText())) {
					long pageNumber = Integer.parseInt(text.getText());
					long tempoffset = pageNumber * PAGE_LIMIT;
					tempoffset++;

					if (pageNumber > numberOfPages) {
						offset = (recordCount - PAGE_LIMIT) + 1;
						pageNumber = numberOfPages;
						statusLineManager.setMessage("Loading page.....");
					} else if (pageNumber < 1) {
						offset = 1;
					} else {
						offset = tempoffset - PAGE_LIMIT;
						statusLineManager
								.setMessage("Page number is greater than number of pages. Loading last page.....");
					}

					populateDataList();
					tableViewer.refresh();

					currentPageNumber = pageNumber;
					System.out.println(currentPageNumber + "/" + numberOfPages);
					pageCountLabel.setText(currentPageNumber + "/"
							+ numberOfPages);
					setDefaultStatusText();
				} else {
					MessageBox messageBox = new MessageBox(Display.getCurrent()
							.getActiveShell());
					messageBox.setMessage("invalid input field");
					messageBox.setText("Error");
					messageBox.open();
				}

			}
		});
		btnGo.setText("Go");
		// setDefaultStatusText();
		statusLineManager.setMessage("No file to view");
	}

	private void setDefaultStatusText() {
		long lastRecord = (offset - 1) + PAGE_LIMIT;
		statusLineManager.setMessage("Number of records in file: "
				+ recordCount + "   |   Showing records from " + offset
				+ " to " + lastRecord);
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	/**
	 * Create the menu manager.
	 * 
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {

		MenuManager menuManager = new MenuManager("menu");
		menuManager.setVisible(true);

		createFileMenu(menuManager);
		createEditMenu(menuManager);
		createViewMenu(menuManager);

		return menuManager;

	}

	public void switchToFormattedTextView() {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject("{\"Data\" : " + dataList.toString()
					+ "}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		stackLayout.topControl = formattedTextViewComposite;
		try {
			styledText.setText(jsonObject.toString(4));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stackLayoutComposite.layout();
	}

	public void switchToVirticalGridView() {
		stackLayout.topControl = virticalGridViewControl;
		stackLayoutComposite.layout();
	}

	private void createViewMenu(MenuManager menuManager) {
		MenuManager viewMenu = createMenu(menuManager, "View");
		viewMenu.add(new GridViewAction("Grid View", this));
		viewMenu.add(new HorizontalViewAction("Horizontal View"));
		viewMenu.add(new UnformattedViewAction("Formatted View", this));
		viewMenu.add(new Separator());
		viewMenu.add(new ReloadAction("Reload", this));
		viewMenu.add(new PreferencesAction("Preferences"));
	}

	private void createEditMenu(MenuManager menuManager) {
		MenuManager editMenu = createMenu(menuManager, "Edit");
		editMenu.add(new SelectAllAction("Select All"));
		editMenu.add(new CopyAction("Copy"));
		editMenu.add(new FilterAction("Find"));
	}

	private void createFileMenu(MenuManager menuManager) {
		MenuManager fileMenu = createMenu(menuManager, "File");
		menuManager.add(fileMenu);
		fileMenu.setVisible(true);

		fileMenu.add(new ExportAction("Export"));
		fileMenu.add(new FilterAction("Filter"));
		fileMenu.add(new GoAction("Go"));
		fileMenu.add(new StopAction("Stop"));
		fileMenu.add(new ClearAction("Clear"));
		fileMenu.add(new CloseAction("Close"));
	}

	private MenuManager createMenu(MenuManager menuManager, String menuName) {
		MenuManager menu = new MenuManager(menuName);
		menuManager.add(menu);
		menuManager.setVisible(true);
		return menu;
	}

	/**
	 * Create the coolbar manager.
	 * 
	 * @return the coolbar manager
	 */
	@Override
	protected CoolBarManager createCoolBarManager(int style) {
		/*
		 * CoolBarManager coolBarManager = new CoolBarManager(style);
		 * 
		 * ToolBarManager toolBarManager = new ToolBarManager();
		 * coolBarManager.add(toolBarManager); return coolBarManager;
		 */
		CoolBarManager coolBarManager = new CoolBarManager(style);

		ToolBarManager toolBarManager = new ToolBarManager();
		coolBarManager.add(toolBarManager);
		Action action;
		action = new ExportAction("Export");
		addtoolbarAction(
				toolBarManager,
				(XMLConfigUtil.CONFIG_FILES_PATH + "/icons/advicons/export.png"),
				action);
		action = new FindAction("Find", this);
		addtoolbarAction(
				toolBarManager,
				(XMLConfigUtil.CONFIG_FILES_PATH + "/icons/advicons/lookup.png"),
				action);
		action = new ReloadAction("Reload", this);
		addtoolbarAction(
				toolBarManager,
				(XMLConfigUtil.CONFIG_FILES_PATH + "/icons/advicons/refresh.png"),
				action);
		action = new FilterAction("Filter");
		addtoolbarAction(
				toolBarManager,
				(XMLConfigUtil.CONFIG_FILES_PATH + "/icons/advicons/filter.png"),
				action);
		Action dropDownAction = new Action("", SWT.DROP_DOWN) {
		};
		dropDownAction.setImageDescriptor(new ImageDescriptor() {

			@Override
			public ImageData getImageData() {
				return new ImageData(XMLConfigUtil.CONFIG_FILES_PATH
						+ "/icons/advicons/switchview.png");
			}
		});
		dropDownAction.setMenuCreator(new ViewDataGridMenuCreator(this));
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
	 * 
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		statusLineManager = new StatusLineManager();
		// statusLineManager.add(new Action("Hello"){});

		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			CSVDataViewer window = new CSVDataViewer();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * 
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
		return new Point(732, 494);
	}

	/**
	 * make wheel scrolling available by installing a wheel listener on this
	 * scrollable's parent and hierarchy of children
	 * 
	 * @param scrollable
	 *            the scrolledComposite to wheel-scroll
	 */
	public static void installMouseWheelScrollRecursively(
			final ScrolledComposite scrollable) {
		MouseWheelListener scroller = createMouseWheelScroller(scrollable);
		if (scrollable.getParent() != null)
			scrollable.getParent().addMouseWheelListener(scroller);
		installMouseWheelScrollRecursively(scroller, scrollable);
	}

	public static MouseWheelListener createMouseWheelScroller(
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
		return tableViewer;
	}
}

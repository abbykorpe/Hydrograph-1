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
import hydrograph.ui.dataviewer.adapters.CSVAdapter;
import hydrograph.ui.dataviewer.constants.ADVConstants;
import hydrograph.ui.dataviewer.datastructures.ColumnData;
import hydrograph.ui.dataviewer.datastructures.RowData;
import hydrograph.ui.dataviewer.datastructures.Schema;

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
	private Composite composite_1;
	private Text text;
	private Text text_1;
	private Composite composite_4;
	private CTabFolder tabFolder;
	
	private StatusLineManager statusLineManager;
	
	
	private CSVAdapter csvAdapter;
	//private List<Schema> tableSchema = new LinkedList<>();
		
	private List<Control> windowControls;
	
	private List<String> columnList;
	
	private static List<RowData> gridViewData;
	private static List<RowData> formattedViewData;
	private static List<RowData> unformattedViewData;
	
	private StyledText unformattedViewDataUIContainer;
	private StyledText formattedViewDataUIContainer;
	private TableViewer horizontalViewUIContainer;
	private TableViewer gridViewUIContainer;
	
	private static String GRID_VIEW_NAME="GRID_VIEW";
	private static String HORIZONTAL_VIEW_NAME="HORIZONTAL_VIEW";
	private static String FORMATTED_VIEW_NAME="FORMATTED_VIEW";
	private static String UNFORMATTED_VIEW_NAME="UNFORMATTED_VIEW";
	
	
	
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
	}

	
	/*private void populateSchemaList() {
		tableSchema.add(new Schema("f_string","java.lang.String", null));
		tableSchema.add(new Schema("f_string1","java.lang.String", null));
		tableSchema.add(new Schema("f_int","java.lang.Integer", null));
		tableSchema.add(new Schema("f_long","java.lang.Long", null));
		tableSchema.add(new Schema("f_double1","java.lang.Double", null));
		tableSchema.add(new Schema("f_double2","java.lang.Double", null));
		tableSchema.add(new Schema("f_float1","java.lang.Float", null));
		tableSchema.add(new Schema("f_float2","java.lang.Float", null));
		tableSchema.add(new Schema("f_short","java.lang.Short", null));
		tableSchema.add(new Schema("f_date1","java.util.Date", "YYYY-MM-DD"));
		tableSchema.add(new Schema("f_date2","java.util.Date", "DDMMYYYY"));
		tableSchema.add(new Schema("f_date3","java.util.Date", "DD.MMM.YYYY"));
		tableSchema.add(new Schema("f_date4","java.util.Date", "yyyy-MM-dd HH:mm:ss"));
		tableSchema.add(new Schema("f_date5","java.util.Date", "yyyy-MM-dd hh:mm:ss"));
		tableSchema.add(new Schema("f_date6","java.util.Date", "ddMMyy"));
		tableSchema.add(new Schema("f_bigDecimal","java.math.BigDecimal", null));
		tableSchema.add(new Schema("f_string3","java.lang.String", null));
		tableSchema.add(new Schema("f_string4","java.lang.String", null));
		tableSchema.add(new Schema("f_int1","java.lang.Integer", null));
		tableSchema.add(new Schema("f_long3","java.lang.Long", null));
		tableSchema.add(new Schema("f_double3","java.lang.Double", null));
		tableSchema.add(new Schema("f_double4","java.lang.Double", null));
		tableSchema.add(new Schema("f_float3","java.lang.Float", null));
		tableSchema.add(new Schema("f_float4","java.lang.Float", null));
		tableSchema.add(new Schema("f_short1","java.lang.Short", null));
		tableSchema.add(new Schema("f_date7","java.util.Date", "YYYY-MM-DD"));
		tableSchema.add(new Schema("f_date21","java.util.Date", "DDMMYYYY"));
		tableSchema.add(new Schema("f_date32","java.util.Date", "DD.MMM.YYYY"));
		tableSchema.add(new Schema("f_date41","java.util.Date", "yyyy-MM-dd HH:mm:ss"));
		tableSchema.add(new Schema("f_date51","java.util.Date", "yyyy-MM-dd hh:mm:ss"));
		tableSchema.add(new Schema("f_date61","java.util.Date", "ddMMyy"));
		tableSchema.add(new Schema("f_bigDecimal1","java.math.BigDecimal", null));
	}*/
	
	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		//populateSchemaList();
		csvAdapter = new CSVAdapter("C:\\Users\\shrirangk\\Desktop\\DataViewerPOC", "Generated_Records", 200, 0,this);
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		{
			tabFolder = new CTabFolder(container, SWT.BORDER);
			tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			tabFolder.setSelectionBackground(new Color(null, 14,76,145));
			tabFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
			tabFolder.setSelectionForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			{
				createGridViewTabItem();
			}
			{
				createHorizantalViewTabItem();
			}
			{
				createFormatedViewTabItem();
			}
			{
				createUnformattedViewTabItem();
			}
			
			tabFolder.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					updateView();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// DO Nothing
					
				}
			});
		}
		{
			createPaginationPanel(container);
		}
		
		setDefaultStatusMessage();
		return container;
	}


	private void updateView(){
		CTabItem tabItem = tabFolder.getSelection();
		if(tabItem.getData("VIEW_NAME").equals(GRID_VIEW_NAME)){
			gridViewUIContainer.refresh();
		}else if(tabItem.getData("VIEW_NAME").equals(HORIZONTAL_VIEW_NAME)){
			
		}else if(tabItem.getData("VIEW_NAME").equals(FORMATTED_VIEW_NAME)){
			updateFormattedView();
		}else if(tabItem.getData("VIEW_NAME").equals(UNFORMATTED_VIEW_NAME)){
			updateUnformattedView();
		}
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
		{
			createPageSwitchPanel(composite_2);
		}
		{
			createPageJumpPanel(composite_2);
		}
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
		final Button button = new Button(composite_3, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				
				setProgressStatusMessage("Please wait, fetching page " + text_1.getText());
				setWindowControlsEnabled(false);
				
				final Long  pageNumberToJump = Long.valueOf(text_1.getText());
				Job job = new Job("JumpToPage") {
					  @Override
					  protected IStatus run(IProgressMonitor monitor) {
						  final int retCode = csvAdapter.jumpToPage(pageNumberToJump);
						  updateDataViewLists();
						  
						  Display.getDefault().asyncExec(new Runnable() {
					      @Override
					      public void run() {
					    	  //gridViewTableViewer.refresh();
					    	  updateView();
						       
								setWindowControlsEnabled(true);
					    	  if(retCode == ADVConstants.EOF){
									appendStatusMessage("End of file reached");
								}else if(retCode == ADVConstants.ERROR){
									statusLineManager.setErrorMessage("Error while featching record");
								}else{
									setDefaultStatusMessage();
								}
					      }
					    });
					    return Status.OK_STATUS;
					  }

					
					};
					
					job.schedule();
			}
		});
		button.setText("Go");
		windowControls.add(button);
	}

	private void updateDataViewLists() {
		gridViewData.clear();
		gridViewData.addAll(csvAdapter.getTableData());
		formattedViewData = csvAdapter.getTableData();
		unformattedViewData = csvAdapter.getTableData();
	}

	private void createJumpPageTextBox(Composite composite_3) {
		text_1 = new Text(composite_3, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_1.addVerifyListener(new VerifyListener() {  
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
		windowControls.add(text_1);
	}


	private void createJumpPageLabel(Composite composite_3) {
		Label label = new Label(composite_3, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Jump to Page: ");
	}


	private void createPageSwitchPanel(Composite composite_2) {
		Composite composite_3 = new Composite(composite_2, SWT.NONE);
		composite_3.setLayout(new GridLayout(3, false));
		{
			createPreviousPageButton(composite_3);
		}
		{
			createPageNumberDisplay(composite_3);
			
		}
		{
			createNextPageButton(composite_3);
		}
	}


	private void createNextPageButton(Composite composite_3) {
		Button button = new Button(composite_3, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				setProgressStatusMessage("Please wait, fetching next page records");
				setWindowControlsEnabled(false);
				
				Job job = new Job("NextPage") {
					  @Override
					  protected IStatus run(IProgressMonitor monitor) {
						  final int retCode = csvAdapter.next();
						  
						  updateDataViewLists();
						  
						  Display.getDefault().asyncExec(new Runnable() {
					      @Override
					      public void run() {
					    	 /* gridViewTableViewer.refresh();
					    	  updateUnformattedView();*/
					    	  updateView();
					    	  setWindowControlsEnabled(true);
								if(retCode == ADVConstants.EOF){
									appendStatusMessage("End of file reached");
								}else if(retCode == ADVConstants.ERROR){
									statusLineManager.setErrorMessage("Error while featching record");
								}else{
									setDefaultStatusMessage();
								}
					      }
					    });
					    return Status.OK_STATUS;
					  }
					};
					
					job.schedule();
			}
		});
		button.setText("Next");
		windowControls.add(button);
	}


	private void createPageNumberDisplay(Composite composite_3) {
		text = new Text(composite_3, SWT.BORDER | SWT.CENTER);
		text.setEnabled(false);
		text.setEditable(false);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 178;
		text.setLayoutData(gd_text);
	}

	private void updatePageNumberDisplayPanel(){
		text.setText(csvAdapter.getPageStatusNumber());
	}

	private void createPreviousPageButton(Composite composite_3) {
		Button button = new Button(composite_3, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				setProgressStatusMessage("Please wait, fetching next page records");
				setWindowControlsEnabled(false);
				
				Job job = new Job("PreviousPage") {
					  @Override
					  protected IStatus run(IProgressMonitor monitor) {
						  final int retCode = csvAdapter.previous();
							
						  updateDataViewLists();
						  
						  Display.getDefault().asyncExec(new Runnable() {
					      @Override
					      public void run() {
					    	  //gridViewTableViewer.refresh();
					    	  updateView();
					    	  setWindowControlsEnabled(true);
					    	  if(retCode == ADVConstants.BOF){
									appendStatusMessage("Begining of file reached");
								}else if(retCode == ADVConstants.ERROR){
									statusLineManager.setErrorMessage("Error while featching record");
								}else{
									setDefaultStatusMessage();
								}
					      }
					    });
					    return Status.OK_STATUS;
					  }
					};
					
					job.schedule();
				
			}
		});
		button.setText("Previous");
		windowControls.add(button);
	}
	
	
	public void setWindowControlsEnabled(boolean enabled){
		for(Control control: windowControls){
			control.setEnabled(enabled);
		}
	}

	public void setDefaultStatusMessage() {
		StringBuilder stringBuilder = new StringBuilder();
		
		statusLineManager.setErrorMessage(null);
		
		if(csvAdapter.getRowCount()!=null){
			stringBuilder.append("Record Count: " + csvAdapter.getRowCount() + " | ");
		}else{
			stringBuilder.append("Counting number of records... | ");
		}
		
		stringBuilder.append("Showing records from " + csvAdapter.getOFFSET() + " to " + (csvAdapter.getOFFSET() + csvAdapter.getPAGE_SIZE()) + " | ");
		statusLineManager.setMessage(stringBuilder.toString().substring(0,stringBuilder.length() -2));
		
		updatePageNumberDisplayPanel();
	}

	
	public void setProgressStatusMessage(String message){
		statusLineManager.setMessage(message);
	}
	
	private void appendStatusMessage(String Message) {
		StringBuilder stringBuilder = new StringBuilder();
		
		if(csvAdapter.getRowCount()!=null){
			stringBuilder.append("Record Count: " + csvAdapter.getRowCount() + " | ");
		}else{
			stringBuilder.append("Counting number of records... | ");
		}
		
		stringBuilder.append("Showing records from " + csvAdapter.getOFFSET() + " to " + (csvAdapter.getOFFSET() + csvAdapter.getPAGE_SIZE()) + " | ");
		
		stringBuilder.append(Message + " | ");
		
		statusLineManager.setMessage(stringBuilder.toString().substring(0,stringBuilder.length() -2));
	}

	
	private void updateUnformattedView(){
		unformattedViewDataUIContainer.setText("");
		StringBuilder stringBuilder = new StringBuilder();
		for(RowData rowData : unformattedViewData){
			//unformattedViewDataUIContainer.append(rowData);
			String row="";
			for(ColumnData columnData : rowData.getColumns()){
				row = row + columnData.getValue() + ",";
			}
			stringBuilder.append(row.substring(0, row.length() -1) + "\n");
			
			
		}
		unformattedViewDataUIContainer.setText(stringBuilder.toString());
	}
	
	private void createUnformattedViewTabItem() {
		CTabItem tbtmUnformattedView = new CTabItem(tabFolder, SWT.CLOSE);
		tbtmUnformattedView.setData("VIEW_NAME", UNFORMATTED_VIEW_NAME);
		tbtmUnformattedView.setText("Unformatted View");
		{
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmUnformattedView.setControl(composite);
			composite.setLayout(new GridLayout(1, false));
			{
				unformattedViewDataUIContainer = new StyledText(composite, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
				unformattedViewDataUIContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			}
		}
	}
	
	private int getMaxLengthColumn(){
		
		int lenght=0;
		
		for(String columnName: csvAdapter.getColumnList()){
			if(columnName.length() > lenght){
				lenght = columnName.length();
			}
		}
		
		return lenght;
	}
	
	
	private void updateFormattedView(){
		formattedViewDataUIContainer.setText("");
		StringBuilder stringBuilder = new StringBuilder();
		int rowNumber=0;
		
		int maxLenghtColumn = getMaxLengthColumn();
		
		maxLenghtColumn+=5;
		String format="\t\t%-" + maxLenghtColumn + "s: %s\n";   
		
		for(RowData rowData : formattedViewData){
			stringBuilder.append("Record: " + rowNumber + "\n\n");
			
			stringBuilder.append("{\n");
			int columnIndex=0;
			for(String columnName: csvAdapter.getColumnList()){
				ColumnData columnData = rowData.getColumns().get(columnIndex);
				String tempString = String.format(format, columnName,columnData.getValue());
				stringBuilder.append(tempString);
				columnIndex++;
			}
			stringBuilder.append("}\n");
			stringBuilder.append("----------------------------------\n");
		}
		formattedViewDataUIContainer.setText(stringBuilder.toString());
	}
	private void createFormatedViewTabItem() {
		CTabItem tbtmFormattedView = new CTabItem(tabFolder, SWT.CLOSE);
		tbtmFormattedView.setData("VIEW_NAME", FORMATTED_VIEW_NAME);
		tbtmFormattedView.setText("Formatted view");
		{
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmFormattedView.setControl(composite);
			composite.setLayout(new GridLayout(1, false));
			{
				formattedViewDataUIContainer = new StyledText(composite, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
				formattedViewDataUIContainer.setFont(SWTResourceManager.getFont("Courier New", 9, SWT.NORMAL));
				formattedViewDataUIContainer.setEditable(false);
				formattedViewDataUIContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			}
		}
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
					composite_4 = new Composite(stackLayoutComposite, SWT.NONE);
					GridLayout gl_composite_4 = new GridLayout(1, false);
					gl_composite_4.verticalSpacing = 0;
					gl_composite_4.marginWidth = 0;
					gl_composite_4.marginHeight = 0;
					gl_composite_4.horizontalSpacing = 0;
					composite_4.setLayout(gl_composite_4);
					{
						horizontalViewUIContainer = new TableViewer(composite_4, SWT.BORDER | SWT.FULL_SELECTION);
						Table table_1 = horizontalViewUIContainer.getTable();
						table_1.setLinesVisible(true);
						table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					}
					stackLayout.topControl = composite_1;
				}
				//scrolledComposite.setContent(composite_4);
				//scrolledComposite.setMinSize(composite_4.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				
				scrolledComposite.getShowFocusedControl();
				scrolledComposite.setShowFocusedControl(true);
				
				
				scrolledComposite.setContent(stackLayoutComposite);
				scrolledComposite.setMinSize(stackLayoutComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				
				installMouseWheelScrollRecursively(scrolledComposite);
				
				createHorizontalViewTableColumns(horizontalViewUIContainer);
				setTableLayoutToMappingTable(horizontalViewUIContainer);
				horizontalViewUIContainer.setContentProvider(new ArrayContentProvider());
				
				updateDataViewLists();
				horizontalViewUIContainer.setInput(gridViewData);
				horizontalViewUIContainer.refresh();
				
				for (int i = 0, n = horizontalViewUIContainer.getTable().getColumnCount(); i < n; i++)
					horizontalViewUIContainer.getTable().getColumn(i).pack();
				
				horizontalViewUIContainer.refresh();
			}
		}
	}

	private void createHorizontalViewTableColumns(
			TableViewer horizontalViewUIContainer2) {
		
		// TODO Auto-generated method stub
		
	}


	private void createGridViewTabItem() {
		CTabItem tbtmGridview = new CTabItem(tabFolder, SWT.NONE);
		tbtmGridview.setData("VIEW_NAME", GRID_VIEW_NAME);
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
					composite_1 = new Composite(stackLayoutComposite, SWT.NONE);
					GridLayout gl_composite_1 = new GridLayout(1, false);
					gl_composite_1.verticalSpacing = 0;
					gl_composite_1.marginWidth = 0;
					gl_composite_1.marginHeight = 0;
					gl_composite_1.horizontalSpacing = 0;
					composite_1.setLayout(gl_composite_1);
					composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
							1));
					{
						gridViewUIContainer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
						Table table = gridViewUIContainer.getTable();
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
				
				createGridViewTableColumns(gridViewUIContainer);
				setTableLayoutToMappingTable(gridViewUIContainer);
				gridViewUIContainer.setContentProvider(new ArrayContentProvider());
				
				updateDataViewLists();
				gridViewUIContainer.setInput(gridViewData);
				gridViewUIContainer.refresh();
				
				for (int i = 0, n = gridViewUIContainer.getTable().getColumnCount(); i < n; i++)
					gridViewUIContainer.getTable().getColumn(i).pack();
				
				gridViewUIContainer.refresh();
			}
		}
	}
	
	private void setTableLayoutToMappingTable(TableViewer tableViewer) {
		Table table =tableViewer.getTable();
		TableColumnLayout layout = new TableColumnLayout();
		tableViewer.getControl().getParent().setLayout(layout);

		for (int columnIndex = 0, n = table.getColumnCount(); columnIndex < n; columnIndex++) {
			table.getColumn(columnIndex).pack();
		}

		for (int i = 0; i < table.getColumnCount(); i++) {
			layout.setColumnData(table.getColumn(i), new ColumnWeightData(1));
		}
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

		fileMenu.add(new ExportAction("Export"));
		fileMenu.add(new FilterAction("Filter"));
		fileMenu.add(new GoAction("Go"));
		fileMenu.add(new StopAction("Stop"));
		fileMenu.add(new ClearAction("Clear"));
		fileMenu.add(new CloseAction("Close"));
	}
	
	private void createEditMenu(MenuManager menuManager) {
		MenuManager editMenu = createMenu(menuManager, "Edit");
		editMenu.add(new SelectAllAction("Select All"));
		editMenu.add(new CopyAction("Copy"));
		editMenu.add(new FilterAction("Find"));
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
	

	/**
	 * Create the coolbar manager.
	 * @return the coolbar manager
	 */
	@Override
	protected CoolBarManager createCoolBarManager(int style) {
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
}

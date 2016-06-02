package hydrograph.ui.dataviewer.adapters;

import hydrograph.ui.dataviewer.DebugDataViewer;
import hydrograph.ui.dataviewer.constants.ADVConstants;
import hydrograph.ui.dataviewer.datastructures.ColumnData;
import hydrograph.ui.dataviewer.datastructures.RowData;
import hydrograph.ui.dataviewer.datastructures.Schema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

public class CSVAdapter {

	private static List<RowData> tableData;

	private String databaseName;
	private String tableName;
	private List<String> columnList;
	private int columnCount;
	private Long rowCount=null;
	//private List<Schema> tableSchema = new LinkedList<>();
	
	private boolean rowCountFetchInProgress;
	
	private Connection connection;
	private Statement statement;
	private DebugDataViewer debugDataViewer;
	
	
	volatile private int PAGE_SIZE;
	volatile private long OFFSET;
	
	/*public CSVAdapter(String databaseName, String tableName,List<Schema> tableSchema,int PAGE_SIZE,long INITIAL_OFFSET, DebugDataViewer debugDataViewer) {
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.tableSchema = tableSchema;
		tableData = new LinkedList<>();
		columnList = new LinkedList<>();
		columnCount = 0;
		this.PAGE_SIZE = PAGE_SIZE;
		this.OFFSET = INITIAL_OFFSET;
		this.debugDataViewer = debugDataViewer;
		initializeAdapter();
	}*/
	
	public CSVAdapter(String databaseName, String tableName,int PAGE_SIZE,long INITIAL_OFFSET, DebugDataViewer debugDataViewer) {
		this.databaseName = databaseName;
		this.tableName = tableName;
		tableData = new LinkedList<>();
		columnList = new LinkedList<>();
		columnCount = 0;
		this.PAGE_SIZE = PAGE_SIZE;
		this.OFFSET = INITIAL_OFFSET;
		this.debugDataViewer = debugDataViewer;
		initializeAdapter();
	}
	
	public int getPAGE_SIZE() {
		return PAGE_SIZE;
	}

	public long getOFFSET() {
		return OFFSET;
	}

	private void initializeAdapter() {
		try {
			Class.forName("org.relique.jdbc.csv.CsvDriver");
			connection = DriverManager.getConnection("jdbc:relique:csv:"
					+ databaseName);
			statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery("SELECT * FROM "
					+ tableName + " LIMIT 0");

			initializeColumnCount(resultSet);
			initializeColumnList(resultSet);
			initializeTableData();
						
			//initializeRowCount();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private void initializeColumnList(ResultSet resultSet) throws SQLException {
		for (int index = 1; index <= columnCount; index++) {
			columnList
					.add(resultSet.getMetaData().getColumnName(index ));
		}
	}

	private void initializeColumnCount(ResultSet resultSet) throws SQLException {
		columnCount = resultSet.getMetaData().getColumnCount();
	}

	public List<String> getColumnList() {
		return columnList;
	}

	public int getColumnCount() {
		return columnCount;

	}

	
	public String getPageStatusNumber(){
		if (getRowCount() !=null ) {
			return String.valueOf((OFFSET+PAGE_SIZE)/PAGE_SIZE) + "/" + String.valueOf(getRowCount()/PAGE_SIZE);
		}else{
			return String.valueOf((OFFSET+PAGE_SIZE)/PAGE_SIZE);
		}
	}
	
	
	public Long getRowCount() {
		
		if(rowCount==null && rowCountFetchInProgress == false){
			rowCountFetchInProgress=true;
			new Thread() {
	            public void run() {
	            	try {
	            		ResultSet rowCountResultSet;
	    				rowCountResultSet = statement.executeQuery("SELECT COUNT(1) FROM "
	    						+ tableName);
	    				rowCountResultSet.next();
	    				rowCount = rowCountResultSet.getLong(1);
	    				
	    				
	    				//Do not Remove below code- 
	    				//Reason: We can not draw outside Display Thread. But in this code I am drawing.
	    				//Reference: https://wiki.eclipse.org/FAQ_Why_do_I_get_an_invalid_thread_access_exception%3F
	    				Display.getDefault().asyncExec(new Runnable() {
	    				    public void run() {
	    				    	debugDataViewer.setDefaultStatusMessage();
	    				    }
	    				});
	    				
	    			} catch (SQLException e) {
	    				e.printStackTrace();
	    			}
	            }
	        }.start();
			
		}
		
		return rowCount;
		
	}

	public List<RowData> getTableData() {
		return tableData;
	}

	public List<RowData> getHorizantalViewData() {

		return null;
	}

	public int next() {
		
		OFFSET = OFFSET+PAGE_SIZE;
		
		if(rowCount!=null){
			if(OFFSET>rowCount){
				OFFSET = rowCount - PAGE_SIZE;
				return ADVConstants.EOF;
			}
		}
		
		tableData.clear();
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM " + tableName
					+ " LIMIT " + PAGE_SIZE + " OFFSET " + OFFSET);
			int rowNumber=1;
			while (results.next()) {
				List<ColumnData> row = new LinkedList<>();
				for (int index = 0; index < columnCount; index++) {
					/*row.add(new ColumnData(results.getString(index + 1),
							tableSchema.get(index)));*/
					row.add(new ColumnData(results.getString(index + 1)));
				}
				
				tableData.add(new RowData(row,rowNumber));
				rowNumber++;
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			return ADVConstants.ERROR;
		}
		
		return 0;
	}
	
	
	public int previous() {
		OFFSET = OFFSET-PAGE_SIZE;
		
		if(OFFSET<0){
			OFFSET=0;
			return ADVConstants.BOF;
		}
		tableData.clear();
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM " + tableName
					+ " LIMIT " + PAGE_SIZE + " OFFSET " + OFFSET);
			int rowNumber=1;
			while (results.next()) {
				List<ColumnData> row = new LinkedList<>();
				for (int index = 0; index < columnCount; index++) {
					/*row.add(new ColumnData(results.getString(index + 1),
							tableSchema.get(index)));*/
					row.add(new ColumnData(results.getString(index + 1)));
				}
				tableData.add(new RowData(row,rowNumber));
				rowNumber++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ADVConstants.ERROR;
		}
		return 0;
	}
	
	private void initializeTableData() {	
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM " + tableName
					+ " LIMIT " + PAGE_SIZE + " OFFSET " + OFFSET);
			int rowIndex=1;
			while (results.next()) {
				List<ColumnData> row = new LinkedList<>();
				for (int index = 1; index <= columnCount; index++) {
					/*row.add(new ColumnData(results.getString(index),
							tableSchema.get(index-1)));*/
					row.add(new ColumnData(results.getString(index)));
				}
				tableData.add(new RowData(row,rowIndex));
				rowIndex++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	private long getCurrentPageNumber(){
		return (OFFSET+PAGE_SIZE)/PAGE_SIZE;
	}
	
	
	
	public int jumpToPage(long pageNumber) {
		
		Long numberOfRecords = getRowCount();
		if (numberOfRecords!=null) {
			long numberOfPages = numberOfRecords/PAGE_SIZE;
			if((getCurrentPageNumber() ==numberOfPages) && (pageNumber>=numberOfPages)){
				return ADVConstants.EOF;
			}
		}
		
		long tempOffset=0;
		tempOffset = OFFSET;
		OFFSET = (pageNumber * PAGE_SIZE) - PAGE_SIZE;
		
		
		if (numberOfRecords!=null) {
			if(OFFSET>=rowCount){
				OFFSET = rowCount - PAGE_SIZE;
			}
		}
		
		List tempTableData  = new LinkedList<>();		
		tempTableData.addAll(tableData);		
		tableData.clear();
		
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM " + tableName
					+ " LIMIT " + PAGE_SIZE + " OFFSET " + OFFSET);
			int rowIndex=1;
			
			while (results.next()) {
				List<ColumnData> row = new LinkedList<>();
				for (int index = 0; index < columnCount; index++) {
					/*row.add(new ColumnData(results.getString(index + 1),
							tableSchema.get(index)));*/
					row.add(new ColumnData(results.getString(index + 1)));
				}
				tableData.add(new RowData(row,rowIndex));
				rowIndex++;
			}
			
			if (tableData.isEmpty()) {
				OFFSET = tempOffset;
				tableData.addAll(tempTableData);
				return ADVConstants.EOF;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return ADVConstants.ERROR;
		}
		return 0;
	}

	private int getNumberOfRecordsInResultSet(ResultSet results)
			throws SQLException {
		int numberOfRecords= 0;
		if (results != null)   
		{   
			results.last();  
			numberOfRecords = results.getRow();
			results.beforeFirst(); 
		}
		return numberOfRecords;
	}
	
	public void dispose() {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

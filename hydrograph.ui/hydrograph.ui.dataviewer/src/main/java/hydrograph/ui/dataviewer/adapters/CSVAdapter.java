package hydrograph.ui.dataviewer.adapters;

import hydrograph.ui.dataviewer.DebugDataViewer;
import hydrograph.ui.dataviewer.constants.ADVConstants;
import hydrograph.ui.dataviewer.datastructures.ColumnData;
import hydrograph.ui.dataviewer.datastructures.RowData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class CSVAdapter {

	private static List<RowData> tableData;

	private String databaseName;
	private String tableName;
	private List<String> columnList;
	private int columnCount;
	volatile private Long rowCount=null;
	
	private boolean rowCountFetchInProgress;
	
	private Connection connection;
	private Statement statement;
	private DebugDataViewer debugDataViewer;
	
	
	volatile private int PAGE_SIZE;
	volatile private long OFFSET;

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
	
	private void initializeTableData() {	
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM " + tableName
					+ " LIMIT " + PAGE_SIZE + " OFFSET " + OFFSET);
			int rowIndex=1;
			while (results.next()) {
				List<ColumnData> row = new LinkedList<>();
				for (int index = 1; index <= columnCount; index++) {
					row.add(new ColumnData(results.getString(index)));
				}
				tableData.add(new RowData(row,rowIndex));
				rowIndex++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getPageStatusNumber(){
		if (getRowCount() !=null ) {
			if((getRowCount()%PAGE_SIZE)!=0)
				return String.valueOf((OFFSET+PAGE_SIZE)/PAGE_SIZE) + "/" + String.valueOf((getRowCount()/PAGE_SIZE)+1);
			else
				return String.valueOf((OFFSET+PAGE_SIZE)/PAGE_SIZE) + "/" + String.valueOf((getRowCount()/PAGE_SIZE));
		}else{
			return String.valueOf((OFFSET+PAGE_SIZE)/PAGE_SIZE);
		}
	}
		
	public Long getRowCount() {
		
		/*if(rowCount==null && rowCountFetchInProgress == false){
			rowCountFetchInProgress=true;
			new Thread() {
	            public void run() {
	            	try {
	            		ResultSet rowCountResultSet;
	    				rowCountResultSet = statement.executeQuery("SELECT COUNT(1) FROM "
	    						+ tableName);
	    				rowCountResultSet.next();
	    				rowCount = rowCountResultSet.getLong(1);
	    				
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
			
		}*/
		
		return rowCount;
		
	}
	
	public void fetchRowCount() {
		try {
    		ResultSet rowCountResultSet;
			rowCountResultSet = statement.executeQuery("SELECT COUNT(1) FROM "
					+ tableName);
			rowCountResultSet.next();
			rowCount = rowCountResultSet.getLong(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private long getCurrentPageNumber(){
		return (OFFSET+PAGE_SIZE)/PAGE_SIZE;
	}


	public List<RowData> getTableData() {
		return tableData;
	}
	
	public List<String> getColumnList() {
		return columnList;
	}

	public int getColumnCount() {
		return columnCount;

	}
	
	public int getPAGE_SIZE() {
		return PAGE_SIZE;
	}

	public long getOFFSET() {
		return OFFSET;
	}
		
	public int next() {
		
		OFFSET = OFFSET+PAGE_SIZE;
		
		if(rowCount!=null){
			if(OFFSET>=rowCount){
				OFFSET = rowCount - PAGE_SIZE;
				if(OFFSET<0){
					OFFSET = 0;
				}
				return ADVConstants.EOF;
			}
		}
		
		
		List<RowData> tempTableData = new LinkedList<>();
		
		
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM " + tableName
					+ " LIMIT " + PAGE_SIZE + " OFFSET " + OFFSET);
			int rowNumber=1;
			while (results.next()) {
				List<ColumnData> row = new LinkedList<>();
				for (int index = 0; index < columnCount; index++) {
					row.add(new ColumnData(results.getString(index + 1)));
				}
				
				tempTableData.add(new RowData(row,rowNumber));
				rowNumber++;
			}		
			
			if(tempTableData.size()!=0){
				tableData.clear();
				tableData.addAll(tempTableData);
			}else{
				OFFSET = rowCount - PAGE_SIZE;
				return ADVConstants.EOF;
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
				if(OFFSET<0){
					OFFSET=0;
				}	
			}
		}
		
		List<RowData> tempTableData  = new LinkedList<>();		

		try {
			ResultSet results = statement.executeQuery("SELECT * FROM " + tableName
					+ " LIMIT " + PAGE_SIZE + " OFFSET " + OFFSET);
			int rowIndex=1;
			
			while (results.next()) {
				List<ColumnData> row = new LinkedList<>();
				for (int index = 0; index < columnCount; index++) {
					row.add(new ColumnData(results.getString(index + 1)));
				}
				tempTableData.add(new RowData(row,rowIndex));
				rowIndex++;
			}
			
			
			if(tempTableData.size()!=0){
				tableData.clear();
				tableData.addAll(tempTableData);				
			}else{
				OFFSET = tempOffset;
				return ADVConstants.EOF;
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return ADVConstants.ERROR;
		}
		return 0;
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

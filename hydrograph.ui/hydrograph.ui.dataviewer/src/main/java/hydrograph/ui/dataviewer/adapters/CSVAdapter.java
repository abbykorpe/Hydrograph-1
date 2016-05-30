package hydrograph.ui.dataviewer.adapters;

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

public class CSVAdapter {

	private static List<RowData> tableData;

	private String databaseName;
	private String tableName;
	private List<String> columnList;
	private int columnCount;
	private Long rowCount=null;
	private List<Schema> tableSchema = new LinkedList<>();
	
	private boolean rowCountFetchInProgress;
	
	private Connection connection;
	private Statement statement;
	
	
	private int PAGE_SIZE;
	private long OFFSET;
	
	public CSVAdapter(String databaseName, String tableName,List<Schema> tableSchema,int PAGE_SIZE,long INITIAL_OFFSET) {
		this.databaseName = databaseName;
		this.tableName = tableName;
		this.tableSchema = tableSchema;
		tableData = new LinkedList<>();
		columnList = new LinkedList<>();
		columnCount = 0;
		this.PAGE_SIZE = PAGE_SIZE;
		this.OFFSET = INITIAL_OFFSET;
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
			// TODO Auto-generated catch block
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

	public Long getRowCount() {
		
		if(rowCount==null && rowCountFetchInProgress == false){
			System.out.println("+++ Executing thread");
			rowCountFetchInProgress=true;
			new Thread() {
	            public void run() {
	            	try {
	            		ResultSet rowCountResultSet;
	    				rowCountResultSet = statement.executeQuery("SELECT COUNT(1) FROM "
	    						+ tableName);
	    				rowCountResultSet.next();
	    				rowCount = rowCountResultSet.getLong(1);
	    			} catch (SQLException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	            }
	        }.start();
			
		}
		
		return rowCount;
		
	}

	public List<RowData> getGridViewData() {
		return tableData;
	}

	public List<RowData> getHorizantalViewData() {

		return null;
	}

	public int next() {
		tableData.clear();
		
		OFFSET = OFFSET+PAGE_SIZE;
				
		/*if(OFFSET>numberOfRecords){
			return ADVConstants.EOF;
		}*/
		
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM " + tableName
					+ " LIMIT " + PAGE_SIZE + " OFFSET " + OFFSET);
			while (results.next()) {
				List<ColumnData> row = new LinkedList<>();
				for (int index = 0; index < columnCount; index++) {
					// row.add(new CellData("String",
					// results.getString(index+1)));
					row.add(new ColumnData(results.getString(index + 1),
							tableSchema.get(index)));
				}
				tableData.add(new RowData(row));
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ADVConstants.ERROR;
		}
		
		return 0;
	}
	
	
	public int previous() {
		tableData.clear();
		
		OFFSET = OFFSET+PAGE_SIZE;
		
		if(OFFSET<0){
			return ADVConstants.BOF;
		}
		
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM " + tableName
					+ " LIMIT " + PAGE_SIZE + " OFFSET " + OFFSET);
			while (results.next()) {
				List<ColumnData> row = new LinkedList<>();
				for (int index = 0; index < columnCount; index++) {
					// row.add(new CellData("String",
					// results.getString(index+1)));
					row.add(new ColumnData(results.getString(index + 1),
							tableSchema.get(index)));
				}
				tableData.add(new RowData(row));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ADVConstants.ERROR;
		}
		return 0;
	}
	
	private void initializeTableData() {	
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM " + tableName
					+ " LIMIT " + PAGE_SIZE + " OFFSET " + OFFSET);
			while (results.next()) {
				List<ColumnData> row = new LinkedList<>();
				for (int index = 1; index <= columnCount; index++) {
					// row.add(new CellData("String",
					// results.getString(index+1)));
					row.add(new ColumnData(results.getString(index),
							tableSchema.get(index-1)));
				}
				tableData.add(new RowData(row));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void jumpToPage(long pageNumber) {
		tableData.clear();
		
		OFFSET = 0;
		OFFSET = (pageNumber * PAGE_SIZE) - PAGE_SIZE;
		
		try {
			ResultSet results = statement.executeQuery("SELECT * FROM " + tableName
					+ " LIMIT " + PAGE_SIZE + " OFFSET " + OFFSET);
			int rowIndex=0;
			while (results.next()) {
				List<ColumnData> row = new LinkedList<>();
				for (int index = 0; index < columnCount; index++) {
					row.add(new ColumnData(results.getString(index + 1),
							tableSchema.get(index)));
				}
				tableData.add(new RowData(row,rowIndex));
				rowIndex++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void dispose() {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

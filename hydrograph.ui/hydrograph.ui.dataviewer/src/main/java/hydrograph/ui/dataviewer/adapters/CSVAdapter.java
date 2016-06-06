package hydrograph.ui.dataviewer.adapters;

import hydrograph.ui.dataviewer.DebugDataViewer;
import hydrograph.ui.dataviewer.constants.StatusConstants;
import hydrograph.ui.dataviewer.datastructures.StatusMessage;
import hydrograph.ui.dataviewer.datastructures.ColumnData;
import hydrograph.ui.dataviewer.datastructures.RowData;
import hydrograph.ui.logging.factory.LogFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;

public class CSVAdapter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(CSVAdapter.class);
	private static List<RowData> tableData;

	private String databaseName;
	private String tableName;

	private List<String> columnList;
	private int columnCount;

	volatile private Long rowCount = null;
	volatile private int PAGE_SIZE;
	volatile private long OFFSET;

	private Connection connection;
	private Statement statement;

	public CSVAdapter(String databaseName, String tableName, int PAGE_SIZE, long INITIAL_OFFSET, DebugDataViewer debugDataViewer)
			throws Exception {
		this.databaseName = databaseName;
		this.tableName = tableName;
		tableData = new LinkedList<>();
		columnList = new LinkedList<>();
		columnCount = 0;
		this.PAGE_SIZE = PAGE_SIZE;
		this.OFFSET = INITIAL_OFFSET;
		initializeAdapter();
	}

	private void initializeAdapter() throws Exception {
		ResultSet resultSet = null;
		try {
			Class.forName("org.relique.jdbc.csv.CsvDriver");
			connection = DriverManager.getConnection("jdbc:relique:csv:" + databaseName);
			statement = connection.createStatement();

			resultSet = statement.executeQuery("SELECT * FROM " + tableName + " LIMIT 0");

			initializeColumnCount(resultSet);
			initializeColumnList(resultSet);
			initializeTableData();
			resultSet.close();
		} catch (Exception e) {
			if (statement != null)
				statement.close();

			if (connection != null)
				connection.close();
			throw e;
		}

	}

	private void initializeColumnList(ResultSet resultSet) throws SQLException {
		for (int index = 1; index <= columnCount; index++) {
			columnList.add(resultSet.getMetaData().getColumnName(index));
		}
	}

	private void initializeColumnCount(ResultSet resultSet) throws SQLException {
		columnCount = resultSet.getMetaData().getColumnCount();
	}

	private void initializeTableData() throws Exception {

		ResultSet results = statement.executeQuery("SELECT * FROM " + tableName + " LIMIT " + PAGE_SIZE + " OFFSET " + OFFSET);
		int rowIndex = 1;
		while (results.next()) {
			List<ColumnData> row = new LinkedList<>();
			for (int index = 1; index <= columnCount; index++) {
				row.add(new ColumnData(results.getString(index)));
			}
			tableData.add(new RowData(row, rowIndex));
			rowIndex++;
		}

	}

	public String getPageStatusNumber() {
		if (getTotalNumberOfPages() != null) {
			return String.valueOf(getCurrentPageNumber()) + "/" + String.valueOf(getTotalNumberOfPages());
		} else {
			return String.valueOf(getCurrentPageNumber());
		}
	}

	public Long getTotalNumberOfPages() {
		if (getRowCount() != null) {
			if ((getRowCount() % PAGE_SIZE) != 0)
				return (getRowCount() / PAGE_SIZE) + 1;
			else
				return (getRowCount() / PAGE_SIZE);
		} else {
			return null;
		}
	}

	public Long getRowCount() {
		return rowCount;
	}

	public StatusMessage fetchRowCount() {
		try (ResultSet rowCountResultSet = statement.executeQuery("SELECT COUNT(1) FROM " + tableName)) {
			rowCountResultSet.next();
			rowCount = rowCountResultSet.getLong(1);

		} catch (SQLException e) {
			logger.debug("Invalid debug file, Unable to fetch row count", e);
			return new StatusMessage(StatusConstants.ERROR, "Invalid debug file, Unable to fetch row count");
		}
		return new StatusMessage(StatusConstants.SUCCESS);

	}

	public long getCurrentPageNumber() {
		return (OFFSET + PAGE_SIZE) / PAGE_SIZE;
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

	public StatusMessage next() {
		adjustOffsetForNext();

		if ((OFFSET + PAGE_SIZE) >= rowCount || OFFSET == 0) {
			return new StatusMessage(StatusConstants.EOF, "End of file reached");
		}

		try (ResultSet results = statement.executeQuery("SELECT * FROM " + tableName + " LIMIT " + PAGE_SIZE + " OFFSET "
				+ OFFSET)) {
			List<RowData> tempTableData = getRecords(results);
			if (tempTableData.size() != 0) {
				tableData.clear();
				tableData.addAll(tempTableData);
			} else {
				OFFSET = rowCount - PAGE_SIZE;
				return new StatusMessage(StatusConstants.EOF, "End of file reached");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new StatusMessage(StatusConstants.ERROR, "Error while featching record");
		}

		return new StatusMessage(StatusConstants.SUCCESS);
	}

	private void adjustOffsetForNext() {
		OFFSET = OFFSET + PAGE_SIZE;
		if (rowCount != null) {
			if (OFFSET >= rowCount) {
				OFFSET = rowCount - PAGE_SIZE;
				if (OFFSET < 0) {
					OFFSET = 0;
				}
			}
		}
	}

	public StatusMessage previous() {
		adjustOffsetForPrevious();
		if (OFFSET == 0) {
			return new StatusMessage(StatusConstants.BOF, "Begining of file reached");
		}
		tableData.clear();
		try (ResultSet results = statement.executeQuery("SELECT * FROM " + tableName + " LIMIT " + PAGE_SIZE + " OFFSET "
				+ OFFSET)) {

			tableData = getRecords(results);
		} catch (SQLException e) {
			e.printStackTrace();
			return new StatusMessage(StatusConstants.ERROR, "Error while featching record");
		}
		return new StatusMessage(StatusConstants.SUCCESS);
	}

	private void adjustOffsetForPrevious() {
		OFFSET = OFFSET - PAGE_SIZE;
		if (OFFSET < 0) {
			OFFSET = 0;
		}
	}

	public StatusMessage jumpToPage(long pageNumber) {

		if(getTotalNumberOfPages()==null){
			return new StatusMessage(StatusConstants.ERROR, "Total number of pages not yet initialized, jump opertion is not allowed");
		}
		
		if (getCurrentPageNumber() == (long) getTotalNumberOfPages() && (pageNumber >= getTotalNumberOfPages())) {
			return new StatusMessage(StatusConstants.EOF, "End of file reached");
		}

		Long numberOfRecords = getRowCount();
		long tempOffset = adjustOffsetForJump(pageNumber, numberOfRecords);

		try (ResultSet results = statement.executeQuery("SELECT * FROM " + tableName + " LIMIT " + PAGE_SIZE + " OFFSET "
				+ OFFSET)) {
			List<RowData> tempTableData = getRecords(results);

			if (tempTableData.size() != 0) {
				tableData.clear();
				tableData.addAll(tempTableData);
			} else {
				OFFSET = tempOffset;
				return new StatusMessage(StatusConstants.EOF, "End of file reached");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return new StatusMessage(StatusConstants.ERROR, "Error while featching record");
		}
		
		if (getCurrentPageNumber() == (long) getTotalNumberOfPages() && (pageNumber >= getTotalNumberOfPages())) {
			return new StatusMessage(StatusConstants.EOF, "End of file reached");
		}else if(getCurrentPageNumber()==1){
			return new StatusMessage(StatusConstants.BOF, "Begining of file reached");
		}else{
			return new StatusMessage(StatusConstants.SUCCESS);
		}
		

	}

	private long adjustOffsetForJump(long pageNumber, Long numberOfRecords) {
		long tempOffset = 0;
		tempOffset = OFFSET;
		OFFSET = (pageNumber * PAGE_SIZE) - PAGE_SIZE;

		if (numberOfRecords != null) {
			if (OFFSET >= rowCount) {
				OFFSET = rowCount - PAGE_SIZE;
				if (OFFSET < 0) {
					OFFSET = 0;
				}
			}
		}
		return tempOffset;
	}

	private List<RowData> getRecords(ResultSet results) throws SQLException {
		List<RowData> tempTableData = new LinkedList<>();
		int rowIndex = 1;
		while (results.next()) {
			List<ColumnData> row = new LinkedList<>();
			for (int index = 0; index < columnCount; index++) {
				row.add(new ColumnData(results.getString(index + 1)));
			}
			tempTableData.add(new RowData(row, rowIndex));
			rowIndex++;
		}
		return tempTableData;
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

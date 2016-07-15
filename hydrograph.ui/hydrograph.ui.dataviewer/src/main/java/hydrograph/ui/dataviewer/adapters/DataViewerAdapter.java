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

package hydrograph.ui.dataviewer.adapters;

import hydrograph.ui.common.schema.Field;
import hydrograph.ui.common.schema.FieldDataTypes;
import hydrograph.ui.common.schema.Fields;
import hydrograph.ui.dataviewer.constants.AdapterConstants;
import hydrograph.ui.dataviewer.constants.Messages;
import hydrograph.ui.dataviewer.constants.PreferenceConstants;
import hydrograph.ui.dataviewer.constants.StatusConstants;
import hydrograph.ui.dataviewer.datastructures.RowData;
import hydrograph.ui.dataviewer.datastructures.RowField;
import hydrograph.ui.dataviewer.datastructures.StatusMessage;
import hydrograph.ui.dataviewer.utilities.ViewDataSchemaHelper;
import hydrograph.ui.dataviewer.window.DebugDataViewer;
import hydrograph.ui.logging.factory.LogFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
/**
 * 
 * DataViewerAdapter is responsible to make data available to DataViewer whenever required
 * 
 * @author Bitwise
 *
 */
public class DataViewerAdapter {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(DataViewerAdapter.class);
	private List<RowData> viewerData;

	private String databaseName;
	private String tableName;

	private List<String> columnList;
	private int columnCount;

	volatile private Long rowCount = null;
	volatile private int pageSize;
	volatile private long offset;

	private Connection connection;
	private Statement statement;
	private Map<String,Integer> allColumnsMap;
	private String filterCondition;
	private DebugDataViewer debugDataViewer;
	private String SCHEMA_FILE_EXTENTION=".xml";

	public DataViewerAdapter(String databaseName, String tableName, int PAGE_SIZE, long INITIAL_OFFSET, DebugDataViewer debugDataViewer) throws ClassNotFoundException, SQLException {
		this.databaseName = databaseName;
		this.tableName = tableName;
		viewerData = new LinkedList<>();
		columnList = new LinkedList<>();
		allColumnsMap= new LinkedHashMap<String,Integer>();
		columnCount = 0;
		this.pageSize = PAGE_SIZE;
		this.offset = INITIAL_OFFSET;
		this.debugDataViewer=debugDataViewer;
		initializeAdapter();
	}
	
	public DataViewerAdapter(String databaseName, String tableName, int PAGE_SIZE, long INITIAL_OFFSET, DebugDataViewer debugDataViewer,String filterCondition) throws ClassNotFoundException, SQLException {
		this.databaseName = databaseName;
		this.tableName = tableName;
		viewerData = new LinkedList<>();
		columnList = new LinkedList<>();
		allColumnsMap= new LinkedHashMap<String,Integer>();
		columnCount = 0;
		this.pageSize = PAGE_SIZE;
		this.offset = INITIAL_OFFSET;
		this.debugDataViewer=debugDataViewer;
		this.filterCondition=filterCondition;
		initializeAdapter();
	}

	/**
	 * 
	 * Initialize adapter
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void initializeAdapter() throws ClassNotFoundException, SQLException{
		ResultSet resultSet = null;

		createConnection();
		String sql = new ViewDataQueryBuilder(tableName).limit(0).getQuery(filterCondition);
		resultSet = statement.executeQuery(sql);

		initializeColumnCount(resultSet);
		initializeColumnList(resultSet);
		initializeTableData();
		resultSet.close();
	}

	private void createConnection() throws ClassNotFoundException, SQLException {
		Class.forName(AdapterConstants.CSV_DRIVER_CLASS);
		Properties properties = new Properties();
		properties.put("columnTypes",getDataTypeString().substring(0,getDataTypeString().length()-1));
		connection = DriverManager.getConnection(AdapterConstants.CSV_DRIVER_CONNECTION_PREFIX + databaseName,properties);
		statement = connection.createStatement();
	}
	
	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}
	
	private String getDataTypeString() {
		String dataTypeString="";
		String debugFileName = debugDataViewer.getDebugFileName();
		String debugFileLocation = debugDataViewer.getDebugFileLocation();

		Fields dataViewerFileSchema = ViewDataSchemaHelper.INSTANCE
				.getFieldsFromSchema(debugFileLocation + debugFileName
						+ SCHEMA_FILE_EXTENTION);
		for (Field field : dataViewerFileSchema.getField()) {
			FieldDataTypes fieldDataTypes=field.getType();
			dataTypeString=dataTypeString+fieldDataTypes.value().split("\\.")[2]+",";
		}
		return dataTypeString;
	}
	
	/**
	 * Re-initialize adapter with given page size
	 * 
	 * @param pageSize
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void reinitializeAdapter(int pageSize,boolean resetRowCount) throws ClassNotFoundException, SQLException  {		
		this.pageSize = pageSize;
		this.offset = PreferenceConstants.INITIAL_OFFSET;
		if(resetRowCount){
			rowCount=null;
		}
		initializeAdapter();
	}
	
	private void initializeColumnList(ResultSet resultSet) throws SQLException {
		columnList.clear();
		for (int index = 1; index <= columnCount; index++) {
			columnList.add(resultSet.getMetaData().getColumnName(index));
			allColumnsMap.put(resultSet.getMetaData().getColumnName(index), index-1);
		}
	}

	private void initializeColumnCount(ResultSet resultSet) throws SQLException {
		columnCount = resultSet.getMetaData().getColumnCount();
	}

	public void initializeTableData() throws SQLException  {
		viewerData.clear();
		
		String sql;
		if (filterCondition!=null&& !filterCondition.isEmpty()) {
			sql = new ViewDataQueryBuilder(tableName).limit(pageSize).offset(offset).getQuery(filterCondition);
		}
		else
		{
			sql = new ViewDataQueryBuilder(tableName).limit(pageSize).offset(offset).getQuery("");
		}
		ResultSet results = statement.executeQuery(sql);
		int rowIndex = 1;
		while (results.next()) {
			List<RowField> row = new LinkedList<>();
			for (int index = 1; index <= columnCount; index++) {
				row.add(new RowField(results.getString(index)));
			}
			viewerData.add(new RowData(row, rowIndex));
			rowIndex++;
		}

		results.close();
	}


	/**
	 * 
	 * Get page status
	 * 
	 * @return Page Status e.g 10/100 or 10
	 */
	public String getPagePageStatus() {
		if (getTotalNumberOfPages() != null) {
			if (getTotalNumberOfPages() == 0) {
				return String.valueOf(getCurrentPageNumber()) + "/" + String.valueOf(getTotalNumberOfPages()+1);
			} else {
				return String.valueOf(getCurrentPageNumber()) + "/" + String.valueOf(getTotalNumberOfPages());
			}

		} else {
			return String.valueOf(getCurrentPageNumber());
		}
	}

	/**
	 * 
	 * Get total number of pages in file
	 * 
	 * @return number of pages
	 */
	public Long getTotalNumberOfPages() {
		if (getRowCount() != null) {
			if ((getRowCount() % pageSize) != 0)
				return (getRowCount() / pageSize) + 1;
			else
				return (getRowCount() / pageSize);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * Get number of rows in file
	 * 
	 * @return number of pages
	 */
	public Long getRowCount() {
		return rowCount;
	}
	
	/**
	 * 
	 * Set number of rows 
	 * 
	 * @return number of pages
	 */
	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * 
	 * Find row count
	 * 
	 * @return
	 */
	public StatusMessage fetchRowCount() {
		String sql = new ViewDataQueryBuilder(tableName).column("COUNT(1)").getQuery(filterCondition);
		try (ResultSet rowCountResultSet = statement.executeQuery(sql)) {
			rowCountResultSet.next();
			rowCount = rowCountResultSet.getLong(1);
		} catch (SQLException sqlException) {
			logger.error("Invalid debug file, Unable to fetch row count", sqlException);
			return new StatusMessage(StatusConstants.ERROR, Messages.INVALID_DEBUG_FILE);
		}
		return new StatusMessage(StatusConstants.SUCCESS);

	}

	/**
	 * 
	 * Get current page number
	 * 
	 * @return current page number
	 */
	public long getCurrentPageNumber() {
		if(((offset + pageSize) % pageSize) !=0){
			return ((offset + pageSize) / pageSize)+1;
		}else{
			return (offset + pageSize) / pageSize;
		}
		
	}

	/**
	 * 
	 * Get file data
	 * 
	 * @return list of {@link RowData}
	 */
	public List<RowData> getFileData() {
		return viewerData;
	}

	/**
	 * 
	 * Get list of columns
	 * 
	 * @return list of columns
	 */
	public List<String> getColumnList() {
		return columnList;
	}
		
	/**
	 * 
	 * Get map of all columns and index
	 * 
	 * @return Map of columns and corresponding index
	 */
	public Map getAllColumnsMap() {
		return allColumnsMap;
	}
	
	/**
	 * 
	 * Get number of columns
	 * 
	 * @return
	 */
	public int getColumnCount() {
		return columnCount;

	}

	/**
	 * 
	 * Get page size
	 * 
	 * @return page size
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 
	 * Get offset 
	 * 
	 * @return offset
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * 
	 * Fetch next set of records from file
	 * 
	 * @return {@link StatusMessage}
	 */
	public StatusMessage next() {
		adjustOffsetForNext();
		String sql = new ViewDataQueryBuilder(tableName).limit(pageSize).offset(offset).getQuery(filterCondition);
		try (ResultSet results = statement.executeQuery(sql)) {
			List<RowData> tempTableData = getRecords(results);
			if (tempTableData.size() != 0) {
				viewerData.clear();
				viewerData.addAll(tempTableData);
			} else {
				offset = rowCount - pageSize;
				return new StatusMessage(StatusConstants.EOF, Messages.END_OF_FILE);
			}
		} catch (SQLException sqlException) {
			logger.error(Messages.ERROR_WHILE_FETCHING_RECORDS,sqlException);
			return new StatusMessage(StatusConstants.ERROR, Messages.ERROR_WHILE_FETCHING_RECORDS);
		}
		
		if(rowCount!=null){
			if ((offset + pageSize) >= rowCount || offset == 0) {
				return new StatusMessage(StatusConstants.EOF, Messages.END_OF_FILE);
			}else{
				return new StatusMessage(StatusConstants.SUCCESS);
			}
		}else{
			return new StatusMessage(StatusConstants.SUCCESS);
		}
		
		
	}

	private void adjustOffsetForNext() {
		offset = offset + pageSize;
		if (rowCount != null) {
			if (offset >= rowCount) {
				offset = rowCount - pageSize;
				if (offset < 0) {
					offset = 0;
				}
			}
		}
	}

	/**
	 * 
	 * Fetch previous set of records from file
	 * 
	 * @return {@link StatusMessage}
	 */
	public StatusMessage previous() {
		adjustOffsetForPrevious();
		
		viewerData.clear();
		
		String sql = new ViewDataQueryBuilder(tableName).limit(pageSize).offset(offset).getQuery(filterCondition);
		try (ResultSet results = statement.executeQuery(sql)) {

			viewerData = getRecords(results);
		} catch (SQLException e) {
			logger.error(Messages.ERROR_WHILE_FETCHING_RECORDS,e);
			return new StatusMessage(StatusConstants.ERROR, Messages.ERROR_WHILE_FETCHING_RECORDS);
		}
		
		if (offset == 0) {
			return new StatusMessage(StatusConstants.BOF, Messages.BEGINING_OF_FILE);
		}else{
			return new StatusMessage(StatusConstants.SUCCESS);
		}
	}

	private void adjustOffsetForPrevious() {
		offset = offset - pageSize;
		if (offset < 0) {
			offset = 0;
		}
	}

	/**
	 * 
	 * Fetch records at given page number from file
	 * 
	 * @param pageNumber
	 * @return {@link StatusMessage}
	 */
	public StatusMessage jumpToPage(long pageNumber) {

		if(getTotalNumberOfPages()==null){
			return new StatusMessage(StatusConstants.ERROR, Messages.JUMP_To_PAGE_OPERATION_NOT_ALLOWED);
		}
		
		if (getCurrentPageNumber() == (long) getTotalNumberOfPages() && (pageNumber >= getTotalNumberOfPages())) {
			return new StatusMessage(StatusConstants.EOF, Messages.END_OF_FILE);
		}

		Long numberOfRecords = getRowCount();
		long tempOffset = adjustOffsetForJump(pageNumber, numberOfRecords);

		String sql = new ViewDataQueryBuilder(tableName).limit(pageSize).offset(offset).getQuery(filterCondition);
		try (ResultSet results = statement.executeQuery(sql)) {
			List<RowData> tempTableData = getRecords(results);

			if (tempTableData.size() != 0) {
				viewerData.clear();
				viewerData.addAll(tempTableData);
			} else {
				offset = tempOffset;
				return new StatusMessage(StatusConstants.EOF, Messages.END_OF_FILE);
			}

		} catch (SQLException sqlException) {
			logger.error(Messages.ERROR_WHILE_FETCHING_RECORDS,sqlException);
			return new StatusMessage(StatusConstants.ERROR, Messages.ERROR_WHILE_FETCHING_RECORDS);
		}
		
		if (pageNumber >= getTotalNumberOfPages()) {
			return new StatusMessage(StatusConstants.EOF, Messages.END_OF_FILE);
		}else if(getCurrentPageNumber()==1){
			return new StatusMessage(StatusConstants.BOF, Messages.BEGINING_OF_FILE);
		}else{
			return new StatusMessage(StatusConstants.SUCCESS);
		}
		

	}

	private long adjustOffsetForJump(long pageNumber, Long numberOfRecords) {
		long tempOffset = 0;
		tempOffset = offset;
		offset = (pageNumber * pageSize) - pageSize;

		if (numberOfRecords != null) {
			if (offset >= rowCount) {
				offset = rowCount - pageSize;
				if (offset < 0) {
					offset = 0;
				}
			}
		}
		return tempOffset;
	}

	private List<RowData> getRecords(ResultSet results) throws SQLException {
		List<RowData> tempTableData = new LinkedList<>();
		int rowIndex = 1;
		while (results.next()) {
			List<RowField> row = new LinkedList<>();
			for (int index = 0; index < columnCount; index++) {
				row.add(new RowField(results.getString(index + 1)));
			}
			tempTableData.add(new RowData(row, rowIndex));
			rowIndex++;
		}
		return tempTableData;
	}

	/**
	 * 
	 * close file connections
	 * 
	 */
	public void closeConnection() {
		try {
			if(statement!=null){
				statement.close();
			}
			
			if(connection!=null){
				connection.close();
			}			
		} catch (SQLException e) {
			logger.warn("Unable to close csv connection", e);			
		}
	}
	
	/**
	 * 
	 * Update the columns list
	 * 
	 * @param columnList
	 */
	public void setColumnList(List<String> columnList) {
		this.columnList.clear();
		this.columnList.addAll(columnList);
	}

}

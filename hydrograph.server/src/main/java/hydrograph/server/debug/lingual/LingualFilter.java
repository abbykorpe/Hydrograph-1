package hydrograph.server.debug.lingual;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will filter data based on query
 * 
 * @author Santlal
 * 
 */
public class LingualFilter {
	static Logger LOG = LoggerFactory.getLogger(LingualFilter.class);
	String resultSchema = null;
	String processingSchema = null;

	public void filterData(String linugalMetaDataPath, String uniqueID,
			String hdfsFilePath, double sizeOfDataInByte,
			String localDebugFilePath, String condition, String[] fieldNames,
			Type[] fieldTypes, Configuration conf) {

		String tableName = uniqueID + "_table";
		String stereotypeName = uniqueID + "_stereo";
	//	resultSchema = uniqueID + "_resultSchema";
		processingSchema = uniqueID + "_process";
		try {
			new LingualSchemaCreator().createCatalog(linugalMetaDataPath,
					processingSchema,  tableName, stereotypeName,
					hdfsFilePath, fieldNames, fieldTypes);
			runJdbcQuery(getQuery(tableName, condition), getProperties(conf),
					sizeOfDataInByte, localDebugFilePath);
			
			new LingualSchemaCreator().cleanUp(processingSchema);
		} catch (ClassNotFoundException | IOException | SQLException e) {
			LOG.error(e.getMessage());
		}
	}

	private Properties getProperties(Configuration conf) {
		Properties properties = new Properties();
		properties.putAll(conf.getValByRegex(".*"));
		return properties;
	}

	private String getQuery(String tableName, String condition) {
		String query = "select * from  \"" + processingSchema + "\"" + ".\""
				+ tableName + "\" where " + condition;
		
		return query;
	}

	private void runJdbcQuery(String query, Properties properties,
			double sizeOfDataInByte, String localDebugFile)
			throws ClassNotFoundException, SQLException, IOException {
		LOG.debug("Initializing Connection ");
		Connection connection;
		Class.forName("cascading.lingual.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:lingual:hadoop2-mr1",
				properties);
		Statement statement = connection.createStatement();

		LOG.debug("executing query: " + query);
		ResultSet resultSet = statement.executeQuery(query);

		writeFiles(resultSet, sizeOfDataInByte, localDebugFile);
		resultSet.close();
	}

	private void writeFiles(ResultSet resultSet, double sizeOfDataInByte,
			String localDebugFile) throws SQLException, IOException {

		String row = "";
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnLength = metaData.getColumnCount();
		int numOfBytes = 0;
		OutputStream os = new FileOutputStream(localDebugFile);
		StringBuilder stringBuilder = new StringBuilder();
		os.write((getColumnName(metaData) + "\n").toString().getBytes());

		while (resultSet.next()) {
			row = "";
			for (int i = 1; i <= columnLength; i++) {
				row = (resultSet.getObject(i) == null) ? "" : resultSet
						.getObject(i).toString();
				if (i != columnLength) {
					row += ",";
				}
				numOfBytes += row.length();
				stringBuilder.append(row);
			}

			if (numOfBytes <= sizeOfDataInByte) {
				os.write((stringBuilder + "\n").toString().getBytes());
			} else {
				break;
			}
			stringBuilder.setLength(0);
		}
		os.close();
	}

	private StringBuilder getColumnName(ResultSetMetaData metaData)
			throws SQLException {
		String columnName = "";
		StringBuilder stringBuilder = new StringBuilder();
		int numberOfColumn = metaData.getColumnCount();
		for (int i = 1; i <= numberOfColumn; i++) {
			columnName = metaData.getColumnName(i);
			if (i != numberOfColumn)
				columnName += ",";
			stringBuilder.append(columnName);
		}
		return stringBuilder;
	}
}

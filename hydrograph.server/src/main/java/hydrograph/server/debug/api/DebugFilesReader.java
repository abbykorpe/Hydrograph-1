/*******************************************************************************
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
 *******************************************************************************/
package hydrograph.server.debug.api;

import hydrograph.server.debug.utilities.Constants;
import hydrograph.server.debug.utilities.ServiceUtilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.avro.Schema.Field;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.IndexedRecord;
import org.apache.avro.mapred.FsInput;
import org.apache.commons.io.FilenameUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathNotFoundException;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Api for reading debug data from avro files
 * 
 * @author abhishekk
 *
 */
public class DebugFilesReader implements PrivilegedAction<Object> {

	static Logger LOG = LoggerFactory.getLogger(DebugFilesReader.class);
	private List<FileStatus> listOfFileStatus;
	private Iterator<FileStatus> fileStatusIterator;
	private org.apache.avro.file.FileReader<IndexedRecord> fileReader;
	private boolean isAvroFileRead = false;
	private boolean isOpen = false;
	private FileStatus[] fileStatus;
	private String debugPath;
	private String deletePath;
	private Path path;
	private Configuration configuration;
	private long numOfBytes = 0;
	private long sizeOfData;

	private char separator = ',';
	private char quotechar = '"';
	private char escapechar = '$';
	// default size of StirngBuilder is 1MB.
	private final int INITIAL_STRING_SIZE = 1048576;

	private final char NO_QUOTE_CHARACTER = '\u0000';
	private final char NO_ESCAPE_CHARACTER = '\u0000';
	private final String DEFAULT_LINE_END = "\n";

	static long COUNTER = 0;

	/**
	 * Populates the values of debugPath and deletePath when running in Local
	 * Mode
	 * 
	 * @param basePath
	 * @param jobId
	 * @param componentId
	 * @param socketId
	 * @param sizeOfData
	 * @param fieldSeperator
	 * @param escapeChar
	 * @param quotChar
	 */
	public DebugFilesReader(String basePath, String jobId, String componentId,
			String socketId, long sizeOfData, char fieldSeperator,
			char escapeChar, char quotChar) {
		this.sizeOfData = sizeOfData;
		this.separator = fieldSeperator;
		this.escapechar = escapeChar;
		this.quotechar = quotChar;
		if (basePath.equals("")) {
			LOG.error("BasePath parameter is empty");
		} else if (jobId.equals("")) {
			LOG.error("JobId parameter is empty");
		} else if (componentId.equals("")) {
			LOG.error("ComponentId parameter is empty");
		} else if (socketId.equals("")) {
			LOG.error("SocketId parameter is empty");
		} else {
			configuration = new Configuration();
			String hdfsConfigPath = ServiceUtilities
					.getServiceConfigResourceBundle().getString(
							Constants.HDFS_SITE_CONFIG_PATH);
			String coreSiteConfigPath = ServiceUtilities
					.getServiceConfigResourceBundle().getString(
							Constants.CORE_SITE_CONFIG_PATH);
			configuration.addResource(new Path(hdfsConfigPath));
			configuration.addResource(new Path(coreSiteConfigPath));

			deletePath = basePath + "/debug/" + jobId;
			debugPath = basePath + "/debug/" + jobId + "/" + componentId + "_"
					+ socketId;
			LOG.info("BasePath : " + basePath);
			LOG.info("JobId : " + jobId);
			LOG.info("ComponentId : " + componentId);
			LOG.info("SocketId : " + socketId);
			LOG.info("DebugFilePath : " + debugPath);
		}
	}

	/**
	 * Populates the values of debugPath and deletePath when running in Remote
	 * Mode It also authenticates the user using Kerberos token
	 * 
	 * @param basePath
	 * @param jobId
	 * @param componentId
	 * @param socketId
	 * @param userId
	 * @param password
	 * @throws Exception
	 */
	public DebugFilesReader(String basePath, String jobId, String componentId,
			String socketId, String userId, String password) throws Exception {
		if (userId.equals("")) {
			LOG.error("UserId parameter is empty");
		} else if (password.equals("")) {
			LOG.error("Passoword parameter is empty");
		} else if (basePath.equals("")) {
			LOG.error("BasePath parameter is empty");
		} else if (jobId.equals("")) {
			LOG.error("JobId parameter is empty");
		} else if (componentId.equals("")) {
			LOG.error("ComponentId parameter is empty");
		} else if (socketId.equals("")) {
			LOG.error("SocketId parameter is empty");
		} else {
			deletePath = basePath + "/debug/" + jobId;
			debugPath = basePath + "/debug/" + jobId + "/" + componentId + "_"
					+ socketId;
			LOG.info("BasePath : " + basePath);
			LOG.info("JobId : " + jobId);
			LOG.info("ComponentId : " + componentId);
			LOG.info("SocketId : " + socketId);
			LOG.info("DebugFilePath : " + debugPath);
			LOG.info("UserID : " + userId);

			configuration = new Configuration();
			String hdfsConfigPath = ServiceUtilities
					.getServiceConfigResourceBundle().getString(
							Constants.HDFS_SITE_CONFIG_PATH);
			String coreSiteConfigPath = ServiceUtilities
					.getServiceConfigResourceBundle().getString(
							Constants.CORE_SITE_CONFIG_PATH);
			System.out.println("In remote mode class loader::"
					+ configuration.getClassLoader());
			LOG.info("In remote mode class loader::"
					+ configuration.getClassLoader().toString());
			configuration.addResource(new Path(hdfsConfigPath));
			configuration.addResource(new Path(coreSiteConfigPath));

			getKerberosToken(userId, password.toCharArray());
		}
	}

	/**
	 * Populates the values of debugPath and deletePath when running in Remote
	 * Mode It also authenticates the user using Kerberos token
	 * 
	 * @param basePath
	 * @param jobId
	 * @param componentId
	 * @param socketId
	 * @param userId
	 * @param password
	 * @param sizeOfData
	 * @param fieldSeperator
	 * @param escapeChar
	 * @param quotChar
	 * @throws Exception
	 */
	public DebugFilesReader(String basePath, String jobId, String componentId,
			String socketId, String userId, String password, long sizeOfData,
			char fieldSeperator, char escapeChar, char quotChar)
			throws Exception {
		this(basePath, jobId, componentId, socketId, userId, password);
		this.sizeOfData = sizeOfData;
		this.separator = fieldSeperator;
		this.escapechar = escapeChar;
		this.quotechar = quotChar;
	}

	private void getKerberosToken(String user, char[] password)
			throws LoginException, IOException {
		LOG.trace("Entering method getKerberosToken() for user: " + user);
		URL url = DebugFilesReader.class.getClassLoader().getResource(
				"jaas.conf");
		System.setProperty("java.security.auth.login.config",
				url.toExternalForm());

		LOG.info("Generating Kerberos ticket for user: " + user);
		UserGroupInformation.setConfiguration(configuration);

		LoginContext lc = new LoginContext("EntryName",
				new UserPassCallbackHandler(user, password));
		lc.login();

		Subject subject = lc.getSubject();
		UserGroupInformation.loginUserFromSubject(subject);
		Subject.doAs(subject, this);
		LOG.info("Kerberos ticket successfully generated for user: " + user);
	}

	private FileStatus getNextAvroFile(Iterator<FileStatus> fileStatusIterator) {
		LOG.trace("Entering method getNextAvroFile()");
		boolean continueSearchForAvroFile = true;
		FileStatus fileStatus = null;
		do {
			if (fileStatusIterator.hasNext()) {
				fileStatus = (FileStatus) fileStatusIterator.next();
				if (FilenameUtils.isExtension(fileStatus.getPath().getName(),
						"avro")) {
					isAvroFileRead = true;
					return fileStatus;
				}
			} else {
				continueSearchForAvroFile = false;
			}
		} while (continueSearchForAvroFile);
		if (continueSearchForAvroFile == false && isAvroFileRead == false) {
			try {
				throw new FileNotFoundException(
						"No Avro Files present in the designated folder");
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	public String getFileSchema() {
		return fileReader.getSchema().toString(true);
	}

	/**
	 * Returns a boolean value indicating if next record is present or not
	 * 
	 * @return a boolean value indicating if next record is present or not
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public boolean hasNext() throws FileNotFoundException,
			IllegalArgumentException, IOException {
		LOG.trace("Entering method hasNext()");
		if (!isOpen) {
			if (isFilePathExists()) {
				openDataReader();
			} else {
				throw new FileNotFoundException("File path not found: "
						+ debugPath);
			}
		}
		if (fileReader != null) {
			if (numOfBytes <= sizeOfData) {
				if (fileReader.hasNext()) {
					return true;
				} else if (fileStatusIterator.hasNext()) {
					FileStatus fileStatus = getNextAvroFile(fileStatusIterator);
					if (fileStatus != null) {
						createAndOpenDataReader(fileStatus);
						return true;
					}
				} else {
					return false;
				}
			}
		}
		close();
		return false;
	}

	// This method will return Object[] and it will show Date values in
	// date format instead of long values.

	/**
	 * Returns next record from the Avro file
	 *
	 * @return next record from the Avro file
	 */
	public StringBuilder next() {
		LOG.trace("Entering method next()");
		try {
			StringBuilder rec = new StringBuilder(INITIAL_STRING_SIZE);
			IndexedRecord next = fileReader.next();

			if (COUNTER == 0) {
				String header = generateHeader(next);
				rec = rec.append(header);
			}
//			rec = rec.append(createCSVLine(CustomAvroToCascading.parseRecord(
//					fileReader.next(), fileReader.getSchema())));

			
			numOfBytes += next.toString().length();
			COUNTER++;
			return rec.append(next);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// return CustomAvroToCascading.parseRecord(
		// fileReader.next(), fileReader.getSchema());
	}

	// /**
	// * Returns next record from the Avro file
	// *
	// * @return next record from the Avro file
	// */
	// public StringBuilder next() {
	// try {
	// StringBuilder rec = new StringBuilder(INITIAL_STRING_SIZE);
	// IndexedRecord next = fileReader.next();
	//
	// if (COUNTER == 0) {
	// String header = generateHeader(next);
	// rec = rec.append(header);
	// }
	// rec = rec
	// .append(createCSVLine(parseRecord(next, next.getSchema())));
	//
	// numOfBytes += rec.length();
	// COUNTER++;
	// return rec;
	// } catch (Exception e) {
	// throw new RuntimeException(e);
	// }
	// }

	/**
	 * @param next
	 * @return
	 */
	private String generateHeader(IndexedRecord next) {
		List<Field> fields = next.getSchema().getFields();
		int cnt = 0;
		StringBuilder sb = new StringBuilder(128);
		for (Field field : fields) {
			if (cnt > 0) {
				sb.append(separator);
			}
			sb.append(field.name());
			cnt++;
		}
		sb.append(DEFAULT_LINE_END);
		return sb.toString();
	}

	private StringBuilder createCSVLine(Object[] objects) {

		StringBuilder stringBuilder = new StringBuilder(INITIAL_STRING_SIZE);
		if (objects == null) {
			return stringBuilder;
		}

		for (int i = 0; i < objects.length; i++) {
			if (i != 0) {
				stringBuilder.append(separator);
			}
			String val = objects[i].toString();

			boolean stringContainsSpecialCharacters = specialCharactersPresentInString(val);
			if ((stringContainsSpecialCharacters)
					&& quotechar != NO_QUOTE_CHARACTER) {
				stringBuilder.append(quotechar);
			}
			if (stringContainsSpecialCharacters) {
				stringBuilder.append(processLine(val));
			} else {
				stringBuilder.append(val);
			}
			if ((stringContainsSpecialCharacters)
					&& quotechar != NO_QUOTE_CHARACTER) {
				stringBuilder.append(quotechar);
			}
		}
		stringBuilder.append(DEFAULT_LINE_END);
		return stringBuilder;
	}

//	private Object fromAvroFixed(Object obj, Schema schema) {
//		Fixed fixed = (Fixed) obj;
//		return new BytesWritable(fixed.bytes());
//	}
//
//	private Object fromAvroMap(Object obj, Schema schema) {
//
//		Map<String, Object> convertedMap = new HashMap<String, Object>();
//		// CharSequence because the str can be configured as either Utf8 or
//		// String.
//		for (Map.Entry<CharSequence, Object> e : ((Map<CharSequence, Object>) obj)
//				.entrySet()) {
//			convertedMap
//					.put(e.getKey().toString(),
//							(schema.getType().getName().equals("")
//									|| schema.getType().getName().equals("") ? fromAvro(
//									e.getValue(), schema.getValueType()) : e
//									.getValue()));
//		}
//		return convertedMap;
//	}

//	private Object fromAvro(Object obj, Schema schema) {
//		if (obj == null) {
//			return null;
//		}
//		switch (schema.getType()) {
//
//		case UNION:
//			return fromAvroUnion(obj, schema);
//
//		case ARRAY:
//			return fromAvroArray(obj, schema);
//
//		case STRING:
//		case ENUM:
//			return obj.toString();
//
//		case FIXED:
//			return fromAvroFixed(obj, schema);
//		case BYTES:
//			return fromAvroBytes((ByteBuffer) obj, schema);
//
//		case RECORD:
//			Object[] objs = parseRecord((IndexedRecord) obj, schema);
//			Tuple result = new Tuple();
//			result.addAll(objs);
//			return result;
//
//		case MAP:
//			return fromAvroMap(obj, schema);
//
//		case NULL:
//		case BOOLEAN:
//		case DOUBLE:
//		case FLOAT:
//		case INT:
//		case LONG:
//			Map<String, JsonNode> props = schema.getJsonProps();
//			if (props.containsKey("logicalType")
//					&& props.get("logicalType").toString().contains("date")) {
//				return new SimpleDateFormat("yyyy-MM-dd").format(new Date(
//						(long) obj));
//			}
//			return obj;
//
//		default:
//			throw new RuntimeException("Can't convert from type "
//					+ schema.getType().toString());
//
//		}
//	}
//
//	private Object fromAvroUnion(Object obj, Schema schema) {
//		List<Schema> types = schema.getTypes();
//		if (types.size() < 1) {
//			throw new AvroRuntimeException("Union has no types");
//		}
//		if (types.size() == 1) {
//			return fromAvro(obj, types.get(0));
//		} else if (types.size() > 2) {
//			throw new AvroRuntimeException(
//					"Unions may only consist of a concrete type and null in cascading.avro");
//		} else if (!types.get(0).getType().equals(Type.NULL)
//				&& !types.get(1).getType().equals(Type.NULL)) {
//			throw new AvroRuntimeException(
//					"Unions may only consist of a concrete type and null in cascading.avro");
//		} else {
//			Integer concreteIndex = (types.get(0).getType() == Type.NULL) ? 1
//					: 0;
//			return fromAvro(obj, types.get(concreteIndex));
//		}
//	}
//
//	private Object fromAvroArray(Object obj, Schema schema) {
//		List<Object> array = new ArrayList<Object>();
//		for (Object element : (GenericData.Array) obj) {
//			array.add(fromAvro(element, schema.getElementType()));
//		}
//		return array;
//	}
//
//	public Object[] parseRecord(IndexedRecord record, Schema readerSchema) {
//
//		Object[] result = new Object[readerSchema.getFields().size()];
//		Schema writerSchema = record.getSchema();
//		List<Field> schemaFields = readerSchema.getFields();
//		for (int i = 0; i < schemaFields.size(); i++) {
//			Field field = schemaFields.get(i);
//			if (writerSchema.getField(field.name()) == null) {
//				throw new AvroRuntimeException("Not a valid schema field: "
//						+ field.name());
//			}
//			Object obj = record.get(i);
//			result[i] = fromAvro(obj, field.schema());
//
//		}
//		return result;
//	}
//
//	private BigDecimal fromAvroBytes(ByteBuffer byteBuffer, Schema schema) {
//
//		int scale = schema.getJsonProp("scale").asInt();
//		return new BigDecimal(new BigInteger(byteBuffer.array()))
//				.movePointLeft(scale);
//
//		// BytesWritable result = new BytesWritable(val.array());
//		// return result;
//	}

	private boolean specialCharactersPresentInString(String line) {
		return line.indexOf(quotechar) != -1 || line.indexOf(escapechar) != -1
				|| line.indexOf(separator) != -1
				|| line.contains(DEFAULT_LINE_END) || line.contains("\r");
	}

	private StringBuilder processLine(String nextElement) {
		StringBuilder sb = new StringBuilder(128);
		for (int j = 0; j < nextElement.length(); j++) {
			char nextChar = nextElement.charAt(j);
			processChar(sb, nextChar);
		}
		return sb;
	}

	private void processChar(StringBuilder sb, char nextChar) {
		if (escapechar != NO_ESCAPE_CHARACTER
				&& (nextChar == quotechar || nextChar == escapechar)) {
			sb.append(escapechar).append(nextChar);
		} else {
			sb.append(nextChar);
		}
	}

	/**
	 * @return a boolean value indicating if the file path is valid or not
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public boolean isFilePathExists() throws IllegalArgumentException,
			IOException {
		LOG.trace("Entering method isFilePathExists()");
		path = new Path(debugPath);
		if (!path.isAbsolute()) {
			LOG.warn("Absolute path not provided. Processing aborted.");
			return false;
		}
		return FileSystem.get(configuration).exists(new Path(debugPath));
	}

	/**
	 * Opens the Avro file data reader
	 * 
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public void openDataReader() throws FileNotFoundException,
			IllegalArgumentException, IOException {
		LOG.trace("Entering method openDataReader()");
		fileStatus = FileSystem.get(configuration).listStatus(
				new Path(debugPath));
		listOfFileStatus = (List<FileStatus>) Arrays.asList(fileStatus);
		fileStatusIterator = listOfFileStatus.iterator();
		if (fileStatusIterator.hasNext()) {
			FileStatus fileStatus = getNextAvroFile(fileStatusIterator);
			if (fileStatus != null) {
				createAndOpenDataReader(fileStatus);
			}
		} else {
			throw new FileNotFoundException(
					"Could not find any files in the designated folder.");
		}
	}

	private void createAndOpenDataReader(FileStatus fileStatus)
			throws IOException {
		LOG.trace("Entering method createAndOpenDataReader()");
		try {
			SeekableInput input = new FsInput(fileStatus.getPath(),
					new Configuration());
			fileReader = DataFileReader.openReader(input,
					new GenericDatumReader<IndexedRecord>());
			isOpen = true;
			LOG.info("Avro File Reader opened");
		} catch (IOException e) {
			LOG.error("Error in creating and opening reader", e);
			throw e;
		}
	}

	@Override
	public Object run() {
		LOG.trace("Entering method run()");
		return null;
	}

	/**
	 * Closes the Avro File Reader
	 * 
	 * @throws IOException
	 */
	private void close() throws IOException {
		LOG.trace("Entering method close()");
		System.out.println("size: " + numOfBytes);
		fileReader.close();
		LOG.info("Avro File Reader closed");
	}

	/**
	 * Deletes the jobId directory
	 * 
	 * @throws IOException
	 */
	public void delete() throws IOException {
		LOG.trace("Entering method delete()");
		FileSystem fileSystem = FileSystem.get(configuration);
		Path deletingFilePath = new Path(deletePath);
		if (!fileSystem.exists(deletingFilePath)) {
			throw new PathNotFoundException(deletingFilePath.toString());
		} else {
			// Delete file
			fileSystem.delete(deletingFilePath, true);
			LOG.info("Deleted path : " + deletePath);
		}
		fileSystem.close();
	}
}
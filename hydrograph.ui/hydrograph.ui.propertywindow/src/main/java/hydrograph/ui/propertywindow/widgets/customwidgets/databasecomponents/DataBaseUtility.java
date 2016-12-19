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
package hydrograph.ui.propertywindow.widgets.customwidgets.databasecomponents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.eclipse.core.runtime.Platform;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import hydrograph.ui.common.datastructures.property.database.DatabaseParameterType;
import hydrograph.ui.common.util.PreferenceConstants;
import hydrograph.ui.communication.debugservice.DebugServiceClient;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.widgets.customwidgets.metastore.DatabaseTableSchema;

/**
 * The Class DataBase Utility
 * @author Bitwise
 *
 */
public class DataBaseUtility {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(DataBaseUtility.class);
	private static DataBaseUtility INSTANCE = new DataBaseUtility();

	private static final String PLUGIN_ID = "hydrograph.ui.dataviewer";
	//private Map<String, DatabaseParameterType> databaseConnector;
	private List<DatabaseParameterType> databaseConnector;
	private static final String SEPARATOR = "|";
	
	
	/**
	 * 
	 */
	private DataBaseUtility() {
		databaseConnector = new ArrayList<>();
	}
	
	/**
	 * Static 'instance' method
	 *
	 */
	public static DataBaseUtility getInstance( ) {
      return INSTANCE;
	}
	
	/**
	 * The Function will to add parameters in the list
	 * @param parameterType
	 */
	public void addDatabaseParams(DatabaseParameterType parameterType){
		databaseConnector.add(parameterType);
	}
	
	/**
	 * @return list of DatabaseParameter Type
	 */
	public List<DatabaseParameterType> getDatabaseParams(){
		return databaseConnector;
	}
	
	/**
	 * @param dataBaseTables
	 * @param parameterType
	 * @return
	 */
	public DatabaseTableSchema extractDatabaseDetails(List<String> dataBaseTables, DatabaseParameterType parameterType){
		String jsonResponse = "";
		DatabaseTableSchema databaseTableSchema = null;
		String host = getServiceHost();
		String port_no = getServicePort();
		
		String connection_param = parameterType.getHostName() + SEPARATOR+parameterType.getPortNo() + SEPARATOR+parameterType.getUserName()
							+ parameterType.getPassword()+ SEPARATOR + parameterType.getJdbcName() +SEPARATOR+parameterType.getDatabaseName();
		
		try {
			jsonResponse = DebugServiceClient.INSTANCE.readMetaStoreDb(connection_param, host, port_no, dataBaseTables);
			ObjectMapper mapper = new ObjectMapper();
			databaseTableSchema = mapper.readValue(jsonResponse, DatabaseTableSchema.class);
		} catch (NumberFormatException | HttpException exception) {
			logger.error("Json to object Mapping issue ", exception);
		} catch (IOException exception) {
			logger.error("Json to object Mapping issue ", exception);
		}
		return databaseTableSchema;
	}
	
	/**
	 * @return host value
	 */
	public String getServiceHost(){
		return Platform.getPreferencesService().getString(PLUGIN_ID, PreferenceConstants.REMOTE_HOST, "", null);
	}
	
	/**
	 * @return port value
	 */
	public String getServicePort(){
		return Platform.getPreferencesService().getString(PLUGIN_ID, PreferenceConstants.REMOTE_PORT_NO, 
				PreferenceConstants.DEFAULT_PORT_NO, null);
	}
	
}

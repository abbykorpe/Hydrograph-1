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

 
package hydrograph.ui.parametergrid.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.slf4j.Logger;

import hydrograph.ui.logging.factory.LogFactory;

/**
 * 
 * Class to load and store property maps to given file
 * 
 * @author Bitwise
 *
 */
public class ParameterFileManager {
	
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ParameterFileManager.class);
	
	private String parameterFilePath;
	
	public ParameterFileManager(String parameterFilePath){
		this.parameterFilePath = parameterFilePath;
		logger.debug("Intantiated parameter file manager");
	}
	
	/**
	 * 
	 * get Parameters from file
	 * 
	 * @return - Parameter map
	 * @throws IOException
	 */
	public Map<String, String> getParameterMap() throws IOException{
		Properties prop = new Properties();
		
		File file = new File(parameterFilePath);
		
		if(file.exists()){
			prop.load(parameterFilePath);
			
			logger.debug("Fetched properties {} from file {}",prop.toString(),parameterFilePath);
		}
		
		return prop.getProperties();
	}
	
	/**
	 * 
	 * Save parameters to file
	 * 
	 * @param parameterMap
	 * @param object 
	 * @param filename 
	 * @throws IOException
	 */
	public void storeParameters(Map<String, String> parameterMap,IFile filename) throws IOException{
		Properties properties = new Properties();
		properties.setProperty(parameterMap);
		
		File file = new File(parameterFilePath);
		
		if(file.exists()){
			properties.store(parameterFilePath);
			logger.debug("Saved properties {} to file {}", properties.toString(),parameterFilePath);
		}
		else
		{
			properties.load(filename.getRawLocation().toString());
			properties.store(file.getAbsolutePath());
		}
	}
}

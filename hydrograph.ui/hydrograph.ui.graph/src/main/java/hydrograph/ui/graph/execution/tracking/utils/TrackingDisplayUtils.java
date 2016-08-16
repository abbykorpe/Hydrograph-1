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

package hydrograph.ui.graph.execution.tracking.utils;

/**
 * The Class TrackingDisplayUtils.
 * @author Bitwise
 */

import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.graph.Activator;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.controller.LinkEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.execution.tracking.preferences.ExecutionPreferenceConstants;
import hydrograph.ui.graph.model.CompStatus;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

public class TrackingDisplayUtils {

public static TrackingDisplayUtils INSTANCE = new TrackingDisplayUtils();
	
	private static Logger logger = LogFactory.INSTANCE.getLogger(TrackingDisplayUtils.class);
	public static final String PROPERY_FILE_PATH = "/service/hydrograph-service.properties";
	public static final String PORT_NUMBER = "EXECUTION_TRACKING_PORT";
	public static final String REMOTE_URL = "WEBSOCKET_REMOTE_URL";
	public static final String LOCAL_URL = "WEBSOCKET_LOCAL_URL";
	public static final String WEBSOCKET_ROUTE = "WEBSOCKET_ROUTE";
	
	private String remoteHost;
	private String localHost;
	private String websocketRoute ;
	

	private TrackingDisplayUtils(){}
	
	/**
	 * Clears the status of all components. Also Initiates record count to 0.
	 */
	public void clearTrackingStatus() {

		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage().getActiveEditor();
		if(editor!=null && editor instanceof ELTGraphicalEditor)
		{
			clearTrackingStatusForEditor(editor);
		}
	}
	
	public void clearTrackingStatus(String jobId) {

		ELTGraphicalEditor editor=(ELTGraphicalEditor) PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage().getActiveEditor();
		if(editor!=null && editor instanceof ELTGraphicalEditor && (editor.getJobId().equals(jobId)))
		{
			clearTrackingStatusForEditor(editor);
		}
	}

	private void clearTrackingStatusForEditor(ELTGraphicalEditor editor) {
		GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
		for (Iterator<EditPart> ite = graphicalViewer.getEditPartRegistry().values().iterator(); 
				ite.hasNext();)
		{
			EditPart editPart = (EditPart) ite.next();
			if(editPart instanceof ComponentEditPart){
				Component component = ((ComponentEditPart)editPart).getCastedModel();
				component.updateStatus(CompStatus.BLANK.value());
			}else if(editPart instanceof LinkEditPart){
				((LinkEditPart)editPart).clearRecordCountAtPort();
			}
		}
	}

	public String getExecutiontrackingPortNo(){
		String portNumber = null;
		try {
			FileReader fileReader = new FileReader(XMLConfigUtil.CONFIG_FILES_PATH + PROPERY_FILE_PATH);
			Properties properties = new Properties();
			properties.load(fileReader);
			if(StringUtils.isNotBlank(properties.getProperty(PORT_NUMBER))){
				portNumber = properties.getProperty(PORT_NUMBER);
				remoteHost = properties.getProperty(REMOTE_URL);
				localHost = properties.getProperty(LOCAL_URL);
				websocketRoute = properties.getProperty(WEBSOCKET_ROUTE);
			}
		} catch (FileNotFoundException e) {
			logger.error("File not exists", e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return portNumber;
	} 
	
	
	public String getWebSocketRemoteUrl(){
		String portNo = getPortFromPreference();
		String remoteUrl = remoteHost + portNo + websocketRoute;
		return remoteUrl;
	}
	
	public String getWebSocketLocalHost(){
		String portNo = getPortFromPreference();
		String localUrl = localHost + portNo + websocketRoute;
		return localUrl;
		
	}
	
	public String getPortFromPreference(){
		String portNo = Platform.getPreferencesService().getString(Activator.PLUGIN_ID, ExecutionPreferenceConstants.TRACKING_PORT_NO, 
				getExecutiontrackingPortNo(), null);
		
		return portNo;
	}
}

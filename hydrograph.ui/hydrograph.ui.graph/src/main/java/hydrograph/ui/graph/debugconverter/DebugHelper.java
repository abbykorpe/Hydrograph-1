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
package hydrograph.ui.graph.debugconverter;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.controller.PortEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Container;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.model.Port;
import hydrograph.ui.logging.factory.LogFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;

import com.thoughtworks.xstream.XStream;


/**
 * @author Bitwise
 *
 */
public class DebugHelper {

	private static Logger logger = LogFactory.INSTANCE.getLogger(DebugHelper.class);
	public static DebugHelper INSTANCE = new DebugHelper();
	public static final String SERVICE_JAR = "SERVICE_JAR";
	public static final String PORT_NUMBER = "PORT_NO";
	public static final String PROPERY_FILE_PATH = "/service/hydrograph-service.properties";
	private List<String> subjobWatcherList = new ArrayList<>(); 

	/**
	 * This function used to return subgraph component_Id and socket_Id
	 *
	 */
	public List<String> getSubgraphComponent(Component component) throws CoreException{
		Container container=null;
		if(StringUtils.equalsIgnoreCase(component.getComponentName(), Constants.SUBJOB_COMPONENT)){
			String subgraphFilePath=(String) component.getProperties().get(Constants.JOB_PATH);
			if(StringUtils.isNotBlank(subgraphFilePath)){
				IPath jobPath=new Path(subgraphFilePath);
				if(jobPath.toFile().exists()){
					XStream xs = new XStream();
					container=(Container) xs.fromXML(jobPath.toFile());
					List<Link> links = null;
					for(Component component_temp:container.getChildren()){
						if(StringUtils.equalsIgnoreCase(component_temp.getComponentLabel().getLabelContents(), Constants.OUTPUT_SUBJOB)){
							links=component_temp.getTargetConnections();
						}
					}
					for(Link str : links){
						String sub_comp = str.getSource().getComponentLabel().getLabelContents();
						String sub_comp_port = str.getSourceTerminal();
						subjobWatcherList.add(sub_comp+"."+sub_comp_port);
					}
					return subjobWatcherList;
				}
				else{
					if(ResourcesPlugin.getWorkspace().getRoot().getFile(jobPath).exists()){
						XStream xs = new XStream();
					container=(Container) xs.fromXML(ResourcesPlugin.getWorkspace().getRoot().getFile(jobPath).getContents(true));
					List<Link> links = null;
					
					for(Component component_temp:container.getChildren()){
						if(StringUtils.equalsIgnoreCase(component_temp.getComponentLabel().getLabelContents(), Constants.OUTPUT_SUBJOB)){
							links=component_temp.getTargetConnections();
							break;
						}
					}for(Link link : links){
						Map<String, Port> map = link.getSource().getPorts();
						for(Entry<String, Port> entry : map.entrySet()){
							Port port = entry.getValue();
							String type = port.getPortType();
							if((type.equalsIgnoreCase("out")||type.equalsIgnoreCase("unused")) && port.isWatched()){
								Component component2 = port.getParent();
								String label = component2.getComponentLabel().getLabelContents();
								//String sub_comp = str.getSource().getComponentLabel().getLabelContents();
								String sub_comp_port = port.getTerminal();
								subjobWatcherList.add(label+"."+sub_comp_port);
							}
						}
					}
					return subjobWatcherList;
					
				  }
				}
			  }
			}
		return null;
	}
	
	/**
	 * This function used to return Rest Service port Number which running on local
	 *
	 */
	public String restServicePort(){
		String portNumber = null;
		try {
			FileReader fileReader = new FileReader(XMLConfigUtil.CONFIG_FILES_PATH + PROPERY_FILE_PATH);
			Properties properties = new Properties();
			properties.load(fileReader);
			if(StringUtils.isNotBlank(properties.getProperty(SERVICE_JAR))){
				portNumber = properties.getProperty(PORT_NUMBER);
			}
		} catch (FileNotFoundException e) {
			logger.error("File not exists", e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		return portNumber;
	}
	
	/**
	 * This function used to return Rest Service jar Name which uses to start the rest service
	 *
	 */
	public String restServiceJar(){
		String restServiceJar = null;
		try {
			FileReader fileReader = new FileReader(XMLConfigUtil.CONFIG_FILES_PATH + PROPERY_FILE_PATH);
			Properties properties = new Properties();
			properties.load(fileReader);
			if(StringUtils.isNotBlank(properties.getProperty(SERVICE_JAR))){
				restServiceJar = properties.getProperty(SERVICE_JAR);
			}
		} catch (FileNotFoundException e) {
			logger.error("File not exists", e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return restServiceJar;
	}
	
	/**
	 * This function will be return process ID which running on defined port
	 *
	 */
	public String getServicePortPID(int portNumber) throws IOException{
		if(OSValidator.isWindows()){
			ProcessBuilder builder = new ProcessBuilder(new String[]{"cmd", "/c" ,"netstat -a -o -n |findstr :"+portNumber});
			Process process =builder.start();
			InputStream inputStream = process.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String str = bufferedReader.readLine();
			str=StringUtils.substringAfter(str, "LISTENING");
			str=StringUtils.trim(str);
			return str;
		}
		else if(OSValidator.isMac()){
			//sudo lsof -n -i4tcp:8004 |grep LISTEN
			ProcessBuilder builder = new ProcessBuilder(new String[]{"sudo lsof -n -i4tcp:"+portNumber+" |grep LISTEN"});
			builder.start();
		}
		else if(OSValidator.isUnix()){
			new ProcessBuilder(new String[]{}).start();
		}
		return "";
	}
	
	
	/**
	 * This function returns that watcher is added on selected port
	 *
	 */
	public boolean checkWatcher(Component selectedComponent, String portName) {
		EditPart editPart = (EditPart) selectedComponent.getComponentEditPart();
		List<PortEditPart> portEdit = editPart.getChildren();
		for(AbstractGraphicalEditPart part : portEdit){
			if(part instanceof PortEditPart){
				String portLabel = ((PortEditPart) part).getCastedModel().getTerminal();
				if(portLabel.equals(portName)){
					return  ((PortEditPart) part).getPortFigure().isWatched();
				}
			}
		}
					
		return false;
	}
	
	/**
	 * This function will check watch point in the graph and return true if any watch point exist 
	 *
	 */
	public boolean hasMoreWatchPoints(){
		IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(activeEditor instanceof ELTGraphicalEditor){
			ELTGraphicalEditor editor=(ELTGraphicalEditor) activeEditor;
			if(editor!=null){
				GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
				for (Object objectEditPart : graphicalViewer.getEditPartRegistry().values()){
					if(objectEditPart instanceof ComponentEditPart){
						List<PortEditPart> portEditParts = ((EditPart) objectEditPart).getChildren();
						for(AbstractGraphicalEditPart part : portEditParts) {	
							if(part instanceof PortEditPart){
								boolean isWatch = ((PortEditPart)part).getPortFigure().isWatched();
								if(isWatch){
									return isWatch;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}
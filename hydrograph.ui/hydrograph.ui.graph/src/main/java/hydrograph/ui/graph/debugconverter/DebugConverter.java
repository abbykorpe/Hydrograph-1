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

import hydrograph.engine.jaxb.debug.Debug;
import hydrograph.engine.jaxb.debug.ViewData;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.execution.tracking.datastructure.SubjobDetails;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.utility.ViewDataUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;


/**
 * The Class used to generate debug xml. When we assign watchers at component source socket. 
 * It will save in debug xml with socket type and record limit.
 * 
 * @author Bitwise
 *
 */
public class DebugConverter {

	public DebugConverter() {
		
	}
	
	
	public Debug getParam() throws Exception{
		Map<String, SubjobDetails> componentNameAndLink = new HashMap();
		Debug debug = new Debug();
		ViewData viewData = null;
		String componenetId = "";
		String socket_Id = "";
		 
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		ELTGraphicalEditor editor=	(ELTGraphicalEditor) page.getActiveEditor();
		
		if(editor!=null && editor instanceof ELTGraphicalEditor)
		{
			GraphicalViewer	graphicalViewer =(GraphicalViewer) ((GraphicalEditor)editor).getAdapter(GraphicalViewer.class);
			for (Iterator<EditPart> iterator = graphicalViewer.getEditPartRegistry().values().iterator(); 
					iterator.hasNext();)
			{
				EditPart editPart = (EditPart) iterator.next();
				if(editPart instanceof ComponentEditPart){
					Component component = ((ComponentEditPart)editPart).getCastedModel();
					
					Map<String, Long> map = component.getWatcherTerminals();
					if(!map.isEmpty()){
						for(Entry<String, Long> entrySet: map.entrySet()){
							List<Link> links = ((ComponentEditPart) editPart).getCastedModel().getSourceConnections();
								if(StringUtils.equalsIgnoreCase(component.getComponentName(), Constants.SUBJOB_COMPONENT)){
									for(Link link : links){
										componentNameAndLink.clear();
										boolean isWatch = link.getSource().getPort(link.getSourceTerminal()).isWatched();
										if(isWatch){
											ViewDataUtils.getInstance().subjobParams(componentNameAndLink, component, new StringBuilder(), link.getSourceTerminal());
											for(Entry<String, SubjobDetails> entry : componentNameAndLink.entrySet()){
												String comp_soc = entry.getKey();
												System.out.println("Comp ID0:::"+comp_soc);
												String[] split = StringUtils.split(comp_soc, "/.");
												componenetId = split[0];
												for(int i = 1;i<split.length-1;i++){
													componenetId = componenetId + "." + split[i];
												}
												socket_Id = split[split.length-1];
											}
											viewData = new ViewData();
											System.out.println("Comp ID:::"+componenetId);
											viewData.setFromComponentId(componenetId);
											viewData.setOutSocketId(socket_Id);
											String portType = socket_Id.substring(0, 3);
											viewData.setOutSocketType(checkPortType(portType));
											debug.getViewData().add(viewData);
										}
									}
									break;
								}else{
									viewData = new ViewData();
									viewData.setFromComponentId(component.getComponentLabel().getLabelContents());
									viewData.setOutSocketId(entrySet.getKey());
									String portType = entrySet.getKey().substring(0, 3);
									
									viewData.setOutSocketType(checkPortType(portType));
									
									if(StringUtils.equalsIgnoreCase(portType, Constants.OUTPUT_SOCKET_TYPE)){
										viewData.setOutSocketType(Constants.OUTPUT_SOCKET_TYPE);
									}else{
									}
									debug.getViewData().add(viewData);
							}
						}
					}  
				}
			}
		}
		
		return debug;
	}
	
	private String checkPortType(String portType){
		String socketType;
		if(StringUtils.equalsIgnoreCase(portType, Constants.OUTPUT_SOCKET_TYPE)){
			socketType = Constants.OUTPUT_SOCKET_TYPE;
		}else{
			socketType = Constants.UNUSED_SOCKET_TYPE;
		}
		return socketType;
	}
	
	public void marshall(Debug debug,IFile outPutFile) throws JAXBException, IOException, CoreException{
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Debug.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		marshaller.marshal(debug, out);
		
		if (outPutFile.exists())
			outPutFile.setContents(new ByteArrayInputStream(out.toByteArray()), true,false, null);
		else
			outPutFile.create(new ByteArrayInputStream(out.toByteArray()),true, null);
		out.close();
	}
	
}

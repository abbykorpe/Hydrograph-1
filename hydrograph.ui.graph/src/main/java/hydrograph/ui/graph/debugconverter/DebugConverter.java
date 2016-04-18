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
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.debug.config.Debug;
import hydrograph.ui.graph.debug.config.DebugPlugin;
import hydrograph.ui.graph.debug.config.Limit;
import hydrograph.ui.graph.debug.config.ViewData;
import hydrograph.ui.graph.editor.ELTGraphicalEditor;
import hydrograph.ui.graph.model.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
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


public class DebugConverter {

	public DebugConverter() {
		
	}
	
	
	public Debug getParam(){
		DebugPlugin debugPlugin = new DebugPlugin();
		debugPlugin.setValue("");
		Debug debug = new Debug();
		 
		//debug.setDebugPlugin(debugPlugin);
		ViewData viewData = null;
		Limit limit = null;
		 
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
							viewData = new ViewData();
							limit = new Limit();
							viewData.setFromComponentId(component.getComponentLabel().getLabelContents());
							viewData.setOutSocketId(entrySet.getKey());
							String portType = entrySet.getKey().substring(0, 3);
							
							if(StringUtils.isNotBlank(portType)){
							if(portType.equalsIgnoreCase(Constants.OUTPUT_SOCKET_TYPE)){
								
								viewData.setOutSocketType(Constants.OUTPUT_SOCKET_TYPE);
							}else{
								viewData.setOutSocketType(Constants.UNUSED_SOCKET_TYPE);
							}
							}
							limit.setValue(entrySet.getValue());
							viewData.setLimit(limit);
							debug.getViewData().add(viewData);
						}
					}  
				}
			}
		}
		
		return debug;
	}
	
	
	public void marshall(Debug debug,IFile outPutFile) throws JAXBException, IOException, CoreException{
		
		if(debug.getDebugPlugin() == null && debug.getViewData().isEmpty()){
			return;
		}
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
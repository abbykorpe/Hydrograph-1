package com.bitwise.app.graph.debugconverter;

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

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.debug.config.Debug;
import com.bitwise.app.graph.debug.config.DebugPlugin;
import com.bitwise.app.graph.debug.config.Limit;
import com.bitwise.app.graph.debug.config.ViewData;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.model.Component;

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
		
		JAXBContext jaxbContext = JAXBContext.newInstance(debug.getClass());
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

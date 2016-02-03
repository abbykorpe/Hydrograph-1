package com.bitwise.app.graph.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.FileEditorInput;

import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.figure.ComponentFigure;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.model.components.SubgraphComponent;

// TODO: Auto-generated Javadoc
/**
 * The Class SubGraphUtility.
 */
public class SubGraphUtility {
	
	private List<Component> cacheInputSubgraphComp = new ArrayList<>();
	private List<Component>  cacheOutSubgraphComp = new ArrayList<>();
	/**
	 * Open sub graph save dialog.
	 *
	 * @return the i file
	 */
	public static IFile openSubGraphSaveDialog() {

		SaveAsDialog obj = new SaveAsDialog(Display.getDefault().getActiveShell());
		IFile file=null;
			obj.setOriginalName("subgraph.job");
		obj.open();
		
		if (obj.getReturnCode() == 0) {
			getCurrentEditor().validateLengthOfJobName(obj);
		}
		
		if(obj.getResult()!=null&&obj.getReturnCode()!=1)
		{
			IPath filePath = obj.getResult().removeFileExtension().addFileExtension("job");
			file= ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
		}
	
		return file;
	}

	/**
	 * Gets the current editor.
	 *
	 * @return the current editor
	 */
	private static ELTGraphicalEditor getCurrentEditor(){
		return (ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	}
	
	/**
	 * Do save as sub graph.
	 *
	 * @return the i file
	 */
	public static IFile doSaveAsSubGraph(){
		
		IFile file=openSubGraphSaveDialog();
       
		if(file!=null){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				out.write(getCurrentEditor().fromObjectToXML(new Container()).getBytes());
				if (file.exists())
					file.setContents(new ByteArrayInputStream(out.toByteArray()), true,	false, null);
				else
					file.create(new ByteArrayInputStream(out.toByteArray()),true, null);
				    getCurrentEditor().genrateTargetXml(file);
			} catch (CoreException  | IOException ce) {
				MessageDialog.openError(new Shell(), "Error", "Exception occured while saving the graph -\n"+ce.getMessage());
			}
			getCurrentEditor().setDirty(false);
	}
		return file;

}
	
	/**
	 * Creates the dynamic input port.
	 *
	 * @param inLinks the in links
	 * @param edComponentEditPart the ed component edit part
	 */
	public  void createDynamicInputPort(List< Link> inLinks,ComponentEditPart edComponentEditPart){
		for(int i=0;i<inLinks.size();i++){
			Component oldTarget=inLinks.get(i).getTarget();
			inLinks.get(i).getSource();
			Link link = inLinks.get(i);
			link.detachTarget();
			link.setTarget(edComponentEditPart.getCastedModel());
			link.setTargetTerminal("in"+i);				
			oldTarget.freeInputPort(link.getTargetTerminal());
			oldTarget.disconnectInput(link); 
			link.attachTarget();
			edComponentEditPart.getCastedModel().engageInputPort("in"+i);
			edComponentEditPart.refresh();
			if(!cacheInputSubgraphComp.contains(oldTarget))
			cacheInputSubgraphComp.add(oldTarget);
		}

	}
	
	/**
	 * Creates the dynamic output port.
	 *
	 * @param outLinks the out links
	 * @param edComponentEditPart the ed component edit part
	 */
	public List<Component> createDynamicOutputPort(List< Link> outLinks,ComponentEditPart edComponentEditPart){
		for(int i=0;i<outLinks.size();i++){
			Component oldSource=outLinks.get(i).getSource();
			Link link = outLinks.get(i);
			link.detachSource(); 
			link.setSource(edComponentEditPart.getCastedModel());
			link.setSourceTerminal("out"+i);
			oldSource.freeOutputPort(link.getTargetTerminal());
			oldSource.disconnectOutput(link);  	
			link.attachSource();
			edComponentEditPart.getCastedModel().engageOutputPort("out"+i);
			edComponentEditPart.refresh(); 
			if(!cacheOutSubgraphComp.contains(oldSource))
				cacheOutSubgraphComp.add(oldSource);
		}
		return cacheOutSubgraphComp;

	}
	
	/**
	 * Update sub graph model properties.
	 *
	 * @param edComponentEditPart the ed component edit part
	 * @param inPort the in port
	 * @param outPort the out port
	 * @param file the file
	 */
	public static void updateSubGraphModelProperties(ComponentEditPart edComponentEditPart,int inPort,int outPort,IFile file){
			edComponentEditPart.getCastedModel().inputPortSettings(inPort); 
			edComponentEditPart.getCastedModel().outputPortSettings(outPort);
			ComponentFigure compFig = (ComponentFigure)edComponentEditPart.getFigure();
			compFig.setHeight(inPort, outPort);			
			Dimension newSize = new Dimension(compFig.getWidth(), compFig.getHeight()+edComponentEditPart.getCastedModel().getComponentLabelMargin());
			
			edComponentEditPart.getCastedModel().setSize(newSize);
			edComponentEditPart.getCastedModel().setComponentLabel(file.getName());
			
			String subGraphFilePath = file.getFullPath().toOSString();
			edComponentEditPart.getCastedModel().getProperties().put("path",subGraphFilePath);
			if(inPort!=0 && outPort!=0)
			edComponentEditPart.getCastedModel().getProperties().put("type", "operation");
			if(inPort!=0 && outPort==0)
			edComponentEditPart.getCastedModel().getProperties().put("type", "output");
			if(inPort==0 && outPort!=0)
			edComponentEditPart.getCastedModel().getProperties().put("type", "input");			
			edComponentEditPart.refresh();
	}

	public List<Component> getCacheInputSubgraphComp() {
		return cacheInputSubgraphComp;
	}

	public void setCacheInputSubgraphComp(List<Component> cacheInputSubgraphComp) {
		this.cacheInputSubgraphComp = cacheInputSubgraphComp;
	}

	public List<Component> getCacheOutSubgraphComp() {
		return cacheOutSubgraphComp;
	}

	public void setCacheOutSubgraphComp(List<Component> cacheOutSubgraphComp) {
		this.cacheOutSubgraphComp = cacheOutSubgraphComp;
	}

	public void createSubGraphXml(ComponentEditPart componentEditPart){
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
		try {
			IPath jobFilePath=new Path((((ComponentEditPart) componentEditPart).getCastedModel()).getProperties().get("path").toString());
			IFile jobFile = ResourcesPlugin.getWorkspace().getRoot().getFile(jobFilePath);
			IFileEditorInput input = new FileEditorInput(jobFile);  
			page.openEditor(input, ELTGraphicalEditor.ID, false);
			//For selecting the created editor so it will trigger the event to activate and load the Palette
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
			if (activeWindow != null) {
				final IWorkbenchPage activePage = activeWindow.getActivePage();
				if (activePage != null) {
					activePage.activate(activePage.findEditor(input));
	        }
	    }  
	} catch (PartInitException e) {
	}
	Container container = ((ELTGraphicalEditor) page.getActiveEditor())
			.getContainer();  
	ELTGraphicalEditor editor=	(ELTGraphicalEditor) page.getActiveEditor();
	editor.viewer.setContents(container);
	
	editor.viewer.addDropTargetListener(editor.createTransferDropTargetListener());
	// listener for selection on canvas
	editor.viewer.addSelectionChangedListener(editor.createISelectionChangedListener());
	List bList = (ArrayList) Clipboard.getDefault().getContents();
			
		   	int inPort=0;
		   	int outPort=0;
		   	SubgraphComponent inputSubComponent=new SubgraphComponent();
		   	SubgraphComponent outSubComponent=new SubgraphComponent();
		   	for (Component com : cacheInputSubgraphComp) {
		   		inputSubComponent.setProperties(new LinkedHashMap<String,Object>());
						for(int j=com.getTargetConnections().size();j<com.getInPortCount();j++) {
							Link linkNew = new Link();
							linkNew.setTarget(com);
							linkNew.setSource(inputSubComponent); 
							linkNew.setTargetTerminal("in"+j);
							linkNew.setSourceTerminal("in"+inPort);
							com.connectInput(linkNew);
							inputSubComponent.connectOutput(linkNew);
							inPort++; 
							}	
						
						inputSubComponent.getProperties().put("type", "inputsubgraph");							   	
					   	container.addChild((Component) com);
					   	bList.remove(com);						
					}
		   	if(cacheInputSubgraphComp.size()>0){
		   		inputSubComponent.getProperties().put("name", "subgraph");
		   		inputSubComponent.setComponentLabel("subgraph");
		   		inputSubComponent.inputPortSettings(inPort);	
		   		inputSubComponent.outputPortSettings(outPort);
		   		inputSubComponent.setParent(container); 
		   		container.addChild(inputSubComponent);
		   	}

		   	for (Component com : cacheOutSubgraphComp) {
		   		outSubComponent.setProperties(new LinkedHashMap<String,Object>());
							Link linkNew = new Link();
							linkNew.setSource(com);
							linkNew.setTarget(outSubComponent);
							linkNew.setSourceTerminal("out0");
							linkNew.setTargetTerminal("out"+outPort);
							com.connectOutput(linkNew);
							outSubComponent.connectInput(linkNew);
							outSubComponent.getProperties().put("type", "outputsubgraph");			
							outPort++;
							if(!cacheInputSubgraphComp.contains(com)){
							container.addChild((Component) com);
							bList.remove(com);
							}
					}
		   	if(cacheOutSubgraphComp.size()>0){
		   		outSubComponent.getProperties().put("name", "subgraph");
		   		outSubComponent.setComponentLabel("subgraph");
		   		outSubComponent.inputPortSettings(inPort);	
		   		outSubComponent.outputPortSettings(outPort);
		   		outSubComponent.setParent(container); 
		   		container.addChild(outSubComponent);
		   	}

			for (Object object : bList) {
				container.addChild((Component)object); 
			}
					
				   
	}
	
	
}

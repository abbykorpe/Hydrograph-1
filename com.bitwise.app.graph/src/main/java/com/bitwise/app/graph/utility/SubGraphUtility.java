package com.bitwise.app.graph.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;

import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.figure.ComponentFigure;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.graph.model.Link;

// TODO: Auto-generated Javadoc
/**
 * The Class SubGraphUtility.
 */
public class SubGraphUtility {
	
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
	public static void createDynamicInputPort(List< Link> inLinks,ComponentEditPart edComponentEditPart){
		for(int i=0;i<inLinks.size();i++){
			Component oldTarget=inLinks.get(i).getTarget();
			inLinks.get(i).getSource();
			Link link = inLinks.get(i);
			link.detachTarget();
			
	
			link.setTarget(edComponentEditPart.getCastedModel());
			link.setTargetTerminal("in"+i);
						
		/*	oldTarget.freeInputPort(link.getTargetTerminal());
			oldTarget.disconnectInput(link); */

			link.attachTarget();
			edComponentEditPart.getCastedModel().engageInputPort("in"+i);
			edComponentEditPart.refresh();  
		}

	}
	
	/**
	 * Creates the dynamic output port.
	 *
	 * @param outLinks the out links
	 * @param edComponentEditPart the ed component edit part
	 */
	public static void createDynamicOutputPort(List< Link> outLinks,ComponentEditPart edComponentEditPart){
		for(int i=0;i<outLinks.size();i++){
			Component oldSource=outLinks.get(i).getSource();
			Link link = outLinks.get(i);
			link.detachSource();
			link.getSource().freeOutputPort(link.getSourceTerminal());
			
			link.setSource(edComponentEditPart.getCastedModel());
			link.setSourceTerminal("out"+i);
			/*oldSource.freeOutputPort(link.getTargetTerminal());
			oldSource.disconnectOutput(link); */
			
			link.attachSource();
			edComponentEditPart.getCastedModel().engageOutputPort("out"+i);
			edComponentEditPart.refresh();  
		}

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
			edComponentEditPart.getCastedModel().getProperties().put("path", file.getFullPath().toOSString());

		edComponentEditPart.refresh();
	}
	
}

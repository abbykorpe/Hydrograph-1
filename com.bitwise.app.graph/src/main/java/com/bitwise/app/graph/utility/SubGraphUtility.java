package com.bitwise.app.graph.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;

import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.model.Container;

public class SubGraphUtility {
	
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

	private static ELTGraphicalEditor getCurrentEditor(){
		return (ELTGraphicalEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	}
	
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
}

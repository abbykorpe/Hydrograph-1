package com.bitwise.app.graph.editorfactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.model.Container;
import com.bitwise.app.logging.factory.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class FileEditorContiner.
 */
public class FileEditorContiner implements IGenrateContainerData {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FileEditorContiner.class);
	private final IFileEditorInput ifileEditorInput;
	private final ELTGraphicalEditor eltGraphicalEditorInstance;
	
	/**
	 * Instantiates a new file editor continer.
	 * 
	 * @param editorInput
	 *            the editor input
	 * @param eltGraphicalEditorInstance
	 *            the elt graphical editor instance
	 */
	public FileEditorContiner(IEditorInput editorInput, ELTGraphicalEditor eltGraphicalEditorInstance) {
		this.ifileEditorInput=(IFileEditorInput)editorInput;
		this.eltGraphicalEditorInstance=eltGraphicalEditorInstance;
	}

	@Override
	public Container getEditorInput() throws CoreException {
		logger.debug("getEditorInput - Setting IFileEditor input");
		IFile Ifile = ifileEditorInput.getFile();
		this.eltGraphicalEditorInstance.setPartName(Ifile.getName());
		return (Container) eltGraphicalEditorInstance.fromXMLToObject(Ifile.getContents());
	}

	@Override
	public void storeEditorInput() throws IOException, CoreException {
		logger.debug("storeEditorInput - Storing IFileEditor input into Ifile");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if(eltGraphicalEditorInstance.getContainer().isSubgraph())
			{
				Container container= eltGraphicalEditorInstance.getContainer();
				for(int i=0;i<container.getChildren().size();i++){
					if(Constants.INPUTSUBGRAPH.equalsIgnoreCase(container.getChildren().get(i).getComponentName())){
						container.getChildren().get(i).getProperties().put(Constants.SCHEMA_TO_PROPAGATE, new HashMap<>());
					}
				}
			}
			eltGraphicalEditorInstance.createOutputStream(out);
			IFile ifile = ifileEditorInput.getFile();
			ifile.setContents(new ByteArrayInputStream(out.toByteArray()),true, false, null);
			this.eltGraphicalEditorInstance.getCommandStack().markSaveLocation();
			this.eltGraphicalEditorInstance.genrateTargetXml(ifile,null);
			this.eltGraphicalEditorInstance.setDirty(false);
		
	}

	
	
}

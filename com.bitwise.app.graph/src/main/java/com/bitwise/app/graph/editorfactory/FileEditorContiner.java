package com.bitwise.app.graph.editorfactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.graph.editor.ETLGraphicalEditor;
import com.bitwise.app.graph.model.Container;

// TODO: Auto-generated Javadoc
/**
 * The Class FileEditorContiner.
 */
public class FileEditorContiner implements IGenrateContainerData {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FileEditorContiner.class);
	private final IFileEditorInput ifileEditorInput;
	private final ETLGraphicalEditor eltGraphicalEditorInstance;
	
	/**
	 * Instantiates a new file editor continer.
	 * 
	 * @param editorInput
	 *            the editor input
	 * @param eltGraphicalEditorInstance
	 *            the elt graphical editor instance
	 */
	public FileEditorContiner(IEditorInput editorInput, ETLGraphicalEditor eltGraphicalEditorInstance) {
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
			eltGraphicalEditorInstance.createOutputStream(out);
			IFile ifile = ifileEditorInput.getFile();
			ifile.setContents(new ByteArrayInputStream(out.toByteArray()),true, false, null);
			this.eltGraphicalEditorInstance.getCommandStack().markSaveLocation();
			this.eltGraphicalEditorInstance.genrateTargetXml(ifile);
			this.eltGraphicalEditorInstance.setDirty(false);
		
	}

	
	
}

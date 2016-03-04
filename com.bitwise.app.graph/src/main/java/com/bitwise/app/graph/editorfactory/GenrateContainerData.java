package com.bitwise.app.graph.editorfactory;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.editor.ELTGraphicalEditorInput;
import com.bitwise.app.graph.model.Container;

// TODO: Auto-generated Javadoc
/**
 * The Class GenrateContainerData.
 */
public class GenrateContainerData {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(GenrateContainerData.class);
	private IGenrateContainerData editorInput;
	
	/**
	 * Sets the editor input.
	 * 
	 * @param editorInput
	 *            the editor input
	 * @param eltGraphicalEditorInstance
	 *            the elt graphical editor instance
	 */
	public void setEditorInput(IEditorInput editorInput, ELTGraphicalEditor eltGraphicalEditorInstance) {
		logger.debug("setEditorInput - ");
		if((ELTGraphicalEditorInput.class).isAssignableFrom(editorInput.getClass()))
		{this.editorInput = new GraphicalEditorContiner(editorInput,eltGraphicalEditorInstance);}
		else{
			if((IFileEditorInput.class).isAssignableFrom(editorInput.getClass()))
				this.editorInput= new FileEditorContainer(editorInput,eltGraphicalEditorInstance);
			else
				if((FileStoreEditorInput.class).isAssignableFrom(editorInput.getClass()))
					this.editorInput=new FileStorageEditorContainer(editorInput,eltGraphicalEditorInstance);
		}
	}
	
	public Container getContainerData() throws CoreException, IOException
	{
		
		return editorInput.getEditorInput();
	}
	
	/**
	 * Store container data.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws CoreException
	 *             the core exception
	 */
	public void storeContainerData() throws IOException, CoreException
	{
		editorInput.storeEditorInput();
		
	}
	
}

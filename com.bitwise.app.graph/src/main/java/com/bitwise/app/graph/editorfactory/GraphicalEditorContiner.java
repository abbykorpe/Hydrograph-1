package com.bitwise.app.graph.editorfactory;

import org.eclipse.ui.IEditorInput;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.editor.ELTGraphicalEditorInput;
import com.bitwise.app.graph.model.Container;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphicalEditorContiner.
 */
public class GraphicalEditorContiner implements IGenrateContainerData {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(GraphicalEditorContiner.class);
	private final ELTGraphicalEditorInput graphicaleditorInput;
	private final ELTGraphicalEditor eltGraphicalEditorInstance;
	
	/**
	 * Instantiates a new graphical editor continer.
	 * 
	 * @param editorInput
	 *            the editor input
	 * @param eltGraphicalEditorInstance
	 *            the elt graphical editor instance
	 */
	public GraphicalEditorContiner(IEditorInput editorInput, ELTGraphicalEditor eltGraphicalEditorInstance) {
		this.graphicaleditorInput = (ELTGraphicalEditorInput) editorInput;
		this.eltGraphicalEditorInstance=eltGraphicalEditorInstance;
	}

	@Override
	public Container getEditorInput() {
		logger.debug("getEditorInput - Setting GraphicalEditor Input");
		this.eltGraphicalEditorInstance.setPartName(this.graphicaleditorInput.getName());
		return new Container();
	}

	@Override
	public void storeEditorInput() {
		logger.debug("storeEditorInput - Calling doSaveAs method");
		eltGraphicalEditorInstance.doSaveAs();
		
	}
}


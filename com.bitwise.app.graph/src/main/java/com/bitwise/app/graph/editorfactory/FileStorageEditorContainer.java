package com.bitwise.app.graph.editorfactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.graph.model.Container;

// TODO: Auto-generated Javadoc
/**
 * The Class FileStorageEditorContainer.
 */
public class FileStorageEditorContainer implements IGenrateContainerData {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FileStorageEditorContainer.class);
	private final FileStoreEditorInput fileStrorageEditorInput;
	private final ELTGraphicalEditor eltGraphicalEditorInstance;
	
	/**
	 * Instantiates a new file storage editor container.
	 * 
	 * @param editorInput
	 *            the editor input
	 * @param eltGraphicalEditorInstance
	 *            the elt graphical editor instance
	 */
	public FileStorageEditorContainer(IEditorInput editorInput,
			ELTGraphicalEditor eltGraphicalEditorInstance) {
		this.fileStrorageEditorInput = (FileStoreEditorInput) editorInput;
		this.eltGraphicalEditorInstance = eltGraphicalEditorInstance;
	}

	@Override
	public Container getEditorInput() throws IOException {
		logger.debug("storeEditorInput - Setting FileStrorageEditor Input into Ifile");
		Container con = null;
		File file = new File(fileStrorageEditorInput.getToolTipText());
		FileInputStream fs = new FileInputStream(file);
		con = (Container) this.eltGraphicalEditorInstance.fromXMLToObject(fs);
		this.eltGraphicalEditorInstance.setPartName(file.getName());
		fs.close();
		return con;
	}

	@Override
	public void storeEditorInput() throws IOException, CoreException {
		logger.debug("storeEditorInput - Storing FileStrorageEditor input into Ifile");
		File file = new File(fileStrorageEditorInput.getToolTipText());
		FileOutputStream fsout = new FileOutputStream(file);
		fsout.write(eltGraphicalEditorInstance.fromObjectToXML(
				eltGraphicalEditorInstance.getContainer()).getBytes());
		fsout.close();
		eltGraphicalEditorInstance.getCommandStack().markSaveLocation();
		eltGraphicalEditorInstance.setDirty(false);
		// genrateTargetXml(ifile);

	}

}

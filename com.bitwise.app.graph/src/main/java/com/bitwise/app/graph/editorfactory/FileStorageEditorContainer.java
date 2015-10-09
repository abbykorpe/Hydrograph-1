package com.bitwise.app.graph.editorfactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.graph.editor.ETLGraphicalEditor;
import com.bitwise.app.graph.model.Container;

public class FileStorageEditorContainer implements IGenrateContainerData {
	private FileStoreEditorInput fileStrorageEditorInput;
	private ETLGraphicalEditor eltGraphicalEditorInstance;
	Logger logger = new LogFactory(getClass().getName()).getLogger();
	
	public FileStorageEditorContainer(IEditorInput editorInput,
			ETLGraphicalEditor eltGraphicalEditorInstance) {
		this.fileStrorageEditorInput = (FileStoreEditorInput) editorInput;
		this.eltGraphicalEditorInstance = eltGraphicalEditorInstance;
	}

	@Override
	public Container getEditorInput() throws IOException {
		logger.debug("storeEditorInput - Setting FileStrorageEditor Input into Ifile");
		Container con = null;
		File file = new File(((FileStoreEditorInput) fileStrorageEditorInput).getToolTipText());
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
				eltGraphicalEditorInstance.getModel()).getBytes());
		fsout.close();
		eltGraphicalEditorInstance.getCommandStack().markSaveLocation();
		eltGraphicalEditorInstance.setDirty(false);
		// genrateTargetXml(ifile);

	}

}
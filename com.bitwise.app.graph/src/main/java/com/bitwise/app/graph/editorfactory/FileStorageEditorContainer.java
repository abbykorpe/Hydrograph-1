/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
package com.bitwise.app.graph.editorfactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;
import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.controller.ComponentEditPart;
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
		IFileStore fileStore = EFS.getLocalFileSystem().fromLocalFile(file);
		this.eltGraphicalEditorInstance.genrateTargetXml(null,fileStore,null);

	}

}

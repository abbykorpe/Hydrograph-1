package com.bitwise.app.graph.action.subgraph;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.slf4j.Logger;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.action.PasteAction;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.editor.ELTGraphicalEditor;
import com.bitwise.app.logging.factory.LogFactory;

/**
 * The Class SubGraphOpenAction use to open sub graph.
 * 
 * @author Bitwise
 */
public class SubGraphOpenAction extends SelectionAction{
	PasteAction pasteAction;
	ComponentEditPart edComponentEditPart;
	
	Logger logger = LogFactory.INSTANCE.getLogger(SubGraphOpenAction.class);
	/**
	 * Instantiates a new cut action.
	 * 
	 * @param part
	 *            the part
	 * @param action
	 *            the action
	 */
	public SubGraphOpenAction(IWorkbenchPart part, IAction action) {
		super(part);
		this.pasteAction = (PasteAction) action;
		setLazyEnablementCalculation(true);
	}

	@Override
	protected void init() {
		super.init();
		
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText("open"); 
		setId("open");
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		setEnabled(false);
	}



	/*
	 * Open the sub graph that saved in sub graph component path property.
	 */
	@Override  
	public void run() { 
		List<Object> selectedObjects =getSelectedObjects();
		if (selectedObjects != null || !selectedObjects.isEmpty()) {
			for(Object obj:selectedObjects)
			{
				if(obj instanceof ComponentEditPart)
				{
					if (((ComponentEditPart) obj).getCastedModel().getCategory().equalsIgnoreCase(Constants.SUBGRAPH_COMPONENT_CATEGORY)) {
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();						
						IPath jobFilePath=new Path((((ComponentEditPart) obj).getCastedModel()).getProperties().get("path").toString());
						IFile jobFile = ResourcesPlugin.getWorkspace().getRoot().getFile(jobFilePath);
						try {
							IDE.openEditor(page, jobFile, ELTGraphicalEditor.ID);
						} catch (PartInitException e) {
							logger.error("Unable to open subgraph");
						}
	
				}
			}
		}
		}
	}
	
	@Override
	protected boolean calculateEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
   }

package com.bitwise.app.graph.action.subgraph;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.action.PasteAction;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.utility.SubGraphUtility;

/**
 * The Class SubGraphUpdateAction use to update sub graph property.
 * 
 * @author Bitwise
 */
public class SubGraphUpdateAction extends SelectionAction {

	PasteAction pasteAction;

	ComponentEditPart componentEditPart;

	/**
	 * Instantiates a new cut action.
	 * 
	 * @param part
	 *            the part
	 * @param action
	 *            the action
	 */
	public SubGraphUpdateAction(IWorkbenchPart part, IAction action) {
		super(part);
		this.pasteAction = (PasteAction) action;
		setLazyEnablementCalculation(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#init()
	 */
	@Override
	protected void init() {
		super.init();

		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText(Constants.SUBGRAPH_UPDATE);
		setId(Constants.SUBGRAPH_UPDATE);
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		setEnabled(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		List<Object> selectedObjects = getSelectedObjects();
		if (selectedObjects != null && !selectedObjects.isEmpty() && selectedObjects.size() == 1) {
			for (Object obj : selectedObjects) {
				if (obj instanceof ComponentEditPart) {
					if (Constants.SUBGRAPH_COMPONENT.equalsIgnoreCase(((ComponentEditPart) obj).getCastedModel()
							.getComponentName()))
						return true;
				}
			}
		}
		return false;
	}

	/*
	 * Updates selected subgraph property from subgraph's job file.
	 */
	@Override
	public void run() {
		String filePath=null;
		Component selectedSubgraphComponent = null;
		componentEditPart=(ComponentEditPart) getSelectedObjects().get(0);
		if (getSelectedObjects() != null && !getSelectedObjects().isEmpty() && getSelectedObjects().size() == 1) {
			selectedSubgraphComponent = componentEditPart.getCastedModel();
			if (StringUtils.equals(Constants.SUBGRAPH_COMPONENT, selectedSubgraphComponent.getComponentName()) && selectedSubgraphComponent.getProperties().get(Constants.PATH_PROPERTY_NAME)!=null) {
				filePath=(String) selectedSubgraphComponent.getProperties().get(Constants.PATH_PROPERTY_NAME);
				SubGraphUtility subGraphUtility=new SubGraphUtility();
				subGraphUtility.updateSubgraphProperty(null,filePath, selectedSubgraphComponent);
				componentEditPart.changePortSettings();
				componentEditPart.refresh();
			}
		}
	}
}

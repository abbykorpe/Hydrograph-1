package com.bitwise.app.graph.action;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.common.util.XMLConfigUtil;
import com.bitwise.app.graph.Messages;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.propertywindow.ELTPropertyWindow;

public class ComponentHelpAction extends SelectionAction {

	public ComponentHelpAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}
	@Override
	protected void init() {
		super.init();
		setText(Messages.HELP);
		setId(Constants.HELP_ID);
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}

	@Override
	public void run() {
		super.run();
		IStructuredSelection currentSelectedComponent = (IStructuredSelection) getSelection();
		if (currentSelectedComponent.getFirstElement() instanceof ComponentEditPart) {
			String componentName = ((ComponentEditPart) currentSelectedComponent.getFirstElement()).getCastedModel()
					.getComponentName();
			String helpFilePath = XMLConfigUtil.INSTANCE.getComponent(componentName).getHelpFilePath();
			PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(helpFilePath);
		}
	}
	

}

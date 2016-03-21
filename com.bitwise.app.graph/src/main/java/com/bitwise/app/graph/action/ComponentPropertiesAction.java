package com.bitwise.app.graph.action;

import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.Messages;
import com.bitwise.app.graph.controller.ComponentEditPart;
import com.bitwise.app.graph.propertywindow.ELTPropertyWindow;

public class ComponentPropertiesAction extends SelectionAction{

	public ComponentPropertiesAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}

	@Override
	protected void init() {
		super.init();
		setText(Messages.PROPERTIES);
		setId(Constants.COMPONENT_PROPERTIES_ID);
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
			Object componentModel = ((ComponentEditPart) currentSelectedComponent.getFirstElement()).getCastedModel();
			ELTPropertyWindow eltPropertyWindow = new ELTPropertyWindow(componentModel);
			eltPropertyWindow.open();
		}
	}
}

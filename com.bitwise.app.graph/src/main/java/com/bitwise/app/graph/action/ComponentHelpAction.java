package com.bitwise.app.graph.action;

import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.bitwise.app.common.util.Constants;
import com.bitwise.app.graph.Messages;
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
		//setEnabled(false);
	}

	@Override
	protected boolean calculateEnabled() {
		return true;
	}
	@Override
	public void run() {
		super.run();
		System.out.println("********Here");
//		ELTPropertyWindow eltPropertyWindow = new ELTPropertyWindow(getModel());
//		eltPropertyWindow.open();
	}
	

}

package com.bitwise.app.menus.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bitwise.app.menus.aboutDialog.CustomAboutDialog;

public class AboutActionHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		 new CustomAboutDialog(HandlerUtil.getActiveShellChecked(event)).open();
		return null;
	}

}

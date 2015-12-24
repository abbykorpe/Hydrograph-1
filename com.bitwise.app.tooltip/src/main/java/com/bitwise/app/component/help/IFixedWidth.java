package com.bitwise.app.component.help;

import org.eclipse.ui.PlatformUI;

public class IFixedWidth implements ComponentHelp{

	@Override
	public void HelpContentofComponent() {
		PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(
				   "/com.bitwise.app.help/html/concepts/subtopic.html");
	}

}

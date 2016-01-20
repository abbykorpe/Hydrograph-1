package com.bitwise.app.component.help;

import org.eclipse.ui.PlatformUI;
/**
 *Creates help for IFixedWidth component
 * @author Bitwise
 *
 */
public class IFixedWidth implements ComponentHelp{
	/**
	 * open help dialog that displays the content of Input_File_Fixed_Width component
	 */
	@Override
	public void HelpContentofComponent() {
		PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(
				   "/com.bitwise.app.help/html/Components/Input_File_Fixed_Width.html");
	}

}

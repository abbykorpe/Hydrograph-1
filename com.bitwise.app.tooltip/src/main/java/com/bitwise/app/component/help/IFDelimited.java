package com.bitwise.app.component.help;

import org.eclipse.ui.PlatformUI;
/**
 *Creates help for IFDelimited component
 * @author Bitwise
 *
 */
public class IFDelimited implements ComponentHelp {
	/**
	 * open help dialog that displays the content of  Input_File_Delimited component
	 */
	@Override
	public void HelpContentofComponent() {
		PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(
				   "/com.bitwise.app.help/html/Components/Input_File_Delimited.html");
	}

}

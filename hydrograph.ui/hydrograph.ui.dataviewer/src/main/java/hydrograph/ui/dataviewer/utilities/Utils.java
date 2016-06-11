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

package hydrograph.ui.dataviewer.utilities;

import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.dataviewer.Activator;
import hydrograph.ui.dataviewer.constants.MessageBoxText;
import hydrograph.ui.dataviewer.constants.PreferenceConstants;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

/**
 * 
 * Utility class for Debug data viewer.
 * Class contains below functionality -
 * - Get debug file size
 * - Get tools Installation path
 * - Get debug file path
 * - Get data viewer page size
 * - Show message box with ok button
 * @author Bitwise
 *
 */
public class Utils {

	/**
	 * 
	 * Returns debug file size from preferences
	 * 
	 * @return {@link String}
	 */
	public static String getFileSize() {
		String fileSize = Platform.getPreferencesService().getString(Activator.PLUGIN_ID,
				PreferenceConstants.VIEW_DATA_FILE_SIZE, PreferenceConstants.DEFAULT_VIEW_DATA_FILE_SIZE, null);
		return fileSize;
	}

	/**
	 * 
	 * Return tools Installation path
	 * 
	 * @return {@link String}
	 */
	public static String getInstallationPath() {
		String installationPath = Platform.getInstallLocation().getURL().getPath();
		if (OSValidator.isWindows()) {
			if (installationPath.startsWith("/")) {
				installationPath = installationPath.replaceFirst("/", "").replace("/", "\\");
			}
		}
		return installationPath;

	}

	/**
	 * 
	 * Return debug file path from preferences
	 * 
	 * @return {@link String}
	 */
	public static String getDebugPath() {
		String debugPath = Platform.getPreferencesService().getString(Activator.PLUGIN_ID,
				PreferenceConstants.VIEW_DATA_TEMP_FILEPATH, getInstallationPath(), null);
		return debugPath;
	}

	/**
	 * 
	 * Returns data viewer page size
	 * 
	 * @return int
	 */
	public static int getDefaultPageSize() {
		String pageSize = Platform.getPreferencesService().getString(Activator.PLUGIN_ID,
				PreferenceConstants.VIEW_DATA_PAGE_SIZE, PreferenceConstants.DEFAULT_VIEW_DATA_PAGE_SIZE, null);
		return Integer.valueOf(pageSize);
	}

	private static int getMessageBoxIcon(String messageBoxType){
		if(StringUtils.equals(MessageBoxText.ERROR, messageBoxType)){
			return SWT.ICON_ERROR;
		}else if(StringUtils.equals(MessageBoxText.WARNING, messageBoxType)){
			return SWT.ICON_WARNING;
		}else{
			return SWT.ICON_INFORMATION;
		}
	}
	
	/**
	 * 
	 * Show message box with ok button
	 * 
	 * @param messageBoxTitle - Message box title
	 * @param message - Message to be displayed 
	 */
	public static void showMessage(String messageBoxTitle, String message) {
		int shellStyle= SWT.APPLICATION_MODAL | SWT.OK | getMessageBoxIcon(messageBoxTitle);
		MessageBox messageBox = new MessageBox(Display.getDefault().getActiveShell(),shellStyle);
		messageBox.setText(messageBoxTitle);
		messageBox.setMessage(message);
		messageBox.open();
	}
}

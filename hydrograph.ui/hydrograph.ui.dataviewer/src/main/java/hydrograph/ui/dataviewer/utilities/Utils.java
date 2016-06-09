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
import hydrograph.ui.dataviewer.constants.PreferenceConstants;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class Utils {
	public static String getFileSize(){
		IScopeContext context = new InstanceScope();
        IEclipsePreferences  eclipsePreferences = context.getNode("hydrograph.ui.dataviewer");
        //eclipsePreferences.getBoolean("recordslimit", true);
        String fileSize = Platform.getPreferencesService().getString("hydrograph.ui.dataviewer", PreferenceConstants.VIEW_DATA_FILE_SIZE, "100", null);
        return fileSize;
	}
	
	
	
	public static String getInstallationPath(){
		String installationPath= Platform.getInstallLocation().getURL().getPath();
		if(OSValidator.isWindows()){
			if(installationPath.startsWith("/")){
				installationPath = installationPath.replaceFirst("/", "").replace("/", "\\");
			}			 
		}
		return installationPath;
		
	}
	
	public static String getDebugPath(){
		IScopeContext context = new InstanceScope();
        IEclipsePreferences  eclipsePreferences = context.getNode("hydrograph.ui.dataviewer");
        String debugPath = Platform.getPreferencesService().getString("hydrograph.ui.dataviewer", PreferenceConstants.VIEW_DATA_TEMP_FILEPATH, getInstallationPath(), null);        
        return debugPath;
	}

	public static int getDefaultPageSize(){
		IScopeContext context = new InstanceScope();
        IEclipsePreferences  eclipsePreferences = context.getNode("hydrograph.ui.dataviewer");
        String pageSize = Platform.getPreferencesService().getString("hydrograph.ui.dataviewer",PreferenceConstants.VIEW_DATA_PAGE_SIZE , "100", null);        
        return Integer.valueOf(pageSize);
	}
}

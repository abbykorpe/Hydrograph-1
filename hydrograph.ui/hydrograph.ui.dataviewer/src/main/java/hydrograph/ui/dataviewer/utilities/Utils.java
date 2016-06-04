package hydrograph.ui.dataviewer.utilities;

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
        String fileSize = Platform.getPreferencesService().getString("hydrograph.ui.dataviewer", "memorysize", "100", null);
        return fileSize;
	}
	
	
	public static String getDebugPath(){
		IScopeContext context = new InstanceScope();
        IEclipsePreferences  eclipsePreferences = context.getNode("hydrograph.ui.dataviewer");
        String debugPath = Platform.getPreferencesService().getString("hydrograph.ui.dataviewer", "temppath", "", null);        
        return debugPath;
	}

	public static int getDefaultPageSize(){
		IScopeContext context = new InstanceScope();
        IEclipsePreferences  eclipsePreferences = context.getNode("hydrograph.ui.dataviewer");
        String pageSize = Platform.getPreferencesService().getString("hydrograph.ui.dataviewer",PreferenceConstants.RECORDSLIMIT , "200", null);        
        return Integer.valueOf(pageSize);
	}
}

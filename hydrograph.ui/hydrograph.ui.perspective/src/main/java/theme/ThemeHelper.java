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

package theme;
import hydrograph.ui.perspective.Activator;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.IThemeManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;


/**
 * The Class ThemeHelper.
 * 
 * @author Bitwise
 */
public class ThemeHelper {
	private static IThemeEngine engine = null;
	private static Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);

	public static IThemeEngine getEngine() {
		if (engine == null) {
			engine = getThemeEngine();
		}
		return engine;
	}

	private static IThemeEngine getThemeEngine() {
		
		
		BundleContext context = bundle.getBundleContext();

		ServiceReference ref = context.getServiceReference(IThemeManager.class
				.getName());
		IThemeManager manager = (IThemeManager) context.getService(ref);

		return manager.getEngineForDisplay(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow() == null ? Display.getCurrent()
				: PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getShell().getDisplay());
	}
	

}
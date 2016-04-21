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

 
package hydrograph.ui.graph.utility;

import hydrograph.ui.common.interfaces.parametergrid.DefaultGEFCanvas;

import org.eclipse.ui.PlatformUI;


/**
 * 
 * This class provides Canvas related utilites
 * @author Bitwise
 *
 */
public class CanvasUtils {
	/**
	 * 
	 * Returns instance of active canvas
	 * 
	 * @return {@link DefaultGEFCanvas}
	 */
	public static DefaultGEFCanvas getComponentCanvas() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
		else
			return null;
	}

	/**
	 * 
	 * Returns true if canvas is dirty otherwise false
	 * 
	 * @return boolean
	 */
	public static boolean isDirtyEditor() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().isDirty();
	}
}

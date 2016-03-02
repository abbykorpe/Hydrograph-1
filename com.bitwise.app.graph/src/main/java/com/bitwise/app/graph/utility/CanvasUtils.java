package com.bitwise.app.graph.utility;

import org.eclipse.ui.PlatformUI;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;

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

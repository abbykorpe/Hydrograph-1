package com.bitwise.app.graph.utility;

import org.eclipse.ui.PlatformUI;

import com.bitwise.app.common.interfaces.parametergrid.DefaultGEFCanvas;

/**
 * 
 * This class provides below functionalities
 * -Return instance of component canvas
 * -check if editor is dirty
 * @author Bitwise
 *
 */
public class CanvasUtils {
	public static DefaultGEFCanvas getComponentCanvas() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof DefaultGEFCanvas)
			return (DefaultGEFCanvas) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
		else
			return null;
	}

	public static boolean isDirtyEditor() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().isDirty();
	}
}

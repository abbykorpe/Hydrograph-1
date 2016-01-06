package com.bitwise.app.common.util;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

public enum ContributionItemManager {
	UndoRedoCustomToolBarManager {
		public void changeUndoRedoStatus(GraphicalViewer viewer) {
			boolean undoStatus = viewer.getEditDomain().getCommandStack()
					.canUndo();
			boolean redoStatus = viewer.getEditDomain().getCommandStack()
					.canRedo();

			WorkbenchWindow workbenchWindow = (WorkbenchWindow) PlatformUI
					.getWorkbench().getActiveWorkbenchWindow();
			Control[] controls = workbenchWindow.getCoolBarManager()
					.getControl().getChildren();

			changeToolControl(controls, undoStatus, redoStatus);
		}

		private void changeToolControl(Control[] controls, boolean undoStatus,
				boolean redoStatus) {
			for (Control control : controls) {
				if (control instanceof ToolBar) {
					if (((ToolBar) control).getItems().length > 5) {
						ToolItem[] toolItems = ((ToolBar) control).getItems();
						toolItems[4].setEnabled(undoStatus);
						toolItems[5].setEnabled(redoStatus);

					}

				}
			}

		}

	};

	public void changeUndoRedoStatus(GraphicalViewer viewer) {
	}

}

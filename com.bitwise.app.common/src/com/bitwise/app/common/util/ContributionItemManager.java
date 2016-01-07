package com.bitwise.app.common.util;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

public enum ContributionItemManager {
	UndoRedoDefaultBarManager, UndoRedoCustomToolBarManager {
		public void changeUndoRedoStatus(GraphicalViewer viewer) {
			undoStatus = viewer.getEditDomain().getCommandStack().canUndo();
			redoStatus = viewer.getEditDomain().getCommandStack().canRedo();

			workbenchWindow = (WorkbenchWindow) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			controls = workbenchWindow.getCoolBarManager().getControl()
					.getChildren();

			changeToolControl(controls, undoStatus, redoStatus);
		}

		private void changeToolControl(Control[] controls, boolean undoStatus,
				boolean redoStatus) {
			for (Control control : controls) {
				if (control instanceof ToolBar) {
					if (((ToolBar) control).getItemCount() > 5) {
						ToolItem[] toolItems = ((ToolBar) control).getItems();
						toolItems[4].setEnabled(undoStatus);
						toolItems[5].setEnabled(redoStatus);

					}

				}
			}

		}

	},
	UndoRedoCustomMenuBarManager {
		public void changeUndoRedoStatus(GraphicalViewer viewer) {
			undoStatus = viewer.getEditDomain().getCommandStack().canUndo();
			redoStatus = viewer.getEditDomain().getCommandStack().canRedo();
			workbenchWindow = (WorkbenchWindow) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			menuItems = workbenchWindow.getMenuBarManager().getMenu()
					.getItems();

			changeMenuControl(menuItems, undoStatus, redoStatus);

		}

		private void changeMenuControl(MenuItem[] menuItems,
				boolean undoStatus, boolean redoStatus) {

			for (MenuItem item : menuItems) {
				if ("menuitem {edit}".equalsIgnoreCase(item.toString())) {
					menu = item.getMenu();
					for (MenuItem menuItem : menu.getItems()) {
						if ("undo	ctrl+z".contains(menuItem.getText()
								.toLowerCase()))
							menuItem.setEnabled(undoStatus);
						if ("redo	ctrl+y".contains(menuItem.getText()
								.toLowerCase()))
							menuItem.setEnabled(redoStatus);

					}
				}

			}
		}

	};
	boolean undoStatus = false;
	boolean redoStatus = false;
	WorkbenchWindow workbenchWindow = (WorkbenchWindow) PlatformUI
			.getWorkbench().getActiveWorkbenchWindow();
	Control[] controls = workbenchWindow.getCoolBarManager().getControl()
			.getChildren();
	ToolItem[] toolItems = null;
	MenuItem[] menuItems = null;
	Menu menu = null;
	Set<String> menuItemsSet = null;

	public void changeUndoRedoStatus(GraphicalViewer viewer) {
		undoStatus = viewer.getEditDomain().getCommandStack().canUndo();
		redoStatus = viewer.getEditDomain().getCommandStack().canRedo();

		workbenchWindow = (WorkbenchWindow) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		controls = workbenchWindow.getCoolBarManager().getControl()
				.getChildren();

		changeToolControl(controls, undoStatus, redoStatus);

		menuItems = workbenchWindow.getMenuBarManager().getMenu().getItems();

		changeMenuControl(menuItems, undoStatus, redoStatus);

	}

	private void changeToolControl(Control[] controls, boolean undoStatus,
			boolean redoStatus) {
		for (Control control : controls) {
			if (control instanceof ToolBar) {
				if (((ToolBar) control).getItems().length > 5) {
					toolItems = ((ToolBar) control).getItems();
					toolItems[4].setEnabled(undoStatus);
					toolItems[5].setEnabled(redoStatus);
					toolItems[8].setEnabled(false);
					toolItems[9].setEnabled(false);
					toolItems[10].setEnabled(false);

				}

			}
		}

	}

	private void changeMenuControl(MenuItem[] menuItems, boolean undoStatus,
			boolean redoStatus) {
		menuItemsSet = ContributionItems.MenuBarItemsManageList
				.getRequiredItems();

		for (MenuItem item : menuItems) {
			if ("menuitem {edit}".equalsIgnoreCase(item.toString())) {
				menu = item.getMenu();
				for (MenuItem menuItem : menu.getItems()) {
					if (menuItemsSet.contains(menuItem.getText().toLowerCase())) {
						menuItem.setEnabled(false);
					} else {
						if ("undo	ctrl+z".contains(menuItem.getText()
								.toLowerCase()))
							menuItem.setEnabled(undoStatus);
						if ("redo	ctrl+y".contains(menuItem.getText()
								.toLowerCase()))
							menuItem.setEnabled(redoStatus);
					}
				}
			}

		}
	}

}

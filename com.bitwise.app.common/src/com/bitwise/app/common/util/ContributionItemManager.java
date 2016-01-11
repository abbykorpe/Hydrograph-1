package com.bitwise.app.common.util;

import java.util.ArrayList;
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
	UndoRedoDefaultBarManager {
		public void changeUndoRedoStatus(GraphicalViewer viewer) {
			UndoRedoDefaultBarManager.initializeViewerResource(viewer);
			changeToolControl(controls, undoStatus, redoStatus);
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

		private void changeMenuControl(MenuItem[] menuItems,
				boolean undoStatus, boolean redoStatus) {

			for (MenuItem item : menuItems) {
				if ("menuitem {edit}".equalsIgnoreCase(item.toString())) {
					menu = item.getMenu();
					for (MenuItem menuItem : menu.getItems()) {
						if (menuItemsList.contains(menuItem.getText()
								.toLowerCase())) {
							menuItem.setEnabled(false);
						} else {
							if ("undo	ctrl+z".contains(menuItem.getText()
									.toLowerCase())) {
								menuItem.setEnabled(undoStatus);
							}
							if ("redo	ctrl+y".contains(menuItem.getText()
									.toLowerCase())) {
								menuItem.setEnabled(redoStatus);
							}
						}
					}
				}

			}
		}
	},
	UndoRedoCustomToolBarManager {
		public void changeUndoRedoStatus(GraphicalViewer viewer) {
			UndoRedoCustomToolBarManager.initializeViewerResource(viewer);
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
			UndoRedoCustomMenuBarManager.initializeViewerResource(viewer);
			changeMenuControl(menuItems, undoStatus, redoStatus);
		}

		private void changeMenuControl(MenuItem[] menuItems,
				boolean undoStatus, boolean redoStatus) {

			for (MenuItem item : menuItems) {
				if ("menuitem {edit}".equalsIgnoreCase(item.toString())) {
					menu = item.getMenu();
					for (MenuItem menuItem : menu.getItems()) {
						if ("undo	ctrl+z".contains(menuItem.getText()
								.toLowerCase())) {
							menuItem.setEnabled(undoStatus);
						}
						if ("redo	ctrl+y".contains(menuItem.getText()
								.toLowerCase())) {
							menuItem.setEnabled(redoStatus);
						}

					}
				}

			}
		}

	},
	CUT {
		public void setEnable(boolean status) {
			CUT.setMenuItemStatus(menuItemsList.get(0), status);
			CUT.setToolItemStatus(8, status);
		}
	},
	COPY {
		public void setEnable(boolean status) {
			COPY.setMenuItemStatus(menuItemsList.get(1), status);
			COPY.setToolItemStatus(9, status);
		}
	},
	PASTE {
		public void setEnable(boolean status) {
			PASTE.setMenuItemStatus(menuItemsList.get(2), status);
			PASTE.setToolItemStatus(10, status);
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
	ArrayList<String> menuItemsList = null;

	private ContributionItemManager() {
		workbenchWindow = (WorkbenchWindow) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		controls = workbenchWindow.getCoolBarManager().getControl()
				.getChildren();
		menuItems = workbenchWindow.getMenuBarManager().getMenu().getItems();
		menuItemsList = ContributionItems.MenuBarItemsManageList
				.getRequiredItems();
	}

	public void changeUndoRedoStatus(GraphicalViewer viewer) {

	}

	public void setEnable(boolean status) {

	}

	private void initializeViewerResource(GraphicalViewer viewer) {
		undoStatus = viewer.getEditDomain().getCommandStack().canUndo();
		redoStatus = viewer.getEditDomain().getCommandStack().canRedo();
	}

	private void setMenuItemStatus(String menuItemName, boolean status) {
		for (MenuItem item : menuItems) {
			if ("menuitem {edit}".equalsIgnoreCase(item.toString())) {
				menu = item.getMenu();
				for (MenuItem menuItem : menu.getItems()) {
					if (menuItemName.contains(menuItem.getText().toLowerCase())) {
						menuItem.setEnabled(status);
					}
				}
			}

		}
	}

	private void setToolItemStatus(int toolItemNumber, boolean status) {
		for (Control control : controls) {
			if (control instanceof ToolBar) {
				if (((ToolBar) control).getItems().length > 5) {
					toolItems = ((ToolBar) control).getItems();
					toolItems[toolItemNumber].setEnabled(status);

				}

			}
		}
	}

}

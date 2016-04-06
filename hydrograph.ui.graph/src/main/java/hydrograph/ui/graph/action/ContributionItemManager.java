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

 
package hydrograph.ui.graph.action;


import java.util.ArrayList;

import org.eclipse.gef.GraphicalViewer;
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
				if (control instanceof ToolBar && (!control.isDisposed())) {
					if (((ToolBar) control).getItems().length > 5) {
						toolItems = ((ToolBar) control).getItems();
						toolItems[4].setEnabled(undoStatus);
						toolItems[5].setEnabled(redoStatus);
						toolItems[6].setEnabled(false);
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
				if (menuList.contains(item.toString().toLowerCase())) {
					menu = item.getMenu();
					for (MenuItem menuItem : menu.getItems()) {
						if (menuItemsList.contains(menuItem.getText()
								.toLowerCase())) {
							menuItem.setEnabled(false);
						} else {
							if (undoRedoItemsList.get(0).contains(menuItem.getText()
									.toLowerCase())) {
								menuItem.setEnabled(undoStatus);
							}
							if (undoRedoItemsList.get(1).contains(menuItem.getText()
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
				if (control instanceof ToolBar && (!control.isDisposed())) {
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
				if (menuList.contains(item.toString().toLowerCase())) {
					menu = item.getMenu();
					for (MenuItem menuItem : menu.getItems()) {
						if (undoRedoItemsList.get(0).contains(menuItem.getText()
								.toLowerCase())) {
							menuItem.setEnabled(undoStatus);
						}
						if (undoRedoItemsList.get(1).contains(menuItem.getText()
								.toLowerCase())) {
							menuItem.setEnabled(redoStatus);
						}

					}
				}

			}
		}

	},
	DELETE {
		public void setEnable(boolean status) {
			DELETE.setMenuItemStatus(menuItemsList.get(3), status);
			DELETE.setToolItemStatus(6, status);
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
	ArrayList<String> undoRedoItemsList = null;
	ArrayList<String> menuList = null;

	private ContributionItemManager() {
		workbenchWindow = (WorkbenchWindow) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		controls = workbenchWindow.getCoolBarManager().getControl()
				.getChildren();
		menuItems = workbenchWindow.getMenuBarManager().getMenu().getItems();
		menuItemsList = ContributionItems.MENU_BAR_ITEMS_LIST
				.getRequiredItems();
		undoRedoItemsList = ContributionItems.UNDO_REDO_ITEMS_LIST
				.getRequiredItems();	
		menuList = ContributionItems.MENU_LIST
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
			if ("menuitem {&edit}".equalsIgnoreCase(item.toString())) {
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
			if (control instanceof ToolBar && (!control.isDisposed())) {
				if (((ToolBar) control).getItems().length > 5) {
					toolItems = ((ToolBar) control).getItems();
					toolItems[toolItemNumber].setEnabled(status);

				}

			}
		}
	}

}

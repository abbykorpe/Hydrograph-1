package com.bitwise.app.common.util;

import java.util.ArrayList;
import java.util.HashSet;

public enum ContributionItems {
	MenuBarItemsManageList {
		public ArrayList<String> getRequiredItems() {
			ArrayList<String> requiredToolItems = new ArrayList<>();
			requiredToolItems.add("cut	ctrl+x");
			requiredToolItems.add("copy	ctrl+c");
			requiredToolItems.add("paste	ctrl+v");

			return requiredToolItems;
		}
	},
	UndoRedoItemsList {
		public ArrayList<String> getRequiredItems() {
			ArrayList<String> requiredToolItems = new ArrayList<>();
			requiredToolItems.add("undo	ctrl+z");
			requiredToolItems.add("redo	ctrl+y");
			return requiredToolItems;
		}
	},
	ToolBarItemManageList {
		public ArrayList<String> getRequiredItems() {
			ArrayList<String> requiredToolItems = new ArrayList<>();

			return requiredToolItems;
		}
	};

	public ArrayList<String> getRequiredItems() {
		ArrayList<String> requiredToolItems = new ArrayList<>();

		return requiredToolItems;
	}

}

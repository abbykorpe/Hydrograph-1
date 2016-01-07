package com.bitwise.app.common.util;

import java.util.HashSet;

public enum ContributionItems {
	MenuBarItemsManageList {
		public HashSet<String> getRequiredItems() {
			HashSet<String> requiredToolItems = new HashSet<>();
			requiredToolItems.add("cut	ctrl+x");
			requiredToolItems.add("copy	ctrl+c");
			requiredToolItems.add("paste	ctrl+v");

			return requiredToolItems;
		}
	},
	UndoRedoItemsList {
		public HashSet<String> getRequiredItems() {
			HashSet<String> requiredToolItems = new HashSet<>();
			requiredToolItems.add("undo	ctrl+z");
			requiredToolItems.add("redo	ctrl+y");
			return requiredToolItems;
		}
	},
	ToolBarItemManageList {
		public HashSet<String> getRequiredItems() {
			HashSet<String> requiredToolItems = new HashSet<>();

			return requiredToolItems;
		}
	};

	public HashSet<String> getRequiredItems() {
		HashSet<String> requiredToolItems = new HashSet<>();

		return requiredToolItems;
	}

}

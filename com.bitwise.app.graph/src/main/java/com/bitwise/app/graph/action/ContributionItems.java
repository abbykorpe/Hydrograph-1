package com.bitwise.app.graph.action;

import java.util.ArrayList;
import java.util.HashSet;

public enum ContributionItems {
	MENU_BAR_ITEMS_LIST {
		public ArrayList<String> getRequiredItems() {
			ArrayList<String> requiredToolItems = new ArrayList<>();
			requiredToolItems.add("cu&t	ctrl+x");
			requiredToolItems.add("&copy	ctrl+c");
			requiredToolItems.add("&paste	ctrl+v");
			requiredToolItems.add("&delete	delete");			
			requiredToolItems.add("select &all	ctrl+a");

			return requiredToolItems;
		}
	},
	UNDO_REDO_ITEMS_LIST {
		public ArrayList<String> getRequiredItems() {
			ArrayList<String> requiredToolItems = new ArrayList<>();
			requiredToolItems.add("&undo	ctrl+z");
			requiredToolItems.add("&redo	ctrl+y");
			return requiredToolItems;
		}
	},
	MENU_LIST{
		public ArrayList<String> getRequiredItems() {
			ArrayList<String> requiredToolItems = new ArrayList<>();
			requiredToolItems.add("menuitem {&edit}");
			return requiredToolItems;
		}
	},
	TOOL_BAR_ITEMS_LIST {
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

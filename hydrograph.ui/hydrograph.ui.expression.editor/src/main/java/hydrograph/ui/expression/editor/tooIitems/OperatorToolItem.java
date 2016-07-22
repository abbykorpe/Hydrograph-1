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

package hydrograph.ui.expression.editor.tooIitems;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class OperatorToolItem extends ToolItem {

	private static final String ITEM_TEXT = "Operators";
	private StyledText expressionEditor;
	private Menu menu;

	public OperatorToolItem(ToolBar parent, int style, StyledText expressionEditor) {
		super(parent, style);
		setText(ITEM_TEXT);
		this.expressionEditor = expressionEditor;

		DropdownSelectionListener listener = new DropdownSelectionListener(this);
		loadDropDownItems(listener);
		this.addSelectionListener(listener);

		getParent().pack();

	}

	private void loadDropDownItems(DropdownSelectionListener listener) {
		listener.add("ADD", "+");
		listener.add("SUBSTRACT", "-");
		listener.add("MULTIPLY", "*");
		listener.add("DIVIDE", "/");
		listener.add("AND", "&&");
		listener.add("OR", "||");
	}

	protected void checkSubclass() {
		// Allow subclassing 
	}

	class DropdownSelectionListener extends SelectionAdapter {
		private ToolItem toolItem;
		private Menu menu;

		public DropdownSelectionListener(OperatorToolItem operatorToolItem) {
			this.toolItem = operatorToolItem;
			menu = new Menu(toolItem.getParent().getShell());
		}

		public void add(String item, String replacement) {
			MenuItem menuItem = new MenuItem(menu, SWT.NONE);
			menuItem.setImage(new Image(
					null,
					"C:\\Users\\niting\\git\\Expression_Editor_Implementation\\Thesis\\hydrograph.ui\\hydrograph.ui.product\\icons\\16x16.bmp"));
			menuItem.setText(item);
			if (replacement != null) {
				menuItem.setData(replacement);
			} else
				menuItem.setData(item);
			menuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					MenuItem selected = (MenuItem) event.widget;
					expressionEditor.insert(" "+(String) selected.getData()+" ");
				}
			});
		}

		public void widgetSelected(SelectionEvent event) {
			if (event.detail == SWT.ARROW) {
				ToolItem item = (ToolItem) event.widget;
				Rectangle rect = item.getBounds();
				Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
				menu.setLocation(pt.x, pt.y + rect.height);
				menu.setVisible(true);
			}
		}
	}
}

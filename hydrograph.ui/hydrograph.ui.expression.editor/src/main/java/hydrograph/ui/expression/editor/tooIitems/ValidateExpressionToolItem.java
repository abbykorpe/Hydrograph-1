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

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ValidateExpressionToolItem extends ToolItem {

	private static final String ITEM_TEXT = "Validate";
	private StyledText expressionEditor;
	
	public ValidateExpressionToolItem(ToolBar parent, int style, StyledText expressionEditor) {
		super(parent, style);
		setText(ITEM_TEXT);
		this.expressionEditor=expressionEditor;
	}

	protected void checkSubclass() {
		// Allow subclassing
	}

}

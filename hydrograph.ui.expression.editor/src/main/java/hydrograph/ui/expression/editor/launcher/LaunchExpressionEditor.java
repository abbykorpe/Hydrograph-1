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

package hydrograph.ui.expression.editor.launcher;

import hydrograph.ui.expression.editor.Constants;
import hydrograph.ui.expression.editor.dialogs.ExpressionEditorDialog;
import hydrograph.ui.expression.editor.jar.util.BuildExpressionEditorDataSturcture;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

public class LaunchExpressionEditor {

	public String launchExpressionEditor(List<String> selectedInputFields){
		BuildExpressionEditorDataSturcture.INSTANCE.createClassRepo(Constants.JAR_FILE_NAME, Constants.PACKAGE_NAME);
		ExpressionEditorDialog expressionEditorDialog=new ExpressionEditorDialog(Display.getCurrent().getActiveShell(),tempData());
		
		expressionEditorDialog.open();
		return "";
	}

	// Delete 
	public static List<String> tempData() {
		List<String> inputFields=new ArrayList<String>();
		inputFields.add("Field1");
		inputFields.add("Field2");
		inputFields.add("Field3");
		inputFields.add("Field4");
		inputFields.add("Field5");
		inputFields.add("Field6");
		inputFields.add("Field7");
		return inputFields;
	}
	
}

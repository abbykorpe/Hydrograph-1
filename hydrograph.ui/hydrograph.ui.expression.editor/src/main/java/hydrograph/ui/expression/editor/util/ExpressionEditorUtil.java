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

package hydrograph.ui.expression.editor.util;

import hydrograph.ui.datastructure.expression.ExpressionEditorData;
import hydrograph.ui.expression.editor.Constants;
import hydrograph.ui.expression.editor.buttons.ValidateExpressionToolButton;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class ExpressionEditorUtil {

	public static final ExpressionEditorUtil INSTANCE = new ExpressionEditorUtil();

	public String[] getformatedData(String formatedString) {
		String[] fieldNameArray = null;
		if (formatedString != null) {
			fieldNameArray = formatedString.split(Constants.FIELD_SEPRATOR_FOR_DRAG_DROP);
		}
		return fieldNameArray;
	}

	private String formatDataToTransfer(TableItem[] selectedTableItems) {
		StringBuffer buffer = new StringBuffer();
		for (TableItem tableItem : selectedTableItems) {
			buffer.append(tableItem.getText() + Constants.FIELD_SEPRATOR_FOR_DRAG_DROP);
		}
		return buffer.toString();
	}

	public void addDragSupport(final Control widget) {
		DragSource dragSource = getDragSource(widget);
		dragSource.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) { 
				if (widget instanceof Table) {
					event.data = formatDataToTransfer(((Table) widget).getSelection());
				}
				if (widget instanceof List) {
					event.data = formatDataToTransfer(((List) widget).getSelection());
				}
			}
		});
	}

	public DragSource getDragSource(Control widget) {
		DragSource dragSource = new DragSource(widget, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		return dragSource;
	}

	private String formatDataToTransfer(String[] selection) {
		StringBuffer buffer = new StringBuffer();
		for (String field : selection) {
			buffer.append(field + Constants.FIELD_SEPRATOR_FOR_DRAG_DROP);
		}
		return buffer.toString();
	}
	
	public InputStream getPropertyFilePath(String fileName) throws IOException{
		URL location = FileLocator.find(Platform.getBundle(Constants.EXPRESSION_EDITOR_PLUGIN_ID), new Path(fileName), null);
		return location.openStream();
	}
	
	public void addFocusListenerToSearchTextBox(final Text searchTextBox) {
		searchTextBox.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				searchTextBox.setForeground(new Color(null,128,128,128));
				if(StringUtils.isBlank(searchTextBox.getText())){
					searchTextBox.setText(Constants.DEFAULT_SEARCH_TEXT);
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				searchTextBox.setForeground(new Color(null,0,0,0));
				if(StringUtils.equalsIgnoreCase(Constants.DEFAULT_SEARCH_TEXT, searchTextBox.getText())){
					searchTextBox.setText(Constants.EMPTY_STRING);
				}
				
			}
		});
	}

	public String lastString(String field, String seperator) {
		String result = Constants.EMPTY_STRING;
		if (StringUtils.isNotBlank(field)) {
			String[] strArray = StringUtils.split(field, seperator);
			result = strArray[strArray.length - 1];
			if (StringUtils.endsWith(result, Constants.SEMICOLON)) {
				result = StringUtils.remove(result, Constants.SEMICOLON);
			}
		}
		return result;
	}
	
	/**
	 * This method validates the given expression and updates the expression-editor's datasturcture accordingly
	 * 
	 * @param expressionText
	 * @param inputFields
	 * @param expressionEditorData
	 */
	public static void validateExpression(String expressionText,Map<String, Class<?>> inputFields,ExpressionEditorData expressionEditorData ) {
		DiagnosticCollector<JavaFileObject> diagnosticCollector = null;
		try {
			diagnosticCollector = ValidateExpressionToolButton
					.compileExpresion(expressionText,inputFields,expressionEditorData.getComponentName());
			if (diagnosticCollector != null && !diagnosticCollector.getDiagnostics().isEmpty()) {
				for (Diagnostic<?> diagnostic : diagnosticCollector.getDiagnostics()) {
					if (StringUtils.equals(diagnostic.getKind().name(), Diagnostic.Kind.ERROR.name())) {
						expressionEditorData.setValid(false);
						return;
					}
				}
			}
		} catch (JavaModelException | InvocationTargetException | ClassNotFoundException | MalformedURLException
				| IllegalAccessException | IllegalArgumentException e) {
			expressionEditorData.setValid(false);
			return;
		}
		expressionEditorData.setValid(true);
	}
}

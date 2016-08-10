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

package hydrograph.ui.expression.editor.evaluate;

import java.util.List;
import java.util.Map;

import hydrograph.ui.expression.editor.Constants;
import hydrograph.ui.expression.editor.buttons.ValidateExpressionToolButton;
import hydrograph.ui.expression.editor.dialogs.ExpressionEditorDialog;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;

public class EvaluateDialog extends Dialog {
	private static final String OUTPUT_COSOLE_ERROR_PREFIX = "Error\t\t: ";
	private static final String OUTPUT_CONSOLE_PREFIX = "Output\t\t: ";
	private String OUTPUT = "Expression's output";
	private Table table;
	private StyledText outputConsole;
	private String expressionText;
	private StyledText expressionEditor;
	private Composite previousExpressionEditorComposite;
	private Button evaluateButton;
	private EvaluateDialog evaluateDialog;
	private Table table_1;
	private Text searchTextBox;
	private EvalDialogFieldTable evalDialogFieldTable;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public EvaluateDialog(Shell parentShell,StyledText expressionEditor) {
		super(parentShell);
		setShellStyle(SWT.CLOSE);
		this.expressionEditor=expressionEditor;
		previousExpressionEditorComposite=expressionEditor.getParent();
		evaluateDialog=this;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite expressionEditorcomposite = new Composite(sashForm, SWT.BORDER);
		expressionEditor.setParent(expressionEditorcomposite);
		expressionEditorcomposite.setLayout(new GridLayout(1, false));
		
		Composite fieldTableComposite = new Composite(sashForm, SWT.BORDER);
		fieldTableComposite.setLayout(new GridLayout(1, false));
		
//		createSearchTextBox(fieldTableComposite);

		evalDialogFieldTable = new EvalDialogFieldTable().createFieldTable(fieldTableComposite,(Map<String, Class<?>>) expressionEditor.getData(ExpressionEditorDialog.FIELD_DATA_TYPE_MAP));
		
		
		
		Composite errorComposite = new Composite(sashForm, SWT.BORDER);
		errorComposite.setLayout(new GridLayout(1, false));
		
		createOutputConsole(errorComposite);
		sashForm.setWeights(new int[] {101, 263, 140});
//		sashForm.setWeights(new int[] {177, 232, 177, 122});
		
		showOutput(OUTPUT);
		
		
		return container;
	}

	private void createOutputConsole(Composite errorComposite) {
		outputConsole = new StyledText(errorComposite, SWT.BORDER|SWT.V_SCROLL|SWT.WRAP);
		outputConsole.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		outputConsole.setEditable(false);
	}

	private void createSearchTextBox(Composite fieldTableComposite) {
		searchTextBox = new Text(fieldTableComposite, SWT.BORDER);
		searchTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		searchTextBox.setForeground(new Color(null,128,128,128));
		searchTextBox.setText(Constants.DEFAULT_SEARCH_TEXT);
	}

	

	void showOutput(String output) {
		StringBuffer buffer=new StringBuffer();
		buffer.append(OUTPUT_CONSOLE_PREFIX+output);
		outputConsole.setText(buffer.toString());
		outputConsole.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	}

	void showError(String error) {
		StringBuffer buffer=new StringBuffer();
		buffer.append(OUTPUT_COSOLE_ERROR_PREFIX+error);
		outputConsole.setText(buffer.toString());
		outputConsole.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
	}
	
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	evaluateButton = createButton(parent, IDialogConstants.NO_ID, "Evaluate", false);
	addListenerToEvaluateButton();
	createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CLOSE_LABEL, false);
	}

	private void addListenerToEvaluateButton() {
		evaluateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				EvaluateExpression evaluateExpression=new EvaluateExpression(expressionEditor,outputConsole,evaluateDialog);
				if(true){
					 try {
						Object[] returnObject=evalDialogFieldTable.validateDataTypeValues();
						String object=evaluateExpression.invokeEvaluateFunctionFromJar(ValidateExpressionToolButton.getExpressionText(expressionEditor.getText()),(String[]) returnObject[0],(Object[])returnObject[1]);
						if(object!=null){
							showOutput(object);
					}
				} catch (InvalidDataTypeValueException invalidDataTypeValueException) {
					showError(invalidDataTypeValueException.getMessage());
				}
			}
		}});
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(554, 600);
	}
	
	@Override
	public boolean close() {
		expressionEditor.setParent(previousExpressionEditorComposite);
		expressionEditor.setSize(previousExpressionEditorComposite.getSize().x-18,previousExpressionEditorComposite.getSize().y-5);
		enableExpressionDialog();
		return super.close();
	}
	
	@Override
	public int open() {
		disableExpressionDialog();
		return super.open();
	}

	private void disableExpressionDialog() {
		ExpressionEditorDialog.CURRENT_INSTANCE.getContainerSashForm();
		ExpressionEditorDialog.CURRENT_INSTANCE.getUpperSashForm();
	}
	private void enableExpressionDialog() {
		ExpressionEditorDialog.CURRENT_INSTANCE.getContainerSashForm();
		ExpressionEditorDialog.CURRENT_INSTANCE.getUpperSashForm();
	}
}

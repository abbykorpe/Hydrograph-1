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

package hydrograph.ui.expression.editor.dialogs;

import hydrograph.ui.datastructure.expression.ExpressionEditorData;
import hydrograph.ui.expression.editor.Messages;
import hydrograph.ui.expression.editor.buttons.ValidateExpressionToolButton;
import hydrograph.ui.expression.editor.color.manager.JavaLineStyler;
import hydrograph.ui.expression.editor.composites.AvailableFieldsComposite;
import hydrograph.ui.expression.editor.composites.CategoriesComposite;
import hydrograph.ui.expression.editor.composites.DescriptionComposite;
import hydrograph.ui.expression.editor.composites.ExpressionEditorComposite;
import hydrograph.ui.expression.editor.composites.FunctionsComposite;
import hydrograph.ui.expression.editor.repo.ClassRepo;
import hydrograph.ui.expression.editor.util.ExpressionEditorUtil;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class ExpressionEditorDialog extends Dialog {

	public static final String FIELD_DATA_TYPE_MAP = "fieldMap";
	private StyledText expressionEditorTextBox;
	private AvailableFieldsComposite availableFieldsComposite;
	private ExpressionEditorComposite expressionEditorComposite;
	private CategoriesComposite categoriesComposite;
	private FunctionsComposite functionsComposite;
	private DescriptionComposite descriptionComposite;
	private List<String> selectedInputFields;
	private JavaLineStyler javaLineStyler;
	private String newExpressionText;
	private String oldExpressionText;
	private Map<String, Class<?>> fieldMap;
	private Composite container;
	public static ExpressionEditorDialog CURRENT_INSTANCE;
	private SashForm containerSashForm;
	private SashForm upperSashForm;
	private ExpressionEditorData expressionEditorData;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ExpressionEditorDialog(Shell parentShell, ExpressionEditorData expressionEditorData) {

		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.fieldMap = expressionEditorData.getSelectedInputFieldsForExpression();
		this.selectedInputFields = new ArrayList<>(fieldMap.keySet());
		javaLineStyler = new JavaLineStyler(selectedInputFields);
		this.oldExpressionText = expressionEditorData.getExpression();
		this.expressionEditorData = expressionEditorData;
		CURRENT_INSTANCE = this;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		container.getShell().setText(Messages.EXPRESSION_EDITOR_TITLE);

		containerSashForm = new SashForm(container, SWT.VERTICAL);
		containerSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite upperComposite = new Composite(containerSashForm, SWT.BORDER);
		upperComposite.setLayout(new GridLayout(1, false));

		upperSashForm = new SashForm(upperComposite, SWT.NONE);
		upperSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		availableFieldsComposite = new AvailableFieldsComposite(upperSashForm, SWT.NONE, expressionEditorTextBox,
				selectedInputFields);

		expressionEditorComposite = new ExpressionEditorComposite(upperSashForm, SWT.NONE, javaLineStyler);
		this.expressionEditorTextBox = expressionEditorComposite.getExpressionEditor();
		upperSashForm.setWeights(new int[] { 288, 576 });

		Composite composite = new Composite(containerSashForm, SWT.BORDER);
		composite.setLayout(new GridLayout(1, false));

		SashForm lowerSashForm = new SashForm(composite, SWT.NONE);
		lowerSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		categoriesComposite = new CategoriesComposite(lowerSashForm, SWT.NONE);
		functionsComposite = new FunctionsComposite(lowerSashForm, categoriesComposite, SWT.NONE);
		descriptionComposite = new DescriptionComposite(lowerSashForm, functionsComposite, categoriesComposite,
				SWT.NONE);

		containerSashForm.setWeights(new int[] { 1, 1 });

		intializeWidgets();
		return container;
	}

	private void intializeWidgets() {
		expressionEditorTextBox.setFocus();
		expressionEditorTextBox.setText(oldExpressionText);
		expressionEditorTextBox.setData(FIELD_DATA_TYPE_MAP, fieldMap);
		getShell().setMaximized(true);
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		container.getShell().layout(true, true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final Point newSize = container.getShell().computeSize(screenSize.width, screenSize.height, true);
		container.getShell().setSize(newSize);
		return newSize;
	}

	public boolean close() {
		if (preCloseActivity()) {
			ExpressionEditorUtil.validateExpression(expressionEditorTextBox.getText(),(Map<String, Class<?>>) expressionEditorTextBox
					.getData(ExpressionEditorDialog.FIELD_DATA_TYPE_MAP),expressionEditorData);
			ClassRepo.INSTANCE.flusRepo();
			return super.close();
		}
		return false;
	}

	@Override
	protected void okPressed() {
		oldExpressionText=expressionEditorTextBox.getText();
		newExpressionText=oldExpressionText;
		super.okPressed();
	}
	
	private boolean preCloseActivity() {
		if (!StringUtils.equals(oldExpressionText, expressionEditorTextBox.getText())) {
			if (confirmToExitWithoutSave()) {
				return true;
			}else
				return false;
		}else{
			return true;
		}
	}

	private boolean confirmToExitWithoutSave() {
		return MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Exiting expression editor",Messages.MESSAGE_TO_EXIT_WITHOUT_SAVE);
	}

	public String getExpressionText() {
		return newExpressionText;
	}

	public SashForm getUpperSashForm() {
		return upperSashForm;
	}

	public SashForm getContainerSashForm() {
		return containerSashForm;
	}

}

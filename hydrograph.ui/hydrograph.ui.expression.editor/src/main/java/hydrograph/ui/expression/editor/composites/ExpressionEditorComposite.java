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

package hydrograph.ui.expression.editor.composites;

import hydrograph.ui.expression.editor.buttons.EvaluateExpressionToolButton;
import hydrograph.ui.expression.editor.buttons.OperatorToolCombo;
import hydrograph.ui.expression.editor.buttons.ValidateExpressionToolButton;
import hydrograph.ui.expression.editor.buttons.WordWrapToolCheckButton;
import hydrograph.ui.expression.editor.color.manager.JavaLineStyler;
import hydrograph.ui.expression.editor.sourceviewer.SourceViewer;
import hydrograph.ui.expression.editor.util.ExpressionEditorUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;


public class ExpressionEditorComposite extends Composite {

	private SourceViewer viewer;
	private StyledText expressionEditor;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ExpressionEditorComposite(Composite parent, int style, JavaLineStyler javaLineStyler) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(10, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite.widthHint = 477;
		gd_composite.heightHint = 39;
		composite.setLayoutData(gd_composite);
		
		viewer = SourceViewer.createViewerWithVariables(this, SWT.BORDER | SWT.MULTI );
		
		expressionEditor = viewer.getTextWidget();
		intializeEditor(expressionEditor, javaLineStyler);
		expressionEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createToolBar(composite);
		
		
		
	}
	
	private void intializeEditor(StyledText expressionEditor, JavaLineStyler javaLineStyler) {
		expressionEditor.setWordWrap(false);
		expressionEditor.addLineStyleListener(javaLineStyler);
		expressionEditor.setFont(new Font(null,"Arial", 10, SWT.NORMAL));
		addDropSupport();
	}
	
	private void addDropSupport() {
		DropTarget dropTarget = new DropTarget(expressionEditor, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				for (String fieldName :ExpressionEditorUtil.INSTANCE.getformatedData((String) event.data)){
					expressionEditor.insert(SWT.SPACE+fieldName+SWT.SPACE);
				}
			}
		});
	}

	protected void createToolBar(Composite composite) {
		
		Button tltmWordWrap = new WordWrapToolCheckButton(composite, SWT.CHECK, expressionEditor);
		Combo tltmOperators = new OperatorToolCombo(composite, SWT.READ_ONLY, expressionEditor);
		Button tltmValidate = new ValidateExpressionToolButton(composite, SWT.NONE, expressionEditor);
		Button tltmTest = new EvaluateExpressionToolButton(composite, SWT.NONE, expressionEditor);
//		Button tltmExport = new ExportExpressionToolButton(composite, SWT.NONE, expressionEditor);
//		Button tltmImport = new ImportExpressionToolButton(composite, SWT.NONE, expressionEditor);
		
	}
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public StyledText getExpressionEditor() {
		return expressionEditor;
	}
	
}

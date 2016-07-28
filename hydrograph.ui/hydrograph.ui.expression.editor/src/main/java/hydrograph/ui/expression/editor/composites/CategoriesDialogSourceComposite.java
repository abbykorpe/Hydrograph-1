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

import hydrograph.ui.expression.editor.Constants;
import hydrograph.ui.expression.editor.PathConstant;
import hydrograph.ui.expression.editor.jar.util.BuildExpressionEditorDataSturcture;
import hydrograph.ui.expression.editor.util.ExpressionEditorUtil;
import hydrograph.ui.logging.factory.LogFactory;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.slf4j.Logger;

public class CategoriesDialogSourceComposite extends Composite {

	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(CategoriesDialogSourceComposite.class);
	private List sourcePackageList;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CategoriesDialogSourceComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		Composite composite = new Composite(this, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 0, 0);
		gd_composite.heightHint = 34;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(1, false));

		Combo comboJarList = new Combo(composite, SWT.READ_ONLY|SWT.V_SCROLL);
		
		comboJarList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 0, 0));

		sourcePackageList = new List(this, SWT.BORDER);
		GridData gd_packageList = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_packageList.heightHint = 254;
		sourcePackageList.setLayoutData(gd_packageList);

		loadComboJaraListFromBuildPath(comboJarList);
		addListnersToCombo(comboJarList);
		addDragSupport(sourcePackageList,comboJarList);
	}

	private void addDragSupport(final List sourcePackageList,final Combo comboJarList) {
		DragSource dragSource=ExpressionEditorUtil.INSTANCE.getDragSource(sourcePackageList);
		dragSource.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) { 
					event.data = formatDataToTransfer(sourcePackageList.getSelection());
				}

			private Object formatDataToTransfer(String[] selection) {
				StringBuffer buffer = new StringBuffer();
				for (String field : selection) {
					buffer.append(field +" -"+comboJarList.getItem(comboJarList.getSelectionIndex())+ Constants.FIELD_SEPRATOR_FOR_DRAG_DROP);
				}
				return buffer.toString();
			}
		});
	}

	private void addListnersToCombo(final Combo comboJarList) {
		comboJarList.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				sourcePackageList.removeAll();
				IPackageFragmentRoot iPackageFragmentRoot = (IPackageFragmentRoot) comboJarList.getData(String
						.valueOf(comboJarList.getSelectionIndex()));
				if (iPackageFragmentRoot != null) {
					try {
						for (IJavaElement iJavaElement : iPackageFragmentRoot.getChildren()) {
							{
								if (iJavaElement instanceof IPackageFragment) {
									IPackageFragment packageFragment = (IPackageFragment) iJavaElement;
									if (packageFragment.containsJavaResources()) {
										sourcePackageList.add(packageFragment.getElementName());
									}
								}
							}
						}
					} catch (JavaModelException javaModelException) {
						LOGGER.warn("Error occurred while fetching packages from "
								+ iPackageFragmentRoot.getElementName());
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	private void loadComboJaraListFromBuildPath(Combo comboJarList) {
		IProject iProject=BuildExpressionEditorDataSturcture.INSTANCE.getCurrentProject();
		try {
			IPackageFragmentRoot[] fragmentRoot = JavaCore.create(iProject).getAllPackageFragmentRoots();
			for (IPackageFragmentRoot iPackageFragmentRoot : fragmentRoot) {
				if (isJarPresentInLibFolder(iPackageFragmentRoot.getPath())|| iPackageFragmentRoot.getKind()==IPackageFragmentRoot.K_SOURCE) {
					comboJarList.add(iPackageFragmentRoot.getElementName());
					comboJarList.setData(String.valueOf(comboJarList.getItemCount() - 1), iPackageFragmentRoot);
				}
			}
		} catch (JavaModelException javaModelException) {
			LOGGER.error("Error occurred while loading engines-transform jar", javaModelException);
		}

	}

	private boolean isJarPresentInLibFolder(IPath path) {
		String currentProjectName = BuildExpressionEditorDataSturcture.INSTANCE.getCurrentProject().getName();
		if (StringUtils.equals(currentProjectName, path.segment(0))
				&& StringUtils.equals(PathConstant.PROJECT_LIB_FOLDER, path.segment(1)))
			return true;
		return false;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}

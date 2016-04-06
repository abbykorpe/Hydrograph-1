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

package hydrograph.ui.project.structure.wizard;

import hydrograph.ui.logging.factory.LogFactory;

import org.eclipse.e4.ui.css.swt.dom.WidgetElement;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.slf4j.Logger;


public class ProjectExplorerView extends CommonNavigator {
	private static final String PROJECT_EXPLORER_TEXT_CSS_ID="projectExplorerTreeColor";
	private static final String PROJECT_EXPLORER_TOOLBAR_CSS_ID="projectExplorerToolBarColor";
			
	Logger logger = LogFactory.INSTANCE.getLogger(ProjectExplorerView.class);
	@Override
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);	
		setCSSID(getCommonViewer().getTree(),PROJECT_EXPLORER_TEXT_CSS_ID);
		IActionBars actionBars= getViewSite().getActionBars();
		ToolBarManager toolbar=(ToolBarManager) actionBars.getToolBarManager();
		setCSSID(toolbar.getControl(),PROJECT_EXPLORER_TOOLBAR_CSS_ID);
	}

	private void setCSSID(Widget widget, String name) {
		WidgetElement.setID(widget, name);
		WidgetElement.getEngine(widget).applyStyles(widget, true);
		
	}

	public boolean isSaveAsAllowed() {
		IEditorPart editorPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		if (editorPart != null) {
			return editorPart.isSaveAsAllowed();
			}
		return false;
	}

	@Override
	public void doSaveAs() {
	
		IEditorPart editorPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		if (editorPart != null) {
			editorPart.doSaveAs();
			}
	}
}

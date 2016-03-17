package com.bitwise.app.project.structure.wizard;

import org.eclipse.e4.ui.css.swt.dom.WidgetElement;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.slf4j.Logger;

import com.bitwise.app.logging.factory.LogFactory;

public class ProjectExplorerView extends CommonNavigator {
	private final String PROJECT_EXPLORER_TEXT_CSS_ID="projectExplorerTreeColor";
	private final String PROJECT_EXPLORER_TOOLBAR_CSS_ID="projectExplorerToolBarColor";
			
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

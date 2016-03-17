package com.bitwise.app.perspective;

import org.eclipse.e4.ui.css.swt.dom.WidgetElement;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.bitwise.app.perspective.config.ELTPerspectiveConfig;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationWorkbenchWindowAdvisor.
 * 
 * @author Bitwise
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	private static final String CURRENT_THEME_ID = "com.bitwise.app.custom.ui.theme";
	private static final String CONSOLE_ID = "com.bitwise.app.project.structure.console.AcceleroConsole";
	private static final String CONSOLE_TOOLBAR_CSS_ID="consoleToolbarColor";
	/**
	 * Instantiates a new application workbench window advisor.
	 * 
	 * @param configurer
	 *            the configurer
	 */
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        ELTPerspectiveConfig eltPerspectiveConfig = new ELTPerspectiveConfig(configurer);
        
        eltPerspectiveConfig.setDefaultELTPrespectiveConfigurations();
        PlatformUI.getWorkbench().getThemeManager().setCurrentTheme(CURRENT_THEME_ID);
    }
    @Override
    public void createWindowContents(Shell shell) {
    	
    	super.createWindowContents(shell);

  
    }

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();

		getWindowConfigurer().getWindow().getShell().setMaximized(true);
		getWindowConfigurer().getWindow().getActivePage().resetPerspective();
		IViewPart consoleView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(CONSOLE_ID);
		ToolBarManager toobar = (ToolBarManager) consoleView.getViewSite().getActionBars().getToolBarManager();
		setCSSID(toobar.getControl(),CONSOLE_TOOLBAR_CSS_ID);
	}
	
	private void setCSSID(Widget widget, String name) {
		WidgetElement.setID(widget, name);
		WidgetElement.getEngine(widget).applyStyles(widget, true);
	}
    
    
    
}

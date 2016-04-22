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

package hydrograph.ui.perspective;

import hydrograph.ui.common.debug.service.IDebugService;
import hydrograph.ui.common.util.OSValidator;
import hydrograph.ui.perspective.config.ELTPerspectiveConfig;

import org.eclipse.e4.ui.css.swt.dom.WidgetElement;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;


// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationWorkbenchWindowAdvisor.
 * 
 * @author Bitwise
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	private static final String CURRENT_THEME_ID = "hydrograph.ui.custom.ui.theme";
	private static final String CONSOLE_ID = "hydrograph.ui.project.structure.console.AcceleroConsole";
	private static final String CONSOLE_TOOLBAR_CSS_ID="consoleToolbarColor";
	private static final String WARNING_TITLE="Warning";
	private static final String WARNING_MESSAGE="Current Dpi Setting is not equal to 100%.Recommended Dpi Setting for tool is 100%.If you will not set it you will face alignment issues.";
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
		if (OSValidator.isWindows()) {
			Point dpiCoordinates = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell().getDisplay()
					.getDPI();
			if (dpiCoordinates.x != 96 && dpiCoordinates.y != 96) {
				MessageBox messageBox = new MessageBox(new Shell(), SWT.OK);
				messageBox.setText(WARNING_TITLE);
				messageBox.setMessage(WARNING_MESSAGE);
				int response = messageBox.open();
				if (response == SWT.OK) {
				}
			}
		}
		
	}
	
	private void setCSSID(Widget widget, String name) {
		WidgetElement.setID(widget, name);
		WidgetElement.getEngine(widget).applyStyles(widget, true);
	}
    
	@Override
    public void dispose() {
		super.dispose();
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
			ServiceReference<IDebugService> serviceReference = (ServiceReference<IDebugService>) bundleContext.getServiceReference(IDebugService.class.getName());
			if(serviceReference != null){
				IDebugService debugService = (IDebugService)bundleContext.getService(serviceReference);
				debugService.deleteDebugFiles();
			}
    }
    
}

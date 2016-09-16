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
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.job.JobManager;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.perspective.config.ELTPerspectiveConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.css.swt.dom.WidgetElement;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
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
import org.slf4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationWorkbenchWindowAdvisor.
 * 
 * @author Bitwise
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	private static final String EXECUTION_TRACKING_SERVER_MAIN_CLASS = "hydrograph.server.execution.tracking.server.websocket.StartServer";
	private static final Logger logger = LogFactory.INSTANCE.getLogger(ApplicationWorkbenchWindowAdvisor.class);
	private static final String CURRENT_THEME_ID = "hydrograph.ui.custom.ui.theme"; //$NON-NLS-1$
	private static final String CONSOLE_ID = "hydrograph.ui.project.structure.console.HydrographConsole"; //$NON-NLS-1$
	private static final String CONSOLE_TOOLBAR_CSS_ID="consoleToolbarColor"; //$NON-NLS-1$
	private static final String WARNING_TITLE="Warning"; //$NON-NLS-1$
	private static final String WARNING_MESSAGE="Current DPI setting is other than 100%. Recommended 100%.\nUpdate it from Control Panel -> Display settings.\n\nNote: DPI setting other than 100% may cause alignment issues."; //$NON-NLS-1$
	private static final int DPI_COORDINATE=96;
	private static final String DRIVER_CLASS = " hydrograph.server.debug.service.DebugService";
	private static final String EXECUTION_TRACKING_PORT = "EXECUTION_TRACKING_PORT";
	private static final String EXECUTION_TRACK_SERVICE_JAR = "EXECUTION_TRACK_SERVICE_JAR";
	private static final String HYDROGRAPH_EXECUTION_TRACKING_SERVER_JAR = "config/service/hydrograph.server.execution.tracking.server.jar";
	
	public static final String SERVICE_JAR = "SERVICE_JAR";

	public static final String PORT_NUMBER = "PORT_NO";
	public static final String PROPERTY_FILE_PATH = "/service/hydrograph-service.properties";
	public static final String TRACKING_PROPERTY_FILE_PATH = "/service/socket-server.properties";
	
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


    // Code to execute launch new process to start execution-tracking service
    private void startExecutionTrackingServer() {
    	String path = Platform.getInstallLocation().getURL().getPath();
		if(StringUtils.isNotBlank(path) && StringUtils.startsWith(path, "/") && OSValidator.isWindows()){
			path = StringUtils.substring(path, 1);
		}
		
    	String command = "java -cp " + path + HYDROGRAPH_EXECUTION_TRACKING_SERVER_JAR	+ " " + EXECUTION_TRACKING_SERVER_MAIN_CLASS;
    	Runtime runtime = Runtime.getRuntime();
    	try {
			if (OSValidator.isWindows()) {
				String commandArray[] = new String[] { "cmd.exe", "/c", command };
				runtime.exec(commandArray);
			} 
			else{
				runtime.exec(command);
			}
		} catch (IOException ioException) {
			logger.error("Exception occurred while starting execution tracking server" + ioException);
		}
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
			if (dpiCoordinates.x != DPI_COORDINATE && dpiCoordinates.y != DPI_COORDINATE) {
				MessageBox messageBox = new MessageBox(new Shell(),SWT.ICON_WARNING| SWT.OK);
				messageBox.setText(WARNING_TITLE);
				messageBox.setMessage(WARNING_MESSAGE);
				int response = messageBox.open();
				if (response == SWT.OK) {
				}
			}
		}
		try {
			serviceInitiator();
		} catch (IOException exception) {
			logger.error("Failure in IO", exception);
		}
		
		startExecutionTrackingServer();
	}
	
	private void serviceInitiator() throws IOException{
		if(OSValidator.isWindows()){		
			String command = "java -cp " + getInstallationConfigPath().trim() + ";" +getInstallationPath() + DRIVER_CLASS;
			ProcessBuilder builder = new ProcessBuilder(new String[]{"cmd", "/c", command});
			builder.start();
		}
		else if(OSValidator.isMac()){
			logger.debug("On Mac Operating System....");
			String command="java -cp " + getInstallationConfigPath().trim() + ":" + getInstallationPath() + DRIVER_CLASS;
			System.out.println("Rest jar Path:::"+command);
			logger.debug("command{}", command);
            ProcessBuilder builder = new ProcessBuilder(new String[]{"bash", "-c", command});
            builder.start();

		}
		else if(OSValidator.isUnix()){
			new ProcessBuilder(new String[]{"java", "-jar", getInstallationPath()}).start();
		}
		else if(OSValidator.isSolaris()){
		}
	}
	
	private void setCSSID(Widget widget, String name) {
		WidgetElement.setID(widget, name);
		WidgetElement.getEngine(widget).applyStyles(widget, true);
	}
    
	
	@Override
	public boolean preWindowShellClose() {
		for (Entry<String, Job> entry : JobManager.INSTANCE.getRunningJobsMap().entrySet()) {
			if (entry.getValue().isRemoteMode()) {
				MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_QUESTION | SWT.YES
						| SWT.CANCEL | SWT.NO);
				box.setMessage(Messages.TOOL_EXT_MESSAGE);
				box.setText(Messages.TOOL_EXIT_MESSAGE_BOX_TITLE);
				int returCode = box.open();
				if (returCode == SWT.YES) {
					JobManager.INSTANCE.killALLRemoteProcess();
				}else 
					return false;
				break;
			}
		}
		return true;
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
		
		try {
			killPortProcess();
			killTrackingPortProcess();
		} catch (IOException e) {
			logger.debug("Socket is not closed.");
		}
    }
	
	public void killPortProcess() throws IOException{
		if(OSValidator.isWindows()){
			String pid =getServicePortPID(PROPERTY_FILE_PATH,PORT_NUMBER,SERVICE_JAR);
			if(StringUtils.isNotBlank(pid)){
				int portPID = Integer.parseInt(pid);
				ProcessBuilder builder = new ProcessBuilder(new String[]{"cmd", "/c", "taskkill /F /PID " + portPID});
				builder.start();
			}
		}
		else if(OSValidator.isMac()){
			int portNumber = Integer.parseInt(getServicePortFromPropFile(PROPERTY_FILE_PATH,PORT_NUMBER,SERVICE_JAR));
			ProcessBuilder builder = new ProcessBuilder(new String[]{"bash", "-c", "lsof -P | grep :" + portNumber + " | awk '{print $2}' | xargs kill -9"});
			builder.start();
		}
	}
	
	/**
	 * This function will be return process ID which running on defined port
	 *
	 */
	public String getServicePortPID(String propertyFile,String portNo,String serviceJar) throws IOException{
		int portNumber = Integer.parseInt(getServicePortFromPropFile(propertyFile,portNo,serviceJar));
		if(OSValidator.isWindows()){
			ProcessBuilder builder = new ProcessBuilder(new String[]{"cmd", "/c" ,"netstat -a -o -n |findstr :"+portNumber});
			Process process =builder.start();
			InputStream inputStream = process.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String str = bufferedReader.readLine();
			str=StringUtils.substringAfter(str, "LISTENING");
			str=StringUtils.trim(str);
			return str;
		}
		return "";
	}
	
	/**
	 * This function used to return Rest Service port Number which running on local
	 *
	 */
	public String getServicePortFromPropFile(String propertyFile,String portNo,String serviceJar){
		String portNumber = null;
		try {
			FileReader fileReader = new FileReader(XMLConfigUtil.CONFIG_FILES_PATH + propertyFile);
			Properties properties = new Properties();
			properties.load(fileReader);
			if(StringUtils.isNotBlank(properties.getProperty(serviceJar))){
				portNumber = properties.getProperty(portNo);
			}
		} catch (FileNotFoundException e) {
			logger.error("File not exists", e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		return portNumber;
	}
	
	private String getRestServiceJar(){
		String restServiceJar = null;
		try {
			FileReader fileReader = new FileReader(XMLConfigUtil.CONFIG_FILES_PATH + PROPERTY_FILE_PATH);
			Properties properties = new Properties();
			properties.load(fileReader);
			if(StringUtils.isNotBlank(properties.getProperty(SERVICE_JAR))){
				restServiceJar = properties.getProperty(SERVICE_JAR);
			}
		} catch (FileNotFoundException e) {
			logger.error("File not exists", e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return restServiceJar;
	}
	
	private  String getInstallationPath()  {
		String path = Platform.getInstallLocation().getURL().getPath();
		String restServiceJar = getRestServiceJar();
		if(StringUtils.isNotBlank(path) && StringUtils.startsWith(path, "/") && OSValidator.isWindows()){
			path = StringUtils.substring(path, 1);
		}
		return path + "config/service/" + restServiceJar;
	}
	
	private static String getInstallationConfigPath()  {
		String path = Platform.getInstallLocation().getURL().getPath();
		if(StringUtils.isNotBlank(path) && StringUtils.startsWith(path, "/") && OSValidator.isWindows()){
			path = StringUtils.substring(path, 1);
		}
		
		return path + "config/service/config" ;
	}
	
	public void killTrackingPortProcess() throws IOException{
		if(OSValidator.isWindows()){
			String pid =getServicePortPID(TRACKING_PROPERTY_FILE_PATH,EXECUTION_TRACKING_PORT,EXECUTION_TRACK_SERVICE_JAR);
			if(StringUtils.isNotBlank(pid)){
				int portPID = Integer.parseInt(pid);
				ProcessBuilder builder = new ProcessBuilder(new String[]{"cmd", "/c", "taskkill /F /PID " + portPID});
				builder.start();
			}
		}
		else if(OSValidator.isMac()){
			int portNumber = Integer.parseInt(getServicePortFromPropFile(TRACKING_PROPERTY_FILE_PATH,EXECUTION_TRACKING_PORT,EXECUTION_TRACK_SERVICE_JAR));
			ProcessBuilder builder = new ProcessBuilder(new String[]{"bash", "-c", "lsof -P | grep :" + portNumber + " | awk '{print $2}' | xargs kill -9"});
			builder.start();
		}
	}
	
}

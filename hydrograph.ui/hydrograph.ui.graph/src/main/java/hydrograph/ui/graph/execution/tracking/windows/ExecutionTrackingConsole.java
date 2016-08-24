package hydrograph.ui.graph.execution.tracking.windows;


import hydrograph.ui.common.util.ImagePathConstant;
import hydrograph.ui.common.util.XMLConfigUtil;
import hydrograph.ui.graph.execution.tracking.constants.MenuConstants;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.execution.tracking.handlers.ActionFactory;
import hydrograph.ui.graph.execution.tracking.handlers.ClearConsoleAction;
import hydrograph.ui.graph.job.JobManager;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * The Class ExecutionTrackingConsole use to display execution tracking log.
 * @author Bitwise
 */
public class ExecutionTrackingConsole extends ApplicationWindow {
	
	/** The styled text. */
	private StyledText styledText;
	
	/** The console name. */
	private String consoleName;
	
	/** The action factory. */
	private ActionFactory actionFactory;
	
	/** The status line manager. */
	private StatusLineManager statusLineManager;
	
	/**
	 * Create the application window,.
	 *
	 * @param consoleName the console name
	 */
	public ExecutionTrackingConsole(String consoleName) {
		super(null);
		addCoolBar(SWT.FLAT);
		addMenuBar();
		addStatusLine();
		this.consoleName = consoleName;
	}
	

	/**
	 * Create contents of the application window.
	 *
	 * @param parent the parent
	 * @return the control
	 */
	@Override
	protected Control createContents(Composite parent) {
		getShell().setText("Execution tracking console - " + consoleName);
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		{
			styledText = new StyledText(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			styledText.setEditable(false);
		}

		statusLineManager.setMessage("Waiting for tracking status from server. Please wait!");
		return container;
	}


	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager(MenuConstants.MENU);
		menuManager.setVisible(true);

		createWindowMenu(menuManager);
		return menuManager;
	}
	
	/**
	 * Creates the window menu.
	 *
	 * @param menuManager the menu manager
	 */
	private void createWindowMenu(MenuManager menuManager) {
		MenuManager windowMenu = createMenu(menuManager, MenuConstants.WINDOW);
		menuManager.add(windowMenu);
		windowMenu.setVisible(true);

		if (actionFactory == null) {
			actionFactory = new ActionFactory(this);
		}
		
		windowMenu.add(actionFactory.getAction(ClearConsoleAction.class.getName()));
	}

	/**
	 * Creates the menu.
	 *
	 * @param menuManager the menu manager
	 * @param menuName the menu name
	 * @return the menu manager
	 */
	private MenuManager createMenu(MenuManager menuManager, String menuName) {
		MenuManager menu = new MenuManager(menuName);
		menuManager.add(menu);
		menuManager.setVisible(true);
		return menu;
	}
	
	/**
	 * Create the coolbar manager.
	 *
	 * @param style the style
	 * @return the coolbar manager
	 */
	@Override
	protected CoolBarManager createCoolBarManager(int style) {
		
		CoolBarManager coolBarManager = new CoolBarManager(style);

		actionFactory = new ActionFactory(this);

		ToolBarManager toolBarManager = new ToolBarManager();
		coolBarManager.add(toolBarManager);
		addtoolbarAction(toolBarManager, (XMLConfigUtil.CONFIG_FILES_PATH + ImagePathConstant.CLEAR_EXEC_TRACKING_CONSOLE),
				actionFactory.getAction(ClearConsoleAction.class.getName()));
		return coolBarManager;
	}
	
	
	/**
	 * Addtoolbar action.
	 *
	 * @param toolBarManager the tool bar manager
	 * @param imagePath the image path
	 * @param action the action
	 */
	private void addtoolbarAction(ToolBarManager toolBarManager, final String imagePath, Action action) {

		ImageDescriptor exportImageDescriptor = new ImageDescriptor() {
			@Override
			public ImageData getImageData() {
				ImageData imageData = new ImageData(imagePath);
				return imageData;
			}
		};
		action.setImageDescriptor(exportImageDescriptor);
		toolBarManager.add(action);
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Configure the shell.
	 *
	 * @param newShell the new shell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 *
	 * @return the initial size
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(401, 300);
	}
	
	/**
	 * Sets the status.
	 *
	 * @param executionStatus the execution status
	 * @param stringBuilder the string builder
	 */
	public void setStatus(ExecutionStatus executionStatus, StringBuilder stringBuilder){
		
		//StringBuilder stringBuilder = new StringBuilder();
		/*if(executionStatus==null){
			return;
		}*/
		
		statusLineManager.setMessage("");
		
		/*stringBuilder.append("Job ID " + executionStatus.getJobId() + "\n");
		return new Point(801, 443);
	}
	
	public void setStatus(ExecutionStatus executionStatus){
		
		StringBuilder stringBuilder = new StringBuilder();
		if(executionStatus==null){
			return;
		}
		
		statusLineManager.setMessage("");
		stringBuilder.append("Job ID " + executionStatus.getJobId() + "\n");
		stringBuilder.append("Job Type: " + executionStatus.getType() + "\n");
		stringBuilder.append("Job Status: " + executionStatus.getJobStatus() + "\n");
		
		for(ComponentStatus componentStatus : executionStatus.getComponentStatus()){
			stringBuilder.append("-------------------------------------\n");
			stringBuilder.append("Component ID: " + componentStatus.getComponentId() + "\n");
			stringBuilder.append("Component Name: " + componentStatus.getComponentName() + "\n");
			stringBuilder.append("Current Status: " + componentStatus.getCurrentStatus() + "\n");
			stringBuilder.append("Processed record count: " + componentStatus.getProcessedRecordCount().toString() + "\n");
		}*/
		
		//stringBuilder.append("============================================================================\n");		
		if(styledText!=null && !styledText.isDisposed()){
			styledText.append(stringBuilder.toString());
			styledText.setTopIndex(styledText.getLineCount() - 1);
		}
	}
	
	/**
	 * Clear console.
	 */
	public void clearConsole(){
		if(styledText!=null && !styledText.isDisposed()){
			styledText.setText("");
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.ApplicationWindow#close()
	 */
	@Override
	public boolean close() {
		JobManager.INSTANCE.getExecutionTrackingConsoles().remove(consoleName.replace(".", "_"));
		return super.close();
	}
}
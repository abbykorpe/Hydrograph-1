package hydrograph.ui.graph.handler;

import hydrograph.ui.graph.job.RunStopButtonCommunicator;
import hydrograph.ui.propertywindow.runconfig.RunConfigDialog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

public class JobHandler extends AbstractHandler {

	public JobHandler() {
		RunStopButtonCommunicator.RunJob.setHandler(this);
	} 
	
	/**
	 * Enable disable run button
	 * 
	 * @param enable
	 */
	public void setRunJobEnabled(boolean enable) {
		setBaseEnabled(enable);
	}

	/**
	 * Enable disable debug button.
	 *
	 * @param enable the new debug job enabled
	 */
	public void setDebugJobEnabled(boolean enable){
		setBaseEnabled(enable);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		RunConfigDialog runConfigDialog = getRunConfiguration();
		if(runConfigDialog.isDebug()){
			new DebugHandler().execute(runConfigDialog);
		}
		else{
			new RunJobHandler().execute(runConfigDialog);
		}
		return null;
	}

	private RunConfigDialog getRunConfiguration() {
		RunConfigDialog runConfigDialog = new RunConfigDialog(Display.getDefault().getActiveShell());
		runConfigDialog.open();
		return runConfigDialog;
	}

}

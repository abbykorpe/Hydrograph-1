package hydrograph.ui.graph.execution.tracking.handlers;

import org.eclipse.jface.action.Action;

import hydrograph.ui.graph.execution.tracking.windows.ExecutionTrackingConsole;

/**
 * @author Bitwise
 *
 */
public class ScrolLockAction extends Action {
	
	/** The execution tracking console. */
	private ExecutionTrackingConsole executionTrackingConsole;
	
	/** The Constant LABEL. */
	private static final String LABEL="&Scroll Lock";
	
	
	/**
	 * Instantiates scroll lock action.
	 * @param executionTrackingConsole
	 */
	public ScrolLockAction(ExecutionTrackingConsole executionTrackingConsole) {
		super(LABEL);
		this.executionTrackingConsole = executionTrackingConsole;
		setChecked(false);
	}
	
	/* (non-Javadoc))
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		if(isChecked()){
			setChecked(true);
			executionTrackingConsole.lockScrollBar(true);
		}else{
			setChecked(false);
			executionTrackingConsole.lockScrollBar(false);
		}
		
		super.run();
		
	}
}

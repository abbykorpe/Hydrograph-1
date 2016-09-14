package hydrograph.ui.graph.action.debug;

import hydrograph.ui.common.util.Constants;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

public class PriorViewDataAction extends SelectionAction{

	public PriorViewDataAction(IWorkbenchPart part) {
		super(part);
	}

	@Override
	protected void init() {
		super.init();
		setText("Prior ViewData");
		setId(Constants.PRIOR_VIEW_DATA_ID);
		setEnabled(true);
	}
	
	@Override
	protected boolean calculateEnabled() {
		return false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
}

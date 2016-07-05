package hydrograph.ui.dataviewer.actions;

import hydrograph.ui.dataviewer.window.DebugDataViewer;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;

public class AutoExpandColumns extends Action {
	private static final String LABEL="Auto E&xapand Columns@Ctrl+X";
	private DebugDataViewer debugDataViewer;
	
	public AutoExpandColumns(DebugDataViewer debugDataViewer){
		super(LABEL);
		setAccelerator(SWT.MOD1 | 'x');
		this.debugDataViewer = debugDataViewer;
	}
	
	
	@Override
	public void run() {
		for(TableColumn tableColumn : this.debugDataViewer.getTableViewer().getTable().getColumns()){
			tableColumn.pack();
		}
		
	}
}

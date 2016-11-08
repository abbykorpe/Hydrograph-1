package hydrograph.ui.graph.execution.tracking.replay;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ReplayComponentDialog extends Dialog{
	private Text text;
	private List<String> extraComponentList;
	private List<String> missedComponentList;
	
	private int extraCompcount = 1;
	private int missedCompcount = 1;

	public ReplayComponentDialog(Shell parentShell, List<String> extraComponentList, List<String> missedComponentList) {
		super(parentShell);
		setShellStyle(SWT.TITLE | SWT.WRAP | SWT.APPLICATION_MODAL | SWT.RESIZE);
		this.extraComponentList = extraComponentList;
		this.missedComponentList = missedComponentList;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText("Component Details");
		container.setLayout(new GridLayout(1, false));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_scrolledComposite.heightHint = 289;
		gd_scrolledComposite.widthHint = 571;
		scrolledComposite.setLayoutData(gd_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		text = new Text(scrolledComposite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		StringBuilder stringBuilder = new StringBuilder();
		
		if(extraComponentList != null && extraComponentList.size() > 0){
			stringBuilder.append("Below new components and ports were introduced for which tracking is not present :" + "\n");
			extraComponentList.forEach(componentName -> {
				stringBuilder.append(extraCompcount + ". " + componentName + "\n");
				extraCompcount++;
			});
		}
		
		if(missedComponentList != null && missedComponentList.size() > 0 && !missedComponentList.isEmpty()){
			stringBuilder.append("Below missed components and ports were introduced for which tracking is not present :" + "\n");
			missedComponentList.forEach(componentName -> {
				stringBuilder.append(missedCompcount + ". " + componentName + "\n");
				missedCompcount++;
			});
		}
		
		text.setText(stringBuilder.toString());
		
		scrolledComposite.setContent(text);
		
		
		return super.createDialogArea(parent);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button okButton = createButton(parent, IDialogConstants.OK_ID, "Ok", false);
		Button closeButton = createButton(parent, IDialogConstants.CANCEL_ID, "Close", false);		
		parent.dispose();
	}
}

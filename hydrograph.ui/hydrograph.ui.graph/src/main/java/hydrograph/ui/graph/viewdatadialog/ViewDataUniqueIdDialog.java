package hydrograph.ui.graph.viewdatadialog;

import hydrograph.ui.graph.job.Job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ViewDataUniqueIdDialog extends Dialog{
	
	private List<Job> jobDetails;
	private String selectedUniqueJobId;
	private Button button1;

	public ViewDataUniqueIdDialog(Shell parentShell, List<Job> jobDetails) {
		super(parentShell);
		this.jobDetails = jobDetails;
	}


	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText("ViewData Execution History");
		container.setLayout(new FillLayout(SWT.VERTICAL));

		Composite composite = new Composite(container, SWT.BORDER);
		composite.setLayout(new RowLayout(SWT.VERTICAL));


		Composite portComposite = new Composite(composite, SWT.BORDER);
		portComposite.setLayoutData(new RowData(512, 130));
		
		buttonWidget(portComposite, SWT.None, new int[] {4, 2, 276, 20}, "Job Id");
		buttonWidget(portComposite, SWT.None, new int[] {280, 2, 130, 20}, "Time Stamp");
		buttonWidget(portComposite, SWT.None, new int[] {410, 2, 100, 20}, "Execution Mode");
		
		int y = 26;
		for(Job job : jobDetails){
			String timeStamp = getTimeStamp(job.getUniqueJobId());
			buttonWidget(portComposite, SWT.RADIO, new int[] {10, y, 270, 20}, job.getUniqueJobId());
			labelWidget(portComposite, SWT.None, new int[] {282, y + 2, 130, 20}, timeStamp);
			String mode = getJobExecutionMode(job.isRemoteMode());
			labelWidget(portComposite, SWT.None|SWT.CENTER, new int[] {412, y + 2, 100, 20}, mode);
			y = y + 20;
		}

		return super.createDialogArea(parent);
	}
	
	
	private Label labelWidget(Composite parent, int style, int[] bounds, String value) {
		Label label = new Label(parent, style);
		label.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		label.setText(value);

		return label;
	}
	
	private Button buttonWidget(Composite parent, int style, int[] bounds, String value) {
		Button button = new Button(parent, style);
		button.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		button.setText(value);
		button.setToolTipText(value);

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				button1 = (Button) event.widget;
			}
		});
		return button;
	}
	
	private String getJobExecutionMode(boolean executionMode){
		String runningMode = "";
		if(executionMode){
			runningMode = "Remote";
		}else{
			runningMode = "Local";
		}
		return runningMode;
	}
	
	public String getSelectedUniqueJobId(){
		return selectedUniqueJobId;
	}
	
	@Override
	protected void okPressed() {
		if(StringUtils.isNotEmpty(button1.getText())){
			selectedUniqueJobId = button1.getText();
		}
		super.okPressed();
	}
	
	@Override
	protected void cancelPressed() {
		selectedUniqueJobId = "";
		super.cancelPressed();
	}
	
	private String getTimeStamp(String jobId){
		String timeStamp;
		String jobUniqueId = jobId;
		
		String[] s1 = jobUniqueId.split("_");
		timeStamp = s1[s1.length-1];
		long times = Long.parseLong(timeStamp);
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
		timeStamp = dateFormat.format(new Date(times));
		
		return timeStamp;
		
	}
}

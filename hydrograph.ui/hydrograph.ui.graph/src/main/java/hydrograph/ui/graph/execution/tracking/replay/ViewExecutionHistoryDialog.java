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

package hydrograph.ui.graph.execution.tracking.replay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.FileNotFoundException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.apache.commons.lang.StringUtils;
import hydrograph.ui.graph.job.Job;
import hydrograph.ui.graph.Messages;
import hydrograph.ui.graph.execution.tracking.datastructure.ExecutionStatus;
import hydrograph.ui.graph.handler.ViewExecutionHistoryHandler;

/**
 * The Class ViewExecutionHistoryDialog use to create dialog to manage previous tracking history.
 * 
 * @author Bitwise
 */
public class ViewExecutionHistoryDialog extends Dialog{

	private static final String EXECUTION_TRACKING_LOG_FILE_EXTENTION = "*.track.log";
	private static final String REMOTE_MODE = "Remote";
	private static final String LOCAL_MODE = "Local";
	
	private List<Job> jobDetails;
	private String selectedUniqueJobId;
	private String[] titles = {"Job Id", "Time Stamp", "Execution Mode", "Job Status"};
	private Table table;
	private Text trackingFileText;
	private String filePath;
	private static final String VIEW_TRACKING_HISTORY="View Execution Tracking History"; 
	private static final String BROWSE_TRACKING_FILE="Browse Tracking File"; 
	private static final String EXECUTION_HISTORY_DIALOG="Execution History Dialog";
	private ViewExecutionHistoryHandler viewExecutionHistoryHandler;
	
	public ViewExecutionHistoryDialog(Shell parentShell, ViewExecutionHistoryHandler viewExecutionHistoryHandler, List<Job> jobDetails) {
		super(parentShell);
		this.jobDetails = jobDetails;
		this.viewExecutionHistoryHandler=viewExecutionHistoryHandler;
	}

	/**
	 * Create dialog for execution tracking view history, that use to manage all previous run tracking history. 
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.getShell().setText(VIEW_TRACKING_HISTORY);
		container.setLayout(new GridLayout(1, false));
		
		
		Composite composite1 = new Composite(container, SWT.BORDER);
		GridData gd_scrolledComposite1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_scrolledComposite1.heightHint = 236;
		gd_scrolledComposite1.widthHint = 844;
		composite1.setLayoutData(gd_scrolledComposite1);
		
		table = new Table(composite1, SWT.BORDER | SWT.Selection | SWT.FULL_SELECTION );
		table.setBounds(0, 0, 842, 234);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
	    for (int i = 0; i < titles.length; i++) {
	      TableColumn column = new TableColumn(table, SWT.NONE);
	      column.setWidth(212);
	      column.setText(titles[i]);
	    }
		
	    for(Job job : jobDetails){
	    	String timeStamp = getTimeStamp(job.getUniqueJobId());
	    	TableItem items = new TableItem(table, SWT.None);
	    	items.setText(0, job.getUniqueJobId());
	    	items.setText(1, timeStamp);
	    	String mode = getJobExecutionMode(job.isRemoteMode());
	    	items.setText(2, mode);
	    	items.setText(3, job.getJobStatus());
	    }
	    
	    table.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TableItem[] item = table.getSelection();
				 for (int i = 0; i < item.length; i++){
					 TableItem selectedItem = item[i];
					 selectedUniqueJobId = selectedItem.getText();
			      }
			}
		});
		return super.createDialogArea(parent);
	}
	
	
	private String getJobExecutionMode(boolean executionMode){
		String runningMode = "";
		if(executionMode){
			runningMode = REMOTE_MODE;
		}else{
			runningMode = LOCAL_MODE;
		}
		return runningMode;
	}
	
	/**
	 * The function will return selected unique job id
	 *@return String
	 */
	public String getSelectedUniqueJobId(){
		return selectedUniqueJobId;
	}
	
	/**
	 * The function will return selected tracking log file path
	 *@return String
	 */
	public String getTrackingFilePath(){
		return filePath;
	}
	
	/**
	   * Creates the browse file contents
	   * 
	   * @param shell the parent shell
	   */
	private void createBrowseButton(final Composite parent){
		Label label=new  Label(parent, SWT.None);
		label.setText(BROWSE_TRACKING_FILE);
		
		// Create the text box extra wide to show long paths
		trackingFileText = new Text(parent, SWT.BORDER);
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 0, 0);
		trackingFileText.setLayoutData(data);
	    
	    // Clicking the button will allow the user
	    // to select a directory
	    Button button = new Button(parent, SWT.PUSH);
	    button.setText("...");
	    button.addSelectionListener(new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			FileDialog fileDialog = new FileDialog(parent.getShell(),  SWT.OPEN  );
			fileDialog.setText(EXECUTION_HISTORY_DIALOG);
			String[] filterExt = { EXECUTION_TRACKING_LOG_FILE_EXTENTION };
			fileDialog.setFilterExtensions(filterExt);
			String path = fileDialog.open();
			if (path == null) return;
			trackingFileText.setText(path);
			trackingFileText.setToolTipText(path);
		}
	    });
	    
	    trackingFileText.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent event) {
	    		filePath = ((Text)event.widget).getText();
	    	}
		});
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayout(new GridLayout(1,true));
		parent.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,true,0,0));
		
		Composite composite=new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout(5,false));
		composite.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true,true,0,0));
		
		createBrowseButton(composite);
		
		Button okButton = createButton(composite, IDialogConstants.OK_ID, "Ok", false);
		Button closeButton = createButton(composite, IDialogConstants.CANCEL_ID, "Close", false);
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedUniqueJobId = "";
				filePath = "";
				close();
			}
		});
		
		
		
	}
	
	@Override
	protected void okPressed() {
		filePath=trackingFileText.getText();
		if(filePath != null){
		}else{
			selectedUniqueJobId = jobDetails.get(0).getUniqueJobId();
		}
		try {
			ExecutionStatus executionStatus = null;
			if(getTrackingFilePath().trim().isEmpty()){
				if(!StringUtils.isEmpty(getSelectedUniqueJobId())){
					executionStatus= viewExecutionHistoryHandler.readJsonLogFile(getSelectedUniqueJobId(), true, viewExecutionHistoryHandler.getLogPath());
				}else{
					super.okPressed();
				}
			}
			else{
				executionStatus= viewExecutionHistoryHandler.readBrowsedJsonLogFile(getTrackingFilePath().trim());
			}
			/*Return from this method if replay not working for old history, so that the view history window will not be closed	and 
			 * proper error message will be displayed over the view history window.*/
			if(executionStatus!=null){
				boolean status = viewExecutionHistoryHandler.replayExecutionTracking(executionStatus);
				if(!status){
					return;
				}
			}else
				return;
		} catch (FileNotFoundException e) {
			viewExecutionHistoryHandler.getMessageDialog(Messages.FILE_DOES_NOT_EXISTS);
			return;
		}catch(Exception e){
			viewExecutionHistoryHandler.getMessageDialog(Messages.INVALID_FILE_FORMATE);
			return;
		}
		super.okPressed();
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

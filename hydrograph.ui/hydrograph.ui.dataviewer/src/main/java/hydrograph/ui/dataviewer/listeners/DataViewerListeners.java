package hydrograph.ui.dataviewer.listeners;

import hydrograph.ui.dataviewer.adapters.CSVAdapter;
import hydrograph.ui.dataviewer.constants.ADVConstants;
import hydrograph.ui.dataviewer.viewloders.DataViewLoader;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.relique.jdbc.csv.CsvDatabaseMetaData;

public class DataViewerListeners {
	private DataViewLoader dataViewLoader;
	private Text jumpPageTextBox;
	private StatusLineManager statusLineManager;
	private CSVAdapter csvAdapter;
	private Text pageNumberDisplayTextBox;
	private List<Control> windowControls;
	
	
	public DataViewerListeners(){
		
	}
	
	public DataViewerListeners(DataViewLoader dataViewLoader,
			Text jumpPageTextBox, StatusLineManager statusLineManager,
			CSVAdapter csvAdapter, Text pageNumberDisplayTextBox,
			List<Control> windowControls) {
		super();
		this.dataViewLoader = dataViewLoader;
		this.jumpPageTextBox = jumpPageTextBox;
		this.statusLineManager = statusLineManager;
		this.csvAdapter = csvAdapter;
		this.pageNumberDisplayTextBox = pageNumberDisplayTextBox;
		this.windowControls = windowControls;
	}
	
	public void setDataViewLoader(DataViewLoader dataViewLoader) {
		this.dataViewLoader = dataViewLoader;
	}

	public void setJumpPageTextBox(Text jumpPageTextBox) {
		this.jumpPageTextBox = jumpPageTextBox;
	}

	public void setStatusLineManager(StatusLineManager statusLineManager) {
		this.statusLineManager = statusLineManager;
	}

	public void setCsvAdapter(CSVAdapter csvAdapter) {
		this.csvAdapter = csvAdapter;
	}

	public void setPageNumberDisplayTextBox(Text pageNumberDisplayTextBox) {
		this.pageNumberDisplayTextBox = pageNumberDisplayTextBox;
	}

	public void setWindowControls(List<Control> windowControls) {
		this.windowControls = windowControls;
	}

	public void addTabFolderSelectionChangeListener(CTabFolder tabFolder) {
		tabFolder.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				dataViewLoader.reloadloadViews();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// DO Nothing

			}
		});
	}
	
	public void attachJumpToPageListener(final Button button,final Button nextPageButton) {
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				
				
				if(jumpPageTextBox.getText().isEmpty()){
					appendStatusMessage("Jump page number can not be blank");
					return;
				}
				
				setProgressStatusMessage("Please wait, fetching page " + jumpPageTextBox.getText());
				setWindowControlsEnabled(false);
				
				
				final Long  pageNumberToJump = Long.valueOf(jumpPageTextBox.getText());
				Job job = new Job("JumpToPage") {
					  @Override
					  protected IStatus run(IProgressMonitor monitor) {
						  final int retCode = csvAdapter.jumpToPage(pageNumberToJump);
						  dataViewLoader.updateDataViewLists();
						  
						  Display.getDefault().asyncExec(new Runnable() {
					      @Override
					      public void run() {
					    	  dataViewLoader.reloadloadViews();
						       
								setWindowControlsEnabled(true);
					    	  if(retCode == ADVConstants.EOF){
									appendStatusMessage("End of file reached");
									nextPageButton.setEnabled(false);
								}else if(retCode == ADVConstants.ERROR){
									statusLineManager.setErrorMessage("Error while featching record");
								}else{
									setDefaultStatusMessage();
								}
					      }
					    });
					    return Status.OK_STATUS;
					  }

					
					};
					
					job.schedule();
			}
		});
	}
	
	
	public void attachJumpToPageListener(final Text text, final Button nextPageButton) {
		text.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				
				if(e.keyCode == SWT.KEYPAD_CR){
					
					if(jumpPageTextBox.getText().isEmpty()){
						appendStatusMessage("Jump page number can not be blank");
						return;
					}
					
					setProgressStatusMessage("Please wait, fetching page "
							+ jumpPageTextBox.getText());
					setWindowControlsEnabled(false);

					final Long pageNumberToJump = Long.valueOf(jumpPageTextBox
							.getText());
					Job job = new Job("JumpToPage") {
						@Override
						protected IStatus run(IProgressMonitor monitor) {
							final int retCode = csvAdapter
									.jumpToPage(pageNumberToJump);
							dataViewLoader.updateDataViewLists();

							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									dataViewLoader.reloadloadViews();

									setWindowControlsEnabled(true);
									if (retCode == ADVConstants.EOF) {
										appendStatusMessage("End of file reached");
										nextPageButton.setEnabled(false);
									} else if (retCode == ADVConstants.ERROR) {
										statusLineManager
												.setErrorMessage("Error while featching record");
									} else {
										setDefaultStatusMessage();
									}
									text.setFocus();
								}
								
							});
							return Status.OK_STATUS;
						}

					};

					job.schedule();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	
	public void attachPreviousPageButtonListener(Button button) {
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				
				setProgressStatusMessage("Please wait, fetching next page records");
				setWindowControlsEnabled(false);
				
				Job job = new Job("PreviousPage") {
					  @Override
					  protected IStatus run(IProgressMonitor monitor) {
						  final int retCode = csvAdapter.previous();
							
						  dataViewLoader.updateDataViewLists();
						  
						  Display.getDefault().asyncExec(new Runnable() {
					      @Override
					      public void run() {
					    	  //gridViewTableViewer.refresh();
					    	  dataViewLoader.reloadloadViews();
					    	  setWindowControlsEnabled(true);
					    	  if(retCode == ADVConstants.BOF){
									appendStatusMessage("Begining of file reached");
									 ((Button)e.widget).setEnabled(false);
								}else if(retCode == ADVConstants.ERROR){
									statusLineManager.setErrorMessage("Error while featching record");
								}else{
									setDefaultStatusMessage();
								}
					      }
					    });
					    return Status.OK_STATUS;
					  }
					};
					
					job.schedule();
				
			}
		});
	}
	
	public void attachNextPageButtonListener(Button button) {
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				
				setProgressStatusMessage("Please wait, fetching next page records");
				setWindowControlsEnabled(false);
				
				Job job = new Job("NextPage") {
					  @Override
					  protected IStatus run(IProgressMonitor monitor) {
						  final int retCode = csvAdapter.next();
						  
						  dataViewLoader.updateDataViewLists();
						  
						  Display.getDefault().asyncExec(new Runnable() {
					      @Override
					      public void run() {
					    	 /* gridViewTableViewer.refresh();
					    	  updateUnformattedView();*/
					    	  dataViewLoader.reloadloadViews();
					    	  setWindowControlsEnabled(true);
								if(retCode == ADVConstants.EOF){
									appendStatusMessage("End of file reached");
									 ((Button)e.widget).setEnabled(false);
								}else if(retCode == ADVConstants.ERROR){
									statusLineManager.setErrorMessage("Error while featching record");
								}else{
									setDefaultStatusMessage();
								}
					      }
					    });
					    return Status.OK_STATUS;
					  }
					};
					
					job.schedule();
			}
		});
	}
	
	public void setProgressStatusMessage(String message){
		statusLineManager.setMessage(message);
	}
	
	private void appendStatusMessage(String Message) {
		StringBuilder stringBuilder = new StringBuilder();
		
		if(csvAdapter.getRowCount()!=null){
			stringBuilder.append("Record Count: " + csvAdapter.getRowCount() + " | ");
		}else{
			stringBuilder.append("Counting number of records... | ");
		}
		
		stringBuilder.append("Showing records from " + csvAdapter.getOFFSET() + " to " + (csvAdapter.getOFFSET() + csvAdapter.getPAGE_SIZE()) + " | ");
		
		stringBuilder.append(Message + " | ");
		
		statusLineManager.setMessage(stringBuilder.toString().substring(0,stringBuilder.length() -2));
	}
	
	public void setDefaultStatusMessage() {
		StringBuilder stringBuilder = new StringBuilder();
		
		statusLineManager.setErrorMessage(null);
		
		if(csvAdapter.getRowCount()!=null){
			stringBuilder.append("Record Count: " + csvAdapter.getRowCount() + " | ");
		}else{
			stringBuilder.append("Counting number of records... | ");
		}
		
		stringBuilder.append("Showing records from " + csvAdapter.getOFFSET() + " to " + (csvAdapter.getOFFSET() + csvAdapter.getPAGE_SIZE()) + " | ");
		statusLineManager.setMessage(stringBuilder.toString().substring(0,stringBuilder.length() -2));
		
		updatePageNumberDisplayPanel();
	}

	
	public void setWindowControlsEnabled(boolean enabled){
		for(Control control: windowControls){
			control.setEnabled(enabled);
			if(csvAdapter.getRowCount()==null){
				if(control.getData("CONTROL_NAME")!=null){
					if(control.getData("CONTROL_NAME").equals("JUMP_BUTTON") || control.getData("CONTROL_NAME").equals("JUMP_TEXT")){
						control.setEnabled(false);
					}
				}
			}
		}
	}
	
	public void updatePageNumberDisplayPanel(){
		pageNumberDisplayTextBox.setText(csvAdapter.getPageStatusNumber());
	}
}

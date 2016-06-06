package hydrograph.ui.dataviewer.listeners;

import hydrograph.ui.dataviewer.adapters.CSVAdapter;
import hydrograph.ui.dataviewer.constants.StatusConstants;
import hydrograph.ui.dataviewer.constants.ControlConstants;
import hydrograph.ui.dataviewer.datastructures.StatusMessage;
import hydrograph.ui.dataviewer.support.StatusManager;
import hydrograph.ui.dataviewer.viewloders.DataViewLoader;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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
import org.eclipse.swt.widgets.Widget;

public class DataViewerListeners {
	private DataViewLoader dataViewLoader;
	private StatusManager statusManager;
	private CSVAdapter csvAdapter;
	private Map<String,Control> windowControls;
	
	public DataViewerListeners(){
		
	}
	
	public DataViewerListeners(DataViewLoader dataViewLoader,
			StatusManager statusManager,
			CSVAdapter csvAdapter,Map<String,Control> windowControls) {
		super();
		this.dataViewLoader = dataViewLoader;
		this.statusManager = statusManager;
		this.csvAdapter = csvAdapter;
		this.windowControls = windowControls;
	}
	
	public void setDataViewLoader(DataViewLoader dataViewLoader) {
		this.dataViewLoader = dataViewLoader;
	}

	public void setCsvAdapter(CSVAdapter csvAdapter) {
		this.csvAdapter = csvAdapter;
	}
	
	public void setStatusManager(StatusManager statusManager) {
		this.statusManager = statusManager;
	}

	public void setWindowControls(Map<String, Control> windowControls) {
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
	
	
	private void jumpPageListener(){
		Text jumpPageTextBox = ((Text)windowControls.get(ControlConstants.JUMP_TEXT));
		
		if(((Text)windowControls.get(ControlConstants.JUMP_TEXT)).getText().isEmpty()){
			statusManager.setStatus(new StatusMessage(StatusConstants.ERROR, "Jump page number can not be blank"));
			return;
		}
		
		statusManager.setStatus(new StatusMessage(StatusConstants.PROGRESS, "Please wait, fetching page " + jumpPageTextBox.getText()));
		statusManager.setAllWindowControlsEnabled(false);
		
		final Long  pageNumberToJump = Long.valueOf(jumpPageTextBox.getText());
		Job job = new Job("JumpToPage") {
			  @Override
			  protected IStatus run(IProgressMonitor monitor) {
				  final StatusMessage status = csvAdapter.jumpToPage(pageNumberToJump);
				  dataViewLoader.updateDataViewLists();
				  
				  Display.getDefault().asyncExec(new Runnable() {
			      @Override
			      public void run() {
			    	  dataViewLoader.reloadloadViews();
			    	  statusManager.setAllWindowControlsEnabled(true);
			    	  statusManager.setStatus(status);
			      }
			    });
			    return Status.OK_STATUS;
			  }

			
			};
			
			job.schedule();
	}
	
	public void attachJumpToPageListener(final Widget widget) {
		
		if(widget instanceof Text){
			((Text)widget).addKeyListener(new KeyListener() {
				
				@Override
				public void keyReleased(KeyEvent e) {
					if(e.keyCode == SWT.KEYPAD_CR || e.keyCode == 13){
						jumpPageListener();
					}
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					// DO Nothing
					
				}
			});
		}
		
		if(widget instanceof Button){
			((Button)widget).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					jumpPageListener();
				}
			});
		}
	}
	
	public void attachPreviousPageButtonListener(Button button) {
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				statusManager.setStatus(new StatusMessage(StatusConstants.PROGRESS,"Please wait, fetching next page records"));
				
				statusManager.setAllWindowControlsEnabled(false);
				
				Job job = new Job("PreviousPage") {
					  @Override
					  protected IStatus run(IProgressMonitor monitor) {
						  final StatusMessage status = csvAdapter.previous();
							
						  dataViewLoader.updateDataViewLists();
						  
						  Display.getDefault().asyncExec(new Runnable() {
					      @Override
					      public void run() {
					    	  dataViewLoader.reloadloadViews();
					    	  statusManager.setAllWindowControlsEnabled(true);
					    	  statusManager.setStatus(status);
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
				statusManager.setStatus(new StatusMessage(StatusConstants.PROGRESS,"Please wait, fetching next page records"));
				statusManager.setAllWindowControlsEnabled(false);
				
				Job job = new Job("NextPage") {
					  @Override
					  protected IStatus run(IProgressMonitor monitor) {
						  final StatusMessage status = csvAdapter.next();
						  
						  dataViewLoader.updateDataViewLists();
						  
						  Display.getDefault().asyncExec(new Runnable() {
					      @Override
					      public void run() {
					    	  dataViewLoader.reloadloadViews();
					    	  statusManager.setAllWindowControlsEnabled(true);
					    	  statusManager.setStatus(status);
					      }
					    });
					    return Status.OK_STATUS;
					  }
					};
					
					job.schedule();
			}
		});
	}
}

package hydrograph.ui.dataviewer.support;

import hydrograph.ui.dataviewer.adapters.CSVAdapter;
import hydrograph.ui.dataviewer.constants.StatusConstants;
import hydrograph.ui.dataviewer.constants.ControlConstants;
import hydrograph.ui.dataviewer.datastructures.StatusMessage;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class StatusManager {
	private StatusLineManager statusLineManager;
	private CSVAdapter csvAdapter;
	private Map<String,Control> windowControls;
	
	public StatusManager(){
		
	}

	public StatusLineManager getStatusLineManager() {
		return statusLineManager;
	}
	
	public void setCsvAdapter(CSVAdapter csvAdapter) {
		this.csvAdapter = csvAdapter;
	}

	public void setWindowControls(Map<String,Control> windowControls) {
		this.windowControls = windowControls;
	}

	public void setStatusLineManager(StatusLineManager statusLineManager) {
		this.statusLineManager = statusLineManager;
	}	
	
	public void setStatus(StatusMessage status) {
		
		statusLineManager.setErrorMessage(null);
		
		if (status.getReturnCode() == StatusConstants.ERROR) {
			statusLineManager.setErrorMessage(status.getStatusMessage());
			return;
		}
		
		if (status.getReturnCode() == StatusConstants.PROGRESS) {
			statusLineManager.setMessage(status.getStatusMessage());
			return;
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("Showing records from " + csvAdapter.getOFFSET()
				+ " to " + (csvAdapter.getOFFSET() + csvAdapter.getPAGE_SIZE())
				+ " | ");
		
		
		if (csvAdapter.getRowCount() != null) {
			stringBuilder.append("Record Count: " + csvAdapter.getRowCount()
					+ " | ");
		} 
		
		if (!StringUtils.isEmpty(status.getStatusMessage()))
			stringBuilder.append(status.getStatusMessage() + " | ");
		
				
		
		statusLineManager.setMessage(stringBuilder.toString().substring(0,
				stringBuilder.length() - 2));
		
		updatePageNumberDisplayPanel();
	}
	
	
	public void appendStatusMessage(String message) {
		statusLineManager.setMessage(" | " + message);
	}
		
	
	public void enableJumpPagePanel(boolean enabled){
		windowControls.get(ControlConstants.JUMP_BUTTON).setEnabled(enabled);
		windowControls.get(ControlConstants.JUMP_TEXT).setEnabled(enabled);
		if(csvAdapter.getRowCount()!=null){
			if(((long)csvAdapter.getTotalNumberOfPages()) == csvAdapter.getCurrentPageNumber()){
				windowControls.get(ControlConstants.NEXT_BUTTON).setEnabled(false);
			}
		}
	}
	
	public void enablePageSwitchPanel(boolean enabled){
		windowControls.get(ControlConstants.PREVIOUS_BUTTON).setEnabled(enabled);
		windowControls.get(ControlConstants.NEXT_BUTTON).setEnabled(enabled);
	}
	
	public void enableNextPageButton(boolean enabled){
		windowControls.get(ControlConstants.NEXT_BUTTON).setEnabled(enabled);
		
	}
	
	public void enablePreviousPageButton(boolean enabled){
		windowControls.get(ControlConstants.PREVIOUS_BUTTON).setEnabled(enabled);
	}
	
	public void enablePaginationPanel(boolean enabled){
		for(String control:windowControls.keySet()){
			windowControls.get(control).setEnabled(enabled);
		}
	}
	
	public void updatePageNumberDisplayPanel(){
		((Text)windowControls.get(ControlConstants.PAGE_NUMBER_DISPLAY)).setText(csvAdapter.getPageStatusNumber());
	}

	public void setAllWindowControlsEnabled(boolean enabled) {
		for(String control: windowControls.keySet()){
			windowControls.get(control).setEnabled(enabled);
		}
		
		if(csvAdapter.getRowCount()==null){
			enableJumpPagePanel(false);
		}else{		
			if(((long)csvAdapter.getTotalNumberOfPages()) == csvAdapter.getCurrentPageNumber()){
				enableNextPageButton(false);
			}
		}
		
		if(csvAdapter.getCurrentPageNumber()==1){
			enablePreviousPageButton(false);
		}
	}

	public void enableInitialPaginationContols(){
		if(csvAdapter.getRowCount()!=null){
			if(((long)csvAdapter.getTotalNumberOfPages()) == csvAdapter.getCurrentPageNumber()){
				enableNextPageButton(false);
			}else{
				enableNextPageButton(true);
			}
		}
		
		if(csvAdapter.getCurrentPageNumber()==1){
			enablePreviousPageButton(false);
		}
		enableJumpPagePanel(false);
	}	
}


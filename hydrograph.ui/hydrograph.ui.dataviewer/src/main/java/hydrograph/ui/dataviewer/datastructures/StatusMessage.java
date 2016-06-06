package hydrograph.ui.dataviewer.datastructures;

public class StatusMessage {
	private int returnCode;
	private String returnMessage="";
	
	public StatusMessage(int returnCode, String returnMessage) {
		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
	}

	public StatusMessage(int returnCode) {
		this.returnCode = returnCode;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public String getStatusMessage() {
		return returnMessage;
	}
	
}

package hydrograph.ui.dataviewer.datastructures;

public class StatusBarData {
	private static String numberOfRecord;
	private static String fromRecords;
	private static String toRecords;
	private static String errorMessage;
	private static String warningMessage;
	private static String progressMessage;
	
	public static void setNumberOfRecord(String numberOfRecord) {
		StatusBarData.numberOfRecord = numberOfRecord;
	}

	public static void setFromRecords(String fromRecords) {
		StatusBarData.fromRecords = fromRecords;
	}

	public static void setToRecords(String toRecords) {
		StatusBarData.toRecords = toRecords;
	}

	public static void setErrorMessage(String errorMessage) {
		StatusBarData.errorMessage = errorMessage;
	}

	public static void setWarningMessage(String warningMessage) {
		StatusBarData.warningMessage = warningMessage;
	}

	public static void setProgressMessage(String progressMessage) {
		StatusBarData.progressMessage = progressMessage;
	}

	public static String getStatusMessage() {
		
		StringBuilder stringBuilder = new StringBuilder();
		
		if(numberOfRecord!=null){
			stringBuilder.append("Record Count: " + numberOfRecord + " | ");
		}
		
		if(fromRecords!=null && toRecords!=null){
			stringBuilder.append("Showing records " + fromRecords + " to " + toRecords + " | ");
		}
		
		
		if(errorMessage!=null){
			stringBuilder.append("Error: " + errorMessage + " | ");
		}
			
		if(warningMessage!=null){
			stringBuilder.append("Warning : " + warningMessage + " | ");
		}
		
		if(progressMessage!=null){
			stringBuilder.append("Progress : " + progressMessage + " | ");
		}
		
		return stringBuilder.toString().substring(0, stringBuilder.length() - 2);
	}
}

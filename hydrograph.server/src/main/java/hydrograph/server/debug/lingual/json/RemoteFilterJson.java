package hydrograph.server.debug.lingual.json;

import java.util.List;

public class RemoteFilterJson {
	private String condition;
	private List<GridRow> schema;
	private int fileSize;
	private JobDetails jobDetails;
	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public List<GridRow> getSchema() {
		return schema;
	}
	public void setSchema(List<GridRow> schema) {
		this.schema = schema;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public JobDetails getJobDetails() {
		return jobDetails;
	}
	public void setJobDetails(JobDetails jobDetails) {
		this.jobDetails = jobDetails;
	}
	
	
	

}

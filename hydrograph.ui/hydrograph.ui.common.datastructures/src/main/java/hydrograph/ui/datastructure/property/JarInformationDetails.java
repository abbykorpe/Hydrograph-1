package hydrograph.ui.datastructure.property;

public class JarInformationDetails {

	private String name = "Jackson.jar";
	private String versionNo="0.0.1.qualifier";
	private String groupId="12345";
	private String artifactNo="0001223";
	private String licenseInfo="C:\\Users\\ashikah\\Git\\Hydrograph_Tool\\Thesis\\hydrograph.ui\\hydrograph.ui.help\\html\\Components\\Input_File_Delimited.html";
	
	public String getLicenseInfo() {
		return licenseInfo;
	}

	public void setLicenseInfo(String licenseInfo) {
		this.licenseInfo = licenseInfo;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getVersionNo() {
		return versionNo;
	}
	
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	
	public String getGroupId() {
		return groupId;
	}
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getArtifactNo() {
		return artifactNo;
	}
	
	public void setArtifactNo(String artifactNo) {
		this.artifactNo = artifactNo;
	}
	
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
	      stringBuilder.append("JarInformation [ ");
	      stringBuilder.append("\nName: ");
	      stringBuilder.append(name);
	      stringBuilder.append("\nVersion No: ");
	      stringBuilder.append(versionNo);
	      stringBuilder.append("\nGroup Id: ");
	      stringBuilder.append(groupId);
	      stringBuilder.append("\nArtifact No: ");
	      stringBuilder.append(artifactNo);
	      stringBuilder.append("\nLicense Info: ");
	      stringBuilder.append(licenseInfo);
	      stringBuilder.append(" ]");
	      return stringBuilder.toString();
	   }
}

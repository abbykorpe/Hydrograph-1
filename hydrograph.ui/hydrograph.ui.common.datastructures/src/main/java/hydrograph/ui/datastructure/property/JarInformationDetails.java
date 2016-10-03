package hydrograph.ui.datastructure.property;

public class JarInformationDetails {

	private String name = "Jackson.jar";
	private String versionNo="0.0.1.qualifier";
	private String genericId="12345";
	private String artifactNo="0001223";
	
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
	
	public String getGenericId() {
		return genericId;
	}
	
	public void setGenericId(String genericId) {
		this.genericId = genericId;
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
	      stringBuilder.append("\nGeneric Id: ");
	      stringBuilder.append(genericId);
	      stringBuilder.append("\nArtifact No: ");
	      stringBuilder.append(artifactNo);
	      stringBuilder.append(" ]");
	      return stringBuilder.toString();
	   }
}

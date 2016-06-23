package hydrograph.ui.dataviewer.filter;

import java.util.List;

public class FilterConditions {
	private List<Condition> localConditions;
	private List<Condition> remoteConditions;
	private boolean retainLocal;
	private boolean retainRemote;
	
	public List<Condition> getLocalConditions() {
		return localConditions;
	}
	public void setLocalConditions(List<Condition> localConditions) {
		this.localConditions = localConditions;
	}
	public List<Condition> getRemoteConditions() {
		return remoteConditions;
	}
	public void setRemoteConditions(List<Condition> remoteConditions) {
		this.remoteConditions = remoteConditions;
	}
	public boolean getRetainLocal() {
		return retainLocal;
	}
	public void setRetainLocal(boolean retainLocal) {
		this.retainLocal = retainLocal;
	}
	public boolean getRetainRemote() {
		return retainRemote;
	}
	public void setRetainRemote(boolean retainRemote) {
		this.retainRemote = retainRemote;
	}
	
}

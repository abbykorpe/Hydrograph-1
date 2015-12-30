package com.bitwise.app.engine.ui.converter;


public class LinkingData {
	
	private String sourceComponentId;
	private  String targetComponentId;
	private String sourceTerminal;
	private String targetTerminal;
	private boolean isMultiplePortsAllowed;
		
	private LinkingData() {
	}

	public LinkingData(String sourceComponentId, String targetComponentId,String sourceTerminal,String targetTerminal) {
		super();
		this.sourceComponentId = sourceComponentId;
		this.targetComponentId = targetComponentId;
		setSourceTerminal(sourceTerminal);
		setTargetTerminal(targetTerminal);

	}

	public String getSourceComponentId() {
		return sourceComponentId;
	}

	public void setSourceComponentId(String sourceComponentId) {
		this.sourceComponentId = sourceComponentId;
	}

	public String getTargetComponentId() {
		return targetComponentId;
	}

	public void setTargetComponentId(String targetComponentId) {
		this.targetComponentId = targetComponentId;
	}
	
	public String getSourceTerminal() {
		return sourceTerminal;
	}

	public void setSourceTerminal(String sourceTerminal) {
		this.sourceTerminal=sourceTerminal;
	}
	
	public String getTargetTerminal() {
		return targetTerminal;
	}

	public void setTargetTerminal(String targetTerminal) {
		this.targetTerminal=targetTerminal;
	}

	@Override
	public String toString() {
	
		return "Source Component ID:"+this.sourceComponentId+" | "
				+ "Source Terminal:"+this.sourceTerminal+" | "
				+ "Target Component ID:"+this.targetComponentId+" | "
				+ "Target Terminal:"+this.targetTerminal+" | "
				+ "Multiple Port Allowed:"+this.isMultiplePortsAllowed;
	}
}


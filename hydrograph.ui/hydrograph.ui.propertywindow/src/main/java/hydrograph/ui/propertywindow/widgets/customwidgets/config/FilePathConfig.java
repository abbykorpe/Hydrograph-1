package hydrograph.ui.propertywindow.widgets.customwidgets.config;

import java.util.ArrayList;
import java.util.List;

import hydrograph.ui.propertywindow.factory.ListenerFactory.Listners;

public class FilePathConfig implements WidgetConfig {
	private String label;
	private List<Listners> listeners = new ArrayList<>();
	private boolean isMandatory;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<Listners> getListeners() {
		return listeners;
	}
	
	public boolean isMandatory() {
		return isMandatory;
	}
	
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
}

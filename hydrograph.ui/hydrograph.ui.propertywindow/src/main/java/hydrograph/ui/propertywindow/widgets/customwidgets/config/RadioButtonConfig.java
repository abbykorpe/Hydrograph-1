package hydrograph.ui.propertywindow.widgets.customwidgets.config;

public class RadioButtonConfig implements WidgetConfig {
	private String widgetDisplayNames[];
	private String propertyName;
	
	/**
	 * @return the widgetDisplayNames
	 */
	public String[] getWidgetDisplayNames() {
		return widgetDisplayNames;
	}

	/**
	 * @param widgetDisplayNames the widgetDisplayNames to set
	 */
	public void setWidgetDisplayNames(String[] widgetDisplayNames) {
		this.widgetDisplayNames = widgetDisplayNames;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

}

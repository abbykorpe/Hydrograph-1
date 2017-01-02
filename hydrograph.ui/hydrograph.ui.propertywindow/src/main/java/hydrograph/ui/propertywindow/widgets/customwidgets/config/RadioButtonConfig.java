package hydrograph.ui.propertywindow.widgets.customwidgets.config;

public class RadioButtonConfig implements WidgetConfig {
	String widgetDisplayNames[];

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
}

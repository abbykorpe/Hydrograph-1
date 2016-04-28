package hydrograph.ui.propertywindow.widgets.listeners;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper.HelperType;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class JoinInputCountFocusOutListener implements IELTListener {
	private int minimunPortCount;
	private Component currentComponent;

	@Override
	public int getListenerType() {
		return SWT.FocusOut;
	}

	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helpers,
			Widget... widgets) {
		final Widget[] widgetList = widgets;
		if (helpers != null) {
			if (helpers.get(HelperType.MINIMUM_PORT_COUNT) != null)
				minimunPortCount = Integer.valueOf((String) helpers.get(HelperType.MINIMUM_PORT_COUNT));
			currentComponent = (Component) helpers.get(HelperType.CURRENT_COMPONENT);
		}
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.FocusOut) {
					Text textBox = (Text) widgetList[0];
					String textBoxValue = ((Text) event.widget).getText();
					if ((Integer.parseInt(textBoxValue) < minimunPortCount || Integer.parseInt(textBoxValue) > 25)
							&& currentComponent.getProperties().get(Constants.UNUSED_AND_INPUT_PORT_COUNT_PROPERTY) != null) {
						textBox.setText((String) currentComponent.getProperties().get(
								Constants.UNUSED_AND_INPUT_PORT_COUNT_PROPERTY));
					}
				}
			}
		};
		return listener;
	}

}

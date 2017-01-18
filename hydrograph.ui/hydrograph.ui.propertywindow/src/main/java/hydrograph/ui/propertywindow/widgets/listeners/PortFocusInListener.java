package hydrograph.ui.propertywindow.widgets.listeners;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper.HelperType;

public class PortFocusInListener implements IELTListener{
	
	ControlDecoration txtDecorator;

	@Override
	public int getListenerType() {
		return SWT.FocusIn;
	}

	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helpers,
			Widget... widgets) {
		if (helpers != null){
			txtDecorator = (ControlDecoration) helpers.get(HelperType.CONTROL_DECORATION);
		}
		
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				String charSet = ((Text) widgets[0]).getText().trim();
				if(SWT.FocusIn == event.type) {
					Matcher matchs=Pattern.compile(Constants.REGEX_NUMERIC_AND_PARAMETER).matcher(charSet);
					if (StringUtils.isBlank(charSet)) {
						txtDecorator.show();
						((Text) widgets[0]).setBackground(new Color(Display.getDefault(), 255, 255, 255));
						((Text) widgets[0]).setToolTipText(txtDecorator.getDescriptionText());
					} else if(!matchs.matches()) {
						txtDecorator.show();
					} else{
						txtDecorator.hide();
					}
				}
			}
		};
		return listener;
	}

}

package hydrograph.ui.propertywindow.widgets.listeners;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper.HelperType;

public class ELTVerifyNumericAndParameterListener  implements IELTListener{

	private ControlDecoration txtDecorator;
	private int characterLimit;

	@Override
	public int getListenerType() {
		return SWT.Modify;
	}

	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helpers,
			Widget... widgets) {
		final Widget[] widgetList = widgets;
		if (helpers != null) {
			txtDecorator = (ControlDecoration) helpers.get(HelperType.CONTROL_DECORATION);
			characterLimit = (int) helpers.get(HelperType.CHARACTER_LIMIT);
		}

		Listener listener=new Listener() {
			@Override
			public void handleEvent(Event event) {
				
				Text text = (Text)widgetList[0];
				String string=text.getText();
				Matcher matchs=Pattern.compile("[\\@]{1}[\\{]{1}[\\w]{1,}[\\}]{1}||[\\d]{1,4}").matcher(string);
				if(StringUtils.isNotBlank(string) && matchs.matches()){
					txtDecorator.hide();
			}else{
				txtDecorator.setDescriptionText("Should be numeric or Paramerter e.g. 1234, @{Param}");
				txtDecorator.show();
				event.doit=false;
				
			}
			}
		};
	return listener;
	}

}

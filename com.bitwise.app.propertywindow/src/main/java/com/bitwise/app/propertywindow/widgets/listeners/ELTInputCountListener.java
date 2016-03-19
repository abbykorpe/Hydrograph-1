/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
package com.bitwise.app.propertywindow.widgets.listeners;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.bitwise.app.propertywindow.messages.Messages;
import com.bitwise.app.propertywindow.propertydialog.PropertyDialogButtonBar;
import com.bitwise.app.propertywindow.widgets.listeners.ListenerHelper.HelperType;

public class ELTInputCountListener implements IELTListener{

	private int minimunPortCount;
	private ControlDecoration txtDecorator;
	@Override
	public int getListenerType() {
		return SWT.Modify;
	}

	@Override
	public Listener getListener(final PropertyDialogButtonBar propertyDialogButtonBar,ListenerHelper helper, Widget... widgets) {
		final Widget[] widgetList = widgets;
		if (helper != null) {
			txtDecorator = (ControlDecoration) helper.get(HelperType.CONTROL_DECORATION);
			if (helper.get(HelperType.MINIMUM_PORT_COUNT) != null)
				minimunPortCount = Integer.valueOf((String) helper.get(HelperType.MINIMUM_PORT_COUNT));
		}

		Listener listener=new Listener() {
			@Override
			public void handleEvent(Event event) {
				String string =((Text) widgetList[0]).getText().trim();	
				if(StringUtils.isNotEmpty(string)){
					if(event.type == SWT.Modify){
						if(Integer.parseInt(string) < minimunPortCount || Integer.parseInt(string) > 25 ){
							txtDecorator.setDescriptionText(Messages.bind(Messages.PORT_VALUE, minimunPortCount));
							txtDecorator.show();
							propertyDialogButtonBar.enableOKButton(false);
							propertyDialogButtonBar.enableApplyButton(false);
						}else
						{
							txtDecorator.hide();
							propertyDialogButtonBar.enableOKButton(true);
						}
					}
				}

			}
		};
		return listener;
	}
}

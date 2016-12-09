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

 
package hydrograph.ui.propertywindow.widgets.listeners;

import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper.HelperType;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;


/**
 * The listener interface for receiving ELTModify events. The class that is interested in processing a ELTModify event
 * implements this interface, and the object created with that class is registered with a component using the
 * component's <code>addELTModifyListener<code> method. When
 * the ELTModify event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see ELTModifyEvent
 */
public class ELTModifyListener implements IELTListener{
	private ControlDecoration txtDecorator;
	
	@Override
	public int getListenerType() {
		return SWT.Modify;
	}

	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helper, Widget... widgets) {
		final Widget[] widgetList = widgets;
		if (helper != null) {
			txtDecorator = (ControlDecoration) helper.get(HelperType.CONTROL_DECORATION);
		}
		Listener listener=new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				String string=((Text)widgetList[0]).getText().trim();
				if(event.type==SWT.Modify){
					if(StringUtils.isBlank(string)){
						//txtDecorator.setDescriptionText(Messages.EMPTYFIELDMESSAGE);
						
						txtDecorator.show();
						((Text) widgetList[0]).setToolTipText(txtDecorator.getDescriptionText());
						((Text) widgetList[0]).setBackground(new Color(Display.getDefault(), 255, 255, 204));
					}else{
						//txtDecorator.setDescriptionText(Messages.EMPTYFIELDMESSAGE);
						txtDecorator.hide();
						((Text) widgetList[0]).setToolTipText("");
						((Text) widgetList[0]).setBackground(new Color(Display.getDefault(), 255, 255, 255));
					}
					
				}else{
					//txtDecorator.setDescriptionText(Messages.EMPTYFIELDMESSAGE);
					txtDecorator.hide();
					((Text) widgetList[0]).setBackground(new Color(Display.getDefault(), 255, 255, 255));
				}
			}		
		};
		
		return listener;
	}
}

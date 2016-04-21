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

 
package hydrograph.ui.propertywindow.runconfig;

import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.widgets.utility.WidgetUtility;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;


public class EmptyTextListener implements ModifyListener {

	private String fieldName;
	private ControlDecoration errorDecorator;

	public EmptyTextListener(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Override
	public void modifyText(ModifyEvent event) {
		String txt= ((Text)event.getSource()).getText();

		if (StringUtils.isEmpty(txt)) {
			errorDecorator = WidgetUtility.addDecorator((Text)event.getSource(),Messages.bind(Messages.EMPTY_FIELD, fieldName));
			errorDecorator.show();
			errorDecorator.setMarginWidth(3);

		} else {
			if(errorDecorator!=null)
				errorDecorator.hide();

		}

	}

}

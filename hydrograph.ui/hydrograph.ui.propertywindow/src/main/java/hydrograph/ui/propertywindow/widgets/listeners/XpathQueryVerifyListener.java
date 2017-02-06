/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.ui.propertywindow.widgets.listeners;

import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.propertywindow.messages.Messages;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;

public class XpathQueryVerifyListener extends ELTVerifyTextListener {
	@Override
	public Listener getListener(PropertyDialogButtonBar propertyDialogButtonBar, ListenerHelper helper,  Widget... widgets) {
		regex=Constants.REGEX_FORWARD_SLASH;
		errorMessage=Messages.CHARACTERSET_XPATH_QUERY;
		return super.getListener(propertyDialogButtonBar, helper, widgets);
	}

}

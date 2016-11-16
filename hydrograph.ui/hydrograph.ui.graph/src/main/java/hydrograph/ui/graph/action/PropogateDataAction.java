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

package hydrograph.ui.graph.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.graph.controller.ComponentEditPart;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.graph.utility.SubJobUtility;

public class PropogateDataAction extends SelectionAction {
    private Component component;
	public PropogateDataAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
	}
   
	@Override
	protected void init() {
		super.init();
		setText(Constants.PROPAGATE_FIELD_FROM_LEFT_ACTION); 
		setId(Constants.PROPAGATE);
		setEnabled(false);
	}
	
	
	@Override
	protected boolean calculateEnabled() {
		List<Object> selectedObjects = getSelectedObjects();
		if (selectedObjects != null && !selectedObjects.isEmpty() && selectedObjects.size() == 1) {
			for (Object obj : selectedObjects) {
				if (obj instanceof ComponentEditPart) {
					component=((ComponentEditPart) obj).getCastedModel();
					if(StringUtils.equalsIgnoreCase(Constants.STRAIGHTPULL, component.getCategory())
						||	StringUtils.equalsIgnoreCase(Constants.TRANSFORM,component.getCategory())
					    || StringUtils.equalsIgnoreCase(Constants.SUBJOB_COMPONENT,component.getComponentName())
							
							)
					{
						if(!component.getTargetConnections().isEmpty())
						return true;
					}	
				}
			}
		}
		return false;
	}
    
	@Override
	public void run() {
		component.setContinuousSchemaPropogationAllow(true);
		new SubJobUtility().setFlagForContinuousSchemaPropogation(component);
	}
	
}

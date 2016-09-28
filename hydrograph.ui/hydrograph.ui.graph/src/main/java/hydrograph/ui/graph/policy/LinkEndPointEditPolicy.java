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

 
package hydrograph.ui.graph.policy;

import hydrograph.ui.graph.figure.ELTColorConstants;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.graphics.Color;

/**
 * The Class LinkEndPointEditPolicy.
 */
public class LinkEndPointEditPolicy extends ConnectionEndpointEditPolicy{

	private Color linkSelectedColor=null;
	
	@Override
	protected void addSelectionHandles() {
		if(linkSelectedColor.isDisposed()){
			linkSelectedColor = new Color(null, ELTColorConstants.COMPONENT_BORDER_SELECTED_RGB[0], ELTColorConstants.COMPONENT_BORDER_SELECTED_RGB[1], ELTColorConstants.COMPONENT_BORDER_SELECTED_RGB[2]);
		}
		getLinkFigure().setForegroundColor(linkSelectedColor);
	}

	protected PolylineConnection getLinkFigure() {
		return (PolylineConnection) ((GraphicalEditPart) getHost()).getFigure();
	}

	@Override
	protected void removeSelectionHandles() {
		super.removeSelectionHandles();
		getLinkFigure().setForegroundColor(ColorConstants.black);
		if(linkSelectedColor!=null){
			linkSelectedColor.dispose();
		}
	}
}

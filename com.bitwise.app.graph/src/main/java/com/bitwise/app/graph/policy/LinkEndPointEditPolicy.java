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

 
package com.bitwise.app.graph.policy;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.graphics.Color;

/**
 * The Class LinkEndPointEditPolicy.
 */
public class LinkEndPointEditPolicy extends ConnectionEndpointEditPolicy{

	@Override
	protected void addSelectionHandles() {
		super.addSelectionHandles();
		getLinkFigure().setForegroundColor(new Color(null, 22, 169, 199));
	}

	protected PolylineConnection getLinkFigure() {
		return (PolylineConnection) ((GraphicalEditPart) getHost()).getFigure();
	}

	@Override
	protected void removeSelectionHandles() {
		super.removeSelectionHandles();
		getLinkFigure().setForegroundColor(ColorConstants.black);
	}
}

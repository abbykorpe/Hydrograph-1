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

 
package hydrograph.ui.graph.controller;

import hydrograph.ui.graph.model.Link;
import hydrograph.ui.graph.policy.LinkEditPolicy;
import hydrograph.ui.graph.policy.LinkEndPointEditPolicy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RoutingAnimator;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;


/**
 * The Class LinkEditPart.
 */
public class LinkEditPart extends AbstractConnectionEditPart
		implements PropertyChangeListener {
	/**
	 * Upon activation, attach to the model element as a property change
	 * listener.
	 */
	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			((Link) getModel()).addPropertyChangeListener(this);
		}
	}

	/**
	 * Upon deactivation, detach from the model element as a property change
	 * listener.
	 */
	@Override
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((Link) getModel())
					.removePropertyChangeListener(this);
		}
	}

	@Override
	protected IFigure createFigure() {

		PolylineConnection connection = (PolylineConnection) super
				.createFigure();
		connection.addRoutingListener(RoutingAnimator.getDefault());
		connection.setTargetDecoration(new PolygonDecoration());
		connection.setLineStyle(getCastedModel().getLineStyle());
		return connection;
	}

	@Override
	protected void createEditPolicies() {
		// Selection handle edit policy. Makes the connection show a feedback,
		// when selected by the user.
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new LinkEndPointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new LinkEditPolicy());
	}

	private Link getCastedModel() {
		return (Link) getModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		// String property = event.getPropertyName();
		// if (Connection.LINESTYLE_PROP.equals(property)) {
		// ((PolylineConnection) getFigure()).setLineStyle(((Connection)
		// getModel()).getLineStyle());
		// }
	}
}

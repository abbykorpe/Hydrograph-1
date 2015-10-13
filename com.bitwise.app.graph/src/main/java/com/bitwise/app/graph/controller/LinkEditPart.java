package com.bitwise.app.graph.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RoutingAnimator;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import com.bitwise.app.graph.model.Link;
import com.bitwise.app.graph.policy.LinkEditPolicy;
import com.bitwise.app.graph.policy.LinkEndPointEditPolicy;

public class LinkEditPart extends AbstractConnectionEditPart
		implements PropertyChangeListener {
	/**
	 * Upon activation, attach to the model element as a property change
	 * listener.
	 */
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

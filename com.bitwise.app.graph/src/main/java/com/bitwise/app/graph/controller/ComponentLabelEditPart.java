package com.bitwise.app.graph.controller;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;

import com.bitwise.app.graph.figure.ComponentLabelFigure;
import com.bitwise.app.graph.model.Component;
import com.bitwise.app.graph.model.ComponentLabel;

/**
 * The Class ComponentLabelEditPart.
 * @author Bitwise
 */

public class ComponentLabelEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener{
	protected AccessibleEditPart createAccessible() {
		return new AccessibleGraphicalEditPart() {
			public void getValue(AccessibleControlEvent e) {
				e.result = getLogicLabel().getLabelContents();
			}

			public void getName(AccessibleEvent e) {
				e.result = "Label";
			}
		};
	}

	
	protected IFigure createFigure() {
		ComponentLabelFigure label = new ComponentLabelFigure(1);
		Component component = ((ComponentEditPart) getParent()).getCastedModel();
		String compLabel = component.getComponentLabel().getLabelContents();
		
		label.setText(compLabel);
		label.setLocation(new Point(1, 1));
		return label;
	}

	private ComponentLabel getLogicLabel() {
		return (ComponentLabel) getModel();
	}


	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equalsIgnoreCase("labelContents"))//$NON-NLS-1$
			refreshVisuals();
		else if (evt.getPropertyName().equalsIgnoreCase("size"))
			refreshVisuals();
	}

	protected void refreshVisuals() {
		
		((ComponentLabelFigure) getFigure()).setText(getLogicLabel()
				.getLabelContents());
		super.refreshVisuals();
	}


	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
		
	}

}

package com.bitwise.app.engine.ui.converter;

import org.eclipse.draw2d.geometry.Dimension;

import com.bitwise.app.engine.ui.constants.UIComponentsPort;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
import com.bitwiseglobal.graph.commontypes.TypeBaseInSocket;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullComponent;
import com.bitwiseglobal.graph.commontypes.TypeStraightPullOutSocket;

public class StraightfullUiConverter extends UIConverter {

	@Override
	public void prepareUIXML() {
		// TODO Auto-generated method stub
		super.prepareUIXML();
		getInPort((TypeStraightPullComponent) typeBaseComponent);
		getOutPort((TypeStraightPullComponent) typeBaseComponent);
	}

	protected void getInPort(TypeStraightPullComponent straightPullComponent) {
		int portCounter = 1;
		if (straightPullComponent.getInSocket() != null) {
			for (TypeBaseInSocket inSocket : straightPullComponent
					.getInSocket()) {
				uiComponent.engageInputPort((UIComponentsPort
						.getPortType(inSocket.getType())) + portCounter);
				UIComponentRepo.INSTANCE.getComponentLinkList().add(
						new LinkingData(inSocket.getFromComponentId(),
								straightPullComponent.getId(),
								straightPullComponent.getInSocket().get(0).getFromSocketId(),
								straightPullComponent.getInSocket().get(0).getId()
							));
				portCounter++;
			}
		}
	}

	protected void getOutPort(TypeStraightPullComponent straightPullComponent) {
		int portCounter = 1;
		Dimension newSize = uiComponent.getSize();
		if (straightPullComponent.getOutSocket() != null) {
			for (TypeStraightPullOutSocket outSocket : straightPullComponent.getOutSocket()) {
				uiComponent.engageOutputPort((UIComponentsPort.getPortType(outSocket.getType())) + portCounter);
				if (portCounter != 1) {
					uiComponent.setSize(newSize.expand(0, 15));
				}
				portCounter++;
			}
		}
	}
}

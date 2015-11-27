package com.bitwise.app.engine.ui.converter;

import com.bitwise.app.engine.ui.constants.UIComponentsPort;
import com.bitwiseglobal.graph.commontypes.TypeInputComponent;
import com.bitwiseglobal.graph.commontypes.TypeInputOutSocket;

public class InputUIConverter extends UIConverter {
		
		@Override
		public void prepareUIXML() {
		
			super.prepareUIXML();
			getOutPort((TypeInputComponent)typeBaseComponent);
		}
	protected void getOutPort(TypeInputComponent inputComponent) {
		int portCounter = 1;
		if (inputComponent.getOutSocket() != null) {
			for (TypeInputOutSocket outSocket : inputComponent.getOutSocket()) {
				uiComponent.engageOutputPort((UIComponentsPort.getPortType(outSocket.getType())) + portCounter);
				portCounter++;
			}
		}
	}
}
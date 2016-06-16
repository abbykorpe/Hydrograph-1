/*******************************************************************************
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
 *******************************************************************************/
package hydrograph.engine.core.helper;

import hydrograph.engine.core.entity.Link;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeBaseInSocket;
import hydrograph.engine.jaxb.commontypes.TypeBaseOutSocket;
import hydrograph.engine.jaxb.commontypes.TypeInputComponent;
import hydrograph.engine.jaxb.commontypes.TypeOperationsComponent;
import hydrograph.engine.jaxb.commontypes.TypeOutputComponent;
import hydrograph.engine.jaxb.commontypes.TypeStraightPullComponent;
import hydrograph.engine.jaxb.main.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkGenerator {

	private Map<String, Link> linkMap;
	private List<TypeBaseComponent> componentFactoryMap;

	public LinkGenerator(Graph graph) {
		linkMap = new HashMap<String, Link>();
		this.componentFactoryMap = graph.getInputsOrOutputsOrStraightPulls();
	}

	public Map<String, Link> getLink() {
		for (TypeBaseComponent baseComponent : componentFactoryMap) {
			linkMap.put(baseComponent.getId(), generateLink(baseComponent));
		}
		return linkMap;
	}

	private Link generateLink(TypeBaseComponent component) {
		List<? extends TypeBaseOutSocket> outSocketList = new ArrayList<TypeBaseOutSocket>();
		List<? extends TypeBaseInSocket> inSocketList = new ArrayList<TypeBaseInSocket>();

		if (component instanceof TypeInputComponent)
			outSocketList = ((TypeInputComponent) component).getOutSocket();
		else if (component instanceof TypeOutputComponent)
			inSocketList = ((TypeOutputComponent) component).getInSocket();
		else if (component instanceof TypeStraightPullComponent) {
			outSocketList = ((TypeStraightPullComponent) component)
					.getOutSocket();
			inSocketList = ((TypeStraightPullComponent) component)
					.getInSocket();
		} else if (component instanceof TypeOperationsComponent) {
			outSocketList = ((TypeOperationsComponent) component).getOutSocket();
			inSocketList = ((TypeOperationsComponent) component).getInSocket();
		}
		return new Link(inSocketList, outSocketList);
	}

}
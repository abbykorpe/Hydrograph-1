package com.bitwise.app.engine.ui.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.bitwise.app.engine.ui.converter.LinkingData;
import com.bitwise.app.graph.model.Component;
import com.bitwiseglobal.graph.commontypes.TypeBaseComponent;
import com.bitwiseglobal.graph.main.Graph;

public class UIComponentRepo {

	private  LinkedHashMap<String, Class> componentFactory = new LinkedHashMap<>();
	private  LinkedHashMap<String, Component> componentUiFactory = new LinkedHashMap<>();
	private  LinkedHashMap<String, List<ParameterData>> parammeterFactory = new LinkedHashMap<>();
	private List<LinkingData> componentLinkList=new ArrayList<LinkingData>();
	
	public static final UIComponentRepo INSTANCE = new UIComponentRepo();
	
	public void genrateComponentRepo(Graph graph) {

		for (Object component : graph.getInputsOrOutputsOrStraightPulls()) {
			getComponentFactory().put(((TypeBaseComponent) component).getId(),
					component.getClass());
		}
	}

	public  LinkedHashMap<String, Class> getComponentFactory() {
		return componentFactory;
	}

	public  LinkedHashMap<String, Component> getComponentUiFactory() {
		return componentUiFactory;
	}
	
	public List<LinkingData> getComponentLinkList() {
		return componentLinkList;
	}
	
	public LinkedHashMap<String, List<ParameterData>> getParammeterFactory() {
		return parammeterFactory;
	}

	public void flusRepository() {
		componentFactory.clear();
		componentUiFactory.clear();
		parammeterFactory.clear();
		componentLinkList.clear();
		}
}

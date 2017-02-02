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
package hydrograph.engine.component.mapping;

import hydrograph.engine.adapters.base.BaseAdapter;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.main.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentAdapterFactory {

	Map<String, BaseAdapter> componentAdapterMap = new HashMap<String, BaseAdapter>();
	private static Logger LOG = LoggerFactory.getLogger(ComponentAdapterFactory.class);

	public ComponentAdapterFactory(Graph graph) {
		generateComponentAdapterMap(graph.getInputsOrOutputsOrStraightPulls());
		LOG.info("Component Adapter map created successfully");
	}

	private void generateComponentAdapterMap(List<TypeBaseComponent> obj) {
		for (TypeBaseComponent eachObj : obj) {
			componentAdapterMap.put(eachObj.getId(), getComponentAdapterObject(eachObj));

		}
	}

	public Map<String, BaseAdapter> getAssemblyGeneratorMap() {
		return componentAdapterMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private BaseAdapter getComponentAdapterObject(TypeBaseComponent baseComponent) {
		Class adapterClass;
		ComponentAdapterMapping componentAdapterMapping = ComponentAdapterMapping
				.valueOf(baseComponent.getClass().getSimpleName().toUpperCase());

		try {
			adapterClass = Class.forName(componentAdapterMapping.getMappingType(baseComponent));
		} catch (ClassNotFoundException e) {
			throw new ComponentAdapterFactoryException(e);
		}
		Constructor constructor;
		try {
			constructor = adapterClass.getDeclaredConstructor(TypeBaseComponent.class);
		} catch (SecurityException e) {
			throw new ComponentAdapterFactoryException(e);
		} catch (NoSuchMethodException e) {
			throw new ComponentAdapterFactoryException(e);
		}
		try {
			return (BaseAdapter) constructor.newInstance(baseComponent);
		} catch (IllegalArgumentException e) {
			throw new ComponentAdapterFactoryException(e);
		} catch (InstantiationException e) {
			throw new ComponentAdapterFactoryException(e);
		} catch (IllegalAccessException e) {
			throw new ComponentAdapterFactoryException(e);
		} catch (InvocationTargetException e) {
			throw new ComponentAdapterFactoryException(e);
		}
	}

	public class ComponentAdapterFactoryException extends RuntimeException {
		private static final long serialVersionUID = -3680200162227543907L;

		public ComponentAdapterFactoryException(Throwable e) {
			super(e);
		}

		public ComponentAdapterFactoryException(String msg) {
			super(msg);
		}
	}
}
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
package hydrograph.engine.cascading.assembly.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.engine.cascading.assembly.generator.base.GeneratorBase;
import hydrograph.engine.cascading.assembly.utils.ComponentAssemblyMapping;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.main.Graph;

public class AssemblyGeneratorFactory {

	Map<String, GeneratorBase> assemblyGeneratorMap = new HashMap<String, GeneratorBase>();
	private static Logger LOG = LoggerFactory.getLogger(AssemblyGeneratorFactory.class);

	public AssemblyGeneratorFactory(Graph graph) {
		generateAssemblyGeneratorMap(graph.getInputsOrOutputsOrStraightPulls());
		LOG.info("Assembly generator map created successfully");
	}

	private void generateAssemblyGeneratorMap(List<TypeBaseComponent> obj) {
		for (TypeBaseComponent eachObj : obj) {
			assemblyGeneratorMap.put(eachObj.getId(), getAssemblyGeneratorObject(eachObj));

		}
	}

	public Map<String, GeneratorBase> getAssemblyGeneratorMap() {
		return assemblyGeneratorMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private GeneratorBase getAssemblyGeneratorObject(TypeBaseComponent baseComponent) {
		Class assemblyClass;
		ComponentAssemblyMapping componentAssemblyMapping = ComponentAssemblyMapping
				.valueOf(baseComponent.getClass().getSimpleName().toUpperCase());

		try {
			assemblyClass = Class.forName(componentAssemblyMapping.getMappingType(baseComponent));
		} catch (ClassNotFoundException e) {
			throw new AssemblyFactoryException(e);
		}
		Constructor constructor;
		try {
			constructor = assemblyClass.getDeclaredConstructor(TypeBaseComponent.class);
		} catch (SecurityException e) {
			throw new AssemblyFactoryException(e);
		} catch (NoSuchMethodException e) {
			throw new AssemblyFactoryException(e);
		}
		try {
			return (GeneratorBase) constructor.newInstance(baseComponent);
		} catch (IllegalArgumentException e) {
			throw new AssemblyFactoryException(e);
		} catch (InstantiationException e) {
			throw new AssemblyFactoryException(e);
		} catch (IllegalAccessException e) {
			throw new AssemblyFactoryException(e);
		} catch (InvocationTargetException e) {
			throw new AssemblyFactoryException(e);
		}
	}

	public class AssemblyFactoryException extends RuntimeException {
		private static final long serialVersionUID = -3680200162227543907L;

		public AssemblyFactoryException(Throwable e) {
			super(e);
		}

		public AssemblyFactoryException(String msg) {
			super(msg);
		}
	}
}
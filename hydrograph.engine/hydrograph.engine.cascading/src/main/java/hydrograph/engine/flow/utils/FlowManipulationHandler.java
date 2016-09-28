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
package hydrograph.engine.flow.utils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.engine.core.core.HydrographJob;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.jaxb.main.Graph;
import hydrograph.engine.batchbreak.plugin.BatchBreakPlugin;
import hydrograph.engine.schemapropagation.SchemaFieldHandler;
import hydrograph.engine.utilities.OrderedProperties;
import hydrograph.engine.utilities.OrderedPropertiesHelper;

/**
 * @author gurdits
 *
 */
public class FlowManipulationHandler {

	enum Plugins {
		batchBreak {
			@Override
			public String getPluginName() {
				return BatchBreakPlugin.class.getName();
			}
		};
		public abstract String getPluginName();
	}

	private static Logger LOG = LoggerFactory.getLogger(FlowManipulationHandler.class);
	private static List<TypeBaseComponent> jaxbComponents;
	private static TypeProperties jaxbJobLevelRuntimeProperties;
	private static String jobName;

	/**
	 * @param flowManipulationContext
	 * @return the HydrographJob
	 */
	public static HydrographJob execute(FlowManipulationContext flowManipulationContext) {

		jaxbComponents = flowManipulationContext.getJaxbMainGraph();
		jaxbJobLevelRuntimeProperties = flowManipulationContext.getJaxbJobLevelRuntimeProperties();
		jobName = flowManipulationContext.getGraphName();
		OrderedProperties properties = new OrderedProperties();
		try {
			properties = OrderedPropertiesHelper.getOrderedProperties("RegisterPlugin.properties");
		} catch (IOException e) {
			throw new RuntimeException("Error reading the properties file: RegisterPlugin.properties" + e);
		}
		for (String pluginName : addPluginFromFile(properties)) {
			jaxbComponents = executePlugin(pluginName, flowManipulationContext);
			flowManipulationContext.setJaxbMainGraph(jaxbComponents);
			flowManipulationContext.setSchemaFieldMap(new SchemaFieldHandler(jaxbComponents));
		}

		return getJaxbObject();
	}

	private static List<String> addPluginFromFile(OrderedProperties properties) {
		List<String> registerdPlugins = new LinkedList<String>();
		// added batchbreak plugin.
		registerdPlugins.addAll(addDefaultPlugins());
		for (Object plugin : properties.values()) {
			registerdPlugins.add(plugin.toString());
		}
		return registerdPlugins;
	}

	private static List<String> addDefaultPlugins() {
		List<String> registerdPlugins = new LinkedList<String>();
		for (Plugins plugin : Plugins.values())
			registerdPlugins.add(plugin.getPluginName());
		return registerdPlugins;
	}

	private static HydrographJob getJaxbObject() {
		Graph graph = new Graph();
		graph.getInputsOrOutputsOrStraightPulls().addAll(jaxbComponents);
		graph.setRuntimeProperties(jaxbJobLevelRuntimeProperties);
		graph.setName(jobName);
		return new HydrographJob(graph);
	}

	private static List<TypeBaseComponent> executePlugin(String clazz,
			FlowManipulationContext flowManipulationContext) {
		try {
			Class assemblyClass = Class.forName(clazz);
			Constructor constructor = assemblyClass.getDeclaredConstructor();
			ManipulatorListener inst = (ManipulatorListener) constructor.newInstance();
			return inst.execute(flowManipulationContext);

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
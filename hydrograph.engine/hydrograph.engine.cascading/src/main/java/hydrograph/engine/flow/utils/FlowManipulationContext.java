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

import hydrograph.engine.assembly.entity.elements.SchemaField;
import hydrograph.engine.core.core.HydrographDebugInfo;
import hydrograph.engine.core.core.HydrographJob;
import hydrograph.engine.jaxb.commontypes.TypeBaseComponent;
import hydrograph.engine.jaxb.commontypes.TypeProperties;
import hydrograph.engine.schemapropagation.SchemaFieldHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FlowManipulationContext {

	private HydrographDebugInfo jaxbDebugGraph;
	private SchemaFieldHandler schemaFieldMap;
	private List<TypeBaseComponent> jaxbMainGraph;
	private String jobId;
	private String basePath;
	private TypeProperties jaxbJobLevelRuntimeProperties;
	private String graphName;

	public FlowManipulationContext(HydrographJob hydrographJob, HydrographDebugInfo bhsDebug,
			SchemaFieldHandler schemaFieldHandler, String jobId, String basePath) {
		this.jaxbMainGraph = hydrographJob.getJAXBObject().getInputsOrOutputsOrStraightPulls();
		this.jaxbJobLevelRuntimeProperties = hydrographJob.getJAXBObject().getRuntimeProperties();
		this.graphName = hydrographJob.getJAXBObject().getName();
		this.jobId = jobId;
		this.jaxbDebugGraph = bhsDebug;
		this.schemaFieldMap = schemaFieldHandler;
		this.basePath = basePath;
	}
	
	public String getGraphName() {
		return graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public TypeProperties getJaxbJobLevelRuntimeProperties() {
		return jaxbJobLevelRuntimeProperties;
	}

	public void setJaxbJobLevelRuntimeProperties(TypeProperties jaxbRuntimeProperties) {
		this.jaxbJobLevelRuntimeProperties = jaxbRuntimeProperties;
	}

	public String getJobId(){
		return jobId;
	}
	
	public String getBasePath(){
		return basePath;
	}

	public Map<String, Set<SchemaField>> getSchemaFieldMap() {
		return schemaFieldMap.getSchemaFieldMap();
	}

	public List<TypeBaseComponent> getJaxbMainGraph() {
		return jaxbMainGraph;
	}

	public void setJaxbMainGraph(List<TypeBaseComponent> jaxbMainGraph) {
		this.jaxbMainGraph = jaxbMainGraph;
	}

	public HydrographDebugInfo getJaxbDebugGraph() {
		return jaxbDebugGraph;
	}

}

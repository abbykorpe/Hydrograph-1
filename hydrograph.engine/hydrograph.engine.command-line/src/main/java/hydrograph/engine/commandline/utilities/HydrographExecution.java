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
package hydrograph.engine.commandline.utilities;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.engine.core.core.HydrographDebugInfo;
import hydrograph.engine.core.core.HydrographJob;
import hydrograph.engine.core.core.HydrographRuntimeService;
import hydrograph.engine.core.props.PropertiesLoader;
import hydrograph.engine.core.xmlparser.HydrographXMLInputService;
import hydrograph.engine.core.xmlparser.XmlParsingUtils;
import hydrograph.engine.core.xmlparser.parametersubstitution.CommandLineOptionsProcessor;
import hydrograph.engine.execution.tracking.ComponentInfo;
import hydrograph.engine.execution.tracking.JobInfo;
import hydrograph.engine.utilities.GeneralUtilities;

public class HydrographExecution {

	private GeneralCommandLineUtilities generalUtilities;
	private PropertiesLoader propertiesLoader;
	private HydrographRuntimeService runtimeService;
	private HydrographXMLInputService hydrographXmlInputService;
	private HydrographJob hydrographJob;
	private HydrographDebugInfo bhsDebug;

	private static Logger LOG = LoggerFactory.getLogger(HydrographExecution.class);

	public HydrographExecution() {
		this.propertiesLoader = PropertiesLoader.getInstance();
		this.generalUtilities = new GeneralCommandLineUtilities();
		this.hydrographXmlInputService = new HydrographXMLInputService();
		loadService();
	}

	public static void main(String args[]) throws Exception {
		
		
		if(GeneralUtilities.IsArgOptionPresent(args, CommandLineOptionsProcessor.OPTION_HELP)){
			
			CommandLineOptionsProcessor.printUsage();
		}
		else{
		HydrographExecution execution = new HydrographExecution();
		execution.run(args);
		}
		
	}

	public void run(String[] args) throws Exception {
		hydrographJob = createHydrographJob(args);
		bhsDebug = createHydrographDebugInfo(args);
		initialization(args, hydrographJob, bhsDebug,
				XmlParsingUtils.getJobId(args),
				XmlParsingUtils.getBasePath(args));
		prepareToExecute();
		finalExecute();
	}
	
	public List<ComponentInfo> getExecutionStatus(){
		return (List<ComponentInfo>) runtimeService.getExecutionStatus();
	}

	private HydrographJob createHydrographJob(String[] args) throws JAXBException {
		LOG.info("Invoking input service");
		return hydrographXmlInputService.parseHydrographJob(
				propertiesLoader.getInputServiceProperties(), args);
	}

	private HydrographDebugInfo createHydrographDebugInfo(String[] args) throws JAXBException {
		LOG.info("Invoking input service");
		return hydrographXmlInputService.parseHydrographDebugInfo(
				propertiesLoader.getInputServiceProperties(), args);
	}

	private void initialization(String[] args, HydrographJob bhsGraph,
			HydrographDebugInfo bhsDebug, String jobId, String basePath) {
		LOG.info("Invoking initialize on runtime service");
		runtimeService.initialize(
				propertiesLoader.getRuntimeServiceProperties(), args, bhsGraph,
				bhsDebug, jobId,basePath);
	}

	private void prepareToExecute()  {
		runtimeService.prepareToExecute();
		LOG.info("Preparation completed. Now starting execution");
	}

	private void finalExecute()  {
		try {
			runtimeService.execute();
		} finally {
			LOG.info("Invoking on complete for cleanup");
			runtimeService.oncomplete();
		}
		LOG.info("Graph '" + hydrographJob.getJAXBObject().getName()
				+ "' executed successfully!");
	}

	private void loadService() {
		runtimeService = (HydrographRuntimeService) generalUtilities
				.loadAndInitClass(propertiesLoader.getRuntimeServiceClassName());
	}
	
	/**
	 * Method to kill the job
	 */
	public void kill() {
		runtimeService.kill();
	}

}

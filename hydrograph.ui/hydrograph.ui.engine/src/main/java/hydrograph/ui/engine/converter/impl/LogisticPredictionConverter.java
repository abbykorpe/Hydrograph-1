/********************************************************************************
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
 ******************************************************************************/

package hydrograph.ui.engine.converter.impl;

import hydrograph.engine.jaxb.commandtypes.RunProgram;
import hydrograph.engine.jaxb.commandtypes.RunProgram.Command;
import hydrograph.ui.common.util.Constants;
import hydrograph.ui.engine.converter.CommandConverter;
import hydrograph.ui.engine.helper.ConverterHelper;
import hydrograph.ui.graph.model.Component;
import hydrograph.ui.logging.factory.LogFactory;

import org.slf4j.Logger;

public class LogisticPredictionConverter extends CommandConverter {

	public static final Logger logger = LogFactory.INSTANCE.getLogger(LogisticPredictionConverter.class);

	public LogisticPredictionConverter(Component component) {
		super(component);
		this.baseComponent = new RunProgram();
		this.component = component;
		this.properties = component.getProperties();
		converterHelper = new ConverterHelper(component);
	}

	@Override
	public void prepareForXML() {
		logger.debug("Generating XML for :{}", properties.get(Constants.PARAM_NAME));
		super.prepareForXML();

		RunProgram runProgram = (RunProgram) baseComponent;

		String LogisticBaseValue = "/opt/spark-1.5.2-bin-hadoop2.6/bin/spark-submit --class SimpleScore --master local /home/hduser/pushp/gmspoc/target/scala-2.11/sample-project_2.11-1.0.jar ";
		String inputData = (String) properties.get("inputdata");
		String modelData = (String) properties.get("modeldata");
		String outputData = (String) properties.get("outputdata");
		String threshold  = (String) properties.get("thresholddata");

		Command command = new Command();
		String cmdValue = LogisticBaseValue + inputData + " " + modelData + " " + outputData+" "+threshold;

		command.setValue(cmdValue);

		runProgram.setCommand(command);

	}

}

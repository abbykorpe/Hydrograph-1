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

import java.util.Iterator;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.ParseException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hydrograph.engine.cascading.integration.HydrographRuntime;
import hydrograph.engine.core.core.HydrographJob;
import hydrograph.engine.core.xmlparser.HydrographXMLInputService;

public class JsonService {
	HydrographXMLInputService inputService;
	HydrographRuntime runtime;
	HydrographJob hydrographJob;
	private static Logger LOG = LoggerFactory.getLogger(JsonService.class);

	public JsonService() {
		inputService = new HydrographXMLInputService();
		runtime = new HydrographRuntime();
	}

	public void executeGraph(String[] args) throws JSONException, JAXBException, ParseException {
		hydrographJob = inputService.parseParameters(args);
		LOG.info("Hydrograph job '" + hydrographJob.getJAXBObject().getName() + "' generated successfully!");

		runtime.executeProcess(args, hydrographJob);
		LOG.info("Hydrograph job '" + hydrographJob.getJAXBObject().getName()
				+ "' runtime processing completed successfully!");
	}

	public static void main(String[] args) throws JSONException, JAXBException, ParseException {
		JsonService josJsonService = new JsonService();
		JSONObject obj = new JSONObject();
		JSONObject obj1 = new JSONObject();
		obj1.put("JOBDIR", "parameters");
		obj1.put("FILLER", "AND");

		obj.put("-paramfiles",
				"XML/parameters/file1.params,XML/parameters/file2.params,XML/parameters/account_schema.params");
		obj.put("-dotpath", "XML/parameters/file1");
		obj.put("-xmlpath", "XML/parameters/test_parameters.xml");
		obj.put("-param", obj1);
		LOG.info("Hydrograph job '" + josJsonService.hydrographJob.getJAXBObject().getName()
				+ "' generation processing started.");
		josJsonService.setArguments(obj);
		LOG.info("Hydrograph job '" + josJsonService.hydrographJob.getJAXBObject().getName()
				+ "' runtime processing completed successfully!");
	}

	public String[] setArguments(JSONObject obj) throws JSONException, JAXBException, ParseException {
		StringBuffer sb = new StringBuffer();
		String[] args = parse(obj, sb, null);
		executeGraph(args);
		return args;
	}

	public String[] parse(JSONObject json, StringBuffer sb, String mainKey) throws JSONException {
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			String val = null;
			try {
				JSONObject value = json.getJSONObject(key);
				mainKey = key;
				parse(value, sb, mainKey);
			} catch (Exception e) {

				val = json.getString(key);
			}

			if (val != null) {
				if (mainKey == null) {
					sb.append(" " + key + " " + val);
				} else {
					sb.append(" " + mainKey + " " + key + "=" + val);
				}
			}
		}
		return sb.toString().split(" ");
	}
}
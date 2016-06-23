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

package hydrograph.ui.graph.utility;

import com.thoughtworks.xstream.XStream;

/**
 * This class configures XStream instance for backward compatibility.
 * 
 * @author Bitwise
 * 
 */
public class XStreamUtil {

	public static final XStreamUtil INSTANCE = new XStreamUtil();
	private XStream xStream;

	private XStreamUtil() { /* Singleton */
	}

	/**
	 * Configures and returns XStream instance.
	 * 
	 * @return
	 */
	public XStream getXStreamInstance() {
		xStream = new XStream();
		addAlias();
		addAnnotations();
		addFieldsToOmitWhileLoadingJobFile();
		ignoreUnknownElements();
		return xStream;
	}

	private void ignoreUnknownElements() {
		xStream.ignoreUnknownElements();
	}

	/*
	 * Mention specific fields which will be ignored from a class while loading job file. e.g.
	 * xStream.omitField(Schema.class, "externalSchemaPath");
	 */
	private void addFieldsToOmitWhileLoadingJobFile() {

	}

	private void addAnnotations() {
		xStream.autodetectAnnotations(true);
	}

	private void addAlias() {
		// Add alias e.g xStream.alias("Job", Container.class);'
	}

}

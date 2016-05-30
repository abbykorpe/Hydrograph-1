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
package hydrograph.engine.core.xmlparser.externalschema;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import hydrograph.engine.core.xmlparser.XmlParsingUtils;
import hydrograph.engine.core.xmlparser.externalschema.ParseExternalSchema;
import hydrograph.engine.core.xmlparser.parametersubstitution.ParameterSubstitutor;
import hydrograph.engine.core.xmlparser.parametersubstitution.UserParameters;

public class ParseExternalSchemaTest {

	private static Logger LOG = LoggerFactory
			.getLogger(ParseExternalSchemaTest.class);
	
	@Test
	public void itShouldGetTheExternalFields() {

		String[] args = new String[] { "-xmlpath",
				"testData/XMLFiles/DelimitedInputAndOutput.xml", "-param",
				"PATH=testData/Input/delimitedInputFile","-param","PATH2=testData/XMLFiles/schema2.xml" };
		try {
			ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor(
					new UserParameters(args));

			String xmlContents = XmlParsingUtils.getXMLStringFromPath(args[1]);

			ParseExternalSchema parseExternalSchema = new ParseExternalSchema(
					parameterSubstitutor, xmlContents);

			Document xmlDom = parseExternalSchema.getXmlDom();

			NodeList nodes = xmlDom.getElementsByTagName("schema");

			Assert.assertEquals(3,nodes.item(0).getChildNodes().getLength());
		} catch (IOException e) {
			LOG.error("", e);
		}
	}
}

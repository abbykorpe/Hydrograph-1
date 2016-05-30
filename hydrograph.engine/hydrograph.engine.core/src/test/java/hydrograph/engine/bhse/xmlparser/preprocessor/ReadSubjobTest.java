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
package hydrograph.engine.bhse.xmlparser.preprocessor;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import hydrograph.engine.core.utilities.XmlUtilities;
import hydrograph.engine.core.xmlparser.XmlParsingUtils;
import hydrograph.engine.core.xmlparser.parametersubstitution.ParameterSubstitutor;
import hydrograph.engine.core.xmlparser.parametersubstitution.UserParameters;
import hydrograph.engine.core.xmlparser.subjob.ReadSubjob;

public class ReadSubjobTest {

	private static Logger LOG = LoggerFactory.getLogger(ReadSubjobTest.class);

	@Test
	public void readInputSubjob() {

		String[] args = new String[] { "-xmlpath",
				"testData/XMLFiles/inputSubjob/Mainjob.xml",
				"-param", "x=zxc" };
		try {
			String xmlContents = XmlParsingUtils.getXMLStringFromPath(args[1]);

			ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor(
					new UserParameters(args));

			ReadSubjob subjobReader = new ReadSubjob(
					parameterSubstitutor.substitute(xmlContents));

			Document expandedXmlDocument = subjobReader.getSubjobDom();

			xmlContents = XmlUtilities
					.getXMLStringFromDocument(expandedXmlDocument);
			
			Assert.assertTrue(xmlContents.contains("<inputs id=\"inputSubjob.input1\" phase=\"0\" xsi:type=\"it:textFileDelimited\">"));
			Assert.assertTrue(xmlContents.contains("<schema name=\"zxc\">"));
			Assert.assertTrue(xmlContents.contains("<operations id=\"inputSubjob.reformat\" xsi:type=\"op:transform\">"));
			Assert.assertTrue(xmlContents.contains("<inSocket fromComponentId=\"inputSubjob.input1\" fromSocketId=\"out0\" id=\"in0\"/>"));
			Assert.assertTrue(xmlContents.contains("<outputs id=\"output1\" phase=\"0\" xsi:type=\"ot:textFileDelimited\">"));
			Assert.assertTrue(xmlContents.contains("<inSocket fromComponentId=\"inputSubjob.reformat\" fromSocketId=\"out0\" id=\"in0\">"));

		} catch (IOException e) {
			LOG.error("", e);
		}
	}
	
	@Test
	public void readOperationSubjob() {

		String[] args = new String[] { "-xmlpath",
				"testData/XMLFiles/operationSubjob/Mainjob.xml",
				"-param", "x=zxc" };
		try {
			String xmlContents = XmlParsingUtils.getXMLStringFromPath(args[1]);

			ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor(
					new UserParameters(args));

			ReadSubjob subjobReader = new ReadSubjob(
					parameterSubstitutor.substitute(xmlContents));

			Document expandedXmlDocument = subjobReader.getSubjobDom();

			xmlContents = XmlUtilities
					.getXMLStringFromDocument(expandedXmlDocument);

			Assert.assertTrue(xmlContents.contains("<inputs id=\"inputSubjob.input1\" phase=\"0\" xsi:type=\"it:textFileDelimited\">"));
			Assert.assertTrue(xmlContents.contains("<schema name=\"zxc\">"));
			Assert.assertTrue(xmlContents.contains("<operations id=\"inputSubjob.reformat\" xsi:type=\"op:transform\">"));
			Assert.assertTrue(xmlContents.contains("<inSocket fromComponentId=\"inputSubjob.input1\" fromSocketId=\"out0\" id=\"in0\"/>"));
			Assert.assertTrue(xmlContents.contains("<straightPulls id=\"operationSubjob.clone\" phase=\"0\" xsi:type=\"spt:clone\">"));
			Assert.assertTrue(xmlContents.contains("<inSocket fromComponentId=\"inputSubjob.reformat\" fromSocketId=\"out0\" id=\"in0\"/>"));
			Assert.assertTrue(xmlContents.contains("<outputs id=\"operationSubjob.output2\" xsi:type=\"ot:textFileDelimited\">"));
			Assert.assertTrue(xmlContents.contains("<inSocket fromComponentId=\"operationSubjob.clone\" fromSocketId=\"out0\" id=\"in0\">"));
			Assert.assertTrue(xmlContents.contains("<outputs id=\"outputSubjob.output1\" xsi:type=\"ot:textFileDelimited\">"));
			Assert.assertTrue(xmlContents.contains("<inSocket fromComponentId=\"operationSubjob.clone\" fromSocketId=\"out1\" id=\"in0\">"));
			
		} catch (IOException e) {
			LOG.error("", e);
		}
	}
	
}


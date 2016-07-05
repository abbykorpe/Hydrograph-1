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

import static org.junit.Assert.*;

import java.io.IOException;

import hydrograph.engine.core.xmlparser.XmlParsingUtils;
import hydrograph.engine.core.xmlparser.parametersubstitution.ParameterSubstitutor;
import hydrograph.engine.core.xmlparser.parametersubstitution.UserParameters;

import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParameterSubstitutorTest {
	private static Logger LOG = LoggerFactory
			.getLogger(ParameterSubstitutorTest.class);
	@Test
	public void itShouldSubstitueParameters() {
		String[] args = new String[] { "-xmlpath",
				"testData/XMLFiles/DelimitedInputAndOutput.xml", "-param",
				"input=input'1","-param",
				"out&put=output" };
		ParameterSubstitutor parameterSubstitutor = null;
			try {
				parameterSubstitutor = new ParameterSubstitutor(
						new UserParameters(args));
			} catch (IOException e) {
				LOG.error("",e);
				throw new RuntimeException(e);
			} catch (ParseException e) {
				LOG.error("",e);
				throw new RuntimeException(e);
			}

			String xmlContents = parameterSubstitutor.substitute(XmlParsingUtils.getXMLStringFromPath(args[1]));
			assertTrue(xmlContents.contains("input&apos;1"));
			assertTrue(xmlContents.contains("output"));
	}

}

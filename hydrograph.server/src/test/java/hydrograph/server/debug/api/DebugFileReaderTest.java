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
package hydrograph.server.debug.api;

import hydrograph.server.debug.api.DebugFilesReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class DebugFileReaderTest {

	private static Logger LOG = Logger.getLogger(DebugFilesReader.class);

	@Test
	public void test() throws FileNotFoundException,
			UnsupportedEncodingException {

		String workingDir = System.getProperty("user.dir");
		DebugFilesReader datareader = new DebugFilesReader(workingDir
				+ "/testData", "JobId", "Input1", "out0", 1200, ',', '\\', '"');

		LOG.info("Current working dir: " + workingDir);

		try {
			while (datareader.hasNext()) {
				StringBuilder string = datareader.next();
				LOG.info("string : " + string);
			}
			// DebugDataReader.delete("testData","JobId");
			Assert.assertEquals("Test passed", "passed", "passed");
		} catch (IOException e) {
			LOG.error("", e);
			Assert.assertEquals("Test failed", "passed", "failed");
		}
		// Please create folder hierarchy such as
		// %USER_HOME_DIRECTORY%/testData/debug/JobId/input1_out0 if you want to
		// rerun delete operation.
		// Input files are placed in /testData/Input/avroFiles
	}

}

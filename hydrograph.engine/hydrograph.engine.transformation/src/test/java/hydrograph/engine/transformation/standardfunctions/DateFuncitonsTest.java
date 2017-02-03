/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 *******************************************************************************/
package hydrograph.engine.transformation.standardfunctions;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

public class DateFuncitonsTest {

	@Test
	public void itShouldGetDays() {
		Integer actual = DateFunctions.today();
		Assert.assertNotNull(actual);
	}

	@Test
	public void itShouldGetDateTime() {
		String actual = DateFunctions.now();
		Assert.assertNotNull(actual);
	}

	@Test
	public void itShouldFormatTheDateFromString() {
		String actual = null;
		try {
			actual = DateFunctions.dateFormatter("20150512", "yyyyMMdd",
					"dd-MM-yyyy");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String expectedDt = "12-05-2015";

		Assert.assertEquals(expectedDt, actual);
	}

	@Test
	public void itShouldFormatTheDateFromDecimal() {
		String actual = null;
		try {
			actual = DateFunctions.dateFormatter(20150512, "yyyyMMdd",
					"yyyy-MM-dd");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String expectedDt = "2015-05-12";

		Assert.assertEquals(expectedDt, actual);
	}

	@Test
	public void itShouldFormatTheDateFromString1() {
		String actual = null;
		try {
			actual = DateFunctions.dateFormatter("2015/05/12", "yyyy/MM/dd",
					"dd-MM-yyyy");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String expectedDt = "12-05-2015";

		Assert.assertEquals(expectedDt, actual);
	}

}

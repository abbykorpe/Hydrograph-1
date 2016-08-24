/*******************************************************************************
 *  Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package hydrograph.server.debug.lingual;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathNotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import cascading.lingual.type.SQLDateCoercibleType;

/**
 * Created by santlalg on 6/27/2016.
 */

public class LingualSchemaCreatorTest {

	static JSONParser jsonParser;
	static Object obj;
	static JSONObject jsonObject;
	static String tableName = "test";
	static String stereotypeName = "test_stereo";
	static String inputPath = "testData/Input/JobId/Input1_out0";
	static String linugalMetaDataPath = "testData/MetaData/";
	static String processingSchema = "lingualschema";
	static String resultSchema = "resultschema";

	@BeforeClass
	public static void init() throws ClassNotFoundException, IOException, ParseException {

		String fieldNames[] = { "f1", "f2", "f3", "f4" };
		Type[] fieldTypes = { String.class, new SQLDateCoercibleType(), new SQLDateCoercibleType(), BigDecimal.class };

		new LingualSchemaCreator().createCatalog(linugalMetaDataPath, processingSchema,  tableName,
				stereotypeName, inputPath, fieldNames, fieldTypes);

		jsonParser = new JSONParser();
		FileReader file = new FileReader("testData/MetaData/.lingual/catalog");
		obj = jsonParser.parse(file);
		jsonObject = (JSONObject) obj;
		file.close();
	}

	@Test
	public void itShouldTestHadoop2Mr1Platform() {

		//given
		String expectedPlatform = "hadoop2-mr1";
		
		// when
		String platformName = (String) jsonObject.get("platformName");

		// then
		Assert.assertTrue(expectedPlatform.equals(platformName));
	}

	@Test
	public void itShouldTestExistenceOfSchema() throws ParseException {
		// given
		String expectedLingualSchema = "lingualschema";
		String expectedResultSchema = "resultSchema";

		// when
		JSONObject lingualSchObj = getLingualSchema(processingSchema);
		JSONObject resultSchObj = getLingualSchema(resultSchema);
		
		// then
		Assert.assertTrue(expectedLingualSchema.equals(lingualSchObj.get("name").toString()));
		Assert.assertTrue(expectedResultSchema.equals(resultSchObj.get("name").toString()));
	}

	@Test
	public void itShouldTestStereoTypeNameOfLingualSchema() throws ParseException {
		// given
		String expectedStereotypeName = "test_stereo";

		// when
		JSONObject lingualSchema = getLingualSchema(processingSchema);
		JSONArray jsonArray = (JSONArray) lingualSchema.get("stereotypes");
		String stereotypeName = null;
		for (Object obj : jsonArray) {
			JSONObject jsonObj = (JSONObject) obj;
			stereotypeName = (String) jsonObj.get("name");
		}

		// then
		Assert.assertTrue(expectedStereotypeName.equals(stereotypeName));

	}

	@Test
	public void itShouldTestFieldNameAndFieldType() throws ParseException {
		// given
		String expectedFieldName = "[\"f1\",\"f2\",\"f3\",\"f4\"]";
		String expectedFieldType = "[\"java.lang.String\",\"cascading.lingual.type.SQLDateCoercibleType\",\"cascading.lingual.type.SQLDateCoercibleType\",\"java.math.BigDecimal\"]";
		String fieldName = null;
		String fieldType = null;

		// when
		JSONObject lingualSchema = getLingualSchema(processingSchema);
		JSONArray jsonArray = (JSONArray) lingualSchema.get("stereotypes");
		for (Object obj : jsonArray) {
			JSONObject jsonObj = (JSONObject) obj;
			JSONObject rootSchema1 = (JSONObject) jsonParser.parse(jsonObj.get("fields").toString());
			fieldName = rootSchema1.get("names").toString();
			fieldType = rootSchema1.get("types").toString();
		}

		// then
		Assert.assertEquals(expectedFieldName, fieldName);
		Assert.assertEquals(expectedFieldType, fieldType);
	}

	@Test
	public void itShouldTestLingualSchemaTableName_StereoTypeFor_Identifier_ForTable() throws ParseException {
		// given
		String expectedIdentifier = "testData/Input/JobId/Input1_out0";
		String expectedTableName = "test";
		String expectedStereotypeName = "test_stereo";

		// when 
		JSONObject lingualSchema = getLingualSchema(processingSchema);
		JSONObject childTableObject = (JSONObject) lingualSchema.get("childTables");
		JSONObject tableObject = (JSONObject) jsonParser.parse(childTableObject.get("test").toString());

		String identifier = tableObject.get("identifier").toString();
		String tableName = tableObject.get("name").toString();
		String stereotypeName = tableObject.get("stereotypeName").toString();

		// then
		Assert.assertEquals(expectedIdentifier, identifier);
		Assert.assertEquals(expectedTableName, tableName);
		Assert.assertEquals(expectedStereotypeName, stereotypeName);
	}

	private JSONObject getLingualSchema(String schema) throws ParseException {
		JSONObject childSchema = getChildSchema();
		JSONObject lingualSchema = (JSONObject) jsonParser.parse(childSchema.get(schema).toString());
		return lingualSchema;
	}

	private JSONObject getChildSchema() throws ParseException {
		JSONObject rootSchema = (JSONObject) jsonParser.parse(jsonObject.get("rootSchemaDef").toString());
		String default1 = (String) rootSchema.get("defaultProtocol");
		JSONObject childSchema = (JSONObject) jsonParser.parse(rootSchema.get("childSchemas").toString());
		return childSchema;
	}

	@AfterClass
	public static void cleanUp() {
		System.gc();
		Configuration configuration = new Configuration();
		FileSystem fileSystem = null;

		try {
			fileSystem = FileSystem.get(configuration);
			Path deletingFilePath = new Path("testData/MetaData/");
			if (!fileSystem.exists(deletingFilePath)) {
				throw new PathNotFoundException(deletingFilePath.toString());
			} else {

				boolean isDeleted = fileSystem.delete(deletingFilePath, true);
				if (isDeleted) {
					fileSystem.deleteOnExit(deletingFilePath);
				}
			}
			fileSystem.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

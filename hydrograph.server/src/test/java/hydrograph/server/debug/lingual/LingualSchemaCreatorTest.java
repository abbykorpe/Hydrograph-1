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
package hydrograph.server.debug.lingual;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cascading.lingual.type.SQLDateCoercibleType;

/**
 * Created by santlalg on 6/27/2016.
 */
public class LingualSchemaCreatorTest {

	JSONParser jsonParser;
	Object obj;
	JSONObject jsonObject;
	String tableName = "test";
	String stereotypeName="test_stereo";
	@Before
	public void init() throws ClassNotFoundException, IOException, ParseException {
		
		String inputPath = "testData/Input/JobId/Input1_out0";
		String fieldName[] = { "f1", "f2", "f3", "f4" };
		Type[] fieldType = { String.class, new SQLDateCoercibleType(), new SQLDateCoercibleType(), BigDecimal.class };

		new LingualSchemaCreator().createCatalog(tableName,stereotypeName ,inputPath, fieldName, fieldType);

		jsonParser = new JSONParser();
		obj = jsonParser.parse(new FileReader(".lingual/catalog"));
		jsonObject = (JSONObject) obj;
	}
	
	@Test
	public void itShouldTestHadoop2Mr1Platform() {

		//when
		String platformName = (String) jsonObject.get("platformName");
		
		//then
		Assert.assertEquals("hadoop2-mr1", platformName);
	}

	@Test
	public void itShouldTestExistenceOfSchema() throws ParseException {

		//when
		JSONObject childSchema = getChildSchema();
		
		//then
		Assert.assertTrue(childSchema.containsKey("lingualschema"));
	}

	@Test
	public void itShouldTestStereoTypeName() throws ParseException {
		//when
		JSONObject lingualSchema = getLingualSchema();
		JSONArray jsonArray = (JSONArray) lingualSchema.get("stereotypes");
		String stereotypeName = null;
		for (Object obj : jsonArray) {
			JSONObject jsonObj = (JSONObject) obj;
			stereotypeName = (String) jsonObj.get("name");
		}

		//then
		Assert.assertTrue(stereotypeName.equals("test_stereo"));

	}

	@Test
	public void itShouldTestFieldNameAndFieldType() throws ParseException {
		// given
		String expectedFieldName = "[\"f1\",\"f2\",\"f3\",\"f4\"]";
		String expectedFieldType = "[\"java.lang.String\",\"cascading.lingual.type.SQLDateCoercibleType\",\"cascading.lingual.type.SQLDateCoercibleType\",\"java.math.BigDecimal\"]";

		// when
		JSONObject lingualSchema = getLingualSchema();
		JSONArray jsonArray = (JSONArray) lingualSchema.get("stereotypes");
		String fieldName = null;
		String fieldType = null;
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
	public void itShouldTestTableName_StereoTypeFor_Identifier_ForTable() throws ParseException
	{
		//given
		String expectedIdentifier="testData/Input/JobId/Input1_out0";
		String expectedTableName="test";
		String expectedStereotypeName="test_stereo";
		
		//when
		JSONObject lingualSchema = getLingualSchema();
		JSONObject childTableObject = (JSONObject)lingualSchema.get("childTables");
		JSONObject tableObject = (JSONObject) jsonParser.parse(childTableObject.get("test").toString());
		
		String identifier = tableObject.get("identifier").toString();
		String tableName = tableObject.get("name").toString();
		String stereotypeName = tableObject.get("stereotypeName").toString();
		
		//then
		Assert.assertEquals(expectedIdentifier, identifier);
		Assert.assertEquals(expectedTableName,tableName);
		Assert.assertEquals(expectedStereotypeName,stereotypeName);
	}
	
	private JSONObject getLingualSchema() throws ParseException {
		JSONObject childSchema = getChildSchema();
		JSONObject lingualSchema = (JSONObject) jsonParser.parse(childSchema.get("lingualschema").toString());
		return lingualSchema;
	}

	private JSONObject getChildSchema() throws ParseException {
		JSONObject rootSchema = (JSONObject) jsonParser.parse(jsonObject.get("rootSchemaDef").toString());
		String default1 = (String) rootSchema.get("defaultProtocol");
		JSONObject childSchema = (JSONObject) jsonParser.parse(rootSchema.get("childSchemas").toString());
		return childSchema;
	}
	
	@After
	public void cleanup()
	{
		new LingualSchemaCreator().removeSteroTypeAndTable(tableName, stereotypeName);
	}

}

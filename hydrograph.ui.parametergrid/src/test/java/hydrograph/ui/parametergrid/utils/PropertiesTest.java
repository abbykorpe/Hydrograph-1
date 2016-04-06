package hydrograph.ui.parametergrid.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;

import hydrograph.ui.logging.factory.LogFactory;

public class PropertiesTest {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(PropertiesTest.class);
	
	@Test
	public void setPropertyByKeyValueTest(){		
		//Given		
		Properties properties = new Properties();
		String expectedProperties="Properties [propertyMap={TestKey=TestValue}]";
		
		//when
		properties.setProperty("TestKey", "TestValue");
		
		//then
		assertEquals(expectedProperties,properties.toString());
	}
	
	@Test
	public void setPropertyByMapTest(){		
		//Given		
		Properties properties = new Properties();
		String expectedProperties="Properties [propertyMap={TestKey=TestValue}]";
		
		Map<String,String> map= new LinkedHashMap<>();
		map.put("TestKey", "TestValue");
		
		//when
		properties.setProperty(map);
		
		//then
		assertEquals(expectedProperties,properties.toString());
	}
	
	@Test
	public void loadStorePropertiesTest(){
		//Given		
		Properties inputProperties = new Properties();
		String expectedProperties="Properties [propertyMap={TestKey1=TestValue1, TestKey2=TestValue2, TestKey3=TestValue3, TestKey4=TestValue4}]";		
		Map<String,String> map= new LinkedHashMap<>();
		map.put("TestKey1", "TestValue1");
		map.put("TestKey2", "TestValue2");
		map.put("TestKey3", "TestValue3");
		map.put("TestKey4", "TestValue4");
		
		//when
		inputProperties.setProperty(map);
		try {
			inputProperties.store("testfile.properties");
		} catch (IOException e) {
			logger.debug("Unable to store properties ", e);
		}
		
		Properties outputProperties = new Properties();
		try {
			outputProperties.load("testfile.properties");
		} catch (IOException e) {
			logger.debug("Unable to load properties ", e);
		}
		
		
		//then
		assertEquals(expectedProperties,inputProperties.toString());		
	}
	
}

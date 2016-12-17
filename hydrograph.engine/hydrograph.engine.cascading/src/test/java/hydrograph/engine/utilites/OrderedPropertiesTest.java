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
package hydrograph.engine.utilites;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import hydrograph.engine.core.component.utils.OrderedProperties;

/**
 * Unit tests for OrderedProperties class
 * 
 * @author Prabodh
 *
 */
public class OrderedPropertiesTest {

	/**
	 * Test whether {@link OrderedProperties#get(Object)} method returns the
	 * correct value
	 */
	@Test
	public void getMethodTest() {
		OrderedProperties properties = getOrderedPropertiesObject();
		Assert.assertEquals(properties.get("a"), "1");
	}

	/**
	 * Test whether {@link OrderedProperties#getProperty(String)} method returns
	 * the correct value
	 */
	@Test
	public void getPropertyMethodTest() {
		OrderedProperties properties = getOrderedPropertiesObject();
		Assert.assertEquals(properties.getProperty("a"), "1");
	}

	/**
	 * Test whether {@link OrderedProperties#getProperty(String, String)} method
	 * returns the correct value
	 */
	@Test
	public void getPropertyWithDefaultValueMethodTest() {
		OrderedProperties properties = getOrderedPropertiesObject();
		Assert.assertEquals(properties.getProperty("a", "default"), "1");
		Assert.assertEquals(properties.getProperty("abcd", "default"),
				"default");
	}

	/**
	 * Test whether {@link OrderedProperties#contains(Object)} works correctly
	 */
	@Test
	public void containsMethodTest() {
		OrderedProperties properties = getOrderedPropertiesObject();
		Assert.assertEquals(properties.contains("1"), true);
		Assert.assertEquals(properties.contains("99"), false);
	}

	/**
	 * Test whether {@link OrderedProperties#containsKey(Object)} works
	 * correctly
	 */
	@Test
	public void containsKeyMethodTest() {
		OrderedProperties properties = getOrderedPropertiesObject();
		Assert.assertEquals(properties.containsKey("a"), true);
		Assert.assertEquals(properties.contains("abcd"), false);
	}

	/**
	 * Test whether {@link OrderedProperties#containsValue(Object)} works
	 * correctly
	 */
	@Test
	public void containsValueMethodTest() {
		OrderedProperties properties = getOrderedPropertiesObject();
		Assert.assertEquals(properties.containsValue("1"), true);
		Assert.assertEquals(properties.containsValue("99"), false);
	}

	/**
	 * Test whether the {@link OrderedProperties#keys()} method returns the keys
	 * in correct order.
	 */
	@Test
	public void keysMethodOrderTest() {
		OrderedProperties properties = getOrderedPropertiesObject();

		Set<String> expected = getExpectedSet();

		Set<String> actual = new LinkedHashSet<String>();
		for (Enumeration e = properties.keys(); e.hasMoreElements();) {
			actual.add((String) e.nextElement());
		}
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test whether {@link OrderedProperties#remove(Object)} method works
	 * correctly
	 */
	@Test
	public void removeMethodTest() {
		OrderedProperties properties = getOrderedPropertiesObject();
		if (properties.containsKey("a")) {
			// Test whether the remove returns correct value
			Assert.assertEquals(properties.remove("a"), "1");

			Set<String> expected = getExpectedSet();
			// remove the value from expected set as well
			expected.remove("a");

			Set<String> actual = new LinkedHashSet<String>();
			for (Enumeration e = properties.keys(); e.hasMoreElements();) {
				actual.add((String) e.nextElement());
			}
			// Test after removal
			Assert.assertEquals(expected, actual);
		} else {
			throw new RuntimeException(
					"The default key values have changed. Update the 'keySetMethodRemoveKeyTest()' method to reflect this change. The method should remove an existing key.");
		}
	}

	/**
	 * Test whether the {@link OrderedProperties#keySet()} method returns the
	 * keys in correct order.
	 */
	@Test
	public void keySetMethodOrderTest() {
		OrderedProperties properties = getOrderedPropertiesObject();

		Set<String> expected = getExpectedSet();

		Set<String> actual = new LinkedHashSet<String>();
		for (Object key : properties.keySet()) {
			actual.add(key.toString());
		}
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test whether the {@link OrderedProperties#keySet()} method returns the
	 * keys in correct order even after removing a key from the keySet. Removing
	 * a key from keySet by the consumer should not have any effect on the
	 * properties object.
	 */
	@Test
	public void keySetMethodRemoveKeyTest() {
		OrderedProperties properties = getOrderedPropertiesObject();

		Set<String> expected = getExpectedSet();

		// Remove a key from keySet(). This should not have any effect on
		// the properties object
		if (!properties.keySet().remove("a")) {
			throw new RuntimeException(
					"The default key values have changed. Update the 'keySetMethodRemoveKeyTest()' method to reflect this change. The method should remove an existing key.");
		}

		Set<String> actual = new LinkedHashSet<String>();
		for (Object key : properties.keySet()) {
			actual.add(key.toString());
		}
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test whether the {@link OrderedProperties#propertyNames()} method returns
	 * the keys in correct order.
	 */
	@Test
	public void propertyNamesMethodOrderTest() {
		OrderedProperties properties = getOrderedPropertiesObject();

		Set<String> expected = getExpectedSet();

		Set<String> actual = new LinkedHashSet<String>();
		for (Enumeration e = properties.propertyNames(); e.hasMoreElements();) {
			actual.add((String) e.nextElement());
		}
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test whether the {@link OrderedProperties#stringPropertyNames()} method
	 * returns the keys in correct order.
	 */
	@Test
	public void stringPropertyNamesMethodOrderTest() {
		OrderedProperties properties = getOrderedPropertiesObject();

		Set<String> expected = getExpectedSet();

		Set<String> actual = new LinkedHashSet<String>();
		actual.addAll(properties.stringPropertyNames());
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test whether the {@link OrderedProperties#stringPropertyNames()} method
	 * returns the keys in correct order even after removing a key from the
	 * property names. Removing a key from property names by the consumer should
	 * not have any effect on the properties object.
	 */
	@Test
	public void stringPropertyNamesMethodRemoveKeyTest() {
		OrderedProperties properties = getOrderedPropertiesObject();

		Set<String> expected = getExpectedSet();

		Set<String> actual = new LinkedHashSet<String>();

		if (!properties.stringPropertyNames().remove("a")) {
			throw new RuntimeException(
					"The default key values have changed. Update the 'stringPropertyNamesMethodRemoveKeyTest()' method to reflect this change. The method should remove an existing key.");
		}

		actual.addAll(properties.stringPropertyNames());
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test whether the {@link OrderedProperties#entrySet()} method returns the
	 * keys in correct order.
	 */
	@Test
	public void entrySetMethodOrderTest() {
		OrderedProperties properties = getOrderedPropertiesObject();

		Set<String> expected = getExpectedSet();

		Set<String> actual = new LinkedHashSet<String>();
		for (Entry<Object, Object> e : properties.entrySet()) {
			actual.add(e.getKey().toString());
		}
		Assert.assertEquals(expected, actual);
	}

	/**
	 * Test whether the {@link OrderedProperties#putAll(java.util.Map)} method
	 * works correctly. When an ordered key list is passed using a
	 * {@link LinkedHashMap}, the order of keys should be maintained
	 */
	@Test
	public void putAllMethodOrderTest() {
		OrderedProperties properties = new OrderedProperties();

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		setPropertiesMap(map);
		properties.putAll(map);

		// Compare arrays to validate the order
		Object[] expected = map.entrySet().toArray();
		Object[] actual = properties.entrySet().toArray();

		Assert.assertArrayEquals(expected, actual);
	}

	private Set<String> getExpectedSet() {
		Set<String> expected = new LinkedHashSet<String>();
		expected.add("a");
		expected.add("b");
		expected.add("c");
		expected.add("d");
		expected.add("p");
		expected.add("q");
		expected.add("r");
		expected.add("s");
		expected.add("l");
		expected.add("m");
		expected.add("n");
		expected.add("o");
		return expected;
	}

	private void setPropertiesMap(Map<String, String> map) {
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");
		map.put("p", "5");
		map.put("q", "6"); // test set property method along with put method
		map.put("r", "7");
		map.put("s", "8");
		map.put("l", "9");
		map.put("m", "10");
		map.put("n", "11");
		map.put("o", "12");
	}

	private OrderedProperties getOrderedPropertiesObject() {
		OrderedProperties properties = new OrderedProperties();

		properties.put("a", "1");
		properties.put("b", "2");
		properties.put("c", "3");
		properties.put("d", "4");
		properties.put("p", "5");
		properties.setProperty("q", "6"); // test set property method along with
											// put method
		properties.put("r", "7");
		properties.put("s", "8");
		properties.put("l", "9");
		properties.put("m", "10");
		properties.put("n", "11");
		properties.put("o", "12");
		return properties;
	}
}
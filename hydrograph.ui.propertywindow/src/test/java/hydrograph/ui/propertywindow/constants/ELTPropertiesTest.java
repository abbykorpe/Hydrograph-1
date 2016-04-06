package hydrograph.ui.propertywindow.constants;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * 
 * @author Bitwise
 * Oct 05, 2015
 * 
 */

public class ELTPropertiesTest {
	
	/**
	 * It should provide name property.
	 */
	@Test
	public void itShouldProvideNameProperty(){
		assertEquals("name",ELTProperties.NAME_PROPERTY.propertyName());		
	}
}

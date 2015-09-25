package com.bitwise.app.propertywindow.exceptions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.bitwise.app.propertywindow.exceptions.EmptyComponentPropertiesException;

/**
 * 
 * @author Shrirang S. Kumbhar
 * Sep 04, 2015
 * 
 */

public class EmptyComponentPropertiesExceptionTest {
	@Test
	public void itShouldShowMessage_FoundEmptyPropertyList(){
		
		//Given
		String expectedExceptionMessage = "Found empty property list";
		
		//When
		String actualExceptionMessage=null;
		try{
			throw new EmptyComponentPropertiesException();
		}catch(EmptyComponentPropertiesException e){
			//System.out.println(e.getMessage());
			actualExceptionMessage= e.getMessage();
		}
		
		
		//Then
		assertEquals(expectedExceptionMessage,actualExceptionMessage);
	}
	
}
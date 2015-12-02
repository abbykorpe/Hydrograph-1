package com.bitwise.app.engine.ui.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import com.bitwise.app.engine.exceptions.EngineException;

public class Main {
	
	public static void main(String[] args) {
		File InPutFile= new File("C:\\WorkSpace\\runtime-com.bitwise.app.perspective.product\\UI_VS_TARGET\\test4.xml");
		UIConverterUtil uiConverterUtil=new UIConverterUtil(); 
		
		try {
			uiConverterUtil.convertToUiXML(InPutFile);
			
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | EngineException e) {
			
			e.printStackTrace();
		}
	}
	
	
}

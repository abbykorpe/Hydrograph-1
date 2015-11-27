package com.bitwise.app.engine.parsing;



import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.bitwise.app.engine.ui.repository.ParameterData;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;

public class XMLParser {
 
	public boolean parseXML(File inputFile){
      try {	
        
         SAXParserFactory factory = SAXParserFactory.newInstance();
         SAXParser saxParser = factory.newSAXParser();
         XMLHandler userhandler = new XMLHandler();
         saxParser.parse(inputFile, userhandler);   
         showParameterData();
         return true;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return false;
   }   

  private void showParameterData() {
		
	  for( Entry<String, List<ParameterData>> entry:UIComponentRepo.INSTANCE.getParammeterFactory().entrySet())
	  {
		  for(ParameterData param:entry.getValue())
		  {
			  System.out.println(param);
		  }
	  }
		
	}

public static void main(String[] args){
	    try {	
         File inputFile = new File("C:\\Users\\niting\\git\\Replace_Model\\com.bitwise.app.engine\\Resource\\input.xml");
         SAXParserFactory factory = SAXParserFactory.newInstance();
         SAXParser saxParser = factory.newSAXParser();
         XMLHandler userhandler = new XMLHandler();
         saxParser.parse(inputFile, userhandler);     
      } catch (Exception e) {
         e.printStackTrace();
      }
   }   

}


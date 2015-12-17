package com.bitwise.app.engine.parsing;


import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
/**
 * The class XMLParser
 * 
 * @author Bitwise
 * 
 */
import com.bitwise.app.common.util.LogFactory;
public class XMLParser {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(XMLParser.class);
	
	/**Parses the XML to fetch parameters.
	 * @param inputFile, source XML  
	 * @return true, if XML is successfully parsed.
	 */
	public boolean parseXML(File inputFile){
      try {	
    	  LOGGER.debug("Parsing target XML for separating Parameters");
         SAXParserFactory factory = SAXParserFactory.newInstance();
         SAXParser saxParser = factory.newSAXParser();
         XMLHandler xmlhandler = new XMLHandler();
         saxParser.parse(inputFile, xmlhandler);  
         return true;
      } catch (Exception exception) {
         LOGGER.error("Parsing failed...",exception);
      }
      return false;
   }  
	
}


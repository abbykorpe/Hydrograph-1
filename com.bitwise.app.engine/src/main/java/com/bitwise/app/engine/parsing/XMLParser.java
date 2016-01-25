package com.bitwise.app.engine.parsing;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.xml.sax.SAXException;

import com.bitwise.app.logging.factory.LogFactory;
public class XMLParser {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(XMLParser.class);
	
	/**Parses the XML to fetch parameters.
	 * @param inputFile, source XML  
	 * @return true, if XML is successfully parsed.
	 * @throws Exception 
	 */
	public boolean parseXML(File inputFile) throws ParserConfigurationException, SAXException, IOException{
      LOGGER.debug("Parsing target XML for separating Parameters");
         SAXParserFactory factory = SAXParserFactory.newInstance();
         SAXParser saxParser;
		try {
			saxParser = factory.newSAXParser();
			XMLHandler xmlhandler = new XMLHandler();
			saxParser.parse(inputFile, xmlhandler);
			return true;
		} catch (ParserConfigurationException | SAXException | IOException exception) {
			 LOGGER.error("Parsing failed...",exception);
			throw exception; 
		}
     
   }  
}


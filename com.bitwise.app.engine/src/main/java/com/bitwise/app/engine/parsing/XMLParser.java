package com.bitwise.app.engine.parsing;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.repository.ParameterData;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;
public class XMLParser {
	private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(XMLParser.class);
	public boolean parseXML(File inputFile){
      try {	
    	  LOGGER.debug("Parsing target XML for seprating Parameters");
         SAXParserFactory factory = SAXParserFactory.newInstance();
         SAXParser saxParser = factory.newSAXParser();
         XMLHandler xmlhandler = new XMLHandler();
         saxParser.parse(inputFile, xmlhandler);  
//         storeParameterData(parameterFile);
         return true;
      } catch (Exception exception) {
         LOGGER.error("Parsing failed...",exception);
      }
      return false;
   }  
	
	private void storeParameterData(IFile parameterFile) throws IOException, CoreException {
		LOGGER.debug("Creating Parameter(i.e *properties) File at {}",parameterFile.getFullPath().toString());
		String emptyValue="";
		Properties properties = new Properties(); 
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		for( Entry<String, List<ParameterData>> entry:UIComponentRepo.INSTANCE.getParammeterFactory().entrySet())
		  {
			  for(ParameterData param:entry.getValue())
			  {
				  properties.setProperty(param.getParameterName().replace("@{","").replace("}", ""), emptyValue);
			  }
		  }
		properties.store(out, null);
		parameterFile.create(new ByteArrayInputStream(out.toByteArray()),true, null);
		}

}


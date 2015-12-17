package com.bitwise.app.engine.parsing;



import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bitwise.app.common.util.LogFactory;
import com.bitwise.app.engine.ui.repository.ParameterData;
import com.bitwise.app.engine.ui.repository.UIComponentRepo;

public class XMLHandler extends DefaultHandler {

  private static final Logger LOGGER = LogFactory.INSTANCE.getLogger(XMLParser.class);
  private String currentComponent;
  private static final String ID="id";
  private static final String VALUE="value";
  private static final String REGEX="[\\@]{1}[\\{]{1}[\\w]*[\\}]{1}";
  
  
   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes)throws SAXException {
	   LOGGER.debug("Parsing - start elements {}",new Object[]{uri,localName,qName,attributes});
	   List<ParameterData> tempParammeterList;
	   Matcher matcher =null;
	   if(isComponent(qName))
	   {
		   currentComponent=attributes.getValue(ID);
		   List<ParameterData> emptyList=new ArrayList<>();
		   UIComponentRepo.INSTANCE.getParammeterFactory().put(currentComponent, emptyList);
	   }
	   if(attributes.getValue(VALUE)!=null){
	   matcher = Pattern.compile(REGEX).matcher(attributes.getValue(VALUE));
	   if(matcher.matches()){
		   tempParammeterList=UIComponentRepo.INSTANCE.getParammeterFactory().get(currentComponent);
		   if(tempParammeterList!=null ) {
			   tempParammeterList.add(new ParameterData(qName, attributes.getValue(VALUE)));
		   	}
		  }
	   }
   }

   private boolean isComponent(String qName) {
	
	   for(ComponentTypes componentType:ComponentTypes.values())
	   {
		   if(componentType.value().equals(qName))
			   return true;		   
	   }
	return false;
}

@Override
   public void endElement(String uri, 
   String localName, String qName) throws SAXException {

   }

   @Override
   public void characters(char ch[], 
      int start, int length) throws SAXException {

   }
}
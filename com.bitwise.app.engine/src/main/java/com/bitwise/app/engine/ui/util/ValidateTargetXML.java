package com.bitwise.app.engine.ui.util;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class ValidateTargetXML {

static	String XSDPATH="Resource/newxmlschema/main/main.xsd";

static String XMLPATH="C:\\Users\\shrirangk\\Desktop\\BHSUIWorkSpace\\runtime-com.bitwise.app.perspective.product\\as\\Job_1.xml";
				// replace above path with generated XML.
	
	
	public static void main(String[] args) {
		try {
			boolean result=validateXMLSchema(XSDPATH,XMLPATH);
			System.out.println(result);
		
		} catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	public static  boolean validateXMLSchema(String xsdPath, String xmlPath) throws Exception{
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File(xsdPath));
       Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new File(xmlPath)));
        return true;
	}
}
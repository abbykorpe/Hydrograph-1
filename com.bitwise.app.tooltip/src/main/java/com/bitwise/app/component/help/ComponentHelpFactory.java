package com.bitwise.app.component.help;

public class ComponentHelpFactory {
	 public ComponentHelp getComponent(String componentName){
	      if(componentName == null){
	         return null;
	      }		
	      if(componentName.equalsIgnoreCase("IFDelimited")){
	         return new IFDelimited();
	         
	      } else if(componentName.equalsIgnoreCase("IFixedWidth")){
	         return new IFixedWidth();
	         
	      } 
	      return null;
	   }
}

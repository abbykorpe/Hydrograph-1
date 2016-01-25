package com.bitwise.app.component.help;


/**
 *Creates Component help factory
 * @author Bitwise
 *
 */
public class ComponentHelpFactory {
	/**
	 * returns the object of class that corresponds to matching component name otherwise 
	 * return null
     * @param componentName
	 * @return
	 */
	 public ComponentHelp getComponent(String componentName){
	      if(componentName == null){
	         return null;
	      }		
	      if(componentName.equalsIgnoreCase(Constants.IFDELIMITED)){
	         return new IFDelimited();
	         
	      } else if(componentName.equalsIgnoreCase(Constants.IFIXEDWIDTH)){
	         return new IFixedWidth();
	         
	      } 
	      return null;
	   }
}

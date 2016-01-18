package com.bitwise.app.common.datastructure.property;



import org.slf4j.Logger;

import com.bitwise.app.cloneableinterface.IDataStructure;
import com.bitwise.app.common.util.LogFactory;



public class FilterProperties implements IDataStructure {
	private static final Logger logger = LogFactory.INSTANCE.getLogger(FilterProperties.class);
	String propertyname;
	public String getPropertyname() {
		return propertyname;
	}

	public void setPropertyname(String propertyname) {
		this.propertyname = propertyname;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propertyname == null) ? 0 : propertyname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilterProperties other = (FilterProperties) obj;
		if (propertyname == null) {
			if (other.propertyname != null)
				return false;
		} else if (!propertyname.equals(other.propertyname))
			return false;
		return true;
	}

	@Override
	public FilterProperties clone(){
		FilterProperties filterProperties=null;
	    try {
	    	filterProperties=this.getClass().newInstance();
		} catch (Exception e) {
			logger.debug("Unable to instantiate cloning object",e);
		}
		filterProperties.setPropertyname(getPropertyname());
		return filterProperties;
	};
	@Override
	public String toString() {
		return "FilterProperties [propertyname=" + propertyname + "]";
	}
	
	
}

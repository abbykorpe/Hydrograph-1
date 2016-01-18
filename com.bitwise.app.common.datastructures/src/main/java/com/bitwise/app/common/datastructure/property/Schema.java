package com.bitwise.app.common.datastructure.property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.bitwise.app.cloneableinterface.CloneObject;
import com.bitwise.app.common.util.LogFactory;


/**
 * This Schema class contains accessors methods.
 * 
 * @author Bitwise
 */

public class Schema implements CloneObject{
	private static final Logger logger = LogFactory.INSTANCE.getLogger(Schema.class);
	private String externalSchemaPath;
	private Boolean isExternal;
	private List<GridRow> gridRow;
	private List<GridRow> clonedGridRow;
   
	public String getExternalSchemaPath() {
		return externalSchemaPath;
	}

	public void setExternalSchemaPath(String externalSchemaPath) {
		this.externalSchemaPath = externalSchemaPath;
	}

	public Boolean getIsExternal() {
		return isExternal;
	}

	public void setIsExternal(Boolean isExternalPath) {
		this.isExternal = isExternalPath;
	}

	public List<GridRow> getGridRow() {
		return gridRow;
	}

	public void setGridRow(List<GridRow> gridRow) {
		this.gridRow = gridRow;
	}
     
	@Override
	public Schema clone()
         { 
		     Schema schema=null;	
		     clonedGridRow=new ArrayList<>();
        	 try {
				schema=this.getClass().newInstance();
			} catch ( Exception e) {
				logger.debug("Unable to instantiate cloning object",e);
			}
        	
        	 for(int i=0;i<gridRow.size();i++)
        	 {
             if(gridRow.get(i)instanceof FixedWidthGridRow )
             clonedGridRow.add(((FixedWidthGridRow)gridRow.get(i)).copy()); 
             else
             clonedGridRow.add(gridRow.get(i).clone());
        	 
        	 }		 
        	 schema.setExternalSchemaPath(getExternalSchemaPath());
        	 schema.setGridRow(clonedGridRow);
        	 schema.setIsExternal(getIsExternal());
        	 return schema;
        	 
         }
    
	@Override
	public String toString() {
		return "ExternalSchema [externalSchemaPath=" + externalSchemaPath
				+ ", isExternalPath=" + isExternal + ", gridRow=" + gridRow
				+ "]";
	}
	
}

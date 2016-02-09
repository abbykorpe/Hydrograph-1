package com.bitwise.app.common.datastructure.property;


import java.util.ArrayList;
import java.util.List;
import com.bitwise.app.cloneableinterface.IDataStructure;



/**
 * This Schema class contains accessors methods.
 * 
 * @author Bitwise
 */

public class Schema implements IDataStructure{
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
		     Schema schema=new Schema();	
		     clonedGridRow=new ArrayList<>();
		     if(gridRow!=null){
        	 for(int i=0;i<gridRow.size();i++)
        	 {
             if(gridRow.get(i)instanceof FixedWidthGridRow )
             clonedGridRow.add(((FixedWidthGridRow)gridRow.get(i)).copy()); 
             else if(gridRow.get(i)!=null)
             clonedGridRow.add(((SchemaGrid)gridRow.get(i)).copy());
        	 
        	 }	}	 
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

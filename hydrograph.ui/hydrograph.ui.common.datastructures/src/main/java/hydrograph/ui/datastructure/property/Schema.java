/********************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

 
package hydrograph.ui.datastructure.property;


import hydrograph.ui.common.cloneableinterface.IDataStructure;

import java.util.ArrayList;
import java.util.List;



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
		if (gridRow != null) {
			for (int i = 0; i < gridRow.size(); i++) {
				if (gridRow.get(i) instanceof FixedWidthGridRow)
					clonedGridRow.add(((FixedWidthGridRow) gridRow.get(i)).copy());
				else if (gridRow.get(i) instanceof MixedSchemeGridRow)
					clonedGridRow.add(((MixedSchemeGridRow) gridRow.get(i)).copy());
				else if (gridRow.get(i) != null)
					clonedGridRow.add(((BasicSchemaGridRow) gridRow.get(i)).copy());


			}
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
